package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class HfUpdateTransOrderCase  extends Base  {
	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.PLATDATE, "20140701");
		map.put(HFBusiDict.TRANSRPID, "W123aaaaa173125");
		map.put(HFBusiDict.ORDERID, "OD10012302");
		map.put(HFBusiDict.ORDERDATE, "20140701");
		map.put(HFBusiDict.PAYRETCODE, "0000");
		
		try {
			Object o = api.doCall("POST", "/hfrestbusi/HfUpdateTransAndOrder/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
