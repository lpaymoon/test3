package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;

/**
 * ******************  类说明  *********************
 * class       :  HfUserRest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  包月订购关系资源
 * @see        :                        
 * ***********************************************
 */
public class HfUserRest extends BaseRest {

	/**
	 * ******************************************** method name : doShowService
	 * modified : LiuJiLong , 2011-11-7
	 * 
	 * @see : @see
	 *      com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map,
	 *      java.lang.String)
	 * ********************************************/
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		// LOG输出上下文
		this.logInfo("查询包月用户关系开始,查询参数为%s", urlargs);
		
		// 装载初始化参数
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		
		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.MOBILEID+".string", mobileid);
		args.put(HFBusiDict.MERID+".string", merid);
		args.put(HFBusiDict.GOODSID+".string", goodsid);
		// 资源获取
		this.logInfo("正在获取包月用户关系信息", "");
		Map<String, Object> rs = null;
		rs = dal.get("psql_HFUSERREST.getMobileInf", args);
		
		// 结果检查
		this.logInfo("开始校验结果", "");
		
		//查询结果是否存在
		if (rs == null || rs.get(HFBusiDict.ORDERTIME) == null) {
			this.logInfo("包月用户关系不存在:%s",mobileid + "-"+ merid + "-" + goodsid);
			return "86801400";
		} else {
			this.logInfo("查询到包月用户关系,返回查询到的值%s %s", BusiConst.SUCCESS, mobileid + "-" + merid
					+ "-" + goodsid);
			out.putAll(rs);
			return BusiConst.SUCCESS;
		}

	}

	/**
	 * ******************************************** method name :
	 * doCreateService modified : LiuJiLong , 2011-11-7
	 * 
	 * @see : @see
	 *      com.umpay.hfrestbusi.core.BaseRest#doCreateService(java.util.Map)
	 * ********************************************/
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {

		// LOG输出上下文
		this.logInfo("插入包月用户信息开始,参数为%s", urlargs);
		
		// 装载初始化参数
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));

		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.MERID+".string", merid);
		args.put(HFBusiDict.GOODSID+".string", goodsid);
		args.put(HFBusiDict.MOBILEID+".string", mobileid);
		args.put(HFBusiDict.ENDDATE+".string", urlargs.get(HFBusiDict.ENDDATE));
		args.put(HFBusiDict.BANKID+".string", urlargs.get(HFBusiDict.BANKID));
		args.put(HFBusiDict.ODDINTERVAL+".int", StringUtil.getDefault(urlargs.get(HFBusiDict.ODDINTERVAL), "0"));
		// serviceid默认值为空
		args.put(HFBusiDict.SERVICEID+".string", StringUtil.getDefault(urlargs
				.get(HFBusiDict.SERVICEID), ""));
		args.put(HFBusiDict.VERIFYCODE+".string", urlargs.get(HFBusiDict.VERIFYCODE));
		args.put(HFBusiDict.RPID+".string", urlargs.get(HFBusiDict.RPID));
		
		//获取包月用户资源
		int rs = 0;
		try {
			this.logInfo("向包月用户表中插入数据", "");
			rs = dal.insert("psql_HFUSERREST.insert", args);
		} catch (Exception e) {
			if (e instanceof DBPKConflictException) {
				this.logInfo("向包月用户表中插入数据中出现主键冲突异常,异常代码与参数为:%s %s",
						"86801402", merid+"-"+goodsid+"-"+mobileid);
				this.logException(e);
				return "86801402";
			} 
			throw e;
		}
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//无主键冲突异常且执行改变的数据条数为0时报系统未知错误
		if (rs != 1) {
			this.logInfo("向包月用户表中插入数据时出现系统未知错误,异常代码与参数为:%s %s",
						BusiConst.SYS_ERROR, merid+"-"+goodsid+"-"+mobileid);
			return BusiConst.SYS_ERROR;
		}
		
		// 向mobile.thfuserlog_YYMM中插入记录
		this.logInfo("向包月用户表中插入数据成功,准备向日志表中插入数据", "");
		//重新初始化输入参数
		args.clear();
		args.put(HFBusiDict.MERID+".string", urlargs.get(HFBusiDict.MERID));
		args.put(HFBusiDict.GOODSID+".string", urlargs.get(HFBusiDict.GOODSID));
		args.put(HFBusiDict.MOBILEID+".string", urlargs.get(HFBusiDict.MOBILEID));
		args.put(HFBusiDict.FSTATE+".int", "0");
		args.put(HFBusiDict.BSTATE+".int", "2");
		args.put(HFBusiDict.BANKID+".string", urlargs.get(HFBusiDict.BANKID));
		// serviceid默认值为空
		args.put(HFBusiDict.SERVICEID+".string", StringUtil.getDefault(urlargs
				.get(HFBusiDict.SERVICEID), ""));
		args.put(HFBusiDict.RPID+".string", urlargs.get(HFBusiDict.RPID));
		args.put(HFBusiDict.CAUSE+".int", StringUtil.getDefault(urlargs.get(HFBusiDict.CAUSE),"2"));
		args.put(HFBusiDict.DETAIL+".string", StringUtil.getDefault(urlargs.get(HFBusiDict.DETAIL),"用户定购服务"));
		args.put("_tableYYMM", TimeUtil.date8().substring(2, 6));		//日志表的表名后缀
		
		//向日志表中插入数据
		try {
			rs = dal.insert("psql_HFUSERREST.insertLog", args);
		} catch (DBPKConflictException e) {
			this.logInfo("向包月用户关系表中插入数据中出现主键冲突异常,异常代码与参数为:%s %s",
					"86801403", merid+"-"+goodsid+"-"+mobileid);
			return "86801403";
		}
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//无主键冲突异常且执行改变的数据条数为0时报系统未知错误
		if (rs != 1) {
			this.logInfo("向包月用户关系表中插入数据时出现系统未知错误,异常代码与参数为:%s %s",
						BusiConst.SYS_ERROR, merid+"-"+goodsid+"-"+mobileid);
			return BusiConst.SYS_ERROR;
		}
		this.logInfo("向包月用户关系表中插入数据成功,返回码与参数为:%s %s",
						BusiConst.SUCCESS, merid+"-"+goodsid+"-"+mobileid);
		return BusiConst.SUCCESS;
	}

	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		logInfo("查询包月用户定制关系计费代码,mobileid=%s", mobileid);
		List<Map<String,Object>> rs = dal.find("psql_THFUSER.getBYServiceid",map);
		if(rs==null||rs.size()==0){
			logInfo("包月用户定制关系计费代码不存在:mobileid=%s",mobileid);
			return "86802013";
		}
		logInfo("查询包月定制关系计费代码成功:%s", rs);
		out.put(HFBusiDict.SERVICEID, rs);
		return BusiConst.SUCCESS;
	}
}
