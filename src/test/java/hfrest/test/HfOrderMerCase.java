package hfrest.test;
import java.util.Map;
import com.ibm.db2.jcc.sqlj.m;
import com.umpay.hfbusi.HFBusiDict;

public class HfOrderMerCase extends Base{

	public void testDoShowService() throws Exception {
		m.put(HFBusiDict.ORDERID, "120410");
		m.put(HFBusiDict.ORDERDATE,getyyyyMMdd());
		m.put(HFBusiDict.MERID,"9996");
		
		Map map = (Map) api.doGET("/hfrestbusi/hforder/mer/132/465.xml", m, Map.class);
		System.out.println(map);
	}
	
	public void testDoUpdateService() throws Exception {
		m.put(HFBusiDict.ORDERID, "120410");//主键
		m.put(HFBusiDict.ORDERDATE, getyyyyMMdd());//主键
		m.put(HFBusiDict.MERID, "9996");//主键
		m.put(HFBusiDict.ORDERSTATE, "1");
		m.put(HFBusiDict.RPID, "1");
		m.put(HFBusiDict.PLATDATE,getyyyyMMdd());
		m.put(HFBusiDict.RESERVED, "86014321");
		Map map = (Map) api.doCall("POST", "/hfrestbusi/hforder/mer/2/123.xml", m, m.class);
		System.out.println(map);
	}

}
