package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 排课后的课表结构
 * {
 * sid:学校id-------------->schoolId
 * te:学期----------------->term
 * gid:年级id-------------->gradeId
 * cid:班级id-------------->classId
 * cli:选择的课程----------->courseList[]
 * ty:类型----------------->type 0发布版、1调课完成版、2调课中、3排课中
 * //以下为新加的
 * lock:是否锁定------------>lock 0未锁定，1已经锁定  解释：用来标记走班课是否已经排完，锁定状态下才可以编排非走班，如解锁则删除所有非走班课
 * week:有效周-------------->week 学期周 0原始状态
 * }
 * Created by qiangm on 2015/9/15.
 */
public class TimeTableEntry extends BaseDBObject {
    public TimeTableEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public TimeTableEntry() {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("sid", null)
                .append("te", null)
                .append("gid", null)
                .append("cid", null)
                .append("cli", new BasicDBList())
                .append("ty", 0)
                .append("lock", 0)
                .append("week", 1);
        setBaseEntry(basicDBObject);
    }

    public TimeTableEntry(String term, ObjectId schoolId, ObjectId gradeId, ObjectId classId,
                          List<CourseItem> courseItemList, int type,int lock, int week) {
        super();
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("sid", schoolId)
                .append("te", term)
                .append("gid", gradeId)
                .append("cid", classId)
                .append("cli", MongoUtils.convert(MongoUtils.fetchDBObjectList(courseItemList)))
                .append("ty", type)
                .append("lock", lock)
                .append("week", week);
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

    public ObjectId getClassId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setClassId(ObjectId classId) {
        setSimpleValue("cid", classId);
    }

    public List<CourseItem> getCourseList() {
        List<CourseItem> courseItemList = new ArrayList<CourseItem>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("cli");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                courseItemList.add(new CourseItem((BasicDBObject) o));
            }
        }
        return courseItemList;
    }

    public void setCourseList(List<CourseItem> courseList) {
        setSimpleValue("cli", MongoUtils.convert(MongoUtils.fetchDBObjectList(courseList)));
    }

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
    }


    public int getLock() {
        if(this.getBaseEntry().containsField("lock")) {
            return getSimpleIntegerValue("lock");
        }
        else
            return 0;
    }

    public void setLock(int lock) {
        setSimpleValue("lock", lock);
    }

    public int getWeek() {
        if(this.getBaseEntry().containsField("week")) {
            return getSimpleIntegerValue("week");
        }
        else
            return 1;
    }

    public void setWeek(int week) {
        setSimpleValue("week", week);
    }
}
