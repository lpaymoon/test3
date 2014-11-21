package com.umpay.hfrestbusi.rest.complex;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.TransAclCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;
import com.umpay.hfrestbusi.util.TransAclUtil;


  

/** ******************  类说明  *********************
 * class       :  HfTransAclRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  交易屏蔽验证
 * @see        :                        
 * ************************************************/   
public class HfTransAclRest extends BaseRest {

	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2011-11-10
	 * description   : 根据交易屏蔽模板产生的屏蔽规则对参数
	 *                 进行验证，判断是否对交易进行屏蔽。
	 *                 如果是白名单用户，直接通过不校验
	 * 				  
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("验证交易是否屏蔽，参数为：%s", urlargs);
		String accessType = rpid.substring(0,1);
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String bankid = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		String iscontrol = StringUtil.trim(urlargs.get(HFBusiDict.ISCONTROL));//0：非控制类；1：一级控制类；2：二级控制类
		String provcode = StringUtil.trim(urlargs.get(HFBusiDict.PROVCODE));
		String areacode = StringUtil.trim(urlargs.get(HFBusiDict.AREACODE));
		String nettype = StringUtil.trim(urlargs.get(HFBusiDict.NETTYPE));
		String cardtype = StringUtil.trim(urlargs.get(HFBusiDict.CARDTYPE));
		String grade = StringUtil.trim(urlargs.get(HFBusiDict.GRADE));
        String busiRollType = StringUtil.trim(urlargs.get(HFBusiDict.BUSIROLLTYPE));//特殊用户标识
        String bankType = "0";//银行类型,0:梦网,1:小额,-1:所有
        if(bankid.startsWith("XE")){
        	bankType = "1";
        }
		String retCode = BusiConst.SYS_ERROR;//返回码
		
		//当没有传入特殊用户标识时，查询用户是否为特殊用户
		if(StringUtil.isNullOrNovalue(busiRollType)){
			//获取话费特殊用户
			Map<String,String> specUserMap = new HashMap<String,String>();
			specUserMap.put(HFBusiDict.MOBILEID,mobileid);
			specUserMap.put(HFBusiDict.PLATDATE,TimeUtil.date8(new Date()));
			Map<String,Object> specUserRs = LocalUtil.doGetService("/hfrestbusi/specuser/"+rpid+"/"+mobileid+".xml", specUserMap);
			retCode=specUserRs.get(HFBusiDict.RETCODE).toString();
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("获取话费特殊用户异常，返回码为:%s", retCode);
				return retCode;
			}
			busiRollType = specUserRs.get(HFBusiDict.BUSIROLLTYPE).toString();
			out.putAll(specUserRs);
		}
		
		//11是白名单用户，直接通过，不进行交易屏蔽
		//begin modify by zhaoYan 20140528 busiRollType中是否在配置中，如果存在，则不需要屏蔽
//		if("11".equals(busiRollType)){
//			logInfo("白名单用户，不需要屏蔽,用户信息:%s", mobileid);
//			return BusiConst.SUCCESS;
//		}
		String brtLists = (String)NamedProperties.getMapValue(BusiConst.SYSPARAMS, "busiRollType_list", "11");
		logInfo("配置文件中获取的用户特殊类型为： %s", brtLists);
		if(brtLists.contains(busiRollType)){
			logInfo("此用户的用户类型包含在不需要屏蔽的用户类型中,用户信息:%s", mobileid);
			return BusiConst.SUCCESS;
		}
		//end modify by zhaoYan 20140528 busiRollType中是否在配置中，如果存在，则不需要屏蔽
		if(StringUtil.isNullOrNovalue(provcode,areacode,nettype,cardtype)){
			logInfo("省份，地区，网类型，卡类型有空值，执行号段查询！%s %s %s %s", provcode,areacode,nettype,cardtype);
			Map<String,String> segMap = new HashMap<String,String>();
			segMap.put(HFBusiDict.MOBILEID,mobileid.substring(0, 7));
			//获取号段信息
			Map<String,Object> segRs = LocalUtil.doGetService("/hfrestbusi/seginf/"+rpid+"/"+mobileid.substring(0, 7)+".xml", segMap);
			retCode=(String) segRs.get(HFBusiDict.RETCODE).toString();
			if(!retCode.equals(BusiConst.SUCCESS)){
				logInfo("获取号段信息失败!返回码：%s", retCode);
				return retCode;
			}
			out.putAll(segRs);
			if(StringUtil.isNullOrNovalue(provcode)) provcode = segRs.get(HFBusiDict.PROVCODE).toString();
			if(StringUtil.isNullOrNovalue(areacode)) areacode = segRs.get(HFBusiDict.AREACODE).toString();
			if(StringUtil.isNullOrNovalue(nettype)) nettype = segRs.get(HFBusiDict.NETTYPE).toString();
			if(StringUtil.isNullOrNovalue(cardtype)) cardtype = segRs.get(HFBusiDict.CARDTYPE).toString();
		}
		
		if(StringUtil.isNullOrNovalue(iscontrol)){//设置商品类型
			logInfo("商品类型为空,设置商品类型!iscontrol:%s",iscontrol);
			Map<String,String> goodsMap = new HashMap<String,String>();
			goodsMap.put(HFBusiDict.MERID,merid);
			goodsMap.put(HFBusiDict.GOODSID,goodsid);
			//获取商品信息
			Map<String,Object> goodsRs = LocalUtil.doGetService("/hfrestbusi/goodsinf/"+rpid+"/"+mobileid+".xml", goodsMap);
			retCode=goodsRs.get(HFBusiDict.RETCODE).toString();
			if(!BusiConst.SUCCESS.equals(retCode)){
				logInfo("获取商品信息失败,返回码:%s", retCode);
				return retCode;
			}
			out.putAll(goodsRs);
			
			//设置商品类型
			iscontrol=goodsRs.get(HFBusiDict.ISCONTROL).toString();
		}
		
		if(StringUtil.isNullOrNovalue(grade)){//设置用户等级
			logInfo("用户等级为空,设置用户等级!grade:%s",grade);
			Map<String,String> userGradeMap = new HashMap<String,String>();
			userGradeMap.put(HFBusiDict.MOBILEID, mobileid);
			//查询用户等级
			Map<String,Object> userGradeRs = LocalUtil.doGetService("/hfrestbusi/usergrade/"+rpid+"/"+mobileid+".xml", userGradeMap);
			retCode = userGradeRs.get(HFBusiDict.RETCODE).toString();
			if(!retCode.equals(BusiConst.SUCCESS)){
				logInfo("查询用户等级失败,返回码:%s!", retCode);
				return retCode;
			}
			grade = userGradeRs.get(HFBusiDict.GRADE).toString();
			out.put(HFBusiDict.GRADE, new Integer(grade));
		}
		
		Map<String,String> transAclMap = new HashMap<String,String>();
		transAclMap.put(HFBusiDict.MERID, merid);
		transAclMap.put(HFBusiDict.PROVCODE, provcode);
		transAclMap.put(HFBusiDict.PLATDATE, TimeUtil.datetime14());
		//通过缓存获取交易屏蔽模板
		List<String> transAclList = (List<String>) CacheUtil.getObject("transAcl", merid+"-"+provcode, transAclMap, new TransAclCacheHelper(dal));
		
		if(transAclList == null||transAclList.size()==0){
			logInfo("没有配置交易屏蔽模板：%s" , merid+"-"+provcode);
			return BusiConst.SUCCESS;
		}
		
		logInfo("查询到交易屏蔽模板为:%s", transAclList);
		for (String transAcl:transAclList) {			
			//验证是否进行交易屏蔽
			if(TransAclUtil.checkRegex(merid, goodsid, provcode, areacode, nettype, cardtype, mobileid, bankType, iscontrol, grade, transAcl)){//验证不通过
				//返回码包含接入方式：1850：SMS , 1851：其他  ,1852：web
				retCode="86801851";
				if(bankid.startsWith("XE")){// liujilong 20130416 增加小额限额销售屏蔽的特殊返回码
					retCode = "86801750";
					logInfo("小额限额销售屏蔽，验证未通过，交易屏蔽！屏蔽模板：%s", transAcl);
				}else if(accessType.equals("M")){
					retCode = "86801850";
					logInfo("SMS接入方式，验证未通过，交易屏蔽！屏蔽模板：%s", transAcl);
				}else if(accessType.equals("W")){
					retCode="86801852";
					logInfo("WEB接入方式，验证未通过，交易屏蔽！屏蔽模板：%s", transAcl);
				}else{
					logInfo("其他接入方式，验证未通过，交易屏蔽！屏蔽模板：%s", transAcl);
				}				
				return retCode;
			}
		}
		logInfo("交易屏蔽验证通过，不需要屏蔽!", "");
		return BusiConst.SUCCESS;
	}

}
