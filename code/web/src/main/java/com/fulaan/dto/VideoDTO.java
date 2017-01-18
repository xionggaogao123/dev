package com.fulaan.dto;

import com.fulaan.util.DateUtils;
import com.pojo.fcommunity.VideoEntry;

/**
 * Created by admin on 2016/10/26.
 */

public class VideoDTO {

    private String videoUrl;
    private String imageUrl;
    private String time;
    private String userId;


    public VideoDTO(VideoEntry videoEntry) {
        this.videoUrl = videoEntry.getVideoUrl();
        this.userId = videoEntry.getUserId().toString();
        this.imageUrl = videoEntry.getImageUrl();
        this.time = DateUtils.timeStampToStr(videoEntry.getTime() / 1000);
    }

    public VideoDTO(){

    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
