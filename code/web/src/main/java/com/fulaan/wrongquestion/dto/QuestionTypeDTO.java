package com.fulaan.wrongquestion.dto;

import com.pojo.wrongquestion.QuestionTypeEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/10/12.
 */
public class QuestionTypeDTO {
    private String id;
    private String subjectId;
    private String name;
    private String sename;



    public QuestionTypeDTO(){

    }
    public QuestionTypeDTO(QuestionTypeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.subjectId = e.getSubjectId() == null ? "" : e.getSubjectId().toString();
            this.name = e.getName();
            this.sename = e.getSename();
        }else{
            new QuestionTypeDTO();
        }
    }

    public QuestionTypeEntry buildAddEntry(){
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }
        QuestionTypeEntry openEntry =
                new QuestionTypeEntry(
                        sId,
                        this.name,
                        this.sename
                );
        return openEntry;

    }

    public QuestionTypeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }
        QuestionTypeEntry openEntry =
                new QuestionTypeEntry(
                        Id,
                        sId,
                        this.name,
                        this.sename
                );
        return openEntry;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSename() {
        return sename;
    }

    public void setSename(String sename) {
        this.sename = sename;
    }
}
