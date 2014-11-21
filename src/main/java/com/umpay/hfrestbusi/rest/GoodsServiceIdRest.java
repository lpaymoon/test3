package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.GoodsServiceIdsCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  GoodsServiceIdRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  商品和计费代码的关系
 * @see        :                        
 * ************************************************/   
public class GoodsServiceIdRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs,String id) throws Exception {
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String gateid = StringUtil.trim(urlargs.get(HFBusiDict.GATEID));
		String feeType="";
		long amount=0;
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);
		map.put(HFBusiDict.GATEID+".string", gateid);
		logInfo("查询商品计费代码:%s", map);
		//初始化结果集
		Map<String, Object> rs =  (Map<String, Object>)CacheUtil.getObject("goodsServiceids", merid+"-"+goodsid+"-"+gateid, map, new GoodsServiceIdsCacheHelper(dal));
		if(rs==null||rs.size()==0){
			logInfo("商品计费代码不存在!");
			return "86802012";
		}
		Integer state = (Integer) rs.get(HFBusiDict.STATE);
		if(state!=2){
			logInfo("商品计费代码未开通!");
			return "86802007";
		}
		logInfo("查询商品计费代码成功");
		out.putAll(rs);
		out.put(HFBusiDict.SERVICEID, rs.get(HFBusiDict.SERVICEID));
		return BusiConst.SUCCESS;
	}

}
