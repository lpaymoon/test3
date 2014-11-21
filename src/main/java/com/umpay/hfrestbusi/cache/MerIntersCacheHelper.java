package com.umpay.hfrestbusi.cache;

import java.util.List;
import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;

public class MerIntersCacheHelper  extends DefaultCacheHelper{

	/**
	 * @param dal
	 */
	public MerIntersCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}

	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		List<Map<String, Object>> rs = dal.find("psql_MERINFREST.findInter", map);
		return (List<Map<String, Object>>) rs;
	}
	
}
