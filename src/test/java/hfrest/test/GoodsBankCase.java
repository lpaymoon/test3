package hfrest.test;


import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
public class GoodsBankCase extends Base {
	public void testDoListService() {
			Map<String, String> map = new HashMap<String, String>();
			//map.put(HFBusiDict.MERID, "9999");
			map.put(HFBusiDict.MERID, "9996");
			//map.put(HFBusiDict.GOODSID, "02999902");
			map.put(HFBusiDict.GOODSID, "100");
			try {
				Map re = (Map) api.doGET("/hfrestbusi/goodsbank/M1111.xml", map,Map.class);
				System.out.println(re);
			} catch (Exception e) {
			}
	}
	public void testDoShowService() throws Exception{
		Map<String, String> m = new HashMap<String, String>();
//		m.put(HFBusiDict.MERID, "9999");
//		m.put(HFBusiDict.GOODSID, "02999902");
		m.put(HFBusiDict.MERID, "9996");
		m.put(HFBusiDict.GOODSID, "100");
		m.put(HFBusiDict.BANKID, "MW029000");
		Object o = api.doGET("/hfrestbusi/goodsbank/465/13.xml", m, Map.class);
		System.out.println(o);
	}
}
