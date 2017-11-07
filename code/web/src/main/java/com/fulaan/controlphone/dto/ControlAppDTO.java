package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlAppEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/6.
 */
public class ControlAppDTO {
    private String id;
    private String communityId;
    private String communityName;
    private List<String> appIdList = new ArrayList<String>();
    public ControlAppDTO(){

    }
    public ControlAppDTO(ControlAppEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.communityId = e.getCommunityId() == null ? "" : e.getCommunityId().toString();
            this.communityName = e.getCommunityName();
            List<ObjectId> uIdList = e.getAppIdList();
            for(ObjectId uId : uIdList){
                appIdList.add(uId.toString());
            }

        }else{
            new ControlAppDTO();
        }
    }

    public ControlAppEntry buildAddEntry(){
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String uId : this.appIdList){
            uIdList.add(new ObjectId(uId));
        }
        ControlAppEntry openEntry =
                new ControlAppEntry(
                        cId,
                        this.communityName,
                        uIdList
                );
        return openEntry;

    }
    public ControlAppEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId cId=null;
        if(this.getCommunityId()!=null&&!"".equals(this.getCommunityId())){
            cId=new ObjectId(this.getCommunityId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String uId : this.appIdList){
            uIdList.add(new ObjectId(uId));
        }
        ControlAppEntry openEntry =
                new ControlAppEntry(
                        Id,
                        cId,
                        this.communityName,
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

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public List<String> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<String> appIdList) {
        this.appIdList = appIdList;
    }
}
