package com.pojo.managecount;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by guojing on 2015/4/8.
 */
public enum StudentCountType {

    MICRO_HOME_POST_NUM(0,"微家园发帖数"),
    CLASSES_WATCH_NUM(1,"班级课程观看数"),
    JOB_COMPLETION_NUM(2,"作业完成数"),
    PAPERS_COMPLETION_NUM(3,"试卷完成数"),
    //QUESTION_NUM(4,"题库完成数"),
    //FRIENDS_NUM(5,"好友数"),
    CLOUD_CURRICULUM_VIEW_NUM(6,"云课程观看数"),
    ;

    private int state;
    private String des;


    private StudentCountType(int state, String des) {
        this.state = state;
        this.des = des;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }

    public static Map<Integer, String> getStudFunMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (StudentCountType thisEnum : StudentCountType.values()) {
            map.put(thisEnum.getState(), thisEnum.getDes());
        }
        return map;
    }
}
