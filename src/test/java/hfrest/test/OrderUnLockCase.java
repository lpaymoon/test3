package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;

public class OrderUnLockCase extends Base {
	@Test
	public void testDoCreateService() throws Exception{
		Map<String,String> m = new HashMap<String,String>();
		m.put(HFBusiDict.ORDERID, "132512364");
		m.put(HFBusiDict.ORDERDATE, getyyyyMMdd());
		m.put(HFBusiDict.MERID, "9996");
		m.put(HFBusiDict.STATE,"1");
		Object o = api.doCall("POST","/hfrestbusi/orderUndoLock/123.xml", m,m.getClass());
		System.out.println(o);
	}
	@Test 
	public void testDoShowService() throws Exception{
		Map<String,String> m = new HashMap<String,String>();
		m.put(HFBusiDict.ORDERID, "132512364");
		m.put(HFBusiDict.ORDERDATE, getyyyyMMdd());
		m.put(HFBusiDict.MERID, "9996");
		m.put(HFBusiDict.STATE,"2");
		Object o = api.doCall("POST","/hfrestbusi/orderUndoLock/123/123.xml", m,m.getClass());
		System.out.println(o);
	}
}
