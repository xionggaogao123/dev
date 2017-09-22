package com.pojo.operation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * title: 作业表
 * Created by James on 2017/8/25.
 * id                             		id
 description      作业内容       		des
 loadTime         作业提交时间          ltm
 status             作业状态            sta  0 已发布    1 定时发布   2 暂不发布
 writeNumber      家长签字数            wnm
 talkNumber       家长讨论数            tnm
 loadNumber       学生提交数            lnm
 questionNumber   学生提问数            qnm
 imageUrl         图片           		img
 subject          学科标签         	    sub
 adminId          发布人id              aid
 recipientName    接收人社区名          rec
 recipientId      接收人社区id          rid
 createTime       创建日期              ctm
 dateTime         发布日期时间          dtm
 month            月份                  mon
 year             年份                  yea
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
            long loadTime,
            int status,
            int writeNumber,
            int talkNumber,
            int loadNumber,
            int questionNumber,
            List<String> imageUrl,
            String subject,
            ObjectId adminId,
            String recipientName,
            ObjectId recipientId,
            int month,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("des",description)
                .append("ltm", loadTime)
                .append("sta", status)
                .append("wnm", writeNumber)
                .append("tnm",talkNumber)
                .append("lnm",loadNumber)
                .append("qnm",questionNumber)
                .append("img", imageUrl)
                .append("sub",subject)
                .append("aid",adminId)
                .append("rec", recipientName)
                .append("rid",recipientId)
                .append("dtm", dateTime)
                .append("mon",month)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public AppCommentEntry(
            ObjectId id,
            String description,
            long loadTime,
            int status,
            int writeNumber,
            int talkNumber,
            int loadNumber,
            int questionNumber,
            List<String> imageUrl,
            String subject,
            ObjectId adminId,
            String recipientName,
            ObjectId recipientId,
            int month,
            long dateTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("des", description)
                .append("ltm", loadTime)
                .append("sta", status)
                .append("wnm", writeNumber)
                .append("tnm",talkNumber)
                .append("lnm", loadNumber)
                .append("qnm",questionNumber)
                .append("img", imageUrl)
                .append("sub",subject)
                .append("aid",adminId)
                .append("rec",recipientName)
                .append("rid",recipientId)
                .append("dtm", dateTime)
                .append("mon",month)
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

    public void se(List<String> imageUrl){
        setSimpleValue("img",imageUrl);
    }
    public List<String> getImageUrl(){
        @SuppressWarnings("rawtypes")
        List imageUrl =(List)getSimpleObjectValue("img");
        return imageUrl;
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

}
