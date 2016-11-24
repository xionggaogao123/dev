package com.pojo.activity.enums;

/**
 * Created by Hap on 2014/10/23.
 */
public enum ActVisibility {
  INVITE_FRIEND(0,"邀请的好友可见"),//邀请的好友可见
  FRIEND(1,"好友可见"),//好友可见
  PUBLIC(2,"公开"),  //公开
    ;

    private int state;
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    ActVisibility(int i, String description) {
        this.state=i;
        this.description=description;
    }
}
