/**
 * 
 */
package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;

/** ******************  类说明  *********************
 * class       :  BJqqtblacklistInfRest
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class BJVGOPUserInfRest extends BaseRest {
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : zhuoyangyang ,  2013-11-11
	 * description   : 北京用户开机信息查询
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("北京用户停开机状态,参数:%s", urlargs);
		String mobileId = urlargs.get(HFBusiDict.MOBILEID);		
		Map<String,String> args = new HashMap<String,String>();		
		args.put(HFBusiDict.MOBILEID+".string",mobileId);
		Map<String, Object> rs = null;
		rs = dal.get("psql_VGOPUSERREST.getVGOPUser", args);
		if(rs==null||rs.size()==0){
			logInfo("该手机号用户开机信息不存在");
			return "86801031";
		}
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}

}
