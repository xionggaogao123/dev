package com.pojo.leave;

/**
 * Created by qiangm on 2016/3/1.
 */
public enum ReplyEnum {
    UNDO("未处理",0),
    AGREE("同意",1),
    REJECT("驳回",2);
    private String name;
    private int index;

    // 构造方法
    private ReplyEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getCheckState(int index) {
        for (ReplyEnum c : ReplyEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return "";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
