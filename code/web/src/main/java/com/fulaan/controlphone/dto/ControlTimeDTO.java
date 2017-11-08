package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlTimeEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/7.
 */
public class ControlTimeDTO {
    private String id;
    private String userId;
    private String parentId;
    private long time;

    public ControlTimeDTO(){

    }
    public ControlTimeDTO(ControlTimeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.time = e.getTime();
        }else{
            new ControlTimeDTO();
        }
    }

    public ControlTimeEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ControlTimeEntry openEntry =
                new ControlTimeEntry(
                        pId,
                        uId,
                        this.time
                );
        return openEntry;

    }
    public ControlTimeEntry updateEntry(){
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
        ControlTimeEntry openEntry =
                new ControlTimeEntry(
                        Id,
                        pId,
                        uId,
                        this.time
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
