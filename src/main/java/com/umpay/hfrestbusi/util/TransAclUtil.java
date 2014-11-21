package com.umpay.hfrestbusi.util;

import java.util.regex.Pattern;
/**
 * 交易屏蔽工具类
 * @author zhangjiandong 
 */
 
public class TransAclUtil {
	/**
	 * 检查是否匹配
	 * @param merId
	 * @param goodsId
	 * @param provCode
	 * @param areaCode
	 * @param netType
	 * @param cardType
	 * @param mobileId
	 * @param regex
	 * @return
	 */ 
	public static boolean checkRegex(String merId, String goodsId,
			String provCode, String areaCode, String netType, String cardType,
			String mobileId, String bankType, String goodsType, String grade, String regex) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(merId).append("#").append(goodsId).append("#").append(
				provCode).append("#").append(areaCode).append("#").append(
				netType).append("#").append(cardType).append("#").append(
				mobileId).append("#").append(bankType).append("#").append(
				goodsType).append("#").append(grade);		 
		return Pattern.matches(regex, buffer.toString());
	} 
    /**
     * 根据屏蔽项生成正则表达式
     * @param merId
     * @param goodsId
     * @param provCode
     * @param areaCode
     * @param netType
     * @param cardType
     * @param mobileId
     * @return
     */
//	public static String translateRegex(Transacl transacl){
//		return translateRegex(transacl.getMerId(),transacl.getGoodsId(),transacl.getProvCode(),
//				transacl.getAreaCode(),transacl.getNetType(),transacl.getCardType(),
//				transacl.getMobileId(),transacl.getBankType(),transacl.getGoodsType(),transacl.getGrade());
//	}
	public static String translateRegex(String merId, String goodsId,
			String provCode, String areaCode, int netType, String cardType,
			String mobileId, int bankType, int goodsType, int grade) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("^");
		if (merId.equals("ALL")) {
			buffer.append("[0-9]{4}");
		} else {
			buffer.append(merId);
		}
		buffer.append("#");
		if (goodsId.equals("ALL")) {
			buffer.append("[0-9]{3,6}");
		} else {
			buffer.append(goodsId);
		}
		buffer.append("#");
		if (provCode.equals("ALL")) {
			buffer.append("[0-9]{3}");
		} else {
			buffer.append(provCode);
		}
		buffer.append("#");
		if (areaCode.equals("ALL")) {
			buffer.append("[0-9]{3}");
		} else {
			buffer.append(areaCode);
		}
		buffer.append("#");
		if (netType == -1) {
			buffer.append("[0,1]{1}");
		} else {
			buffer.append(netType);
		}
		buffer.append("#");
		if (cardType.equals("ALL")) {
			buffer.append("[0-9]{1,2}");
		} else {
			buffer.append(cardType);
		}
		buffer.append("#");
		if (mobileId.equals("ALL")) {
			buffer.append("[0-9]{11}");
		} else {
			int l = 11 - mobileId.length();
			buffer.append(mobileId + "[0-9]{" + l + "}");
		}
		buffer.append("#");
		if(bankType == -1){
			buffer.append("[0,1]{1}");
		}else{
			buffer.append(bankType);
		}
		buffer.append("#");
		if(goodsType == -1){
			buffer.append("[0,1,2]{1}");
		}else{
			buffer.append(goodsType);
		}
		buffer.append("#");
		if(grade == -1){
			buffer.append("[0-9]{1,2}");
		}else{
			buffer.append(grade);
		}
		buffer.append("$");
		return buffer.toString();
	}
}
