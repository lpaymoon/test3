package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.OrderTableUtil;


/** ******************  类说明  *********************
 * class       :  WmersmsInfRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  无线接入商户信息
 * @see        :                        
 * ************************************************/   
public class WmersmsInfRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String tablename = OrderTableUtil.getCurTableNum();
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID+".string",merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put("_tablename", tablename);
		logInfo("更新无线接入交易记录表，累加有效数开始:%s", map);
		int state = dal.update("psql_WmersmsInfRest.upValidnum", map);
		if(state!=1){
			logInfo("更新数为0，当天该号码不存在无线交易，新增记录!");
			dal.insert("psql_WmersmsInfRest.insertValidnum", map);
		}
		logInfo("无线接入交易记录表操作成功!");
		return BusiConst.SUCCESS;
	}

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String tablename = OrderTableUtil.getCurTableNum();
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID+".string",merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put("_tablename", tablename);
		logInfo("更新无线接入交易记录表，对validnum值进行累减:%s", map);
		int state = dal.update("psql_WmersmsInfRest.downValidnum", map);
		if(state!=1){
			logInfo("记录不存在或者有效值（validnum）为0");
			return "86802016";
		}
		logInfo("无线接入交易记录表操作成功,已更新validnum的值");
		return BusiConst.SUCCESS;
	}

}
