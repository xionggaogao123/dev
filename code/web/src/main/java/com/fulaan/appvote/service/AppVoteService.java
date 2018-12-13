package com.fulaan.appvote.service;

import com.db.appactivity.AppActivityDao;
import com.db.appactivity.AppActivityUserDao;
import com.db.appvote.AppVoteDao;
import com.db.appvote.TransferVoteAndActivityDao;
import com.db.backstage.TeacherApproveDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.forum.FVoteDao;
import com.db.indexPage.IndexContentDao;
import com.db.indexPage.IndexPageDao;
import com.fulaan.appvote.dto.AppVoteDTO;
import com.fulaan.appvote.dto.VoteOption;
import com.fulaan.appvote.dto.VoteResult;
import com.fulaan.dto.VideoDTO;
import com.fulaan.forum.service.FVoteService;
import com.fulaan.indexpage.dto.IndexContentDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.appactivity.AppActivityEntry;
import com.pojo.appactivity.AppActivityUserEntry;
import com.pojo.appvote.AppVoteEntry;
import com.pojo.appvote.TransferVoteAndActivityEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.forum.FVoteDTO;
import com.pojo.forum.FVoteEntry;
import com.pojo.indexPage.IndexContentEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.integral.IntegralType;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.TimeChangeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by scott on 2017/11/6.
 */
@Service
public class AppVoteService {

    private AppVoteDao appVoteDao = new AppVoteDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private CommunityDao communityDao = new CommunityDao();

    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();

    private FVoteDao fVoteDao = new FVoteDao();

    private MemberDao memberDao=new MemberDao();

    private TransferVoteAndActivityDao transferVoteAndActivityDao = new TransferVoteAndActivityDao();

    private AppActivityUserDao appActivityUserDao = new AppActivityUserDao();

    private AppActivityDao appActivityDao = new AppActivityDao();

    private RedDotService redDotService = new RedDotService();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    @Autowired
    private FVoteService fVoteService;

    @Autowired
    private UserService userService;


    private IntegralSufferService integralSufferService = new IntegralSufferService();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private IndexContentDao indexContentDao = new IndexContentDao();


    public String saveAppVote(AppVoteDTO appVoteDTO) throws Exception {
        if(true){
            throw new Exception("新版本投票已上线，请升级到最新版家校美体验！");
        }
        SimpleDateFormat format = new SimpleDateFormat(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        Date date = format.parse(appVoteDTO.getDeadFormatTime());
       // List<AppVoteEntry> entries = new ArrayList<AppVoteEntry>();
        List<ObjectId> oids = new ArrayList<ObjectId>();
        for (GroupOfCommunityDTO dto : appVoteDTO.getGroupExamDetailDTOs()) {
            AppVoteDTO item = new AppVoteDTO(
                    appVoteDTO.getSubjectId(),
                    appVoteDTO.getUserId(),
                    appVoteDTO.getSubjectName(),
                    appVoteDTO.getTitle(),
                    appVoteDTO.getContent(),
                    appVoteDTO.getImageList(),
                    appVoteDTO.getVoteContent(),
                    appVoteDTO.getVoteMaxCount(),
                    date.getTime(),
                    appVoteDTO.getVoteType(),
                    appVoteDTO.getVisiblePermission(),
                    dto.getGroupId(),
                    dto.getCommunityId(),
                    dto.getGroupName()
            );
            oids.add(new ObjectId(dto.getCommunityId()));
           // entries.add(item.buildEntry());
            AppVoteEntry appVoteEntry = item.buildEntry();
            appVoteDao.saveAppVote(appVoteEntry);
            if(appVoteDTO.getVisiblePermission()==1 || appVoteDTO.getVisiblePermission()==3){//家长
                //发送通知
                PictureRunNable.addTongzhi(item.getCommunityId(), item.getUserId(), 5,appVoteDTO.getTitle());

                //首页记录
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.piao.getType());
                dto1.setUserId(appVoteDTO.getUserId());
                dto1.setCommunityId(dto.getCommunityId());
                dto1.setContactId(appVoteEntry.getID().toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);

                //新首页记录
                IndexPageDTO dto2 = new IndexPageDTO();
                dto2.setType(CommunityType.allNotice.getType());
                dto2.setUserId(appVoteDTO.getUserId());
                dto2.setCommunityId(dto.getCommunityId());
                dto2.setContactId(appVoteEntry.getID().toString());
                IndexPageEntry entry2 = dto2.buildAddEntry();
                indexPageDao.addEntry(entry2);
                IndexContentDTO indexContentDTO = new IndexContentDTO(
                        appVoteDTO.getSubjectName(),
                        "通知-投票",
                        appVoteDTO.getTitle(),
                        new ArrayList<VideoDTO>(),
                        appVoteDTO.getImageList(),
                        new ArrayList<Attachement>(),
                        new ArrayList<Attachement>(),
                        dto.getGroupName(),
                        "");
                List<ObjectId> members=memberDao.getAllMemberIds(new ObjectId(dto.getGroupId()));
                IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(appVoteDTO.getUserId(),appVoteDTO.getSubjectId(), dto.getGroupId(),dto.getCommunityId(),appVoteDTO.getVisiblePermission());
                indexContentEntry.setReadList(new ArrayList<ObjectId>());
                indexContentEntry.setContactId(appVoteEntry.getID());
                indexContentEntry.setContactType(7);
                indexContentEntry.setAllCount(members.size());
                indexContentDao.addEntry(indexContentEntry);
            }
            if(appVoteDTO.getVisiblePermission()==2){
                //新首页记录
                IndexPageDTO dto2 = new IndexPageDTO();
                dto2.setType(CommunityType.allNotice.getType());
                dto2.setUserId(appVoteDTO.getUserId());
                dto2.setCommunityId(appVoteDTO.getUserId());
                dto2.setContactId(appVoteEntry.getID().toString());
                IndexPageEntry entry2 = dto2.buildAddEntry();
                indexPageDao.addEntry(entry2);
                IndexContentDTO indexContentDTO = new IndexContentDTO(
                        appVoteDTO.getSubjectName(),
                        "通知-投票",
                        appVoteDTO.getTitle(),
                        new ArrayList<VideoDTO>(),
                        appVoteDTO.getImageList(),
                        new ArrayList<Attachement>(),
                        new ArrayList<Attachement>(),
                        dto.getGroupName(),
                        "");
                List<ObjectId> members=memberDao.getAllMemberIds(new ObjectId(dto.getGroupId()));
                IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(appVoteDTO.getUserId(),appVoteDTO.getSubjectId(), dto.getGroupId(),dto.getCommunityId(),appVoteDTO.getVisiblePermission());
                indexContentEntry.setReadList(new ArrayList<ObjectId>());
                indexContentEntry.setContactId(appVoteEntry.getID());
                indexContentEntry.setContactType(7);
                indexContentEntry.setAllCount(members.size());
                indexContentDao.addEntry(indexContentEntry);
            }

        }
        if(appVoteDTO.getVisiblePermission()==1 || appVoteDTO.getVisiblePermission()==3 ){//红点
            //添加红点
            redDotService.addOtherEntryList(oids, new ObjectId(appVoteDTO.getUserId()), ApplyTypeEn.piao.getType(), 1);
        }else{
            //添加红点
            redDotService.addOtherEntryList(oids,new ObjectId(appVoteDTO.getUserId()), ApplyTypeEn.piao.getType(),2);
        }
        //appVoteDao.saveEntries(entries);
        int score = integralSufferService.addIntegral(new ObjectId(appVoteDTO.getUserId()), IntegralType.vote,4,1);
        return score+"";
    }

    public Map<String,Object> gatherAppVotes(ObjectId userId, int page, int pageSize){
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        List<AppVoteDTO> appVoteDTOs = new ArrayList<AppVoteDTO>();
        List<AppVoteEntry> appVoteEntries = appVoteDao.getGatherAppVoteEntries(userId, groupIds,page, pageSize);
        getVoteDtos(appVoteDTOs, appVoteEntries,userId);
        int count = appVoteDao.countGatherAppVotes(userId,groupIds);
        //清除红点
       // redDotService.cleanOtherResult(userId, ApplyTypeEn.piao.getType());
        retMap.put("list", appVoteDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }


    public void removeAppVote(ObjectId appVoteId){
        appVoteDao.removeAppVote(appVoteId);
        //删除首页记录
        indexPageDao.delEntry(appVoteId);
    }


    public Map<String,Object> getMySendAppVote(ObjectId userId, int page, int pageSize){
       Map<String, Object> retMap = new HashMap<String, Object>();
       List<AppVoteDTO> appVoteDTOs = new ArrayList<AppVoteDTO>();
       List<AppVoteEntry> appVoteEntries = appVoteDao.getMySendAppVoteEntries(userId, page, pageSize);
       getVoteDtos(appVoteDTOs, appVoteEntries,userId);
       int count = appVoteDao.countMySendAppVoteEntries(userId);
       retMap.put("list", appVoteDTOs);
       retMap.put("page", page);
       retMap.put("pageSize", pageSize);
       retMap.put("count", count);
       return retMap;
    }

    public AppVoteDTO getOneAppVote(ObjectId userId, ObjectId id){
        AppVoteEntry entry = appVoteDao.getEntry(id);
        List<AppVoteDTO> appVoteDTOs = new ArrayList<AppVoteDTO>();
        List<AppVoteEntry> appVoteEntries = new ArrayList<AppVoteEntry>();
        appVoteEntries.add(entry);
        getVoteDtos(appVoteDTOs, appVoteEntries, userId);
        if(appVoteDTOs.size()>0){
            return appVoteDTOs.get(0);
        }else{
            return null;
        }
    }



    public Map<String, Object> getMyReceivedAppVote(ObjectId userId, int page, int pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<AppVoteDTO> appVoteDTOs = new ArrayList<AppVoteDTO>();
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        List<AppVoteEntry> appVoteEntries = appVoteDao.getMyReceivedAppVoteEntries(groupIds, userId, page, pageSize);
        getVoteDtos(appVoteDTOs, appVoteEntries,userId);
        int count = appVoteDao.countMyReceivedAppVoteEntries(groupIds,userId);
        retMap.put("list", appVoteDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }


    public void getVoteDtos(List<AppVoteDTO> dtos, List<AppVoteEntry> appVoteEntries,ObjectId userId) {
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        Set<ObjectId> groupIds = new HashSet<ObjectId>();
        for (AppVoteEntry appVoteEntry : appVoteEntries) {
            userIds.add(appVoteEntry.getUserId());
            groupIds.add(appVoteEntry.getGroupId());
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        if (userIds.size() > 0) {
            userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
        }
        Map<ObjectId,Map<ObjectId,Integer>> groupMap = new HashMap<ObjectId,Map<ObjectId,Integer>>();
        if(groupIds.size()>0){
            groupMap = memberDao.getMemberGroupManage(new ArrayList<ObjectId>(groupIds));
        }
        for (AppVoteEntry entry : appVoteEntries) {
            AppVoteDTO dto = new AppVoteDTO(entry);
            UserEntry userEntry = userEntryMap.get(entry.getUserId());
            if (null != userEntry) {
                dto.setUserName(StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
            }
            long nowTime = System.currentTimeMillis();
            if (nowTime < entry.getVoteDeadTime()) {
                dto.setVoteDeadFlag(0);
            } else {
                dto.setVoteDeadFlag(1);
            }
            dto.setIsVoted(0);
            FVoteEntry fVoteEntry = fVoteService.getFVote(entry.getID().toString(), userId.toString());
            if (null != fVoteEntry) {
                dto.setIsVoted(1);
            }
            List<VoteResult> voteResults=new ArrayList<VoteResult>();
            List<FVoteDTO> fVoteEntryList = fVoteService.getFVoteList(entry.getID().toString());
            int totalCount = fVoteEntryList.size();
            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(0);
            dto.setVoteCount(totalCount);
            Set<ObjectId> totalUserIds=new HashSet<ObjectId>();
            Map<ObjectId,Long> timeRecord=new HashMap<ObjectId, Long>();
            List<VoteOption> voteOptions = new ArrayList<VoteOption>();
            for (int i = 0; i < entry.getVoteContent().size(); i++) {
                int number=i+1;
                VoteOption voteOption =new VoteOption(number,entry.getVoteContent().get(i));
                voteOptions.add(voteOption);
            }
            dto.setVoteAndroidList(voteOptions);
            for (int i = 0; i < entry.getVoteContent().size(); i++) {
                Set<ObjectId> selectUserIds=new HashSet<ObjectId>();
                VoteResult voteResult = new VoteResult();
                int j = i + 1;
                int count = 0;
                int hasVoted = 0;
                for (FVoteDTO fVoteDTO : fVoteEntryList) {
                    int number = fVoteDTO.getNumber();
                    timeRecord.put(new ObjectId(fVoteDTO.getUserId()),new ObjectId(fVoteDTO.getId()).getTime());
                    if (j == number) {
                        count++;
                        selectUserIds.add(new ObjectId(fVoteDTO.getUserId()));
                        if (new ObjectId(fVoteDTO.getUserId()).equals(userId)) {
                            hasVoted = 1;
                        }
                    }
                }
                totalUserIds.addAll(selectUserIds);
                voteResult.setHasVoted(hasVoted);
                voteResult.setUserIds(MongoUtils.convertToStringList(selectUserIds));
                double pItem = (double) count / (double) totalCount;
                voteResult.setVoteItemStr(entry.getVoteContent().get(i));
                voteResult.setVoteItemCount(count);
                if (count == 0) {
                    voteResult.setVoteItemPercent("0%");
                } else {
                    voteResult.setVoteItemPercent(nt.format(pItem));
                }
                voteResults.add(voteResult);
            }
            Map<ObjectId,UserEntry> idUserEntryMap=userService.getUserEntryMap(totalUserIds,Constant.FIELDS);
            List<User> users=new ArrayList<User>();
            for(Map.Entry<ObjectId,UserEntry> entryEntry:idUserEntryMap.entrySet()){
                UserEntry userEntry1=entryEntry.getValue();
                users.add(new User(userEntry1.getUserName(),userEntry1.getNickName(),userEntry1.getID().toString(),
                        AvatarUtils.getAvatar(userEntry1.getAvatar(), userEntry1.getRole(),userEntry1.getSex()),
                        userEntry1.getSex(), DateTimeUtils.convert(timeRecord.get(userEntry1.getID()),DateTimeUtils.DATE_YYYY_MM_DD)));
            }
            dto.setVoteUsers(users);
            //查询是否大V
            List<ObjectId> userOb = new ArrayList<ObjectId>();
            userIds.add(userId);
            userOb.addAll(userIds);
            List<ObjectId> objectIdList1 = teacherApproveDao.selectMap(userOb);
            for(VoteResult voteResult:voteResults){
                List<User> voteUsers=new ArrayList<User>();
                Set<ObjectId> ItemUserIds=new HashSet<ObjectId>();
                ItemUserIds.addAll(MongoUtils.convertToObjectIdList(voteResult.getUserIds()));
                for(ObjectId id:ItemUserIds){
                    UserEntry user=idUserEntryMap.get(id);
                    if(null!=user){
                        voteUsers.add(new User(user.getUserName(),user.getNickName(),user.getID().toString(),
                                AvatarUtils.getAvatar(user.getAvatar(),user.getRole(),user.getSex()),user.getSex(),
                                DateTimeUtils.convert(timeRecord.get(user.getID()),DateTimeUtils.DATE_YYYY_MM_DD)));
                    }
                }
                voteResult.setVoteUsers(voteUsers);
            }
            dto.setVoteResultList(voteResults);
            dto.setOwner(false);
            dto.setManageDelete(Constant.ZERO);
            if(entry.getUserId().equals(userId)){
                dto.setOwner(true);
                dto.setManageDelete(Constant.ONE);
            }else{
                if(null!=groupMap.get(entry.getGroupId())){
                    Map<ObjectId,Integer> groupUserIds =  groupMap.get(entry.getGroupId());
                    if(null!=groupUserIds.get(userId)){
                        int role = groupUserIds.get(userId);
                        if(null!=groupUserIds.get(entry.getUserId())){
                            int userRole = groupUserIds.get(entry.getUserId());
                            if(role>userRole){
                                dto.setManageDelete(Constant.ONE);
                            }else{
                                if(userRole==0){//同为普通成员
                                    if(objectIdList1.contains(userId) && !objectIdList1.contains(entry.getUserId())){
                                        dto.setManageDelete(Constant.ONE);//我是大V        你不是
                                    }
                                }
                            }
                        }else{
                            dto.setManageDelete(Constant.ONE);
                        }
                    }
                }
            }
            dto.setSubmitTime(TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
            dtos.add(dto);
        }
    }

    /**
     * 查询学生接收的投票列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String, Object> getStudentReceivedAppVotes(ObjectId userId, int page, int pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<AppVoteDTO> appVoteDTOs = new ArrayList<AppVoteDTO>();
        List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getAllStudentBindEntries(userId);
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
            communityIds.add(bindEntry.getCommunityId());
        }
        List<ObjectId> groupIds = communityDao.getGroupIdsByCommunityIds(communityIds);
        List<AppVoteEntry> appVoteEntries = appVoteDao.getStudentReceivedEntries(groupIds, page, pageSize);
        getVoteDtos(appVoteDTOs, appVoteEntries,userId);

        int count = appVoteDao.countStudentReceivedEntries(groupIds);
        retMap.put("list", appVoteDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }


    public void transferActivity(){
        int page=1;
        int pageSize=200;
        boolean flag=true;
        while (flag){
            List<CommunityDetailEntry> communityDetailEntries =
                    communityDetailDao.getDetailEntries(Constant.TWO,page,pageSize);
            if(communityDetailEntries.size()>0){
                for(CommunityDetailEntry communityDetailEntry:communityDetailEntries){
                    ObjectId activityId= communityDetailEntry.getID();
                    List<ObjectId> partInList = communityDetailEntry.getPartInList();
                    List<AppActivityUserEntry> entries = new ArrayList<AppActivityUserEntry>();
                    CommunityEntry communityEntry = communityDao.findByObjectId(new ObjectId(communityDetailEntry.getCommunityId()));
                    AppActivityEntry appActivityEntry = new AppActivityEntry(
                            new ObjectId("59dc8a68bf2e791a140769b4"),
                            communityDetailEntry.getCommunityUserId(),
                            "其他",
                            communityDetailEntry.getCommunityTitle(),
                            communityDetailEntry.getCommunityContent(),
                            communityEntry.getGroupId(),
                            new ObjectId(communityDetailEntry.getCommunityId()),
                            communityEntry.getCommunityName(),
                            communityDetailEntry.getImageList(),
                            communityDetailEntry.getVideoList(),
                            Constant.THREE
                    );
                    ObjectId newItemId= new ObjectId();
                    appActivityEntry.setID(newItemId);
                    for(ObjectId partInId:partInList){
                        AppActivityUserEntry userEntry = new AppActivityUserEntry(newItemId,partInId);
                        entries.add(userEntry);
                    }
                    TransferVoteAndActivityEntry entry= transferVoteAndActivityDao.getEntryByTransferId(activityId);
                    if(null!=entry){
                        ObjectId generateId = entry.getNewItemId();
                        appActivityDao.removeById(generateId);
                        appActivityUserDao.removeActivityData(generateId);
                        transferVoteAndActivityDao.removeEntry(activityId);
                    }
                    appActivityDao.saveEntry(appActivityEntry);
                    if(entries.size()>0){
                        appActivityUserDao.saveEntries(entries);
                    }
                    transferVoteAndActivityDao.saveEntry(new TransferVoteAndActivityEntry(activityId,newItemId,Constant.TWO));
                }
            }else{
                flag=false;
            }
            page++;
        }
    }


    public void transferVote(){
        int page=1;
        int pageSize=200;
        boolean flag =true;
        while(flag){
            List<CommunityDetailEntry> communityDetailEntries = communityDetailDao.getDetailEntries(Constant.SEVEN,page,pageSize);
            if(communityDetailEntries.size()>0){
                for(CommunityDetailEntry communityDetailEntry:communityDetailEntries){
                    ObjectId voteId= communityDetailEntry.getID();
                    String voteContent = communityDetailEntry.getVoteContent();
                    List<String> voteOptions = new ArrayList<String>();
                    if(StringUtils.isNotEmpty(voteContent)) {
                        if (voteContent.contains("/n/r")) {
                            String[] str = voteContent.split("/n/r");
                            for (String item : str) {
                                voteOptions.add(item);
                            }
                        } else {
                            voteOptions.add(voteContent);
                        }
                    }
                    List<FVoteEntry> fVoteEntries = fVoteDao.getFVoteList(voteId);
                    List<FVoteEntry> voteEntries = new ArrayList<FVoteEntry>();
                    CommunityEntry communityEntry = communityDao.findByObjectId(new ObjectId(communityDetailEntry.getCommunityId()));
                    AppVoteEntry appVoteEntry = new AppVoteEntry(new ObjectId("59dc8a68bf2e791a140769b4"),
                            communityDetailEntry.getCommunityUserId(),
                            "其他",
                            communityDetailEntry.getCommunityTitle(),
                            communityDetailEntry.getCommunityContent(),
                            communityEntry.getGroupId(),
                            new ObjectId(communityDetailEntry.getCommunityId()),
                            communityEntry.getCommunityName(),
                            communityDetailEntry.getImageList(),
                            voteOptions,
                            communityDetailEntry.getVoteMaxCount(),
                            communityDetailEntry.getVoteDeadTime(),
                            communityDetailEntry.getVoteType(),
                            Constant.THREE);
                    ObjectId newItemId= new ObjectId();
                    appVoteEntry.setID(newItemId);
                    for(FVoteEntry fVoteEntry:fVoteEntries){
                        voteEntries.add(new FVoteEntry(newItemId,fVoteEntry.getUserId(),fVoteEntry.getNumber()));
                    }
                    TransferVoteAndActivityEntry entry= transferVoteAndActivityDao.getEntryByTransferId(voteId);
                    if(null!=entry){
                        ObjectId generateId = entry.getNewItemId();
                        fVoteDao.removeOldData(generateId);
                        appVoteDao.removeById(generateId);
                        transferVoteAndActivityDao.removeEntry(voteId);
                    }
                    appVoteDao.saveAppVote(appVoteEntry);
                    if(voteEntries.size()>0) {
                        fVoteDao.addFVoteList(voteEntries);
                    }
                    transferVoteAndActivityDao.saveEntry(new TransferVoteAndActivityEntry(voteId,newItemId,Constant.SEVEN));
                }
            }else{
                flag=false;
            }
            page++;
        }
    }
}
