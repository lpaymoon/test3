package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  XeRiskCase
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  小额风控
 * @see        :                        
 * ************************************************/   
public class HfMwbillCase extends Base {

	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.BANKID, "MW010000");
		map.put(HFBusiDict.MOBILEID, "13426123451");
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.DELAYSEND, "1");
		map.put(HFBusiDict.PROVCODE, "314");
		map.put(HFBusiDict.AREACODE, "123");
		map.put(HFBusiDict.AMT, "500");
		map.put(HFBusiDict.ORDERID, "22");
		map.put(HFBusiDict.REQDATE, getyyyyMMdd());
		map.put(HFBusiDict.REQTIME, "212212");
		map.put(HFBusiDict.MSGFEETYPE, "3");
		map.put(HFBusiDict.BUSITYPE, "13");
		map.put(HFBusiDict.TRANSFLAG, "0");
		try {
			Object o = api.doCall("POST", "/hfrestbusi/mwbill/createproc/1212/132.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
