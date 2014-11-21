package com.umpay.hfrestbusi.rest.complex;

import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  XeTransCreateRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  小额交易保存
 * @see        :                        
 * ************************************************/   
public class HfXeTransCreateRest extends BaseRest {
	
	/** ********************************************
	 * method name   : doUpdateService 
	 * description   : 记交易操作
	 * args          : platdate	          交易日期	Y   小额交易INSERT子资源参数之一
     *                 rpid	              平台流水	Y   必输参数
	 *				   transeq	 请求流水(平台生成)	Y   小额交易INSERT子资源参数之一
	 *				   mobileid	         手机号码	Y   小额交易INSERT子资源与小额风控回滚参数之一
	 *				   mercheckdate	与商户结帐日期	Y   小额交易INSERT子资源参数之一，如为空则会被赋于platdate
	 *				   plattime	         交易时间	Y   小额交易INSERT子资源参数之一
	 *				   isnew	     新增还是续费	Y   小额交易INSERT子资源参数之一
	 *				   transtate	     交易状态	Y   小额交易INSERT子资源参数之一；决定是否做小额风控。
	 *				   amt	             交易金额	Y   小额交易INSERT子资源与小额风控回滚参数之一
	 *				   merid	           商户号	Y   小额交易INSERT子资源与小额风控回滚参数之一
	 *			       goodsid	           商品号	Y   小额交易INSERT子资源与小额风控回滚参数之一
	 *				   bankid	         银行代码	Y   小额交易INSERT子资源与小额风控回滚参数之一
	 *				   areacode			 地区编码	Y   小额交易INSERT子资源参数之一
	 *				   provcode			   省代码	Y   小额风控回滚参数之一
	 *				   banktrace	  银行返回流水	Y   小额交易INSERT子资源参数之一
	 *				   orderid	           订单号	N   小额交易INSERT子资源参数之一
	 *				   orderdate	     订单日期	N   小额交易INSERT子资源参数之一
	 *				   reserved	         保留字段	N   小额交易INSERT子资源参数之一
	 *				   cardtype	       手机卡类型	N   小额交易INSERT子资源参数之一
	 *				   mercustid	 商户业务号码	N   小额交易INSERT子资源参数之一
	 *				   bankcheckdate与银行结帐日期	N   小额交易INSERT子资源参数之一，如为空则会被赋于platdate
	 *			       iscontrol	控制类商品标识	N   小额交易INSERT子资源与小额风控回滚参数之一
	 *				   xemercheck	   是否做限额	N   小额风控回滚参数之一
	 * modified      : LiuJiLong ,  2012-2-28
	 * @see          : @see com.umpay.hfrestbusi.core.BaseRest#doUpdateService(java.util.Map, java.lang.String)
	 * ********************************************/     
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id) throws Exception{

		String retCode=BusiConst.SYS_ERROR;
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String tranState = StringUtil.trim(urlargs.get(HFBusiDict.TRANSTATE));                 //交易状态，0为成功，其它为失败
		
		//初始化小额交易插入所需参数
		Map<String,String> transAllMap = StringUtil.getNewMap(urlargs, new String[]{
				HFBusiDict.MOBILEID,     HFBusiDict.RPID,        HFBusiDict.BANKID,
			    HFBusiDict.BANKTRACE,    HFBusiDict.PLATDATE,    HFBusiDict.PLATTIME,     
				HFBusiDict.MERID,        HFBusiDict.ORDERID,     HFBusiDict.BANKCHECKDATE,
				HFBusiDict.MERCHECKDATE, HFBusiDict.ISNEW,       HFBusiDict.TRANSTATE,
				HFBusiDict.AMT,          HFBusiDict.GOODSID,     HFBusiDict.AREACODE,
				HFBusiDict.ORDERDATE,    HFBusiDict.TRANSEQ,     HFBusiDict.MERCUSTID,
				HFBusiDict.RESERVED,     HFBusiDict.CARDTYPE,    HFBusiDict.BRANCHID
		},false);
		
		//检测mercheckdate与bankcheckdate，如果为NULL，则将其设为platedate.
		//mercheck不是传入参数，将mercheck设为0.
		if(StringUtil.isNullOrNovalue(transAllMap.get(HFBusiDict.MERCHECKDATE)))
			transAllMap.put(HFBusiDict.MERCHECKDATE, urlargs.get(HFBusiDict.PLATDATE));
		if(StringUtil.isNullOrNovalue(transAllMap.get(HFBusiDict.BANKCHECKDATE)))
			transAllMap.put(HFBusiDict.BANKCHECKDATE, urlargs.get(HFBusiDict.PLATDATE));
		transAllMap.put(HFBusiDict.MERCHECK, "0");
		
		transAllMap.put(HFBusiDict.FUNCODE, "P100");						//记交易的FUNCODE固定为"P100"
		
		//保存transall
		Map<String,Object> rs = LocalUtil.doPostService("/hfrestbusi/xetrans/common/"+rpid+".xml",transAllMap);
		retCode = rs.get(HFBusiDict.RETCODE).toString();
		
		//若交易成功，跳过小额风控；否则不跳过
		if(!tranState.equals("0")){
			//小额风控回滚,对其回应不做判断
			Map<String,String> riskMap = StringUtil.getNewMap(urlargs, new String[]{
					HFBusiDict.PROVCODE,  HFBusiDict.AMT,         HFBusiDict.MERID,
					HFBusiDict.GOODSID,   HFBusiDict.MOBILEID,    HFBusiDict.ISCONTROL,
					HFBusiDict.XEMERCHECK,HFBusiDict.BANKID
			},false);
			String bankId = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
			String reserved = StringUtil.trim(urlargs.get(HFBusiDict.RESERVED));
			if("XE021000".equals(bankId)&&reserved.matches("^1\\d{10}")){
				riskMap.put(HFBusiDict.MOBILEID, reserved);
				logInfo("上海小额交易失败,风控回滚手机号按RESERVED处理:bankid[%s]|reserved[%s]|mobileid[%s]", bankId, reserved, mobileid);
			}
			LocalUtil.doPostService("/hfrestbusi/xerisk/revokeproc/"+rpid+"/"+mobileid+".xml",riskMap);
		}
		
		//返回码只返回记交易的成功与否
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("新增小额交易失败,返回码为:%s", retCode);
		}
		return retCode;
	}
}
