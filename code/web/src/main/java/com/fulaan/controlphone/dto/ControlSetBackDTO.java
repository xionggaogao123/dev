package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlSetBackEntry;
import org.bson.types.ObjectId;

/**
 * 全局默认配置表
 * Created by James on 2017/11/20.
 *
 */
public class ControlSetBackDTO {
    private String id;
    private long backTime;
    private long appTime;
    private int type;
    public ControlSetBackDTO(){

    }
    public ControlSetBackDTO(ControlSetBackEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.backTime = e.getBacktime();
            this.appTime = e.getAppTime();
            this.type = e.getType();
        }else{
            new ControlSetBackDTO();
        }
    }

    public ControlSetBackEntry buildAddEntry(){
        ControlSetBackEntry openEntry =
                new ControlSetBackEntry(
                        this.backTime,
                        this.appTime,
                        this.type
                );
        return openEntry;

    }
    public ControlSetBackEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ControlSetBackEntry openEntry =
                new ControlSetBackEntry(
                        Id,
                        this.backTime,
                        this.appTime,
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

    public long getBackTime() {
        return backTime;
    }

    public void setBackTime(long backTime) {
        this.backTime = backTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getAppTime() {
        return appTime;
    }

    public void setAppTime(long appTime) {
        this.appTime = appTime;
    }
}
