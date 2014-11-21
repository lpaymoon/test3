package com.umpay.hfrestbusi.cache;

import java.util.Map;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;

public class GoodsInfCacheHelper  extends DefaultCacheHelper {

	/**
	 * @param dal
	 */
	public GoodsInfCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}

	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		Map<String, Object> rs = dal.get("psql_GOODINFREST.getGoodInf", map);
		return rs;
	}

}
