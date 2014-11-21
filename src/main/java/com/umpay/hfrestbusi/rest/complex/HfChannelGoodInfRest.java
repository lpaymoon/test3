package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.ChnlInfCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;


/** ******************  类说明  *********************
 * class       :  HfChannelGoodInfRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  渠道商品信息
 * @see        :                        
 * ************************************************/   
public class HfChannelGoodInfRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String channelid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		Map<String,String> map = new HashMap<String,String>();		
		//验证渠道信息
		map.put(HFBusiDict.CHANNELID, channelid);
		logInfo("调用基础资源获取渠道信息");
		Map<String, Object> chnlInf = LocalUtil.doGetService("/hfrestbusi/channelInfRest/"+rpid+"/"+channelid+".xml", map);
		String retCode = String.valueOf(chnlInf.get(HFBusiDict.RETCODE));
		if(!"0000".equals(retCode)){
			logInfo("获取渠道信息失败");
			return retCode;
		}
		logInfo("验证渠道信息....");
		int chnlstate = (Integer) chnlInf.get(HFBusiDict.STATE);
		if(chnlstate==4||chnlstate!=2){
			logInfo("渠道已禁用");
			return "86801080";
		}
		logInfo("渠道信息验证通过");
		//检查渠道商品信息
		map.put(HFBusiDict.CHANNELID+".string", channelid);
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);	
		Map<String, Object> chnlgoodsRs = dal.get("psql_chnlcheck.getchnlgoodinf", map);
		if(chnlgoodsRs ==null||chnlgoodsRs.size()==0){
			logInfo("渠道商品信息不存在");
			return "86801081";
		}
		int chnlgoodsstate = (Integer) chnlgoodsRs.get(HFBusiDict.STATE);
		logInfo("查询的渠道商品信息为:%s",chnlgoodsRs);
		if(chnlgoodsstate==4||chnlgoodsstate!=2){
			logInfo("渠道商品已禁用");
			return "86801082";
		}
		//查询商品信息
		Map<String,String> goodsInfMap = new HashMap<String,String>();
		goodsInfMap.put(HFBusiDict.MERID, merid);
		goodsInfMap.put(HFBusiDict.GOODSID, goodsid);
		Map<String,Object> goodsInfRs = LocalUtil.doGetService("/hfrestbusi/goodsinf/"+rpid+"/"+merid+"-"+goodsid+".xml", goodsInfMap);
		retCode = (String) goodsInfRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商品信息失败,返回码：%s", retCode);
			return retCode;
		}
		
		out.putAll(goodsInfRs);
		return BusiConst.SUCCESS;
	}

}
