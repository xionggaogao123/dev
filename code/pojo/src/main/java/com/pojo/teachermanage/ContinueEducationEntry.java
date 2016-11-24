package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师继续教育信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 *  time时间
 *  trais培训机构Training institutions
 *  course培训课程
 *  cerf证书certificate
 *  address 地点
 *  classhour课时
 *  record成绩
 *  content
 *  open公开
 *
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class ContinueEducationEntry extends BaseDBObject {
    private static final long serialVersionUID = -4336876235252331531L;

    public ContinueEducationEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }
    public ContinueEducationEntry(ObjectId userId,ObjectId schoolId,String time,String institutions,String course,String certificate,String address,String classHour,int record,String content,int open) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si",schoolId)
                .append("time", time)
                .append("trais",institutions)
                .append("course", course)
                .append("cerf", certificate)
                .append("address", address)
                .append("ch", classHour)
                .append("record", record)
                .append("con",content)
                .append("open",open);
        setBaseEntry(dbo);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si",schoolId);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui",userId);
    }
    public String getTime() {
        return getSimpleStringValue("time");
    }
    public void setTime(String time) {
        setSimpleValue("time",time);
    }
    public String getContent() {
        return getSimpleStringValue("con");
    }
    public void setContent(String content) {
        setSimpleValue("con",content);
    }
    public String getInstitutions() {
        return getSimpleStringValue("trais");
    }
    public void setInstitutions(String institutions) {
        setSimpleValue("trais",institutions);
    }
    public String getCourse() {
        return getSimpleStringValue("course");
    }
    public void setCourse(String course) {
        setSimpleValue("course",course);
    }
    public String getCertificate() {
        return getSimpleStringValue("cerf");
    }
    public void setCertificate(String certificate) {
        setSimpleValue("cerf",certificate);
    }
    public String getAddress() {
        return getSimpleStringValue("address");
    }
    public void setAddress(String address) {
        setSimpleValue("address",address);
    }
    public String getClassHour() {
        return getSimpleStringValue("ch");
    }
    public void setClassHour(String classHour) {
        setSimpleValue("ch",classHour);
    }
    public int getRecord() {
        return getSimpleIntegerValue("record");
    }
    public void setRecord(int record) {
        setSimpleValue("record",record);
    }

    public int getOpen() {
        return getSimpleIntegerValue("open");
    }
    public void setOpen(int open) {
        setSimpleValue("open",open);
    }
}
