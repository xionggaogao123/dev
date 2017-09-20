package com.fulaan.experience.controller;

import com.fulaan.base.BaseController;
import com.fulaan.experience.dto.ExperienceLogDTO;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.log.LogTask;
import com.fulaan.log.dto.LogDTO;
import com.fulaan.user.service.UserService;
import com.pojo.log.LogType;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Api(value="")
@Controller
@RequestMapping("/experience")
public class ExperienceController extends BaseController {

    private ExperienceService experienceService = new ExperienceService();

    @Autowired
    private UserService userService;

    //  学生积分统计页面
    @ApiOperation(value = "学生积分统计页面", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/studentScoreList")
    public String studentExpList(String studentId, Map<String, Object> model) {
        //判断studentId是否是null或""
        if (studentId == null || "".equals(studentId)) {
            studentId = getUserId().toString();
        }
        //获取用户信息
        UserDetailInfoDTO userInfo = userService.getUserInfoById(studentId);
        model.put("studentId", studentId);
        //判断用户角色是否是学生或家长
        if (UserRole.isStudentOrParent(userInfo.getRole())) {
            return "experienceHistory/studentExperienceHistory";
        } else {
            return "experienceHistory/teacherExperienceHistory";
        }

    }
    @ApiOperation(value = "experienceRule", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/experienceRule")
    public String experienceRule(Map<String, Object> model) {
        int role = getSessionValue().getUserRole();
        model.put("role", role);
        return "experienceHistory/experienceRule";

    }

    /**
     * 学生经验值Log的取得
     *
     * @return
     */
    @ApiOperation(value = "学生经验值Log的取得", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Page.class)})
    @RequestMapping("/getStuScoreInfo")//原名“getStuScoreInfo”
    public
    @ResponseBody
    Page<ExperienceLogDTO> getUserExpInfo(@RequestParam("userid") String usreid, @RequestBody Pageable pageable) {
        //分页查询用户积分日志
        return experienceService.selUserExperienceInfoList(usreid, pageable);
    }

    /**
     * 学生经验值记录
     *
     * @return
     */
    @ApiOperation(value = "学生经验值记录", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/studentScoreLog")
    public
    @ResponseBody
    Map<String, Object> studentScoreLog(@RequestParam("relateId") String relateId, @RequestParam("scoretype") String scoretype, String courseId) {
        Map<String, Object> result = new HashMap<String, Object>();
        int userRole = getSessionValue().getUserRole();
        //判断用户角色是否是学生
        if (UserRole.isStudent(userRole)) {
            int scoretypeInt = Integer.parseInt(scoretype);
            ExpLogType type = null;
            boolean expResult = false;
            //判断scoretypeInt，当scoretypeInt等于0时，积分类型是云课程，当scoretypeInt等于1时，积分类型是班级课程
            if (scoretypeInt == 0) {
                type = ExpLogType.CLOUD;
                LogDTO log = new LogDTO();
                log.setActionType(LogType.VIEW_CLOUD_VIDEO.getCode());
                log.setUserId(getUserId().toString());
                LogTask.put(log);
                expResult = experienceService.updateNoRepeatAndDaily(getUserId().toString(), type, relateId);
            } else if (scoretypeInt == 1) {
                type = ExpLogType.LESSON;
                expResult = experienceService.updateNoRepeat(getUserId().toString(), type, relateId);
            }
            //修改用户积分、积分日志表中记录，并判断操作是否成功
            if (expResult) {
                result.put("resultcode", 0);
                result.put("desc", type.getDesc());
                result.put("score", type.getExp());
            } else {
                result.put("resultcode", 1);
            }
        } else {
            result.put("resultcode", 1);
        }
        return result;
    }
}
