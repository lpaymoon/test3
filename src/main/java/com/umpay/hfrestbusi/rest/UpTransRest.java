package com.umpay.hfrestbusi.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.DynSqlUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;


/** ******************  类说明  *********************
 * class       :  UpTransRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  综合支付交易
 * @see        :                        
 * ************************************************/   
public class UpTransRest extends BaseRest {

	
	/** ********************************************
	 * method name   : doCreateService 小额交易插入子资源
	 * modified      : LiuJiLong ,  2012-3-1
	 * args          : platdate	          交易日期	Y   小额交易INSERT参数之一
     *                 rpid	              平台流水	Y   必输参数
	 *				   transeq	 请求流水(平台生成)	Y   小额交易INSERT参数之一
	 *				   mobileid	         手机号码	Y   小额交易INSERT参数之一
	 *				   mercheckdate	与商户结帐日期	Y   小额交易INSERT参数之一，如为空则会被赋于platdate
	 *				   plattime	         交易时间	Y   小额交易INSERT参数之一
	 *				   isnew	     新增还是续费	Y   小额交易INSERT参数之一
	 *				   transtate	     交易状态	Y   小额交易INSERT参数之一
	 *				   amt	             交易金额	Y   小额交易INSERT参数之一
	 *				   merid	           商户号	Y   小额交易INSERT参数之一
	 *			       goodsid	           商品号	Y   小额交易INSERT参数之一
	 *				   bankid	         银行代码	Y   小额交易INSERT参数之一
	 *				   areacode			 地区编码	Y   小额交易INSERT参数之一
	 *				   banktrace	  银行返回流水	Y   小额交易INSERT参数之一
	 *				   orderid	           订单号	N   小额交易INSERT参数之一
	 *				   orderdate	     订单日期	N   小额交易INSERT参数之一
	 *				   reserved	         保留字段	N   小额交易INSERT参数之一
	 *				   cardtype	       手机卡类型	N   小额交易INSERT参数之一,老表中为brantchid字段
	 *				   mercustid	 商户业务号码	N   小额交易INSERT参数之一
	 *				   bankcheckdate与银行结帐日期	N   小额交易INSERT参数之一，如为空则会被赋于platdate			       
	 * description   : funcode为“P100”，即为正常交易；batchid为当前日期；
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doCreateService(java.util.Map)
	 * ********************************************/     
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		
	//	TRANSID FUNCODE GOODSID PLATORDID BANK_TRACE BANK_CHECK MERCUSTID EXPAND TRANSDATE TRANSTIME
	//	MERID AMOUNT MOBILEID BANKID ORDERDATE PROVCODE AREACODE TRANSTATE TRANSEQ MERCHECK CARD_TYPE 
	//	MERCHECKDATE BANKCHECKDATE BATCHID SERVPRIV MERPRIV INTIME MODTIME
		
		//初始化参数

		String transDate = StringUtil.trim(urlargs.get(HFBusiDict.UPTRANSDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String orderDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String mercheck = StringUtil.trim(urlargs.get(HFBusiDict.MERCHECK));
		String bankcheckdate = StringUtil.trim(urlargs.get(HFBusiDict.BANKCHECKDATE));
		String mercheckdate = StringUtil.trim(urlargs.get(HFBusiDict.MERCHECKDATE));
		String cardtype = StringUtil.trim(urlargs.get(HFBusiDict.CARDTYPE));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String transId=StringUtil.trim(urlargs.get(HFBusiDict.TRANSID));
		/*
		 * 生成TRANSID 32位
		 */
		if("".equals(transId)){
	        long servicesequence = SequenceUtil.getInstance().getBatchSeq("UPTRANS");
	        String timeString=TimeUtil.datetime14();
	        transId=merid+timeString+SequenceUtil.formatSequence(servicesequence, 10)+transDate.substring(2,6);
	        logInfo("生成的序列数为:%s, transid:%s",servicesequence,transId);	
		}
		
		String batchid = TimeUtil.date8(new Date());                           //批次号默认当前日期
		String funcode = StringUtil.trim(urlargs.get(HFBusiDict.FUNCODE));   
		String month = transDate.substring(2,6);
		
		if(StringUtil.isNullOrNovalue(mercheckdate))
			mercheckdate = new String(transDate);
		if(StringUtil.isNullOrNovalue(bankcheckdate))
			bankcheckdate = new String(transDate);
		if(StringUtil.isNullOrNovalue(mercheck))
			mercheck = new String("0");
		if(StringUtil.isNullOrNovalue(orderDate))
			orderDate = new String("");
		Map<String,String> newTransAllMap = new HashMap<String,String>();	
		newTransAllMap.put("_tablename",month);
		newTransAllMap.put(HFBusiDict.TRANSID+".string",transId );
		newTransAllMap.put(HFBusiDict.PLATORDID+".string", StringUtil.trim(urlargs.get(HFBusiDict.PLATORDID)));
		newTransAllMap.put(HFBusiDict.TRANSEQ+".string", StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ)));
		newTransAllMap.put(HFBusiDict.ORDERDATE+".string", StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE)));
		newTransAllMap.put(HFBusiDict.AMOUNT+".long", StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT)));	
		newTransAllMap.put(HFBusiDict.MERID+".string", StringUtil.trim(urlargs.get(HFBusiDict.MERID)));
		newTransAllMap.put(HFBusiDict.GOODSID+".string", StringUtil.trim(urlargs.get(HFBusiDict.GOODSID)));
		newTransAllMap.put(HFBusiDict.MOBILEID+".string", mobileId);
//		newTransAllMap.put(HFBusiDict.BATCHID+".string",batchid);
		newTransAllMap.put(HFBusiDict.FUNCODE+".string",funcode);
//		newTransAllMap.put(HFBusiDict.MERCHECK+".int",mercheck);
		newTransAllMap.put(HFBusiDict.UPTRANSDATE+".string",transDate);
		newTransAllMap.put(HFBusiDict.TRANSTIME+".string", StringUtil.trim(urlargs.get(HFBusiDict.TRANSTIME)));
		newTransAllMap.put(HFBusiDict.BANKCHECKDATE+".string",bankcheckdate);
		newTransAllMap.put(HFBusiDict.MERCHECKDATE+".string",mercheckdate);
		newTransAllMap.put(HFBusiDict.PROVCODE+".string",StringUtil.trim(urlargs.get(HFBusiDict.PROVCODE)));
		newTransAllMap.put(HFBusiDict.BANKID+".string", StringUtil.trim(urlargs.get(HFBusiDict.BANKID)));
		newTransAllMap.put(HFBusiDict.AREACODE+".string", StringUtil.trim(urlargs.get(HFBusiDict.AREACODE)));
		newTransAllMap.put(HFBusiDict.EXPAND+".string", StringUtil.trim(urlargs.get(HFBusiDict.EXPAND)));
		newTransAllMap.put(HFBusiDict.MERPRIV+".string", StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV)));
		newTransAllMap.put(HFBusiDict.MERCUSTID+".string", StringUtil.trim(urlargs.get(HFBusiDict.MERCUSTID)));
		newTransAllMap.put(HFBusiDict.BANKTRACE+".string",StringUtil.trim(urlargs.get(HFBusiDict.BANKTRACE)));
	//	logInfo("新增交易：=======&&&&&&&&&&&&&&&&&&&  newTransAllMap====%s", newTransAllMap);

		int state = 0;                                                       
		try{
			state = dal.insert("psql_UPTRANSYYYYMM.addtrans", newTransAllMap);
		}catch(DBPKConflictException e){
			logInfo("xetrans"+month+"表中此交易已经存在!  transid:"+transId +"  merid:"+ merid +"  transDate:"+ transDate);
			return "86801033";
		}
		if(state!=1){
			this.logInfo("在xetrans"+month+"新增交易出现系统未知异常   transDate:"+ transDate);
			return BusiConst.SYS_ERROR;
		}
		
		out.put(HFBusiDict.TRANSID,transId);
		logInfo("新增综合支付交易成功!");
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : doShowService 
	 * modified      : LiuJiLong ,  2012-3-1
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String tranSeq = StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ));
		String funcode = StringUtil.trim(urlargs.get(HFBusiDict.FUNCODE));
		String month = tranSeq.substring(tranSeq.length()-4);
		
		Map<String,String> map = new HashMap<String,String>();

		map.put(HFBusiDict.TRANSEQ+".string", tranSeq);
		map.put(HFBusiDict.FUNCODE+".string", funcode);
		map.put("_tablename", month);
		
		Map<String,Object> rs = dal.get("psql_UPTRANS.getUPtransBYtransSeq", map);
		if(rs==null||rs.size()==0){
			this.logInfo("交易信息不存在","");
			return "86801032";
		}
		logInfo("查询交易成功");
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : LiuJiLong ,  2012-3-1
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 * ********************************************/     


	public String doUpdateService(Map<String, String> urlargs, String id)
	throws Exception {
		String transSate = StringUtil.trim(urlargs.get(HFBusiDict.TRANSTATE));
		
		String funcode = StringUtil.trim(urlargs.get(HFBusiDict.FUNCODE));
		String transSeq = StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ));
		String month = transSeq.substring(transSeq.length()-4);
		
		
		Map<String,String> map = new HashMap<String,String>();
		//map.put(HFBusiDict.RESERVED+".string", reserved);
		
		//组装SQL语句的SET语句块
	    Map<String,Object> setMap = new HashMap<String,Object>();
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.MOBILEID))){
			setMap.put(HFBusiDict.MOBILEID, urlargs.get(HFBusiDict.MOBILEID));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.TRANSTATE))){
			setMap.put(HFBusiDict.TRANSTATE, Integer.parseInt(transSate));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get(HFBusiDict.BANKID))){
			setMap.put(HFBusiDict.BANKID, urlargs.get(HFBusiDict.BANKID));
		}
		if(!StringUtil.isNullOrNovalue(urlargs.get("servpriv"))){
			setMap.put("servpriv", urlargs.get("servpriv"));
		}
//		map.put(HFBusiDict.TRANSTATE+".int",transSate);
		map.put(HFBusiDict.FUNCODE,funcode);
		map.put(HFBusiDict.TRANSEQ,transSeq);
		map.put("_tablename",month);
		map.put("setPara",DynSqlUtil.getUpdateSql(setMap));
		
//		int state = dal.update("psql_UPTRANSYYYYMM.updatetransBytranSeq", map);
		int state = dal.update("UPTRANSYYYYMM.updatetransBytranSeq", map);
		if(state!=1){
			logInfo("txetrans"+month+"表中不存在此小额交易");
			return "86801032";
		}
		logInfo("更新小额交易成功");
		return BusiConst.SUCCESS;
}
	/**
	 * ********************************************
	 * method name   : getRpid 
	 * description   : 获取rpid
	 * @return       : String
	 * @param        : @param prpid
	 * @param        : @return
	 * modified      : panxingwu ,  2012-2-24  上午9:43:40
	 * @see          : 
	 * *******************************************
	 */
    public String getRpid(String cardtype, String merid) {
		if (merid == null || merid.length() < 4)
			merid = "1860";
		if (merid.length() > 4)
			merid = merid.substring(1, 5);
		SequenceUtil su = SequenceUtil.getInstance();
		String rpid = cardtype
				+ "C"
				+ merid
				+ SequenceUtil.formatSequence(su.getSequence4File("C1860.rpid"), 8);
		return rpid;
	}
}
