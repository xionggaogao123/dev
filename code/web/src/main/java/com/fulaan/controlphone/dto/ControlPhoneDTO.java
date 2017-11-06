package com.fulaan.controlphone.dto;

import com.pojo.controlphone.ControlPhoneEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/3.
 */
public class ControlPhoneDTO {
    private String id;
    private String parentId;
    private String userId;
    private String name;
    private String phone;
    private int type;
    private String createTime;


    public ControlPhoneDTO(){

    }
    public ControlPhoneDTO(ControlPhoneEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.name = e.getName();
            this.phone = e.getPhone();
            this.type = e.getType();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
        }else{
            new ControlPhoneDTO();
        }
    }

    public ControlPhoneEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        ControlPhoneEntry openEntry =
                new ControlPhoneEntry(
                        pId,
                        uId,
                        this.name,
                        this.phone,
                        this.type
                );
        return openEntry;

    }
    public ControlPhoneEntry updateEntry(){
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
        ControlPhoneEntry openEntry =
                new ControlPhoneEntry(
                        Id,
                        pId,
                        uId,
                        this.name,
                        this.phone,
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
