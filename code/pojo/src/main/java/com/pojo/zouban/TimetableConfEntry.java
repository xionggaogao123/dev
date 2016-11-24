package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/25.
 *
 * 课表结构配置
 * {
 * te : term             学期
 * gid : gradeId         年级id
 * days[] : days         上课天数
 * cc : classCount       每天节数
 * ct[] : classTime      上课时间
 * evn[] : events        不可排课事件(教研活动、学校各种会议、老师个人事务等)
 *
 * //新增字段(新增班级事务,例如某班级)
 * clvn[] : classEvents  班级事务（自习、无课）
 *
 * lock : lock           锁定 0 : 未锁定 1 : 已锁定
 * }
 */
public class TimetableConfEntry extends BaseDBObject {

    public TimetableConfEntry(){
        super();
    }

    public TimetableConfEntry(BasicDBObject baseEntry) {
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

    public List<Integer> getDays() {
        List<Integer> days = new ArrayList<Integer>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("days");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                days.add((Integer) o);
            }
        }
        return days;
    }

    public void setDays(List<Integer> days) {
        setSimpleValue("days", MongoUtils.convert(days));
    }

    public int getClassCount() {
        return getSimpleIntegerValue("cc");
    }

    public void setClassCount(Integer count) {
        setSimpleValue("cc", count);
    }

    public List<String> getClassTime() {
        List<String> classTimeList = new ArrayList<String>();
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("ct");
        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                classTimeList.add((String) o);
            }
        }
        return classTimeList;
    }

    public void setClassTime(List<String> classTimeList) {
        setSimpleValue("ct", MongoUtils.convert(classTimeList));
    }

    public List<Event> getEvent() {
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("evn");
        List<Event> eventList = new ArrayList<Event>();

        if (basicDBList != null) {
            for (Object o : basicDBList) {
                eventList.add(new Event((BasicDBObject) o));
            }
        }

        return eventList;
    }

    public void setEvent(List<Event> eventList) {
        setSimpleValue("evn", MongoUtils.convert(MongoUtils.fetchDBObjectList(eventList)));
    }

    public List<ClassEvent> getClassEvent() {
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("clvn");
        List<ClassEvent> eventList = new ArrayList<ClassEvent>();

        if (basicDBList != null && !basicDBList.isEmpty()) {
            for (Object o : basicDBList) {
                eventList.add(new ClassEvent((BasicDBObject) o));
            }
        }

        return eventList;
    }

    public void setClassEvent(List<ClassEvent> eventList) {
        setSimpleValue("clvn", MongoUtils.convert(MongoUtils.fetchDBObjectList(eventList)));
    }

    public int getLock() {
        return getSimpleIntegerValue("lock");
    }

    public void setLock(int lock) {
        setSimpleValue("lock", lock);
    }


    /**
     *  班级事务
     *  {
     *   nm: name           事务名称
     *   x:x                星期几
     *   y:y                第几节课
     *   cl: classList      班级列表Id
     * }
     */
    public static class ClassEvent extends BaseDBObject{
        public ClassEvent() {
            super();
        }

        public ClassEvent(BasicDBObject baseEntry) {
            setBaseEntry(baseEntry);
        }

        public String getName() {
            return getSimpleStringValue("nm");
        }

        public void setName(String name) {
            setSimpleValue("nm", name);
        }

        public int getX(){
            return getSimpleIntegerValue("x");
        }

        public void setX(int x){
            setSimpleValue("x",x);
        }

        public int getY(){
            return getSimpleIntegerValue("y");
        }

        public void setY(int y){
            setSimpleValue("y",y);
        }

        public List<IdNamePair> getClassList() {
            BasicDBList basicDBList = (BasicDBList)getSimpleObjectValue("cl");
            List<IdNamePair> teacherList = new ArrayList<IdNamePair>();

            if (basicDBList != null) {
                for (Object o : basicDBList) {
                    teacherList.add(new IdNamePair((BasicDBObject) o));
                }
            }

            return teacherList;
        }

        public void setClassList(List<IdNamePair> classList) {
            setSimpleValue("cl", MongoUtils.convert(MongoUtils.fetchDBObjectList(classList)));
        }
    }

    /**
    * 不可排课事务
    * {
        *     nm: name           事务名称
        *     pl: pointList      时间点列表
        *     tl: teacherList    老师列表
        * }
    */
    public static class Event extends BaseDBObject{
        public Event() {
            super();
        }

        public Event(BasicDBObject baseEntry) {
            setBaseEntry(baseEntry);
        }


        public String getName() {
            return getSimpleStringValue("nm");
        }

        public void setName(String name) {
            setSimpleValue("nm", name);
        }

        public List<PointEntry> getPointList() {
            BasicDBList dbList = (BasicDBList) getSimpleObjectValue("pl");
            List<PointEntry> pointList = new ArrayList<PointEntry>();
            if (dbList != null) {
                for (Object o : dbList) {
                    pointList.add(new PointEntry((BasicDBObject) o));
                }
            }

            return pointList;
        }

        public void setPointList(List<PointEntry> pointList) {
            setSimpleValue("pl", MongoUtils.convert(MongoUtils.fetchDBObjectList(pointList)));
        }

        public List<IdNamePair> getTeacherList() {
            BasicDBList basicDBList = (BasicDBList)getSimpleObjectValue("tl");
            List<IdNamePair> teacherList = new ArrayList<IdNamePair>();

            if (basicDBList != null) {
                for (Object o : basicDBList) {
                    teacherList.add(new IdNamePair((BasicDBObject) o));
                }
            }

            return teacherList;
        }

        public void setTeacherList(List<IdNamePair> teacherList) {
            setSimpleValue("tl", MongoUtils.convert(MongoUtils.fetchDBObjectList(teacherList)));
        }
    }
}
