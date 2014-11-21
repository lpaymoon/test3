package com.umpay.hfrestbusi.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.LocalDal;
import com.umpay.hfrestbusi.util.DynSqlUtil;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;

/**
 * ****************** 类说明 ********************* class : HfSpecuserRest
 * 
 * @author : panxingwu
 * @version : 1.0 description : 话费特殊用户
 * @see : *
 ***********************************************/
public class HfSpecUserRest extends BaseRest {

	/**
	 * ******************************************** method name : doShowService
	 * modified : panxingwu , 2011-11-8 description : 查询话费特殊用户 *
	 *******************************************/
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("查询话费特殊用户,参数:%s", urlargs);
		// 获取DAL
		CommonDalInf dal = new LocalDal();
		logInfo("初始化dal对象成功:%s", dal);

		// 装载初始化参数
		String mobileId = urlargs.get(HFBusiDict.MOBILEID);

		Map<String, String> args = new HashMap<String, String>();
		args.put(HFBusiDict.MOBILEID + ".string", mobileId);
		args.put(HFBusiDict.PLATDATE + ".string", TimeUtil.date8(new Date()));

		// 获取话费特殊用户资源
		Map<String, Object> rs = null;
		rs = dal.get("psql_HFSPECUSERREST.getSpecUser", args);

		// 结果检查
		this.logInfo("开始校验结果......", "");

		// 查询话费特殊用户是否存在
		if (rs != null && rs.size() > 0) {
			// 判断是否为黑名单
			if ("10".equals(rs.get(HFBusiDict.BUSIROLLTYPE))) {
				logInfo("黑名单用户%s", rs);
				return "86801029";
			} else {
				out.putAll(rs);
				logInfo("话费特殊用户查询成功！%s", rs);
				return BusiConst.SUCCESS;
			}
		} else {
			// 查询白名单 20130505 liujilong START
			Map<String, String> wListArgs = new HashMap<String, String>();
			wListArgs.put(HFBusiDict.MOBILEID, mobileId);
			Map<String, Object> wListRs = LocalUtil.doGetService(
					"/hfrestbusi/wlist/" + rpid + "/" + mobileId + ".xml",
					wListArgs);
			String isWList = StringUtil.trim((String) wListRs
					.get(HFBusiDict.ISCONTROL));
			if ("true".equalsIgnoreCase(isWList)) {
				out.put(HFBusiDict.BUSIROLLTYPE, "21");// busirolltype=21 等级为11
														// 代表外部接入白名单
				logInfo("话费特殊用户查询白名单成功！%s", mobileId);
				return BusiConst.SUCCESS;
			} else {
				out.put(HFBusiDict.BUSIROLLTYPE, "*");
				logInfo("话费特殊用户查询白名单不存在或查询失败！%s", mobileId);
			}
			// 查询白名单 20130505 liujilong END
		}

		return BusiConst.SUCCESS;
	}

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String busitype = StringUtil.trim(urlargs.get(HFBusiDict.BUSITYPE));
		String busirolltype = StringUtil.trim(urlargs
				.get(HFBusiDict.BUSIROLLTYPE));
		String reasontype = StringUtil.trim(urlargs.get(HFBusiDict.REASONTYPE));
		String expireddate = StringUtil.trim(urlargs
				.get(HFBusiDict.EXPIREDDATE));

		Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.MOBILEID, mobileid);
		map.put(HFBusiDict.BUSITYPE, busitype);

		Map<String, Object> setmap = new HashMap<String, Object>();
		if (!StringUtil.isNullOrNovalue(busirolltype))
			setmap.put(HFBusiDict.BUSIROLLTYPE, busirolltype);
		if (!StringUtil.isNullOrNovalue(reasontype))
			setmap.put(HFBusiDict.REASONTYPE, Integer.parseInt(reasontype));
		if (!StringUtil.isNullOrNovalue(expireddate))
			setmap.put(HFBusiDict.EXPIREDDATE, expireddate);
		map.put("setPara", DynSqlUtil.getUpdateSql(setmap));

		logInfo("更新特殊用户信息,参数为:%s", map);
		int num = dal.update("USERGRADEREST.update", map);
		if (num != 1) {
			logInfo("特殊用户不存在");
			return "86802026";
		}
		logInfo("更新特殊用户信息成功");
		return BusiConst.SUCCESS;
	}

	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String busitype = StringUtil.trim(urlargs.get(HFBusiDict.BUSITYPE));
		String busirolltype = StringUtil.trim(urlargs
				.get(HFBusiDict.BUSIROLLTYPE));
		String reasontype = StringUtil.trim(urlargs.get(HFBusiDict.REASONTYPE));
		String expireddate = StringUtil.trim(urlargs
				.get(HFBusiDict.EXPIREDDATE));
		String moduser = StringUtil.trim(urlargs.get(HFBusiDict.MODUSER));

		Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.MOBILEID + ".string", mobileid);
		map.put(HFBusiDict.BUSITYPE + ".string", busitype);
		map.put(HFBusiDict.BUSIROLLTYPE + ".string", busirolltype);
		map.put(HFBusiDict.REASONTYPE + ".int", reasontype);
		map.put(HFBusiDict.EXPIREDDATE + ".string", expireddate);
		map.put(HFBusiDict.MODUSER + ".string", moduser);
		logInfo("新增话费特殊用户，参数为:%s", map);
		dal.insert("psql_USERGRADEREST.add", map);
		logInfo("新增特殊用户成功！");
		return BusiConst.SUCCESS;
	}

}
