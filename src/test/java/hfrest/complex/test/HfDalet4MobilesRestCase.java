package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;

public class HfDalet4MobilesRestCase extends Base {

	@Test
	public void testDoUpdateService() {
		//Map<String, String> map = new HashMap<String, String>();
		// map.put(HFBusiDict.BUSITYPE, "1");
		// map.put(HFBusiDict.PROVCODE, "010");
		// map.put(HFBusiDict.AREACODE, "010");
		// map.put(HFBusiDict.MERID, "9996");
		// map.put(HFBusiDict.GOODSID, "100");
		// map.put(HFBusiDict.ISCONTROL, "010");
		// map.put(HFBusiDict.BUSIROLLTYPE, "1");
	//	map.put(HFBusiDict.ISCONTROL, "false");
		//map.put(HFBusiDict.CONTENT, "15011385231");
		// map.put(HFBusiDict.AMT, "100");
		// map.put(HFBusiDict.FEEAMOUNT, "100");
		// map.put("USERTYPE", "-1");
		// map.put(HFBusiDict.BANKID,"12345");
		// map.put("doltd", "true");
		try {

			// Object o1 = api.doCall("POST", "/hfrestbusi/bwlist/132.xml", map,
			// Map.class);
			// Object o2 = api.doGET("/hfrestbusi/bwlist/132.xml", map,
			// Map.class);
			// Object o3 = api.doCall("POST",
			// "/hfrestbusi/bwlist/132/15011385231.xml", map,Map.class);
			// Map<String, String> map2 = new HashMap<String, String>();
			// map2.put(HFBusiDict.ISCONTROL, "false");
			// map2.put(HFBusiDict.MOBILEID, "15011385231");
			// Object o4 = api.doGET("/hfrestbusi/bwlist/132/15011385231.xml",
			// map2, Map.class);
			Map<String, String> map3 = new HashMap<String, String>();
//			map3.put(HFBusiDict.ISCONTROL, "1");
//			map3.put(HFBusiDict.MOBILEID, "13466860999");
//			map3.put(HFBusiDict.CONTENT, "13466860999");
			Map<String, String> map4 = new HashMap<String, String>();
			map4.put(HFBusiDict.ISCONTROL, "1");
			map4.put(HFBusiDict.MOBILEID, "13466860999");
			map4.put(HFBusiDict.CONTENT, "13466860999");
			 Object o5 = api.doCall("POST", "/hfrestbusi/wlist/baimingdan.xml", map3,
			 Map.class);
			//Object o4 = api.doCall("POST",
			//		"/hfrestbusi/wlist/132/13466860999.xml", map3, Map.class);
//			Object o5 = api.doGET("/hfrestbusi/wlist/132.xml", map4, Map.class);
			// Object o5 = api.doGET("/hfrestbusi/bwlist/132/13466860999.xml",
			// map3, Map.class);
			// Object o5 = api.doCall("POST",
			// "/hfrestbusi/FullGCRest/132/13466860999.xml", map3, Map.class);//
			// FULL GC

			// System.out.println(o1);
			// System.out.println(o2);
			// /System.out.println(o3);
			//System.out.println(o4);
			System.out.println(o5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		//while (true) {
////			try {
////				Thread.sleep(10000);
////			} catch (InterruptedException e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			}
//			String rpid = getRpid();
//			String mobileId = "15011385231";
//			int i = 0;
//			i++;
//			Map<String, Object> rs = null;
//			try {
//				// rs = (Map<String, Object>) api.doGET("/hfrestbusi/wlist/" +
//				// rpid + "/"
//				// + mobileId + ".xml", getSelectMap(mobileId), Map.class);
//				rs = (Map<String, Object>) api.doCall("POST",
//						"/hfrestbusi/wlist/baimingdan.xml", getEmptyMap(),
//						Map.class);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(rs);
//			api.close();
//		//}
//	}
//	public void doAllTest() throws Exception {
//		try {
//
//			String mobileId = "15011385231";
//			int i = 0;
//			Map<String, Object> rs = null;
//			String rpid = null;
//			// 1.查询手机号
//			rpid = getRpid();
//			i++;
//			rs = (Map<String, Object>) api.doGET("/hfrestbusi/wlist/" + rpid
//					+ "/" + mobileId + ".xml", getSelectMap(mobileId),
//					Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败    	rpid["
//					+ rpid + "]";
//			assert (rs.get(HFBusiDict.ISCONTROL).toString()
//					.equalsIgnoreCase("false")) : "第" + i
//					+ "步查询结果应该为FALSE	rpid[" + rpid + "]";
//			rs.clear();
//			// 2.修改手机号
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doCall("POST", "/hfrestbusi/wlist/"
//					+ rpid + "/" + mobileId + ".xml", getUpdateMap(mobileId,
//					"1"), Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败		rpid["
//					+ rpid + "]";
//			rs.clear();
//			// 3.查询手机号
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doGET("/hfrestbusi/wlist/" + rpid
//					+ "/" + mobileId + ".xml", getSelectMap(mobileId),
//					Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败	    rpid["
//					+ rpid + "]";
//			assert (rs.get(HFBusiDict.ISCONTROL).toString()
//					.equalsIgnoreCase("TRUE")) : "第" + i
//					+ "步查询结果应该为TRUE	    rpid[" + rpid + "]";
//			rs.clear();
//			// 4.主动加载
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doCall("POST", "/hfrestbusi/wlist/"
//					+ rpid + ".xml", getEmptyMap(), Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败	    rpid["
//					+ rpid + "]";
//			rs.clear();
//			// 5.查询手机号
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doGET("/hfrestbusi/wlist/" + rpid
//					+ "/" + mobileId + ".xml", getSelectMap(mobileId),
//					Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败	    rpid["
//					+ rpid + "]";
//			assert (rs.get(HFBusiDict.ISCONTROL).toString()
//					.equalsIgnoreCase("false")) : "第" + i
//					+ "步查询结果应该为FALSE	    rpid[" + rpid + "]";
//			rs.clear();
//			// 6.修改手机号
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doCall("POST", "/hfrestbusi/wlist/"
//					+ rpid + "/" + mobileId + ".xml", getUpdateMap(mobileId,
//					"1"), Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败	    rpid["
//					+ rpid + "]";
//			rs.clear();
//			// 7.序列化手机号
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doGET("/hfrestbusi/wlist/" + rpid
//					+ ".xml", getEmptyMap(), Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败	    rpid["
//					+ rpid + "]";
//			rs.clear();
//			// 8.主动加载
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doCall("POST", "/hfrestbusi/wlist/"
//					+ rpid + ".xml", getEmptyMap(), Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败	    rpid["
//					+ rpid + "]";
//			rs.clear();
//			// 9.查询手机号
//			i++;
//			rpid = getRpid();
//			rs = (Map<String, Object>) api.doGET("/hfrestbusi/wlist/" + rpid
//					+ "/" + mobileId + ".xml", getSelectMap(mobileId),
//					Map.class);
//			assert (rs.get(HFBusiDict.RETCODE).toString()
//					.equalsIgnoreCase("0000")) : "第" + i + "步操作失败	    rpid["
//					+ rpid + "]";
//			assert (rs.get(HFBusiDict.ISCONTROL).toString()
//					.equalsIgnoreCase("true")) : "第" + i
//					+ "步查询结果应该为TRUE	    rpid[" + rpid + "]";
//			rs.clear();
//		} finally {
//			api.close();
//		}
//	}

	private static String getRpid() {
		return "RTESTbyLJL" + getRandomStr(5);
	}

	private static Map<String, String> getEmptyMap() {
		return new HashMap<String, String>();
	}

	private Map<String, String> getUpdateMap(String mobileId, String string) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.CONTENT, mobileId);
		map.put(HFBusiDict.ISCONTROL, string);
		return map;
	}

	private static Map<String, String> getSelectMap(String mobileId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.MOBILEID, mobileId);
		return map;
	}
}
