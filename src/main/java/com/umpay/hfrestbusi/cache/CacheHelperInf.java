/** *****************  JAVA头文件说明  ****************
 * file name  :  CacheAction.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 15, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.Map;





/** ******************  类说明  *********************
 * class       :  CacheAction
 * @author     :  lingling
 * @version    :  1.0  
 * description :  当缓存不存在时运行的动作接口
 * @see        :                        
 * ************************************************/

public interface CacheHelperInf {
	
	/**
	 * ********************************************
	 * method name   : getDataFromDB 
	 * description   : 从数据库中获取数据
	 * @return       : Object
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : lingling ,  Nov 15, 2011  3:13:33 PM
	 * @see          : 
	 * *******************************************
	 */
	public Object getDataFromDB(Map<String,String> map)  throws Exception;
	
}



