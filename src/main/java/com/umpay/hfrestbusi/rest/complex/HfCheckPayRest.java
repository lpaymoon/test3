package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;


/** ******************  类说明  *********************
 * class       :  HfCheckPayRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  支付鉴权
 * @see        :                        
 * ************************************************/   
public class HfCheckPayRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String bankId = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String businessType = StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE));
		String bankPre=getGateIdByBusiType(businessType);
		String retCode = BusiConst.SYS_ERROR;		
		if(!"".equals(mobileId)){//如果有手机号，重新获取bankId		
			//获取话费特殊用户信息
			Map<String,String> specUserMap = new HashMap<String,String>();
			specUserMap.put(HFBusiDict.MOBILEID, mobileId);
			Map<String,Object> specUser = LocalUtil.doGetService("/hfrestbusi/specuser/"+rpid+"/"+mobileId+".xml", specUserMap);
			retCode = (String) specUser.get(HFBusiDict.RETCODE);
			if(!BusiConst.SUCCESS.equals(retCode)) {
				logInfo("获取话费特殊用户信息失败,返回码：%s", retCode);
				return retCode;	
			}
			
			//获取号段信息
			Map<String,String> segInfMap = new HashMap<String,String>();
			segInfMap.put(HFBusiDict.MOBILEID, mobileId.substring(0,7));
			Map<String,Object> segInf = LocalUtil.doGetService("/hfrestbusi/seginf/"+rpid+"/"+mobileId.substring(0,7)+".xml", segInfMap);
			retCode = (String) segInf.get(HFBusiDict.RETCODE);
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("获取号段信息失败,返回码：%s %s", retCode,mobileId.substring(0,7));
				return retCode;
			}
		
			String provcode= StringUtil.trim((String)segInf.get(HFBusiDict.PROVCODE));
			String areacode= StringUtil.trim((String)segInf.get(HFBusiDict.AREACODE));
			out.put(HFBusiDict.PROVCODE,provcode);
			out.put(HFBusiDict.AREACODE,areacode);
			bankId=bankPre+provcode+"000";
		}
		//获取商户信息
		Map<String,String> merMap = new HashMap<String,String>();
		merMap.put(HFBusiDict.MERID, merId);
		Map<String,Object> merInf = LocalUtil.doGetService("/hfrestbusi/merinf/"+rpid+"/"+merId+".xml", merMap);
		retCode = (String)merInf.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			return retCode;//商户不存在或者商户未开通
		}
		
		//获取商品信息
		Map<String,String> goodsMap = new HashMap<String,String>();
		goodsMap.put(HFBusiDict.MERID, merId);
		goodsMap.put(HFBusiDict.GOODSID, goodsId);
		Map<String,Object> goodsInf = LocalUtil.doGetService("/hfrestbusi/goodsinf/"+rpid+"/"+merId+"-"+goodsId+".xml", goodsMap);
		retCode = (String) goodsInf.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			return retCode;//商品不存在或者商品未开通或者包月商品未配置属性
		}
		if(!"".equals(bankId)){
			//获取商户银行信息
			Map<String,String> merBankMap = new HashMap<String,String>();
			merBankMap.put(HFBusiDict.MERID, merId);
			merBankMap.put(HFBusiDict.BANKID, bankId);
			if(bankId.startsWith("XE")){
				Map<String,Object> merBank = LocalUtil.doGetService("/hfrestbusi/merbank/"+rpid+"/"+merId+"-"+bankId+".xml", merBankMap);
				retCode = (String) merBank.get(HFBusiDict.RETCODE);
				if(!BusiConst.SUCCESS.equals(retCode)){
					return retCode;
				}
			}
			
			//获取商品银行信息
			Map<String,String> goodsBankMap = new HashMap<String,String>();
			goodsBankMap.put(HFBusiDict.MERID, merId);
			goodsBankMap.put(HFBusiDict.GOODSID, goodsId);
			goodsBankMap.put(HFBusiDict.BANKID, bankId);
			Map<String,Object> goodsBank = LocalUtil.doGetService("/hfrestbusi/goodsbank/"+rpid+"/"+merId+"-"+goodsId+"-"+bankId+".xml", goodsBankMap);
			retCode = (String) goodsBank.get(HFBusiDict.RETCODE);
			if(!BusiConst.SUCCESS.equals(retCode)){
				return retCode;
			}
			String kstate = goodsBank.get(HFBusiDict.KSTATE).toString();
			if("23".equals(kstate)){
				logInfo("商品银行新增和续费已注销", "");
				out.put(HFBusiDict.KSTATE,goodsBank.get(HFBusiDict.KSTATE));
				return "86801020";
			}else{
				if(!("11".equals(kstate)||"13".equals(kstate))){
					logInfo("商品银行未开通新增");
					return "86801018";
				} 
			}
	
			//如果是定价模式，要检查amount
			if("0".equals(goodsInf.get(HFBusiDict.PRICEMODE).toString())){
				if(!amount.equals(StringUtil.trim(goodsBank.get(HFBusiDict.AMOUNT).toString()))){
					logInfo("定价商品金额不符,商品配置金额为:%s, 实际金额为:%s", goodsBank.get(HFBusiDict.AMOUNT),amount);
					return "86801057";
				}
			}
	
		}

		out.putAll(merInf);
		out.putAll(goodsInf);
		out.put(HFBusiDict.BANKID,bankId);
	
	//	out.putAll(specUser);
		logInfo("支付鉴权通过","");
		return BusiConst.SUCCESS;
	}
	
	
	/**
	 * ********************************************
	 * method name   : getGateIdByBusiType 
	 * description   : 根据bussinessType获取通道号
	 * @return       : String
	 * @param        : @param bussinessType
	 * @param        : @return string
	 * modified      : xuwei ,  
	 * @see          : 
	 * *******************************************
	 */
	private String getGateIdByBusiType(String bussinessType) {
		// TODO Auto-generated method stub
		String gateId = NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, "gateId."+bussinessType.substring(0, 3), "");
		return gateId ;
	}
}
