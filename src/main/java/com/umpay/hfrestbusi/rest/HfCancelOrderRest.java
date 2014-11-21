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


/** ******************  类说明  *********************
 * class       :  HfCancelOrderRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  包月制关系注销订单信息
 * @see        :                        
 * ************************************************/   
public class HfCancelOrderRest extends BaseRest {
	
	/** ********************************************
	 * method name   : doCreateService 
	 * modified      : xuwei ,  2011-11-4
	 * description   : 新增话费订单信息
	 * ********************************************/     
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("新增注销订单信息:%s", urlargs);
		
		// 装载初始化参数
	//	String orderId = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String mobileId= StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String orderParam= StringUtil.trim(urlargs.get(HFBusiDict.GOODSINFO));
	
		Map<String,String> args = new HashMap<String,String>();
		
	    String orderId = CreatOrderId();
	
		args.put(HFBusiDict.ORDERID+".string",orderId);
		args.put(HFBusiDict.ORDERDATE+".string",orderDate);
		args.put(HFBusiDict.MOBILEID+".string",mobileId);
		args.put(HFBusiDict.GOODSINFO+".string",orderParam);
		args.put(HFBusiDict.EXPIRETIME+".timestamp", StringUtil.trim(urlargs.get(HFBusiDict.EXPIRETIME)));
//	    String pid=tableName.substring(tableName.length()-1);
//		args.put("_tableName",pid);
		
		//向数据中插入数据
		int state = 0;
		try{
			state = dal.insert("psql_HFORDERREST.addHfCancelOrder", args);
		}catch(DBPKConflictException e){
			logException(e);
			//主键冲突，库中已经存在数据
			Map<String,String> m = new HashMap<String,String>();
			m.put(HFBusiDict.ORDERID+".string", orderId);
			m.put(HFBusiDict.ORDERDATE+".string", orderDate);
			m.put(HFBusiDict.MOBILEID+".string",mobileId);
			//主键冲突则向用户返回冲突的该数据
			Map<String,Object> order = dal.get("psql_HFORDERREST.findHfCancelOrder", m);
			out.putAll(order);
			logInfo("订单已经存在:%s", order);
			return "86801102";
		}
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		if(state!=1){
			this.logInfo("新增注销订单出现系统未知错误,返回码为:%s", BusiConst.SYS_ERROR);
			return BusiConst.SYS_ERROR;
		}
		Map<String,Object> rs = new HashMap<String,Object>();
		rs.put(HFBusiDict.ORDERID, orderId);
		out.putAll(rs);
		logInfo("新增注销订单信息成功!%s", state);
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : CreatOrderId 
	 * description   : 生成订单号
	 * @return       : Boolean
	 * @param        : @param merId
	 * @param        : @return
	 * modified      : xuwei ,  2012-4-5  下午02:42:33
	 * @throws Exception 
	 * @see          : 
	 * ********************************************/      
	
	private String CreatOrderId() throws Exception {
		String num = "801";//短信子号
		return  num+ SequenceUtil.formatSequence(SequenceUtil.getBatchSeq("CancelOrderId"),8);
		 
	}


	/** ********************************************
	 * method name   : doShowService 
	 * modified      : xuwei ,  2011-11-3
	 * description   : 根据手机号和订单号查询订单信息
	 *                 数据库表名为umpay.t_order_0,1,2,3,4,5,6
	 *                 其中后面一位数据是orderid的最后一位
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("查询话费注销订单信息:%s", urlargs);
		
		// 装载初始化参数
		String orderId= StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		
		Map<String,String> args = new HashMap<String,String>();
		
//		//验证数据库表是否存在
//		Integer lastNum = Integer.parseInt(orderId.substring(orderId.length()-1));
//		if(lastNum<0||lastNum>6){
//			logInfo("数据库表不存在:%s", "noTable");
//			return "86801103";
//		}
		
		args.put(HFBusiDict.MOBILEID+".string",mobileId);
		args.put(HFBusiDict.ORDERID+".string",orderId);
//		args.put("_tableName", lastNum.toString());
		
		//获取话费订单资源
		Map<String,Object> rs = dal.get("psql_HFORDERREST.findHfCancelOrder", args);
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//话费订单信息查询结果为空
		if(rs==null||rs.size()==0){
			logInfo("注销订单信息不存在或者订单已过期：%s %s", orderId,mobileId);
			return "86801100";
		}
		
		logInfo("查询注销订单信息成功：%s", rs);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}
	/**
	 * ******************************************** method name :
	 * doUpdateService modified : xuwei , 2014-03-27 description : 订单更新
	 * ********************************************/
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("订单更新", urlargs);
		//验证数据库表是否存在
		String orderId = urlargs.get(HFBusiDict.ORDERID);
		//装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		
        String isOrderLock=StringUtil.trim(urlargs.get(HFBusiDict.ISSPECIAL));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String rpid = StringUtil.trim(urlargs.get(HFBusiDict.RPID));
	//	String orderState = StringUtil.trim(urlargs.get(HFBusiDict.ORDERSTATE));//更新后状态
		args.put(HFBusiDict.MOBILEID+".string", mobileId);
		args.put(HFBusiDict.ORDERID+".string", orderId);
		args.put(HFBusiDict.PLATDATE+".string", platdate);
		args.put(HFBusiDict.RPID+".string", rpid);
		args.put(HFBusiDict.ORDERSTATE+".int", urlargs.get(HFBusiDict.ORDERSTATE));
		
		
//		// 根据订单日期取得对应的数据库表名  PORDERID
//		args.put("_tablename", lastNum.toString());
		
		
		//执行更新话费订单信息
		int state =0;
		if(!isOrderLock.equals("0")){
			state =dal.update("psql_HFORDERREST.updateHFCancelOrder", args);
		}else{//订单锁定
			state =dal.update("psql_HFORDERREST.lockHFCancelOrder", args);
		}
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		//STATE为零,则输出更新话费订单数为0
		if (state == 0) {
			logInfo("更新订单数为0 : %s %s %s", HFBusiDict.PLATDATE, HFBusiDict.MOBILEID, HFBusiDict.ORDERID);
			return "86801048";//话费订单更新数为0
		}
		logInfo("更新订单成功:%s", state);
		return BusiConst.SUCCESS;
	}
}
