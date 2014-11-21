package com.umpay.hfrestbusi.rest;

import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;


import com.bs.mpsp.util.SignUtil;
import com.bs.utils.Base64;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.MerCertCacheHelper;

/**
 * ******************  类说明  *********************
 * class       :  MerSignRest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  商户签名资源
 * @see        :                        
 * ***********************************************
 */
public class MerSignRest extends BaseRest {

	/**
	 * ******************************************** method name : doShowService
	 * modified : LiuJiLong , 2011-11-2
	 * 
	 * @throws Exception
	 * @see : @see
	 *      com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map,
	 *      java.lang.String)
	 * ********************************************/
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		// LOG输出上下文
		this.logInfo("商户验签,参数为%s", urlargs);
		
		// 装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String signstr = StringUtil.trim(urlargs.get(HFBusiDict.SIGNSTR));
		String unsignstr = StringUtil.trim(urlargs.get(HFBusiDict.UNSIGNSTR));
		args.put(HFBusiDict.MERID+".string", merid);

		// 资源获取(证书)
		byte[] mercert = null;
		mercert = (byte[]) CacheUtil.getObject("merCert", merid, args,
				new MerCertCacheHelper(dal));

		// 检查结果，决定返回码
		this.logInfo("开始校验结果", "");
		
		//获得的证书为空
		if (mercert == null) {
			this.logInfo("商户证书未配置:%s",merid);
			return "86801026";
		} else {
			boolean flag;
			try {
				// 配置获得成功,开始进行验签
				X509Certificate x509cert = SignUtil.genCertificate(mercert);
				byte[] bytesign = signstr.getBytes();
				byte[] bSign = Base64.decode(bytesign);
				flag = false;
			
				// 进行GBK和UTF-8两种方式的编码进行分别验签,只要有一次成功就算验签成功
				flag = SignUtil.verify(x509cert, unsignstr.getBytes("GBK"), bSign)
						|| SignUtil.verify(x509cert, unsignstr.getBytes("utf-8"), bSign);
			} catch (Exception e){
				this.logInfo("验签失败,异常代码为:%s %s",
						e.getMessage(), merid+"---"+signstr+"---"+unsignstr);
				return "86801056";
			}
			
			//返回结果
			if (flag) {
				this.logInfo("验签通过:%s",merid+"-"+signstr+"-"+unsignstr);
				return BusiConst.SUCCESS;
			} else {
				this.logInfo("验签未通过:%s",merid+"-"+signstr+"-"+unsignstr);
				return "86801027";
			}
		}
	}
}
