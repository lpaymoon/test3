package com.umpay.hfrestbusi.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umpay.hfbusi.HFBusiDict;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.core.BaseRest;
import com.umpay.hfrestbusi.util.StringUtil;
import com.umpay.hfrestbusi.util.TimeUtil;
/** ******************  类说明  *********************
 * class       :  HfWxTransRecordRest
 * @author     :  panxingwu
 * @version    :  1.0  
 * description :  无线用户交易记录查询
 * @see        :                        
 * ************************************************/   
public class HfWxTransRecordRest extends BaseRest {

	@Override
	public String doListService(Map<String, String> urlargs) throws Exception {
		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
		//transmonth和monthsign如果都传入值了,则以transmonth为准
		String transmonth = StringUtil.trim(urlargs.get(HFBusiDict.TRANSMONTH));
		String monthsign = StringUtil.trim(urlargs.get(HFBusiDict.MONTHSIGN));//0:全部交易（三个月的） 1：当月   2：上月
		String platdate = "";
		String platdateEnd="";
		int flag = 0;
		if(!"".equals(monthsign)) flag=Integer.parseInt(monthsign);
		List<Map<String, Object>> rsMap = null;
		
		if(!"".equals(transmonth)){//传入的查询月份不为空，则按月份查询
			//校验日期
			if(!isRightDate(transmonth)){
				logInfo("查询日期校验未通过，传入日期为:%s", transmonth);
				return "86801115";
			}
			platdate = transmonth+"01";
	        SimpleDateFormat format = new SimpleDateFormat( "yyyyMMdd"); 
	        //对应的Date实例 
	        Date date = format.parse(platdate); 
	        //获得Calendar实例 
	        Calendar calendar = Calendar.getInstance(); 
	        //根据date赋值 
	        calendar.setTime(date); 
	        calendar.add(Calendar.MONTH, 1);
	        calendar.add(Calendar.DATE,-1); 
	        platdateEnd = format.format(calendar.getTime());//transmonth的最后一天
	        
	        Map<String,String> map = new HashMap<String,String>();
	        String mwTable = transmonth.substring(2);//全网交易表标识
	        map.put(HFBusiDict.MOBILEID+".string", mobileid);
			map.put("platdateStart.string", platdate);
			map.put("platdateEnd.string", platdateEnd);
			map.put("_mwTable", mwTable);
			map.put("_tableNameXE", transmonth);//20140416 add by liujilong 读库迁移,从ALL表转为月表
			rsMap = dal.find("db3","psql_TRANSRECODER.getTransRecoder", map);
		}
//		if(flag==0){
//			//String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
//			Map<String,String> map = new HashMap<String,String>();
//			Calendar c1 = Calendar.getInstance();   
//			c1.add(c1.MONTH,-1);//得到上个月的月份   
//			Date d1 = c1.getTime(); 
//			String tableName1 = TimeUtil.date8().substring(0, 6);
//			String tableName2 = new SimpleDateFormat("yyyyMM").format(d1);
//			map.put(HFBusiDict.MOBILEID+".string", mobileid);
//			map.put("_xetableName1",tableName1);//小额当月交易表
//			map.put("_xetableName2",tableName2);//小额上月交易表
//			map.put("_mwtableName1",tableName1.substring(2));//全网当月交易表
//			map.put("_mwtableName2",tableName2.substring(2));//全网上月交易表
//			rsMap = dal.find("db3","psql_TRANSRECODER.getTransRecoder", map);
//		}
		else{
			//跟据月份标识判断要查询的是当月还是上月的交易记录
			switch (flag) {
			case 1:
				platdate = TimeUtil.date8().substring(0, 6);
				platdate=platdate+"01";
				platdateEnd=TimeUtil.date8().substring(0, 8);
				break;
			case 2:
				Calendar c = Calendar.getInstance();   
				c.add(c.MONTH,-1);//得到上个月的月份   
				Date d = c.getTime(); 
				platdate = new SimpleDateFormat("yyyyMM").format(d);
				platdate=platdate+"01";
				platdateEnd =TimeUtil.date8().substring(0, 6)+"01";
				break;
			case 3:
				Calendar c1 = Calendar.getInstance();   
				c1.add(c1.MONTH,-1);//得到上个月的月份   
				Date d1 = c1.getTime(); 
				platdate = new SimpleDateFormat("yyyyMM").format(d1);
				platdate=platdate+"01";
				platdateEnd=TimeUtil.date8().substring(0, 8);
				break;
			}
			Map<String,String> map = new HashMap<String,String>();
			logInfo("查询月份platdate=%s", platdate);
			map.put(HFBusiDict.MOBILEID+".string", mobileid);
			map.put("platdateStart.string", platdate);
			map.put("platdateEnd.string", platdateEnd);
			map.put("_tableNameXE", platdate.substring(0, 6));
			rsMap = dal.find("db3","psql_TRANSRECODER.oldgetTransRecoder", map);	
		}
		
		//根据传入月份查询交易记录
		
		
//		String mobileid = StringUtil.trim(urlargs.get(HFBusiDict.MOBILEID));
//		Map<String,String> map = new HashMap<String,String>();
//		Calendar c1 = Calendar.getInstance();   
//		c1.add(c1.MONTH,-1);//得到上个月的月份   
//		Date d1 = c1.getTime(); 
//		String tableName1 = TimeUtil.date8().substring(0, 6);
//		String tableName2 = new SimpleDateFormat("yyyyMM").format(d1);
//		map.put(HFBusiDict.MOBILEID+".string", mobileid);
//		map.put("_xetableName1",tableName1);//小额当月交易表
//		map.put("_xetableName2",tableName2);//小额上月交易表
//		map.put("_mwtableName1",tableName1.substring(2));//全网当月交易表
//		map.put("_mwtableName2",tableName2.substring(2));//全网上月交易表
//		List<Map<String, Object>> rsMap = dal.find("db3","psql_TRANSRECODER.getTransRecoder", map);
		System.out.println("查询到的交易记录为:"+rsMap.size());
		if(rsMap==null||rsMap.size()==0){
			logInfo("无交易记录!");
			return "86801111";
		}
		out.put("transdata",rsMap);
		logInfo("查询交易记录成功!");
		return BusiConst.SUCCESS;
	}

	/** ********************************************
	 * method name   : isRightDate 
	 * description   : 校验查询月份是否是包括当前月份在内的前四个月
	 * @return       : boolean
	 * @param        : @param transmonth
	 * @param        : @return
	 * modified      : panxingwu ,  2013-6-6  上午9:43:56
	 * @see          : 
	 * ********************************************/      
	private boolean isRightDate(String transmonth) {
		boolean flag = false;
        SimpleDateFormat format = new SimpleDateFormat( "yyyyMMdd"); 
        Date date = null;
        transmonth=transmonth+"01";//设置为当月1号
        try {
			date = format.parse(transmonth);
		} catch (ParseException e) {
			logInfo("日期转换失败:%s", transmonth);
		}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long time = calendar.getTimeInMillis();
        
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(new Date());
        calendarToday.set(Calendar.HOUR_OF_DAY, 0);  
        calendarToday.set(Calendar.MINUTE, 0);  
        calendarToday.set(Calendar.SECOND, 0);  
        calendarToday.set(Calendar.MILLISECOND, 0); 
        calendarToday.set(Calendar.DAY_OF_MONTH,1);//设置成当月第一天
        
        calendarToday.add(Calendar.MONTH, 0);//设置成当月
        long endTime = calendarToday.getTimeInMillis();
        
        calendarToday.add(Calendar.MONTH, -3);//设置成往前推第三个月
        logInfo("可查询到最早的交易记录:%s", format.format(calendarToday.getTime()));
        long startTime = calendarToday.getTimeInMillis();
        
        if(startTime<=time&&time<=endTime){
        	flag=true;
        }
		return flag;
	}

	public static void main(String[] args) {
		System.out.println(TimeUtil.date8().substring(0, 8));
		System.out.println(TimeUtil.date8().substring(0, 6) + "01");
	}
}
