package com.db.school;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.SynDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.attendance.Attendance;
import com.pojo.attendance.AttendanceEntry;
import com.sys.constants.Constant;

/**
 * Created by qiangm on 2015/7/18.
 */
public class AttendanceDao extends SynDao {

    /**
     * 根据学生id获取其考勤记录
     * @param stuId
     * @return
     */
    public List<Attendance> getAttendanceEntryListByStuId(ObjectId stuId,ObjectId classId)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("sid",stuId);
        query.append("cid",classId);
        BasicDBObject sort=new BasicDBObject();
        sort.append("dt",-1);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ATTENDANCE_NAME,query,Constant.FIELDS,sort);
        List<Attendance> attendances=new ArrayList<Attendance>();
        for(DBObject dbObject : list)
        {
            attendances.add(new Attendance(new AttendanceEntry((BasicDBObject)dbObject)));
        }
        return attendances;
    }

    /**
     * 判断该时间段内的考勤是否已经有,有则返回true，表示不可添加
     * @return
     */
    public boolean checkIfHave(Attendance attendance)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("sid",new ObjectId(attendance.getStudentId()));
        query.append("cid",new ObjectId(attendance.getClassId()));
        query.append("dt",attendance.getDate());
        List<DBObject> result=find(MongoFacroty.getAppDB(),Constant.COLLECTION_ATTENDANCE_NAME,query,Constant.FIELDS);
        if(attendance.getTime()==0)//全天
        {
            if(result.size()==0)
            {
                return false;
            }
            else
                return true;
        }
        else
        {
            List<AttendanceEntry> attendanceList=new ArrayList<AttendanceEntry>();
            for(DBObject dbObject : result)
            {
                attendanceList.add(new AttendanceEntry((BasicDBObject)dbObject));
            }
            for(AttendanceEntry attendanceEntry : attendanceList)
            {
                if(attendanceEntry.getTime()==0||attendanceEntry.getTime()==attendance.getTime())//已存在和本次添加的一致或全天类型的
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 添加考勤记录
     * @param attendanceEntry
     */
    public void save(AttendanceEntry attendanceEntry)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ATTENDANCE_NAME,attendanceEntry.getBaseEntry());
    }

    /**
     * 删除考勤记录
     * @param attendanceId
     */
    public void delete(ObjectId attendanceId)
    {
        BasicDBObject query=new BasicDBObject(Constant.ID,attendanceId);
        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_ATTENDANCE_NAME,query);
    }
}
