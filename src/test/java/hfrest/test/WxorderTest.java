package hfrest.test;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;
public class WxorderTest extends Base {
	public void testDoCreateService() {
		String orderid = getRandomNum(15);
		Map<String,String> map = new HashMap<String,String>();
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		long future = now + 60*60*1000;
		map.put(HFBusiDict.ORDERID, orderid);
		map.put(HFBusiDict.ORDERDATE, getyyyyMMdd());
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.EXPIRETIME, new Timestamp(future).toString());
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.AMOUNT, "1000");
		map.put(HFBusiDict.VERSION, "1.0");
		map.put(HFBusiDict.MERPRIV, "123");
		map.put(HFBusiDict.EXPAND, "123");
		map.put(HFBusiDict.RPID, "123");
		map.put(HFBusiDict.IMEI, "12312312312");
		map.put(HFBusiDict.CLIENTVERSION, "2");
		System.out.println("orderid="+orderid);
		try {
			Object o = api.doCall("POST", "/hfrestbusi/wxorder/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public void testDoShowService() {
//		Map<String,String> map = new HashMap<String,String>();
//		map.put(HFBusiDict.ORDERID, "2012081616");
//		map.put(HFBusiDict.ORDERDATE, "20131106");
//		map.put(HFBusiDict.MERID, "9996");
//		try {
//			Object o = api.doCall("GET", "/hfrestbusi/wxorder/123/123.xml", map, Map.class);
//			System.out.println(o);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public void testDoUpdateService() {
//		Map<String,String> map = new HashMap<String,String>();
//		map.put(HFBusiDict.ORDERID, "1000000");
//		map.put(HFBusiDict.ORDERDATE, "20120803");
//		map.put(HFBusiDict.MERID, "9996");
//		map.put(HFBusiDict.ORDERSTATE, "1");
//		try {
//			Object o = api.doCall("POST", "/hfrestbusi/wxorder/123/123.xml", map, Map.class);
//			System.out.println(o);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
