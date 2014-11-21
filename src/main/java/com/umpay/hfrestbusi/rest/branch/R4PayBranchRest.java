package com.umpay.hfrestbusi.rest.branch;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bs3.ioc.core.BeansContext;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.Encryptor;
import com.umpay.hfrestbusi.util.HTTPSendUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.loadstrategy.base.LoadStrategyInf;

/** ******************  类说明  ******************
 * class       :  R4PayBranchRest
 * date        :  2014-9-23 
 * @author     :  Roy
 * @version    :  V1.0  
 * description :  R4支付子流程
 * @see        :                         
 * ***********************************************/
public class R4PayBranchRest  extends BaseRest {
	private static LoadStrategyInf loadStrategyWeb;
	private static Encryptor encryptor ;
	static{
		String key = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "WXCLIENT.DES.KEY", "Ms/JhfSs");
		encryptor = new Encryptor(key);
	}
	static{
		loadStrategyWeb = (LoadStrategyInf)BeansContext.getInstance().getBean("loadStrategyWeb");//初始化短信解析负载组件
	}
	
	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 */
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id){
		Map<String, String> r4Map = new HashMap<String, String>();
		String merId =  StringUtil.trim(urlargs.get(HFBusiDict.MERID)); 
		String version =  StringUtil.trim(urlargs.get(HFBusiDict.VERSION));
		String amount =  StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String goodsId =  StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String merDate =  StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String orderId =  StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ));
		
		/*
		 * 生成银行交易流水
		 */
		
		r4Map.put("orderId", orderId);
		r4Map.put("goodsId", goodsId);
		r4Map.put("merDate", merDate);
		//r4Map.put(HFBusiDict.merDate, );
		r4Map.put(HFBusiDict.R4MERID, merId);
		r4Map.put(HFBusiDict.VERSION, version);
		r4Map.put(HFBusiDict.AMOUNT, amount);
		
		r4Map.put(HFBusiDict.MODEL, StringUtil.trim(urlargs.get(HFBusiDict.MODEL)));
		r4Map.put(HFBusiDict.CHNLID, StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID)));
		r4Map.put("networkOperatorName", StringUtil.trim(urlargs.get("networkOperatorName")));
//		r4Map.put(HFBusiDict.networkOperatorName, StringUtil.trim(urlargs.get(HFBusiDict.networkOperatorName)));
		r4Map.put(HFBusiDict.IMSI, StringUtil.trim(urlargs.get(HFBusiDict.IMSI)));
		r4Map.put(HFBusiDict.ICCID, StringUtil.trim(urlargs.get(HFBusiDict.ICCID)));
//		r4Map.put(HFBusiDict.isRoot, StringUtil.trim(urlargs.get(HFBusiDict.isRoot)));
		r4Map.put(HFBusiDict.PLATTYPE, StringUtil.trim(urlargs.get(HFBusiDict.PLATTYPE)));
		r4Map.put("versionCode", StringUtil.trim(urlargs.get("versionCode")));
//		r4Map.put(HFBusiDict.versionCode, StringUtil.trim(urlargs.get(HFBusiDict.versionCode)));
		r4Map.put("versionName", StringUtil.trim(urlargs.get("versionName")));
//		r4Map.put(HFBusiDict.versionName, StringUtil.trim(urlargs.get(HFBusiDict.versionName)));
		r4Map.put("sdkType", StringUtil.trim(urlargs.get(HFBusiDict.SDKTYPE)));
		r4Map.put("cid", StringUtil.trim(urlargs.get("clientid")));
//		r4Map.put(HFBusiDict.cid, StringUtil.trim(urlargs.get(HFBusiDict.cid)));
		r4Map.put("mobileNet", StringUtil.trim(urlargs.get(HFBusiDict.UPNETTYPE)));
//		r4Map.put(HFBusiDict.mobileNet, StringUtil.trim(urlargs.get(HFBusiDict.mobileNet)));
		r4Map.put(HFBusiDict.SIGN, StringUtil.trim(urlargs.get(HFBusiDict.SIGN)));
		r4Map.put(HFBusiDict.MOBILEOS, StringUtil.trim(urlargs.get(HFBusiDict.PHONEOS)));
		r4Map.put("lastGpsLocation", StringUtil.trim(urlargs.get(HFBusiDict.AREA)));
//		r4Map.put(HFBusiDict.lastGpsLocation, StringUtil.trim(urlargs.get(HFBusiDict.lastGpsLocation)));
		r4Map.put(HFBusiDict.IMEI, StringUtil.trim(urlargs.get(HFBusiDict.IMEI)));
		r4Map.put("userAppsList", StringUtil.trim(urlargs.get("hfuserlist")));
//		r4Map.put(HFBusiDict.userAppsList, StringUtil.trim(urlargs.get(HFBusiDict.userAppsList)));
		r4Map.put("merPriv", StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV)));
		r4Map.put(HFBusiDict.EXPAND, StringUtil.trim(urlargs.get(HFBusiDict.EXPAND)));
		r4Map.put(HFBusiDict.MOBILENO, StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID)));
		r4Map.put(HFBusiDict.CLIENTVERSION, StringUtil.trim(urlargs.get(HFBusiDict.CLIENTVERSION)));
		
		StringUtil.removeEmpty(r4Map);
		String retCode = "9999";
		String urlPre = getSrvPath();
		String url = "http://" + urlPre + "/hfwebbusi/pay/wxOrder.do";
		try {
			String sendBody = net.sf.json.JSONObject.fromObject(r4Map).toString();
			byte[] sendByte = encryptor.encyptString(sendBody);
			logInfo("发送R4请求%s, %s", r4Map, url);
			byte[] respBytes = HTTPSendUtil.getHttpResPost_Xstream(url, sendByte);
			
			String json = encryptor.decryptString(respBytes);
			logInfo("发送R4响应：%s", json);
			
			Map<String, String> respMsg = JSONObject.parseObject(json, Map.class);
			if (respMsg != null) {
				retCode = respMsg.get(HFBusiDict.RETCODE);
			}
			if ("0000".equals(retCode)) {
				logInfo("R4下单成功");
				out.putAll(respMsg);
			} else {
				logInfo("R4下单失败失败 retcode[ " + retCode + "][" + respMsg + "]");
				return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "retcode.0611." + retCode, "86801156");
//				return "86801156";//R4无线下单失败
			}
		} catch (Exception e) {
			e.printStackTrace();
			logInfo("R4下单失败失败 retcode[ " + retCode + "]");
			logException(e);
			return "86801156";//R4无线下单失败
		} finally {
			finish(urlPre, retCode==null?9999+"":retCode);
		}
		return BusiConst.SUCCESS;
	}
		

	/** *****************  方法说明  *****************
	 * method name   :  getSmsSrvPath
	 * @param		 :  @return
	 * @return		 :  String
	 * @author       :  LiuJiLong 2014-3-4 上午10:54:48
	 * description   :  策略负载获得URL
	 * @see          :  
	 * ***********************************************/
	private String getSrvPath(){
		return loadStrategyWeb.lookup();
	}
	
	/** *****************  方法说明  *****************
	 * method name   :  finish
	 * @param		 :  @param url
	 * @param		 :  @param retCode
	 * @param		 :  @param responseMsgGlobal
	 * @return		 :  void
	 * @author       :  LiuJiLong 2014-3-4 上午10:48:12
	 * description   :  策略负载反馈
	 * @see          :  
	 * ***********************************************/
	private void finish(String url, String retCode) {
		loadStrategyWeb.finish(url, retCode);
	}
	
	
}
