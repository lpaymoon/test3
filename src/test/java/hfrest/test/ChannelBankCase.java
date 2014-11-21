package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

public class ChannelBankCase extends Base {
	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.BANKID, "XE010000");
		map.put(HFBusiDict.CHANNELID, "100004");
		try {
			Object o = api.doGET("/hfrestbusi/channelBankRest/312/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
