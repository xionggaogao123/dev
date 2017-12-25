package com.fulaan.fgroup;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.fgroup.dto.GroupChatRecordDTO;
import com.fulaan.fgroup.service.GroupService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
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
 * Created by scott on 2017/12/25.
 */
@RequestMapping("/jxmapi/groupchatrecord")
@Controller
public class GroupChatRecordController extends BaseController{

    @Autowired
    private GroupService groupService;

    @ApiOperation(value = "保存聊天记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/saveEntry")
    @ResponseBody
    public RespObj saveEntry(@RequestBody GroupChatRecordDTO dto){
        RespObj respObj =new RespObj(Constant.SUCCESS_CODE);
        dto.setUserId(getUserId().toString());
        groupService.saveGroupChatRecord(dto);
        respObj.setMessage("保存信息成功");
        return respObj;
    }


    @ApiOperation(value = "获取聊天记录", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getGroupChatRecords")
    @ResponseBody
    public RespObj getGroupChatRecords(@ObjectIdType ObjectId groupId,
                                       @ObjectIdType ObjectId userId,
                                       String time,
                                       int page,
                                       int pageSize){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<GroupChatRecordDTO> recordDTOList = groupService.getGroupChatRecords(groupId,userId,page,pageSize,time);
        respObj.setMessage(recordDTOList);
        return respObj;
    }
}
