package com.fulaan.questionbook.dto;

import com.pojo.questionbook.QuestionReadEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/1/8.
 */
public class QuestionReadDTO {
    /* ObjectId userId,
            ObjectId parentId,
            int type,
            int unReadNum*/
    private String id;
    private String userId;//用户id
    private String parentId;//老师id或家长id
    private int type;//用户类型（1 孩子  2 学生）
    private int unReadNum;//新增题数
    private int allNum;//总数
    private String userName;//用户名
    private String studyNum;//学号
    private String ImageUrl;//头像
    private QuestionBookDTO dto; //新增的题目

    public QuestionReadDTO(){

    }

    public QuestionReadDTO(QuestionReadEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.type = e.getType();
            this.unReadNum = e.getUnReadNum();
        }else{
            new QuestionReadDTO();
        }
    }

    public QuestionReadEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        QuestionReadEntry openEntry =
                new QuestionReadEntry(
                        uId,
                        pId,
                        this.type,
                        this.unReadNum
                );
        return openEntry;

    }
    public QuestionReadEntry updateEntry(){
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
        QuestionReadEntry openEntry =
                new QuestionReadEntry(
                        Id,
                        uId,
                        pId,
                        this.type,
                        this.unReadNum
                );
        return openEntry;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStudyNum() {
        return studyNum;
    }

    public void setStudyNum(String studyNum) {
        this.studyNum = studyNum;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public QuestionBookDTO getDto() {
        return dto;
    }

    public void setDto(QuestionBookDTO dto) {
        this.dto = dto;
    }
}