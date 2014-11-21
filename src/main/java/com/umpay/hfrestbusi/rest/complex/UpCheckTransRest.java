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
import com.umpay.hfrestbusi.util.IccidUtil;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfCheckTrans
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  交易鉴权
 * @see        :                        
 * ************************************************/   
public class UpCheckTransRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("执行交易鉴权,参数：%s", urlargs);
		String retCode = BusiConst.SYS_ERROR;
		
		Map<String,String> merMap = new HashMap<String,String>();
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String provCode=StringUtil.trim(urlargs.get(HFBusiDict.PROVCODE));
		merMap.put(HFBusiDict.MERID, merId);
		
		//获取商户信息
		Map<String,Object> merInf = LocalUtil.doGetService("/hfrestbusi/merinf/"+rpid+"/"+merId+".xml", merMap);
		retCode = merInf.get(HFBusiDict.RETCODE).toString();
		
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商户信息失败,merid:%s", merId);
			return retCode;
		}
		Map<String,String> goodsMap = new HashMap<String,String>();
		goodsMap.put(HFBusiDict.MERID, merId);
		goodsMap.put(HFBusiDict.GOODSID, goodsId);
		
		//获取商品信息
		Map<String,Object> goodsInf = LocalUtil.doGetService("/hfrestbusi/goodsinf/"+rpid+"/"+merId+"-"+goodsId+".xml", goodsMap);
		retCode = goodsInf.get(HFBusiDict.RETCODE).toString();
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商品信息失败：%s %s", merInf,goodsInf);
			return retCode;
		}
		
		//获取商户银行信息
		Map<String,String> merBankMap = new HashMap<String,String>();
		merBankMap.put(HFBusiDict.MERID, merId);
		merBankMap.put(HFBusiDict.PROVCODE, provCode);
		Map<String,Object> merBankRs = LocalUtil.doGetService("/hfrestbusi/merbank/"+rpid+".xml", merBankMap);
		retCode = (String) merBankRs.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商户银行信息失败,返回码：%s", retCode);
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
		
		List<Map<String,Object>> merBank = (List<Map<String,Object>>) merBankRs.get(HFBusiDict.MERBANKS);
		List<Map<String,Object>> goodsBank = (List<Map<String,Object>>)goodsBankRs.get(HFBusiDict.GOODSBANKS);
		List<Map<String,Object>> banks = new ArrayList<Map<String,Object>>();
	    if(!"".equals(provCode)){
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
				}else if(gBankId.startsWith("BS"+provCode)||gBankId.startsWith("GM"+provCode)){// 20140918 xuwei 加入博升，游戏基地
					banks.add(gBank);
				}
				
			}
			if(banks.size()==0){
				this.logInfo("支付银行不存在", "");	
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
			out.put("combineList", combineList);
			out.put("getBankList", "true");
	    }else{
	    	out.put("getBankList", "false");
	    }
		out.putAll(merInf);
		out.putAll(goodsInf);
		
		logInfo("交易鉴权通过,鉴权信息：%s %s", merInf,goodsInf);
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
