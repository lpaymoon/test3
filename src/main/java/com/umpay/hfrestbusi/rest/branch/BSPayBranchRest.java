package com.umpay.hfrestbusi.rest.branch;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSONObject;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.HTTPUtil;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/******************* 类说明 ******************
 *  class : BSPayBranchRest 
 *  date : 2014-9-23
 * 
 * @author : Roy
 * @version : V1.0 
 *  description : 博升支付流程
 * @see :
 * ***********************************************/
public class BSPayBranchRest extends BaseRest {

	/* (non-Javadoc)
	 * @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 */
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id) {
		String isGreen = StringUtil.trim(urlargs.get("isgreen"));// 是否绿色版
		String rpid = StringUtil.trim(urlargs.get(HFBusiDict.RPID));
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String accountId = StringUtil.trim(urlargs.get(HFBusiDict.MERCUSTID));// 购买人登录账号
		String tranSeq = StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ));// 购买人登录账号
		String clientType = "wap";
		
		/*
		 * 生成银行交易流水
		 * 
		 */
		String url = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"BS.ORDER.URL",
				"http://cmpay.dalasu.com/mmwap/JumpOrderPageServlet");// BS.ORDER.URL
		String biz_id = NamedProperties.getMapValue(BusiConst.SYSPARAMS, 
				"BS.ORDER.BIZ_ID", "144");// 商家账号
		String secrtykey = NamedProperties.getMapValue(BusiConst.SYSPARAMS, 
				"BS.ORDER.SECRTYKEY", "f8a97340efa59e389863dd05e6ee1599");
		String orderType = NamedProperties.getMapValue(BusiConst.SYSPARAMS,
				"BS.ORDER.TYPE", "0");// 订单类型 0 新用户订购 1 老用户续订 按次交易默认为0
		
		/*
		 * 查询计费码
		 */
		Map<String, String> serviceMap = new HashMap<String, String>();
		serviceMap.put(HFBusiDict.MERID, merId);
		serviceMap.put(HFBusiDict.GOODSID, goodsId);
		serviceMap.put(HFBusiDict.GATEID, "BS");
		Map<String, Object> serviceRs = LocalUtil.doGetService("/uprestbusi/goodsserviceid/"+rpid+"/"+merId+"-"+goodsId+"-BS.xml", serviceMap);
		String productId = StringUtil.trim((String)serviceRs.get(HFBusiDict.SERVICEID));
		if (!"0000".equals(serviceRs.get(HFBusiDict.RETCODE))
				|| productId == null || "null".equals(productId)
				|| "".equals(productId)) {
			return "86802011";//计费码无法查询
		}
		out.putAll(serviceRs);
		try {
			/*
			 * 向博升发出验证码下发请求
			 * sign加密串拼写例子
			 * md5(“biz_id=xxx&product_id=xxxx&order_id=xxxxx&account_id
			 * =xxxx&order_type=xxx&client_type=www&
			 * mobile=15000415244secrtykey”) 其中secrtykey为博升提供的私钥
			 */
			StringBuffer signBuffer = new StringBuffer();
			signBuffer.append("biz_id=").append(biz_id)
							.append("&product_id=").append(productId)
							.append("&order_id=").append(tranSeq)
							.append("&account_id=").append(accountId)
							.append("&client_type=").append(clientType)
							.append("&mobile=").append(mobileId)
							.append("&order_type=").append(orderType);
			String signString = signBuffer.toString();
			logger.info("准备MD5的字符串：" + signString + secrtykey);
			String sign = DigestUtils.md5Hex(signString + secrtykey);
			logger.info("MD5加密后：" + sign);

			Map<String, String> bsOrderMap = new HashMap<String, String>();
			bsOrderMap.put("sign", sign);
			bsOrderMap.put("biz_id", biz_id);
			bsOrderMap.put("mobile", mobileId);
			bsOrderMap.put("order_id", tranSeq);
			bsOrderMap.put("product_id", productId);
			bsOrderMap.put("account_id", accountId);
			bsOrderMap.put("client_type", clientType);
			bsOrderMap.put("order_type", orderType);

			logger.info("博升优势的下单args：" + bsOrderMap);
			Object bsOrderRs = HTTPUtil.get(url, bsOrderMap);
			logger.info("博升优势的下单接口返回值：" + bsOrderRs);
			if (bsOrderRs == null) {//TODO 如何识别返回的失败码
				return "86801129";// 博升下单失败
			}
			String result = bsOrderRs.toString();

			/*
			 * 发送短信验证码
			 */
			Map<String, String> smsMap = new HashMap<String, String>();
			smsMap.put("isgreen", isGreen);
			smsMap.put(HFBusiDict.SIGNSTR, result);
			smsMap.put(HFBusiDict.MOBILEID, mobileId);
 
			Map<String, Object> bsCheckCodeRs = LocalUtil
					.doPostService("/uprestbusi/bscheckcode/" + rpid + "/bs.xml", smsMap);
			String jsonStr = JSONObject.toJSONString(bsCheckCodeRs);
			
			out.put("jsonrs", jsonStr);
			out.putAll(bsCheckCodeRs);
			String bsCheckRetCode = (String) bsCheckCodeRs.get(HFBusiDict.RETCODE);
			if(!BusiConst.SUCCESS.equals(bsCheckCodeRs.get(HFBusiDict.RETCODE))){
				return bsCheckRetCode;
			}
			logger.info("发送短信的返回json：" + jsonStr);
		} catch (Exception e) {
			logger.error("向博升请求下单失败。", e);
			return "86801129";// 博升下单失败
		}
		return BusiConst.SUCCESS;
	}

}
