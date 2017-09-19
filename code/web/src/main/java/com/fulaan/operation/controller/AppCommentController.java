package com.fulaan.operation.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.AppRecordDTO;
import com.fulaan.operation.service.AppCommentService;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/8/25.
 */
@Api(value = "作业相关接口")
@Controller
@RequestMapping("/appOperation")
public class AppCommentController extends BaseController {

    @Autowired
    private AppCommentService appCommentService;
    /**
     * 添加作业
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加作业", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 401, message = "未授权客户机访问数据"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addCommentEntry")
    @ResponseBody
    public String addCommentEntry(@ApiParam @RequestBody AppCommentDTO dto,@ApiParam(name = "comList", required = true, value = "社区idList") @RequestParam("comList") String comList){
        //
        dto.setAdminId(getUserId().toString());
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = appCommentService.addCommentEntry(dto,comList);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查找当前老师今天发布的作业
     * @return
     */
    @RequestMapping("/selectListByTeacherId")
    @ResponseBody
    public String selectListByTeacherId(){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppCommentDTO> dtos = appCommentService.selectListByTeacherId(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查找当前家长收到的作业
     * @return
     */
    @RequestMapping("/selectListFromParent")
    @ResponseBody
    public String selectListFromParent(){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppCommentDTO> dtos = appCommentService.selectListFromParent(getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 查找当前作业签到的家长名单
     * @return
     */
    @RequestMapping("/selectRecordList")
    @ResponseBody
    public String selectRecordList(@RequestParam("id") String id,@RequestParam("type") int type){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppRecordDTO> dtos = appCommentService.selectRecordList(new ObjectId(id),type);
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 是否签到
     * @return
     */
    @RequestMapping("/isSign")
    @ResponseBody
    public String isSign(@RequestParam("date") String date){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            long dateTime = DateTimeUtils.getStrToLongTime(date, "yyyy-MM-dd");
            Map<String,Object> result = appCommentService.isSign(getUserId(),dateTime);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 签到
     * @return
     */
    @RequestMapping("/goSign")
    @ResponseBody
    public String goSign(@RequestParam("qid") String qid){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = appCommentService.goSign(new ObjectId(qid));
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查找当前当前年份用户发放作业情况名单
     * @return
     */
    @RequestMapping("/selectResultList")
    @ResponseBody
    public String selectResultList(@RequestParam("month") int month){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<String> dtos = appCommentService.selectResultList(month, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 查找当前点击的事件老师/家长发放作业情况名单
     * @return
     */
    @RequestMapping("/selectDateList")
    @ResponseBody
    public String selectDateList(@RequestParam("date") String date){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            long dateTime = DateTimeUtils.getStrToLongTime(date, "yyyy-MM-dd");
            Map<String,Object> dtos = appCommentService.selectDateList(dateTime, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 查找当前点击的事件学生收到作业情况名单
     * @return
     */
    @RequestMapping("/getStuLit")
    @ResponseBody
    public String getStuLit(@RequestParam("date") String date){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            long dateTime = DateTimeUtils.getStrToLongTime(date, "yyyy-MM-dd");
            List<AppCommentDTO> dtos = appCommentService.getStuLit(dateTime, getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }
    /**
     * 根据作业id查找当前评论列表
     * @return
     */
    @RequestMapping("/getOperationList")
    @ResponseBody
    public String getOperationList(@RequestParam("id") String id){

        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            List<AppOperationDTO> dtos = appCommentService.getOperationList(new ObjectId(id),getUserId());
            respObj.setMessage(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加作业评论
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加作业评论", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping("/addOperationEntry")
    @ResponseBody
    public String addOperationEntry(@ApiParam @RequestBody AppOperationDTO dto){
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            dto.setUserId(getUserId().toString());
            String result = appCommentService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");

        }
        return JSON.toJSONString(respObj);
    }

    //community/myCommunitys/page/pageSize/?platform=app
}
