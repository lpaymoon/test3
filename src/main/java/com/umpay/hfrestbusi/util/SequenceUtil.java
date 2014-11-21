/*
 * @(#)SequenceUtil.java	1.00 03/03/21
 */
package com.umpay.hfrestbusi.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.umpay.hfrestbusi.dal.CommonDalInf;
import com.umpay.hfrestbusi.dal.DalFactory;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SequenceUtil {

    private static SequenceUtil su = null;
    private static Map<String,List> keyMap = new HashMap<String,List>();
    private static final Hashtable serials = new Hashtable();
    private static final Hashtable fchannels = new Hashtable();
    private SequenceUtil() {
    }
    public static synchronized SequenceUtil getInstance() {
        if (su == null)
            su = new SequenceUtil();
        return su;
    }
    /**
     * 存储所有支付流程TRANSID前缀的MAP，必须在本类静态代码块前初始化。
     */
    private static Map<String, BRANCHPAYIDPRE> bMap = new HashMap<String, BRANCHPAYIDPRE>();
    /** ******************  类说明  ******************
     * class       :  BRANCHPAYIDPRE
     * date        :  2014-9-29 
     * @author     :  Roy
     * @version    :  V1.0  
     * description :  支付流程TRANSID前缀
     * @see        :                         
     * ***********************************************/
    public enum BRANCHPAYIDPRE{
    	r("0611", "UPPAYR", 22),b("0601", "1", 11),y("0620", "y", 19),x("0612", "UPPAYX", 22);
    	private String pre;
    	private int num;
    	BRANCHPAYIDPRE(String businessType, String _pre, int _num){
    		pre = _pre;
    		num = _num;
    		bMap.put(businessType, this);
    	}
    	
    	public String toString(){
    		return pre;
    	}
    	
    	public int getNum(){
    		return num;
    	}
    	
    	/** *****************  方法说明  *****************
    	 * method name   :  getBranchPayIdPre
    	 * @param		 :  @param businessType
    	 * @param		 :  @return
    	 * @return		 :  String
    	 * @author       :  Roy 2014-9-29 下午2:37:35
    	 * description   :  获取某分支流程的TRANSEQ前缀
    	 * @see          :  
    	 * ***********************************************/
    	public static String getBranchPayIdPre(String businessType){
    		try {
				return bMap.get(businessType).toString() + formatSequence(getBatchSeq(businessType), bMap.get(businessType).getNum());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
    	}
    }
    public static void main(String[] args) {
    	//System.out.println("12345".substring(0, 32));
    	System.out.println(BRANCHPAYIDPRE.getBranchPayIdPre("0601"));
    	String affix = "123456";
    	System.out.println(affix.substring(affix.length() - 12, affix.length()));
    //	System.out.println(BRANCHPAYIDPRE.getBranchPayId("0611", 11));
    	System.out.println(SequenceUtil.bMap.size());
    }
  
    /**
     * 获取序列数
     * @throws Exception 
     * 
     */
	public static long getSequence(String name) throws Exception{
	    long seq = -1;
	    CommonDalInf dal = DalFactory.getDal();
	    HashMap<String, String> map = new HashMap<String,String>();
	    map.put("name.string", name);
	    Map<String, String> retMap=new HashMap<String,String>();
		
	    retMap = dal.call("call_getseq", map);
	    
		Object obj = retMap.get("out.seq.int");
		seq = (Integer) obj;
        return seq;
	}

    public static String formatSequence(long seq, int width) {
        Long lg = new Long(seq);
        String is = lg.toString();
        if (is.length() < width) { // 需要前面补'0'
            while (is.length() < width)
                is = "0" + is;
        } else { // 需要将高位丢弃
            is = is.substring(is.length() - width, is.length());
        }
        return is;
    }
    
    private static int getFirstSeq(String key, List<Integer> list)throws Exception {
		int seq = 0;
		CommonDalInf dal = DalFactory.getDal();
		Map<String, String> map = new HashMap<String, String>();
		map.put("in_seqid.string", key.toString());
		// 每次取100个可用序列号
		map.put("in_interval.int", "100");
		Map m = dal.call("call_getBatchSeq", map);
		int begin = Integer.valueOf(m.get("out.seq_begin.int").toString());
		int end = Integer.valueOf(m.get("out.seq_end.int").toString());
		for (int i = begin; i < end; i++) {
			list.add(i);
		}
		seq = list.remove(0);
		seq = list.remove(0);
		keyMap.put(key, list);
		return seq;
	}
   public  static synchronized long getBatchSeq(String key) throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		if (keyMap.containsKey(key)) {
			list = keyMap.get(key);
			if (list.size() > 0) {
				int seq = list.remove(0);
				keyMap.put(key, list);
				return seq;
			} else {
				return getFirstSeq(key, list);
			}
		} else {
			return getFirstSeq(key, list);
		}
	}
   
   public synchronized long getSequence4File(String name) {
       String appKey = name + ".seq";
       FileChannel fc = null;
       MappedByteBuffer serial = null;
       
       try {
       	fc = (FileChannel) fchannels.get(appKey);
       	serial = (MappedByteBuffer) serials.get(appKey);
           if (serial == null) {
               //获得一个可读写的随机存取文件对象
               RandomAccessFile RAFile = new RandomAccessFile(appKey, "rw");
               if (RAFile.length() < 8) {
                   RAFile.writeLong(0);
               }

               //获得相应的文件通道
               fc = RAFile.getChannel();

               //取得文件的实际大小，以便映像到共享内存
               int size = (int) fc.size();

               //获得共享内存缓冲区，该共享内存可读写
               serial = fc.map(FileChannel.MapMode.READ_WRITE, 0, size);
               
               fchannels.put(appKey, fc);
               serials.put(appKey, serial);
	        }
	        
	        FileLock flock = fc.lock();
	        serial.rewind();
	        long serno = serial.getLong();
           serno++;
	        serial.flip();
	        serial.putLong(serno);
	        flock.release();

	        return serno;
       } catch (IOException e) {
           e.printStackTrace();
           return -1;
       }
   }
}