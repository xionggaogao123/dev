package com.fulaan.service;

import com.db.fcommunity.*;
import com.db.user.UserDao;
import com.fulaan.cache.RedisUtils;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.community.dto.CommunityDetailDTO;
import com.fulaan.community.dto.PartInContentDTO;
import com.fulaan.community.dto.RemarkDTO;
import com.fulaan.dto.*;
import com.fulaan.dto.VideoDTO;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.fgroup.service.GroupService;
import com.fulaan.forum.service.FVoteService;
import com.fulaan.friendscircle.service.FriendApplyService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.pojo.*;
import com.fulaan.user.service.UserService;
import com.fulaan.util.DateUtils;
import com.pojo.activity.FriendApplyEntry;
import com.pojo.fcommunity.*;
import com.pojo.fcommunity.VideoEntry;
import com.pojo.forum.FVoteDTO;
import com.pojo.forum.FVoteEntry;
import com.pojo.user.AvatarType;
import com.pojo.user.UserEntry;
import com.pojo.video.*;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * Created by jerry on 2016/10/24.
 */
@Service
public class CommunityService {

    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private FriendApplyService friendApplyService;
    @Autowired
    private EmService emService;
    @Autowired
    private FVoteService fVoteService;

    private UserDao userDao = new UserDao();
    private CommunityDao communityDao = new CommunityDao();
    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();
    private PartInContentDao partInContentDao = new PartInContentDao();
    private MineCommunityDao mineCommunityDao = new MineCommunityDao();
    private CommunitySeqDao seqDao = new CommunitySeqDao();
    private RemarkDao remarkDao = new RemarkDao();
    private MemberDao memberDao = new MemberDao();

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
        if (emChatId == null) {
            return null;
        }
        ObjectId groupId = groupService.createGroupWithCommunity(communityId, userId, emChatId, name, desc, qrUrl);
        CommunityEntry entry = new CommunityEntry(communityId, seqId, groupId, emChatId, name, logo, desc, qrUrl, open, userId);
        communityDao.save(entry);
        pushToUser(communityId, userId, 1);
        return communityId;
    }

    /**
     * 生成名字
     *
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
        return communityEntry == null ? null : new CommunityDTO(communityEntry);
    }

    public void updateCommunityPrio(ObjectId communityId, int prio) {
        mineCommunityDao.updatePriority(communityId, prio);
    }


    public Map<String, MemberEntry> getMemberEntryMap(List<ObjectId> groupIds, List<ObjectId> userIds) {
        return memberDao.getGroupNick(groupIds, userIds);
    }

    /**
     * 根据ObjectId 获取详情
     *
     * @param communityDetailId
     * @return
     */
    public CommunityDetailDTO findDetailById(ObjectId communityDetailId, ObjectId loginUserId) {

        CommunityDetailEntry communityDetailEntry = communityDetailDao.findByObjectId(communityDetailId);
        ObjectId userId = communityDetailEntry.getCommunityUserId();
        ObjectId groupId = getGroupId(new ObjectId(communityDetailEntry.getCommunityId()));
        List<ObjectId> groupIds = new ArrayList<ObjectId>();
        groupIds.add(groupId);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        userIds.add(userId);
        Map<String, MemberEntry> memberEntryMap = memberDao.getGroupNick(groupIds, userIds);
        Map<ObjectId,RemarkEntry> remarkEntryMap=new HashMap<ObjectId, RemarkEntry>();
        if(null!=loginUserId){
            remarkEntryMap=remarkDao.find(loginUserId,userIds);
        }
        List<PartInContentEntry> partInContentEntries = partInContentDao.getPartInContent(communityDetailEntry.getID(), -1, 1, 10);
        CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(communityDetailEntry, partInContentEntries);
        UserEntry userEntry = userDao.findByUserId(userId);

        if (null != memberEntryMap) {
            MemberEntry entry1 = memberEntryMap.get(groupId + "$" + userId);
            setCommunityDetailInfo(communityDetailDTO, userEntry, entry1);
        } else {
            communityDetailDTO.setNickName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
        }
        //设置备注名
        if(null!=remarkEntryMap){
            RemarkEntry remarkEntry=remarkEntryMap.get(userId);
            if(null!=remarkEntry){
                communityDetailDTO.setNickName(remarkEntry.getRemark());
            }
        }
        int partIncontentCount = partInContentDao.countPartPartInContent(communityDetailId);
        communityDetailDTO.setPartIncotentCount(partIncontentCount);
        communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));

        if (communityDetailDTO.getType() == CommunityDetailType.VOTE.getType()) {
            int voteCount = fVoteService.getFVoteCount(communityDetailEntry.getID().toString());
            communityDetailDTO.setVoteCount(voteCount);
            long nowTime = System.currentTimeMillis();
            if (nowTime < communityDetailEntry.getVoteDeadTime()) {
                communityDetailDTO.setVoteDeadFlag(0);
            } else {
                communityDetailDTO.setVoteDeadFlag(1);
            }
            communityDetailDTO.setHasVoted(0);
            if (null != userId) {
                FVoteEntry fVoteEntry = fVoteService.getFVote(communityDetailEntry.getID().toString(), userId.toString());
                if (null != fVoteEntry) {
                    communityDetailDTO.setHasVoted(1);
                }
            }
            String voteContent = communityDetailEntry.getVoteContent();
            List<String> voteOptions = new ArrayList<String>();
            if (voteContent.contains("/n/r")) {
                String[] str = voteContent.split("/n/r");
                for (String item : str) {
                    voteOptions.add(item);
                }
            } else {
                voteOptions.add(voteContent);
            }
            communityDetailDTO.setVoteOptions(voteOptions);
            List<CommunityDetailDTO.VoteResult> mapList = new ArrayList<CommunityDetailDTO.VoteResult>();
            List<FVoteDTO> fVoteEntryList = fVoteService.getFVoteList(communityDetailEntry.getID().toString());
            int totalCount = fVoteEntryList.size();
            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(0);
            communityDetailDTO.setVoteTotalCount(totalCount);
            Set<ObjectId> totalUserIds=new HashSet<ObjectId>();
            Map<ObjectId,Long> timeRecord=new HashMap<ObjectId, Long>();
            for (int i = 0; i < voteOptions.size(); i++) {
                Set<ObjectId> selectUserIds=new HashSet<ObjectId>();
                CommunityDetailDTO.VoteResult voteResult = new CommunityDetailDTO.VoteResult();
                int j = i + 1;
                int count = 0;
                int hasVoted = 0;
                for (FVoteDTO fVoteDTO : fVoteEntryList) {
                    int number = fVoteDTO.getNumber();
                    timeRecord.put(new ObjectId(fVoteDTO.getUserId()),new ObjectId(fVoteDTO.getId()).getTime());
                    if (j == number) {
                        count++;
                        selectUserIds.add(new ObjectId(fVoteDTO.getUserId()));
                        if (null != loginUserId) {
                            if (new ObjectId(fVoteDTO.getUserId()).equals(loginUserId)) {
                                hasVoted = 1;
                            }
                        }
                    }
                }
                totalUserIds.addAll(selectUserIds);
                voteResult.setHasVoted(hasVoted);
                voteResult.setUserIds(selectUserIds);
                double pItem = (double) count / (double) totalCount;
                voteResult.setVoteItemStr(voteOptions.get(i));
                voteResult.setVoteItemCount(count);
                if (count == 0) {
                    voteResult.setVoteItemPercent("0%");
                } else {
                    voteResult.setVoteItemPercent(nt.format(pItem));
                }
                if (null == loginUserId) {
                    voteResult.setHasVoted(0);
                }
                mapList.add(voteResult);
            }
            communityDetailDTO.setIsOwner(0);

            if(null!=loginUserId){
                if(loginUserId.equals(communityDetailEntry.getCommunityUserId())){
                    communityDetailDTO.setIsOwner(1);
                }
            }

            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(totalUserIds,Constant.FIELDS);
            List<User> users=new ArrayList<User>();
            for(Map.Entry<ObjectId,UserEntry> entryEntry:userEntryMap.entrySet()){
                UserEntry userEntry1=entryEntry.getValue();
                users.add(new User(userEntry1.getUserName(),userEntry1.getNickName(),userEntry1.getID().toString(),
                        AvatarUtils.getAvatar(userEntry1.getAvatar(),AvatarType.MIN_AVATAR.getType()),
                        userEntry1.getSex(), DateTimeUtils.convert(timeRecord.get(userEntry1.getID()),DateTimeUtils.DATE_YYYY_MM_DD)));
            }
            communityDetailDTO.setVoteUsers(users);
            if(communityDetailDTO.getVoteType()==0){

                for(CommunityDetailDTO.VoteResult voteResult:mapList){
                    List<User> voteUsers=new ArrayList<User>();
                    Set<ObjectId> ItemUserIds=voteResult.getUserIds();
                    for(ObjectId id:ItemUserIds){
                        UserEntry user=userEntryMap.get(id);
                        if(null!=user){
                            voteUsers.add(new User(user.getUserName(),user.getNickName(),user.getID().toString(),
                                    AvatarUtils.getAvatar(user.getAvatar(),AvatarType.MIN_AVATAR.getType()),user.getSex(),
                                    DateTimeUtils.convert(timeRecord.get(user.getID()),DateTimeUtils.DATE_YYYY_MM_DD)));
                        }
                    }
                    voteResult.setVoteUsers(voteUsers);
                }
            }
            communityDetailDTO.setMapList(mapList);
        }
        return communityDetailDTO;
    }

    public void pushToUser(ObjectId communityId, ObjectId uid, int prioity) {
        String customSortSort = RedisUtils.getString("customSort" + uid);
        int customSort = 0;
        if (StringUtils.isNotBlank(customSortSort)) {
            customSort = Integer.parseInt(customSortSort);
        }
        MineCommunityEntry mineCommunityEntry = new MineCommunityEntry(uid, communityId, prioity, customSort);
        mineCommunityDao.save(mineCommunityEntry);
    }

    public void pullFromUser(ObjectId communityId, ObjectId uid) {
        mineCommunityDao.delete(communityId, uid);
    }

    public String getSeq() {
        return String.valueOf(seqDao.getRandom().getSeq());
    }

    public void generateSeq() {
        for (int i = 150000; i < 200000; i++) {
            CommunitySeqEntry entry = new CommunitySeqEntry(1, i);
            seqDao.save(entry);
        }
    }

    public void saveMimeEntry(MineCommunityEntry entry) {
        mineCommunityDao.save(entry);
    }

    /**
     * 获取我的社团
     *
     * @param uid
     */
    public List<CommunityDTO> getCommunitys(ObjectId uid, int page, int pageSize) {
        List<MineCommunityEntry> allMineCommunitys = mineCommunityDao.findAll(uid, page, pageSize);
        List<CommunityDTO> list = new ArrayList<CommunityDTO>();
        for (MineCommunityEntry mineCommunityEntry : allMineCommunitys) {
            CommunityEntry entry = communityDao.findByObjectId(mineCommunityEntry.getCommunityId());
            if (entry != null) {
                CommunityDTO communityDTO = new CommunityDTO(entry);
                communityDTO.setTop(mineCommunityEntry.getTop());
                list.add(communityDTO);
            }
        }
        return list;
    }

    public int countMycommunitys(ObjectId userId) {
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
    @Async
    public void updateCommunity(ObjectId cid, String name, String desc, String logo, int open) {
        communityDao.updateCommunityOpenStatus(cid, open);
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
    public void saveMessage(ObjectId uid, CommunityMessage message) throws Exception {
        List<AttachmentEntry> attachmentEntries = splitAttachements(message.getAttachements(), uid);
        List<AttachmentEntry> vedios = splitAttachements(message.getVedios(), uid);
        List<AttachmentEntry> images = splitAttachements(message.getImages(), uid);
        List<VideoEntry> videoEntries = splitVideos(message.getVideoDTOs(), uid);
        CommunityDetailEntry entry = new CommunityDetailEntry(new ObjectId(message.getCommunityId()),
                uid, userService.replaceSensitiveWord(message.getTitle()), userService.replaceSensitiveWord(message.getContent()), message.getType(),
                new ArrayList<ObjectId>(), attachmentEntries, vedios, images,
                message.getShareUrl(), message.getShareImage(), message.getShareTitle(), message.getSharePrice(), message.getVoteContent(), message.getVoteMaxCount(),
                ConvertStrToLong(message.getVoteDeadTime()), message.getVoteType(), videoEntries
        );
        communityDetailDao.save(entry);
    }

    private long ConvertStrToLong(String voteDeadTime) throws Exception {
        if (StringUtils.isNotBlank(voteDeadTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date time = format.parse(voteDeadTime);
            return time.getTime();
        } else {
            return -1L;
        }

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

    private List<CommunityDetailDTO> getNews(List<ObjectId> communityIds, CommunityDetailType type, int page, int pageSize,
                                             int order, ObjectId userId, int operation) {
        CommunityEntry communityEntry = new CommunityEntry();
        if (null != communityIds && communityIds.size() > 0 && operation == 1) {
            communityEntry = communityDao.findByObjectId(communityIds.get(0));
        }
        List<CommunityDetailEntry> communitys = communityDetailDao.getNewsByType(communityIds, type, page, pageSize, order);

        int unreadCount = 0;
        if (userId != null && communityIds != null && communityIds.size() > 0 && operation == 1) {
            int totalCount = communityDetailDao.count(communityIds.get(0), type);
            int readCount = communityDetailDao.countRead(type, communityIds.get(0), userId);
            unreadCount = totalCount - readCount;
        }

        List<CommunityDetailDTO> dtos = new ArrayList<CommunityDetailDTO>();
        Set<ObjectId> uuids = new HashSet<ObjectId>();
        Set<ObjectId> uuuids = new HashSet<ObjectId>();

        for (CommunityDetailEntry entry : communitys) {
            uuids.add(entry.getCommunityUserId());
            uuuids.add(new ObjectId(entry.getCommunityId()));
        }
        List<ObjectId> objectIds = new ArrayList<ObjectId>(uuids);
        List<ObjectId> communities = new ArrayList<ObjectId>(uuuids);
        Map<ObjectId, UserEntry> map = userService.getUserEntryMap(objectIds, Constant.FIELDS);
        //获取群昵称
        List<ObjectId> groupIdList = new ArrayList<ObjectId>();
        Map<ObjectId, ObjectId> groupIds = communityDao.getGroupIds(communities);
        for (Map.Entry<ObjectId, ObjectId> item : groupIds.entrySet()) {
            groupIdList.add(item.getValue());
        }
        Map<String, MemberEntry> memberMap = memberDao.getGroupNick(groupIdList, objectIds);
        //查询备注名
        Map<ObjectId, RemarkEntry> remarkEntryMap=new HashMap<ObjectId, RemarkEntry>();
        if(null!=userId){
            remarkEntryMap = remarkDao.find(userId, objectIds);
        }

        for (CommunityDetailEntry entry : communitys) {
            UserEntry userEntry = map.get(entry.getCommunityUserId());
            CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
            ObjectId groupId = groupIds.get(new ObjectId(entry.getCommunityId()));
            //判断是否为学习用品
            if (type.getType() == CommunityDetailType.MATERIALS.getType()) {
                List<PartInContentDTO> partInContentDTOs = new ArrayList<PartInContentDTO>();
                List<PartInContentEntry> partInContentEntries = partInContentDao.getPartInContent(entry.getID(), 6, 1, 1);
                Set<ObjectId> partinUserIds = new HashSet<ObjectId>();
                for (PartInContentEntry partEntry : partInContentEntries) {
                    partinUserIds.add(partEntry.getUserId());
                }
                List<ObjectId> tempUserIds = new ArrayList<ObjectId>(partinUserIds);
                Map<String, MemberEntry> partInMembermap = memberDao.getGroupNick(groupIdList, tempUserIds);
                //查询备注名
                Map<ObjectId, RemarkEntry> remarkEntryHashMap=new HashMap<ObjectId, RemarkEntry>();
                if(null!=userId){
                    remarkEntryHashMap = remarkDao.find(userId, tempUserIds);
                }
                for (PartInContentEntry partEntry : partInContentEntries) {
                    PartInContentDTO dto = new PartInContentDTO(partEntry);
                    UserEntry user = userService.findById(partEntry.getUserId());
                    //判断用户是否为空
                    if (null != user) {
                        dto.setAvator(AvatarUtils.getAvatar(user.getAvatar(), AvatarType.MIN_AVATAR.getType()));
                        MemberEntry entry2 = partInMembermap.get(groupId + "$" + partEntry.getUserId());
                        setPartInContentDTOInfo(dto, user, entry2);
                        //设置备注名
                        if(null!=remarkEntryHashMap){
                            RemarkEntry remarkEntry=remarkEntryHashMap.get(partEntry.getUserId());
                            if(null!=remarkEntry){
                                dto.setNickName(remarkEntry.getRemark());
                            }
                        }
                        dto.setTime(DateUtils.timeStampToStr(partEntry.getID().getTimestamp()));
                        partInContentDTOs.add(dto);
                    }
                }
                communityDetailDTO.setPartList(partInContentDTOs);
            } else if (type.getType() == CommunityDetailType.VOTE.getType()) {
                int voteCount = fVoteService.getFVoteCount(entry.getID().toString());
                int voteTotalCount = fVoteService.countTotalVote(entry.getID().toString());
                communityDetailDTO.setVoteTotalCount(voteTotalCount);
                communityDetailDTO.setVoteCount(voteCount);
                long nowTime = System.currentTimeMillis();
                if (nowTime < entry.getVoteDeadTime()) {
                    communityDetailDTO.setVoteDeadFlag(0);
                } else {
                    communityDetailDTO.setVoteDeadFlag(1);
                }
                communityDetailDTO.setHasVoted(0);
                if (null != userId) {
                    FVoteEntry fVoteEntry = fVoteService.getFVote(entry.getID().toString(), userId.toString());
                    if (null != fVoteEntry) {
                        communityDetailDTO.setHasVoted(1);
                    }
                }
                String voteContent = entry.getVoteContent();
                List<String> voteOptions = new ArrayList<String>();
                if (voteContent.contains("/n/r")) {
                    String[] str = voteContent.split("/n/r");
                    for (String item : str) {
                        voteOptions.add(item);
                    }
                } else {
                    voteOptions.add(voteContent);
                }
                communityDetailDTO.setVoteOptions(voteOptions);
            }
            if (null != communityEntry.getOwerID()) {
                communityDetailDTO.setCommunityName(communityEntry.getCommunityName());
            } else {
                communityEntry = communityDao.findByObjectId(new ObjectId(entry.getCommunityId()));
                communityDetailDTO.setCommunityName(communityEntry.getCommunityName());
            }
            communityDetailDTO.setOperation(0);

            if (null != userId) {
                if (memberService.isManager(groupId, userId)) {
                    communityDetailDTO.setOperation(1);
                }
            }

            //设置权限
            setRoleStr(communityDetailDTO, communityEntry, entry.getCommunityUserId());
            int totalCount = partInContentDao.countPartPartInContent(entry.getID());
            communityDetailDTO.setPartIncotentCount(totalCount);

            if (null != userEntry) {
                communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            }

            //先获取群昵称
            MemberEntry entry1 = memberMap.get(groupId + "$" + entry.getCommunityUserId());
            setCommunityDetailInfo(communityDetailDTO, userEntry, entry1);

            //设置备注名
            if(null!=remarkEntryMap){
                RemarkEntry entry2=remarkEntryMap.get(entry.getCommunityUserId());
                if(null!=entry2){
                    communityDetailDTO.setNickName(entry2.getRemark());
                }
            }
            communityDetailDTO.setUnReadCount(unreadCount);
            communityDetailDTO.setPartInCount(communityDetailDTO.getPartInList().size());
            dtos.add(communityDetailDTO);
        }
        return dtos;
    }


    private void setPartInContentDTOInfo(PartInContentDTO dto, UserEntry userEntry, MemberEntry entry) {
        if (null != entry) {
            if (StringUtils.isNotBlank(entry.getNickName())) {
                dto.setNickName(entry.getNickName());
            } else {
                dto.setNickName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
            }
        } else {
            dto.setNickName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
        }
    }


    private void setCommunityDetailInfo(CommunityDetailDTO communityDetailDTO, UserEntry userEntry, MemberEntry entry) {
        if (entry != null) {
            if (StringUtils.isNotBlank(entry.getNickName())) {
                communityDetailDTO.setNickName(entry.getNickName());
            } else {
                if (null != userEntry) {
                    communityDetailDTO.setNickName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                }
            }
        } else {
            if (null != userEntry) {
                communityDetailDTO.setNickName(StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
            }
        }
    }

    private void setRoleStr(CommunityDetailDTO communityDetailDTO, CommunityEntry communityEntry, ObjectId userId) {
        ObjectId groupId = communityEntry.getGroupId();
        if (userId.equals(communityEntry.getOwerID())) {
            communityDetailDTO.setRoleStr("社长");
        } else {
            if (memberService.isManager(groupId, userId)) {
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
        int totalCount = communityDetailDao.count(communityId);
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if (totalPages == 0 || page < 1) {
            page = 1;
        }
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalCount(totalCount);
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
        UserEntry userEntry = userService.findById(userId);
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
            //过滤掉不存在的用户
            if (null != userEntry1) {
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
        UserEntry userEntry = userService.findById(userId);
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
                UserEntry userEntry1;
                userEntry1 = map.get(new ObjectId(memberUserId));
                //过滤掉不存在的用户成员
                if (null != userEntry1) {
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
                }
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
        int counts = memberDao.getMemberCount(groupId);
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
     * @return
     */
    public PageModel<CommunityDetailDTO> getMessages(ObjectId communityId, int page, int pageSize, CommunityDetailType type, ObjectId userId, boolean isApp) {
        PageModel<CommunityDetailDTO> pageModel = new PageModel<CommunityDetailDTO>();
        List<CommunityDetailEntry> entries = communityDetailDao.getDetails(communityId, page, pageSize, Constant.DESC, type);
        int counts = communityDetailDao.count(communityId, type);

        int totalPages = counts % pageSize == 0 ? counts / pageSize : (int) Math.ceil(counts / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if (totalPages == 0 || page < 1) {
            page = 1;
        }

        if (type.getType() == CommunityDetailType.ANNOUNCEMENT.getType() && isApp) {
            if (null != userId) {
                setAppRead(userId, entries);
            }
        }
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        List<CommunityDetailDTO> dtos = new ArrayList<CommunityDetailDTO>();
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for (CommunityDetailEntry entry : entries) {
            userIds.add(entry.getCommunityUserId());
        }
        List<ObjectId> objectIds = new ArrayList<ObjectId>(userIds);
        Map<ObjectId, UserEntry> map = userService.getUserEntryMap(objectIds, Constant.FIELDS);
        //获取群昵称
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        communityIds.add(communityId);
        List<ObjectId> groupIdList = new ArrayList<ObjectId>();
        Map<ObjectId, ObjectId> groupIds = communityDao.getGroupIds(communityIds);
        for (Map.Entry<ObjectId, ObjectId> item : groupIds.entrySet()) {
            groupIdList.add(item.getValue());
        }
        Map<String, MemberEntry> memberMap = memberDao.getGroupNick(groupIdList, objectIds);
        //查询备注名
        Map<ObjectId, RemarkEntry> remarkEntryMap=new HashMap<ObjectId, RemarkEntry>();
        if(null!=userId){
            remarkEntryMap=remarkDao.find(userId,objectIds);
        }
        for (CommunityDetailEntry entry : entries) {
            UserEntry userEntry = map.get(entry.getCommunityUserId());
            CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
            if (userEntry != null) {
                communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            }

            ObjectId groupId = groupIds.get(new ObjectId(entry.getCommunityId()));
            MemberEntry entry1 = memberMap.get(groupId + "$" + entry.getCommunityUserId());

            setCommunityDetailInfo(communityDetailDTO, userEntry, entry1);
            //设置备注名
            if(null!=remarkEntryMap){
                RemarkEntry remarkEntry=remarkEntryMap.get(entry.getCommunityUserId());
                if(null!=remarkEntry){
                    communityDetailDTO.setNickName(remarkEntry.getRemark());
                }
            }

            if (null != userId) {
                communityDetailDTO.setReadFlag(0);
                if (entry.getUnReadList().size() > 0 && entry.getUnReadList().contains(userId)) {
                    communityDetailDTO.setReadFlag(1);
                }
            } else{
                communityDetailDTO.setReadFlag(1);
            }

            if (type.getType() == CommunityDetailType.VOTE.getType()) {
                int voteCount = fVoteService.getFVoteCount(entry.getID().toString());
                communityDetailDTO.setVoteCount(voteCount);
                long nowTime = System.currentTimeMillis();
                if (nowTime < entry.getVoteDeadTime()) {
                    communityDetailDTO.setVoteDeadFlag(0);
                } else {
                    communityDetailDTO.setVoteDeadFlag(1);
                }
                communityDetailDTO.setHasVoted(0);
                if (null != userId) {
                    FVoteEntry fVoteEntry = fVoteService.getFVote(entry.getID().toString(), userId.toString());
                    if (null != fVoteEntry) {
                        communityDetailDTO.setHasVoted(1);
                    }
                }
                String voteContent = entry.getVoteContent();
                List<String> voteOptions = new ArrayList<String>();
                if (voteContent.contains("/n/r")) {
                    String[] str = voteContent.split("/n/r");
                    for (String item : str) {
                        voteOptions.add(item);
                    }
                } else {
                    voteOptions.add(voteContent);
                }
                communityDetailDTO.setVoteOptions(voteOptions);
            }

            List<PartInContentDTO> partInContentDTOs = new ArrayList<PartInContentDTO>();
            List<PartInContentEntry> partInContentEntries = partInContentDao.getPartInContent(entry.getID(), -1, 1, 1);
            Set<ObjectId> partInUserIds = new HashSet<ObjectId>();
            for (PartInContentEntry partInContentEntry : partInContentEntries) {
                partInUserIds.add(partInContentEntry.getUserId());
            }
            List<ObjectId> partInUsers= new ArrayList<ObjectId>(partInUserIds);
            Map<String, MemberEntry> partInMembermap = memberDao.getGroupNick(groupIdList,partInUsers);
            Map<ObjectId, RemarkEntry> remarkEntryHashMap=new HashMap<ObjectId, RemarkEntry>();
            if(null!=userId){
                remarkEntryHashMap=remarkDao.find(userId,partInUsers);
            }
            for (PartInContentEntry partInContentEntry : partInContentEntries) {
                PartInContentDTO partInContentDTO = new PartInContentDTO(partInContentEntry);
                UserEntry userEntry1 = userService.findById(partInContentEntry.getUserId());
                partInContentDTO.setUserName(userEntry1.getUserName());
                partInContentDTO.setAvator(AvatarUtils.getAvatar(userEntry1.getAvatar(), AvatarType.MIN_AVATAR.getType()));

                MemberEntry entry2 = partInMembermap.get(groupId + "$" + partInContentEntry.getUserId());
                setPartInContentDTOInfo(partInContentDTO, userEntry1, entry2);
                //设置备注名
                if(null!=remarkEntryHashMap){
                    RemarkEntry remarkEntry=remarkEntryHashMap.get(partInContentEntry.getUserId());
                    if(null!=remarkEntry){
                        partInContentDTO.setNickName(remarkEntry.getRemark());
                    }
                }
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

        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalCount(counts);
        pageModel.setTotalPages(totalPages);
        pageModel.setResult(dtos);
        int totalCount = communityDetailDao.count(communityId, type);
        int readCount = communityDetailDao.countRead(type, communityId, userId);
        int unreadCount = totalCount - readCount;
        pageModel.setTotalUnReadCount(unreadCount);
        return pageModel;

    }

    private void setAppRead(ObjectId userId, List<CommunityDetailEntry> entries) {
        for (CommunityDetailEntry entry : entries) {
            boolean flag = true;
            List<ObjectId> unReadList = entry.getUnReadList();
            for (ObjectId item : unReadList) {
                if (userId.equals(item)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                pushRead(entry.getID(), userId);
            }
        }
    }


    private boolean judgePartner(List<ObjectId> partners, ObjectId userId) {
        boolean judge = false;
        for (ObjectId objectId : partners) {
            if (userId.equals(objectId)) {
                judge = true;
                break;
            }
        }
        return judge;
    }

    private List<String> getPlaymate(Map<ObjectId, UserEntry> partnerInfo, List<ObjectId> mutual) {
        List<String> playmate = new ArrayList<String>();
        if (mutual.size() == 0) {
            return playmate;
        } else if (mutual.size() >= 1) {
            playmate.add(StringUtils.isNotBlank(partnerInfo.get(mutual.get(0)).getNickName()) ?
                    partnerInfo.get(mutual.get(0)).getNickName() : partnerInfo.get(mutual.get(0)).getUserName());
        }
        return playmate;
    }

    private List<String> getTagList(List<UserEntry.UserTagEntry> ftags, List<UserEntry.UserTagEntry> tags) {
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
        map.put(CommunityDetailType.VOTE.getDecs().toLowerCase(), getNews(communityIds, CommunityDetailType.VOTE, page, pageSize, order, userId, operation));
        return map;
    }

    public PageModel<CommunityDetailDTO> getMyMessages(ObjectId userId, int page, int pageSize, int order) {

        List<MineCommunityEntry> mineCommunityEntries = mineCommunityDao.findAll(userId, -1, 0);
        Set<ObjectId> uuuids = new HashSet<ObjectId>();
        for (MineCommunityEntry mineCommunityEntry : mineCommunityEntries) {
            uuuids.add(mineCommunityEntry.getCommunityId());

        }
        List<ObjectId> myCommunitys = new ArrayList<ObjectId>(uuuids);
        int totalCount = communityDetailDao.count(myCommunitys);
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if (totalPages == 0 || page < 1) {
            page = 1;
        }

        PageModel<CommunityDetailDTO> pageModel = new PageModel<CommunityDetailDTO>();
        List<CommunityDetailEntry> entries = communityDetailDao.getDetailsByUserId(myCommunitys, page, pageSize, order);
        List<CommunityDetailDTO> dtos = new ArrayList<CommunityDetailDTO>();
        Set<ObjectId> uuids = new HashSet<ObjectId>();
        for (CommunityDetailEntry entry : entries) {
            uuids.add(entry.getCommunityUserId());
        }
        List<ObjectId> objectIds = new ArrayList<ObjectId>(uuids);
        Map<ObjectId, UserEntry> map = userService.getUserEntryMap(objectIds, Constant.FIELDS);
        //获取群昵称
        Map<ObjectId, ObjectId> groupIds = communityDao.getGroupIds(myCommunitys);
        List<ObjectId> groupIdList = new ArrayList<ObjectId>();
        for (Map.Entry<ObjectId, ObjectId> item : groupIds.entrySet()) {
            groupIdList.add(item.getValue());
        }
        Map<String, MemberEntry> memberMap = memberDao.getGroupNick(groupIdList, objectIds);
        for (CommunityDetailEntry entry : entries) {
            UserEntry userEntry = map.get(entry.getCommunityUserId());
            CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
            communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            //先获取群昵称
            ObjectId groupId = groupIds.get(new ObjectId(entry.getCommunityId()));
            MemberEntry entry1 = memberMap.get(groupId + "$" + entry.getCommunityUserId());
            setCommunityDetailInfo(communityDetailDTO, userEntry, entry1);

            dtos.add(communityDetailDTO);
        }
        pageModel.setPage(page);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalCount(totalCount);
        pageModel.setTotalPages(totalPages);
        pageModel.setResult(dtos);
        return pageModel;
    }

    public List<CommunityDTO> search(String regular, ObjectId userId) {
        List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
        List<CommunityDTO> returnData = new ArrayList<CommunityDTO>();
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
            int count = memberService.getMemberCount(new ObjectId(dto.getGroupId()));
            if (null != head) {
                dto.setMemberCount(count);
                dto.setHead(head);
                returnData.add(dto);
            }
        }
        return returnData;
    }

    private List<VideoEntry> splitVideos(List<VideoDTO> videoDTOs, ObjectId uid) {
        List<VideoEntry> videoEntries = new ArrayList<VideoEntry>();
        if (videoDTOs == null) return videoEntries;
        for (VideoDTO videoDTO : videoDTOs) {
            long time = System.currentTimeMillis();
            VideoEntry entry = new VideoEntry(videoDTO.getVideoUrl(), videoDTO.getImageUrl(), time, uid);
            videoEntries.add(entry);
        }
        return videoEntries;
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

    public RemarkEntry getRemarkEntry(ObjectId userId, ObjectId endUserId) {
        return remarkDao.getEntry(userId, endUserId);
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
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if (totalPages == 0 || page < 1) {
            page = 1;
        }
        pageModel.setTotalPages(totalPages);
        pageModel.setPageSize(pageSize);
        pageModel.setPage(page);
        boolean isManager = false;
        ObjectId groupId = new ObjectId();
        List<PartInContentEntry> entrys = partInContentDao.getPartInContent(detailId, -1, page, pageSize);
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        if (entrys.size() > 0) {
            ObjectId communityId = entrys.get(0).getCommunityId();
            communityIds.add(communityId);
            groupId = getGroupId(communityId);
            if (null != userId) {
                if (memberDao.isManager(groupId, userId)) {
                    isManager = true;
                }
            }
        }

        Set<ObjectId> uuids = new HashSet<ObjectId>();
        for (PartInContentEntry entry : entrys) {
            uuids.add(entry.getUserId());
        }
        List<PartInContentDTO> parts = new ArrayList<PartInContentDTO>();
        List<ObjectId> groupIdList = new ArrayList<ObjectId>();
        Map<String, MemberEntry> memberMap = new HashMap<String, MemberEntry>();
        Map<ObjectId,RemarkEntry> remarkEntryMap = new HashMap<ObjectId, RemarkEntry>();
        Map<ObjectId, ObjectId> groupIds;
        if (communityIds.size() > 0) {
            groupIds = communityDao.getGroupIds(communityIds);
            for (Map.Entry<ObjectId, ObjectId> item : groupIds.entrySet()) {
                groupIdList.add(item.getValue());
            }
            List<ObjectId> objectIds = new ArrayList<ObjectId>(uuids);
            memberMap = memberDao.getGroupNick(groupIdList, objectIds);
            if(null!=userId){
                remarkEntryMap=findRemarkEntries(userId,objectIds);
            }
        }

        for (PartInContentEntry entry : entrys) {
            UserEntry userEntry = userService.findById(entry.getUserId());
            PartInContentDTO dto = new PartInContentDTO(entry);
            dto.setUserName(userEntry.getUserName());
            dto.setAvator(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));

            MemberEntry entry1 = memberMap.get(groupId + "$" + entry.getUserId());
            setPartInContentDTOInfo(dto, userEntry, entry1);
            //设置备注名
            if(null!=remarkEntryMap){
                RemarkEntry remarkEntry=remarkEntryMap.get(entry.getUserId());
                if(null!=remarkEntry){
                    dto.setNickName(remarkEntry.getRemark());
                }
            }

            dto.setTime(DateUtils.timeStampToStr(entry.getID().getTimestamp()));
            //判断该用户是否点过赞
            if (null != userId) {
                setPartIncontentZan(dto, userId);
            }
            dto.setManager(isManager);
            parts.add(dto);
        }
        pageModel.setResult(parts);
        return pageModel;
    }

    private void setPartIncontentZan(PartInContentDTO dto, ObjectId userId) {
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
    public List<CommunityDTO> getOpenCommunityS(ObjectId userId, int page, int pageSize, String lastId) {
        List<CommunityDTO> communityDTOs = new ArrayList<CommunityDTO>();
        List<CommunityEntry> communityEntries = communityDao.getOpenCommunitys(page, pageSize, lastId);

        if (null == userId) {
            for (CommunityEntry communityEntry : communityEntries) {

                CommunityDTO communityDTO = new CommunityDTO(communityEntry);
                communityDTOs.add(communityDTO);
            }
        } else {
            for (CommunityEntry communityEntry : communityEntries) {
                if (null != communityEntry.getOwerID()) {
                    if (!memberService.isGroupMember(communityEntry.getGroupId(), userId)) {
                        CommunityDTO communityDTO = new CommunityDTO(communityEntry);
                        communityDTOs.add(communityDTO);
                    }
                }
            }
            //判断查询的是否满足所有条件
            int count = pageSize - communityDTOs.size();
            if (count > 0) {
                if (communityDTOs.size() != 0) {
                    lastId = communityDTOs.get(communityDTOs.size() - 1).getId();
                }
                boolean flag = false;
                List<Integer> integers = new ArrayList<Integer>();
                integers.add(0);
                //每次查询300条,查询次数最多10次
                dealWithData(flag, page, 300, pageSize, lastId, communityDTOs, userId, integers);
            }

        }
        return communityDTOs;
    }


    private boolean dealWithData(boolean flag, int page, int count, int pageSize, String lastId, List<CommunityDTO> communityDTOs, ObjectId userId, List<Integer> integers) {
        if (StringUtils.isNotBlank(lastId)) {
            if (page == 1) {
                page = 2;
            }
        }
        int integer = integers.get(0);
        if (integer > 10) {
            return true;
        } else {
            integers.set(0, integer + 1);
        }
        while (true) {
            List<CommunityEntry> communityEntries1 = communityDao.getOpenCommunitys(page, count, lastId);
            if (communityEntries1.size() != 0) {
                for (CommunityEntry communityEntry : communityEntries1) {
                    if (pageSize == communityDTOs.size()) {
                        break;
                    } else if (!memberService.isGroupMember(communityEntry.getGroupId(), userId)) {
                        CommunityDTO communityDTO = new CommunityDTO(communityEntry);
                        communityDTOs.add(communityDTO);
                    }
                }
                int temp = pageSize - communityDTOs.size();
                if (temp > 0) {
                    if (communityDTOs.size() != 0) {
                        lastId = communityDTOs.get(communityDTOs.size() - 1).getId();
                    }
                    flag = dealWithData(flag, page, count, pageSize, lastId, communityDTOs, userId, integers);
                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }
            if (flag) {
                break;
            }
        }
        return flag;
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
    public boolean zanToPartInContent(ObjectId partInContentId, ObjectId userId, int zan) {
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
    public CommunityDetailDTO getLatestAnnouncement(ObjectId communityId,ObjectId userId) {

        CommunityDetailEntry entry = communityDetailDao.getLatestDetails(communityId, CommunityDetailType.ANNOUNCEMENT.getType());
        if (entry == null) {
            return null;
        }
        CommunityDetailDTO communityDetailDTO = new CommunityDetailDTO(entry);
        UserEntry userEntry = userDao.findByUserId(entry.getCommunityUserId());
        communityDetailDTO.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), AvatarType.MIN_AVATAR.getType()));
        RemarkEntry remarkEntry=remarkDao.getEntry(userId,entry.getCommunityUserId());
        if(null!=remarkEntry){
            communityDetailDTO.setNickName(remarkEntry.getRemark());
        }else{
            if (StringUtils.isNotBlank(userEntry.getNickName())) {
                communityDetailDTO.setNickName(userEntry.getNickName());
            } else {
                communityDetailDTO.setNickName(userEntry.getUserName());
            }
            communityDetailDTO.setPartInCount(communityDetailDTO.getPartInList().size());
        }

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
    public Boolean isCommunityNameUnique(String communityName) {
        return communityDao.isCommunityNameUnique(communityName);
    }


    public CommunityDTO getCommunityByName(final String name) {
        CommunityEntry communityEntry = communityDao.findByName(name);
        return communityEntry == null ? null : new CommunityDTO(communityEntry);
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


    public CommunityDTO getByEmChatId(String emChatId) {
        CommunityEntry communityEntry = communityDao.findByEmChatId(emChatId);
        return communityEntry == null ? null : new CommunityDTO(communityEntry);
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

    public void updateImage(ObjectId id, String newImageUrl, String oldImageUrl) {
        partInContentDao.pushImage(id, newImageUrl);
        partInContentDao.pullImage(id, oldImageUrl);
    }

    public PartInContentEntry findPartIncontById(ObjectId id) {
        return partInContentDao.find(id);
    }

    public void savePartIncontent(PartInContentEntry partInContentEntry) {
        partInContentDao.saveParInContent(partInContentEntry);
    }

    public void removeCommunityDetailById(ObjectId id) {
        communityDetailDao.removeCommunityDetail(id);
    }

    public List<ObjectId> getAllMemberIds(ObjectId groupId) {
        return memberDao.getAllMemberIds(groupId);
    }


    public void setTop(ObjectId community, ObjectId userId, int top) {
        MineCommunityEntry entry = mineCommunityDao.findByUserAndCommunity(community, userId);
        entry.setTop(top);
        mineCommunityDao.save(entry);
    }

    public void setDefaultSort() {
        mineCommunityDao.setDefaultSort();
    }

    public MineCommunityEntry getTopEntry(ObjectId community, ObjectId userId) {
        return mineCommunityDao.findByUserAndCommunity(community, userId);
    }

    public void cleanNecessaryCommunity(ObjectId userId, ObjectId communityId) {
        mineCommunityDao.cleanNecessaryCommunity(userId, communityId);
    }


    public List<RemarkDTO> getRemarkDtos(ObjectId userId) {
        List<RemarkDTO> dtos = new ArrayList<RemarkDTO>();
        List<RemarkEntry> entries = remarkDao.getRemarkEntries(userId);
        for (RemarkEntry entry : entries) {
            dtos.add(new RemarkDTO(entry));
        }
        return dtos;
    }

    public void updateInitSort(ObjectId userId) {
        mineCommunityDao.updateInitSort(userId);
    }

    public Map<ObjectId, MineCommunityEntry> getMySortCommunities(ObjectId userId, List<ObjectId> communityIds) {
        return mineCommunityDao.getMySortCommunities(userId, communityIds);
    }

    public void batchSave(List<MineCommunityEntry> entries) {
        mineCommunityDao.batchSave(entries);
    }

    public void cleanPrior(){
        mineCommunityDao.cleanPrior();
    }

    public Map<ObjectId, RemarkEntry> findRemarkEntries(ObjectId startUserId, List<ObjectId> endUserIds){
        return remarkDao.find(startUserId,endUserIds);
    }

}
