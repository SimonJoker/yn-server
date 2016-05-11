package com.yunnong.utils;

public class CharUtil {

	/**
	 * @param str The String
	 * @param key
	 * @return Return the third string which are separated by special characters.
	 */
	public static String cut3String(String str, String key, int num){
		String[]  strs = str.split(key);
		int count = 0;
		for(String element  :  strs){
			count ++;
			if (count == num) {
				return element;
			}
		}
		return str;
	}
	
}
