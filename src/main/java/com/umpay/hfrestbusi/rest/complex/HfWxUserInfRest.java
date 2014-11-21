package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/**
 * @author panxingwu
 * 查询无线用户信息
 */
public class HfWxUserInfRest extends BaseRest{

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String IMEI = StringUtil.trim(urlargs.get(HFBusiDict.IMEI));
		String platType = StringUtil.trim(urlargs.get(HFBusiDict.PLATTYPE));
		Map<String,String> map = new HashMap<String, String>();
		map.put(HFBusiDict.IMEI, IMEI);
		map.put(HFBusiDict.PLATTYPE, platType);
		logInfo("调用基础资源查询无线用户信息表");
		Map<String, Object> userInf = LocalUtil.doGetService("/hfrestbusi/wxclientuser/"+rpid+"/"+IMEI+"-"+platType+".xml", map);
		String retCode = String.valueOf(userInf.get(HFBusiDict.RETCODE));
		if(!"0000".equals(retCode)){
			logInfo("无线用户信息不存在");
			return retCode;
		}
		logInfo("查询号段信息");
		String mobileid = StringUtil.trim(String.valueOf(userInf.get(HFBusiDict.MOBILEID)));
		if("".equals(mobileid)){
			logInfo("当前无该无线用户手机号码信息");
			return "86801028";
		}
		Map<String,String> segMap = new HashMap<String,String>();
		segMap.put(HFBusiDict.MOBILEID, mobileid.substring(0, 7));
		Map<String, Object> segInf = LocalUtil.doGetService("/hfrestbusi/seginf/"+rpid+"/"+mobileid+".xml", segMap);
		String retCode1 = String.valueOf(segInf.get(HFBusiDict.RETCODE));
		if(!"0000".equals(retCode1)){
			logInfo("号段信息不存在");
			return retCode1;
		}
		logInfo("获取无线用户信息成功");
		segInf.put(HFBusiDict.MOBILEID, mobileid);//返回整个号码，而不是返回前七位号段
		out.putAll(userInf);
		out.putAll(segInf);
		return BusiConst.SUCCESS;
	}

}
