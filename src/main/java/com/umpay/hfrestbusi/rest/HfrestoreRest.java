package com.umpay.hfrestbusi.rest;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.exception.dbexception.DBPKConflictException;
import com.umpay.hfrestbusi.util.DynSqlUtil;
import com.umpay.hfrestbusi.util.StringUtil;

/**
 * @author panxingwu
 * desccription:风控被拒交易挽回
 */
public class HfrestoreRest extends BaseRest {

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		String operid = StringUtil.trim(urlargs.get(HFBusiDict.OPERID));
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String merid = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsid = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String porderid = StringUtil.trim(urlargs.get(HFBusiDict.PORDERID));
		String state = StringUtil.trim(urlargs.get(HFBusiDict.STATE));
		String moduser = StringUtil.trim(urlargs.get(HFBusiDict.MODUSER));

		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.OPERID+".string", operid);
		map.put(HFBusiDict.MOBILEID+".string", mobileid);
		map.put(HFBusiDict.MERID+".string", merid);
		map.put(HFBusiDict.GOODSID+".string", goodsid);
		map.put(HFBusiDict.PLATDATE+".string", platdate);
		map.put(HFBusiDict.AMOUNT+".int", amount);
		map.put(HFBusiDict.PORDERID+".string", porderid);
		map.put(HFBusiDict.STATE+".int", state);
		map.put(HFBusiDict.MODUSER+".string", moduser);
		map.put(HFBusiDict.RPID+".string", rpid);
		logInfo("新增风控被拒挽回交易,参数为:%s", map);
		try{
			dal.insert("psql_HfrestoreRest.add", map);
		}catch(DBPKConflictException e){
			logInfo("被拒交易信息已经存在!");
			return "86802025";
		}
		return BusiConst.SUCCESS;
	}

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {

		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String platdate = StringUtil.trim(urlargs.get(HFBusiDict.PLATDATE));
		String porderid = StringUtil.trim(urlargs.get(HFBusiDict.PORDERID));
		String state = StringUtil.trim(urlargs.get(HFBusiDict.STATE));
		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String bstate = StringUtil.trim(urlargs.get(HFBusiDict.BSTATE));//原状态
		
		Map<String,Object> setPara = new HashMap<String,Object>();
		if(!StringUtil.isNullOrNovalue(state)) setPara.put(HFBusiDict.STATE, Integer.parseInt(state));//setPara.append(HFBusiDict.STATE+"="+state+",");
		if(!StringUtil.isNullOrNovalue(amount)) setPara.put(HFBusiDict.AMOUNT,  Integer.parseInt(amount)); //setPara.append(HFBusiDict.AMOUNT+"="+amount+",");
		Map<String,Object> wherePara = new HashMap<String,Object>();
		if(!StringUtil.isNullOrNovalue(mobileid)) wherePara.put(HFBusiDict.MOBILEID,mobileid);
		if(!StringUtil.isNullOrNovalue(platdate)) wherePara.put(HFBusiDict.PLATDATE,platdate);
		if(!StringUtil.isNullOrNovalue(porderid)) wherePara.put(HFBusiDict.PORDERID,porderid);
		if(!StringUtil.isNullOrNovalue(bstate)) wherePara.put(HFBusiDict.STATE,Integer.parseInt(bstate));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("setPara", DynSqlUtil.getUpdateSql(setPara));
		map.put("wherePara",  DynSqlUtil.getWhereSql(wherePara));
		logInfo("更新被拒交易信息,参数为:%s", map);
		int num=dal.update("HfrestoreRest.update", map);
		if(num==0){
			logInfo("没有对应条件的被拒交易挽回信息，更新失败");
			return "86802027";
		}
		logInfo("更新被拒交易信息成功");
		return BusiConst.SUCCESS;
	}

}
