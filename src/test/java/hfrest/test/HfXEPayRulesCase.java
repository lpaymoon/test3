package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;

/** ******************  类说明  *********************
 * class       :  
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  
 * @see        :  
 * @date：2014-7-25 下午04:39:16                      
 * ************************************************/
public class HfXEPayRulesCase extends Base{
	@Test
	public void testDoShowService() {
		Map<String,String> map = new HashMap<String,String>();
		map.put(HFBusiDict.PROVCODE,"010");
		try {
			Object o = api.doGET("/hfrestbusi/xepayrule/123.xml", map, Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
