package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;
public class HfMerBusiCase extends Base {

	@Test
	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID,"9996");
		map.put(HFBusiDict.BUSINESSTYPE, "0204");
		try {
			Object o = api.doGET("/hfrestbusi/hfMerBusi/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
