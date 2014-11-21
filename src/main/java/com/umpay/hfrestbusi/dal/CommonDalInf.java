/* *****************  JAVA头文件说明  ****************
 * file name  :  CommonDal.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Oct 9, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.dal;

import java.util.List;
import java.util.Map;




/* ******************  类说明  *********************
 * class       :  CommonDal
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  Dal需要实现的功能
 * @see        :                        
 * ************************************************/

public interface CommonDalInf {
	
	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从默认数据源
	 * @return       : List<?>
	 * @param        : @param sqlid
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Map<String,Object>> find(String sqlid) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从可选数据源
	 * @return       : List<?>
	 * @param        : @param sqlid
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Map<String,Object>> find(String dbName , String sqlid) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从默认数据源，并且将返回信息注入PO
	 * @return       : List<T>
	 * @param        : @param sqlid
	 * @param        : @param po
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Object> find(String sqlid, Object po) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从可选数据源，并且将返回信息注入PO
	 * @return       : List<T>
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param po
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Object> find(String dbName , String sqlid, Object po) throws Exception;
	
	
	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从默认数据源，可选择输入查询数据
	 * @return       : List<?>
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Map<String,Object>> find(String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从可选数据源，可选择输入查询数据
	 * @return       : List<?>
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Map<String,Object>> find(String dbName , String sqlid ,Map<String,String> map) throws Exception;

	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从默认数据源，可选择输入查询数据，并且将返回信息注入PO
	 * @return       : List<?>
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @param po
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Object> find(String sqlid , Map<String,String> map, Object po) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : find 
	 * description   : 查询数据列表，从可选数据源，可选择输入查询数据，并且将返回信息注入PO
	 * @return       : List<?>
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @param po
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public List<Object> find(String dbName , String sqlid ,Map<String,String> map, Object po) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : get 
	 * description   : 从默认库查询单个数据
	 * @return       : Map<String,Object>
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public Map<String,Object> get(String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : get 
	 * description   : 从可选库读取单个数据
	 * @return       : Map<String,Object>
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public Map<String,Object> get(String dbName , String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : get 
	 * description   : 从默认库读取单个数据，信息注入PO
	 * @return       : T
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @param po
	 * @param        : @return
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public Object get(String sqlid , Map<String,String> map, Object po) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : get 
	 * description   : 从可选库读取单个数据，信息注入PO
	 * @return       : T
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @param po
	 * @param        : @return
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public Object get(String dbName , String sqlid ,Map<String,String> map, Object po) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : update 
	 * description   : 从默认库更新数据
	 * @return       : int
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public int update(String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : update 
	 * description   : 从可选库更新数据
	 * @return       : int
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public int update(String dbName , String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : insert 
	 * description   : 在默认库中新增数据
	 * @return       : int
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public int insert(String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : insert 
	 * description   : 在可选库中新增数据
	 * @return       : int
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public int insert(String dbName ,String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : call 
	 * description   : 默认库中执行存储过程
	 * @return       : Map<String,String>
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public Map<String,String> call(String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : call 
	 * description   : 在可选库中执行存储过程
	 * @return       : Map<String,String>
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public Map<String,String> call(String dbName , String sqlid ,Map<String,String> map) throws Exception;

	
	/*
	 * ********************************************
	 * method name   : delete 
	 * description   : 在默认库中删除数据
	 * @return       : int
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public int delete(String sqlid ,Map<String,String> map) throws Exception;
	
	/*
	 * ********************************************
	 * method name   : delete 
	 * description   : 在可选库中删除数据
	 * @return       : int
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @param map
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	public int delete(String dbName ,String sqlid ,Map<String,String> map) throws Exception;
}



