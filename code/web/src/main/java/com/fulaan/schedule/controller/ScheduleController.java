package com.fulaan.schedule.controller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.docflow.controller.DocFlowController;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.schedule.dto.GradeClassDTO;
import com.fulaan.schedule.service.ScheduleService;
import com.fulaan.zouban.dto.CourseConfDTO;
import com.fulaan.zouban.dto.TermDTO;
import com.fulaan.zouban.service.CourseService;
import com.fulaan.zouban.service.TermService;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入课表
 * Created by qiangm on 2016/2/29.
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController extends BaseController{
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private TermService termService;
    @Autowired
    private CourseService courseService;



    @UserRoles(value = {UserRole.ADMIN,UserRole.HEADMASTER,UserRole.K6KT_HELPER,UserRole.SYSMANAGE})
    @RequestMapping("/viewSchedule")
    public String viewSchedule(Model model)
    {
        Map<String,Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId());
        model.addAttribute("term",map.get("term"));
        return "schedule/viewSchedule";
    }
    @UserRoles(value = {UserRole.ADMIN,UserRole.HEADMASTER,UserRole.K6KT_HELPER,UserRole.SYSMANAGE})
    @RequestMapping("/importSchedule")
    public String importSchedule(Model model)
    {
        Map<String,Object> map = termService.getCurrentTerm(getSessionValue().getSchoolId());
        model.addAttribute("term",map.get("term"));
        return "schedule/importSchedule";
    }
    //下载模板
    @RequestMapping("/exportTemplate")
    @ResponseBody
    public void exportTemplate(@RequestParam String term,HttpServletResponse response)
    {
        try {
            term=java.net.URLDecoder.decode(term, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        scheduleService.exportTemplate(term,getSessionValue().getSchoolId(),response);
    }
    /**
     * 导入学生课表
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/import")
    @ResponseBody
    public Map<String,String> importData(@RequestParam String term,@RequestParam("file") MultipartFile file) throws Exception {
        //获取班级ID为了校验上传文件是否合适
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls")&&!fileName.endsWith(".xlsx")) {
            Map<String,String> map=new HashMap<String, String>();
            map.put("result","fail");
            map.put("reason","导入文件格式不对，请导入excel文件");
            map.put("line","0");
            return map;
        }
        else if(!fileName.contains(term))
        {
            Map<String,String> map=new HashMap<String, String>();
            map.put("result","fail");
            map.put("reason","导入文件名中的学期和目标学期不一致");
            map.put("line","0");
            return map;
        }
        //Map<String,Object> mapObj=termService.getCurrentTerm(getSessionValue().getSchoolId());
        //String year=mapObj.get("year").toString();
        return scheduleService.importExcel(file.getInputStream(), getSessionValue().getSchoolId(), term);
    }

    /**
     * 获取年级班级列表
     * @return
     */
    @RequestMapping("/getGradeClass")
    @ResponseBody
    public List<GradeClassDTO> getGradeClass()
    {
        return scheduleService.getAllClasses(getSessionValue().getSchoolId());
    }

    /**
     * 获取班级课表
     * @param classId
     */
    @RequestMapping("/getClassSchedule")
    @ResponseBody
    public Map<String,Object> getClassSchedule(@RequestParam String term,@RequestParam String classId,@RequestParam int week)
    {
        return scheduleService.getClassSchedule(getSessionValue().getSchoolId(),term, classId,week);
    }

    /**
     * 发布课表
     * @param term
     * @return
     */
    @RequestMapping("/publishSchedule")
    @ResponseBody
    public Map<String,String> publishSchedule(@RequestParam String term)
    {
        return scheduleService.publishSchedule(term,new ObjectId(getSessionValue().getSchoolId()));
    }

    /**
     * 获取每学期的教学周
     * @param term
     * @return
     */
    @RequestMapping("/findWeeekList")
    @ResponseBody
    public Map<String,Integer> findWeekList(@RequestParam String term)
    {
        Map<String,Integer> map=new HashMap<String, Integer>();
        String year=term.substring(0,11);
        TermDTO termDTO=termService.findTermDTO(year, getSchoolId());
        String termIndex=term.substring(11,15);
        if(termIndex.equals("第二学期"))
        {
            map.put("all", termDTO.getSweek());
        }
        else
        {
            map.put("all",termDTO.getFweek());
        }
        return map;
    }


    /**
     * 获取教学周
     *
     * @param year
     * @return
     */
    @RequestMapping(value = "/termConfig", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTermConfig(String year) {
        TermDTO termDTO = termService.findTermDTO(year, getSchoolId());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("termConfig", termDTO);
        return result;
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
     * 添加课表配置
     * @param courseConfDTO
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/addCourseConf")
    @ResponseBody
    public RespObj addCourseConf(@RequestBody CourseConfDTO courseConfDTO) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            courseService.addCourseConf(courseConfDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }

    /**
     * 添加课表配置
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/findCourseConf")
    @ResponseBody
    public RespObj findCourseConf(String term) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            CourseConfDTO courseConfDTO = courseService.findCourseConfBySchool(getSchoolId().toString(), term);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(courseConfDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }

        return respObj;
    }


}
