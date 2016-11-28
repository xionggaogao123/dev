package com.fulaan.dto;


import com.pojo.fcommunity.AttachmentEntry;

/**
 * Created by admin on 2016/10/24.
 */
public class AttachmentDTO {

    private String url;
    private String fileName;
    private long time;
    private String userId;

    public AttachmentDTO(AttachmentEntry attachmentEntry) {
        this.url = attachmentEntry.getUrl();
        this.userId = attachmentEntry.getUserId().toString();
        this.fileName = attachmentEntry.getFileName();
        this.time = attachmentEntry.getTime();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
