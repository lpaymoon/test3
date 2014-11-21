package hfrest.test;

import java.util.HashMap;
import java.util.Map;
public class WxVerifyRest extends Base{
	public void testDoCreateService() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("mobileid", "18810528827");
		map.put("randomkey", "123");
		Object o = api.doCall("POST", "/hfrestbusi/hfVerifyInfoRest/132.xml", map, Map.class);
		System.out.println(o);
	}

//	@Test
//	public void testDoShowService() throws Exception {
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("mobileid", "18810528827");
//		map.put("randomkey", "123");
//		Object o = api.doGET("/hfrestbusi/hfVerifyInfoRest/132/123.xml", map, Map.class);
//		System.out.println(o);
//	}

//	@Test
//	public void testDoUpdateService() throws Exception {
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("mobileid", "18810528827");
//		map.put("randomkey", "321");
//		map.put("verifytimes", "2");
//		Object o = api.doCall("POST","/hfrestbusi/hfVerifyInfoRest/132/123.xml", map, Map.class);
//		System.out.println(o);
//	}

}
