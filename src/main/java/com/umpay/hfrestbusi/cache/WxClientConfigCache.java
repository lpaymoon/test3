/** *****************  JAVA头文件说明  ****************
 * file name  :  BankInfCacheHelper.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2011-12-28
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;

/** 
* @ClassName	: WxClientConfigCache 
* @Description	: 查询无线客户端配置
* @author		： panxingwu
* @date			： 2013-5-24 上午9:48:05 
*/
public class WxClientConfigCache extends DefaultCacheHelper {

	/**
	 * @param dal
	 */
	public WxClientConfigCache(CommonDalInf dal) {
		super(dal);
	}

	/** ********************************************
	 * method name   : getCacheData 
	 * modified      : lingling ,  Nov 4, 2011
	 * @see          : @see com.umpay.hfrestbusi.cache.CacheUtil.CacheAction#getCacheData(java.util.Map)
	 * ********************************************/
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		Map<String, Object> rs = dal.get("psql_WXCLIENTCONF.getConfig", map);
		return (Map<String, Object>) rs;
	}

}
