package com.umpay.hfrestbusi.util;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import com.bs.mpsp.busisvr.tools.sms.SmsCenter;
import com.bs.mpsp.busisvr.tools.sms.SmsSender;
import com.bs2.inf.Datalet2Inf;
import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.constants.BusiConst;
 
/**
 * ******************  类说明  *********************
 * class       :  SmsSender
 * @author     :  lingling
 * @version    :  1.0  
 * description :  短信发送工具类
 * @see        :                        
 * ***********************************************
 */
public class SmsSenderUtil implements Datalet2Inf {

	public static Logger logger = Logger.getLogger(SmsSenderUtil.class);
	
	/**
	 * ********************************************
	 * method name   : onData 
	 * modified      : lingling ,  Nov 21, 2011
	 * @see          : @see com.bs2.inf.Datalet2Inf#onData(java.lang.Object)
	 * *******************************************
	 */
	public void onData(Object sms) {
		String ip = "";
//		try{
//			ip =InetAddress.getLocalHost().getHostAddress();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		try{
			ip = SystemHelper.getSystemLocalIp().getHostAddress();
			if(ip==null||"".equals(ip)){
				ip = SystemHelper.getSystemHostName();
			}
		}catch(Exception e){
			e.printStackTrace();
			try{
				ip = SystemHelper.getSystemHostName();
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
		
		
		//短信前缀
		StringBuffer sb = new StringBuffer();
		sb.append("REST(");
		sb.append(ip);
		sb.append("):");
		
		//短信内容
		if(sms instanceof List) {
			List<String> batch = (List<String>)sms;	//Vector		
			sb.append(this.doBatch(batch));
		}else{
			sb.append((String) sms);
		}
		if(sb.length()>70){
			sb.setLength(70);
		}
		
		String mobiles = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.alarm.mobiles", "13488725450");		
		String rpid = "HR"+SequenceUtil.getInstance().formatSequence(SequenceUtil.getInstance().getSequence4File("warn.rpid"), 11);			
		
		SmsSender sender=SmsCenter.getInstance();		
		sender.sendSms(false, rpid, "",mobiles,SmsSender.SMS_FMT_CN,sb.toString());
		logger.info("发送报警短信-"+rpid+"-10658008-"+mobiles+"-"+sb.toString());
	}
	
	/**
	 * ********************************************
	 * method name   : doBatch 
	 * description   : 获取批量队列数据
	 * @return       : String
	 * @param        : @param v
	 * @param        : @return
	 * modified      : lingling ,  Nov 21, 2011  8:04:13 PM
	 * @see          : 
	 * *******************************************
	 */
	protected String doBatch(List<String> v) {
		StringBuffer sb = new StringBuffer();
		Set<String> strSet = new HashSet<String>(); 
		for(String s : v){
			//去除重复报警短信
			if(!strSet.contains(s)){
				sb.append(s);
				sb.append(",");
				strSet.add(s);
			}			
		}
		return sb.toString();
	}
}
