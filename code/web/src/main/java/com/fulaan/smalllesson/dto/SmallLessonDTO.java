package com.fulaan.smalllesson.dto;

import com.pojo.smalllesson.SmallLessonEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/27.
 */
public class SmallLessonDTO {
    private String id;
    private String name;
    private String dateTime;
    private String userId;
    private int type;
    private int nodeTime;

    public SmallLessonDTO(){

    }
    public SmallLessonDTO(SmallLessonEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.name = e.getName();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            if(e.getDateTime()!=0l){
                this.dateTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.dateTime = "";
            }
            this.type = e.getType();
            this.nodeTime = e.getNodeTime();
        }else{
            new SmallLessonDTO();
        }
    }

    public SmallLessonEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }

        SmallLessonEntry openEntry =
                new SmallLessonEntry(
                        this.name,
                        uId,
                        this.type,
                        this.nodeTime
                );
        return openEntry;

    }
    public SmallLessonEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }

        SmallLessonEntry openEntry =
                new SmallLessonEntry(
                        Id,
                        this.name,
                        uId,
                        this.type,
                        this.nodeTime
                );
        return openEntry;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNodeTime() {
        return nodeTime;
    }

    public void setNodeTime(int nodeTime) {
        this.nodeTime = nodeTime;
    }
}
