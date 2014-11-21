package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class SegInfCase extends Base{

	public void testDoShowService() throws Exception {
		Map m = new HashMap();
		m.put(HFBusiDict.MOBILEID, "1842901");
		Map map = (Map) api.doGET("/hfrestbusi/seginf/M1111/12.xml", m, Map.class);
		System.out.println(map);
	}

}
