package hfrest.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;

import org.junit.Test;

import com.bs2.core.ext.Base64;

/**
 * ****************** 类说明 ********************* class : ChannelCheckCase
 * 
 * @author : panxingwu
 * @version : 1.0 description : 渠道鉴权资源测试类
 * @see :
 * ************************************************/
public class ChannelCheckCase extends Base {
	@Test
	public void testDoShowService() throws Exception {
//		Map<String, String> map = new HashMap<String, String>();
//		String chnlid = "100004";
//		String merid = "9996";
//		String goodsid = "100";
//		map.put(HFBusiDict.CHANNELID, chnlid);
//		map.put(HFBusiDict.MERID, merid);
//		map.put(HFBusiDict.GOODSID, goodsid);
//		map.put("bankid", "xe010000");
//		map.put("amount", "1000000");
//		map.put("pricemode", "1");
//
//		try {
//			Object o = api.doGET("/hfrestbusi/checkChannelRest/321/321.xml",
//					map, Map.class);
//			System.out.println(o);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	//@Test
	public void testDoUpdateService() throws Exception {
//		String chnlid = "100012";
//		String merid = "9996";
//		String goodsid = "100";
//		String unsignstr = "chnlId="+chnlid+"&chnlOrderId=123456&chnlDate=20130314&merId="+merid+"&goodsId="+goodsid+"&goodsInf=test&amount=1000&mobileId=18810528823&chnlPriv=testPriv&expand=testExpand&verion=3.0";
////		String unsignstr="chnlId=100011&chnlOrderId=1364202979349&chnlDate=20130325&merId=9996&goodsId=100&goodsInf=商品信息（渠道自定义）&amount=1000&mobileId=18810528823&chnlPriv=私有信息&expand=拓展信息&verion=3.0";
//		String signstr = sign(unsignstr,"testMer.key.p8");
//		Map<String, String> map = new HashMap<String, String>();
//		System.out.println("签名串为:"+signstr);
//		map.put(HFBusiDict.CHANNELID, chnlid);
//		map.put(HFBusiDict.MERID, merid);
//		map.put(HFBusiDict.GOODSID, goodsid);
//		map.put(HFBusiDict.SIGNSTR, signstr);
//		map.put(HFBusiDict.UNSIGNSTR, unsignstr);
//
//		try {
//			Object o = api.doCall("POST","/hfrestbusi/checkChannelRest/321/321.xml",map, Map.class);
//			System.out.println(o);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@SuppressWarnings("unused")
	public static String sign(String plain, String certName) throws Exception {
		byte[] key = null;
		key = new byte[20480];
		InputStream in = null;
		try {
			in = new java.io.BufferedInputStream(new java.io.FileInputStream(new File("e:\\testMer.key.p8")));
			if (in == null) {
				error("找不到签名证书" + certName);
				return "";
			}
			in.read(key);
		} catch (IOException e) {
			error(e.getMessage());
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					error(e.getMessage());
				}
		}
		if (key == null)
			throw new Exception("无签名证书");
		PrivateKey pk = com.bs.mpsp.util.SignUtil.genPrivateKey(key);
		byte[] signData = com.bs.mpsp.util.SignUtil.sign(pk, plain.getBytes());
		String sign = new String(Base64.encode(signData));
		return sign;
	}

	private static void info(String string) {
		System.out.println(string);
	}
	private static void error(String string) {
		System.out.println(string);
	}
	//iVsxflnJ96vjHallkKdBietR7f3DQtLNYEbFbIE3FAf4U6oTuwbz06UknuMkt35pr7B7t6Wb4qPN+TshXC8iIgUTzUYIz9AzogYO8s1v8y4Atza56Te3x987uJcjXQfK5BafKXB1CB4P1i94yXYJnWX0UXNCKgZ9eDU3vuDemD8=
}
