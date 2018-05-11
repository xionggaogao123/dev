package com.fulaan.excellentCourses.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by James on 2018-05-08.
 */
public class RoomUtil {

    /**
     * 功能：将一个Map按照Key字母升序构成一个QueryString. 并且加入时间混淆的hash串
     * @param queryMap  query内容
     *  加密时候，为当前时间；解密时，为从querystring得到的时间；
     * @param salt   加密salt
     * @return
     */

    public static String createHashedQueryString(Map<String, String> queryMap, String salt) {

        //Map<String, String> map = new TreeMap<String, String>(queryMap);
        String qs = createQueryString(queryMap); //生成queryString方法可自己编写
        if (qs == null || qs.equals("")) {
            return null;
        }
        long time = new Date().getTime() / 1000;
        String thqs = "";
        try{
            String hash =md5(String.format("%s&time=%s&salt=%s", qs, time, salt));
            System.out.print(hash);
            hash = hash.toUpperCase();
            thqs = String.format("%s&time=%d&hash=%s", qs, time, hash);
        }catch (Exception e){

        }

        return thqs;
    }

   /* public static String createQueryString(Map<String, String> map){
        String  qs =  "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            qs  = qs + key+"=" +value+"&";
        }
        String aa = qs.substring(0,qs.length()-1);
        return aa;
    }*/
    /**
     * 功能：用一个Map生成一个QueryString，参数的顺序不可预知。
     *
     * @return
     */
    public static String createQueryString(Map<String, String> queryMap) {

        if (queryMap == null) {
            return null;
        }

        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                String key = entry.getKey().trim();
                /*String value = URLEncoder.encode(entry.getValue().trim(),
                        "utf-8");*/
                String value = entry.getValue().trim();
                sb.append(String.format("%s=%s&", key, value));
            }
            return sb.substring(0, sb.length() - 1);
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        Map<String,String>  map = new TreeMap<String, String>();
        map.put("name","我丧服");
        map.put("level","top");
        map.put("salary","1000");
        String str = createHashedQueryString(map,"s521Bt59I02irf0UKQjuwhpyCAyuYJNI");
        System.out.print(str);
       // String str = md5("level=top&name=harry&salary=1000&time=1291879392&salt=aSdF1234");
        //level=top&name=%E6%88%91%E4%B8%A7%E6%9C%8D&salary=1000&time=1291879&hash=72D976B743BE35E93B7132666DD67ABA
        //level=top&name=%E6%88%91%E4%B8%A7%E6%9C%8D&salary=1000&time=1291879&hash=72D976B743BE35E93B7132666DD67ABA
    }

    /**
     * 功能：计算字符串的md5值
     *
     * @param src
     * @return
     */
    public static String md5(String src) {
        return digest(src, "MD5");
    }

    /**
     * 功能：根据指定的散列算法名，得到字符串的散列结果。
     *
     * @param src
     * @param name
     * @return
     */
    private static String digest(String src, String name){
        try {
            MessageDigest alg = MessageDigest.getInstance(name);
            byte[] result = alg.digest(src.getBytes());
            return byte2hex(result);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    /**
     * 功能：将byte数组转换成十六进制可读字符串。
     * @param b 需要转换的byte数组
     * @return 如果输入的数组为null，则返回null；否则返回转换后的字符串。
     */
    public static String byte2hex(byte[] b) {

        if (b == null){
            return null;
        }
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0" + stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }


}
