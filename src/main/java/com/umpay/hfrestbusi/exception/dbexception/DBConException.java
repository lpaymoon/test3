/** *****************  JAVA头文件说明  ****************
 * file name  :  DBConnectException.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 17, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.exception.dbexception;

import com.umpay.hfrestbusi.constants.BusiConst;


/** ******************  类说明  *********************
 * class       :  DBConnectException
 * @author     :  lingling
 * @version    :  1.0  
 * description :  数据库连接异常
 * @see        :                        
 * ************************************************/

public class DBConException extends DBException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8197124748736270931L;
	
	public DBConException(){
		super(BusiConst.DB_CONNECTERROR);
	}

}



