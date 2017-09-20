package com.fulaan.wrongquestion.dto;

import com.pojo.wrongquestion.CreateGradeEntry;
import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

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



    public CreateGradeDTO(){

    }
    public CreateGradeDTO(CreateGradeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.gradeName = e.getGradeName();
            this.type = e.getType();
            this.ename = e.getEname();
        }else{
            new SubjectClassDTO();
        }
    }

    public CreateGradeEntry buildAddEntry(){
        CreateGradeEntry openEntry =
                new CreateGradeEntry(
                        this.gradeName,
                        this.type,
                        this.ename
                );
        return openEntry;

    }

    public CreateGradeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }

        CreateGradeEntry openEntry =
                new CreateGradeEntry(
                        Id,
                        this.gradeName,
                        this.type,
                        this.ename
                );
        return openEntry;

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
