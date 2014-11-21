package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;

public class HfBJWhileUserInfoCase extends Base {
	@Test
	  public void testDoUpdateService() throws Exception{
		  Map<String,String> map = new HashMap<String,String>();
		  String mobileid = "18710019400";
		  map.put(HFBusiDict.MOBILEID, mobileid);
		  map.put(HFBusiDict.USERTYPE,"0");
		  Object o =api.doCall("GET", "/hfrestbusi/hfbjWhiteUserInfo/222/333.xml", map, map.getClass());
		  System.out.println(o);
	  }
}
