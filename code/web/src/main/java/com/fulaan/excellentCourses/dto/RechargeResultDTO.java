package com.fulaan.excellentCourses.dto;

import com.pojo.excellentCourses.RechargeResultEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-05-10.
 */
public class RechargeResultDTO{
    private String id;
    private String userId;
    private String createTime;
    private String description;
    private int way;
    private int type;
    private int money;
    private String sonId;
    private String contactId;
    private List<String> classList = new ArrayList<String>();

    public RechargeResultDTO(){

    }

    public RechargeResultDTO(RechargeResultEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            this.description = e.getDescription();
            this.way = e.getWay();
            this.type = e.getType();
            this.money =e.getMoney();
            this.sonId = e.getSonId()==null?"":e.getSonId().toString();
            this.contactId = e.getContactId()==null?"":e.getContactId().toString();
            if(e.getClassList()!=null){
                for(ObjectId cid: e.getClassList()){
                    this.classList.add(cid.toString());
                }
            }

        }else{
            new RechargeResultDTO();
        }
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSonId() {
        return sonId;
    }

    public void setSonId(String sonId) {
        this.sonId = sonId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }
}
