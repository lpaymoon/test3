package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;

/**
 * @author 无线订单符合资源
 *
 */
public class HfWxOrderComplexRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("保存无线订单【复合资源】");
		String IMEI = StringUtil.trim(urlargs.get(HFBusiDict.IMEI));
		String IMSI = StringUtil.trim(urlargs.get(HFBusiDict.IMSI));
		String orderdate = StringUtil.trim(urlargs.get(HFBusiDict.ORDERDATE));
		String clientId = StringUtil.trim(urlargs.get(HFBusiDict.CLIENTID));
		String maxNum = StringUtil.trim(urlargs.get(HFBusiDict.MAX));
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.IMEI, IMEI);
		map.put(HFBusiDict.IMSI, IMSI);
		map.put(HFBusiDict.ORDERDATE, orderdate);
		map.put(HFBusiDict.CLIENTID, clientId);
		if(!"".equals(maxNum)){
			Map<String,Object> orderInf=LocalUtil.doGetService("/hfrestbusi/wxorder/"+rpid+".xml", map);
			List<Map<String,String>> rs = (List<Map<String, String>>) orderInf.get("orderList");
			if(rs!=null&&rs.size()>=(Integer.parseInt(maxNum))){
				logInfo("无线交易，超过下单次数限制！");
				return "86801125";
			}
		}
		Map<String,Object> saveResult = LocalUtil.doPostService("/hfrestbusi/wxorder/"+rpid+".xml", urlargs);
		String retCode = (String) saveResult.get(HFBusiDict.RETCODE);
		out.putAll(saveResult);
		return retCode;
	}

	
}
