package com.umpay.hfrestbusi.rest;

import java.util.Map;

import com.bs.mpsp.util.StringUtil;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.SequenceUtil;


/** ******************  类说明  *********************
 * class       :  SequenceRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  获取序列数
 * @see        :                        
 * ************************************************/   
public class SequenceRest extends BaseRest {

	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String xeseq = StringUtil.trim(urlargs.get(HFBusiDict.XESEQ));
		long servicesequence = 0;
		try{
			servicesequence = SequenceUtil.getBatchSeq(xeseq);
		}catch(Exception e){
			logInfo("调用存储过程获取序列数异常");
			logException(e);
			return "86801055";
		}
		out.put(HFBusiDict.SERVICESEQUENCE, servicesequence);
		return BusiConst.SUCCESS;
	}

}
