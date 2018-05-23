package com.fulaan.systemMessage.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppCommentDTO;
import com.fulaan.operation.dto.WebAppCommentDTO;
import com.fulaan.systemMessage.service.SystemMessageService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by James on 2018-05-22.
 */
@Controller
@RequestMapping("/web/systemMessage")
public class SystemMessageController extends BaseController {

    @Autowired
    private SystemMessageService systemMessageService;


    @ApiOperation(value = "发送特殊通知", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/addEntry")
    @ResponseBody
    public String addEntry(@ApiParam @RequestBody WebAppCommentDTO wdto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            AppCommentDTO dto = wdto.getAppCommentDTO(wdto);
            ObjectId userId = new ObjectId("575e21be0cf2a633a9ff7b6b");
            dto.setAdminId(userId.toString());
            systemMessageService.addEntry(userId, dto);
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("发送特殊通知失败");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     * 历史回顾
     * @return
     */
    @ApiOperation(value = "历史回顾", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/getMyRoleCommunity")
    @ResponseBody
    public String getMyRoleCommunity(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            //indexPageService.addIndexPage(communityId, contactId, type,getUserId());
            respObj.setMessage("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("发送特殊通知失败");
        }
        return JSON.toJSONString(respObj);
    }
}
