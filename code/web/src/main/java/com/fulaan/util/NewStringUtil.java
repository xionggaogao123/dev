package com.fulaan.util;

/**
 * Created by James on 2018/2/7.
 */
public class NewStringUtil {

    /**
 * 处理转义字符问题，防止json数据混乱，导致flexgrid显示不出来
 * params:
 *  str:需要处理的字符串
 * return:
 *  res:处理后的字符 
 */
    public static String toGoodJsonStr(String str){
        StringBuffer res=new StringBuffer();

        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\"':
                    res.append("\\\"");
                    break;
                case '\\':
                    res.append("\\\\");
                    break;
                case '/':
                    res.append("\\/");
                    break;
                case '\b':
                    res.append("\\b");
                    break;
                case '\f':
                    res.append("\\f");
                    break;
                case '\n':
                    res.append("\\n");
                    break;
                case '\r':
                    res.append("\\r");
                    break;
                case '\t':
                    res.append("\\t");
                    break;
                case '\'':
                    res.append("\\\'");
                    break;
                default:
                    res.append(c);
            }
        }
        return res.toString();
    }
}
