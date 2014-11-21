package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

/** ******************  类说明  *********************
 * class       :  
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  
 * @see        :  
 * @date：2014-7-7 下午04:31:21                      
 * ************************************************/
public class HfCheckPayRuleCase extends Base {

	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID,"9996");
		map.put(HFBusiDict.GOODSID,"100");
		map.put(HFBusiDict.MOBILEID,"18710019400");
		map.put(HFBusiDict.XESEQ,"xeseq");
		map.put(HFBusiDict.BANKID,"MW021000");
		map.put(HFBusiDict.AMOUNT, "100");
		map.put(HFBusiDict.ISNEW, "1");//0新增 1续费
		
		try {
			Object o = api.doGET("/hfrestbusi/checktrans/payrule/123/123.xml",map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

