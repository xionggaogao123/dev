package com.fulaan.excellentCourses.api;

import com.fulaan.smalllesson.api.BaseAPI;

/**
 * Created by James on 2018-05-08.
 */
public class CoursesRoomAPI  extends BaseAPI{

    //创建直播间
    public static String createRoom(String sysCode) {
        //long current = System.currentTimeMillis();
        String str = getCCForToken("/room/create"+"?"+sysCode);
        return str;
    }


    //创建直播间
    public static String createNewRoom(String sysCode,String json) {
        //long current = System.currentTimeMillis();
        String str = getZhanCCForToken("/room/create" + "?" + sysCode + "&alarms={json}", json);
        return str;
    }

    //获取回放列表
    public static String getRoomList(String sysCode) {
        //long current = System.currentTimeMillis();
        String str = getCCForToken("/v2/record/info"+"?"+sysCode);
        return str;
    }

    //获取单个回放
    public static String getOneBack(String sysCode) {
        String str = getCCForToken("/v2/record/search"+"?"+sysCode);
        return str;
    }


    //获取用户出入情况  http://api.csslcloud.net/api/statis/useraction
    public static String getUserList(String sysCode) {
        String str = getCCForToken("/statis/useraction"+"?"+sysCode);
        return str;
    }

    //获取某个直播间的在线情况     http://api.csslcloud.net/api/rooms/publishing
    public static  String getRoomStatus(String sysCode){
        String str = getCCForToken("/rooms/publishing"+"?"+sysCode);
        return str;
    }
}
