package hfrest.test.presstest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.bs2.core.Util;
import com.bs2.mpsp.XmlMobile;
import com.bs3.app.dal.DalApi4Inf;
import com.bs3.app.dal.client.DalClientFactory;
import com.umpay.hfbusi.HFBusiDict;

/**
 * 压力测试
 * 
 * @author 沈建林
 * @version 1.0
 */
public class TestStress {
	private static AtomicInteger ai = new AtomicInteger(0);
	private static AtomicInteger index = new AtomicInteger(0);
	private static AtomicInteger failTrade = new AtomicInteger(0);
	private static AtomicInteger tradeAll;
	private static boolean debug = false;
	private static int _threadNum;

	private static int _period;

	private static int _taskNum;

	private static long startTime;
	
	private static TESTTYPE type = TESTTYPE.UPDATE;
	private static String shortUrl = "uprestbusi/branch/0620/";
	private static String longUrl = "uprestbusi/branch/0620/" + getRpid() + "/yxjd.xml";
//	private static String shortUrl = "/hfrestbusi/bwlist/132.xml";
//	private static String longUrl = "/hfrestbusi/bwlist/15011385231/132.xml";

	private static String getRpid() {
		return "TESLJL" + getRandomStr(9);
	}
	protected static String getRandomStr(int length) {
		String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);// 0~61
			sf.append(str.charAt(number));
		}
		return sf.toString();
	}
	/**
	 * @param threadNum
	 *            压力测试启动的线程数
	 * @param period
	 *            两笔交易之间的时间间隔
	 * @param taskNum
	 *            每个线程发送的次数
	 * @throws Exception
	 */
	public TestStress(int threadNum, int period, int taskNum) throws Exception {
		_threadNum = threadNum;
		_period = period;
		_taskNum = taskNum;
		tradeAll = new AtomicInteger(_threadNum * _taskNum);
		startTime = System.currentTimeMillis();
	}

	void getStatisticInfo() {
		if (tradeAll.decrementAndGet() <= 0) {
			long spentAll = System.currentTimeMillis() - startTime;
			int tradeAll = _taskNum * _threadNum;
			double avg = spentAll /tradeAll;
			double failPercentage = failTrade.get() / tradeAll * 100;
			double pms = 1000.0 * tradeAll / spentAll;

			System.out
					.println(String
							.format(
									"Statistic Info: Trade All[ %d ],AVG[ %s ms],PMS[ %s ],Fail[ %d ],Fail Percentage[ %s",
									tradeAll, avg, pms, failTrade.get(),
									failPercentage)
							+ "% ]");
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("names.ini", "simulation.names.ini");
		PrintWriter out = new PrintWriter(System.out);
		String argsLine = "1,0,1";
		out
				.print("Stress参数（回车使用默认值=" + argsLine + "）"
						+ "\r\n格式：线程数,交易间隔,任务数>");
		out.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String userIn = null;
		try {
			userIn = in.readLine();
		} catch (Exception e) {
			out.println(e.toString());
		}
		if (!userIn.equals("")) {
			argsLine = userIn;
		}

		String[] inputs = argsLine.split(",");

		_threadNum = Integer.parseInt(inputs[0]);
		_period = Integer.parseInt(inputs[1]);
		_taskNum = Integer.parseInt(inputs[2]);
		System.out.println("_threadNum=" + _threadNum + "_period" + _period
				+ "_taskNum" + _taskNum);
		final TestStress test = new TestStress(_threadNum, _period, _taskNum);
		Thread[] threads = new Thread[_threadNum];

		// 初始化线程线程组并启动
		for (int i = 0; i < threads.length; i++) {
			new Thread(new Runnable(){
				public void run() {
					Task so = test.new CM20Task("10.10.38.214", 8630 + "");
					
					for(int i=0;i<_taskNum;i++){
						so.doTask(ai.incrementAndGet());
					}
					so.finishJob();
				}
			}).start();
		}
		
//		new Thread(new Runnable() {
//			public void run() {
//				DalApi4Inf api = null;
//				try {
//					api = DalClientFactory.newRemoteApi(
//							"cm20://10.10.38.214:8620", null, 1, 5);
//					Object rs = null;
//					for (int i = 0; i < _taskNum; i++) {
//						Thread.sleep(1000 * 30);
//						rs = api.doCall("POST", shortUrl, null, Map.class);
//						System.out.println(Thread.currentThread().getId() +";操作类型[CREATE];返回结果:"+ rs);
//					}
//				} catch (URISyntaxException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} finally {
//					api.close();
//				}
//			}
//		}).start();
		
	}
	enum TESTTYPE {
		LIST, CREATE, SHOW, UPDATE, MUL;
	};
	class CM20Task implements Task{
		protected DalApi4Inf api = null;
		public CM20Task(String ip, String port) {
			try {
				api = DalClientFactory.newRemoteApi("cm20://" + ip +":" + port, null, 1, 5);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		public void doTask(int count) {
			Map<String, String> args = getMap();
			Object rs = null;
			try {
				switch (type) {
				case SHOW:
					rs = api.doGET(longUrl.replaceFirst("15011385231", getRpid()), args, Map.class);
					break;
				case LIST:
					rs = api.doGET(shortUrl, args, Map.class);
					break;
				case CREATE:
					rs = api.doPUT(shortUrl, args, Map.class);
					break;
				case UPDATE:
					rs = api.doPUT(longUrl, args, Map.class);
					break;
				case MUL:
					int i = count%2;
					switch (TESTTYPE.values()[i]) {
					case SHOW:
						rs = api.doGET(longUrl, args, Map.class);
						break;
					case LIST:
						rs = api.doGET(shortUrl, args, Map.class);
						break;
					case CREATE:
						rs = api.doCall("POST", shortUrl, args, Map.class);
						break;
					case UPDATE:
						rs = api.doPUT(longUrl, args, Map.class);
						break;
					default:
						System.out.println("E 模后不支持的操作类型!!!!!");
						System.exit(0);
					}
					break;
				default:
					System.out.println("E 不支持的操作类型!!!!!");
					System.exit(0);
				}
				System.out.println(Thread.currentThread().getId()
						+ ";操作类型["
						+ (type.compareTo(TESTTYPE.MUL) == 0 ? TESTTYPE
								.values()[count % 2] : type) + "];返回结果:" + rs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				getStatisticInfo();
			}
		}

		public void finishJob() {
			if(api!=null) api.close();
		}
		
	}

	interface Task {
		public abstract void doTask(int count);
		public abstract void finishJob();
	}

	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		String date8 = Util.strDate8();
		String time6 = Util.strTime6();
		String rpid = "PRSTest";
		map.put(XmlMobile.RPID, rpid);
		
		//--------------
//		map.put(XmlMobile.FUNCODE, "6205");
		map.put(HFBusiDict.MOBILEID, "15011385231");
//		map.put(XmlMobile.CALLED, "106580084");
//		map.put(XmlMobile.PLATSEQ, rpid);
//		map.put(XmlMobile.Seg_ProvCode, "010");
//		map.put(XmlMobile.HOSTID, "M20100");// 全网
//		map.put(XmlMobile.MSGNUM, "1");
//		map.put(XmlMobile.TRANSNUM, "1");
//		map.put(XmlMobile.RETCODE, "9999");
//		map.put(XmlMobile.SUBSTATIONID, "000");
//		map.put(XmlMobile.MSGFORMAT + "#0", "0");
//		map.put(XmlMobile.MSGTO + "#0", "106580084");
		map.put(XmlMobile.REQDATE, date8);
		map.put(XmlMobile.REQTIME, time6);
		map.put(HFBusiDict.TRANSDATE, "20141008");
		map.put(HFBusiDict.MERID, "9996");
		map.put(HFBusiDict.GOODSID, "100");
//		map.put(XmlMobile.ACCESSTYPE, "M");
//		map.put(XmlMobile.MSGCON + "#0", Base64.encode(str.getBytes()));
		return map;
	}
}
