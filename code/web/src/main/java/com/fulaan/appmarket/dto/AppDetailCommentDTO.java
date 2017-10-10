package com.fulaan.appmarket.dto;

import com.pojo.appmarket.AppDetailCommentEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailCommentDTO {

    private String id;
    private String appDetailId;
    private String userId;
    private String userName;
    private String timeStr;
    private int star;
    private String content;

    public AppDetailCommentDTO(){

    }

    public AppDetailCommentDTO(AppDetailCommentEntry commentEntry){
        this.id=commentEntry.getID().toString();
        this.appDetailId=commentEntry.getAppDetailId().toString();
        this.userId=commentEntry.getUserId().toString();
        this.star=commentEntry.getStar();
        this.content=commentEntry.getContent();
        this.timeStr= DateTimeUtils.convert(commentEntry.getTime(),DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A);
    }

    public AppDetailCommentEntry buildEntry(){
        ObjectId aId=null;
        if(StringUtils.isNotBlank(appDetailId)&&ObjectId.isValid(appDetailId)){
            aId=new ObjectId(appDetailId);
        }
        ObjectId uId=null;
        if(StringUtils.isNotBlank(userId)&&ObjectId.isValid(userId)){
            uId=new ObjectId(userId);
        }
        return new AppDetailCommentEntry(aId,
               uId, star, content);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppDetailId() {
        return appDetailId;
    }

    public void setAppDetailId(String appDetailId) {
        this.appDetailId = appDetailId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
