package com.pojo.forum;

/**
 * Created by admin on 2016/5/31.
 */
public class FSectionCountDTO implements Comparable<FSectionCountDTO>{
    private long themeCount; //主题数
    private long postCount; //贴数
    private long totalScanCount; //总浏览数
    private long totalCommentCount; //总评论数

    private String memo; //论坛版块logo
    private String sectionName; //版主名称
    private String name; //论坛版块名称
    private String memoName; //论坛版块简介
    private String fSectionId;//论坛版块Id
    private String imageAppSrc;//手机首页图片上传路径
    private String imageBigAppSrc;//手机首页图片上传大图路径
    private int sort;

    public FSectionCountDTO(){}

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
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

    public String getMemoName() {
        return memoName;
    }

    public void setMemoName(String memoName) {
        this.memoName = memoName;
    }

    public String getfSectionId() {
        return fSectionId;
    }

    public void setfSectionId(String fSectionId) {
        this.fSectionId = fSectionId;
    }

    public String getImageAppSrc() {
        return imageAppSrc;
    }

    public void setImageAppSrc(String imageAppSrc) {
        this.imageAppSrc = imageAppSrc;
    }

    public String getImageBigAppSrc() {
        return imageBigAppSrc;
    }

    public void setImageBigAppSrc(String imageBigAppSrc) {
        this.imageBigAppSrc = imageBigAppSrc;
    }

    @Override
    public int compareTo(FSectionCountDTO o) {
        return this.sort < o.getSort()?-1:1;
    }
}
