package com.umpay.hfrestbusi.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.bs3.utils.NamedProperties;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.umpay.hfrestbusi.constants.BusiConst;

public class HTTPSendUtil {

	private static final Log log = LogFactory.getLog(HTTPSendUtil.class);
	private static MultiThreadedHttpConnectionManager httpConnectionManager;
	private static HttpClient client;
	private static  XStream xstream;
	private static IdleConnectionTimeoutThread monThread;
//	private static  MyStream.BStreamInf bs;
	static{
		if(client==null){
			httpConnectionManager = new MultiThreadedHttpConnectionManager();
			client = new HttpClient(httpConnectionManager);
			 //每主机最大连接数和总共最大连接数，通过hosfConfiguration设置host来区分每个主机  
	        client.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(100);
	        client.getHttpConnectionManager().getParams().setMaxTotalConnections(400);
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
	        //client.getHttpConnectionManager().getParams().setSoTimeout(10000);
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
//	        try {
//				bs = (MyStream.BStreamInf)MyStream.getStreamCodec("JavaRpc");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}	
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
	public static Object getHttpResGet_JSON(String url, Map m){
		GetMethod get = new GetMethod(url);
		try {
			get.getParams().setVersion(HttpVersion.HTTP_1_1);
    		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
    		get.setRequestHeader("Connection" , "Keep-Alive");
    		NameValuePair[] data = getData(m);
    		get.setQueryString(data);
    		client.executeMethod(get);
    		if (get.getStatusCode() == HttpStatus.SC_OK){ 
    			String reader = get.getResponseBodyAsString();
    			return JSONObject.parseObject(reader, Map.class);
    		}else{
    			return null;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			get.releaseConnection();
			// 游戏基地释放链接   不这么做的话会出现NoHttpResponseException
			// 这是临时解决方案   需要做个配置    能关掉
			String closeFlag = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "yxjd.http.close", "0");
			if("0".equals(closeFlag)){
				log.info("getHttpResGet_JSON需要立刻关闭连接。");
				client.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
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
			post.setRequestHeader("Connection" , "Keep-Alive");
			String body = xstream.toXML(m);
			RequestEntity entity= new StringRequestEntity(body, "text/xml", "utf-8");
			post.setRequestEntity(entity);
    		client.executeMethod(post);
    		if (post.getStatusCode() == HttpStatus.SC_OK){ 
//    			log.info(post.getResponseBodyAsString());
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
				// TODO Auto-generated catch block
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
			post.setRequestHeader("Connection" , "Keep-Alive");
			NameValuePair[] data = getData(m);
			post.setRequestBody(data);
			int statusCode = client.executeMethod(post);
			if(statusCode == HttpStatus.SC_OK){
//				return bs.unserialize(post.getResponseBody(), Map.class);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			post.releaseConnection();
//			client.getHttpConnectionManager().closeIdleConnections(10000);
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
	public static Object getHttpResPost_Form(String url, Map m) {
		PostMethod post = new PostMethod(url);
		Reader reader = null;
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			post.setRequestHeader("Connection" , "Keep-Alive");
			NameValuePair[] data = getData(m);
			post.setRequestBody(data);
			int statusCode = client.executeMethod(post);
			if(statusCode == HttpStatus.SC_OK){
//				return bs.unserialize(post.getResponseBody(), Map.class);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			post.releaseConnection();
//			client.getHttpConnectionManager().closeIdleConnections(10000);
		}
	}

	public static Object getHttpResPost_JSON(String url, Map m) {
		PostMethod post = new PostMethod(url);
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			post.setRequestHeader("Connection" , "Keep-Alive");
			NameValuePair[] data = getData(m);
			post.setRequestBody(data);
			int statusCode = client.executeMethod(post);
			if(statusCode == HttpStatus.SC_OK){
				String reader = post.getResponseBodyAsString();
    			return JSONObject.parseObject(reader, Map.class);
            }else{
            	return null;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			post.releaseConnection();
			// 游戏基地释放链接   不这么做的话会出现NoHttpResponseException
			// 这是临时解决方案   需要做个配置    能关掉
			String closeFlag = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "yxjd.http.close", "0");
			if("0".equals(closeFlag)){
				log.info("getHttpResPost_JSON需要立刻关闭连接。");
				client.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
	}
	public static Object getHttpResPost_Xstream(String _url, Map _map){
		Reader reader = null;
		PostMethod post = new PostMethod(_url);
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			post.setRequestHeader("Connection" , "Keep-Alive");
			String body = xstream.toXML(_map);
			RequestEntity entity= new StringRequestEntity(body, "text/xml", "utf-8");
			post.setRequestEntity(entity);
    		client.executeMethod(post);
    		if (post.getStatusCode() == HttpStatus.SC_OK){ 
//    			log.info(post.getResponseBodyAsString());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			post.releaseConnection();
		}
	}

	public static Object getHttpResPost_Xstream(String _url, List<Map> _list) {
		Reader reader = null;
		PostMethod post = new PostMethod(_url);
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			post.setRequestHeader("Connection" , "Keep-Alive");
			String body = xstream.toXML(_list);
			RequestEntity entity= new StringRequestEntity(body, "text/xml", "utf-8");
			post.setRequestEntity(entity);
    		client.executeMethod(post);
    		if (post.getStatusCode() == HttpStatus.SC_OK){ 
//    			log.info(post.getResponseBodyAsString());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			post.releaseConnection();
		}
	}

	public static byte[] getHttpResPost_Xstream(String _url, byte[] sendByte) {
		Reader reader = null;
		PostMethod post = new PostMethod(_url);
//		ByteArrayInputStream en = null;
		try {
			post.getParams().setVersion(HttpVersion.HTTP_1_1);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			post.setRequestHeader("Connection" , "Keep-Alive");
			//			String body = xstream.toXML(r4Map);
//			RequestEntity entity= new StringRequestEntity(body, "text/xml", "utf-8");
//			post.setRequestEntity(entity);
			RequestEntity en = new ByteArrayRequestEntity(sendByte);
			post.setRequestEntity(en);
//			en = new ByteArrayInputStream(sendByte);
//			post.setRequestBody(en);
    		client.executeMethod(post);
    		System.out.println("++++++++++++++++++++++++++++++++" + post.getStatusCode() );
    		if (post.getStatusCode() == HttpStatus.SC_OK){ 
//    			log.info(post.getResponseBodyAsString());
    			return post.getResponseBody();
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
