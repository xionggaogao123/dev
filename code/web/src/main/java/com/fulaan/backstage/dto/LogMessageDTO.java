package com.fulaan.backstage.dto;

import com.pojo.backstage.LogMessageEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/12/4.
 */
public class LogMessageDTO {
       /* * id               id
     * userId           用户id
     * contactId        联系id
     * createTime       创建时间
     * function         模块名称
     * content          内容*/
    private String id;
    private String userId;
    private String contactId;
    private String createTime;
    private String function;
    private String content;
    private int type;

    public LogMessageDTO(){

    }
    public LogMessageDTO(LogMessageEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.contactId = e.getContactId() == null ? "" : e.getContactId().toString();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
            this.function = e.getFunction();
            this.type = e.getType();
            this.content = e.getContent();

        }else{
            new LogMessageDTO();
        }
    }

    public LogMessageEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId cId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            cId=new ObjectId(this.getContactId());
        }
        LogMessageEntry openEntry =
                new LogMessageEntry(
                        uId,
                        this.function,
                        cId,
                        this.content,
                        this.type
                );
        return openEntry;

    }
    public LogMessageEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId cId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            cId=new ObjectId(this.getContactId());
        }
        LogMessageEntry openEntry =
                new LogMessageEntry(
                        Id,
                        uId,
                        this.function,
                        cId,
                        this.content,
                        this.type
                );
        return openEntry;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
