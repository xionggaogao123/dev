package com.fulaan.wrongquestion.dto;

import com.pojo.resources.ResourceDictionaryEntry;

/**
 * Created by guojing on 2017/3/13.
 */
public class ResourcesDictionaryDTO {

    private String id;
    private int type;
    private String name;
    private String parentId;

    public ResourcesDictionaryDTO(){

    }

    public ResourcesDictionaryDTO(ResourceDictionaryEntry e){
        if(e!=null) {
            this.id = e.getID() == null ? "" : e.getID().toString();
            this.type = e.getType();
            this.name = e.getName();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
        }else{
            new ResourcesDictionaryDTO();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
