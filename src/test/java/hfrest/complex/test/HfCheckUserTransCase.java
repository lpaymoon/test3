package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class HfCheckUserTransCase extends Base{

	public void testDoShowService() throws Exception {
		Map<String,String>m = new HashMap<String,String>();
		m.put(HFBusiDict.MERID, "9996");
		m.put(HFBusiDict.MOBILEID, "18312340000");
		m.put(HFBusiDict.GOODSID, "100");
		m.put(HFBusiDict.ISNEW, "1");
		Map map = (Map) api.doGET("/hfrestbusi/checktrans/mobileid/132/13.xml", m, Map.class);
		System.out.println(map);
	}

}
