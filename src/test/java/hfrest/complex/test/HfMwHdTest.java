package hfrest.complex.test;

import hfrest.test.Base;

import java.util.*;

import com.umpay.hfbusi.HFBusiDict;

public class HfMwHdTest extends Base {
	public void testDoUpdate(){
		Map<String,String> map = new HashMap<String,String>();
		
		map.put(HFBusiDict.BANKID, "MW010000");
		map.put(HFBusiDict.MOBILEID, "18810528823");
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.DELAYSEND, "1");
		map.put(HFBusiDict.PROVCODE, "010");
		map.put(HFBusiDict.AREACODE, "010");
		map.put(HFBusiDict.AMT, "1000");
		map.put(HFBusiDict.ORDERID, "22");
		map.put(HFBusiDict.REQDATE, getyyyyMMdd());
		map.put(HFBusiDict.REQTIME, "212212");
		map.put(HFBusiDict.MSGFEETYPE, "3");
		map.put(HFBusiDict.BUSITYPE, "13");
		map.put(HFBusiDict.TRANSFLAG, "0");
		
		map.put(HFBusiDict.ISCONTROL, "010");
		map.put(HFBusiDict.CURSIGN, "0");
		map.put(HFBusiDict.OPENSIG, "0");
		map.put(HFBusiDict.CANCELSIG, "3");
		map.put(HFBusiDict.FEEAMOUNT, "1");
		map.put(HFBusiDict.BUSIROLLTYPE, "10");
		
		try {
			Object o = api.doCall("POST", "hfrestbusi/mwhd/123/123.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
