package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.ResumeEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/3/4.
 */
public class ResumeDTO {
    private String id;//用户id
    private String name;//登录号
    private String username;//姓名
    private String role;//权限
    private int userrole;//权限
    private int ismanage;//是否是管理员
    private int sex;//性别
    private int area;//学区
    private String birth;//出生日期
    private String place;//籍贯
    private int national;//民族
    private int card;//身份证类型
    private String cardnumber;//身份证件号码
    private String vail;//	身份证件有效期
    private int maritalstatus;//婚姻状况
    private int political;//政治面貌
    private String registerplace;//户口所在地
    private String adress;//家庭住址
    private String nowadress;//现住址
    private String zipcode;//邮政编码
    private String phone;//联系电话
    private String contact;//紧急联系人
    private String contactphone;//联系人电话
    private String email;//电子信箱
    private int education;//初始学历
    private String major;//专业
    private String deutime;//取得时间
    private int degree;//初始学位
    private String degtime;//取得时间
    private String jobtime;//参加工作时间
    private String schooltime;//来校年月
    private String teachtime;//从教年月
    private int teachSubject;//从教学科
    private int mandarinlevel;//普通话等级
    private String teacerticode;//教师资格证代码
    private String teachernumber;//职工号
    private int organization;//编制
    private String introduction;//自我介绍
    private int type;
    private String postiondec;
    private String tab1;
    private String tab2;
    private String tab3;
    private String tab4;
    private String tab5;
    private String tab6;
    private String tab7;
    private String tab8;


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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getNational() {
        return national;
    }

    public void setNational(int national) {
        this.national = national;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getVail() {
        return vail;
    }

    public void setVail(String vail) {
        this.vail = vail;
    }

    public int getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(int maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public int getPolitical() {
        return political;
    }

    public void setPolitical(int political) {
        this.political = political;
    }

    public String getRegisterplace() {
        return registerplace;
    }

    public void setRegisterplace(String registerplace) {
        this.registerplace = registerplace;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNowadress() {
        return nowadress;
    }

    public void setNowadress(String nowadress) {
        this.nowadress = nowadress;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDeutime() {
        return deutime;
    }

    public void setDeutime(String deutime) {
        this.deutime = deutime;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getDegtime() {
        return degtime;
    }

    public void setDegtime(String degtime) {
        this.degtime = degtime;
    }

    public String getJobtime() {
        return jobtime;
    }

    public void setJobtime(String jobtime) {
        this.jobtime = jobtime;
    }

    public String getSchooltime() {
        return schooltime;
    }

    public void setSchooltime(String schooltime) {
        this.schooltime = schooltime;
    }

    public String getTeachtime() {
        return teachtime;
    }

    public void setTeachtime(String teachtime) {
        this.teachtime = teachtime;
    }

    public int getTeachSubject() {
        return teachSubject;
    }

    public void setTeachSubject(int teachSubject) {
        this.teachSubject = teachSubject;
    }

    public int getMandarinlevel() {
        return mandarinlevel;
    }

    public void setMandarinlevel(int mandarinlevel) {
        this.mandarinlevel = mandarinlevel;
    }

    public String getTeacerticode() {
        return teacerticode;
    }

    public void setTeacerticode(String teacerticode) {
        this.teacerticode = teacerticode;
    }

    public int getOrganization() {
        return organization;
    }

    public void setOrganization(int organization) {
        this.organization = organization;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTab1() {
        return tab1;
    }

    public void setTab1(String tab1) {
        this.tab1 = tab1;
    }

    public String getTab2() {
        return tab2;
    }

    public void setTab2(String tab2) {
        this.tab2 = tab2;
    }

    public String getTab3() {
        return tab3;
    }

    public void setTab3(String tab3) {
        this.tab3 = tab3;
    }

    public String getTab4() {
        return tab4;
    }

    public void setTab4(String tab4) {
        this.tab4 = tab4;
    }

    public String getTab5() {
        return tab5;
    }

    public void setTab5(String tab5) {
        this.tab5 = tab5;
    }

    public String getTab6() {
        return tab6;
    }

    public void setTab6(String tab6) {
        this.tab6 = tab6;
    }

    public String getTab7() {
        return tab7;
    }

    public void setTab7(String tab7) {
        this.tab7 = tab7;
    }

    public String getTab8() {
        return tab8;
    }

    public void setTab8(String tab8) {
        this.tab8 = tab8;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserrole() {
        return userrole;
    }

    public void setUserrole(int userrole) {
        this.userrole = userrole;
    }

    public int getIsmanage() {
        return ismanage;
    }

    public void setIsmanage(int ismanage) {
        this.ismanage = ismanage;
    }

    public String getTeachernumber() {
        return teachernumber;
    }

    public void setTeachernumber(String teachernumber) {
        this.teachernumber = teachernumber;
    }

    public String getPostiondec() {
        return postiondec;
    }

    public void setPostiondec(String postiondec) {
        this.postiondec = postiondec;
    }

    public ResumeEntry buildResume(ObjectId userId,String schoolId) {
        return new ResumeEntry(userId,new ObjectId(schoolId),this.area,this.birth,this.place,this.national,this.card,this.cardnumber
        ,this.vail,this.maritalstatus,this.political,this.registerplace,this.adress,this.nowadress,this.zipcode,this.phone,
                this.contact,this.contactphone,this.email,this.education,this.major,this.deutime,this.degree,
                this.degtime,this.jobtime,this.schooltime,this.teachtime,this.teachSubject,this.mandarinlevel,
                this.teacerticode,this.introduction);
    }

    public ResumeDTO() {

    }
    public ResumeDTO(UserEntry userEntry,ResumeEntry resumeEntry) {
        this.id = userEntry.getID().toString();
        this.name = userEntry.getRealUserName();
        this.username = userEntry.getNickName();
        //this.userrole=userEntry.getRole();
        this.sex = userEntry.getSex();
        this.organization = userEntry.getPostion();
        this.teachernumber = userEntry.getJobnumber()==null?"老师":userEntry.getJobnumber();
        this.postiondec = userEntry.getPostionDec()==null? UserRole.getRoleDescription(userEntry.getRole()):userEntry.getPostionDec();
        if (resumeEntry!=null) {
            this.area = resumeEntry.getArea();
            this.birth = resumeEntry.getBirth();
            this.place = resumeEntry.getBirthPlace();
            this.national = resumeEntry.getNation();
            this.card = resumeEntry.getCardType();
            this.cardnumber = resumeEntry.getCardNum();
            this.vail = resumeEntry.getCardValid();
            this.maritalstatus = resumeEntry.getMaritalStatus();
            this.political = resumeEntry.getPoliticalLandscape();
            this.registerplace = resumeEntry.getResidence();
            this.adress = resumeEntry.getAddress();
            this.nowadress = resumeEntry.getNowAddress();
            this.zipcode = resumeEntry.getZipcode();
            this.phone = resumeEntry.getPhone();
            this.contact = resumeEntry.getContact();
            this.contactphone = resumeEntry.getContactPhone();
            this.email = resumeEntry.getEmail();
            this.education = resumeEntry.getEducation();
            this.major = resumeEntry.getMajor();
            this.deutime = resumeEntry.getHavetime();
            this.degree = resumeEntry.getDegree();
            this.degtime = resumeEntry.getDegreetime();
            this.jobtime = resumeEntry.getJobtime();
            this.schooltime = resumeEntry.getSchooltime();
            this.teachtime = resumeEntry.getTeachtime();
            this.teachSubject = resumeEntry.getTeachsubject();
            this.mandarinlevel = resumeEntry.getlevel();
            this.teacerticode = resumeEntry.getCode();
            this.introduction = resumeEntry.getIntroduce();
        }

    }
}
