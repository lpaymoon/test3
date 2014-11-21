/* *****************  JAVA头文件说明  ****************
 * file name  :  CheckReqUtil.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Oct 25, 2011
 * *************************************************/

package com.umpay.hfrestbusi.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.hfrestbusi.exception.BusiException;
import com.umpay.hfbusi.*;


/** ******************  类说明  *********************
 * class       :  CheckReqUtil
 * @author     :  lingling
 * @version    :  1.0  
 * description :  业务请求数据通用校验工具类
 * @see        :                        
 * ************************************************/   
public class CheckReqUtil {
	protected static Logger logger = Logger.getLogger(CheckReqUtil.class);
	private static Map<String, String> map = new HashMap<String, String>();
	private static String[] reqArrays = null;

	static {
		Field[] fields = HFBusiDict.class.getFields();
		for (Field f : fields) {
			try {
				map.put(f.getName(), f.get(null).toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * ********************************************
	 * method name   : checkReq 
	 * description   : 根据配置文件验证请求参数方法
	 * @return       : void
	 * @param        : @param className
	 * @param        : @param method
	 * @param        : @param args
	 * @param        : @param out
	 * @param        : @throws Exception
	 * modified      : lingling ,  Nov 4, 2011  11:04:47 AM
	 * @see          : 
	 * *******************************************
	 */
	public static void checkReq(String className, String method,
			Map<String, String> args, Map<String, Object> out) throws Exception {
		//Map<String,Object> context =null;
		// 从配置文件当中获取需要检查的请求参数，使用[]的是可选参数
		String[] cName = className.split("\\.");
		
		String busiKey = cName[cName.length - 1] + "." + method;
				
		String reqParams = NamedProperties.getMapValue(BusiConst.REQVALIDPARAMS,busiKey, "");
		if (reqParams == "") {
			System.out.println("请求的该方法没有配置对应的输入参数");
			out.put(BusiConst.MISSINGKEY, busiKey);
			throw new BusiException("86801007");
		}
		if(out.get(BusiConst.BUSIKEY)==null){//防止最外层参数被覆盖
		    out.put(BusiConst.BUSIKEY, busiKey);
		}

		reqArrays = reqParams.split(",");
		for (String key : reqArrays) {
			// 判断是否为必输参数，若参数使用[]扩住，说明为可选参数
			boolean isNeeded = true;
			String configKey = "";
			if (key.startsWith("[") && key.endsWith("]")) {
				// 非必须校验字段
				key = key.substring(1, key.length() - 1);
				isNeeded = false;
			}
			// 获取配置的key名称
			configKey = map.get(key);
			if (configKey == null) {
				logger.info(args.get("rpid")+"数据字典无此参数配置,参数为:"+key);
				//字典里查无此KEY
				out.put(BusiConst.MISSINGKEY, key);
				throw new BusiException("86801006");
			}
			
			// 取得args中configKey对应的value值
			String value = args.get(configKey);
			
			if (value==null||value.equals("")) {//如果参数为null或者为空串
				// 如果是必须包含的值，抛出异常。否则continue
				if (isNeeded) {
					logger.info(args.get("rpid")+"必输参数没有输入值,参数为:"+configKey);
					out.put(BusiConst.MISSINGKEY, configKey);
					throw new BusiException("86801000");
				} else {
					continue;
				}
			}
			
			out.put(configKey, value);
			
			// args中包含该key，进行正则效验
			boolean isOk = false;

			// 取得configKey对应在配置文件中对应的正则表达式
			String regTag = NamedProperties.getMapValue(BusiConst.REQVALIDPARAMS,
					"CheckReq." + key, "");
			if (regTag.equals("")) {
				// 没有找到配置的正则表达式
				continue;
			} else {			
				// 一个字段在业务服务器中可能用于多种含义的字段，每种规则都进行校验，必须匹配一种规则
				String regs[] = regTag.split("[@]");
				for (String reg : regs) {
					if (value.matches(reg)) {
						isOk = true;
						break;
					}
				}
			}
			if (!isOk) {
				logger.info(args.get("rpid")+"参数校验未通过，参数为:"+configKey);
				throw new BusiException("86801001");// 这里抛出验证未通过的错误码
			}
		}

	}

	/**
	 * ********************************************
	 * method name   : getDictMapping 
	 * description   : 
	 * @return       : void
	 * @param        : 
	 * modified      : lingling ,  Nov 4, 2011  11:39:03 AM
	 * @see          : 
	 * *******************************************
	 */
	public static Map<String, String> getDictMapping(){
		return map;
	}
	
	/**
	 * ********************************************
	 * method name   : checkMapElement 
	 * description   : 判断传入的key值在m中是否全部存在
	 * @return       : boolean
	 * @param        : @param m
	 * @param        : @param keys
	 * @param        : @return
	 * modified      : lingling ,  Nov 11, 2011  8:49:23 PM
	 * @see          : 
	 * *******************************************
	 */
	public static boolean checkMapElement(Map<String, Object> m ,String ... keys){
		boolean re = true;
		
		for(String key : keys){
			if(!m.containsKey(key)){
				re =false;
				return re;
			}else if(m.get(key) == null){
				re =false;
				return re;
			}
		}
		return re;
	}

}
