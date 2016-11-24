package com.pojo.zouban;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/10/10.
 *
 * 排课冲突记录
 * {
 *     te : term---------------------学期
 *     gid : gradeId-----------------年级id
 *     x : x-------------------------时间（星期几）
 *     y : y-------------------------时间（第几节）
 *     eid : eventId-----------------冲突事务id
 *     tid : teacherId---------------冲突老师id
 *     cid : courseId----------------冲突课程id
 * }
 *
 */
public class EventConflictEntry extends BaseDBObject {
    public EventConflictEntry() {
        super();
    }

    public EventConflictEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public EventConflictEntry(String term, ObjectId gradeId, int x, int y, ObjectId eventId, ObjectId teacherId, ObjectId courseId) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId)
                .append("x", x)
                .append("y", y)
                .append("eid", eventId)
                .append("tid", teacherId)
                .append("cid", courseId);
        setBaseEntry(baseEntry);
    }


    public String getTerm() {
        return getSimpleStringValue("te");
    }

    public void setTerm(String term) {
        setSimpleValue("te", term);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public int getX() {
        return getSimpleIntegerValue("x");
    }

    public void setX(int x) {
        setSimpleValue("x", x);
    }

    public int getY() {
        return getSimpleIntegerValue("y");
    }

    public void setY(int y) {
        setSimpleValue("y", y);
    }

    public ObjectId getEventId() {
        return getSimpleObjecIDValue("eid");
    }

    public void setEventId(ObjectId eventId) {
        setSimpleValue("eid", eventId);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }

    public ObjectId getCourseId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setCourseId(ObjectId courseId) {
        setSimpleValue("cid", courseId);
    }


}
