package com.fulaan.dto;

import com.pojo.user.UserEntry;
import com.sys.utils.AvatarUtils;
import org.bson.types.ObjectId;

/**
 * Created by jerry on 2016/9/19.
 * 用户传输 DTO
 */
public class UserDTO {

    private ObjectId id;
    private String userId;
    private String userName;
    private String nickName;
    private int sex;
    private String avator;
    private long birthday;
    private long distance;
    private long login;
    private String phone;
    private String email;
    private String gugc;
    public UserDTO(){

    }

    public UserDTO(UserEntry entry) {
        if (entry == null) {
            return;
        }
        this.userId = entry.getID().toString();
        this.id = entry.getID();
        this.userName = entry.getUserName();
        this.nickName = entry.getNickName();
        this.sex = entry.getSex();
        this.avator = AvatarUtils.getAvatar(entry.getAvatar(), entry.getRole(),entry.getSex());
        this.birthday = entry.getBirthDate();
        this.phone = entry.getMobileNumber();
        this.email = entry.getEmail();
        this.gugc = entry.getGenerateUserCode();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(org.bson.types.ObjectId id) {
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getLogin() {
        return login;
    }

    public void setLogin(long login) {
        this.login = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGugc() {
        return gugc;
    }

    public void setGugc(String gugc) {
        this.gugc = gugc;
    }
    
    
}
