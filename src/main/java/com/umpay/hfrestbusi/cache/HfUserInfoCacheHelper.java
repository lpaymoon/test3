package com.umpay.hfrestbusi.cache;

import java.util.List;
import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;


/** ******************  类说明  *********************
 * class       :  HfUserInfoCacheHelper
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  根据手机号查询包月用户关系缓存处理类
 * @see        :                        
 * ************************************************/   
public class HfUserInfoCacheHelper  extends DefaultCacheHelper{
	public HfUserInfoCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		List<Map<String, Object>> rs = dal.find("psql_hfuserinfrest.getusers",map);
		return rs;
	}
}
