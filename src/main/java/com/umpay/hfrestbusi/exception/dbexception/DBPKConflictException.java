/** *****************  JAVA头文件说明  ****************
 * file name  :  DBPKConflictException.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 17, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.exception.dbexception;

import com.umpay.hfrestbusi.constants.BusiConst;




/** ******************  类说明  *********************
 * class       :  DBPKConflictException
 * @author     :  lingling
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class DBPKConflictException extends DBException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 762332800958706783L;
	
	public DBPKConflictException(){
		super(BusiConst.DB_PKCONFLICT);
	}

}



