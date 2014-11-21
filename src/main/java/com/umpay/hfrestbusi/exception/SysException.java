/* *****************  JAVA头文件说明  ****************
 * file name  :  SysException.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Sep 21, 2010
 * *************************************************/ 

package com.umpay.hfrestbusi.exception;


import com.umpay.hfrestbusi.constants.BusiConst;




/* ******************  类说明  *********************
 * class       :  SysException
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  系统未知错误
 * @see        :                        
 * ************************************************/

public class SysException extends BusiException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7531375118793668463L;

	public SysException() {
		super( BusiConst.SYS_ERROR);
	}
	
	public SysException(Throwable cause) {
		super( BusiConst.SYS_ERROR , cause);
	}
}



