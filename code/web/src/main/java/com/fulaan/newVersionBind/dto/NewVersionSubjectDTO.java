package com.fulaan.newVersionBind.dto;

import com.pojo.newVersionGrade.NewVersionSubjectEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/22.
 */
public class NewVersionSubjectDTO {
    private String id;
    private String userId;
    private List<String> subjectId = new ArrayList<String>();


    public NewVersionSubjectDTO(){

    }
    public NewVersionSubjectDTO(NewVersionSubjectEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            List<ObjectId> uIdList = e.getSubjectList();
            for(ObjectId uId : uIdList){
                subjectId.add(uId.toString());
            }
        }else{
            new NewVersionSubjectDTO();
        }
    }

    public NewVersionSubjectEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String oId : subjectId){
            uIdList.add(new ObjectId(oId));
        }
        NewVersionSubjectEntry openEntry =
                new NewVersionSubjectEntry(
                        uId,
                        uIdList
                );
        return openEntry;

    }
    public NewVersionSubjectEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String oId : subjectId){
            uIdList.add(new ObjectId(oId));
        }
        NewVersionSubjectEntry openEntry =
                new NewVersionSubjectEntry(
                        Id,
                        uId,
                        uIdList
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

    public List<String> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(List<String> subjectId) {
        this.subjectId = subjectId;
    }
}
