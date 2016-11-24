package com.pojo.ebusiness;

/**
 * Created by wangkaidong on 2016/4/21.
 */

public enum EBookType {

    HUIBEN(1,"绘本"),
    WENXUE(2,"文学"),
    KEPU(3,"科普百科"),
    ;


    private int type;
    private String name;

    private EBookType(int type, String name) {
        this.name = name;
        this.type = type;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }


    public static EBookType getEBookType(int type)
    {
        for(EBookType orderState:EBookType.values())
        {
            if(orderState.getType()==type)
            {
                return orderState;
            }
        }

        return null;
    }
}