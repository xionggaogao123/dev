package com.fulaan.operation.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.AppRecordDTO;
import com.fulaan.operation.service.AppCommentService;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by James on 2017/8/25.
 */
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
    @RequestMapping("/addCommentEntry")
    @ResponseBody
    public String addCommentEntry(AppCommentDTO dto,@RequestParam("comList")List<String> comList){
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
     * 查找当前当前月份用户发放作业情况名单
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
    @RequestMapping("/addOperationEntry")
    @ResponseBody
    public String addOperationEntry(AppOperationDTO dto){
        //todo  上传文件类型判断
        RespObj respObj=null;
        try {
            respObj = RespObj.SUCCESS;
            String result = appCommentService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj = RespObj.FAILD;
            respObj.setMessage("添加关键字失败!");
        }
        return JSON.toJSONString(respObj);
    }
}
