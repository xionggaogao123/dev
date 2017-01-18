package com.fulaan.dto;

import com.fulaan.util.DateUtils;
import com.pojo.fcommunity.VideoEntry;

/**
 * Created by admin on 2016/10/26.
 */

public class VideoDTO {

    private String videourl;
    private String imageurl;
    private String time;
    private String userId;


    public VideoDTO(VideoEntry videoEntry) {
        this.videourl = videoEntry.getVideoUrl();
        this.userId = videoEntry.getUserId().toString();
        this.imageurl = videoEntry.getImageUrl();
        this.time = DateUtils.timeStampToStr(videoEntry.getTime() / 1000);
    }

    public VideoDTO(){

    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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
