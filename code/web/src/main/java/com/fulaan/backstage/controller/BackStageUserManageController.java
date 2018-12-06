package com.fulaan.backstage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.easemob.server.EaseMobAPI;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.backstage.dto.UserManageResultDTO;
import com.fulaan.backstage.service.BackStageUserManageService;
import com.fulaan.base.BaseController;
import com.fulaan.business.service.BusinessManageService;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.controlphone.service.ControlPhoneService;
import com.fulaan.fgroup.dto.GroupDTO;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.fgroup.service.GroupService;
import com.fulaan.jiaschool.service.HomeSchoolService;
import com.fulaan.newVersionBind.dto.BindChildrenDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.service.CommunityService;
import com.fulaan.service.CommunitySystemInfoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private HomeSchoolService homeSchoolService;

    @Autowired
    private CommunitySystemInfoService communitySystemInfoService;

    @Autowired
    private BusinessManageService businessManageService;

    @Autowired
    private ControlPhoneService controlPhoneService;
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
            Map<String, Object> result = backStageUserManageService.getCommunityInfo(map);
            respObj.setMessage(result);
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
     * 复制 DefaultCommunityController 下的inviteMembers
     * param userId communityId
     * @return
     */
    @ApiOperation(value = "当前用户加入社群", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/addUserJoinCommunity")
    @ResponseBody
    public RespObj join(@RequestBody Map map) throws IOException, IllegalParamException {
        ObjectId userId = new ObjectId(map.get("userId").toString());
        ObjectId communityId = new ObjectId(map.get("communityId").toString());
        ObjectId groupId = communityService.getGroupId(communityId);
        String emChatId = groupService.getEmchatIdByGroupId(groupId);
            if (!memberService.isGroupMember(groupId, userId)) {
                if (memberService.isBeforeMember(groupId, userId)) {
                    if (emService.addUserToEmGroup(emChatId, userId)) {
                        memberService.updateMember(groupId, userId, 0);
                        communityService.setPartIncontentStatus(communityId, userId, 0);
                        communityService.pushToUser(communityId, userId, 1);
                    }
                } else {
                    if (emService.addUserToEmGroup(emChatId, userId)) {
                        memberService.saveMember(userId, groupId);
                        communityService.pushToUser(communityId, userId, 1);
                    }
                }
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
        String userIdStrings = map.get("userIds").toString();
        ObjectId communityId = new ObjectId(map.get("communityId").toString());
//        if (StringUtils.isBlank(userIds)) {
//            return RespObj.FAILD("userIds 为空 !");
//        }
//        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
//        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
//        ObjectId userId = getUserId();
////        if (!memberService.isManager(groupId, userId)) {
////            return RespObj.FAILD("您没有这个权限");
////        }
//        List<ObjectId> userList = MongoUtils.convertObjectIds(userIds);
//        if (userList.contains(userId)) {
//            userList.remove(userId);
//        }
//        for (ObjectId personId : userList) {
//            if (memberService.isGroupMember(groupId, personId)) {
//                if (emService.removeUserFromEmGroup(emChatId, personId)) {
//                    memberService.deleteMember(groupId, personId);
//                    //废除数据
//                    if (groupDTO.isBindCommunity()) {
//                        communityService.setPartIncontentStatus(new ObjectId(groupDTO.getCommunityId()), userId, 1);
//                        communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), personId);
//                    }
//                }
//            }
//        }
//        if (!groupDTO.isBindCommunity()) {
//            groupService.asyncUpdateHeadImage(groupId);
//            if (groupDTO.getIsM() == 0) {
//                groupService.asyncUpdateGroupNameByMember(new ObjectId(groupDTO.getId()));
//            }
//        }
        List<ObjectId> userIds = MongoUtils.convertObjectIds(userIdStrings);
        CommunityDTO community = communityService.findByObjectId(communityId);
        ObjectId groupId = communityService.getGroupId(communityId);
        for (ObjectId userId : userIds) {

            if (memberService.isHead(new ObjectId(community.getGroupId()), userId)) {
                return RespObj.FAILD("不能删除社长");
            }
//            if (emService.removeUserFromEmGroup(community.getEmChatId(), userId)) {//这个if判断导致有的社群退不掉？
                emService.removeUserFromEmGroup(community.getEmChatId(), userId);
                communityService.pullFromUser(communityId, userId);
                memberService.deleteMember(groupId, userId);
                //设置先前该用户所发表的数据为废弃掉的
                communityService.setPartIncontentStatus(communityId, userId, 1);
                if (memberService.isManager(groupId, userId)) { //发送退出消息
                    //当是副社长退出时
                    List<ObjectId> objectIds = communityService.getAllMemberIds(groupId);
                    communitySystemInfoService.addBatchData(userId, objectIds, "副社长", 1, communityId);
                }

//            }
        }
        return RespObj.SUCCESS("退出成功！");
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
     * 社群后台设置用户社群角色（前端穿梭框方式）
     * ids role
     * @return
     */
    @ApiOperation(value = "社群后台设置用户社群角色（前端穿梭框方式）", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/setCommunityRoleForTransfer")
    @ResponseBody
    public String setCommunityRoleForTransfer(@RequestBody Map map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try{
            String message = backStageUserManageService.setCommunityRoleForTransfer(map);
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
                //删除社群
                communityService.delCommunityById(new ObjectId(groupDTO.getCommunityId()));
                //删除学校社群关联
                homeSchoolService.delSchoolSort(new ObjectId(groupDTO.getCommunityId()));

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
            //解除孩子社群绑定
            String unCheckUserIds = map.get("unCheckUserIds").toString();
            if (unCheckUserIds != ""){
                backStageUserManageService.relieveChildrenBindRelation(communityId, unCheckUserIds);
            }
            //添加孩子社群绑定
            String checkUserIds = map.get("checkUserIds").toString();
            String mainUserId = map.get("mainUserId").toString();
            if (checkUserIds != ""){
                backStageUserManageService.addChildrenBindRelation(communityId, mainUserId, checkUserIds);
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 获得我的社区
     *
     * @return
     */
    @RequestMapping("/myCommunitys")
    @ResponseBody
    @SessionNeedless
    @ApiOperation(value = "获取我的社区列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "获取我的社区列表成功",response = RespObj.class),
            @ApiResponse(code = 500, message = "获取我的社区列表失败")})
    public RespObj getMyCommunitys(@ApiParam(name="page",required = false,value = "页码")@RequestParam(defaultValue = "1", required = false) int page,//传入-1 查所有
                                   @ApiParam(name="pageSize",required = false,value = "页数")@RequestParam(defaultValue = "100", required = false) int pageSize,
                                   @ApiParam(name="parentUserIds",required = false,value = "孩子父母Ids")@RequestParam(defaultValue = "", required = false) String parentUserIds) {
//        ObjectId userId = getUserId();
        List<ObjectId> userIds = MongoUtils.convertObjectIds(parentUserIds);
        List<CommunityDTO> communityDTOList = new ArrayList<CommunityDTO>();
        CommunityDTO fulanDto = communityService.getCommunityByName("复兰社区");
        if ("" == parentUserIds && null != fulanDto) {
            communityDTOList.add(fulanDto);
            return RespObj.SUCCESS(communityDTOList);
        } else {
            if (null != fulanDto) {
                //加入复兰社区
                joinFulaanCommunity(getUserId(), new ObjectId(fulanDto.getId()));
            }
            for (ObjectId userId : userIds){
                //getCommunitys 不好改造
                List<CommunityDTO> communityDTOListTemp = new ArrayList<CommunityDTO>();
                communityDTOListTemp = communityService.getCommunitys(userId, page, pageSize);
                communityDTOList.addAll(communityDTOListTemp);
            }
            List<CommunityDTO> communityDTOList2 = new ArrayList<CommunityDTO>();
            if(communityDTOList.size()>0){
                for(CommunityDTO dto3 : communityDTOList){
                    //5a7bb6e13d4df96672b6a2bf
                    if(!dto3.getName().equals("复兰社区") && !dto3.getName().equals("复兰大学")){
                        communityDTOList2.add(dto3);
                    }else{
//                        if(!dto3.getName().equals("复兰社区") && userId.toString().equals("5a7bb6e13d4df96672b6a2bf")){
//                        communityDTOList2.add(dto3);
                        if(!dto3.getName().equals("复兰社区")){
                            for (ObjectId userId : userIds){
                                if (userId.toString().equals("5a7bb6e13d4df96672b6a2bf")){
                                    communityDTOList2.add(dto3);
                                }
                            }

                        }
                    }
                }
            }
//            if ("web".equals(platform)) {
//                int count = communityService.countMycommunitys(userId);
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("list", communityDTOList2);
//                map.put("count", count);
//                map.put("pageSize", pageSize);
//                map.put("page", page);
//                return RespObj.SUCCESS(map);
//            } else {
                return RespObj.SUCCESS(communityDTOList2);
//            }
        }
    }


    /**
     * 加入社区但是不加入环信群组---这里只有复兰社区调用
     * 加入复兰社区--- 复兰社区很特殊，特殊对待
     *
     * @param userId
     * @param communityId
     * @return
     */
    private void joinFulaanCommunity(ObjectId userId, ObjectId communityId) {

        ObjectId groupId = communityService.getGroupId(communityId);
        //type=1时，处理的是复兰社区
        if (memberService.isGroupMember(groupId, userId)) {
            return;
        }
        //判断该用户是否曾经加入过该社区
        if (memberService.isBeforeMember(groupId, userId)) {
            memberService.updateMember(groupId, userId, 0);
            communityService.pushToUser(communityId, userId, 3);
            //设置先前该用户所发表的数据
            communityService.setPartIncontentStatus(communityId, userId, 0);
        } else {
            //新人
            communityService.pushToUser(communityId, userId, 3);
            memberService.saveMember(userId, groupId, 0);
        }
    }

    /**
     * 获取孩子绑定社群列表
     */
    @ApiOperation(value = "获取孩子绑定社群列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/getSonCommunityList")
    @ResponseBody
    public String getSonCommunityList(@ApiParam(name = "sonId", required = true, value = "孩子id") @RequestParam("sonId") String sonId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            List<GroupOfCommunityDTO> dto = controlPhoneService.getSonCommunityList(new ObjectId(sonId));
            respObj.setMessage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("获取孩子绑定社群列表!");
        }
        return JSON.toJSONString(respObj);
    }


    /**
     *
     * @param userId
     * @param relieveCommunityIds addCommunityIds
     * @return
     */
    @ApiOperation(value = "处理孩子的绑定关系", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("updateCommunityBindRelation")
    @ResponseBody
    public RespObj relieveCommunityBindRelation(@ObjectIdType ObjectId userId,String relieveCommunityIds,String addCommunityIdsData){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            //解除社区绑定
            if ("" != relieveCommunityIds && null != relieveCommunityIds){
                newVersionBindService.relieveCommunityBindRelation(userId, relieveCommunityIds);
            }
            //添加绑定
            if ("" != addCommunityIdsData && null != addCommunityIdsData){
                //处理数据
                String[] strings = addCommunityIdsData.split(",");
                for (String eachData : strings){
                    ObjectId communityId = new ObjectId(eachData.split("==")[1]);
                    ObjectId parentId = new ObjectId(eachData.split("==")[0]);
                    String userIds = userId+",";
                    newVersionBindService.addCommunityBindEntry(userIds, communityId, parentId);
                }
            }
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作成功！");
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    /**
     * 用户管理重置账号(用户名 也是 手机号)
     * map 中 mobile
     * @return
     */
    @ApiOperation(value = "用户管理重置账号", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class),
            @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    @RequestMapping("/resetUserMobile")
    @ResponseBody
    public RespObj resetUserMobile(@RequestBody Map map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
//            String mobile = map.get("mobile") == null ?"":map.get("mobile").toString();
//            if(){
//
//            }
            String result = backStageUserManageService.resetUserMobile(map);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(result);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**---------解除绑定关系------------**/
    /**
     *
     * @param map
     * parentId childrenId
     * @return
     */
    @ApiOperation(value = "后台运营解除绑定关系", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/relieveBindRelationBack")
    @ResponseBody
    public RespObj relieveBindRelationBack(@RequestBody Map map){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            ObjectId parentId = new ObjectId(map.get("parentId").toString());
            ObjectId childrenId = new ObjectId(map.get("childrenId").toString());
            newVersionBindService.relieveBindRelationBack( parentId, childrenId);
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("手机管控解除成功");
        }catch (Exception e){
            e.printStackTrace();
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
