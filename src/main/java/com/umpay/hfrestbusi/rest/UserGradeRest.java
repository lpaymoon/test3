package com.umpay.hfrestbusi.rest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;

/**
 * ******************  类说明  *********************
 * class       :  UserGradeRest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  用户等级资源
 * @see        :                        
 * ***********************************************
 */
public class UserGradeRest extends BaseRest {

	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : LiuJiLong ,  2011-11-7
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id) throws Exception {
		// LOG输出上下文
		this.logInfo("查询用户等级开始,查询参数为%s", urlargs);
		
		// 装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		Map<String, Object> rs = new HashMap<String, Object>();
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		args.put(HFBusiDict.MOBILEID+".string", mobileid);
		
		// 资源获取
		rs = dal.get("psql_USERGRADEREST.getGrade", args);
		
		// 检查结果，决定返回码
		this.logInfo("开始校验结果", "");
		
		//如果查询到对象则返回查询到的值,如果没有则返回默认值(GRADE:1; PAYLOCK:0)
		if (rs == null || rs.get(HFBusiDict.GRADE) == null) {
			this.logInfo("没有查询到对象,默认为新用户,返回GRADE:1; PAYLOCK:0; mobileid:%s", mobileid);
			out.put(HFBusiDict.GRADE, new Integer(1));
			out.put(HFBusiDict.PAYLOCK, new Integer(0));
			out.put(HFBusiDict.MODTIME, new Timestamp(System.currentTimeMillis()));
		} else {
			this.logInfo("查询到对象,返回查询到的值:%s; mobileid:%s", rs.get(HFBusiDict.GRADE) + "-"
					+ rs.get(HFBusiDict.PAYLOCK), mobileid);
			out.putAll(rs);
		}
		return BusiConst.SUCCESS;
	}
}
