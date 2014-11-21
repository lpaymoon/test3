package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;
import com.bs.mpsp.util.DateTimeUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfOrderLock
 * @author     :  Wu Enzhen
 * @version    :  1.0  
 * description :  订单锁定
 * @see        :                        
 * ************************************************/   
public class HfOrderLockRest extends BaseRest {
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String resCode = "0000";
		logInfo("执行订单锁定,参数：%s", urlargs);
		Map<String,String> orderMap = new HashMap<String,String>();
		String porderId = StringUtil.trim(urlargs.get(HFBusiDict.PORDERID));
		String verifycode = StringUtil.trim(urlargs.get(HFBusiDict.MSGCON));
		String reqDate = DateTimeUtil.getDateString(DateTimeUtil.currentDateTime());
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String retCode = BusiConst.SYS_ERROR;
		int num = 0;
		//更新订单状态
		logInfo("porderid [" + porderId + "] is getting order ...");
		orderMap.put(HFBusiDict.ORDERSTATE, "1");
		orderMap.put(HFBusiDict.PLATDATE, reqDate);
		orderMap.put(HFBusiDict.MOBILEID, mobileId);
		orderMap.put(HFBusiDict.RPID, rpid);
		orderMap.put(HFBusiDict.PORDERID, porderId);
		orderMap.put(HFBusiDict.VERIFYCODE, verifycode);
		
		Map<String,Object> orderInf=LocalUtil.doPostService("/hfrestbusi/hforder/common/"+rpid+"/"+mobileId+"-"+porderId+".xml", orderMap);
		retCode = orderInf.get(HFBusiDict.RETCODE).toString();
		//数据库表不存在
		if(retCode.equals("86801103"))
		{
			logInfo("数据库表不存在:%s", "noTable");
			return retCode;
		}
		//查询订单信息
		num = ((BusiConst.SUCCESS.equals(retCode))?1:0);
		Map<String,Object> orderList = LocalUtil.doGetService("/hfrestbusi/hforder/common/"+rpid+"/"+mobileId+"-"+porderId+".xml", orderMap);
		if(num==0){
			retCode = orderList.get(HFBusiDict.RETCODE).toString();
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("订单不存在或者订单已过期","");
				return retCode;
			}
			if(!orderList.get(HFBusiDict.VERIFYCODE).toString().equals(verifycode)){
				// TODO 验证码错误的时候，需要返回订单信息
				out.putAll(orderList);
				return "86801047";//订单验证码错误
			}
			int state = Integer.parseInt(orderList.get(HFBusiDict.ORDERSTATE).toString());
			switch(state){
			case 1:
				resCode = "86801043"; break;
//				return "86801043";//订单正在充值中
			case 2:
				resCode = "86801044"; break;
//				return "86801044";//订单充值成功
			case 4:
				resCode = "86801045"; break;
//				return "86801045";//订单充值超时
			default:
				resCode = "86801046"; 
//				return "86801046";//订单状态错误
			}
		}
		logInfo("porderid [%s] get order ok!", porderId);
		out.putAll(orderList);
		logInfo("订单锁定成功,订单信息：", orderList);
		return resCode;
	}	
}
