package com.pojo.integral;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-16.
 */
public enum IntegralType {
    con(-1,"con", 50,50),
    all(0,"all", 50,50),
    operation(1,"operation", 10,20),//作业
    notice(2,"notice",10,20),//通知
    hot(3,"hot",10,20),//火热分享
    find(4,"find",10,20),//发现
    repordcard(5,"repordcard",10,20),//成绩单
    vote(6,"vote",10,20),//投票报名
    book(7,"book",10,20),//参考资料
    lesson(8,"lesson",0,50);//小课堂


    private int type;
    private String des;
    private int eName;
    private int sName;



    private IntegralType(int type, String des, int eName,int sName) {
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

    public int getEname() {
        return eName;
    }

    public void setEname(int eName) {
        this.eName = eName;
    }

    public int getSname() {
        return sName;
    }

    public void setSname(int sName) {
        this.sName = sName;
    }

    public static int getBigType()
    {
        int sort = 0;
        for(IntegralType lt : IntegralType.values())
        {
            if(lt.getType()>sort)
            {
                sort = lt.getType();
            }
        }

        return sort;
    }

    public static List<String> getDesList()
    {
        List<String> str = new ArrayList<String>();
        for(IntegralType lt : IntegralType.values())
        {
            str.add(lt.getDes());
        }
        return str;
    }

    public static int getEnameAllType()
    {
        int sort = 0;
        for(IntegralType lt : IntegralType.values())
        {
                sort = lt.getEname()+sort;
        }
        sort = sort - IntegralType.book.getEname();
        return sort;
    }
    public static int getSnameAllType()
    {
        int sort = 0;
        for(IntegralType lt : IntegralType.values())
        {
            sort = lt.getSname()+sort;
        }
        sort = sort - IntegralType.book.getSname();
        return sort;
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

    public static int getProTypeEname(int type)
    {
        for(IntegralType lt : IntegralType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getEname();
            }
        }

        return 0;
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
