package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.OrderTableUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfOrderRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  话费订单信息
 * @see        :                        
 * ************************************************/   
public class HfOrderRest extends BaseRest {
	
	/** ********************************************
	 * method name   : doCreateService 
	 * modified      : panxingwu ,  2011-11-4
	 * description   : 新增话费订单信息
	 * ********************************************/     
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("新增话费订单信息:%s", urlargs);
		
		// 装载初始化参数
		String orderId = StringUtil.trim(urlargs.get(HFBusiDict.ORDERID));
		String orderDate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String porderId = StringUtil.trim(urlargs.get(HFBusiDict.PORDERID));
		
		String fristNum = StringUtil.trim(urlargs.get(HFBusiDict.PORDERIDFRISTNUM));//20130322 panxingwu add
		//设置porderid的首位（不同业务短信子号不同，不一定是9）
		if(fristNum==null||fristNum.equals("")){
			fristNum="9";
		}
		Map<String,String> args = new HashMap<String,String>();
		//根据订单日期创建相应的数据库表名
		String tableName = OrderTableUtil.getTableNameByDate(urlargs.get(HFBusiDict.ORDERDATE));
		if(StringUtil.isNullOrNovalue(porderId)){
			porderId = isSpecialMer(merId, tableName,fristNum);
		}
		args.put(HFBusiDict.PORDERID+".string",porderId);
		args.put(HFBusiDict.ORDERID+".string",orderId);
		args.put(HFBusiDict.ORDERDATE+".string",orderDate);
		args.put(HFBusiDict.MERID+".string",merId);
		args.put(HFBusiDict.GOODSID+".string", StringUtil.trim(urlargs.get(HFBusiDict.GOODSID)));
		args.put(HFBusiDict.MOBILEID+".string", StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID)));
		args.put(HFBusiDict.EXPIRETIME+".timestamp", StringUtil.trim(urlargs.get(HFBusiDict.EXPIRETIME)));
		args.put(HFBusiDict.BANKID+".string", StringUtil.trim(urlargs.get(HFBusiDict.BANKID)));
//		args.put(HFBusiDict.ACCESSTYPE+".string", StringUtil.trim(urlargs.get(HFBusiDict.ACCESSTYPE)));
		args.put(HFBusiDict.AMOUNT+".long", StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT)));
		args.put(HFBusiDict.VERIFYCODE+".string", StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE)));
		args.put(HFBusiDict.MERCUSTID+".string", StringUtil.trim(urlargs.get(HFBusiDict.MERCUSTID)));
		args.put(HFBusiDict.MERPRIV+".string", StringUtil.trim(urlargs.get(HFBusiDict.MERPRIV)));
//		args.put(HFBusiDict.NOTIFYURL+".string", StringUtil.trim(urlargs.get(HFBusiDict.NOTIFYURL)));
		args.put(HFBusiDict.EXPAND+".string", StringUtil.trim(urlargs.get(HFBusiDict.EXPAND)));
		args.put(HFBusiDict.AMTTYPE+".string", StringUtil.trim(urlargs.get(HFBusiDict.AMTTYPE)));
//		args.put(HFBusiDict.ORIGAMOUNT+".long", StringUtil.trim(urlargs.get(HFBusiDict.ORIGAMOUNT)));
//		args.put(HFBusiDict.ORPID+".string", rpid);
		args.put(HFBusiDict.VERSION+".string", StringUtil.trim(urlargs.get(HFBusiDict.VERSION)));
		args.put(HFBusiDict.IMEI+".string", StringUtil.trim(urlargs.get(HFBusiDict.IMEI)));//手机串号
		args.put(HFBusiDict.RESERVED+".string", StringUtil.trim(urlargs.get(HFBusiDict.RESERVED)));
		args.put(HFBusiDict.BUSINESSTYPE+".string", StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE)));
		
		String pid = porderId.substring(porderId.length()-1);
		args.put("_tableName",pid);
		
		//向数据中插入数据
		int state = 0;
		try{
			state = dal.insert("psql_HFORDERREST.addHfOrder", args);
		}catch(DBPKConflictException e){
			logException(e);
			//主键冲突，库中已经存在数据
			Map<String,String> m = new HashMap<String,String>();
			m.put(HFBusiDict.ORDERID+".string", orderId);
			m.put(HFBusiDict.ORDERDATE+".string", orderDate);
			m.put(HFBusiDict.MERID+".string",merId);
			m.put("_tableName", pid);
			//主键冲突则向用户返回冲突的该数据
			Map<String,Object> order = dal.get("psql_HFORDERREST.findHfOrder", m);
			out.putAll(order);
			logInfo("话费订单已经存在:%s", order);
			return "86801102";
		}
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		if(state!=1){
			this.logInfo("新增话费订单出现系统未知错误,返回码为:%s", BusiConst.SYS_ERROR);
			return BusiConst.SYS_ERROR;
		}
		Map<String,Object> rs = new HashMap<String,Object>();
		rs.put(HFBusiDict.PORDERID,porderId);
		out.putAll(rs);
		logInfo("新增话费订单信息成功!%s", state);
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
	
	private String isSpecialMer(String merId, String tableName,String porderidFirstNum) throws Exception {
		//20130322 panxingwu update 在做渠道的时候新配了短信子号15，原有的porderid生成规则是首位9，不能满足新的需求，故修改此处逻辑
		String specialMer = NamedProperties.getMapValue(BusiConst.SYSPARAMS, "specialMer", "");
		String isTest =  NamedProperties.getMapValue(BusiConst.SYSPARAMS, "sys.type", "");
		int num = 5;
		if("1".equals(isTest)){
			num=4;
		}
		if(specialMer!=null){
			for(String mer:specialMer.split(",")){
				if(mer.equals(merId)||"ALL".equalsIgnoreCase(mer)){
					return porderidFirstNum + merId + SequenceUtil.formatSequence(SequenceUtil.getBatchSeq("pOrderId"),num)+tableName.substring(tableName.length()-1);
				}
			}
		}
		return porderidFirstNum + SequenceUtil.formatSequence(SequenceUtil.getBatchSeq("pOrderId"),7)+tableName.substring(tableName.length()-1);
		 
	}


	/** ********************************************
	 * method name   : doShowService 
	 * modified      : panxingwu ,  2011-11-3
	 * description   : 根据手机号和平台流水号查询话费订单信息
	 *                 数据库表名为umpay.t_order_0,1,2,3,4,5,6
	 *                 其中后面一位数据是porderid的最后一位
	 * ********************************************/     
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("查询话费订单信息:%s", urlargs);
		
		// 装载初始化参数
		String porderId = StringUtil.trim(urlargs.get(HFBusiDict.PORDERID));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		
		Map<String,String> args = new HashMap<String,String>();
		
		//验证数据库表是否存在
		Integer lastNum = Integer.parseInt(porderId.substring(porderId.length()-1));
		if(lastNum<0||lastNum>6){
			logInfo("数据库表不存在:%s", "noTable");
			return "86801103";
		}
		
		args.put(HFBusiDict.MOBILEID+".string",mobileId);
		args.put(HFBusiDict.PORDERID+".string",porderId);
		args.put("_tableName", lastNum.toString());
		
		//获取话费订单资源
		Map<String,Object> rs = dal.get("psql_HFORDERREST.getHfOrder", args);
		// 结果检查
		this.logInfo("开始校验结果......", "");
		
		//话费订单信息查询结果为空
		if(rs==null||rs.size()==0){
			logInfo("话费订单信息不存在或者订单已过期：%s %s", porderId,mobileId);
			return "86801100";
		}
		
		logInfo("查询话费订单信息成功：%s", rs);
		out.putAll(rs);
		return BusiConst.SUCCESS;
	}
	/**
	 * ******************************************** method name :
	 * doUpdateService modified : Wu Enzhen , 2012-01-12 description : 订单更新
	 * ********************************************/
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("订单更新", urlargs);
		//验证数据库表是否存在
		String porderId = urlargs.get(HFBusiDict.PORDERID);
		Integer lastNum = Integer.parseInt(porderId.substring(porderId.length()-1));
		String verifycode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));
		if(lastNum<0||lastNum>6){
			logInfo("数据库表不存在:%s", "noTable");
			return "86801103";
		}
		if(verifycode.getBytes().length!=verifycode.length()||verifycode.getBytes().length>4){
			logInfo("验证码格式有误:verifycode=%s", verifycode);
			return "86801047";
		}
		
		//装载初始化参数
		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.ORDERSTATE+".int", urlargs.get(HFBusiDict.ORDERSTATE));
		args.put(HFBusiDict.RPID+".string", urlargs.get(HFBusiDict.RPID));
		args.put(HFBusiDict.PLATDATE+".string", urlargs.get(HFBusiDict.PLATDATE));
		args.put(HFBusiDict.MOBILEID+".string", urlargs.get(HFBusiDict.MOBILEID));
		args.put(HFBusiDict.PORDERID+".string", urlargs.get(HFBusiDict.PORDERID));
		args.put(HFBusiDict.VERIFYCODE+".string", urlargs.get(HFBusiDict.VERIFYCODE));
		// 根据订单日期取得对应的数据库表名  PORDERID
		args.put("_tablename", lastNum.toString());
		
		
		//执行更新话费订单信息
		int state = dal.update("psql_HFORDERREST.updateHFOrder", args);
		
		// 结果检查
		this.logInfo("开始校验结果......", "");
		//STATE为零,则输出更新话费订单数为0
		if (state == 0) {
			logInfo("更新话费订单数为0 : %s %s %s", HFBusiDict.PLATDATE, HFBusiDict.MOBILEID, HFBusiDict.PORDERID);
			return "86801048";//话费订单更新数为0
		}
		logInfo("更新话费订单成功:%s", state);
		return BusiConst.SUCCESS;
	}
}
