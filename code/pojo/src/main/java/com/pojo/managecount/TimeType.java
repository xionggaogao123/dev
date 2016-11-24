package com.pojo.managecount;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by guojing on 2015/4/8.
 */
public enum TimeType {

    ALL(0,"全部"),
    CURR_SCHOOL_YEAR(1,"本学年"),
    CURR_SCHOOL_TERM(2,"本学期"),
    CURR_MONTH(3,"本月"),
    CURR_WEEK(4,"本周"),
    CURR_DAY(5,"今天"),
    USER_DEFINED(6,"自定义"),
    ;

    private int state;
    private String des;


    private TimeType(int state, String des) {
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

    public static Map<Integer, String> getTimeTypeMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (TimeType thisEnum : TimeType.values()) {
            map.put(thisEnum.getState(), thisEnum.getDes());
        }
        return map;
    }
}
