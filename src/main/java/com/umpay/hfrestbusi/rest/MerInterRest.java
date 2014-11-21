package com.umpay.hfrestbusi.rest;

/** *****************  JAVA头文件说明  ****************
 * file name  :  MerInterRest.java
 * owner      :  WuEnzhen
 * copyright  :  UMPAY
 * description:  
 * modified   :  2011-12-28
 * *************************************************/ 

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.MerInterCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  MerInterRest
 * @author     :  WuEnzhen
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class MerInterRest extends BaseRest{
	
	/** ********************************************
	 * method name   : doListService 
	 * modified      : WuEnzhen ,  2012-01-10
	 * description   : 查询商户接口地址信息列表
	 * ********************************************/     
	public String doListService(Map<String, String> urlargs) throws Exception {
		this.logInfo("查询商户接口地址列表%s", urlargs);
				
		String merid = urlargs.get(HFBusiDict.MERID);
        //设置查询条件的参数
        Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.MERID+".string", merid);
		
		List<Map<String, Object>> rs = (List<Map<String, Object>>) CacheUtil.getObject("merInter", merid, map, new MerInterCacheHelper(dal));
		
		if(rs==null||rs.size()==0){
			logInfo("查询商户接口地址列表信息列失败，信息不存在:%s", merid);
			return "86801040";
		}
		out.put(HFBusiDict.MERINTERS,rs);
		logInfo("查询商户接口地址列表成功%s", rs);
		return BusiConst.SUCCESS;
	}
	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : WuEnzhen ,  2012-01-10
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map, java.lang.String)
	 * ********************************************/     
	public String doShowService(Map<String, String> urlargs, String id) throws Exception{
		// LOG输出上下文
		this.logInfo("查询商户接口地址信息,参数为：%s", urlargs);
		// 装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		Map<String,Object> merInter = new HashMap<String,Object>();
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String infuncode = StringUtil.trim(urlargs.get(HFBusiDict.INFUNCODE));
		String inversion = StringUtil.trim(urlargs.get(HFBusiDict.INVERSION));
		args.put(HFBusiDict.MERID+".string", merid);
		
		// 商户信息资源获取
		this.logInfo("开始获取MerInter资源:%s", args);
		List<Map<String, Object>> rsMerInter = (List<Map<String, Object>>) CacheUtil.getObject("merInter", merid, args, new MerInterCacheHelper(dal));
		for (Iterator<Map<String, Object>> iterator = rsMerInter.iterator(); iterator.hasNext();) {
			Map<String, Object> m = (Map<String, Object>) iterator.next();
			if(m==null) break;
			if(infuncode.equals(m.get(HFBusiDict.INFUNCODE).toString())&&inversion.equals(m.get(HFBusiDict.INVERSION).toString())){
				merInter.putAll(m);
			}
		}
		
		// 检查结果，决定返回码
	    this.logInfo("开始校验结果......%s", "");
	    
		if (merInter==null||merInter.size()==0) {
			this.logInfo("查询单条商户接口地址信息失败，信息不存在: %s",merid,infuncode,inversion);
			return "86801040";
		}
		//商品状态未开通
		Integer state = (Integer) merInter.get(HFBusiDict.STATE);		//获取结果集中的STATE值
		if (state!=2) {
			this.logInfo("商品通知接口未开通:%s",state);
			return "86801041";
		}else{
			out.putAll(merInter);
			this.logInfo("查询单条商户接口地址信息成功:%s ", merInter);
			return BusiConst.SUCCESS;
		}
	}
}
