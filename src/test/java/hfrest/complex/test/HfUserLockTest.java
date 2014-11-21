package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class HfUserLockTest extends Base {

	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "999602");
		map.put(HFBusiDict.MOBILEID, "13910503209");
		
		try {
			Object o = api.doCall("POST", "/hfrestbusi/hfUserLockRest/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
