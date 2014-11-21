/* *****************  JAVA头文件说明  ****************
 * file name  :  LocalDal.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Oct 23, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.dal;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.bs3.app.dal.client.DalClient4Local;
import com.bs3.app.dal.client.DalClientFactory;
import com.bs3.utils.MyUtil;
import com.umpay.hfrestbusi.exception.dbexception.DBException;
import com.umpay.hfrestbusi.exception.dbexception.DBExceptionFactory;


/** ******************  类说明  *********************
 * class       :  LocalDal
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  local版本dal
 * @see        :                        
 * ************************************************/

public class LocalDal implements CommonDalInf {
	
	private String dbName = "dbOn";
	
	private static DalClient4Local api_local = null;
	
	static{
		try {
			api_local = DalClientFactory.newLocalApi2(null, null, null, null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/*
	 * ********************************************
	 * method name   : creatUrl 
	 * description   : 创建dal的url
	 * @return       : String
	 * @param        : @param dbName
	 * @param        : @param sqlid
	 * @param        : @return
	 * modified      : zhangwl ,  Oct 9, 2011
	 * @see          : 
	 * *******************************************
	 */
	private String creatUrl(String dbName, String sqlid){
		return "/hfrestbusi/dal/"+dbName+"/"+sqlid+".xml";
	}
	
	/* ********************************************
	 * method name   : call 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#call(java.lang.String, java.util.Map)
	 * ********************************************/
	public Map<String, String> call(String sqlid, Map<String, String> map)
			throws Exception {	
		return this.call(dbName, sqlid, map);
	}

	/* ********************************************
	 * method name   : call 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#call(java.lang.String, java.lang.String, java.util.Map)
	 * ********************************************/
	public Map<String, String> call(String dbName, String sqlid,
			Map<String, String> map) throws Exception {
		Object re = api_local.doPUT(creatUrl(dbName,sqlid), map, Map.class);
		if(re instanceof Exception){
			throw DBExceptionFactory.getDBException((Exception)re);
		}		
		if(!(re instanceof Map)){
			throw new DBException();
		}
		return (Map<String, String>)re;
	}

	/* ********************************************
	 * method name   : delete 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#delete(java.lang.String, java.util.Map)
	 * ********************************************/
	public int delete(String sqlid, Map<String, String> map) throws Exception {
		return this.update( sqlid, map);
	}

	/* ********************************************
	 * method name   : delete 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#delete(java.lang.String, java.lang.String, java.util.Map)
	 * ********************************************/
	public int delete(String dbName, String sqlid, Map<String, String> map)
			throws Exception {
		return this.update(dbName, sqlid, map);
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String)
	 * ********************************************/
	public List<Map<String, Object>> find(String sqlid) throws Exception {
		return this.find(dbName, sqlid);
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String, java.lang.String)
	 * ********************************************/
	public List<Map<String, Object>> find(String dbName, String sqlid)
			throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		return this.find(dbName, sqlid, map);
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String, java.lang.Object)
	 * ********************************************/
	public List<Object> find(String sqlid, Object po) throws Exception {
		return this.find(dbName, sqlid, po);
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String, java.lang.String, java.lang.Object)
	 * ********************************************/
	public List<Object> find(String dbName, String sqlid, Object po)
			throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		return this.find(dbName, sqlid, map, po);
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String, java.util.Map)
	 * ********************************************/
	public List<Map<String, Object>> find(String sqlid, Map<String, String> map)
			throws Exception {
		return this.find(dbName, sqlid ,map);
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String, java.lang.String, java.util.Map)
	 * ********************************************/
	public List<Map<String, Object>> find(String dbName, String sqlid,
			Map<String, String> map) throws Exception {
		Object re = api_local.doGET(this.creatUrl(dbName, sqlid), map, LinkedList.class);
		if(re instanceof Exception){
			throw DBExceptionFactory.getDBException((Exception)re);
		}	
		if(!(re instanceof LinkedList)){
			throw new DBException();
		}
		//将BigDecimal类型映射为Long
		if(re != null){
			List<Map<String, Object>> list = (List<Map<String, Object>>)re;
			for(Map<String, Object> o : list){
				for(String key : o.keySet()){
					Object value = o.get(key);
					if( value instanceof BigDecimal){
						o.put(key, Long.parseLong(value.toString()));
					}
				}
			}
		}		
		return (List<Map<String, Object>>)re;				
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String, java.util.Map, java.lang.Object)
	 * ********************************************/
	public List<Object> find(String sqlid, Map<String, String> map, Object po)
			throws Exception {
		return this.find(dbName, sqlid, map, po);
	}

	/* ********************************************
	 * method name   : find 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#find(java.lang.String, java.lang.String, java.util.Map, java.lang.Object)
	 * ********************************************/
	public List<Object> find(String dbName, String sqlid,
		Map<String, String> map, Object po) throws Exception {
		List<Map<String,Object>> rlist = null;
		List<Object> resultList = new LinkedList<Object>();		
		rlist = (List<Map<String, Object>>) find(sqlid,map);
		for(Map<String,Object> m:rlist){
			Object result = po.getClass().newInstance();
			MyUtil.inject2setter(result, m , false);
			resultList.add(result);
		}
		return resultList;		
	}

	/* ********************************************
	 * method name   : get 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#get(java.lang.String, java.util.Map)
	 * ********************************************/
	public Map<String, Object> get(String sqlid, Map<String, String> map)
			throws Exception {
		return this.get(dbName, sqlid, map);
	}

	/* ********************************************
	 * method name   : get 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#get(java.lang.String, java.lang.String, java.util.Map)
	 * ********************************************/
	public Map<String, Object> get(String dbName, String sqlid,
			Map<String, String> map) throws Exception {

		Map<String,Object> result = null;
		try {		
			List<?> list = find( dbName, sqlid,map);	
			if(list.size()!=0){
				result = (Map<String, Object>) list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block"	
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/* ********************************************
	 * method name   : get 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#get(java.lang.String, java.util.Map, java.lang.Object)
	 * ********************************************/
	public Object get(String sqlid, Map<String, String> map, Object po)
			throws Exception {
		return this.get(dbName, sqlid, map, po);	
	}

	/* ********************************************
	 * method name   : get 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#get(java.lang.String, java.lang.String, java.util.Map, java.lang.Object)
	 * ********************************************/
	public Object get(String dbName, String sqlid, Map<String, String> map,
			Object po) throws Exception {
		Object result = null;
		Map<String,Object> m = get(dbName, sqlid, map);
		if(m!=null){
			result = po.getClass().newInstance();
			MyUtil.inject2setter(result, m , false);
		}		
		return result;	
	}

	/* ********************************************
	 * method name   : insert 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#insert(java.lang.String, java.util.Map)
	 * ********************************************/
	public int insert(String sqlid, Map<String, String> map) throws Exception {
		return this.update(sqlid, map);
	}

	/* ********************************************
	 * method name   : insert 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#insert(java.lang.String, java.lang.String, java.util.Map)
	 * ********************************************/
	public int insert(String dbName, String sqlid, Map<String, String> map)
			throws Exception {
		return this.update(dbName, sqlid, map);
	}

	/* ********************************************
	 * method name   : update 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#update(java.lang.String, java.util.Map)
	 * ********************************************/
	public int update(String sqlid, Map<String, String> map) throws Exception {
		return this.update(dbName, sqlid, map);
	}

	/* ********************************************
	 * method name   : update 
	 * modified      : zhangwl ,  Oct 23, 2011
	 * @see          : @see com.umpay.hfrestbusi.dal.CommonDal#update(java.lang.String, java.lang.String, java.util.Map)
	 * ********************************************/
	public int update(String dbName, String sqlid, Map<String, String> map)
			throws Exception,DBException {
		Object re = api_local.doPUT(creatUrl(dbName,sqlid), map, Integer.class);
		if(re instanceof Exception){
			throw DBExceptionFactory.getDBException((Exception)re);
		}			
		if(!(re instanceof Integer)){
			throw new DBException();
		}
		return (Integer)re;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}



