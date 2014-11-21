package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class ChannelInfCase extends Base {

	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.CHANNELID, "100001");
		try {
			Object o = api.doGET("/hfrestbusi/channelInfRest/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
