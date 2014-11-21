/* *****************  JAVA头文件说明  ****************
 * file name  :  DBException.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Sep 21, 2010
 * *************************************************/ 

package com.umpay.hfrestbusi.exception.dbexception;

import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.exception.BusiException;





/* ******************  类说明  *********************
 * class       :  DBException
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  数据库错误
 * @see        :                        
 * ************************************************/

public class DBException extends BusiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5854418201557671352L;

	public DBException() {
		super( BusiConst.DB_ERROR);
	}
	
	public DBException(String errorCode) {
		super(errorCode);
	}
	
	public DBException(Throwable cause) {
		super( BusiConst.DB_ERROR,cause);
	}
	
	public DBException(String errorCode, Throwable cause) {
		super( errorCode,cause);
	}

}



