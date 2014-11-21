/* *****************  JAVA头文件说明  ****************
 * file name  :  BusiException.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Sep 20, 2010
 * *************************************************/ 

package com.umpay.hfrestbusi.exception;

import com.umpay.hfrestbusi.util.StringUtil;






/** ******************  类说明  *********************
 * class       :  BusiException
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  业务异常类
 * @see        :                        
 * ************************************************/

public class BusiException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3139345456945204302L;

	public BusiException(String errorCode){
		super(errorCode);
	}
	
	public BusiException(String errorCode, Throwable cause) {
		super(errorCode,cause);
	}
	
	public static boolean isBusiException(Throwable e){
		
		if(e instanceof BusiException){
			return true;
		}
		if(StringUtil.isDigitalString(e.getMessage())){
			return true;
		}
		return false;			
	}
}



