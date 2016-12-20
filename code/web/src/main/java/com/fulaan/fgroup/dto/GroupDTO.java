package com.fulaan.fgroup.dto;


import com.fulaan.dto.MemberDTO;
import com.pojo.fcommunity.GroupEntry;

import java.util.List;

/**
 * Created by jerry on 2016/11/3.
 * GroupDTO
 */
public class GroupDTO {

    private String id;
    private String emChatId;
    private String communityId;
    private String name;
    private String logo;
    private String desc;
    private String qrUrl;
    private String owerId;
    private String headImage;
    private Object curAnnounceMent;
    private boolean isBindCommunity;
    private int count;
    private String searchId;
    private int isM;

    private List<MemberDTO> members;
    private List<MemberDTO> managers;

    private MemberDTO mine;

    public GroupDTO(GroupEntry groupEntry, List<MemberDTO> members,
                    List<MemberDTO> managers) {
        this.id = groupEntry.getID().toString();
        this.emChatId = groupEntry.getEmChatId();
        this.name = groupEntry.getName();
        this.logo = groupEntry.getLogo();
        this.desc = groupEntry.getDesc();
        this.qrUrl = groupEntry.getQrUrl();
        this.owerId = groupEntry.getOwerId().toString();
        this.headImage = groupEntry.getHeadImage();
        this.members = members;
        this.managers = managers;
        this.isM = groupEntry.getIsName();
        this.communityId = groupEntry.getCommunityId() == null ? "" : groupEntry.getCommunityId().toString();
        this.isBindCommunity = groupEntry.getCommunityId() != null;
    }

    public int getIsM() {
        return isM;
    }

    public void setIsM(int isM) {
        this.isM = isM;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmChatId() {
        return emChatId;
    }

    public void setEmChatId(String emChatId) {
        this.emChatId = emChatId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public String getOwerId() {
        return owerId;
    }

    public void setOwerId(String owerId) {
        this.owerId = owerId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public boolean isBindCommunity() {
        return isBindCommunity;
    }

    public void setBindCommunity(boolean bindCommunity) {
        isBindCommunity = bindCommunity;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDTO> members) {
        this.members = members;
    }

    public List<MemberDTO> getManagers() {
        return managers;
    }

    public void setManagers(List<MemberDTO> managers) {
        this.managers = managers;
    }

    public MemberDTO getMine() {
        return mine;
    }

    public void setMine(MemberDTO mine) {
        this.mine = mine;
    }

    public Object getCurAnnounceMent() {
        return curAnnounceMent;
    }

    public void setCurAnnounceMent(Object curAnnounceMent) {
        this.curAnnounceMent = curAnnounceMent;
    }
}
