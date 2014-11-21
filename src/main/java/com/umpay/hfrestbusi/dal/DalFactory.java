package com.umpay.hfrestbusi.dal;

/**
 * ******************  类说明  *********************
 * class       :  DalFactory
 * @author     :  lingling
 * @version    :  1.0  
 * description :  获取local dal 的工厂类
 * @see        :                        
 * ***********************************************
 */
public class DalFactory {
	
	/**
	 * 
	 */
	public static CommonDalInf getDal(){
		return new LocalDal();
	}
}
