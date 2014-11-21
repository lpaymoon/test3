package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.XEPayRulesCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;

/** ******************  类说明  *********************
 * class       :  HfXEPayRulesRest
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description : 获取小额支付规则 
 * @see        :  
 * @date：2014-7-22 下午02:02:01                      
 * ************************************************/
public class HfXEPayRulesRest extends BaseRest{

	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		this.logInfo("查询省网支付规则信息列%s", urlargs);
		
		// 装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		String provCode  = StringUtil.trim((String)urlargs.get(HFBusiDict.PROVCODE));
		args.put(HFBusiDict.PROVCODE+".string", provCode);
		
		//获取商户银行资源
		List<Map<String, Object>> rs = null;
		rs = (List<Map<String, Object>>) CacheUtil.getObject("XEpayRules", provCode, args, new XEPayRulesCacheHelper(dal));
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
			
		out.put("payrules",rs);
		logInfo("查询省网支付规则成功%s", rs);
		return BusiConst.SUCCESS;
	}
}
