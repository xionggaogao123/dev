package com.fulaan.fgroup;

import com.easemob.server.EaseMobAPI;
import com.easemob.server.comm.constant.MsgType;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.base.BaseController;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.community.dto.CommunityDetailDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.fgroup.dto.GroupDTO;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.fgroup.service.GroupService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.pojo.*;
import com.fulaan.service.CommunityService;
import com.fulaan.service.GroupNoticeService;
import com.fulaan.service.MemberService;
import com.fulaan.user.service.UserService;
import com.pojo.fcommunity.CommunityDetailType;
import com.pojo.fcommunity.GroupEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.RespObj;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

/**
 * Created by jerry on 2016/10/31.
 * GroupController
 */
@Api(value="GroupController")
@Controller
@RequestMapping("/jxmapi/group")
public class DefaultGroupController extends BaseController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupNoticeService groupNoticeService;
    @Autowired
    private BackStageService backStageService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private NewVersionBindService newVersionBindService;
    @Autowired
    private EmService emService;


    /**
     * 创建群聊
     * 1.创建环信群聊
     * 2.创建群聊组
     *
     * @param userIds
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "创建群聊", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/create")
    @ResponseBody
    public RespObj createGroup(@RequestParam(defaultValue = "", required = false) String userIds) throws Exception {
        ObjectId userId = getUserId();
        //创建环信群聊
        String emChatId = emService.createEmGroup(userId);
        if (emChatId == null) {
            return RespObj.FAILD("环信群组创建失败");
        }
        //创建组
        ObjectId groupId = groupService.createGroupWithoutCommunity(userId, emChatId);
        List<ObjectId> userList = MongoUtils.convertObjectIds(userIds);
        for (ObjectId personId : userList) {
            if (!memberService.isGroupMember(groupId, personId)) {
                if (emService.addUserToEmGroup(emChatId, personId)) {
                    memberService.saveMember(personId, groupId);
                }
            }
        }
        groupService.asyncUpdateHeadImage(groupId);
        groupService.asyncUpdateGroupNameByMember(groupId);
        return RespObj.SUCCESS(groupService.findById(groupId,userId));
    }

    /**
     * 获取群聊详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取群聊详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping(value = "/{id}")
    @ResponseBody
    public RespObj detail(@PathVariable String id) {
        ObjectId groupId = groupService.getGroupIdByChatId(id);
        if (groupId == null) {
            return RespObj.FAILD("群组不存在");
        }
        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
        if (groupDTO.isBindCommunity()) {
            CommunityDTO communityDTO = communityService.getByEmChatId(groupDTO.getEmChatId());
            groupDTO.setSearchId(communityDTO.getSearchId());
            String image = StringUtils.isNotBlank(groupDTO.getHeadImage()) ? groupDTO.getHeadImage() : communityDTO.getLogo();
            groupDTO.setHeadImage(image);
            groupDTO.setName(communityDTO.getName());
        }
        MemberDTO mine = memberService.getUser(groupId, getUserId());
        groupDTO.setCount(memberService.getMemberCount(groupId));
        groupDTO.setMine(mine);
        if (groupDTO.isBindCommunity()) {
            ObjectId communityId = new ObjectId(groupDTO.getCommunityId());
            CommunityDetailDTO groupAnnounceDTO = communityService.getLatestAnnouncement(communityId,getUserId());
            groupDTO.setCurAnnounceMent(groupAnnounceDTO);
        } else {
            GroupAnnounceDTO groupAnnounceDTO = groupNoticeService.getEarlyAnnounce(groupId);
            groupDTO.setCurAnnounceMent(groupAnnounceDTO);
        }
        return RespObj.SUCCESS(groupDTO);
    }

    /**
     * 根据emChatids获取群组信息
     *
     * @param emChatIds
     * @return
     */
    @ApiOperation(value = "根据emChatids获取群组信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/listEmchat")
    @ResponseBody
    public RespObj listEmchat(String emChatIds) {
        String[] emChatList = emChatIds.split(",");
        List<GroupDTO> list = new ArrayList<GroupDTO>();
        for (String emChatId : emChatList) {
            ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
            if (groupId == null) continue;
            try {
                GroupDTO groupDTO = groupService.findById(groupId,getUserId());
                MemberDTO mine = memberService.getUser(groupId, getUserId());
                groupDTO.setCount(memberService.getMemberCount(groupId));
                groupDTO.setMine(mine);
                list.add(groupDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return RespObj.SUCCESS(list);
    }

    /**
     * 获取群聊成员
     *
     * @param emChatId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取群聊成员", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/members")
    @ResponseBody
    public RespObj getMembers(String emChatId,
                              @RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize) {
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        return RespObj.SUCCESS(memberService.getGroupMembers(groupId, page, pageSize));
    }

    /**
     * 获取全部成员
     *
     * @param emChatId
     * @return
     */
    @ApiOperation(value = "获取全部成员", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/allMembers")
    @ResponseBody
    public RespObj getMembers(String emChatId) {
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        return RespObj.SUCCESS(memberService.getAllGroupMembers(groupId,getUserId()));
    }

    /**
     * 群主邀请人进群
     *
     * @param emChatId 环信id
     * @param userIds
     * @return
     */
    @ApiOperation(value = "群主邀请人进群", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/inviteMember")
    @ResponseBody
    public RespObj inviteMember(String emChatId,
                                String userIds) throws IOException, IllegalParamException {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
//            ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
            GroupEntry groupEntry = groupService.getGroupEntryByEmchatId(emChatId);
            if(null==groupEntry){
                throw new Exception("传入的环信Id有误");
            }
            ObjectId groupId=groupEntry.getID();
            GroupDTO groupDTO = groupService.findById(groupId, getUserId());
            List<ObjectId> userList = MongoUtils.convertObjectIds(userIds);
            if(null!=groupEntry.getCommunityId()){
                //判断是社群时，不能把学生拉进去社群
                if(newVersionBindService.judgeUserStudent(userList)){
                    throw new Exception("不能把学生拉进社群");
                }
            }
            for (ObjectId personId : userList) {
                if (!memberService.isGroupMember(groupId, personId)) {
                    if (emService.addUserToEmGroup(emChatId, personId)) {
                        if (memberService.isBeforeMember(groupId, personId)) {
                            memberService.updateMember(groupId, personId, 0);
                        } else {
                            memberService.saveMember(personId, groupId);
                        }
                        if (groupDTO.isBindCommunity()) {
                            communityService.setPartIncontentStatus(new ObjectId(groupDTO.getCommunityId()), personId, 0);
                            communityService.pushToUser(new ObjectId(groupDTO.getCommunityId()), personId, 1);
                        }
                    }
                }
            }

            //自动加为好友(社群拉人自动加为好友)
            if(groupDTO.isBindCommunity()) {
                List<String> keysList=MongoUtils.convertToStringList(userList);
                if(keysList.size()>0) {
                    List<ObjectId> communityIds = new ArrayList<ObjectId>();
                    communityIds.add(groupEntry.getCommunityId());
                    String[] keys = keysList.toArray(new String[keysList.size()]);
                    backStageService.setAutoChildFriends(keys,communityIds);
                }
            }
            //更新群聊头像
            groupService.asyncUpdateHeadImage(groupId);
            if (groupDTO.getIsM() == 0) {
                groupService.asyncUpdateGroupNameByMember(new ObjectId(groupDTO.getId()));
            }

            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage("操作成功");
        }catch (Exception e){
             respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    /**
     * 加入群组
     *
     * @param emChatId
     * @return
     * @throws java.io.IOException
     * @throws com.sys.exceptions.IllegalParamException
     */
    @ApiOperation(value = "加入群组", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/join")
    @ResponseBody
    public RespObj join(String emChatId) throws IOException, IllegalParamException {
        GroupDTO groupDTO = groupService.findByEmChatId(emChatId,getUserId());
        if (groupDTO == null) {
            return RespObj.FAILDWithErrorMsg("不存在此群聊或社区");
        }
        ObjectId groupId = new ObjectId(groupDTO.getId());
        ObjectId userId = getUserId();
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
     * 群主踢人
     *
     * @param emChatId
     * @param userIds
     * @return
     * @throws java.io.IOException
     * @throws com.sys.exceptions.IllegalParamException
     */
    @ApiOperation(value = "群主踢人", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/quitMember")
    @ResponseBody
    public RespObj quitMember(String emChatId, String userIds) throws IOException, IllegalParamException {
        if (StringUtils.isBlank(userIds)) {
            return RespObj.FAILD("userIds 为空 !");
        }
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
        ObjectId userId = getUserId();
        if (!memberService.isManager(groupId, userId)) {
            return RespObj.FAILD("您没有这个权限");
        }
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
     * 退出群聊
     *
     * @param emChatId
     * @return
     */
    @ApiOperation(value = "退出群聊", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/quit")
    @ResponseBody
    public RespObj quitGroup(String emChatId) throws IOException, IllegalParamException {

        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
        ObjectId userId = getUserId();
        if (memberService.isHead(groupId, userId)) {
            List<MemberDTO> memberDTOs = memberService.getMembers(groupId, 2,userId);
            //转让
            if (memberDTOs.size() == 2) {
                MemberDTO memberDTO = memberDTOs.get(1);
                EaseMobAPI.transferChatGroupOwner(emChatId, memberDTO.getUserId());
                memberService.setHead(groupId, new ObjectId(memberDTO.getUserId()));
                groupService.transferOwerId(groupId, new ObjectId(memberDTO.getUserId()));

                if (emService.removeUserFromEmGroup(emChatId, userId)) {
                    memberService.deleteMember(groupId, userId);
                }
                if (groupDTO.isBindCommunity()) {
                    communityService.setPartIncontentStatus(new ObjectId(groupDTO.getCommunityId()), userId, 1);
                    communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), userId);
                    communityService.transferOwner(new ObjectId(groupDTO.getCommunityId()),new ObjectId(memberDTO.getUserId()));
                }
            }
            if (memberDTOs.size() == 1) {//只有一个人
                EaseMobAPI.deleteChatGroup(emChatId);
                memberService.deleteMember(groupId, userId);
                groupService.deleteGroup(groupId);
                if (groupDTO.isBindCommunity()) {
                    communityService.setPartIncontentStatus(new ObjectId(groupDTO.getCommunityId()), userId, 1);
                    communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), userId);
                }
            }
        } else {
            if (emService.removeUserFromEmGroup(emChatId, userId)) {
                memberService.deleteMember(groupId, userId);
                if (groupDTO.isBindCommunity()) {
                    communityService.setPartIncontentStatus(new ObjectId(groupDTO.getCommunityId()), userId, 1);
                    communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), userId);
                }
            }
        }
        groupService.asyncUpdateHeadImage(groupId);
        if (groupDTO.getIsM() == 0) {
            groupService.asyncUpdateGroupNameByMember(new ObjectId(groupDTO.getId()));
        }
        return RespObj.SUCCESS("退出成功");
    }

    /**
     * 获取不在群聊的好友列表
     *
     * @param emChatId
     * @return
     */
    @ApiOperation(value = "获取不在群聊的好友列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getOnJoin")
    @ResponseBody
    public RespObj getOnJoin(String emChatId) {
        ObjectId userId = getUserId();
        List<User> users = new ArrayList<User>();
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        List<UserDetailInfoDTO> friends = friendService.findMyFriends(userId.toString());
        for (UserDetailInfoDTO detailInfoDTO : friends) {
            boolean isMember = groupService.isGroupMember(groupId, new ObjectId(detailInfoDTO.getId()));
            if (!isMember) {
                User user = new User();
                user.setId(detailInfoDTO.getId());
                user.setUserName(detailInfoDTO.getUserName());
                user.setNickName(detailInfoDTO.getNickName());
                user.setAvator(detailInfoDTO.getImgUrl());
                user.setUserId(detailInfoDTO.getId());
                users.add(user);
            }
        }
        return RespObj.SUCCESS(users);
    }

    /**
     * 获取在群里的可删列表
     *
     * @param emChatId
     * @return
     */
    @ApiOperation(value = "获取在群里的可删列表", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getOnQuitList")
    @ResponseBody
    public RespObj getQuitList(String emChatId) {
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        List<ObjectId> userIds = memberService.getGroupQuitList(groupId);
        List<User> users = new ArrayList<User>();
        List<UserEntry> userEntries = userService.getUserByList(userIds);
        for (UserEntry userEntry : userEntries) {
            User user = new User();
            user.setUserId(userEntry.getID().toString());
            user.setUserName(userEntry.getUserName());
            user.setNickName(userEntry.getNickName());
            user.setAvator(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(),userEntry.getSex()));
            users.add(user);
        }
        return RespObj.SUCCESS(users);
    }

    /**
     * 更新群聊名称
     *
     * @param emChatId
     * @param groupName
     * @return
     */
    @ApiOperation(value = "更新群聊名称", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateGroupName")
    @ResponseBody
    public RespObj updateGroupName(String emChatId,
                                   String groupName) {
        ObjectId userId = getUserId();
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        if (!memberService.isManager(groupId, userId)) {
            return RespObj.FAILD("对不起，您没有此权限");
        }
        if (groupName.equals("复兰大学")) {
            return RespObj.FAILD("对不起，此群聊名已被使用");
        }
        GroupDTO groupDTO = groupService.findById(groupId,userId);
        if (groupDTO.isBindCommunity()) {
            communityService.updateCommunityName(new ObjectId(groupDTO.getCommunityId()), groupName);
        }
        groupService.asyncUpdateGroupName(groupId, groupName);
        //向群里发送文本消息
        sendGroupInfo(groupId,userId,groupName,emChatId);
        return RespObj.SUCCESS("操作成功");
    }

    public void sendGroupInfo(ObjectId groupId,ObjectId userId,String groupName,String emchatId){
        Map<String, String> sendMessage = new HashMap<String, String>();
        sendMessage.put("type", MsgType.TEXT);

        List<ObjectId> groupIds = new ArrayList<ObjectId>();
        groupIds.add(groupId);
        List<ObjectId> partInUserIds=new ArrayList<ObjectId>();
        partInUserIds.add(userId);
        List<String> targets =new ArrayList<String>();
        targets.add(emchatId);
        Map<String, MemberEntry> memberEntryMap = communityService.getMemberEntryMap(groupIds, partInUserIds);
        MemberEntry entry1 = memberEntryMap.get(groupId + "$" + userId);
        String nickName="";
        UserEntry userEntry=userService.findById(userId);
        if (null != entry1) {
            if (StringUtils.isNotBlank(entry1.getNickName())) {
                nickName=entry1.getNickName();
            } else {
                nickName=StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
            }
        } else {
            nickName=StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
        }
        String msg=nickName+"修改了群名";
        sendMessage.put("msg", msg);
        Map<String, String> ext=new HashMap<String, String>();
        ext.put("userId",userId.toString());
        ext.put("groupName",groupName);
        ext.put("updateGroupName","YES");
        ext.put("nickName",nickName);
        if(groupService.findByObjectId(groupId)){
            ext.put("groupStyle","community");//社群
        }else{
            ext.put("groupStyle","discussgroup");//讨论组
        }

        emService.sendTextMessage("chatgroups", targets, userId.toString(), ext, sendMessage);
    }

    /**
     * 更新在群组的昵称
     *
     * @param emChatId
     * @param nickName
     * @return
     */
    @ApiOperation(value = "更新在群组的昵称", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateMyNickname")
    @ResponseBody
    public RespObj updateMyNickname(String emChatId,
                                    String nickName) {
        ObjectId userId = getUserId();
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        memberService.updateMyNickname(groupId, userId, nickName);
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 更新设置 免打扰设置
     *
     * @param emChatId
     * @param status
     * @return
     */
    @ApiOperation(value = "更新设置 免打扰设置", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateMyStatus")
    @ResponseBody
    public RespObj updateMyStatus(String emChatId,
                                  @RequestParam(defaultValue = "0", required = false) int status) {
        ObjectId userId = getUserId();
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        memberService.updateMyStatus(groupId, userId, status);
        return RespObj.SUCCESS("操作成功");
    }

    /**
     * 设置副群主
     *
     * @param emChatId
     * @param userIds
     * @return
     */
    @ApiOperation(value = "设置副群主", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/setMaster")
    @ResponseBody
    public RespObj setRole(String emChatId,
                           @RequestParam(defaultValue = "", required = false) String userIds) {
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        if (!memberService.isHead(groupId, getUserId())) {
            return RespObj.FAILD("您没有权限");
        }
        if (StringUtils.isBlank(userIds)) {
            memberService.clearDeputyHead(groupId);
            return RespObj.SUCCESS("操作成功！");
        }
        List<ObjectId> memberIds = MongoUtils.convertObjectIds(userIds);
        if (memberIds.size() > 2) {
            return RespObj.FAILD("只能设置两个副社长");
        }
        MemberDTO head = memberService.getHead(groupId);
        if (memberIds.contains(new ObjectId(head.getUserId()))) {
            return RespObj.FAILD("不能设置群主为副群主");
        }
        memberService.setDeputyHead(groupId, memberIds);
        return RespObj.SUCCESS("操作成功！");
    }

    /**
     * 取消 副群主
     *
     * @param emChatId
     * @param userIds
     * @return
     */
    @ApiOperation(value = "取消 副群主", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/unsetMaster")
    @ResponseBody
    public RespObj unsetMaster(String emChatId,
                               @RequestParam(defaultValue = "", required = false) String userIds) {
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        if (!memberService.isHead(groupId, getUserId())) {
            return RespObj.FAILD("您没有权限");
        }
        String[] userList = userIds.split(",");
        for (String userId : userList) {
            memberService.unsetDeputyHead(groupId, new ObjectId(userId));
        }
        return RespObj.SUCCESS("操作成功！");
    }

    /**
     * 获取群公告 - 分页
     *
     * @param emChatId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取群公告 - 分页", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getAnnounceMent")
    @ResponseBody
    public RespObj getAnnounceMent(String emChatId,
                                   @RequestParam(defaultValue = "1", required = false) int page,
                                   @RequestParam(defaultValue = "10", required = false) int pageSize) {
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        GroupDTO groupDTO = groupService.findById(groupId,getUserId());
        if (groupDTO.isBindCommunity()) { //有社区
            ObjectId communityId = new ObjectId(groupDTO.getCommunityId());
            PageModel<CommunityDetailDTO> pageModel = communityService.getMessages(communityId, page,
                    pageSize, CommunityDetailType.ANNOUNCEMENT, getUserId(), false);
            return RespObj.SUCCESS(pageModel);
        }
        return RespObj.SUCCESS(groupNoticeService.getGroupAnnounceByMessage(groupId, page, pageSize));
    }


    /**
     * 发布群公告
     *
     * @param emChatId
     * @param content
     * @param images
     * @return
     */
    @ApiOperation(value = "发布群公告", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/setAnnounceMent")
    @ResponseBody
    public RespObj setAnnounceMent(String emChatId,
                                   @RequestParam(defaultValue = "", required = false) String title,
                                   @RequestParam(defaultValue = "", required = false) String content,
                                   @RequestParam(defaultValue = "", required = false) String images) {
        ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
        if (groupId == null) {
            return RespObj.FAILD("群组不存在");
        }
        ObjectId userId = getUserId();
        if (!memberService.isManager(groupId, userId)) {
            return RespObj.FAILD("对不起，您没有权限");
        }
        GroupDTO groupDTO = groupService.findById(groupId,userId);
        if (groupDTO.isBindCommunity()) { //有社区
            CommunityMessage message = new CommunityMessage();
            message.setContent(content);
            List<Attachement> attachements = new ArrayList<Attachement>();
            if (StringUtils.isNotBlank(images)) {
                String[] imageList = images.split(",");

                for (String imageStr : imageList) {
                    Attachement attachement = new Attachement();
                    attachement.setUrl(imageStr);
                    attachements.add(attachement);
                }
                message.setImages(attachements);
            }
            message.setType(CommunityDetailType.ANNOUNCEMENT.getType());
            message.setCommunityId(groupDTO.getCommunityId());
            message.setTitle(title);
            try {
                communityService.saveMessage(userId, message);
                return RespObj.SUCCESS("发布成功");
            } catch (Exception e) {
                return RespObj.SUCCESS("发布失败");
            }
        }

        List<String> imageArray = new ArrayList<String>();
        if (StringUtils.isNotBlank(images)) {
            String[] imageList = images.split(",");
            Collections.addAll(imageArray, imageList);
        }
        groupNoticeService.save(groupId, title, content, getUserId(), imageArray);
        return RespObj.SUCCESS("发布成功");
    }
    @ApiOperation(value = "updateHeadImage", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/updateHeadImage")
    @SessionNeedless
    @ResponseBody
    public RespObj updateHeadImage(@ObjectIdType ObjectId groupId) {
        try {
            groupService.asyncUpdateHeadImage(groupId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalParamException e) {
            e.printStackTrace();
        }
        return RespObj.SUCCESS;
    }

    /**
     * 获取未读消息个数
     *
     * @return
     */
    @ApiOperation(value = "获取未读消息个数", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/offlineMsgCount")
    @ResponseBody
    @SessionNeedless
    public RespObj msgCount() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (getUserId() == null) {
            map.put("offlineCount", 0);
            return RespObj.SUCCESS(map);
        }
        ObjectId userId = getUserId();
        int msgCount = EaseMobAPI.getOfflineMsgCount(userId.toString());
        map.put("offlineCount", msgCount);
        return RespObj.SUCCESS(map);
    }


    /**
     * 获取孩子的群组列表
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取孩子的群组列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getChildrenGroups")
    @ResponseBody
    public RespObj getChildrenGroups(@ObjectIdType ObjectId userId){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<GroupDTO> groupDTOs = groupService.getChildrenGroups(userId);
        respObj.setMessage(groupDTOs);
        return respObj;
    }


    @ApiOperation(value = "获取孩子的群组列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/getAdrress")
    @ResponseBody
    public RespObj getAdrress(){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Object obj = groupService.getAddress();
        respObj.setMessage(obj);
        return respObj;
    }


    @ApiOperation(value = "获取孩子的群组列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/sendTestMessage")
    @ResponseBody
    public RespObj sendTestMessage(){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            boolean k=groupService.sendTestMessage();
            respObj.setCode(Constant.SUCCESS_CODE);
            respObj.setMessage(k);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

}
