package hfrest.complex.test;

import hfrest.test.Base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;

/** ******************  类说明  *********************
 * class       :  HfQueryOrderByMonthTest
 * @author     :  LiuJiLong 
 * description :  渠道冲正订单查询测试类
 * @see        :  
 * @version    :  1.0                   
 * ************************************************/   
public class HfQueryOrderByMonthCase extends Base {

	private Map<String, String> sendMap = new HashMap<String, String>();
	private Map<String, String> checkMap = new HashMap<String, String>();

	/** ********************************************
	 * method name   : data 
	 * description   : 准备测试参数,请手动为本年离线库增加一条超出六天前的数据,不然有用例会失败
	 * @return       : Collection
	 * @param        : @return
	 * modified      : LiuJiLong ,  2013-2-19 上午09:46:31
	 * @see          : 
	 * ********************************************/      
	@SuppressWarnings({ "unchecked"})
	public static Collection data() {
		//准备数据
//		Calendar cal = Calendar.getInstance();
//		int year = cal.get(Calendar.YEAR);
//		int month = cal.get(Calendar.MONTH);
//		int date = cal.get(Calendar.DATE);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		cal.set(year, month-1, date);
//		String lastMonth = sdf.format(cal.getTime());
//		String nowDate = sdf.format(new Date());
//		cal.set(year, month, date-1);
//		String yesDate = sdf.format(cal.getTime());
//		cal.set(year, month, date-6);
//		String sixDaysBeforeDate = sdf.format(cal.getTime());
//		cal.set(year, month, date-7);
//		String sixDaysAgoDate = sdf.format(cal.getTime());
//		BaseCase.addOrder("WQuOrd" + TimeUtil.time6(), "todayExist", nowDate, "9996", null, null, null, "XE010000", null, "1000", 
//				null, null, null, null, null, null, null, null, null, null, null); // 为今天增加一条订单
//		BaseCase.addOrder("WQuOrd" + TimeUtil.time6(), "yestodayExist", yesDate, "9996", null, null, null, "XE010000", null, "1000", 
//				null, null, null, null, null, null, null, null, null, null, null); // 为昨天增加一条订单
//		BaseCase.addOrder("WQuOrd" + TimeUtil.time6(), "sixDaysBeforeExist", sixDaysBeforeDate, "9996", null, null, null, "XE010000", null, "1000", 
//				null, null, null, null, null, null, null, null, null, null, null); // 为六天前增加一条订单
////		BaseCase.addOrder("WQuOrd" + TimeUtil.time6(), "sixDayAgoExist", sixDaysAgoDate, "9996", null, null, null, "XE010000", null, "1000", 
////				null, null, null, null, null, null, null, null, null, null, null); // 为超出六天前增加一条订单
//		Object[] beyondMonth = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "beyondMonth", lastMonth, 86801060", "跨月"};
//		Object[] todayNotExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "todayNotExist", nowDate, 86801101", "当天无数据"};
//		Object[] todayExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "todayExist", nowDate, "0000", "当天有数据"};
//		Object[] yestodayNotExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "yestodayNotExist", yesDate, 86801101", "昨天无数据"};
//		Object[] yestodayExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "yestodayExist", yesDate, "0000", "昨天有数据"};
//		Object[] sixDaysBeforeNotExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "sixDaysBeforeNotExist", sixDaysBeforeDate, 86801101", "六天前无数据"};
//		Object[] sixDaysBeforeExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "sixDaysBeforeExist", sixDaysBeforeDate, "0000", "六天前有数据"};
//		Object[] sixDaysAgoNotExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "sixDayAgoNotExist", sixDaysAgoDate, 86801101", "超出六天前无数据"};
//		Object[] sixDaysAgoExist = new Object[]{"WQuOrd" + TimeUtil.time6(), "9996", "sixDayAgoExist", sixDaysAgoDate, "0000", "超出六天前有数据"};
//		return Arrays.asList(new Object[][] {
//				beyondMonth,
//				todayNotExist,
//				todayExist,
//				yestodayNotExist,
//				yestodayExist,
//				sixDaysBeforeNotExist,
//				sixDaysBeforeExist,
//				sixDaysAgoNotExist,
//				sixDaysAgoExist,
//				});
//	}
//
//	public HfQueryOrderByMonthCase(String rpid, String merId, String orderId,
//			String orderDate, String retCode, String testName) {
//		sendMap.put(HFBusiDict.RPID, rpid);
//		sendMap.put(HFBusiDict.REQDATE, TimeUtil.date8());
//		sendMap.put(HFBusiDict.REQTIME, TimeUtil.time6());
//		sendMap.put(HFBusiDict.MERID, merId);
//		sendMap.put(HFBusiDict.ORDERID, orderId);
//		sendMap.put(HFBusiDict.ORDERDATE, orderDate);
//		checkMap.put(HFBusiDict.RETCODE, retCode);
//		checkMap.put("testName", testName);
		return null;
	}

	public void testBeyandMonth() {
//		try {
//			Map re = (Map) api.doGET("/hfrestbusi/hfQueryOrderByMonthRest/"
//					+ sendMap.get(HFBusiDict.RPID) + "/"
//					+ sendMap.get(HFBusiDict.ORDERID) + "-"
//					+ sendMap.get(HFBusiDict.ORDERDATE) + "-"
//					+ sendMap.get(HFBusiDict.MERID) + ".xml", sendMap,
//					Map.class);
//			System.out.println(checkMap.get("testName")+ ": " + re);
//			String retCode = re.get(HFBusiDict.RETCODE).toString();
//			assertEquals(checkMap.get(HFBusiDict.RETCODE).toString(), retCode);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
