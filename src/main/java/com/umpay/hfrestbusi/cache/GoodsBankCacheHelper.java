/** *****************  JAVA头文件说明  ****************
 * file name  :  MerCertCacheAction.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 4, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.List;
import java.util.Map;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;



/** ******************  类说明  *********************
 * class       :  GoodsBankCacheAction
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  商户银行信息缓存获取
 * @see        :                        
 * ************************************************/   
public class GoodsBankCacheHelper  extends DefaultCacheHelper {
	/**
	 * @param dal
	 */
	public GoodsBankCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}

	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		List<Map<String, Object>> rs = dal.find("psql_GOODSBANKREST.getBankInf",map);
		return rs;
	}

}



