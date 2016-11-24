package com.pojo.forum;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/7/18.
 */
public class FLogDTO {

    private String id;
    private long time;
    private String personId;
    private String actionName;
    private String keyId;
    private String path;

    public FLogDTO(){

    }
    //存储举报信息
    public FLogDTO(FLogEntry fLogEntry){
        this.id=fLogEntry.getID().toString();
        this.time=fLogEntry.getTime();
        this.personId=fLogEntry.getPersonId().toString();
        this.actionName=fLogEntry.getActionName();
        this.keyId=fLogEntry.getKeyId().toString();
        this.path=fLogEntry.getPath();
    }

    public FLogEntry exportEntry(){
        FLogEntry fLogEntry=new FLogEntry();
        if(id != null && !id.equals("")){
            fLogEntry.setID(new ObjectId(id));
        }

        fLogEntry.setTime(time);
        if(personId != null && !personId.equals("")){
            fLogEntry.setPersonId(new ObjectId(personId));
        }
        if(keyId != null && !keyId.equals("")){
            fLogEntry.setKeyId(new ObjectId(keyId));
        }
        fLogEntry.setActionName(actionName);
        fLogEntry.setPath(path);
        return fLogEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
