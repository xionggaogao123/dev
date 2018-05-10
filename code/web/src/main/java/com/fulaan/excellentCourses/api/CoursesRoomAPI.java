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

}
