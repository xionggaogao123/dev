package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/6.
 * <p/>
 * tm------>term 学期
 * cid----->courseId 课程id
 * cnm------>courseName 课程名字
 * lnm------>lessonName 课时名称
 * tid------>teacherId 老师id
 * tname------>teacherName 老师名字
 * atdct----->attendedCount 出勤人数
 * dt------>date 上课日期
 * wk------>week 第几周
 * day------>day 星期几
 * sct------>section 第几节
 * sl------->studentList 学生列表(内嵌文档 Student)
 * ts------->teacherScore 老师评分
 * cs------->classScore 班级评分
 */
public class AttendanceEntry extends BaseDBObject {
    public AttendanceEntry() {
        super();
    }

    public AttendanceEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public AttendanceEntry(String term, ObjectId courseId, String courseName, String lessonName, ObjectId teacherId, String teacherName, int attendedCount,
                           String date, int week, int day, int section, List<Student> studentList, int teacherScore, int classScore) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("term", term)
                .append("cid", courseId)
                .append("cnm", courseName)
                .append("lnm", lessonName)
                .append("tid", teacherId)
                .append("tnm", teacherName)
                .append("atdct", attendedCount)
                .append("dt", date)
                .append("wk", week)
                .append("day", day)
                .append("sct", section)
                .append("sl", MongoUtils.convert(MongoUtils.fetchDBObjectList(studentList)))
                .append("ts", teacherScore)
                .append("cs", classScore);
        setBaseEntry(baseEntry);
    }

    public String getTerm() {
        return getSimpleStringValue("tm");
    }

    public void setTerm(String term) {
        setSimpleValue("tm", term);
    }

    public ObjectId getCourseId() {
        return getSimpleObjecIDValue("cid");
    }

    public void setCourseId(ObjectId courseId) {
        setSimpleValue("cid", courseId);
    }

    public String getCourseName() {
        return getSimpleStringValue("cnm");
    }

    public void setCourseName(String courseName) {
        setSimpleValue("cnm", courseName);
    }

    public String getLessonName() {
        return getSimpleStringValue("lnm");
    }

    public void setLessonName(String lessonName) {
        setSimpleValue("lnm", lessonName);
    }

    public ObjectId getTeacherId() {
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId) {
        setSimpleValue("tid", teacherId);
    }

    public String getTeacherName() {
        return getSimpleStringValue("tnm");
    }

    public void setTeacherName(String teacherName) {
        setSimpleValue("tnm", teacherName);
    }

    public int getAttendedCount() {
        return getSimpleIntegerValue("atdct");
    }

    public void setAttendedCount(int attendedCount) {
        setSimpleValue("atdct", attendedCount);
    }

    public String getDate() {
        if (getBaseEntry().containsField("dt")) {
            return getSimpleStringValue("dt");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String dateStr = format.format(date);
            return dateStr;
        }
    }

    public void setDate(String date) {
        setSimpleValue("dt", date);
    }

    public int getWeek() {
        return getSimpleIntegerValue("wk");
    }

    public void setWeek(int week) {
        setSimpleValue("wk", week);
    }

    public int getDay() {
        return getSimpleIntegerValue("day");
    }

    public void setDay(int day) {
        setSimpleValue("day", day);
    }

    public int getSection() {
        return getSimpleIntegerValue("sct");
    }

    public void setSection(int section) {
        setSimpleValue("sct", section);
    }

    public List<Student> getStudentList() {
        List<Student> studentList = new ArrayList<Student>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("sl");
        if (null != dbList && dbList.size() > 0) {
            for (Object o : dbList) {
                studentList.add(new Student((BasicDBObject) o));
            }
        }
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        setSimpleValue("sl", MongoUtils.convert(MongoUtils.fetchDBObjectList(studentList)));
    }

    public int getTeacherScore() {
        return getSimpleIntegerValue("ts");
    }

    public void setTeacherScore(int teacherScore) {
        setSimpleValue("ts", teacherScore);
    }

    public int getClassScore() {
        return getSimpleIntegerValue("cs");
    }

    public void setClassScore(int classScore) {
        setSimpleValue("cs", classScore);
    }


    /**
     * 学生考勤记录
     * sid------->studentId 学生id
     * sn-------->studentName 学生姓名
     * atd-------->attendance 是否出勤 1：出勤 0：缺勤
     * sc1--------->score1 课堂表现
     * sc2---------->score2 遵守纪律
     * sc3----------->score3 爱护公物
     */
    public static class Student extends BaseDBObject {
        public Student() {
            super();
        }

        public Student(BasicDBObject baseEntry) {
            super(baseEntry);
        }

        public ObjectId getStudentId() {
            return getSimpleObjecIDValue("sid");
        }

        public void setStudentId(ObjectId studentId) {
            setSimpleValue("sid", studentId);
        }

        public String getStudentName() {
            return getSimpleStringValue("sn");
        }

        public void setStudentName(String studentName) {
            setSimpleValue("sn", studentName);
        }

        public int getAttendance() {
            return getSimpleIntegerValue("atd");
        }

        public void setAttendance(int attendance) {
            setSimpleValue("atd", attendance);
        }

        public int getScore1() {
            if (getBaseEntry().containsField("sc1")) {
                return getSimpleIntegerValue("sc1");
            } else {
                return 5;
            }
        }

        public void setScore1(int score) {
            setSimpleValue("sc1", score);
        }

        public int getScore2() {
            if (getBaseEntry().containsField("sc2")) {
                return getSimpleIntegerValue("sc2");
            } else {
                return 5;
            }
        }

        public void setScore2(int score) {
            setSimpleValue("sc2", score);
        }

        public int getScore3() {
            if (getBaseEntry().containsField("sc3")) {
                return getSimpleIntegerValue("sc3");
            } else {
                return 5;
            }
        }

        public void setScore3(int score) {
            setSimpleValue("sc3", score);
        }
    }
}
