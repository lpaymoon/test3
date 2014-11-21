package hfrest.test;


import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class MerInterCase extends Base {
	public void testDoListService() {
			Map<String, String> map = new HashMap<String, String>();
			//map.put(HFBusiDict.MERID, "9999");
			map.put(HFBusiDict.MERID, "9996");
			//map.put(HFBusiDict.GOODSID, "02999902");
			try {
				Map re = (Map) api.doGET("/hfrestbusi/merinter/M111S1.xml", map,Map.class);
				System.out.println(re);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void testDoShowService() throws Exception{
		Map<String, String> m = new HashMap<String, String>();
		m.put(HFBusiDict.MERID, "9996");
		m.put(HFBusiDict.INFUNCODE, "UMOD");
		m.put(HFBusiDict.INVERSION, "2.0");
		try {
			Map re = (Map) api.doGET("/hfrestbusi/merinter/6785/M1111.xml", m,Map.class);
			System.out.println(re);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Object o = api.doGET("/hfrestbusi/merinter/4865/9996UMOD2.0", m, Map.class);
//		System.out.println(o);
	}
}
