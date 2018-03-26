package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlVersionEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/3/22.
 */
public class ControlVersionDTO {
    private String id;

    private String communityId;

    private String userId;

    private String version;

    private long dateTime;

    private int type;

    private int level;

    public ControlVersionDTO(){

    }
    public ControlVersionDTO(ControlVersionEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.communityId = e.getCommunityId() == null ? "" : e.getCommunityId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.version = e.getVersion();
            this.dateTime = e.getDateTime();
            this.type =e.getType();
        }else{
            new ControlVersionDTO();
        }
    }
    public ControlVersionEntry buildAddEntry(){
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        ObjectId aId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            aId=new ObjectId(this.getUserId());
        }
        ControlVersionEntry openEntry =
                new ControlVersionEntry(
                        cId,
                        aId,
                        this.version,
                        this.type
                );
        return openEntry;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
