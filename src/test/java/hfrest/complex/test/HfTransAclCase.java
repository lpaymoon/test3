package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  HfTransAclCase
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  交易屏蔽验证
 * @see        :                        
 * ************************************************/   
public class HfTransAclCase extends Base{

	public void testDoShowService() throws Exception {
		Map<String,String> map = new HashMap<String,String>();

		//必输
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
		map.put(HFBusiDict.MOBILEID, "18312340000");
		map.put(HFBusiDict.BANKID, "MW010000");
		//可选
		map.put(HFBusiDict.ISCONTROL, "");
		map.put(HFBusiDict.PROVCODE, "");
		map.put(HFBusiDict.AREACODE, "");
		map.put(HFBusiDict.NETTYPE, "");
		map.put(HFBusiDict.CARDTYPE, "");
		map.put(HFBusiDict.GRADE, "1");
		
		Map m = (Map) api.doGET("/hfrestbusi/transacl/M132/123.xml", map, Map.class);
		System.out.println(m);
	}

}
