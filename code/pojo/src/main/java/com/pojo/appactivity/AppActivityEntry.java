package com.pojo.appactivity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/27.
 */
public class AppActivityEntry extends BaseDBObject{

    public AppActivityEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppActivityEntry(ObjectId subjectId,
                            ObjectId userId,
                            String subjectName,
                            String title,
                            String content,
                            ObjectId groupId,
                            ObjectId communityId,
                            String groupName,
                            List<AttachmentEntry> imageList,
                            List<VideoEntry> videoEntries,
                            int visiblePermission){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("sud",subjectId)
                .append("uid",userId)
                .append("su",subjectName)
                .append("tl",title)
                .append("cn",content)
                .append("gid",groupId)
                .append("gn",groupName)
                .append("cid",communityId)
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("vl", MongoUtils.fetchDBObjectList(videoEntries))
                .append("vp",visiblePermission)
                .append("sti",System.currentTimeMillis())
                .append("ptc",Constant.ZERO)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setSubmitTime(long submitTime){
        setSimpleValue("sti",submitTime);
    }

    public long getSubmitTime(){
        return getSimpleLongValue("sti");
    }

    public void setPartInCount(int partInCount){
        setSimpleValue("ptc",partInCount);
    }

    public int getPartInCount(){
        return getSimpleIntegerValue("ptc");
    }

    public void setVideoEntries(List<VideoEntry> videoEntries){
        setSimpleValue("vl",MongoUtils.fetchDBObjectList(videoEntries));
    }

    public List<VideoEntry> getVideoEntries() {
        BasicDBList list = getDbList("vl");
        List<VideoEntry> videoEntries = new ArrayList<VideoEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            videoEntries.add(new VideoEntry(dbObject));
        }
        return videoEntries;
    }


    public void setGroupName(String groupName){
        setSimpleValue("gn",groupName);
    }

    public String getGroupName(){
        return getSimpleStringValue("gn");
    }

    public void setVisiblePermission(int visiblePermission){
        setSimpleValue("vp",visiblePermission);
    }

    public int getVisiblePermission(){
        return getSimpleIntegerValue("vp");
    }

    public void setImageList(List<AttachmentEntry> imageList){
        setSimpleValue("il",MongoUtils.fetchDBObjectList(imageList));
    }

    public List<AttachmentEntry> getImageList() {
        BasicDBList list = getDbList("il");
        List<AttachmentEntry> imageEntries = new ArrayList<AttachmentEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            imageEntries.add(new AttachmentEntry(dbObject));
        }
        return imageEntries;
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gid",groupId);
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setContent(String content){
        setSimpleValue("cn",content);
    }

    public String getContent(){
        return getSimpleStringValue("cn");
    }

    public void setTitle(String title){
        setSimpleValue("tl",title);
    }

    public String getTitle(){
        return getSimpleStringValue("tl");
    }

    public void setSubjectName(String subjectName){
        setSimpleValue("su",subjectName);
    }

    public String getSubjectName(){
        return getSimpleStringValue("su");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sud",subjectId);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sud");
    }

}
