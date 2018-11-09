package com.fulaan.appvote.dto;

import com.fulaan.dto.VideoDTO;
import com.fulaan.pojo.Attachement;
import com.pojo.appvote.AppNewVoteEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.VideoEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-10-29.
 */
public class AppNewVoteDTO {
    private String id;
    private String title;    //提交
    private String content;  //提交
    private String schoolId;
    private String subjectId;
    private String subjectName;
    private String communityNames;
    private String userId;
    private String userName;
    private String avatar;
    private int role;
    private int userCount;
    private List<VideoDTO> videoList=new ArrayList<VideoDTO>();           //提交
    private List<Attachement> imageList=new ArrayList<Attachement>();     //提交
    private List<Attachement> attachements=new ArrayList<Attachement>();  //提交
    private List<Attachement> voiceList=new ArrayList<Attachement>();     //提交
    private int type;                                                     //提交    1   投票（必）     2 报名（或）

    private List<Integer> applyTypeList = new ArrayList<Integer>();       //（或）提交
    private int applyCount;                                               //（或）提交  1
    private String applyStartTime;                                        //（或）提交
    private String applyEndTime;                                          //（或）提交  1

    private List<String> communityList = new ArrayList<String>();         //提交
    private List<Integer> voteTypeList = new ArrayList<Integer>();        //提交
    private int voteCount;                                                //提交
    private int sign;                                                     //提交
    private int open;                                                     //提交
    private String voteStartTime;                                         //提交
    private String voteEndTime;                                           //提交
    private int  urlType; //选项类型                                      //提交           0  文本
    private String createTime;

    private List<String> option = new ArrayList<String>(); //选项                       //（必）提交

    private int level;       //所处阶段    0  未开始     1 报名      2 投票     3 已结束
    private int isApply;        //      0 不可申请      1    可申请    2 已申请
    private int isOwner;        //      0   非自己      1    自己
    private int isVote;         //      0  不可投       1    可投      2 已投
    private String timeExpression;

    public AppNewVoteDTO(){

    }
    public AppNewVoteDTO(AppNewVoteEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.title = e.getTitle();
            this.content = e.getContent();
            this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
            this.subjectId = e.getSubjectId() == null?"":e.getSubjectId().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.role = e.getRole();
            this.userCount = e.getUserCount();
            List<AttachmentEntry> attachmentEntries = e.getImageList();
            if(attachmentEntries != null && attachmentEntries.size()>0){
                for(AttachmentEntry entry : attachmentEntries){
                    this.imageList.add(new Attachement(entry));
                }
            }
            List<AttachmentEntry> attachmentEntries2 = e.getAttachmentEntries();
            if(attachmentEntries2 != null && attachmentEntries2.size()>0){
                for(AttachmentEntry entry2 : attachmentEntries2){
                    this.attachements.add(new Attachement(entry2));
                }
            }
            List<AttachmentEntry> attachmentEntries3 = e.getVoiceList();
            if(attachmentEntries3 != null && attachmentEntries3.size()>0){
                for(AttachmentEntry entry3 : attachmentEntries3){
                    this.voiceList.add(new Attachement(entry3));
                }
            }
            List<VideoEntry> videoEntries = e.getVideoList();
            if(videoEntries != null && videoEntries.size()>0) {
                for (VideoEntry entry3 : videoEntries) {
                    this.videoList.add(new VideoDTO(entry3));
                }
            }
            this.type = e.getType();
            this.applyCount = e.getApplyCount();
            this.applyTypeList = e.getApplyTypeList();
            if(e.getApplyStartTime()!=0l){
                this.applyStartTime = DateTimeUtils.getLongToStrTimeTwo(e.getApplyStartTime());
            }else{
                this.applyStartTime = "";
            }
            if(e.getApplyEndTime()!=0l){
                this.applyEndTime = DateTimeUtils.getLongToStrTimeTwo(e.getApplyEndTime());
            }else{
                this.applyEndTime = "";
            }
            if(e.getVoteStartTime()!=0l){
                this.voteStartTime = DateTimeUtils.getLongToStrTimeTwo(e.getVoteStartTime());
            }else{
                this.voteStartTime = "";
            }
            if(e.getVoteEndTime()!=0l){
                this.voteEndTime = DateTimeUtils.getLongToStrTimeTwo(e.getVoteEndTime());
            }else{
                this.voteEndTime = "";
            }
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            List<ObjectId> cmIdList = e.getCommunityList();
            for (ObjectId cId : cmIdList) {
                communityList.add(cId.toString());
            }
            this.voteTypeList = e.getVoteTypeList();
            this.voteCount = e.getVoteCount();
            this.sign = e.getSign();
            this.open = e.getOpen();

        }else{
            new AppNewVoteDTO();
        }
    }

    public AppNewVoteEntry buildAddEntry(){
        ObjectId sId=null;
        if(this.getSchoolId()!=null&&!"".equals(this.getSchoolId())){
            sId=new ObjectId(this.getSchoolId());
        }
        ObjectId bId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            bId=new ObjectId(this.getSubjectId());
        }

        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        long ast = 0l;
        if(this.getApplyStartTime() != null && this.getApplyStartTime() != ""){
            ast = DateTimeUtils.getStrToLongTime(this.getApplyStartTime(),"yyyy-MM-dd HH:mm");
        }
        long aet = 0l;
        if(this.getApplyEndTime() != null && this.getApplyEndTime() != ""){
            aet = DateTimeUtils.getStrToLongTime(this.getApplyEndTime(),"yyyy-MM-dd HH:mm");
        }
        long vst = 0l;
        if(this.getVoteStartTime() != null && this.getVoteStartTime() != ""){
            vst = DateTimeUtils.getStrToLongTime(this.getVoteStartTime(),"yyyy-MM-dd HH:mm");
        }
        long vet = 0l;
        if(this.getVoteEndTime() != null && this.getVoteEndTime() != ""){
            vet = DateTimeUtils.getStrToLongTime(this.getVoteEndTime(),"yyyy-MM-dd HH:mm");
        }
        List<VideoEntry> videoEntries=new ArrayList<VideoEntry>();
        if(videoList.size()>0){
            for(VideoDTO videoDTO:videoList){
                videoEntries.add(new VideoEntry(videoDTO.getVideoUrl(),
                        videoDTO.getImageUrl(),System.currentTimeMillis(), uId));
            }
        }
        List<AttachmentEntry> imageEntries=new ArrayList<AttachmentEntry>();
        if(imageList.size()>0){
            for(Attachement image:imageList){
                imageEntries.add(new AttachmentEntry(image.getUrl(), image.getFlnm(),
                        System.currentTimeMillis(),
                        uId));
            }
        }
        List<AttachmentEntry> attachmentEntries=new ArrayList<AttachmentEntry>();
        if(attachements.size()>0){
            for(Attachement attachement:attachements){
                attachmentEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        uId));
            }
        }
        List<AttachmentEntry> voiceEntries=new ArrayList<AttachmentEntry>();
        if(voiceList.size()>0){
            for(Attachement attachement:voiceList){
                voiceEntries.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
                        System.currentTimeMillis(),
                        uId));
            }
        }
        List<ObjectId> cmIdList = new ArrayList<ObjectId>();
        for(String mId : this.communityList){
            cmIdList.add(new ObjectId(mId));
        }
        AppNewVoteEntry openEntry =
                new AppNewVoteEntry(
                        this.title,
                        this.content,
                        sId,
                        bId,
                        uId,
                        this.role,
                        this.userCount,
                        imageEntries,
                        voiceEntries,
                        videoEntries,
                        attachmentEntries,
                        this.type,
                        this.applyTypeList,
                        this.applyCount,
                        ast,
                        aet,
                        cmIdList,
                        this.voteTypeList,
                        this.voteCount,
                        this.sign,
                        this.open,
                        vst,
                        vet);
        return openEntry;

    }

    public String getTimeExpression() {
        return timeExpression;
    }

    public void setTimeExpression(String timeExpression) {
        this.timeExpression = timeExpression;
    }

    public String getCommunityNames() {
        return communityNames;
    }

    public void setCommunityNames(String communityNames) {
        this.communityNames = communityNames;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIsApply() {
        return isApply;
    }

    public void setIsApply(int isApply) {
        this.isApply = isApply;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<VideoDTO> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoDTO> videoList) {
        this.videoList = videoList;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public List<Attachement> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<Attachement> attachements) {
        this.attachements = attachements;
    }

    public List<Attachement> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<Attachement> voiceList) {
        this.voiceList = voiceList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getApplyTypeList() {
        return applyTypeList;
    }

    public void setApplyTypeList(List<Integer> applyTypeList) {
        this.applyTypeList = applyTypeList;
    }

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }


    public List<String> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(List<String> communityList) {
        this.communityList = communityList;
    }

    public List<Integer> getVoteTypeList() {
        return voteTypeList;
    }

    public void setVoteTypeList(List<Integer> voteTypeList) {
        this.voteTypeList = voteTypeList;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getApplyStartTime() {
        return applyStartTime;
    }

    public void setApplyStartTime(String applyStartTime) {
        this.applyStartTime = applyStartTime;
    }

    public String getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(String applyEndTime) {
        this.applyEndTime = applyEndTime;
    }

    public String getVoteStartTime() {
        return voteStartTime;
    }

    public void setVoteStartTime(String voteStartTime) {
        this.voteStartTime = voteStartTime;
    }

    public String getVoteEndTime() {
        return voteEndTime;
    }

    public void setVoteEndTime(String voteEndTime) {
        this.voteEndTime = voteEndTime;
    }

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
