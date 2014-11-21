package com.umpay.hfrestbusi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.bs3.utils.MyIO;
import com.bs3.utils.MyLog;
import com.bs3.utils.MyUtil;
import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.constants.BusiConst;

/**
 * 用于手机号黑白名单处理（去重，加载，转换）
 * @author lius
 * http://baike.baidu.com/view/781662.htm 三大运营商号段
中国移动号段 134,135,136,137,138,139,150,151,152,157,158,159,147,182,183,184[1],187,188
中国联通号段 130,131,132,155,156,185,186,145(属于联通无线上网卡号段)
中国电信号段 133,153,180,181,189
http://www.cnitblog.com/weiweibbs/archive/2008/09/11/49009.aspx Java 中的BitSet的初了解


 */
public class MobileNumbers {

	private static final MyLog _log = MyLog.getLog(MobileNumbers.class);
	private static class Const{
		private static final String[]	cmcc = "134,135,136,137,138,139,150,151,152,157,158,159,147,182,183,184,187,188,111".split(",");
		private static final String[]	unicom = "130,131,132,155,156,185,186,145".split(",");	//中国联通号段
		private static final String[]	ct = "133,153,180,181,189".split(",");	//中国电信号段
	}
	private static final Random		Rand = new Random();
	private static final	int			MobileLen = 11;	//每个号段共1亿个号码。
	private static final	int			MobileCount = 100000000;	//每个号段共1亿个号码。

	/**
	 * 生成单号段文件(全为TRUE状态)
	 */
	public static BitSet makeOne(int prex3, int count) throws IOException{
		String fbits = String.format("MobileNumbers-%03d.bitset", prex3);
		long t0 = MyUtil.timeMilliSecond();
        BitSet bits = new BitSet(MobileCount);
		for(int i=0; i<count; i++) {
			int idx = Rand.nextInt(MobileCount);
			bits.set(idx);
		}
		long t1 = MyUtil.timeMilliSecond();
		_log.debug("# makeOne()...耗时%5d(ms)，产生%d个号码/%d", t1-t0, count, bits.size());
		MyIO.writeObject(fbits, bits);
		long t2 = MyUtil.timeMilliSecond();
		_log.debug("# makeOne()...耗时%5d(ms)，写入%d个号码/%s", t2-t1, count, fbits);
		return bits;
	}

	public static BitSet makeOne(String prex3, int count) throws NumberFormatException, IOException{
		return makeOne(Integer.parseInt(prex3), count);
	}

	public static String printHeapMemery(){
		StringBuffer sb = new StringBuffer();
		Runtime run = Runtime.getRuntime();
		return sb.append("MAX:[" + run.maxMemory()).append("]  TOTAL:[" + run.totalMemory()).append("]  FREE:[" + run.freeMemory()).toString() + "]";
	}
	/**
	 * 加载单号段文件(serial)
	 */
	public static BitSet loadOneBits(String fbits) throws IOException{//prex3={null|139|138|...}
		long t0 = MyUtil.timeMilliSecond();
		Object fobj = MyIO.readObject(fbits);
		BitSet bits = null;
		if (fobj==null) {
			bits = new BitSet();
		}else{
			bits = (BitSet)fobj;
		}
		long t1 = MyUtil.timeMilliSecond();
		_log.debug("# loadOne()...耗时%5d(ms)，加载%d个号码/%s", t1-t0, bits.size());
		return bits;
	}

	/**
	 * 生成批次号段黑白名单列表文件
	 */
	public static void makeAll(int[] prex3array, int max) throws IOException{
		long t0 = MyUtil.timeMilliSecond();
		Map<Integer, BitSet> bmap = new HashMap<Integer,BitSet>();
		for(int prex: prex3array) {
			BitSet bits = makeOne(prex, max);
		//	bmap.put(Integer.toString(prex), bits);	//<String,BitSet>
			bmap.put(Integer.valueOf(prex), bits);	//<Integer,BitSet>
		}
		long t1 = MyUtil.timeMilliSecond();
		_log.debug("# makeAll()...耗时%5d(ms)，生成%d个号码段", t1-t0, bmap.size());
		String fmap = "Dalet4MobileNo-all.bsm";
		MyIO.writeObject(fmap, bmap);
		long t2 = MyUtil.timeMilliSecond();
		_log.debug("# makeAll()...耗时%5d(ms)，写入%d个号码段/%s", t2-t1, bmap.size(), fmap);
	}

	public static void makeAll(String[] prex3array, int max) throws IOException{
		long t0 = MyUtil.timeMilliSecond();
		Map<Integer, BitSet> bmap = new HashMap<Integer,BitSet>();
		for(String prex: prex3array) {
			BitSet bits = makeOne(prex, max);
		//	bmap.put(Integer.toString(prex), bits);	//<String,BitSet>
			bmap.put(Integer.valueOf(prex), bits);	//<Integer,BitSet>
		}
		long t1 = MyUtil.timeMilliSecond();
		_log.debug("# makeAll()...耗时%5d(ms)，生成%d个号码段", t1-t0, bmap.size());
		String fmap = "Dalet4MobileNo-all.bsm";
		MyIO.writeObject(fmap, bmap);
		long t2 = MyUtil.timeMilliSecond();
		_log.debug("# makeAll()...耗时%5d(ms)，写入%d个号码段/%s", t2-t1, bmap.size(), fmap);
	}

	/**
	 * MAP>>txt
	 */
	public static boolean saveNumbers2TXT(Map<Integer, BitSet> fobj, String fileName){
		long t0 = MyUtil.timeMilliSecond();
		if (fobj==null) {
			_log.info("存储MAP对象为空,返回存储失败!");
			return false;
		}else{
			fobj = (Map<Integer, BitSet>)fobj;
		}
		_log.debug("# loadMap()...加载%d个号码段/%s", fobj.size(), fobj);
		//转化为text文件。
		BufferedWriter bw = null;
		int count_all = 0;
		if (fileName==null||"".equals(fileName)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			fileName = "NumbersSave_"+sdf.format(new Date());
		}
		String fTxt = fileName +".txt";
		try{
			bw = new BufferedWriter(new FileWriter(new File(fTxt)));
			for(Map.Entry<Integer, BitSet> me: fobj.entrySet()) {
				String prex = me.getKey().toString();
				BitSet bits = me.getValue();
				int bitsLen = MobileLen - prex.length();
				for(int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i+1)) {
					String m = String.format("%s%0"+bitsLen+"d", prex, i);
					bw.write(m);	bw.write("\n");		count_all++;
				}
			}
		} catch (IOException e) {
			_log.info("出现IO异常,返回存储失败!");
			e.printStackTrace();
			return false;
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					_log.info("写文件出现IO异常,返回存储失败!");
					e.printStackTrace();
					return false;
				}
			}
		}
		long t2 = MyUtil.timeMilliSecond();
		_log.debug("# Map>>TXT...耗时%5d(ms)，转化%d个号码段/%s...%d", t2-t0, fobj.size(),fTxt, count_all);
		return true;
	}
	
	
	/**
	 * serial>>txt
	 */
	public static Map<Integer, BitSet> loadAllMap(String fmap, boolean toTxt) throws IOException{//prex3={null|139|138|...}
		long t0 = MyUtil.timeMilliSecond();
		Object fobj = MyIO.readObject(fmap);
		Map<Integer, BitSet> bmap = null;
		if (fobj==null) {
			bmap = new HashMap<Integer, BitSet>();
		}else{
			bmap = (Map<Integer, BitSet>)fobj;
		}
		long t1 = MyUtil.timeMilliSecond();
		_log.debug("# loadAllMap()...耗时%5d(ms)，加载%d个号码段/%s", t1-t0, bmap.size(), fmap);
		if (! toTxt)		return bmap;
		//辅助功能，转化为text文件。
		BufferedWriter bw = null;
		int count_all = 0;
		String fTxt = fmap +".txt";
		try{
			bw = new BufferedWriter(new FileWriter(fTxt));
			for(Map.Entry<Integer, BitSet> me: bmap.entrySet()) {
				String prex = me.getKey().toString();
				BitSet bits = me.getValue();
				int bitsLen = MobileLen - prex.length();
				for(int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i+1)) {
					String m = String.format("%s%0"+bitsLen+"d", prex, i);
					bw.write(m);	bw.write("\n");		count_all++;
				}
			}
		}finally{
			if (bw!=null)	{	bw.flush();	bw.close();	}
		}
		long t2 = MyUtil.timeMilliSecond();
		_log.debug("# loadAllMap()...耗时%5d(ms)，转化%d个号码段/%s...%d", t2-t1, bmap.size(),fTxt, count_all);
		return bmap;
	}
	
	public static Map<Integer, BitSet> loadAllTxt(Map<Integer, BitSet> bmap, String fTxt, int bitsLen, boolean toMap, boolean toDebug, String preCheck) throws IOException{
		if (bmap==null)		bmap = new HashMap<Integer, BitSet>();
		long t0 = MyUtil.timeMilliSecond();
		if (bitsLen>=8)	bitsLen = 8;
		if (bitsLen<=4)	bitsLen = 4;
		int bitsSize = 1;
		for(int i=0; i<bitsLen; i++)	bitsSize *= 10;
		if (toDebug) _log.debug("# loadAllTxt(%s) bitsLen=%d/bitsSize=%d", fTxt, bitsLen, bitsSize);
		BufferedReader br = null;
		int count=0;
		try{
			br = new BufferedReader(new FileReader(fTxt));
			String line = null;
			int errorNum = 0;
			do{
				line = br.readLine();
				if (line==null)		break;
				line = line.trim();
				if (line.length() < MobileLen) {
					_log.warn("E loadAllTxt() Error Line '%s'", line);
					continue;
				}
				String prex = line.substring(0, MobileLen - bitsLen);
//				if(preCheck.indexOf(prex)==-1){
//					_log.error(null, "# loadErrorMobile [%s] num [%s]", line, ++errorNum);
//					continue;// 20130506 liujilonng 增加前缀检查,防止因错误号段输入而导致内存大增
//				}
				Integer keys = Integer.valueOf(prex);	//keys = prex;
				String ends = line.substring(MobileLen - bitsLen, MobileLen);
				int endsInt = Integer.parseInt(ends);
				//_log.debug("# loadAllTxt(%s) %s-%s", line, prex, ends);
				//BitSet bits = bmap.get(prex);
				BitSet bits = bmap.get(keys);
				if (bits==null) {
					bits = new BitSet(bitsSize);
					bmap.put(keys, bits);
				}
				bits.set(endsInt);	//设置。
				count++;
			}while(line!=null);
		}finally{
			if (br!=null)	{	br.close();	}
		}
		long t1 = MyUtil.timeMilliSecond();
		if (toDebug) _log.debug("# loadAllTxt()...耗时%5d(ms)，生成%d个号码段%d个号码", t1-t0, bmap.size(), count);
		if (! toMap)		return bmap;	//无需转化为bmap文件
		String fmap = String.format("%s-%d.bsm2",fTxt, bitsLen);
		MyIO.writeObject(fmap, bmap);
		long t2 = MyUtil.timeMilliSecond();
		if (toDebug) _log.debug("# loadAllTxt()...耗时%5d(ms)，写入%d个号码段%d个号码/%s", t2-t1, bmap.size(), count, fmap);
		return bmap;
	}
	
	/** ********************************************
	 * method name   : loadNumbers 
	 * description   : 全量加载
	 * @return       : void
	 * @param        : @param txtdir
	 * @param        : @throws NumberFormatException
	 * @param        : @throws IOException
	 * modified      : LiuJiLong ,  2013-5-7 上午09:45:35
	 * @see          : 
	 * ********************************************/      
	private static Map<Integer, BitSet> loadNumbers(Map<Integer, BitSet> bmap, String txtdir, String preCheck) throws NumberFormatException, IOException{
		//生成对象
		long t0 = MyUtil.timeMilliSecond();
		if(bmap==null) bmap = new HashMap<Integer,BitSet>();
		for(String prex: MobileNumbers.Const.cmcc) {
			BitSet bits = new BitSet(100000000);
			bmap.put(Integer.valueOf(prex), bits);	//<Integer,BitSet>
		}
		long t1 = MyUtil.timeMilliSecond();
		_log.info("# loadNumbers()...耗时%5d(ms)，生成%d个号码段", t1-t0, bmap.size());
		//加载TXT中的手机号
		File txts = new File(txtdir);
		if (txts.isFile()) {
			_log.warn("E loadNumbeer()...Not a PATH:%s", txtdir);
			return null;
		}
		String[] fileNames = txts.list(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt")||name.endsWith(".TXT");
			}
		});
		
		int bitsSize = 1;
		for(int i=0; i<8; i++)	bitsSize *= 10;
		
		long count = 0;
		for (String fileName : fileNames) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(txtdir + "/" + fileName));
				count = loadNumbersFromOneFile(bmap, preCheck, bitsSize, count,
						br);
			} finally {
				if (br != null) {
					br.close();
				}
			}
		}
		long num = 0;
		for (BitSet bs : bmap.values())
			num += bs.cardinality();
		long t2 = MyUtil.timeMilliSecond();
		_log.info("# loadNumbers()...耗时%5d(ms)，加载%d个号码,有效号码数量%d", t2 - t1,
				count, num);

		// 序列化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fmap = "NumbersObj_" + sdf.format(new Date());
		MyIO.writeObject(txtdir + "/" + fmap, bmap);
		_log.info("# loadNumbers()...共耗时%5d(ms)，序列化完成", t2 - t0);
		return bmap;
	}

	/** ********************************************
	 * method name   : loadNumbersFromOneFile 
	 * description   : 加载单一文件号码
	 * @return       : long 已加载号码数量
	 * @param        : @param bmap 号码存储对象
	 * @param        : @param preCheck 前缀检查字符串
	 * @param        : @param bitsSize 生成对象大小
	 * @param        : @param count 已加载号码数量
	 * @param        : @param br 文件读取流
	 * @param        : @return
	 * @param        : @throws IOException
	 * modified      : LiuJiLong ,  2013-5-13 下午03:15:25
	 * @see          : 
	 * ********************************************/      
	private static long loadNumbersFromOneFile(Map<Integer, BitSet> bmap,
			String preCheck, int bitsSize, long count, BufferedReader br) throws IOException {
		String line;
		int errorNum = 0;
		do {
			line = br.readLine();
			if (line == null)
				break;
			line = line.trim();
			if (line.length() < MobileLen) {
				_log.warn("E loadAllTxt() Error Line '%s'", line);
				continue;
			}
			count++;
			String prex = line.substring(0, MobileLen - 8);
			if (preCheck.indexOf(prex) == -1) {
				_log.warn("# loadErrorMobile [%s] num [%s]", line, ++errorNum);
				continue;// 增加前缀检查,防止因错误号段输入而导致内存大增
			}
			Integer keys = Integer.valueOf(prex); // keys = prex;
			String ends = line.substring(MobileLen - 8, MobileLen);
			int endsInt = Integer.parseInt(ends);
			BitSet bits = bmap.get(keys);
			if (bits == null) {
				bits = new BitSet(bitsSize);
				bmap.put(keys, bits);
			}
			bits.set(endsInt); // 设置。
		} while (line != null);
		return count;
	}

	/** ********************************************
	 * method name   : checkNumbers 
	 * description   : 号码检测
	 * @return       : boolean
	 * @param        : @param bmap
	 * @param        : @param txtdir
	 * @param        : @return
	 * @param        : @throws IOException
	 * modified      : LiuJiLong ,  2013-5-7 上午11:12:06
	 * @see          : 
	 * ********************************************/      
	private static void checkNumbers(Map<Integer, BitSet> bmap, String txtdir)
			throws IOException {
		long t0 = MyUtil.timeMilliSecond();
		File txts = new File(txtdir);
		if (txts.isFile()) {
			_log.warn("E checkNumbers()...Not a PATH:%s", txtdir);
			return;
		}
		String[] fileNames = txts.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt") || name.endsWith(".TXT");
			}
		});

		int bitsSize = 1;
		for (int i = 0; i < 8; i++)
			bitsSize *= 10;

		long count = 0;
		long errorNum = 0;
		for (String fileName : fileNames) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(txtdir + "/" + fileName));
				String line = null;
				do {
					line = br.readLine();
					if (line == null)
						break;
					line = line.trim();
					if (line.length() < MobileLen) {
						_log.warn("E checkNumbers() Error Line '%s'", line);
						continue;
					}
					count++;
					BitSet bs = bmap.get(Integer.valueOf(line.substring(0, 3)));
					if (bs != null
							&& !bs.get(Integer.valueOf(line.substring(3)))) {
						_log.info("检测到号码未加载成功[%s] ", line);
						errorNum++;
						continue;
					}
				} while (line != null);
			} catch (IOException e) {
				_log.error(e, "E checkNumbers()...%s", e);
			} finally {
				if (br != null) {
					br.close();
				}
			}
		}
		long t2 = MyUtil.timeMilliSecond();
		_log.info("# checkNumbers()...耗时%5d(ms)，检测%d个号码, 检测到%d个错误号码", t2 - t0,
				count, errorNum);
	}
	
	public static void main(String[] args) {
		try {
			Map<Integer, BitSet> bmap = loadNumbers(null, "d:/numtest", "134,135,136,137,138,139,150,151,152,157,158,159,147,182,183,184,187,188");//号码生成
			checkNumbers(bmap, "d:/numtest");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static class Test{
		private static final MyLog _log = MyLog.getLog(Test.class);
		public static void main(String[] args) throws Exception {
			//test_one_1();
//			test_all_1();
//			test_all_2();
//			test_all_3();
			//test_bitset_0();
			makeAll(Const.cmcc,0);
//			BitSet b1 = makeOne("130", 10000);
//			BitSet b2 = makeOne("132", 10000);
//			BitSet b3 = makeOne("133", 10000);
//			BitSet b4 = makeOne("134", 10000);
//			BitSet b5 = makeOne("135", 10000);
//			Map<String, BitSet> map = new HashMap<String, BitSet>(4);
//			map.put("130", b1);
//			map.put("132", b2);
//			map.put("133", b3);
//			map.put("134", b4);
//			map.put("135", b5);
			//test_all_2();
			//produceNums("numsFile");
		}
		
		private static void produceNums(String fileName) throws IOException{
			BufferedWriter bw = null;
			int count_all = 0;
			String fTxt = fileName +".txt";
			try{
				bw = new BufferedWriter(new FileWriter(fTxt));
				for(String pre:MobileNumbers.Const.cmcc)
				for(int i=0;i<100000;i++) {
					String m = String.format("%s%08d", pre, i);
					bw.write(m);	bw.write("\n");		count_all++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if (bw!=null)	{	bw.flush();	bw.close();	}
			}
		}
		public static void test_bitset_0() throws Exception {
			BitSet bits = new BitSet(100);
			bits.set(1);
			bits.set(10);	//size=128,length=11
			bits.set(14);	//size=128,length=15
			bits.set(101);	//size=128,length=102
			_log.info("# test_bitset_0()....size=%d,length=%d", bits.size(), bits.length());
		}
		public static void test_all_1() throws Exception {
			makeOne(151, 10000);
			makeOne(152, 100000);
			makeOne(153, 1000000);
			//Box.loadAll(fnames)
		}
		public static void test_all_2() throws Exception {
			makeAll(new int[]{135,136,137}, 100000);	//6250249 B < 6250365 B ( = 1250073 B*5 )
			loadAllMap("MobileNumbers-all.bsm", true);
		}
		public static void test_all_3() throws Exception {
		//	loadAllTxt(null, "MobileNumbers-all.bsm.txt", 8, true, true);
//			loadAllTxt(null, "MobileNumbers-all.bsm.txt", 7, true, true);
//			loadAllTxt(null, "MobileNumbers-all.bsm.txt", 6, true, true);
//			loadAllTxt(null, "MobileNumbers-all.bsm.txt", 5, true, true);
			//loadAllTxt(null, "MobileNumbers-all.bsm.txt", 4, true, true);
		}
		public static void test_one_1() throws Exception {
			makeOne(130, 10000);
			makeOne(132, 1000000);
			makeOne(133, 10000000);
//			BitSet bset = loadOneBits("Dalet4MobileNo-130.bitset");
		}

	}

}
/*
0228.103824.874 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt(Dalet4MobileNo-all.bsm.txt) bitsLen=8/bitsSize=100000000
0228.103825.059 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时  187(ms)，生成5个号码段49998个号码
0228.103825.693 [main                    ] INFO  L:30  MyIO                   - W: Dalet4MobileNo-all.bsm.txt-8.txt...62500249B
0228.103825.694 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时  635(ms)，写入5个号码段49998个号码/Dalet4MobileNo-all.bsm.txt-8.txt
0228.103825.695 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt(Dalet4MobileNo-all.bsm.txt) bitsLen=7/bitsSize=10000000
0228.103825.784 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时   89(ms)，生成50个号码段49998个号码
0228.103826.392 [main                    ] INFO  L:30  MyIO                   - W: Dalet4MobileNo-all.bsm.txt-7.txt...62501334B
0228.103826.392 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时  608(ms)，写入50个号码段49998个号码/Dalet4MobileNo-all.bsm.txt-7.txt
0228.103826.393 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt(Dalet4MobileNo-all.bsm.txt) bitsLen=6/bitsSize=1000000
0228.103826.459 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时   66(ms)，生成500个号码段49998个号码
0228.103827.152 [main                    ] INFO  L:30  MyIO                   - W: Dalet4MobileNo-all.bsm.txt-6.txt...62512634B
0228.103827.154 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时  694(ms)，写入500个号码段49998个号码/Dalet4MobileNo-all.bsm.txt-6.txt
0228.103827.154 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt(Dalet4MobileNo-all.bsm.txt) bitsLen=5/bitsSize=100000
0228.103827.228 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时   74(ms)，生成5000个号码段49998个号码
0228.103828.076 [main                    ] INFO  L:30  MyIO                   - W: Dalet4MobileNo-all.bsm.txt-5.txt...62650134B
0228.103828.077 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时  849(ms)，写入5000个号码段49998个号码/Dalet4MobileNo-all.bsm.txt-5.txt
0228.103828.077 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt(Dalet4MobileNo-all.bsm.txt) bitsLen=4/bitsSize=10000
0228.103828.187 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时  109(ms)，生成31562个号码段49998个号码
0228.103829.337 [main                    ] INFO  L:30  MyIO                   - W: Dalet4MobileNo-all.bsm.txt-4.txt...40494180B
0228.103829.338 [main                    ] DEBUG L:26  Dalet4MobileNo$Box     - # loadAllTxt()...耗时 1152(ms)，写入31562个号码段49998个号码/Dalet4MobileNo-all.bsm.txt-4.txt

*/