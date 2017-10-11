package com.fulaan.wrongquestion.dto;

import com.pojo.wrongquestion.CreateGradeEntry;
import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/6.
 */
public class CreateGradeDTO {
    @ApiModelProperty(name="id",value = "id", required = false)
    private String id;
    @ApiModelProperty(name="gradeName",value = "八年级", required = true)
    private String gradeName;
    @ApiModelProperty(name="type",value = "15", required = true)
    private int type;
    @ApiModelProperty(name="ename",value = "英文标识", required = true)
    private String ename;
    @ApiModelProperty(name="subjectList",value = "包含学科list", required = true)
    private List<String> subjectList;



    public CreateGradeDTO(){

    }
    public CreateGradeDTO(CreateGradeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.gradeName = e.getGradeName();
            this.type = e.getType();
            this.ename = e.getEname();
            List<ObjectId> uIdList = e.getSubjectList();
            for(ObjectId uId : uIdList){
                subjectList.add(uId.toString());
            }
        }else{
            new SubjectClassDTO();
        }
    }

    public CreateGradeEntry buildAddEntry(){
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String uId : subjectList){
            uIdList.add(new ObjectId(uId));
        }
        CreateGradeEntry openEntry =
                new CreateGradeEntry(
                        this.gradeName,
                        this.type,
                        this.ename,
                        uIdList
                );
        return openEntry;

    }

    public CreateGradeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        List<ObjectId> uIdList = new ArrayList<ObjectId>();
        for(String uId : subjectList){
            uIdList.add(new ObjectId(uId));
        }

        CreateGradeEntry openEntry =
                new CreateGradeEntry(
                        Id,
                        this.gradeName,
                        this.type,
                        this.ename,
                        uIdList
                );
        return openEntry;

    }


    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }
}
