package com.fulaan.controller;

import com.easemob.server.EaseMobAPI;
import com.fulaan.dto.CommunityDTO;
import com.fulaan.dto.CommunityDetailDTO;
import com.fulaan.dto.GroupDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.pojo.*;
import com.fulaan.service.*;
import com.fulaan.user.service.UserService;
import com.fulaan.util.CommunityDetailType;
import com.fulaan.util.ImageUtils;
import com.fulaan_old.friendscircle.service.FriendService;
import com.pojo.user.AvatarType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.docx4j.wml.P;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jerry on 2016/10/31.
 * GroupController
 */
@Controller
@RequestMapping("/group")
public class GroupController extends BaseController {

  @Autowired
  private MemberService memberService;
  @Autowired
  private GroupService groupService;
  @Autowired
  private FriendService friendService;
  @Autowired
  private UserService userService;
  @Autowired
  private GroupAnnounceService groupAnnounceService;
  @Autowired
  private CommunityService communityService;
  @Autowired
  private EmService emService;


  /**
   * 创建群聊
   * 1.创建环信群聊
   * 2.创建群聊组
   *
   * @param request
   * @param userIds
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/create")
  @ResponseBody
  public RespObj createGroup(HttpServletRequest request,
                             @RequestParam(defaultValue = "", required = false) String userIds) throws Exception {
    ObjectId owerId = getUserId();
    List<ObjectId> userList = new ArrayList<ObjectId>();

    String[] users = userIds.split(",");
    for(String user:users) {
      if(ObjectId.isValid(user)){
        userList.add(new ObjectId(user));
      }
    }

    if(userList.contains(owerId)) {
      userList.remove(owerId);
    }

    //创建环信群聊
    String emChatId = emService.createEmGroup(owerId);
    if(emChatId == null) {
      return RespObj.FAILD("环信创建失败");
    }
    //创建组
    ObjectId groupId = groupService.createGroupWithoutCommunity(owerId, emChatId);

    groupService.addMembers(groupId, userList);

    for(ObjectId userId:userList) {
      emService.addUserToGroup(emChatId,userId);
    }

    groupService.updateHeadImage(groupId);

    groupService.updateGroupNameByMember(groupId);

    return RespObj.SUCCESS(groupService.findByObjectId(groupId));
  }

  /**
   * 获取群聊详情
   *
   * @param id
   * @return
   */
  @RequestMapping(value = "/{id}")
  @ResponseBody
  public RespObj detail(@PathVariable String id) {
    ObjectId groupId = groupService.getGroupIdByChatId(id);
    if (groupId == null) {
      return RespObj.FAILD("群组不存在");
    }
    GroupDTO groupDTO = groupService.findByObjectId(groupId);

    if (groupDTO.isBindCommunity()) {
      CommunityDTO communityDTO = communityService.getByGroupId(groupDTO.getEmChatId());
      groupDTO.setSearchId(communityDTO.getSearchId());
      groupDTO.setHeadImage(communityDTO.getLogo());
      groupDTO.setName(communityDTO.getName());
    }

    MemberDTO mine = memberService.getUser(groupId, getUserId());
    groupDTO.setCount(memberService.countMember(groupId));
    groupDTO.setMine(mine);

    if (groupDTO.isBindCommunity()) {
      ObjectId communityId = new ObjectId(groupDTO.getCommunityId());
      CommunityDetailDTO groupAnnounceDTO = communityService.getLatestAnnouncement(communityId);
      groupDTO.setCurAnnounceMent(groupAnnounceDTO);
    } else {
      GroupAnnounceDTO groupAnnounceDTO = groupAnnounceService.getEarlyAnnounce(groupId);
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
  @RequestMapping("/listEmchat")
  @ResponseBody
  public RespObj listEmchat(String emChatIds) {
    String[] emChatList = emChatIds.split(",");
    List<GroupDTO> list = new ArrayList<GroupDTO>();
    for (String emChatId : emChatList) {
      ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
      if (groupId == null) continue;
      try {
        GroupDTO groupDTO = groupService.findByObjectId(groupId);
        MemberDTO mine = memberService.getUser(groupId, getUserId());
        groupDTO.setCount(memberService.countMember(groupId));
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
  @RequestMapping("/allMembers")
  @ResponseBody
  public RespObj getMembers(String emChatId) {
    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
    return RespObj.SUCCESS(memberService.getAllGroupMembers(groupId));
  }

  /**
   * 群主邀请人进群
   *
   * @param emChatId 环信id
   * @param userIds
   * @return
   */
  @RequestMapping("/inviteMember")
  @ResponseBody
  public RespObj inviteMember(String emChatId,
                              String userIds) throws IOException, IllegalParamException {

    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
    GroupDTO groupDTO = groupService.findByObjectId(groupId);
    String[] userList = userIds.split(",");
    List<String> userLists = new ArrayList<String>();
    Collections.addAll(userLists, userList);
    groupService.inviteMembers(emChatId, userLists);

    //更新群聊头像
    groupService.updateHeadImage(groupId);

    if (groupDTO.getIsM() == 0) {
      groupService.updateGroupNameByMember(new ObjectId(groupDTO.getId()));
    }
    return RespObj.SUCCESS("操作成功");
  }

  @RequestMapping("/join")
  @ResponseBody
  public RespObj join(String emChatId) throws IOException, IllegalParamException {

    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
    GroupDTO groupDTO = groupService.findByObjectId(groupId);
    List<String> userLists = new ArrayList<String>();
    userLists.add(getUserId().toString());
    groupService.inviteMembers(emChatId, userLists);

    //更新群聊头像
    groupService.updateHeadImage(groupId);

    if (groupDTO.getIsM() == 0) {
      groupService.updateGroupNameByMember(new ObjectId(groupDTO.getId()));
    }
    return RespObj.SUCCESS("操作成功");
  }



  /**
   * 群主踢人
   *
   * @param emChatId
   * @param userIds
   * @return
   * @throws IOException
   * @throws IllegalParamException
   */
  @RequestMapping("/quitMember")
  @ResponseBody
  public RespObj quitMember(String emChatId, String userIds) throws IOException, IllegalParamException {
    if (StringUtils.isBlank(userIds)) {
      return RespObj.FAILD("userIds 为空 !");
    }
    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
    GroupDTO groupDTO = groupService.findByObjectId(groupId);

    ObjectId userId = getUserId();
    if (!memberService.isManager(groupId, userId)) {
      return RespObj.FAILD("您没有这个权限");
    }

    String[] userList = userIds.split(",");

    for (String userEntryId : userList) {

      groupService.quitMember(groupId, new ObjectId(userEntryId));

      if (groupDTO.isBindCommunity()) {
        if (StringUtils.isNotBlank(groupDTO.getCommunityId())) {
          communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), new ObjectId(userEntryId));
        }
      }
    }

    if (!groupDTO.isBindCommunity()) {
      groupService.updateHeadImage(groupId);
      if (groupDTO.getIsM() == 0) {
        groupService.updateGroupNameByMember(new ObjectId(groupDTO.getId()));
      }
    }
    return RespObj.SUCCESS("退出成功");
  }

  /**
   * 自己退出群聊
   *
   * @param request
   * @param emChatId
   * @return
   */
  @RequestMapping("/quit")
  @ResponseBody
  public RespObj quitGroup(String emChatId) throws IOException, IllegalParamException {

    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
    GroupDTO groupDTO = groupService.findByObjectId(groupId);

    ObjectId userId = getUserId();

    if (memberService.isHead(groupId, userId)) {

      List<MemberDTO> memberDTOs = memberService.getMembers(groupId, 2);
      //转让
      if (memberDTOs.size() == 2) {
        MemberDTO memberDTO = memberDTOs.get(1);
        EaseMobAPI.transferChatGroupOwner(emChatId, memberDTO.getUserId());
        memberService.setHead(groupId, new ObjectId(memberDTO.getUserId()));
        groupService.transferOwerId(groupId, new ObjectId(memberDTO.getUserId()));
        groupService.quitMember(groupId, userId);

        if (groupDTO.isBindCommunity()) {
          if (StringUtils.isNotBlank(groupDTO.getCommunityId())) {
            communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), userId);
          }
        }
      }

      if (memberDTOs.size() == 1) {//只有一个人
        EaseMobAPI.deleteChatGroup(emChatId);
        groupService.quitMember(groupId, userId);
        groupService.deleteGroup(groupId);

        if (groupDTO.isBindCommunity()) {
          if (StringUtils.isNotBlank(groupDTO.getCommunityId())) {
            communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), userId);

            communityService.deleteCommunity(new ObjectId(groupDTO.getCommunityId()));
          }
        }
      }

    } else {
      groupService.quitMember(groupId, userId);

      if (groupDTO.isBindCommunity()) {
        if (StringUtils.isNotBlank(groupDTO.getCommunityId())) {
          communityService.pullFromUser(new ObjectId(groupDTO.getCommunityId()), userId);
        }
      }
    }

    groupService.updateHeadImage(groupId);
    if (groupDTO.getIsM() == 0) {
      groupService.updateGroupNameByMember(new ObjectId(groupDTO.getId()));
    }
    return RespObj.SUCCESS("退出成功");
  }

  /**
   * 获取不在群聊的好友列表
   *
   * @param emChatId
   * @return
   */
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
  @RequestMapping("/getOnQuitList")
  @ResponseBody
  public RespObj getQuitList(String emChatId) {
    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
    List<ObjectId> userIds = memberService.getQuitList(groupId);
    List<User> users = new ArrayList<User>();
    List<UserEntry> userEntries = userService.getUserByList(userIds);
    for (UserEntry userEntry : userEntries) {
      User user = new User();
      user.setUserId(userEntry.getID().toString());
      user.setUserName(userEntry.getUserName());
      user.setNickName(userEntry.getNickName());
      user.setAvator(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
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
  @RequestMapping("/updateGroupName")
  @ResponseBody
  public RespObj updateGroupName(String emChatId,
                                 String groupName) {

    ObjectId userId = getUserId();
    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);
    if (!memberService.isManager(groupId, userId)) {
      return RespObj.FAILD("对不起，您没有此权限");
    }

    GroupDTO groupDTO = groupService.findByObjectId(groupId);
    if (groupDTO.isBindCommunity()) {
      communityService.updateCommunityName(new ObjectId(groupDTO.getCommunityId()), groupName);
    }
    groupService.updateGroupName(groupId, groupName);
    return RespObj.SUCCESS("操作成功");
  }

  /**
   * 更新在群组的昵称
   *
   * @param emChatId
   * @param nickName
   * @return
   */
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

    List<ObjectId> memberIds = getMembersId(userIds);

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

  private List<ObjectId> getMembersId(String memberId) {
    String[] members = memberId.split(",");
    List<ObjectId> objectIds = new ArrayList<ObjectId>();
    for (String item : members) {
      objectIds.add(new ObjectId(item));
    }
    return objectIds;
  }

  /**
   * 取消 副群主
   *
   * @param emChatId
   * @param userIds
   * @return
   */
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
  @RequestMapping("/getAnnounceMent")
  @ResponseBody
  public RespObj getAnnounceMent(String emChatId,
                                 @RequestParam(defaultValue = "1", required = false) int page,
                                 @RequestParam(defaultValue = "10", required = false) int pageSize) {

    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);

    GroupDTO groupDTO = groupService.findByObjectId(groupId);

    if (groupDTO.isBindCommunity()) { //有社区

      ObjectId communityId = new ObjectId(groupDTO.getCommunityId());
      PageModel<CommunityDetailDTO> pageModel = communityService.getMessages(communityId, page, pageSize, -1, CommunityDetailType.ANNOUNCEMENT.getType());
      return RespObj.SUCCESS(pageModel);
    }
    return RespObj.SUCCESS(groupAnnounceService.getGroupAnnounceByMessage(groupId, page, pageSize));
  }


  /**
   * 发布群公告
   *
   * @param emChatId
   * @param content
   * @param images
   * @return
   */
  @RequestMapping("/setAnnounceMent")
  @ResponseBody
  public RespObj setAnnounceMent(String emChatId,
                                 @RequestParam(defaultValue = "", required = false) String title,
                                 String content,
                                 @RequestParam(defaultValue = "", required = false) String images) {

    ObjectId groupId = groupService.getGroupIdByChatId(emChatId);

    if (groupId == null) {
      return RespObj.FAILD("群组不存在");
    }
    ObjectId userId = getUserId();
    if (!memberService.isManager(groupId, userId)) {
      return RespObj.FAILD("对不起，您没有权限");
    }

    GroupDTO groupDTO = groupService.findByObjectId(groupId);

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
      communityService.saveMessage(userId, message);
      return RespObj.SUCCESS("发布成功");
    }

    List<String> imageArray = new ArrayList<String>();
    if (StringUtils.isNotBlank(images)) {
      String[] imageList = images.split(",");
      for (String imageStr : imageList) {
        imageArray.add(imageStr);
      }
    }
    groupAnnounceService.save(groupId, title, content, getUserId(), imageArray);
    return RespObj.SUCCESS("发布成功");
  }


}
