package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class HfWxorderLockTest extends Base {

	public void testDoUpdateService() {
		Map<String,String> map=new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID, "669343998083136");
		map.put(HFBusiDict.ORDERDATE, getyyyyMMdd());
		map.put(HFBusiDict.MERID, "9996");
		
		try {
			Object o = api.doCall("POST", "/hfrestbusi/hfWxOrderLockRest/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
