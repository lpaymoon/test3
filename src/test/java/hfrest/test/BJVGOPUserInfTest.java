/**
 * 
 */
package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

/** ******************  类说明  *********************
 * class       :  BJVGOPUserInfTest
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class BJVGOPUserInfTest extends BaseCase {
	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.MOBILEID, "18710019400");
		try {
			Object o = api.doGET("/hfrestbusi/bjVGOPUserInfRest/123/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
