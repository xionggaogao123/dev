package com.fulaan.extendedcourse.controller;

import com.fulaan.base.BaseController;
import com.fulaan.dto.VideoDTO;
import com.fulaan.extendedcourse.dto.ExtendedCourseDTO;
import com.fulaan.extendedcourse.dto.ExtendedSchoolLabelDTO;
import com.fulaan.extendedcourse.service.ExtendedCourseService;
import com.fulaan.pojo.Attachement;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-12-07.
 */
@Api(value="拓展课")
@Controller
@RequestMapping("/web/extendedcourse")
public class WebExtendedCourseController extends BaseController {

    @Autowired
    private ExtendedCourseService extendedCourseService;

    private static final Logger logger = Logger.getLogger(ExtendedCourseController.class);
    /**
     * 学校开课类型设置
     */
    @ApiOperation(value = "设置学校开课类型", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveExtendedSchoolSetting")
    @ResponseBody
    public RespObj saveExtendedSchoolSetting(@RequestParam(value="schoolId") String schoolId,
                                             @RequestParam(value="courseType") int courseType){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.saveExtendedSchoolSettingEntry(new ObjectId(schoolId),courseType);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("修改成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("修改开课类型失败");
        }
        return respObj;
    }

    /**
     * 删除课程标签
     */
    @ApiOperation(value = "删除课程标签", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteExtendedSchoolLabel")
    @ResponseBody
    public RespObj deleteExtendedSchoolLabel(@RequestParam(value="id") String id){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            extendedCourseService.deleteExtendedSchoolLabel(new ObjectId(id));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("删除成功");
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("删除课程标签失败");
        }
        return respObj;
    }

    /**
     * 添加课程标签
     */
    @ApiOperation(value = "添加课程标签", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/addExtendedSchoolLabel")
    @ResponseBody
    public RespObj addExtendedSchoolLabel(@RequestParam(value="name") String name,
                                          @RequestParam(value="schoolId") String schoolId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.addExtendedSchoolLabel(new ObjectId(schoolId),getUserId(),name);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加课程标签失败");
        }
        return respObj;
    }

    /**
     * 查询课程标签列表
     */
    @ApiOperation(value = "添加课程标签", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/selectExtendedSchoolLabelList")
    @ResponseBody
    public RespObj selectExtendedSchoolLabelList(@RequestParam(value="schoolId") String schoolId){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            List<ExtendedSchoolLabelDTO> result = extendedCourseService.selectExtendedSchoolLabelList(new ObjectId(schoolId));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询课程标签失败");
        }
        return respObj;
    }


    /**
     * 新增拓展课
     */
    @ApiOperation(value = "新增拓展课", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveExtendedCourse")
    @ResponseBody
    public RespObj saveExtendedCourse(@RequestBody ExtendedCourseDTO dto){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String result = extendedCourseService.saveExtendedCourse(dto,getUserId(),new ObjectId(dto.getSchoolId()));
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            logger.error("error",e);
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("新增拓展课失败");
        }
        return respObj;
    }

    public static void main(String[] args){
        ExtendedCourseDTO dto = new ExtendedCourseDTO();
        dto.setCourseName("新建拓展课1");
        dto.setDescription("美术拓展课");
        dto.setTypeId(null);
        dto.setTypeName("美术");
        dto.setApplyStartTime("2018-12-12 15:14:00");
        dto.setApplyEndTime("2018-12-13 15:14:00");
        dto.setVoteStartTime("2018-12-14 15:14:00");
        dto.setVoteEndTime("2018-12-15 15:14:00");
        dto.setWeek(2);
        dto.setType(1);
        dto.setLessonType(8);
        dto.setTeacherName("James老师");
        dto.setTypeId("");
        List<String> gradeList = new ArrayList<String>();
        gradeList.add("2");
        gradeList.add("3");
        dto.setGradeList(gradeList);
        dto.setUserAllNumber(10);
        dto.setClassUserNumber(2);
        dto.setRoomName("美术大教室");
        List<VideoDTO> videoList=new ArrayList<VideoDTO>();           //提交
        List<Attachement> imageList=new ArrayList<Attachement>();     //提交
        List<Attachement> attachements=new ArrayList<Attachement>();  //提交
        List<Attachement> voiceList=new ArrayList<Attachement>();     //提交
        dto.setVideoList(videoList);
        dto.setImageList(imageList);
        dto.setAttachements(attachements);
        dto.setVoiceList(voiceList);
        ExtendedCourseService extendedCourseService =  new ExtendedCourseService();
        extendedCourseService.saveExtendedCourse(dto,new ObjectId("58f6bea2de04cb5a4bc72d38"),new ObjectId("5c0f2dc41f7e9d303818dda9"));
    }




}
