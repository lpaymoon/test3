package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.LocalDal;
import com.umpay.hfrestbusi.util.StringUtil;

/** ******************  类说明  *********************
 * class       :  SeginfRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  号段信息
 * @see        :                        
 * ************************************************/   
public class SeginfRest extends BaseRest {

	/**
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2011-11-3
	 * description   : 根据主键查询号段相关信息
	 */     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)throws Exception {
		this.logInfo("查询号段信息%s", urlargs);
		//获取DAL
		CommonDalInf dal = new LocalDal();
		
		// 装载初始化参数
		Map<String,String> args = new HashMap<String,String>(); 
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		args.put(HFBusiDict.MOBILEID+".string",mobileId);
		 
		//获取号段资源
		Map<String, Object> rs = null;
		rs = dal.get("psql_SEGINFREST.getByKey", args);
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//号段查询为空
		if(rs==null||rs.size()==0){
			logInfo("号段信息不存在:mobileId=%s", mobileId);
			return "86801028";
		}
		
		out.putAll(rs);
		logInfo("查询号段信息成功%s", rs);
		return BusiConst.SUCCESS;
	}
}
