package com.umpay.hfrestbusi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class HTTPUtil {

	private static final Logger logger = Logger.getLogger(HTTPUtil.class);
	private static MultiThreadedHttpConnectionManager httpConnectionManager;
	private static HttpClient client;
	private static  XStream xstream;
	private static IdleConnectionTimeoutThread monThread;
	
	public static final String REQUEST_TYPE_XSTREAM = "XSTREAM";
	
	public static final String REQUEST_TYPE_FORM = "FORM";
	
	static{
		if(client==null){
			httpConnectionManager = new MultiThreadedHttpConnectionManager();
			client = new HttpClient(httpConnectionManager);
			 //每主机最大连接数和总共最大连接数，通过hosfConfiguration设置host来区分每个主机  
	        client.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(100);
	        client.getHttpConnectionManager().getParams().setMaxTotalConnections(400);
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
	        
	        //modify by yangwr 2012-07-20 客户端断链时间可配置
	        client.getHttpConnectionManager().getParams().setSoTimeout(60000);
	        client.getHttpConnectionManager().getParams().setTcpNoDelay(true);
	        client.getHttpConnectionManager().getParams().setLinger(-1);
	        client.getHttpConnectionManager().getParams().setStaleCheckingEnabled(false);
	        xstream = new XStream(new DomDriver());
	        // 创建线程   
	        monThread = new IdleConnectionTimeoutThread();   
//	        // 注册连接管理器   
	        monThread.addConnectionManager(httpConnectionManager);
	        //为保证客户端先断连，避免丢包现象，各个时间设置应遵循以下公式
	        //服务端断连时间-客户端断连时间（connectionTimeout)>=客户端回收空闲链路时间（timeoutInterval）
	        monThread.setConnectionTimeout(19000L);
	        monThread.setTimeoutInterval(5000L);
//	        // 启动线程   
	        monThread.start();
		}
		
	}
	
	
	public static void destory(){
		if(client!=null){
			httpConnectionManager.shutdown();
			monThread.shutdown();
			monThread=null;
			httpConnectionManager=null;
			client=null;
		}
	}
	
	
	/** ********************************************
	 * method name   : getHttpResXstreamGet 
	 * description   : Xstream序列化方式发生http请求_Get
	 * @return       : Map
	 * @param        : @param urlstr
	 * @param        : @param m
	 * @param        : @return
	 * modified      : zhaowei ,  2011-11-16  下午02:58:11
	 * @see          : 
	 * ********************************************/      
	public static Object getHttpResGet_Form(Map m,GetMethod get){
		BufferedReader reader = null;
		try {
			get.setRequestHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
			get.setRequestHeader("Connection", "Keep-Alive"); 
			get.setRequestHeader("Pragma", "no-cache"); 
			get.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
			
			Object host = m.remove("Host");
			if(host != null){
				get.setRequestHeader("Host", "cmpay.dalasu.com"); 
			}
			else{
				get.setRequestHeader("Host", "ospd.mmarket.com:8089"); 
			}
			
			String referer = StringUtil.trim((String)m.remove("Referer"));
			if(referer.length() > 0){
				get.setRequestHeader("Referer", referer); 
			}
			
			get.getParams().setVersion(HttpVersion.HTTP_1_1);
    		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			NameValuePair[] data = getData(m);
    		get.setQueryString(data);
    		
    		boolean exception = false;
    		int status = 0;
    		int i = 0;
    		do{
    			try {
    				logger.info("请求次数：" + i);
    				status = client.executeMethod(get);
    				exception = false;
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
					exception = true;
				}finally{
					i++;
				}
    		}while(exception&&i<3);
    			
    		logger.info("HTTP RETCODE：" + HttpStatus.SC_OK);
    		if (status == HttpStatus.SC_OK){ 
    			reader = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(), Charset.forName("utf-8")));  
    			
    			StringBuffer stringBuffer = new StringBuffer();  
    			String str = "";  
    			while((str = reader.readLine())!=null){  
    			    stringBuffer.append(str);  
    			}  
    			String ts = stringBuffer.toString();  
    			
    			return ts;
    		}else{
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader!= null)reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			get.releaseConnection();
		}
	}
	
	/**
	 * 
	 * @param url 请求地址
	 * @param requestMap 请求参数
	 * @param requestType  请求类型（form方式提交或者xtream序列化方式   一般form即可）  
	 * 						取值（HTTPUtil.REQUEST_TYPE_FORM 或者  HTTPUtil.REQUEST_TYPE_XSTREAM）
	 * 						如果不是以上两个值  抛出异常
	 * @return
	 * @throws BusiException 请求类型出错86170206
	 */
	public static Object post(String url, Map<String, String> requestMap, String requestType){
		PostMethod postMethod = new PostMethod(url);
		
		if(REQUEST_TYPE_FORM.equalsIgnoreCase(requestType)){
			return getHttpResPost_Form(requestMap, postMethod);
		}
		
		else if(REQUEST_TYPE_XSTREAM.equalsIgnoreCase(requestType)){
			return getHttpResPost_Xstream(requestMap, postMethod);
		}
		
		// 如果类型未知  默认以xtream序列化方式请求  
		else{
			logger.error("http.post的请求类型未知[" + url + "][" + requestType + "]");
			return getHttpResPost_Xstream(requestMap, postMethod);
		}
	}
	
	public static Object get(String url, Map<String, String> requestMap){
		logger.info("Get.url = " + url);
		return getHttpResGet_Form(requestMap, new GetMethod(url));
	}
	
	/** ********************************************
	 * method name   : getHttpResXstreamPost 
	 * description   : Xstream序列化方式发生http请求_Post
	 * @return       : Map
	 * @param        : @param urlstr
	 * @param        : @param m
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhaowei ,  2011-11-15  上午11:15:29
	 * @see          : 
	 * ********************************************/      
	public static Object getHttpResPost_Xstream(Map m,PostMethod post){
		Reader reader = null;
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			String body = xstream.toXML(m);
			RequestEntity entity= new StringRequestEntity(body, "text/xml", "utf-8");
			post.setRequestEntity(entity);
    		client.executeMethod(post);
    		if (post.getStatusCode() == HttpStatus.SC_OK){ 
    			reader = new InputStreamReader(post.getResponseBodyAsStream(),"utf-8");
    			return xstream.fromXML(reader);
    		}else{
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader!= null)reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			post.releaseConnection();
		}
	}
	
	
	
	
	/** ********************************************
	 * method name   : getHttpResForm 
	 * description   : form表单形式提交http请求
	 * @return       : Map
	 * @param        : @param urlstr
	 * @param        : @param m
	 * @param        : @return
	 * @param        : @throws Exception
	 * modified      : zhaowei ,  2011-11-15  上午11:14:53
	 * @see          : 
	 * ********************************************/      
	public static Object getHttpResPost_Form(Map m,PostMethod post) {
		Reader reader = null;
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			NameValuePair[] data = getData(m);
			post.setRequestBody(data);
			int statusCode = client.executeMethod(post);
			if(statusCode == HttpStatus.SC_OK){
    			reader = new InputStreamReader(post.getResponseBodyAsStream());
				return xstream.fromXML(reader);
            }else{
            	return null;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader!= null)reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			post.releaseConnection();
		}
	}

	public static NameValuePair[] getData(Map mp){
		//过滤空值
		Map map = new HashMap();
		Iterator it = mp.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if(value!=null){
				map.put(key, value);
			}
		}
		NameValuePair[] data = new NameValuePair[map.size()];
		it = map.entrySet().iterator();
		int j = 0;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if(value!=null){
				data[j] = new NameValuePair(key.toString(), value.toString());
				j++;
			}
		}
		return data;
	}


	public static Object getHttpResPost_Xstream(String _url, List<Map> _list) {
		Reader reader = null;
		PostMethod post = new PostMethod(_url);
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			String body = xstream.toXML(_list);
			RequestEntity entity= new StringRequestEntity(body, "text/xml", "utf-8");
			post.setRequestEntity(entity);
    		client.executeMethod(post);
    		if (post.getStatusCode() == HttpStatus.SC_OK){ 
    			reader = new InputStreamReader(post.getResponseBodyAsStream(),"utf-8");
    			return xstream.fromXML(reader);
    		}else{
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader!= null)reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			post.releaseConnection();
		}
	}
	
	
}
