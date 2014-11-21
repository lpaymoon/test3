/** *****************  JAVA头文件说明  ****************
 * file name  :  HfXeTransRevokeprocCase.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-2-29
 * *************************************************/ 

package hfrest.complex.test;


import java.util.HashMap;
import java.util.Map;

import hfrest.test.Base;

import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  HfXeTransRevokeprocCase
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  小额交易充正
 * @see        :                        
 * ************************************************/

public class HfXeTransRevokeprocCase extends Base{

	/**
	 * Test method for {@link com.umpay.hfrestbusi.rest.complex.HfXeTransRevokeprocRest#doCreateService(java.util.Map)}.
	 */
	public void testDoCreateService() {
		Map<String,String> map = new HashMap<String,String>();
		//必输
		map.put(HFBusiDict.PLATDATE, getyyyyMMdd());
		map.put(HFBusiDict.FUNCODE, "P100");
		map.put(HFBusiDict.RPID, "ljeeeasdfsadfs5t");
		map.put(HFBusiDict.TRANSEQ, "5415465465465");
	
		map.put(HFBusiDict.MOBILEID, "13118559996");

		map.put(HFBusiDict.MERCHECKDATE, getyyyyMMdd());
		map.put(HFBusiDict.PLATTIME, "191200");
		map.put(HFBusiDict.ISNEW, "SF");
		map.put(HFBusiDict.TRANSTATE, "1");
		map.put(HFBusiDict.AMOUNT, "100");
		map.put(HFBusiDict.TRANSTATE, "1");
		map.put(HFBusiDict.ORDERID, "23511");

		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "1001");
		map.put(HFBusiDict.MOBILEID, "18312340000");
		map.put(HFBusiDict.BANKID, "MW010000");
		map.put(HFBusiDict.AREACODE, "010");
		map.put(HFBusiDict.PROVCODE, "010");
		map.put(HFBusiDict.BANKTRACE, getyyyyMMdd());
		map.put(HFBusiDict.AMT, "100");
		map.put(HFBusiDict.MERCHECK, "23423");
		map.put(HFBusiDict.ISNEW, "1");
		
		//可选
		map.put(HFBusiDict.RESERVED, "");
		map.put(HFBusiDict.CARDTYPE, "22");
		map.put(HFBusiDict.MERCUSTID, "54554");
		map.put(HFBusiDict.BANKCHECKDATE, "20120228");
		Map m = null;
		try {
			m = (Map) api.doCall("post","/hfrestbusi/xetrans/revokeproc/KLJASDFKL.XML", map, Map.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(m);
	}

	/**
	 * Test method for {@link com.umpay.hfrestbusi.rest.complex.HfXeTransRevokeprocRest#doUpdateService(java.util.Map, java.lang.String)}.
	 */
	public void testDoUpdateService() {
		//PLATDATE,TRANSTATE,RPID
		Map<String,String> map =  new HashMap<String,String>();
		map.put(HFBusiDict.PLATDATE,"20120803");
		map.put(HFBusiDict.TRANSTATE,"2");
		map.put(HFBusiDict.FUNCODE, "P100");
		map.put(HFBusiDict.TRANSRPID, "555");
		
		
		try {
			Object o = api.doCall("POST", "/hfrestbusi/xetrans/revokeproc/321/123.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
