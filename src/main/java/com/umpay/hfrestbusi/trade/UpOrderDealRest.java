package com.umpay.hfrestbusi.trade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.GetRightRuleUtil;
import com.umpay.hfrestbusi.util.IccidUtil;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;



/** ******************  类说明  *********************
 * class       :  UpOrderDealRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  综合支付下单流程
 * @see        :                        
 * ************************************************/   
public class UpOrderDealRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs)
			throws Exception {
		logInfo("综合支付下单开始,参数为:%s",urlargs);
		String rpid = StringUtil.trim((String)urlargs.get(HFBusiDict.RPID));
		String mobileId = StringUtil.trim((String)urlargs.get(HFBusiDict.MOBILEID));
		String merId = StringUtil.trim((String)urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim((String)urlargs.get(HFBusiDict.GOODSID));
	//	String netType = StringUtil.trim((String)urlargs.get(HFBusiDict.NETTYPE));//手机运行商类型
		String netType = StringUtil.trim((String)urlargs.get(HFBusiDict.UPNETTYPE));//手机运行商类型
		String provCode=StringUtil.trim((String)urlargs.get(HFBusiDict.PROVCODE));
		String iccid=StringUtil.trim((String)urlargs.get(HFBusiDict.ICCID));
		String skdVersion=StringUtil.trim((String)urlargs.get(HFBusiDict.SDKVERSION));//sdk版本号
		String platType=StringUtil.trim((String)urlargs.get(HFBusiDict.PLATTYPE));//接入类型
		String amount=StringUtil.trim((String)urlargs.get(HFBusiDict.AMOUNT));//金额
        //根据  接入类型.子类型  获取接入类型支持的可用支付流程
		String platTypeChnlKey = NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, "platTypeCheckKeys", "platType,version");
		String platTypechnls = ""; 
		try{
			Map <String,String> map = new HashMap <String,String> ();
			map.put(HFBusiDict.PLATTYPE, platType);
		    map.put(HFBusiDict.VERSION, skdVersion);
		    String[] platTypeChnlKeys = platTypeChnlKey.split(",");  
			platTypechnls = GetRightRuleUtil.findCondition(platTypeChnlKeys, "platTypeChnlChek", map,"platTypeChnl");
		    logInfo("获得的接入方式可用支付流程为 platTypechnls[" + platTypechnls + "]");
		   } catch (Exception e) {
			    logException(e);
			    return "86801098";
		   }
		if("".equals(platTypechnls)){
			logInfo("接入方式无可用支付流程  platType[" + platType + "] version["+skdVersion+"]");
			return "86801098";
		}
		String retCode = BusiConst.SYS_ERROR;
		List<Map<String, Object>> combineList =null;
		String plTypechls[]=platTypechnls.split(",");
		String businessType=plTypechls[0];// 默认接入平台最优先的支付流程
		//用户交易鉴权
		Map<String,Object> ckTransResp =null;
		if(!"".equals(mobileId)){
			Map<String,String> checkTrans =new HashMap<String,String>();
			checkTrans.put(HFBusiDict.MERID, merId);
			checkTrans.put(HFBusiDict.GOODSID, goodsId);
			checkTrans.put(HFBusiDict.MOBILEID, mobileId);
			String ids=merId+"-"+goodsId+"-"+mobileId;
			
			ckTransResp = LocalUtil.doGetService("/hfrestbusi/checktrans/mobileid/"+rpid+"/"+ids+".xml", checkTrans);
			retCode = (String) ckTransResp.get(HFBusiDict.RETCODE);
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("用户交易鉴权失败,返回码：%s", retCode);
				return retCode;
			}
			provCode=StringUtil.trim((String)ckTransResp.get(HFBusiDict.PROVCODE));
	        combineList =(List<Map<String, Object>>) ckTransResp.get("combineList");
		}else {//无手机号交易鉴权
			Map<String,String> checkTrans =new HashMap<String,String>();
			checkTrans.put(HFBusiDict.MERID, merId);
			checkTrans.put(HFBusiDict.GOODSID, goodsId);
			if(!"".equals(iccid)&&iccid.length()>11){
				provCode= getProCodeByICCID(iccid);
				checkTrans.put(HFBusiDict.PROVCODE, provCode);
			}
			String ids=merId+"-"+goodsId;
			ckTransResp = LocalUtil.doGetService("/uprestbusi/checktrans/common/"+rpid+"/"+ids+".xml", checkTrans);
			retCode = (String) ckTransResp.get(HFBusiDict.RETCODE);
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("交易鉴权失败,返回码：%s", retCode);
				return retCode;
			}
			if(ckTransResp.get("getBankList").equals("true")){
			    combineList =(List<Map<String, Object>>) ckTransResp.get("combineList"); 
			}
		}
		
	   Map<String, Object> bankInfo = null;
//	   String sdk_chanl=NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, "SDK."+skdVersion, "");//sdk支持的流程
	   String chnlRuleKey = NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, "chnlCheckKeys", "nettype,provcode,merid,goodsid");
	   String gateId=""; //通道
	   if(combineList!=null){ 			
		 //根据   运营商.省份.商户.商品获取 支付通道优先级
			//获取运营商字段 netType
			if("".equals(netType)){
				netType="1"; //默认移动
			}
			Map <String,String> map = new HashMap <String,String> ();
			map.put(HFBusiDict.NETTYPE, netType);
		    map.put(HFBusiDict.PROVCODE, provCode);
            map.put(HFBusiDict.MERID, merId);
		    map.put(HFBusiDict.GOODSID, goodsId);
		    String[] chnlRuleKeys = chnlRuleKey.split(",");    
		    String paychnls = "";
		    try
		    {
		    	paychnls = GetRightRuleUtil.findCondition(chnlRuleKeys, "paychnlChek", map,"paychnlChek");
		        logInfo("匹配后获得的支付流程为 paychnls[" + paychnls + "]");
		    } catch (Exception e) {
		    	logException(e);
		    }
			if(!"".equals(paychnls)){//选择支付流程
				String [] chnls=paychnls.split(",");
				String prex="";
				String flag="0";
				for(int i=0;i<chnls.length;i++){

					prex=getGateIdByBusiType(chnls[i]);
				    for (Map<String, Object> combineMap : combineList) {
						String tempBankId = (String)combineMap.get(HFBusiDict.BANKID);
						if(tempBankId.startsWith(prex)&&platTypechnls.contains(chnls[i])){//sdk支付该流程
							bankInfo = combineMap;
							businessType=chnls[i];
							flag="1";
							break;
						}
					}
				    if("1".equals(flag)){ //跳出循环
				    	break;
				    } 
				}
				if("0".equals(flag)){
					//无可支付流程
					 logInfo("商户无可用支付流程:%s",rpid);
					 return "86801095";
				}
				
			}else{
				//未配置可支付流程
				 logInfo("未配置商户商品支付流程:%s",rpid);
				 return "86801094";
			}
	   }
		
		//保存订单表
		Long expireTime = (Long)(ckTransResp.get(HFBusiDict.EXPIRETIME));
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		long future = now + expireTime * 1000;
		urlargs.put(HFBusiDict.EXPIRETIME, new Timestamp(future).toString());
		urlargs.put(HFBusiDict.AMTTYPE, "02");//话费支付
		urlargs.put(HFBusiDict.BUSINESSTYPE, businessType);
		urlargs.put(HFBusiDict.NETTYPE, netType);
		urlargs.put(HFBusiDict.VERSION, StringUtil.trim((String)urlargs.get(HFBusiDict.UPVERSION)));
        if(bankInfo!=null){
        	urlargs.put(HFBusiDict.BANKID, StringUtils.trim((String) bankInfo.get(HFBusiDict.BANKID)));
        	out.put(HFBusiDict.BANKID,StringUtils.trim((String) bankInfo.get(HFBusiDict.BANKID)));
        }
		Map<String,Object> orderResp = LocalUtil.doPostService("/uprestbusi/uporder/common/"+rpid+".xml", urlargs);
		retCode = (String) orderResp.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("保存订单信息失败,返回码：%s", retCode);
			return retCode;
		}

		gateId=getGateIdByBusiType(businessType);
        String  gateIdList= NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, "gateIdList", "BS");//需要查询计费码的通道列表
        if(gateIdList.contains(gateId)){
			Map<String, String> serviceMap = new HashMap<String, String>();
			serviceMap.put(HFBusiDict.MERID, merId);
			serviceMap.put(HFBusiDict.GOODSID, goodsId);
			serviceMap.put(HFBusiDict.GATEID, gateId);
			Map<String, Object> serviceRs = LocalUtil.doGetService("/uprestbusi/goodsserviceid/"+rpid+"/"+merId+"-"+goodsId+"-"+gateId+".xml", serviceMap);
			String productId = StringUtil.trim((String)serviceRs.get(HFBusiDict.SERVICEID));
			if (!"0000".equals(serviceRs.get(HFBusiDict.RETCODE))
					|| productId == null || "null".equals(productId)
					|| "".equals(productId)) {
				return "86802011";//计费码无法查询
			}
			//和计费码金额校验
			String amt=StringUtils.trim(serviceRs.get(HFBusiDict.AMOUNT).toString());
			if(!amount.equals(amt)){
				logInfo("商品金额amount与计费码金额amt不符：%s,%s",amount,amt );
				return "86802005";
			}
			out.putAll(serviceRs);
        }
		out.put(HFBusiDict.BUSINESSTYPE,businessType);
		out.putAll(ckTransResp);
		out.putAll(orderResp);
		out.remove("combineList");
		out.remove("URI");
		out.remove("LOCAL");	
		logInfo("下单成功:%s,%s", rpid,mobileId);
		return BusiConst.SUCCESS;
	}
	/**
	 * ********************************************
	 * method name   : getProCodeByICCID 
	 * description   : 根据ICCID获取省份代码
	 * @return       : String
	 * @param        : @param combineList
	 * @param        : @return string
	 * modified      : xuwei ,  
	 * @see          : 
	 * *******************************************
	 */
	private String getProCodeByICCID(String iccid) {
		// TODO Auto-generated method stub
		String provCode="";
		if(iccid.startsWith("898600")){//移动
			provCode= IccidUtil.matchYdProv(iccid.substring(8, 10));		
		}else if(iccid.startsWith("898601")){//联通
			provCode= IccidUtil.matchLTProv(iccid.substring(9, 11));
		}else if(iccid.startsWith("898603")){//电信
			provCode= iccid.substring(9, 12);
		}		
		return provCode ;
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
//		String gateId="";
//	    if(bussinessType.startsWith("060")){
//	        gateId="BS";
//	    }else if(bussinessType.startsWith("061")){
//	        gateId="XE";
//	    }else if(bussinessType.startsWith("062")){
//	       	gateId="GM";
//	    }
		String gateId = NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, "gateId."+bussinessType.substring(0, 3), "");
		return gateId ;
	}
	
	/**
	 * ********************************************
	 * method name   : getBankId 
	 * description   : 根据支付银行列表和用户银行列表交集，获取唯一的bankId
	 * @return       : String
	 * @param        : @param combineList
	 * @param        : @return
	 * modified      : zhaoyan ,  Nov 11, 2011 11:27:57 AM
	 * @see          : 
	 * *******************************************
	 */
	private Map<String, Object> getBankInfo(List<Map<String, Object>> combineList) {
		Map<String, Object> bankInfo = null;
		for (Map<String, Object> combineMap : combineList) {
			String tempBankId = (String)combineMap.get(HFBusiDict.BANKID);
			if(tempBankId.startsWith("XE")){
				bankInfo = combineMap;
				break;
			}else{
				bankInfo = combineMap;
			}
		}
		return bankInfo;
	}
}
