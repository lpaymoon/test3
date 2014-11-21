/** *****************  JAVA头文件说明  ****************
 * file name  :  BankIdInfTest.java
 * owner      :  LiuJiLong
 * copyright  :  UMPAY
 * description:  
 * modified   :  2011-12-28
 * *************************************************/ 

package hfrest.test;

import java.util.HashMap;
import java.util.Map;


/** ******************  类说明  *********************
 * class       :  BankIdInfTest
 * @author     :  LiuJiLong
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class BankIdInfTest extends Base{
	public final void testDoShowService() {
		Map<String,String> map = new HashMap<String , String>();
		//map.put("bankid","JF007000"); 	//正常
		//map.put("bankid","JF007000ljl"); 	//银行信息不存在
		map.put("bankid","123"); 	//银行未开通
		try {
//			Map re = (Map)api.doCall("POST", "/hfrestbusi/classtest/LJL234", map , Map.class);
			Map re = (Map)api.doGET("/hfrestbusi/bankinf/LJL234/9996", map , Map.class);
			System.out.println(re);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		api.close();
	}
}
