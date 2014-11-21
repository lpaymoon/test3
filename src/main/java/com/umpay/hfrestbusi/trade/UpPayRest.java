package com.umpay.hfrestbusi.trade;

import java.util.HashMap;
import java.util.Map;

import com.bs3.app.dal.engine.DalEngine2;
import com.bs3.inf.IRestlets.RestletInf;
import com.bs3.utils.BeanFactoryArgs;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.SequenceUtil;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;


/** ******************  类说明  *********************
 * class       :  UpPayRest
 * @author     :  xuwei
 * @version    :  1.0  
 * description :  综合支付处理业务
 * @see        :                        
 * ************************************************/   
public class UpPayRest extends BaseRest {
static BeanFactoryArgs<RestletInf> bfa = new BeanFactoryArgs<RestletInf>();

	static{
		bfa.setMapping(NamedProperties.getMap(DalEngine2.CFG_KEYNAME));
	}

	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		String bankId = StringUtil.trim(urlargs.get(HFBusiDict.BANKID));
		String mobileId = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		String platordId = StringUtil.trim(urlargs.get(HFBusiDict.PLATORDID));//平台订单号
		String provCode=StringUtil.trim((String)urlargs.get(HFBusiDict.PROVCODE));
		String areaCode=StringUtil.trim((String)urlargs.get(HFBusiDict.AREACODE));
		String transDate =TimeUtil.date8();
		String transTime =TimeUtil.time6();

		String amount = StringUtil.trim(urlargs.get(HFBusiDict.AMOUNT));
		String bussinesstype = StringUtil.trim(urlargs.get(HFBusiDict.BUSINESSTYPE));
		String rpid=StringUtil.trim(urlargs.get(HFBusiDict.RPID));
		String retCode = BusiConst.SYS_ERROR;
		String gateId = NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, "gateId."+bussinesstype.substring(0, 3), "");
		
		if(gateId != null && gateId.startsWith(BusiConst.PRE_BANKID_XE)){
			urlargs.put(HFBusiDict.VERSION, BusiConst.XE_VERSION);
		}
		
		String payBranchUrl = "/uprestbusi/branch/" + bussinesstype + "/" + rpid + "/"
				+ mobileId + "-" + merId + "-" + goodsId + ".xml";
		NamedProperties.refresh(DalEngine2.CFG_KEYNAME, false, null);//更新配置文件
		if(bfa.findBean(payBranchUrl, new HashMap<String, String>())==null){//检测子流程是否存在
			return "86801127";//该支付子流程不存在
		}
		Map<String,String> checkTrans =new HashMap<String,String>();
		checkTrans.put(HFBusiDict.MERID, merId);
		checkTrans.put(HFBusiDict.GOODSID, goodsId);
		checkTrans.put(HFBusiDict.MOBILEID, mobileId);
		checkTrans.put(HFBusiDict.BANKID, bankId);
		checkTrans.put(HFBusiDict.ISNEW, "0");
		checkTrans.put(HFBusiDict.AMOUNT, amount);
		checkTrans.put(HFBusiDict.BUSINESSTYPE, bussinesstype);
		String ids=merId+"-"+goodsId+"-"+mobileId;
		
		Map<String,Object> ckPayResp = LocalUtil.doGetService("/hfrestbusi/checktrans/pay/"+rpid+"/"+ids+".xml", checkTrans);
		retCode = (String) ckPayResp.get(HFBusiDict.RETCODE);
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("支付鉴权失败,返回码：%s", retCode);
			return retCode;
		}
		bankId=StringUtil.trim((String)ckPayResp.get(HFBusiDict.BANKID));
		if(!"".equals(mobileId)){
			provCode=StringUtil.trim((String)ckPayResp.get(HFBusiDict.PROVCODE));
			areaCode=StringUtil.trim((String)ckPayResp.get(HFBusiDict.AREACODE));
		}
		out.putAll(ckPayResp);
		String tranSeq=SequenceUtil.BRANCHPAYIDPRE.getBranchPayIdPre(bussinesstype);
		tranSeq=tranSeq+transDate.substring(2,6);
		logInfo("生成TRANSEQ:%s", tranSeq);
		out.put(HFBusiDict.TRANSEQ,tranSeq);
		//保存trans
		Map<String,String> transMap = new HashMap<String,String>();
		transMap.putAll(urlargs);
		
		transMap.put(HFBusiDict.BANKID, bankId);
		transMap.put(HFBusiDict.FUNCODE, "P100");
		transMap.put(HFBusiDict.TRANSEQ, tranSeq);
		transMap.put(HFBusiDict.UPTRANSDATE, transDate);
		transMap.put(HFBusiDict.TRANSTIME, transTime);
		transMap.put(HFBusiDict.PROVCODE, provCode);
		transMap.put(HFBusiDict.AREACODE, areaCode);
//		transMap.put(HFBusiDict.BANKTRACE, StringUtil.trim((String)payBranchRs.get(HFBusiDict.BANKTRACE)));

		removeUnuseArgs(transMap);
		Map<String,Object> rs = LocalUtil.doPostService("/uprestbusi/uptrans/common/"+rpid+".xml",transMap);
		retCode = rs.get(HFBusiDict.RETCODE).toString();
		//返回码只返回记交易的成功与否
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("新增交易失败,返回码为:%s", retCode);
			return retCode;
		}
	    String transId=StringUtil.trim((String)rs.get(HFBusiDict.TRANSID));			
		//更新订单表 orderstate,transid,transdate,mobileid,bankid
		Map<String,String> upOrdMap = new HashMap<String,String>();
		upOrdMap.put(HFBusiDict.MOBILEID, mobileId);
		upOrdMap.put(HFBusiDict.MERCUSTID, urlargs.get(HFBusiDict.MERCUSTID));
		upOrdMap.put(HFBusiDict.ORDERSTATE, "1");//默认更新为支付中
		upOrdMap.put(HFBusiDict.UPTRANSDATE, transDate);
		upOrdMap.put(HFBusiDict.TRANSID, transId);
		upOrdMap.put(HFBusiDict.PLATORDID, platordId);//平台订单号
		upOrdMap.put(HFBusiDict.RPID, rpid);
		upOrdMap.put(HFBusiDict.BANKID, bankId);
		Map<String,Object> rs2 = LocalUtil.doPostService("/uprestbusi/uporder/common/"+rpid+"/"+platordId+".xml",upOrdMap);
		retCode = rs2.get(HFBusiDict.RETCODE).toString();
		//返回码只返回记交易的成功与否
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("更新订单失败,返回码为:%s", retCode);
			return retCode;
		}	
		logInfo("公共流程处理通过 ,%s",rpid);	
		/*
		 * 支付请求各子流程后续处理
		 */

		logInfo("支付子流程ID:" + bussinesstype);
		Map<String, String> argsBranch = new HashMap<String, String>();// COPY一份支付鉴权参数到子流程
		argsBranch.putAll(urlargs);
		argsBranch.put(HFBusiDict.MERID, merId);
		argsBranch.put(HFBusiDict.BANKID, bankId);
		argsBranch.put(HFBusiDict.AMOUNT, amount);
		argsBranch.put(HFBusiDict.GOODSID, goodsId);
		argsBranch.put(HFBusiDict.MOBILEID, mobileId);
		argsBranch.put(HFBusiDict.PROVCODE, provCode);
		argsBranch.put(HFBusiDict.AREACODE, areaCode);
		argsBranch.put(HFBusiDict.UPTRANSDATE, transDate);
		argsBranch.put(HFBusiDict.TRANSEQ, tranSeq);
		
		/**
		 *  为了使商户既能做小额交易 又能做综合支付的交易  综合支付在请求话费平台的时候 merpriv固定传VIRPREMPU000001
		 *  然后小额平台根据VIRPREMPU000001去配置通知地址   不再根据mer_inter表里的地址去通知
		 * @see hfMerFrontBusi.PlatPayNotifyAdapter
		 * fanxiangchi 20141105
		 */
		if(gateId != null && gateId.startsWith(BusiConst.PRE_BANKID_XE)){
			argsBranch.put(HFBusiDict.MERPRIV, BusiConst.XE_MERPRIV);
		}
		removeUnuseArgs(argsBranch);
		Map<String, Object> payBranchRs = LocalUtil.doPostService(payBranchUrl,
				argsBranch);
		payBranchRs.remove(BusiConst.BUSIKEY);
		payBranchRs.remove("LOCAL");
		out.putAll(payBranchRs);

		retCode = payBranchRs.get(HFBusiDict.RETCODE).toString();
		/**
		 * 更新交易表需要更新的字段
		 */
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(HFBusiDict.FUNCODE, "P100");
		argsMap.put(HFBusiDict.TRANSEQ, tranSeq);
		argsMap.put("servpriv", retCode);
		argsMap.put(HFBusiDict.BANKTRACE, StringUtil.trim((String)payBranchRs.get(HFBusiDict.BANKTRACE)));
		Map<String, Object> resMap = LocalUtil.doPostService("/uprestbusi/uptrans/update/" + rpid + "/" + rpid+"-"+tranSeq+"-P100.xml", argsMap);
		String rcode = StringUtil.trim((String) resMap.get(HFBusiDict.RETCODE));
		if(!BusiConst.SUCCESS.equals(rcode)){
			logInfo("更新交易记录失败，retCode,retMsg:%s,%s",rcode,resMap.get(HFBusiDict.RETMSG));
			return rcode;
		}
		
		out.remove("URI");
		out.remove("LOCAL");
		retCode = payBranchRs.get(HFBusiDict.RETCODE).toString();
		if(!BusiConst.SUCCESS.equals(retCode)){
			return retCode;
		}
	
		return BusiConst.SUCCESS;
	}

	/** *****************  方法说明  *****************
	 * method name   :  removeUnuseArgs
	 * @param		 :  @param argsBranch
	 * @return		 :  void
	 * @author       :  Roy 2014-9-29 下午3:22:23
	 * description   :  删除无用参数
	 * @see          :  
	 * ***********************************************/
	private void removeUnuseArgs(Map<String, String> argsBranch) {
		/*
		 * 删除无效参数
		 */
		argsBranch.remove("REMOTE");
		argsBranch.remove("CLASS");
		argsBranch.remove("EXT");
		argsBranch.remove("RID");
		argsBranch.remove("id");
		argsBranch.remove("METHOD");
		argsBranch.remove("URI");
	}
}
