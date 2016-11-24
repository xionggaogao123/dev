package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 课表结构配置
 * {
 * sid:学校id---------------->schoolId
 * te:学期------------------->term
 * gid:年级id---------------->gradeId
 * days:上课天数-------------->classDays
 * cou:每天节数--------------->classCount
 * cti:上课时间--------------->classTime
 * evn:不可排课事件------------>events
 * }
 * Created by qiangm on 2015/9/15.
 */
public class CourseConfEntry extends BaseDBObject {
    public CourseConfEntry() {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("sid", null)
                .append("te", null)
                .append("gid", null)
                .append("days", new BasicDBList())
                .append("cou", 0)
                .append("cti", new BasicDBList())
                .append("evn", new BasicDBList());
        setBaseEntry(basicDBObject);
    }
    public CourseConfEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public CourseConfEntry(ObjectId schoolId, String term, ObjectId gradeId, List<Integer> days, int count,
                           List<String> classTime, List<CourseEvent> events) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("te", term)
                .append("gid", gradeId)
                .append("days", MongoUtils.convert(days))
                .append("cou", count)
                .append("cti", MongoUtils.convert(classTime))
                .append("evn", MongoUtils.convert(MongoUtils.fetchDBObjectList(events)));
        setBaseEntry(basicDBObject);
    }

    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("sid");
    }

    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("sid", schoolId);
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

    public List<Integer> getClassDays() {
        List<Integer> classDays = new ArrayList<Integer>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("days");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                classDays.add((Integer) o);
            }
        }
        return classDays;
    }

    public void setClassDays(List<Integer> classDays) {
        setSimpleValue("days", MongoUtils.convert(classDays));
    }

    public int getClassCount() {
        return getSimpleIntegerValue("cou");
    }

    public void setClassCount(Integer count) {
        setSimpleValue("cou", count);
    }

    public List<String> getClassTime() {
        List<String> classTimeList = new ArrayList<String>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("cti");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                classTimeList.add((String) o);
            }
        }
        return classTimeList;
    }

    public void setClassTimeLists(List<String> classTimeList) {
        setSimpleValue("cti", MongoUtils.convert(classTimeList));
    }

    public List<CourseEvent> getClassEvents() {
        List<CourseEvent> classEvents = new ArrayList<CourseEvent>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("evn");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                classEvents.add(new CourseEvent((BasicDBObject)o));
            }
        }
        return classEvents;
    }

    public void setClassEvents(List<CourseEvent> classEvents) {
        setSimpleValue("evn", MongoUtils.convert(MongoUtils.fetchDBObjectList(classEvents)));
    }
}
