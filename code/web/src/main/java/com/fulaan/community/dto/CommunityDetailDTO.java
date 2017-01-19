package com.fulaan.community.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import com.fulaan.util.DateUtils;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.PartInContentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/24.
 */
public class CommunityDetailDTO {

    private String id;
    private String communityId;
    private String communityName;
    private String userId;
    private String title;
    private String content;
    private int type;
    private String time;
    private String imageUrl;
    private String nickName;
    private int partInCount;
    private List<String> unReadList = new ArrayList<String>();
    private List<String> partInList = new ArrayList<String>();
    private List<Attachement> attachements = new ArrayList<Attachement>();
    private List<Attachement> vedios = new ArrayList<Attachement>();
    private List<Attachement> images = new ArrayList<Attachement>();
    private List<Attachement> voices = new ArrayList<Attachement>();

    private String shareUrl;
    private String shareImage;
    private String shareTitle;
    private String sharePrice;

    //发活动人的权限
    private String roleStr;

    private int partIncotentCount;

    //时间显示
    private String timeStr;

    private int unReadCount;

    //判断登录人的权限
    private int operation;

    private int readFlag;

    //投票
    //投票截止标志 1:未截止已:未截止
    private int voteDeadFlag;
    //投票人数
    private int voteCount;
    //投票信息
    //投票内容
    private String voteContent;
    private int voteMaxCount;
    private int voteType;
    private long voteDeadTime;
    //视频字段
    private List<VideoDTO> videoDTOs=new ArrayList<VideoDTO>();
    //判断是否投票了 1:已投票，2:未投票
    private int hasVoted;

    //内容列表
    private List<String> voteOptions;

    //投票结果列表
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();



    private List<PartInContentDTO> partList = new ArrayList<PartInContentDTO>();

    public CommunityDetailDTO() {

    }

    public CommunityDetailDTO(CommunityDetailEntry communityDetailEntry, List<PartInContentEntry> partInContentEntrys) {
        this.id = communityDetailEntry.getID().toString();
        this.communityId = communityDetailEntry.getCommunityId();
        this.userId = communityDetailEntry.getCommunityUserId().toString();
        this.title = communityDetailEntry.getCommunityTitle();
        this.content = communityDetailEntry.getCommunityContent();
        this.type = communityDetailEntry.getCommunityType();
        this.time = DateUtils.timeStampToStr(communityDetailEntry.getCreateTime() / 1000);
        this.partInCount = communityDetailEntry.getPartInList().size();
        List<ObjectId> communityDetailEntryUnReadList = communityDetailEntry.getUnReadList();
        if (null != communityDetailEntryUnReadList) {
            for (ObjectId objectId : communityDetailEntryUnReadList) {
                if(objectId != null) {
                    this.unReadList.add(objectId.toString());
                }
            }
        }
        List<ObjectId> communityDetailEntryPartInList = communityDetailEntry.getPartInList();
        if (null != communityDetailEntryPartInList) {
            for (ObjectId objectId : communityDetailEntryPartInList) {
                this.partInList.add(objectId.toString());
            }
        }

        this.attachements = getAttachments(communityDetailEntry.getAttachmentList());
        this.images = getAttachments(communityDetailEntry.getImageList());
        this.vedios = getAttachments(communityDetailEntry.getVoiceList());
        if (null != partInContentEntrys) {
            for (PartInContentEntry partInContent : partInContentEntrys) {
                this.partList.add(new PartInContentDTO(partInContent));
            }
        }

        this.shareImage = communityDetailEntry.getShareImage();
        this.shareUrl = communityDetailEntry.getShareUrl();
        this.shareTitle = communityDetailEntry.getShareTitle();
        this.sharePrice = communityDetailEntry.getSharePrice();
        this.voteContent = communityDetailEntry.getVoteContent();
        this.voteMaxCount = communityDetailEntry.getVoteMaxCount();
        this.voteDeadTime = communityDetailEntry.getVoteDeadTime();
        this.voteType = communityDetailEntry.getVoteType();
        this.videoDTOs = getVideos(communityDetailEntry.getVideoList());
    }

    public CommunityDetailDTO(CommunityDetailEntry communityDetailEntry) {
        this.id = communityDetailEntry.getID().toString();
        this.communityId = communityDetailEntry.getCommunityId();
        this.userId = communityDetailEntry.getCommunityUserId().toString();
        this.title = communityDetailEntry.getCommunityTitle();
        this.content = communityDetailEntry.getCommunityContent();
        this.type = communityDetailEntry.getCommunityType();
        this.time = DateUtils.timeStampToStr(communityDetailEntry.getCreateTime() / 1000);

        this.partInCount = communityDetailEntry.getPartInList().size();
        List<ObjectId> communityDetailEntryUnReadList = communityDetailEntry.getUnReadList();
        if (null != communityDetailEntryUnReadList) {
            for (ObjectId objectId : communityDetailEntryUnReadList) {
                if(null!=objectId){
                    this.unReadList.add(objectId.toString());
                }
            }
        }

        List<ObjectId> communityDetailEntryPartInList = communityDetailEntry.getPartInList();
        if (null != communityDetailEntryPartInList) {
            for (ObjectId objectId : communityDetailEntryPartInList) {
                this.partInList.add(objectId.toString());
            }
        }

        this.shareImage = communityDetailEntry.getShareImage();
        this.shareUrl = communityDetailEntry.getShareUrl();
        this.shareTitle = communityDetailEntry.getShareTitle();
        this.sharePrice = communityDetailEntry.getSharePrice();

        this.attachements = getAttachments(communityDetailEntry.getAttachmentList());
        this.images = getAttachments(communityDetailEntry.getImageList());
        this.vedios = getAttachments(communityDetailEntry.getVoiceList());

        this.timeStr = getTimeStr(communityDetailEntry.getCreateTime());
        this.voteContent = communityDetailEntry.getVoteContent();
        this.voteMaxCount = communityDetailEntry.getVoteMaxCount();
        this.voteDeadTime = communityDetailEntry.getVoteDeadTime();
        this.voteType = communityDetailEntry.getVoteType();
        this.videoDTOs = getVideos(communityDetailEntry.getVideoList());
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

    private List<Attachement> getAttachments(List<AttachmentEntry> entries) {
        List<Attachement> attachements = new ArrayList<Attachement>();
        for (AttachmentEntry entry : entries) {
            attachements.add(new Attachement(entry));
        }
        return attachements;
    }

    private List<VideoDTO> getVideos(List<VideoEntry> entries){
        List<VideoDTO> videoDTOs= new ArrayList<VideoDTO>();
        for(VideoEntry videoEntry:entries){
            videoDTOs.add(new VideoDTO(videoEntry));
        }
        return videoDTOs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPartInCount() {
        return partInCount;
    }

    public void setPartInCount(int partInCount) {
        this.partInCount = partInCount;
    }

    public List<String> getUnReadList() {
        return unReadList;
    }

    public void setUnReadList(List<String> unReadList) {
        this.unReadList = unReadList;
    }

    public List<String> getPartInList() {
        return partInList;
    }

    public void setPartInList(List<String> partInList) {
        this.partInList = partInList;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }

    public List<Attachement> getVedios() {
        return vedios;
    }

    public void setVedios(List<Attachement> vedios) {
        this.vedios = vedios;
    }

    public List<Attachement> getImages() {
        return images;
    }

    public void setImages(List<Attachement> images) {
        this.images = images;
    }

    public List<Attachement> getVoices() {
        return voices;
    }

    public void setVoices(List<Attachement> voices) {
        this.voices = voices;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(String sharePrice) {
        this.sharePrice = sharePrice;
    }

    public List<PartInContentDTO> getPartList() {
        return partList;
    }

    public void setPartList(List<PartInContentDTO> partList) {
        this.partList = partList;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    public int getPartIncotentCount() {
        return partIncotentCount;
    }

    public void setPartIncotentCount(int partIncotentCount) {
        this.partIncotentCount = partIncotentCount;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(int readFlag) {
        this.readFlag = readFlag;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(String voteContent) {
        this.voteContent = voteContent;
    }

    public int getVoteMaxCount() {
        return voteMaxCount;
    }

    public void setVoteMaxCount(int voteMaxCount) {
        this.voteMaxCount = voteMaxCount;
    }

    public int getVoteType() {
        return voteType;
    }

    public void setVoteType(int voteType) {
        this.voteType = voteType;
    }

    public long getVoteDeadTime() {
        return voteDeadTime;
    }

    public void setVoteDeadTime(long voteDeadTime) {
        this.voteDeadTime = voteDeadTime;
    }

    public int getVoteDeadFlag() {
        return voteDeadFlag;
    }

    public void setVoteDeadFlag(int voteDeadFlag) {
        this.voteDeadFlag = voteDeadFlag;
    }

    public List<VideoDTO> getVideoDTOs() {
        return videoDTOs;
    }

    public void setVideoDTOs(List<VideoDTO> videoDTOs) {
        this.videoDTOs = videoDTOs;
    }

    public int getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(int hasVoted) {
        this.hasVoted = hasVoted;
    }

    public List<String> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(List<String> voteOptions) {
        this.voteOptions = voteOptions;
    }

    public List<Map<String, Object>> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map<String, Object>> mapList) {
        this.mapList = mapList;
    }
}
