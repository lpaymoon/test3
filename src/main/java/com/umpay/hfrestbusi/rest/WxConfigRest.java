package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.WxClientConfigCache;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;

/** 
* @ClassName	: WxConfigRest 
* @Description	: TODO
* @author		： panxingwu
* @date			： 2013-5-24 上午9:37:33 
*/
public class WxConfigRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String clientName = StringUtil.trim(urlargs.get("clientName"));// 客户端名称
		String clientType = StringUtil.trim(urlargs.get("clientType"));//客户端类型 0:富客户端  1：瘦客户端
		if("".equals(clientName)){
			clientName="RemoteBilling";//默认值
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("clientName.string", clientName);
		map.put("clientType.string", clientType);
		Map<String, Object> rsConf = null; 
		rsConf = (Map<String, Object>) CacheUtil.getObject("wxConf",clientName+clientType, map, new WxClientConfigCache(dal));
		if(rsConf==null||rsConf.size()==0){
			logInfo("客户端配置信息不存在！");
			return "86801114";
		}
		logInfo("查询客户端信息成功:%s", rsConf);
		out.putAll(rsConf);
		return BusiConst.SUCCESS;
	}

}
