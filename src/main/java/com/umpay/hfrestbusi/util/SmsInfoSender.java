package com.umpay.hfrestbusi.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.bs2.inf.Datalet2Inf;
import com.bs3.ioc.core.BeansContext;
import com.umpay.hfbusi.HFBusiDict;

import com.umpay.loadstrategy.base.LoadStrategyInf;
 

public class SmsInfoSender implements Datalet2Inf {

	private static Logger log = Logger.getLogger(SmsInfoSender.class);

	private static LoadStrategyInf loadStrategy;

	static{
		loadStrategy = (LoadStrategyInf)BeansContext.getInstance().getBean("loadStrategySms");//初始化短信解析负载组件
	}
    
	public void onData(Object smsMapInfo) {
		Vector batch = (Vector)smsMapInfo;	
//		Map<String,String> paramMap = (Map<String,String>)smsMapInfo;
		Map<String,String> paramMap=doBatch(batch);
		log.info("***执行队列中的发送短信任务****");
		
		/*
		 * 20140304 liujilong 修改短信接口为策略负载模式
		 */
		String retCode = "";
		String url = getSmsSrvPath();
		try {
			Map<String, String> respMsg = (Map<String, String>) HTTPSendUtil.getHttpResPost_Xstream("http://" + url + "/hfdownsms",
					paramMap);
			if(respMsg!=null){
				retCode = respMsg.get(HFBusiDict.RETCODE);
			}
			if ("0000".equals(retCode)) {
				log.info("短信发送成功:[rpid]" + paramMap.get(HFBusiDict.RPID) + ","
						+ paramMap.get(HFBusiDict.CALLING));
			} else {
				log.info("短信发送失败:[rpid]" + paramMap.get(HFBusiDict.RPID)
						+ "calling[ " + paramMap.get(HFBusiDict.CALLING)
						+ " ],called [" + paramMap.get(HFBusiDict.CALLED)
						+ " ], retcode[ " +retCode+ "] ");
			}
		} finally {
			finish(url, retCode);
		}
		
	}
	

	
	/** *****************  方法说明  *****************
	 * method name   :  getSmsSrvPath
	 * @param		 :  @return
	 * @return		 :  String
	 * @author       :  LiuJiLong 2014-3-4 上午10:54:48
	 * description   :  策略负载获得URL
	 * @see          :  
	 * ***********************************************/
	private String getSmsSrvPath(){
		return loadStrategy.lookup();
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
		loadStrategy.finish(url, retCode);
	}
	
	
	 /**
	  * 发送参数组装
	  * @param params
	  * @return
	  */
	 private static Map<String, String> doBatch(
			 Vector params) {
			Map<String, String> paramMap = new HashMap<String, String>();
			Iterator it=params.iterator();
			while(it.hasNext()){
				Map<String,String> s= (HashMap<String,String>)it.next();
				paramMap.putAll(s);
			}
			return paramMap;
		}
}
