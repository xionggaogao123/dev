package com.pojo.indexPage;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-09-03.
 * 首页内容记录
 * userId                   用户id              uid
 * subject                  学科名称            sub
 * title                    标题                tit
 * content                  内容                con
 * groupId                  群组id              gid
 * communityId              社群id              cmid
 * contactId                关联来源            cid
 * contactType              关联类型            cty           1 通知    7 投票    8 成绩单
 * watchPermission          观看类型            wat           1 家长    2 学生    3 学生和家长
 * videoList                视屏
 * attachmentEntries        附件
 * voiceList                音频
 * imageList                图片
 * groupName                社群名              gnm
 * userName                 用户名              unm
 * createTime               时间                ctm
 * readList                 阅读人数            rlt
 * allCount                 阅读总数            act
 *
 */
public class IndexContentEntry extends BaseDBObject {
    public IndexContentEntry(){

    }
    public IndexContentEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public IndexContentEntry(
            ObjectId userId,
            String subject,
            String title,
            String content,
            ObjectId groupId,
            int watchPermission,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries,
            List<AttachmentEntry> voiceList,
            String groupName,
            String userName,
            ObjectId subjectId,
            ObjectId communityId,
            List<AttachmentEntry> imageList){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("uid",userId)
                .append("su",subject)
                .append("tl",title)
                .append("ti",System.currentTimeMillis())
                .append("cn",content)
                .append("sd", DateTimeUtils.convert(System.currentTimeMillis(),DateTimeUtils.DATE_YYYY_MM_DD))
                .append("rl", MongoUtils.convert(new ArrayList<ObjectId>()))
                .append("cc", Constant.ZERO)
                .append("gi",groupId)
                .append("gn",groupName)
                .append("un",userName)
                .append("wp",watchPermission)
                .append("cmId",communityId)
                .append("ats",MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vl",MongoUtils.fetchDBObjectList(videoList))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("il",MongoUtils.fetchDBObjectList(imageList))
                .append("sid",subjectId)
                .append("ir",Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public IndexContentEntry(
            ObjectId userId,
            String subject,
            String title,
            String content,
            ObjectId groupId,
            int watchPermission,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries,
            List<AttachmentEntry> voiceList,
            String groupName,
            String userName,
            ObjectId subjectId,
            ObjectId communityId,
            List<AttachmentEntry> imageList,
            List<ObjectId> readList,
            ObjectId contactId,
            int contactType,
            int allCount){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("uid",userId)
                .append("su", subject)
                .append("tl",title)
                .append("ti",System.currentTimeMillis())
                .append("cn",content)
                .append("sd", DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD))
                .append("rl", MongoUtils.convert(new ArrayList<ObjectId>()))
                .append("cc", Constant.ZERO)
                .append("gi",groupId)
                .append("gn",groupName)
                .append("un",userName)
                .append("wp",watchPermission)
                .append("cmId",communityId)
                .append("ats",MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vl",MongoUtils.fetchDBObjectList(videoList))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("il",MongoUtils.fetchDBObjectList(imageList))
                .append("sid",subjectId)
                .append("rlt",readList)
                .append("cid", contactId)
                .append("cty",contactType)
                .append("act",allCount)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public long getSendDay(){
        return getSimpleLongValueDef("sd",0L);
    }

    public void setSendDay(long sendDay){
        setSimpleValue("sd",sendDay);
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

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cmId");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cmId",communityId);
    }


    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }

    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public String getUserName(){
        return getSimpleStringValue("un");
    }

    public void setUserName(String userName){
        setSimpleValue("un",userName);
    }


    public String getGroupName(){
        String str = getSimpleStringValue("gn");
        if(str !=null && str.equals("复兰大学")){
            str = "复兰教育";
        }
        return str;
    }

    public void setGroupName(String groupName){
        setSimpleValue("gn",groupName);
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
        setSimpleValue("rlt",MongoUtils.convert(readList));
    }

    public List<ObjectId> getReaList(){
        List<ObjectId> readList=new ArrayList<ObjectId>();
        if (!getBaseEntry().containsField("rlt")) {
            return readList;
        } else {
            BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("rlt");
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

    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setContactType(int contactType){
        setSimpleValue("cty",contactType);
    }

    public int getContactType(){
        return getSimpleIntegerValue("cty");
    }


    public void setAllCount(int allCount){
        setSimpleValue("act",allCount);
    }

    public int getAllCount(){
        return getSimpleIntegerValue("act");
    }

    public void setRemove(int remove){
        setSimpleValue("ir",remove);
    }

}
