package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师职称信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 * ui
 *  name名称
 *  time获取时间
 *  jty岗位类别jobtype
 *  level岗位等级
 *  apptime聘任时间appointment time
 *  open公开
 *
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/29.
 */
public class TitleEntry extends BaseDBObject {
    private static final long serialVersionUID = -4175222244169027183L;

    public TitleEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public TitleEntry(ObjectId userId,ObjectId schoolId,String name,String time,String jobType,String level,String appointmentTime,int open) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si",schoolId)
                .append("name", name)
                .append("time",time)
                .append("jty", jobType)
                .append("level", level)
                .append("apptime",appointmentTime)
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
    public String getName() {
        return getSimpleStringValue("name");
    }
    public void setName(String name) {
        setSimpleValue("name",name);
    }
    public String getTime() {
        return getSimpleStringValue("time");
    }
    public void setTime(String time) {
        setSimpleValue("time",time);
    }
    public String getJobType() {
        return getSimpleStringValue("jty");
    }
    public void setJobType(String jobType) {
        setSimpleValue("jty",jobType);
    }
    public String getLevel() {
        return getSimpleStringValue("level");
    }
    public void setLevel(String level) {
        setSimpleValue("level",level);
    }
    public String getAppointmentTime() {
        return getSimpleStringValue("apptime");
    }
    public void setAppointmentTime(String appointmentTime) {
        setSimpleValue("apptime",appointmentTime);
    }
    public int getOpen() {
        return getSimpleIntegerValue("open");
    }
    public void setOpen(int open) {
        setSimpleValue("open",open);
    }
}
