package com.fulaan.operation.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.service.AppNoticeService;
import com.fulaan.pojo.User;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/22.
 */
@Api(value = "通知相关接口")
@Controller
@RequestMapping("/jxmapi/appNotice")
public class DefaultAppNoticeController extends BaseController {

    @Autowired
    private AppNoticeService appNoticeService;

    private static final Logger logger =Logger.getLogger(DefaultAppNoticeController.class);

    @RequestMapping("/getReadNoticeUserList")
    @ResponseBody
    @ApiOperation(value = "查询已阅和未阅列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getReadNoticeUserList(@ObjectIdType ObjectId id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,List<User>> userList=appNoticeService.getReadNoticeUserList(id);
            respObj.setMessage(userList);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @RequestMapping("/removeAppNoticeEntry")
    @ResponseBody
    @ApiOperation(value = "删除通知信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除通知信息已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj removeAppNoticeEntry(@ObjectIdType ObjectId noticeId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.removeAppNoticeEntry(noticeId,getUserId());
            respObj.setMessage("删除通知信息成功！");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping(value="/saveAppNoticeEntry", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @ApiOperation(value = "保存通知信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getReadNoticeUserList(@RequestBody AppNoticeDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.saveAppNoticeEntry(dto,getUserId());
            respObj.setMessage("保存通知信息成功！");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            logger.error("error",e);
            if("推送失败".equals(e.getMessage())) {
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(e.getMessage());
            }else{
                respObj.setErrorMessage(e.getMessage());
            }
        }
        return respObj;
    }


    @RequestMapping("/getMySendAppNoticeDtos")
    @ResponseBody
    @ApiOperation(value = "获取我发送的通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMySendAppNoticeDtos(
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
            ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object>  retMap=appNoticeService.getMySendAppNoticeDtos(getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/getMyReceivedAppNoticeDtosForParent")
    @ResponseBody
    @ApiOperation(value = "获取我接收到的通知(家长)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMyReceivedAppNoticeDtosForParent(
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appNoticeService.getMyReceivedAppNoticeDtos(getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    @RequestMapping("/pushRead")
    @ResponseBody
    @ApiOperation(value = "阅读该通知消息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj pushRead(
           @ObjectIdType ObjectId id
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.pushRead(id,getUserId());
            respObj.setMessage("阅读成功!");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "添加通知评论", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value="/addOperationEntry")
    @ResponseBody
    public String addOperationEntry(@RequestParam(required = false,defaultValue = "") String contactId,
                                    @RequestParam(required = false,defaultValue = "0") int role,
                                    @RequestParam(required = false,defaultValue = "") String backId,
                                    @RequestParam(required = false,defaultValue = "") String parentId,
                                    @RequestParam(required = false,defaultValue = "") String description){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            AppOperationDTO dto=new AppOperationDTO();
            dto.setContactId(contactId);
            dto.setRole(role);
            dto.setBackId(backId);
            dto.setParentId(parentId);
            dto.setDescription(description);
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(1);
            String result = appNoticeService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业评论失败!");

        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加通知评论
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加通知评论1", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value="/addOperationEntry2")
    @ResponseBody
    public String addOperationEntry2(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为通知id，role为2学生评论区，role为1家长评论区") @RequestBody AppOperationDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(1);
            String result = appNoticeService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业评论失败!");

        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加通知二级评论
     */
    @ApiOperation(value="添加二级回复",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/addSecondOperation")
    @ResponseBody
    public String addSecondOperation(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为通知id，role为1家长评论区，role为2学生评论区") @RequestBody AppOperationDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(2);
            String result = appNoticeService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业二级评论失败!");

        }
        return JSON.toJSONString(respObj);

    }


    @RequestMapping("/getMyReceivedAppNoticeDtosForStudent")
    @ResponseBody
    @ApiOperation(value = "获取我接收到的通知(学生)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMyReceivedAppNoticeDtosForStudent(
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appNoticeService.getMyReceivedAppNoticeDtosForStudent(getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/searchAppNotice")
    @ResponseBody
    @ApiOperation(value = "搜索合适条件的通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "搜索合适条件的通知成功",response = RespObj.class),
            @ApiResponse(code = 500, message = "搜索合适条件的通知失败")})
    public RespObj searchAppNotice(
            @RequestParam(required = true, defaultValue = "")String keyWord,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<AppNoticeDTO> dtos=appNoticeService.searchAppNotice(keyWord,getUserId(),page,pageSize);
            respObj.setMessage(dtos);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/getMyGatherNotice")
    @ResponseBody
    @ApiOperation(value = "获取我汇总的所有通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMyGatherNotice(
            @RequestParam(required = false, defaultValue = "")String communityId,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object>  retMap=appNoticeService.getMyAppNotices(communityId,getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
