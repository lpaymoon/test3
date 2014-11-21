package hfrest.test;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;
public class MerReferCase extends Base {
	@Test
	public void testDoListService() {
			Map<String, String> map = new HashMap<String, String>();
			map.put(HFBusiDict.MERID, "9996");
			map.put(HFBusiDict.GOODSID, "100");
			try {
				Object re =  api.doGET("/hfrestbusi/merrefer/M111S1.xml", map,Map.class);
				System.out.println(re);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}