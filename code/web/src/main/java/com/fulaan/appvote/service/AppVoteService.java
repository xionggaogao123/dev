package com.fulaan.appvote.service;

import com.db.appvote.AppVoteDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.fulaan.appvote.dto.AppVoteDTO;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.user.service.UserService;
import com.pojo.appvote.AppVoteEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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





    public Map<String, Object> getMyReceivedAppVote(ObjectId userId, int page, int pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<AppVoteDTO> appVoteDTOs = new ArrayList<AppVoteDTO>();
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        List<AppVoteEntry> appVoteEntries = appVoteDao.getMyReceivedAppVoteEntries(groupIds, userId, page, pageSize);
        getVoteDtos(appVoteDTOs, appVoteEntries);
        int count = appVoteDao.countMyReceivedAppVoteEntries(groupIds,userId);
        retMap.put("list", appVoteDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }


    public void getVoteDtos(List<AppVoteDTO> dtos, List<AppVoteEntry> appVoteEntries) {
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
        getVoteDtos(appVoteDTOs, appVoteEntries);

        int count = appVoteDao.countStudentReceivedEntries(groupIds);
        retMap.put("list", appVoteDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }
}
