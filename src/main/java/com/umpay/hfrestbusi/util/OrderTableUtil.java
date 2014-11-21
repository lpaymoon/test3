/* *****************  JAVA头文件说明  ****************
 * file name  :  OrderTableUtil.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  2008-11-17
 * *************************************************/ 

package com.umpay.hfrestbusi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bs.mpsp.util.DateTimeUtil;




/* ******************  类说明  *********************
 * class       :  OrderTableUtil
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class OrderTableUtil {
	
	public static String getCurTableName(){
		return getTableNameByDate(DateTimeUtil.getDateString(DateTimeUtil.currentDateTime()));
	}
	
	public static String getTableNameByDate(String date){
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date temp = null;
		try {
			temp = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			temp = new Date();
		}
		calendar.setTime(temp);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return  "T_ORDER_"+dayOfWeek;
	}
	
	public static String getLastTableName(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return  "T_ORDER_"+dayOfWeek;
	}
	
	public static String getTableNameByPorderId(String porderId){
		if(porderId==null||porderId==""){
			return "T_ORDER_0";
		}else{
			return "T_ORDER_"+porderId.substring(porderId.length()-1);		
		}		
	}
	
	
	public static String getCurTableNum(){
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return dayOfWeek+"";		
	}
	
	public static String getTableNumByDate(String date){
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date temp = null;
		try {
			temp = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			temp = new Date();
		}
		calendar.setTime(temp);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return dayOfWeek+"";
	}
	
	public static void main(String[] arg){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String date = format.format(new Date());
		System.out.println(date);
		//System.out.println(getCurTableName());
		System.out.println(getTableNameByDate("20111106"));
		//System.out.println(getLastTableName());
	}
}
