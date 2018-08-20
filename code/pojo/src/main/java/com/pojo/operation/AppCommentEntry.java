package com.pojo.operation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * title: 作业表
 * Created by James on 2017/8/25.
 * id                             		id
 description      作业内容       		des
 title            标题                  tit
 loadTime         作业提交时间          ltm
 status           作业状态              sta  0 已发布    1 定时发布   2 暂不发布
 writeNumber      家长签字数            wnm
 allWriterNumber  家长签字总数          awm
 allLoadNumber    学生提交总数          alm
 talkNumber       家长讨论数            tnm
 loadNumber       学生提交数            lnm
 questionNumber   学生提问数            qnm
 List<VideoEntry> videoList,                    vl//视频
 List<AttachmentEntry> attachmentEntries,       ats//文件
 List<AttachmentEntry> imageList                il//图片
 List<AttachmentEntry> voiceList                il//语音
 subject          学科标签         	    sub
 subjectId        学科id                sid
 adminId          发布人id              aid
 recipientName    接收人社区名          rec
 recipientId      接收人社区id          rid
 createTime       创建日期              ctm
 dateTime         发布日期时间          dtm
 month            月份                  mon
 year             年份                  yea

 showType         是否展示              sht    是否展示  2 不展示  1 展示
 */
public class AppCommentEntry extends BaseDBObject {
    public AppCommentEntry(){

    }
    public AppCommentEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public AppCommentEntry(
            String description,
            String title,
            long loadTime,
            int status,
            int writeNumber,
            int allWriterNumber,
            int allLoadNumber,
            int talkNumber,
            int loadNumber,
            int questionNumber,
            List<AttachmentEntry> imageList,
            List<AttachmentEntry> voiceList,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries,
            String subject,
            ObjectId subjectId,
            ObjectId adminId,
            String recipientName,
            ObjectId recipientId,
            int month,
            int showType,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("des",description)
                .append("tit", title)
                .append("ltm", loadTime)
                .append("sta", status)
                .append("wnm", writeNumber)
                .append("awm",allWriterNumber)
                .append("alm",allLoadNumber)
                .append("tnm",talkNumber)
                .append("lnm",loadNumber)
                .append("qnm",questionNumber)
                .append("ats", MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("vl", MongoUtils.fetchDBObjectList(videoList))
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("sub", subject)
                .append("sid",subjectId)
                .append("aid",adminId)
                .append("rec", recipientName)
                .append("rid",recipientId)
                .append("dtm", dateTime)
                .append("mon",month)
                .append("sht",showType)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public AppCommentEntry(
            ObjectId id,
            String description,
            String title,
            long loadTime,
            int status,
            int writeNumber,
            int allWriterNumber,
            int allLoadNumber,
            int talkNumber,
            int loadNumber,
            int questionNumber,
            List<AttachmentEntry> imageList,
            List<AttachmentEntry> voiceList,
            List<VideoEntry> videoList,
            List<AttachmentEntry> attachmentEntries,
            String subject,
            ObjectId subjectId,
            ObjectId adminId,
            String recipientName,
            ObjectId recipientId,
            int month,
            int showType,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("des", description)
                .append("tit", title)
                .append("ltm", loadTime)
                .append("sta", status)
                .append("wnm", writeNumber)
                .append("awm", allWriterNumber)
                .append("alm",allLoadNumber)
                .append("tnm", talkNumber)
                .append("lnm", loadNumber)
                .append("qnm",questionNumber)
                .append("ats", MongoUtils.fetchDBObjectList(attachmentEntries))
                .append("vt", MongoUtils.fetchDBObjectList(voiceList))
                .append("vl", MongoUtils.fetchDBObjectList(videoList))
                .append("il", MongoUtils.fetchDBObjectList(imageList))
                .append("sub",subject)
                .append("sid",subjectId)
                .append("aid", adminId)
                .append("rec",recipientName)
                .append("rid",recipientId)
                .append("dtm", dateTime)
                .append("mon",month)
                .append("sht",showType)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getAdminId(){
        return getSimpleObjecIDValue("aid");
    }

    public void setAdminId(ObjectId adminId){
        setSimpleValue("aid",adminId);
    }
    public ObjectId getRecipientId(){
        return getSimpleObjecIDValue("rid");
    }

    public void setRecipientId(ObjectId recipientId){
        setSimpleValue("rid",recipientId);
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

    public String getTitle(){
        return getSimpleStringValue("tit");
    }

    public void setTitle(String title){
        setSimpleValue("tit",title);
    }
    public ObjectId getSubjectId(){
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId){
        setSimpleValue("sid",subjectId);
    }
    public String getRecipientName(){
        return getSimpleStringValue("rec");
    }

    public void setRecipientName(String recipientName){
        setSimpleValue("rec",recipientName);
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
    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
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
