package com.umpay.hfrestbusi.rest.complex;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfQueryOrderByMonthRest
 * @author     :  LiuJiLong 
 * description :  渠道冲正对订单进行查询接口
 * @see        :  
 * @version    :  1.0                   
 * ************************************************/   
public class HfQueryOrderByMonthRest extends BaseRest {

	/** ********************************************
	 * method name   : doShowService 渠道冲正订单查询
	 * modified      : LiuJiLong ,  2012-10-22
	 * args          : merid	           商户号	Y   订单表主键
	 *				   orderid	           订单号	Y   订单表主键
	 *				   orderdate	     订单日期	Y   订单表主键
	 * description   :  根据主键orderdate判断是否在当前自然月，如果不是则返回订单查询超出期限（本月）；判断
	 * 是否在七天以内，比如当前日期为10号，则判断日期是否在4、5、6、7、8、9、10 之中。如果是的话则直接调用本
	 * 地接口：/hfrestbusi/hforder/mer/{rpid}/{id}并返回结果；否则查询年表，返回结果。存在返回0000，否则返
	 * 回话费订单不存在。
	 * 2013-02-18 liujilong 第七天的时候在线库与离线库都进行查询
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doShowService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("话费订单信息查询开始,参数:%s", urlargs);

		// 装载必须参数
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));// 商户号
		String orderId = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));// 订单号
		String orderDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));// 订单日期
		Map<String, Object> rs = new HashMap<String, Object>();

		// 根据orderDate(yyyyMMdd)判断是否在当前自然月
		Calendar cal = Calendar.getInstance();// Calendar对象
		int month = Integer.parseInt(orderDate.substring(4, 6));// 订单所在月份
		int monthNow = cal.get(Calendar.MONTH) + 1;// 当前月份
		if (month != monthNow) {
			logInfo("订单查询超出期限（本月）%s", orderDate);
			return "86801060";// 86001060=订单查询超出期限（本月）
		}
		
		// 判断是否在七天之内
		int day = Integer.parseInt(orderDate.substring(6));
		int dayNow = cal.get(Calendar.DAY_OF_MONTH);
		if (dayNow - day > 6) {
			// 超出七天，需要调用离线库，查询年表
			logInfo("[ORDERDATE]在本月且超过七天,数据从离线库中取:%s-%s-%s", orderId,
					orderDate, merId);
			rs = getDBOffOrder(merId, orderId, orderDate);
			// 查询订单不存在
			if (rs == null || rs.size() == 0) {
				logInfo("话费订单信息不存在:%s-%s-%s", orderId, orderDate, merId);
				return "86801101";// 86001101=话费订单不存在
			}
		} else if (dayNow - day == 6) {
			// 正处于第七天，需要先查在线库,再查离线库,查到为止
			logInfo("[ORDERDATE]在本月且日期间隔为七天,数据先从在线库中查询:%s-%s-%s", orderId,
					orderDate, merId);
			rs = getDBOnOrder(merId, orderId, orderDate);
			String retCode = (String) rs.get(HFBusiDict.RETCODE);
			if (!BusiConst.SUCCESS.equals(retCode)) {
				logInfo(
						"[ORDERDATE]在本月且日期间隔为七天,且[在线库]中话费订单信息不存在或数据表不存在,将在离线库中查询:%s-%s-%s",
						orderId, orderDate, merId);
				rs = getDBOffOrder(merId, orderId, orderDate);
				// 查询订单不存在
				if (rs == null || rs.size() == 0) {
					logInfo("话费订单信息不存在:%s-%s-%s", orderId, orderDate, merId);
					return "86801101";// 86001101=话费订单不存在
				}
			}
		} else {
			// 未超过七天，只需要调用本地接口:/hfrestbusi/hforder/mer/{rpid}/{id},并返回结果
			logInfo("[ORDERDATE]在本月且在七天内,数据从在线库中取:%s-%s-%s", orderId,
					orderDate, merId);
			rs = getDBOnOrder(merId, orderId, orderDate);
			String retCode = (String) rs.get(HFBusiDict.RETCODE);
			if (!BusiConst.SUCCESS.equals(retCode)) {
				logInfo("话费订单信息不存在或数据表不存在:%s-%s-%s", orderId, orderDate, merId);
				return retCode;// 86001103=话费订单数据库表不存在;86001101=话费订单不存在
			}
		}
		logInfo("查询话费订单信息成功:%s-%s-%s", orderId, orderDate, merId);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}

	/** ********************************************
	 * method name   : getDBOnOrder 
	 * description   : 查询在线库订单
	 * @return       : Map<String,Object>
	 * @param        : @param merId 商户号
	 * @param        : @param orderId 订单号
	 * @param        : @param orderDate 订单日期
	 * @param        : @return
	 * modified      : LiuJiLong ,  2013-2-18 下午03:05:37
	 * @see          : 
	 * ********************************************/      
	private Map<String, Object> getDBOnOrder(String merId, String orderId,
			String orderDate) {
		Map<String, String> argsOrder = new HashMap<String, String>();
		argsOrder.put(HFBusiDict.MERID, merId);
		argsOrder.put(HFBusiDict.ORDERID, orderId);
		argsOrder.put(HFBusiDict.ORDERDATE, orderDate);
		Map<String, Object> rs = LocalUtil.doGetService(
				"/hfrestbusi/hforder/mer/" + rpid + "/" + merId + "-" + orderId
						+ "-" + orderDate + ".xml", argsOrder);
		return rs;
	}
	
	/** ********************************************
	 * method name   : getDBOffOrder 
	 * description   : 查询离线库订单
	 * @return       : Map<String,Object>
	 * @param        : @param merId
	 * @param        : @param orderId
	 * @param        : @param orderDate
	 * @param        : @return
	 * modified      : LiuJiLong ,  2013-2-18 下午03:13:32
	 * @throws Exception 
	 * @see          : 
	 * ********************************************/      
	private Map<String, Object> getDBOffOrder(String merId, String orderId,
			String orderDate) throws Exception {
		Map<String, String> argsOrderOff = new HashMap<String, String>();
		argsOrderOff.put(HFBusiDict.ORDERID + ".string", orderId);
		argsOrderOff.put(HFBusiDict.ORDERDATE + ".string", orderDate);
		argsOrderOff.put(HFBusiDict.MERID + ".string", merId);
		argsOrderOff.put("_tableName", Calendar.getInstance()
				.get(Calendar.YEAR)
				+ "");
		Map<String, Object> rs = dal.get("dbOff",
				"psql_HfQueryOrderByMonthRest.get", argsOrderOff);
		return rs;
	}
	
	
}
