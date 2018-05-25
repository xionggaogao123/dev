package com.fulaan.appactivity.service;

import com.db.appactivity.AppActivityDao;
import com.db.appactivity.AppActivityUserDao;
import com.db.backstage.TeacherApproveDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.fulaan.appactivity.dto.AppActivityDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.appactivity.AppActivityEntry;
import com.pojo.appactivity.AppActivityUserEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.integral.IntegralType;
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

    private RedDotService redDotService = new RedDotService();

    private IntegralSufferService integralSufferService = new IntegralSufferService();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();


    public String saveEntry(AppActivityDTO appActivityDTO){
        List<AppActivityEntry> entries = new ArrayList<AppActivityEntry>();
        List<ObjectId> oids = new ArrayList<ObjectId>();
        for (GroupOfCommunityDTO dto : appActivityDTO.getGroupOfCommunityDTOs()) {
            oids.add(new ObjectId(dto.getCommunityId()));
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
            if(appActivityDTO.getVisiblePermission()==1){//发送给家长
                //发送通知
                PictureRunNable.addTongzhi(item.getCommunityId(), item.getUserId(), 5);
            }
            entries.add(item.buildEntry());
        }
        if(appActivityDTO.getVisiblePermission()==1){//红点
            //添加红点
            redDotService.addOtherEntryList(oids, new ObjectId(appActivityDTO.getUserId()), ApplyTypeEn.piao.getType(), 1);
        }else{
            //添加红点
            redDotService.addOtherEntryList(oids, new ObjectId(appActivityDTO.getUserId()), ApplyTypeEn.piao.getType(),2);
        }
        appActivityDao.saveEntries(entries);
        int score = integralSufferService.addIntegral(new ObjectId(appActivityDTO.getUserId()), IntegralType.vote,4,1);
        return score+"";
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
        userIds.add(userId);
        Map<ObjectId,Map<ObjectId,Integer>> groupMap = new HashMap<ObjectId,Map<ObjectId,Integer>>();
        if(groupIds.size()>0){
            groupMap = memberDao.getMemberGroupManage(new ArrayList<ObjectId>(groupIds));
        }
        //查询是否大V
        List<ObjectId> userOb = new ArrayList<ObjectId>();
        userOb.addAll(userIds);
        List<ObjectId> objectIdList1 = teacherApproveDao.selectMap(userOb);
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
                    Map<ObjectId,Integer> groupUserIds =  groupMap.get(entry.getGroupId());
                    if(null!=groupUserIds.get(userId)){
                        int role = groupUserIds.get(userId);
                        if(null!=groupUserIds.get(entry.getUserId())){
                            int userRole = groupUserIds.get(entry.getUserId());
                            if(role>userRole){
                                appActivityDTO.setManageDelete(Constant.ONE);
                            }else{
                                if(userRole==0){//同为普通成员
                                    if(objectIdList1.contains(userId) && !objectIdList1.contains(entry.getUserId())){
                                        appActivityDTO.setManageDelete(Constant.ONE);//我是大V        你不是
                                    }
                                }
                            }
                        }else{
                            appActivityDTO.setManageDelete(Constant.ONE);
                        }
                    }
                }
            }
            appActivityDTO.setSubmitTime(TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
            int totalCount=0;
            if(entry.getVisiblePermission()==Constant.ONE){
                totalCount=memberDao.getMemberCount(entry.getGroupId());
            }else if(entry.getVisiblePermission()==Constant.TWO){
//                totalCount=newVersionCommunityBindDao.countStudentIdListByCommunityId(entry.getCommunityId());
                List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao.getStudentIdListByCommunityId(entry.getCommunityId());
                List<ObjectId> uIds = new ArrayList<ObjectId>();
                for(NewVersionCommunityBindEntry bindEntry:bindEntries){
                    uIds.add(bindEntry.getUserId());
                }
                List<ObjectId> filterUserIds = userService.filterAvailableObjectIds(uIds);
                totalCount=filterUserIds.size();
            }else{
                int memberCount=memberDao.getMemberCount(entry.getGroupId());
                List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao.getStudentIdListByCommunityId(entry.getCommunityId());
                List<ObjectId> uIds = new ArrayList<ObjectId>();
                for(NewVersionCommunityBindEntry bindEntry:bindEntries){
                    uIds.add(bindEntry.getUserId());
                }
                List<ObjectId> filterUserIds = userService.filterAvailableObjectIds(uIds);
                int studentCount=filterUserIds.size();
                totalCount=memberCount+studentCount;
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
        }else if(appActivityEntry.getVisiblePermission()==Constant.TWO){
            List<NewVersionCommunityBindEntry> entries =newVersionCommunityBindDao.getStudentIdListByCommunityId(
                    appActivityEntry.getCommunityId()
            );
            for(NewVersionCommunityBindEntry communityBindEntry:entries){
                userIds.add(communityBindEntry.getUserId());
            }
        }else{
            List<MemberEntry> memberEntries = memberDao.getAllMembers(appActivityEntry.getGroupId());
            for(MemberEntry memberEntry:memberEntries){
                userIds.add(memberEntry.getUserId());
            }
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
                        AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
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
                        AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
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
        //清除红点
        redDotService.cleanOtherResult(userId, ApplyTypeEn.piao.getType());
        retMap.put("list", appActivityDTOs);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        retMap.put("count", count);
        return retMap;
    }


    public void removeActivity(ObjectId activityId){
        appActivityDao.removeActivity(activityId);
    }

    public String partInActivity(ObjectId activityId,int type,ObjectId userId)throws Exception{
        AppActivityUserEntry entry =appActivityUserDao.getEntry(activityId,userId);
        if(type==Constant.ONE){
            if(null!=entry){
                throw  new Exception("已经报名了");
            }else {
                appActivityUserDao.saveEntry(new AppActivityUserEntry(activityId,userId));
                appActivityDao.partInActivity(activityId);
            }
            int score = integralSufferService.addIntegral(userId, IntegralType.vote,4,1);
            return score+"";
        }else{
            if(null==entry){
                throw  new Exception("已经取消报名了");
            }else{
                appActivityUserDao.removeEntry(activityId, userId);
                appActivityDao.popActivity(activityId);
            }
            return "0";
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
