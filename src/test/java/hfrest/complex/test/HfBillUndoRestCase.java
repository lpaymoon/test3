package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;

public class HfBillUndoRestCase  extends Base {
//RPID,TRANSRPID,FUNCODE,PLATDATE
	@Test
	public void testDoUpdateService() {
		Map<String,String> map =  new HashMap<String,String>();
		map.put(HFBusiDict.TRANSRPID,"M1051091051091");
		map.put(HFBusiDict.FUNCODE,"P100");
		map.put(HFBusiDict.PLATDATE,getyyyyMMdd());
		try {
			Object o = api.doCall("POST", "/hfrestbusi/HfBillUndoRest/320780001/123.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}