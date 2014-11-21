package hfrest.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import com.bs3.app.dal.DalApi4Inf;
import com.bs3.app.dal.client.DalClientFactory;
import com.bs3.app.dal.engine.BeansServer;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;

@SuppressWarnings("deprecation")
public class BaseCase extends TestCase{
	protected static DalApi4Inf api = null;
	Map<String, String> m = new HashMap<String, String>();
	static {
		try {
//			BeansServer.main_start("beans.properties");
//			NamedProperties.register(BusiConst.SYSPARAMS, "system.properties");
//			NamedProperties.register(BusiConst.MESSAGEPARAMS,
//					"message.properties");
//			NamedProperties.register(BusiConst.REQVALIDPARAMS,
//					"reqvalid.properties");
//			NamedProperties.refresh(false);
////			api = DalClientFactory.newLocalApi2(null, null, null, null,null);
//			
//			api = DalClientFactory.newRemoteApi("cm20://127.0.0.1:8620/", null, 1, 5);
//			api = DalClientFactory.newRemoteApi("cm20://10.10.1.166:8620/", null, 1, 5);
//			api = DalClientFactory.newRemoteApi("cm20://10.10.1.171:8620/", null, 1, 5);
//			api = DalClientFactory.newRemoteApi("cm20://10.10.1.172:8620/", null, 1, 5);
			api = DalClientFactory.newRemoteApi("cm20://10.10.38.214:8620/", null, 1, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	enum TESTTYPE {SHOW, LIST, CREATE, UPDATE, MUL};
	/** ********************************************
	 * method name   : main 
	 * description   : TODO
	 * @return       : void
	 * @param        : @param args
	 * modified      : LiuJiLong ,  2013-4-28 上午09:23:37
	 * @see          : 
	 * ********************************************/      
	public static void main(String[] args) {
		int i = 104%4;
		System.out.println(i);
		System.out.println(TESTTYPE.values()[i]);
	}
	
	/** ********************************************
	 * method name   : addOrder 
	 * description   : 增加一条订单信息
	 * @return       : void
	 * @param        : @param rpid
	 * @param        : @param orderId
	 * @param        : @param orderDate
	 * @param        : @param merId
	 * @param        : @param goodsId
	 * @param        : @param mobileId
	 * @param        : @param expiretime
	 * @param        : @param bankId
	 * @param        : @param accessType
	 * @param        : @param amount
	 * @param        : @param verifyCode
	 * @param        : @param merCustId
	 * @param        : @param merPriv
	 * @param        : @param notifyURL
	 * @param        : @param expand
	 * @param        : @param AMTType
	 * @param        : @param origAmount
	 * @param        : @param orpid
	 * @param        : @param version
	 * @param        : @param sign
	 * @param        : @param reserved
	 * modified      : LiuJiLong ,  2013-2-18 下午05:28:56
	 * @see          : 
	 * ********************************************/      
	@SuppressWarnings({ "unchecked" })
	protected static void addOrder(String rpid, String orderId, String orderDate,
			String merId, String goodsId, String mobileId, String expiretime,
			String bankId, String accessType, String amount, String verifyCode,
			String merCustId, String merPriv, String notifyURL, String expand,
			String AMTType, String origAmount, String orpid, String version,
			String sign, String reserved) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(HFBusiDict.ORDERID, orderId==null?"000000":orderId);// 主键
//		map.put(HFBusiDict.ORDERDATE, orderDate==null?TimeUtil.date8():orderDate);// 主键
		map.put(HFBusiDict.MERID, merId==null?"9996":merId);// 主键
		map.put(HFBusiDict.GOODSID, goodsId==null?"001":goodsId);
		map.put(HFBusiDict.MOBILEID, mobileId==null?"15011385231":mobileId);
		map.put(HFBusiDict.EXPIRETIME, expiretime==null?"2013-11-09 16:23:55.312":expiretime);
		map.put(HFBusiDict.BANKID, bankId==null?"XE010000":bankId);
		map.put(HFBusiDict.ACCESSTYPE, accessType==null?"M":accessType);
		map.put(HFBusiDict.AMOUNT, amount==null?"100":amount);
		map.put(HFBusiDict.VERIFYCODE, verifyCode==null?"8":verifyCode);
		map.put(HFBusiDict.MERCUSTID, merCustId==null?"45":merCustId);
		map.put(HFBusiDict.MERPRIV, merPriv==null?"test":merPriv);
		map.put(HFBusiDict.NOTIFYURL, notifyURL==null?"test":notifyURL);
		map.put(HFBusiDict.EXPAND, expand==null?"test":expand);
		map.put(HFBusiDict.AMTTYPE, AMTType==null?"03":AMTType);
		map.put(HFBusiDict.ORIGAMOUNT, origAmount==null?"1000":origAmount);
		map.put(HFBusiDict.ORPID, orpid==null?"123321":orpid);
		map.put(HFBusiDict.VERSION, version==null?"3.0":version);
		map.put(HFBusiDict.SIGN, sign==null?"testsign":sign);
		map.put(HFBusiDict.RESERVED, reserved==null?"testreserved":reserved);

		try {
			Map rs = (Map) api.doCall("POST","/hfrestbusi/hforder/common/"+rpid+".xml", map, Map.class);
			String retCode = rs.get(HFBusiDict.RETCODE).toString();
			if("0000".equals(retCode)){
				System.out.println("订单增加成功: " + rs);
			}else{
				System.out.println("订单增加失败: " + rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static String getRandomStr(int length) {
		String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);// 0~61
			sf.append(str.charAt(number));
		}
		return sf.toString();
	}
	protected static String getRandomNum(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer sf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(10);// 0~61
			sf.append(str.charAt(number));
		}
		return sf.toString();
	}
	public static String getyyyyMMdd(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		return format.format(date);
	}
	public String getLastDate(){
		 SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		 Calendar date = Calendar.getInstance();
		 date.add(Calendar.MONTH,-1);
		 return format.format(date.getTime());
	}
}
