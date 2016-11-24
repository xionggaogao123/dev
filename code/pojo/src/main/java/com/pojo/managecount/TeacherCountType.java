package com.pojo.managecount;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by guojing on 2015/4/8.
 */
public enum TeacherCountType {

    MICRO_CAMPUS_POST_NUM(0,"微校园发帖数"),
    //CLASS_UPLOAD_NUM(1,"班级课程上传数"),
    HOMEWORK_UPLOAD_NUM(2,"作业上传数"),
    PAPERS_UPLOAD_NUM(3,"试卷上传数"),
    PREPARATION_UPLOAD_NUM(4,"备课上传数"),
    NOTICE_NUM(5,"通知发布数"),
    ;

    private int state;
    private String des;


    private TeacherCountType(int state, String des) {
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

    public static Map<Integer, String> getTeachFunMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (TeacherCountType thisEnum : TeacherCountType.values()) {
            map.put(thisEnum.getState(), thisEnum.getDes());
        }
        return map;
    }
}
