package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlMapEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/7.
 */
public class ControlMapDTO {
    private String id;
    private String userId;
    private String parentId;
    private String longitude;
    private String latitude;
    private String angle;
    private String distance;
    private long dateTime;
    private int speed;
    private int isSafe;



    public ControlMapDTO(){

    }
    public ControlMapDTO(ControlMapEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.longitude = e.getLongitude();
            this.latitude = e.getLatitude();
            this.angle = e.getAngle();
            this.distance = e.getDistance();
            this.speed = e.getSpeed();
            this.isSafe = e.getIsSafe();
            this.dateTime = e.getDateTime();
        }else{
            new ControlMapDTO();
        }
    }

    public ControlMapEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ControlMapEntry openEntry =
                new ControlMapEntry(
                        pId,
                        uId,
                        this.longitude,
                        this.latitude,
                        this.angle,
                        this.distance,
                        this.speed,
                        this.isSafe
                );
        return openEntry;

    }
    public ControlMapEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ControlMapEntry openEntry =
                new ControlMapEntry(
                        Id,
                        pId,
                        uId,
                        this.longitude,
                        this.latitude,
                        this.angle,
                        this.distance,
                        this.speed,
                        this.isSafe
                );
        return openEntry;

    }


    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getIsSafe() {
        return isSafe;
    }

    public void setIsSafe(int isSafe) {
        this.isSafe = isSafe;
    }
}
