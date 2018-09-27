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
    private String timeStr;
    private int voteType;
    private int isPublic;

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
            this.timeStr = getTimeStr(e.getCreateTime());
            try {
                this.isPublic = e.getIsPublic();
            } catch (Exception e2) {
                // TODO: handle exception
                this.isPublic = 0;
            }
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

    private String getTimeStr(long time) {
        long nowTime = System.currentTimeMillis();
        long compareTime = nowTime - time;
        long month = 24 * 60 * 60 * 1000 * 30l;
        long day = 24 * 60 * 60 * 1000l;
        long hour = 60 * 60 * 1000l;
        long minute = 60 * 1000l;
        long sencond = 1000l;
        if (compareTime > month) {
            return DateTimeUtils.convert(time, DateTimeUtils.DATE_YYYY_MM_DD);
        } else if (compareTime > day) {
            return (int) compareTime / day + "天前";
        } else if (compareTime > hour) {
            return (int) compareTime / hour + "小时前";
        } else if (compareTime > minute) {
            return (int) compareTime / minute + "分前";
        } else {
            return (int) compareTime / sencond + "秒前";
        }
    }
    
    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
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

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }
    
    
}
