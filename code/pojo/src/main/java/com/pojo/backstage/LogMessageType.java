package com.pojo.backstage;

/**
 * Created by James on 2016/3/21.
 */
public enum LogMessageType {
    main(1,"首页", "main"),
    table(2,"应用市场","table"),
    fulan(3,"推送系统应用", "fulan"),
    schoolApp(3,"推送校本资源", "schoolApp"),
    black(4,"应用黑名单管理", "black"),
    schoolTime(5,"管控默认上课时间", "schoolTime"),
    systemPhone(6,"常用电话设置", "systemPhone"),
    backTime(7,"默认回调频率", "backTime"),
    teaValidate(8,"老师认证", "teaValidate"),
    content(9,"内容管理","content"),
    userRole(10,"角色管理","userRole"),
    manageUserRole(11,"用户角色管理","manageUserRole"),
    school(12,"学校管理","school"),
    communitTime(13,"学校上课时间","communityTime"),
    courses(14,"直播课堂","courses"),
    yunRole(15,"运营管理员","yunRole"),
    schoolRole(16,"学校管理员","schoolRole"),
    coursesRole(17,"直播课堂管理","coursesRole");

    private int type;
    private String des;
    private String eName;



    private LogMessageType(int type, String des, String eName) {
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


    public static LogMessageType getProType(int type)
    {
        for(LogMessageType lt : LogMessageType.values())
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
        for(LogMessageType lt : LogMessageType.values())
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
        for(LogMessageType lt : LogMessageType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getDes();
            }
        }

        return null;
    }
}
