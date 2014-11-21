package com.umpay.hfrestbusi.rest.branch;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.HTTPSendUtil;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/** ******************  类说明  ******************
 * class       :  YXJDReqVerifyCode
 * date        :  2014-9-30 
 * @author     :  Roy
 * @version    :  V1.0  
 * description :  
 * SHOW:获取SESSIONID
 * UPDATE:请求验证码
 * CREATE:确认验证码
 * @see        :                         
 * ***********************************************/
public class YXJDReqVerifyCode extends BaseRest {
	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 */
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id) {
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		
		String url = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"YXJD.VERIFYCODEREQ.URL", "http://g.10086.cn/pay/open/index");
		String key = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"YXJD.MDKEY", "38b9d080e00bd62345562fee8121239d");
		
		String appid = "ldysyx";
		String salechannelid = "41045000";//结算ID
		String method = "applyforpurchase";
		String seconds = System.currentTimeMillis()/1000 + "";
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		
		// modify by fanxiangchi 20141016  ###set serviceid to consumecode
		String consumecode = StringUtil.trim(urlargs.get(HFBusiDict.SERVICEID));
		
		/*
		 * 获取SESSIONKEY
		 */
		Map<String, String> sessionReqMap = new HashMap<String, String>();
		Map<String, Object> sessionRs = LocalUtil.doGetService("/uprestbusi/branch/0620/"+rpid+"/"+merId+"-"+goodsId+"-YXJD.xml", sessionReqMap);
		String sessionkey = StringUtil.trim((String)sessionRs.get("sessionkey") + "");
		if (!"0000".equals(sessionRs.get(HFBusiDict.RETCODE))
				|| sessionkey == null || "null".equals(sessionkey)
				|| "".equals(sessionkey)) {
			return "86801158";//游戏基地SESSIONKEY获取失败
		}
		
		Map<String, String> reqVerifyCodeMap = new HashMap<String, String>();
		reqVerifyCodeMap.put("app", appid);
		reqVerifyCodeMap.put("tel", mobileId);
		reqVerifyCodeMap.put("time", seconds);
		reqVerifyCodeMap.put("format", "json");
		reqVerifyCodeMap.put("method", method);
		reqVerifyCodeMap.put("sessionkey", sessionkey);
		reqVerifyCodeMap.put("salechannelid", salechannelid);
		reqVerifyCodeMap.put("consumecode", consumecode);
		StringBuffer md5Str = new StringBuffer();
		md5Str.append("app=").append(appid).append("&")
					.append("method=").append(method).append("&")
					.append("tel=").append(mobileId).append("&")
					.append("consumecode=").append(consumecode).append("&")
					.append("time=").append(seconds).append("&")
					.append("sessionkey=").append(sessionkey).append("&")
					.append("key=").append(key);
		reqVerifyCodeMap.put("hash", DigestUtils.md5Hex(md5Str.toString()));
		
		logInfo("游戏基地验证码，请求参数:%s | %s", reqVerifyCodeMap, url);
		Map<String, Object> verifyCodeRs  = (Map<String, Object>) HTTPSendUtil.getHttpResPost_JSON(url, reqVerifyCodeMap);
		logInfo("游戏基地验证码，返回数据:%s", verifyCodeRs);
		
		if(verifyCodeRs==null){
			return "86801159";//游戏基地验证码请求失败
		}
		
		
		out.put(HFBusiDict.BANKTRACE, verifyCodeRs.get("orderid") + "");
		String resultCode = verifyCodeRs.get("resultCode") + "";
		if("200000".equals(resultCode)||"2000".equals(resultCode)){
			return BusiConst.SUCCESS;
		}else{
			return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "retcode.yxjd." + resultCode, "86801008");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doCreateService(java.util.Map)
	 */
	public String doCreateService(Map<String, String> urlargs){
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String bankTrace = StringUtil.trim(urlargs.get(HFBusiDict.BANKTRACE));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String verifycode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));
		
		String url = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"YXJD.VERIFYCODECONFIRM.URL", "http://g.10086.cn/pay/open/index");
		String key = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"YXJD.MDKEY", "38b9d080e00bd62345562fee8121239d");
		
		String appid = "ldysyx";
		String salechannelid = "41045000";
		String method = "confirmpurchase";
		String seconds = System.currentTimeMillis()/1000 + "";
		
		/*
		 * 获取SESSIONKEY
		 */
		Map<String, String> sessionReqMap = new HashMap<String, String>();
		Map<String, Object> sessionRs = LocalUtil.doGetService("/uprestbusi/branch/0620/"+rpid+"/"+merId+"-"+goodsId+"-YXJD.xml", sessionReqMap);
		String sessionkey = StringUtil.trim((String)sessionRs.get("sessionkey"));
		if (!"0000".equals(sessionRs.get(HFBusiDict.RETCODE))
				|| sessionkey == null || "null".equals(sessionkey)
				|| "".equals(sessionkey)) {
			return "86801158";//游戏基地SESSIONKEY获取失败
		}
		
		Map<String, String> reqVerifyCodeMap = new HashMap<String, String>();
		reqVerifyCodeMap.put("app", appid);
		reqVerifyCodeMap.put("time", seconds);
		reqVerifyCodeMap.put("format", "json");
		reqVerifyCodeMap.put("method", method);
		reqVerifyCodeMap.put("orderid", bankTrace);
		reqVerifyCodeMap.put("verifycode", verifycode);
		reqVerifyCodeMap.put("sessionkey", sessionkey);
		reqVerifyCodeMap.put("salechannelid", salechannelid);
		StringBuffer md5Str = new StringBuffer();
		md5Str.append("app=").append(appid).append("&")
					.append("method=").append(method).append("&")
					.append("verifycode=").append(verifycode).append("&")
					.append("orderid=").append(bankTrace).append("&")
					.append("time=").append(seconds).append("&")
					.append("sessionkey=").append(sessionkey).append("&")
					.append("key=").append(key);
		reqVerifyCodeMap.put("hash", DigestUtils.md5Hex(md5Str.toString()));
		logInfo("游戏基地确认支付,请求参数:%s | %s", reqVerifyCodeMap, url);
		Map<String, Object> verifyCodeRs = (Map<String, Object>) HTTPSendUtil.getHttpResPost_JSON(url, reqVerifyCodeMap);
		
		if(verifyCodeRs==null){
			return "86801160";//游戏基地验证码确认请求失败
		}
		
		logInfo("游戏基地确认支付,返回数据:%s", verifyCodeRs);
		
		String resultCode = verifyCodeRs.get("resultCode") + "";
		if("200000".equals(resultCode)||"2000".equals(resultCode)){
			return BusiConst.SUCCESS;
		}else{
			return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "retcode.yxjd." + resultCode, "86801008");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 */
	@Override
	public String doShowService(Map<String, String> urlargs, String id) {
		String url = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"YXJD.SESSION.URL", "http://g.10086.cn/pay/open/index");
		String key = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"YXJD.MDKEY", "38b9d080e00bd62345562fee8121239d");
		
		String appid = "ldysyx";
		String method = "getsessionkey";
		String seconds = System.currentTimeMillis()/1000 + "";
		
		Map<String, String> serviceMap = new HashMap<String, String>();
		serviceMap.put("app", appid);
		serviceMap.put("method", method);
		serviceMap.put("time", seconds);
		serviceMap.put("format", "json");
		StringBuffer md5Str = new StringBuffer();
		md5Str.append("app=").append(appid).append("&")
					.append("method=").append(method).append("&")
					.append("time=").append(seconds).append("&")
					.append("key=").append(key);

		serviceMap.put("hash", DigestUtils.md5Hex(md5Str.toString()));
		logInfo("游戏基地SESSIONID，请求参数:%s, %s", serviceMap, url);
		Map<String, Object> sessionMap = (Map<String, Object>) HTTPSendUtil.getHttpResGet_JSON(url, serviceMap) ;
		logInfo("游戏基地SESSIONID，返回数据:%s", sessionMap);

		if(sessionMap==null){
			return "86801159";//游戏基地业务失败
		}
		String sessionkey = StringUtil.trim((String)sessionMap.get("sessionkey"));
		out.put("sessionkey", sessionkey);
		String resultCode = sessionMap.get("resultCode") + "";
		if("200000".equals(resultCode)||"2000".equals(resultCode)){
			return BusiConst.SUCCESS;
		}else{
			return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "retcode.yxjd." + resultCode, "86801008");
		}
	}

}
