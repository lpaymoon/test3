package com.umpay.hfrestbusi.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.SequenceUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;


/** ******************  类说明  *********************
 * class       :  XeTrans
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  小额交易
 * @see        :                        
 * ************************************************/   
public class XeTransRest extends BaseRest {

	
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
		
		//初始化参数
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String mercheck = StringUtil.trim(urlargs.get(HFBusiDict.MERCHECK));
		String bankcheckdate = StringUtil.trim(urlargs.get(HFBusiDict.BANKCHECKDATE));
		String mercheckdate = StringUtil.trim(urlargs.get(HFBusiDict.MERCHECKDATE));
		String cardtype = StringUtil.trim(urlargs.get(HFBusiDict.CARDTYPE));
		
		String batchid = TimeUtil.date8(new Date());                           //批次号默认当前日期
		String funcode = StringUtil.trim(urlargs.get(HFBusiDict.FUNCODE));   
		String month = platdate.substring(0,6);
		
		if(StringUtil.isNullOrNovalue(mercheckdate))
			mercheckdate = new String(platdate);
		if(StringUtil.isNullOrNovalue(bankcheckdate))
			bankcheckdate = new String(platdate);
		if(StringUtil.isNullOrNovalue(mercheck))
			mercheck = new String("0");
		if(StringUtil.isNullOrNovalue(orderdate))
			orderdate = new String("");
		
		//初始化小额交易新表插入所需参数
		Map<String,String> newTransAllMap = StringUtil.getNewMap(urlargs, new String[]{
				HFBusiDict.MOBILEID+".long",     HFBusiDict.RPID,        HFBusiDict.BANKID,
			    HFBusiDict.BANKTRACE,    HFBusiDict.PLATDATE,    HFBusiDict.PLATTIME,     
				HFBusiDict.MERID,        HFBusiDict.ORDERID,     HFBusiDict.BANKCHECKDATE,
				HFBusiDict.MERCHECKDATE, HFBusiDict.ISNEW+".int",HFBusiDict.TRANSTATE+".int",
				HFBusiDict.AMT+".long",  HFBusiDict.GOODSID,     HFBusiDict.AREACODE,
				HFBusiDict.ORDERDATE,    HFBusiDict.TRANSEQ,     HFBusiDict.MERCUSTID,
				HFBusiDict.RESERVED,     HFBusiDict.CARDTYPE
		},true);		
		newTransAllMap.put("_tablename",month);
		newTransAllMap.put(HFBusiDict.BATCHID+".string",batchid);
		newTransAllMap.put(HFBusiDict.FUNCODE+".string",funcode);
		newTransAllMap.put(HFBusiDict.MERCHECK+".int",mercheck);
		newTransAllMap.put(HFBusiDict.ORDERDATE+".string",orderdate);
		newTransAllMap.put(HFBusiDict.BANKCHECKDATE+".string",bankcheckdate);
		newTransAllMap.put(HFBusiDict.MERCHECKDATE+".string",mercheckdate);
				
		int state = 0;                                                       
		try{
			state = dal.insert("psql_XETRANSYYYYMM.addXetrans", newTransAllMap);
		}catch(DBPKConflictException e){
			logInfo("xetrans"+month+"表中此小额交易已经存在!  goodsid:"+goodsid +"  merid:"+ merid +"  platdate:"+ platdate);
			return "86801033";
		}
		if(state!=1){
			this.logInfo("在xetrans"+month+"新增小额交易出现系统未知异常   platdate:"+ platdate);
			return BusiConst.SYS_ERROR;
		}
		
//		
//		//初始化小额交易旧表插入所需参数
//		Map<String,String> oldTransAllMap = StringUtil.getNewMap(urlargs, new String[]{
//				HFBusiDict.MOBILEID,     HFBusiDict.RPID,        HFBusiDict.BANKID,
//			    HFBusiDict.BANKTRACE,    HFBusiDict.PLATDATE,    HFBusiDict.PLATTIME,     
//				HFBusiDict.MERID,        HFBusiDict.ORDERID,     HFBusiDict.BANKCHECKDATE,
//				HFBusiDict.MERCHECKDATE, HFBusiDict.ISNEW+".int",HFBusiDict.TRANSTATE+".int",
//				HFBusiDict.AMT+".long",  HFBusiDict.GOODSID,     HFBusiDict.AREACODE,
//				HFBusiDict.ORDERDATE,    HFBusiDict.TRANSEQ,     HFBusiDict.MERCUSTID,
//				HFBusiDict.RESERVED,     HFBusiDict.BRANCHID
//		},true);
////		业务层生成		
////		if("UNDO".equals(funcode)){
////			rpid = getRpid(cardtype,merid);//重新生成RPID，确保与原纪录主键不冲突(transall表中funcode不是主键)
////		}
//		oldTransAllMap.put("_tablename",month);
//		oldTransAllMap.put(HFBusiDict.RPID+".string",rpid);
//		oldTransAllMap.put(HFBusiDict.BATCHID+".string",batchid);
//		oldTransAllMap.put(HFBusiDict.FUNCODE+".string",funcode);
//		if(StringUtil.trim(urlargs.get(HFBusiDict.BRANCHID))==""){
//			oldTransAllMap.put(HFBusiDict.BRANCHID+".string",cardtype);//transall表中是branchid
//		}
//		oldTransAllMap.put(HFBusiDict.MERCHECK+".int",mercheck);
//		oldTransAllMap.put(HFBusiDict.ORDERDATE+".string",orderdate);
//		oldTransAllMap.put(HFBusiDict.BANKCHECKDATE+".string",bankcheckdate);
//		oldTransAllMap.put(HFBusiDict.MERCHECKDATE+".string",mercheckdate);
//				
//		try{
//			dal.insert("psql_XETRANS.addXetrans", oldTransAllMap);
//		}catch(DBPKConflictException e){
//			logInfo("transall表中此小额交易已经存在!   platdate:"+ platdate);
//		}
		logInfo("新增小额交易成功!");
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
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String funcode = StringUtil.trim(urlargs.get(HFBusiDict.FUNCODE));
		String preRpid = StringUtil.trim(urlargs.get(HFBusiDict.TRANSRPID));
		String month = platdate.substring(0,6);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.TRANSRPID+".string", preRpid);
		map.put(HFBusiDict.PLATDATE+".string", platdate);
		map.put(HFBusiDict.FUNCODE+".string", funcode);
		map.put("_tablename", month);
		
		Map<String,Object> rs = dal.get("psql_XETRANS.getXetrans", map);
		if(rs==null||rs.size()==0){
			this.logInfo("小额交易信息不存在","");
			return "86801032";
		}
		logInfo("查询小额交易成功");
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}

	
	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : LiuJiLong ,  2012-3-1
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String transtate = StringUtil.trim(urlargs.get(HFBusiDict.TRANSTATE));
		String reserved = StringUtil.trim(urlargs.get(HFBusiDict.RESERVED));
		String funcode = StringUtil.trim(urlargs.get(HFBusiDict.FUNCODE));
		String transRpid = StringUtil.trim(urlargs.get(HFBusiDict.TRANSRPID));
		String month = platdate.substring(0,6);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.RPID+".string", transRpid);
		map.put(HFBusiDict.PLATDATE+".string",platdate);
		map.put(HFBusiDict.RESERVED+".string", reserved);
		map.put(HFBusiDict.TRANSTATE+".int",transtate);
		map.put(HFBusiDict.FUNCODE+".string",funcode);
		map.put(HFBusiDict.TRANSRPID+".string",transRpid);
		map.put("_tablename",month);
		
		int state = dal.update("psql_XETRANSYYYYMM.updateXetrans", map);
		if(state!=1){
			logInfo("txetrans"+month+"表中不存在此小额交易");
			return "86801032";
		}
		dal.update("psql_XETRANS.udpateXetrans", map);
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
