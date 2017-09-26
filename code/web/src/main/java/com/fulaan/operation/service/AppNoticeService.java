package com.fulaan.operation.service;

import com.db.fcommunity.MemberDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.pojo.User;
import com.fulaan.user.service.UserService;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
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

    @Autowired
    private UserService userService;


    /**
     * 保存信息
     * @param dto
     */
    public void saveAppNoticeEntry(AppNoticeDTO dto,ObjectId userId){
        String gId=dto.getGroupId();
        String[] groupIds=gId.split(",");
        for(String groupId:groupIds){
            AppNoticeDTO appNoticeDTO=new AppNoticeDTO(dto.getSubject(),
                    dto.getTitle(),
                    dto.getContent(),
                    groupId,
                    dto.getWatchPermission(),
                    dto.getVideoList(),
                    dto.getImageList());
            appNoticeDTO.setUserId(userId.toString());
            appNoticeDao.saveAppNoticeEntry(appNoticeDTO.buildEntry());
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



    public void saveUser( List<User> users,List<ObjectId> userIds){
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
    public List<AppNoticeDTO> getMySendAppNoticeDtos(ObjectId userId,int page,int pageSize){
        List<AppNoticeDTO> dtos=new ArrayList<AppNoticeDTO>();
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
    public List<AppNoticeDTO> getMyReceivedAppNoticeDtos(ObjectId userId,int page,int pageSize){
        List<AppNoticeDTO> dtos=new ArrayList<AppNoticeDTO>();
        List<ObjectId> groupIds=memberDao.getUnManagerGroupIdsByUserId(userId);
        List<AppNoticeEntry> entries=appNoticeDao.getMyReceivedAppNoticeEntries(groupIds,page,pageSize,userId);

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
            dtos.add(dto);
        }
        return dtos;
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
