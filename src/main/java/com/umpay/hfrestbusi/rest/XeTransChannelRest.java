package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;


/** ******************  类说明  *********************
 * class       :  XeTransChannelRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  小额交易渠道相关
 * @see        :                        
 * ************************************************/   
public class XeTransChannelRest extends BaseRest{

	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String month = platdate.substring(0,6);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.PLATDATE+".string", platdate);
		map.put(HFBusiDict.AMOUNT+".long", amount);
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.MERID+".string",merid);
		map.put("_tablename",month);
		
		Map<String,Object> rsMap = dal.get("psql_XETRANSCHANNELREST.getXeTransChannel", map);
		if(rsMap==null||rsMap.size()==0){
			this.logInfo("小额交易渠道相关信息不存在:mobileid=%s platdate=%s amout=%s orderid=%s merid=%s", mobileid,platdate,amount,orderid,merid);
			return "86801053";
		}
		out.putAll(rsMap);
		logInfo("查询小额交易渠道相关信息成功", "");
		return BusiConst.SUCCESS;
	}
	/** ********************************************
	 * method name   : doUpdateService 
	 * modified      : zhuoyangyang ,  2013-7-10
	 * description   : 根据交易状态更新交易状态，小额冲正锁定订单和恢复交易状态时会使用到
	 * @see          : 
	 * ********************************************/  
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String transtate = StringUtil.trim(urlargs.get(HFBusiDict.TRANSTATE));
		String bstate = StringUtil.trim(urlargs.get(HFBusiDict.BSTATE));
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
		map.put(HFBusiDict.BSTATE+".int",bstate);
		map.put("_tablename",month);
		
		int state = dal.update("psql_XETRANSYYYYMM.updateXetransState", map);
		if(state!=1){
			logInfo("txetrans"+month+"表中不存在此小额交易");
			return "86801032";
		}
		dal.update("psql_XETRANS.udpateXetransState", map);
		logInfo("更新小额交易成功");
		return BusiConst.SUCCESS;
	}

}
