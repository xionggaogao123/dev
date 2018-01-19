package com.fulaan.business.dto;

import com.fulaan.controlphone.dto.ControlAppDTO;
import com.pojo.business.LoginTimeEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/1/16.
 */
public class LoginTimeDTO {

    private String id;
    private String userId;
    private String loginTime;
    private int duration;
    private int phoneType;

    public LoginTimeDTO(){

    }
    public LoginTimeDTO(LoginTimeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            if(e.getLoginTime()!=0l){
                this.loginTime = DateTimeUtils.getLongToStrTimeTwo(e.getLoginTime());
            }else{
                this.loginTime = "";
            }
           this.duration = e.getDuration();
            this.phoneType = e.getPhoneType();

        }else{
            new ControlAppDTO();
        }
    }

    public LoginTimeEntry buildAddEntry(){
        ObjectId aId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            aId=new ObjectId(this.getUserId());
        }
        long dTm = 0l;
        if(this.getLoginTime() != null && this.getLoginTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getLoginTime());
        }
        LoginTimeEntry openEntry =
                new LoginTimeEntry(
                        aId,
                        dTm,
                        this.duration,
                        this.phoneType
                );
        return openEntry;

    }
    public LoginTimeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId aId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            aId=new ObjectId(this.getUserId());
        }
        long dTm = 0l;
        if(this.getLoginTime() != null && this.getLoginTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getLoginTime());
        }
        LoginTimeEntry openEntry =
                new LoginTimeEntry(
                        Id,
                        aId,
                        dTm,
                        this.duration,
                        this.phoneType
                );
        return openEntry;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }
}
