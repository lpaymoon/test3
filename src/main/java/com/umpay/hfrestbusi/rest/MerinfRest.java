package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.MerInfCacheHelper;
import com.umpay.hfrestbusi.cache.MerIntersCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;

/**
 * ******************  类说明  *********************
 * class       :  MerinfRest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  商户资源
 * @see        :                        
 * ***********************************************
 */
public class MerinfRest extends BaseRest {

	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : LiuJiLong ,  2011-11-7
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id) throws Exception{
		// LOG输出上下文
		this.logInfo("查询商户信息,参数为：%s", urlargs);
		// 装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		args.put(HFBusiDict.MERID+".string", merid);
		
		// 商户信息资源获取
		this.logInfo("开始获取MERINF资源......%s", "");
		Map<String, Object> rsMer = null; 
		rsMer = (Map<String, Object>) CacheUtil.getObject("mer", urlargs.get(HFBusiDict.MERID), args, new MerInfCacheHelper(dal));
		
		//重新初始化参数
		args.clear();
		args.put(HFBusiDict.MERID+".string", urlargs.get(HFBusiDict.MERID));
		
		//商户接口资源获取
		this.logInfo("正在获取inters资源......%s", "");
	    List<Map<String, Object>> rsMerInter = null;
	    rsMerInter = (List<Map<String, Object>>) CacheUtil.getObject("merInter", urlargs.get(HFBusiDict.MERID), args, new MerIntersCacheHelper(dal));
		
		// 检查结果，决定返回码
	    this.logInfo("开始校验结果......%s", "");
	    
	    //只有商户存在且已开通的情况(state为2)下才返回查询成功
		if (rsMer==null||rsMer.get(HFBusiDict.MERNAME) == null||rsMer.get(HFBusiDict.VERSION)==null) {
			this.logInfo("商户不存在: %s",merid);
			return "86801011";
		}else if (!rsMer.get(HFBusiDict.STATE).toString().equals("2")) {
			this.logInfo("商户未开通:%s",merid);
			String flag = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "MERBANK.silentSMS."+merid, ""));
		    if ("true".equalsIgnoreCase(flag)){
			    logInfo("商户未开通是否沉默短信标识:%s",flag);
				return "86801096";
			}
			return "86801012";
		}else{
			this.logInfo("结果效验结束，正常返回返回码与参数为:%s %s", BusiConst.SUCCESS, merid);
			out.putAll(rsMer);
		//	out.put(HFBusiDict.MERINTERS, rsMerInter);
			return BusiConst.SUCCESS;
		}
	}
	
	
}
