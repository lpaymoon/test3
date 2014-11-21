package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/**
 * @author panxingwu
 * description:订单通用类，用以后续新增需求
 */
public class HfOrderCommonRest extends BaseRest {

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String verifycode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.VERIFYCODE+".string", verifycode);
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.MERID+".string", merid);
		// 根据订单日期取得对应的数据库表名
		String tableName = OrderTableUtil.getTableNameByDate(urlargs
				.get(HFBusiDict.ORDERDATE));
		map.put("_tableName", tableName.substring(tableName.length() - 1));
		logInfo("更新订单表verifycode,参数为：%s", map);
		int num = dal.update("psql_HfOrderCommonRest.update", map);
		if(num!=1){
			logInfo("没有订单信息，更新失败");
			return "86801101";//订单不存在
		}
		logInfo("更新订单表成功【更新verifycode】");
		return BusiConst.SUCCESS;
	}

}
