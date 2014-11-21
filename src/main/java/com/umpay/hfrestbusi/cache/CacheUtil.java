/** *****************  JAVA头文件说明  ****************
 * file name  :  CacheUtil.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 4, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/** ******************  类说明  *********************
 * class       :  CacheUtil
 * @author     :  lingling
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class CacheUtil {
	
	private static CacheManager cacheManager;
	private static Logger logger = Logger.getLogger(CacheUtil.class);
	/**
	 * ********************************************
	 * method name   : getCache 
	 * description   : 获取缓存
	 * @return       : Cache
	 * @param        : @param cacheName
	 * @param        : @return
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  4:06:38 PM
	 * @see          : 
	 * *******************************************
	 */
	private static Cache getCache(String cacheName) throws CacheException {
		if (cacheManager == null) {
			URL url = CacheUtil.class.getResource("/ehcache.xml");
			cacheManager = CacheManager.create(url);
		}
		return cacheManager.getCache(cacheName);
	}
	
	/**
	 * ********************************************
	 * method name   : getElement 
	 * description   : 获取缓存元素
	 * @return       : Object
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @return
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  3:59:25 PM
	 * @see          : 
	 * *******************************************
	 */
	public static Element getElement(String cacheName, String key) throws CacheException {
		Cache cache = getCache(cacheName);
		if(cache==null){
			return null;
		}
		Element result = cache.get(key);
		return (result==null)?null:result;
	}
	
	/**
	 * ********************************************
	 * method name   : getObject 
	 * description   : 获取缓存对象
	 * @return       : Object
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @return
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  4:51:21 PM
	 * @see          : 
	 * *******************************************
	 */
	public static Object getObject(String cacheName, String key) throws CacheException{
		Element result = getElement(cacheName,key);
		return (result==null)?null:result.getValue();
	}
	
	/**
	 * ********************************************
	 * method name   : getElementQuiet 
	 * description   : 获取缓存元素但是不更新统计
	 * @return       : Object
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @return
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  4:09:00 PM
	 * @see          : 
	 * *******************************************
	 */
	public static Element getElementQuiet(String cacheName, String key) throws CacheException {
		Cache cache = getCache(cacheName);
		if(cache==null){
			return null;
		}
		Element result = cache.getQuiet(key);
		return (result==null)?null:result;
	}
	
	/**
	 * ********************************************
	 * method name   : getObjectQuiet
	 * description   : 获取缓存对象但是不更新统计
	 * @return       : Object
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @return
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  4:51:21 PM
	 * @see          : 
	 * *******************************************
	 */
	public static Object getObjectQuiet(String cacheName, String key) throws CacheException{
		Element result = getElementQuiet(cacheName,key);
		return (result==null)?null:result.getValue();
	}
	
	
	/**
	 * ********************************************
	 * method name   : putElement 
	 * description   : 保存缓存
	 * @return       : void
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @param object
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  4:01:04 PM
	 * @see          : 
	 * *******************************************
	 */
	public static void putElement(String cacheName, Element e) throws CacheException {
		Cache cache = getCache(cacheName);
		if (cache != null) {
			cache.put(e);
		}
	}
	
	/**
	 * ********************************************
	 * method name   : putObject 
	 * description   : 保存缓存
	 * @return       : void
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @param o
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  4:54:53 PM
	 * @see          : 
	 * *******************************************
	 */
	public static void putObject(String cacheName, String key ,Object o) throws CacheException {
		putElement(cacheName , new Element(key ,o));
	}
	
	/**
	 * ********************************************
	 * method name   : getElement 
	 * description   : 获取缓存元素
	 * @return       : Object
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @return
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  3:59:25 PM
	 * @see          : 
	 * *******************************************
	 */
	public static Element getElement(String cacheName, String key, Map<String,String> map,CacheHelperInf action) throws Exception {
		Element result = getElement(cacheName,key);
		if(result==null){
			Object o = action.getDataFromDB(map);
			logger.info("数据从数据库中获取");
			if(o!=null){
				result = new Element(key, o);
				putElement(cacheName, result);
			}
		}else{
			logger.info("数据从缓存中获取");
		}
		return result;
	}
	
	/**
	 * ********************************************
	 * method name   : getObject 
	 * description   : 
	 * @return       : Object
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @param map
	 * @param        : @param action
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : lingling ,  Nov 4, 2011  4:57:09 PM
	 * @see          : 
	 * *******************************************
	 */
	public static Object getObject(String cacheName, String key, Map<String,String> map,CacheHelperInf action) throws Exception {
		Element result = getElement(cacheName,key,map,action);
		return result==null?null:result.getValue();
	}
	
	
	/**
	 * ********************************************
	 * method name   : getElementQuiet 
	 * description   : 获取缓存元素但是不更新统计
	 * @return       : Object
	 * @param        : @param cacheName
	 * @param        : @param key
	 * @param        : @return
	 * @param        : @throws CacheException
	 * modified      : lingling ,  Nov 4, 2011  4:09:00 PM
	 * @see          : 
	 * *******************************************
	 */
	public static Element getElementQuiet(String cacheName, String key, Map<String,String> map,CacheHelperInf action) throws Exception {
		Element result = getElementQuiet(cacheName,key);		
		if(result==null){
			result = (Element) action.getDataFromDB(map);
			if(result!=null){
				putElement(cacheName,result);
			}
		}		
		return result;
	}
	
	/**
	 * ********************************************
	 * method name   : clearAll 
	 * description   : 清空所有缓存
	 * @return       : void
	 * @param        : 
	 * modified      : lingling ,  Nov 13, 2011  9:20:25 PM
	 * @see          : 
	 * *******************************************
	 */
	public static void clearAll(){
		cacheManager.clearAll();
	}
	
	/**
	 * ********************************************
	 * method name   : clear 
	 * description   : 清空指定缓存
	 * @return       : void
	 * @param        : @param cacheName
	 * modified      : lingling ,  Nov 13, 2011  9:20:53 PM
	 * @see          : 
	 * *******************************************
	 */
	public static void clear(String cacheName){
		Cache cache = getCache(cacheName);
		if(cache!=null){
			cache.removeAll();
		}		
	}
	
	/**
	 * ********************************************
	 * method name   : remove 
	 * description   : 清除指定缓存的内容
	 * @return       : void
	 * @param        : @param cacheName
	 * @param        : @param key
	 * modified      : lingling ,  Nov 13, 2011  9:28:31 PM
	 * @see          : 
	 * *******************************************
	 */
	public static void remove(String cacheName,String key){
		Cache cache = getCache(cacheName);
		if(cache!=null){
			cache.remove(key);
		}		
	}
	
	
}



