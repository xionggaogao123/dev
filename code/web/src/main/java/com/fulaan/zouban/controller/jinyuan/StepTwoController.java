package com.fulaan.zouban.controller.jinyuan;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.zouban.dto.SchoolSubjectGroupDTO;
import com.fulaan.zouban.dto.XuanKeDTO;
import com.fulaan.zouban.dto.XuanKeSubjectDtailDTO;
import com.fulaan.zouban.service.SchoolSubjectGroupService;
import com.fulaan.zouban.service.StudentXuankeService;
import com.fulaan.zouban.service.XuanKeService;
import com.fulaan.zouban.service.ZoubanStateService;
import com.pojo.app.*;
import com.pojo.user.UserRole;
import com.pojo.zouban.XuankeConfEntry;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkaidong on 2016/7/6.
 * <p/>
 * 学生选课进度Controller
 */
@Controller
@RequestMapping("/zouban/studentXuanke")
public class StepTwoController extends BaseController {
    @Autowired
    private StudentXuankeService studentXuankeService;
    @Autowired
    private XuanKeService xuanKeService;
    @Autowired
    private SchoolSubjectGroupService subjectGroupService;


    /**
     * 选课进度页面
     *
     * @param term
     * @param gid
     * @param gnm
     * @param model
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping
    public String subjectConfig(String term, String gid, String gnm, Model model) {
        try {
            XuankeConfEntry xuankeConfEntry = xuanKeService.findXuanKeConfEntry(term, gid, getSchoolId().toString());
            model.addAttribute("term", URLDecoder.decode(term, "UTF-8"));
            model.addAttribute("gradeId", gid);
            model.addAttribute("gradeName", URLDecoder.decode(gnm, "UTF-8"));
            model.addAttribute("xuankeId", xuankeConfEntry.getID());
            model.addAttribute("isRelease", xuankeConfEntry.getIsRelease());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "zoubannew/admin/studentXuanke";
    }


    /**
     * 选课进度列表
     *
     * @param xuankeId
     * @param classId
     * @param choose
     * @param userName
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/studentXuanKeList")
    @ResponseBody
    public Map<String, Object> studentXuanKeList(String xuankeId, String classId, int choose, String userName) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<XuanKeSubjectDtailDTO> detaillist = xuanKeService.studentXuanKeList(xuankeId, classId, choose, userName, getSessionValue().getSchoolId());
        result.put("rows", detaillist);
        return result;
    }




    /**
     * 获取选课结果以及配置-----finish
     * 供管理员查看并选课
     *
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/getXuankeResultAdmin")
    @ResponseBody
    public Map<String, Object> getXuankeResultAdmin(String term, String gradeId, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();

        String schoolId = getSessionValue().getSchoolId();
        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf(term, gradeId, 1, schoolId);

        map.put("term", term);
        map.put("conf", xuanKeDTO);
        //已选课程
        Map<String, List<IdValuePair>> value = studentXuankeService.getChoosedCourse(new ObjectId(userId), new ObjectId(xuanKeDTO.getXuankeId()), schoolId);
        List<IdValuePairDTO1> adv = new ArrayList<IdValuePairDTO1>();
        for (IdValuePair idValuePair : value.get("adv")) {
            adv.add(new IdValuePairDTO1(idValuePair));
        }
        List<IdValuePairDTO1> sim = new ArrayList<IdValuePairDTO1>();
        for (IdValuePair idValuePair : value.get("sim")) {
            sim.add(new IdValuePairDTO1(idValuePair));
        }
        map.put("adv", adv);
        map.put("sim", sim);
        return map;
    }

    /**
     * 管理员替学生选课
     *
     * @param classId
     * @param advance
     * @param simple
     * @param stuId
     * @param stuName
     * @param xuankeId
     * @return
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/teacherXK")
    @ResponseBody
    public String teacherXuanke(String classId, String advance, String simple, String stuId, String stuName, String xuankeId) {
        return studentXuankeService.studentXuanke(new ObjectId(stuId), stuName, new ObjectId(classId), advance, simple, new ObjectId(xuankeId));
    }


    /**
     * 下载模板
     *
     * @param gradeId
     * @param response
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/exportExcel")
    @ResponseBody
    public void downloadExcel(String gradeId, HttpServletResponse response) {
        studentXuankeService.generalExcel(response, gradeId);
    }

    /**
     * 批量导入学生选课结果
     *
     * @param file
     * @param gradeId
     * @return
     * @throws Exception
     */
    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/import")
    @ResponseBody
    public Map<String, String> importData(@RequestParam("file") MultipartFile file, String gradeId, String term) throws Exception {
        //获取班级ID为了校验上传文件是否合适
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "fail");
            map.put("reason", "导入文件格式不对，请导入excel文件");
            map.put("line", "0");
            return map;
        }
        return studentXuankeService.importXuankexcel(file.getInputStream(), term, gradeId, getSessionValue().getSchoolId());
    }

    /**
     * 学科组合列表
     *
     * @param year
     * @param gradeId
     * @return
     */
    @RequestMapping("/subjectGroups")
    @ResponseBody
    public RespObj getSubjectGroups(String year, @RequestParam ObjectId gradeId) {
        RespObj respObj = new RespObj(RespObj.SUCCESS.code);
        try {
            SchoolSubjectGroupDTO schoolSubjectGroupDTO = subjectGroupService.getSchoolSubjectGroupDTO(getSchoolId(), year, gradeId);
            respObj.setMessage(schoolSubjectGroupDTO);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(RespObj.FAILD.code);
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 更新组合开放状态
     *
     * @param subjectGroupId
     * @param isPublic
     * @return
     */
    @RequestMapping("/updateSubjectGroups")
    @ResponseBody
    public RespObj updateSubjectGroups(@ObjectIdType ObjectId subjectGroupId, Boolean isPublic) {
        RespObj respObj = new RespObj(RespObj.SUCCESS.code);
        subjectGroupService.updatePublicState(subjectGroupId, isPublic);
        return respObj;
    }

    @UserRoles(value = {UserRole.ADMIN, UserRole.HEADMASTER})
    @RequestMapping("/updateXuanKeInfo")
    @ResponseBody
    public RespObj updateXuanKeInfo(@ObjectIdType ObjectId xuankeId, String info) {
        RespObj respObj = new RespObj(RespObj.SUCCESS.code);
        xuanKeService.updateXuanKeInfo(xuankeId, info);
        return respObj;
    }

    /**
     * 选课记录更新,参数设置
     *
     * @param
     * @return
     */
    @RequestMapping("/updateXuanKeConf")
    @ResponseBody
    public Map updateXuanKeConf(@RequestParam String term, @RequestParam String gradeId,
                                @RequestParam String startDate, @RequestParam String endDate) {
        HashMap map = new HashMap();
        try {
            ObjectId xuankeId = xuanKeService.updateXuanKeConf(term, gradeId, startDate, endDate, getSessionValue().getSchoolId());
            map.put("flg", true);
            map.put("xuankeid", xuankeId);
        } catch (Exception e) {
            map.put("flg", false);
        }
        return map;
    }

    /**
     * 查询选课记录
     * @param term
     * @param gradeId
     * @param type
     * @return
     */
    @RequestMapping("/findXuanKeConf")
    @ResponseBody
    public XuanKeDTO findXuanKeConf(String term, String gradeId, int type) {
        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf(term, gradeId, type, getSessionValue().getSchoolId());
        return xuanKeDTO;
    }

    /**
     * 发布、取消发布
     *
     * @param xuankeId
     * @return
     */
    @RequestMapping("/release")
    @ResponseBody
    public Map release(String xuankeId) {
        HashMap map = new HashMap();
        try {
            int isRelease = xuanKeService.release(xuankeId);
            map.put("flg", true);
            map.put("isRelease", isRelease);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("flg", false);
        }
        return map;
    }

    /**
     * 选课明细
     *
     * @param xuankeId
     * @param subjectId
     * @param classId
     * @return
     */
    @RequestMapping("/xuanKeSubjectDetail")
    @ResponseBody
    public Map xuanKeSubjectDetail(String xuankeId, String subjectId, String classId, int type) {
        return xuanKeService.xuanKeSubjectDetail(xuankeId, subjectId, classId, getSessionValue().getSchoolId(), type);
    }

    /**
     * 组合名称及人数
     *
     * @param term
     * @param gradeId
     * @param xuankeId
     * @return
     * @throws Exception
     */
    @RequestMapping("/subjectGroupStudentNumPair")
    @ResponseBody
    public RespObj getSubjectGroupStudentNumPair(String term, @ObjectIdType ObjectId gradeId, @ObjectIdType ObjectId xuankeId) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);
        try {
            List<IdNameValuePairDTO> nameValuePairDTOs = studentXuankeService.getSubjectGroupStudentNumPair(getSchoolId(), term, gradeId, xuankeId);
            respObj.setCode(RespObj.SUCCESS.code);
            respObj.setMessage(nameValuePairDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 每种组合学生列表
     *
     * @param subjectGroupId
     * @param xuankeId
     * @param gradeId
     * @param term
     * @return
     */
    @RequestMapping("/subjectGroupStudent")
    @ResponseBody
    public RespObj getSubjectGroupStudent(@ObjectIdType ObjectId subjectGroupId, @ObjectIdType ObjectId xuankeId, @ObjectIdType ObjectId gradeId, String term) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<NameValuePairDTO> nameValuePairDTOs = studentXuankeService.getStudentChoosesBySubjectGroup(subjectGroupId, xuankeId, getSchoolId(), gradeId, term);
        respObj.setMessage(nameValuePairDTOs);
        return respObj;
    }

    /**
     * 导出学生选课结果
     *
     * @param type     类型
     * @param response
     */
    @RequestMapping("/exportResult")
    public void exportExcel(int type, @ObjectIdType ObjectId xuankeId, @ObjectIdType ObjectId gradeId, String term, HttpServletResponse response) throws Exception {
        if (1 == type) {
            studentXuankeService.exportResultByClass(xuankeId, gradeId, term, getSchoolId(), response);
        } else if (2 == type) {
            studentXuankeService.exportResultByGroup(xuankeId, gradeId, term, getSchoolId(), response);
        }
    }


}
