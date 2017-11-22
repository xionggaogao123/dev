package com.pojo.backstage;

/**
 * Created by James on 2016/3/21.
 */
public enum PictureType {
    userUrl(1,"用户头像", "avt"),
    communityLogo(2,"社区logo","cmlg"),
    noticeImage(3,"通知图片", "url"),
    operationImage(4,"作业图片", "url2"),
    activeImage(5,"活动报名图片", "url3"),
    studyImage(6,"学习用品图片", "url4"),
    happyImage(7,"兴趣小组图片", "url5"),
    commentImage(8,"评论图片", "url6"),
    wrongImage(9,"错题本图片","url7"),
    answerImage(10,"解析图片","url8");



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
