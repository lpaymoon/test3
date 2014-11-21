package hfrest.complex.test;

import hfrest.test.Base;

import java.util.HashMap;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;


/** ******************  类说明  *********************
 * class       :  HfOrderLockCase
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  订单锁定
 * @see        :                        
 * ************************************************/   
public class HfOrderLockCase extends Base{

	public void testDoShowService() throws Exception {
		Map<String,String>m = new HashMap<String,String>();
		m.put(HFBusiDict.PORDERID, "18620355501");
		m.put(HFBusiDict.VERIFYCODE, "9");
		m.put(HFBusiDict.MOBILEID, "18810528823");
		m.put(HFBusiDict.MSGCON, "1231");
		Map map = (Map) api.doCall("POST","/hfrestbusi/hforder/lockproc/132/141.xml", m, Map.class);
		System.out.println(map);
	}

}
