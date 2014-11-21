/** *****************  JAVA头文件说明  ****************
 * file name  :  DBAuthException.java
 * owner      :  lingling
 * copyright  :  UMPAY
 * description:  
 * modified   :  Nov 17, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.exception.dbexception;

import com.umpay.hfrestbusi.constants.BusiConst;




/** ******************  类说明  *********************
 * class       :  DBAuthException
 * @author     :  lingling
 * @version    :  1.0  
 * description :  数据库权限异常
 * @see        :                        
 * ************************************************/

public class DBAuthException extends DBException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5418032665324372594L;
	
	public DBAuthException(){
		super(BusiConst.DB_AUTHERROR);
	}

}



