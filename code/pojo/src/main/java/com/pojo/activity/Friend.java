package com.pojo.activity;

public class Friend {
    private String id;

    private String userid1;

    private String userid2;

    public Friend(){}
    public Friend(String i, String i1) {
        this.userid1=i;
        this.userid2=i1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid1() {
        return userid1;
    }

    public void setUserid1(String userid1) {
        this.userid1 = userid1;
    }

    public String getUserid2() {
        return userid2;
    }

    public void setUserid2(String userid2) {
        this.userid2 = userid2;
    }
}