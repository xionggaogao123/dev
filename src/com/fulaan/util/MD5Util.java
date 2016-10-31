package com.fulaan.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具
 * @author xusy
 */
public class MD5Util {
	
	public static final String[] HEX = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
			"a", "b", "c", "d", "e", "f"};
	
	/**
	 * 将byte转换为hex字符串
	 * @param b
	 * @return
	 */
	private static String byteToHex(byte b) {
		int num = b;
		if(num < 0) {
			num += 256;
		}
		
		int high = num / 16;
		int low = num % 16;
		
		return HEX[high] + HEX[low];
	}
	
	/**
	 * 将字节数字转换为字符串
	 * @param bytes
	 * @return
	 */
	private static String bytesToString(byte[] bytes) {
		
		StringBuffer buff = new StringBuffer();
		
		for(byte b : bytes) {
			buff.append(byteToHex(b));
		}
		
		return buff.toString();
	}
	
	/**
	 * md5加密
	 * @param password
	 * @return
	 */
	public static String MD5Encode(String password) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = bytesToString(md.digest(password.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Util.MD5Encode("1"));
	}
	
}
