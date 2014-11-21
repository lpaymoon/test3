package com.umpay.hfrestbusi.rest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.MerBankCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;


/** ******************  类说明  *********************
 * class       :  MerbankRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  商户银行信息
 * @see        :                        
 * ************************************************/   
public class MerBankRest extends BaseRest {
	
	/** ********************************************
	 * method name   : doListService 
	 * modified      : panxingwu ,  2011-11-2
	 * description   : 查询状态正常的商户银行列
	 * ********************************************/     
	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		this.logInfo("查询商户银行信息列%s", urlargs);
		
		// 装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		String merId = urlargs.get(HFBusiDict.MERID);
		String provCode  = StringUtil.trim((String)urlargs.get(HFBusiDict.PROVCODE));
		args.put(HFBusiDict.MERID+".string", merId);
		
		//获取商户银行资源
		List<Map<String, Object>> rs = null;
		rs = (List<Map<String, Object>>) CacheUtil.getObject("merBank", merId, args, new MerBankCacheHelper(dal));
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//没有查询到相关信息
		if(rs==null||rs.size()==0){
			logInfo("商户银行信息不存在,参数为:%s", merId);
			String flag = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "MERBANK.silentSMS."+merId+"."+provCode, ""));
		    if ("true".equalsIgnoreCase(flag)){
			    logInfo("商户银行未开通是否沉默短信标识:%s",flag);
				return "86801096";
			}
			return "86801021";
		}
		
		out.put(HFBusiDict.MERBANKS,rs);
		logInfo("查询商户银行信息成功%s", rs);
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2012-2-13
	 * description   : 根据主键查询商户银行并验证状态 
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String bankId = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID+".string", merId);
		map.put(HFBusiDict.BANKID+".string",bankId);
		
		Map<String,Object> merBank = dal.get("psql_MERBANKREST.getBankByKey", map);
		
		if(merBank==null||merBank.size()==0){
			logInfo("商户银行不存在:%s",bankId);
			return "86801021";
		}
		if("4".equals((merBank.get(HFBusiDict.STATE).toString()))){
			logInfo("商户银行未开通:%s",bankId);
			String  provCode  = null;
			try{
			 provCode = bankId.substring(2,5);
			}catch (Exception e) {
				logInfo("商户银行格式有误:%s",bankId);
			}
			String flag = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "MERBANK.silentSMS."+merId+"."+provCode, ""));
		    if ("true".equalsIgnoreCase(flag)){//add by zhuoyangyang 2014-06-09 
			    logInfo("商户银行未开通是否沉默短信标识:%s",flag);
				return "86801096";
			}
			return "86801022";
		}
		logInfo("查询商户银行信息成功:%s",merBank);
		out.putAll(merBank);
		return BusiConst.SUCCESS;
	}
	
	
}
