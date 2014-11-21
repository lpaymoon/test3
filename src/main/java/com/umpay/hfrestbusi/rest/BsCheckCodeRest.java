package com.umpay.hfrestbusi.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.HTTPUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/******************* 类说明 ****************** 
 * class : BsCheckCodeRest 
 * date : 2014-9-23
 * 
 * @author : Roy
 * @version : V1.0 description : 通知博升下发验证码
 * @see :
 * ***********************************************/
public class BsCheckCodeRest extends BaseRest {
	private static final String ORDER_NOT_GREEN = "2";
	private static final String[] paramSeq = new String[] { "inputMobile", "inner_id", "ch", "ex", "random" };

	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 */
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String isGreen = StringUtil.trim(urlargs.get("isgreen"));
		String html = StringUtil.trim(urlargs.get(HFBusiDict.SIGNSTR));
		String temp = html.substring(html.indexOf("url="));
		String verifycodeUrl = temp.substring(4, temp.indexOf(">") - 1).trim();// 输入验证码的url 是根据BS接口的返回值进行的判断

		out.put("MMURL", verifycodeUrl);
		if (!ORDER_NOT_GREEN.equals(isGreen)) {// 判断是否是绿色版 如果是继续发短信 如果不是 则参数返回
			// 拆分url的参数
			Map<String, String> paramMap = getInputValueMap(verifycodeUrl);
			out.putAll(paramMap);

			Map<String, String> requestMap = new HashMap<String, String>();
			// 获取参数列表
			for (String param : paramSeq) {
				requestMap.put(param, paramMap.get(param));
			}
			requestMap.put("MMURL", verifycodeUrl);

			String url = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
							"MMARKET.SENDSMSCODE.URL",
							"http://ospd.mmarket.com:8089/wabp/wabpWapOrder!sendSmsCode.action");
			Object mmCheckCodeRs = HTTPUtil.get(url, requestMap);
			if (mmCheckCodeRs == null) {
				return "86801130";// MM接口发送验证码失败
			}
			logger.info("MM发送验证码接口返回内容：" + mmCheckCodeRs.toString());
			String bsCode = getResultValue(mmCheckCodeRs.toString());
			return NamedProperties.getMapValue(BusiConst.SYSPARAMS, "BS." + bsCode, "9999");
		}
		return BusiConst.SUCCESS;
	}

	/**
	 * 读取url的页面源码 把页面的input域的内容取出来
	 * 
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	private static Map<String, String> getInputValueMap(String sourceUrl) throws IOException {
		Map<String, String> valueMap = new HashMap<String, String>();
		// 读取页面内容
		URL url = null;
		BufferedReader in = null;
		URLConnection conn = null;
		try {
			url = new URL(sourceUrl);
			conn = url.openConnection();
			conn.setDoOutput(true);

			InputStream inputStream = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
			in = new BufferedReader(isr);
			String htmlLine;

			while ((htmlLine = in.readLine()) != null) {
				// 去掉每行的空格元素 并把双引号去掉
				String line = htmlLine.trim().replaceAll("\"", "");

				// 如果是input域
				int inputIndex = line.indexOf("<input");
				int hiddenIndex = line.indexOf("hidden");
				if (inputIndex != -1 && hiddenIndex != -1) {
					// 取出input域的字符串那一步
					String inputHTML = line.substring(inputIndex);
					inputHTML = inputHTML
							.substring(0, inputHTML.indexOf(">") + 1)
							.replace("<input", "").replace("/>", "")
							.replace(">", "").trim();
					// 按照空格拆分 取出name=mobile value=13811081453这样的格式
					String[] elementArray = inputHTML.split(" ");
					if (elementArray != null && elementArray.length > 0) {
						String key = "";
						String value = "";
						for (String element : elementArray) {
							if (element.indexOf("name=") != -1) {
								key = StringUtil.trim(element.split("=")[1]);
							}
							if (element.indexOf("value=") != -1) {
								value = StringUtil.trim(element.split("=")[1]);
							}
						}
						valueMap.put(key, value);
					}
				}
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return valueMap;
	}

	/**
	 * 根据url获取请求参数 前提是get方式 url后有参数的那种情况
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unused")
	private Map<String, String> getRequestMap(String url) {
		Map<String, String> requestMap = new HashMap<String, String>();
		String paramString = url.substring(url.indexOf("?") + 1);
		String[] paramArray = paramString.split("&");
		for (String param : paramArray) {
			String[] key_value = param.split("=");
			if (key_value != null && key_value.length == 2) {
				requestMap.put(StringUtil.trim(key_value[0]),
						StringUtil.trim(key_value[1]));
			} else {
				requestMap.put(StringUtil.trim(key_value[0]), "");
			}
		}
		return requestMap;
	}

	/**
	 * 根据接口返回内容获取value
	 * 
	 * @param result格式
	 *            ： < input id="result" type="hidden" value="1" />
	 * @return
	 */
	private static String getResultValue(String result) {
		result = result.replace("\"", "").replace("<input", "").replace("/>", "").trim();
		String[] elementArray = result.split(" ");
		for (String element : elementArray) {
			if (element.indexOf("value=") != -1) {
				String value = StringUtil.trim(element.split("=")[1]);
				if ("1".equals(value)) {
					return  BusiConst.SUCCESS;
				}else{
					return value;
				}
			}
		}
		return "86801131";// MM接口取参失败
	}
}
