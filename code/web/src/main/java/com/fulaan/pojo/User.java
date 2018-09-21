package com.fulaan.pojo;

/**
 * Created by jerry on 2016/11/5.
 */
public class User {

    private String id;
    private String userName;
    private String nickName;
    private String userId;
    private String avator;
    private int sex;
    private String time;
    private int role; //0:家长,1:学生
    private String mobileNumber;

    public User(){}

    public User(String userName,String nickName,String userId,String avator,int sex,String time ,String mobileNumber){
        this.userName=userName;
        this.nickName=nickName;
        this.userId=userId;
        this.avator=avator;
        this.sex=sex;
        this.time=time;
        this.mobileNumber=mobileNumber;
    }
    
    

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }


    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userId='" + userId + '\'' +
                ", avator='" + avator + '\'' +
                '}';
    }
}
