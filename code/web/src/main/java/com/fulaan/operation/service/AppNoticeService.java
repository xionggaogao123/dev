package com.fulaan.operation.service;

import cn.jpush.api.push.model.audience.Audience;
import com.db.business.ModuleNumberDao;
import com.db.business.ModuleTimeDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.IndexContentDao;
import com.db.indexPage.IndexPageDao;
import com.db.indexPage.WebHomePageDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.fulaan.indexpage.dto.IndexContentDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.integral.service.IntegralSufferService;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.picturetext.service.CheckTextAndPicture;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JPushUtils;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.backstage.PictureType;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.IndexContentEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.indexPage.WebHomePageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.integral.IntegralType;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    ModuleNumberDao moduleNumberDao = new ModuleNumberDao();

    private ModuleTimeDao moduleTimeDao = new ModuleTimeDao();
    @Autowired
    private UserService userService;
    @Autowired
    private RedDotService redDotService;

    private IndexContentDao indexContentDao = new IndexContentDao();

    @Autowired
    private NewVersionBindService newVersionBindService;
    @Autowired
    private IntegralSufferService integralSufferService;

    //学生社群
   // private static final String STUDENTCOMMUNIY = "5abaf547bf2e791a5457a584";
    //线上
   private static final String STUDENTCOMMUNIY = "5b04d9eb3d4df9273f5c7747";


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
    public String saveAppNoticeEntry(AppNoticeDTO dto,ObjectId userId)throws Exception{
        UserEntry userEntry=userService.findById(userId);
        //JPushUtils jPushUtils=new JPushUtils();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        //文本检测
        Map<String,Object> flag = CheckTextAndPicture.checkText(dto.getContent() + "-----------" + dto.getTitle(),userId);
        String f = (String)flag.get("bl");
        if(f.equals("1")){
            //return (String)flag.get("text");
            return (String)flag.get("text");
        }

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
            //发送通知
            PictureRunNable.addTongzhi(appNoticeDTO.getCommunityId(),appNoticeDTO.getUserId(),2);

            //图片检测
            List<Attachement> alist = appNoticeDTO.getImageList();
            if(alist != null && alist.size()>0){
                for(Attachement entry5 : alist){
                    PictureRunNable.send(oid.toString(), userId.toString(), PictureType.answerImage.getType(), 1, entry5.getUrl());
                }
            }



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

                IndexPageDTO dto2 = new IndexPageDTO();
                dto2.setType(CommunityType.allNotice.getType());
                dto2.setUserId(userId.toString());
                dto2.setCommunityId(communityDTO.getCommunityId());
                dto2.setContactId(oid.toString());
                IndexPageEntry entry2 = dto2.buildAddEntry();
                indexPageDao.addEntry(entry2);

                IndexContentDTO indexContentDTO = new IndexContentDTO(
                        dto.getSubject(),
                        dto.getTitle(),
                        dto.getContent(),
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        communityDTO.getGroupName(),
                        userEntry.getUserName());
                List<ObjectId> members=memberDao.getAllMemberIds(new ObjectId(communityDTO.getGroupId()));
                IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(userId.toString(),dto.getSubjectId(), communityDTO.getGroupId(),communityDTO.getCommunityId(),dto.getWatchPermission());
                indexContentEntry.setReadList(new ArrayList<ObjectId>());
                indexContentEntry.setContactId(oid);
                indexContentEntry.setContactType(1);
                indexContentEntry.setAllCount(members.size());
                indexContentDao.addEntry(indexContentEntry);

                WebHomePageEntry pageEntry=new WebHomePageEntry(Constant.TWO, userId,
                        StringUtils.isNotEmpty(communityDTO.getCommunityId())?
                                new ObjectId(communityDTO.getCommunityId()):null, oid, StringUtils.isNotEmpty(dto.getSubjectId())?
                        new ObjectId(dto.getSubjectId()):null,
                        null,null,null,Constant.ZERO
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
                        null,null,null,Constant.ZERO
                );
                webHomePageDao.saveWebHomeEntry(pageEntry);
            }
            //通知发送记录
            moduleTimeDao.addEntry(userId,ApplyTypeEn.notice.getType(),new ObjectId(communityDTO.getCommunityId()));
        }
        //1:家长2:学生3:家长，学生
        redDotService.addEntryList(objectIdList,userId, ApplyTypeEn.notice.getType(),dto.getWatchPermission());
        redDotService.addOtherEntryList(objectIdList,userId, ApplyTypeEn.daynotice.getType(),dto.getWatchPermission());

        int  score = integralSufferService.addIntegral(userId, IntegralType.notice,1,1);
       /* try {
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
            throw new Exception("特殊"+score);
        }*/
        return score+"";
    }


    public void removeAppNoticeEntry(ObjectId noticeId,ObjectId userId)throws Exception{
        AppNoticeEntry appNoticeEntry=appNoticeDao.getAppNoticeEntry(noticeId);
        if(null!=appNoticeEntry) {
            if(null!=appNoticeEntry.getUserId()&&null!=userId
                    &&appNoticeEntry.getUserId().toString().equals(userId.toString())){
                long current=System.currentTimeMillis();
                if(appNoticeEntry.getSubmitTime() >current-24*60*60*1000) {
                    appNoticeDao.removeAppNoticeEntry(noticeId);
                    //删除首页记录
                    indexPageDao.delEntry(noticeId);
                    //删除首页
                    webHomePageDao.removeContactId(noticeId);
                }else{
                    throw new Exception("已过有效时间!");
                }
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
        reads.remove(entry.getUserId());
        List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
        members.remove(entry.getUserId());
        //修改首页已阅人数和未阅人数
        int allCount = members.size();
        members.removeAll(reads);
        List<User> read=new ArrayList<User>();
        List<User> unRead=new ArrayList<User>();
        if(reads.size()>0){
            saveUser(read,reads,entry.getGroupId());
        }
        if(members.size()>0){
            saveUser(unRead,members,entry.getGroupId());
        }
        IndexContentEntry indexContentEntry = indexContentDao.getEntry(id);
        if(indexContentEntry!=null){
           // indexContentDao.updateAllEntry(id,allCount,members);
            indexContentEntry.setAllCount(allCount);
            indexContentEntry.setReadList(members);
            indexContentDao.addEntry(indexContentEntry);
        }

        userMap.put("read",read);
        userMap.put("unRead",unRead);
        return userMap;
    }



    public void saveUser(List<User> users,List<ObjectId> userIds,ObjectId groupId){
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        Map<ObjectId,String>  smap = memberDao.getNickNameByUserIds(userIds,groupId);
        for(Map.Entry<ObjectId,UserEntry> userEntryEntry:userEntryMap.entrySet()){
            UserEntry userEntry=userEntryEntry.getValue();
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            String name2 = smap.get(userEntry.getID());
            if(name2!=null){
                name = name2;
            }
            User user=new User(name,
                    name,
                    userEntry.getID().toString(),
                    AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()),
                    userEntry.getSex(),
                    "");
            users.add(user);
        }
    }


    public Map<String,Object> getMyAppNotices(String cId,String sId,ObjectId userId,int page,int pageSize){
        Map<String,Object> result=new HashMap<String,Object>();
        ObjectId communityId=StringUtils.isNotEmpty(cId)?new ObjectId(cId):null;
        ObjectId subjectId=StringUtils.isNotEmpty(sId)?new ObjectId(sId):null;
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        List<AppNoticeEntry> entries=appNoticeDao.getMyAppNotices(communityId,subjectId,groupIds,userId,page,pageSize);
        int count=appNoticeDao.countMyAppNotices(communityId,subjectId,groupIds,userId);
        List<AppNoticeDTO> appNoticeDTOs=getAppNoticeDtos(entries,userId);
        result.put("list",appNoticeDTOs);
        result.put("count",count);
        result.put("page",page);
        result.put("pageSize",pageSize);
        moduleNumberDao.addEntry(userId,ApplyTypeEn.notice.getType());
        //redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
        return result;
    }


    public Map<String,Object> getMySendAppNoticeDtos(String cId,String sId,ObjectId userId,int page,int pageSize){
        List<AppNoticeDTO> dtos=new ArrayList<AppNoticeDTO>();
        Map<String,Object> retMap=new HashMap<String,Object>();
        ObjectId communityId=StringUtils.isNotEmpty(cId)?new ObjectId(cId):null;
        ObjectId subjectId=StringUtils.isNotBlank(sId)?new ObjectId(sId):null;
        List<AppNoticeEntry> entries=appNoticeDao.getMySendAppNoticeEntries(communityId,subjectId,userId,page,pageSize);
        UserEntry userEntry=userService.findById(userId);
        sendAppNotices(entries,userEntry,dtos,userId);
        int count=appNoticeDao.countMySendAppNoticeEntries(communityId,subjectId,userId);
        retMap.put("page",page);
        retMap.put("pageSize",pageSize);
        retMap.put("list",dtos);
        retMap.put("count",count);
        return retMap;
    }

    public void sendAppNotices(List<AppNoticeEntry> entries,UserEntry userEntry,
                               List<AppNoticeDTO> dtos,
                               ObjectId userId){
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
        sendAppNotices(entries,userEntry,dtos,userId);
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
        //默认查询复兰大学
        CommunityEntry communityEntry = communityDao.findByName("复兰大学");
        if(communityEntry!=null){
            communityIds.add(communityEntry.getID());
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
        //redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
        return retMap;
    }

    public Map<String,Object> getNewMyReceivedAppNoticeDtosForStudent(ObjectId userId,int page,int pageSize){
        Map<String,Object> retMap=new HashMap<String,Object>();
        List<NewVersionCommunityBindEntry> bindEntries=newVersionCommunityBindDao.getAllStudentBindEntries(userId);
        List<ObjectId> communityIds=new ArrayList<ObjectId>();
        for(NewVersionCommunityBindEntry bindEntry:bindEntries){
            communityIds.add(bindEntry.getCommunityId());
        }
        //默认查询复兰大学
        CommunityEntry communityEntry = communityDao.findByName("复兰大学");
        if(communityEntry!=null){
            communityIds.add(communityEntry.getID());
            communityIds.add(new ObjectId(STUDENTCOMMUNIY));
        }
        List<ObjectId> groupIds=communityDao.getGroupIdsByCommunityIds(communityIds);
        List<AppNoticeEntry> entries=appNoticeDao.getMyReceivedAppNoticeEntriesForStudent(groupIds,page,pageSize);
        List<AppNoticeDTO> dtos=getNewAppNoticeDtos(entries, userId);
        int count=appNoticeDao.countMyReceivedAppNoticeEntriesForStudent(groupIds);
        retMap.put("list",dtos);
        retMap.put("count",count);
        retMap.put("page",page);
        retMap.put("pageSize",pageSize);
        //清除红点
        //redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
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
            List<ObjectId> reads=entry.getReaList();
            reads.remove(entry.getUserId());
            dto.setReadCount(reads.size());
            if(entry.getUserId().equals(userId)){
                //设置已阅和未阅的人数
                List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
                members.remove(userId);
                dto.setTotalReadCount(members.size());
                members.removeAll(reads);
                dto.setUnReadCount(members.size());
                dto.setOwner(true);
            }
            dto.setTimeExpression(TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
            dtos.add(dto);
        }
        return dtos;
    }

    public  List<AppNoticeDTO> getNewAppNoticeDtos( List<AppNoticeEntry> entries,ObjectId userId){
        List<AppNoticeDTO> dtos=new ArrayList<AppNoticeDTO>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for(AppNoticeEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(AppNoticeEntry entry:entries){
            AppNoticeDTO dto=new AppNoticeDTO(entry);
            if(dto.getCommunityId().equals(STUDENTCOMMUNIY)){
                UserEntry userEntry=userEntryMap.get(entry.getUserId());
                if(null!=userEntry){
                    dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                    //dto.setUserName(dto);
                }
                dto.setUserName(entry.getUserName());
                dto.setIsRead(0);
                if(dto.getReadList().contains(userId.toString())){
                    dto.setIsRead(1);
                }
                dto.setOwner(false);
                if(entry.getUserId().equals(userId)){
                    //设置已阅和未阅的人数
                    List<ObjectId> reads=entry.getReaList();
                    dto.setTotalReadCount(0);
                    dto.setReadCount(reads.size());
                    dto.setUnReadCount(0);
                    dto.setOwner(true);
                }
                dto.setTimeExpression(TimeChangeUtils.getChangeTime(entry.getSubmitTime()));
                dto.setCardType(2);//特殊
                dto.setSubject(entry.getSubject());
                dto.setAvatar("http://7xiclj.com1.z0.glb.clouddn.com/5a26565027fddd1db08722f1.png");
            }else{
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
                dto.setCardType(1);//普通
            }

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
        //redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
        return retMap;
    }

    public Map<String,Object> getMyReceivedAppNoticeDtos(String cId,String sId,ObjectId userId,int page,int pageSize){
        Map<String,Object> retMap=new HashMap<String,Object>();
        List<ObjectId> groupIds=memberDao.getGroupIdsByUserId(userId);
        ObjectId communityId=StringUtils.isNotEmpty(cId)?new ObjectId(cId):null;
        ObjectId subjectId=StringUtils.isNotEmpty(sId)?new ObjectId(sId):null;
        List<AppNoticeEntry> entries=appNoticeDao.getMyReceivedAppNoticeEntries(communityId,subjectId,groupIds,page,pageSize,userId);
        List<AppNoticeDTO> appNoticeDtos=getAppNoticeDtos(entries,userId);
        int count=appNoticeDao.countMyReceivedAppNoticeEntries(communityId,subjectId,groupIds,userId);
        retMap.put("list",appNoticeDtos);
        retMap.put("count",count);
        retMap.put("page",page);
        retMap.put("pageSize",pageSize);
        //清除红点
        //redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
        return retMap;
    }

    public String pushRead(ObjectId id,ObjectId userId)throws Exception{
        AppNoticeEntry appNoticeEntry=appNoticeDao.getAppNoticeEntry(id);
        if(null!=appNoticeEntry && !appNoticeEntry.getUserId().equals(userId)){//防止自己签到
            List<ObjectId> readList=appNoticeEntry.getReaList();
            if(!readList.contains(userId)) {
                appNoticeDao.pushReadList(userId, id);
               // redDotService.cleanResult(userId,ApplyTypeEn.notice.getType(),0l);
            }
            //首页通用
            IndexContentEntry indexContentEntry = indexContentDao.getEntry(id);
            if(indexContentEntry!=null){
                List<ObjectId> reList = indexContentEntry.getReaList();
                if(!reList.contains(userId)) {
                    indexContentDao.pushReadList(userId, id);
                    //红点减一
                    redDotService.jianRedDot(userId,ApplyTypeEn.notice.getType());
                }
            }
        }else{
            throw new Exception("传入的id参数有误");
        }
        int score = integralSufferService.addIntegral(userId, IntegralType.notice,3,2);
        return score+"";
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


    public void updateOldList(){
        int page = 1;
        int pageSize = 10;
        boolean flag = true;
        indexPageDao.delAllEntry();
        indexContentDao.delAllEntry();
        while(flag){
            page++;
            List<AppNoticeEntry> appNoticeEntries = appNoticeDao.getMyAppNoticeList(page, pageSize);
            if(appNoticeEntries.size()<10){
                flag = false;
            }
            for(AppNoticeEntry appNoticeEntry :appNoticeEntries){
                AppNoticeDTO dto = new AppNoticeDTO(appNoticeEntry);
                IndexPageDTO dto2 = new IndexPageDTO();
                dto2.setType(CommunityType.allNotice.getType());
                dto2.setUserId(dto.getUserId());
                dto2.setCommunityId(dto.getCommunityId());
                dto2.setContactId(dto.getId());
                IndexPageEntry entry2 = dto2.buildAddEntry();
                indexPageDao.addEntry(entry2);

                IndexContentDTO indexContentDTO = new IndexContentDTO(
                        dto.getSubject(),
                        dto.getTitle(),
                        dto.getContent(),
                        dto.getVideoList(),
                        dto.getImageList(),
                        dto.getAttachements(),
                        dto.getVoiceList(),
                        dto.getGroupName(),
                        dto.getUserName());
                List<ObjectId> members=memberDao.getAllMemberIds(new ObjectId(dto.getGroupId()));
                IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(dto.getUserId().toString(),dto.getSubjectId(), dto.getGroupId(),dto.getCommunityId(),dto.getWatchPermission());
                indexContentEntry.setReadList(appNoticeEntry.getReaList());
                indexContentEntry.setSubmitTime(appNoticeEntry.getSubmitTime());
                indexContentEntry.setContactId(appNoticeEntry.getID());
                indexContentEntry.setContactType(1);
                indexContentEntry.setAllCount(members.size());
                indexContentDao.addEntry(indexContentEntry);
            }

        }
    }
}
