package com.umpay.hfrestbusi.cache;

import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;

public class ChnlInfCacheHelper  extends DefaultCacheHelper {

	public ChnlInfCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		Map<String, Object> rs = dal.get("psql_chnlcheck.getchnlinf", map);
		if(rs==null){
			return null;
		}
		return rs;
	}
}
