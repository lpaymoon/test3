package com.umpay.hfrestbusi.util;

import java.security.MessageDigest;
import java.util.Random;

public class SecurityUtil {
	public static String getCID(String type){
		String cliengId = "86"+type;
		cliengId = cliengId+System.currentTimeMillis()+getRandomNum(15);
		return getMD5String(cliengId);
	}
	public static String getRandomNum(int length){
		String num = "0123456789";
		Random random = new Random();
		StringBuffer sf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(10);// 0~61
			sf.append(num.charAt(number));
		}
		return  sf.toString();
	}
	public static String getMD5String(String plainText){
		// 返回字符串
		   String md5Str = null;
		   try {
		    // 操作字符串
		    StringBuffer buf = new StringBuffer();
		    /**
			    * MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
			    * 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			    * 
			    * MessageDigest 对象开始被初始化。
			    * 该对象通过使用 update()方法处理数据。
			    * 任何时候都可以调用 reset()方法重置摘要。
			    * 一旦所有需要更新的数据都已经被更新了，应该调用digest()方法之一完成哈希计算。 
			    * 
			    * 对于给定数量的更新数据，digest 方法只能被调用一次。
			    * 在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
			    */ 
		    MessageDigest md = MessageDigest.getInstance("MD5");
		   
		    // 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
		    md.update(plainText.getBytes());
		    // 计算出摘要,完成哈希计算。
		    byte b[] = md.digest();
		    int i;
		    for (int offset = 0; offset < b.length; offset++) {
		     i = b[offset];
		     if (i < 0) {
		      i += 256;
		     }
		     if (i < 16) {
		      buf.append("0");
		     }
		     // 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
		     buf.append(Integer.toHexString(i));
		    }
		    // 32位的加密
		    md5Str = buf.toString();
		    // 16位的加密
		    // md5Str = buf.toString().md5Strstring(8,24);
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		   return md5Str;
	}
	/** 
	* @Title      : main  
	* @param      : @param args 
	* @return     : void 
	* @throws 
	* @Description: 测试类
	*/
	public static void main(String[] args) {
		String str =  getMD5String(getCID("0"));
		System.out.println(str+","+str.length());
	}
}
