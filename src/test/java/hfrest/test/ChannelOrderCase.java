package hfrest.test;

import java.util.HashMap;
import java.util.Map;
import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  HfChannelOrderCase
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  渠道订单资源测试类
 * @see        :                        
 * ************************************************/   
public class ChannelOrderCase extends Base {
	public void testDoCreateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.CHANNELORDERID, "T0000001");
		map.put(HFBusiDict.CHANNELID, "100004");
		map.put(HFBusiDict.CHANNELDATE,getyyyyMMdd());
		map.put(HFBusiDict.MOBILEID, "18810528823");
		map.put(HFBusiDict.ORDERID, "D00000002");
		map.put(HFBusiDict.ORDERDATE, getyyyyMMdd());
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.ORDERSTATE, "2");
		map.put(HFBusiDict.AMOUNT, "1000");
		map.put(HFBusiDict.PORDERID, "P000000001");
		map.put(HFBusiDict.VERSION, "3.0");
		map.put(HFBusiDict.CHANNELPRIV, "testPriv");
		map.put(HFBusiDict.CHANNELEXPAND, "testExpan");
		
		try {
			Object o = api.doCall("POST","/hfrestbusi/channelOrder/123.xml",map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//@Test
	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.CHANNELID, "100005");
		map.put(HFBusiDict.CHANNELORDERID, "T0000001");
		map.put(HFBusiDict.CHANNELDATE,"20130314");
		
		Object o;
		try {
			o = api.doGET("/hfrestbusi/channelOrder/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//@Test
	public void testDoUpdateService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.ORDERID, "D00000001");
		map.put(HFBusiDict.ORDERDATE, "20130325");
		map.put(HFBusiDict.ORDERSTATE, "3");
		map.put(HFBusiDict.PLATDATE, "20130325");
		map.put(HFBusiDict.RESERVED, "0000");
		try {
			Object o = api.doCall("POST", "/hfrestbusi/channelOrder/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
