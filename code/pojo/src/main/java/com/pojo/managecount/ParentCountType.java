package com.pojo.managecount;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by guojing on 2015/4/8.
 */
public enum ParentCountType {

    MICRO_HOME_POST_NUM(0,"微家园发帖数"),
    //CLASSES_WATCH_NUM(1,"班级课程观看数"),
    //CLOUD_CURRICULUM_VIEW_NUM(2,"云课程观看数"),
    //FRIENDS_NUM(3,"好友数"),
    //JOB_COMPLETION_NUM(4,"孩子作业完成数"),
    //PAPERS_COMPLETION_NUM(5,"孩子试卷完成数"),
    ;

    private int state;
    private String des;


    private ParentCountType(int state, String des) {
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

    public static Map<Integer, String> getParentFunMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (ParentCountType thisEnum : ParentCountType.values()) {
            map.put(thisEnum.getState(), thisEnum.getDes());
        }
        return map;
    }
}
