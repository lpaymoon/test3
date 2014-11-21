package hfrest.test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.ibm.db2.jcc.sqlj.m;
import com.umpay.hfrestbusi.util.TimeUtil;
import com.umpay.hfbusi.HFBusiDict;

public class HfInfoMerFaillTest extends Base{

	@Test
	public void testDoCreateService() throws Exception {
	//	HfMerInformFailRest.DOCREATE=RPID,PLATDATE,MOBILEID,MERID,GOODSID,ORDERDATE,ORDERID,BANKID,STATE,TAKETIMES,[BANKCHECKDATE],VERSION,AMOUNT,[MERPRIV]
		String rpid="M123"+TimeUtil.time6() + "1234";
		m.put(HFBusiDict.RPID, rpid);
		m.put(HFBusiDict.PLATDATE, TimeUtil.date8());
		m.put(HFBusiDict.MOBILEID, "15810678155");
		m.put(HFBusiDict.MERID, "9996");
		m.put(HFBusiDict.GOODSID, "100");
		m.put(HFBusiDict.ORDERID, TimeUtil.time6());
		m.put(HFBusiDict.ORDERDATE, TimeUtil.date8());	
		m.put(HFBusiDict.BANKID, "XE010000");
		m.put(HFBusiDict.STATE, "2");
		m.put(HFBusiDict.TAKETIMES, "1");
		m.put(HFBusiDict.MERPRIV,"test");
		m.put(HFBusiDict.AMOUNT,"1000");
		m.put(HFBusiDict.VERSION,"3.0");
		m.put(HFBusiDict.BANKCHECKDATE,getyyyyMMdd());


		
		Map map = (Map) api.doCall("POST", "/hfrestbusi/hfinformfailRest/"+rpid+".xml", m, Map.class);
		System.out.println(map);
	}

	//@Test
	public void testDoShowService() throws Exception {
		m.put(HFBusiDict.MOBILEID, "13671352924");
		m.put(HFBusiDict.PORDERID, "952021492");
		Map map = (Map) api.doGET("/hfrestbusi/hforder/common/103/123.xml", m, Map.class);
		System.out.println(map);
	}
    //@Test
	public void testDoUpdateService() throws Exception {
		m.put(HFBusiDict.PLATDATE, "20120724");
		m.put(HFBusiDict.ORDERSTATE, "2");
		m.put(HFBusiDict.PORDERID, "952021492");
		m.put(HFBusiDict.MOBILEID, "13671352924");
		m.put(HFBusiDict.VERIFYCODE, "不好");
		Map map = (Map) api.doCall("POST", "/hfrestbusi/hforder/common/M1231608281/123.xml", m, m.class);
		System.out.println(map);
	}
	
}
