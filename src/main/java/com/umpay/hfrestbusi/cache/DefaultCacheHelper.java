/** *****************  JAVA头文件说明  ****************
 * file name  :  DefaultCacheHelper.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 17, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;




/** ******************  类说明  *********************
 * class       :  DefaultCacheHelper
 * @author     :  lingling
 * @version    :  1.0  
 * description :  默认实现，将输入对象缓存
 * @see        :                        
 * ************************************************/

public class DefaultCacheHelper implements CacheHelperInf {

	protected CommonDalInf dal = null;
	
	public DefaultCacheHelper(CommonDalInf dal){
		this.dal=dal;
	}
	
	/** ********************************************
	 * method name   : getDataFromDB 
	 * modified      : lingling ,  Nov 15, 2011
	 * @see          : @see com.umpay.hfrestbusi.cache.CacheAction#getDataFromDB(java.util.Map)
	 * ********************************************/     
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		return map;
	}

}



