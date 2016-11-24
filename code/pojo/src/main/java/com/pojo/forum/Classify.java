package com.pojo.forum;

/**
 * Created by jerry on 2016/9/5.
 * 板块主题
 * 全部，官方公告，精彩活动，闲聊灌水，原创文章，其他
 */
public enum Classify {

    All("全部",0),
    OFFICIAL("官方公告",1),
    ACTIVITY("精彩活动",2),
    HELP("问题求助",3),
    ADVICE("功能建议",4),
    CHAT("闲聊灌水",5),
    ORIGINAL("原创文章",6),
    OTHER("其他",7);


    // 成员变量
    private String name;
    private int index;

    // 构造方法
    Classify(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
