package com.pojo.newVersionGrade;

/**
 * Created by wang_xinxin on 2016/3/21.
 */
public enum CommunityType {
    appComment(1,"作业", "zuoye"),
    appNotice(2,"通知","tongzhi"),
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



    private CommunityType(int type, String des, String eName) {
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


    public static CommunityType getProType(int type)
    {
        for(CommunityType lt : CommunityType.values())
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
        for(CommunityType lt : CommunityType.values())
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
        for(CommunityType lt : CommunityType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getDes();
            }
        }

        return null;
    }
}