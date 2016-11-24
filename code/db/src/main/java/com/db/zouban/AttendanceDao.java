package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.attendance.Attendance;
import com.pojo.zouban.AttendanceEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/6.
 */
public class AttendanceDao extends BaseDao {
    /**
     * 新增考勤（课时）
     */
    public ObjectId add(AttendanceEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 修改考勤（课时）
     */
    public void update(ObjectId id, String lessonName, String date, int week, int day, int section) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("lnm", lessonName)
                .append("dt", date)
                .append("wk", week)
                .append("day", day)
                .append("sct", section);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, query, updateValue);
    }

    /**
     * 根据学期和课程id获取考勤列表
     */
    public List<AttendanceEntry> getAttendanceList(String term, ObjectId courseId) {
        BasicDBObject query = new BasicDBObject("tm", term).append("cid", courseId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, query, Constant.FIELDS);
        List<AttendanceEntry> attendanceEntryList = new ArrayList<AttendanceEntry>();

        if (dbObjectList != null && dbObjectList.size() > 0) {
            for (DBObject o : dbObjectList) {
                AttendanceEntry attendanceEntry = new AttendanceEntry((BasicDBObject) o);
                attendanceEntryList.add(attendanceEntry);
            }
        }

        return attendanceEntryList;
    }


    /**
     * 根据id获取考勤（课时）
     */
    public AttendanceEntry findAttendanceById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, query, Constant.FIELDS);
        return new AttendanceEntry((BasicDBObject) dbObject);
    }


    /**
     * 修改老师评分
     */
    public void updateTeacherScore(ObjectId id, int score) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("ts", score);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, query, updateValue);
    }

    /**
     * 修改班级评分
     */
    public void updateClassScore(ObjectId id, int score) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject("cs", score);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, query, updateValue);

    }

    /**
     * 修改学生评分
     */
    public void updateStudentScore(ObjectId id, ObjectId studentId, String scoreItem, int score) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id).append("sl.sid", studentId);
        BasicDBObject update = new BasicDBObject();
        if (scoreItem.equals("sc1")) {
            update.append("sl.$.sc1", score);
        } else if (scoreItem.equals("sc2")) {
            update.append("sl.$.sc2", score);
        } else {
            update.append("sl.$.sc3", score);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, query, updateValue);
    }

    /**
     * 修改学生考勤
     */
    public void updateStudentAttendance(ObjectId id, ObjectId studentId, int attendance) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id).append("sl.sid", studentId);
        BasicDBObject update = new BasicDBObject("sl.$.atd", attendance);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
        if (attendance == 0) {
            updateValue.append(Constant.MONGO_INC, new BasicDBObject("atdct", -1));
        } else {
            updateValue.append(Constant.MONGO_INC, new BasicDBObject("atdct", 1));
        }
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_ATTENDANCE, query, updateValue);
    }


}
