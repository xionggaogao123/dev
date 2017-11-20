package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlSetTimeEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/20.
 */
public class ControlSetTimeDTO {
    private String id;
    private long time;
    private String name;

    public ControlSetTimeDTO(){

    }
    public ControlSetTimeDTO(ControlSetTimeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.time = e.getTime();
            this.name = e.getName();
        }else{
            new ControlSetTimeDTO();
        }
    }

    public ControlSetTimeEntry buildAddEntry(){
        ControlSetTimeEntry openEntry =
                new ControlSetTimeEntry(
                        this.time,
                        this.name
                );
        return openEntry;

    }
    public ControlSetTimeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ControlSetTimeEntry openEntry =
                new ControlSetTimeEntry(
                        Id,
                        this.time,
                        this.name
                );
        return openEntry;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
