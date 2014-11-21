package hfrest.test;


import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;

/**
 * @author panxingwu
 * 风控被拒挽回交易
 */
public class HfrestOreTest extends Base {

	public void testDoCreateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.OPERID, "pan125");
		map.put(HFBusiDict.MOBILEID, "18810527725");
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.PLATDATE,getyyyyMMdd());
		map.put(HFBusiDict.AMOUNT, "100");
		map.put(HFBusiDict.PORDERID, "123456789");
		map.put(HFBusiDict.STATE, "2");
		map.put(HFBusiDict.MODUSER, "panxingwu");
		try {
			Object o = api.doCall("POST", "/hfrestbusi/hfrestore/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
//		map.put(HFBusiDict.STATE, "3");
//		map.put(HFBusiDict.MOBILEID, "18810528823");
//		map.put(HFBusiDict.PLATDATE, "20120705");
//		map.put(HFBusiDict.PORDERID, "123456789");
//		map.put(HFBusiDict.BSTATE, "2");
		
		map.put(HFBusiDict.AMOUNT, "10000");
		map.put(HFBusiDict.STATE, "3");
		map.put(HFBusiDict.MOBILEID, "18810527725");
		map.put(HFBusiDict.PORDERID, "123456789");
		map.put(HFBusiDict.BSTATE, "2");
		try {
			Object o = api.doCall("POST","/hfrestbusi/hfrestore/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
