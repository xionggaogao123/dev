package com.pojo.instantmessage;

/**
 * Created by James on 2016/3/21.
 */
public enum ApplyTypeEn {
    operation(1,"作业", "other"),  //除了自己的老师
    notice(2,"通知","same"),       // 除了自己
    hot(3,"火热分享","same"),
    text(4,"参考资料","same"),
    repordcard(5,"成绩单","third"),//已绑定用户
    study(6,"学习用品","same"),
    piao(7,"投票","same"),
    happy(8,"兴趣小组","same"),
    active(9,"活动报名","same"),
    daynotice(10,"每日通知","same"),
    home(11,"家管控","same"),
    school(12,"校管控","same");
//成绩单 火热分享 活动报名 参考资料 学习用品 兴趣小组
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
