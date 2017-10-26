package com.fulaan.instantmessage.dto;

import com.pojo.instantmessage.RedDotEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/10/25.
 */
public class RedDotDTO {
    private String id;
    private String userId;
    private long dateTime;
    private int newNumber;
    private int type;


    public RedDotDTO(){

    }
    public RedDotDTO(RedDotEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.dateTime = e.getDateTime();
            this.newNumber = e.getNewNumber();
            this.type = e.getType();
        }else{
            new RedDotDTO();
        }
    }

    public RedDotEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        RedDotEntry openEntry =
                new RedDotEntry(
                        uId,
                        this.dateTime,
                        this.newNumber,
                        this.type
                );
        return openEntry;

    }
    public RedDotEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        RedDotEntry openEntry =
                new RedDotEntry(
                        Id,
                        uId,
                        this.dateTime,
                        this.newNumber,
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getNewNumber() {
        return newNumber;
    }

    public void setNewNumber(int newNumber) {
        this.newNumber = newNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
