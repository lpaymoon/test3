package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/**
 * ****************** 类说明 ********************* class : HfOrderMerRest
 * 
 * @author : panxingwu
 * @version : 1.0 description : 话费订单信息商户
 * @see :
 * ************************************************/
public class UpOrderMerRest extends BaseRest {

	/**
	 * ******************************************** method name : doShowService
	 * modified : panxingwu , 2011-11-4 description : 根据主键查询话费订单商户信息
	 * ********************************************/
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("查询订单信息商户%s", urlargs);

		// 装载初始化参数
		String orderId = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));

		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.ORDERID+".string", orderId);
		args.put(HFBusiDict.ORDERDATE+".string", orderDate);
		args.put(HFBusiDict.MERID+".string", merId);
		// 根据订单时间获取数据库名称
		String tableName = OrderTableUtil.getTableNameByDate(urlargs
				.get(HFBusiDict.ORDERDATE));
		Integer lastNum = Integer.parseInt(tableName.substring(tableName
				.length() - 1));
		if (lastNum < 0 || lastNum > 6) {// 数据库表不存在
			logInfo("数据库表不存在:%s", "noTable");
			return "86801103";
		}
		args.put("_tableName", lastNum.toString());
		// 获取话费订单商户资源
		Map<String, Object> rs = dal.get("psql_UPORDERMERREST.getorderMer", args);

		// 结果检查
		this.logInfo("开始校验结果......", "");
		//查询商户订单资源不存在
		if (rs == null || rs.size() == 0) {
			logInfo("商户订单信息不存在:%s-%s-%s", orderId, orderDate, merId);
			return "86801101";
		}
		logInfo("商户查询订单信息成功:%s", rs);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}

	/**
	 * ********************************************
	 *  method name :doUpdateService 
	 *  modified : xuwei , 
	 *  2014-11-4 
	 *  description : 更新话费订单发货状态
	 * ********************************************/
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("更新订单信息%s", urlargs);
		
		// 装载初始化参数
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String rpid = StringUtil.trim(urlargs.get(HFBusiDict.RPID));
		String orderDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String tableName = OrderTableUtil.getTableNameByDate(orderDate);
		Integer lastNum = Integer.parseInt(tableName.substring(tableName.length()-1));
		
		if(lastNum<0||lastNum>6){
			logInfo("数据库表不存在:%s", "noTable");
			return "86801103";
		}
		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.MERID+".string", merId);
		args.put(HFBusiDict.ORDERDATE+".string", orderDate);
		args.put(HFBusiDict.ORDERID+".string", orderid);
		args.put(HFBusiDict.SHIPSTATE+".int", urlargs.get(HFBusiDict.SHIPSTATE));
		// 根据订单日期取得对应的数据库表名
		args.put("_tableName", lastNum.toString());
		logInfo("更新话费订单信息 输入参数args:%s", args);
		//执行更新话费订单信息
		int state = dal.update("psql_UPORDERMERREST.UpdateOrder", args);
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		//STATE为零,则输出所需商户不存在
		if (state == 0) {
			logInfo("话费订单信息不存在 : %s,%s", orderid,rpid);
			return "86801101";
		}
		logInfo("更新话费订单成功:%s", state);
		return BusiConst.SUCCESS;
	}
}
