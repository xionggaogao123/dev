package com.fulaan.smartcard.dto;

/**
 * Created by wang_xinxin on 2016/9/8.
 */
public class ClassDTO {

    private String userName;

    private String className;

    private String sex;

    public ClassDTO() {

    }

    public ClassDTO(String name,String className,String sex) {
        this.userName = name;
        this.className = className;
        this.sex = sex;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
