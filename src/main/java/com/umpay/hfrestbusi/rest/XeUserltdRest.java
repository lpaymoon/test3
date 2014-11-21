package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;


/** ******************  类说明  *********************
 * class       :  XeUserltdRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  小额交易累计信息
 * @see        :                        
 * ************************************************/   
public class XeUserltdRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		logInfo("查询小额交易累计信息,mobileid=%s", mobileid);
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		Map<String,Object> rsMap = new HashMap<String,Object>();
		rsMap = dal.get("psql_XEuserltd.getXEuserltd", map);
		if(rsMap==null||rsMap.size()==0){
			logInfo("该手机的小额交易累计信息不存在");
			return "86801058";
		}
		out.putAll(rsMap);
		logInfo("查询小额交易累计信息成功,查询结果为:%s",rsMap);
		return BusiConst.SUCCESS;
	}
	
}
