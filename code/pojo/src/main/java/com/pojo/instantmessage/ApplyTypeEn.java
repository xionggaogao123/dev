package com.pojo.instantmessage;

/**
 * Created by James on 2016/3/21.
 */
public enum ApplyTypeEn {
    operation(1,"作业", "other"),
    notice(2,"通知","same");

    private int type;
    private String des;
    private String eName;



    private ApplyTypeEn(int type, String des, String eName) {
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


    public static ApplyTypeEn getProType(int type)
    {
        for(ApplyTypeEn lt : ApplyTypeEn.values())
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
        for(ApplyTypeEn lt : ApplyTypeEn.values())
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
        for(ApplyTypeEn lt : ApplyTypeEn.values())
        {
            if(lt.getType()==type)
            {
                return lt.getDes();
            }
        }

        return null;
    }
}
