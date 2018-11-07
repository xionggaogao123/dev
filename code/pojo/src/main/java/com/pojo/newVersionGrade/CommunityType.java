package com.pojo.newVersionGrade;

/**
 * Created by wang_xinxin on 2016/3/21.
 */
public enum CommunityType {
    appComment(1,"作业", "zuoye"),
    appNotice(2,"通知","tongzhi"),
    appOtherNotice(3,"通知另类", "tongzhiOther"),
    system(4,"系统", "system"),
    newSystem(5,"newSystem", "newSystem"),
    piao(6,"投票", "piao"),
    activity(7,"活动", "activity"),
    woShiLaoShi(7,"初一", "junior"),
    allNotice(8,"集合通知", "allNotice"),
    reportCard(9, "成绩单", "reportCard"),
    newVote(10, "新投票", "newVote"),
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
