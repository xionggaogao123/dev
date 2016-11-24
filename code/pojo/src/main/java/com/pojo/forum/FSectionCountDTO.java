package com.pojo.forum;

/**
 * Created by admin on 2016/5/31.
 */
public class FSectionCountDTO {
    private int themeCount; //主题数
    private int postCount; //贴数
    private int totalScanCount; //总浏览数
    private int totalCommentCount; //总评论数

    public FSectionCountDTO(){}

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getTotalCommentCount() {
        return totalCommentCount;
    }

    public void setTotalCommentCount(int totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }

    public int getTotalScanCount() {
        return totalScanCount;
    }

    public void setTotalScanCount(int totalScanCount) {
        this.totalScanCount = totalScanCount;
    }

    public int getThemeCount() {
        return themeCount;
    }

    public void setThemeCount(int themeCount) {
        this.themeCount = themeCount;
    }
}
