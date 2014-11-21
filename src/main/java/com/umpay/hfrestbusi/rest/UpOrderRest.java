package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.DynSqlUtil;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;


/** ******************  类说明  *********************
 * class       :  UpOrderRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  话费订单信息
 * @see        :                        
 * ************************************************/   
public class UpOrderRest extends BaseRest {
	
	/** ********************************************
	 * method name   : doCreateService 
	 * modified      : xuwei ,  2014-09-23
	 * description   : 新增订单信息
	 * ********************************************/     
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("新增订单信息:%s", urlargs);
	
		// 装载初始化参数
		String orderId = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String sdkType=StringUtil.trim(urlargs.get(HFBusiDict.SDKTYPE));
		if("".equals(sdkType)){
			sdkType="-99";
		}
		String timeString=TimeUtil.datetime14();
        String platdate=timeString.substring(0,8);
        String tableName = OrderTableUtil.getTableNameByDate(orderDate);
		long servicesequence = SequenceUtil.getInstance().getBatchSeq("UPORD");
		
        String platordId=timeString+merId+SequenceUtil.formatSequence(servicesequence, 13)+tableName.substring(tableName.length()-1);
        logInfo("生成的序列数为:%s, platordid:%s",servicesequence,platordId);	

		Map<String,String> args = new HashMap<String,String>();
		//根据订单日期创建相应的数据库表名
		
		args.put(HFBusiDict.PLATORDID+".string",platordId);
		args.put(HFBusiDict.ORPID+".string",rpid);
		args.put(HFBusiDict.ORDERID+".string",orderId);
		args.put(HFBusiDict.ORDERDATE+".string",orderDate);
		args.put(HFBusiDict.MERID+".string",merId);
		args.put(HFBusiDict.GOODSID+".string", StringUtil.trim(urlargs.get(HFBusiDict.GOODSID)));
		args.put(HFBusiDict.MOBILEID+".string", StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID)));
		args.put(HFBusiDict.EXPIRETIME+".timestamp", StringUtil.trim(urlargs.get(HFBusiDict.EXPIRETIME)));
		args.put(HFBusiDict.BANKID+".string", StringUtil.trim(urlargs.get(HFBusiDict.BANKID)));
		args.put(HFBusiDict.AMOUNT+".long", StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT)));
		args.put(HFBusiDict.MERCUSTID+".string", StringUtil.trim(urlargs.get(HFBusiDict.MERCUSTID)));
		args.put(HFBusiDict.MERPRIV+".string", StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV)));
		args.put(HFBusiDict.EXPAND+".string", StringUtil.trim(urlargs.get(HFBusiDict.EXPAND)));
		args.put(HFBusiDict.AMTTYPE+".string", StringUtil.trim(urlargs.get(HFBusiDict.AMTTYPE)));
		args.put(HFBusiDict.VERSION+".string", StringUtil.trim(urlargs.get(HFBusiDict.VERSION)));
		args.put(HFBusiDict.IMEI+".string", StringUtil.trim(urlargs.get(HFBusiDict.IMEI)));//手机串号
		args.put(HFBusiDict.IMSI+".string", StringUtil.trim(urlargs.get(HFBusiDict.IMSI)));//
		args.put(HFBusiDict.ICCID+".string", StringUtil.trim(urlargs.get(HFBusiDict.ICCID)));//
		args.put(HFBusiDict.APPID+".string", StringUtil.trim(urlargs.get(HFBusiDict.APPID)));//应用ID
		args.put(HFBusiDict.CHANNELID+".string", StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID)));//渠道服务商编号
		args.put(HFBusiDict.RESERVED+".string", StringUtil.trim(urlargs.get(HFBusiDict.RESERVED)));
		args.put(HFBusiDict.BUSINESSTYPE+".string", StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE)));
		args.put(HFBusiDict.SDKTYPE+".int", sdkType);//sdk类型
		args.put(HFBusiDict.SDKVERSION+".string", StringUtil.trim(urlargs.get(HFBusiDict.SDKVERSION)));//sdk版本
		args.put(HFBusiDict.PHONEOS+".string", StringUtil.trim(urlargs.get(HFBusiDict.PHONEOS)));//手机操作系统
		args.put(HFBusiDict.NETTYPE+".string", StringUtil.trim(urlargs.get(HFBusiDict.NETTYPE)));//运营商编号
		args.put(HFBusiDict.RETURL+".string", StringUtil.trim(urlargs.get(HFBusiDict.RETURL)));//
		args.put(HFBusiDict.APPDESC+".string", StringUtil.trim(urlargs.get(HFBusiDict.APPDESC)));//

	
		logInfo("新增话费订单map!%s", args);

		String pid = platordId.substring(platordId.length()-1);
		args.put("_tableName",pid);
		
		//向数据中插入数据
		int state = 0;
		try{
			state = dal.insert("psql_UPORDERREST.addOrder", args);
		}catch(DBPKConflictException e){
			logException(e);
			//主键冲突，库中已经存在数据
//			Map<String,String> m = new HashMap<String,String>();
//			m.put(HFBusiDict.PLATORDID+".string", platordId);
//			m.put("_tableName", pid);
//			//主键冲突则向用户返回冲突的该数据
//			Map<String,Object> order = dal.get("psql_UPORDERREST.findOrder", m);
//			out.putAll(order);
			logInfo("话费订单已经存在:orderid=%s",orderId);
			return "86801102";
		}
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		if(state!=1){
			this.logInfo("新增订单出现系统未知错误,返回码为:%s", BusiConst.SYS_ERROR);
			return BusiConst.SYS_ERROR;
		}
		Map<String,Object> rs = new HashMap<String,Object>();
		rs.put(HFBusiDict.PLATORDID, platordId);
		out.putAll(rs);
		logInfo("新增话费订单信息成功!%s", state);
		return BusiConst.SUCCESS;
	}

	


	/** ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2011-11-3
	 * description   : 根据手机号和平台流水号查询话费订单信息
	 *                 数据库表名为umpay.t_order_0,1,2,3,4,5,6
	 *                 其中后面一位数据是porderid的最后一位
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("查询话费订单信息:%s", urlargs);
		
		// 装载初始化参数
		String porderId = StringUtil.trim(urlargs.get(HFBusiDict.PLATORDID));
		
		Map<String,String> args = new HashMap<String,String>();
		
		//验证数据库表是否存在
		Integer lastNum = Integer.parseInt(porderId.substring(porderId.length()-1));
		if(lastNum<0||lastNum>6){
			logInfo("数据库表不存在:%s", "noTable");
			return "86801103";
		}

		args.put(HFBusiDict.PLATORDID + ".string", porderId);
		args.put("_tableName", lastNum.toString());
		
		//获取话费订单资源
		Map<String,Object> rs = dal.get("psql_UPORDERREST.getOrder", args);
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//话费订单信息查询结果为空
		if(rs==null||rs.size()==0){
			logInfo("话费订单信息不存在或者订单已过期：%s ", porderId);
			return "86801100";
		}
		
		logInfo("查询话费订单信息成功：%s", rs);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}
	/**
	 * ******************************************** method name :
	 * doUpdateService modified : 
	 *  description : 订单更新
	 * ********************************************/
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("订单更新", urlargs);
		//验证数据库表是否存在
		String porderId = urlargs.get(HFBusiDict.PLATORDID);
		Integer lastNum = Integer.parseInt(porderId.substring(porderId.length()-1));
		if(lastNum<0||lastNum>6){
			logInfo("数据库表不存在:%s", "noTable");
			return "86801103";
		}
		
		//装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		

		//组装SQL语句的SET语句块
		Map<String,Object> setMap = new HashMap<String,Object>();
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.MOBILEID))){
			setMap.put(HFBusiDict.MOBILEID, urlargs.get(HFBusiDict.MOBILEID));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.ORDERSTATE))){
			setMap.put(HFBusiDict.ORDERSTATE, Integer.parseInt(urlargs.get(HFBusiDict.ORDERSTATE)));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.UPTRANSDATE))){
			setMap.put(HFBusiDict.UPTRANSDATE, urlargs.get(HFBusiDict.UPTRANSDATE));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.TRANSID))){
			setMap.put(HFBusiDict.TRANSID, urlargs.get(HFBusiDict.TRANSID));
		}	
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.BANKID))){
			setMap.put(HFBusiDict.BANKID, urlargs.get(HFBusiDict.BANKID));
		}	
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.MERCUSTID))){
			setMap.put(HFBusiDict.MERCUSTID, urlargs.get(HFBusiDict.MERCUSTID));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.SHIPSTATE))){
			setMap.put(HFBusiDict.SHIPSTATE, Integer.parseInt(urlargs.get(HFBusiDict.SHIPSTATE)));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.RPID))){
			setMap.put(HFBusiDict.RPID, urlargs.get(HFBusiDict.RPID));
		}
		
		args.put(HFBusiDict.PLATORDID, urlargs.get(HFBusiDict.PLATORDID));
		// 根据订单日期取得对应的数据库表名  PORDERID
		args.put("setPara",DynSqlUtil.getUpdateSql(setMap));
		args.put("_tablename", lastNum.toString());
		
		//执行更新话费订单信息
		int state = dal.update("UPORDERREST.updateOrder", args);
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		//STATE为零,则输出更新话费订单数为0
		if (state == 0) {
			logInfo("更新话费订单数为0 : %s", urlargs.get(HFBusiDict.PLATORDID));
			return "86801048";//话费订单更新数为0
		}
		logInfo("更新话费订单成功:%s", state);
		return BusiConst.SUCCESS;
	}
}
