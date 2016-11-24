package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.TermDTO;
import com.fulaan.zouban.service.TermService;
import com.fulaan.zouban.service.TimeTableService;
import com.fulaan.zouban.service.ZoubanModeService;
import com.fulaan.zouban.service.ZoubanStateService;
import com.pojo.app.SessionValue;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by wangkaidong on 2016/7/5.
 *
 * 走班基础设置Controller
 */

@Controller
@RequestMapping("/zouban/baseConfig")
public class StepZeroController extends BaseController {
    @Autowired
    private TermService termService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private ClassService classService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ZoubanModeService zoubanModeService;




    private void checkLogin(HttpServletRequest request, HttpServletResponse response) {
        SessionValue sessionValue = (SessionValue)request.getAttribute(BaseController.SESSION_VALUE);
        if (sessionValue == null || sessionValue.isEmpty() || sessionValue.getId() == null) {
            try {
                response.sendRedirect("/user/homepage.do");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 首页
     *
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping
    public String index(HttpServletRequest request, HttpServletResponse response) {
        /*checkLogin(request, response);*/
        return "zoubannew/admin/index";
    }


    /**
     * 移动端判断学校是否购买了走班功能
     *
     * @return
     */
    @RequestMapping("/appGetMode")
    @ResponseBody
    public int getSchoolMode() {
        return zoubanModeService.checkSchoolExist(getSchoolId().toString()) ? 1 : 0;
    }



    /**
     * 获取走班模式
     *
     * @param gradeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getMode")
    @ResponseBody
    public Map<String, Integer> getMode(String gradeId) {
        int mode = zoubanModeService.getGradeMode(getSchoolId().toString(), gradeId);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("mode", mode);
        return map;
    }



    /**
     * 获取走班进度
     *
     * @param term
     * @param gradeId
     * @return
     */
    @RequestMapping("/getState")
    @ResponseBody
    public Map<String, Integer> getState(String term, String gradeId) {
        if(gradeId.equals("") && UserRole.isStudent(getSessionValue().getUserRole())){
            gradeId =  classService.getClassEntryByStuId(getUserId(), Constant.FIELDS).getGradeId().toString();
        }
        int state = zoubanStateService.getZoubanState(term, getSchoolId().toString(), gradeId);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("state", state);
        return map;
    }

    /**
     * 修改教学周
     *
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/termConfig", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateTermConfig(@RequestBody TermDTO termDTO) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            termDTO.setSchoolId(getSchoolId().toString());
            termService.updateTerm(termDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 获取教学周
     *
     * @param term
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping(value = "/termConfig", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTermConfig(String term) {
        TermDTO termDTO = termService.findTermDTO(term, getSchoolId());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("termConfig", termDTO);
        return result;
    }



}
