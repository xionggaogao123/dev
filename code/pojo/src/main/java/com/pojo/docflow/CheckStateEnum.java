package com.pojo.docflow;

/**
 * Created by qiangm on 2015/8/19.
 */
public enum CheckStateEnum {
        UNDO("未处理", -1),
        AGREE("同意并转发", 0),
        REJECT("驳回", 1),
        PUBLISH("结束流程并发布", 2),
        ABANDON("废弃", 3),
        TRANSPOND("转寄", 4),
        REVOCATE("撤销",5),
        CREATE("撰写",6),
        MODIFY("修改",7),
        DELETE("删除",8);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private CheckStateEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getCheckState(int index) {
        for (CheckStateEnum c : CheckStateEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return "";
    }

    // get set 方法
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
