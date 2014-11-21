package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/** ******************  类说明  *********************
 * class       :  ChannelOrder
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :   渠道订单
 * @see        :                        
 * ************************************************/   
public class HfChannelOrder extends BaseRest {
	/** ********************************************
	 * method name   : doCreateService 
	 * modified      : panxingwu ,  2013-3-7
	 * description   : 由于渠道订单和商户订单的日期可能存在跨天的情况，故统一以商户订单日期为准
	 * 				        确定订单表，这么做的目的是为了支付完成以后能够找到对应的渠道订单表修改渠道订单信息。 
	 * ********************************************/     
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("新增渠道订单,参数为:%s",urlargs);
		String channelOrderId = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELORDERID));
		String channelId = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		String channelDate = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELDATE));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String version =  StringUtil.trim(urlargs.get(HFBusiDict.VERSION));
		String channelpriv=  StringUtil.trim(urlargs.get(HFBusiDict.CHANNELPRIV));
		String channelexpand=  StringUtil.trim(urlargs.get(HFBusiDict.CHANNELEXPAND));
		String porderId = StringUtil.trim(urlargs.get(HFBusiDict.PORDERID));
		String retCode = StringUtil.trim(urlargs.get(HFBusiDict.RETCODE));//商户下单返回码
		//根据订单日期创建相应的数据库表名
		String tableName = OrderTableUtil.getTableNameByDate(orderdate);
		String tableNum = tableName.substring(tableName.length()-1);
		int num = Integer.parseInt(tableNum);
		if(num<0||num>6){
			logInfo("渠道订单表t_hf_chnl_order_"+num+"不存在");
			return "86801087";
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.CHANNELORDERID+".string", channelOrderId);
		map.put(HFBusiDict.CHANNELID+".string", channelId);
		map.put(HFBusiDict.CHANNELDATE+".string",channelDate);
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);
		map.put(HFBusiDict.AMOUNT+".long", urlargs.get(HFBusiDict.AMOUNT));
		map.put(HFBusiDict.PORDERID+".string", porderId);
		map.put(HFBusiDict.VERSION+".string", version);
		map.put(HFBusiDict.CHANNELPRIV+".string", channelpriv);
		map.put(HFBusiDict.EXPAND+".string", channelexpand);
		map.put(HFBusiDict.RETCODE+".string", retCode);
		map.put("_tablename",tableName.substring(tableName.length()-1));
		try{
			dal.insert("psql_channelOrder.addOrder", map);
		}catch(DBPKConflictException e){
			logInfo("渠道订单已经存在");
			return "86801077";
		}
		logInfo("新增渠道订单成功");
		out.put(HFBusiDict.ORDERID, orderid);
		out.put(HFBusiDict.ORDERDATE, orderdate);
		out.put(HFBusiDict.PORDERID,porderId);
		return BusiConst.SUCCESS;
	}

	
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("查询渠道订单信息,参数为:%s",urlargs);
		String channelid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		String channelorderid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELORDERID));
		String channeldate = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELDATE));
		String tablename = OrderTableUtil.getTableNameByDate(channeldate);
		String tableNum = tablename.substring(tablename.length()-1);
		int num = Integer.parseInt(tableNum);
		if(num<0||num>6){
			logInfo("渠道订单表t_hf_chnl_order_"+num+"不存在");
			return "86801087";
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("channelid"+".string", channelid);
		map.put("channelorderid"+".string", channelorderid);
		map.put("channeldate"+".string",channeldate);
		map.put("_tablename",tablename.substring(tablename.length()-1));
		
		Map<String,Object> rsMap = dal.get("psql_channelOrder.getOrder", map);
		if(rsMap==null||rsMap.size()==0){
			logInfo("渠道订单不存在");
			return "86801078";
		}
		out.putAll(rsMap);
		return BusiConst.SUCCESS;
	}
	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2013-3-7
	 * description   : 更新平台订单和渠道订单的状态
	 * ********************************************/ 
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String orderstate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERSTATE));
		String tablename = OrderTableUtil.getTableNameByDate(orderdate);
		String tableNum = tablename.substring(tablename.length()-1);
		int num = Integer.parseInt(tableNum);
		if(num<0||num>6){
			logInfo("订单表t_order_"+num+"不存在");
			return "86801103";
		}
		
		logInfo("更新平台订单...");
		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.ORDERID+".string", orderid);
		args.put(HFBusiDict.ORDERDATE+".string", orderdate);
		args.put(HFBusiDict.MERID+".string", merid);
		args.put(HFBusiDict.ORDERSTATE+".int", urlargs.get(HFBusiDict.ORDERSTATE));
		args.put(HFBusiDict.RPID+".string", urlargs.get(HFBusiDict.RPID));
		args.put(HFBusiDict.PLATDATE+".string", urlargs.get(HFBusiDict.PLATDATE));
		args.put(HFBusiDict.RESERVED+".string", urlargs.get(HFBusiDict.RESERVED));
		// 根据订单日期取得对应的数据库表名
		String tableName = OrderTableUtil.getTableNameByDate(urlargs.get(HFBusiDict.ORDERDATE));
		args.put("_tableName", tableName.substring(tableName.length() - 1));
		logInfo("更新话费订单信息 输入参数args:%s", args);
		//执行更新话费订单信息
		int state = dal.update("psql_HFORDERMERREST.UpdateHFOrder", args);
		// 结果检查
		this.logInfo("开始校验结果...", "");
		//STATE为零,则输出所需商户不存在
		if (state == 0) {
			logInfo("话费订单不存在 : %s %s %s", orderid, orderdate, merid);
			return "86801101";
		}
		logInfo("更新平台订单成功:%s", state);
		
		
		logInfo("查询渠道订单信息");
		Map<String,String> getMap = new HashMap<String,String>();
		getMap.put(HFBusiDict.ORDERDATE+".string", orderdate);
		getMap.put(HFBusiDict.ORDERID+".string", orderid);
		getMap.put(HFBusiDict.MERID+".string", merid);
		getMap.put("_tablename",tableNum);
		Map<String,Object> rs = dal.get("psql_channelOrder.getOrderInf", getMap);
		if(rs==null||rs.size()==0){
			logInfo("渠道订单不存在【平台订单已经更新成功，忽略渠道订单异常情况，直接返回成功！】");
			return BusiConst.SUCCESS;
		}
		logInfo("更新渠道订单状态");
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.ORDERSTATE+".int", orderstate);
		map.put("_tablename", tablename.substring(tablename.length()-1));
		dal.update("psql_channelOrder.updateOrder", map);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}
}
