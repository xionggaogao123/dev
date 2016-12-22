package com.pojo.parentChild;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/12/22.
 * //数据爬取
 * {
 *     sti:startTime 时间
 *     rid:regionId 城市
 *     ci:city城市名称
 *     eps:expense 费用
 *     acn:activityName 活动名称
 *     acm:activityImage 活动图片
 *     ace:activityDescription 活动简介
 *     aci:activityDescImage 活动简介图片
 *     act:activityTime 活动简介时间
 *     aco:activityContent 活动简介内容
 *     ac:activityUrl 活动链接
 *
 * }
 */
public class ParentChildActivityEntry extends BaseDBObject{

    public ParentChildActivityEntry(){

    }
    public ParentChildActivityEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ParentChildActivityEntry(String startTime, ObjectId regionId, String city, int expense,
                                    String activityName, String activityImage, String activityDescription,
                                    String activityDescImage, String activityTime, String activityContent,
                                    String activityUrl){
        BasicDBObject dbo=new BasicDBObject("sti",startTime)
                .append("rid",regionId)
                .append("ci",city)
                .append("eps",expense)
                .append("acn",activityName)
                .append("acm",activityImage)
                .append("ace",activityDescription)
                .append("aci",activityDescImage)
                .append("act",activityTime)
                .append("aco",activityContent)
                .append("ac",activityUrl);
        setBaseEntry(dbo);

    }

    public String getStartTime(){
        return getSimpleStringValue("sti");
    }

    public void setStartTime(String startTime){
        setSimpleValue("sti",startTime);
    }

    public ObjectId getRegionId(){
        return getSimpleObjecIDValue("rid");
    }

    public void setRegionId(ObjectId regionId){
        setSimpleValue("rid",regionId);
    }

    public String getCity(){
        return getSimpleStringValue("ci");
    }

    public void setCity(String city){
        setSimpleValue("ci",city);
    }

    public int getExpense(){
        return getSimpleIntegerValue("eps");
    }

    public void setExpense(int expense){
        setSimpleValue("eps",expense);
    }

    public String getActivityName(){
        return getSimpleStringValue("acn");
    }

    public void setActivityName(String activityName){
        setSimpleValue("acn",activityName);
    }

    public String getActivityImage(){
        return getSimpleStringValue("acm");
    }

    public void setActivityImage(String activityImage){
        setSimpleValue("acm",activityImage);
    }

    public String getActivityDescription(){
        return getSimpleStringValue("ace");
    }

    public void setActivityDescription(String activityDescription){
        setSimpleValue("ace",activityDescription);
    }

    public String getActivityDescImage(){
        return getSimpleStringValue("aci");
    }

    public void setActivityDescImage(String activityDescImage){
        setSimpleValue("aci",activityDescImage);
    }

    public String getActivityTime(){
        return getSimpleStringValue("act");
    }

    public void setActivityTime(String activityTime){
        setSimpleValue("act",activityTime);
    }

    public String getActivityContent(){
        return getSimpleStringValue("aco");
    }

    public void setActivityContent(String activityContent){
        setSimpleValue("aco",activityContent);
    }

    public String getActivityUrl(){
        return getSimpleStringValue("ac");
    }

    public void setActivityUrl(String activityUrl){
        setSimpleValue("ac",activityUrl);
    }

}
