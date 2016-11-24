package com.fulaan.leave.controller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.leave.dto.LeaveDTO;
import com.fulaan.leave.dto.ReplaceCourseDTO;
import com.fulaan.leave.dto.TeacherInfo;
import com.fulaan.leave.service.LeaveService;
import com.fulaan.zouban.dto.CourseConfDTO;
import com.fulaan.zouban.service.TermService;
import com.pojo.leave.ReplyEnum;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2016/3/1.
 */
@Controller
@RequestMapping("/leave")
public class LeaveController extends BaseController {
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private TermService termService;







    @UserRoles(noValue = {UserRole.PARENT, UserRole.STUDENT})
    @RequestMapping("/teacher")
    public String teacher() {
        return "/teacherLeave/teacher";
    }

    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER, UserRole.K6KT_HELPER, UserRole.SYSMANAGE})
    @RequestMapping("/manage")
    public String manage() {
        return "/teacherLeave/manage";
    }

    /**
     * 获取个人请假列表，分页形式
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getMyLeave")
    @ResponseBody
    public Map<String, Object> myLeave(int page, int pageSize) {
        int count = leaveService.countMyLeave(getUserId().toString());
        List<LeaveDTO> leaveDTOList = leaveService.findMyLeaveList(getUserId().toString(), page, pageSize);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", count);
        map.put("list", leaveDTOList);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    /**
     * @param teacherId
     * @param term
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getAllReplace")
    @ResponseBody
    public Map<String, Object> getAllReplace(@RequestParam String teacherId, @RequestParam String term,
                                             @RequestParam int page, @RequestParam int pageSize) {
        return leaveService.getReplaceList(teacherId, term, page, pageSize, getSessionValue().getSchoolId());
    }

    /**
     * 老师提交请假申请
     *
     * @param dateFrom
     * @param dateEnd
     * @param classCount
     * @param title
     * @param content
     * @return
     */
    @RequestMapping("/addLeave")
    @ResponseBody
    public Map<String, String> addLeave(@RequestParam String dateFrom, @RequestParam String dateEnd, @RequestParam int classCount,
                                        @RequestParam String title, @RequestParam String content) {
        Map<String, String> map = new HashMap<String, String>();
        ObjectId objId = leaveService.addTeacherLeave(getSessionValue().getSchoolId(), getUserId().toString(), title, content, dateFrom, dateEnd, classCount);
        map.put("result", Constant.SUCCESS_CODE);
        if (objId == null) {
            map.put("leaveId", "");
        } else {
            map.put("leaveId", objId.toString());
        }
        return map;
    }

    /**
     * 获取个人请假详情
     *
     * @param leaveId
     * @return
     */
    @RequestMapping("/getLeaveDetail")
    @ResponseBody
    public LeaveDTO getLeaveDTO(@RequestParam String leaveId) {
        return leaveService.findLeaveById(leaveId);
    }

    /**
     * 获取全校的请假
     *
     * @param teacherId
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getAllLeaves")
    @ResponseBody
    public Map<String, Object> getAllLeaves(String teacherId, int day, int type, int page, int pageSize) {
        String term = termService.getCurrentTerm(getSessionValue().getSchoolId()).get("term").toString();
        int count = leaveService.countAllTeacherLeaveCount(getSessionValue().getSchoolId(), teacherId, day, type, term);
        List<LeaveDTO> leaveDTOList = leaveService.findAllTeacherLeaveList(getSessionValue().getSchoolId(), teacherId, day, type, page, pageSize, term);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", count);
        map.put("list", leaveDTOList);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    /**
     * 管理员驳回请假申请
     *
     * @param leaveId
     * @return
     */
    @RequestMapping("/rejectLeave")
    @ResponseBody
    public Map<String, String> rejectLeave(@RequestParam String leaveId) {
        Map<String, String> map = new HashMap<String, String>();
        leaveService.rejectLeave(leaveId, ReplyEnum.REJECT.getIndex(), getUserId().toString());
        map.put("result", Constant.SUCCESS_CODE);
        return map;
    }

    /**
     * 获取当前学期开始周，结束周
     *
     * @return
     */
    @RequestMapping("/getCurrentTerm")
    @ResponseBody
    public Map<String, String> getCurrentTerm() {
        Map<String, Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId());
        Map<String, String> result = new HashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long today = new Date().getTime();
        long startTime = Long.parseLong(map.get("startDate").toString());
        long dt = today > startTime ? today : startTime;
        result.put("start", sdf.format(new Date(dt)));
        result.put("end", sdf.format(new Date(Long.parseLong(map.get("endDate").toString()))));
        return result;
    }

    /**
     * 根据日期计算该段时间内老师共有多少节课
     *
     * @param dateFrom
     * @param dateEnd
     * @return
     */
    @RequestMapping("/calLeaveInfo")
    @ResponseBody
    public Map<String, Object> calLeaveInfo(@RequestParam String dateFrom, @RequestParam String dateEnd) {
        return leaveService.calClass(getSessionValue().getSchoolId(), dateFrom, dateEnd, getUserId().toString());
    }

    /**
     * 获取课表结构配置
     *
     * @return
     */
    @RequestMapping("/findTableConf")
    @ResponseBody
    public CourseConfDTO findTableConf() {
        return leaveService.getCourseConf(getSessionValue().getSchoolId());
    }

    @RequestMapping("/getLeaveCourseDetail")
    @ResponseBody
    public Map<String, Object> getLeaveCourseDetail(@RequestParam String leaveId) {
        return leaveService.getLeaveCourseDetail(leaveId);
    }

    /**
     * 获取可以代课的老师列表
     *
     * @param week
     * @param x
     * @param y
     * @param courseName
     * @return
     */
    @RequestMapping("/getAvailableTeacherList")
    @ResponseBody
    public List<TeacherInfo> getAvailableTeacherList(@RequestParam int week, @RequestParam int x, @RequestParam int y,
                                                     @RequestParam String courseName) {
        return leaveService.getAvailableTeacherList(getSessionValue().getSchoolId(), week, x, y, courseName);
    }

    /**
     * 确认代课
     *
     * @param teacherId
     * @param oldTeacherId
     * @param courseName
     * @param week
     * @param x
     * @param y
     * @return
     */
    @RequestMapping("/replaceCourse")
    @ResponseBody
    public Map<String, String> replaceCourse(@RequestParam String teacherId, @RequestParam String oldTeacherId, @RequestParam String courseId, @RequestParam String courseName,
                                             @RequestParam int week, @RequestParam int x, @RequestParam int y, @RequestParam String leaveId) {
        Map<String, String> map = new HashMap<String, String>();
        String replaceId = leaveService.replaceCourse(getSessionValue().getSchoolId(), teacherId, oldTeacherId, courseId, courseName, week, x, y, getUserId().toString(), leaveId);
        map.put("repId", replaceId);
        return map;
    }

    /**
     * 同意请假
     *
     * @param replaceIds
     * @param leaveId
     * @return
     */
    @RequestMapping("/agreeLeave")
    @ResponseBody
    public Map<String, String> agreeLeave(@RequestParam String replaceIds, @RequestParam String leaveId) {
        Map<String, String> map = new HashMap<String, String>();
        leaveService.agreeLeave(replaceIds, leaveId, getUserId().toString());
        map.put("result", Constant.SUCCESS_CODE);
        return map;
    }

    /**
     * 删除代课记录
     *
     * @param replaceIds
     * @return
     */
    @RequestMapping("/removeReplace")
    @ResponseBody
    public Map<String, String> removeReplace(@RequestParam String replaceIds) {
        Map<String, String> map = new HashMap<String, String>();
        leaveService.removeReplace(replaceIds);
        map.put("result", Constant.SUCCESS_CODE);
        return map;
    }

    /**
     * 分页获取个人代课
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/findMyReplace")
    @ResponseBody
    public Map<String, Object> findMyReplace(@RequestParam int page, @RequestParam int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        String term = termService.getCurrentTerm(getSessionValue().getSchoolId()).get("term").toString();
        List<ReplaceCourseDTO> list = leaveService.findMyReplace(getSessionValue().getSchoolId(), getUserId().toString(), page, pageSize, term);
        int count = leaveService.countMyLeave(getUserId().toString(), term);
        map.put("total", count);
        map.put("list", list);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    /**
     * 删除我的请假
     *
     * @param leaveId
     * @return
     */
    @RequestMapping("/removeMyLeave")
    @ResponseBody
    public Map<String, Object> removeMyLeave(@RequestParam String leaveId) {
        return leaveService.removeMyLeave(leaveId);
    }
}
