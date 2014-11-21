package hfrest.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.util.HTTPSendUtil;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  ******************
 * class       :  BSTest
 * date        :  2014-9-25 
 * @author     :  Roy
 * @version    :  V1.0  
 * description :  博升下单支付测试类
 * @see        :                         
 * ***********************************************/
public class BSTest extends Base{
	public static void main(String[] args) {
		try {
			bsPayTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** *****************  方法说明  *****************
	 * method name   :  bsPayTest
	 * @param		 :  @throws Exception
	 * @return		 :  void
	 * @author       :  Roy 2014-9-25 上午12:27:55
	 * description   :  整体支付流程测试
	 * @see          :  
	 * ***********************************************/
	public static void bsPayTest() throws Exception{
		Map<String, Object> map = testMMSms();
//		System.out.println(map.get("apco"));
//		System.out.println(map.get("aptid"));
//		System.out.println(map.get("aptrid"));
//		System.out.println(map.get("bu"));
//		System.out.println(map.get("ch"));
//		System.out.println(map.get("ex"));
//		System.out.println(map.get("inner_id"));
//		System.out.println(map.get("inputMobile"));
//		System.out.println(map.get("random"));
//		System.out.println(map.get("rpid"));
//		System.out.println(map.get("MMURL"));
		
		Map<String, String> argsMap = new HashMap<String,  String>();
		argsMap.put("apco",map.get("apco").toString());
		argsMap.put("aptid",map.get("aptid").toString());
		argsMap.put("aptrid",map.get("aptrid").toString());
		argsMap.put("bu",map.get("bu").toString());
		argsMap.put("ch",map.get("ch").toString());
		argsMap.put("ex",map.get("ex").toString());
		argsMap.put("inner_id",map.get("inner_id").toString());
		argsMap.put("mobileid",map.get("inputMobile").toString());
		argsMap.put("random",map.get("random").toString());
		argsMap.put("rpid",map.get("rpid").toString());
		argsMap.put("MMURL",map.get("MMURL").toString());
		
		BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("请输入一个支付验证码:");
		String inputCode = strin.readLine();//在控制台输入从MM收到的支付验证码
		argsMap.put(HFBusiDict.VERIFYCODE, StringUtil.trim(inputCode));
		testBSPay(argsMap);//请求支付
	}
	
	/** *****************  方法说明  *****************
	 * method name   :  testMMSms
	 * @param		 :  @return
	 * @param		 :  @throws Exception
	 * @return		 :  Map<String,Object>
	 * @author       :  Roy 2014-9-25 上午12:26:46
	 * description   :  向MM请求短信验证码下发
	 * @see          :  
	 * ***********************************************/
	public static Map<String, Object> testMMSms() throws Exception{
		Map<String, String> argsMap = new HashMap<String,  String>();
		/*
		 * 初始参数设置
		 */
		argsMap.put(HFBusiDict.ISGREEN, "0");
		argsMap.put(HFBusiDict.CLIENTTYPE, "wap");
		argsMap.put(HFBusiDict.ACCOUNTID, "2273");
		argsMap.put(HFBusiDict.MERID, "9996");
		argsMap.put(HFBusiDict.ORDERID, "8372175");
		argsMap.put(HFBusiDict.GOODSID, "100");
		argsMap.put(HFBusiDict.MOBILEID, "15017568581");
		argsMap.put("merdate", "20140924");
		argsMap.put(HFBusiDict.AMOUNT, "100");
		argsMap.put(HFBusiDict.BUSINESSTYPE, "0601");
		argsMap.put(HFBusiDict.PLATORDID, "20140925165813999600000000006014");
		argsMap.put(HFBusiDict.BANKID, "XE010000");
		
//		Object rs = HTTPSendUtil.getHttpResPost_Form("http://10.10.38.214:8690/uprestbusi/branch/0601/frdfjltest/bs.xml", argsMap);
		Object rs = HTTPSendUtil.getHttpResPost_Form("http://10.10.38.214:8690/uprestbusi/UpPayRest/frdf87942234/bs.xml", argsMap);
//		Object rs = HTTPSendUtil.getHttpResPost_Form("http://127.0.0.1:8690/hfrestbusi/HfChannelGoodInfRest/ll/bs.xml", argsMap);
//		Map rs = (Map) api.doCall("POST", "/hfrestbusi/HfChannelGoodInfRest/ll/bs.xml", argsMap, Map.class);
		System.out.println(rs);                                                                        //  /uprestbusi/rest/bscheckcode/
		Map<String, Object> map = (Map<String, Object>) rs;
		return map;
	}

	private static void testBSPay(Map<String, String> argsMap ){
		Object rs = HTTPSendUtil.getHttpResPost_Form("http://10.10.38.214:8690/uprestbusi/branch/bspay/frdfjltest/bs.xml", argsMap);
		System.out.println(rs);          
	}
}
