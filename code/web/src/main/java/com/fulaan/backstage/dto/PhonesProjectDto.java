package com.fulaan.backstage.dto;

import com.pojo.backstage.PhonesProjectEntry;

/**
 * Created by taotao.chan on 2018年9月20日14:21:43
 */
public class PhonesProjectDto {
    private String id;
    private String projectName;
    private String projectDockPeople;
    private String schoolName;
    private String accessClass;
    private String accessObj;
    private String contactInfo;
    private String address;

    public PhonesProjectDto(PhonesProjectEntry entry){
        if (entry != null){
            this.id = entry.getID().toString();
            this.projectName = entry.getProjectName();
            this.projectDockPeople = entry.getProjectDockPeople();
            this.schoolName = entry.getSchoolName();
            this.accessClass = entry.getAccessClass();
            this.accessObj = entry.getAccessObj();
            this.contactInfo = entry.getContactInfo();
            this.address = entry.getAddress();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDockPeople() {
        return projectDockPeople;
    }

    public void setProjectDockPeople(String projectDockPeople) {
        this.projectDockPeople = projectDockPeople;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAccessClass() {
        return accessClass;
    }

    public void setAccessClass(String accessClass) {
        this.accessClass = accessClass;
    }

    public String getAccessObj() {
        return accessObj;
    }

    public void setAccessObj(String accessObj) {
        this.accessObj = accessObj;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
