package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class WmersmsInfTest extends Base {

	public void testDoCreateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID,"9999");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.MOBILEID, "18810528888");
		
		try {
			Object ob = api.doCall("POST", "/hfrestbusi/wmersmsinf/111.xml", map, Map.class);
			System.out.println(ob);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID,"9999");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.MOBILEID, "18810528888");
		
		try {
			Object ob = api.doCall("POST", "/hfrestbusi/wmersmsinf/111/222.xml", map, Map.class);
			System.out.println(ob);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
