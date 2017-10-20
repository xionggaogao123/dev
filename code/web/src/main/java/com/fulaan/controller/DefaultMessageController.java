package com.fulaan.controller;

import com.fulaan.base.BaseController;
import com.fulaan.dto.MessageDTO;
import com.fulaan.forum.service.FInformationService;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/9/13.
 * 消息Rest API[私信，系统消息,点赞]
 */
@Api(value="消息Rest API[私信，系统消息,点赞]")
@Controller
@RequestMapping("/jxmapi/v1/message")
public class DefaultMessageController extends BaseController {

    @Autowired
    private FInformationService fInformationService;
    @ApiOperation(value = "获得信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespObj getMessage(@RequestParam(value = "clear", defaultValue = "0") int clear) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (clear == 0) {
            List<MessageDTO> messageList = fInformationService.getInformationUnRead(getUserId());
            map.put("count", messageList.size());
            map.put("list", messageList);
            return RespObj.SUCCESS(map);
        } else if (clear == 1) {
            fInformationService.clearReadedMessage(getUserId());
        }
        return RespObj.SUCCESS;
    }
    @ApiOperation(value = "getUnReadSystemMessageCount", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/unReadSysCount", method = RequestMethod.GET)
    @ResponseBody
    public RespObj getUnReadSystemMessageCount() {
        ObjectId uid = getUserId();
        return RespObj.SUCCESS(fInformationService.getUnReadSystemMsgCount(uid));
    }
}
