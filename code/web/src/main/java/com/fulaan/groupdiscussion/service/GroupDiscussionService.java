package com.fulaan.groupdiscussion.service;

import com.db.groups.GroupsDao;
import com.fulaan.groupdiscussion.dto.GroupChatDTO;
import com.fulaan.groupdiscussion.dto.GroupDiscussionDTO;
import com.fulaan.groupdiscussion.dto.GroupFileDTO;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JsonUtil;
import com.fulaan.utils.StringUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.groups.GroupsChatEntry;
import com.pojo.groups.GroupsEntry;
import com.pojo.groups.GroupsFileEntry;
import com.pojo.groups.GroupsUser;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by wang_xinxin on 2015/3/24.
 */

@Service
public class GroupDiscussionService {

    private static final Logger logger= Logger.getLogger(GroupDiscussionService.class);

    private GroupsDao groupsDao = new GroupsDao();

    @Autowired
    private EaseMobService easeMobService;

    @Autowired
    private UserService userService;


    /**
     * 查询群组
     * @param userid
     * @return
     */
    public List<GroupsEntry> selGroupsList(ObjectId userid) {

        return null;
    }

    /**
     * 创建群组
     * @param groupsEntry
     */
    public void addGroup(GroupsEntry groupsEntry) {
        List<String> memberList = new ArrayList<String>();
        for (GroupsUser groupuser : groupsEntry.getGroupsUserList()) {
            if (!groupuser.getUserid().equals(groupsEntry.getUserid())) {
                memberList.add(groupuser.getUserid().toString());
            }
        }
        Future map = easeMobService.createChatGroup(groupsEntry.getGroupname(), "群组", String.valueOf(groupsEntry.getUserid()), memberList);
        String jsonStr = "";
        String roomid = "";
        try {
            jsonStr = (String) map.get();
            Map<String, Object> result = JsonUtil.Json2Map(jsonStr);
            LinkedHashMap<String,String> resultjson = (LinkedHashMap<String, String>) result.get("data");
            roomid = resultjson.get("groupid");
            groupsEntry.setRoomid(roomid);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
        }
        groupsDao.addGroups(groupsEntry);
        memberList.add(groupsEntry.getUserid().toString());
        IdValuePair idvalue = new IdValuePair(roomid,"");
        if (memberList!=null && memberList.size()!=0) {
            userService.updateUserGroupList(memberList,idvalue);
        }
    }

    /**
     * 更新群组
     * @param groupsEntry
     */
    public void updateGroup(GroupsEntry groupsEntry,String[] groupAry) {
        List<String> memberList = new ArrayList<String>();
        GroupsEntry groups = groupsDao.selGroups(groupsEntry.getRoomid(),null);
        List<GroupsUser> currGroupUserList = null;
        if (groups!=null) {
            currGroupUserList = groups.getGroupsUserList();
        }
        List<String> userList = new ArrayList<String>();
        if (currGroupUserList!=null && currGroupUserList.size()!=0) {
            for (GroupsUser userView : currGroupUserList) {
                if(!(userView.getUserid().toString()).equals(groups.getUserid().toString())) {
                    userList.add(String.valueOf(userView.getUserid()));
                }
            }
        }
        String[] userAry = new String[userList.size()];
        if (userList!=null && userList.size()!=0) {
            for (int i=0;i<userAry.length;i++) {
                userAry[i] = userList.get(i);
            }
        }
        List<String> missUser = StringUtil.compare(groupAry, userAry);
        List<String> addUser = StringUtil.compare(userAry, groupAry);
        IdValuePair idvalue = new IdValuePair(groups.getRoomid(),"");
        if (addUser!=null && addUser.size()!=0) {
            for (String userid : addUser) {
                GroupsUser gorupuser = new GroupsUser(userid,0,System.currentTimeMillis());
                groupsDao.addGroupUser(groupsEntry.getRoomid(),gorupuser);
                memberList.add(userid);
                easeMobService.addUserToGroup(groups.getRoomid(), userid);
            }
            userService.updateUserGroupList(memberList,idvalue);
        }
        if (missUser!=null && missUser.size()!=0) {
            for (String userid : missUser) {
                for (GroupsUser userView : currGroupUserList) {
                    if (userid.equals(userView.getUserid().toString()) &&!userid.equals(String.valueOf(groups.getUserid()))) {
                        groupsDao.deleteGroupUser(groupsEntry.getRoomid(), userView);
                        easeMobService.deleteUserFromGroup(groups.getRoomid(), userid);
                        memberList.add(userid);
                    }
                }
            }
            userService.deleteUserGroupList(memberList, groups.getRoomid());
        }
        groupsDao.updateGroups(groupsEntry.getRoomid(),groupsEntry);
        easeMobService.updateChatGroups(groups.getRoomid(), groups.getGroupname());
    }

    /**
     * app更新群名称
     * @param group
     */
    public void updateGroupName(GroupsEntry group) {
        groupsDao.updateGroupName(group.getRoomid(),group);
        easeMobService.updateChatGroups(group.getRoomid(), group.getGroupname());
    }

//    /**
//     * 删除群成员
//     * @param groupsEntry
//     */
//    public void deleteGroupUser(ObjectId id,GroupsEntry groupsEntry) {
//        groupsDao.deleteGroupUser(id, groupsEntry);
//        for (GroupsUser groupUser : groupsEntry.getGroupsUserList()) {
//            easeMobService.deleteUserFromGroup(groupsEntry.getRoomid(), groupUser.getUserid().toString());
//        }
//    }

    /**
     * 添加群成员
     * @param groupsEntry
     */
    public void addGroupUser(ObjectId id,GroupsEntry groupsEntry) {
        groupsDao.addGroupUser(id, groupsEntry);
        for (GroupsUser groupUser : groupsEntry.getGroupsUserList()) {
            easeMobService.addUserToGroup(groupsEntry.getRoomid(), groupUser.getUserid().toString());
        }

    }

    /**
     * 添加群成员
     * @param userid
     * @param roomid
     * @param id
     */
    public void addGroupMember(String userid, String roomid, String id) {
        GroupsUser groupsUser = new GroupsUser(userid,0,System.currentTimeMillis());
        GroupDiscussionDTO group = new GroupDiscussionDTO();
        GroupsEntry groupsEntry = group.buildGroupsEntry();
        List<GroupsUser> groupsUserList = new ArrayList<GroupsUser>();
        groupsUserList.add(groupsUser);
        groupsEntry.setGroupsUserList(groupsUserList);
        groupsDao.addGroupUser(new ObjectId(id),groupsEntry);
        easeMobService.addUserToGroup(roomid, String.valueOf(userid));
    }

//    /**
//     * 删除群成员
//     * @param userid
//     * @param roomid
//     * @param id
//     */
//    public void delGroupMember(String userid, String roomid, String id) {
//        GroupsUser groupsUser = new GroupsUser(new ObjectId(userid),0,System.currentTimeMillis());
//        GroupDiscussionDTO group = new GroupDiscussionDTO();
//        GroupsEntry groupsEntry = group.buildGroupsEntry();
//        List<GroupsUser> groupsUserList = new ArrayList<GroupsUser>();
//        groupsUserList.add(groupsUser);
//        groupsEntry.setGroupsUserList(groupsUserList);
//        groupsDao.deleteGroupUser(new ObjectId(id), groupsEntry);
//        easeMobService.deleteUserFromGroup(roomid, String.valueOf(userid));
//    }

    /**
     * 删除群组
     * @param roomid
     * @param type
     *          1.解散群
     * 			2.退群
     * @param userId
     */
    public void deleteGroup(String roomid, int type, String userId) {
        List<String> memberList = new ArrayList<String>();
        GroupsEntry groups = groupsDao.selGroups(roomid,null);
        if (type == 1) {
            for (GroupsUser groupuser : groups.getGroupsUserList()) {
                memberList.add(groupuser.getUserid().toString());
            }
            userService.deleteUserGroupList(memberList,roomid);
            groupsDao.deleteGroup(roomid,userId);
            easeMobService.deleteChatGroups(roomid);
        } else if (type == 2) {
            for (GroupsUser groupuser : groups.getGroupsUserList()) {
                if (groupuser.getUserid().equals(userId)) {
                    groupsDao.deleteGroupUser(roomid, groupuser);
                }
            }
            memberList.add(userId.toString());
            userService.deleteUserGroupList(memberList, roomid);
            easeMobService.deleteUserFromGroup(roomid, String.valueOf(userId));
        }

    }

    /**
     * 上传文件
     * @param groupFile
     */
    public void insertGroupFile(GroupsFileEntry groupFile) {
        groupsDao.insertGroupFile(groupFile);
    }

    /**
     * 查询单条文件
     * @param fileid
     * @return
     */
    public GroupsFileEntry selGroupFile(ObjectId fileid) {
        return groupsDao.selGroupFile(fileid);
    }

    /**
     * 更新群文件数量
     * @param fileid
     */
    public void updateGroupFile(ObjectId fileid) {
        groupsDao.updateGroupFile(fileid);
    }

    /**
     * 查询群文件列表
     * @param roomid
     * @param pageable
     * @return
     */
    public Page<GroupFileDTO> selGroupFileList(String roomid, Pageable pageable) {
        List<GroupFileDTO> filelist = new ArrayList<GroupFileDTO>();
        int page = pageable.getOffset();
        int pageSize = pageable.getPageSize()+pageable.getOffset();
        List<GroupsFileEntry> groupfilelist = groupsDao.selGroupFileList(roomid,page,pageSize);
        GroupFileDTO file = null;
        if (groupfilelist!=null && groupfilelist.size()!=0) {
            for (GroupsFileEntry groupfile : groupfilelist) {
                file = new GroupFileDTO();
                String path = "C:\\fulaan\\"+groupfile.getFilepath();
                File dir = new File(path);
                if (dir.exists()) {
                    file.setDownloadflag(true);
                    file.setDownloadpath(path);
                } else {
                    file.setDownloadflag(false);
                }
                file.setId(groupfile.getID().toString());
                file.setUploadtime(DateTimeUtils.convert(groupfile.getUploadtime(), "yyyy-MM-dd"));
                file.setRoomid(groupfile.getRoomid());
                file.setCount(groupfile.getCount());
                file.setFilePath(groupfile.getFilepath());
                file.setFileName(groupfile.getFilename());
                file.setFilesize(groupfile.getFilesize());
                file.setUploadUserid(groupfile.getUploadUserid().toString());
                UserEntry user = userService.searchUserChatId(groupfile.getUploadUserid());
                file.setUsername(user.getNickName());
                filelist.add(file);
            }
        }
        return new PageImpl<GroupFileDTO>(filelist, pageable, groupsDao.selGroupFileCount(roomid));
    }

    /**
     * 删除群文件
     * @param id
     */
    public void deleteGroupFile(String id) {
        groupsDao.deleteGroupFile(new ObjectId(id));
    }

    /**
     * 添加聊天记录
     * @param groupChat
     */
    public void addGroupChat(GroupsChatEntry groupChat) {
        groupsDao.addGroupsChat(groupChat);
    }

    /**
     * 取单个群组
     * @param roomid
     * @return
     */
    public GroupsEntry selSingleGroup(String roomid) {
        return groupsDao.selGroups(roomid,null);
    }

    /**
     * 是否是群主
     * @param roomid
     * @param userId
     * @return
     */
    public boolean isGroupMain(String roomid, String userId) {
        GroupsEntry group = groupsDao.selGroups(roomid, userId);
        if (group==null) {
            return false;
        }
        return true;
    }

    public Page<GroupChatDTO> selHistoryChatList(GroupChatDTO groupchatdto,Pageable pageable) throws ResultTooManyException {
        int page = pageable.getOffset();
        int pageSize = pageable.getPageSize()+pageable.getOffset();
        List<GroupChatDTO> groupChatDTOList = new ArrayList<GroupChatDTO>();
        long ceratedate = 0;
        if (!StringUtils.isEmpty(groupchatdto.getChatid())) {
            GroupsChatEntry chat = groupsDao.selGroupChat(groupchatdto.getChatid());
            ceratedate = chat.getCreatetime();
        }

        List<GroupsChatEntry> groupchatList = groupsDao.selChatLogList(groupchatdto.getRoomid(),groupchatdto.getChatid(),ceratedate,null,page,pageSize);
        Collections.reverse(groupchatList);
        GroupChatDTO groupChatdto = null;
        if (groupchatList!=null && groupchatList.size()!=0) {
            for (GroupsChatEntry groupchat : groupchatList) {
                groupChatdto = new GroupChatDTO();
                groupChatdto.setRoomid(groupchat.getRoomid());
                groupChatdto.setChatContent(groupchat.getChatContent());
                groupChatdto.setGroupUserid(groupchat.getUserid().toString());
                groupChatdto.setSendDate(new Date(groupchat.getCreatetime()));
                groupChatdto.setId(groupchat.getID().toString());
                UserEntry user = userService.searchUserChatId(groupchat.getUserid());
                groupChatdto.setUserName(user.getUserName());
                groupChatdto.setImageUrl(AvatarUtils.getAvatar(user.getAvatar(), 3));
                groupChatDTOList.add(groupChatdto);
            }
        }
        return new PageImpl<GroupChatDTO>(groupChatDTOList, pageable, groupsDao.selChatLogListCount(groupchatdto.getRoomid(),groupchatdto.getChatid(),ceratedate));
    }

    /**
     * 获取聊天记录
     * @param groupid
     * @param userId
     * @return
     */
    public Map<String, Object> selChatList(ObjectId groupid, String userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        GroupsEntry groupentry = groupsDao.selSingleRefGroupUser(groupid, userId);
        List<GroupsUser> groupuserlist = groupentry.getGroupsUserList();
        GroupsUser refGroupUser = groupuserlist.get(0);
        int notviewCount = 0;
        List<GroupsChatEntry> groupchatlist = new ArrayList<GroupsChatEntry>();
        List<GroupChatDTO> chatlist = new ArrayList<GroupChatDTO>();
        if (refGroupUser.getIsread()==1) {
            groupchatlist =  groupsDao.selChatList(groupid, 1, null,0);
        } else {
            groupchatlist = groupsDao.selChatList(groupid,2,null,refGroupUser.getUpdatetime());
            notviewCount = groupsDao.selNotViewChatCount(groupid,refGroupUser.getUpdatetime(),1);
        }
        GroupChatDTO groupChatdto = new GroupChatDTO();
        for (GroupsChatEntry groupchat : groupchatlist) {
            groupChatdto = new GroupChatDTO();
            groupChatdto.setRoomid(groupchat.getRoomid());
            groupChatdto.setChatContent(groupchat.getChatContent());
            groupChatdto.setGroupUserid(groupchat.getUserid().toString());
            groupChatdto.setId(groupchat.getID().toString());
            groupChatdto.setSendtime(DateTimeUtils.convert(groupchat.getCreatetime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
            UserEntry user = userService.searchUserChatId(groupchat.getUserid());
            groupChatdto.setImageUrl(AvatarUtils.getAvatar(user.getAvatar(), 3));
            groupChatdto.setUserName(user.getNickName());
            chatlist.add(groupChatdto);
        }
        Collections.reverse(chatlist);
        boolean flag = false;
        int count = groupsDao.selNotViewChatCount(groupid,0,2);
        if (chatlist!=null && count>chatlist.size()) {
            flag = true;
        }
        result.put("flag", flag);
        result.put("groupchatList", chatlist);
        result.put("notviewCount", notviewCount);
        return result;
    }

    /**
     * roomid获取群信息
     * @param roomid
     * @return
     */
    public GroupsEntry selectGroupByRoomid(String roomid) {
        return groupsDao.selectGroupByRoomid(roomid);
    }
    
    
    /**
     * 根据房间id 数组查询
     * @param roomids
     * @param fields
     * @return
     */
    public List<GroupsEntry> selGroupsEntryList(List<String> roomids, DBObject fields) {
    	return groupsDao.selGroupsEntryList(roomids,fields);
    }

    /**
     * 收到消息更新状态
     * @param refGroupUser
     */
    public void updateStatus(GroupsUser refGroupUser,String roomid) {
        groupsDao.updateStatus(refGroupUser,roomid);
    }

//    public  List<String> selUserBySchoolid(String schoolid) {
//        List<String> useridlist = new ArrayList<String>();
//        List<UserDetailInfoDTO> userlist = userService.findUserHuanXin(new ObjectId(schoolid));
//        if (userlist!=null && userlist.size()>0) {
//            for (UserDetailInfoDTO user : userlist) {
//                useridlist.add(user.getChatid());
//            }
//        }
//        return useridlist;
//    }
}
