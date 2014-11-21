package com.umpay.hfrestbusi.rest.branch;

import java.util.HashMap;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.HTTPUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/** ******************  类说明  ******************
 * class       :  BSPayRest
 * date        :  2014-9-25 
 * @author     :  Roy
 * @version    :  V1.0  
 * description :  博升支付
 * @see        :                         
 * ***********************************************/
public class BSPayRest extends BaseRest{

	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 */
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		String ex = StringUtil.trim(urlargs.get(HFBusiDict.EX));
		String ch = StringUtil.trim(urlargs.get(HFBusiDict.CH));
		String bu = StringUtil.trim(urlargs.get(HFBusiDict.BU));
		String apco = StringUtil.trim(urlargs.get(HFBusiDict.APCO));
		String aptId = StringUtil.trim(urlargs.get(HFBusiDict.APTID));
		String aptrId = StringUtil.trim(urlargs.get(HFBusiDict.APTRID));
		String innerId = StringUtil.trim(urlargs.get(HFBusiDict.INNERID));
		String mmURL = StringUtil.trim(urlargs.get(HFBusiDict.MMURL));
		String random = StringUtil.trim(urlargs.get(HFBusiDict.RANDOM));
		String platOrdId = StringUtil.trim(urlargs.get(HFBusiDict.PLATORDID));
		String inputMobile = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String inputCode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));

		/*
		 * 返回输入的platOrdId给SDK
		 */
		out.put("platordid", platOrdId);
		
		Map<String, String> requestCheckSmsMap = new HashMap<String, String>();
		requestCheckSmsMap.put(HFBusiDict.APTID, aptId);
		requestCheckSmsMap.put(HFBusiDict.INNERID, innerId);
		requestCheckSmsMap.put(HFBusiDict.MMURL, mmURL);
		requestCheckSmsMap.put(HFBusiDict.INPUTCODE, inputCode);
		requestCheckSmsMap.put(HFBusiDict.INPUTMOBILE, inputMobile);
		
		String url = NamedProperties.getMapValue(BusiConst.SYSPARAMS, 
						"MM.VEFIFYCODE.URL",
						"http://ospd.mmarket.com:8089/wabp/wabpWapOrder!voifyCode.action");

		logger.info("MM验证短信码请求参数：" +requestCheckSmsMap);
		Object mmSendSmsRs = HTTPUtil.get(url, requestCheckSmsMap);// 向MM请求验证短信支付验证码
		if (mmSendSmsRs == null) {
			return BusiConst.SYS_ERROR;
		}
		String mmRetCode = getResultValue(mmSendSmsRs.toString());
		
		logger.info("MM验证短信码接口返回内容：" + mmSendSmsRs.toString() + ":" + mmRetCode);
		if(!BusiConst.SUCCESS.equals(mmRetCode)){
			return mmRetCode;
		}
		Map<String, String> retMap = new HashMap<String, String>();
		String mmConfirmUrl = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"MM.CONFIRMPAY.URL",
				"http://ospd.mmarket.com:8089/wabp/wfeen_h5.action");

		Map<String, String> requestConfirmMap = new HashMap<String, String>();
		requestConfirmMap.put(HFBusiDict.EX, ex);
		requestConfirmMap.put(HFBusiDict.CH, ch);
		requestConfirmMap.put(HFBusiDict.BU, bu);
		requestConfirmMap.put("Referer", mmURL);
		requestConfirmMap.put(HFBusiDict.APCO, apco);
		requestConfirmMap.put(HFBusiDict.APTID, aptId);
		requestConfirmMap.put(HFBusiDict.APTRID, aptrId);
		requestConfirmMap.put(HFBusiDict.INNERID, innerId);
		requestConfirmMap.put(HFBusiDict.RANDOM, random);
		requestConfirmMap.put(HFBusiDict.INPUTCODE, inputCode);
		requestConfirmMap.put(HFBusiDict.INPUTMOBILE, inputMobile);
		
		logger.info("MM支付接口请求参数：" + requestConfirmMap);
		Object payRs = HTTPUtil.get(mmConfirmUrl, requestConfirmMap);//请求MM确认支付接口
		logger.info("MM支付接口返回内容：" + payRs);
		
		if (payRs == null) {
			return BusiConst.SYS_ERROR;
		}
		try {
			// 获取参数
			String resultUrl = getResultUrl(payRs);// 解析xml 获取wabp_result的值
			String bsPayQueryUrl = resultUrl.substring(0,
					resultUrl.indexOf("?"));
			String[] paramArray = resultUrl
					.substring(resultUrl.indexOf("?") + 1)
					.replaceAll("amp;", "").split("&");

			Map<String, String> payMap = new HashMap<String, String>();
			payMap.put("Host", "cmpay.dalasu.com");
			if (paramArray != null && paramArray.length > 0) {
				for (String paramEntryString : paramArray) {
					String[] paramEntry = paramEntryString.split("=");
					payMap.put(StringUtil.trim(paramEntry[0]),
							StringUtil.trim(paramEntry[1]));
				}
			}
			logger.info("请求博升支付结果参数：" + payMap + "; URL:" + bsPayQueryUrl);
			Object obj  = HTTPUtil.get(bsPayQueryUrl, payMap);// 请求博升支付结果 TODO 改为非阻塞
			logger.info("请求博升支付结果：" + obj);

			String result = payMap.get("wabp_result");
			if ("000".equals(result)) {
				return BusiConst.SUCCESS;
			} else {
				return "86801157";// MM验证支付失败
			}
		} catch (Exception e) {
			return BusiConst.SYS_ERROR;
		}
	}

	/** *****************  方法说明  *****************
	 * method name   :  getResultUrl
	 * @param		 :  @param payRs
	 * @param		 :  @return
	 * @return		 :  String
	 * @author       :  fan 2014-9-25 上午1:30:50
	 * description   :  
	 * @see          :  
	 * ***********************************************/
	private String getResultUrl(Object payRs) {
		String html = payRs.toString();
		String temp = html.substring(html.indexOf("url="));
		// 输入验证码的url 是根据BS接口的返回值进行的判断
		String resultUrl = temp.substring(4, temp.indexOf("/>") - 1).trim();
		return resultUrl;
	}

	/**
	 * 根据接口返回内容获取value
	 * @param result格式： < input id="result" type="hidden" value="1" />
	 * @return
	 */
	private static String getResultValue(String result) {
		result = result.replace("\"", "").replace("<input", "")
				.replace("/>", "").trim();
		String[] elementArray = result.split(" ");
		for (String element : elementArray) {
			if (element.indexOf("value=") != -1) {
				String value = StringUtil.trim(element.split("=")[1]);
				if ("1".equals(value)) {
					return BusiConst.SUCCESS;
				} else {
					return NamedProperties.getMapValue(BusiConst.SYSPARAMS,
							"BS." + value, "9999");
				}
			}
		}
		return BusiConst.SYS_ERROR;
	}
}
