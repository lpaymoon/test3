package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;

/**
 * @author panxingwu
 * 更新订单表的verifycode
 */
public class HfOrderCommonTest extends Base {
	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.ORDERID, "120410");
		map.put(HFBusiDict.ORDERDATE, getyyyyMMdd());
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.VERIFYCODE, "test");
		try {
			Object o = api.doCall("POST", "/hfrestbusi/hfordercommon/123/123.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
