package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class XEuserltdTest extends Base {
	public void testDoShowService() {
		Map<String,String> map =  new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID, "18810528823");
		try {
			Object o = api.doGET("/hfrestbusi/xeuserltd/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
