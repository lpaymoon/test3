package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.GoodsInfCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.CheckReqUtil;

/**
 * ******************  类说明  *********************
 * class       :  GoodInfRest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  商户信息资源
 * @see        :                        
 * ***********************************************
 */
public class GoodInfRest extends BaseRest {

	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : LiuJiLong ,  2011-11-7
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id) throws Exception {
		logInfo("查询商品信息，参数:%s", urlargs);
		
		// 装载初始化参数
		String merid = urlargs.get(HFBusiDict.MERID);		//商户ID
		String goodsid = urlargs.get(HFBusiDict.GOODSID);	//商品ID
		String cachekey = merid + "-" + goodsid;	//缓存ID
		
		Map<String, String> args = new HashMap<String, String>();	//传入参数
		args.put(HFBusiDict.MERID+".string", merid);
		args.put(HFBusiDict.GOODSID+".string", goodsid);
		
		//初始化结果集
		Map<String, Object> rs = null;
			
		// 获取GOODSINF资源
		this.logInfo("开始获取GOODS资源......", "");
		rs = (Map<String, Object>) CacheUtil.getObject("goods",cachekey, args,new GoodsInfCacheHelper(dal));
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//商品不存在
		if (rs == null || rs.get(HFBusiDict.GOODSNAME) == null) {
			this.logInfo("商品不存在:%s",cachekey);
			return "86801013";
		} 
		//商品状态未开通
		Integer state = (Integer) rs.get(HFBusiDict.STATE);		//获取结果集中的STATE值
		if (state!=2) {
			this.logInfo("商品未开通:%s",cachekey);
			return "86801014";
		}
		//商品为包月商品，但是没有包月商品信息时返回异常，当merinf.servtype=3时mongoods.SERVMONTH,CONMODE,INTERVAL必须存在
//		String servtype = rs.get(HFBusiDict.SERVTYPE).toString();	//获取结果集中的商品类型
//		if("3".equals(servtype)){
//			if(!CheckReqUtil.checkMapElement(rs,HFBusiDict.SERVMONTH,HFBusiDict.CONMODE,HFBusiDict.INTERVAL)){
//				this.logInfo("包月商品未配置相应的包月商品属性:%s",cachekey);
//				return "86801023";
//			}					
//		}
//			
//		//当没有查询到商品控制类型时，返回商品为非控制类型0
//		Integer isControl = (Integer)rs.get(HFBusiDict.ISCONTROL);
//		if(isControl==null){
//			rs.put(HFBusiDict.ISCONTROL, new Integer(0));
//			this.logInfo("商品%s控制类型不存在，设置为非控类商品",cachekey);
//		}
		
		this.logInfo("商品查询成功， %s",  cachekey);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}
}
