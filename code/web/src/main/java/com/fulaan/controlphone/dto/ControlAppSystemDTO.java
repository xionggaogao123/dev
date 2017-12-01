package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlAppSystemEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/30.
 */
public class ControlAppSystemDTO {
    private String id;
    private List<String> appIdList = new ArrayList<String>();
    public ControlAppSystemDTO(){

    }
    public ControlAppSystemDTO(ControlAppSystemEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            List<ObjectId> uIdList = e.getAppIdList();
            for(ObjectId uId : uIdList){
                appIdList.add(uId.toString());
            }

        }else{
            new ControlAppSystemDTO();
        }
    }

    public ControlAppSystemEntry buildAddEntry(){

        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String sId : this.appIdList){
            uIdList.add(new ObjectId(sId));
        }
        ControlAppSystemEntry openEntry =
                new ControlAppSystemEntry(

                        uIdList
                );
        return openEntry;

    }
    public ControlAppSystemEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }

        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String sId : this.appIdList){
            uIdList.add(new ObjectId(sId));
        }
        ControlAppSystemEntry openEntry =
                new ControlAppSystemEntry(
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


    public List<String> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<String> appIdList) {
        this.appIdList = appIdList;
    }
}
