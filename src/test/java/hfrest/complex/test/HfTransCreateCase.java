/** *****************  JAVA头文件说明  ****************
 * file name  :  HfTransCreateCase.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-2-28
 * *************************************************/ 

package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  HfTransCreateCase
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class HfTransCreateCase extends Base{

	/**
	 * Test method for {@link com.umpay.hfrestbusi.rest.complex.HfXeTransCreateRest#doUpdateService(java.util.Map, java.lang.String)}.
	 * @throws Exception 
	 */
	public void testDoUpdateService() throws Exception {
		Map<String,String> map = new HashMap<String,String>();

		//必输
		map.put(HFBusiDict.PLATDATE, getyyyyMMdd());
		map.put(HFBusiDict.RPID, "ljeeeasdfsadfs5t");
		map.put(HFBusiDict.TRANSEQ, "5415465465465");
	
		map.put(HFBusiDict.MOBILEID, "18810528823");

		map.put(HFBusiDict.MERCHECKDATE, getyyyyMMdd());
		map.put(HFBusiDict.PLATTIME, "110621");
		map.put(HFBusiDict.ISNEW, "SF");
		map.put(HFBusiDict.TRANSTATE, "-1");
		map.put(HFBusiDict.AMOUNT, "100");
		map.put(HFBusiDict.ORDERID, "23511");

		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.MOBILEID, "18810528823");
		map.put(HFBusiDict.BANKID, "MW010000");
		map.put(HFBusiDict.AREACODE, "010");
		map.put(HFBusiDict.PROVCODE, "010");
		map.put(HFBusiDict.BANKTRACE, "23423543");
		map.put(HFBusiDict.AMT, "100");
		map.put(HFBusiDict.MERCHECK, "23423");
		map.put(HFBusiDict.ISNEW, "1");
		
		//可选
		map.put(HFBusiDict.RESERVED, "brachid7");
		map.put(HFBusiDict.BRANCHID, "brachid7");
		map.put(HFBusiDict.CARDTYPE, "22");
		map.put(HFBusiDict.MERCUSTID, "54554");
		map.put(HFBusiDict.BANKCHECKDATE, getyyyyMMdd());
		map.put(HFBusiDict.XEMERCHECK, "TRUE");
		Map m = (Map) api.doCall("post","/hfrestbusi/xetrans/createproc/l17444555test/9996.xml", map, Map.class);
		System.out.println(m);
	}

}
