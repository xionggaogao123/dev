package com.fulaan.wrongquestion.dto;

import com.pojo.newVersionGrade.NewVersionGradeEntry;
import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/6.
 */
public class NewVersionGradeDTO {
    @ApiModelProperty(name="id",value = "id", required = false)
    private String id;
    @ApiModelProperty(name="userId",value = "String", required = true)
    private String userId;
    @ApiModelProperty(name="year",value = "学年", required = false)
    private String year;
    @ApiModelProperty(name="gradeType",value = "1", required = true)
    private int gradeType;


    public NewVersionGradeDTO(){

    }

    public NewVersionGradeDTO(String userId,String year,int gradeType){
        this.userId=userId;
        this.year=year;
        this.gradeType=gradeType;
    }

    public NewVersionGradeDTO(NewVersionGradeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.userId = e.getUserId() == null ? "" : e.getUserId().toString();
            this.year = e.getYear();
            this.gradeType = e.getGradeType();
        }else{
            new NewVersionGradeDTO();
        }
    }

    public NewVersionGradeEntry buildAddEntry(){
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        NewVersionGradeEntry openEntry =
                new NewVersionGradeEntry(
                        uId,
                        this.year,
                        this.gradeType
                );
        return openEntry;

    }

    public NewVersionGradeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        ObjectId uId=null;
        if(this.getUserId()!=null&&!"".equals(this.getUserId())){
            uId=new ObjectId(this.getUserId());
        }
        NewVersionGradeEntry openEntry =
                new NewVersionGradeEntry(
                        Id,
                        uId,
                        this.year,
                        this.gradeType
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getGradeType() {
        return gradeType;
    }

    public void setGradeType(int gradeType) {
        this.gradeType = gradeType;
    }
}
