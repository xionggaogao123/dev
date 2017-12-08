package com.fulaan.questionbook.dto;

import com.pojo.questionbook.QuestionTagsEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/12/8.
 */
public class QuestionTagsDTO {
    private String id;
    private String name;
    private String userId;
    private String createTime;

    public QuestionTagsDTO(){

    }
    public QuestionTagsDTO(QuestionTagsEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.name = e.getName();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
        }else{
            new QuestionTagsDTO();
        }
    }

    public QuestionTagsEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        QuestionTagsEntry openEntry =
                new QuestionTagsEntry(
                     uId,
                     this.name
                );
        return openEntry;

    }
    public QuestionTagsEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        QuestionTagsEntry openEntry =
                new QuestionTagsEntry(
                        Id,
                        uId,
                        this.name
                );
        return openEntry;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
