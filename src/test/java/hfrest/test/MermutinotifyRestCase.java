package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;
public class MermutinotifyRestCase extends Base {

	@Test
	public void testDoCreateService() {
		Map<String,String> map= new HashMap<String,String>();
		map.put(HFBusiDict.RPID, "123321123");
		map.put(HFBusiDict.PLATDATE, getyyyyMMdd());
		map.put(HFBusiDict.MOBILEID, "18810528823");
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.ORDERDATE,getyyyyMMdd());
		map.put(HFBusiDict.ORDERID, "00000001");
		map.put(HFBusiDict.BANKID, "WY000001");
		map.put(HFBusiDict.STATE, "3");
		map.put(HFBusiDict.TAKETIMES, "1");
		map.put(HFBusiDict.GOODSINFO, "狗运测试");
		
		try {
			Object o = api.doCall("POST", "/hfrestbusi/mermutinotify/123321.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
