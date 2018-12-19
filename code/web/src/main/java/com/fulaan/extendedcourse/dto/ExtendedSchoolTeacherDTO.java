package com.fulaan.extendedcourse.dto;

import com.pojo.extendedcourse.ExtendedSchoolTeacherEntry;

/**
 * Created by James on 2018-12-18.
 */
public class ExtendedSchoolTeacherDTO {
    private String id;
    private String schoolId;
    private String userId;
    private String name;
    private String createId;

    public ExtendedSchoolTeacherDTO(){

    }

    public ExtendedSchoolTeacherDTO(ExtendedSchoolTeacherEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.schoolId = e.getSchoolId()==null?"":e.getSchoolId().toString();
            this.userId = e.getUserId()==null?"":e.getUserId().toString();
            this.name = e.getName();
            this.createId =e.getCreateId()==null?"":e.getCreateId().toString();

        }else{
            new ExtendedSchoolTeacherDTO();
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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }
}
