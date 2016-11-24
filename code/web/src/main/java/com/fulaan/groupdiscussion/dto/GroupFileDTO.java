package com.fulaan.groupdiscussion.dto;

import java.util.Date;

/**
 * Created by wang_xinxin on 2015/4/28.
 */
public class GroupFileDTO {
    private String id;

    private String roomid;

    private String fileName;

    private String filePath;

    private String uploadUserid;

    private Date uploadDate;

    private Integer count;

    private Integer delflg;

    private Integer filesize;

    private String username;

    private boolean downloadflag;

    private String downloadpath;

    private String uploadtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadUserid() {
        return uploadUserid;
    }

    public void setUploadUserid(String uploadUserid) {
        this.uploadUserid = uploadUserid;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getDelflg() {
        return delflg;
    }

    public void setDelflg(Integer delflg) {
        this.delflg = delflg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDownloadflag() {
        return downloadflag;
    }

    public void setDownloadflag(boolean downloadflag) {
        this.downloadflag = downloadflag;
    }

    public String getDownloadpath() {
        return downloadpath;
    }

    public void setDownloadpath(String downloadpath) {
        this.downloadpath = downloadpath;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }
}
