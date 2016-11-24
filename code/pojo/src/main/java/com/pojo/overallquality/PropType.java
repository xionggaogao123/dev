package com.pojo.overallquality;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by guojing on 2016/8/26.
 */
public enum PropType {

    YX_CASTLE(8, "ws", "城堡"),
    YX_VILLAGER(5,"hg", "村民"),
    YX_SOLDIERS(4,"td", "士兵");
    private int state;
    private String type;
    private String des;


    private PropType(int state, String type, String des) {
        this.state = state;
        this.type=type;
        this.des = des;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }

    public static PropType getPropType(int state)
    {
        for(PropType sjt:PropType.values())
        {
            if(sjt.getState()==state)
            {
                return sjt;
            }
        }
        return null;
    }

    public static PropType getPropType(String type)
    {
        for(PropType sjt:PropType.values())
        {
            if(sjt.getType().equals(type))
            {
                return sjt;
            }
        }
        return null;
    }

    public static Map<Integer, String> getPropTypeMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (PropType thisEnum : PropType.values()) {
            map.put(thisEnum.getState(), thisEnum.getType());
        }
        return map;
    }
}
