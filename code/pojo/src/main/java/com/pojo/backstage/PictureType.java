package com.pojo.backstage;

/**
 * Created by James on 2016/3/21.
 */
public enum PictureType {
    phone(100,"家长设置电话推送", "100"),
    app(200,"推送app消息","200"),
    mi(300,"防沉迷设置推送", "300");



    private int type;
    private String des;
    private String eName;



    private PictureType(int type, String des, String eName) {
        this.type = type;
        this.des = des;
        this.eName = eName;
    }

    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }


    public String getDes() {
        return des;
    }


    public void setDes(String des) {
        this.des = des;
    }

    public String getEname() {
        return eName;
    }

    public void setEname(String eName) {
        this.eName = eName;
    }


    public static PictureType getProType(int type)
    {
        for(PictureType lt : PictureType.values())
        {
            if(lt.getType()==type)
            {
                return lt;
            }
        }

        return null;
    }

    public static String getProTypeEname(int type)
    {
        for(PictureType lt : PictureType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getEname();
            }
        }

        return null;
    }
    public static String getDes(int type)
    {
        for(PictureType lt : PictureType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getDes();
            }
        }

        return null;
    }
}
