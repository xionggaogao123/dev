package com.pojo.zouban;

/**
 * Created by wangkaidong on 2016/6/20.
 *
 * 走班类型枚举
 */
public enum ZoubanType {
    ZOUBAN(1, "走班课"),
    FEIZOUBAN(2, "非走班课"),
    PE(4, "体育课"),
    OTHER(5, "其他"),
    INTEREST(6, "兴趣班（选修课）"),
    GROUPZOUBAN(7, "分组走班"),
    ODDEVEN(8, "单双周课")
    ;


    private int type;
    private String description;

    private ZoubanType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
