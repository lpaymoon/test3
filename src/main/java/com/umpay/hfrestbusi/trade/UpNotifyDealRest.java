package com.umpay.hfrestbusi.trade;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.DateTimeUtil;
import com.bs.mpsp.util.StringUtil;
import com.bs2.core.Context4Spring;
import com.bs2.core.ext.Service4QObj;
import com.bs2.inf.Service4QInf;
import com.bs2.mpsp.XmlMobile;
import com.bs3.ext.bs2.Service4QRun;
import com.bs3.ioc.core.BeansContext;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.HTTPSendUtil;
import com.umpay.hfrestbusi.util.HTTPUtil;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.TimeUtil;

import com.umpay.loadstrategy.base.LoadStrategyInf;
/** ******************  类说明  *********************
 * class       :  UpNotifyDealRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  接收支付结果通知处理流程
 * @see        :                        
 * ************************************************/
public class UpNotifyDealRest extends BaseRest{
	private static LoadStrategyInf loadStrategyMerfront;
	static{
		loadStrategyMerfront = (LoadStrategyInf)BeansContext.getInstance().getBean("loadStrategyMerfront");//初始化短信解析负载组件
	}
	
	
	public String doUpdateService(Map<String, String> urlargs, String id)throws Exception{
		logInfo("更新交易表参数：%s", urlargs);
		//根据什么参数更新交易表状态？tranSeq ,根据tranSeq查询交易
		String tranSeq= StringUtil.trim((String) urlargs.get(HFBusiDict.TRANSEQ));
//		String brankCheckDate=StringUtil.trim((String) urlargs.get("settleDate")); //支付通道返回流水
		String brankCheckDate=StringUtil.trim((String) urlargs.get(HFBusiDict.SETTLEDATE)); //清算日期
		String payResult = StringUtil.trim((String)urlargs.get(HFBusiDict.PAYRETCODE));//支付返回码
		String mobileId =StringUtil.trim((String)urlargs.get(HFBusiDict.MOBILEID));//手机号
		String transType=StringUtil.trim((String) urlargs.get(HFBusiDict.TRANSTYPE));//交易类型
		String servType=StringUtil.trim((String) urlargs.get(HFBusiDict.SERVTYPE));//服务类型
		String merPriv=StringUtil.trim((String) urlargs.get(HFBusiDict.MERPRIV));//私有信息
		String orderdate=StringUtil.trim((String) urlargs.get(HFBusiDict.ORDERDATE));//订单日期
		String rpid = StringUtil.trim((String) urlargs.get(HFBusiDict.RPID));
		String reqdate=TimeUtil.date8();
		if("".equals(brankCheckDate)){
			brankCheckDate=reqdate;
		}
		if("".equals(transType)||"null".equals(transType)){
			transType="0";//交易类型 ：新增
		}
		String retCode = BusiConst.SYS_ERROR;
	    //更新交易表
		String ordersate="3";//默认失败
	    String transstate="-1";//默认失败
		if(BusiConst.SUCCESS.equals(payResult)){
				ordersate="2";
				transstate="0";
		}
		
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(HFBusiDict.FUNCODE, "P100");
		argsMap.put(HFBusiDict.TRANSEQ, tranSeq);
		argsMap.put(HFBusiDict.TRANSTATE, transstate);
		argsMap.put("servpriv", payResult);
		argsMap.put(HFBusiDict.MOBILEID, mobileId);
		Map<String, Object> resMap = LocalUtil.doPostService("/uprestbusi/uptrans/common/" + rpid + "/" + rpid+"-"+reqdate+"-P100.xml", argsMap);
		String rcode = StringUtil.trim((String) resMap.get(HFBusiDict.RETCODE));
		if(!BusiConst.SUCCESS.equals(rcode)){
			logInfo("更新交易记录失败，retCode,retMsg:%s,%s",rcode,resMap.get(HFBusiDict.RETMSG));
			return rcode;
		}
		logInfo("更新交易记录成功");
		//从交易表查询订单platordId
		Map<String, String> tansargsMap = new HashMap<String, String>();
		tansargsMap.put(HFBusiDict.FUNCODE, "P100");
		tansargsMap.put(HFBusiDict.TRANSEQ, tranSeq);


		Map<String, Object> qTransMap = LocalUtil.doGetService("/uprestbusi/uptrans/common/" + rpid + "/" + rpid+"-"+tranSeq+"-P100.xml", tansargsMap);
		String retcode = StringUtil.trim((String) qTransMap.get(HFBusiDict.RETCODE));
		if(!BusiConst.SUCCESS.equals(retcode)){
			logInfo("查询交易记录失败，retCode,retMsg:%s,%s",rcode,resMap.get(HFBusiDict.RETMSG));
			return rcode;
		}
		logInfo("查询交易记录成功");
		String platordId=(String)  StringUtil.trim((String)qTransMap.get(HFBusiDict.PLATORDID));
		
		//查询订单
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put(HFBusiDict.PLATORDID, platordId);
		Map<String,Object> orderQueryResp = LocalUtil.doGetService("/uprestbusi/uporder/common/"+rpid+"/"+platordId+".xml", queryMap);
		retCode = (String) orderQueryResp.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("查询订单失败,返回码：%s", retCode);
			return retCode;
		}
		logInfo("查询订单记录成功");
		//更新订单状态
		String orderDate = StringUtil.trim((String) qTransMap.get(HFBusiDict.ORDERDATE));
		String merId = StringUtil.trim((String) qTransMap.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim((String) qTransMap.get(HFBusiDict.GOODSID));
			
		Map<String,String> orderMap = new HashMap<String,String>();
		orderMap.put(HFBusiDict.PLATORDID, platordId);
		orderMap.put(HFBusiDict.ORDERDATE, orderDate);
		orderMap.put(HFBusiDict.ORDERSTATE, ordersate);//如果支付失败将订单状态置成3，允许用户再次支付
		orderMap.put(HFBusiDict.RESERVED, payResult);
		orderMap.put(HFBusiDict.MOBILEID, mobileId);
		
		Map<String,Object> orderInf = LocalUtil.doPostService("/uprestbusi/uporder/common/"+rpid+"/"+merId+"-"+platordId+"-"+orderDate+".xml", orderMap);
		retCode = StringUtil.trim((String) orderInf.get(HFBusiDict.RETCODE));
		//	log.info("update order更新订单状态："+retCode);
		if(!retCode.equals(BusiConst.SUCCESS)){
			logInfo("update order更新订单状态失败,返回码%s",retCode);
			return retCode;
		}
			
		if(BusiConst.SUCCESS.equals(payResult)){ //支付成功通知商户发货	
			// 发送支付结果通知到商户前置	
			String notfiyResult="2"; //默认发货失败
			Map<String, String> notifyMerMap = new HashMap<String, String>();
			notifyMerMap.put(HFBusiDict.FRONT3FUNCODE, "UTZS");
			notifyMerMap.put(HFBusiDict.FUNCODE, "UTZS");
			notifyMerMap.put(HFBusiDict.RPID, rpid);
			notifyMerMap.put(HFBusiDict.MERID, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.MERID)));
			notifyMerMap.put(HFBusiDict.GOODSID, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.GOODSID)));
			notifyMerMap.put(HFBusiDict.ORDERID, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.ORDERID)));
			notifyMerMap.put(HFBusiDict.AMOUNT, StringUtil.trim(String.valueOf(orderQueryResp.get(HFBusiDict.AMOUNT))));
			notifyMerMap.put(HFBusiDict.UPTRANSDATE, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.UPTRANSDATE)));
			if("".equals(mobileId)){
				mobileId=StringUtil.trim((String) orderQueryResp.get(HFBusiDict.MOBILEID));
			}
			notifyMerMap.put(HFBusiDict.MOBILEID,mobileId );
			notifyMerMap.put(HFBusiDict.RETCODE, StringUtil.trim((String) urlargs.get(HFBusiDict.PAYRETCODE)));
			if("".equals(servType)){
				servType="2";
			}
			notifyMerMap.put(HFBusiDict.SERVICETYPE, servType);
			notifyMerMap.put(HFBusiDict.UPVERSION, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.VERSION)));
			notifyMerMap.put(HFBusiDict.ORDERDATE, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.ORDERDATE)));
			notifyMerMap.put(HFBusiDict.TRANSTYPE, transType);//交易类型：新增
//			notifyMerMap.put(HFBusiDict.BANKCHECKDATE, brankCheckDate);//settleDate
			notifyMerMap.put(HFBusiDict.SETTLEDATE, brankCheckDate);//settleDate
			notifyMerMap.put(HFBusiDict.MERPRIV, StringUtil.trim((String)orderQueryResp.get(HFBusiDict.MERPRIV)));
			notifyMerMap.put(HFBusiDict.CHANNELID, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.CHANNELID)));
			notifyMerMap.put(HFBusiDict.APPID, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.APPID)));
	
			String urlPre = getSrvPath();
			String url = "http://" + urlPre;
			Map<String, String> respMsg=null;
			try {
				logInfo("发送发货请求%s, %s", notifyMerMap, url);
				respMsg = (Map<String, String>) HTTPSendUtil
						.getHttpResPost_Xstream(url, notifyMerMap);
				if (respMsg != null) {
					retCode = respMsg.get(HFBusiDict.RETCODE);
				}else{
					logInfo("发送发货通知失败 :%s",respMsg);
					retCode="86801571";
					return "86801571";
				}
				if ("0000".equals(retCode)) {
					logInfo("发送发货通知成功:%s",respMsg);
					out.putAll(respMsg);
					notfiyResult="1";
				} else {
					logInfo("发货请求失败 retcode[ " + retCode + "][" + respMsg + "]");
					retCode="86801571";
					return "86801571";//发货失败
				}
			} finally {
				finish(urlPre, retCode==null?9999+"":retCode);
				
				//记录通知失败交易
				if(!retCode.equals("0000")){
		
					notifyMerMap.put(HFBusiDict.BANKID, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.BANKID)));
					notifyMerMap.put(HFBusiDict.STATE, "1");//通知失败
					Map<String,Object> resInfoFail = LocalUtil.doPostService("uprestbusi/upinformfailRest/" + rpid + ".xml", notifyMerMap);
					retCode = StringUtil.trim((String) resInfoFail.get(HFBusiDict.RETCODE));
					if(!retCode.equals(BusiConst.SUCCESS)){
						logInfo("失败通知交易记录入表失败,返回码%s",retCode);
						return retCode;
					}		
					logInfo("失败通知交易记录入表成功,返回码%s",retCode);
				}
				//根据通知结果更新发货状态
				
				Map<String,String> uporderMap = new HashMap<String,String>();
				uporderMap.put(HFBusiDict.PLATORDID, platordId);
				uporderMap.put(HFBusiDict.SHIPSTATE, notfiyResult);
				uporderMap.put(HFBusiDict.RPID, rpid);
		
				Map<String,Object> uporderInf = LocalUtil.doPostService("/uprestbusi/uporder/common/"+rpid+"/"+platordId+"-"+notfiyResult+".xml", uporderMap);
				retCode = StringUtil.trim((String) uporderInf.get(HFBusiDict.RETCODE));
				if(!retCode.equals(BusiConst.SUCCESS)){
					logInfo("update order更新订单发货状态失败,返回码%s",retCode);
					return retCode;
				}		
				logInfo("update order更新订单发货状态成功,返回码%s",retCode);
				out.put(HFBusiDict.SHIPSTATE, notfiyResult);
				
				
				
			}
			if("1".equals(notfiyResult)){//通知商户成功，判断是否下发商品信息
				Map<String,String> goodsMap = new HashMap<String,String>();
				goodsMap.put(HFBusiDict.MERID, merId);
				goodsMap.put(HFBusiDict.GOODSID, goodsId);
				
				//获取商品信息
				Map<String,Object> goodsInf = LocalUtil.doGetService("/hfrestbusi/goodsinf/"+rpid+"/"+merId+"-"+goodsId+".xml", goodsMap);
				retCode = goodsInf.get(HFBusiDict.RETCODE).toString();
				if(!BusiConst.SUCCESS.equals(retCode)){
					logInfo("获取商品信息失败：%s", goodsInf);
					return retCode;
				}
			    String goodsName = (String)goodsInf.get(HFBusiDict.GOODSNAME);
			    String goodsDesc = String.valueOf(goodsInf.get(HFBusiDict.GOODSDESC));
			    String cusPhone = (String)goodsInf.get(HFBusiDict.CUSPHONE);
			    String pushInf = String.valueOf(goodsInf.get(HFBusiDict.PUSHINF));
			   
			    String merProduct = StringUtil.trim((String)respMsg.get(HFBusiDict.RETMSG));
		//	    String merProduct ="卡密1234444447";
			    logInfo("从商户前置查询出的商品信息 merProduct["+merProduct+"]");
			    if(!"".equals(merProduct) && pushInf.equals("1")){ //下发商品描述
					Map<String,String> sendMap = new HashMap<String,String>();

					String isTest = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "isTest", "false");
					sendMap.put(HFBusiDict.RPID, rpid);
					sendMap.put(HFBusiDict.REQDATE, reqdate); //请求时间  
					sendMap.put(HFBusiDict.REQTIME, TimeUtil.time6()); //请求时间 
					sendMap.put(HFBusiDict.MERID, merId); //商户号 
					sendMap.put(HFBusiDict.GOODSID, goodsId); //商品号 
					sendMap.put(HFBusiDict.BANKID, StringUtil.trim((String) orderQueryResp.get(HFBusiDict.BANKID))); //银行号 
					sendMap.put(HFBusiDict.AMOUNT, StringUtil.trim(String.valueOf(orderQueryResp.get(HFBusiDict.AMOUNT)))); //支付金额

					sendMap.put(XmlMobile.FUNCODE, "UPSHXX");
					sendMap.put(HFBusiDict.CALLING, mobileId);
					sendMap.put(HFBusiDict.CUSPHONE, cusPhone);
					sendMap.put(HFBusiDict.GOODSDESC, merProduct);
					
					sendMap.put(HFBusiDict.SERVTYPE, StringUtil.trim(String.valueOf(goodsInf.get(HFBusiDict.SERVTYPE)))); //按次,包月
			
					//暂定seqId由merid代替
				    sendMap.put(HFBusiDict.SUBNO, merId);//短信长号码
					
					sendMap.put(HFBusiDict.RETCODE, BusiConst.SUCCESS);
					sendMap.put("ISNOTIFY", "TRUE");
					sendMap.put("ISTEST", isTest);
					logInfo("通知短信 %s" , sendMap);
//					(Service4QInf)BeansContext.getInstance().
					Service4QInf smsQueue = (Service4QInf)BeansContext.getInstance().getBean("smsQueue");
					logInfo("smsQueue: %s", smsQueue);
					smsQueue.putJob(sendMap);
			   }else{
				   logInfo("不下发短信-- pushInf [" + pushInf + "] merProduct["+merProduct+"]");
			   }
			}
			
			logInfo("通知商户处理完成,返回码%s,%s",retCode,platordId);
			
			out.put(HFBusiDict.SHIPSTATE, notfiyResult);
			out.putAll(orderQueryResp);
			out.putAll(respMsg);
			out.remove("LOCAL");
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
		return loadStrategyMerfront.lookup();
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
		loadStrategyMerfront.finish(url, retCode);
	}
}
