package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfQueryUserLtdRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  查询用户月交易累计信息
 * @see        :                        
 * ************************************************/   
public class HfQueryUserLtdRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("执行用户交易累计信息查询,参数：%s", urlargs);
		String retCode = BusiConst.SYS_ERROR;
		
		Map<String,String> xeMap = new HashMap<String,String>();
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		xeMap.put(HFBusiDict.MOBILEID, mobileId);
		
		//获取省网累计交易信息
		Map<String,Object> xeUserInf = LocalUtil.doGetService("/hfrestbusi/xeuserltd/"+rpid+"/"+mobileId+".xml", xeMap);
		retCode = xeUserInf.get(HFBusiDict.RETCODE).toString();
		
		if(BusiConst.SUCCESS.equals(retCode)){
			String xeMthPayed=StringUtil.trim(String.valueOf(xeUserInf.get(HFBusiDict.MONTHPAYED)));
			String xeDaypayed=StringUtil.trim(String.valueOf(xeUserInf.get(HFBusiDict.DAYPAYED)));
			String xeModtime=StringUtil.trim(xeUserInf.get(HFBusiDict.MODTIME).toString());
			out.put("xemonthpayed",xeMthPayed);
			out.put("xedaypayed",xeDaypayed);
			out.put("xemodtime",xeModtime);
		} else if("86801058".equals(retCode)){
			logInfo("用户小额交易累计信息不存在,mobileid:%s,retCode%s", mobileId,retCode);
			out.put("xemonthpayed","0");
			out.put("xedaypayed","0");
		}else {
			logInfo("获取用户小额交易累计信息失败,mobileid:%s,retCode%s", mobileId,retCode);
			return retCode;		
		}
	
		
		Map<String,String> mwMap = new HashMap<String,String>();
		mwMap.put(HFBusiDict.MOBILEID, mobileId);
		
		//获取全网交易累计信息
		Map<String,Object> MwUserInf = LocalUtil.doGetService("/hfrestbusi/mwuserltd/"+rpid+"/"+mobileId+".xml", mwMap);
		retCode = MwUserInf.get(HFBusiDict.RETCODE).toString();
		if(BusiConst.SUCCESS.equals(retCode)){
			out.putAll(MwUserInf);
		}else if("86801097".equals(retCode)){
			logInfo("用户全网交易累计信息不存在：%s %s", xeUserInf,MwUserInf);
			out.put("monthpayed","0");
			out.put("daypayed","0");

		}else{
			logInfo("获取用户全网交易累计信息失败：%s %s", xeUserInf,MwUserInf);
			return retCode;
		}
		logInfo("查询成功,用户信息：%s %s", xeUserInf,MwUserInf);
		return BusiConst.SUCCESS;
	}
	
}
