package com.umpay.hfrestbusi.util;

import java.util.HashMap;

public class IccidUtil {
	/**
	 * 根据移动iccid映射地区号
	 * @param provCode
	 * @return
	 */
	public static String matchYdProv(String provCode) {
		String resProvCode = "";
		if ("01".equals(provCode)) {//北京
		resProvCode = "010";
		} else if ("02".equals(provCode)) {//天津
		resProvCode = "022";
		} else if ("03".equals(provCode)) {//河北
		resProvCode = "311";
		} else if ("04".equals(provCode)) {//山西
		resProvCode = "351";
		} else if ("05".equals(provCode)) {//内蒙古
		resProvCode = "471";
		} else if ("06".equals(provCode)) {//辽宁
		resProvCode = "024";
		} else if ("07".equals(provCode)) {//吉林
		resProvCode = "431";
		} else if ("08".equals(provCode)) {//黑龙江
		resProvCode = "451";
		} else if ("09".equals(provCode)) {//上海
		resProvCode = "021";
		} else if ("10".equals(provCode)) {//江苏
		resProvCode = "025";
		} else if ("11".equals(provCode)) {//浙江
		resProvCode = "571";
		} else if ("12".equals(provCode)) {//安徽
		resProvCode = "551";
		} else if ("13".equals(provCode)) {//福建
		resProvCode = "591";
		} else if ("14".equals(provCode)) {//江西
		resProvCode = "791";
		} else if ("15".equals(provCode)) {//山东
		resProvCode = "531";
		} else if ("16".equals(provCode)) {//河南
		resProvCode = "371";
		} else if ("17".equals(provCode)) {//湖北
		resProvCode = "027";
		} else if ("18".equals(provCode)) {//湖南
		resProvCode = "731";
		} else if ("19".equals(provCode)) {//广东
		resProvCode = "020";
		} else if ("20".equals(provCode)) {//广西
		resProvCode = "771";
		} else if ("21".equals(provCode)) {//海南
		resProvCode = "898";
		} else if ("22".equals(provCode)) {//四川
		resProvCode = "028";
		} else if ("23".equals(provCode)) {//贵州
		resProvCode = "851";
		} else if ("24".equals(provCode)) {//云南
		resProvCode = "871";
		} else if ("25".equals(provCode)) {//西藏
		resProvCode = "891";
		} else if ("26".equals(provCode)) {//陕西
		resProvCode = "029";
		} else if ("27".equals(provCode)) {//甘肃
		resProvCode = "931";
		} else if ("28".equals(provCode)) {//青海
		resProvCode = "971";
		} else if ("29".equals(provCode)) {//宁夏
		resProvCode = "951";
		} else if ("30".equals(provCode)) {//新疆
		resProvCode = "991";
		} else if ("31".equals(provCode)) {//重庆
		resProvCode = "023";
		}
		return resProvCode;
		}
	
	
	
	/**
	 * 根据 联通iccid映射地区号
	 * @param provCode
	 * @return
	 */
	public static String matchLTProv(String provCode) {
		String resProvCode = "";
		if ("11".equals(provCode)) {//北京
		resProvCode = "010";
		} else if ("13".equals(provCode)) {//天津
		resProvCode = "022";
		} else if ("18".equals(provCode)) {//河北
		resProvCode = "311";
		} else if ("19".equals(provCode)) {//山西
		resProvCode = "351";
		} else if ("10".equals(provCode)) {//内蒙古
		resProvCode = "471";
		} else if ("91".equals(provCode)) {//辽宁
		resProvCode = "024";
		} else if ("90".equals(provCode)) {//吉林
		resProvCode = "431";
		} else if ("97".equals(provCode)) {//黑龙江
		resProvCode = "451";
		} else if ("31".equals(provCode)) {//上海
		resProvCode = "021";
		} else if ("34".equals(provCode)) {//江苏
		resProvCode = "025";
		} else if ("36".equals(provCode)) {//浙江
		resProvCode = "571";
		} else if ("30".equals(provCode)) {//安徽
		resProvCode = "551";
		} else if ("38".equals(provCode)) {//福建
		resProvCode = "591";
		} else if ("75".equals(provCode)) {//江西
		resProvCode = "791";
		} else if ("17".equals(provCode)) {//山东
		resProvCode = "531";
		} else if ("76".equals(provCode)) {//河南
		resProvCode = "371";
		} else if ("71".equals(provCode)) {//湖北
		resProvCode = "027";
		} else if ("74".equals(provCode)) {//湖南
		resProvCode = "731";
		} else if ("51".equals(provCode)) {//广东
		resProvCode = "020";
		} else if ("59".equals(provCode)) {//广西
		resProvCode = "771";
		} else if ("50".equals(provCode)) {//海南
		resProvCode = "898";
		} else if ("81".equals(provCode)) {//四川
		resProvCode = "028";
		} else if ("85".equals(provCode)) {//贵州
		resProvCode = "851";
		} else if ("86".equals(provCode)) {//云南
		resProvCode = "871";
		} else if ("79".equals(provCode)) {//西藏
		resProvCode = "891";
		} else if ("84".equals(provCode)) {//陕西
		resProvCode = "029";
		} else if ("87".equals(provCode)) {//甘肃
		resProvCode = "931";
		} else if ("70".equals(provCode)) {//青海
		resProvCode = "971";
		} else if ("88".equals(provCode)) {//宁夏
		resProvCode = "951";
		} else if ("89".equals(provCode)) {//新疆
		resProvCode = "991";
		} else if ("83".equals(provCode)) {//重庆
		resProvCode = "023";
		}
		return resProvCode;
		}

}
