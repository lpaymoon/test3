package com.umpay.hfrestbusi.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfUserInfoRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  用户信息（目前用于保存无线用户查询交易记录的时候登陆验证）
 * @see        :                        
 * ************************************************/   
public class HfVerifyInfoRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put("_tablename", OrderTableUtil.getCurTableNum());
		Map<String, Object> resultMap = dal.get("dbOn","psql_HFUSERINFO.getUserInfo", map);
		if(resultMap==null||resultMap.size()==0){
			return "86801108";
		}
		logInfo("查询动态验证码信息成功");
		out.putAll(resultMap);
		return BusiConst.SUCCESS;
	}

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("更新动态验证码信息");
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String randomkey = StringUtil.trim(urlargs.get(HFBusiDict.RANDOMKEY));
		String verifytimes = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYTIMES));
		String expiretime = StringUtil.trim(urlargs.get(HFBusiDict.EXPIRETIME));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.RANDOMKEY+".string",randomkey);
		map.put(HFBusiDict.VERIFYTIMES+".int",verifytimes);
		map.put(HFBusiDict.EXPIRETIME+".timestamp", expiretime);
		map.put("_tablename", OrderTableUtil.getCurTableNum());
		int state=dal.update("dbOn","psql_HfUserInfo.update", map);
		if(state==0){
			logInfo("动态验证码不存在");
			return "86801108";
		}
		return BusiConst.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("新增动态验证码信息");
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String verifytimes = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYTIMES));
		String randomKey = StringUtil.trim(urlargs.get(HFBusiDict.RANDOMKEY));
		
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		long future = now + 30*60*1000;
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.VERIFYTIMES+".int", verifytimes==""?"0":verifytimes);
		map.put(HFBusiDict.RANDOMKEY+".string", randomKey);
		map.put(HFBusiDict.EXPIRETIME+".timestamp",new Timestamp(future).toString());//动态码30分钟内有效
		map.put("_tablename", OrderTableUtil.getCurTableNum());
		try{
			dal.insert("dbOn","psql_HfUserInfo.insert", map);
		}catch(DBPKConflictException e){
			return "86801112";//已经存在
		}
		return BusiConst.SUCCESS;
	}
}
