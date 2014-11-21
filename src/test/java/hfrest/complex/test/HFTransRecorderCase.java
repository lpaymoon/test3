package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

public class HFTransRecorderCase extends Base {

	public void testDoListService() throws Exception {
		Map<String, String>  map = new HashMap<String,String>();
		map.put("mobileid", "18810528823");
		map.put("transmonth", "201308");
		map.put("monthsign", "2");
		Object o = api.doGET("/hfrestbusi/hfWxTransRecordRest/1233.xml", map, Map.class);
		System.out.println(o);
	}
}
