package com.fulaan.extendedcourse.dto;

import com.pojo.extendedcourse.ExtendedSchoolLabelEntry;

/**
 * Created by James on 2018-12-06.
 */
public class ExtendedSchoolLabelDTO {
    private String id;
    private String schoolId;
    private String userId;
    private String name;

    public ExtendedSchoolLabelDTO(){

    }

    public ExtendedSchoolLabelDTO(ExtendedSchoolLabelEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.name = e.getName();

        }else{
            new ExtendedSchoolLabelDTO();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
