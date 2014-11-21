package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;

public class MerBankCase extends Base{
	public void testDoListService() {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put(HFBusiDict.MERID, "9996");
				Map re = (Map) api.doGET("/hfrestbusi/merbank/M1111.xml", map,Map.class);
				System.out.println(re);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	public void testDoShowService(){
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put(HFBusiDict.MERID, "9996");
			map.put(HFBusiDict.BANKID, "XE010000");
			Map re = (Map) api.doGET("/hfrestbusi/merbank/M1111/132.xml", map,Map.class);
			System.out.println(re);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
