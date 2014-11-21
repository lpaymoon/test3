package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class HfCheckTransCase extends Base{

	public void testDoShowService() throws Exception {
		Map<String,String> m = new HashMap<String,String>();
		m.put(HFBusiDict.MERID, "9996");
		m.put(HFBusiDict.GOODSID, "100");
		System.out.println(api.doGET("/hfrestbusi/checktrans/common/1111/1111.xml", m, Map.class));
	}

}
