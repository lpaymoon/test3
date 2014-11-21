package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;
public class XeTransChannelCase extends Base {
//	public void testDoListService() {
//		Map<String,String> map = new HashMap<String,String>();
//		map.put(HFBusiDict.MOBILEID,"13910503201");
//		map.put(HFBusiDict.PLATDATE,getyyyyMMdd());
//		map.put(HFBusiDict.MERID,"7508");
//		map.put(HFBusiDict.ORDERID,"000093");
//		map.put(HFBusiDict.AMOUNT,"10000");
//		
//		try {
//			Object o = api.doGET("/hfrestbusi/xetrans/channel/123.xml", map, Map.class);
//			System.out.println(o);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	@Test
	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.PLATDATE,"20130710");
		map.put(HFBusiDict.RESERVED, "0300");
		map.put(HFBusiDict.FUNCODE,"P100");
		map.put(HFBusiDict.TRANSTATE,"0");
		map.put(HFBusiDict.TRANSRPID,"M123115230");
		map.put(HFBusiDict.BSTATE,"-1");
		try {
			Object o = api.doCall("POST","hfrestbusi/xetrans/channel/222/132.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
