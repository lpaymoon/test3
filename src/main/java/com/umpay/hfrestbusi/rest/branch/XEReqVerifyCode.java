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
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.loadstrategy.base.LoadStrategyInf;

/** ******************  类说明  ******************
 * class       :  YXJDReqVerifyCode
 * date        :  2014-9-30 
 * @author     :  Roy
 * @version    :  V1.0  
 * description :  
 * UPDATE:请求验证码
 * CREATE:确认验证码
 * @see        :                         
 * ***********************************************/
public class XEReqVerifyCode extends BaseRest {
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
	public String doUpdateService(Map<String, String> urlargs, String id) {
		String bankId = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));		
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String expand = StringUtil.trim(urlargs.get(HFBusiDict.EXPAND));
		String merpriv = StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV));
		String version =  StringUtil.trim(urlargs.get(HFBusiDict.VERSION));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String merDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String transDate = StringUtil.trim(urlargs.get(HFBusiDict.UPTRANSDATE));		
		String businessType = StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE));		
		String transeq = StringUtil.trim((String)urlargs.get(HFBusiDict.TRANSEQ));

		/*
		 * XE无线下单
		 */
		Map<String, String> orderReqMap = new HashMap<String, String>();
		orderReqMap.put(HFBusiDict.MERID, merId);
		orderReqMap.put(HFBusiDict.EXPAND, expand);
		orderReqMap.put(HFBusiDict.MERPRIV, merpriv);
		orderReqMap.put(HFBusiDict.AMOUNT, amount);
		orderReqMap.put(HFBusiDict.GOODSID, goodsId);
		orderReqMap.put(HFBusiDict.ORDERDATE, merDate);
		orderReqMap.put(HFBusiDict.UPTRANSDATE, transDate);
		orderReqMap.put(HFBusiDict.MOBILEID, mobileId);
		orderReqMap.put(HFBusiDict.VERSION, version);
		orderReqMap.put(HFBusiDict.BUSINESSTYPE, businessType);
		orderReqMap.put(HFBusiDict.TRANSEQ, transeq);
		orderReqMap.putAll(urlargs);
		StringUtil.removeEmpty(orderReqMap);
		
		Map<String, Object> orderRs = LocalUtil.doGetService("/uprestbusi/branch/0612/"+rpid+"/"+merId+"-"+goodsId+"-order.xml", orderReqMap);
		if (!"0000".equals(orderRs.get(HFBusiDict.RETCODE))) {
			return (String) orderRs.get(HFBusiDict.RETCODE);//XE下单失败
		}
		
		Map<String, String> xeMap = new HashMap<String, String>();
		xeMap.put("orderId", transeq);
		xeMap.put("goodsId", goodsId);
		xeMap.put("merDate", merDate);
		xeMap.put("merId", merId);
		xeMap.put(HFBusiDict.AMOUNT, amount);
		xeMap.put("mobileId", mobileId);
		xeMap.put(HFBusiDict.BANKID, bankId);
		
		String retCode = "9999";
		String urlPre = getSrvPath();
		String url = "http://" + urlPre+ "/hfwebbusi/wx/verifyCode.do";
		try {
			String sendBody = net.sf.json.JSONObject.fromObject(xeMap).toString();
			byte[] sendByte = encryptor.encyptString(sendBody);
			logInfo("发送XE验证码请求%s, %s", xeMap, url);
			byte[] respBytes = HTTPSendUtil.getHttpResPost_Xstream(url, sendByte);
			
			String json = encryptor.decryptString(respBytes);
			logInfo("发送XE验证码响应：%s", json);
			
			Map<String, String> respMsg = JSONObject.parseObject(json, Map.class);
			if (respMsg != null) {
				retCode = respMsg.get(HFBusiDict.RETCODE);
			}
			if ("0000".equals(retCode)) {
				logInfo("XE获取验证码成功");
				out.putAll(respMsg);
			} else {
				logInfo("XE获取验证码失败 retCode[ " + retCode + "][" + respMsg + "]");
				return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "retcode.0612." + retCode, "86801205");
//				return "86801205";//XE验证码请求失败
			}
		} catch (Exception e) {
			logInfo("XE获取验证码失败 retCode[ " + retCode + "]");
			logException(e);
			return "86801205";//XE验证码请求失败
		} finally {
			finish(urlPre, retCode==null?9999+"":retCode);
		}
		return BusiConst.SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doCreateService(java.util.Map)
	 */
	public String doCreateService(Map<String, String> urlargs){
		//merId,goodsId,orderId,merDate,amount,verifyCode,mobileId,[merPriv],[expand]
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String orderId = StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ));
		String merDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String verifycode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));
		String merPriv = StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV));
		String expand = StringUtil.trim(urlargs.get(HFBusiDict.EXPAND));
		Map<String, String> xeMap = new HashMap<String, String>();
		xeMap.put("orderId", orderId);
		xeMap.put("goodsId", goodsId);
		xeMap.put("merDate", merDate);
		xeMap.put("merId", merId);
		xeMap.put(HFBusiDict.AMOUNT, amount);
		xeMap.put("mobileId", mobileId);
		xeMap.put("verifyCode", verifycode);
		xeMap.put("merPriv", merPriv);
		xeMap.put(HFBusiDict.EXPAND, expand);
		
		String retCode = "9999";
		String urlPre = getSrvPath();
		String url = "http://" + urlPre + "/hfwebbusi/wx/verifyPay.do";
		try {
			String sendBody = net.sf.json.JSONObject.fromObject(xeMap).toString();
			byte[] sendByte = encryptor.encyptString(sendBody);
			logInfo("发送XE验证码请求%s, %s", xeMap, url);
			byte[] respBytes = HTTPSendUtil.getHttpResPost_Xstream(url, sendByte);
			
			String json = encryptor.decryptString(respBytes);
			logInfo("发送XE验证码响应：%s", json);
			
			Map<String, String> respMsg = JSONObject.parseObject(json, Map.class);
			if (respMsg != null) {
				retCode = respMsg.get(HFBusiDict.RETCODE);
			}
			if ("0000".equals(retCode)) {
				logInfo("XE确认验证码成功");
				out.putAll(respMsg);
			} else {
				logInfo("XE确认验证码失败 retCode[ " + retCode + "][" + respMsg + "]");
				
				return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "retcode.0612." + retCode, "86801206");
//				return "86801206";//XE验证码确认请求失败
			}
		} catch (Exception e) {
			logInfo("XE确认验证码失败 retCode[ " + retCode + "]");
			logException(e);
			return "86801206";//XE验证码确认请求失败
		} finally {
			finish(urlPre, retCode==null?9999+"":retCode);
		}
		return BusiConst.SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 */
	@Override
	public String doShowService(Map<String, String> urlargs, String id){
		Map<String, String> r4Map = new HashMap<String, String>();
		String merId =  StringUtil.trim(urlargs.get(HFBusiDict.MERID)); 
		String amount =  StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String goodsId =  StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String merDate =  StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String version =  StringUtil.trim(urlargs.get(HFBusiDict.VERSION));
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
		r4Map.put(HFBusiDict.BUSINESSTYPE, StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE)));
		
		String retCode = "9999";
		String urlPre = getSrvPath();
		String url = "http://" + urlPre + "/hfwebbusi/pay/wxOrder.do";
		try {
			StringUtil.removeEmpty(r4Map);
			String sendBody = net.sf.json.JSONObject.fromObject(r4Map).toString();
			byte[] sendByte = encryptor.encyptString(sendBody);
			logInfo("发送XE下单请求%s, %s", r4Map, url);
			byte[] respBytes = HTTPSendUtil.getHttpResPost_Xstream(url, sendByte);
			
			String json = encryptor.decryptString(respBytes);
			logInfo("发送XE下单响应：%s", json);
			
			Map<String, String> respMsg = JSONObject.parseObject(json, Map.class);
			if (respMsg != null) {
				retCode = respMsg.get(HFBusiDict.RETCODE);
			}
			if ("0000".equals(retCode)) {
				logInfo("XE下单成功");
				out.putAll(respMsg);
			} else {
				logInfo("XE下单失败失败 retcode[ " + retCode + "][" + respMsg + "]");
				return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "retcode.0612." + retCode, "86801156");
//				return "86801156";//无线下单失败
			}
		} catch (Exception e) {
			e.printStackTrace();
			logInfo("XE下单失败失败 retcode[ " + retCode + "]");
			logException(e);
			return "86801156";//无线下单失败
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
