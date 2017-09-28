package com.fulaan.smalllesson.dto;

import com.pojo.smalllesson.SmallLessonUserCodeEntry;

/**
 * Created by scott on 2017/9/28.
 */
public class SmallLessonUserCodeDTO {

    private String userId;
    private String id;
    private String qrUrl;
    private String code;

    public SmallLessonUserCodeDTO(){

    }

    public SmallLessonUserCodeDTO(SmallLessonUserCodeEntry entry){
        this.id=entry.getID().toString();
        this.userId=entry.getUserId().toString();
        this.qrUrl=entry.getQrUrl();
        this.code=entry.getCode();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
