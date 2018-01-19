package com.fulaan.business.dto;

import com.pojo.business.ModuleNumberEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/1/16.
 */
public class ModuleNumberDTO {
    /* ObjectId id,
            ObjectId userId,
            int moduleType,
            int number*/

    private String id;
    private String userId;
    private int moduleType;
    private String moduleName;
    private int number;


    public ModuleNumberDTO(){

    }
    public ModuleNumberDTO(ModuleNumberEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.moduleType = e.getModuleType();
            this.moduleName = e.getModuleName();
            this.number = e.getNumber();
        }else{
            new ModuleNumberDTO();
        }
    }

    public ModuleNumberEntry buildEntry(){
        ObjectId uId = null;
        if(this.getUserId()!=null && !this.getUserId().equals("")){
            uId = new ObjectId(this.getUserId());
        }
        ModuleNumberEntry entry =
                new ModuleNumberEntry(
                             uId,
                             this.moduleType,
                             this.moduleName,
                             this.number
                );
        return entry;
    }

    public ModuleNumberEntry updateEntry(){
        ObjectId Id = null;
        if(this.getId()!=null && ! this.getId().equals("")){
            Id = new ObjectId(this.getId());
        }
        ObjectId uId = null;
        if(this.getUserId()!= null && !this.getUserId().equals("")){
            uId = new ObjectId(this.getUserId());
        }
        ModuleNumberEntry entry =
                new ModuleNumberEntry(
                    Id,
                    uId,
                    this.moduleType,
                    this.moduleName,
                    this.number
                 );
        return entry;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
