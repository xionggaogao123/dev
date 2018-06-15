package com.fulaan.systemMessage.dto;

import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.fulaan.pojo.Attachement;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.sys.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-13.
 */
public class SuperTopicDTO {
    private String id;
    private String logo;
    private String title;
    private String userName;
    private String readName;
    private List<Attachement> imageUrl = new ArrayList<Attachement>();
    private String url;
    private String createTime;
    private int voteType;

    public SuperTopicDTO(){

    }

    public SuperTopicDTO(CommunityDetailEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.title = e.getCommunityTitle();
            this.logo = e.getShareUrl();
            this.userName = e.getShareImage();
            this.readName = e.getShareTitle();
            this.url = e.getCommunityContent();
            this.voteType = e.getVoteType();
            for (AttachmentEntry entry : e.getImageList()) {
                this.imageUrl.add(new Attachement(entry));
            }
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
        }else{
            new HourClassDTO();
        }

    }

    public int getVoteType() {
        return voteType;
    }

    public void setVoteType(int voteType) {
        this.voteType = voteType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReadName() {
        return readName;
    }

    public void setReadName(String readName) {
        this.readName = readName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public List<Attachement> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<Attachement> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
