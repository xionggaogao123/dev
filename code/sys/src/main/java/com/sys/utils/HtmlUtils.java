package com.sys.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.sys.constants.Constant;

/**
 * 防止JS注入
 * @author fourer
 *
 */
public class HtmlUtils {

	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式

	public static String delScriptTag(String htmlStr) {
		
		if(StringUtils.isBlank(htmlStr))
			return htmlStr;
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(Constant.EMPTY); 

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(Constant.EMPTY); 

		return htmlStr.trim(); 
	}
	
	
	public static void main(String[] args) {
		String str = "dd"; 
		
		System.out.println(HtmlUtils.delScriptTag(str));
	}
}
