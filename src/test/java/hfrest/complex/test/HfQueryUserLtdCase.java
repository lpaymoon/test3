package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class HfQueryUserLtdCase extends Base{

	public void testDoShowService() throws Exception {
		Map<String,String> m = new HashMap<String,String>();
		m.put(HFBusiDict.MOBILEID, "13426085974");
		System.out.println(api.doGET("/hfrestbusi/hfQueryUserLtd/1111/13426085974.xml", m, Map.class));
	}

}
