package hfrest.test;


import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class GoodsServiceIdTest extends Base {

	public void testDoListService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "250");
		
		try {
			Object o = api.doCall("GET", "/hfrestbusi/goodsserviceid/132.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
