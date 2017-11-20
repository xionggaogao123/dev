package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlAppResultEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/9.
 */
public class ControlAppResultDTO {
    /*  ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            ObjectId appId,
            ObjectId appName,
            int useTime,
            long dateTime*/
    private String id;
    private String parentId;
    private String userId;
    private String appId;
    private String logo;
    private String appName;
    private String packageName;
    private int type;
    private long userTime;
    private String dateTime;

    public ControlAppResultDTO(){

    }
    public ControlAppResultDTO(ControlAppResultEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.appId = e.getAppid() == null ? "" : e.getAppid().toString();
            this.appName = e.getAppName();
            this.userTime = e.getUseTime();
            this.packageName = e.getPackageName();
            this.type = e.getType();
            if(e.getDateTime()!=0l){
                this.dateTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.dateTime = "";
            }
        }else{
            new ControlAppResultDTO();
        }
    }

    public ControlAppResultEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ObjectId aId=null;
        if(this.getAppId()!=null&&!"".equals(this.getAppId())){
            aId=new ObjectId(this.getAppId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        ControlAppResultEntry openEntry =
                new ControlAppResultEntry(
                        pId,
                        uId,
                        aId,
                        this.appName,
                        this.packageName,
                        this.type,
                        this.userTime,
                        dTm
                );
        return openEntry;

    }
    public ControlAppResultEntry updateEntry(){
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
        ObjectId aId=null;
        if(this.getAppId()!=null&&!"".equals(this.getAppId())){
            aId=new ObjectId(this.getAppId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        ControlAppResultEntry openEntry =
                new ControlAppResultEntry(
                        Id,
                        pId,
                        uId,
                        aId,
                        this.appName,
                        this.packageName,
                        this.type,
                        this.userTime,
                        dTm
                );
        return openEntry;

    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getUserTime() {
        return userTime;
    }

    public void setUserTime(long userTime) {
        this.userTime = userTime;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }



    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
