package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * Created by admin on 2016/7/11.
 *
 * 论坛等级
 * {
 *     le:level 等级名称
 *     sl：startLevel 起始经验值
 *     el: endLevel 结束经验值
 *     ss: stars 星星数
 * }
 */
public class FLevelEntry extends BaseDBObject {

    public FLevelEntry(){}

    public FLevelEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public  String getLevel(){
        return getSimpleStringValue("le");
    }

    public void setLevel(String level){
        setSimpleValue("le",level);
    }

    public Long getStartLevel(){
        return getSimpleLongValue("sl");
    }

    public void setStartLevel(Long startLevel){
        setSimpleValue("sl",startLevel);
    }

    public Long getEndLevel(){
        return getSimpleLongValue("el");
    }

    public void setEndLevel(Long startLevel){
        setSimpleValue("el",startLevel);
    }

    public Long getStars(){
        return getSimpleLongValue("ss");
    }

    public void setStars(Long stars){
        setSimpleValue("ss",stars);
    }

}
