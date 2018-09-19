package com.fulaan.excellentCourses.api;

import com.fulaan.smalllesson.api.BaseAPI;

/**
 * Created by James on 2018-09-19.
 */
public class CoursesGanKaoAPI extends BaseAPI {
    //创建直播间
    public static String sendMessage(String sysCode) {
        //long current = System.currentTimeMillis();
        String str = getGanKaoForToken("/payNotify_fulan" + "?" + sysCode);
        return str;
    }
}
