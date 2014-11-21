package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.OrderTableUtil;
/**
 *  ******************  类说明  *********************
 * class       :  HfMerInformFailRest
 * @author     :  zhaoyan
 * @version    :  1.0  
 * description :  通知商户发货失败记录
 * @see        :                        
 * ***********************************************
 */
public class UpMerInformFailRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("执行插入通知商户发货失败记录操作", "");
		String rpid = StringUtil.trim(urlargs.get(HFBusiDict.RPID));
		String transdate = StringUtil.trim(urlargs.get(HFBusiDict.UPTRANSDATE));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String bankid = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		String state = StringUtil.trim(urlargs.get(HFBusiDict.STATE));
//		String taketimes = StringUtil.trim(urlargs.get(HFBusiDict.TAKETIMES));
		String bankcheckdate = StringUtil.trim(urlargs.get(HFBusiDict.SETTLEDATE));
		String version = StringUtil.trim(urlargs.get(HFBusiDict.UPVERSION));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String merpriv = StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV));
		String appid = StringUtil.trim(urlargs.get(HFBusiDict.APPID));
		String channelid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		String transtype = StringUtil.trim(urlargs.get(HFBusiDict.TRANSTYPE));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.RPID+".string", rpid);
		map.put(HFBusiDict.UPTRANSDATE+".string", transdate);
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.BANKID+".string", bankid);
		map.put(HFBusiDict.STATE+".int", state);
		map.put(HFBusiDict.TAKETIMES+".int", "0");
		map.put(HFBusiDict.BANKCHECKDATE+".string", bankcheckdate);
		map.put(HFBusiDict.VERSION+".string", version);
		map.put(HFBusiDict.AMOUNT+".long", amount);
		map.put(HFBusiDict.MERPRIV+".string", merpriv);
		map.put(HFBusiDict.APPID+".string", appid);
		map.put(HFBusiDict.CHANNELID+".string", channelid);
		map.put(HFBusiDict.TRANSTYPE+".string", transtype);
		// 根据交易日期取得对应的数据库表名
		String tableName = OrderTableUtil.getTableNameByDate(transdate);
//		logInfo("执行操作  _tablename ", tableName.substring(tableName.length() - 1));
		map.put("_tablename", tableName.substring(tableName.length() - 1));
		int excutestate = 0;
		try{
			logInfo("执行操作", "");
			excutestate=dal.insert("psql_UPINFORMFAIL.addInformFail", map);
		}catch(DBPKConflictException e){
			logException(e);
			//主键冲突
			logInfo("商户通知发货失败记录已经存在:%s %s", rpid,transdate);
			return "86801030";
		}
		if(excutestate!=1){
			logInfo("新增通知商户发货失败信息出现未知错误,返回码为:%s", BusiConst.SYS_ERROR);
			return BusiConst.SYS_ERROR;
		}
		logInfo("新增通知商户发货失败信息成功:%s", map);
		return BusiConst.SUCCESS;
	} 
}
