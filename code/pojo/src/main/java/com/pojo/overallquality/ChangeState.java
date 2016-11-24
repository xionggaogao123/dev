package com.pojo.overallquality;

/**
 * Created by guojing on 2016/8/25.
 */
public enum ChangeState {
    AUDIT(0,"审核中"),
    PASS(1,"通过"),
    REFUSE(2,"拒绝"),
    ;
    private int state;
    private String des;

    private ChangeState(int state, String des) {
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

    public static ChangeState getChangeState(int state)
    {
        for(ChangeState sjt:ChangeState.values())
        {
            if(sjt.getState()==state)
            {
                return sjt;
            }
        }
        return null;
    }
}
