package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  HfCheckPayCase
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  支付鉴权
 * @see        :                        
 * ************************************************/   
public class HfCheckPayCase extends Base {

	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MERID,"9996");
		map.put(HFBusiDict.GOODSID,"100");
		map.put(HFBusiDict.MOBILEID,"13810174448");
		map.put(HFBusiDict.XESEQ,"xeseq");
		map.put(HFBusiDict.BANKID,"MW010000");
		map.put(HFBusiDict.AMOUNT, "1000");
		map.put(HFBusiDict.ISNEW, "1");//0新增 1续费
		
		try {
			Object o = api.doGET("/hfrestbusi/checktrans/pay/123/123.xml",map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
