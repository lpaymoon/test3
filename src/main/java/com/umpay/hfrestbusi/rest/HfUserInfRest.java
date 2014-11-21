package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.cache.CacheUtil;
import com.umpay.hfrestbusi.cache.HfUserInfoCacheHelper;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;


/** ******************  类说明  *********************
 * class       :  HfUserInfRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  根据手机号查询包月用户关系列表（纪鑫宇需求）
 * @see        :                        
 * ************************************************/   
public class HfUserInfRest extends BaseRest {

	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		logInfo("查询包月用户关系列表");
		List<Map<String, Object>> rs = (List<Map<String, Object>>) CacheUtil.getObject("hfuserinfo", mobileid, map, new HfUserInfoCacheHelper(dal));
		if(rs==null||rs.size()==0){
			logInfo("包月用户关系不存在");
			return "86801400";
		}
		out.put("hfuserlist", rs);
		return BusiConst.SUCCESS;
		
	}

}
