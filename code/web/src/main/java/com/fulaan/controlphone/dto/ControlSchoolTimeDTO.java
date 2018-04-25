package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlSchoolTimeEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/16.
 */
public class ControlSchoolTimeDTO {
    private String id;
    private String parentId;
    private String userId;
    private int type;
    private String dataTime;
    private String startTime;
    private String endTime;
    private int week;
    private String communityId;

    private String manyClassTime;

    public ControlSchoolTimeDTO(){

    }
    public ControlSchoolTimeDTO(ControlSchoolTimeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.communityId = e.getCommunityId() == null ? "" : e.getCommunityId().toString();
            this.type = e.getType();
            this.dataTime = e.getDataTime();
            this.startTime = e.getStartTime();
            this.endTime = e.getEndTime();
            this.week = e.getWeek();
        }else{
            new ControlSchoolTimeDTO();
        }
    }

    public ControlSchoolTimeDTO(ControlSchoolTimeEntry e,String manyClassTime){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.communityId = e.getCommunityId() == null ? "" : e.getCommunityId().toString();
            this.type = e.getType();
            this.dataTime = e.getDataTime();
            this.startTime = e.getStartTime();
            this.endTime = e.getEndTime();
            this.week = e.getWeek();
            this.manyClassTime = manyClassTime;
        }else{
            new ControlSchoolTimeDTO();
        }
    }

    public ControlSchoolTimeEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ControlSchoolTimeEntry openEntry =
                new ControlSchoolTimeEntry(
                        pId,
                        uId,
                        this.type,
                        this.dataTime,
                        this.startTime,
                        this.endTime,
                        this.week,
                        cId
                );
        return openEntry;

    }
    public ControlSchoolTimeEntry updateEntry(){
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
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ControlSchoolTimeEntry openEntry =
                new ControlSchoolTimeEntry(
                        Id,
                        pId,
                        uId,
                        this.type,
                        this.dataTime,
                        this.startTime,
                        this.endTime,
                        this.week,
                        cId
                );
        return openEntry;

    }

    public String getManyClassTime() {
        return manyClassTime;
    }

    public void setManyClassTime(String manyClassTime) {
        this.manyClassTime = manyClassTime;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
