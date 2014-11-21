package com.umpay.hfrestbusi.rest.complex;

import java.io.File;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import com.bs3.utils.MyIO;
import com.bs3.utils.MyLog;
import com.bs3.utils.MyUtil;
import com.bs3.utils.NamedProperties;
import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.MobileNumbers;
import com.umpay.hfrestbusi.util.StringUtil;


/** ******************  类说明  *********************
 * class       :  Dalet4Mobiles
 * @author     :  LiuJiLong
 * @version    :  1.0
 * description :  白名单资源
 * @see        :
 * ************************************************/
public class HfDalet4MobilesRest extends BaseRest{

	private final static MyLog _log = MyLog.getLog(HfDalet4MobilesRest.class);
	private static Map<Integer, BitSet> g_map = new HashMap<Integer, BitSet>();
	//---------------------
	private final static String DEFAULT_KEYS = HfDalet4MobilesRest.class.getName();
	private final static String DEFAULT_FILE = "Dalet4MobileNo-all.bsm";
	//---------------------
	static{
		try{
			if (g_map.size()==0) Box.m_load(g_map);
			_log.info("加载白名单文件成功");
		}catch(Exception e){
			e.printStackTrace();
			_log.error(e, "加载白名单文件失败");
		}
	}
	public HfDalet4MobilesRest() {
		this.setAuthRequired(false);
	}

	/**
	 * 加载本地配置文件,一般情况无需主动调用,调用任何接口都会先判断是否加载,否的话会自动加载.
	 */
	@Override
	public String doCreateService(Map<String, String> urlargs) throws Exception {
		logInfo("开始主动加载手机号配置  MAPSIZE:%s", g_map.size());
		int rc = Box.m_load(HfDalet4MobilesRest.g_map);
		if (rc == 0) {
			logInfo("主动加载黑白名单失败,找不到可加载文件 ", "");
			return "86801090";// 主动加载黑白名单失败
		}
		logInfo("主动加载手机号配置完成 MAPSIZE: %s", g_map.size());
		return BusiConst.SUCCESS;
	}

	/**
	 * 将当前配置MAP序列化到本地文件中
	 */
	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		_log.debug("# doList()...%d %s", g_map.size(), g_map.keySet());
		String fileName = StringUtil.trim((String)urlargs.get(HFBusiDict.FILENAME));

		logInfo("开始序列化手机号配置: g_map.size[%s]", g_map.size());
		Box.m_save(g_map, "".equals(fileName)?null:fileName);
		//boolean rs = MobileNumbers.saveNumbers2TXT(g_map, fileName);//考虑到号码数量过大,不再生成TXT文件
		logInfo("序列化手机号配置完成: g_map.size[%s]", g_map.size());
		return BusiConst.SUCCESS;
	}

	/**
	 * 检查手机号
	 */
	@Override
	public String doShowService(Map<String, String> urlargs, String id)
			throws Exception {
		String mobileId = StringUtil.trim((String)urlargs.get(HFBusiDict.MOBILEID));

		logInfo("开始检查手机号配置　: %s", mobileId);
		String rs = Box.m_check(g_map, mobileId) + "";
		out.put(HFBusiDict.ISCONTROL, rs);
		logInfo("手机号配置检查完成　: %s; %s", mobileId, rs);
		return BusiConst.SUCCESS;
	}

	/**
	 * 修改手机号状态,支持多个手机号同时修改为一种状态
	 * iscontrol 0:false;1:true
	 */
	@Override
	public String doUpdateService(Map<String, String> urlargs, String id)
			throws Exception {
		String content = StringUtil.trim((String)urlargs.get(HFBusiDict.CONTENT));
		String sign = StringUtil.trim((String)urlargs.get(HFBusiDict.ISCONTROL));

		logInfo("开始修改手机号配置,MOBILES[%s] 改为: ISCONTROL[%s]", content, "1".equals(sign));
		String[] mobileIds = content.split(",");
		for(String mobileId:mobileIds){
			if(!"".equals(mobileId))	Box.m_set(g_map, mobileId, "1".equals(sign));
		}
		logInfo("手机号配置修改完成, MOBILES[%s] 改为: ISCONTROL[%s]", content, "1".equals(sign));
		return BusiConst.SUCCESS;
	}
	//----------------------
	/**
	 * 专利申请TD1303146F（刘胜）一种号码集合的存储和访问方法-专利交底书-v1.1.20130307.doc
	 * @auther lius
	 */
	private static class Box{
		private final static MyLog _log = MyLog.getLog(Box.class);
		@SuppressWarnings("unchecked")
		static int m_load(Map<Integer, BitSet> gmap) {//先加载file.bitset文件，后加载txt文件，同时保持bsm文件。
			Map<String,String> vars = NamedProperties.getMap(BusiConst.SYSPARAMS);
			if (vars==null) {
				_log.warn("E m_load().getMap(%s) = null", DEFAULT_KEYS);
				return 0;
			}
			_log.info("Mobile_Map is LOADING,SIZE[%s]", gmap.size());// add by liujilong 20130425
			gmap.clear();
			//System.gc();
			String fMap = MyUtil.getMapValue(vars, "file.bitset", DEFAULT_FILE);
			File file = new File(fMap);
			if (fMap!=null&&file.exists()) {
				Map<Integer, BitSet> m = (Map<Integer,BitSet>)MyIO.readObject(fMap);
				//_log.debug("# m_load().readObject(%s)...%d Bytes", fMap, file.length());
				gmap.putAll(m);	//全部添加。
			}
			_log.debug("MEMERY BEFORE LOAD FINISH: " + MobileNumbers.printHeapMemery());// add by liujilong 20130425
			_log.info("Mobile_Map is LOADED,SIZE[%s]", gmap.size());// add by liujilong 20130425
			//this.doSave(fMap);//重新保存。
			_log.debug("MEMERY AFTER LOAD FINISH: " + MobileNumbers.printHeapMemery());// add by liujilong 20130425
			
			return gmap.size();
		}
		
		static void m_save(Map<Integer, BitSet> gmap, String fMap) {
			if (fMap==null) {
				Map<String,String> vars = NamedProperties.getMap(BusiConst.SYSPARAMS);
				fMap = MyUtil.getMapValue(vars, "file.bitset", DEFAULT_FILE);
			}
			MyIO.writeObject(fMap, gmap);
			_log.debug("# m_save().writeObject(%s)...%d Items", fMap, gmap.size());
		}
		static boolean m_check(Map<Integer, BitSet> gmap, String number11) {
			Integer index = Box.getIndex(number11);
			BitSet bitset = gmap.get(index);
			if (bitset==null)	return false;
			int endsInt = Box.getEndsInt(number11);
			boolean ok = bitset.get(endsInt);
			_log.debug("# m_check(%s).get(%d-%d) = %s", number11, index, endsInt, ok);
			return ok;
		}
		static void m_set(Map<Integer, BitSet> gmap, String number11, boolean valid) {
			Integer index = Box.getIndex(number11);
			BitSet bitset = gmap.get(index);
			if (bitset==null) {
				_log.debug("E m_set(%s).get(%d)=null", number11, index);
				if (valid) {	//如果是false则无需创建。
					bitset = new BitSet();			gmap.put(index, bitset);
					_log.debug("# m_set(%s).put(%d) = new BitSet", number11, index);
				} else {
					return;
				}
			}
			int endsInt = Box.getEndsInt(number11);
			bitset.set(endsInt, valid);//设置true|false
			_log.debug("# m_set(%s).set(%d-%d) = %s", number11, index, endsInt, bitset.get(endsInt));
		}
		/**
		 * 获得前缀
		 */
		static Integer getIndex(String number11) {
			if (number11==null || number11.length()<11) {
				_log.warn("E getIndex(%s)...ERROR", number11);
				return null;
			}
			int endsLen = getEndsLen();
			String index = number11.substring(0, number11.length() - endsLen);
		//	_log.debug("# getIndex(%s) = %s", number11, index);
			return Integer.parseInt(index);
		}
		static int getEndsInt(String s11) {
			String endsStr = s11.substring(s11.length() - Box.getEndsLen());
			int    endsInt = Integer.parseInt(endsStr);
			return endsInt;
		}
		static int getEndsLen() {
			Map<String,String> vars = NamedProperties.getMap(DEFAULT_KEYS);
			if (vars==null) {
				_log.warn("E getEndsLen().getMap(%s) = null", DEFAULT_KEYS);
				return 8;
			}
			int endsLen = MyUtil.getMapValue(vars, "ends.length", 8);
			return endsLen;
		}
	}
//	MAX:[520290304]  TOTAL:[520290304]  FREE:[514470568]
//	                                          MAX:[520290304]  TOTAL:[520290304]  FREE:[294529256]
//	                                          MAX:[520290304]  TOTAL:[520290304]  FREE:[69586648]
//	                                          MAX:[520290304]  TOTAL:[520290304]  FREE:[69590688]

//	public static void main(String[] args) {
//		System.out.println(MobileNumbers.printHeapMemery());
//		Map<Integer, BitSet> m = (Map<Integer,BitSet>)MyIO.readObject("Dalet4MobileNo-all.bsm");
//		//_log.debug("# m_load().readObject(%s)...%d Bytes", fMap, file.length());
//		Dalet4Mobiles.g_map.clear();
//		Dalet4Mobiles.g_map.putAll(m);	//全部添加。
//		System.out.println(MobileNumbers.printHeapMemery());
//		Map<Integer, BitSet> m1 = (Map<Integer,BitSet>)MyIO.readObject("Dalet4MobileNo-all.bsm");
//		//_log.debug("# m_load().readObject(%s)...%d Bytes", fMap, file.length());
//		Dalet4Mobiles.g_map.clear();
//		Dalet4Mobiles.g_map.putAll(m1);	//全部添加。
//		System.out.println(MobileNumbers.printHeapMemery());
//		//m1 = null;
//		m = null;
//		System.gc();
//		System.out.println(MobileNumbers.printHeapMemery());
//		
//	}
//	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
//		System.out.println(MobileNumbers.printHeapMemery());
//		ObjectInputStream ois =	new ObjectInputStream(new FileInputStream("Dalet4MobileNo-all.bsm"));
//		Map<Integer, BitSet> m = (Map<Integer, BitSet>) ois.readObject();
//		Dalet4Mobiles.g_map.clear();
//		Dalet4Mobiles.g_map.putAll(m);	//全部添加。
//		ois.close();
//		System.out.println(MobileNumbers.printHeapMemery());
//		ObjectInputStream ois1 =	new ObjectInputStream(new FileInputStream("Dalet4MobileNo-all.bsm"));
//		Map<Integer, BitSet> m1 = (Map<Integer,BitSet>)ois1.readObject();
//		//_log.debug("# m_load().readObject(%s)...%d Bytes", fMap, file.length());
//		Dalet4Mobiles.g_map.clear();
//		Dalet4Mobiles.g_map.putAll(m1);	//全部添加。
//		ois1.close();
//		System.out.println(MobileNumbers.printHeapMemery());
//		ois = null;
//		ois1 = null;
////		m = null;
////		m1 = null;
//		System.gc();
//		System.out.println(MobileNumbers.printHeapMemery());
//		
//	}
	
}