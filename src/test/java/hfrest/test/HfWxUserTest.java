package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class HfWxUserTest extends Base {

	public void testDoCreateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.IMEI, "123456789126666");
		map.put(HFBusiDict.PLATTYPE, "1");
		map.put(HFBusiDict.CHNLID, "0001");
		map.put(HFBusiDict.CLIENTVERSION,"2.0.3");
		map.put(HFBusiDict.MOBILEOS, "4.0.3 android");
		map.put(HFBusiDict.MODEL, "MIONE PLUS");
		map.put(HFBusiDict.SOURCEMER,"123213");
		try {
			Object o = api.doCall("POST", "/hfrestbusi/wxclientuser/123.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
