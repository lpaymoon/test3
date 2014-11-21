/** *****************  JAVA头文件说明  ****************
 * file name  :  MerCertCacheAction.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 4, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.cache;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;
import com.umpay.hfrestbusi.util.TransAclUtil;


/** ******************  类说明  *********************
 * class       :  TransAclAction
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  交易屏蔽模板缓存策略
 * @see        :                        
 * ************************************************/   
public class TransAclCacheHelper  extends DefaultCacheHelper {
    
	
	/**
	 * @param dal
	 */
	public TransAclCacheHelper(CommonDalInf dal) {
		super(dal);
		// TODO Auto-generated constructor stub
	}

	/** ********************************************
	 * method name   : getCacheData 
	 * modified      : panxingwu ,  2011-11-9
	 * description   : 查询交易屏蔽模板
	 * ********************************************/     
	public Object getDataFromDB(Map<String, String> map) throws Exception {
		CommonDalInf dal = DalFactory.getDal();
		List<Map<String, Object>> rs = dal.find("HFTRANSACL.getTransAcl",map);
		if(rs==null){
			return null;
		}
		List<String> result = new LinkedList<String>();
		
		for (Map<String, Object> rsMap : rs) {
			String r_merId = rsMap.get(HFBusiDict.MERID).toString();
			String r_goodsId = rsMap.get(HFBusiDict.GOODSID).toString();
			String r_provCode = rsMap.get(HFBusiDict.PROVCODE).toString();
			String r_areaCode = rsMap.get(HFBusiDict.AREACODE).toString();
			int r_netType = Integer.parseInt((rsMap.get(HFBusiDict.NETTYPE).toString()));
			String r_cardType = rsMap.get(HFBusiDict.CARDTYPE).toString();
			String r_mobileId = rsMap.get(HFBusiDict.MOBILEID).toString();
			int r_banktype = Integer.parseInt(rsMap.get(HFBusiDict.BANKTYPE).toString());
			int r_goodstype= Integer.parseInt(rsMap.get(HFBusiDict.GOODSTYPE).toString());
			int r_usergrade = Integer.parseInt(rsMap.get(HFBusiDict.GRADE).toString());
			//根据屏蔽项生成正则表达式
			String regex = TransAclUtil.translateRegex(r_merId,r_goodsId,r_provCode,r_areaCode,r_netType,r_cardType,r_mobileId,r_banktype,r_goodstype,r_usergrade); 		
			result.add(regex);
		}
		return result;
	}

}



