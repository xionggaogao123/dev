package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 教师不可排课个人事务
 * {
 * id:-------------->id
 * sid:学科id-------->subjectId
 * tid:教师id-------->teacherId
 * evn:事情---------->event
 * }
 * Created by qiangm on 2015/9/15.
 */
@Deprecated
public class TeacherEvent extends BaseDBObject {
    public TeacherEvent(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public TeacherEvent() {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("id", null)
                .append("sid", null)
                .append("tid", null)
                .append("evn", null);
        setBaseEntry(basicDBObject);
    }

    public TeacherEvent(ObjectId id, ObjectId subjectId, ObjectId teacherId, String event) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("id", id)
                .append("sid", subjectId)
                .append("tid", teacherId)
                .append("evn", event);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }

    public void setId(ObjectId id) {
        setSimpleValue("id", id);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("sid", subjectId);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }

    public String getEvent() {
        return getSimpleStringValue("evn");
    }

    public void setEvent(String event) {
        setSimpleValue("evn", event);
    }
}
