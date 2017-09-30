package com.fulaan.reportCard.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.reportCard.dto.GroupExamDetailDTO;
import com.fulaan.reportCard.dto.RecordExamScoreDTO;
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


    @ApiOperation(value = "家长签字功能", httpMethod = "POST", produces = "application/json")
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

    @ApiOperation(value = "删除成绩单", httpMethod = "POST", produces = "application/json")
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


    @ApiOperation(value = "保存或编辑成绩列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存或编辑成绩列表已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/saveRecordExamScore")
    @ResponseBody
    public RespObj saveRecordExamScore(@RequestBody List<RecordExamScoreDTO> examScoreDTOs, int status){
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


    @RequestMapping("/getGroupExamDetailByType")
    @ResponseBody
    public RespObj getGroupExamDetailByType(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            GroupExamDetailDTO detailDTO=reportCardService.getGroupExamDetailByType(groupExamDetailId,getUserId());
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(detailDTO);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


}
