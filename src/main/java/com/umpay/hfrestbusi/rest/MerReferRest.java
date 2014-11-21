package com.umpay.hfrestbusi.rest;

/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInterRest.java
 * owner      :  WuEnzhen
 * copyright  :  UMPAY
 * description:  
 * modified   :  2011-12-28
 * *************************************************/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.MerReferCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;

/**
 * ****************** 类说明 ********************* class : MerReferRest
 * 
 * @author : WuEnzhen
 * @version : 1.0 description :
 * @see :
 * ************************************************/

public class MerReferRest extends BaseRest {

	/**
	 * ******************************************** method name : doListService
	 * modified : WuEnzhen , 2012-02-15 description : 查询商户渠道报备基本信息列表
	 * ********************************************/
	public String doListService(Map<String, String> urlargs) throws Exception {
		this.logInfo("查询商户渠道报备列表%s", urlargs);
		String merid = urlargs.get(HFBusiDict.MERID);
		String goodsid = urlargs.get(HFBusiDict.GOODSID);
		String cachekey = merid + "-" + goodsid; // 缓存ID
		// 设置查询条件的参数
		Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);
		List<Map<String, Object>> rs = (List<Map<String, Object>>) CacheUtil.getObject("merRefer", cachekey, map, new MerReferCacheHelper(dal));
		if (rs == null || rs.size() == 0) {
			logInfo("查询商户渠道报备基本信息失败，信息不存在:%s", cachekey);
			return "86801105";
		}
		out.put(HFBusiDict.MERREFER,rs);
		logInfo("查询商户渠道报备列表成功%s", rs);
		return BusiConst.SUCCESS;
	}
}