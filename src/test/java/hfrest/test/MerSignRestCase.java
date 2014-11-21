package hfrest.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.umpay.hfbusi.HFBusiDict;
public class MerSignRestCase extends Base {

	@Test
	public final void testDoShowService() {
		Map<String,String> map = new HashMap<String , String>();
		map.put(HFBusiDict.MERID, "9996");
		//map.put(HFBusiDict.UNSIGNSTR, "merId=6671&settleDate=20120112&version=3.0");
		map.put(HFBusiDict.UNSIGNSTR, "merId=6671&settleDate=20120112&version=3.0");
		map.put(HFBusiDict.SIGNSTR, "l9a0QYx6GCBjaOD0U4uPEUPbC%2FBVWJ9OXHFifmEazURJqsYfcbcibeUKUTHbRh%2FqQXsqqv3AvSl4gmetlx16cpQPW%2FE3OTYP9VNdjzZ5C53COwpDkNtR46%2FONl%2FdGvXyMr5W9MIMXbcB3rnfd%2BIeISY%2FwLibFavMYjttD8i96Xk%3D%3FmerId%3D6671&unsignstring=merId%3D6671%26settleDate%3D20120112%26version%3D3.0&merid=6671&x-accept-charset=UTF-8");
		//map.put(XmlMobile.PLATDATE, "20110831");
		//map.put(XmlMobile.REQDATE, "20111010");
		//map.put(key, value)
		try {
			Map re = (Map)api.doGET("/hfrestbusi/mersign/LJL234/7535.xml", map , Map.class);
			System.out.println(re);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
