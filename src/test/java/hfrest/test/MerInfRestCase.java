package hfrest.test;

import java.util.HashMap;
import java.util.Map;
public class MerInfRestCase extends Base {
	public final void testDoShowService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put("merid","5953");
		try {
			Map re = (Map)api.doGET("/hfrestbusi/merinf/LJL234/5953", map , Map.class);
			System.out.println(re);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
