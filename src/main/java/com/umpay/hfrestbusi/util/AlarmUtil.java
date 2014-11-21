/** *****************  JAVA头文件说明  ****************
 * file name  :  AlarmUtil.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 17, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.util;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import net.sf.ehcache.Element;
import com.umpay.hfrestbusi.cache.DefaultCacheHelper;
import org.apache.log4j.Logger;
import com.umpay.hfbusi.HFBusiDict;
import com.bs2.inf.Service4QInf;
import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.bs3.ioc.core.BeansContext;

/** ******************  类说明  *********************
 * class       :  AlarmUtil
 * @author     :  lingling
 * @version    :  1.0  
 * description :  异常报警工具类
 * @see        :                        
 * ************************************************/

public class AlarmUtil {
	
	protected static Logger logger = Logger.getLogger(AlarmUtil.class);
	private static final String Sended_SMS_Count= "intSendedCount";
	/**
	 * ********************************************
	 * method name   : alarm 
	 * description   : 报警功能
	 * @return       : void
	 * @param        : @param out
	 * modified      : lingling ,  Nov 17, 2011  7:46:50 PM
	 * @see          : 
	 * *******************************************
	 */
	public static void alarm(Map<String,Object> _out){
		Map<String, Object> out = new HashMap<String, Object>();
		out.putAll(_out);
		try{
			//获取报警配置信息
			//需要进行报警的返回码
			String alarmRetCode = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.alarm.retcode", "");
			//需要报警的业务处理时间
			String alarmSpendTime = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.alarm.spendtime", "5000");
			//新报警开关,默认开启
			String charge = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.alarm.charge", "true");
						
			//业务返回码
			String retCode = (String)out.get(HFBusiDict.RETCODE);
			
			//1、验证返回码是否需要报警
			if(alarmRetCode.indexOf(retCode)!=-1){
				if("true".equalsIgnoreCase(charge)){
					send2AlarmCenter(retCode,out);
				}else{
					alarm(retCode,out);
				}
			}		
			
			//业务处理时间
			long spendTime = (Long) out.get(BusiConst.BUSISPENDTIME);
			long _spendTime = Long.parseLong(alarmSpendTime);
			if(spendTime>_spendTime){
				if ("true".equalsIgnoreCase(charge)) {
					out.put("realRetCode", retCode);
					send2AlarmCenter("9995", out);
				} else {
					alarm(BusiConst.BUSISPENDTIME, out);
				}
			}
		}catch(Exception e){
			//报警异常
			e.printStackTrace();
		}	
	}
	
	/** *****************  方法说明  *****************
	 * method name   :  send2AlarmCenter
	 * @param		 :  @param retCode
	 * @param		 :  @param out
	 * @return		 :  void
	 * @author       :  LiuJiLong 2014-4-14 上午10:47:42
	 * description   :  发送至报警中心
	 * @see          :  
	 * ***********************************************/
	private static void send2AlarmCenter(String retCode, Map<String, Object> out) {
		String reIp = null;
		try {
			reIp = SystemHelper.getSystemLocalIp().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(reIp!=null&&reIp.matches("\\d+.\\d+.\\d+.\\d+")&&reIp.split("\\.").length==4){
			out.put(HFBusiDict.ADDRESS, reIp.split("\\.")[3]);
		}else{
			out.put(HFBusiDict.ADDRESS, "127.0.0.1");
		}
		out.put(HFBusiDict.SERVICEID, "REST");
		out.put(HFBusiDict.RETCODE, retCode);
		logger.info("发送报警:" + retCode);
		
		Service4QInf alarmQueue = (Service4QInf)BeansContext.getInstance().getBean("alarmQueue");
		alarmQueue.putJob(out);
	}

	/**
	 * ********************************************
	 * method name   : alarm 
	 * description   : 根据返回码进行报警
	 * @return       : void
	 * @param        : @param retCode
	 * modified      : lingling ,  Nov 17, 2011  8:21:14 PM
	 * @see          : 
	 * *******************************************
	 */
	public static void alarm(String retCode,Map<String,Object> out) throws Exception{
		//警报缓存的tags
		String alarmTags = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.alarm.tags", "");
		//报警累计次数
		String alarmTimes = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.alarm.times", "3");
		int times = Integer.parseInt(alarmTimes);
		//警报在一定时间内达到报警次数才报警
		String alarmInterval = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.alarm.interval", "2000");
		long interval = Integer.parseInt(alarmInterval);
		
		String maxSms = NamedProperties.getMapValue(BusiConst.SYSPARAMS,"sys.alarm.maxsms","5");
		long intMaxSms = Integer.parseInt(maxSms);
		
		String[] tagsStr = alarmTags.split(",");
		//没有配置报警标签时退出
		if(tagsStr.length==0){
			return;
		}
		
		//报警标签配置方式为tag1.tag2,tag1.tag3,...
		for(String tagArray : tagsStr){
			String[] tagArr = tagArray.split("[.]");
			if(tagArr.length==0){
				continue;
			}
			StringBuffer sb= new StringBuffer();
			sb.append(retCode);
			for(String tag:tagArr){
				sb.append(".");
				Object value = out.get(tag);
				sb.append(value);				
			}
			String key = sb.toString();
			Map<String,String> map = new HashMap<String,String>();
			Element cache = CacheUtil.getElement("alarm", key, map, new DefaultCacheHelper(null));
			Map<String,Object> cacheInfo = (Map<String,Object>)cache.getValue();
			if(cacheInfo.get(Sended_SMS_Count)==null){
				cacheInfo.put(Sended_SMS_Count,new Integer(0));
			}
			long inter = System.currentTimeMillis() - cache.getLastUpdateTime();
			
			//更改cache的更新时间，用于计算上次报警时间
			cache.updateUpdateStatistics();
			Integer intSendedCount = (Integer)cacheInfo.get(Sended_SMS_Count);
			//累计次数超限并且间隔时间过小报警
			if(intSendedCount<intMaxSms&&times<cache.getHitCount()&&interval>inter){	
				logger.warn(String.format("key[%s] out[%s] 下发报警短信", key,out));
//				String machineFlag = "";//机器标识
//				try{
//					machineFlag = SystemHelper.getSystemLocalIp().getHostAddress();
//					if(machineFlag==null||"".equals(machineFlag)){
//						machineFlag = SystemHelper.getSystemHostName();
//					}
//				}catch(Exception e){
//					e.printStackTrace();
//					try{
//						machineFlag = SystemHelper.getSystemHostName();
//					}catch(Exception ee){
//						ee.printStackTrace();
//					}
//				}
				//短信前缀
				StringBuffer smsContent = new StringBuffer();
//				smsContent.append("HFRESTBUSI(");
//				smsContent.append(machineFlag);
//				smsContent.append("):").append(key);
				smsContent.append(key);
				Service4QInf queue = (Service4QInf)BeansContext.getInstance().getBean("SmsSenderQueue");
				queue.putJob(smsContent.toString());
				intSendedCount++;
				cacheInfo.put(Sended_SMS_Count, intSendedCount);	
			}				
		}
	}
	
}



