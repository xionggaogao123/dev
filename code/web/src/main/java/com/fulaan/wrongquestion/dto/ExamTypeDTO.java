package com.fulaan.wrongquestion.dto;

import com.pojo.reportCard.ExamTypeEntry;

/**
 * Created by James on 2017/10/13.
 */
public class ExamTypeDTO {
    private String id;
    private String examTypeName;


    public ExamTypeDTO(){

    }
    public ExamTypeDTO(ExamTypeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.examTypeName = e.getExamTypeName();
        }else{
            new ExamTypeDTO();
        }
    }

    public ExamTypeEntry buildAddEntry(){
        ExamTypeEntry openEntry =
                new ExamTypeEntry(
                        this.examTypeName
                );
        return openEntry;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamTypeName() {
        return examTypeName;
    }

    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
    }
}
