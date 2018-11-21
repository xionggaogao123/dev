package com.fulaan.lancustom.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.lancustom.dto.FeedbackManageDto;
import com.fulaan.lancustom.service.FeedbackManageService;
import com.fulaan.operation.dto.AppOperationDTO;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/11/21 10:53
 * @Description: 运营管理 留言反馈 后台
 */
@Api(value="留言反馈")
@Controller
@RequestMapping("/web/feedbackmanage")
public class FeedbackManageController extends BaseController {

    @Autowired
    private FeedbackManageService feedbackManageService;

    /**
     * 获取待办留言反馈
     * @param inputParam
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取待办留言反馈", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "获取待办留言反馈",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getFeedbackMsgByReadyRead")
    @ResponseBody
    public RespObj getFeedbackMsgByReadyRead(String inputParam,
                                  @RequestParam(required = false, defaultValue = "1")int page,
                                  @RequestParam(required = false, defaultValue = "10")int pageSize){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map<String,Object> map = feedbackManageService.getFeedbackMsgByReadyRead(inputParam, page, pageSize);
//            List<FeedbackManageDto> feedbackManageDtos = feedbackManageService.getFeedbackMsgByReadyRead(inputParam, page, pageSize);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 获取已办留言反馈
     * @param inputParam
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取已办留言反馈", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "获取已办留言反馈",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getFeedbackMsgByAlreadyRead")
    @ResponseBody
    public RespObj getFeedbackMsgByAlreadyRead(String inputParam,
                                  @RequestParam(required = false, defaultValue = "1")int page,
                                  @RequestParam(required = false, defaultValue = "10")int pageSize){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            Map<String,Object> map = feedbackManageService.getFeedbackMsgByAlreadyRead(inputParam, page, pageSize);
            respObj.setMessage(map);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 回复留言
     */
    @ApiOperation(value="回复留言",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/replyFeedbackMsg")
    @ResponseBody
    public String replyFeedbackMsg(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,role为2学生评论区") @RequestBody FeedbackManageDto dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(2);
            String result = feedbackManageService.replyFeedbackMsg(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业二级评论失败!");

        }
        return JSON.toJSONString(respObj);

    }

    /**
     * 查看回复
     * @param parentId
     * @return
     */
    @ApiOperation(value = "查看回复", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "查看回复",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getFeedbackMsgAnswerByParentId")
    @ResponseBody
    public RespObj getFeedbackMsgAnswerByParentId(String parentId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            FeedbackManageDto result = feedbackManageService.getFeedbackMsgAnswerByParentId(parentId);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
