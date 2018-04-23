package com.fulaan.controlservice.api;

import com.fulaan.controlservice.util.EncryptionUtils;
import com.fulaan.smalllesson.api.BaseAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 2018-04-08.
 */
public class ControlTokenAPI extends BaseAPI {

    private static final String APP_ID = "M6o3WqWcbza3wlB2wYA2rGmVAtVGMYYb";

    private static final String APP_KEY = "AFVYcApb9qh1qqHXZaI61aZjr03QH88w";

    private static final int SYS_CODE = 0;

    //获得accessToken
    public static String getAccessToken(String sysCode) {
        long current = System.currentTimeMillis();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("appId",APP_ID);
        map.put("timeStamp",current);
        map.put("keyInfo",getTokenGenerator(APP_ID,APP_KEY,current));
        map.put("sysCode",sysCode);
        String str = postForToken("/apigateway/getAccessToken", map);
        return str;
    }


    //获得usessionId
    public static String getUsessionId(String accessToken) {
        String str = getForToken("/userSession/sessionInterchange?accessToken="+accessToken);
        return str;
    }

    //获得应用信息列表
    public static String getAppList(String accessToken,String usessionId) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("pageNo",1);
        map.put("pageSize",20);
        //map.put("usessionId",usessionId);
       /// map.put("eduType","");
        String str = postForToken("/appInfo/getAppList?accessToken=" + accessToken, map);
        return str;
    }


    //获得ticket
    public static String getTicket(String accessToken,String usessionId) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("usessionId",usessionId);
        String str = postForToken("/userSession/createTicket?accessToken="+accessToken, map);
        return str;
    }

    //验证ticket
    public static String validaTicket(String accessToken,String  ticket) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("ticket",ticket);
        String str = postForToken("/userSession/validaTicket?accessToken="+accessToken, map);
        return str;
    }

    //获得用户信息
    public static String getUserInfo(String accessToken,String usessionId) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("usessionId",usessionId);
        String str = postForToken("/userSession/getLoginUserInfo?accessToken="+accessToken, map);
        return str;
    }

    public static String getTokenGenerator(String appid,String appkey,long ts){
        String paramValues = appid + appkey + ts;
        byte[] hmacSHA = EncryptionUtils.getHmacSHA1(paramValues, appkey);
        String digest = EncryptionUtils.bytesToHexString(hmacSHA);
        digest = digest.toUpperCase();
        System.out.println("digest:" + digest);
        return digest;
    }

    public static void main(String[] args){
        String str = getTokenGenerator("M6o3WqWcbza3wlB2wYA2rGmVAtVGMYYb","AFVYcApb9qh1qqHXZaI61aZjr03QH88w",System.currentTimeMillis());
    }


}
