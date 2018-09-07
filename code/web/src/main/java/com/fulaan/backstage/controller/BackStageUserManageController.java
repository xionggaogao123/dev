package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.easemob.server.EaseMobAPI;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.backstage.dto.UserManageResultDTO;
import com.fulaan.backstage.service.BackStageUserManageService;
import com.fulaan.base.BaseController;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.fgroup.dto.GroupDTO;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.fgroup.service.GroupService;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.service.CommunityService;
import com.fulaan.service.MemberService;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by taotao.chan on 2018年8月30日09:32:46
 */
@Api(value = "后台用户管理类")
@Controller
@RequestMapping("/web/backstageusermanage")
public class BackStageUserManageController extends BaseController {
    @Autowired
    private BackStageUserManageService backStageUserManageService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private EmService emService;

    @Autowired
    private NewVersionBindService newVersionBindService;

    /**
     * 后台用户管理角色筛选
     * @return
     */
    @ApiOperation(value = "后台用户管理角色筛选", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserRoleOption")
    @ResponseBody
    public RespObj getUserRoleOption() {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            JSONArray jsonArray = backStageUserManageService.getUserRoleOption();
            respObj.setMessage(jsonArray);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("用户管理角色筛选查询错误!");
        }
        return respObj;
    }

    /**
     * 后台用户管理查询
     * @return
     */
    @ApiOperation(value = "后台用户管理查询", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserListByRole")
    @ResponseBody
    public RespObj getUserListByRole(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<UserManageResultDTO> userManageResultDTOS = backStageUserManageService.getUserListByRole(map);
            respObj.setMessage(userManageResultDTOS);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("后台用户管理查询错误!");
        }
        return respObj;
    }

    /**
     * 后台用户管理查询创建社群
     * @return
     */
    @ApiOperation(value = "查询用户创建社群", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserCreatedCommunity")
    @ResponseBody
    public RespObj getUserCreatedCommunity(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> userManageResultDTOS = backStageUserManageService.getUserCreatedCommunity(map);
            respObj.setMessage(userManageResultDTOS);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询用户创建社群错误!");
        }
        return respObj;
    }


    /**
     * 后台用户管理查询加入社群
     * @return
     */
    @ApiOperation(value = "查询用户加入社群", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getUserJoinCommunity")
    @ResponseBody
    public RespObj getUserJoinCommunity(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> userManageResultDTOS = backStageUserManageService.getUserJoinCommunity(map);
            respObj.setMessage(userManageResultDTOS);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询用户加入社群错误!");
        }
        return respObj;
    }

    /**
     * 后台用户管理查询社群成员
     * @return
     */
    @ApiOperation(value = "查询社群成员", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommunityMember")
    @ResponseBody
    public RespObj getCommunityMember(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            Map<String,Object> result = backStageUserManageService.getCommunityMember(map);
            respObj.setMessage(result);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("查询社群成员错误!");
        }
        return respObj;
    }

    /**
     * 搜索社群信息
     * searchParam(包括searchId（社群查找Id）communityName)
     * @param map
     * @return
     */
    @ApiOperation(value = "搜索社群信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getCommunityInfo")
    @ResponseBody
    public RespObj getCommunityInfo(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            CommunityDTO communityDTO = backStageUserManageService.getCommunityInfo(map);
            respObj.setMessage(communityDTO);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("搜索社群信息错误!");
        }
        return respObj;
    }

    /**
     * 用户加入社群
     * 复制 WebGroupController 下的join
     * param userId emChatId
     * @return
     */
    @ApiOperation(value = "当前用户加入社群", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addUserJoinCommunity")
    @ResponseBody
    public RespObj join(@RequestBody Map map) throws IOException, IllegalParamException {
        String emChatId = map.get("emChatId").toString();
        ObjectId userId = new ObjectId(map.get("userId").toString());
//        GroupDTO groupDTO = groupService.findByEmChatId(emChatId,getUserId());
        GroupDTO groupDTO = groupService.findByEmChatId(emChatId,userId);
        if (groupDTO == null) {
            return RespObj.FAILDWithErrorMsg("不存在此群聊或社区");
        }
        ObjectId groupId = new ObjectId(groupDTO.getId());
//        ObjectId userId = getUserId();
        if (!memberService.isGroupMember(groupId, userId)) {
            if (memberService.isBeforeMember(groupId, userId)) {
                if (emService.addUserToEmGroup(emChatId, userId)) {
                    memberService.updateMember(userId, groupId, 0);
                    if (groupDTO.isBindCommunity()) {
                        communityService.setPartIncontentStatus(new ObjectId(groupDTO.getCommunityId()), userId, 0);
                    }
                }
            } else {
                if (emService.addUserToEmGroup(emChatId, userId)) {
                    memberService.saveMember(userId, groupId, 0);
                }
            }
        }
        //更新群聊头像
        groupService.asyncUpdateHeadImage(groupId);
        if (groupDTO.getIsM() == 0) {
            groupService.asyncUpdateGroupNameByMember(new ObjectId(groupDTO.getId()));
        }
        return RespObj.SUCCESS("操作成功");
    }


    /**
     * 批量删除成员
     * 复制 WebGroupController 下的quitMember
     * param userIds emChatId
     * @param
     * @param
     * @return
     */
    @RequestMapping("/deleteMembers")
    @ResponseBody
    @ApiOperation(value = "删除该社区选中的人员列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除该社区选中的人员列表成功",response = RespObj.class),
            @ApiResponse(code = 500, message = "删除该社区选中的人员列表失败")})
    public RespObj quitMember(@RequestBody Map map) throws IOException, IllegalParamException {
        String emChatId = map.get("emChatId").toString();
        String userIds = map.get("userIds").toString();
        if (StringUtils.isBlank(userIds)) {
            return RespObj.FAILD("userIds 为空 !");
        }
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
        ObjectId userId = getUserId();
//        if (!memberService.isManager(groupId, userId)) {
//            return RespObj.FAILD("您没有这个权限");
//        }
        List<ObjectId> userList = MongoUtils.convertObjectIds(userIds);
        if (userList.contains(userId)) {
            userList.remove(userId);
        }
        for (ObjectId personId : userList) {
            if (memberService.isGroupMember(groupId, personId)) {
                if (emService.removeUserFromEmGroup(emChatId, personId)) {
                    memberService.deleteMember(groupId, personId);
                    //废除数据
                    if (groupDTO.isBindCommunity()) {
                        communityService.setPartIncontentStatus(new ObjectId(groupDTO.getCommunityId()), userId, 1);
                        communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), personId);
                    }
                }
            }
        }
        if (!groupDTO.isBindCommunity()) {
            groupService.asyncUpdateHeadImage(groupId);
            if (groupDTO.getIsM() == 0) {
                groupService.asyncUpdateGroupNameByMember(new ObjectId(groupDTO.getId()));
            }
        }
        return RespObj.SUCCESS("退出成功");
    }

    /**
     * 后台设置用户社群角色
     * id role
     * @return
     */
    @ApiOperation(value = "后台设置用户社群角色", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/setCommunityRole")
    @ResponseBody
    public String setCommunityRole(@RequestBody Map map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String message = backStageUserManageService.setCommunityRole(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(message);
        }catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("设置失败");
        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 解散群聊
     * 复制从 DefaultGroupController
     * emChatId
     * @param
     * @return
     */
    @ApiOperation(value = "解散群聊", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/deleteGroup")
    @ResponseBody
    public RespObj deleteGroup(@RequestBody Map map) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String emChatId = map.get("emChatId").toString();
            ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
            GroupDTO groupDTO = groupService.findById(groupId,getUserId());
            ObjectId userId = getUserId();
//            if (memberService.isHead(groupId, userId)) {

                // List<String> str = new ArrayList<String>();
                //   EaseMobAPI.removeBatchUsersFromChatGroup(emChatId, str);
                // communityService.deleteReplyDetailText();
                List<ObjectId> userIds = memberService.getAllGroupMemberIds(groupId);
                List<ObjectId> userIds2 = new ArrayList<ObjectId>();
                userIds2.addAll(userIds);
                userIds.remove(userId);
                if (userIds.size() >0 && emService.removeUserListFromEmGroup(emChatId, userIds)) {
                    if (groupDTO.isBindCommunity()) {
                        //社群删除
                        communityService.setPartIncontentStatusList(new ObjectId(groupDTO.getCommunityId()), userIds2, 1);
                        //成绩单删除
                        communityService.pullFromUserList(new ObjectId(groupDTO.getCommunityId()), userIds2);
                    }
                }else{
                    if (groupDTO.isBindCommunity()) {
                        //社群删除
                        communityService.setPartIncontentStatusList(new ObjectId(groupDTO.getCommunityId()), userIds2, 1);
                        //成绩单删除
                        communityService.pullFromUserList(new ObjectId(groupDTO.getCommunityId()), userIds2);
                    }
                }
                //环信删除
                EaseMobAPI.deleteChatGroup(emChatId);
                //成员删除
                memberService.deleteMemberList(groupId, userIds2);
                //群组删除
                groupService.deleteGroup(groupId);


//            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("解散群聊成功");
        }catch (Exception e){
            respObj.setMessage("解散群聊失败");
            respObj.setCode(Constant.FAILD_CODE);
            e.printStackTrace();
        }
        return respObj;
    }


    /**
     * 后台设置解除孩子们绑定
     * map 中 userIds（,拼接的孩子的Id） communityId
     * copy from API /relieveCommunityBindRelation
     * @return
     */
    @ApiOperation(value = "解除孩子们绑定", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/relieveChildrenBindRelation")
    @ResponseBody
    public RespObj relieveChildrenBindRelation(@RequestBody Map map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            ObjectId communityId = new ObjectId(map.get("communityId").toString());
            String userIds = map.get("userIds").toString();
            backStageUserManageService.relieveChildrenBindRelation(communityId, userIds);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
