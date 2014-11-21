package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;

/** 
* @ClassName  : HfMerBusinessRest 
* @author 	  : panxingwu 
* @date 	  : 2014年1月3日 下午2:23:28 
* @Description: 商户业务类型
*/
public class HfMerBusinessRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));//必输
		String businessType = StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE));//必输
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID+".string",merId);
		map.put(HFBusiDict.BUSINESSTYPE+".string", businessType);
		Map<String,Object> rs = dal.get("psql_HFMERBUSI.get", map);
		if(rs==null||rs.size()==0){
			logInfo("商户未配置业务类型，默认为关闭业务!");
			return "86801126";
		}
		String state = String.valueOf(rs.get(HFBusiDict.STATE));
		if(!"2".equals(state)){
			logInfo("商户未开通该业务");
			return "86801126";
		}
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}
	
}
