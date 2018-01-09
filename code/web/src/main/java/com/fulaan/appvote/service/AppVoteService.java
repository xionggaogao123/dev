package com.fulaan.appvote.service;

import com.db.appvote.AppVoteDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.fulaan.appvote.dto.AppVoteDTO;
import com.fulaan.appvote.dto.VoteResult;
import com.fulaan.community.dto.CommunityDetailDTO;
import com.fulaan.forum.service.FVoteService;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.appvote.AppVoteEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.forum.FVoteDTO;
import com.pojo.forum.FVoteEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.StringUtil;
import com.sys.utils.TimeChangeUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
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

    private MemberDao memberDao=new MemberDao();

    @Autowired
    private FVoteService fVoteService;

    @Autowired
    private UserService userService;


    public void saveAppVote(AppVoteDTO appVoteDTO) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
        Date date = format.parse(appVoteDTO.getDeadFormatTime());
        List<AppVoteEntry> entries = new ArrayList<AppVoteEntry>();
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
            entries.add(item.buildEntry());
        }
        appVoteDao.saveEntries(entries);
    }

    public Map<String,Object> gatherAppVotes(ObjectId userId, int page, int pageSize){
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        List<AppVoteDTO> appVoteDTOs = new ArrayList<AppVoteDTO>();
        List<AppVoteEntry> appVoteEntries = appVoteDao.getGatherAppVoteEntries(userId, groupIds,page, pageSize);
        getVoteDtos(appVoteDTOs, appVoteEntries,userId);
        int count = appVoteDao.countGatherAppVotes(userId,groupIds);
        retMap.put("list", appVoteDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }


    public void removeAppVote(ObjectId appVoteId){
        appVoteDao.removeAppVote(appVoteId);
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
        for (AppVoteEntry appVoteEntry : appVoteEntries) {
            userIds.add(appVoteEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        if (userIds.size() > 0) {
            userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
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
            if(entry.getUserId().equals(userId)){
                dto.setOwner(true);
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
}
