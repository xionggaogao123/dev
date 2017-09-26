package com.pojo.smalllesson;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * 复兰互动小课堂
 * Created by James on 2017/9/26.
 * id
 * name         课程名           nam
 * dateTime     上课日期         dtm
 * userId       用户id           uid
 * isRemove     是否删除         isr
 * 
 *
 */
public class SmallLessonEntry extends BaseDBObject {
    public SmallLessonEntry(){

    }
    public SmallLessonEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public SmallLessonEntry(
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
    public SmallLessonEntry(
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
