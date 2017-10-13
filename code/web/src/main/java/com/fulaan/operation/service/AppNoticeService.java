package com.fulaan.operation.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.IndexPageDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.db.user.UserDao;
import com.fulaan.dto.VideoDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.operation.AppOperationEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.TimeChangeUtils;
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

    @Autowired
    private UserService userService;


    public static void main(String[] args){
        UserDao userDao=new UserDao();
        AppNoticeDao appNoticeDao=new AppNoticeDao();
        for(int i=0;i<2;i++) {
            AppNoticeDTO dto = new AppNoticeDTO();
            dto.setSubjectId("55935198f6f28b7261c9bf5f");
            dto.setSubject("英语");
            dto.setTitle("今天我最帅，我是品牌代言人"+i);
            dto.setContent("党的十八大以来");
            dto.setGroupId("59c32cc6670ab23fb82dc4ae");
            dto.setCommunityId("59c32cc5670ab23fb82dc4ac");
            dto.setWatchPermission(Constant.THREE);
            dto.setVideoList(new ArrayList<VideoDTO>());
            List<Attachement> imageList = new ArrayList<Attachement>();
            Attachement attachement = new Attachement();
            attachement.setUrl("http://7xiclj.com1.z0.glb.clouddn.com/582effea3d4df91126ff2b9a.png");
            attachement.setFlnm("啦啦啦啦");
            Attachement attachement1= new Attachement();
            attachement1.setUrl("http://7xiclj.com1.z0.glb.clouddn.com/57a51bab3d4df9703fd552a1.jpg");
            attachement1.setFlnm("1111");
            imageList.add(attachement1);
            Attachement attachement2= new Attachement();
            attachement2.setUrl("http://7xiclj.com1.z0.glb.clouddn.com/57b6c46ede04cb06131ced0d.JPG");
            attachement2.setFlnm("323131");
            imageList.add(attachement2);
            Attachement attachement3= new Attachement();
            attachement3.setUrl("http://7xiclj.com1.z0.glb.clouddn.com/20170321182122.jpg");
            attachement3.setFlnm("wqa111");
            imageList.add(attachement3);
            if(i==1){
                Attachement attachement4= new Attachement();
                attachement4.setUrl("http://7xiclj.com1.z0.glb.clouddn.com/head-0.5721118256915361.jpg");
                attachement4.setFlnm("wqa111");
                imageList.add(attachement4);
            }
            dto.setAttachements(new ArrayList<Attachement>());
            dto.setImageList(imageList);
            dto.setGroupName("本地我最帅-scott");
            ObjectId userId = new ObjectId("59c32c8c670ab23fb82dc49a");
            UserEntry userEntry = userDao.getUserEntry(userId, Constant.FIELDS);
            dto.setUserName(userEntry.getUserName());
            dto.setUserId(userId.toString());
            appNoticeDao.saveAppNoticeEntry(dto.buildEntry());
        }
    }

    /**
     * 保存信息
     * @param dto
     */
    public void saveAppNoticeEntry(AppNoticeDTO dto,ObjectId userId){
        UserEntry userEntry=userService.findById(userId);
        List<GroupOfCommunityDTO> dtos=new ArrayList<GroupOfCommunityDTO>();
        for(GroupOfCommunityDTO communityDTO:dtos){
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
                    communityDTO.getGroupName(),
                    userEntry.getUserName());
            appNoticeDTO.setUserId(userId.toString());


            ObjectId oid = appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());

            //添加临时记录表
            IndexPageDTO dto1 = new IndexPageDTO();
            dto1.setType(CommunityType.appNotice.getType());
            dto1.setCommunityId(communityDTO.getCommunityId());
            dto1.setContactId(oid.toString());
            IndexPageEntry entry = dto1.buildAddEntry();
            indexPageDao.addEntry(entry);
        }
    }


    public void removeAppNoticeEntry(ObjectId noticeId){
        appNoticeDao.removeAppNoticeEntry(noticeId);
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
            dto.setUserName(userEntry.getNickName());
            dto.setIsRead(0);
            if(dto.getReadList().contains(userId.toString())){
                dto.setIsRead(1);
            }
            //设置已阅和未阅的人数
            List<ObjectId> reads=entry.getReaList();
            List<ObjectId> members=memberDao.getAllMemberIds(entry.getGroupId());
            members.remove(userId);
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
                dto.setUserName(userEntry.getNickName());
            }
            dto.setIsRead(0);
            if(dto.getReadList().contains(userId.toString())){
                dto.setIsRead(1);
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
        return retMap;
    }

    public void pushRead(ObjectId id,ObjectId userId){
        appNoticeDao.pushReadList(userId,id);
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
