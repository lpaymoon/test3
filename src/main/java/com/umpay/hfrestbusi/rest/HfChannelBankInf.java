package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;


/** ******************  类说明  *********************
 * class       :  HfChannelBankInf
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  渠道银行信息
 * @see        :                        
 * ************************************************/   
public class HfChannelBankInf extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("获取渠道银行信息,参数为:%s",urlargs);
		String bankid = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		String channelid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.BANKID+".string",bankid);
		map.put(HFBusiDict.CHANNELID+".string", channelid);
		Map<String,Object> rs = dal.get("psql_chnlbank.getBank", map);
		if(rs==null||rs.size()==0){
			logInfo("渠道银行信息不存在");
			return "86801083";
		}
		int state = (Integer)rs.get(HFBusiDict.STATE);
		if(state==4||state!=2){
			logInfo("渠道银行已经禁用");
			return "86801084";
		}
		return BusiConst.SUCCESS;
	}

}
