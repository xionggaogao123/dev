package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 不可排课的事件
 * {
 * id:----------------->id
 * xi:星期几------------>xIndex
 * yi:第几节课---------->yIndex
 * fb:不可选课---------->forbid[]
 * gs:集体调研---------->groupStudy
 * pe:个人事务---------->personEvent
 * }
 * Created by qiangm on 2015/9/15.
 */
public class CourseEvent extends BaseDBObject {
    public CourseEvent(BasicDBObject basicDBObject)
    {
        super(basicDBObject);
    }
    public CourseEvent() {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("id", null)
                .append("xi", 0)
                .append("yi", 0)
                .append("fb", new BasicDBList())
                .append("gs", new BasicDBList())
                .append("pe", new BasicDBList());
        setBaseEntry(basicDBObject);
    }

    public CourseEvent(ObjectId id, int xIndex, int yIndex, List<String> forbidList, List<ObjectId> groupStudy,
                       List<TeacherEvent> teacherEvent) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("id", id)
                .append("xi", xIndex)
                .append("yi", yIndex)
                .append("fb", MongoUtils.convert(forbidList))
                .append("gs", MongoUtils.convert(groupStudy))
                .append("pe", MongoUtils.convert(MongoUtils.fetchDBObjectList(teacherEvent)));
        setBaseEntry(basicDBObject);
    }

    public ObjectId getId() {
        return getSimpleObjecIDValue("id");
    }

    public void setId(ObjectId id) {
        setSimpleValue("id", id);
    }

    public int getXIndex() {
        return getSimpleIntegerValue("xi");
    }

    public void setXIndex(int xIndex) {
        setSimpleValue("xi", xIndex);
    }

    public int getYIndex() {
        return getSimpleIntegerValue("yi");
    }

    public void setYIndex(int yIndex) {
        setSimpleValue("yi", yIndex);
    }

    public List<String> getForbidEvent() {
        List<String> forbidList = new ArrayList<String>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("fb");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                forbidList.add((String) o);
            }
        }
        return forbidList;
    }

    public void setForbidEvent(List<String> forbids) {
        setSimpleValue("fb", MongoUtils.convert(forbids));
    }

    public List<ObjectId> getGroupStudy() {
        List<ObjectId> groupStudyList = new ArrayList<ObjectId>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("gs");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                groupStudyList.add((ObjectId) o);
            }
        }
        return groupStudyList;
    }

    public void setGroupStudy(List<ObjectId> groupStudy) {
        setSimpleValue("gs", MongoUtils.convert(groupStudy));
    }

    public List<TeacherEvent> getPersonEvent() {
        List<TeacherEvent> teacherEvents = new ArrayList<TeacherEvent>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("pe");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                teacherEvents.add(new TeacherEvent( (BasicDBObject)o));
            }
        }
        return teacherEvents;
    }

    public void setPersonEvent(List<TeacherEvent> teacherEvents) {
        setSimpleValue("pe", MongoUtils.convert(MongoUtils.fetchDBObjectList(teacherEvents)));
    }
}




