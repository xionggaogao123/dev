package com.pojo.forum;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 版块
 * {
 *     nm : nm 板块名称
 *     itd : introduction 板块简介（内容摘要）
 *     ct : count 每日发帖数
 *     tct : totalCount 总发帖数
 *     lvl : level 等级
 *     st : sort 排序
 *     pid : parentId 父板块
 *     img ：image 板块图片
 *     snm : sectionName 版主
 *     mm : memo 描述
 *
 * }
 */
public class FSectionEntry extends BaseDBObject {

    public FSectionEntry(){
        super();
    }

    public FSectionEntry(String Name,String introduction,String sectionName,String memo){
        this(Name, introduction,0,0,1,1,null,null,sectionName,memo);
    }

    public FSectionEntry(String Name,String introduction,int count,int totalCount,int level,int sort,ObjectId parentid,String image,String sectionName,String memo){

        super();

        BasicDBObject baseEntry =new BasicDBObject()
                .append("nm", Name)
                .append("itd", introduction)
                .append("ct", count)
                .append("tct", totalCount)
                .append("lvl", level)
                .append("st", sort)
                .append("pid", parentid)
                .append("img", image)
                .append("snm", sectionName)
                .append("mm",memo);
        setBaseEntry(baseEntry);
    }

    public FSectionEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public String getName() {
        return getSimpleStringValue("nm");
    }

    public void setName(String name) {
        setSimpleValue("nm", name);
    }

    public String getIntroduction(){
        return getSimpleStringValue("itd");
    }

    public void setIntroduction(String introduction) {
        setSimpleValue("itd", introduction);
    }

    public int getCount(){
        return getSimpleIntegerValue("ct");
    }

    public void setCount(int count) {
        setSimpleValue("ct", count);
    }

    public int getTotalCount(){
        return getSimpleIntegerValue("tct");
    }

    public void setTotalCount(int totalCount) {
        setSimpleValue("tct", totalCount);
    }

    public int getLevel(){
        return getSimpleIntegerValue("lvl");
    }

    public void setLevel(int level){
        setSimpleValue("lvl", level);
    }

    public ObjectId getParentId(){
            return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid", parentId);
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

    public void setImage(String image) {
        setSimpleValue("itd", image);
    }

    public String getSectionName() {
        return getSimpleStringValue("snm");
    }

    public void setSectionName(String sectionName) {
        setSimpleValue("snm", sectionName);
    }

    public String getMemo(){
        return getSimpleStringValue("mm");
    }

    public void setMemo(String memo) {
        setSimpleValue("mm", memo);
    }
}
