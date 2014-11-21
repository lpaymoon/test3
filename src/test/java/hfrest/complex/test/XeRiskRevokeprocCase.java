package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  XeTransRevokeprocCase
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  小额风控回滚
 * @see        :                        
 * ************************************************/   
public class XeRiskRevokeprocCase extends Base {

	public void testDoUpdateService() {
		Map<String,String> map =  new HashMap<String,String>();
		map.put(HFBusiDict.CALLING,"18810528823");
		map.put(HFBusiDict.MERID,"9996");
		map.put(HFBusiDict.GOODSID,"100");
		map.put(HFBusiDict.PROVCODE,"010");
		map.put(HFBusiDict.AMT,"100");
		map.put(HFBusiDict.MOBILEID,"18810528823");
		map.put(HFBusiDict.ISCONTROL,"1");
		map.put("doltd", "true");
		map.put("bankid", "MW10004");
		
		try {
			Object o = api.doCall("POST", "/hfrestbusi/xerisk/revokeproc/321/123.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
