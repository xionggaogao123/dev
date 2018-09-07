package com.fulaan.excellentCourses.dto;

import com.pojo.excellentCourses.BackOrderEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-08-28.
 */
public class BackOrderDTO {

    private String id;
    private String userId;
    private String userName;
    private String jiaId;
    private int count;
    private String deleteId;
    private String contactId;
    private List<String> classIdList = new ArrayList<String>();
    private List<String> orderIdList = new ArrayList<String>();
    private double price;
    private String orderId;
    private int orderType;
    private String createTime;
    private int status;
    private int role;
    private int money;
    private String newPrice;

    public BackOrderDTO(){

    }

    public BackOrderDTO(BackOrderEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.deleteId = e.getDeleteId()==null?"":e.getDeleteId().toString();
            this.contactId = e.getContactId()==null?"":e.getContactId().toString();
            this.price = e.getPrice();
            this.orderId = e.getOrderId();
            this.orderType = e.getOrderType();
            this.status = e.getStatus();
            this.role = e.getRole();
            this.money = e.getMoney();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            List<ObjectId> cIdList =e.getClassIdList();
            for (ObjectId uId : cIdList) {
                classIdList.add(uId.toString());
            }

            List<ObjectId> oIdList = e.getOrderIdList();
            for (ObjectId uId : oIdList) {
                orderIdList.add(uId.toString());
            }

        }else{
         new HourClassDTO();
        }
    }

    public BackOrderEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId dId=null;
        if(this.getDeleteId()!=null&&!"".equals(this.getDeleteId())){
            dId=new ObjectId(this.getDeleteId());
        }

        ObjectId cId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            cId=new ObjectId(this.getContactId());
        }
        long cTm = 0l;
        if(this.getCreateTime() != null && this.getCreateTime() != ""){
            cTm = DateTimeUtils.getStrToLongTime(this.getCreateTime());
        }

        List<ObjectId> cIdList = new ArrayList<ObjectId>();
        for(String sId : this.classIdList){
            cIdList.add(new ObjectId(sId));
        }

        List<ObjectId> oIdList = new ArrayList<ObjectId>();
        for(String sId : this.orderIdList){
            oIdList.add(new ObjectId(sId));
        }
        BackOrderEntry openEntry =
                new BackOrderEntry(
                       cId,
                        uId,
                        dId,
                        cIdList,
                        oIdList,
                        this.price,
                        this.orderId,
                        this.orderType,
                        this.status,
                        this.role,
                        this.money
                );
        return openEntry;

    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJiaId() {
        return jiaId;
    }

    public void setJiaId(String jiaId) {
        this.jiaId = jiaId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public List<String> getClassIdList() {
        return classIdList;
    }

    public void setClassIdList(List<String> classIdList) {
        this.classIdList = classIdList;
    }

    public List<String> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
