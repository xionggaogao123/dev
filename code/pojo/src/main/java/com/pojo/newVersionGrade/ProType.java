package com.pojo.newVersionGrade;

/**
 * Created by James on 2016/3/21.
 */
public enum ProType {
    bookSearch(1,"一年级", "primary"),
    baiJia(2,"二年级","primary"),
    showEShow(3,"三年级", "primary"),
    redCross(4,"四年级", "primary"),
    challenge(5,"五年级", "primary"),
    myShow(6,"六年级", "primary"),
    woShiLaoShi(7,"初一", "junior"),
    spiritualGurus(8,"初二", "junior"),
    florilegium(9, "初三", "junior"),
    onlineList(10, "高一", "senior"),
    binFen(11, "高二", "senior"),
    gongYi(12, "高三", "senior"),
    gangwei(13,"已毕业", "senior");
    private int type;
    private String des;
    private String eName;



    private ProType(int type, String des, String eName) {
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


    public static ProType getProType(int type)
    {
        for(ProType lt : ProType.values())
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
        for(ProType lt : ProType.values())
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
        for(ProType lt : ProType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getDes();
            }
        }

        return null;
    }
}
