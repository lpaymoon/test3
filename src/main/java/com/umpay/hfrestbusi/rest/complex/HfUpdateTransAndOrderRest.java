package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.DateTimeUtil;
import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
/** ******************  类说明  *********************
 * class       :  HfUpdateTransAndOrderRest
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  更新交易表订单表状态
 * @see        :                        
 * ************************************************/
public class HfUpdateTransAndOrderRest extends BaseRest{
	public String doUpdateService(Map<String, String> urlargs, String id)throws Exception{
		logInfo("更新交易表参数：%s", urlargs);
		String platdate = StringUtil.trim((String) urlargs.get(HFBusiDict.PLATDATE));
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(HFBusiDict.FUNCODE, "P100");
		argsMap.put(HFBusiDict.PLATDATE, platdate);
		argsMap.put(HFBusiDict.TRANSRPID, StringUtil.trim((String) urlargs.get(HFBusiDict.TRANSRPID)));
		argsMap.put(HFBusiDict.TRANSTATE, "0");
		Map<String, Object> resMap = LocalUtil.doPostService("hfrestbusi/xetrans/common/" + rpid + "/" + rpid+"-"+platdate+"-P100.xml", argsMap);
		String rcode = StringUtil.trim((String) resMap.get(HFBusiDict.RETCODE));
		if(!BusiConst.SUCCESS.equals(rcode)){
			logInfo("更新交易记录失败，retCode,retMsg:%s,%s",rcode,resMap.get(HFBusiDict.RETMSG));
			return rcode;
		}
		logInfo("更新交易记录成功");
		
		String payResult = StringUtil.trim((String)urlargs.get(HFBusiDict.PAYRETCODE));
		if(BusiConst.SUCCESS.equals(payResult)){
			//更新订单状态
			String orderId = StringUtil.trim((String) urlargs.get(HFBusiDict.ORDERID));
			String orderDate = StringUtil.trim((String) urlargs.get(HFBusiDict.ORDERDATE));
			String merId = StringUtil.trim((String) urlargs.get(HFBusiDict.MERID));
			String reqDate = DateTimeUtil.getDateString(DateTimeUtil.currentDateTime());
			
			Map<String,String> orderMap = new HashMap<String,String>();
			orderMap.put(HFBusiDict.ORDERID, orderId);
			orderMap.put(HFBusiDict.ORDERDATE, orderDate);
			orderMap.put(HFBusiDict.MERID, merId);
			orderMap.put(HFBusiDict.ORDERSTATE, "2");//如果支付失败将订单状态置成3，允许用户再次支付
			orderMap.put(HFBusiDict.RPID, rpid);
			orderMap.put(HFBusiDict.PLATDATE, reqDate);
			orderMap.put(HFBusiDict.RESERVED, payResult);
			//由于5622商户orderid中带有#,url处理时出现解析异常，因此暂将url中的orderid固定为 201208245622(修改日期+因5622修改)
//			Map<String,Object> orderInf=restResource.doPost("hfrestbusi/hforder/mer/"+rpid+"/"+merId+"-"+orderId+"-"+orderDate+".xml", orderMap);
			Map<String,Object> orderInf = LocalUtil.doPostService("hfrestbusi/hforder/mer/"+rpid+"/"+merId+"-201407015622-"+orderDate+".xml", orderMap);
			String retCode = StringUtil.trim((String) orderInf.get(HFBusiDict.RETCODE));
		//	log.info("update order更新订单状态："+retCode);
			if(!retCode.equals(BusiConst.SUCCESS)){
				logInfo("update order更新订单状态失败,返回码%s",retCode);
				return retCode;
			}
		}
		return BusiConst.SUCCESS;
	}
}
