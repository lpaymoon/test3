/** *****************  JAVA头文件说明  ****************
 * file name  :  BusiConst.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Oct 25, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.constants;




/** ******************  类说明  *********************
 * class       :  BusiConst
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  系统静态参数
 * @see        :                        
 * ************************************************/

public class BusiConst {
	
	/**
	 * 系统配置参数
	 */
	public static final String SYSPARAMS = "SYSTEM.PARAMS";
	
	/**
	 * 响应配置参数
	 */
	public static final String MESSAGEPARAMS = "MESSAGE.PARAMS";
	
	/**
	 * 需匹配的字段
	 */
	public static final String REQVALIDPARAMS = "REQVALID.PARAMS";
	
	/**
	 * 需匹配的支付方式配置文件
	 */
	public static final String PAYCHLPARAMS = "PAYCHL.PARAMS";
	/**
	 * DOLIST方法
	 */
	public static final String DOLIST = "DOLIST";
	
	/**
	 * DOCREATE方法
	 */
	public static final String DOCREATE = "DOCREATE";
	
	/**
	 * DOSHOW方法
	 */
	public static final String DOSHOW = "DOSHOW";
	
	/**
	 * DOUPDATE方法
	 */
	public static final String DOUPDATE = "DOUPDATE";
	
	/**
	 * 标识为本地资源调用
	 */
	public static final String LOCAL = "LOCAL";
	
	/**
	 * 业务处理时间
	 */
	public static final String BUSISPENDTIME = "BUSISPENDTIME";
	
	
	/**
	 * 业务ID
	 */
	public static final String BUSIKEY = "BUSIKEY";
	
	/**
	 *  返回成功
	 */	
	public static final String SUCCESS="0000";
	
	/**
	 * 系统未知错误
	 */
	public static final String SYS_ERROR="86809999";
	
	/**
	 * 数据库错误
	 */
	public static final String DB_ERROR="86800200";
	
	/**
	 * 数据库连接异常
	 */
	public static final String DB_CONNECTERROR="86800201";
	
	/**
	 * 数据库权限异常
	 */
	public static final String DB_AUTHERROR="86800202";
	
	/**
	 * 数据库插入主键冲突
	 */
	public static final String DB_PKCONFLICT="86800203";
	
	
	

	/**
	 * 找不到所需要的更新数据
	 */
	public static final String UPDATENULL = "86800205";
	
	/**
	 * 不支持编码
	 */
	public static final String UNSUPPORTENCODING = "";
	
	public static final String MISSINGKEY = "MISSINGKEY";
	
	public static final String FILTERKEY = "FILTERKEY";
	
	public static final String LOGDATA = "LOGDATA";
	
	/**
	 * 综合支付请求小额平台时传的merpriv
	 */
	public static final String XE_MERPRIV = "VIRPREMPU000001";
	
	/**
	 * 综合支付请求小额平台时传的merpriv
	 */
	public static final String XE_VERSION = "3.0";
	
	/**
	 * 小额银行前缀
	 */
	public static final String PRE_BANKID_XE = "XE";
	
	/**
	 * 游戏基地银行前缀
	 */
	public static final String PRE_BANKID_GM = "GM";
	
	/**
	 * 游戏基地IDO流程的银行
	 */
	public static final String BANKID_OFFLINE_GM = "GM000000";
}



