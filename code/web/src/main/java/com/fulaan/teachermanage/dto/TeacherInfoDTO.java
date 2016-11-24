package com.fulaan.teachermanage.dto;

import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/3.
 */
public class TeacherInfoDTO {

    private String id;

    private String name;

    private String username;

    private String role;

    private int userRole;

    private int ismanage;//是否是管理员

    private List<String> infos;

    private String jobNumber;

    private String postionDec;

    private String letter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getInfos() {
        return infos;
    }

    public void setInfos(List<String> infos) {
        this.infos = infos;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getIsmanage() {
        return ismanage;
    }

    public void setIsmanage(int ismanage) {
        this.ismanage = ismanage;
    }

    public String getPostionDec() {
        return postionDec;
    }

    public void setPostionDec(String postionDec) {
        this.postionDec = postionDec;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
