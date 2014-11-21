package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfWxOrderLockRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  锁定无线商户订单
 * @see        :                        
 * ************************************************/   
public class HfWxOrderLockRest extends BaseRest {

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String resCode = "0000";
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String orderstate = "1";
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID, orderid);
		map.put(HFBusiDict.ORDERDATE, orderdate);
		map.put(HFBusiDict.MERID, merid);
		map.put(HFBusiDict.ORDERSTATE, orderstate);
		String tableName = OrderTableUtil.getTableNameByDate(urlargs.get(HFBusiDict.ORDERDATE));
		String tableNum = tableName.substring(tableName.length()-1);
		map.put("tablename",tableNum);
		
		Map<String,String> updateMap = new HashMap<String,String>();
		updateMap.put(HFBusiDict.MERID+".string", merid);
		updateMap.put(HFBusiDict.ORDERID+".string", orderid);
		updateMap.put(HFBusiDict.ORDERDATE+".string", orderdate);
		updateMap.put(HFBusiDict.ORDERSTATE+".int", orderstate);
		updateMap.put("_tablename",tableNum);
 		int num = dal.update("psql_Wxorder.lockOrder", updateMap);
 		Map<String,Object> rsMap = LocalUtil.doGetService("/hfrestbusi/wxorder/"+rpid+"/"+orderid+"-"+orderdate+"-"+merid+".xml", map);
 		if(num==0){
 			String retCode = String.valueOf(rsMap.get(HFBusiDict.RETCODE));
 			if(!BusiConst.SUCCESS.equals(retCode)){
 				logInfo("订单不存在或者订单已过期");
 				resCode = retCode;
// 				return retCode;
 			}else{
 	 			int state = Integer.parseInt(String.valueOf(rsMap.get(HFBusiDict.ORDERSTATE)));
 	 			switch(state){
 				case 1:
 					resCode = "86801043"; break;
// 					return "86801043";//订单正在充值中
 				case 2:
 					resCode = "86801044"; break;
// 					return "86801044";//订单充值成功
 				case 4:
 					resCode = "86801045"; break;
// 					return "86801045";//订单充值超时
 				default:
 					resCode = "86801046";
// 					return "86801046";//订单状态错误
 				}
 			}
 		}
		out.putAll(rsMap);
		return resCode;
	}
	
}
