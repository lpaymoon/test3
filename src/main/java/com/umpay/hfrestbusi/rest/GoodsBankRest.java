package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.GoodsBankCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  GoodsbankRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  商品银行信息
 * @see        :                        
 * ************************************************/   
public class GoodsBankRest extends BaseRest {
	
	
	/** ********************************************
	 * method name   : doListService 
	 * modified      : panxingwu ,  2011-11-4
	 * description   : 查询商品银行信息列表
	 * ********************************************/     
	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		this.logInfo("查询商品银行列表%s", urlargs);
				
		String merId = urlargs.get(HFBusiDict.MERID);
        String goodsId = urlargs.get(HFBusiDict.GOODSID);
        //设置查询条件的参数
        Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.MERID+".string", merId);
		map.put(HFBusiDict.GOODSID+".string", goodsId);
		
		List<Map<String, Object>> rs = (List<Map<String, Object>>) CacheUtil.getObject("goodsBank", merId+"-"+goodsId, map, new GoodsBankCacheHelper(dal));
		
		if(rs==null||rs.size()==0){
			logInfo("查询商品银行列表信息列失败，信息不存在:%s %s", merId,goodsId);
			return "86801017";
		}
		out.put(HFBusiDict.GOODSBANKS,rs);
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2011-11-4
	 * description   : 查询单个商品银行信息
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		this.logInfo("查询单条商品银行信息%s", urlargs);
		
		Map<String, String> map = new HashMap<String, String>();
		Map<String,Object> goodsBank = new HashMap<String,Object>();
		
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String bankId = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		
		map.put(HFBusiDict.MERID+".string", merId);
		map.put(HFBusiDict.GOODSID+".string", goodsId);
		
		List<Map<String, Object>> result = (List<Map<String, Object>>) CacheUtil.getObject("goodsBank", merId+"-"+goodsId, map, new GoodsBankCacheHelper(dal));
		for (Iterator<Map<String, Object>> iterator = result.iterator(); iterator.hasNext();) {
			Map<String, Object> m = (Map<String, Object>) iterator.next();
			if(m==null) break;
			if(bankId.equals(m.get(HFBusiDict.BANKID).toString())){
				goodsBank.putAll(m);
			}
		}
		//Map<String,Object> rs = dal.get("GOODSBANKREST.findBankInf", map);
		if(goodsBank==null||goodsBank.size()==0){
			logInfo("查询单条商品银行信息失败，信息不存在：%s %s %s", merId,goodsId,bankId);
			return "86801017";
		}
		
		out.putAll(goodsBank);
		logInfo("查询单条商品银行信息成功%s", goodsBank);
		return BusiConst.SUCCESS;
	}
	
	

}
