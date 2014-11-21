/** *****************  JAVA头文件说明  ****************
 * file name  :  UserMerLtdHelper.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-2-23
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.List;
import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;


/** ******************  类说明  *********************
 * class       :  UserMerLtdHelper
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class UserMerLtdHelper extends DefaultCacheHelper {
	
	/**
	 * @param dal
	 */
	public UserMerLtdHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}

	/** ********************************************
	 * method name   : getDataFromDB 
	 * modified      : LiuJiLong ,  2012-2-23
	 * @see          : @see com.umpay.hfrestbusi.cache.CacheHelperInf#getDataFromDB(java.util.Map)
	 * ********************************************/
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		List<Map<String, Object>> rs = dal.find("UserMerLtd.getUserMerRule", map);
		return (List<Map<String, Object>>) rs;
	}

}
