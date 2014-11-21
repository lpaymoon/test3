package com.umpay.hfrestbusi.rest.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;



/** ******************  类说明  *********************
 * class       :  HfCheckUserTransRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  用户交易鉴权
 * @see        :                        
 * ************************************************/   
public class HfCheckUserTransRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("用户交易鉴权开始,参数为:%s",urlargs);
	
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String netType = StringUtil.trim(urlargs.get(HFBusiDict.NETTYPE)); //运营商
		
		String retCode = BusiConst.SYS_ERROR;
		//获取话费特殊用户信息
		Map<String,String> specUserMap = new HashMap<String,String>();
		specUserMap.put(HFBusiDict.MOBILEID, mobileId);
		Map<String,Object> specUserRs = LocalUtil.doGetService("/hfrestbusi/specuser/"+rpid+"/"+mobileId+".xml", specUserMap);
		retCode = (String) specUserRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)) {
			logInfo("获取话费特殊用户信息失败,返回码：%s", retCode);
			return retCode;	
		}
		//获取号段信息
		Map<String,String> segInfMap = new HashMap<String,String>();
		segInfMap.put(HFBusiDict.MOBILEID, mobileId.substring(0,7));
		Map<String,Object> segInfRs = LocalUtil.doGetService("/hfrestbusi/seginf/"+rpid+"/"+mobileId.substring(0,7)+".xml", segInfMap);
		retCode = (String) segInfRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取号段信息失败,返回码：%s %s", retCode,mobileId.substring(0,7));
			return retCode;
		}
		String provcode = StringUtil.trim((String)segInfRs.get(HFBusiDict.PROVCODE));// ADD BY ZHUOYANGYANG 2014-03-18  增加北京白名单库判断
//		if ("010".equals(provcode)) {
//			String isNeedCheck = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "BJWHITEUSER_NEEDCHECKFLAG", ""));
//			
//			if ("true".equals(isNeedCheck)) {
//				//查询北京白名单库
//				String bjMeridLists = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "BJWHITEUSER_CANACCESSMERISLISTS", ""));
//				if(bjMeridLists.indexOf(merId)!=-1){
//					logInfo("北京白名单鉴权通过，该商户在可交易商户列表内故不做用户白名单库查询：%s %s",merId,bjMeridLists);
//				}else{
//					Map<String,String> whiteUserMap = new HashMap<String,String>();
//					whiteUserMap.put(HFBusiDict.MOBILEID, mobileId);	
//					whiteUserMap.put(HFBusiDict.USERTYPE, "0");	//0:白名单 1：黑名单 2：其他
//					Map<String,Object> whiteUserRs = LocalUtil.doGetService("/hfrestbusi/hfbjWhiteUserInfo/"+rpid+"/"+mobileId+".xml", whiteUserMap);
//					retCode = StringUtil.trim((String) whiteUserRs.get(HFBusiDict.RETCODE));
//					if(!BusiConst.SUCCESS.equals(retCode)){
//						logInfo("获取白名单信息失败,返回码：%s %s", retCode,mobileId);
//						return retCode;
//					}else{
//						logInfo("北京白名单鉴权通过,返回码：%s %s", retCode,mobileId);
//					}
//				}
//			}else{
//				logInfo("北京白名单鉴权通过,无需判断：%s %s", isNeedCheck,mobileId);
//			}
//		}else{
//			logInfo("非北京用户，无需白名单鉴权：%s %s", provcode,mobileId);
//		}
		//获取商户信息
		Map<String,String> merInfMap = new HashMap<String,String>();
		merInfMap.put(HFBusiDict.MERID, merId);
		Map<String,Object> merInfRs = LocalUtil.doGetService("/hfrestbusi/merinf/"+rpid+"/"+merId+".xml", merInfMap);
		retCode = (String) merInfRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商户信息失败,返回码：%s", retCode);
			return retCode;
		}
		
		//获取商户银行信息
		Map<String,String> merBankMap = new HashMap<String,String>();
		merBankMap.put(HFBusiDict.MERID, merId);
		merBankMap.put(HFBusiDict.PROVCODE, StringUtil.trim((String)segInfRs.get(HFBusiDict.PROVCODE)));
		Map<String,Object> merBankRs = LocalUtil.doGetService("/hfrestbusi/merbank/"+rpid+".xml", merBankMap);
		retCode = (String) merBankRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商户银行信息失败,返回码：%s", retCode);
			return retCode;
		}
		
		//获取商品信息
		Map<String,String> goodsInfMap = new HashMap<String,String>();
		goodsInfMap.put(HFBusiDict.MERID, merId);
		goodsInfMap.put(HFBusiDict.GOODSID, goodsId);
		Map<String,Object> goodsInfRs = LocalUtil.doGetService("/hfrestbusi/goodsinf/"+rpid+"/"+merId+"-"+goodsId+".xml", goodsInfMap);
		retCode = (String) goodsInfRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商品信息失败,返回码：%s", retCode);
			return retCode;
		}
		
		//获取商品银行信息
		Map<String,String> goodsBankMap = new HashMap<String,String>();
		goodsBankMap.put(HFBusiDict.MERID, merId);
		goodsBankMap.put(HFBusiDict.GOODSID, goodsId);
		Map<String,Object> goodsBankRs = LocalUtil.doGetService("/hfrestbusi/goodsbank/"+rpid+".xml", goodsBankMap);
		retCode = (String) goodsBankRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商品银行信息失败,返回码：%s", retCode);
			return retCode;
		}
		out.put(HFBusiDict.HASUMG, "0");//包月定制关系，0不存在

		List<Map<String,Object>> merBank = (List<Map<String,Object>>) merBankRs.get(HFBusiDict.MERBANKS);
		List<Map<String,Object>> goodsBank = (List<Map<String,Object>>)goodsBankRs.get(HFBusiDict.GOODSBANKS);
		List<Map<String,Object>> banks = new ArrayList<Map<String,Object>>();
		String provCode = segInfRs.get(HFBusiDict.PROVCODE).toString();
		//计算支付银行
		for (Iterator<Map<String, Object>> iterator = goodsBank.iterator(); iterator.hasNext();) {
			Map<String,Object> gBank = (Map<String, Object>) iterator.next();
			String gBankId = gBank.get(HFBusiDict.BANKID)==null?null:(gBank.get(HFBusiDict.BANKID).toString());
			
			//20111206 zhangwl 小额银行要进行merbank和goodsbank的交集，全网银行以goodsbank为准
			if(gBankId.startsWith("XE"+provCode)){//20120718 liujilong 加入小额银行对省份的过滤
				for (Iterator<Map<String, Object>> iter = merBank.iterator(); iter.hasNext();) {
					Map<String,Object> mBank = (Map<String, Object>) iter.next();
					String mBankId = mBank.get(HFBusiDict.BANKID)==null?null:(mBank.get(HFBusiDict.BANKID).toString());
					if(mBankId!=null && mBankId.equals(gBankId)){
						banks.add(gBank);
					}
				}
			}else if(gBankId.startsWith("BS"+provCode)||gBankId.startsWith("GM"+provCode)){// 20140918 xuwei 加入博升,游戏基地
				banks.add(gBank);
			}
			
		}
		if(banks.size()==0){
			this.logInfo("支付银行不存在", "");	
			String flag = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "MERBANK.silentSMS."+merId+"."+provCode, ""));
		    if ("true".equalsIgnoreCase(flag)){
			    logInfo("商户银行未开通是否沉默短信标识:%s",flag);
				return "86801096";
			}
			return "86801104";
		}
		List<Map<String,Object>> userBankList = new ArrayList<Map<String,Object>>();
		//设置用户银行
		Map<String,Object> m =new HashMap<String,Object>();
		m.put(HFBusiDict.BANKID, "BS"+provCode+"000");//新增博升用户银行，默认开通
		
		Map<String,Object> m2 =new HashMap<String,Object>();
		m2.put(HFBusiDict.BANKID, "GM"+provCode+"000");//新增游戏基地用户银行，默认开通
		userBankList.add(m);
		userBankList.add(m2);
		//获取已开通小额的省份
		String provCodes=NamedProperties.getMapValue(BusiConst.SYSPARAMS, "XE.ProvList", "");
		if(provCodes.indexOf(provCode)!=-1){//如果是小额用户
			Map<String,Object> m1 =new HashMap<String,Object>();
			m1.put(HFBusiDict.BANKID, "XE"+provCode+"000");
			userBankList.add(m1);
		}
		//获取用户银行与商品银行交集
	
		List<Map<String, Object>> combineList = findCombineBanks(banks, userBankList);
		logInfo("请求数据中的支付银行列表和用户银行列表的交集combineList["+combineList+"]");
		if(combineList.size() <= 0){
			//无可支付银行
			 logInfo("无可支付银行:%s",rpid);
			 return "86801097";
		}			
		segInfRs.remove(HFBusiDict.MOBILEID);//防止out区字段被覆盖
		out.putAll(specUserRs);
		out.putAll(segInfRs);
		out.putAll(merInfRs);
		out.putAll(goodsInfRs);
		out.put("combineList", combineList);
		logInfo("用户交易鉴权验证通过:%s %s", userBankList,banks);
		return BusiConst.SUCCESS;
	}

		
		
		/**
		 * ********************************************
		 * method name   : findCombineBanks 
		 * description   : 取得支付银行和用户银行列表交集
		 * @return       : List<Map<String,Object>>
		 * @param        : @param payBankList
		 * @param        : @param userBankList
		 * @param        : @return
		 * modified      : zhaoyan ,  Nov 11, 2011 11:26:07 AM
		 * @see          : 
		 * *******************************************
		 */
		private List<Map<String, Object>> findCombineBanks(
				List<Map<String, Object>> payBankList,
				List<Map<String, Object>> userBankList) {
			List<Map<String, Object>> combineList = new ArrayList<Map<String, Object>>();
			for (Iterator iterator = payBankList.iterator(); iterator.hasNext();) {
				Map<String, Object> payBankMap = (Map<String, Object>) iterator.next();
				String payBankKey = StringUtil.trim((String)payBankMap.get(HFBusiDict.BANKID));
				String kstate = StringUtil.trim(String.valueOf(payBankMap.get(HFBusiDict.KSTATE)));
				//11只开通新增 13新增与续费全部开通
				if(kstate.equals("11") || kstate.equals("13")){
					for (Iterator iterator2 = userBankList.iterator(); iterator2.hasNext();) {
						Map<String, Object>  userBankMap = (Map<String, Object>) iterator2.next();
						String userBankKey = (String)userBankMap.get(HFBusiDict.BANKID);
						if(payBankKey.equals(userBankKey)){
							combineList.add(payBankMap);
						}
					}
				}			
			}
			return combineList;
		}
}
