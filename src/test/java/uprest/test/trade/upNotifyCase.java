package uprest.test.trade;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ibm.db2.jcc.sqlj.m;
import com.umpay.hfbusi.HFBusiDict;

public class upNotifyCase extends Base{

	@Test
	public void testDoCreateService() throws Exception {
		String orderid = getRandomNum(15);
		String porderid = getRandomNum(10)+"1";
		Map<String,String> m = new HashMap<String,String>();
//		m.put("platordid","20140925173532999600000000010014");//platordid
		m.put("transeq","7060001409");
		m.put("payretcode","0000");


		Map map = (Map) api.doCall("POST", "/uprestbusi/UpNotifyDealRest/123/7060001409.xml", m, Map.class);
		System.out.println(map);
	}

	
//	@Test
//	public void testDoShowService() throws Exception {
//		m.put(HFBusiDict.MOBILEID, "13671352924");
//		m.put(HFBusiDict.PORDERID, "952021492");
//		Map map = (Map) api.doGET("/hfrestbusi/hforder/common/103/123.xml", m, Map.class);
//		System.out.println(map);
//	}
////    @Test
//	public void testDoUpdateService() throws Exception {
//		m.put(HFBusiDict.PLATDATE, getyyyyMMdd());
//		m.put(HFBusiDict.ORDERSTATE, "2");
//		m.put(HFBusiDict.PORDERID, "952021492");
//		m.put(HFBusiDict.MOBILEID, "13671352924");
//		m.put(HFBusiDict.VERIFYCODE, "不好");
//		Map map = (Map) api.doCall("POST", "/hfrestbusi/hforder/common/M1231608281/123.xml", m, m.class);
//		System.out.println(map);
//	}
}
