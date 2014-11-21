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
public class XeRiskCase extends Base {

	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.BUSITYPE, "1");
		map.put(HFBusiDict.PROVCODE, "010");
		map.put(HFBusiDict.AREACODE, "010");
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.ISCONTROL, "010");
		map.put(HFBusiDict.BUSIROLLTYPE, "1");
		map.put(HFBusiDict.CANCELSIG, "1");
		map.put(HFBusiDict.MOBILEID, "18810528823");
		map.put(HFBusiDict.AMT, "100");
		map.put(HFBusiDict.FEEAMOUNT, "100");
		map.put("USERTYPE", "-1");
		map.put(HFBusiDict.BANKID,"12345");
		map.put("doltd",  "true");
		try {
			Object o = api.doCall("POST", "/hfrestbusi/xerisk/common/123/132.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
