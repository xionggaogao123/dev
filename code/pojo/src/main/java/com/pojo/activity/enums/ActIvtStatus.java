package com.pojo.activity.enums;

/**
 * Created by Hao on 2014/10/23.
 */
public enum ActIvtStatus {
    INVITE(0,"邀请"),
    HESITATE(1,"犹豫"), //犹豫
    ACCEPT(2,"接受"),   //接受
    REJECT(3,"拒绝"),   //拒绝
    ATTENDED(4,"已经参加"),//已经参加
    NOINVITE(5,"已经发出");//已经发出邀请

    private int state;
    private String description;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    ActIvtStatus(int i, String description) {
        this.state=i;
        this.description=description;
    }
}
