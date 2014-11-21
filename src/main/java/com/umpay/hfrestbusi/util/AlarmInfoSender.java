package com.umpay.hfrestbusi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bs2.inf.Datalet2Inf;
import com.bs3.ioc.core.BeansContext;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.loadstrategy.base.LoadStrategyInf;
 

/** ******************  类说明  ******************
 * class       :  AlarmInfoSender
 * date        :  2014-4-14 
 * @author     :  LiuJiLong
 * @version    :  V1.0  
 * description :  报警发送
 * @see        :                         
 * ***********************************************/
public class AlarmInfoSender implements Datalet2Inf {

	private static Logger log = Logger.getLogger(AlarmInfoSender.class);

	private static LoadStrategyInf loadStrategy;

	static{
		loadStrategy = (LoadStrategyInf)BeansContext.getInstance().getBean("loadStrategyAlarm");//初始化短信解析负载组件
	}
    
	public void onData(Object smsMapInfo) {
		List<Map> paramList = (List<Map>)smsMapInfo;
		log.info("***执行队列中的发送报警任务****");
		
		String retCode = null;
		String url = getSrvPath();
		try {
			Map<String, String> respMsg = (Map<String, String>) HTTPSendUtil.getHttpResPost_Xstream("http://" + url + "/hfalarm",
					paramList);
			if(respMsg!=null){
				retCode = respMsg.get(HFBusiDict.RETCODE);
			}
			if ("0000".equals(retCode)) {
				log.info("报警信息发送成功");
			} else {
				log.info("报警信息发送失败 retcode[ " + retCode + "]");
			}
		} finally {
			finish(url, retCode==null?9999+"":retCode);
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
	private String getSrvPath(){
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
}
