package com.fulaan.operation.dto;

import com.pojo.operation.AppRecordEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/8/25.
 */
public class AppRecordDTO {
    private String id;
    private String parentId;
    private String userId;
    private String userName;
    private String imageUrl;
    private int isLoad;
    private String dateTime;
    private String createTime;


    public AppRecordDTO(){

    }
    public AppRecordDTO(AppRecordEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.isLoad = e.getIsLoad();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.userName = e.getUserName();
            if(e.getDateTime()!=0l){
                this.dateTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.dateTime = "";
            }
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
        }else{
            new AppRecordDTO();
        }
    }

    public AppRecordEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        long cTm = 0l;
        if(this.getCreateTime() != null && this.getCreateTime() != ""){
            cTm = DateTimeUtils.getStrToLongTime(this.getCreateTime());
        }
        AppRecordEntry openEntry =
                new AppRecordEntry(
                        pId,
                        uId,
                        this.userName,
                        dTm,
                        cTm,
                        this.isLoad
                );
        return openEntry;

    }
    public AppRecordEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        long cTm = 0l;
        if(this.getCreateTime() != null && this.getCreateTime() != ""){
            cTm = DateTimeUtils.getStrToLongTime(this.getCreateTime());
        }
        AppRecordEntry openEntry =
                new AppRecordEntry(
                        Id,
                        pId,
                        uId,
                        this.userName,
                        dTm,
                        cTm,
                        this.isLoad
                );
        return openEntry;

    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIsLoad() {
        return isLoad;
    }

    public void setIsLoad(int isLoad) {
        this.isLoad = isLoad;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
