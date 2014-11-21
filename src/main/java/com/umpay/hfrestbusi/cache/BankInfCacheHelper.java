/** *****************  JAVA头文件说明  ****************
 * file name  :  BankInfCacheHelper.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2011-12-28
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;


/** ******************  类说明  *********************
 * class       :  BankInfCacheHelper
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class BankInfCacheHelper extends DefaultCacheHelper {

	/**
	 * @param dal
	 */
	public BankInfCacheHelper(CommonDalInf dal) {
		super(dal);
	}

	/** ********************************************
	 * method name   : getCacheData 
	 * modified      : lingling ,  Nov 4, 2011
	 * @see          : @see com.umpay.hfrestbusi.cache.CacheUtil.CacheAction#getCacheData(java.util.Map)
	 * ********************************************/
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		Map<String, Object> rs = dal.get("psql_BANKINFREST.getByBankId", map);
		return (Map<String, Object>) rs;
	}

}
