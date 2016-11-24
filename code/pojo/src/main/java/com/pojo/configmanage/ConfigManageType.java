package com.pojo.configmanage;

/**
 * 打分事项
 * Created by Tony on 2014/9/24.
 */

public enum ConfigManageType {

    CEILINGOFDAILYEXP(1, "日经验值上限");

    private final int code;
    private final String desc;


    /**
     * @param code 类型
     * @param desc  描述
     */
    ConfigManageType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
