package com.pojo.forum;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/5/31.
 */
public class FPostDTO {

  private String fpostId;
  private String personId;
  private String personName;
  private String postTitle;
  private String postSectionId;
  private String postSectionName;
  private Long time;
  private Long updateTime;
  private Long updateDateTime;
  private String postSectionLevelId;
  private String postSectionLevelName;
  private int cream;
  private int isTop;
  private int scanCount;
  private int commentCount;
  private int postSectionCount;
  private String comment;
  private int draught;
  private int classify;
  private String platform;
  private String classifyContent;
  private String plainText;
  private String imageSrc;
  private String creamText; //精华
  private String topText; //置顶
  private String timeText;
  private String image;
  private int praiseCount; //该帖子的点赞数
  private int opposeCount; //该贴子的反对数
  private String replyUserId;
  private String replyUserName;
  private long replyTime;
  private List<String> userReplyList = new ArrayList<String>();
  private List<String> opposeList = new ArrayList<String>();
  //是否回帖
  private int logReply;
  //活动
  private String actionHeadImage;
  private String activityImage;
  private String activityMemo;
  private String activityTime;
  private String activityStartTime;
  private String activityEndTime;
  private List<Map<String, String>> videoList = new ArrayList<Map<String, String>>();
  private List<String> imageList = new ArrayList<String>();
  private int inSet;
  private int cate;  //帖子类型

  //app端上传的图片以及视频
  private String appImageList;
  private String appVideoList;
  //app端组装内容
  private String appComment;

  //投票贴
  private String voteContent;
  private String voteSelect;
  private int type;
  private int optionCount;
  //显示投票贴
  private List<String> voteOptions;

  //存储举报信息
  private List<ReportedDTO> reportedDTOList = new ArrayList<ReportedDTO>();
  private int reported;
  //举报经验值
  private int reportedExperience;
  //留言
  private String reportedComment;
  //未编辑内容
  private String backUpComment;

  //显示举报信息
  //举报人
  private String reportedUserName;
  //举报时间
  private String reportedTime;
  //提示信息
  private String reportedTip;

  //帖子删除状态
  private int removeStatus;

  //合并Id和板块名称
  private String merge;

  private String version;
  private String versionVideo;

  //回帖奖励
  private int rewardScore;
  private int rewardCount;
  private int rewardMax;
  //悬赏帖
  private Long offeredScore;
  private int offeredCompleted;
  //最佳答案
  private String solution;

  //标志位
  private int postFlag;

  //主标题
  private String mainTitle;
  //小标题
  private String title;
  //一部分内容
  private String partContent;

  public FPostDTO() {
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }

  public FPostDTO(FPostEntry fPostEntry) {

    this.fpostId = fPostEntry.getID().toString();
    this.personId = fPostEntry.getPersonId().toString();
    this.replyUserId = fPostEntry.getReplyUserId();
    this.personName = fPostEntry.getPersonName();
    this.postTitle = fPostEntry.getPostTitle();
    this.postSectionId = fPostEntry.getPostSectionId().toString();
    this.postSectionName = fPostEntry.getPostSectionName();
    this.time = fPostEntry.getTime();
    this.updateTime = fPostEntry.getUpdateTime();
    this.updateDateTime = fPostEntry.getUpdateDateTime();
    this.cate = fPostEntry.getCate();
//        this.replyTime = fPostEntry.getUpdateTime();
    if (null != fPostEntry.getPostSectionLevelId()) {
      this.postSectionLevelId = fPostEntry.getPostSectionLevelId().toString();
    } else {
      this.postSectionLevelId = "";
    }

    if (null != fPostEntry.getSolution()) {
      this.solution = fPostEntry.getSolution().toString();
    } else {
      this.solution = "";
    }
    this.postSectionLevelName = fPostEntry.getPostSectionLevelName();
    this.cream = fPostEntry.getCream();
    this.isTop = fPostEntry.getIsTop();
    this.scanCount = fPostEntry.getScanCount();
    this.commentCount = fPostEntry.getCommentCount();
    this.postSectionCount = fPostEntry.getPostSectionCount();
    this.comment = fPostEntry.getComment();
    this.draught = fPostEntry.getDraught();
    this.classify = fPostEntry.getClassify();
    this.platform = fPostEntry.getPlatform();
    this.plainText = fPostEntry.getPlainText();
    this.imageSrc = fPostEntry.getImageSrc();
    this.image = fPostEntry.getImage();
    this.praiseCount = fPostEntry.getPraiseCount();
    this.opposeCount = fPostEntry.getOpposeCount();
    this.actionHeadImage = fPostEntry.getActionHeadImage();
    this.activityImage = fPostEntry.getActivityImage();
    this.activityMemo = fPostEntry.getActivityMemo();
    this.activityTime = fPostEntry.getActivityTime().toString();
    this.activityStartTime = fPostEntry.getActivityStartTime();
    this.activityEndTime = fPostEntry.getActivityEndTime();
    this.inSet = fPostEntry.getInSet();
    this.appImageList = fPostEntry.getAppImageList();
    this.appVideoList = fPostEntry.getAppVideoList();
    this.appComment = fPostEntry.getAppComment();
    this.voteContent = fPostEntry.getVoteContent();
    this.voteSelect = fPostEntry.getVoteSelect();
    this.type = fPostEntry.getType();
    this.logReply = fPostEntry.getLogReply();
    this.removeStatus = fPostEntry.getRemoveStatus();
    this.backUpComment = fPostEntry.getBackUpComment();
    this.reportedExperience = fPostEntry.getReportedExperience();
    this.reportedComment = fPostEntry.getReportedComment();
    this.reported = fPostEntry.getReported();
    this.postFlag = fPostEntry.getPostFlag();

    this.version = fPostEntry.getVersion();
    this.optionCount = fPostEntry.getOptionCount();
    this.versionVideo = fPostEntry.getVersionVideo();

    this.rewardScore = fPostEntry.getRewardScore();
    this.rewardCount = fPostEntry.getRewardCount();
    this.rewardMax = fPostEntry.getRewardMax();

    this.offeredScore = fPostEntry.getOfferedScore();
    this.offeredCompleted = fPostEntry.getOfferedCompleted();
    for (ObjectId id : fPostEntry.getUserReplyList()) {
      this.userReplyList.add(id.toString());
    }

    for (ObjectId id : fPostEntry.getOpposeList()) {
      this.opposeList.add(id.toString());
    }

    List<FPostEntry.Reported> reportedList = fPostEntry.getReportedList();
    if (null != reportedList) {
      for (FPostEntry.Reported reported : reportedList) {
        this.reportedDTOList.add(new ReportedDTO(reported));
      }
    }

  }

  public FPostEntry exportEntry() {
    FPostEntry fPostEntry = new FPostEntry();
    if (fpostId != null && !fpostId.equals("")) {
      fPostEntry.setID(new ObjectId(fpostId));
    }
    if (!personId.equals("")) {
      fPostEntry.setPersonId(new ObjectId(personId));
    }
    fPostEntry.setPersonName(personName);
    fPostEntry.setPostTitle(postTitle);
    if (!postSectionId.equals("")) {
      fPostEntry.setPostSectionId(new ObjectId(postSectionId));
    }
    if (replyUserId != null && !replyUserId.equals("")) {
      fPostEntry.setReplyUserId(replyUserId);
    }
    fPostEntry.setPostSectionName(postSectionName);
    fPostEntry.setTime(time);
    fPostEntry.setUpdateTime(updateTime);
    fPostEntry.setUpdateDateTime(updateDateTime);
    if (postSectionLevelId != null && !postSectionLevelId.equals("")) {
      fPostEntry.setPostSectionLevelId(new ObjectId(postSectionLevelId));
    }

    if (StringUtils.isNotBlank(solution)) {
      fPostEntry.setSolution(new ObjectId(solution));
    }
    fPostEntry.setPostSectionLevelName(postSectionLevelName);
    fPostEntry.setCream(cream);
    fPostEntry.setIsTop(isTop);
    fPostEntry.setScanCount(scanCount);
    fPostEntry.setCommentCount(commentCount);
    fPostEntry.setPostSectionCount(postSectionCount);
    fPostEntry.setComment(comment);
    fPostEntry.setDraught(draught);
    fPostEntry.setClassify(classify);
    fPostEntry.setPlatform(platform);
    fPostEntry.setPlainText(plainText);
    fPostEntry.setImageSrc(imageSrc);
    fPostEntry.setImage(image);
    fPostEntry.setPraiseCount(praiseCount);
    fPostEntry.setOpposeCount(opposeCount);
    fPostEntry.setLogReply(logReply);
    fPostEntry.setActionHeadImage(actionHeadImage);
    fPostEntry.setActivityImage(activityImage);
    fPostEntry.setActivityMemo(activityMemo);
    fPostEntry.setActivityStartTime(activityStartTime);
    fPostEntry.setActivityEndTime(activityEndTime);
    fPostEntry.setReported(reported);
    fPostEntry.setRemoveStatus(removeStatus);
    fPostEntry.setOptionCount(optionCount);
    fPostEntry.setPostFlag(postFlag);
    if (null != activityTime) {
      fPostEntry.setActivityTime(Long.parseLong(activityTime));
    }
    fPostEntry.setInSet(inSet);
    fPostEntry.setAppImageList(appImageList);
    fPostEntry.setAppVideoList(appVideoList);
    fPostEntry.setAppComment(appComment);
    fPostEntry.setVoteContent(voteContent);
    fPostEntry.setVoteSelect(voteSelect);
    fPostEntry.setType(type);
    fPostEntry.setBackUpComment(backUpComment);
    fPostEntry.setReportedExperience(reportedExperience);
    fPostEntry.setReportedComment(reportedComment);
    fPostEntry.setVersion(version);
    fPostEntry.setVersionVideo(versionVideo);

    fPostEntry.setRewardScore(rewardScore);
    fPostEntry.setRewardCount(rewardCount);
    fPostEntry.setRewardMax(rewardMax);
    fPostEntry.setIsRemove(0);
    fPostEntry.setOfferedScore(offeredScore);
    fPostEntry.setOfferedCompleted(offeredCompleted);
    fPostEntry.setCate(cate); //帖子类型  cate == 1 大赛帖
    List<ObjectId> userReplies = new ArrayList<ObjectId>();
    if (userReplyList.size() > 0) {
      for (String id : userReplyList) {
        userReplies.add(new ObjectId(id));
      }
    }
    fPostEntry.setUserReplyList(userReplies);

    List<ObjectId> opposes = new ArrayList<ObjectId>();
    if (opposeList.size() > 0) {
      for (String id : opposeList) {
        opposes.add(new ObjectId(id));
      }
    }
    fPostEntry.setOpposeList(opposes);

    List<FPostEntry.Reported> reportedList = new ArrayList<FPostEntry.Reported>();
    if (null != reportedDTOList) {
      for (ReportedDTO reportedDTO : reportedDTOList) {
        reportedList.add(reportedDTO.exportEntry());
      }
    }
    fPostEntry.setReportedList(reportedList);
    return fPostEntry;
  }

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public String getFpostId() {
    return fpostId;
  }

  public void setFpostId(String fpostId) {
    this.fpostId = fpostId;
  }

  public String getPersonName() {
    return personName;
  }

  public void setPersonName(String personName) {
    this.personName = personName;
  }

  public String getPostSectionName() {
    return postSectionName;
  }

  public void setPostSectionName(String postSectionName) {
    this.postSectionName = postSectionName;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public int getClassify() {
    return classify;
  }

  public void setClassify(int classify) {
    this.classify = classify;
  }

  public int getDraught() {
    return draught;
  }

  public void setDraught(int draught) {
    this.draught = draught;
  }

  public int getPostSectionCount() {
    return postSectionCount;
  }

  public void setPostSectionCount(int postSectionCount) {
    this.postSectionCount = postSectionCount;
  }

  public int getScanCount() {
    return scanCount;
  }

  public void setScanCount(int scanCount) {
    this.scanCount = scanCount;
  }

  public int getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }

  public String getPostSectionLevelName() {
    return postSectionLevelName;
  }

  public void setPostSectionLevelName(String postSectionLevelName) {
    this.postSectionLevelName = postSectionLevelName;
  }

  public int getCream() {
    return cream;
  }

  public void setCream(int cream) {
    this.cream = cream;
  }

  public String getPostSectionLevelId() {
    return postSectionLevelId;
  }

  public void setPostSectionLevelId(String postSectionLevelId) {
    this.postSectionLevelId = postSectionLevelId;
  }

  public String getPostSectionId() {
    return postSectionId;
  }

  public void setPostSectionId(String postSectionId) {
    this.postSectionId = postSectionId;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getClassifyContent() {
    return classifyContent;
  }

  public void setClassifyContent(String classifyContent) {
    this.classifyContent = classifyContent;
  }

  public int getIsTop() {
    return isTop;
  }

  public void setIsTop(int isTop) {
    this.isTop = isTop;
  }

  public String getImageSrc() {
    return imageSrc;
  }

  public void setImageSrc(String imageSrc) {
    this.imageSrc = imageSrc;
  }

  public String getPlainText() {
    return plainText;
  }

  public void setPlainText(String plainText) {
    this.plainText = plainText;
  }

  public String getTopText() {
    return topText;
  }

  public void setTopText(String topText) {
    this.topText = topText;
  }

  public String getCreamText() {
    return creamText;
  }

  public void setCreamText(String creamText) {
    this.creamText = creamText;
  }

  public String getTimeText() {
    return timeText;
  }

  public void setTimeText(String timeText) {
    this.timeText = timeText;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public int getLogReply() {
    return logReply;
  }

  public void setLogReply(int logReply) {
    this.logReply = logReply;
  }

  public String getActivityMemo() {
    return activityMemo;
  }

  public void setActivityMemo(String activityMemo) {
    this.activityMemo = activityMemo;
  }

  public String getActivityTime() {
    return activityTime;
  }

  public void setActivityTime(String activityTime) {
    this.activityTime = activityTime;
  }

  public int getInSet() {
    return inSet;
  }

  public void setInSet(int inSet) {
    this.inSet = inSet;
  }

  public String getActivityImage() {
    return activityImage;
  }

  public void setActivityImage(String activityImage) {
    this.activityImage = activityImage;
  }

  public List<Map<String, String>> getVideoList() {
    return videoList;
  }

  public void setVideoList(List<Map<String, String>> videoList) {
    this.videoList = videoList;
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

  public String getAppComment() {
    return appComment;
  }

  public void setAppComment(String appComment) {
    this.appComment = appComment;
  }

  public String getReplyUserId() {
    return replyUserId;
  }

  public void setReplyUserId(String replyUserId) {
    this.replyUserId = replyUserId;
  }

  public String getReplyUserName() {
    return replyUserName;
  }

  public void setReplyUserName(String replyUserName) {
    this.replyUserName = replyUserName;
  }

  public int getOpposeCount() {
    return opposeCount;
  }

  public void setOpposeCount(int opposeCount) {
    this.opposeCount = opposeCount;
  }

  public long getReplyTime() {
    return replyTime;
  }

  public void setReplyTime(long replyTime) {
    this.replyTime = replyTime;
  }

  public List<String> getOpposeList() {
    return opposeList;
  }

  public void setOpposeList(List<String> opposeList) {
    this.opposeList = opposeList;
  }

  public String getVoteContent() {
    return voteContent;
  }

  public void setVoteContent(String voteContent) {
    this.voteContent = voteContent;
  }

  public String getVoteSelect() {
    return voteSelect;
  }

  public void setVoteSelect(String voteSelect) {
    this.voteSelect = voteSelect;
  }

  public List<String> getVoteOptions() {
    return voteOptions;
  }

  public void setVoteOptions(List<String> voteOptions) {
    this.voteOptions = voteOptions;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public List<ReportedDTO> getReportedDTOList() {
    return reportedDTOList;
  }

  public void setReportedDTOList(List<ReportedDTO> reportedDTOList) {
    this.reportedDTOList = reportedDTOList;
  }

  public String getBackUpComment() {
    return backUpComment;
  }

  public void setBackUpComment(String backUpComment) {
    this.backUpComment = backUpComment;
  }

  public String getReportedTime() {
    return reportedTime;
  }

  public void setReportedTime(String reportedTime) {
    this.reportedTime = reportedTime;
  }

  public String getReportedUserName() {
    return reportedUserName;
  }

  public void setReportedUserName(String reportedUserName) {
    this.reportedUserName = reportedUserName;
  }

  public String getReportedComment() {
    return reportedComment;
  }

  public void setReportedComment(String reportedComment) {
    this.reportedComment = reportedComment;
  }

  public int getReportedExperience() {
    return reportedExperience;
  }

  public void setReportedExperience(int reportedExperience) {
    this.reportedExperience = reportedExperience;
  }

  public int getReported() {
    return reported;
  }

  public void setReported(int reported) {
    this.reported = reported;
  }

  public String getReportedTip() {
    return reportedTip;
  }

  public void setReportedTip(String reportedTip) {
    this.reportedTip = reportedTip;
  }

  public String getActivityStartTime() {
    return activityStartTime;
  }

  public void setActivityStartTime(String activityStartTime) {
    this.activityStartTime = activityStartTime;
  }

  public String getActivityEndTime() {
    return activityEndTime;
  }

  public void setActivityEndTime(String activityEndTime) {
    this.activityEndTime = activityEndTime;
  }

  public int getRemoveStatus() {
    return removeStatus;
  }

  public void setRemoveStatus(int removeStatus) {
    this.removeStatus = removeStatus;
  }

  public String getMerge() {
    return merge;
  }

  public void setMerge(String merge) {
    this.merge = merge;
  }

  public Long getUpdateDateTime() {
    return updateDateTime;
  }

  public void setUpdateDateTime(Long updateDateTime) {
    this.updateDateTime = updateDateTime;
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

  public int getOptionCount() {
    return optionCount;
  }

  public void setOptionCount(int optionCount) {
    this.optionCount = optionCount;
  }

  public int getRewardScore() {
    return rewardScore;
  }

  public void setRewardScore(int rewardScore) {
    this.rewardScore = rewardScore;
  }

  public int getRewardMax() {
    return rewardMax;
  }

  public void setRewardMax(int rewardMax) {
    this.rewardMax = rewardMax;
  }

  public int getRewardCount() {
    return rewardCount;
  }

  public void setRewardCount(int rewardCount) {
    this.rewardCount = rewardCount;
  }

  public Long getOfferedScore() {
    return offeredScore;
  }

  public void setOfferedScore(Long offeredScore) {
    this.offeredScore = offeredScore;
  }

  public int getOfferedCompleted() {
    return offeredCompleted;
  }

  public void setOfferedCompleted(int offeredCompleted) {
    this.offeredCompleted = offeredCompleted;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public int getPostFlag() {
    return postFlag;
  }

  public void setPostFlag(int postFlag) {
    this.postFlag = postFlag;
  }

  public int getCate() {
    return cate;
  }

  public void setCate(int cate) {
    this.cate = cate;
  }

  public String getMainTitle() {
    return mainTitle;
  }

  public void setMainTitle(String mainTitle) {
    this.mainTitle = mainTitle;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPartContent() {
    return partContent;
  }

  public void setPartContent(String partContent) {
    this.partContent = partContent;
  }

  public String getActionHeadImage() {
    return actionHeadImage;
  }

  public void setActionHeadImage(String actionHeadImage) {
    this.actionHeadImage = actionHeadImage;
  }
}
