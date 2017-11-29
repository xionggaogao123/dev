package com.fulaan.service;

import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.fcommunity.RemarkDao;
import com.db.user.UserDao;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.dto.MemberDTO;
import com.fulaan.pojo.PageModel;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.fcommunity.RemarkEntry;
import com.pojo.user.UserEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2016/11/1.
 * MemberService
 */
@Service
public class MemberService {

    private MemberDao memberDao = new MemberDao();
    private UserDao userDao = new UserDao();
    private RemarkDao remarkDao = new RemarkDao();

    private CommunityDao communityDao = new CommunityDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    /**
     * 添加组成员
     *
     * @param userId
     * @param role
     * @param groupId
     */
    public void saveMember(ObjectId userId, ObjectId groupId, int role) {
        UserEntry user = userDao.findByUserId(userId);
        String nickName = StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName();
        String userName = user.getUserName();
        MemberEntry entry = new MemberEntry(userId, groupId, nickName, user.getAvatar(), role, userName);
        memberDao.save(entry);
    }


    /**
     * 添加到组--普通成员
     *
     * @param userId
     * @param groupId
     */
    public void saveMember(ObjectId userId, ObjectId groupId) {
        UserEntry user = userDao.findByUserId(userId);
        String nickName = StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName();
        String userName = user.getUserName();
        MemberEntry entry = new MemberEntry(userId, groupId, nickName, user.getAvatar(), 0, userName);
        memberDao.save(entry);
    }

    /**
     * 设置多个副社长
     *
     * @param groupId
     * @param userIds
     */
    public void setDeputyHead(ObjectId groupId, List<ObjectId> userIds) {
        memberDao.cleanDeputyHead(groupId);
        memberDao.setDeputyHead(groupId, userIds);
    }

    /**
     * 清除所有副社长
     *
     * @param groupId
     */
    public void clearDeputyHead(ObjectId groupId) {
        memberDao.cleanDeputyHead(groupId);
    }

    /**
     * 取消 - 副社长
     *
     * @param groupId
     * @param userId
     */
    public void unsetDeputyHead(ObjectId groupId, ObjectId userId) {
        memberDao.unsetDeputyHead(groupId, userId);
    }

    /**
     * 设置群主
     *
     * @param groupId
     * @param userId
     */
    public void setHead(ObjectId groupId, ObjectId userId) {
        memberDao.setHead(groupId, userId);
    }

    /**
     * 获取讨论组成员列表 分页
     *
     * @param grid
     * @param page
     * @param pageSize
     * @return
     */
    public PageModel getGroupMembers(ObjectId grid, int page, int pageSize) {
        int totalCount = memberDao.getMemberCount(grid);
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : (int) Math.ceil(totalCount / pageSize) + 1;
        page = page > totalPages ? totalPages : page;
        if(totalPages == 0 || page < 1) {
            page = 1;
        }
        List<MemberEntry> entries = memberDao.getMembers(grid, page, pageSize);
        List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
        for (MemberEntry entry : entries) {
            memberDTOs.add(new MemberDTO(entry));
        }

        PageModel<MemberDTO> pageModel = new PageModel<MemberDTO>();
        pageModel.setPage(page);
        pageModel.setTotalCount(totalCount);
        pageModel.setTotalPages(totalPages);
        pageModel.setResult(memberDTOs);
        pageModel.setPageSize(pageSize);
        return pageModel;
    }

    /**
     * 删除社区成员
     *
     * @param groupId
     * @param userId
     */
    public void deleteMember(ObjectId groupId, ObjectId userId) {
        memberDao.deleteMember(groupId, userId);
        //剔除掉绑定关系
        ObjectId communityId=communityDao.getCommunityIdByGroupId(groupId);
        if(null!=communityId){
            newVersionCommunityBindDao.removeNewVersionCommunity(communityId,userId);
        }
    }

    /**
     * 判断是否是组成员
     *
     * @param userId
     * @return
     */
    public boolean isGroupMember(ObjectId groupId, ObjectId userId) {
        return memberDao.isMember(groupId, userId);
    }

    /**
     * 判断该用户存在数据
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isBeforeMember(ObjectId groupId, ObjectId userId) {
        return memberDao.isBeforeMember(groupId, userId);
    }

    /**
     * 退出社区后再加入社区
     */
    public void updateMember(ObjectId groupId, ObjectId userId, int remove) {
        memberDao.updateMember(groupId, userId, remove);
    }

    /**
     * 获取管理者
     *
     * @param id
     * @return
     */
    public List<MemberDTO> getManagers(ObjectId id) {
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        List<MemberEntry> entrys = memberDao.getManagers(id);
        for (MemberEntry entry : entrys) {
            members.add(new MemberDTO(entry));
        }
        return members;
    }

    /**
     * 获取某个成员
     *
     * @param groupId
     * @param userId
     * @return
     */
    public MemberDTO getUser(ObjectId groupId, ObjectId userId) {
        MemberEntry memberEntry = memberDao.getUser(groupId, userId);
        return memberEntry == null ? null : new MemberDTO(memberEntry);
    }

    /**
     * 获取几个成员
     *
     * @param groupId
     * @param count
     * @return
     */
    public List<MemberDTO> getMembers(ObjectId groupId, int count,ObjectId userId) {
        List<MemberDTO> members = new ArrayList<MemberDTO>();
        List<MemberEntry> list = memberDao.getMembers(groupId, count);
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for(MemberEntry entry : list){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,RemarkEntry> remarkEntryMap=new HashMap<ObjectId, RemarkEntry>();
        if(null!=userId) {
            remarkEntryMap=remarkDao.find(userId, userIds);
        }
        for (MemberEntry entry : list) {
            MemberDTO memberDTO=new MemberDTO(entry);
            if(null!=remarkEntryMap){
                RemarkEntry remarkEntry=remarkEntryMap.get(entry.getUserId());
                if(null!=remarkEntry){
                    memberDTO.setNickName(remarkEntry.getRemark());
                }
            }
            members.add(memberDTO);
        }
        return members;
    }

    /**
     * 判断是否是管理员 包含群主，副群主
     *
     * @param groupId
     * @param uid
     * @return
     */
    public boolean isManager(ObjectId groupId, ObjectId uid) {
        return memberDao.isManager(groupId, uid);
    }

    /**
     * 社长
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isHead(ObjectId groupId, ObjectId userId) {
        return memberDao.isHead(groupId, userId);
    }

    /**
     * 获取 社长
     *
     * @param groupId
     * @return
     */
    public MemberDTO getHead(ObjectId groupId) {
        MemberEntry memberEntry = memberDao.getHead(groupId);
        return memberEntry == null ? null : new MemberDTO(memberEntry);
    }


    /**
     * 获取可退出列表
     *
     * @param groupId
     * @return
     */
    public List<ObjectId> getGroupQuitList(ObjectId groupId) {
        return memberDao.getQuitList(groupId);
    }

    /**
     * 更新我在群里的昵称
     *
     * @param groupId
     * @param userId
     * @param nickName
     */
    @Async
    public void updateMyNickname(ObjectId groupId, ObjectId userId, String nickName) {
        memberDao.updateMyNickname(groupId, userId, nickName);
    }

    /**
     * 更新 免打扰状态
     *
     * @param groupId
     * @param userId
     * @param status
     */
    @Async
    public void updateMyStatus(ObjectId groupId, ObjectId userId, int status) {
        memberDao.updateMyStatus(groupId, userId, status);
    }

    /**
     * 获得群组成员个数
     *
     * @param groupId
     * @return
     */
    public int getMemberCount(ObjectId groupId) {
        return memberDao.getMemberCount(groupId);
    }

    /**
     * 获取全部成员
     *
     * @param groupId
     * @return
     */
    public Object getAllGroupMembers(ObjectId groupId,ObjectId userId) {
        List<MemberEntry> entries = memberDao.getAllMembers(groupId);
        List<MemberDTO> memberDTOs = new ArrayList<MemberDTO>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for (MemberEntry entry : entries) {
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,RemarkEntry> remarkEntryMap=new HashMap<ObjectId, RemarkEntry>();
        if(null!=userId) {
            remarkEntryMap=remarkDao.find(userId, userIds);
        }
        for (MemberEntry entry : entries) {
            MemberDTO memberDTO=new MemberDTO(entry);
            if(null!=remarkEntryMap){
                RemarkEntry remarkEntry=remarkEntryMap.get(entry.getUserId());
                if(null!=remarkEntry){
                    memberDTO.setNickName(remarkEntry.getRemark());
                }
            }
            memberDTOs.add(memberDTO);
        }
        return memberDTOs;
    }

    /**
     * 更改头像
     *
     * @param userId
     * @param avatar
     */
    @Async
    public void updateAllAvatar(ObjectId userId, String avatar) {
        memberDao.updateAllAvatar(userId, avatar);
    }


    public List<CommunityDTO>  getManageCommunitysByUserId(ObjectId userId){
        List<CommunityDTO> dtos=new ArrayList<CommunityDTO>();
        List<ObjectId> groupIds=memberDao.getManagerGroupIdsByUserId(userId);
        List<CommunityEntry> entries=communityDao.getCommunityEntriesByGroupIds(groupIds);
        for(CommunityEntry entry:entries){
            dtos.add(new CommunityDTO(entry));
        }
        return dtos;
    }

    public boolean judgeManagePermissionOfUser(ObjectId userId){
        return memberDao.judgeManagePermissionOfUser(userId);
    }

    public int judgePersonPermission(ObjectId userId){
        int status=0;
        if(memberDao.judgeIsParent(userId)){
            status=2;
            if(memberDao.judgeManagePermissionOfUser(userId)){
                status=3;
            }
        }else{
            if(memberDao.judgeManagePermissionOfUser(userId)){
                status=1;
            }
        }
        return status;
    }
}
