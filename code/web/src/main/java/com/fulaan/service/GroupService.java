package com.fulaan.service;

import com.db.user.UserDao;
import com.easemob.server.EaseMobAPI;
import com.easemob.server.comm.wrapper.ResponseWrapper;
import com.fulaan.dao.GroupDao;
import com.fulaan.dao.MemberDao;
import com.fulaan.dto.GroupDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.util.ImageUtils;
import com.fulaan.util.QRUtils;
import com.pojo.fcommunity.GroupEntry;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.QiniuFileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 2016/11/1.
 */
@Service
public class GroupService {

  @Autowired
  private MemberService memberService;
  @Autowired
  private CommunityService communityService;

  private GroupDao groupDao = new GroupDao();
  private MemberDao memberDao = new MemberDao();
  private UserDao userDao = new UserDao();

  public ObjectId createGroupWithCommunity(ObjectId commid, ObjectId owerId, String emChatId, String name,
                                           String desc, String qrUrl) throws Exception {
    ObjectId groupId = new ObjectId();
    GroupEntry group = new GroupEntry(groupId, commid, owerId, qrUrl, emChatId, name, desc);
    groupDao.add(group);
    memberService.saveMember(owerId, groupId, 2);
    return groupId;
  }

  public ObjectId createGroupWithoutCommunity(ObjectId owerId, String emChatId) {
    ObjectId groupId = new ObjectId();
    String qrUrl = QRUtils.getGroupQrUrl(groupId);
    GroupEntry group = new GroupEntry(groupId, owerId, qrUrl, emChatId);
    groupDao.add(group);
    memberService.saveMember(owerId, groupId, 2);
    return groupId;
  }

  /**
   * 获取讨论组成员列表
   *
   * @param chatId
   * @return
   */
  public ObjectId getGroupIdByChatId(String chatId) {
    return groupDao.getGroupIdByEmchatId(chatId);
  }

  public ObjectId getCommunityIdByGroupId(ObjectId groupId) {
    return groupDao.getCommunityIdByGroupId(groupId);
  }

  /**
   * findObjectId
   *
   * @param groupId
   * @return
   */
  public GroupDTO findByObjectId(ObjectId groupId) {
    GroupEntry groupEntry = groupDao.findByObjectId(groupId);
    List<MemberDTO> managers = memberService.getManagers(groupId);
    List<MemberDTO> members = memberService.getMembers(groupId, 20);
    return new GroupDTO(groupEntry, members, managers);
  }

  /**
   * 根据id获取List<GroupDTO>
   *
   * @param ids
   * @return
   */
  private List<GroupDTO> findByList(List<ObjectId> ids) {
    List<GroupDTO> groups = new ArrayList<GroupDTO>();
    List<GroupEntry> entrys = groupDao.findByIdList(ids);
    for (GroupEntry entry : entrys) {
      groups.add(new GroupDTO(entry));
    }
    return groups;
  }

  /**
   * 获取群聊头像
   *
   * @param groupId
   * @return
   */
  public String getHeadImage(ObjectId groupId) {
    return groupDao.getHeadImage(groupId);
  }

  /**
   * 更新群聊头像
   *
   * @param groupId
   * @return
   * @throws IOException
   * @throws IllegalParamException
   */
  public void updateHeadImage(ObjectId groupId) throws IOException, IllegalParamException {
    List<MemberDTO> members = memberService.getMembers(groupId, 4);
    List<String> images = new ArrayList<String>();
    for (MemberDTO memberDTO : members) {
      if (StringUtils.isNotBlank(memberDTO.getAvator())) {
        images.add(memberDTO.getAvator());
      }
    }
    BufferedImage bufferedImage = ImageUtils.getCombinationOfhead(images);
    ObjectId fileKey = new ObjectId();
    File outFile = File.createTempFile(fileKey.toString(), ".jpg");
    ImageIO.write(bufferedImage, "JPG", outFile);

    QiniuFileUtils.uploadFile(fileKey.toString() + ".jpg", new FileInputStream(outFile), QiniuFileUtils.TYPE_IMAGE);
    outFile.delete();
    String url = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey.toString() + ".jpg");
    groupDao.setHeadImage(groupId, url);
  }

  /**
   * 邀请别人进入群聊
   *
   * @param emChatId
   * @param userLists
   */
  public void inviteMembers(String emChatId, List<String> userLists) {
    ObjectId groupId = getGroupIdByChatId(emChatId);
    for (String userId : userLists) {
      if (ObjectId.isValid(userId)) {
        inviteMember(groupId, emChatId, new ObjectId(userId));
      }
    }
  }

  public void addMembers(ObjectId groupId, List<ObjectId> userList) {
    for (ObjectId userId : userList) {
      if (!isGroupMember(groupId, userId)) {
        //添加成员
        if (isBeforeMember(groupId, userId)) {
          memberService.updateMember(groupId, userId, 0);
        } else {
          memberService.saveMember(userId, groupId);
        }
      }
    }
  }

  /**
   * 邀请别人进群聊
   *
   * @param groupId
   * @param emChatId
   * @param userId
   */
  public void inviteMember(ObjectId groupId, String emChatId, ObjectId userId) {
    if (!isGroupMember(groupId, userId)) { //不是群聊成员
      ResponseWrapper responseWrapper = EaseMobAPI.addSingleUserToChatGroup(emChatId, userId.toString());
      if (responseWrapper.getResponseStatus() == 200) {
        //添加成员
        if (isBeforeMember(groupId, userId)) {
          memberService.updateMember(groupId, userId, 0);
        } else {
          memberService.saveMember(userId, groupId);
        }
      }
    }
  }


  public void joinGroup(ObjectId groupId, ObjectId userId) {
    String emChatId = getEmchatIdByGroupId(groupId);
    if (!isGroupMember(groupId, userId)) { //不是群聊成员
      if (inviteToEmGroup(emChatId, userId)) {
        if (isBeforeMember(groupId, userId)) {
          memberService.updateMember(groupId, userId, 0);
        } else {
          memberService.saveMember(userId, groupId);
        }
      }
    }
  }

  /**
   * 加入组 - 但是不加入环信
   *
   * @param groupId
   * @param userId
   */
  public void joinGroupWithoutEm(ObjectId groupId, ObjectId userId) {
    String emChatId = getEmchatIdByGroupId(groupId);
    if (!isGroupMember(groupId, userId)) { //不是群聊成员
      memberService.saveMember(userId, groupId);
    }
  }

  /**
   * 退出群组
   * 逻辑
   * 1.判断是否是群成员
   * 2.从环信群聊中剔除
   * 3.在成员表中删除
   *
   * @param groupId
   * @param userId
   */
  public void quitMember(ObjectId groupId, ObjectId userId) {
    String emChatId = getEmchatIdByGroupId(groupId);
    if (quitToEmGroup(emChatId, userId)) {
      //删除成员
      memberService.deleteMember(groupId, userId);
    }
  }

  /**
   * 更新群聊名称-按照用户名,的方式拼接
   *
   * @param groupId
   * @param emId
   */
  public void updateGroupNameByMember(ObjectId groupId) {
    List<MemberDTO> members = memberService.getMembers(groupId, 3);
    String name = "";
    for (MemberDTO member : members) {
      name += member.getNickName() + ",";
    }
    if (name.contains(",")) {
      name = name.substring(0, name.lastIndexOf(","));
    }
    groupDao.updateGroupName(groupId, name);
  }

  /**
   * 更新 - 群聊名称
   *
   * @param groupId
   * @param groupName
   */
  public void updateGroupName(ObjectId groupId, String groupName) {
    groupDao.updateIsM(groupId, 1);
    groupDao.updateGroupName(groupId, groupName);
  }

  /**
   * 是否 - 是群聊成员
   *
   * @param groupId
   * @param userId
   * @return
   */
  public boolean isGroupMember(ObjectId groupId, ObjectId userId) {
    return memberDao.isMember(groupId, userId);
  }

  /**
   * 是否以前加入过
   * @param groupId
   * @param userId
   * @return
   */
  public boolean isBeforeMember(ObjectId groupId, ObjectId userId) {
    return memberDao.isBeforeMember(groupId, userId);
  }

  /**
   * 获取群聊名称
   *
   * @param groupId
   * @return
   */
  public String getGroupName(ObjectId groupId) {
    return groupDao.getGroupName(groupId);
  }

  /**
   * 根据群组id获取环信id
   *
   * @param groupId
   * @return
   */
  public String getEmchatIdByGroupId(ObjectId groupId) {
    return groupDao.getEmchatIdByGroupId(groupId);
  }

  /**
   * 群主 - 邀请人加入环信群聊
   *
   * @param emChatId
   * @param userId
   * @return
   */
  public boolean inviteToEmGroup(String emChatId, ObjectId userId) {
    ResponseWrapper responseWrapper = EaseMobAPI.addSingleUserToChatGroup(emChatId, userId.toString());
    if (responseWrapper.getResponseStatus() == 200) {
      return true;
    }
    return false;
  }

  /**
   * 群主 - 从环信群聊中剔除
   *
   * @param emChatId
   * @param userId
   * @return
   */
  public boolean quitToEmGroup(String emChatId, ObjectId userId) {
    ResponseWrapper responseWrapper = EaseMobAPI.removeSingleUserFromChatGroup(emChatId, userId.toString());
    if (responseWrapper.getResponseStatus() == 200) {
      return true;
    }
    return false;
  }

  /**
   * 群组转让
   *
   * @param groupId
   * @param owerId
   */
  public void transferOwerId(ObjectId groupId, ObjectId owerId) {
    groupDao.setOwerId(groupId, owerId);
  }

  /**
   * 删除群组
   *
   * @param groupId
   */
  public void deleteGroup(ObjectId groupId) {
    groupDao.delete(groupId);
  }

}
