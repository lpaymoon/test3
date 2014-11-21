/** *****************  JAVA头文件说明  ****************
 * file name  :  CacheRest.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 13, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.rest;


import java.util.Map;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;



/** ******************  类说明  *********************
 * class       :  CacheRest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  缓存管理资源
 * @see        :                        
 * ************************************************/

public class CacheRest extends BaseRest {
	
	/**
	 * ********************************************
	 * method name   : doUpdateService 
	 * modified      : lingling ,  Nov 13, 2011
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 * *******************************************
	 */
	public String doUpdateService(Map<String, String> urlargs, String id) throws Exception {
		this.logInfo("刷新缓存开始...%s", urlargs);
		
		if(id.equals("ALLCACHE")){
			CacheUtil.clearAll();
		}else{
			CacheUtil.clear(id);		
		}
		
		this.logInfo("刷新缓存完成...%s", urlargs);
		return BusiConst.SUCCESS;
	}
	
}



