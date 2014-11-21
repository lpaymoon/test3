package com.umpay.hfrestbusi.rest.complex;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * class       :  HfWxOrderVerifyRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  订单验证码信息复合资源
 * @see        :                        
 * ************************************************/   
public class HfOrderVerifyRest extends BaseRest {

	private  static final int MAX_GET_TIMES=2;//获取验证码的最大次数（实际获取数是此数加1，因为数据库默认为0）
	private  static final int MAX_VERIFY_TIMES=2;//验证码输入错误的最大次数（实际获取数是此数加1，因为数据库默认为0）
	
	/** ********************************************
	 * method name   : doCreateService 
	 * modified      : panxingwu ,  2013-9-26
	 * 功能：生成校验码
	 * 规则：每个订单最多只能获取三次验证码。
	 * 首先查询验证码，如果查询结果为空，则生成一个验证码并存入数据库
	 * 如果查询结果不为空，则判断获取验证码次数（getTimes）是否超过规定
	 * 上限，如果超过上限，则返回错误提示。如果没有超过上限，则重新生成验证码
	 * 并覆盖库中原来的验证码，同时获取次数加1
	 * ********************************************/     
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("获取订单验证码，参数：%s", urlargs);
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String expireTime = StringUtil.trim(urlargs.get(HFBusiDict.EXPIRETIME));
		String randomKey = "";
		String expiretime = "5";//默认值为5分钟
		if(!expireTime.equals("")){
			expiretime=expireTime;
		}
		
		//1.查询验证码信息
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID, orderid);
		map.put(HFBusiDict.ORDERDATE, orderdate);
		map.put(HFBusiDict.MERID, merid);
		Map<String,Object> queryResult = LocalUtil.doGetService("/hfrestbusi/HfOrderVerifyInfo/"+rpid+"/"+orderid+"_"+orderdate+"_"+merid+".xml", map);
		String retCode = (String) queryResult.get(HFBusiDict.RETCODE);
		//2.如果验证码不存在，则生成新的验证码
		if("86801118".equals(retCode)){
			logInfo("查询结果为空，新增一条验证码动态信息");
			randomKey = RandomUtil.genRandomNumString(6);//生成一个6位数随机码
			Map<String,String> insertMap = new HashMap<String,String>();
			insertMap.put(HFBusiDict.ORDERID,orderid);
			insertMap.put(HFBusiDict.ORDERDATE, orderdate);
			insertMap.put(HFBusiDict.MERID,merid);
			insertMap.put(HFBusiDict.RANDOMKEY, randomKey);
			insertMap.put(HFBusiDict.MOBILEID, mobileid);
			insertMap.put(HFBusiDict.EXPIRETIME, expiretime);
			LocalUtil.doPostService("/hfrestbusi/HfOrderVerifyInfo/"+rpid+".xml", insertMap);
		}else{
			//3.如果验证码存在，则首先判断是否超过获取验证码的次数限制(需要重新下单来获取验证码)
			int getTimes = (Integer) queryResult.get("gettimes");
			if(MAX_GET_TIMES==getTimes){
				logInfo("超过获取验证码最大限制次数");
				return "86801119";
			}
			//4.没有超过次数限制，则重新生成一个验证码覆盖原来库里面的验证码，并将获取次数加1
			int times = getTimes+1;
			randomKey = RandomUtil.genRandomNumString(6);//生成一个6位数随机码
			Map<String,String> updateMap = new HashMap<String,String>();
			updateMap.put(HFBusiDict.ORDERID, orderid);
			updateMap.put(HFBusiDict.ORDERDATE, orderdate);
			updateMap.put(HFBusiDict.MERID, merid);
			updateMap.put(HFBusiDict.RANDOMKEY, randomKey);
			updateMap.put(HFBusiDict.GETTIMES, String.valueOf(times));
			updateMap.put(HFBusiDict.MOBILEID, mobileid);
			LocalUtil.doPostService("/hfrestbusi/HfOrderVerifyInfo/"+rpid+"/"+orderid+"_"+orderdate+"_"+merid+".xml", updateMap);
		}
		logInfo("获取验证码成功,randomkey=%s",randomKey);
		out.put(HFBusiDict.RANDOMKEY, randomKey);
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : panxingwu ,  2013-9-26
	 * 功能：验证码校验
	 * 规则：查询验证码信息，看验证码是否存在，校验错误次数是否大于规定的上限。
	 * 如果以上两个条件都通过，则对比传入的动态码与数据库里面的动态码是否一致，如果不一致
	 * 则返回校验错误提示，并将数据库里面的校验错误次数加1
	 * ********************************************/     
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("订单验证码校验，参数：%s", urlargs);
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String randomCode = StringUtil.trim(urlargs.get(HFBusiDict.RANDOMKEY));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID, orderid);
		map.put(HFBusiDict.ORDERDATE, orderdate);
		map.put(HFBusiDict.MERID, merid);
		
		//1.查询验证码信息
		Map<String,Object> queryResult = LocalUtil.doGetService("/hfrestbusi/HfOrderVerifyInfo/"+rpid+"/"+orderid+"_"+orderdate+"_"+merid+".xml", map);
		logInfo("查询的验证码信息:%s",queryResult);
		String retCode = (String) queryResult.get(HFBusiDict.RETCODE);
		if("86801118".equals(retCode)){
			logInfo("无验证码");
			return "86801118";
		}
		int wrongNum = (Integer) queryResult.get("wrongnum");
		String expiretime = String.valueOf(queryResult.get(HFBusiDict.EXPIRETIME));
		String randomKey = String.valueOf(queryResult.get(HFBusiDict.RANDOMKEY));
		String mobile = String.valueOf(queryResult.get(HFBusiDict.MOBILEID));
		if(!mobileId.equals(mobile)){
			logInfo("下单和支付手机号不一致");
			return "86801122";
		}
		//2.验证动态码是否过期
		logInfo("查询到过期时间为:%s",expiretime);
		DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = formatDate.parse(expiretime);
	    Long time = date.getTime();
	    Long currtime = System.currentTimeMillis();
	    logInfo("过期时间为:"+time+",系统当前时间为:"+currtime);
	    if(currtime>time) return "86801109";//动态码已过期
	    
	    //3.验证错误次数是否超过限制
	    if(wrongNum==MAX_VERIFY_TIMES) return "86801120";//验证错误次数超限
	    
	    //4.验证动态码，如果动态码错误则更新错误次数(verifyTimes)
	    if(!randomCode.equals(randomKey)){
	    	logInfo("验证码错误，传入校验码:%s,库中的校验码:%s",randomCode,randomKey);
	    	int times = wrongNum+1;
		    Map<String,String> updateMap = new HashMap<String,String>();
		    updateMap.put(HFBusiDict.ORDERID, orderid);
			updateMap.put(HFBusiDict.ORDERDATE, orderdate);
			updateMap.put(HFBusiDict.MERID, merid);
			updateMap.put(HFBusiDict.WRONGNUM, String.valueOf(times));
			LocalUtil.doPostService("/hfrestbusi/HfOrderVerifyInfo/"+rpid+"/"+orderid+"_"+orderdate+"_"+merid+".xml", updateMap);
			return "86801121";
	    }
		return BusiConst.SUCCESS;
	}

}
