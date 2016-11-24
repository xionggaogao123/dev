package com.pojo.activity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 15-2-26.
 *  *an   活动名称
    *oid  组织者id
    *cd   创建时间
    *asd  活动开始时间
    *aed  活动结束时间
    *lc   活动地址
    *ci   活动封面
    *as   活动状态
    *av   活动可见性
    *mc   可参加成员数量
    *ds   活动描述
    *diss 讨论(内嵌)
    *dis  讨论数量
    *ic   图片数量
    *att  出席活动人员id(list)
    *rid  地区id
    *attc 出席活动人数
    *sid  学校id
 */
public class ActivityEntry extends BaseDBObject {

    /**

    * */
    private static final long serialVersionUID = -4610886224141288549L;
    
    public ActivityEntry(){
        BasicDBObject base = new BasicDBObject().append("an", "").append("oid", null).append("cd", null).
                append("asd", null).append("aed", null).append("lc", "").append("ci", "").
                append("as", null).append("av", null).append("mc", null).append("ds", "").append("diss",new BasicDBList()).
                append("dis", 0).append("ic", 0).append("att",null).append("rid",null).append("attc",0).
                append("sid", null);
        setBaseEntry(base);
    }
    

    public ActivityEntry(BasicDBObject basicDBObject){
        super(basicDBObject);
    }
    
    
    
    public ActivityEntry(String actName, ObjectId organizerId, String organizerName, int organizerRole, String organizerImageUrl
            , long createDate, long actStartDate, long actEndDate, String location, String coverImage,
                         int actStatus, int actVisibility, int memberCount, String description, int discuss, int image,String regionId,
                         int attendCount,ObjectId schoolId,List<ObjectId> userIds){
        BasicDBObject base = new BasicDBObject().append("an", actName).append("oid", organizerId).append("cd", createDate).
                append("asd", actStartDate).append("aed", actEndDate).append("lc", location).append("ci", coverImage).
                append("as", actStatus).append("av", actVisibility).append("mc", memberCount).append("ds", description).append("diss",new BasicDBList()).
                append("dis", discuss).append("ic", image).append("att",userIds).append("rid",regionId).append("attc",attendCount).
                append("sid", schoolId);
        setBaseEntry(base);
    }

    
    
    
    public  String getActName(){
        return getSimpleStringValue("an");
    }
    public ObjectId getOrganizerId(){
        return getSimpleObjecIDValue("oid");
    }
    public long getCreateDate() {
        return getSimpleLongValue("cd");
    }
    public long getActStartDate(){
        return  getSimpleLongValue("asd");
    }
    public long getActEndDate(){
        return  getSimpleLongValue("aed");
    }
    public String getLocation(){
        return getSimpleStringValue("lc");
    }
    public String getCoverImage(){
        return getSimpleStringValue("ci");
    }
    public int getActStatus(){
        return getSimpleIntegerValue("as");
    }
    public int getActVisibility(){
        return getSimpleIntegerValue("av");
    }
    public int getMemberCount(){
        return getSimpleIntegerValue("mc");
    }
    public String getDescription(){
        return getSimpleStringValue("ds");
    }
    public int getDiscussCount(){
        return getSimpleIntegerValue("dis");
    }
    public int  getImageCount(){
        return getSimpleIntegerValue("ic");
    }
    public ObjectId getRegionId(){
        return getSimpleObjecIDValue("rid");
    }
    public int getAttendCount(){
        return getSimpleIntegerValue("attc");
    }
    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }
    public List<ObjectId> getAttendIds(){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("att");
        if(basicDBList!=null){
            for(Object object:basicDBList){
                objectIdList.add((ObjectId) object);
            }
        }
        return objectIdList;
    }
    public List<ActivityDiscuss> getActDiscusses(){
        List<ActivityDiscuss> activityDiscussList =new ArrayList<ActivityDiscuss>();
        BasicDBList basicDBList= (BasicDBList) getSimpleObjectValue("diss");
        if(basicDBList!=null){
            for(Object object:basicDBList){
                activityDiscussList.add(new ActivityDiscuss((BasicDBObject)object));
            }
        }
        return activityDiscussList;
    }
    public void setActDiscusses(List<ActivityDiscuss> activityDiscussEntries){
        BasicDBList disses=null;
        if(activityDiscussEntries!=null){
           disses=new BasicDBList();
            for(ActivityDiscuss activityDiscuss:activityDiscussEntries){
                disses.add(activityDiscuss.getBaseEntry());
            }
        }
        setSimpleValue("diss",disses);
    }
    

    //set
    public void  setActName(String actName){
        setSimpleValue("an",actName);
    }
    public void setOrganizerId(ObjectId organizerId){
        setSimpleValue("oid",organizerId);
    }
    public void setCreateDate(long createDate){
        setSimpleValue("cd",createDate);
    }
    public void setActStartDate(long actStartDate){
        setSimpleValue("asd",actStartDate);
    }
    public void setActEndDate(long actEndDate){
        setSimpleValue("aed",actEndDate);
    }
    public void setLocation(String location){
        setSimpleValue("lc",location);
    }
    public void setCoverImage(String coverImage){
        setSimpleValue("ci",coverImage);
    }
    public void setActStatus(int actStatus){
        setSimpleValue("as",actStatus);
    }
    public void setActVisibility(int actVisibility){
        setSimpleValue("av",actVisibility);
    }
    public void setMemberCount(int memberCount){
        setSimpleValue("mc",memberCount);
    }
    public void setDescription(String description){
        setSimpleValue("ds",description);
    }
    public void setDiscussCount(int discussCount){
        setSimpleValue("dis",discussCount);
    }
    public void setImageCount(int imageCount){
        setSimpleValue("ic",imageCount);
    }
    public void setAttendIds(List<ObjectId> objectIds){
        setSimpleValue("att",objectIds);
    }
    public void setRegionId(ObjectId objectId){
        setSimpleValue("rid",objectId);
    }
    public void setAttendCount(int count){
        setSimpleValue("attc",count);
    }
    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }


}
