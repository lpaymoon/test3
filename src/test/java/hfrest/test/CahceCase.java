package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class CahceCase extends Base {
  @Test
  public void testDoUpdateService() throws Exception{
	  Map<String,String> map = new HashMap<String,String>();
	  Object o =api.doCall("POST", "/hfrestbusi/cache/222/333.xml", map, map.getClass());
	  System.out.println(o);
  }
}
