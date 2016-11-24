package com.pojo.groups;

/**
 * 群状态
 * Created by wang_xinxin on 2015/3/30.
 */
public enum GroupState {
    NORMAL(0,"正常"),
    DELETED(1,"已经删除"),
    ;
    private int state;
    private String des;


    private GroupState(int state, String des) {
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
}
