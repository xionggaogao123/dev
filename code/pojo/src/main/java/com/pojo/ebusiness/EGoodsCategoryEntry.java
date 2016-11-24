package com.pojo.ebusiness;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**产品分类
 * Created by fl on 2016/3/3.
 * pid  父类id
 * nm   名称
 * lel  等级
 * st  sort 用于排序
 * img 首页图片
 * mimg 移动端首页图片
 * mcimg 移动端分类图片
 */
public class EGoodsCategoryEntry extends BaseDBObject {

    public EGoodsCategoryEntry(){}

    public EGoodsCategoryEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public EGoodsCategoryEntry(ObjectId parentId, String name, int level, int sort){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("pid", parentId)
                .append("nm", name)
                .append("lvl", level)
                .append("st", sort)
                .append("img","")
                .append("mimg","")
                .append("mcimg","");
        setBaseEntry(baseEntry);
    }
    public EGoodsCategoryEntry(ObjectId parentId, String name, int level, int sort,String image,
                               String mobileImage, String mobileCategoryImg){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("pid", parentId)
                .append("nm", name)
                .append("lvl", level)
                .append("st", sort)
                .append("img",image)
                .append("mimg", mobileImage)
                .append("mcimg",mobileCategoryImg);
        setBaseEntry(baseEntry);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid", parentId);
    }

    public String getName(){
        return getSimpleStringValue("nm");
    }

    public void setName(String name){
        setSimpleValue("nm", name);
    }

    public int getLevel(){
        return getSimpleIntegerValue("lvl");
    }

    public void setLevel(int level){
        setSimpleValue("lvl", level);
    }

    public int getSort(){
        return getSimpleIntegerValue("st");
    }

    public void setSort(int sort){
        setSimpleValue("st", sort);
    }

    public String getImage(){
        if(this.getBaseEntry().containsField("img")){
            return getSimpleStringValue("img");
        }
        return "";
    }

    public void setImage(String image){
        setSimpleValue("img", image);
    }

    public String getMobileImage(){
        if(this.getBaseEntry().containsField("mimg")){
            return getSimpleStringValue("mimg");
        }
        return "";
    }

    public void setMobileImage(String mobileImage){
        setSimpleValue("mimg", mobileImage);
    }

    public String getMobileCategoryImage(){
        if(this.getBaseEntry().containsField("mcimg")){
            return getSimpleStringValue("mcimg");
        }
        return "";
    }

    public void setMobileCategoryImage(String mobileCategoryImg){
        setSimpleValue("mcimg", mobileCategoryImg);
    }
}
