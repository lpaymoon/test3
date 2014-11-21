package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.StringUtil;

/** 
* @ClassName	: WxUserReplyRest 
* @Description	: 保存客户端用户反馈信息
* @author		： panxingwu
* @date			： 2013-5-6 下午5:19:57 
*/
public class WxUserReplyRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		String IMEI = StringUtil.trim(urlargs.get(HFBusiDict.IMEI));
		String contacInfo =  StringUtil.trim(urlargs.get(HFBusiDict.CONTACINFO));
		String replyTime =  StringUtil.trim(urlargs.get(HFBusiDict.REPLYTIME));
		String replyInfo =  StringUtil.trim(urlargs.get(HFBusiDict.REPLYINFO));
		String clientName = StringUtil.trim(urlargs.get("clientName"));
		String platType = StringUtil.trim(urlargs.get(HFBusiDict.PLATTYPE));
		String versionCode = StringUtil.trim(urlargs.get(HFBusiDict.VERSION));
		String replyType = StringUtil.trim(urlargs.get("replyType")); 
		if("".equals(replyType)) replyType="1";
		
		Map <String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.IMEI+".string", IMEI);
		map.put(HFBusiDict.CONTACINFO+".string", contacInfo);
		map.put(HFBusiDict.REPLYTIME+".string", replyTime);
		map.put(HFBusiDict.REPLYINFO+".string", replyInfo);
		map.put("clientName.string", clientName);
		map.put(HFBusiDict.PLATTYPE+".int", platType);
		map.put("versionCode.string", versionCode);
		map.put("replyType.int", replyType);
		try{
			dal.insert("psql_wxuserreply.addInfo", map);
		}catch(DBPKConflictException e){
			logInfo("用户反馈信息已经存在");
			return "86801114";
		}
		logInfo("保存用户反馈信息成功");
		return BusiConst.SUCCESS;
	}

}
