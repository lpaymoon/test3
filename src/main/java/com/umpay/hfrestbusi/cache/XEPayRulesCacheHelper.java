/**
 * 
 */
package com.umpay.hfrestbusi.cache;

import java.util.List;
import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;

/** ******************  类说明  *********************
 * class       :  XEPayRulesCacheHelper
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  省网支付规则信息缓存
 * @see        :  
 * @date：2014-7-22 下午02:04:58                      
 * ************************************************/
public class XEPayRulesCacheHelper  extends DefaultCacheHelper {

	
	/**
	 * @param dal
	 */
	public XEPayRulesCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}

	/** ********************************************
	 * method name   : getCacheData 
	 * modified      : panxingwu ,  2011-11-5
	 * description   : 查询省网支付规则信息
	 * ********************************************/     
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		List<Map<String, Object>> rs = dal.find("psql_HFXEPAYRULESREST.getRules",map);
		return rs;
	}
}
