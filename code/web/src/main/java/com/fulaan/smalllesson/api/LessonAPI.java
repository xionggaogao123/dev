package com.fulaan.smalllesson.api;

import java.util.List;

/**
 * Created by James on 2017/12/29.
 */
public class LessonAPI extends BaseAPI {
    public static void addCustomVote(List<String> codes) {
        for(String str : codes){
            String resoureUrl = "/small_class/class_over/"+str;
            getLessonForObject(resoureUrl);
        }
    }
}
