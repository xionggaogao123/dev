package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师学历信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 * ui 老师id
 * si
 *  edu学历
 *  degree学位
 *  major专业
 *  time获取时间
 *  open公开
 *
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/26.
 */
public class EducationEntry extends BaseDBObject {
    private static final long serialVersionUID = -1417686496595926348L;

    public EducationEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public EducationEntry(ObjectId userId,ObjectId schoolId,String education,String degree,String major,String time,int open) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si",schoolId)
                .append("edu", education)
                .append("degree",degree)
                .append("major", major)
                .append("time", time)
                .append("open", open);
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
    public String getEducation() {
        return getSimpleStringValue("edu");
    }
    public void setEducation(String education) {
        setSimpleValue("edu",education);
    }
    public String getDegree() {
        return getSimpleStringValue("degree");
    }
    public void setDegree(String degree) {
        setSimpleValue("degree",degree);
    }
    public String getMajor() {
        return getSimpleStringValue("major");
    }
    public void setMajor(String major) {
        setSimpleValue("major",major);
    }
    public String getTime() {
        return getSimpleStringValue("time");
    }
    public void setTime(String time) {
        setSimpleValue("time",time);
    }
    public int getOpen() {
        return getSimpleIntegerValue("open");
    }
    public void setOpen(int open) {
        setSimpleValue("open",open);
    }
}
