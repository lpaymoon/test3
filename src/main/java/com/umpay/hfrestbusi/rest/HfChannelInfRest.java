package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.ChnlInfCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;


/** ******************  类说明  *********************
 * class       :  ChannelInfRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  渠道信息
 * @see        :                        
 * ************************************************/   
public class HfChannelInfRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String channelid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.CHANNELID+".string", channelid);
		Map<String, Object> chnlInf = (Map<String, Object>) CacheUtil.getObject("chnlinf", channelid, map,new ChnlInfCacheHelper(dal));
		if(chnlInf==null||map.size()==0){
			logInfo("渠道信息不存在");
			return "86801079";
		}
		out.putAll(chnlInf);
		return BusiConst.SUCCESS;
	}

}
