package com.umpay.hfrestbusi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类
 * 
 * @author sunxiaoshi
 *
 */
public class TimeUtil {
//    private static final SimpleDateFormat fmtMonth6 = new SimpleDateFormat("yyyyMM");
//    private static final SimpleDateFormat fmtDate8 = new SimpleDateFormat("yyyyMMdd");
//    private static final SimpleDateFormat fmtTime6 = new SimpleDateFormat("HHmmss");
//    private static final SimpleDateFormat fmtDatetime14 = new SimpleDateFormat("yyyyMMddHHmmss");
    
    public static String date(String fmt) {
        return new SimpleDateFormat(fmt).format(new Date());
    }

    public static String date(String fmt, long t) {
        return new SimpleDateFormat(fmt).format(new Date(t));
    }

    public static String date8() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    public static String date8(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }
    
    public static String time6() {
        return new SimpleDateFormat("HHmmss").format(new Date());
    }

    public static String datetime14() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static String datetime14(Date date) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    }
    
    public static String datetime14(long t) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(t));
    }
    
    public static String calcMonth(String month6, int m) {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(month6.substring(0, 4)), Integer.parseInt(month6.substring(4, 6))-1, 1);
        cal.add(Calendar.MONTH, m);
        return new SimpleDateFormat("yyyyMM").format(cal.getTime());
    }
    
	public static String calcDay(String day8, int d) {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(day8.substring(0, 4)), Integer.parseInt(day8.substring(4, 6))-1, Integer.parseInt(day8.substring(6, 8)));
		cal.add(Calendar.DATE, d);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}
	
    public static String calcSecond(String time14, int s) {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(time14.substring(0, 4)), Integer.parseInt(time14.substring(4, 6))-1, Integer.parseInt(time14.substring(6, 8)),
                Integer.parseInt(time14.substring(8, 10)), Integer.parseInt(time14.substring(10, 12)), Integer.parseInt(time14.substring(12, 14)));
        cal.add(Calendar.SECOND, s);
        return new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
    }
    
    public static long toMilliSec(String time14) {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(time14.substring(0, 4)), Integer.parseInt(time14.substring(4, 6))-1, Integer.parseInt(time14.substring(6, 8)),
                Integer.parseInt(time14.substring(8, 10)), Integer.parseInt(time14.substring(10, 12)), Integer.parseInt(time14.substring(12, 14)));
        return cal.getTimeInMillis();
    }

    public static int getActualMaximum(String day8, int field) {
        Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(day8.substring(0, 4)), Integer.parseInt(day8.substring(4, 6))-1, Integer.parseInt(day8.substring(6, 8)));
        return cal.getActualMaximum(field);
    }
    
    public static int getActualMinimum(String day8, int field) {
        Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(day8.substring(0, 4)), Integer.parseInt(day8.substring(4, 6))-1, Integer.parseInt(day8.substring(6, 8)));
        return cal.getActualMinimum(field);
    }

	public static void main(String args[]) {
	    System.out.println("yyyyMMddHH=" + date("yyyyMMddHH"));
	}
}