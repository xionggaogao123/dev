package com.fulaan.operation.service;

import cn.jpush.api.push.model.audience.Audience;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.IndexPageDao;
import com.db.indexPage.WebHomePageDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JPushUtils;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.backstage.PictureType;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.operation.AppOperationEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.TimeChangeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by scott on 2017/9/22.
 */
@Service
public class AppNoticeService {

    private AppNoticeDao appNoticeDao=new AppNoticeDao();

    private MemberDao memberDao=new MemberDao();

    private AppOperationDao appOperationDao = new AppOperationDao();

    private CommunityDao communityDao=new CommunityDao();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao=new NewVersionCommunityBindDao();

    private WebHomePageDao webHomePageDao = new WebHomePageDao();

    @Autowired
    private UserService userService;
    @Autowired
    private RedDotService redDotService;

    @Autowired
    private NewVersionBindService newVersionBindService;


    public static void main(String[] args){
        List<String> thistags =new ArrayList<String>();
        thistags.add("59e81c77bf2e7906ec403844");
        Audience audience = Audience.alias(thistags);
        JPushUtils jPushUtils=new JPushUtils();
        jPushUtils.pushRestAndroid(audience, "江经纬同学", "江经纬同学---江经纬同学", "您有新的通知", new HashMap<String, String>());
//        jPushUtils.pushRestWinPhone(audience, "江经纬同学");
    }

    /**
     * 保存信息
     * @param dto
     */
    public void saveAppNoticeEntry(AppNoticeDTO dto,ObjectId userId)throws Exception{
        UserEntry userEntry=userService.findById(userId);
        JPushUtils jPushUtils=new JPushUtils();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(GroupOfCommunityDTO communityDTO:dto.getGroupOfCommunityDTOs()){
            AppNoticeDTO appNoticeDTO=new AppNoticeDTO(
                    dto.getSubjectId(),
                    dto.getSubject(),
                    dto.getTitle(),
                    dto.getContent(),
                    communityDTO.getGroupId(),
                    communityDTO.getCommunityId(),
                    dto.getWatchPermission(),
                    dto.getVideoList(),
                    dto.getImageList(),
                    dto.getAttachements(),
                    dto.getVoiceList(),
                    communityDTO.getGroupName(),
                    userEntry.getUserName());
            appNoticeDTO.setUserId(userId.toString());
            ObjectId oid = appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());


            //图片检测
            List<Attachement> alist = appNoticeDTO.getImageList();
            if(alist != null && alist.size()>0){
                for(Attachement entry5 : alist){
                    PictureRunNable.send(oid.toString(), userId.toString(), PictureType.answerImage.getType(), 1, entry5.getUrl());
                }
            }

            //1:家长2:学生3:家长，学生
            redDotService.addEntryList(objectIdList,userId, ApplyTypeEn.notice.getType(),dto.getWatchPermission());
            redDotService.addOtherEntryList(objectIdList,userId, ApplyTypeEn.daynotice.getType(),dto.getWatchPermission());

            //添加临时记录表
            if(dto.getWatchPermission()!=2){
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.appNotice.getType());
                dto1.setUserId(userId.toString());
                dto1.setCommunityId(communityDTO.getCommunityId());
                objectIdList.add(new ObjectId(communityDTO.getCommunityId()));
                dto1.setContactId(oid.toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);

                WebHomePageEntry pageEntry=new WebHomePageEntry(Constant.TWO, userId,
                        StringUtils.isNotEmpty(communityDTO.getCommunityId())?
                                new ObjectId(communityDTO.getCommunityId()):null, oid, StringUtils.isNotEmpty(dto.getSubjectId())?
                        new ObjectId(dto.getSubjectId()):null,
                        null,null,Constant.ZERO
                );
                webHomePageDao.saveWebHomeEntry(pageEntry);
            }else{
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.appOtherNotice.getType());
                dto1.setUserId(userId.toString());
                dto1.setCommunityId(communityDTO.getCommunityId());
                objectIdList.add(new ObjectId(communityDTO.getCommunityId()));
                dto1.setContactId(oid.toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);

                WebHomePageEntry pageEntry=new WebHomePageEntry(Constant.FOUR, userId,
                        StringUtils.isNotEmpty(communityDTO.getCommunityId())?
                                new ObjectId(communityDTO.getCommunityId()):null, oid, StringUtils.isNotEmpty(dto.getSubjectId())?
                        new ObjectId(dto.getSubjectId()):null,
                        null,null,Constant.ZERO
                );
                webHomePageDao.saveWebHomeEntry(pageEntry);
            }
        }
        try {
            for (GroupOfCommunityDTO communityDTO : dto.getGroupOfCommunityDTOs()) {
                if (StringUtils.isNotBlank(communityDTO.getGroupId())) {
                    List<MemberEntry> memberEntries = memberDao.getAllMembers(new ObjectId(communityDTO.getGroupId()));
                    Set<String> userIds = new HashSet<String>();
                    for (MemberEntry memberEntry : memberEntries) {
                        userIds.add(memberEntry.getUserId().toString());
                    }
                    Audience audience = Audience.alias(new ArrayList<String>(userIds));
                    jPushUtils.pushRestIosbusywork(audience, dto.getTitle(), new HashMap<String, String>());
                    jPushUtils.pushRestAndroidParentBusyWork(audience, dto.getContent(), "", dto.getTitle(), new HashMap<String, String>());
                    List<String> bindUserIds = newVersionBindService.getStudentIdListByCommunityId(new ObjectId(communityDTO.getCommunityId()));
                    if (bindUserIds.size() > 0) {
                        Audience studentAudience = Audience.alias(new ArrayList<String>(bindUserIds));
                        jPushUtils.pushRestAndroidStudentNotice(studentAudience, dto.getContent(), "", dto.getTitle(), new HashMap<String, String>());
                    }
                }
            }
        }catch (Exception e){
            throw new Exception("推送失败");
        }


    }


    public void removeAppNoticeEntry(ObjectId noticeId,ObjectId userId)throws Exception{
        AppNoticeEntry appNoticeEntry=appNoticeDao.getAppNoticeEntry(noticeId);
        if(null!=appNoticeEntry) {
            if(null!=appNoticeEntry.getUserId()&&null!=userId
                    &&appNoticeEntry.getUserId().toString().equals(userId.toString())){
                appNoticeDao.removeAppNoticeEntry(noticeId);
                //删除首页记录
                indexPageDao.delEntry(noticeId);
                //删除首页
                webHomePageDao.removeContactId(noticeId);
            }else {
                throw new Exception("你没有权限删除该通知!");
            }

        }
    }

    /**
     * 查询已阅和未阅列表
     * @param id
     * @return
     */
    public Map<String,List<User>> getReadNoticeUserList(ObjectId id){
        Map<String,List<User>> userMap=new HashMap<String, List<User>>();
        AppNoticeEntry entry=appNoticeDao.getAppNoticeEntry(id);
        List<ObjectId> reads=entry.getReaList();
        List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
        members.remove(entry.getUserId());
        members.removeAll(reads);
        List<User> read=new ArrayList<User>();
        List<User> unRead=new ArrayList<User>();
        if(reads.size()>0){
            saveUser(read,reads);
        }
        if(members.size()>0){
            saveUser(unRead,members);
        }
        userMap.put("read",read);
        userMap.put("unRead",unRead);
        return userMap;
    }



    public void saveUser(List<User> users,List<ObjectId> userIds){
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(Map.Entry<ObjectId,UserEntry> userEntryEntry:userEntryMap.entrySet()){
            UserEntry userEntry=userEntryEntry.getValue();
            User user=new User(userEntry.getUserName(),
                    userEntry.getNickName(),
                    userEntry.getID().toString(),
                    AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
                    userEntry.getSex(),
                    "");
            users.add(user);
        }
    }


    public Map<String,Object> getMyAppNotices(String cId,ObjectId userId,int page,int pageSize){
        Map<String,Object> result=new HashMap<String,Object>();
        ObjectId communityId=StringUtils.isNotEmpty(cId)?new ObjectId(cId):null;
        List<AppNoticeEntry> entries=appNoticeDao.getMyAppNotices(communityId,userId,page,pageSize);
        int count=appNoticeDao.countMyAppNotices(communityId,userId);
        List<AppNoticeDTO> appNoticeDTOs=getAppNoticeDtos(entries,userId);
        result.put("list",appNoticeDTOs);
        result.put("count",count);
        result.put("page",page);
        result.put("pageSize",pageSize);
        return result;
    }


    /**
     * 获取我发送的通知
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String,Object> getMySendAppNoticeDtos(ObjectId userId,int page,int pageSize){
        List<AppNoticeDTO> dtos=new ArrayList<AppNoticeDTO>();
        Map<String,Object> retMap=new HashMap<String,Object>();
        List<AppNoticeEntry> entries=appNoticeDao.getMySendAppNoticeEntries(userId,page,pageSize);
        UserEntry userEntry=userService.findById(userId);
        for(AppNoticeEntry entry:entries){
            AppNoticeDTO dto=new AppNoticeDTO(entry);
            dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
            dto.setUserName(StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            dto.setIsRead(0);
            if(dto.getReadList().contains(userId.toString())){
                dto.setIsRead(1);
            }
            //设置已阅和未阅的人数
            List<ObjectId> reads=entry.getReaList();
            List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
            members.remove(userId);
            dto.setTotalReadCount(members.size());
            members.removeAll(reads);
            dto.setReadCount(reads.size());
            dto.setUnReadCount(members.size());
            dto.setTimeExpression(TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
            dtos.add(dto);
        }
        int count=appNoticeDao.countMySendAppNoticeEntries(userId);
        retMap.put("page",page);
        retMap.put("pageSize",pageSize);
        retMap.put("list",dtos);
        retMap.put("count",count);
        return retMap;
    }


    public List<AppNoticeDTO> searchAppNotice(String keyWord,ObjectId userId,int page,int pageSize){
        List<AppNoticeEntry> entries=appNoticeDao.searchAppNotice(keyWord,page,pageSize);
        return getAppNoticeDtos(entries,userId);
    }


    public Map<String,Object> getMyReceivedAppNoticeDtosForStudent(ObjectId userId,int page,int pageSize){
        Map<String,Object> retMap=new HashMap<String,Object>();
        List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao.getAllStudentBindEntries(userId);
        List<ObjectId> communityIds=new ArrayList<ObjectId>();
        for(NewVersionCommunityBindEntry bindEntry:bindEntries){
            communityIds.add(bindEntry.getCommunityId());
        }
        List<ObjectId> groupIds=communityDao.getGroupIdsByCommunityIds(communityIds);
        List<AppNoticeEntry> entries=appNoticeDao.getMyReceivedAppNoticeEntriesForStudent(groupIds,page,pageSize);
        List<AppNoticeDTO> dtos=getAppNoticeDtos(entries,userId);
        int count=appNoticeDao.countMyReceivedAppNoticeEntriesForStudent(groupIds);
        retMap.put("list",dtos);
        retMap.put("count",count);
        retMap.put("page",page);
        retMap.put("pageSize",pageSize);
        //清除红点
        redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
        return retMap;
    }

    public  List<AppNoticeDTO> getAppNoticeDtos( List<AppNoticeEntry> entries,ObjectId userId){
        List<AppNoticeDTO> dtos=new ArrayList<AppNoticeDTO>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for(AppNoticeEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(AppNoticeEntry entry:entries){
            AppNoticeDTO dto=new AppNoticeDTO(entry);
            UserEntry userEntry=userEntryMap.get(entry.getUserId());
            if(null!=userEntry){
                dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                dto.setUserName(StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName());
            }
            dto.setIsRead(0);
            if(dto.getReadList().contains(userId.toString())){
                dto.setIsRead(1);
            }
            dto.setOwner(false);
            if(entry.getUserId().equals(userId)){
                //设置已阅和未阅的人数
                List<ObjectId> reads=entry.getReaList();
                List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
                members.remove(userId);
                dto.setTotalReadCount(members.size());
                members.removeAll(reads);
                dto.setReadCount(reads.size());
                dto.setUnReadCount(members.size());
                dto.setOwner(true);
            }
            dto.setTimeExpression(TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 获取我接收到的通知
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String,Object> getMyReceivedAppNoticeDtos(ObjectId userId,int page,int pageSize){
        Map<String,Object> retMap=new HashMap<String,Object>();
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        List<AppNoticeEntry> entries=appNoticeDao.getMyReceivedAppNoticeEntries(groupIds,page,pageSize,userId);
        List<AppNoticeDTO> appNoticeDtos=getAppNoticeDtos(entries,userId);
        int count=appNoticeDao.countMyReceivedAppNoticeEntries(groupIds,userId);
        retMap.put("list",appNoticeDtos);
        retMap.put("count",count);
        retMap.put("page",page);
        retMap.put("pageSize",pageSize);
        //清除红点
        redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
        return retMap;
    }

    public void pushRead(ObjectId id,ObjectId userId)throws Exception{
        AppNoticeEntry appNoticeEntry=appNoticeDao.getAppNoticeEntry(id);
        if(null!=appNoticeEntry){
            List<ObjectId> readList=appNoticeEntry.getReaList();
            if(!readList.contains(userId)) {
                appNoticeDao.pushReadList(userId, id);
                redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
            }
        }else{
            throw new Exception("传入的id参数有误");
        }
    }


    public String addOperationEntry(AppOperationDTO dto){
        AppOperationEntry en = dto.buildAddEntry();
        //获得当前时间
        long current=System.currentTimeMillis();
        en.setDateTime(current);
        String id = appOperationDao.addEntry(en);
        appNoticeDao.updateCommentCount(en.getContactId());
        return id;
    }

}
