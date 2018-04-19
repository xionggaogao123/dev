package com.fulaan.indexpage.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.indexpage.service.WebHomePageService;
import com.fulaan.reportCard.service.ReportCardService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by scott on 2017/11/18.
 */
@Controller
@RequestMapping("/web/webHome")
public class WebHomePageController extends BaseController{

    @Autowired
    private WebHomePageService webHomePageService;
    
    @Autowired
    private ReportCardService reportCardService;

    /**
     *
     * @param page
     * @param pageSize
     * @param mode
     * @param type
     * @param subjectId
     * @param status
     * @param startTime
     * @param endTime
     * @param communityId
     * @return
     */
    @RequestMapping("/getMySendHomeEntries")
    @ResponseBody
    public RespObj getMySendHomeEntries(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize,
            @RequestParam(required = false,defaultValue = "0")int mode,
            @RequestParam(required = false,defaultValue = "-1")int type,
            @RequestParam(required = false,defaultValue = "")String subjectId,
            @RequestParam(required = false,defaultValue = "-1")int status,
            @RequestParam(required = false,defaultValue = "")String startTime,
            @RequestParam(required = false,defaultValue = "")String endTime,
            @RequestParam(required = false,defaultValue = "")String communityId

    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result=webHomePageService.getMySendHomePageEntries(getUserId(),type, subjectId, mode,
                    status, startTime, endTime, communityId, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param page
     * @param pageSize
     * @param mode
     * @param type
     * @param subjectId
     * @param status
     * @param startTime
     * @param endTime
     * @param communityId
     * @return
     */
    @RequestMapping("/getMyReceiveHomeEntries")
    @ResponseBody
    public RespObj getMyReceiveHomeEntries(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize,
            @RequestParam(required = false,defaultValue = "0")int mode,
            @RequestParam(required = false,defaultValue = "-1")int type,
            @RequestParam(required = false,defaultValue = "")String subjectId,
            @RequestParam(required = false,defaultValue = "-1")int status,
            @RequestParam(required = false,defaultValue = "")String startTime,
            @RequestParam(required = false,defaultValue = "")String endTime,
            @RequestParam(required = false,defaultValue = "")String communityId

    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result=webHomePageService.getMyReceivedEntries(getUserId(),type, subjectId, mode,
                    status, startTime, endTime, communityId, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     *
     * @param page
     * @param pageSize
     * @param type
     * @param subjectId
     * @param communityId
     * @return
     */
    @RequestMapping("/getGatherEntries")
    @ResponseBody
    public RespObj getGatherEntries(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int pageSize,
            @RequestParam(required = false,defaultValue = "-1")int type,
            @RequestParam(required = false,defaultValue = "")String subjectId,
            @RequestParam(required = false,defaultValue = "")String communityId
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> result=webHomePageService.getGatherEntries(getUserId(),type,subjectId,communityId, page, pageSize);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }
    
    @ApiOperation(value = "获取成绩单的签字和未签字列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "保存考试信息已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/searchReportCardSignList")
    @ResponseBody
    public RespObj searchReportCardSignList(@ObjectIdType ObjectId groupExamDetailId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String,Object> result=reportCardService.searchReportCardSignList(groupExamDetailId);
        respObj.setMessage(result);
        return respObj;
    }
    
    /**
    *
    * @param groupExamDetailId
    * @return
    */
   @ApiOperation(value = "删除成绩单", httpMethod = "GET", produces = "application/json")
   @ApiResponses( value = {@ApiResponse(code = 200, message = "删除成绩单已完成",response = String.class),
           @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
           @ApiResponse(code = 500, message = "服务器不能完成请求")})
   @RequestMapping("/removeGroupExam")
   @ResponseBody
   public RespObj removeGroupExam(@ObjectIdType ObjectId groupExamDetailId){
       RespObj respObj=new RespObj(Constant.FAILD_CODE);
       try{
           reportCardService.removeGroupExamDetailEntry(groupExamDetailId,getUserId());
           respObj.setCode(Constant.SUCCESS_CODE);
           respObj.setMessage("删除成绩单成功！");
       }catch (Exception e){
           respObj.setErrorMessage(e.getMessage());
       }
       return respObj;
   }



    /**
     * 数据映射（把现有的数据映射成新的数据）
     * @return
     */
    @RequestMapping("/dataMapping")
    @ResponseBody
    public RespObj  dataMapping(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            webHomePageService.dataMapping();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("数据映射成功!");
        }catch (Exception e){
            respObj.setMessage(e.getMessage());
        }
        return respObj;
    }

    @RequestMapping("/generateUserCode")
    @ResponseBody
    public RespObj generateUserCode(){
        RespObj respObj=new RespObj(Constant.SUCCESS_CODE,"生成序列号成功");
        webHomePageService.generateUserCode();
        return respObj;
    }



}
