package com.pojo.business;

/**
 * Created by James on 2018-05-15.
 */
public enum RoleType {

    updateCommunityName(1,"修改群名称", "updateCommunityName"),
    commentAndZan(2,"刷赞和评论","commentAndZan"),
    communityTalk(3,"简易社群会话","communityTalk"),
    noFriend(4,"自动不成为好友","noFriend"),
    sendRole(5,"发送运营文章","sendRole");



    private int type;
    private String des;
    private String eName;



    private RoleType(int type, String des, String eName) {
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


    public static RoleType getProType(int type)
    {
        for(RoleType lt : RoleType.values())
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
        for(RoleType lt : RoleType.values())
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
        for(RoleType lt : RoleType.values())
        {
            if(lt.getType()==type)
            {
                return lt.getDes();
            }
        }

        return null;
    }

    public static String getD(String eName)
    {
        for(RoleType lt : RoleType.values())
        {
            if(lt.getEname().equals(eName))
            {
                return lt.getDes();
            }
        }

        return null;
    }
}
