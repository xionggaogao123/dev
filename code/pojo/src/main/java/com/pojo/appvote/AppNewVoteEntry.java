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
 * Created by James on 2018-10-25.
 *       新投票（模仿k6Kt的投票选举2.0）
 *   title                    标题                       tit
 *   content                  内容                       con
 *   schoolId                 学校id                     sid
 *   subjectId                学科id                     bid
 *   createTime               创建时间                   ctm
 *   userId                   创建者                     uid
 *   role                     创建者身份                 rol
 *   userCount                投票人数                   uco
 *   isRemove                 是否删除                   isr
 *   List<VideoEntry> videoList,                        vl//视频
 *   List<AttachmentEntry> attachmentEntries,           ats//文件
 *   List<AttachmentEntry> imageList                    il//图片
 *   List<AttachmentEntry> voiceList                    vt//语音
 *
 *   type                     类型                      typ             1   投票       2    报名(不指定选项投票)
 *   applyTypeList            申请对象                  aty             l   学生       2    家长      3 老师      （针对报名）
 *   applyCount               申请数量                  aco            （针对报名）
 *   applyStartTime           申请开始时间              ast            （针对报名）
 *   applyEndTime             申请结束时间              aet            （针对报名）
 *
 *   communityList<ObjectId>  投票社群集合              clt
 *   voteTypeList             投票对象                  vtl
 *   voteCount                投票数量                  vct
 *   sign                     是否记名                  sig              0   不记名         1 记名
 *   open                     是否公开                  ope              0   不公开         1 公开
 *   voteStartTime            投票开始时间              vst
 *   voteEndTime              投票结束时间              vet
 *
 *
 */
public class AppNewVoteEntry extends BaseDBObject {
    public AppNewVoteEntry(){

    }


    public AppNewVoteEntry(BasicDBObject dbObject){
       setBaseEntry(dbObject);
    }

    public AppNewVoteEntry(
            String title,
            String content,
            ObjectId schoolId,
            ObjectId subjectId,
            ObjectId userId,
            int role,
            int userCount,
            List<AttachmentEntry> imageList,
            List<AttachmentEntry> voiceList,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries,
            int type,
            List<Integer>  applyTypeList,
            int applyCount,
            long applyStartTime,
            long applyEndTime,
            List<ObjectId> communityList,
            List<Integer> voteTypeList,
            int voteCount,
            int sign,
            int open,
            long voteStartTime,
            long voteEndTime
    ){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("tit", title)
                .append("con", content)
                .append("sid",schoolId)
                .append("bid", subjectId)
                .append("ctm", System.currentTimeMillis())
                .append("uid", userId)
                .append("rol", role)
                .append("uco",userCount)
                .append("ats", MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("vl", MongoUtils.fetchDBObjectList(videoList))
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("typ", type)
                .append("aty",applyTypeList)
                .append("aco",applyCount)
                .append("ast", applyStartTime)
                .append("aet",applyEndTime)
                .append("clt", communityList)
                .append("vtl",voteTypeList)
                .append("vct",voteCount)
                .append("sig",sign)
                .append("ope", open)
                .append("vst", voteStartTime)
                .append("vet", voteEndTime)
                .append("isr", 0);
        setBaseEntry(basicDBObject);

    }
    public String getTitle(){
        return getSimpleStringValue("tit");
    }

    public void setTitle(String title){
        setSimpleValue("tit",title);
    }

    public String getContent(){
        return getSimpleStringValue("con");
    }

    public void setContent(String content){
        setSimpleValue("con",content);
    }


    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }
    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("bid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("bid",subjectId);
    }


    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }


    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setDescription(String description){
        setSimpleValue("des",description);
    }

    public int getAllWriterNumber(){
        return getSimpleIntegerValueDef("awm", 0);
    }

    public void setAllWriterNumber(int allWriterNumber){
        setSimpleValue("awm",allWriterNumber);
    }


    public int getAllLoadNumber(){
        return getSimpleIntegerValueDef("alm",0);
    }

    public void setAllLoadNumber(int allLoadNumber){
        setSimpleValue("alm",allLoadNumber);
    }

    public int getStatus(){
        return getSimpleIntegerValue("sta");
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }
    public int getWriteNumber(){
        return getSimpleIntegerValue("wnm");
    }

    public void setWriteNumber(int writeNumber){
        setSimpleValue("wnm",writeNumber);
    }  public int getTalkNumber(){
        return getSimpleIntegerValue("tnm");
    }

    public void setTalkNumber(int talkNumber){
        setSimpleValue("tnm",talkNumber);
    }
    public int getLoadNumber(){
        return getSimpleIntegerValue("lnm");
    }

    public void setLoadNumber(int loadNumber){
        setSimpleValue("lnm",loadNumber);
    }
    public int getQuestionNumber(){
        return getSimpleIntegerValue("qnm");
    }

    public void setQuestionNumber(int questionNumber){
        setSimpleValue("qnm",questionNumber);
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
    public String getSubject(){
        return getSimpleStringValue("sub");
    }

    public void setSubject(String subject){
        setSimpleValue("sub",subject);
    }



    public String getRecipientName(){
        return getSimpleStringValue("rec");
    }

    public void setRecipientName(String recipientName){
        setSimpleValue("rec",recipientName);
    }

    public ObjectId getTutorId(){
        return getSimpleObjecIDValue("tid");
    }

    public void setTutorId(ObjectId tutorId){
        setSimpleValue("tid",tutorId);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }
    public long getLoadTime(){
        return getSimpleLongValue("ltm");
    }
    public void setLoadTime(long loadTime){
        setSimpleValue("ltm",loadTime);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
    public int getMonth(){
        return getSimpleIntegerValue("mon");
    }

    public void setMonth(int month){
        setSimpleValue("mon",month);
    }

    public int getShowType(){
        return getSimpleIntegerValueDef("sht", 0);
    }

    public void setShowType(int showType){
        setSimpleValue("sht",showType);
    }


}
