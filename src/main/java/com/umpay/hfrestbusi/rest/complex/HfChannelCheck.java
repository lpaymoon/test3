package com.umpay.hfrestbusi.rest.complex;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.SignUtil;
import com.bs.utils.Base64;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfChannelCheck
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  渠道鉴权(包括渠道证书验签，渠道信息验证，渠道商品信息验证，渠道银行信息验证)
 * @see        :                        
 * ************************************************/   
public class HfChannelCheck extends BaseRest {
	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2013-3-14
	 * description   : 渠道开通验证，渠道商品开通验证
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String channelid = StringUtil.trim(urlargs.get("channelid"));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String bankid = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));//20130506 潘兴武 
		String qdamount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));//20130506 潘兴武 
		String pricemode = StringUtil.trim(urlargs.get(HFBusiDict.PRICEMODE));//20130506 潘兴武
		
		Map<String,String> chnlmap = new HashMap<String,String>();
		Map<String,String> chnlgoodsmap = new HashMap<String,String>();
		//1
		chnlmap.put(HFBusiDict.CHANNELID, channelid);
		logInfo("调用基础资源获取渠道信息");
		Map<String, Object> chnlInf = LocalUtil.doGetService("/hfrestbusi/channelInfRest/"+rpid+"/"+channelid+".xml", chnlmap);
		String retCode = String.valueOf(chnlInf.get(HFBusiDict.RETCODE));
		if(!"0000".equals(retCode)){
			logInfo("获取渠道信息失败");
			return retCode;
		}
		logInfo("验证渠道信息....");
		int chnlstate = (Integer) chnlInf.get(HFBusiDict.STATE);
		if(chnlstate==4||chnlstate!=2){
			logInfo("渠道已禁用");
			return "86801080";
		}
		logInfo("渠道信息验证通过");
		
		logInfo("验证渠道商品信息....");
		chnlgoodsmap.put(HFBusiDict.CHANNELID+".string", channelid);
		chnlgoodsmap.put(HFBusiDict.MERID+".string", merid);
		chnlgoodsmap.put(HFBusiDict.GOODSID+".string", goodsid);
		Map<String,Object> chnlgoodsRs = dal.get("psql_chnlcheck.getchnlgoodinf", chnlgoodsmap);
		if(chnlgoodsRs ==null||chnlgoodsRs.size()==0){
			logInfo("渠道商品信息不存在");
			return "86801081";
		}
		int chnlgoodsstate = (Integer) chnlgoodsRs.get(HFBusiDict.STATE);
		logInfo("查询的渠道商品信息为:%s",chnlgoodsRs);
		if(chnlgoodsstate==4||chnlgoodsstate!=2){
			logInfo("渠道商品已禁用");
			return "86801082";
		}
		
		//20130506 潘兴武 (新增渠道传入价格的验证)
		//20130621潘兴武 修正传入的参数，bankid,pricemod,amount为可选参数
		if("0".equals(pricemode)&&!"".equals(bankid)&&!"".equals(qdamount)){
			Map<String,String> bankMap = new HashMap<String,String>();
			bankMap.put(HFBusiDict.MERID, merid);
			bankMap.put(HFBusiDict.GOODSID, goodsid);
			bankMap.put(HFBusiDict.BANKID, bankid);
			Map<String,Object> resultMap = LocalUtil.doGetService("/hfrestbusi/goodsbank/"+rpid+"/"+merid+"-"+goodsid+"-"+bankid+".xml", bankMap);
			Object getamt = resultMap.get(HFBusiDict.AMOUNT);
			if(getamt==null||"".equals(getamt)){
				logInfo("定价商品未配置价格【merid=%s,goodsid=%s】",merid,goodsid);
				return "86801089";
			}
			long amt = Long.valueOf(String.valueOf(getamt));
			long amount = Long.valueOf(qdamount);
			if(amt!=amount){
				logInfo("定价商品金额不符，平台配置价格%s,渠道传入价格%s", getamt,qdamount);
				return "86801057";
			}
		}
		
		logInfo("渠道信息验证通过");
		out.putAll(chnlInf);
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : panxingwu ,  2013-3-22
	 * descriptoin   : 渠道验签 
	 * ********************************************/     
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("渠道验签，参数为:%s", urlargs);
		String channelid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		String signstr = StringUtil.trim(urlargs.get(HFBusiDict.SIGNSTR));//签名串
		String unsignstr = StringUtil.trim(urlargs.get(HFBusiDict.UNSIGNSTR));//原串
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.CHANNELID, channelid);
		Map<String, Object> chnlInf = LocalUtil.doGetService("/hfrestbusi/channelInfRest/"+rpid+"/"+channelid+".xml", map);
		String retCode = String.valueOf(chnlInf.get(HFBusiDict.RETCODE));
		if(!"0000".equals(retCode)){
			logInfo("获取渠道信息失败");
			return retCode;
		}
		if(chnlInf.get(HFBusiDict.MERCERT)==null||String.valueOf(chnlInf.get(HFBusiDict.MERCERT)).equals("")){
			logInfo("渠道证书未配置");
			return "86801088";
		}
		byte[] chnlcert = null;
		chnlcert = (byte[]) chnlInf.get(HFBusiDict.MERCERT);
		
		boolean flag = false;
		try {
			// 配置获得成功,开始进行验签
			X509Certificate x509cert = SignUtil.genCertificate(chnlcert);
			byte[] bytesign = signstr.getBytes();
			byte[] bSign = Base64.decode(bytesign);
			// 进行GBK和UTF-8两种方式的编码进行分别验签,只要有一次成功就算验签成功
			flag = SignUtil.verify(x509cert, unsignstr.getBytes("GBK"), bSign)
					|| SignUtil.verify(x509cert, unsignstr.getBytes("utf-8"), bSign);
		} catch (Exception e){
			logInfo("渠道验签异常:%s", e.getMessage());
			return "86801086";
		}
		if(!flag){
			logInfo("渠道验签未通过!");
			return "86801085";
		}
		logInfo("渠道验签通过");
		return BusiConst.SUCCESS;
	}
}
