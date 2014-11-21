package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class HfSpecUserTest extends Base {
	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID, "15011395231");
		Object p;
		try {
			p = api.doCall("GET", "/hfrestbusi/specuser/123/123.xml", map, Map.class);
			System.out.println(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID, "13645700000");
		map.put(HFBusiDict.BUSITYPE, "1");
		map.put(HFBusiDict.BUSIROLLTYPE, "12");
		map.put(HFBusiDict.REASONTYPE, "2");
		map.put(HFBusiDict.EXPIREDDATE, "20121111");
		Object p;
		try {
			p = api.doCall("POST", "/hfrestbusi/specuser/123/123.xml", map, Map.class);
			System.out.println(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void testDoCreateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID, "188"+getyyyyMMdd());
		map.put(HFBusiDict.BUSITYPE, "1");
		map.put(HFBusiDict.BUSIROLLTYPE, "11");
		map.put(HFBusiDict.REASONTYPE, "1");
		map.put(HFBusiDict.EXPIREDDATE, getyyyyMMdd());
		map.put(HFBusiDict.EXPIRETIME, "123456");
		Object p;
		try {
			p = api.doCall("POST", "/hfrestbusi/specuser/123.xml", map, Map.class);
			System.out.println(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
