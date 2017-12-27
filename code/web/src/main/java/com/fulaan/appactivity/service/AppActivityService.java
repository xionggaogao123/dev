package com.fulaan.appactivity.service;

import com.db.appactivity.AppActivityDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.fulaan.appactivity.dto.AppActivityDTO;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.user.service.UserService;
import com.pojo.appactivity.AppActivityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.TimeChangeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by scott on 2017/12/27.
 */
@Service
public class AppActivityService {

    @Autowired
    private UserService userService;


    private MemberDao memberDao = new MemberDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private AppActivityDao appActivityDao = new AppActivityDao();

    private CommunityDao communityDao = new CommunityDao();


    public void saveEntry(AppActivityDTO appActivityDTO){
        List<AppActivityEntry> entries = new ArrayList<AppActivityEntry>();
        for (GroupOfCommunityDTO dto : appActivityDTO.getGroupOfCommunityDTOs()) {
            AppActivityDTO item = new AppActivityDTO(
                    appActivityDTO.getSubjectId(),
                    appActivityDTO.getUserId(),
                    appActivityDTO.getSubjectName(),
                    appActivityDTO.getTitle(),
                    appActivityDTO.getContent(),
                    appActivityDTO.getImageList(),
                    appActivityDTO.getVideoDTOs(),
                    appActivityDTO.getVisiblePermission(),
                    dto.getGroupId(),
                    dto.getCommunityId(),
                    dto.getGroupName()
            );
            entries.add(item.buildEntry());
        }
        appActivityDao.saveEntries(entries);
    }

    public void getDtosByEntries(List<AppActivityDTO> appActivityDTOs,List<AppActivityEntry> entries, ObjectId userId){
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for (AppActivityEntry appActivityEntry : entries) {
            userIds.add(appActivityEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        if (userIds.size() > 0) {
            userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
        }

        for (AppActivityEntry entry : entries) {
            AppActivityDTO appActivityDTO = new AppActivityDTO(entry);
            UserEntry userEntry = userEntryMap.get(entry.getUserId());
            if (null != userEntry) {
                appActivityDTO.setUserName(StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                appActivityDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
            }
            List<ObjectId> partInList=entry.getPartInList();
            appActivityDTO.setPartIn(false);
            if(partInList.contains(userId)){
                appActivityDTO.setPartIn(true);
            }
            appActivityDTO.setOwner(false);
            if(entry.getUserId().equals(userId)){
                appActivityDTO.setOwner(true);
            }
            appActivityDTO.setSubmitTime(TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
            int totalCount=0;
            if(entry.getVisiblePermission()==Constant.ONE){
                totalCount=memberDao.getMemberCount(entry.getGroupId());
            }else{
                totalCount=newVersionCommunityBindDao.countStudentIdListByCommunityId(entry.getCommunityId());
            }
            appActivityDTO.setTotalCount(totalCount);
            appActivityDTOs.add(appActivityDTO);
        }
    }


    public Map<String,Object> getGatherAppActivities(ObjectId userId,int page,int pageSize){
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<AppActivityDTO> appActivityDTOs = new ArrayList<AppActivityDTO>();
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        List<AppActivityEntry> appActivityEntries = appActivityDao.getGatherActivities(userId,groupIds,page, pageSize);
        getDtosByEntries(appActivityDTOs, appActivityEntries,userId);
        int count = appActivityDao.countGatherActivities(userId,groupIds);
        retMap.put("list", appActivityDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }

    public void partInActivity(ObjectId activityId,int type,ObjectId userId)throws Exception{
        AppActivityEntry appActivityEntry = appActivityDao.getEntryById(activityId);
        List<ObjectId> partInList = appActivityEntry.getPartInList();
        if(type==Constant.ONE){
            if(partInList.contains(userId)){
                throw  new Exception("已经报名了");
            }else{
                appActivityDao.partInActivity(activityId,userId);
            }
        }else{
            if(partInList.contains(userId)){
                appActivityDao.popActivity(activityId,userId);
            }else{
                throw  new Exception("已经取消报名了");
            }
        }
    }


    public Map<String, Object> getStudentReceivedAppVotes(ObjectId userId, int page, int pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<AppActivityDTO> appActivityDTOs = new ArrayList<AppActivityDTO>();
        List<NewVersionCommunityBindEntry> bindEntries = newVersionCommunityBindDao.getAllStudentBindEntries(userId);
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (NewVersionCommunityBindEntry bindEntry : bindEntries) {
            communityIds.add(bindEntry.getCommunityId());
        }
        List<ObjectId> groupIds = communityDao.getGroupIdsByCommunityIds(communityIds);
        List<AppActivityEntry> appActivityEntries = appActivityDao.getStudentActivities(groupIds, page, pageSize);
        getDtosByEntries(appActivityDTOs, appActivityEntries,userId);

        int count = appActivityDao.countStudentActivities(groupIds);
        retMap.put("list", appActivityDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }


}
