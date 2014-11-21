/** *****************  JAVA头文件说明  ****************
 * file name  :  MerCertCacheAction.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 4, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;




/** ******************  类说明  *********************
 * class       :  MerCertCacheAction
 * @author     :  lingling
 * @version    :  1.0  
 * description :  商户证书获取缓存方法
 * @see        :                        
 * ************************************************/

public class MerCertCacheHelper  extends DefaultCacheHelper {

	/**
	 * @param dal
	 */
	public MerCertCacheHelper(CommonDalInf dal) {
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
		Map<String, Object> rs = dal.get("psql_MERSIGNREST.getMercert", map);
		if(rs==null){
			return null;
		}
		return (byte[]) rs.get(HFBusiDict.MERCERT);
	}

}



