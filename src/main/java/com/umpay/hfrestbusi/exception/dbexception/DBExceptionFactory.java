/** *****************  JAVA头文件说明  ****************
 * file name  :  DBExceptionFactory.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 17, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.exception.dbexception;

import java.sql.SQLException;




/** ******************  类说明  *********************
 * class       :  DBExceptionFactory
 * @author     :  lingling
 * @version    :  1.0  
 * description :  数据库异常工厂类
 * @see        :                        
 * ************************************************/

public class DBExceptionFactory {
	
	/**
	 * ********************************************
	 * method name   : getDBException 
	 * description   : 当dal的返回结果为异常时，根据结果返回相应的异常信息
	 * @return       : DBException
	 * @param        : @param e
	 * @param        : @return
	 * modified      : lingling ,  Nov 17, 2011  2:32:48 PM
	 * @see          : 
	 * *******************************************
	 */
	public static DBException getDBException(Exception e){
		if(e instanceof SQLException){
			SQLException se = (SQLException)e;
			if(se==null){
				return new DBException(e);
			}
						
			//jdbc返回的sqlstate
			String sqlState = se.getSQLState();
			if(sqlState==null){
				return new DBException(e);
			}
			
			//连接异常
			if(sqlState.startsWith("08")){
				return new DBConException();
			}
			
			//权限异常
			if(sqlState.equals("42501")){
				return new DBAuthException();
			}
			
			//主键冲突
			if(sqlState.equals("23505")){
				return new DBPKConflictException();
			}						
		}
		return new DBException(e);
	}
	
}



