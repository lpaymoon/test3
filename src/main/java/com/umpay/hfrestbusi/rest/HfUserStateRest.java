package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;

/**
 * ******************  类说明  *********************
 * class       :  HfUserStateRest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  包月订购关系资源
 * @see        :                        
 * ***********************************************
 */
public class HfUserStateRest extends HfUserRest {

	
	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : LiuJiLong ,  2011-11-7
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		// LOG输出上下文
		this.logInfo("更新包月用户关系开始,参数为：%s", urlargs);
		
		// 装载初始化参数
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));			//商户ID
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));		//商品ID
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));	//手机号
		String fstate = StringUtil.trim(urlargs.get(HFBusiDict.FSTATE));		//修改前状态
		
		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.MERID, merid);
		args.put(HFBusiDict.GOODSID, goodsid);
		args.put(HFBusiDict.MOBILEID, mobileid);
		args.put(HFBusiDict.FSTATE, fstate);
		
		//组装SQL语句的SET语句块
		StringBuffer setPara = new StringBuffer();
		if (urlargs.get(HFBusiDict.HASNOTICED) != null
				&& (!(urlargs.get(HFBusiDict.HASNOTICED).toString().equals(""))))
			setPara.append(HFBusiDict.HASNOTICED + "='"
					+ urlargs.get(HFBusiDict.HASNOTICED) + "',");
		if (urlargs.get(HFBusiDict.ENDDATE) != null
				&& (!(urlargs.get(HFBusiDict.ENDDATE).toString().equals(""))))
			setPara.append(HFBusiDict.ENDDATE + "='"
					+ urlargs.get(HFBusiDict.ENDDATE) + "',");
		if (urlargs.get(HFBusiDict.BSTATE) != null
				&& (!(urlargs.get(HFBusiDict.BSTATE).toString().equals(""))))
			setPara.append(HFBusiDict.STATE + "="
					+ urlargs.get(HFBusiDict.BSTATE) + ",");
		if (urlargs.get(HFBusiDict.BANKID) != null
				&& (!(urlargs.get(HFBusiDict.BANKID).toString().equals(""))))
			setPara.append(HFBusiDict.BANKID + "='"
					+ urlargs.get(HFBusiDict.BANKID) + "',");
		if (urlargs.get(HFBusiDict.SERVICEID) != null
				&& (!(urlargs.get(HFBusiDict.SERVICEID).toString().equals(""))))
			setPara.append(HFBusiDict.SERVICEID + "='"
					+ urlargs.get(HFBusiDict.SERVICEID) + "',");
		if (urlargs.get(HFBusiDict.ODDINTERVAL) != null
				&& (!(urlargs.get(HFBusiDict.ODDINTERVAL).toString().equals(""))))
			setPara.append(HFBusiDict.ODDINTERVAL + "="
					+ urlargs.get(HFBusiDict.ODDINTERVAL) + ",");
		if (urlargs.get(HFBusiDict.CANCELDATE) != null
				&& (!(urlargs.get(HFBusiDict.CANCELDATE).toString().equals(""))))
			setPara.append(HFBusiDict.CANCELDATE + "='"
					+ urlargs.get(HFBusiDict.CANCELDATE) + "',");
		if (urlargs.get(HFBusiDict.ORDERTIME) != null
				&& (!(urlargs.get(HFBusiDict.ORDERTIME).toString().equals(""))))
			setPara.append(HFBusiDict.ORDERTIME + "='"
					+ urlargs.get(HFBusiDict.ORDERTIME) + "',");
		if (urlargs.get(HFBusiDict.VERIFYCODE) != null
				&& (!(urlargs.get(HFBusiDict.VERIFYCODE).toString().equals(""))))
			setPara.append(HFBusiDict.VERIFYCODE + "='"
					+ urlargs.get(HFBusiDict.VERIFYCODE) + "',");
		args.put("setPara", setPara.toString().substring(0, setPara.length()-1));
		
		//插入数据
		this.logInfo("向包月用户表中更新数据中", "");
		int rs = 0;														//语句执行结果,意义为改变的数据库条数量
		rs = dal.update("HFUSERREST.update", args);

		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//查询数据不存在
		if (rs == 0) {
			this.logInfo("所需更新的包月用户关系不存在,参数为%s",
					 merid+"-"+goodsid+"-"+mobileid);
			return "86801400";
		}
		this.logInfo("向包月用户表中更新数据成功,准备向日志表中插入数据", "");
		// 初始化参数
		args.clear();
		args.put(HFBusiDict.MERID+".string", urlargs.get(HFBusiDict.MERID));
		args.put(HFBusiDict.GOODSID+".string", urlargs.get(HFBusiDict.GOODSID));
		args.put(HFBusiDict.MOBILEID+".string", urlargs.get(HFBusiDict.MOBILEID));
		args.put(HFBusiDict.RPID+".string", urlargs.get(HFBusiDict.RPID));
		args.put(HFBusiDict.FSTATE+".int", urlargs.get(HFBusiDict.FSTATE));
		args.put(HFBusiDict.BSTATE+".int", urlargs.get(HFBusiDict.BSTATE));
		args.put(HFBusiDict.BANKID+".string", urlargs.get(HFBusiDict.BANKID));
		args.put(HFBusiDict.SERVICEID+".string", urlargs.get(HFBusiDict.SERVICEID));
		args.put(HFBusiDict.CAUSE+".int", urlargs.get(HFBusiDict.CAUSE));
		args.put(HFBusiDict.DETAIL+".string", urlargs.get(HFBusiDict.DETAIL));
		args.put("_tableYYMM", TimeUtil.date8().substring(2, 6));
		this.logInfo("向包月用户状态变化记录表中更新数据中", "");
		rs = 0;
		try {
			rs = dal.insert("psql_HFUSERREST.insertLog", args);
		} catch (DBPKConflictException e) {
			this.logInfo("INSERT导致主键约束冲突 ,返回码与参数为:%s %s", "86801403", merid+"-"+goodsid+"-"+mobileid);
			return "86801403";
		}
		// 检验结果
		if (rs != 1) {
			this.logInfo("向包月用户状态变化记录表中更新数据时出现系统未知错误,返回码与参数为:%s %s",
					BusiConst.SYS_ERROR,  merid+"-"+goodsid+"-"+mobileid);
			return BusiConst.SYS_ERROR;
		}
		this.logInfo("向包月用户状态变化记录表中更新数据成功,返回码与参数为:%s %s", BusiConst.SUCCESS, merid+"-"+goodsid+"-"+mobileid);
		return BusiConst.SUCCESS;
	}

	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.MERID+".string", merid);
		logInfo("查询包月订购信息,参数为:%s", map);
		List<Map<String,Object>> rs = (List<Map<String, Object>>) dal.find("psql_HFUSERREST.getuserInf", map);
		if(rs==null||rs.size()==0){
			logInfo("包月订购信息不存在");
			return "86802021";
		}
		logInfo("查询包月订购关系信息成功");
		out.put("hfuserlist", rs);
		return BusiConst.SUCCESS;
	}
	
}
