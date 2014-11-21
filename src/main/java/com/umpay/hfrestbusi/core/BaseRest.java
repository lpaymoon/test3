/** *****************  JAVA头文件说明  ****************
 * file name  :  BaseRest.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Oct 14, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.util.AlarmUtil;
import com.umpay.hfrestbusi.util.CheckReqUtil;
import org.apache.log4j.Logger;
import com.bs3.app.dal.DalHelper;
import com.bs3.app.dal.engine.Dalet4Base;
import com.bs3.inf.IBeans.BeansContextInf;
import com.bs3.inf.IProcessors.HSessionInf;
import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;
import com.umpay.hfrestbusi.exception.BusiException;
import com.umpay.hfrestbusi.util.StringUtil;




/** ******************  类说明  *********************
 * class       :  BaseRest
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  资源组件基类
 * @see        :                        
 * ************************************************/

public class BaseRest extends Dalet4Base {
	
	/**
	 * 日志工具
	 */
	protected  Logger logger = Logger.getLogger(getClass());

	/**
	 * 业务返回数据
	 */
	protected Map<String,Object> out = null;
	
	/**
	 * 交易流水号
	 */
	protected String rpid = null;
	/**
	 * 资源url
	 */
	protected String restUrl = null;
	
	/**
	 * 资源name
	 */
	protected String restName = null;

	/**
	 * 资源操作
	 */
	protected String restMethod = null;

	/**
	 * 系统配置文件
	 */
	protected Map<String,String> params = null;
	
	/**
	 * 上下文依赖注入——子类无法访问
	 */
	private BeansContextInf ctx = null;
	
	protected CommonDalInf dal = null;
	
	/**
	 * 设置资源的idname全部为id
	 */
	public String getIdName() {
		return "id";	
	}
	
	/**
	 * ********************************************
	 * method name   : iniContext 
	 * description   : 初始化响应数据
	 * @return       : void
	 * @param        : @param urlargs
	 * modified      : zhangwl ,  Oct 14, 2011
	 * @see          : 
	 * *******************************************
	 */
	private void iniContext(Map<String, String> urlargs) throws Exception{
		
		//读取系统配置信息
		params = NamedProperties.getMap(BusiConst.SYSPARAMS);
		try {
			NamedProperties.refresh(false);
		} catch (IOException e) {
			logException(e);
		}
		//初始化响应对象
		out = new HashMap<String,Object>();
		
		ctx = this.getBeansContext();
		dal = DalFactory.getDal();
		//1 去掉/hfrestbusi/，尽量减少日志文件大小，20120808
		this.restUrl = StringUtil.trim((String)urlargs.get(DalHelper.URI)).replace("/hfrestbusi/", "");
		if(urlargs.containsKey(BusiConst.LOCAL)){
			out.put(BusiConst.LOCAL,urlargs.get(BusiConst.LOCAL));
		};
		this.rpid = StringUtil.trim((String)urlargs.get(HFBusiDict.RPID));
	    String[] cName = urlargs.get(DalHelper.CLASS).split("\\.");		
	    this.restName = cName[cName.length - 1] + "." + restMethod;
		//this.logInfo("校验参数开始%s", urlargs);
		CheckReqUtil.checkReq(urlargs.get(DalHelper.CLASS),restMethod,urlargs,out);
		this.out.put(HFBusiDict.RETCODE, BusiConst.SYS_ERROR);
		
	}
	
	
	/**
	 * ********************************************
	 * method name   : doList 
	 * modified      : zhangwl ,  Oct 14, 2011
	 * description   : 对应资源的GET方法，url中没有{id}
	 * @see          : @see com.bs3.inf.IRestlets$Restlet4Adaptor#doList(com.bs3.inf.IProcessors.HSessionInf, java.lang.Object, java.util.Map)
	 * *******************************************
	 */
	public Object doList(HSessionInf session, Object req, Map<String, String> urlargs) throws Exception {		
		long startTime = System.currentTimeMillis();
		String retCode=BusiConst.SYS_ERROR;
		try{			
			this.restMethod=BusiConst.DOLIST;
			iniContext(urlargs);
			this.logInfo("context[%s] 业务逻辑开始", urlargs);
			retCode = doListService(urlargs);
		}catch(Exception e){
			e.printStackTrace();
			logException(e);
			if(BusiException.isBusiException(e)){
				retCode = e.getMessage();
			}
			try{
				doListException(urlargs);
			}catch(Exception ex){
				logException(ex);
			}			
		}finally{
			out.put(HFBusiDict.RETCODE, retCode);
			out.put(HFBusiDict.RETMSG, NamedProperties.getMapValue(BusiConst.MESSAGEPARAMS, retCode, ""));
			long endTime = System.currentTimeMillis();	
			long spendTime =endTime-startTime;
			out.put(BusiConst.BUSISPENDTIME, spendTime);			
			logMpsp();
			AlarmUtil.alarm(out);
//			this.logInfo("timespend[%s] out[%s] 业务逻辑结束", spendTime,out);		
			this.logInfo("timespend[%s] 业务逻辑结束", spendTime);	
		}
		return out;
	}
	
	/**
	 * ********************************************
	 * method name   : doCreate
	 * description   : 对应资源的POST方法，url中没有{id} 
	 * modified      : zhangwl ,  Oct 17, 2011
	 * @see          : @see com.bs3.inf.IRestlets$Restlet4Adaptor#doCreate(com.bs3.inf.IProcessors.HSessionInf, java.lang.Object, java.util.Map)
	 * *******************************************
	 */
	public Object doCreate(HSessionInf session, Object req, Map<String, String> urlargs) throws Exception {
		long startTime = System.currentTimeMillis();
		String retCode = BusiConst.SYS_ERROR;
		try{
			
			this.restMethod=BusiConst.DOCREATE;
			iniContext(urlargs);
			this.logInfo("context[%s] 业务逻辑开始", urlargs);
			retCode = doCreateService(urlargs);
		}catch(Exception e){
			e.printStackTrace();
			logException(e);
			if(BusiException.isBusiException(e)){
				retCode = e.getMessage();
			}
			try{
				doListException(urlargs);
			}catch(Exception ex){
				logException(ex);
			}			
		}finally{

			out.put(HFBusiDict.RETCODE, retCode);
			out.put(HFBusiDict.RETMSG, NamedProperties.getMapValue(BusiConst.MESSAGEPARAMS, retCode, ""));
			long endTime = System.currentTimeMillis();	
			long spendTime =endTime-startTime;
			out.put(BusiConst.BUSISPENDTIME, spendTime);			
			logMpsp();
			AlarmUtil.alarm(out);	
			this.logInfo("timespend[%s] out[%s] 业务逻辑结束", spendTime+"",out);		
		}
		return out;
	}
	
	/**
	 * ********************************************
	 * method name   : doShow 
	 * modified      : zhangwl ,  Oct 17, 2011
	 * description   : 对应资源的GET方法，url中有{id}
	 * @see          : @see com.bs3.inf.IRestlets$Restlet4Adaptor#doShow(com.bs3.inf.IProcessors.HSessionInf, java.lang.Object, java.util.Map, java.lang.String)
	 * *******************************************
	 */
	public Object doShow(HSessionInf session, Object req, Map<String, String> urlargs, String id) throws Exception {
		long startTime = System.currentTimeMillis();
		String retCode = BusiConst.SYS_ERROR;
		try{
			
			this.restMethod=BusiConst.DOSHOW;
			iniContext(urlargs);
			this.logInfo("context[%s] 业务逻辑开始", urlargs);
			retCode = doShowService(urlargs,id);
		}catch(Exception e){
			e.printStackTrace();
			logException(e);
			if(BusiException.isBusiException(e)){
				retCode = e.getMessage();
			}
			try{
				doListException(urlargs);
			}catch(Exception ex){
				logException(ex);
			}			
		}finally{
			out.put(HFBusiDict.RETCODE, retCode);
			out.put(HFBusiDict.RETMSG, NamedProperties.getMapValue(BusiConst.MESSAGEPARAMS, retCode, ""));
			long endTime = System.currentTimeMillis();	
			long spendTime =endTime-startTime;
			out.put(BusiConst.BUSISPENDTIME, spendTime);			
			logMpsp();
			AlarmUtil.alarm(out);
			this.logInfo("timespend[%s] out[%s] 业务逻辑结束", spendTime+"",out);			
		}
		return out;
	}
	
	/**
	 * ********************************************
	 * method name   : doUpdate 
	 * modified      : zhangwl ,  Oct 17, 2011
	 * description   : 对应资源的POST方法，url中有{id}
	 * @see          : @see com.bs3.inf.IRestlets$Restlet4Adaptor#doUpdate(com.bs3.inf.IProcessors.HSessionInf, java.lang.Object, java.util.Map, java.lang.String)
	 * *******************************************
	 */
	public Object doUpdate(HSessionInf session, Object req, Map<String, String> urlargs, String id) throws Exception {
		long startTime = System.currentTimeMillis();
		String retCode = BusiConst.SYS_ERROR;
		try{
			
			this.restMethod=BusiConst.DOUPDATE;
			iniContext(urlargs);
			this.logInfo("context[%s] 业务逻辑开始", urlargs);
			retCode = doUpdateService(urlargs,id);
		}catch(Exception e){
			e.printStackTrace();
			logException(e);
			if(BusiException.isBusiException(e)){
				retCode = e.getMessage();
			}
			try{
				doListException(urlargs);
			}catch(Exception ex){
				logException(ex);
			}			
		}finally{
			out.put(HFBusiDict.RETCODE, retCode);
			out.put(HFBusiDict.RETMSG, NamedProperties.getMapValue(BusiConst.MESSAGEPARAMS, retCode, ""));
			long endTime = System.currentTimeMillis();	
			long spendTime =endTime-startTime;
			out.put(BusiConst.BUSISPENDTIME, spendTime);			
			logMpsp();
			AlarmUtil.alarm(out);
			this.logInfo("timespend[%s] out[%s] 业务逻辑结束", spendTime+"",out);			
		}
		return out;
	}
	
	public String doListService(Map<String, String> urlargs) throws Exception {
		this.logInfo("context[%s] 未实现业务逻辑", urlargs);
		return BusiConst.SUCCESS;
	}
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		this.logInfo("context[%s] 未实现业务逻辑", urlargs);
		return BusiConst.SUCCESS;
	}
	public String doShowService(Map<String, String> urlargs, String id) throws Exception {
		this.logInfo("context[%s] 未实现业务逻辑", urlargs);
		return BusiConst.SUCCESS;
	}
	public String doUpdateService(Map<String, String> urlargs, String id) throws Exception {
		this.logInfo("context[%s] 未实现业务逻辑", urlargs);
		return BusiConst.SUCCESS;
	}
	
	public void doListException(Map<String, String> urlargs) throws Exception {
		this.logInfo("context[%s] 未实现异常业务逻辑", urlargs);	
	}
	public void doCreateException(Map<String, String> urlargs) throws Exception {
		this.logInfo("context[%s] 未实现异常业务逻辑", urlargs);	
	}
	public void doShowException(Map<String, String> urlargs, String id) throws Exception {
		this.logInfo("context[%s] 未实现异常业务逻辑", urlargs);		
	}
	public void doUpdateException(Map<String, String> urlargs, String id) throws Exception {
		this.logInfo("context[%s] 未实现异常业务逻辑", urlargs);	
	}
	
	
	/**
	 * ********************************************
	 * method name   : logException 
	 * description   : 打印整个异常栈
	 * @return       : void
	 * @param        : @param e
	 * modified      : zhangwl ,  Oct 14, 2011
	 * @see          : 
	 * *******************************************
	 */
	public void logException( Exception e){
		logger.error(StringUtil.getExceptionStr(restUrl+","+restMethod, e));
	}
	
	/**
	 * ********************************************
	 * method name   : logInfo 
	 * description   : 打印业务日志
	 * @return       : void
	 * @param        : @param log
	 * @param        : @param message
	 * @param        : @param args
	 * modified      : zhangwl ,  Oct 14, 2011
	 * @see          : 
	 * *******************************************
	 */
	public void logInfo(String message, Object...args){
		logger.info(String.format("%s,%s,",restUrl,restMethod)+String.format(message,args));
	}
	
	
	/**
	 * ********************************************
	 * method name   : logMpsp 
	 * description   : 打印mpsp日志
	 * @return       : void
	 * @param        : 
	 * modified      : lingling ,  Nov 4, 2011  11:41:02 AM
	 * @see          : 
	 * *******************************************
	 */
	public void logMpsp(){
		//内部调用不打印mpsp日志
		if(out.containsKey(BusiConst.LOCAL)){
			return;
		}
		
		//从Sys.mpsp配置中读取需要输出的标签列表
		String mpspConf = StringUtil.trim(params.get("sys.mpsp"));
		
		//没有查询到配置的时候不打印日志
		if(mpspConf.equals("")){
			return;
		}

//		String restName = StringUtil.trim((String)out.get(BusiConst.BUSIKEY));
		//查询out数据并打印日志
		StringBuffer sb = new StringBuffer();
//		sb.append(restName);
		out.put(HFBusiDict.RESTRACE, restName);
//		sb.append(",");
//		sb.append(restMethod);
//		sb.append(",");
		String[] tags = mpspConf.split(",");
		for(String t : tags){
			String keyStr = StringUtil.trim(CheckReqUtil.getDictMapping().get(t));
//			String valueStr = StringUtil.trim((String)out.get(keyStr));	
			String valueStr = getStr(keyStr);	
			sb.append(valueStr);	
			sb.append(",");
		}
	//	sb.append(",");
		sb.append((Long)out.get(BusiConst.BUSISPENDTIME)+"");
		out.remove("CLASS");
		this.out.remove(BusiConst.BUSIKEY);
		Logger.getLogger("MPSP").info(sb.toString());
	}
	
	
	
	public  String getStr(String key){
		Object value = out.get(key);
		if(value == null){
			return "";
		}else{
			if(value instanceof String){
				return (String)value;
			}else{
				return value.toString();
			}
		}
	}
	
	
}



