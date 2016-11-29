package com.fulaan.dto;

import com.fulaan.util.DateUtils;
import com.pojo.fcommunity.CommunityEntry;

import java.util.List;

/**
 * Created by admin on 2016/10/24.
 */
public class CommunityDTO {

    private String id;
    private String groupId;
    private String searchId;
    private String emChatId;
    private String name;
    private String logo;
    private String desc;
    private String qrUrl;
    private String createTime;
    private String owerId;

    private List<MemberDTO> members;
    private MemberDTO head;
    private int memberCount;

    private int open;

    private String headImage;
    private boolean isJoin;

    private MemberDTO mine;

    private String errorMsg;

    public CommunityDTO() {

    }

    public CommunityDTO(CommunityEntry communityEntry) {
        this.id = communityEntry.getID().toString();
        this.searchId = communityEntry.getSearchId();
        this.name = communityEntry.getCommunityName();
        this.logo = communityEntry.getCommunityLogo();
        this.desc = communityEntry.getCommunityDescription();
        this.qrUrl = communityEntry.getCommunityQRCode();
        this.createTime = DateUtils.timeStampToStr(communityEntry.getCommunityTime() / 1000);
        this.groupId = communityEntry.getGroupId();
        this.owerId = communityEntry.getOwerID().toString();
        this.emChatId = communityEntry.getEmChatId();
        this.open = communityEntry.getOpen();
    }

    public MemberDTO getMine() {
        return mine;
    }

    public void setMine(MemberDTO mine) {
        this.mine = mine;
    }

    public boolean getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(boolean isJoin) {
        this.isJoin = isJoin;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getEmChatId() {
        return emChatId;
    }

    public void setEmChatId(String emChatId) {
        this.emChatId = emChatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOwerId() {
        return owerId;
    }

    public void setOwerId(String owerId) {
        this.owerId = owerId;
    }

    public List<MemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDTO> members) {
        this.members = members;
    }

    public MemberDTO getHead() {
        return head;
    }

    public void setHead(MemberDTO head) {
        this.head = head;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}