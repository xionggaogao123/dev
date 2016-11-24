package com.fulaan.zouban.service;

import com.db.school.ClassDao;
import com.db.user.UserDao;
import com.db.zouban.AttendanceDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.zouban.dto.AttendanceDTO;
import com.fulaan.zouban.dto.AttendanceStudentDTO;
import com.fulaan.zouban.dto.ZouBanCourseDTO;
import com.fulaan.zouban.dto.TermDTO;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.pojo.zouban.AttendanceEntry;
import com.pojo.zouban.PointEntry;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangkaidong on 2016/5/3.
 */
@Service
public class AttendanceService {
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private ClassDao classDao = new ClassDao();
    private AttendanceDao attendanceDao = new AttendanceDao();
    private UserDao userDao = new UserDao();
    @Autowired
    private TermService termService;



    /**
     * 获取课程列表
     * */
    public List<ZouBanCourseDTO> getCourseList(String term,String grade,int courseType,String subject,ObjectId teacherId,String schoolId,int skip,int limit){
        List<ZouBanCourseDTO> courseList = new ArrayList<ZouBanCourseDTO>();
        List<ZouBanCourseEntry> entryList = zouBanCourseDao.findCourseList(term, new ObjectId(grade), courseType, subject, teacherId,new ObjectId(schoolId),skip,limit);
        for(ZouBanCourseEntry entry: entryList){
            ZouBanCourseDTO dto = new ZouBanCourseDTO(entry);
            if(courseType == 2){//非走班
                String className = classDao.getClassEntryById(entry.getClassId().get(0), Constant.FIELDS).getName();
                dto.setClassName(className);
            }
            courseList.add(dto);
        }
        return courseList;
    }

    /**
     * 获取课程数量（分页）
     * */
    public int getCourseCount(String term,String grade,int courseType,String subject,ObjectId teacherId,String schoolId){
        int count = zouBanCourseDao.courseListCount(term, new ObjectId(grade), courseType, subject, teacherId, new ObjectId(schoolId));
        return count;
    }


    /**
     * 获取本学期总教学周
     * */
    public int getWeek(String schoolId,String yearAndTerm){
        int term = yearAndTerm.contains("一") ? 1 : 2;
        TermDTO termDTO = termService.findTermDTO(yearAndTerm.substring(0, yearAndTerm.indexOf("第")), new ObjectId(schoolId));
        int weekAmount;
        if(term == 1){
            weekAmount = termDTO.getFweek();
        }else {
            weekAmount = termDTO.getSweek();
        }

        return weekAmount;
    }

    /**
     * 获取课时列表
     * */
    public List<AttendanceDTO> getLessonList(String term,String courseId){
        List<AttendanceEntry> entryList = attendanceDao.getAttendanceList(term,new ObjectId(courseId));
        List<AttendanceDTO> list = new ArrayList<AttendanceDTO>();
        for(AttendanceEntry entry : entryList){
            list.add(new AttendanceDTO(entry));
        }

        return list;
    }


    /**
     * 新建课时
     * */
    public void addLesson(String yearAndTerm,String courseId,String lessonName,String date,int week,int day,int section){
        ZouBanCourseEntry courseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(courseId));

        AttendanceEntry attendanceEntry = new AttendanceEntry();
        attendanceEntry.setTerm(yearAndTerm);
        attendanceEntry.setCourseId(new ObjectId(courseId));
        attendanceEntry.setCourseName(courseEntry.getClassName());
        attendanceEntry.setLessonName(lessonName);
        attendanceEntry.setTeacherId(courseEntry.getTeacherId());
        attendanceEntry.setTeacherName(courseEntry.getTeacherName());
        attendanceEntry.setAttendedCount(courseEntry.getStudentList().size());
        attendanceEntry.setDate(date);
        attendanceEntry.setWeek(week);
        attendanceEntry.setDay(day);
        attendanceEntry.setSection(section);
        attendanceEntry.setStudentList(getStudentList(courseEntry.getStudentList()));
        attendanceEntry.setTeacherScore(5);
        attendanceEntry.setClassScore(5);

        attendanceDao.add(attendanceEntry);
    }


    /**
     * 获取学生列表
     * */
    private List<AttendanceEntry.Student> getStudentList(List<ObjectId> ids){
        List<AttendanceEntry.Student> studentList = new ArrayList<AttendanceEntry.Student>();
        for(ObjectId id : ids){
            UserEntry userEntry = userDao.getUserEntry(id,Constant.FIELDS);
            AttendanceEntry.Student student = new AttendanceEntry.Student();
            student.setStudentId(userEntry.getID());
            student.setStudentName(userEntry.getUserName());
            student.setAttendance(1);
            student.setScore1(5);
            student.setScore2(5);
            student.setScore3(5);
            studentList.add(student);
        }
        return studentList;
    }

    /**
     * 修改课时
     * */
    public void updateLesson(String lessonId,String lessonName,String date,int week,int day,int section){
        attendanceDao.update(new ObjectId(lessonId),lessonName,date,week,day,section);
    }

    /**
     * 通过id获取考勤
     * @param attendanceId
     * */
    public AttendanceDTO getAttendance(String attendanceId){
        AttendanceEntry entry = attendanceDao.findAttendanceById(new ObjectId(attendanceId));
        return new AttendanceDTO(entry);
    }


    /**
     * 考勤
     * */
    public void attend(String attendanceId,String studentId,int attendance){
        attendanceDao.updateStudentAttendance(new ObjectId(attendanceId),new ObjectId(studentId),attendance);
    }

    /**
     * 打分
     * */
    public void score(String attendanceId,String studentId,String scoreItem,int score){
        attendanceDao.updateStudentScore(new ObjectId(attendanceId),new ObjectId(studentId),scoreItem,score);
    }

    /**
     * 巡视打分老师评分
     * */
    public void teacherScore(String attendanceId,int score){
        attendanceDao.updateTeacherScore(new ObjectId(attendanceId),score);
    }

    /**
     * 巡视打分班级评分
     * */
    public void classScore(String attendanceId,int score){
        attendanceDao.updateClassScore(new ObjectId(attendanceId),score);
    }


}
