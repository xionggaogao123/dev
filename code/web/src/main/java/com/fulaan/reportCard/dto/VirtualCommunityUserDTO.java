package com.fulaan.reportCard.dto;

/**
 * Created by scott on 2017/11/24.
 */
public class VirtualCommunityUserDTO {

    private String communityId;
    private String communityName;
    private String sheZhang;
    private String fileName;
    private int userCount;

    public VirtualCommunityUserDTO(){

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getSheZhang() {
        return sheZhang;
    }

    public void setSheZhang(String sheZhang) {
        this.sheZhang = sheZhang;
    }
    
    
}
