package com.pojo.pet;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by guojing on 2015/3/26.
 * 宠物信息
 *<pre>
 * {
 *  nm:宠物名称
 *  enm:宠物英文名称
 *  pex:宠物说明
 *  shw:是否展示  0：否，1：是
 *  act:是否是活动宠物 0：否，1：是
 *  pi:宠物标准图片
 *  minpi:宠物小图片
 *  maxpi:宠物大图片
 *  midpi:宠物中等图片
 *
 * }
 * </pre>
 *
 * @author guojing
 */
public class PetTypeEntry extends BaseDBObject {
    private static final long serialVersionUID = 7933557028492747487L;

    public PetTypeEntry(BasicDBObject dbo){setBaseEntry(dbo);}
    /**
     * 构造器
     *
     */
    public PetTypeEntry(String petname, String petEnName,  String petexplain,int show, int activity,
                        String petimage, String minpetimage, String maxpetimage, String middlepetimage)
    {
        super();
        BasicDBObject dbo =new BasicDBObject()
                .append("nm", petname)
                .append("enm", petEnName)
                .append("pex", petexplain)
                .append("shw", show)
                .append("act", activity)
                .append("pi", petimage)
                .append("minpi", minpetimage)
                .append("maxpi", maxpetimage)
                .append("midpi", middlepetimage)
                ;
        setBaseEntry(dbo);
    }

    public String getPetname() {
        return getSimpleStringValue("nm");
    }

    public void setPetname(String petname) {
        setSimpleValue("nm",petname);
    }

    public String getPetEnName() {
        return getSimpleStringValue("enm");
    }

    public void setPetEnName(String petEnName) {
        setSimpleValue("enm",petEnName);
    }

    public String getPetexplain() {
        return getSimpleStringValue("pex");
    }

    public void setPetexplain(String petexplain) {
        setSimpleValue("pex",petexplain);
    }

    public int getShow() {
        return getSimpleIntegerValue("shw");
    }

    public void setShow(int show) {
        setSimpleValue("shw", show);
    }

    public int getActivity() {
        return getSimpleIntegerValue("act");
    }

    public void setActivity(int activity) {
        setSimpleValue("act", activity);
    }

    public String getPetimage() {
        return getSimpleStringValue("pi");
    }

    public void setPetimage(String petimage) {
        setSimpleValue("pi",petimage);
    }

    public String getMinpetimage() {
        return getSimpleStringValue("minpi");
    }

    public void setMinpetimage(String minpetimage) {
        setSimpleValue("minpi",minpetimage);
    }

    public String getMaxpetimage() {
        return getSimpleStringValue("maxpi");
    }

    public void setMaxpetimage(String maxpetimage) {
        setSimpleValue("maxpi",maxpetimage);
    }

    public String getMiddlepetimage() {
        return getSimpleStringValue("midpi");
    }

    public void setMiddlepetimage(String middlepetimage) {
        setSimpleValue("midpi",middlepetimage);
    }

}
