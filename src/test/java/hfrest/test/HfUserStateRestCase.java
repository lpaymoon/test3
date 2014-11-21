package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;

public class HfUserStateRestCase extends Base{

	public final void testDoUpdateService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put("merid","1810");
		map.put("goodsid","654657");
		map.put("mobileid","13518661123");
		map.put("ordertime","2008-08-25 13:35:05.316623");
		map.put("noticetime","2008-08-25 13:35:05.316623");
		map.put("hasnoticed","1");
		map.put("enddate",getyyyyMMdd());
		map.put("fstate","2");
		map.put("bstate","4");
		map.put("state","4");
		map.put("bankid","LJL0000");
		map.put("oddinterval","0");
		map.put("serviceid","-LkL");
		map.put("verifycode","4");
		map.put("rpid","M2010029032147");
		map.put("billdate","20110419");
		map.put("cause","8");
		map.put("detail","sdaklfjlkag");
		try {
			Map re = (Map)api.doCall("POST","/hfrestbusi/hfuser/state/LJL234/2344", map , Map.class);
			System.out.println(re);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public final void testDoListService(){
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.MOBILEID, "13910503209");
		try {
			Object o = api.doCall("GET", "/hfrestbusi/hfuser/state/LJL234.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

