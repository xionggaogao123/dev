package com.fulaan.zouban.controller.zhuzhou;

import com.fulaan.base.controller.BaseController;
import com.fulaan.zouban.dto.AttendanceDTO;
import com.fulaan.zouban.dto.ZouBanCourseDTO;
import com.fulaan.zouban.service.AttendanceService;
import com.pojo.user.UserRole;
import com.pojo.zouban.AttendanceEntry;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/4/29.
 *
 * 株洲 考勤Controller
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController extends BaseController{
    @Autowired
    private AttendanceService attendanceService;


    /**
     * 班级页面
     * */
    @RequestMapping("/course")
    public String courseListPage(Map<String,Object> model){
        model.put("role",getSessionValue().getUserRole());
        model.put("myName",getSessionValue().getUserName());

        return "zouban/attendance/classlist";
    }

    /**
     * 课时页面
     * */
    @RequestMapping("/lesson")
    public String lesson(){
        return "zouban/attendance/lesson";
    }

    /**
     * 考勤页面
     * */
    @RequestMapping("/attendAndScore")
    public String attendance(){
        return "zouban/attendance/attendance";
    }

    /**
     * 巡视打分
     * */
    @RequestMapping("/xunshiScore")
    public String adminAttendance(){
        return "zouban/attendance/xunshiscore";
    }


    /**
     * 获取班级列表
     * */
    @RequestMapping("/courseList")
    @ResponseBody
    public Map<String,Object> getCourseList(String term,String grade,int courseType,String subject,int page,int pageSize){
        ObjectId teacherId = null;
        int role = getSessionValue().getUserRole();
        if(UserRole.isTeacherOnly(role)){
            teacherId = getUserId();
        }
        List<ZouBanCourseDTO> courseList = attendanceService.getCourseList(term,grade,courseType,subject,teacherId,getSessionValue().getSchoolId(),(page-1)*pageSize,pageSize);
        int count = attendanceService.getCourseCount(term,grade,courseType,subject,teacherId,getSessionValue().getSchoolId());
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("courseList",courseList);
        result.put("page",page);
        result.put("pageSize",pageSize);
        result.put("count",count);
        return result;
    }

    /**
     * 获取教学周总数
     * */
    @RequestMapping("week")
    @ResponseBody
    public Map<String,Object> getWeek(String term){
        int week = attendanceService.getWeek(getSessionValue().getSchoolId(),term);
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("week",week);
        return result;
    }


    /**
     * 获取课时列表
     * */
    @RequestMapping("/lessonList")
    @ResponseBody
    public Map<String,Object> getLessonList(String term,String courseId){
        Map<String,Object> result = new HashMap<String, Object>();
        List<AttendanceDTO> list = attendanceService.getLessonList(term,courseId);
        result.put("lessonList",list);
        return result;
    }

    /**
     * 新增&修改课时
     * */
    @RequestMapping("/addOrUpdateLesson")
    @ResponseBody
    public RespObj addOrUpdateLesson(String lessonId,String term,String courseId,String lessonName,String date,int week,int day,int section){
        RespObj respObj = RespObj.FAILD;
        try{
            if(StringUtils.isNotBlank(lessonId)){//修改
                attendanceService.updateLesson(lessonId,lessonName,date,week,day,section);
            }else{//新增
                attendanceService.addLesson(term,courseId,lessonName,date,week,day,section);
            }
            respObj = RespObj.SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
        }

        return respObj;
    }


    /**
     * 通过id获取考勤
     * @param attendanceId
     * */
    @RequestMapping("/getAttendance")
    @ResponseBody
    public Map<String,Object> getAttendance(String attendanceId){
        AttendanceDTO dto = attendanceService.getAttendance(attendanceId);
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("attendance",dto);
        return result;
    }


    /**
     * 考勤
     * */
    @RequestMapping("/attend")
    @ResponseBody
    public RespObj attend(String attendanceId,String studentId,int attendance){
        RespObj respObj = RespObj.FAILD;
        try{
            attendanceService.attend(attendanceId,studentId,attendance);
            respObj = RespObj.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 打分
     * */
    @RequestMapping("/score")
    @ResponseBody
    public RespObj score(String attendanceId,String studentId,String scoreItem,int score){
        RespObj respObj = RespObj.FAILD;
        try{
            attendanceService.score(attendanceId,studentId,scoreItem,score);
            respObj = RespObj.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
        }
        return respObj;
    }


    /**
     * 巡视打分任课老师评分
     * */
    @RequestMapping("/teacherScore")
    @ResponseBody
    public RespObj teacherScore(String attendanceId,int score){
        RespObj respObj = RespObj.FAILD;
        try{
            attendanceService.teacherScore(attendanceId,score);
            respObj = RespObj.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            respObj.setMessage("打分失败");
        }
        return respObj;
    }

    /**
     * 巡视打分班级评分
     * */
    @RequestMapping("/courseScore")
    @ResponseBody
    public RespObj classScore(String attendanceId,int score){
        RespObj respObj = RespObj.FAILD;
        try{
            attendanceService.classScore(attendanceId,score);
            respObj = RespObj.SUCCESS;
        }catch (Exception e){
            e.printStackTrace();
            respObj.setMessage("打分失败");
        }
        return respObj;
    }

}
