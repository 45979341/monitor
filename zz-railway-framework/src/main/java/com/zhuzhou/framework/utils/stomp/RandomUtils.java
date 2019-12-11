package com.zhuzhou.framework.utils.stomp;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 随机字符串帮助类，提供静态方法，不可以实例化。 
 */
public class RandomUtils {

	/**
	 * 产生随机字符串，长度由参数指定。
	 * 
	 * @param length
	 *            产生的字符串的长度
	 * @return 已产生的字符串
	 */
	public static String getRandString(int length) {
		String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
		return RandomStringUtils.random(length, chars);
	}

	/**
	 * 产生随机字符串，长度由参数指定。
	 * 
	 * @param length
	 *            产生的字符串的长度
	 * @return 已产生的字符串
	 */
	public static String getRandNumber(int length) {
		String chars = "0123456789";
		return RandomStringUtils.random(length, chars);
	}

	/**
	 * 产生随机字符串，长度由参数指定。
	 * 
	 * @param chars
	 *            基础字符集合
	 * @param length
	 *            产生的字符串的长度
	 * @return 已产生的字符串
	 */
	public static String getRandString(String chars, int length) {
		return RandomStringUtils.random(length, chars);
	}

	/**
	 * 产生随机汇款金额
	 * 
	 * @return 已产生的金额
	 */
	public static String getRandAmount() {
		String number= RandomStringUtils.random(1, "123456789");
		String rev = "0."+number;
		return rev;
	}
	
	/** 
	 * 随机指定范围内N个不重复的数 
	 * @param min 指定范围最小值 
	 * @param max 指定范围最大值 
	 * @param n 随机数个数 
	 */  
	public static long[] randomCommon(long min, long max, int n){
		if (n > (max - min + 1) || max < min) {  
	           return null;  
	       } 
		long[] result = new long[n];  
	    int count = 0;
	    while(count < n) {  
	        long num = (long) (Math.random() * (max - min)) + min;  
	        boolean flag = true;  
	        for (int j = 0; j < n; j++) {  
	            if(num == result[j]){  
	                flag = false;  
	                break;  
	            }  
	        }  
	        if(flag){  
	            result[count] = num;  
	            count++;  
	        }  
	    } 
		return result;
	}
}
