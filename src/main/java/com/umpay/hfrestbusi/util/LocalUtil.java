/* *****************  JAVA头文件说明  ****************
 * file name  :  ServiceUtil.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Oct 23, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.bs3.app.dal.DalApi4Inf;
import com.bs3.app.dal.client.DalClientFactory;
import com.umpay.hfrestbusi.constants.*;
import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  ServiceUtil
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  调用本地资源工具
 * @see        :                        
 * ************************************************/

public class LocalUtil {
	
	private static DalApi4Inf api;
	private static final Logger _log = Logger.getLogger(LocalUtil.class);
	
	static {
		try {
			api = DalClientFactory.newLocalApi2(null, null, null, null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ********************************************
	 * method name   : doGetService 
	 * description   : GET方法调用本地资源
	 * @return       : Map<String,Object>
	 * @param        : @param url
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 24, 2011
	 * @see          : 
	 * *******************************************
	 */
	public static Map<String,Object> doGetService(String url , Map<String,String> map)  {
		Map<String, Object> res = null;	
		map.put(BusiConst.LOCAL, BusiConst.LOCAL);
		try{
			res = (Map<String,Object>)api.doGET(url, map, Map.class);
		}catch(Exception e){
			e.printStackTrace();
			_log.error(StringUtil.getExceptionStr(url, e));
			res = new HashMap<String, Object>();
			res.putAll(map);
			res.put(HFBusiDict.RETCODE, "86801005");
		}
		return res;		
	}
	
	/**
	 * ********************************************
	 * method name   : doPostService 
	 * description   : POST方法调用本地资源
	 * @return       : Map<String,Object>
	 * @param        : @param url
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 24, 2011
	 * @see          : 
	 * *******************************************
	 */
	public static Map<String,Object> doPostService(String url , Map<String,String> map) throws Exception {
		Map<String, Object> res = null;	
		map.put(BusiConst.LOCAL, BusiConst.LOCAL);
		try{
			res = (Map<String,Object>)api.doCall("POST", url ,map, Map.class);
		}catch(Exception e){
			e.printStackTrace();
			_log.error(StringUtil.getExceptionStr(url, e));
			res = new HashMap<String, Object>();
			res.putAll(map);
			res.put(HFBusiDict.RETCODE, "86801005");
		}
		return res;		
	}
	
}



