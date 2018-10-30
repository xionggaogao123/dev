package com.pojo.appvote;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-26.
 *      新投票选项类
 *
 *      voteId              投票id                vid
 *      description         投票选项              des
 *      userId              投票选项所属人        uid
 *      type                选项类型              typ       1  普通选项       2 自选选项
 *      select              是否选择的选项         sel
 *      List<VideoEntry> videoList,                        vl//视频
 *      List<AttachmentEntry> attachmentEntries,           ats//文件
 *      List<AttachmentEntry> imageList                    il//图片
 *      List<AttachmentEntry> voiceList                    vt//语音
 *      createTime          创建时间                       ctm
 *      count               投票人数                       cot
 *      userList<Ob >       投票列表                       ult
 *
 */
public class AppVoteOptionEntry extends BaseDBObject {


    public AppVoteOptionEntry(){

    }

    public AppVoteOptionEntry(BasicDBObject dbObject){
        setBaseEntry(dbObject);
    }

    public AppVoteOptionEntry(
            ObjectId voteId,
            String description,
            ObjectId userId,
            int type,
            int select,
            List<AttachmentEntry> imageList,
            List<AttachmentEntry> voiceList,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries,
            int count,
            List<ObjectId> userIdList){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("vid",voteId)
                .append("des",description)
                .append("uid",userId)
                .append("typ",type)
                .append("sel", select)
                .append("ats", MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("vl", MongoUtils.fetchDBObjectList(videoList))
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("cou",count)
                .append("ult",userIdList)
                .append("isr", 0);
        setBaseEntry(basicDBObject);
    }


    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setDescription(String description){
        setSimpleValue("des",description);
    }


    public ObjectId getVoteId(){
        return getSimpleObjecIDValue("vid");
    }

    public void setVoteId(ObjectId voteId){
        setSimpleValue("vid",voteId);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }


    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }


    public int getType(){
        return  getSimpleIntegerValue("typ");
    }
    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getSelect(){
        return getSimpleIntegerValue("sel");
    }

    public void setSelect(int select){
        setSimpleValue("sel",select);
    }
    public int getCount(){
        return getSimpleIntegerValue("cou");
    }

    public void setCount(int count){
        setSimpleValue("cou",count);
    }

    public void setUserIdList(List<ObjectId> userIdList){
        setSimpleValue("ult", MongoUtils.convert(userIdList));
    }

    public List<ObjectId> getUserIdList(){
        ArrayList<ObjectId> userIdList = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("ult");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                userIdList.add((ObjectId)obj);
            }
        }
        return userIdList;
    }


    public void setVoiceList(List<AttachmentEntry> voiceList){
        setSimpleValue("vt",MongoUtils.fetchDBObjectList(voiceList));
    }

    public List<AttachmentEntry> getVoiceList() {
        BasicDBList list = getDbList("vt");
        List<AttachmentEntry> voiceList = new ArrayList<AttachmentEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            voiceList.add(new AttachmentEntry(dbObject));
        }
        return voiceList;
    }
    public void setAttachmentEntries(List<AttachmentEntry> attachmentEntries){
        setSimpleValue("ats",MongoUtils.fetchDBObjectList(attachmentEntries));
    }

    public List<AttachmentEntry> getAttachmentEntries() {
        BasicDBList list = getDbList("ats");
        List<AttachmentEntry> attachmentEntries = new ArrayList<AttachmentEntry>();
        for (Object dbo : list) {
            BasicDBObject dbObject = (BasicDBObject) dbo;
            attachmentEntries.add(new AttachmentEntry(dbObject));
        }
        return attachmentEntries;
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


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}
