package com.fulaan.questionbook.dto;

import com.pojo.questionbook.QuestionAdditionEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by James on 2017/9/30.
 */
public class QuestionAdditionDTO {

    private String id;
    private String parentId;
    private String content;
    private List<String> answerList;
    private int answerType;
    private String createTime;
    private int level;

    public QuestionAdditionDTO(){

    }
    public QuestionAdditionDTO(QuestionAdditionEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.answerType = e.getAnswerType();
            this.parentId = e.getParentId() == null ? "" : e.getParentId().toString();
            this.content = e.getContent();
            this.answerList = e.getAnswerList();
            this.level = e.getLevel();
            if(e.getCreateTime()!=0l){
                this.createTime = DateTimeUtils.getLongToStrTimeTwo(e.getCreateTime());
            }else{
                this.createTime = "";
            }
        }else{
            new QuestionAdditionDTO();
        }
    }

    public QuestionAdditionEntry buildAddEntry(){
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        QuestionAdditionEntry openEntry =
                new QuestionAdditionEntry(
                        pId,
                        this.content,
                        this.answerList,
                        this.answerType,
                        this.level
                );
        return openEntry;

    }
    public QuestionAdditionEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId pId=null;
        if(this.getParentId()!=null&&!"".equals(this.getParentId())){
            pId=new ObjectId(this.getParentId());
        }
        long cTm = 0l;
        if(this.getCreateTime() != null && this.getCreateTime() != ""){
            cTm = DateTimeUtils.getStrToLongTime(this.getCreateTime());
        }
        QuestionAdditionEntry openEntry =
                new QuestionAdditionEntry(
                        Id,
                        pId,
                        this.content,
                        cTm,
                        this.answerList,
                        this.answerType,
                        this.level
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
