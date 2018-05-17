package com.fulaan.excellentCourses.dto;

import com.pojo.excellentCourses.ClassOrderEntry;
import com.sys.utils.DateTimeUtils;

/**
 * Created by James on 2018-04-26.
 */
public class ClassOrderDTO {
    private String id;
    private String userId;
    private String userName;
    private String parentId;
    private String contactId;
    private int isBuy;
    private int price;
    private int function;
    private int type;
    private String createTime;

    public ClassOrderDTO(){

    }

    public ClassOrderDTO(ClassOrderEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.parentId = e.getParentId()==null?"":e.getParentId().toString();
            this.contactId = e.getContactId()==null?"":e.getContactId().toString();
            this.isBuy = e.getIsBuy();
            this.price = e.getPrice();
            this.function = e.getFunction();
            this.type = e.getType();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
        }else{
            new HourClassDTO();
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(int isBuy) {
        this.isBuy = isBuy;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFunction() {
        return function;
    }

    public void setFunction(int function) {
        this.function = function;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
