package com.umpay.hfrestbusi.rest.complex;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.LocalUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  HfCheckTrans
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  交易鉴权
 * @see        :                        
 * ************************************************/   
public class HfCheckTransRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		logInfo("执行交易鉴权,参数：%s", urlargs);
		String retCode = BusiConst.SYS_ERROR;
		
		Map<String,String> merMap = new HashMap<String,String>();
		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
		merMap.put(HFBusiDict.MERID, merId);
		
		//获取商户信息
		Map<String,Object> merInf = LocalUtil.doGetService("/hfrestbusi/merinf/"+rpid+"/"+merId+".xml", merMap);
		retCode = merInf.get(HFBusiDict.RETCODE).toString();
		
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商户信息失败,merid:%s", merId);
			return retCode;
		}
		Map<String,String> goodsMap = new HashMap<String,String>();
		goodsMap.put(HFBusiDict.MERID, merId);
		goodsMap.put(HFBusiDict.GOODSID, goodsId);
		
		//获取商品信息
		Map<String,Object> goodsInf = LocalUtil.doGetService("/hfrestbusi/goodsinf/"+rpid+"/"+merId+"-"+goodsId+".xml", goodsMap);
		retCode = goodsInf.get(HFBusiDict.RETCODE).toString();
		if(!BusiConst.SUCCESS.equals(retCode)){
			logInfo("获取商品信息失败：%s %s", merInf,goodsInf);
			return retCode;
		}
		out.putAll(merInf);
		out.putAll(goodsInf);
		logInfo("交易鉴权通过,鉴权信息：%s %s", merInf,goodsInf);
		return BusiConst.SUCCESS;
	}
	
}
