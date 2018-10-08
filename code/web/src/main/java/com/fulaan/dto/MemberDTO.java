package com.fulaan.dto;

import com.fulaan.newVersionBind.dto.NewVersionBindRelationDTO;
import com.fulaan.util.DateUtils;
import com.fulaan.util.OperationType;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.user.AvatarType;
import com.sys.utils.AvatarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/10/24.
 */

public class MemberDTO {

    private String id;
    private String userId;
    private String time;
    private String avator;
    private String userName;
    private String nickName;
    private String groupId;
    private String roleStr;
    private int status;
    private int flag;
    private int role;
    private List<String> tags = new ArrayList<String>();
    private List<String> playmate = new ArrayList<String>();
    private int playmateCount;
    private int tagType;//0:没有标签 1:有标签
    private int playmateFlag;
    private String remarkId;
    //判断是否本人的标志位
    private int isOwner;

    //获取某个社区下绑定的孩子有哪些（userId在communityId下的孩子）
    private List<NewVersionBindRelationDTO> bindRelationDTOList;

    private String jiaId;

    public MemberDTO() {

    }

    public MemberDTO(MemberEntry entry) {
        this.id = entry.getID().toString();
        this.userId = entry.getUserId().toString();
        this.time = DateUtils.timeStampToStr(entry.getID().getTimestamp());
        this.nickName = entry.getNickName();
        this.status = entry.getStatus();
        this.role = entry.getRole();
        this.roleStr = getRoleStr(entry.getRole());
        this.userName = entry.getUserName();
        this.avator = AvatarUtils.getAvatar(entry.getAvator(), AvatarType.MIN_AVATAR.getType());
        if (null != entry.getGroupId()) {
            this.groupId = entry.getGroupId().toString();
        }

    }

    private String getRoleStr(int role) {
        String roleStr;
        switch (role) {
            case 0:
                roleStr = OperationType.NORMAL.getDecs();
                break;
            case 1:
                roleStr = OperationType.MONITOR.getDecs();
                break;
            case 2:
                roleStr = OperationType.MANAGEMENT.getDecs();
                break;

            default:
                roleStr = "";
        }
        return roleStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getPlaymate() {
        return playmate;
    }

    public void setPlaymate(List<String> playmate) {
        this.playmate = playmate;
    }

    public int getPlaymateCount() {
        return playmateCount;
    }

    public void setPlaymateCount(int playmateCount) {
        this.playmateCount = playmateCount;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public int isPlaymateFlag() {
        return playmateFlag;
    }

    public void setPlaymateFlag(int playmateFlag) {
        this.playmateFlag = playmateFlag;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }

    public int getPlaymateFlag() {
        return playmateFlag;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public List<NewVersionBindRelationDTO> getBindRelationDTOList() {
        return bindRelationDTOList;
    }

    public void setBindRelationDTOList(List<NewVersionBindRelationDTO> bindRelationDTOList) {
        this.bindRelationDTOList = bindRelationDTOList;
    }

    public String getJiaId() {
        return jiaId;
    }

    public void setJiaId(String jiaId) {
        this.jiaId = jiaId;
    }
}
