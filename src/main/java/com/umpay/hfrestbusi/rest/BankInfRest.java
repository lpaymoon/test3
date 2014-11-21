/** *****************  JAVA头文件说明  ****************
 * file name  :  BankInfRest.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2011-12-28
 * *************************************************/ 

package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.BankInfCacheHelper;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  BankInfRest
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class BankInfRest extends BaseRest{
	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : LiuJiLong ,  2011-11-7
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map, java.lang.String)
	 * ********************************************/     
	public String doShowService(Map<String, String> urlargs, String id) throws Exception{
		// LOG输出上下文
		this.logInfo("查询银行信息,参数为：%s", urlargs);
		// 装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		String bankid = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		args.put(HFBusiDict.BANKID+".string", bankid);
		
		// 商户信息资源获取
		this.logInfo("开始获取BANK资源......%s", "");
		Map<String, Object> rsBank = null; 
		rsBank = (Map<String, Object>) CacheUtil.getObject("bank", urlargs.get(HFBusiDict.BANKID), args, new BankInfCacheHelper(dal));
		
		// 检查结果，决定返回码
	    this.logInfo("开始校验结果......%s", "");
	    
	    //只有商户存在且已开通的情况(state为2)下才返回查询成功
		if (rsBank==null||rsBank.get(HFBusiDict.BANKNAME) == null) {
			this.logInfo("银行不存在: %s",bankid);
			return "86801035";
		}else if (!rsBank.get(HFBusiDict.STATE).toString().equals("2")) {
			this.logInfo("银行未开通:%s",bankid);
			return "86801016";
		}else{
			this.logInfo("结果效验结束，正常返回返回码与参数为:%s %s", BusiConst.SUCCESS, bankid);
			out.putAll(rsBank);
			return BusiConst.SUCCESS;
		}
	}
}
