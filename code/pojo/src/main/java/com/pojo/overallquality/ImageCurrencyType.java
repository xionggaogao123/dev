package com.pojo.overallquality;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by guojing on 2016/8/24.
 */
public enum ImageCurrencyType {
    YX_NLXX(1,"努力学习"),
    YX_RQCY(2,"热情参与"),
    YX_GWZZ(3,"岗位职责"),
    YX_ZJML(4,"遵纪明理"),
    YX_YGKA(5,"阳光可爱");

    private int state;
    private String des;


    private ImageCurrencyType(int state, String des) {
        this.state = state;
        this.des = des;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }

    public static ImageCurrencyType getImageCurrencyType(int state)
    {
        for(ImageCurrencyType sjt:ImageCurrencyType.values())
        {
            if(sjt.getState()==state)
            {
                return sjt;
            }
        }
        return null;
    }

    public static Map<Integer, String> getImageCurrencyTypeMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (ImageCurrencyType thisEnum : ImageCurrencyType.values()) {
            map.put(thisEnum.getState(), thisEnum.getDes());
        }
        return map;
    }
}
