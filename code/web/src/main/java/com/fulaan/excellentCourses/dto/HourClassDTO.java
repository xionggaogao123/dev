package com.fulaan.excellentCourses.dto;

import com.pojo.excellentCourses.HourClassEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-04-26.
 */
public class HourClassDTO {
    private String id;
    private String userId;
    private String parentId;
    private String content;
    private String startTime;
    private String dateTime;
    private int currentTime;
    private int classOldPrice;
    private int classNewPrice;
    private int week;
    private int order;
    private int type;
    private String createTime;
    private int status;  // 0 已结束   1  未购买   2  已购买


    public HourClassDTO(){

    }

    public HourClassDTO(HourClassEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.parentId = e.getParentId()==null?"":e.getParentId().toString();
            this.content = e.getContent();
            this.currentTime = e.getCurrentTime();
            this.classOldPrice = e.getClassOldPrice();
            this.classNewPrice = e.getClassNewPrice();
            this.order = e.getOrder();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            if(e.getStartTime()!=0l){
                this.startTime = DateTimeUtils.getLongToStrTimeTwo(e.getStartTime()).substring(0,16);
            }else{
                this.startTime = "";
            }
            if(e.getDateTime()!=0l){
                this.dateTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.dateTime = "";
            }
            this.week = e.getWeek();
            this.type = e.getType();

        }else{
            new HourClassDTO();
        }
    }

    public HourClassEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime(), "yyyy-MM-dd");
        }
        long sTm = 0l;
        if(this.getStartTime() != null && this.getStartTime() != ""){
            sTm = DateTimeUtils.getStrToLongTime(this.getStartTime());
        }
        HourClassEntry openEntry =
                new HourClassEntry(
                        uId,
                        pId,
                        this.content,
                        sTm,
                        dTm,
                        this.currentTime,
                        this.classOldPrice,
                        this.classNewPrice,
                        this.week,
                        this.order,
                        this.type
                );
        return openEntry;

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getClassOldPrice() {
        return classOldPrice;
    }

    public void setClassOldPrice(int classOldPrice) {
        this.classOldPrice = classOldPrice;
    }

    public int getClassNewPrice() {
        return classNewPrice;
    }

    public void setClassNewPrice(int classNewPrice) {
        this.classNewPrice = classNewPrice;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
