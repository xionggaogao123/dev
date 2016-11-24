package com.fulaan_old.video.dto;

import com.pojo.app.IdValuePairDTO;
import com.pojo.video.VideoViewRecordEntry;

/**
 * Created by qinbo on 15/6/3.
 */
public class VideoViewRecordDTO {

    private IdValuePairDTO videoInfo;
    private IdValuePairDTO userInfo;
    private IdValuePairDTO classInfo;
    private IdValuePairDTO voiceInfo;
    private int viewState;
    private int viewCount;
    private long viewTime;

    public VideoViewRecordDTO() {

    }

    public VideoViewRecordDTO(VideoViewRecordEntry videoViewRecordEntry) {
        this.videoInfo = new IdValuePairDTO(videoViewRecordEntry.getVideoInfo());
        this.userInfo = new IdValuePairDTO(videoViewRecordEntry.getUserInfo());
        this.classInfo = new IdValuePairDTO(videoViewRecordEntry.getClassInfo());
        if (videoViewRecordEntry.getVideoInfo() != null) {
            this.voiceInfo = new IdValuePairDTO(videoViewRecordEntry.getVideoInfo());
        }
        this.viewState = videoViewRecordEntry.getState();
        this.viewCount = videoViewRecordEntry.getCount();
        this.viewTime = videoViewRecordEntry.getLastViewTime();
    }

    public IdValuePairDTO getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(IdValuePairDTO videoInfo) {
        this.videoInfo = videoInfo;
    }

    public IdValuePairDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(IdValuePairDTO userInfo) {
        this.userInfo = userInfo;
    }

    public IdValuePairDTO getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(IdValuePairDTO classInfo) {
        this.classInfo = classInfo;
    }

    public IdValuePairDTO getVoiceInfo() {
        return voiceInfo;
    }

    public void setVoiceInfo(IdValuePairDTO voiceInfo) {
        this.voiceInfo = voiceInfo;
    }

    public int getViewState() {
        return viewState;
    }

    public void setViewState(int viewState) {
        this.viewState = viewState;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public long getViewTime() {
        return viewTime;
    }

    public void setViewTime(long viewTime) {
        this.viewTime = viewTime;
    }
}
