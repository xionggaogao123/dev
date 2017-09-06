package com.fulaan.wrongquestion.dto;

import com.pojo.wrongquestion.SubjectClassEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/6.
 */
public class SubjectClassDTO {
    private String id;
    private String subjectId;
    private String name;


    public SubjectClassDTO(){

    }
    public SubjectClassDTO(SubjectClassEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.subjectId = e.getSubjectId() == null ? "" : e.getSubjectId().toString();
            this.name = e.getName();
        }else{
            new SubjectClassDTO();
        }
    }

    public SubjectClassEntry buildAddEntry(){
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }
        SubjectClassEntry openEntry =
                new SubjectClassEntry(
                        sId,
                        this.name
                );
        return openEntry;

    }

    public SubjectClassEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId sId=null;
        if(this.getSubjectId()!=null&&!"".equals(this.getSubjectId())){
            sId=new ObjectId(this.getSubjectId());
        }
        SubjectClassEntry openEntry =
                new SubjectClassEntry(
                        Id,
                        sId,
                        this.name
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
}
