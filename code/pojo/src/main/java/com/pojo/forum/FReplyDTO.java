package com.pojo.forum;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/5/31.
 */
public class FReplyDTO implements Serializable{

    private String fReplyId;
    private String postId;
    private String postTitle;
    private String replyImage;
    private String replyComment;
    private String postSectionId;
    private Long   time;
    private Long   timeText;
    private Long   updateTime;
    private String platform;
    private String personName;
    private String personId;
    private String plainText;

    //回帖人头像
    private String imageSrc;
    private String replyNickName;
    private List<Map<String,String>> videoList = new ArrayList<Map<String,String>>();
    private List<String> imageList = new ArrayList<String>();
    private List<String> audioList = new ArrayList<String>();

    //楼中楼回复数量
    private int replyToCount;
    //楼中楼跟帖Id
    private String replyPostId;
    //是否为楼中贴
    private int    repliesToReply;
    //楼中贴信息
    private String nickName;
    private String postTime;
    private String context;

    //楼中贴存储信息
    private List<RepliesDTO> repliesList = new ArrayList<RepliesDTO>();
    private int   repliesFlag;

    //app端上传的图片以及视频
    private String appImageList;
    private String appVideoList;
    private String appAudioStr;

    //点赞
    private List<String> userReplyList = new ArrayList<String>();
    private int    praiseCount; //该帖子的点赞数
    //楼层以及删除标志
    private int floor;
    private int remove;

    //判断是否回帖点赞
    private int isZan;

    private String version;
    private String versionVideo;

    //设置标志位让管理员下载视频
    private int download;

    private String voiceFile;
    private List<Map<String,String>> voiceList;


    //========================================作文比赛
    private String word;
    private String pdf;
    private String wordName;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public void setWordName(String word){
        this.wordName = word;
    }

    public String getWordName(){
        return wordName;
    }

    public FReplyDTO(){}

    public FReplyDTO(FReplyEntry fReplyEntry){
        this.fReplyId=fReplyEntry.getID().toString();
        if(null !=fReplyEntry.getPostId()){
            this.postId=fReplyEntry.getPostId().toString();
        }else{
            this.postId="";
        }
        this.word = fReplyEntry.getUserWord();
        this.pdf = fReplyEntry.getUserPdf();
        this.wordName = fReplyEntry.getUserWordName();

        this.postTitle=fReplyEntry.getPostTitle();
        this.replyImage=fReplyEntry.getReplyImage();
        this.replyComment=fReplyEntry.getReplyComment();
        this.postSectionId=fReplyEntry.getPostSectionId().toString();
        this.time=fReplyEntry.getTime();
        this.updateTime=fReplyEntry.getUpdateTime();
        this.platform=fReplyEntry.getPlatform();
        this.personName=fReplyEntry.getPersonName();
        this.praiseCount=fReplyEntry.getPraiseCount();
        if(null !=fReplyEntry.getPersonId()){
            this.personId=fReplyEntry.getPersonId().toString();
        }else{
            this.personId="";
        }

        this.remove=fReplyEntry.getRemove();
        this.floor=fReplyEntry.getFloor();

        this.plainText=fReplyEntry.getPlainText();
        this.repliesToReply=fReplyEntry.getRepliesToReply();

        this.repliesFlag=fReplyEntry.getRepliesFlag();

        this.appImageList=fReplyEntry.getAppImageList();
        this.appVideoList=fReplyEntry.getAppVideoList();
        this.version=fReplyEntry.getVersion();
        this.versionVideo=fReplyEntry.getVersionVideo();
        this.voiceFile=fReplyEntry.getVoiceFile();
        List<FReplyEntry.Replies> replies = fReplyEntry.getRepliesList();
        if(null != replies){
            for(FReplyEntry.Replies reply : replies){
                RepliesDTO repliesDTO = new RepliesDTO(reply);
                repliesDTO.setRpid(fReplyEntry.getReplyPostId().toString());
                this.repliesList.add(repliesDTO);
            }
        }
        if(null!=fReplyEntry.getReplyPostId()){
            this.replyPostId=fReplyEntry.getReplyPostId().toString();
        }
        for(ObjectId id : fReplyEntry.getUserReplyList()){
            this.userReplyList.add(id.toString());
        }
    }
    public FReplyEntry exportEntry() {
        FReplyEntry fReplyEntry = new FReplyEntry();
        if(fReplyId != null &&!fReplyId.equals("")){
            fReplyEntry.setID(new ObjectId(fReplyId));
        }
        if(postId != null&& !postId.equals("")){
            fReplyEntry.setPostId(new ObjectId((postId)));
        }

        //==============word
        fReplyEntry.setUserWord(word);
        fReplyEntry.setUserPdf(pdf);
        fReplyEntry.setWordName(wordName);

        //==================

        fReplyEntry.setPostTitle(postTitle);
        fReplyEntry.setReplyImage(replyImage);
        fReplyEntry.setReplyComment(replyComment);
        fReplyEntry.setPlatform(platform);
        fReplyEntry.setTime(time);
        fReplyEntry.setPersonName(personName);
        fReplyEntry.setPlainText(plainText);
        fReplyEntry.setRepliesToReply(repliesToReply);

        fReplyEntry.setRepliesFlag(repliesFlag);

        fReplyEntry.setVoiceFile(voiceFile);

        fReplyEntry.setUpdateTime(updateTime);
        List<FReplyEntry.Replies> replyList = new ArrayList<FReplyEntry.Replies>();
        if(null != repliesList){
            for(RepliesDTO repliesDTO : repliesList){
                replyList.add(repliesDTO.exportEntry());
            }
        }


        fReplyEntry.setRepliesList(replyList);
        fReplyEntry.setAppImageList(appImageList);
        fReplyEntry.setAppVideoList(appVideoList);
        fReplyEntry.setAppAudioStr(appAudioStr);
        fReplyEntry.setVersion(version);
        fReplyEntry.setVersionVideo(versionVideo);
        if(postSectionId !=null &&!postSectionId.equals("")){
            fReplyEntry.setPostSectionId(new ObjectId((postSectionId)));
        }
        if(personId !=null && !personId.equals("")){
            fReplyEntry.setPersonId(new ObjectId(personId));
        }
        if(replyPostId !=null && !replyPostId.equals("")){
            fReplyEntry.setReplyPostId(new ObjectId(replyPostId));
        }

        List<ObjectId> userReplies = new ArrayList<ObjectId>();
        if(userReplyList.size() > 0){
            for(String id : userReplyList){
                userReplies.add(new ObjectId(id));
            }
        }
        fReplyEntry.setUserReplyList(userReplies);
        fReplyEntry.setPraiseCount(praiseCount);
        fReplyEntry.setRemove(remove);
        fReplyEntry.setFloor(floor);
        return fReplyEntry;
    }

    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }

    public String getReplyImage() {
        return replyImage;
    }

    public void setReplyImage(String replyImage) {
        this.replyImage = replyImage;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getfReplyId() {
        return fReplyId;
    }

    public void setfReplyId(String fReplyId) {
        this.fReplyId = fReplyId;
    }

    public String getPostSectionId() {
        return postSectionId;
    }

    public void setPostSectionId(String postSectionId) {
        this.postSectionId = postSectionId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getReplyPostId() {
        return replyPostId;
    }

    public void setReplyPostId(String replyPostId) {
        this.replyPostId = replyPostId;
    }

    public List<RepliesDTO> getRepliesList() {
        return repliesList;
    }

    public void setRepliesList(List<RepliesDTO> repliesList) {
        this.repliesList = repliesList;
    }

    public int getRepliesFlag() {
        return repliesFlag;
    }

    public void setRepliesFlag(int repliesFlag) {
        this.repliesFlag = repliesFlag;
    }

    public int getRepliesToReply() {
        return repliesToReply;
    }

    public void setRepliesToReply(int repliesToReply) {
        this.repliesToReply = repliesToReply;
    }

    public String getReplyNickName() {
        return replyNickName;
    }

    public void setReplyNickName(String replyNickName) {
        this.replyNickName = replyNickName;
    }

    public List<Map<String, String>> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Map<String, String>> videoList) {
        this.videoList = videoList;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getAppImageList() {
        return appImageList;
    }

    public void setAppImageList(String appImageList) {
        this.appImageList = appImageList;
    }

    public String getAppVideoList() {
        return appVideoList;
    }


    public void setAppVideoList(String appVideoList) {
        this.appVideoList = appVideoList;
    }

    public String getAppAudioStr() {
        return appAudioStr;
    }

    public void setAppAudioStr(String appAudioStr) {
        this.appAudioStr = appAudioStr;
    }

    public List<String> getAudioList() {
        return audioList;
    }

    public void setAudioList(List<String> audioList) {
        this.audioList = audioList;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public List<String> getUserReplyList() {
        return userReplyList;
    }

    public void setUserReplyList(List<String> userReplyList) {
        this.userReplyList = userReplyList;
    }

    public int getRemove() {
        return remove;
    }

    public void setRemove(int remove) {
        this.remove = remove;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsZan() {
        return isZan;
    }

    public void setIsZan(int isZan) {
        this.isZan = isZan;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionVideo() {
        return versionVideo;
    }

    public void setVersionVideo(String versionVideo) {
        this.versionVideo = versionVideo;
    }

    public Long getTimeText() {
        return timeText;
    }

    public void setTimeText(Long timeText) {
        this.timeText = timeText;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getReplyToCount() {
        return replyToCount;
    }

    public void setReplyToCount(int replyToCount) {
        this.replyToCount = replyToCount;
    }

    public String getVoiceFile() {
        return voiceFile;
    }

    public void setVoiceFile(String voiceFile) {
        this.voiceFile = voiceFile;
    }

    public List<Map<String, String>> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<Map<String, String>> voiceList) {
        this.voiceList = voiceList;
    }


    @Override
    public String toString() {
        return "FReplyDTO{" +
                "fReplyId='" + fReplyId + '\'' +
                ", postId='" + postId + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", replyImage='" + replyImage + '\'' +
                ", replyComment='" + replyComment + '\'' +
                ", postSectionId='" + postSectionId + '\'' +
                ", time=" + time +
                ", timeText=" + timeText +
                ", updateTime=" + updateTime +
                ", platform='" + platform + '\'' +
                ", personName='" + personName + '\'' +
                ", personId='" + personId + '\'' +
                ", plainText='" + plainText + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", replyNickName='" + replyNickName + '\'' +
                ", videoList=" + videoList +
                ", imageList=" + imageList +
                ", audioList=" + audioList +
                ", replyToCount=" + replyToCount +
                ", replyPostId='" + replyPostId + '\'' +
                ", repliesToReply=" + repliesToReply +
                ", nickName='" + nickName + '\'' +
                ", postTime='" + postTime + '\'' +
                ", context='" + context + '\'' +
                ", repliesList=" + repliesList +
                ", repliesFlag=" + repliesFlag +
                ", appImageList='" + appImageList + '\'' +
                ", appVideoList='" + appVideoList + '\'' +
                ", appAudioStr='" + appAudioStr + '\'' +
                ", userReplyList=" + userReplyList +
                ", praiseCount=" + praiseCount +
                ", floor=" + floor +
                ", remove=" + remove +
                ", isZan=" + isZan +
                ", version='" + version + '\'' +
                ", versionVideo='" + versionVideo + '\'' +
                ", download=" + download +
                ", voiceFile='" + voiceFile + '\'' +
                ", voiceList=" + voiceList +
                ", word='" + word + '\'' +
                ", pdf='" + pdf + '\'' +
                ", wordName='" + wordName + '\'' +
                '}';
    }
}
