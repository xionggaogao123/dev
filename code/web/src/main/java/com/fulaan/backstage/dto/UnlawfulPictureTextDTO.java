package com.fulaan.backstage.dto;

import com.pojo.backstage.UnlawfulPictureTextEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/11/18.
 */
public class UnlawfulPictureTextDTO {
    /* createTime          创建时间             ctm
 userId               用户id              uid
 function              功能               fun
 type                1 图片 2 文字       typ
 isCheck               是否通过 (0 未审核 1 通过  2删除)          isc
 isRemove              是否删除           isr
 content               内容               con*/
    private String id;
    private String userId;
    private String contactId;
    private String createTime;
    private int function;
    private int type;
    private int isCheck;
    private String content;
    public UnlawfulPictureTextDTO(){

    }
    public UnlawfulPictureTextDTO(UnlawfulPictureTextEntry e){
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
            this.isCheck = e.getIsCheck();
            this.content = e.getContent();

        }else{
            new UnlawfulPictureTextDTO();
        }
    }

    public UnlawfulPictureTextEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId cId=null;
        if(this.getContactId()!=null&&!"".equals(this.getContactId())){
            cId=new ObjectId(this.getContactId());
        }
        UnlawfulPictureTextEntry openEntry =
                new UnlawfulPictureTextEntry(
                        uId,
                        this.function,
                        cId,
                        this.content,
                        this.type
                );
        return openEntry;

    }
    public UnlawfulPictureTextEntry updateEntry(){
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
        UnlawfulPictureTextEntry openEntry =
                new UnlawfulPictureTextEntry(
                        Id,
                        uId,
                        this.function,
                        cId,
                        this.content,
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

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
