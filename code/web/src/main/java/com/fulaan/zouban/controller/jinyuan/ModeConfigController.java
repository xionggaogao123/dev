package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.base.controller.BaseController;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.ZoubanModeDTO;
import com.fulaan.zouban.service.ZoubanModeService;
import com.pojo.app.SessionValue;
import com.pojo.school.SchoolDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/7/14.
 * <p/>
 * 学校走班模式配置Controller
 */
@Controller
@RequestMapping("/zouban/modeConf")
public class ModeConfigController extends BaseController {
    @Autowired
    private ZoubanModeService zoubanModeService;
    @Autowired
    private SchoolService schoolService;


    private void checkLogin(HttpServletRequest request, HttpServletResponse response) {
        SessionValue sessionValue = (SessionValue)request.getAttribute(BaseController.SESSION_VALUE);
        boolean login = true;
        if (sessionValue == null || sessionValue.isEmpty() || sessionValue.getId() == null) {
            login = false;
        } else {
            if (!(UserRole.isManager(sessionValue.getUserRole()) || UserRole.isHeadmaster(sessionValue.getUserRole()))) {
                login = false;
            }
        }
        try {
            if (!login) {
                response.sendRedirect("/user/homepage.do");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 模式配置页
     *
     * @return
     */
    @RequestMapping
    public String modeConfig(HttpServletRequest request, HttpServletResponse response) {
        checkLogin(request , response);
        return "zoubannew/admin/modeConfig";
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/getZoubanModeList")
    @ResponseBody
    public Map<String, Object> getZoubanModeList(int page, int pageSize) {
        List<ZoubanModeDTO> dtoList = zoubanModeService.findZoubanModeList(page, pageSize);
        int count = zoubanModeService.count();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("modeList", dtoList);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", count);
        return result;
    }

    /**
     * 获取学校名称
     *
     * @param schoolId
     * @return
     */
    @RequestMapping("/getSchoolName")
    @ResponseBody
    public Map<String, Object> getSchoolName(String schoolId) {
        SchoolDTO schoolDTO = schoolService.findSchoolById(schoolId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("schoolName", schoolDTO.getSchoolName());
        return result;
    }

    /**
     * 添加学校
     *
     * @param zoubanModeDTO
     * @return
     */
    @RequestMapping("/addSchool")
    @ResponseBody
    public RespObj addSchool(@RequestBody ZoubanModeDTO zoubanModeDTO) {
        RespObj respObj = RespObj.FAILD;
        try {
            if (zoubanModeService.checkSchoolExist(zoubanModeDTO.getSchoolId())) {
                respObj.setMessage("学校已存在，不能重复添加！");
            } else {
                zoubanModeService.add(zoubanModeDTO);
                respObj = RespObj.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 根据id获取学校走班模式
     *
     * @param id
     * @return
     */
    @RequestMapping("/getSchoolMode")
    @ResponseBody
    public Map<String, Object> getSchoolMode(String id) {
        ZoubanModeDTO dto = zoubanModeService.findZoubanMode(id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("schoolMode", dto);
        return result;
    }

    /**
     * 根据学校名模糊查询
     *
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> search(String key, int page, int pageSize) {
        List<ZoubanModeDTO> dtoList = zoubanModeService.findZoubanModeList(key, page, pageSize);
        int count = zoubanModeService.count();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("modeList", dtoList);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", count);
        return result;
    }

    /**
     * 更新学校走班模式
     *
     * @param zoubanModeDTO
     * @return
     */
    @RequestMapping("/updateSchool")
    @ResponseBody
    public RespObj updateSchool(@RequestBody ZoubanModeDTO zoubanModeDTO) {
        RespObj respObj = RespObj.FAILD;
        try {
            zoubanModeService.updateSchool(zoubanModeDTO);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     * 删除学校
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteSchool/{id}")
    @ResponseBody
    public RespObj deleteSchool(@PathVariable String id) {
        RespObj respObj = RespObj.FAILD;
        try {
            zoubanModeService.delete(id);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respObj;
    }


    /**
     * 更新年级走班模式
     *
     * @param id
     * @param gradeId
     * @param mode
     * @return
     */
    @RequestMapping("/updateGrade")
    @ResponseBody
    public RespObj updateGrade(String id, String gradeId, int mode) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            zoubanModeService.updateGrade(id, gradeId, mode);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e);
        }
        return respObj;
    }


}
