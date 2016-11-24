package com.fulaan.service;

import com.db.user.UserDao;
import com.fulaan.dao.*;
import com.fulaan.dto.CommunityDTO;
import com.fulaan.dto.CommunityDetailDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.dto.PartInContentDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.CommunityMessage;
import com.fulaan.pojo.PageModel;
import com.fulaan.pojo.ProductModel;
import com.fulaan.user.service.UserService;
import com.fulaan.util.CommunityDetailType;
import com.fulaan.util.DateUtils;
import com.fulaan.util.QRUtils;
import com.fulaan_old.friendscircle.service.FriendApplyService;
import com.fulaan_old.friendscircle.service.FriendService;
import com.pojo.activity.FriendApplyEntry;
import com.pojo.fcommunity.*;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jerry on 2016/10/24.
 */
@Service
public class CommunityService {

  @Autowired
  private CommunityDao communityDao;
  @Autowired
  private CommunityDetailDao communityDetailDao;
  @Autowired
  private UserService userService;
  @Autowired
  private MemberService memberService;
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private FriendService friendService;
  @Autowired
  private RemarkDao remarkDao;
  @Autowired
  private GroupService groupService;
  @Autowired
  private FriendApplyService friendApplyService;
  @Autowired
  private EmService emService;

  private UserDao userDao = new UserDao();
  private PartInContentDao partInContentDao = new PartInContentDao();
  private MineCommunityDao mineCommunityDao = new MineCommunityDao();
  private CommunitySeqDao seqDao = new CommunitySeqDao();

  /**
   * 创建社区
   * <p>
   * 1.创建环信组
   * 2.创建讨论组
   * 3.创建社区
   *
   * @param communityId
   * @param name
   * @param desc
   * @param logo
   * @param qrUrl
   */
  public ObjectId createCommunity(ObjectId communityId, ObjectId userId, String name, String desc, String logo, String qrUrl, String seqId, int open) throws Exception {

    String emChatId = emService.createEmGroup(userId);
    ObjectId groupId = groupService.createGroupWithCommunity(communityId, userId, emChatId, name, desc, qrUrl);
    groupService.updateHeadImage(groupId);
    CommunityEntry entry = new CommunityEntry(communityId, seqId, groupId, emChatId, name, logo, desc, qrUrl, open, userId);
    communityDao.save(entry);
    return communityId;
  }

  /**
   * 生成名字
   * @param userId
   * @return
   */
  public String generateCommunityNames(ObjectId userId) {
    String names = "";
    List<MineCommunityEntry> mineCommunityEntries = mineCommunityDao.findByCount(userId, 3);
    for (MineCommunityEntry mineCommunityEntry : mineCommunityEntries) {
      ObjectId communityId = mineCommunityEntry.getCommunityId();
      CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
      names += communityEntry.getCommunityName() + ",";
    }
    if (names.length() > 1) {
      return names.substring(0, names.lastIndexOf(","));
    }
    return names;
  }

  /**
   * 根据ObjectId 获取社区DTO
   *
   * @param communityId
   * @return
   */
  public CommunityDTO findByObjectId(ObjectId communityId) {
    CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
    if (communityEntry == null) return null;
    return new CommunityDTO(communityEntry);
  }

  /**
   * 根据ObjectId 获取详情
   *
   * @param communityDetailId
   * @return
   */
  public CommunityDetailDTO findDetailByObjectId(ObjectId communityDetailId) {
    CommunityDetailEntry communityDetailEntry = communityDetailDao.findByObjectId(communityDetailId);
    List<PartInContentEntry> partInContentEntries = partInContentDao.getPartInContent(communityDetailEntry.getID(), -1, 1, 10);

    CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(communityDetailEntry, partInContentEntries);
    ObjectId userId = communityDetailEntry.getCommunityUserId();
    UserEntry userEntry = userDao.findByObjectId(userId);

    if (StringUtils.isNotBlank(userEntry.getNickName())) {
      communityDetailDTO.setNickName(userEntry.getNickName());
    } else {
      communityDetailDTO.setNickName(userEntry.getUserName());
    }

    communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
    return communityDetailDTO;
  }

  public void pushToUser(ObjectId communityId, ObjectId uid,int prioity) {
    MineCommunityEntry mineCommunityEntry = new MineCommunityEntry(uid, communityId,prioity);
    mineCommunityDao.save(mineCommunityEntry);
  }

  public void pullFromUser(ObjectId communityId, ObjectId uid) {
    mineCommunityDao.delete(communityId, uid);
  }

  public String getSeq() {
    return String.valueOf(seqDao.getRandom().getSeq());
  }

  public void generateSeq() {

    for (int i = 100000; i < 150000; i++) {
      CommunitySeqEntry entry = new CommunitySeqEntry(1, i);
      seqDao.save(entry);
    }
  }

  /**
   * 获取我的社团
   *
   * @param uid
   */
  public List<CommunityDTO> getCommunitys(ObjectId uid,int page,int pageSize) {

    List<MineCommunityEntry> allMineCommunitys = mineCommunityDao.findAll(uid,page,pageSize);
    List<ObjectId> myCommunitys = new ArrayList<ObjectId>();
    for (MineCommunityEntry mineCommunityEntry : allMineCommunitys) {
      myCommunitys.add(mineCommunityEntry.getCommunityId());
    }
    List<CommunityDTO> list = new ArrayList<CommunityDTO>();
    if (allMineCommunitys.size() > 0) {
      List<CommunityEntry> entries = communityDao.getCommunitysByIds(myCommunitys);
      for (CommunityEntry e : entries) {
        list.add(new CommunityDTO(e));
      }
    }
    return list;
  }

  public int countMyCommunity(ObjectId userId){
    return mineCommunityDao.count(userId);
  }

  /**
   * 更新社团
   *
   * @param cid  社团id
   * @param name 名称
   * @param desc 简介
   * @param logo 图标
   */
  public void updateCommunity(ObjectId cid, String name, String desc, String logo, int open) {
    communityDao.updateCommunityOpen(cid, open);
    if (StringUtils.isNotBlank(name)) {
      communityDao.updateCommunityName(cid, name);
    }
    if (StringUtils.isNotBlank(desc)) {
      communityDao.updateCommunityDesc(cid, desc);
    }
    if (StringUtils.isNotBlank(logo)) {
      communityDao.updateCommunityLogo(cid, logo);
    }
  }

  /**
   * 保存
   *
   * @param uid
   * @param message
   */
  public void saveMessage(ObjectId uid, CommunityMessage message) {
    List<AttachmentEntry> attachmentEntries = splitAttachements(message.getAttachements(), uid);
    List<AttachmentEntry> vedios = splitAttachements(message.getVedios(), uid);
    List<AttachmentEntry> images = splitAttachements(message.getImages(), uid);
    CommunityDetailEntry entry = new CommunityDetailEntry(new ObjectId(message.getCommunityId()),
            uid, message.getTitle(), message.getContent(), message.getType(),
            new ArrayList<ObjectId>(), attachmentEntries, vedios, images,
            message.getShareUrl(), message.getShareImage(), message.getShareTitle(), message.getSharePrice());
    communityDetailDao.save(entry);
  }

  /**
   * 获取最新消息
   *
   * @param communityIds
   * @param type
   * @param page
   * @param pageSize
   * @param order
   * @param operation    1:当前社区 2:我的社区的信息
   * @return
   */

  public List<CommunityDetailDTO> getNews(List<ObjectId> communityIds, CommunityDetailType type, int page, int pageSize,
                                          int order, ObjectId userId, int operation) {
    CommunityEntry communityEntry = new CommunityEntry();
    if (null != communityIds && communityIds.size() > 0 && operation == 1) {
      communityEntry = communityDao.findByObjectId(communityIds.get(0));
    }
    List<CommunityDetailEntry> communitys = communityDetailDao.getNewsByType(communityIds, type, page, pageSize, order);

    int unreadCount = 0;
    if (userId != null) {
      int totalCount = communityDetailDao.count(communityIds.get(0), type.getType());
      int readCount = communityDetailDao.countRead(type.getType(), communityIds.get(0), userId);
      unreadCount = totalCount - readCount;
    }

    List<CommunityDetailDTO> dtos = new ArrayList<CommunityDetailDTO>();
    List<ObjectId> objectIds = new ArrayList<ObjectId>();
    for (CommunityDetailEntry entry : communitys) {
      objectIds.add(entry.getCommunityUserId());
    }
    Map<ObjectId, UserEntry> map = userService.getUserEntryMap(objectIds, Constant.FIELDS);
    for (CommunityDetailEntry entry : communitys) {

      UserEntry userEntry = map.get(entry.getCommunityUserId());
      CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
      //判断是否为学习用品
      if (type.getType() == CommunityDetailType.MATERIALS.getType()) {
        List<PartInContentDTO> partInContentDTOs = new ArrayList<PartInContentDTO>();
        List<PartInContentEntry> partInContentEntries = partInContentDao.getPartInContent(entry.getID(), 6, 1, 1);
        for (PartInContentEntry partEntry : partInContentEntries) {
          PartInContentDTO dto = new PartInContentDTO(partEntry);
          UserEntry user = userService.find(partEntry.getUserId());
          dto.setUserName(user.getUserName());
          dto.setAvator(AvatarUtils.getAvatar(user.getAvatar(), AvatarType.MIN_AVATAR.getType()));
          dto.setNickName(StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName());
          dto.setTime(DateUtils.timeStampToStr(partEntry.getID().getTimestamp()));
          partInContentDTOs.add(dto);
        }
        communityDetailDTO.setPartList(partInContentDTOs);
      }
      if (null != communityEntry.getOwerID()) {
        communityDetailDTO.setCommunityName(communityEntry.getCommunityName());
      } else {
        communityEntry = communityDao.findByObjectId(new ObjectId(entry.getCommunityId()));
        communityDetailDTO.setCommunityName(communityEntry.getCommunityName());
      }

      //设置权限
      setRoleStr(communityDetailDTO, communityEntry, entry.getCommunityUserId());
      int totalCount = partInContentDao.countPartPartInContent(entry.getID());
      communityDetailDTO.setPartIncotentCount(totalCount);
      communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
      if (StringUtils.isNotBlank(userEntry.getNickName())) {
        communityDetailDTO.setNickName(userEntry.getNickName());
      } else {
        communityDetailDTO.setNickName(userEntry.getUserName());
      }
      communityDetailDTO.setUnReadCount(unreadCount);
      communityDetailDTO.setPartInCount(communityDetailDTO.getPartInList().size());
      dtos.add(communityDetailDTO);
    }
    return dtos;
  }

  public void setRoleStr(CommunityDetailDTO communityDetailDTO, CommunityEntry communityEntry, ObjectId userId) {
    String groupId = communityEntry.getGroupId();
    if (userId.equals(communityEntry.getOwerID())) {
      communityDetailDTO.setRoleStr("社长");
    } else {
      if (memberService.isManager(new ObjectId(groupId), userId)) {
        communityDetailDTO.setRoleStr("副社长");
      } else {
        communityDetailDTO.setRoleStr("社区成员");
      }
    }
  }

  /**
   * 获取Messages
   *
   * @param communityId
   * @param page
   * @param pageSize
   * @param order
   * @return
   */
  public PageModel<CommunityDetailDTO> getMessages(ObjectId communityId, int page, int pageSize, int order) {
    PageModel<CommunityDetailDTO> pageModel = new PageModel<CommunityDetailDTO>();
    List<CommunityDetailEntry> entries = communityDetailDao.getDetailsByCommunityId(communityId, page, pageSize, order);
    List<CommunityDetailDTO> dtos = new ArrayList<CommunityDetailDTO>();
    for (CommunityDetailEntry entry : entries) {
      dtos.add(new CommunityDetailDTO(entry));
    }
    int counts = communityDetailDao.count(communityId);
    int totalPages = (int) Math.ceil((counts / (pageSize * 1.0)));
    if (page > totalPages) {
      page = totalPages;
    }
    pageModel.setPage(page);
    pageModel.setPageSize(pageSize);
    pageModel.setTotalCount(counts);
    pageModel.setTotalPages(totalPages);
    pageModel.setResult(dtos);
    return pageModel;
  }

  /**
   * 获取我的玩伴列表
   */
  public List<MemberDTO> getMyPartners(ObjectId userId) {
    List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();

    //查出当前用户的信息
    UserEntry userEntry = userService.find(userId);
    List<UserEntry.UserTagEntry> ftags = userEntry.getUserTag();
    //查找伙伴
    List<ObjectId> partners = friendService.getObjectFriends(userId);
    //查出所有成员的信息
    Map<ObjectId, UserEntry> partnerInfo = userService.getUserEntryMap(partners, Constant.FIELDS);

    //查询出被修改备注的信息
    Map<ObjectId, RemarkEntry> remarkEntryMap = remarkDao.find(userId, partners);

    for (ObjectId partner : partners) {
      MemberDTO memberDTO = new MemberDTO();
      memberDTO.setRemarkId(Constant.EMPTY);
      UserEntry userEntry1 = partnerInfo.get(partner);
      List<UserEntry.UserTagEntry> partnerTags = userEntry1.getUserTag();
      if (partnerTags.size() == 0) {
        memberDTO.setTagType(0);
        memberDTO.setTags(new ArrayList<String>());
      } else {
        memberDTO.setTagType(1);
        memberDTO.setTags(getTagList(ftags, partnerTags));
      }
      List<ObjectId> partners1 = friendService.getObjectFriends(partner);
      partners1.retainAll(partners);
      memberDTO.setRoleStr("好友");
      memberDTO.setUserId(partner.toString());
      memberDTO.setAvator(AvatarUtils.getAvatar(userEntry1.getAvatar(), AvatarType.MIN_AVATAR.getType()));
      memberDTO.setNickName(StringUtils.isNotBlank(userEntry1.getNickName()) ? userEntry1.getNickName() : userEntry1.getUserName());
      memberDTO.setPlaymateCount(partners1.size());
      memberDTO.setPlaymate(getPlaymate(partnerInfo, partners1));

      if (null != remarkEntryMap.get(partner)) {
        memberDTO.setNickName(remarkEntryMap.get(partner).getRemark());
        memberDTO.setRemarkId(remarkEntryMap.get(partner).getID().toString());
      }
      memberDTOs.add(memberDTO);
    }
    return memberDTOs;
  }

  /**
   * 获取社区成员列表
   *
   * @param page
   * @param pageSize
   * @param userId
   * @param communityId
   * @return
   */
  public PageModel<MemberDTO> getMemberList(int page, int pageSize, ObjectId userId, ObjectId communityId) {
    PageModel<MemberDTO> pageModel = new PageModel<MemberDTO>();
    List<ObjectId> members = new ArrayList<ObjectId>();
    List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
    ObjectId groupId = getGroupId(communityId);
    List<MemberEntry> memberEntries = memberDao.getMembers(groupId, page, pageSize);
    for (MemberEntry memberEntry : memberEntries) {
      members.add(memberEntry.getUserId());
    }
    //查出当前用户的信息
    UserEntry userEntry = userService.find(userId);
    List<UserEntry.UserTagEntry> ftags = userEntry.getUserTag();
    //查找伙伴
    List<ObjectId> partners = friendService.getObjectFriends(userId);
    //查出所有成员的信息
    Map<ObjectId, UserEntry> partnerInfo = userService.getUserEntryMap(partners, Constant.FIELDS);
    //查询出被修改备注的信息
    Map<ObjectId, RemarkEntry> remarkEntryMap = remarkDao.find(userId, members);

    //查询该用户的所有申请列表
    Map<ObjectId, FriendApplyEntry> friendApplyEntryMap = friendApplyService.getFriendApplyMap(userId, members);
    //查出所有成员的信息
    Map<ObjectId, UserEntry> map = userService.getUserEntryMap(members, Constant.FIELDS);
    for (MemberEntry memberEntry : memberEntries) {
      MemberDTO memberDTO = new MemberDTO(memberEntry);
      memberDTO.setRemarkId(Constant.EMPTY);
      String memberUserId = memberDTO.getUserId();
      if (!memberUserId.equals(userId.toString())) {
        UserEntry userEntry1 = map.get(new ObjectId(memberUserId));
        List<UserEntry.UserTagEntry> tags = userEntry1.getUserTag();
        if (tags.size() == 0) {
          memberDTO.setTagType(0);
          memberDTO.setTags(new ArrayList<String>());
        } else {
          memberDTO.setTagType(1);
          memberDTO.setTags(getTagList(ftags, tags));
        }
        boolean judge = judgePartner(partners, new ObjectId(memberUserId));
        if (judge) {
          memberDTO.setPlaymateFlag(1);
        } else {
          //若该用户不是同伴的话，判断是否申请加为好友
          FriendApplyEntry friendApplyEntry = friendApplyEntryMap.get(new ObjectId(memberUserId));
          if (null != friendApplyEntry) {
            //申请了
            memberDTO.setPlaymateFlag(2);
          } else {
            memberDTO.setPlaymateFlag(0);
          }
        }
        List<ObjectId> partners1 = friendService.getObjectFriends(new ObjectId(memberUserId));
        partners1.retainAll(partners);
        memberDTO.setPlaymateCount(partners1.size());
        memberDTO.setPlaymate(getPlaymate(partnerInfo, partners1));

        if (null != remarkEntryMap.get(new ObjectId(memberUserId))) {
          memberDTO.setNickName(remarkEntryMap.get(new ObjectId(memberUserId)).getRemark());
          memberDTO.setRemarkId(remarkEntryMap.get(new ObjectId(memberUserId)).getID().toString());
        }
        memberDTO.setIsOwner(0);
        memberDTOs.add(memberDTO);
      } else {
        List<String> ownerTags = new ArrayList<String>();
        UserEntry userEntry1 = map.get(new ObjectId(memberUserId));
        List<UserEntry.UserTagEntry> userTagEntries = userEntry1.getUserTag();
        for (UserEntry.UserTagEntry uItem : userTagEntries) {
          if (ownerTags.size() < 2) {
            ownerTags.add(uItem.getTag());
          } else {
            break;
          }
        }
        if (ownerTags.size() > 0) {
          memberDTO.setTagType(1);
        } else {
          memberDTO.setTagType(0);
        }
        memberDTO.setTags(ownerTags);

        memberDTO.setIsOwner(1);
        memberDTOs.add(memberDTO);
      }
    }
    int counts = memberDao.countMember(groupId);
    int totalPages = (int) Math.ceil((counts / (pageSize * 1.0)));
    if (page > totalPages) {
      page = totalPages;
    }
    pageModel.setPage(page);
    pageModel.setPageSize(pageSize);
    pageModel.setTotalCount(counts);
    pageModel.setTotalPages(totalPages);
    pageModel.setResult(memberDTOs);
    return pageModel;
  }

  /**
   * 获取Messages
   *
   * @param communityId
   * @param page
   * @param pageSize
   * @param order
   * @return
   */
  public PageModel<CommunityDetailDTO> getMessages(ObjectId communityId, int page, int pageSize, int order, int type) {
    PageModel<CommunityDetailDTO> pageModel = new PageModel<CommunityDetailDTO>();
    List<CommunityDetailEntry> entries = communityDetailDao.getDetails(communityId, page, pageSize, order, type);
    int counts = communityDetailDao.count(communityId, type);

    CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
    List<CommunityDetailDTO> dtos = new ArrayList<CommunityDetailDTO>();
    List<ObjectId> objectIds = new ArrayList<ObjectId>();
    for (CommunityDetailEntry entry : entries) {
      objectIds.add(entry.getCommunityUserId());
    }
    Map<ObjectId, UserEntry> map = userService.getUserEntryMap(objectIds, Constant.FIELDS);
    for (CommunityDetailEntry entry : entries) {
      UserEntry userEntry = map.get(entry.getCommunityUserId());
      CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
      communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
      if (StringUtils.isNotBlank(userEntry.getNickName())) {
        communityDetailDTO.setNickName(userEntry.getNickName());
      } else {
        communityDetailDTO.setNickName(userEntry.getUserName());
      }

      List<PartInContentDTO> partInContentDTOs = new ArrayList<PartInContentDTO>();
      List<PartInContentEntry> partInContentEntries = partInContentDao.getPartInContent(entry.getID(), -1, 1, 1);
      for (PartInContentEntry partInContentEntry : partInContentEntries) {
        PartInContentDTO partInContentDTO = new PartInContentDTO(partInContentEntry);
        UserEntry userEntry1 = userService.find(partInContentEntry.getUserId());
        partInContentDTO.setUserName(userEntry1.getUserName());
        partInContentDTO.setAvator(AvatarUtils.getAvatar(userEntry1.getAvatar(), AvatarType.MIN_AVATAR.getType()));
        partInContentDTO.setNickName(StringUtils.isNotBlank(userEntry1.getNickName()) ? userEntry1.getNickName() : userEntry1.getUserName());
        partInContentDTO.setTime(DateUtils.timeStampToStr(partInContentEntry.getID().getTimestamp()));
        partInContentDTOs.add(partInContentDTO);
      }
      int totalCount = partInContentDao.countPartPartInContent(entry.getID());
      communityDetailDTO.setPartIncotentCount(totalCount);
      //设置权限
      setRoleStr(communityDetailDTO, communityEntry, entry.getCommunityUserId());
      communityDetailDTO.setPartList(partInContentDTOs);
      dtos.add(communityDetailDTO);
    }


    int totalPages = (int) Math.ceil((counts / (pageSize * 1.0)));
    if (page > totalPages) {
      page = totalPages;
    }
    pageModel.setPage(page);
    pageModel.setPageSize(pageSize);
    pageModel.setTotalCount(counts);
    pageModel.setTotalPages(totalPages);
    pageModel.setResult(dtos);
    return pageModel;

  }


  public boolean judgePartner(List<ObjectId> partners, ObjectId userId) {
    boolean judge = false;
    for (ObjectId objectId : partners) {
      if (userId.equals(objectId)) {
        judge = true;
        break;
      }
    }
    return judge;
  }

  public List<String> getPlaymate(Map<ObjectId, UserEntry> partnerInfo, List<ObjectId> mutual) {
    List<String> playmate = new ArrayList<String>();
    if (mutual.size() == 0) {
      return playmate;
    } else if (mutual.size() >= 1) {
      playmate.add(StringUtils.isNotBlank(partnerInfo.get(mutual.get(0)).getNickName()) ?
              partnerInfo.get(mutual.get(0)).getNickName() : partnerInfo.get(mutual.get(0)).getUserName());
    }
    return playmate;
  }

  public List<ObjectId> mutual(List<ObjectId> partners, List<ObjectId> partners1) {
    List<ObjectId> objectIds = new ArrayList<ObjectId>();
    for (ObjectId objectId : partners) {
      for (ObjectId item : partners1) {
        if (objectId.equals(item)) {
          objectIds.add(objectId);
          break;
        }
      }
    }
    return objectIds;
  }

  public List<String> getTagList(List<UserEntry.UserTagEntry> ftags, List<UserEntry.UserTagEntry> tags) {
    List<String> tagEntries = new ArrayList<String>();
    for (UserEntry.UserTagEntry userTagEntry : ftags) {
      if (tagEntries.size() < 2) {
        for (UserEntry.UserTagEntry item : tags) {
          if (userTagEntry.getCode() == item.getCode()) {
            tagEntries.add(userTagEntry.getTag());
            break;
          }
        }
      } else {
        break;
      }
    }
    return tagEntries;
  }

  /**
   * 获取最新资讯
   *
   * @param communityIds
   * @param page
   * @param pageSize
   * @param order
   * @return
   */
  public Map<String, Object> getNews(List<ObjectId> communityIds, int page, int pageSize, int order, ObjectId userId, int operation) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(CommunityDetailType.ANNOUNCEMENT.getDecs().toLowerCase(), getNews(communityIds, CommunityDetailType.ANNOUNCEMENT, page, pageSize, order, userId, operation));
    map.put(CommunityDetailType.ACTIVITY.getDecs().toLowerCase(), getNews(communityIds, CommunityDetailType.ACTIVITY, page, pageSize, order, userId, operation));
    map.put(CommunityDetailType.SHARE.getDecs().toLowerCase(), getNews(communityIds, CommunityDetailType.SHARE, page, pageSize, order, userId, operation));
    map.put(CommunityDetailType.MEANS.getDecs().toLowerCase(), getNews(communityIds, CommunityDetailType.MEANS, page, pageSize, order, userId, operation));
    map.put(CommunityDetailType.HOMEWORK.getDecs().toLowerCase(), getNews(communityIds, CommunityDetailType.HOMEWORK, page, pageSize, order, userId, operation));
    map.put(CommunityDetailType.MATERIALS.getDecs().toLowerCase(), getNews(communityIds, CommunityDetailType.MATERIALS, page, pageSize, order, userId, operation));
    return map;
  }

  public PageModel<CommunityDetailDTO> getMyMessages(ObjectId userId, int page, int pageSize, int order) {

    List<MineCommunityEntry> mineCommunityEntries = mineCommunityDao.findAll(userId,-1,0);
    List<ObjectId> myCommunitys = new ArrayList<ObjectId>();
    for (MineCommunityEntry mineCommunityEntry : mineCommunityEntries) {
      myCommunitys.add(mineCommunityEntry.getCommunityId());
    }

    PageModel<CommunityDetailDTO> pageModel = new PageModel<CommunityDetailDTO>();
    List<CommunityDetailEntry> entries = communityDetailDao.getDetailsByUserId(myCommunitys, page, pageSize, order);
    List<CommunityDetailDTO> dtos = new ArrayList<CommunityDetailDTO>();
    List<ObjectId> objectIds = new ArrayList<ObjectId>();
    for (CommunityDetailEntry entry : entries) {
      objectIds.add(entry.getCommunityUserId());
    }
    Map<ObjectId, UserEntry> map = userService.getUserEntryMap(objectIds, Constant.FIELDS);
    for (CommunityDetailEntry entry : entries) {
      UserEntry userEntry = map.get(entry.getCommunityUserId());
      CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
      communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
      if (StringUtils.isNotBlank(userEntry.getNickName())) {
        communityDetailDTO.setNickName(userEntry.getNickName());
      } else {
        communityDetailDTO.setNickName(userEntry.getUserName());
      }

      dtos.add(communityDetailDTO);
    }
    int counts = communityDetailDao.count(myCommunitys);
    int totalPages = (int) Math.ceil((counts / (pageSize * 1.0)));
    if (page > totalPages) {
      page = totalPages;
    }
    pageModel.setPage(page);
    pageModel.setPageSize(pageSize);
    pageModel.setTotalCount(counts);
    pageModel.setTotalPages(totalPages);
    pageModel.setResult(dtos);
    return pageModel;
  }

  public List<CommunityDTO> search(String regular, ObjectId userId) {
    List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
    List<CommunityEntry> entries = communityDao.findByRegularName(regular);
    for (CommunityEntry communityEntry : entries) {
      CommunityDTO communityDTO = new CommunityDTO(communityEntry);
      if (memberService.isGroupMember(new ObjectId(communityDTO.getGroupId()), userId)) {
        communityDTO.setIsJoin(true);
      }
      dtos.add(communityDTO);
    }

    CommunityEntry entries2 = communityDao.findBySearchId(regular);

    if (entries2 != null) {
      CommunityDTO communityDTO = new CommunityDTO(entries2);
      if (memberService.isGroupMember(new ObjectId(communityDTO.getGroupId()), userId)) {
        communityDTO.setIsJoin(true);
      }
      dtos.add(communityDTO);
    }

    for (CommunityDTO dto : dtos) {
      MemberDTO head = memberService.getHead(new ObjectId(dto.getGroupId()));
      int count = memberService.countMember(new ObjectId(dto.getGroupId()));
      dto.setMemberCount(count);
      dto.setHead(head);
    }
    return dtos;
  }

  private List<AttachmentEntry> splitAttachements(List<Attachement> attachements, ObjectId uid) {
    List<AttachmentEntry> attachmentEntries = new ArrayList<AttachmentEntry>();
    if (attachements == null) return attachmentEntries;
    for (Attachement attachement : attachements) {
      long time = System.currentTimeMillis();
      AttachmentEntry entry = new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(), time, uid);
      attachmentEntries.add(entry);
    }
    return attachmentEntries;
  }

  public void enterCommunityDetail(ObjectId communityDetailId, ObjectId userId) {
    communityDetailDao.enterCommunityDetail(communityDetailId, userId);
  }

  /**
   * 取消报名
   *
   * @param communityDetailId
   * @param userId
   */
  public void cancelCommunityDetail(ObjectId communityDetailId, ObjectId userId) {
    communityDetailDao.cancelBaoming(communityDetailId, userId);
  }

  /**
   * 删除备注
   *
   * @param communityDetailId
   * @param userId
   */
  public void deleteReplyDetailText(ObjectId communityDetailId, ObjectId userId) {
    communityDetailDao.deleteBaomingBeizhu(communityDetailId, userId);
  }


  public void updateRemark(ObjectId id, String remark) {
    remarkDao.update(id, remark);
  }

  public void saveRemark(RemarkEntry entry) {
    remarkDao.save(entry);
  }

  /**
   * 根据id删除成员
   */
  public void deleteMembers(List<ObjectId> ids) {
    memberDao.deleteMember(ids);
  }

  /**
   * 查出副社长人数
   *
   * @param communityId
   * @return
   */
  public int countSecondMember(ObjectId communityId) {
    ObjectId groupId = getGroupId(communityId);
    return memberDao.countDeputyHead(groupId);
  }

  /**
   * 设置副社长
   *
   * @param ids
   * @param role
   */
  public void setSecondMembers(List<ObjectId> ids, int role) {
    memberDao.setDeputyHead(ids, role);
  }

  /**
   * 查询该社区副社长信息
   *
   * @param communityId
   * @return
   */
  public List<MemberDTO> getSecondMembers(ObjectId communityId) {
    List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
    ObjectId groupId = getGroupId(communityId);
    List<MemberEntry> memberEntries = memberDao.getDeputyHead(groupId);
    for (MemberEntry memberEntry : memberEntries) {
      memberDTOs.add(new MemberDTO(memberEntry));
    }
    return memberDTOs;
  }

  /**
   * 保存单个的文本
   *
   * @param detailId
   * @param userId
   * @param text
   */
  public void saveReplyDetailText(ObjectId communityId, ObjectId detailId, ObjectId userId, String text, int type) {
    PartInContentEntry partInContent = new PartInContentEntry(communityId, detailId, userId, text, type);
    partInContentDao.saveParInContent(partInContent);
  }

  /**
   * 获取某人发的Part
   *
   * @param detailId
   * @param userId
   * @return
   */
  public PartInContentDTO getPartInContent(ObjectId detailId, ObjectId userId) {
    PartInContentEntry partInContentEntry = partInContentDao.getPartInContent(detailId, userId);
    if (partInContentEntry != null) {
      return new PartInContentDTO(partInContentEntry);
    }
    return null;
  }

  /**
   * 获取内容
   *
   * @param detailId
   * @param page
   * @param pageSize
   * @return
   */
  public PageModel<PartInContentDTO> getPartInContent(ObjectId userId, ObjectId detailId, int page, int pageSize) {
    PageModel<PartInContentDTO> pageModel = new PageModel<PartInContentDTO>();

    int totalCount = partInContentDao.countPartPartInContent(detailId);
    pageModel.setTotalCount(totalCount);
    int totalPage = (int) Math.ceil(totalCount / pageSize) + 1;
    if (page > totalPage) {
      page = totalPage;
    }
    pageModel.setTotalPages(totalPage);
    pageModel.setPageSize(pageSize);
    pageModel.setPage(page);
    boolean isManager=false;
    List<PartInContentEntry> entrys = partInContentDao.getPartInContent(detailId, -1, page, pageSize);
    if(entrys.size()>0){
      ObjectId communityId=entrys.get(0).getCommunityId();
      ObjectId groupId=getGroupId(communityId);
      if(null!=userId){
        if(memberDao.isManager(groupId,userId)){
          isManager=true;
        }
      }
    }
    List<PartInContentDTO> parts = new ArrayList<PartInContentDTO>();
    for (PartInContentEntry entry : entrys) {
      UserEntry userEntry = userService.find(entry.getUserId());
      PartInContentDTO dto = new PartInContentDTO(entry);
      dto.setUserName(userEntry.getUserName());
      dto.setAvator(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
      dto.setNickName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
      dto.setTime(DateUtils.timeStampToStr(entry.getID().getTimestamp()));
      //判断该用户是否点过赞
      if(null!=userId) {
        setPartIncontentZan(dto, userId);
      }
      dto.setManager(isManager);
      parts.add(dto);
    }
    pageModel.setResult(parts);
    return pageModel;
  }

  public void setPartIncontentZan(PartInContentDTO dto, ObjectId userId) {
    List<String> zanUserIds = dto.getZanUserIds();
    boolean flag = false;
    for (String item : zanUserIds) {
      if (item.equals(userId.toString())) {
        flag = true;
        break;
      }
    }
    if (flag) {
      dto.setOwnerZan(1);
    } else {
      dto.setOwnerZan(0);
    }
  }

  /**
   * 获取公开的社区
   *
   * @return
   */
  public List<CommunityDTO> getOpenCommunityS(ObjectId userId) {
    List<CommunityDTO> communityDTOs = new ArrayList<CommunityDTO>();
    List<CommunityEntry> communityEntries = communityDao.getOpenCommunitys();
//    List<ObjectId> groupIds=new ArrayList<ObjectId>();
    if (null == userId) {
      for (CommunityEntry communityEntry : communityEntries) {
//      groupIds.add(new ObjectId(communityEntry.getGroupId()));
        CommunityDTO communityDTO = new CommunityDTO(communityEntry);
        communityDTOs.add(communityDTO);
      }
    } else {
      for (CommunityEntry communityEntry : communityEntries) {
//      groupIds.add(new ObjectId(communityEntry.getGroupId()));
        if (!memberService.isGroupMember(new ObjectId(communityEntry.getGroupId()), userId)) {
          CommunityDTO communityDTO = new CommunityDTO(communityEntry);
          communityDTOs.add(communityDTO);
        }

      }
    }

//    if(null!=userId){
//      Map<ObjectId,MemberEntry> map=memberDao.getHotOwenr(groupIds,userId);
//      for(CommunityDTO communityDTO:communityDTOs){
//        MemberEntry memberEntry=map.get(new ObjectId(communityDTO.getGroupId()));
//        if(null!=memberEntry){
//          communityDTO.setHot(1);
//        }else{
//          communityDTO.setHot(0);
//        }
//      }
//    }

    return communityDTOs;
  }

  /**
   * 上传作业
   *
   * @param communityDetailId
   * @param uid
   * @param content
   * @param images
   * @param attacheMents
   */
  public void saveHomeWork(ObjectId communityId, ObjectId communityDetailId, ObjectId uid, String content, String images, String attacheMents, int type) {
    List<String> imagesList = new ArrayList<String>();
    List<AttachmentEntry> dbAttacheMents = new ArrayList<AttachmentEntry>();
    if (StringUtils.isNotBlank(images)) {
      String[] imageList = images.split(",");
      Collections.addAll(imagesList, imageList);
    }

    if (StringUtils.isNotBlank(attacheMents)) {
      String[] attachMentList = attacheMents.split(",");
      for (String attch : attachMentList) {
        String[] names = attch.split("@");
        AttachmentEntry att = new AttachmentEntry(names[1], names[0], uid);
        dbAttacheMents.add(att);
      }
    }
    PartInContentEntry partIn = new PartInContentEntry(communityId, communityDetailId, uid, content, imagesList, dbAttacheMents, type);
    partInContentDao.saveParInContent(partIn);
  }

  /**
   * 上传分享
   *
   * @param communityDetailId
   * @param uid
   * @param content
   * @param images
   * @param vedios
   */
  public void saveCommunityShare(ObjectId communityId, ObjectId communityDetailId, ObjectId uid, String content, String images, String vedios, int type) {

    List<String> imagesList = new ArrayList<String>();
    List<VideoEntry> dbAttacheMents = new ArrayList<VideoEntry>();
    if (StringUtils.isNotBlank(images)) {
      String[] imageList = images.split(",");
      Collections.addAll(imagesList, imageList);
    }
    if (StringUtils.isNotBlank(vedios)) {
      String[] attachMentList = vedios.split(",");
      for (String attch : attachMentList) {
        String[] names = attch.split("@");

        if (names.length >= 2) {
          VideoEntry att = new VideoEntry(names[1], names[0], uid);
          dbAttacheMents.add(att);
        } else {
          VideoEntry att = new VideoEntry(names[0], uid);
          dbAttacheMents.add(att);
        }

      }
    }
    PartInContentEntry partIn = new PartInContentEntry(dbAttacheMents, communityId, communityDetailId, uid, content, imagesList, type);
    partInContentDao.saveParInContent(partIn);
  }

  public ObjectId getGroupId(ObjectId communityId) {
    return communityDao.getGroupId(communityId);
  }

  /**
   * 保存分享链接
   *
   * @param communityDetailId
   * @param uid
   * @param productModel
   */
  public void saveCommunityShare(ObjectId communityId, ObjectId communityDetailId, ObjectId uid, ProductModel productModel, String shareCommend, int type) {
    PartInContentEntry partIn = new PartInContentEntry(communityId, communityDetailId, uid,
            productModel.getUrl(), productModel.getImageUrl(), productModel.getProductDescription(),
            productModel.getProductPrice(), shareCommend, type);
    partInContentDao.saveParInContent(partIn);
  }

  /**
   * 学习用品保存数据
   */
  public void saveCommunityRecommend(ObjectId communityId, ObjectId communityDetailId, ObjectId uid, String shareUrl, String shareImage,
                                     String description, String sharePrice, String shareCommend, int type) {
    PartInContentEntry partIn = new PartInContentEntry(communityId, communityDetailId, uid,
            shareUrl, shareImage, description, sharePrice, shareCommend, type);
    partInContentDao.saveParInContent(partIn);
  }


  /**
   * 对回复进行点赞
   *
   * @param partInContentId
   * @param userId
   * @param zan
   */
  public boolean ZanToPartInContent(ObjectId partInContentId, ObjectId userId, int zan) {
    if (zan == 1) {
      if (!partInContentDao.isZanToPartInContent(partInContentId, userId)) {
        partInContentDao.setZanToPartInContent(partInContentId, userId);
        return true;
      } else {
        return false;
      }
    } else {
      if (partInContentDao.isZanToPartInContent(partInContentId, userId)) {
        partInContentDao.downZanToPartInContent(partInContentId, userId);
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * 获取某个社区的最新的公告
   *
   * @param communityId
   * @return
   */
  public CommunityDetailDTO getLatestAnnouncement(ObjectId communityId) {

    CommunityDetailEntry entry = communityDetailDao.getLatestDetails(communityId, CommunityDetailType.ANNOUNCEMENT.getType());
    if (entry == null) {
      return null;
    }

    UserEntry userEntry = userDao.findByObjectId(entry.getCommunityUserId());
    CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
    communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
    if (StringUtils.isNotBlank(userEntry.getNickName())) {
      communityDetailDTO.setNickName(userEntry.getNickName());
    } else {
      communityDetailDTO.setNickName(userEntry.getUserName());
    }
    communityDetailDTO.setPartInCount(communityDetailDTO.getPartInList().size());

    return communityDetailDTO;
  }


  public void setPartIncontentStatus(ObjectId communityId, ObjectId userId, int remove) {
    partInContentDao.setPartIncontentStatus(communityId, userId, remove);
  }

  /**
   * 创建时名字是否唯一
   *
   * @param communityName
   * @return
   */
  public Boolean judgeCommunityCreate(String communityName) {
    return communityDao.judgeCommunity(communityName);
  }


  public CommunityDTO getDefaultDto(String name) {
    CommunityEntry communityEntry = communityDao.getDefaultEntry(name);
    if (null != communityEntry) {
      return new CommunityDTO(communityEntry);
    } else {
      return null;
    }

  }


  /**
   * 该条消息已读取
   *
   * @param detailId
   * @param userId
   */
  public void pushRead(ObjectId detailId, ObjectId userId) {
    communityDetailDao.pushRead(detailId, userId);
  }


  public CommunityDTO getByGroupId(String emChatId) {
    CommunityEntry communityEntry = communityDao.findByGroupId(emChatId);
    if (communityEntry == null) return null;
    return new CommunityDTO(communityEntry);
  }

  /**
   * 编辑社区时判断是否改变
   */
  public boolean judgeCommunityName(String comuniytName, ObjectId id) {
    return communityDao.judgeCommunityName(comuniytName, id);
  }

  public void updateCommunityName(ObjectId communityId, String groupName) {
    communityDao.updateCommunityName(communityId, groupName);
  }

  public int countCommunityAmount(ObjectId userId) {
    return communityDao.getCommunityCountByOwerId(userId);
  }

  public void generateQrUrl(String searchId) {

    CommunityEntry communityEntry = communityDao.findBySearchId(searchId);
    String qrUrl = QRUtils.getCommunityQrUrl(communityEntry.getID());
    communityDao.updateCommunityQrUrl(communityEntry.getID(), qrUrl);
    CommunityDTO communityDto = findByObjectId(communityEntry.getID());

  }

  public List<CommunityDTO> findAllCommunity() {
    List<CommunityEntry> communityEntrys = communityDao.findAll();
    List<CommunityDTO> communityDtos = new ArrayList<CommunityDTO>();
    for (CommunityEntry entry : communityEntrys) {
      communityDtos.add(new CommunityDTO(entry));
    }
    return communityDtos;
  }

  public void resetLogo(String communityId, String logo) {
    communityDao.resetLogo(communityId, logo);
  }

  public void deleteCommunity(ObjectId communityId) {
    communityDao.deleteCommunity(communityId);
  }


  public void updateImage(ObjectId id,String newImageUrl,String oldImageUrl) {
    partInContentDao.pushImage(id, newImageUrl);
    partInContentDao.pullImage(id, oldImageUrl);
  }

  public void resetMineCommunityS(ObjectId userId,ObjectId communityId,int priory){
    mineCommunityDao.resetMineCommunitys(userId,communityId,priory);
  }

  public PartInContentEntry findPartIncontById(ObjectId id){
    return partInContentDao.find(id);
  }

  public void savePartIncontent(PartInContentEntry partInContentEntry){
    partInContentDao.saveParInContent(partInContentEntry);
  }
}
