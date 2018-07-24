package com.fulaan.systemMessage.dto;

import com.pojo.operation.AppNewOperationEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-13.
 */
public class AppNewOperationDTO {
    private String id;
    private String parentId;
    private String contactId;
    private String userId;
    private String backId;
    private int level;
    private int type;
    private int role;
    private String description;
    private int second;
    private String cover;
    private String fileUrl;
    private String dateTime;
    private String userName;
    private String userUrl;
    private String backName;
    private int  zanCount;
    private int isZan;
    private List<AppNewOperationDTO> alist = new ArrayList<AppNewOperationDTO>();
    private int operation;

    public AppNewOperationDTO(){

    }
    public AppNewOperationDTO(AppNewOperationEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.description = e.getDescription();
            this.fileUrl = e.getFileUrl();
            this.type = e.getType();
            this.role = e.getRole();
            this.level = e.getLevel();
            this.second = e.getSecond();
            this.cover = e.getCover();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.contactId = e.getContactId() == null ? "" : e.getContactId().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.backId = e.getBackId() == null ? "" : e.getBackId().toString();
            if(e.getDateTime()!=0l){
                this.dateTime = DateTimeUtils.getLongToStrTimeTwo(e.getDateTime());
            }else{
                this.dateTime = "";
            }
        }else{
            new AppNewOperationDTO();
        }
    }

    public AppNewOperationEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId cId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            cId=new ObjectId(this.getContactId());
        }
        ObjectId bId=null;
        if(this.getBackId()!=null&&!"".equals(this.getBackId())){
            bId=new ObjectId(this.getBackId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        long dTm = 0l;
        if(this.getDateTime() != null && this.getDateTime() != ""){
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        AppNewOperationEntry openEntry =
                new AppNewOperationEntry(
                        cId,
                        pId,
                        uId,
                        bId,
                        this.level,
                        dTm,
                        this.type,
                        this.role,
                        this.description,
                        this.second,
                        this.cover,
                        this.fileUrl
                );
        return openEntry;

    }
    public AppNewOperationEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId cId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            cId=new ObjectId(this.getContactId());
        }
        ObjectId bId=null;
        if(this.getBackId()!=null&&!"".equals(this.getBackId())){
            bId=new ObjectId(this.getBackId());
        }
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
            dTm = DateTimeUtils.getStrToLongTime(this.getDateTime());
        }
        AppNewOperationEntry openEntry =
                new AppNewOperationEntry(
                        Id,
                        cId,
                        pId,
                        uId,
                        bId,
                        this.level,
                        dTm,
                        this.type,
                        this.role,
                        this.description,
                        this.second,
                        this.cover,
                        this.fileUrl
                );
        return openEntry;

    }


    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getBackId() {
        return backId;
    }

    public void setBackId(String backId) {
        this.backId = backId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBackName() {
        return backName;
    }

    public void setBackName(String backName) {
        this.backName = backName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<AppNewOperationDTO> getAlist() {
        return alist;
    }

    public void setAlist(List<AppNewOperationDTO> alist) {
        this.alist = alist;
    }

    public int getZanCount() {
        return zanCount;
    }

    public void setZanCount(int zanCount) {
        this.zanCount = zanCount;
    }

    public int getIsZan() {
        return isZan;
    }

    public void setIsZan(int isZan) {
        this.isZan = isZan;
    }
    public int getOperation() {
        return operation;
    }
    public void setOperation(int operation) {
        this.operation = operation;
    }
    
    
}
