package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlAppUserEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/17.
 */
public class ControlAppUserDTO {
    private String id;
    private String parentId;
    private String userId;
    private List<String> appIdList = new ArrayList<String>();
    public ControlAppUserDTO(){

    }
    public ControlAppUserDTO(ControlAppUserEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            List<ObjectId> uIdList = e.getAppIdList();
            for(ObjectId uId : uIdList){
                appIdList.add(uId.toString());
            }

        }else{
            new ControlAppUserDTO();
        }
    }

    public ControlAppUserEntry buildAddEntry(){
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String sId : this.appIdList){
            uIdList.add(new ObjectId(sId));
        }
        ControlAppUserEntry openEntry =
                new ControlAppUserEntry(
                        pId,
                        uId,
                        uIdList
                );
        return openEntry;

    }
    public ControlAppUserEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String sId : this.appIdList){
            uIdList.add(new ObjectId(sId));
        }
        ControlAppUserEntry openEntry =
                new ControlAppUserEntry(
                        pId,
                        uId,
                        uIdList
                );
        return openEntry;

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

    public List<String> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<String> appIdList) {
        this.appIdList = appIdList;
    }

}
