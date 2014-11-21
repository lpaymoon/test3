/** *****************  JAVA头文件说明  ****************
 * file name  :  DataClientRestTest.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Jun 6, 2012
 * *************************************************/ 

package hfrest.test;

import java.util.HashMap;
import java.util.Map;

/** ******************  类说明  *********************
 * class       :  DataClientRestTest
 * @author     :  lingling
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class DataClientRestTest extends Base{
	
	public void testDoListService() {
			Map<String, String> map = new HashMap<String, String>();			
			try {
				Map re = (Map) api.doGET("/hfrestbusi/logdata/M1234567890/10_10_38_214.xml", map,Map.class);
				System.out.println(re);
			} catch (Exception e) {
			}
	}
	
	public void testDoUpdateService() {
//		20120809204229,M2010037283136,M,R3DXZF,86001811,MW551000,18712156196,3130,031,300,20120809204227570788,551,556,,超过号段月限额,306
//		20120809204230,M2010037283154,M,HFDXZF,86001820,MW898000,18789868132,5548,221,2000,35355054,898,898,,超过用户次限额,70
//		20120809204240,M2010037283566,M,R3DXZF,86001820,MW991000,18799607534,5836,040,400,kLnowjmaaaavtFVz1_02337401_45_0,991,909,,超过用户次限额,1100
//		20120809204320,M2010037285253,M,R3DXZF,86001871,MW871000,14769219235,5867,010,100,1344516163955422,871,876,,号段到频度控制阀值，时间间隔短,2725
//		20120809204324,M2010037285522,M,R3DXZF,86001811,MW551000,15077931394,3130,031,300,20120809204322720421,551,554,,超过号段月限额,307
			Map<String, String> map = new HashMap<String, String>();
			map.put("Data#1#REJT", "20120821204229,M2010037283136,M,R3DXZF,86001811,MW551000,18712156196,3130,031,300,20120809204227570788,551,556,,超过号段月限额,306");
			map.put("Data#2#REJT", "20120821204230,M2010037283154,M,HFDXZF,86001820,MW898000,18789868132,5548,221,2000,35355054,898,898,,超过用户次限额,70");
			map.put("Data#3#REJT", "20120821204240,M2010037283566,M,R3DXZF,86001820,MW991000,18799607534,5836,040,400,kLnowjmaaaavtFVz1_02337401_45_0,991,909,,超过用户次限额,1100");
			map.put("Data#4#REJT", "20120821204320,M2010037285253,M,R3DXZF,86001871,MW871000,14769219235,5867,010,100,1344516163955422,871,876,,号段到频度控制阀值，时间间隔短,2725");
			map.put("Data#5#REJT", "20120821204324,M2010037285522,M,R3DXZF,86001811,MW551000,15077931394,3130,031,300,20120809204322720421,551,554,,超过号段月限额,307");
			try {
				Map re = (Map) api.doCall("POST","/hfrestbusi/logdata/M1234/10_10_38_214.xml", map,Map.class);
				System.out.println(re);
			} catch (Exception e) {
			}
	}
}



