package com.umpay.hfrestbusi.cache;

import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;

public class MerInfCacheHelper  extends DefaultCacheHelper {

	/**
	 * @param dal
	 */
	public MerInfCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}

	/** ********************************************
	 * method name   : getCacheData 
	 * modified      : lingling ,  Nov 4, 2011
	 * @see          : @see com.umpay.hfrestbusi.cache.CacheUtil.CacheAction#getCacheData(java.util.Map)
	 * ********************************************/
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		Map<String, Object> rs = dal.get("psql_MERINFREST.getByKey", map);
		return (Map<String, Object>) rs;
	}

}
