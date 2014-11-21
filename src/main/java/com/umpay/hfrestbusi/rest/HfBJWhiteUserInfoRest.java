package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;
/**
 * ******************  类说明  *********************
 * class       :  BJWhiteUserInfoRest
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  北京白名单库信息资源
 * @see        :                        
 * ***********************************************
 */
public class HfBJWhiteUserInfoRest extends BaseRest{

	/** ********************************************
	 * method name   : doShowService 
	 * add           : zhuoyangyang ,  2014-03-18
	 * description   : 根据手机号和用户类型查询用户信息
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("查询北京白名单用户信息:%s", urlargs);
		
		// 装载初始化参数
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String userType = StringUtil.trim(urlargs.get(HFBusiDict.USERTYPE));
		Map<String,String> args = new HashMap<String,String>();
		
		
		args.put(HFBusiDict.MOBILEID+".string",mobileId);
		args.put(HFBusiDict.USERTYPE+".int",userType);
		
		//获取白名单用户信息资源
		Map<String,Object> rs = dal.get("psql_HFBJWHITEUSERREST.getInfo", args);
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//白名单用户信息查询结果为空
		if(rs==null||rs.size()==0){
			logInfo("该手机用户信息不存在或非北京白名单用户：%s %s",mobileId,userType);
			return "86802029";
		}
		
		logInfo("查询北京白名单信息成功：%s", rs);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}
}
