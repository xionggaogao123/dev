package com.pojo.user;

import com.sys.utils.AvatarUtils;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDetailInfoDTO extends UserInfoDTO implements Serializable {

    private static final long serialVersionUID = 4002785167984267964L;
    private String userName;
    private String passWord;
    private String nickName;
    private String imgUrl;
    private String mobileNumber;
    private String phoneNumber;
    private String email;
    private String qq;
    private String weiXin;
    private String weiBo;
    private String address;
    private String postCode;//邮编
    private int bloodType;//血型
    private Date registerTime;
    private String registerIP;
    private String schoolID;
    private Date birthDate;
    private Date lastActiveDate;//最后登陆时间
    private String relationId;//关联id    如果是学生该字段为家长id 如过是家长该字段是学生id
    private String studentNum;//学号
    private String registerNum;//学籍号


    private String jobName;//职务名称
    private String introduction;
    private String jobNumber;
    private int experienceValue;
    private String synchronize;//是否同步


    //extra 属性
    private boolean isFriend;
    private String cityName;
    private String mainClassName;
    private String schoolName;
    private String schoolLogo;
    private int relation;//1，是好友，2，不是好友 添加好友申请已发出 3，不是好友未发出添加申请
    private String petName;
    private String chatid;
    private double balance;
    private int jinyan;
    private List<String> connectIds;

    //个人Id
    private String generateUserCode;

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public UserDetailInfoDTO() {
    }

    public UserDetailInfoDTO(UserEntry userEntry, int type) {
        this.imgUrl = AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex());
        setId(userEntry.getID().toString());
        this.nickName = userEntry.getNickName();
        this.chatid = userEntry.getChatId();
        this.userName = userEntry.getUserName();
        setRole(userEntry.getRole());
    }

    public UserDetailInfoDTO(UserEntry userEntry) {
        this.generateUserCode=userEntry.getGenerateUserCode();
        this.address = userEntry.getAddress();
        this.imgUrl = AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex());
        this.birthDate = new Date(userEntry.getBirthDate());
        this.bloodType = userEntry.getBloodType();
        this.email = userEntry.getEmail();
        setExperienceValue(userEntry.getExperiencevalue());
        setId(userEntry.getID().toString());
        this.introduction = userEntry.getIntroduce();
        this.jobNumber = userEntry.getJobnumber();
        this.lastActiveDate = new Date(userEntry.getLastActiveDate());
        this.mobileNumber = userEntry.getMobileNumber();
        this.nickName = userEntry.getNickName();
        this.passWord = userEntry.getPassword();
        String permission = userEntry.getPermission();
        if (!StringUtils.isBlank(permission))
            setPermission(new Long(userEntry.getPermission()).intValue());
        this.phoneNumber = userEntry.getPhoneNumber();
        this.postCode = userEntry.getPostCode();
        this.qq = userEntry.getQq();
        this.registerIP = userEntry.getRegisterIP();
        this.registerNum = userEntry.getRegisterNum();
        this.registerTime = new Date(userEntry.getRegisterTime());

        try {
            this.relationId = (userEntry.getConnectIds().size() == 0) ? null : userEntry.getConnectIds().get(0).toString();
        } catch (Exception ex) {

        }
        setRole(userEntry.getRole());
        this.schoolID = userEntry.getSchoolID() + "";
        setSex(userEntry.getSex());
        this.studentNum = userEntry.getStudyNum();
        this.userName = userEntry.getUserName();
        this.weiBo = userEntry.getWeiBo();
        this.weiXin = userEntry.getWeiXin();
        this.experienceValue = userEntry.getExperiencevalue();
        if (StringUtils.isBlank(userEntry.getJobnumber())) {
            this.jobNumber = "";
        }
        this.chatid = userEntry.getChatId();
        this.jobName = userEntry.getJob();
        this.jinyan = userEntry.getJinyan();
        List<ObjectId> connectIdList = userEntry.getConnectIds();
        List<String> cids = new ArrayList<String>();
        if (connectIdList != null && connectIdList.size() != 0) {
            for (ObjectId connectId : connectIdList) {
                cids.add(connectId.toString());
            }
        }
        this.connectIds = cids;
    }

    public boolean getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin;
    }

    public String getWeiBo() {
        return weiBo;
    }

    public void setWeiBo(String weiBo) {
        this.weiBo = weiBo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public int getBloodType() {
        return bloodType;
    }

    public void setBloodType(int bloodType) {
        this.bloodType = bloodType;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterIP() {
        return registerIP;
    }

    public void setRegisterIP(String registerIP) {
        this.registerIP = registerIP;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Date lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(String registerNum) {
        this.registerNum = registerNum;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getSynchronize() {
        return synchronize;
    }

    public void setSynchronize(String synchronize) {
        this.synchronize = synchronize;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGenerateUserCode() {
        return generateUserCode;
    }

    public void setGenerateUserCode(String generateUserCode) {
        this.generateUserCode = generateUserCode;
    }

    /*
        * 学校管理中 班级添加学生所用
        *
        * */
    public UserEntry exportEntry() {
        UserEntry userEntry = new UserEntry(userName, passWord, getSex(), null);
        userEntry.setAddress(this.address);
        userEntry.setUserName(this.userName);
        userEntry.setNickName(this.nickName);
        userEntry.setSex(this.getSex());
        userEntry.setRole((int) this.getRole());
        userEntry.setJob(this.jobName);
        userEntry.setStudyNum(this.studentNum);
        if (null != getId() && ObjectId.isValid(getId())) {
            userEntry.setID(new ObjectId(getId()));
        }
        if (null != getRelationId() && ObjectId.isValid(getRelationId())) {

            List<ObjectId> list = new ArrayList<ObjectId>();
            list.add(new ObjectId(this.relationId));
            userEntry.setConnectIds(list);
        }
        if (null != getSchoolID() && ObjectId.isValid(getSchoolID())) {
            userEntry.setSchoolID(new ObjectId(getSchoolID()));
        }
        return userEntry;
    }

    @Override
    public int getExperienceValue() {
        return experienceValue;
    }

    @Override
    public void setExperienceValue(int experienceValue) {
        this.experienceValue = experienceValue;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getJinyan() {
        return jinyan;
    }

    public void setJinyan(int jinyan) {
        this.jinyan = jinyan;
    }

    public List<String> getConnectIds() {
        return connectIds;
    }

    public void setConnectIds(List<String> connectIds) {
        this.connectIds = connectIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailInfoDTO)) return false;

        UserDetailInfoDTO that = (UserDetailInfoDTO) o;

        if (!imgUrl.equals(that.imgUrl)) return false;
        if (!nickName.equals(that.nickName)) return false;
        if (!userName.equals(that.userName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + nickName.hashCode();
        result = 31 * result + imgUrl.hashCode();
        return result;
    }
}
