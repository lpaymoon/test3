package branchpay;

import hfrest.test.Base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.util.HTTPSendUtil;

public class YXJDTest extends Base{
	public static void main(String[] args) {
		//testSessionId();
		String id = testVerifycode();
		try {
			testVerifycodeConfirm(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void testVerifycodeConfirm(String id) throws IOException {
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(HFBusiDict.BANKTRACE, id);
		argsMap.put(HFBusiDict.MERID, "9996");
		argsMap.put(HFBusiDict.GOODSID, "100");
//		String merId = StringUtil.trim(urlargs.get(HFBusiDict.MERID));
//		String tranSeq = StringUtil.trim(urlargs.get(HFBusiDict.TRANSEQ));
//		String goodsId = StringUtil.trim(urlargs.get(HFBusiDict.GOODSID));
//		String verifycode = StringUtil.trim(urlargs.get(HFBusiDict.VERIFYCODE));
		BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("请输入一个支付验证码:");
		String inputCode = strin.readLine();//在控制台输入从MM收到的支付验证码
		argsMap.put(HFBusiDict.VERIFYCODE, inputCode);
		Map rs = (Map) HTTPSendUtil.getHttpResPost_Form("http://10.10.38.214:8690/uprestbusi/branch/0620/" + getRandomNum(16) + ".xml", argsMap);
		System.out.println(rs);
	}

	private static String testVerifycode() {
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(HFBusiDict.TRANSDATE, "20141008");
		argsMap.put(HFBusiDict.MERID, "9996");
		argsMap.put(HFBusiDict.GOODSID, "100");
		argsMap.put(HFBusiDict.MOBILEID, "13647813416");
		Map rs = (Map) HTTPSendUtil.getHttpResPost_Form("http://10.10.38.214:8690/uprestbusi/branch/0620/" + getRandomNum(16) + "/yxjd.xml", argsMap);
		System.out.println(rs);
		String id =(String) rs.get(HFBusiDict.BANKTRACE);
		return id;
	}

	private static void testSessionId() {
		Object rs = HTTPSendUtil.getHttpResGet_JSON("http://10.10.38.214:8690/uprestbusi/branch/0620/" + getRandomNum(16) + "/yxjd.xml", new HashMap<String, String>());
		System.out.println(rs);          
	}
	
}
