package com.umpay.hfrestbusi.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bs.mpsp.util.StringUtil;
import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.constants.BusiConst;

import org.apache.log4j.Logger;

/** ******************  类说明  *********************
 * class       :  GetRightRuleUtil
 * @author     :  zhuoyangyang
 * @version    :  1.0  
 * description :  获取最优配置
 * @see        :                        
 * ************************************************/  
public class GetRightRuleUtil {
	
	private static final Logger log = Logger.getLogger(GetRightRuleUtil.class);
	/**
	 * *****************  方法说明  *****************
	 * method name   :  findCondition
	 * @param  :  @param keys
	 * @param  :  @param funcode
	 * @param  :  @param msg
	 * @param  :  @return
	 * @param  :  @throws Exception
	 * @return  :  String
	 * @author   :  zhaoYan 2013-9-23 下午12:18:38 
	 * @Description  :  根据匹配条件，获取配置信息(有优先级关系)
	 * @see          : 
	 * @throws       :
	 * **********************************************
	 */
	public static String findCondition(String[] keys, String funcode, Map<String, String> msg,String rulePrefix)
	    throws Exception
	  {
	    int limitLen = keys.length;
	    if ((limitLen == 1) && (keys[0].trim().length() == 0)) {
	      log.debug("功能码:" + funcode + 
	        " 没有配置模板获取标签，请检查配置文件!");
	      return null;
	    }
	    StringBuilder limitStr = new StringBuilder();
	    for (int i = 0; i < limitLen; i++)
	    {
	      limitStr.append("1");
	    }

	    int limit = Integer.parseInt(limitStr.toString(), 2);
	    log.info("二进制limitStr:" + limitStr + " 转为十进制 limit:" + limit);
	    StringBuilder baseCaseStr = new StringBuilder();
	    String[] values = new String[limitLen];
	    StringBuilder tags = new StringBuilder();
	    for (int i = 0; i < limitLen; i++) {
	      tags.append(keys[i]).append(".");
	      String value = StringUtil.trim((String)msg.get(keys[i]));
	      values[i] = value;
	      if ("".equals(value)) {
	        baseCaseStr.append("0");
	        log.debug("功能码:" + funcode + " 模板查找key [无]:" + keys[i] +  "-->" + value);
	      } else {
	        baseCaseStr.append("1");
	        log.debug("功能码:" + funcode + " 模板查找key [有]:" + keys[i] + "-->" + value);
	      }
	    }

	    int baseCase = Integer.parseInt(baseCaseStr.toString(), 2);
	    Set used = new HashSet();

	    for (int i = limit; i >= 0; i--) {
	      int x = i & baseCase;
	      log.debug("第:" + i + " 次查找！ 相与后的键图:" + x + " baseCase:" + 
	        baseCase);
	      if (used.contains(Integer.valueOf(x))) {
	        continue;
	      }
	      used.add(Integer.valueOf(x));
	      StringBuilder condition = new StringBuilder();
	      for (int n = limitLen - 1; n >= 0; n--) {
	        if ((x & 0x1) == 1)
	          condition.insert(0, values[n] + ".");
	        else {
	          condition.insert(0, "*.");
	        }
	        x >>= 1;
	      }
	      if (condition.length() > 0) {
	        condition.deleteCharAt(condition.length() - 1);
	      }
	      String catchedStr = NamedProperties.getMapValue(BusiConst.PAYCHLPARAMS, rulePrefix+"."+condition.toString(), "");		
//	      String catchedStr = StringUtil.trim((String)rules.get(condition.toString()));
	      if ("".equals(catchedStr)) {
	        log.debug(">>功能码:" + funcode + " 条件:" + condition + 
	          " 没有找到模板配置，继续下一个");
	      }
	      else {
	        log.info("查找的键值:" + condition + " 模板:" + catchedStr);
	//        dc.put(funcode,condition.toString());
	        return catchedStr;
	      }
	    }
	    return "";
	  }
}
