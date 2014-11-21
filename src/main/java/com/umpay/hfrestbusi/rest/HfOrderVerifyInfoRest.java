package com.umpay.hfrestbusi.rest;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.DynSqlUtil;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfOrderVerifyInfoRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  订单验证码校验
 * @see        :                        
 * ************************************************/   
public class HfOrderVerifyInfoRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String randomKey = StringUtil.trim(urlargs.get(HFBusiDict.RANDOMKEY));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String expireTime = StringUtil.trim(urlargs.get(HFBusiDict.EXPIRETIME));
		int expiretime = 5;//过期时长，单位：分钟
		if(!expireTime.equals(""))
		{
			expiretime=Integer.parseInt(expireTime);
		}		
		/*计算过期时间*/
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		long future = now + expiretime*60*1000;
		
		/*获取表标识*/
		String tableName = OrderTableUtil.getTableNumByDate(urlargs.get(HFBusiDict.ORDERDATE));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.RANDOMKEY+".string", randomKey);
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.EXPIRETIME+".timestamp", new Timestamp(future).toString());
		map.put("_tablename", tableName);
		logInfo("新增订单验证码信息，参数为:%s", map);
		try{
			dal.insert("psql_orderVerifyInfo.insert", map);
		}catch(DBPKConflictException e){
			logInfo("订单验证码信息已存在");
			return "86801116";
		}catch(Exception e){
			logInfo("新增订单验证码信息异常");
			e.printStackTrace();
			return "86801117";
		}
		return BusiConst.SUCCESS;
	}

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		/*获取表标识*/
		String tableName = OrderTableUtil.getTableNumByDate(urlargs.get(HFBusiDict.ORDERDATE));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.MERID+".string", merid);
		map.put("_tablename", tableName);
		logInfo("查询订单验证码信息，参数:%s",map);
		Map<String,Object> result = dal.get("psql_orderVerifyInfo.getInfo", map);
		if(result==null||result.size()==0){
			logInfo("订单验证码信息不存在");
			return "86801118";
		}
		out.putAll(result);
		return BusiConst.SUCCESS;
	}

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String randomKey = StringUtil.trim(urlargs.get(HFBusiDict.RANDOMKEY));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String wrongNum = StringUtil.trim(urlargs.get(HFBusiDict.WRONGNUM));
		String getTimes = StringUtil.trim(urlargs.get(HFBusiDict.GETTIMES));
		long future = -1;
		String expritime = "";
		/*获取表标识*/
		String tableName = OrderTableUtil.getTableNumByDate(urlargs.get(HFBusiDict.ORDERDATE));
		
		Map<String,Object> setMap = new HashMap<String,Object>();
		if(!StringUtil.isNullOrNovalue(randomKey)){
			setMap.put(HFBusiDict.RANDOMKEY,randomKey);
			Calendar calendar = Calendar.getInstance();
			long now = calendar.getTimeInMillis();
			future = now + 5*60*1000;//新的验证码过期时间需要重新计算
			expritime = ""+new Timestamp(future).toString();
			expritime = expritime.substring(0, expritime.indexOf("."));//getUpdateSql方法组装参数的时候，对于带.的字符串有特殊处理！
		} 
		if(!StringUtil.isNullOrNovalue(mobileid)) setMap.put(HFBusiDict.MOBILEID,mobileid);
		if(!StringUtil.isNullOrNovalue(wrongNum)) setMap.put(HFBusiDict.WRONGNUM,Integer.parseInt(wrongNum));
		if(!StringUtil.isNullOrNovalue(getTimes)) setMap.put(HFBusiDict.GETTIMES,Integer.parseInt(getTimes));
		if(future!=-1){
			setMap.put(HFBusiDict.EXPIRETIME,expritime);
		}
		if(setMap.size()==0){
			logInfo("无信息需要更新");
			return BusiConst.SUCCESS;
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID, orderid);
		map.put(HFBusiDict.ORDERDATE, orderdate);
		map.put(HFBusiDict.MERID, merid);
		map.put("setPara",DynSqlUtil.getUpdateSql(setMap));
		map.put("_tablename", tableName);
		logInfo("更新订单验证码信息，参数为:%s", map);
		int result = dal.update("orderVerifyInfo.update", map);
		if(result==0){
			logInfo("订单验证码信息不存在");
			return "86801118";
		}
		logInfo("更新订单验证码信息成功");
		return BusiConst.SUCCESS;
	}
}
