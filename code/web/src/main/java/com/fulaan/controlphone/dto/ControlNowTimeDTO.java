package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlNowTimeEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/17.
 */
public class ControlNowTimeDTO {

    private String id;
    private String userId;
    private String dataTime;
    private String startTime;
    private String endTime;
    private String communityId;
    public ControlNowTimeDTO(){

    }
    public ControlNowTimeDTO(ControlNowTimeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.communityId = e.getCommunityId() == null ? "" : e.getCommunityId().toString();
            this.dataTime = e.getDataTime();
            this.startTime = e.getStartTime();
            this.endTime = e.getEndTime();
        }else{
            new ControlNowTimeDTO();
        }
    }

    public ControlNowTimeEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ControlNowTimeEntry openEntry =
                new ControlNowTimeEntry(
                        uId,
                        this.dataTime,
                        this.startTime,
                        this.endTime,
                        cId
                );
        return openEntry;

    }
    public ControlNowTimeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ControlNowTimeEntry openEntry =
                new ControlNowTimeEntry(
                        Id,
                        uId,
                        this.dataTime,
                        this.startTime,
                        this.endTime,
                        cId
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

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
