package com.zkwg.modelmanager.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
	//盐，用于混交md5
	private static final String slat = "&%213123***&&%%$$#@";
	/**
	 * 生成md5
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		String base = str +"/"+slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

}