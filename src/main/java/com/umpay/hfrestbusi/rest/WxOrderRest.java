package com.umpay.hfrestbusi.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;


/** ******************  类说明  *********************
 * class       :  ClientOrderRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  无线接入商户订单
 * @see        :                        
 * ************************************************/   
public class WxOrderRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String expiretime = StringUtil.trim(urlargs.get(HFBusiDict.EXPIRETIME));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String version = StringUtil.trim(urlargs.get(HFBusiDict.VERSION));
		String merpriv = StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV));
		String expand = StringUtil.trim(urlargs.get(HFBusiDict.EXPAND));
		String IMEI = StringUtil.trim(urlargs.get(HFBusiDict.IMEI));
		String IMSI = StringUtil.trim(urlargs.get(HFBusiDict.IMSI));
		String clientVersion = StringUtil.trim(urlargs.get(HFBusiDict.CLIENTVERSION));
		String channelid = StringUtil.trim(urlargs.get(HFBusiDict.CHANNELID));
		String clientId = StringUtil.trim(urlargs.get(HFBusiDict.CLIENTID));
		String verifycode = "8";
		if ("0106".equals(StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE)))) {
			verifycode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));
		}
		
		String ICCID = StringUtil.trim(urlargs.get("ICCID"));
		String PHONETYPE = StringUtil.trim(urlargs.get("PHONETYPE"));
		String PHONEOS = StringUtil.trim(urlargs.get("PHONEOS"));
		String PLATTPYE = StringUtil.trim(urlargs.get("PLATTPYE"));
		String SDKTPYE = StringUtil.trim(urlargs.get("SDKTPYE"));
		String ISROOT = StringUtil.trim(urlargs.get("ISROOT"));
		String PHONEOPERATOR = StringUtil.trim(urlargs.get("PHONEOPERATOR"));
		String NETTYPE = StringUtil.trim(urlargs.get("NETTYPE"));
		String LASTGPSLOCATION = StringUtil.trim(urlargs.get("LASTGPSLOCATION"));
		String GETPHONENUM = StringUtil.trim(urlargs.get("GETPHONENUM"));
		String APPSLISI = StringUtil.trim(urlargs.get("APPSLISI"));

		//根据订单日期创建相应的数据库表名
		String tableName = OrderTableUtil.getTableNameByDate(urlargs.get(HFBusiDict.ORDERDATE));
		String  porderId = isSpecialMer(merid, tableName);
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID+".string",orderid);
		map.put(HFBusiDict.ORDERDATE+".string",orderdate);
		map.put(HFBusiDict.MERID+".string",merid);
		map.put(HFBusiDict.GOODSID+".string",goodsid);
		map.put(HFBusiDict.EXPIRETIME+".timestamp",expiretime);
		map.put(HFBusiDict.PORDERID+".string",porderId);
		map.put(HFBusiDict.AMOUNT+".long",amount);
		map.put(HFBusiDict.VERIFYCODE+".string",verifycode);
		map.put(HFBusiDict.VERSION+".string",version);
		map.put(HFBusiDict.MERPRIV+".string",merpriv);
		map.put(HFBusiDict.EXPAND+".string",expand);
		map.put(HFBusiDict.IMEI+".string", IMEI);
		map.put(HFBusiDict.IMSI+".string", IMSI);
		map.put(HFBusiDict.CLIENTVERSION+".string", clientVersion);
		map.put(HFBusiDict.CHANNELID+".string", channelid);
		map.put(HFBusiDict.CLIENTID+".string", clientId);

		map.put("iccid.string", ICCID);
		map.put("model.string", PHONETYPE);
		map.put("mobileos.string", PHONEOS);
		if (!"".equals(PLATTPYE)) {
			map.put("platType.int", PLATTPYE);
		}
		if (!"".equals(SDKTPYE)) {
			map.put("sdkType.int", SDKTPYE);
		}
		if (!"".equals(ISROOT)) {
			map.put("isRoot.int", ISROOT);
		}
		map.put("networkOperatorName.string", PHONEOPERATOR);
		map.put("mobileNet.string", NETTYPE);
		map.put("lastGpsLocation.string", LASTGPSLOCATION);
		map.put("mobileNo.string", GETPHONENUM);
		map.put("userAppsList.string", APPSLISI);

		map.put("_tablename",porderId.substring(porderId.length()-1));
		int state = 0;
		logInfo("无线R4订单信息:%s", map);
		try{
			state = dal.insert("psql_WXorder.addOrder", map);
		}catch(DBPKConflictException e){
			logInfo("无线商户订单已经存在");
			return "86801106";
		}
		if(state!=1){
			this.logInfo("新增无限商户订单出现系统未知错误,返回码为:%s", BusiConst.SYS_ERROR);
			return BusiConst.SYS_ERROR;
		}
		out.put(HFBusiDict.PORDERID,porderId);
		logInfo("新增无线商户订单成功");
		return BusiConst.SUCCESS;
	}
	/**
	 * ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2012-6-20
	 * description   : 根据主键查询无线订单信息 
	 * *******************************************
	 */
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		if ("true".equals(urlargs.get("queryForKJZF"))) {
			logInfo("根据三码查询无线订单列表....");
			String IMEI = StringUtil.trim(urlargs.get(HFBusiDict.IMEI));
			String IMSI = StringUtil.trim(urlargs.get(HFBusiDict.IMSI));
			String ICCID = StringUtil.trim(urlargs.get("iccid"));
			logInfo("IMEI:"+IMEI+",IMSI:"+IMSI+",ICCID:"+ICCID);
			String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
			String tableName = OrderTableUtil.getTableNameByDate(orderdate);
			String tableNum = tableName.substring(tableName.length()-1);
			Map<String,String> map = new HashMap<String,String>();
			map.put(HFBusiDict.IMEI+".string", IMEI);
			map.put(HFBusiDict.IMSI+".string", IMSI);
			map.put("iccid.string", ICCID);
			map.put("_tablename",tableNum);
			String tableNum2 = "";
			if ("0".equals(tableNum)) {
				tableNum2 = "6";
			} else {
				tableNum2 = String.valueOf((Integer.parseInt(tableNum)-1));
			}
			map.put("_tablename2",tableNum2);
			Map<String,Object> rs = dal.get("psql_Wxorder.queryOrders", map);
			out.put("qryOrder", rs);
			return BusiConst.SUCCESS;
		}
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.MERID+".string", merid);
		String tableName = OrderTableUtil.getTableNameByDate(urlargs.get(HFBusiDict.ORDERDATE));
		String tableNum = tableName.substring(tableName.length()-1);
		map.put("_tablename",tableNum);
		logInfo("查询无线商户订单信息,参数为%s", map);
		Map<String,Object> rsMap = dal.get("psql_Wxorder.getOrder", map);
		if(rsMap==null||rsMap.size()==0){
			logInfo("无线商户订单不存在或者订单已经过期");
			return "86802022";
		}
		logInfo("查询无线商户订单信息成功");
		//转换字段大小写
		String imei = StringUtil.trim((String) rsMap.get("imei"));
		String imsi = StringUtil.trim((String) rsMap.get("imsi"));
		if(!"".equals(imei))rsMap.put(HFBusiDict.IMEI, imei);
		if(!"".equals(imsi))rsMap.put(HFBusiDict.IMSI, imsi);
		out.putAll(rsMap);
		return BusiConst.SUCCESS;
	}
	/**
	 * ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2012-6-20
	 * description   : 更新无线订单状态
	 * *******************************************
	 */
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String orderid = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String orderstate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERSTATE));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID+".string", orderid);
		map.put(HFBusiDict.ORDERDATE+".string", orderdate);
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.ORDERSTATE+".int", orderstate);
		String tableName = OrderTableUtil.getTableNameByDate(urlargs.get(HFBusiDict.ORDERDATE));
		String tableNum = tableName.substring(tableName.length()-1);
		map.put("_tablename",tableNum);
		logInfo("更新无线订单状态,参数为:%s", map);
		int num = dal.update("psql_Wxorder.updateOrder", map);
		if(num!=1){
			logInfo("无线订单不存在或者订单不存在");
			return "86802022";
		}
		logInfo("更新无线订单状态成功");
		return BusiConst.SUCCESS;
	}
	
	
	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		logInfo("查询无线订单列表....");
		String clientId = StringUtil.trim(urlargs.get(HFBusiDict.CLIENTID));
		String IMEI = StringUtil.trim(urlargs.get(HFBusiDict.IMEI));
		String IMSI = StringUtil.trim(urlargs.get(HFBusiDict.IMSI));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String tableName = OrderTableUtil.getTableNameByDate(orderdate);
		String tableNum = tableName.substring(tableName.length()-1);
		if(!"".equals(clientId)){//从3.0.0开始使用客户端ID进行交易限制
			Map<String,String> listMap = new HashMap<String,String>();
			listMap.put(HFBusiDict.CLIENTID+".string",clientId);
			listMap.put("_tablename",tableNum);
			List<Map<String,Object>> rs = dal.find("psql_Wxorder.findOrders", listMap);
			out.put("orderList", rs);
			return BusiConst.SUCCESS; 
		}
		Map<String,String> map = new HashMap<String,String>();
		List<Map<String,Object>> rs=new ArrayList<Map<String,Object>>();
		if("".equals(IMEI)||"".equals(IMSI)){
			map.put(HFBusiDict.IMEI+".string", IMEI);
			map.put(HFBusiDict.IMSI+".string", IMSI);
			map.put("_tablename",tableNum);
		}
		out.put("orderList", rs);
		return BusiConst.SUCCESS;
	}
	/** ********************************************
	 * method name   : isSpecialMer 
	 * description   : 获取特殊商户
	 * @return       : Boolean
	 * @param        : @param merId
	 * @param        : @return
	 * modified      : LiuJiLong ,  2012-4-5  下午02:42:33
	 * @throws Exception 
	 * @see          : 
	 * ********************************************/      
	
	private String isSpecialMer(String merId, String tableName) throws Exception {
		String specialMer = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "specialMer", "");
		String isTest =  NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.type", "");
		int num = 5;
		if("1".equals(isTest)){
			num=4;
		}
		if(specialMer!=null){
			for(String mer:specialMer.split(",")){
				if(mer.equals(merId)||"ALL".equalsIgnoreCase(mer)){
					logInfo("特殊商户配置有此商户号，生成新规则的porderid", specialMer);
					return "9" + merId + SequenceUtil.formatSequence(SequenceUtil.getBatchSeq("pOrderId"),num)+tableName.substring(tableName.length()-1);
				}
			}
		}
		return "9" + SequenceUtil.formatSequence(SequenceUtil.getBatchSeq("pOrderId"),7)+tableName.substring(tableName.length()-1);
		 
	}
}
