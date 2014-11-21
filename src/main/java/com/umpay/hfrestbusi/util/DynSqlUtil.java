package com.umpay.hfrestbusi.util;

/* *****************  JAVAͷ�ļ�˵��  ****************
 * file name  :  DynSqlUtil.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 19, 2010
 * *************************************************/ 



import java.util.HashMap;
import java.util.Map;




/* ******************  ��˵��  *********************
 * class       :  DynSqlUtil
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  ��ɶ�̬��乤����
 * @see        :                        
 * ************************************************/

public class DynSqlUtil {
	
	
	private static String getSql(Map<String , Object> map ,String spliter){
		StringBuffer sb = new StringBuffer();
		for(String key : map.keySet()){
			Object o = map.get(key);
			if(o.getClass().getName().equals("java.lang.String")){
				sb.append(key);
				sb.append("=");
				//用于两个表的级联查询
				if(o.toString().indexOf(".") != -1){
					sb.append(o.toString());
				}else{
					sb.append("'");				
					sb.append(o.toString());
					sb.append("'");				
				}
			}else{
				sb.append(key);
				if(key.indexOf(">")==-1 && key.indexOf("<")==-1){
					sb.append("=");
				}
				sb.append(o.toString());
			}
			sb.append(spliter);			
		}
		return sb.length()==0?"1=1":sb.substring(0, sb.length()-spliter.length());
	}
	
	
	public static String getWhereSql (Map<String , Object> map){
		return getSql(map , " and ");
	}
	
	public static String getUpdateSql (Map<String , Object> map){
		return getSql(map , " , ");
	}
	
	public static void main(String[] args){
		Map<String , Object> map = new HashMap<String,Object>();
		map.put("a", "a");
		map.put("b", 1);
		map.put("c", "a");
		
		System.out.println(getUpdateSql(map));
	}
}



