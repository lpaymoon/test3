package com.umpay.hfrestbusi.util;
/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtil {
	
	public static void removeEmpty(Map<String, String> map) {
		
		Map<String, String> r4Map = new HashMap<String, String>();
		r4Map.putAll(map);
		for(Entry<String, String> entry:r4Map .entrySet()){
			if("".equals(entry.getValue())){
				map.remove(entry.getKey());
			}
		}
	}
	/*
	 * 将字符串"元"转换成"分"
	 */
	public static String Dollar2Cent(String s) {
		s = StringUtil.trim(s);
		int i = s.indexOf(".");
		if (i == -1)
			return s + "00";
		String intStr = s.substring(0, i);
		if (intStr.length() <= 0)
			intStr = "0";
		String decStr = s.substring(i + 1, s.length());
		if (decStr.length() <= 0)
			decStr = "00";
		if (decStr.length() == 1)
			decStr += "0";
		if (decStr.length() > 2)
			decStr = decStr.substring(0, 2);
		int iInt = Integer.parseInt(intStr);
		if (iInt <= 0)
			return decStr;
		else
			return intStr + decStr;
	}
	/*
	 * 将字符串"分"转换成"元"（长格式），如：100分被转换为1.00元。
	 */
	public static String Cent2Dollar(String s) {
		long l = 0;
		try {
			if (s.charAt(0) == '+') {
				s = s.substring(1);
			}
			l = Long.parseLong(s);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		boolean negative = false;
		if (l < 0) {
			negative = true;
			l = Math.abs(l);
		}
		s = Long.toString(l);
		if (s.length() == 1)
			return (negative ? ("-0.0" + s) : ("0.0" + s));
		if (s.length() == 2)
			return (negative ? ("-0." + s) : ("0." + s));
		else
			return (negative ? ("-" + s.substring(0, s.length() - 2) + "." + s
					.substring(s.length() - 2)) : (s.substring(0,
					s.length() - 2)
					+ "." + s.substring(s.length() - 2)));
	}
	/*
	 * 将字符串"分"转换成"元"（短格式），如：100分被转换为1元。
	 */
	public static String Cent2DollarShort(String s) {
		String ss = Cent2Dollar(s);
		ss = "" + Double.parseDouble(ss);
		if (ss.endsWith(".0"))
			return ss.substring(0, ss.length() - 2);
		if (ss.endsWith(".00"))
			return ss.substring(0, ss.length() - 3);
		else
			return ss;
	}
	/*
	 * 去掉字符串首尾的 <空格字符>（包括TAB），如果s为null则返回空字符串""。
	 */
	public static String trim(String s) {
		if (s == null)
			return "";
		else
			return s.trim();
	}
	/*
	 * 去掉字符串首部位置中连续出现的chars中的字符， 如chars=" ,;#$";s=",#$, abcd"，则返回abcd。
	 */
	public static String trimLeft(String chars, String s) {
		if (s == null)
			return "";
		if (s.length() <= 0)
			return s;
		if (chars == null)
			return s;
		if (chars.length() <= 0)
			return s;
		int i = 0;
		for (i = 0; i < s.length(); i++) {
			if (-1 == chars.indexOf(s.charAt(i)))
				break;
		}
		return s.substring(i);
	}
	/*
	 * 去掉字符串尾部位置中连续出现的chars中的字符， 如chars=" ,;#$";s=",#$, abcd"，则返回abcd。
	 */
	public static String trimRight(String chars, String s) {
		if (s == null)
			return "";
		if (s.length() <= 0)
			return s;
		if (chars == null)
			return s;
		if (chars.length() <= 0)
			return s;
		int i = s.length();
		for (i = s.length() - 1; i > -1; i--) {
			if (-1 == chars.indexOf(s.charAt(i)))
				break;
		}
		if (i < 0)
			return "";
		else
			return s.substring(0, i + 1);
	}
	/*
	 * 去掉字符串中连续出现的chars中的字符， 如chars=" ,;#$";s=",#$, ab,c##d;;;"，则返回abcd。 added
	 * by liuxd 2004-12-02
	 */
	public static String trimAll(String chars, String s) {
		if (s == null)
			return "";
		if (s.length() <= 0)
			return s;
		if (chars == null)
			return s;
		if (chars.length() <= 0)
			return s;
		int i = 0, j = 0;
		StringBuffer sb = new StringBuffer(s);
		do {
			j = sb.length();
			for (i = 0; i < sb.length(); i++) {
				if (chars.indexOf(sb.charAt(i)) != -1) {
					sb.replace(i, i + 1, "");
				}
			}
		} while (j != sb.length());
		return sb.toString();
	}
	/*
	 * 压缩字符串中连续 <空格字符>，及将多个连续的 <空格字符>变成一个 <空格字符> 如s=",#$, abcd"，则返回,#$, abcd。
	 */
	public static String compressSpace(String s) {
		if (s == null)
			return "";
		String ss = s.trim();
		String tmp = "";
		boolean lastIsSpace = false;
		for (int i = 0; i < ss.length(); i++) {
			if (ss.charAt(i) != ' ' && ss.charAt(i) != '\t') {
				tmp += ss.charAt(i);
				lastIsSpace = false;
			} else if (!lastIsSpace) {
				tmp += ' ';
				lastIsSpace = true;
			} else
				continue;
		}
		return tmp;
	}
	/*
	 * 判断字符串是否为数字字符串。是则返回true，否则返回false。
	 */
	public static boolean isDigitalString(String s) {
		if (s == null)
			return false;
		if (s.length() == 0)
			return false;
		String ds = "0123456789";
		for (int i = 0; i < s.length(); i++) {
			if (ds.indexOf(s.charAt(i)) < 0)
				return false;
		}
		return true;
	}
	/*
	 * 判断字符串是否为英文字符串。是则返回true，否则返回false。
	 */
	public static boolean isLetterString(String s) {
		if (s == null)
			return false;
		if (s.length() == 0)
			return false;
		String ds = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < s.length(); i++) {
			if (ds.indexOf(s.charAt(i)) < 0)
				return false;
		}
		return true;
	}
	/*
	 * ISO8859字符串转换成GB2312字符串
	 */
	public static String ISO8859toGB2312(String s) {
		try {
			return new String(s.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	/*
	 * ISO8859字符串转换成GBK字符串（目前该函数已经失效）
	 */
	public static String DBCharset2HostCharset(String s) {
		try {
			return new String(s.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	/*
	 * 是否为null或者空串""
	 */
	public static boolean isNullOrNovalue(String ...s){
		boolean flag = false;
		for (int i = 0; i < s.length; i++) {
			if(s[i]==null||"".equals(s[i])){
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * ********************************************
	 * method name   : getExceptionStr 
	 * description   : 输出加前缀的exception异常栈
	 * @return       : String
	 * @param        : @param pre
	 * @param        : @param e
	 * @param        : @return
	 * modified      : lingling ,  Nov 4, 2011  5:31:32 PM
	 * @see          : 
	 * *******************************************
	 */
	public static String getExceptionStr(String pre , Exception e){
		StackTraceElement[] ste = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%s *** %s \r\n", pre,e.getMessage()));
		for (int i=0;i<ste.length;i++) {
		   sb.append(String.format("%s *** %s \r\n", pre, ste[i].toString()));
	    }		     
		if(e.getCause()!=null){
			ste = e.getCause().getStackTrace();
			sb.append(String.format("%s *** cause: %s \r\n", pre,e.getCause().getMessage()));
			for (int i=0;i<ste.length;i++) {
			   sb.append(String.format("%s *** %s \r\n", pre,ste[i].toString()));
		    }	
		}	
		return sb.toString();
	}
	/** ********************************************
	 * method name   : getDefault 
	 * description   : 获取数据项的默认值
	 * @return       : String
	 * @param        : @param string
	 * @param        : @param string2
	 * @param        : @return
	 * modified      : LiuJiLong ,  2011-11-13  下午05:29:12
	 * @see          : 
	 * ********************************************/      
	public static String getDefault(String string, String string2) {
		return (string == null || string.equals("")) ? string2 : string;
	}
	
	/** ********************************************
	 * method name   : getNewMap 
	 * description   : 返回一个新的带有对应参数的HASHMAP
	 * @return       : Map<String,String>
	 * @param        : @param urlargs
	 * @param        : @param args
	 * @param        : @return
	 * modified      : LiuJiLong ,  2012-2-27  上午10:24:24
	 * @see          : 
	 * ********************************************/      
	public static Map<String, String> getNewMap(Map<String, String> urlargs, String[] args,boolean isChange){
		Map<String, String> newMap = new HashMap<String, String>();
		for(String arg: args){
			if(isChange){
				if(arg.indexOf(".")!=-1){
					newMap.put(arg, StringUtil.trim(urlargs.get(arg.substring(0,arg.indexOf(".")))));
				}else{
					newMap.put(arg+".string", StringUtil.trim(urlargs.get(arg)));
				}
			}else{
				newMap.put(arg, StringUtil.trim(urlargs.get(arg)));
			}
		}
		return newMap;
	}
	
	/** ********************************************
	 * method name   : getPSQLMap 
	 * description   : 根据传来的参数生成PSQL所需的MAP，如果传来的值为NULL，则换为默认值
	 * @return       : Map<String,String>
	 * @param        : @param urlargs   参数MAP
	 * @param        : @param argsType  KEY：参数字典名；VALUE：参数类型，即PSQL所需带的后缀
	 * @param        : @param defaultValues 参数NULL时的默认值
	 * @param        : @return
	 * modified      : LiuJiLong ,  2012-11-19 上午11:14:37
	 * @see          : 
	 * ********************************************/      
	public static Map<String, String> getPSQLMap(Map<String, String> urlargs, Map<String, String> argsType, Map<String, String> defaultValues){
		Map<String, String> rsMap = new HashMap<String, String>();
		for(Entry<String, String> entry: argsType.entrySet()){
			String key = entry.getKey() + "." + entry.getValue();
			String value = urlargs.get(entry.getKey())==null? defaultValues.get(entry.getKey()):urlargs.get(entry.getKey());
			rsMap.put(key, value);
		}
		return rsMap;
	}
	
	  /**
     * 全角转成半角
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}
    
    public static String getParam(String str,String type){
    	if(isNullOrNovalue(str)){
    		return ""+"."+type;
    	}
    	return str+"."+type;
    }
    /** 
    * @Title      : subString  
    * @param      : @param param
    * @param      : @param length
    * @param      : @return 
    * @return     : String 
    * @throws 
    * @Description: 按指定的字节长度，截取字符串
    */
    public static String subString(String param,int length){
    	if(param==null) return param;//如果字符串为null，则直接返回
    	byte[] bytes=null;
		try {
			bytes = param.getBytes("Unicode");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(bytes.length<=length) return param;//如果字节长度小于要截取的字节数，则直接返回
		
         int n = 0; // 表示当前的字节数
         int i = 2; // 要截取的字节数，从第3个字节开始
         for (; i < bytes.length && n < length; i++)
         {
             // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
             if (i % 2 == 1)
             {
                 n++; // 在UCS2第二个字节时n加1
             }
             else
             {
                 // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                 if (bytes[i] != 0)
                 {
                     n++;
                 }
             }
         }
         // 如果i为奇数时，处理成偶数
         if (i % 2 == 1)

         {
             // 该UCS2字符是汉字时，去掉这个截一半的汉字
             if (bytes[i - 1] != 0)
                 i = i - 1;
             // 该UCS2字符是字母或数字，则保留该字符
             else
                 i = i + 1;
         }
         try {
			param = new String(bytes, 0, i, "Unicode");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
         return param;
    }
	/** 
	* @Title      : compareVersion  
	* @param      : @param version1
	* @param      : @param version2
	* @param      : @return 
	* @return     : int 
	* @throws 
	* @Description: 比较版本号的大小
	*/
	public static int compareVersion(String version1,String version2){
		String[] arr1 = version1.split("[.]");
		String[] arr2 = version2.split("[.]");
		int result = 0;
		int limit = arr1.length>=arr2.length?arr2.length:arr1.length;//取较小的为限制条件，防止数组越界
		for (int i = 0; i < limit; i++) {
			int temp1 = Integer.parseInt(arr1[i]);
			int temp2 = Integer.parseInt(arr2[i]);
			if(temp1>temp2){
				result = 1;
				break;
			}
			if(temp1<temp2){
				result = -1;
				break;
			}
		}
		if(result==0){
			//类似于2.2.1跟2.2的比较，前两位相等，但是明显2.2.1要大于2.2
			if(arr1.length>arr2.length) result=1;
			if(arr1.length<arr2.length) result=-1;
		}
		return result;
	}
    /*
	 * 测试主函数
	 */
	public static void main(String[] args) {
		System.out.println("Cent2Dollar(1) = " + Cent2Dollar("1"));
		System.out.println("Cent2Dollar(11) = " + Cent2Dollar("11"));
		System.out.println("Cent2Dollar(111) = " + Cent2Dollar("111"));
		System.out.println("Cent2Dollar(1111) = " + Cent2Dollar("1111"));
		System.out.println("Dollar2Cent(0.55) = " + Dollar2Cent("0.55"));
		System.out.println("Dollar2Cent(1) = " + Dollar2Cent("1"));
		System.out.println("Dollar2Cent(1.) = " + Dollar2Cent("1."));
		System.out.println("Dollar2Cent(1.1) = " + Dollar2Cent("1.1"));
		System.out.println("Dollar2Cent(1.11) = " + Dollar2Cent("1.11"));
		System.out.println("Dollar2Cent(1.234) = " + Dollar2Cent("1.234"));
		String splitStr = "#$ ,;";
		String ss = splitStr + "abde" + splitStr;
		System.out.println(trimLeft(splitStr, ss));
		System.out.println(trimRight(splitStr, ss));
		System.out.println(trimRight(splitStr, splitStr).length());
		System.out.println(subString("12310中联动123优势",1));
	}
}