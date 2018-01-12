package com.fulaan.appactivity.service;

import com.db.appactivity.AppActivityDao;
import com.db.appactivity.AppActivityUserDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.fulaan.appactivity.dto.AppActivityDTO;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.appactivity.AppActivityEntry;
import com.pojo.appactivity.AppActivityUserEntry;
import com.pojo.fcommunity.MemberEntry;
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

    private AppActivityUserDao appActivityUserDao = new AppActivityUserDao();


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
        Set<ObjectId> groupIds = new HashSet<ObjectId>();
        for (AppActivityEntry appActivityEntry : entries) {
            userIds.add(appActivityEntry.getUserId());
            groupIds.add(appActivityEntry.getGroupId());
        }
        Map<ObjectId, UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        if (userIds.size() > 0) {
            userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
        }

        Map<ObjectId,List<ObjectId>> groupMap = new HashMap<ObjectId, List<ObjectId>>();
        if(groupIds.size()>0){
            groupMap = memberDao.getMemberGroupManage(new ArrayList<ObjectId>(groupIds));
        }

        for (AppActivityEntry entry : entries) {
            AppActivityDTO appActivityDTO = new AppActivityDTO(entry);
            UserEntry userEntry = userEntryMap.get(entry.getUserId());
            if (null != userEntry) {
                appActivityDTO.setUserName(StringUtils.isNotEmpty(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName());
                appActivityDTO.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
            }
            appActivityDTO.setPartIn(false);
            AppActivityUserEntry entry1 =appActivityUserDao.getEntry(entry.getID(),userId);
            if(null!=entry1){
                appActivityDTO.setPartIn(true);
            }
            appActivityDTO.setOwner(false);
            appActivityDTO.setManageDelete(Constant.ZERO);
            if(entry.getUserId().equals(userId)){
                appActivityDTO.setOwner(true);
                appActivityDTO.setManageDelete(Constant.ONE);
            }else{
                if(null!=groupMap.get(entry.getGroupId())){
                    List<ObjectId> groupUserIds =  groupMap.get(entry.getGroupId());
                    if(groupUserIds.contains(userId)){
                        appActivityDTO.setManageDelete(Constant.ONE);
                    }
                }
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

    /**
     * 查询该活动的报名列表
     * @param activityId
     * @return
     */
    public Map<String,Object> getPartInActivityUserList(ObjectId activityId){
        Map<String,Object> result = new HashMap<String,Object>();
        List<User> sign = new ArrayList<User>();
        List<User> unSign = new ArrayList<User>();
        AppActivityEntry appActivityEntry =appActivityDao.getEntryById(activityId);
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        if(appActivityEntry.getVisiblePermission()==Constant.ONE){
            List<MemberEntry> memberEntries = memberDao.getAllMembers(appActivityEntry.getGroupId());
            for(MemberEntry memberEntry:memberEntries){
                userIds.add(memberEntry.getUserId());
            }
        }else{
            List<NewVersionCommunityBindEntry> entries =newVersionCommunityBindDao.getStudentIdListByCommunityId(
                    appActivityEntry.getCommunityId()
            );
            for(NewVersionCommunityBindEntry communityBindEntry:entries){
                userIds.add(communityBindEntry.getUserId());
            }
        }
        List<AppActivityUserEntry> entries =appActivityUserDao.getEntries(activityId);
        Set<ObjectId> unSignUserIds = new HashSet<ObjectId>();
        Set<ObjectId> signUserIds = new HashSet<ObjectId>();
        Map<ObjectId,String> signRecord = new HashMap<ObjectId, String>();
        for(AppActivityUserEntry entry:entries){
            signUserIds.add(entry.getUserId());
            signRecord.put(entry.getUserId(), TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
        }
        unSignUserIds.addAll(userIds);
        unSignUserIds.removeAll(signUserIds);
        Set<ObjectId> allUserIds = new HashSet<ObjectId>();
        allUserIds.addAll(unSignUserIds);
        allUserIds.addAll(signUserIds);
        Map<ObjectId,UserEntry> userEntryMap = userService.getUserEntryMap(allUserIds,Constant.FIELDS);
        for(ObjectId uId:signUserIds){
            String time=signRecord.get(uId);
            UserEntry userEntry=userEntryMap.get(uId);
            if(StringUtils.isNotEmpty(time)&&null!=userEntry){
                User user = new User(userEntry.getUserName(),
                       userEntry.getNickName(),
                        userEntry.getID().toString(),
                        AvatarUtils.getAvatar2(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
                userEntry.getSex(),time);
                sign.add(user);
            }
        }
        for(ObjectId uId:unSignUserIds){
            UserEntry userEntry=userEntryMap.get(uId);
            if(null!=userEntry){
                User user = new User(userEntry.getUserName(),
                        userEntry.getNickName(),
                        userEntry.getID().toString(),
                        AvatarUtils.getAvatar2(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
                        userEntry.getSex(),Constant.EMPTY);
                unSign.add(user);
            }
        }
        result.put("SignList",sign);
        result.put("SignListNum",sign.size());
        result.put("UnSignList",unSign);
        result.put("UnSignListNum",unSign.size());
        return result;
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


    public void removeActivity(ObjectId activityId){
        appActivityDao.removeActivity(activityId);
    }

    public void partInActivity(ObjectId activityId,int type,ObjectId userId)throws Exception{
        AppActivityUserEntry entry =appActivityUserDao.getEntry(activityId,userId);
        if(type==Constant.ONE){
            if(null!=entry){
                throw  new Exception("已经报名了");
            }else {
                appActivityUserDao.saveEntry(new AppActivityUserEntry(activityId,userId));
                appActivityDao.partInActivity(activityId);
            }
        }else{
            if(null==entry){
                throw  new Exception("已经取消报名了");
            }else{
                appActivityUserDao.removeEntry(activityId, userId);
                appActivityDao.popActivity(activityId);
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
