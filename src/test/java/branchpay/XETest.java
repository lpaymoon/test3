package branchpay;

import hfrest.test.Base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.util.HTTPSendUtil;

public class XETest extends Base{
	public static void main(String[] args) {
		//testSessionId();
		String id = testVerifycode();
		try {
			testVerifycodeConfirm(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testVerifycodeConfirm(String id) throws IOException {
		//merId,goodsId,orderId,merDate,amount,verifyCode,mobileId,[merPriv],[expand]
		Map<String, String> argsMap = new HashMap<String, String>();
//		argsMap.put(HFBusiDict.BANKTRACE, id);
		argsMap.put(HFBusiDict.TRANSDATE, "20141010");
		argsMap.put(HFBusiDict.MERDATE, "20141010");
		argsMap.put(HFBusiDict.MERID, "9996");
		argsMap.put(HFBusiDict.GOODSID, "100");
		argsMap.put(HFBusiDict.MOBILEID, "15011385231");
		argsMap.put(HFBusiDict.ORDERID, id);
		argsMap.put(HFBusiDict.AMOUNT, "100");
		argsMap.put("merPriv", "100");
		argsMap.put("expand", "100");
//		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
//		String tranSeq = StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ));
//		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
//		String verifycode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));
		BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("请输入一个支付验证码:");//818262
		String inputCode = strin.readLine();//在控制台输入从MM收到的支付验证码
		argsMap.put(HFBusiDict.VERIFYCODE, inputCode);
		Map rs = (Map) HTTPSendUtil.getHttpResPost_Form("http://10.10.38.214:8690/uprestbusi/branch/0612/" + getRandomNum(16) + ".xml", argsMap);
		System.out.println(rs);
	}

	private static String testVerifycode() {		
		//merDate,merId,orderId,mobileId,goodsId,amount
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(HFBusiDict.TRANSDATE, "20141010");
		argsMap.put(HFBusiDict.MERDATE, "20141010");
		argsMap.put(HFBusiDict.MERID, "9996");
		argsMap.put(HFBusiDict.GOODSID, "100");
		argsMap.put(HFBusiDict.MOBILEID, "15011385231");
		argsMap.put(HFBusiDict.ORDERID, "13647813423216");
		argsMap.put(HFBusiDict.AMOUNT, "100");
		argsMap.put(HFBusiDict.VERSION, "1.0");
		Map rs = (Map) HTTPSendUtil.getHttpResPost_Form("http://10.10.38.214:8690/uprestbusi/branch/0612/" + getRandomNum(16) + "/yxjd.xml", argsMap);
		System.out.println(rs);// http://10.10.38.214:8602/hfwebbusi/pay/wxOrder.do
		String id =(String) rs.get(HFBusiDict.TRANSEQ);
		return id;
	}

	
}
