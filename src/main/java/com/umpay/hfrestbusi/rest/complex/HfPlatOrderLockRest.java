package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfPlatOrderLockRest
 * @author     :  LiuJiLong 
 * description :  平台临时订单锁定
 * @see        :  
 * @version    :  1.0                   
 * ************************************************/   
public class HfPlatOrderLockRest extends BaseRest {
	
	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : LiuJiLong ,  2012-11-24
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("执行平台订单锁定");
		Map<String,String> lockPlatOrderMap = new HashMap<String,String>();
		Map<String,String> selectPlatOrderMap = new HashMap<String,String>();
		String platOrderId = StringUtil.trim(urlargs.get(HFBusiDict.PLATORDERID));
		
		String retCode = BusiConst.SYS_ERROR;
		int num = 0;
		//更新订单状态
		logInfo("platorderid [" + platOrderId + "] is getting order ...");
		lockPlatOrderMap.put(HFBusiDict.PLATORDERID + ".string", platOrderId);
		int day = Integer.parseInt(platOrderId.charAt(platOrderId.length()-1)+"");
		if(day<0||day>6){
			logInfo("数据库表不存在:%s", platOrderId);
			return "86801103";
		}
		lockPlatOrderMap.put("_tablename", day+"");
		selectPlatOrderMap.put(HFBusiDict.PLATORDERID, platOrderId);
		
		num = dal.update("psql_HfPlatOrderLockRest.doLockUpdate", lockPlatOrderMap);
	
		//查询订单信息
		Map<String,Object> orderInfo = LocalUtil.doGetService("/hfrestbusi/platorder/" + rpid + "/" + platOrderId + ".xml", selectPlatOrderMap);
		String resCode = "0000";
		if(num==0){
			retCode = orderInfo.get(HFBusiDict.RETCODE).toString();
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("订单不存在或者订单已过期","");
				resCode  = retCode;
//				return retCode;
			}else{
				int state = Integer.parseInt(orderInfo.get(HFBusiDict.ORDERSTATE).toString());
				switch(state){
					case 1:
						resCode = "86801073";
//						return "86801073";//平台临时订单正在进行中
					case 2:
						resCode = "86801074";
//						return "86801074";//平台临时订单已成功
					case 3:
						resCode = "86801075";
//						return "86801075";//平台临时订单已失败
					default:
						resCode = "86801076";
//						return "86801076";//平台临时订单状态错误
				}
			}
		}
		
		logInfo("porderid [%s] get order ok!", platOrderId);
		out.putAll(orderInfo);
		logInfo("平台临时订单锁定成功,订单信息：", orderInfo);
		return resCode;
	}
	

}
