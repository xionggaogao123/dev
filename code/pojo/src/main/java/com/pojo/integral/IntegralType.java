package com.pojo.integral;

/**
 * Created by James on 2018-04-16.
 */
public enum IntegralType {

    operation(1,"operation", "10","20"),//作业
    notice(2,"notice","10","20"),//通知
    hot(3,"hot","10","20"),//火热分享
    find(4,"find","10","20"),//发现
    repordcard(5,"repordcard","10","20"),//成绩单
    vote(6,"vote","10","20"),//投票报名
    book(7,"book","10","20");//参考资料


    private int type;
    private String des;
    private String eName;
    private String sName;



    private IntegralType(int type, String des, String eName,String sName) {
        this.type = type;
        this.des = des;
        this.eName = eName;
        this.sName = sName;
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

    public String getSname() {
        return sName;
    }

    public void setSname(String sName) {
        this.sName = sName;
    }


    public static IntegralType getProType(int type)
    {
        for(IntegralType lt : IntegralType.values())
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
        for(IntegralType lt : IntegralType.values())
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
        for(IntegralType lt : IntegralType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getDes();
            }
        }

        return null;
    }
}
