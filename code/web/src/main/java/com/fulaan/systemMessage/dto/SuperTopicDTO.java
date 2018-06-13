package com.fulaan.systemMessage.dto;

import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by James on 2018-06-13.
 */
public class SuperTopicDTO {
    private String id;
    private String logo;
    private String title;
    private String userName;
    private String readName;
    private String url;
    private String createTime;

    public SuperTopicDTO(){

    }

    public SuperTopicDTO(CommunityDetailEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.title = e.getCommunityTitle();
            this.userName = e.getShareImage();
            this.readName = e.getShareTitle();
            this.url = e.getCommunityContent();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
        }else{
            new HourClassDTO();
        }

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
}
