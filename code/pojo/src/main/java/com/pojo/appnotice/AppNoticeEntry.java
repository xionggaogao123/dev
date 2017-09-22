package com.pojo.appnotice;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.ValidateInfoEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/22.
 * {
 *
 * }
 */
public class AppNoticeEntry extends BaseDBObject{

    public AppNoticeEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public AppNoticeEntry(
            ObjectId userId,
            String subject,
            String title,
            String content,
            ObjectId groupId,
            int watchPermission,
            List<VideoEntry> videoList,
            List<AttachmentEntry> imageList){
       BasicDBObject basicDBObject=new BasicDBObject()
               .append("uid",userId)
               .append("su",subject)
               .append("tl",title)
               .append("ti",System.currentTimeMillis())
               .append("cn",content)
               .append("rl", MongoUtils.convert(new ArrayList<ObjectId>()))
               .append("cc", Constant.ZERO)
               .append("gi",groupId)
               .append("wp",watchPermission)
               .append("vl",MongoUtils.fetchDBObjectList(videoList))
               .append("il",MongoUtils.fetchDBObjectList(imageList))
               .append("ir",Constant.ZERO);
        setBaseEntry(basicDBObject);
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

    public void setVideoList(List<VideoEntry> videoList){
        setSimpleValue("vl",MongoUtils.fetchDBObjectList(videoList));
    }

    public List<VideoEntry> getVideoList() {
        BasicDBList list = getDbList("vl");
        List<VideoEntry> videoEntries = new ArrayList<VideoEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            videoEntries.add(new VideoEntry(dbObject));
        }
        return videoEntries;
    }

    public void setWatchPermission(int watchPermission){
        setSimpleValue("wp",watchPermission);
    }

    public int getWatchPermission(){
        return getSimpleIntegerValue("wp");
    }

    public ObjectId getGroupId(){
        return getSimpleObjecIDValue("gi");
    }

    public void setGroupId(ObjectId groupId){
        setSimpleValue("gi",groupId);
    }

    public void setCommentCount(int commentCount){
        setSimpleValue("cc",commentCount);
    }

    public int getCommentCount(){
        return getSimpleIntegerValue("cc");
    }

    public void setReadList(List<ObjectId> readList){
        setSimpleValue("rl",MongoUtils.convert(readList));
    }

    public List<ObjectId> getReaList(){
        List<ObjectId> readList=new ArrayList<ObjectId>();
        if (!getBaseEntry().containsField("rl")) {
            return readList;
        } else {
            BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("rl");
            if(null!=basicDBList&&!basicDBList.isEmpty()){
                for(Object o:basicDBList){
                    readList.add((ObjectId)o);
                }
            }
        }
        return readList;
    }

    public void setContent(String content){
        setSimpleValue("cn",content);
    }

    public String getContent(){
        return getSimpleStringValue("cn");
    }

    public void setSubmitTime(long submitTime){
        setSimpleValue("ti",submitTime);
    }

    public long getSubmitTime(){
        return getSimpleLongValue("ti");
    }

    public void setTitle(String title){
        setSimpleValue("tl",title);
    }


    public String getTitle(){
        return getSimpleStringValue("tl");
    }


    public void setSubject(String subject){
        setSimpleValue("su",subject);
    }

    public String getSubject(){
        return getSimpleStringValue("su");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
}
