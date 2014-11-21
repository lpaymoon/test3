package hfrest.test;

import java.util.HashMap;
import java.util.Map;

public class UserGradeRestCase extends Base{
	public final void testDoShowService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put("mobileid","1301111");
		try {
			Map re = (Map)api.doGET("/hfrestbusi/usergrade/LJL234/9996", map , Map.class);
			System.out.println(re);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
