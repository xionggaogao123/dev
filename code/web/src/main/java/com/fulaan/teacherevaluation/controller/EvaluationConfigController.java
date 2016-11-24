package com.fulaan.teacherevaluation.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.teacherevaluation.service.EvaluationConfigService;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.IdValuePairDTO1;
import com.pojo.app.NameValuePairDTO;
import com.pojo.teacherevaluation.ElementDTO;
import com.pojo.teacherevaluation.MemberGroupDTO;
import com.pojo.teacherevaluation.ProportionDTO;
import com.pojo.teacherevaluation.SettingDTO;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/4/19.
 */
@Controller
@RequestMapping("/teacherevaluation/{evaluationId}/config")
public class EvaluationConfigController extends BaseController {
    @Autowired
    private EvaluationConfigService configService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@PathVariable String evaluationId, Model model){
        model.addAttribute("evaluationId", evaluationId);
        return "/teacherevaluation/config";
    }
//========================================================人员分组===================================================
    /**
     * 导出全校老师模板
     * @param response
     */
    @RequestMapping("/exportTeacher")
    public void exportExcel(HttpServletResponse response){
        configService.exportExcel(new ObjectId(getSessionValue().getSchoolId()), response);
    }

    /**
     * 导入分组情况
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/importMemberGroup")
    @ResponseBody
    public RespObj importExcel(MultipartFile file,@PathVariable @ObjectIdType ObjectId evaluationId) throws Exception{
        configService.importExcel(file.getInputStream(), getSchoolId(), evaluationId);
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        return respObj;
    }

    /**
     * 获取人员分组情况
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getMemberGroup(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        MemberGroupDTO memberGroupDTO = configService.getMemberGroup(evaluationId, getSchoolId());
        respObj.setMessage(memberGroupDTO);
        return respObj;
    }

    /**
     * 新增一个老师分组
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/groups/teacherGroups", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addTeacherGroup(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        ObjectId groupId = configService.addTeacherGroup(evaluationId);
        respObj.setMessage(groupId.toString());
        return respObj;
    }

    /**
     * 删除一个老师分组
     * @param evaluationId
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/groups/teacherGroups/{groupId}/deletion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteTeacherGroup(@PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId groupId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        configService.deleteTeacherGroup(evaluationId, groupId);
        return respObj;
    }

    /**
     * 修改老师分组信息
     * @param evaluationId
     * @param groupId
     * @param name
     * @param num
     * @return
     */
    @RequestMapping(value = "/groups/teacherGroups/{groupId}", method = RequestMethod.POST)
    @ResponseBody
    public RespObj editTeacherGroup(@PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId groupId, String name, int num, int lnum){
        configService.updateTeacherGroup(evaluationId, groupId, name, num, lnum);
        return RespObj.SUCCESS;
    }

    /**
     * 增加老师
     * @param teacherIds
     * @param type  1:领导小组领导  2：领导小组其他成员  3：老师小组
     * @param evaluationId
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/{type}/members", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addTeacher(String teacherIds, @PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId,
                           @RequestParam(required = false) @ObjectIdType ObjectId groupId) throws Exception{
        List<ObjectId> teachers = MongoUtils.convert(teacherIds);
        configService.addTeacher(evaluationId, type, teachers, groupId);
        return getTeachers(type, evaluationId, groupId);
    }

    /**
     * 删除老师
     * @param teacherId
     * @param type  1:领导小组领导  2：领导小组其他成员   3：老师小组
     * @param evaluationId
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/{type}/members/{teacherId}/deletion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteTeacher(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId,
                              @RequestParam(required = false) @ObjectIdType ObjectId groupId){
        configService.deleteTeacher(evaluationId, type, teacherId, groupId);
        return getTeachers(type, evaluationId, groupId);
    }

    /**
     * 删除老师
     * @param teacherId
     * @param type  3：老师小组
     * @param evaluationId
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/{type}/groups/{groupId}/members/{teacherId}/deletion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteGroupTeacher(@PathVariable @ObjectIdType ObjectId teacherId, @PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId,
                                 @PathVariable @ObjectIdType ObjectId groupId){
        configService.deleteTeacher(evaluationId, type, teacherId, groupId);
        return getTeachers(type, evaluationId, groupId);
    }

    /**
     * 得到已选老师、未选老师列表
     * @param type
     * @param evaluationId
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/{type}/members", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTeachers(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId,
                                 @RequestParam(required = false) @ObjectIdType ObjectId groupId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> teacherMap = configService.getTeachers(getSchoolId(), evaluationId, type, groupId);
        respObj.setMessage(teacherMap);
        return respObj;
    }

    //========================================================评分比重===================================================
    @RequestMapping(value = "/proportion", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getProportion(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        ProportionDTO proportionDTO = configService.getProportion(evaluationId);
        respObj.setMessage(proportionDTO);
        return respObj;
    }

    @RequestMapping(value = "/proportion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addProportion(@RequestBody ProportionDTO proportionDTO, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        ObjectId id = null;
        try {
            id = configService.addProportion(proportionDTO, evaluationId);
            respObj.setMessage(id.toString());
        } catch (Exception e) {
            respObj.setMessage(e.getMessage());
            respObj.setCode(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return respObj;
    }

    //========================================================考核要素===================================================

    /**
     * 考核元素或量化成绩列表
     * @param type 类型 1：考核要素  2：量化成绩
     * @param evaluationId
     * @return
     */
    @RequestMapping(value = "/{type}/elements", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getElement(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ElementDTO> elementDTOs = configService.getElements(evaluationId, type);
        respObj.setMessage(elementDTOs);
        return respObj;
    }

    /**
     * 增加考核元素或量化成绩
     * @param type
     * @param evaluationId
     * @param name
     * @param score
     * @return
     */
    @RequestMapping(value = "/{type}/elements", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addElement(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId, String name, double score){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ElementDTO> elementDTOs = configService.addElement(evaluationId, type, name, score);
        respObj.setMessage(elementDTOs);
        return respObj;
    }

    /**
     * 更新考核元素或量化成绩
     * @param type
     * @param evaluationId
     * @param name
     * @param score
     * @param elementId
     * @return
     */
    @RequestMapping(value = "/{type}/elements/{elementId}", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateElement(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId, String name, double score, @PathVariable @ObjectIdType ObjectId elementId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ElementDTO> elementDTOs = configService.updateElement(evaluationId, type, name, score, elementId);
        respObj.setMessage(elementDTOs);
        return respObj;
    }

    /**
     * 删除考核元素或量化成绩
     * @param type
     * @param evaluationId
     * @param elementId
     * @return
     */
    @RequestMapping(value = "/{type}/elements/{elementId}/deletion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteElement(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId elementId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ElementDTO> elementDTOs = configService.deleteElement(evaluationId, type, elementId);
        respObj.setMessage(elementDTOs);
        return respObj;
    }

    /**
     * 获取考核内容
     * @param type
     * @param evaluationId
     * @param elementId
     * @return
     */
    @RequestMapping(value = "/{type}/elements/{elementId}/contents", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getContents(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId elementId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<IdValuePairDTO1> contents = configService.getContents(elementId);
        respObj.setMessage(contents);
        return respObj;
    }

    /**
     * 增加考核内容
     * @param type
     * @param evaluationId
     * @param elementId
     * @param value
     * @return
     */
    @RequestMapping(value = "/{type}/elements/{elementId}/contents", method = RequestMethod.POST)
    @ResponseBody
    public RespObj addContent(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId elementId, String value){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<IdValuePairDTO1> contents = configService.addContent(elementId, value);
        respObj.setMessage(contents);
        return respObj;
    }

    /**
     * 更新考核内容
     * @param type
     * @param evaluationId
     * @param elementId
     * @param value
     * @param contentId
     * @return
     */
    @RequestMapping(value = "/{type}/elements/{elementId}/contents/{contentId}", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateContent(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId elementId, String value,
                                 @PathVariable @ObjectIdType ObjectId contentId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<IdValuePairDTO1> contents = configService.updateContent(elementId, contentId, value);
        respObj.setMessage(contents);
        return respObj;
    }

    /**
     * 删除考核内容
     * @param type
     * @param evaluationId
     * @param elementId
     * @param contentId
     * @return
     */
    @RequestMapping(value = "/{type}/elements/{elementId}/contents/{contentId}/deletion", method = RequestMethod.POST)
    @ResponseBody
    public RespObj deleteContent(@PathVariable int type, @PathVariable @ObjectIdType ObjectId evaluationId, @PathVariable @ObjectIdType ObjectId elementId,
                                 @PathVariable @ObjectIdType ObjectId contentId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<IdValuePairDTO1> contents = configService.deleteContent(elementId, contentId);
        respObj.setMessage(contents);
        return respObj;
    }

    //=======================================教师评价设置  包含  等第设置 评分时间 评比规则=================================

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getSetting(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        SettingDTO settingDTO = configService.getSetting(evaluationId);
        respObj.setMessage(settingDTO);
        return respObj;
    }

    @RequestMapping(value = "/setting/grade", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateGradeSetting(@RequestBody List<SettingDTO.GradeSettingDTO> gradeSettingDTOs,@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        configService.updateGrade(evaluationId, gradeSettingDTOs);
        return respObj;
    }

    @RequestMapping(value = "/setting/time", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateTimeSetting(@PathVariable @ObjectIdType ObjectId evaluationId,
                                     String evaluationTimeBegin, String evaluationTimeEnd){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        configService.updateTime(evaluationId, evaluationTimeBegin, evaluationTimeEnd);
        return respObj;
    }

    @RequestMapping(value = "/setting/time", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getTimeSetting(@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        SettingDTO settingDTO = configService.getSetting(evaluationId);
        settingDTO.setModeGrades(new ArrayList<NameValuePairDTO>());
        settingDTO.setRule("");
        respObj.setMessage(settingDTO);
        return respObj;
    }

    @RequestMapping(value = "/setting/rule", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateRuleSetting(String rule,@PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        configService.updateRule(evaluationId, rule);
        return respObj;
    }

    @RequestMapping(value = "/setting/mode", method = RequestMethod.POST)
    @ResponseBody
    public RespObj updateModeSetting(String grades, int mode, @PathVariable @ObjectIdType ObjectId evaluationId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        configService.updateMode(evaluationId, mode, grades);
        return respObj;
    }


}
