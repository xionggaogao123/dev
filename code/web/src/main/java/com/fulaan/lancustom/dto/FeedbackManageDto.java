package com.fulaan.lancustom.dto;

import com.pojo.operation.AppOperationEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: taotao.chan
 * @Date: 2018/11/21 11:05
 * @Description:
 */
public class FeedbackManageDto {
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
    private List<FeedbackManageDto> alist = new ArrayList<FeedbackManageDto>();
    //学生作业
    private int read;
    private int readType;
    private int status;
    private String scrawlUrl;


    private String jiaId;
    //用户角色
    private String userRoleName;


    public FeedbackManageDto(){

    }
    public FeedbackManageDto(AppOperationEntry e){
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
            this.read = e.getRead();
            this.readType =e.getReadType();
            this.status = e.getStatus();
            if(e.getScrawlUrl()!=null){
                this.scrawlUrl = e.getScrawlUrl();
            }else{
                this.scrawlUrl = "";
            }
        }else{
            new FeedbackManageDto();
        }
    }

    public AppOperationEntry buildAddEntry(){
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
        AppOperationEntry openEntry =
                new AppOperationEntry(
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
    public AppOperationEntry updateEntry(){
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
        AppOperationEntry openEntry =
                new AppOperationEntry(
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

    public List<FeedbackManageDto> getAlist() {
        return alist;
    }

    public void setAlist(List<FeedbackManageDto> alist) {
        this.alist = alist;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getReadType() {
        return readType;
    }

    public void setReadType(int readType) {
        this.readType = readType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getScrawlUrl() {
        return scrawlUrl;
    }

    public void setScrawlUrl(String scrawlUrl) {
        this.scrawlUrl = scrawlUrl;
    }

    public String getJiaId() {
        return jiaId;
    }

    public void setJiaId(String jiaId) {
        this.jiaId = jiaId;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }
}
