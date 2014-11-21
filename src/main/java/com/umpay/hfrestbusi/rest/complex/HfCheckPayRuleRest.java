package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;


/** ******************  类说明  *********************
 * class       :  HfCheckPayRuleRest
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  支付鉴权+获取支付规则
 * @see        :                        
 * ************************************************/   
public class HfCheckPayRuleRest extends BaseRest {

	@SuppressWarnings("unchecked")
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String bankId = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String xeseq = StringUtil.trim(urlargs.get(HFBusiDict.XESEQ));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String isnew = StringUtil.trim(urlargs.get(HFBusiDict.ISNEW));//0是新增，1是续费
		
		String retCode = BusiConst.SYS_ERROR;
		
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
		
		//获取商户银行信息
		Map<String,String> merBankMap = new HashMap<String,String>();
		merBankMap.put(HFBusiDict.MERID, merId);
		merBankMap.put(HFBusiDict.BANKID, bankId);
		if(!bankId.startsWith("MW")){
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
		}
		if("0".equals(isnew)){
			if(!("11".equals(kstate)||"13".equals(kstate))){
				logInfo("商品银行未开通新增");
				return "86801018";
			} 
		}else{
			if(!("12".equals(kstate)||"13".equals(kstate))){
				logInfo("商品银行未开通续费");
				return "86801019";
			}
		}
		Map<String,Object> goodsService = new HashMap<String,Object>();
		if(bankId.startsWith("MW")){
			Map<String,String> serviceMap = new HashMap<String,String>();
			serviceMap.put(HFBusiDict.MERID, merId);
			serviceMap.put(HFBusiDict.GOODSID, goodsId);
			goodsService = LocalUtil.doGetService("/hfrestbusi/goodsserviceid/"+merId+"-"+goodsId+".xml", serviceMap);
			retCode = (String) goodsService.get(HFBusiDict.RETCODE);
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("【支付鉴权】获取商品计费代码失败");
				return retCode;
			}
			out.put(HFBusiDict.FEETYPE, goodsService.get(HFBusiDict.FEETYPE));
		}
		//如果是定价模式，要检查amount
		if("0".equals(goodsInf.get(HFBusiDict.PRICEMODE).toString())){
			if(!amount.equals(StringUtil.trim(goodsBank.get(HFBusiDict.AMOUNT).toString()))){
				logInfo("定价商品金额不符,商品配置金额为:%s, 实际金额为:%s", goodsBank.get(HFBusiDict.AMOUNT),amount);
				return "86801057";
			}
		}
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
		String provcode = StringUtil.trim((String)segInf.get(HFBusiDict.PROVCODE));// ADD BY ZHUOYANGYANG 2014-03-18  增加北京白名单库判断
		if ("010".equals(provcode)) {
			String isNeedCheck = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "BJWHITEUSER_NEEDCHECKFLAG", ""));
			if ("true".equals(isNeedCheck)) {  
				//查询北京白名单库
				String bjMeridLists = StringUtil.trim(NamedProperties.getMapValue(BusiConst.SYSPARAMS, "BJWHITEUSER_CANACCESSMERISLISTS", ""));
				if(bjMeridLists.indexOf(merId)!=-1){
					logInfo("北京白名单鉴权通过，该商户在可交易商户列表内故不做用户白名单库查询：%s %s",merId,bjMeridLists);
				}else{
					Map<String,String> whiteUserMap = new HashMap<String,String>();
					whiteUserMap.put(HFBusiDict.MOBILEID, mobileId);	
					whiteUserMap.put(HFBusiDict.USERTYPE, "0");	//0:白名单 1：黑名单 2：其他
					Map<String,Object> whiteUserRs = LocalUtil.doGetService("/hfrestbusi/hfbjWhiteUserInfo/"+rpid+"/"+mobileId+".xml", whiteUserMap);
					retCode = StringUtil.trim((String) whiteUserRs.get(HFBusiDict.RETCODE));
					if(!BusiConst.SUCCESS.equals(retCode)){
						logInfo("获取白名单信息失败,返回码：%s %s", retCode,mobileId);
						return retCode;
					}else{
						logInfo("北京白名单鉴权通过,返回码：%s %s", retCode,mobileId);
					}
				}
			}else{
				logInfo("北京白名单鉴权通过,无需判断：%s %s", isNeedCheck,mobileId);
			}
		}else{
			logInfo("非北京用户，无需白名单鉴权：%s %s", provcode,mobileId);
		}
		out.put(HFBusiDict.HASUMG, "0");//包月定制关系，0不存在
		//获取包月用户关系信息
		if("3".equals(goodsInf.get(HFBusiDict.SERVTYPE).toString())&&("0".equals(isnew))){//是包月商品才执行
			Map<String,String> hfUserMap = new HashMap<String,String>();
			hfUserMap.put(HFBusiDict.MERID, merId);
			hfUserMap.put(HFBusiDict.MOBILEID,mobileId);
			hfUserMap.put(HFBusiDict.GOODSID,goodsId);
			Map<String,Object> thfUserRs = LocalUtil.doGetService("/hfrestbusi/hfuser/common/"+rpid+"/"+mobileId+"-"+merId+"-"+goodsId+".xml", hfUserMap);
			retCode = (String) thfUserRs.get(HFBusiDict.RETCODE);
			if(BusiConst.SUCCESS.equals(retCode)){
				Integer state = (Integer) thfUserRs.get(HFBusiDict.STATE);	
				out.put(HFBusiDict.USERSTATUS, state);
				if(state==2){//2为已定制状态
					logInfo("获取包月用户关系成功，包月用户 定制关系已经存在!%s", retCode);
					out.put(HFBusiDict.HASUMG, "1");//存在包月定制关系，设置为1
					return "86801401";
				}
			}
		}
		long servicesequence = 0;
		if(!StringUtil.isNullOrNovalue(xeseq)){
			logInfo("输入了xeseq字段，需要生成序列数");
			servicesequence = SequenceUtil.getInstance().getBatchSeq(xeseq);
			logInfo("生成的序列数为:%s",servicesequence);
			out.put(HFBusiDict.SERVICESEQUENCE,servicesequence);
		}else{
			logInfo("没有传入xeseq不需要生成序列数");
		}
		//获取支付规则
		Map<String,Object> payRules = new HashMap<String,Object>();
		Map<String,String> payRuleMap = new HashMap<String,String>();
		payRuleMap.put(HFBusiDict.PROVCODE, provcode);
		if(bankId.startsWith("MW")){
			payRules = LocalUtil.doGetService("/hfrestbusi/mwpayrule/"+rpid+".xml", payRuleMap);	
		}else if (bankId.startsWith("XE")){
			payRules = LocalUtil.doGetService("/hfrestbusi/xepayrule/"+rpid+".xml", payRuleMap);
		}
		retCode = (String) payRules.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取支付规则信息失败,返回码：%s", retCode);
			return retCode;
		}
		List<Map<String,Object>> payrules = (List<Map<String,Object>>)payRules.get("payrules");
		Map<String,Object> payrulesvalues = new HashMap<String,Object>();
		if(payrules.size()>0){
			for (Iterator<Map<String, Object>> iterator = payrules.iterator(); iterator.hasNext();) {
				Map<String,Object> payrule = (Map<String, Object>) iterator.next();
		
				StringBuilder keys = new StringBuilder();
				StringBuilder values = new StringBuilder();
				if(bankId.startsWith("MW")){
					String grade = StringUtil.trim(String.valueOf(payrule.get(HFBusiDict.GRADE)));
					if("-1".equals(grade)){
						grade = "*";
					}
				    keys.append(grade).append(".").
					append(getKeys((String)payrule.get(HFBusiDict.PROVCODE))).append(".").
					append(getKeys((String)payrule.get(HFBusiDict.AREACODE))).append(".").
					append(getKeys((String)payrule.get(HFBusiDict.MERID))).append(".").
					append(getKeys((String)payrule.get(HFBusiDict.GOODSID))).append(".").
					append(getKeys((String)payrule.get(HFBusiDict.CARDTYPE))).append(".").
				    append(getKeys((String)payrule.get(HFBusiDict.FEETYPE)));
					values.append(getValues((String)payrule.get("surplusacl"))).append("|").
					append(getValues((String)payrule.get("accesstimeacl"))).append("|").
					append(getValues((String)payrule.get("voiceacl"))).append("|").
					append(getValues((String)payrule.get("brandtime"))).append("|").
					append(getValues((String)payrule.get("userstatus")));
					payrulesvalues.put(keys.toString(),values.toString());
				}else if(bankId.startsWith("XE")){
					String grade = StringUtil.trim(String.valueOf(payrule.get(HFBusiDict.GRADE)));
					if("-1".equals(grade)){
						grade = "*";
					}
					keys.append(grade).append(".");
					keys.append(getKeys((String)payrule.get(HFBusiDict.PROVCODE))).append(".");
					keys.append(getKeys((String)payrule.get(HFBusiDict.AREACODE))).append(".");
					keys.append(getKeys((String)payrule.get(HFBusiDict.MERID))).append(".");
					keys.append(getKeys((String)payrule.get(HFBusiDict.GOODSID))).append(".");
					keys.append(getKeys((String)payrule.get(HFBusiDict.CARDTYPE))).append(".");
					keys.append(getKeys((String)payrule.get(HFBusiDict.FEETYPE)));
				
					
					values.append(getValues((String)payrule.get("surplusacl"))).append("|");
					values.append(getValues((String)payrule.get("accesstimeacl"))).append("|");
					values.append(getValues((String)payrule.get("voiceacl"))).append("|");
					values.append(getValues((String)payrule.get("brandtimeacl"))).append("|");
					values.append(getValues((String)payrule.get("userstatus"))).append("|");
					values.append(getValues((String)payrule.get("balsignacl"))).append("|");
					values.append(getValues((String)payrule.get("starttimeacl")));
					payrulesvalues.put(keys.toString(),values.toString());
				}
				
			}
		}
		//全网获取用户等级
		if(bankId.startsWith("MW")){
			Map<String,String> gradeMap = new HashMap<String,String>();
			gradeMap.put(HFBusiDict.MOBILEID, mobileId);
			Map<String,Object> userGradeRs = LocalUtil.doGetService("/hfrestbusi/mwuserltd/"+rpid+"/"+mobileId+".xml", gradeMap);
			retCode = (String) userGradeRs.get(HFBusiDict.RETCODE);
			if(BusiConst.SUCCESS.equals(retCode)){
				     logInfo("获取全网用户等级信息成功,返回码：%s", retCode);
				     out.put(HFBusiDict.GRADE, StringUtil.trim(String.valueOf(userGradeRs.get(HFBusiDict.GRADE))));
			 }else{
				 logInfo("获取全网用户等级信息失败,返回码：%s", retCode);
			 }
		}
		
		out.putAll(merInf);
		out.putAll(goodsInf);
		out.putAll(segInf);
		out.putAll(specUser);
		out.put(HFBusiDict.KSTATE,goodsBank.get(HFBusiDict.KSTATE));
		out.put(HFBusiDict.ISREALTIME, goodsBank.get(HFBusiDict.ISREALTIME));
		out.put("payrulesList", payrulesvalues);
		logInfo("支付鉴权通过","");
		return BusiConst.SUCCESS;
	}
	public String getKeys(String key){
		if("".equals(StringUtil.trim(key))){
			return "*";
		}
		return key;
	}
	public String getValues(String value){
		if("".equals(StringUtil.trim(value))){
			return "-1";
		}
		return value;
	}
}
