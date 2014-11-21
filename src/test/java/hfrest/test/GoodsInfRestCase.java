package hfrest.test;

import java.util.HashMap;
import java.util.Map;

public class GoodsInfRestCase extends Base{
	public final void testDoShowService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put("merid","5500");
		map.put("goodsid", "550005");
		//map.put("goodstype", "9");
		try {
			Map re = (Map)api.doGET("/hfrestbusi/goodsinf/LJL234/9996", map , Map.class);
			System.out.println(re);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
