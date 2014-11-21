/** *****************  JAVA头文件说明  ****************
 * file name  :  HfUserMerLtdTest.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2012-2-21
 * *************************************************/ 

package hfrest.complex.test;


import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;



/** ******************  类说明  *********************
 * class       :  HfUserMerLtdTest
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class HfUserMerLtdTest extends Base{

	/**
	 * Test method for {@link com.umpay.hfrestbusi.rest.complex.HfUserMerLtdRest#doCreateService(java.util.Map)}.
	 * @throws Exception 
	 */
	
	public void testDoCreateService() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("merid", "8810");
		map.put("mobileid", "13716230433");
//		map.put("usertype", "3");
		map.put("userStatus", "10");
		map.put("bankid", "XE010000");
		map.put("amt", "231");
		
		Object m =  api.doCall("POST","/hfrestbusi/usermerltd/M132.xml", map, Map.class);
		System.out.println(m);
	}

	/**
	 * Test method for {@link com.umpay.hfrestbusi.rest.complex.HfUserMerLtdRest#doUpdateService(java.util.Map, java.lang.String)}.
	 * @throws Exception 
	 */
//	public void testDoUpdateService() throws Exception {
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("merid", "9996");
//		map.put("mobileid", "13396399070");
//		map.put("bankid", "XE011000");
//		map.put("amt", "1000");
//		map.put("usertype", "3");
//		
//		Map m = (Map) api.doCall("POST","/hfrestbusi/usermerltd/M132/123.xml", map, Map.class);
//		System.out.println(m);
//	}
}
