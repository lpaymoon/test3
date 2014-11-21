package com.umpay.hfrestbusi.rest.complex;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.RandomUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfSmsRandomKeyRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  获取动态验证码
 * @see        :                        
 * ************************************************/   
public class HfSmsRandomKeyRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		Map<String,String> getmap = new HashMap<String,String>();
		getmap.put(HFBusiDict.MOBILEID,mobileid);
		Map<String,Object> rsMap = LocalUtil.doGetService("/hfrestbusi/hfVerifyInfoRest/"+rpid+"/"+mobileid+".xml", getmap);
		String retCode = (String) rsMap.get(HFBusiDict.RETCODE);
		String randomKey = RandomUtil.genRandomNumString(6);//新的随机码
		
		if(!"0000".equals((String)rsMap.get(HFBusiDict.RETCODE))){
			if("86801108".equals(retCode)){//数据不存在，执行新增
				Map<String,String> updateMap = new HashMap<String,String>();
				Calendar calendar = Calendar.getInstance();
				long now = calendar.getTimeInMillis();
				long future = now + 30*60*1000;
				Map<String,String> addmap = new HashMap<String,String>();
				addmap.put(HFBusiDict.MOBILEID, mobileid);
				addmap.put(HFBusiDict.VERIFYTIMES, "1");
				addmap.put(HFBusiDict.RANDOMKEY, randomKey);
				addmap.put(HFBusiDict.EXPIRETIME, new Timestamp(future).toString());
				logInfo("新增动态验证信息");
				Map<String,Object> addRsMap = LocalUtil.doPostService("/hfrestbusi/hfVerifyInfoRest/"+rpid+".xml",addmap);
				String rtCode = (String) addRsMap.get(HFBusiDict.RETCODE);
				if(!"0000".equals(rtCode)) {
					return rtCode;
				}
				out.put(HFBusiDict.RANDOMKEY, randomKey);
				return (String)addRsMap.get(HFBusiDict.RETCODE);
			}
			logInfo("获取动态验证码失败");
			return (String)rsMap.get(HFBusiDict.RETCODE);
		}
		int verifyTimes = Integer.parseInt(String.valueOf(rsMap.get(HFBusiDict.VERIFYTIMES))); 

		//更新动态验证码
		Map<String,String> updateMap = new HashMap<String,String>();
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		long future = now + 30*60*1000;
		updateMap.put(HFBusiDict.MOBILEID, mobileid);
		updateMap.put(HFBusiDict.RANDOMKEY, randomKey);
		updateMap.put(HFBusiDict.VERIFYTIMES, String.valueOf(verifyTimes+1));
		updateMap.put(HFBusiDict.EXPIRETIME, new Timestamp(future).toString());
		Map<String,Object> updateRsMap = LocalUtil.doPostService("/hfrestbusi/hfVerifyInfoRest/"+rpid+"/"+mobileid+".xml", updateMap);
		out.put(HFBusiDict.RANDOMKEY, randomKey);
		return (String)updateRsMap.get(HFBusiDict.RETCODE);
	}

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String key = StringUtil.trim(urlargs.get(HFBusiDict.RANDOMKEY));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID,mobileid);
		Map<String,Object> rsMap = LocalUtil.doGetService("/hfrestbusi/hfVerifyInfoRest/"+rpid+"/"+mobileid+".xml", map);
		String retCode = (String) rsMap.get(HFBusiDict.RETCODE);
		if(!"0000".equals(rsMap.get(HFBusiDict.RETCODE))) return retCode;
		String randomKey = String.valueOf(rsMap.get(HFBusiDict.RANDOMKEY));
		String expiretime = String.valueOf(rsMap.get(HFBusiDict.EXPIRETIME));
		//验证动态码是否过期
		 logInfo("查询到过期时间为:%s",expiretime);
		 DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     Date date = formatDate.parse(expiretime);
	     Long time = date.getTime();
	     Long currtime = System.currentTimeMillis();
	     logInfo("过期时间为:"+time+",系统当前时间为:"+currtime);
	     if(currtime>time) return "86801109";//动态码已过期
	     if(!key.equals(randomKey)) return "86801110";//动态验证码校验未通过
	     return BusiConst.SUCCESS;
	}

	
}
