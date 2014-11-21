package com.umpay.hfrestbusi.cache;

import java.util.List;
import java.util.Map;

import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;


/** ******************  类说明  *********************
 * class       :  GoodsServiceIdsCacheHelper
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  商品计费代码列表缓存
 * @see        :                        
 * ************************************************/   
public class GoodsServiceIdsCacheHelper extends DefaultCacheHelper {

	public GoodsServiceIdsCacheHelper(CommonDalInf dal) {
		super(dal);
	}

	@Override
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		Map<String, Object> rs = dal.get("psql.getGoodsServiceid",map);
		return rs;
	}

}
