/** *****************  JAVA头文件说明  ****************
 * file name  :  HfUserXeLtdRest.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-2-19
 * *************************************************/ 

package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.UserMerLtdHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfUserXeLtdRest
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class HfUserMerLtdRest extends BaseRest {

	private String userMerLtdKey = "userMerRule";
	
	/** ********************************************
	 * method name   : doCreateService 
	 * modified      : LiuJiLong ,  2012-2-24
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doCreateService(java.util.Map)
	 * ********************************************/     
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		// LOG输出上下文
		this.logInfo("执行用户商户限额,参数为：%s", urlargs);
		// 装载初始化参数
		String mobileid = urlargs.get(HFBusiDict.MOBILEID);
		String amt = urlargs.get(HFBusiDict.AMT);
		String merId = urlargs.get(HFBusiDict.MERID);
		String bankId = urlargs.get(HFBusiDict.BANKID);
		String userStatus = urlargs.get(HFBusiDict.USERSTATUS);
		if(StringUtil.isNullOrNovalue(userStatus)){
			userStatus = "-1";
		}
		int retcode = 0;
				
		List<Map<String, Object>> rs = (List<Map<String, Object>>) CacheUtil.getObject("userMerLtd", userMerLtdKey, new HashMap<String, String>(), new UserMerLtdHelper(dal));
		//如果没有配置项，直接返回成功
		boolean flag = doMatch(rs, merId, bankId, userStatus);
		if(!flag){
			this.logInfo("没有配置项，返回成功：%s + %s + %s", merId, bankId, userStatus);
			return BusiConst.SUCCESS;
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("in_merid.string", merId);
		map.put("in_mobileid.string", mobileid);
		map.put("in_userstate.int", userStatus);
		map.put("in_bankid.string", bankId);
		map.put("in_amt.double", amt);
		
		Map<String, String> retMap = new HashMap<String,String>();
		retMap = dal.call("call.UserXeLtdCheck", map);
		
		Object o= retMap.get("out_retcode.int");
		String temp = o.toString();
		retcode = Integer.parseInt(temp);

		logInfo("用户商户限额完成，结果为： %s 返回码为：%s",retMap.get("out_str.string"), retcode);
		if(retcode == 0)
			return BusiConst.SUCCESS;
		return "8600"+ (retcode==0?"0000":retcode);
	}

	
	/** ********************************************
	 * method name   : doMatch 
	 * description   : 匹配LIST中的MAP值是否与merId,bankId,userStatus匹配
	 * @return       : boolean
	 * @param        : @param rs
	 * @param        : @param merId
	 * @param        : @param bankId
	 * @param        : @param userStatus
	 * @param        : @return
	 * modified      : LiuJiLong ,  2012-3-2  上午11:28:59
	 * @see          : 
	 * ********************************************/      
	
	private boolean doMatch(List<Map<String, Object>> rs, String merId,
			String bankId, String userStatus) {
		for(Map<String, Object> map : rs){
			
			String tempMerId = (String) map.get(HFBusiDict.MERID);
			String tempBankId = (String) map.get(HFBusiDict.BANKID);
			
			//tempMerId与tempBankId为精确匹配，userStatus可以匹配到精确值或"-1"
			if(tempMerId.equals(merId)&&tempBankId.equals(bankId)){
				
				String tempUserStatus = map.get(HFBusiDict.USERTYPE).toString();
				tempUserStatus = tempUserStatus == null ? "-1" : tempUserStatus;
				if(tempUserStatus.equals(userStatus)||tempUserStatus.equals("-1")){
					return true;
				}
			}
		}
		return false;
	}


	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : LiuJiLong ,  2012-2-24
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		// LOG输出上下文
		this.logInfo("执行用户商户限额回滚,参数为：%s", urlargs);
		// 装载初始化参数
		String mobileId = urlargs.get(HFBusiDict.MOBILEID);
		String merId = urlargs.get(HFBusiDict.MERID);
		String amt = urlargs.get(HFBusiDict.AMT);
		String bankId = urlargs.get(HFBusiDict.BANKID);
		String userStatus = urlargs.get(HFBusiDict.USERSTATUS);
		int retcode = 0;
				
		List<Map<String, Object>> rs = (List<Map<String, Object>>) CacheUtil.getObject("userMerLtd", userMerLtdKey, new HashMap<String, String>(), new UserMerLtdHelper(dal));
		//如果没有配置，直接返回成功
		boolean flag = doMatch(rs, merId, bankId, userStatus);
		if(!flag){
			this.logInfo("没有配置项，返回成功：%s + %s + %s", merId, bankId, userStatus);
			return BusiConst.SUCCESS;
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("in_merid.string", merId);
		map.put("in_mobileid.string", mobileId);
		map.put("in_amt.double", amt);
	
		Map<String, String> retMap = new HashMap<String,String>();
		retMap = dal.call("call.UserXeLtdCancel", map);
		
		Object ret = retMap.get("out_retcode.int");		
		
		if(ret==null){
			logInfo("用户商户限额回滚发生系统未知错误");
			return "86009999";
		}
		retcode = Integer.parseInt(ret.toString());
		
		logInfo("用户商户限额回滚完成");
		if(retcode == 0)
			return BusiConst.SUCCESS;
		return "86001908";
	}
	
}
