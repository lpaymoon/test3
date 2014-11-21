package hfrest.test;

import java.util.HashMap;
import java.util.Map;
public class HfUserRestCase extends Base{

	public final void testDoCreateService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put("merid","1832");
		map.put("goodsid","654657");
		map.put("mobileid","13518654321");
		map.put("ordertime","2008-08-25 13:35:05.316623");
		map.put("noticetime","2008-08-25 13:35:05.316623");
		map.put("hasnoticed","1");
		map.put("enddate","20110414");
		map.put("fstate","2");
		map.put("bstate","4");
		map.put("state","4");
		map.put("bankid","LJL0000");
		map.put("oddinterval","0");
		map.put("serviceid","-LJL");
		map.put("verifycode","4");
		map.put("rpid","M2010029032147");
		map.put("billdate","20110419");
		map.put("cause","8");
		map.put("detail","sdaklfjlkag");
		try {
			Map re = (Map)api.doCall("POST","/hfrestbusi/hfuser/common/LJL234", map , Map.class);
			System.out.println(re);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void testDoShowService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put("merid", "5500");
		map.put("mobileid", "13910006996");
		map.put("goodsid", "550005");
		
		try {
			Map re = (Map)api.doGET("/hfrestbusi/hfuser/common/LJL234/7535", map , Map.class);
			System.out.println(re);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void testDoListService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put("mobileid", "13518661123");
		
		try {
			Object o = api.doGET("/hfrestbusi/hfuser/common/LJL234.xml", map , Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
