package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;

public class XeTransCase extends Base{
	public void testDoCreateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.PLATDATE,getyyyyMMdd());//
		map.put(HFBusiDict.FUNCODE,"P100");//
		map.put(HFBusiDict.MOBILEID,"15810678155");//
		map.put(HFBusiDict.TRANSEQ,"123");//
		map.put(HFBusiDict.PLATTIME,"110621");//
		map.put(HFBusiDict.MERID,"9996");
		map.put(HFBusiDict.GOODSID,"100");
		map.put(HFBusiDict.AMT,"100");
		map.put(HFBusiDict.BANKID,"MW100000");
		map.put(HFBusiDict.AREACODE,"010");
		map.put(HFBusiDict.TRANSTATE,"0");
		map.put(HFBusiDict.BANKTRACE,"123");
		map.put(HFBusiDict.ISNEW,"1");
		map.put(HFBusiDict.BRANCHID, "brachid2");
//		map.put(HFBusiDict.ORDERID,"123");
//		map.put(HFBusiDict.ORDERDATE,"20120220");
//		map.put(HFBusiDict.CARDTYPE,"1");
//		map.put(HFBusiDict.MERCUSTID,"123");
//		map.put(HFBusiDict.BANKCHECKDATE,"20120219");
//		map.put(HFBusiDict.MERCHECKDATE,"20122019");
		map.put(HFBusiDict.RESERVED,"brachid2");
		Object o;
		try {
			o = api.doCall("POST","/hfrestbusi/xetrans/common/555.xml",map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//@Test
	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.PLATDATE, "20120809");
		map.put(HFBusiDict.FUNCODE,"P100");
		map.put(HFBusiDict.TRANSRPID, "M2010037716243");
		try {
			Object o = api.doGET("hfrestbusi/xetrans/common/444/132.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.PLATDATE,"20120803");
		map.put(HFBusiDict.RESERVED, "0300");
		map.put(HFBusiDict.FUNCODE,"P100");
		map.put(HFBusiDict.TRANSTATE,"1");
		map.put(HFBusiDict.TRANSRPID,"555");
		try {
			Object o = api.doCall("POST","hfrestbusi/xetrans/common/444/132.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
