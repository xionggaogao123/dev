package com.pojo.forum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/8/4.
 */
public class FAppCollectPostDTO {

    //收藏Id
    private String collectedId;
    //帖子的
    //帖子Id
    private String fpostId;
    //发帖时间
    private String postTime;
    //视频
    private List<Map<String,String>> videoList = new ArrayList<Map<String,String>>();
    //图片
    private List<String> imageList = new ArrayList<String>();
    //发帖人昵称
    private String nickName;
    //发帖人头像
    private String userImage;
    //点赞数
    private int    praiseCount;
    //浏览数
    private int    scanCount;
    //评论数
    private int    commentCount;
    //主题
    private String postTitle;
    //时间
    private long   time;
    //内容
    private String plainText;
    //帖子人Id
    private String personId;

    //板块的
    //板块Id
    private String postSectionId;
    //板块名称
    private String name;
    //版主名称
    private String sectionName;
    //板块描述
    private String memo;
    //板块简介
    private String memoName;
    //总浏览数
    private long totalScanCount;
    //总评论数
    private long totalCommentCount;
    //主题数
    private long themeCount;
    //总发帖数
    private long postCount;

    //app端上传小图片
    private String imageAppSrc;
    //app端上传大图片
    private String imageBigAppSrc;

    private int inSet;

    public int getInSet() {
        return inSet;
    }

    public void setInSet(int inSet) {
        this.inSet = inSet;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<Map<String, String>> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Map<String, String>> videoList) {
        this.videoList = videoList;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }



    public String getImageAppSrc() {
        return imageAppSrc;
    }

    public void setImageAppSrc(String imageAppSrc) {
        this.imageAppSrc = imageAppSrc;
    }

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }

    public long getThemeCount() {
        return themeCount;
    }

    public void setThemeCount(long themeCount) {
        this.themeCount = themeCount;
    }

    public long getTotalCommentCount() {
        return totalCommentCount;
    }

    public void setTotalCommentCount(long totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }

    public long getTotalScanCount() {
        return totalScanCount;
    }

    public void setTotalScanCount(long totalScanCount) {
        this.totalScanCount = totalScanCount;
    }

    public String getMemoName() {
        return memoName;
    }

    public void setMemoName(String memoName) {
        this.memoName = memoName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageBigAppSrc() {
        return imageBigAppSrc;
    }

    public void setImageBigAppSrc(String imageBigAppSrc) {
        this.imageBigAppSrc = imageBigAppSrc;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getCollectedId() {
        return collectedId;
    }

    public void setCollectedId(String collectedId) {
        this.collectedId = collectedId;
    }

    public String getPostSectionId() {
        return postSectionId;
    }

    public void setPostSectionId(String postSectionId) {
        this.postSectionId = postSectionId;
    }

    public String getFpostId() {
        return fpostId;
    }

    public void setFpostId(String fpostId) {
        this.fpostId = fpostId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
