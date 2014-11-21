package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;

/** ******************  类说明  *********************
 * class       :  HfCheckUndoTransRestCase
 * @author     :  LiuJiLong 
 * description :  冲正交易检查测试类
 * @see        :  
 * @version    :  1.0                   
 * ************************************************/   
public class HfCheckUndoTransRestCase extends Base {
//RPID,TRANSRPID,FUNCODE,PLATDATE
	public void testDoUpdateService() {
		Map<String,String> map =  new HashMap<String,String>();
		map.put(HFBusiDict.TRANSRPID,"M1051091051091");
		map.put(HFBusiDict.FUNCODE,"P100");
		map.put(HFBusiDict.PLATDATE,getyyyyMMdd());
		try {
			Object o = api.doCall("POST", "/hfrestbusi/xetrans/checkundo/320780001/123.xml", map,Map.class);
			System.out.println(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
