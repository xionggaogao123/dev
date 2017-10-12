package com.fulaan.reportCard.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.reportCard.dto.GroupExamDetailDTO;
import com.fulaan.reportCard.dto.GroupExamUserRecordDTO;
import com.fulaan.reportCard.service.ReportCardService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by scott on 2017/9/30.
 */
@Controller
@RequestMapping(value="/reportCard")
@Api(value = "成绩单的所有接口")
public class ReportCardController extends BaseController{

    @Autowired
    private ReportCardService reportCardService;


    @ApiOperation(value = "保存考试信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveGroupExamDetail")
    @ResponseBody
    public RespObj saveGroupExamDetail(@RequestBody GroupExamDetailDTO groupExamDetailDTO){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.saveGroupExamDetail(groupExamDetailDTO,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "获取老师发送的成绩单列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getMySendGroupExam")
    @ResponseBody
    public RespObj getMySendGroupExam(
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "-1")int examType,
            @RequestParam(required = false, defaultValue = "-1")int status,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamDetailDTO> groupExamDetailDTOs=reportCardService.getMySendGroupExamDetailDTOs(
                   subjectId,examType,status,getUserId(),page,pageSize
            );
            respObj.setMessage(groupExamDetailDTOs);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    @ApiOperation(value = "获取学生接收的成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getStudentReceiveExams")
    @ResponseBody
    public RespObj getStudentReceiveExams(
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "-1")int examType,
            @RequestParam(required = false, defaultValue = "-1")int status,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamDetailDTO> groupExamDetailDTOs=reportCardService.getReceiveExams(
                    subjectId,examType,status,getUserId(),page,pageSize
            );
            respObj.setMessage(groupExamDetailDTOs);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }




    @ApiOperation(value = "获取家长接收的成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getParentReceiveExams")
    @ResponseBody
    public RespObj getParentReceiveExams(
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "-1")int examType,
            @RequestParam(required = false, defaultValue = "-1")int status,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamDetailDTO> groupExamDetailDTOs=reportCardService.getParentReceivedGroupExamDetailDTOs(
                    subjectId,examType,status,getUserId(),page,pageSize
            );
            respObj.setMessage(groupExamDetailDTOs);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "家长签字功能", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/pushSign")
    @ResponseBody
    public RespObj pushSign(@ObjectIdType ObjectId groupExamDetailId,
                            @ObjectIdType ObjectId userId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.pushSign(groupExamDetailId,userId);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "删除成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除成绩单已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/removeGroupExam")
    @ResponseBody
    public RespObj removeGroupExam(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.removeGroupExamDetailEntry(groupExamDetailId);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "发送成绩单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除成绩单已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/sendGroupExam")
    @ResponseBody
    public RespObj sendGroupExam(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.sendGroupExam(groupExamDetailId);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "查询录入成绩的学生名单", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/searchRecordStudentScores")
    @ResponseBody
    public RespObj searchRecordStudentScores(@ObjectIdType ObjectId examGroupDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<GroupExamUserRecordDTO> examScoreDTOs=reportCardService.searchRecordStudentScores(examGroupDetailId);
            respObj.setMessage(examScoreDTOs);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @ApiOperation(value = "保存或编辑成绩列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveRecordExamScore")
    @ResponseBody
    public RespObj saveRecordExamScore(@RequestBody List<GroupExamUserRecordDTO> examScoreDTOs, int status){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            reportCardService.saveRecordExamScore(examScoreDTOs, status);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询详情信息
     * @param groupExamDetailId
     * @return
     */
    @ApiOperation(value = "获取该条成绩单的详情信息(针对老师)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getTeacherGroupExamDetail")
    @ResponseBody
    public RespObj getTeacherGroupExamDetail(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamDetailDTO detailDTO=reportCardService.
                    getTeacherGroupExamDetail(groupExamDetailId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(detailDTO);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 查询详情信息
     * @param singleId
     * @return
     */
    @ApiOperation(value = "获取该条成绩单的详情信息(针对家长或学生)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getGroupExamDetail")
    @ResponseBody
    public RespObj getGroupExamDetail(@ObjectIdType ObjectId singleId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamDetailDTO detailDTO=reportCardService.
                    getGroupExamDetail(singleId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(detailDTO);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
