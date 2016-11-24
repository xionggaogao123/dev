package com.pojo.teachermanage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * 老师简历信息
 * <pre>
 * collectionName:resume
 * </pre>
 * <pre>
 * {
 * uid:
 *  area:学区
 *  nm:姓名
 *  sex:性别
 *  birth:出生日期
 *  bplace:籍贯birthplace
 *  nation:民族nation
 *  cdtype:身份证类型cardtype
 *  cdnum:身份证号cardnum
 *  cdvalid:身份证有效期cardvalid
 *  maritalst:婚姻状况marital status
 *  polils:政治面貌political landscape
 *  residence:户口所在地residence
 *  address:家庭住址address
 *  nowaddress:现住地址Presentaddress
 *  zipcode:邮编zipcode
 *  phone:联系电话phone
 *  contact:联系人contact
 *  cotphone:联系人电话contactphone
 *  email:邮箱email
 *  edu:学历education
 *  major:专业major
 *  htime：取得时间have time
 *  degree：学位degree
 *  dtime:学位取得时间degreetime
 *  jtime工作时间jobtime
 *  stime来校时间schooltime
 *  teatime从教时间teachtime
 *  ts从教学科teachsubject
 *  level  普通话等级mandarin level
 *  code 教师资格证代码 code
 *  intro自我介绍introduce
 *  teanub 职工号 teachernumber
 * }
 * </pre>
 * Created by wang_xinxin on 2016/2/26.
 */
public class ResumeEntry extends BaseDBObject {

    private static final long serialVersionUID = -770716000755469730L;

    public ResumeEntry(BasicDBObject basicDBObject) {
        super(basicDBObject);
    }

    public ResumeEntry(ObjectId userId,ObjectId schoolId,int area,String birth,String birthPlace,int nation,int cardType,String cardNum,String cardValid,
                       int maritalStatus,int politicalLandscape,String residence,String address,String nowAddress,String zipcode,String phone,
                       String contact,String contactPhone,String email,int education,String major,String havetime,int degree,String degreetime,String jobtime,
                       String schooltime,String teachtime,int teachsubject,int level,String code,String introduce) {
        BasicDBObject dbo =new BasicDBObject()
                .append("ui", userId)
                .append("si", schoolId)
                .append("birth",birth)
                .append("area", area)
                .append("bplace",birthPlace)
                .append("nation", nation)
                .append("cdtype", cardType)
                .append("cdnum", cardNum)
                .append("cdvalid", cardValid)
                .append("maritalst", maritalStatus)
                .append("polils", politicalLandscape)
                .append("residence", residence)
                .append("address", address)
                .append("nowaddress", nowAddress)
                .append("zipcode", zipcode)
                .append("phone", phone)
                .append("contact", contact)
                .append("cotphone", contactPhone)
                .append("email", email)
                .append("edu", education)
                .append("major", major)
                .append("htime", havetime)
                .append("degree", degree)
                .append("dtime", degreetime)
                .append("jtime", jobtime)
                .append("stime", schooltime)
                .append("teatime", teachtime)
                .append("ts", teachsubject)
                .append("level", level)
                .append("code", code)
                .append("intro", introduce)
                ;
        setBaseEntry(dbo);
    }
    public ObjectId getSchoolId() {
        return getSimpleObjecIDValue("si");
    }
    public void setSchoolId(ObjectId schoolId) {
        setSimpleValue("si",schoolId);
    }
    public int getCardType() {
        return getSimpleIntegerValue("cdtype");
    }
    public void setCardType(int cardType){
        setSimpleValue("cdtype",cardType);
    }
    public String getCardNum(){
        return getSimpleStringValue("cdnum");
    }
    public void setCardNum(String cardNum) {
        setSimpleValue("cdnum",cardNum);
    }

    public String getCardValid() {
        return getSimpleStringValue("cdvalid");
    }
    public String getBirth() {
        return getSimpleStringValue("birth");
    }
    public void setBirth(String birth) {
        setSimpleValue("birth",birth);
    }
    public void setCardValid(String cardValid) {
        setSimpleValue("cdvalid",cardValid);
    }
    public  int getMaritalStatus() {
        return getSimpleIntegerValue("maritalst");
    }
    public void setMaritalStatus(int maritalStatus) {
        setSimpleValue("maritalst",maritalStatus);
    }
    public int getPoliticalLandscape(){
        return getSimpleIntegerValue("polils");
    }
    public void setPoliticalLandscape(int politicalLandscape) {
        setSimpleValue("polils",politicalLandscape);
    }
    public String getResidence() {
        return getSimpleStringValue("residence");
    }
    public void setResidence(String residence) {
        setSimpleValue("residence",residence);
    }
    public String getAddress() {
        return getSimpleStringValue("address");
    }
    public void setAddress(String address) {
        setSimpleValue("address",address);
    }
    public String getNowAddress() {
        return getSimpleStringValue("nowaddress");
    }
    public void setNowAddress(String nowAddress) {
        setSimpleValue("nowaddress",nowAddress);
    }
    public String getZipcode() {
        return getSimpleStringValue("zipcode");
    }
    public void setZipcode(String zipcode) {
        setSimpleValue("zipcode",zipcode);
    }
    public String getPhone() {
        return getSimpleStringValue("phone");
    }
    public void setPhone(String phone) {
        setSimpleValue("phone",phone);
    }
    public String getContact() {
        return getSimpleStringValue("contact");
    }
    public void setContact(String contact) {
        setSimpleValue("contact",contact);
    }
    public String getContactPhone() {
        return getSimpleStringValue("cotphone");
    }
    public void setContactPhone(String contactPhone) {
        setSimpleValue("cotphone",contactPhone);
    }
    public String getEmail() {
        return getSimpleStringValue("email");
    }
    public void setEmail(String email) {
        setSimpleValue("email",email);
    }
    public int getEducation() {
        return getSimpleIntegerValue("edu");
    }

    public void setEducation(int education) {
        setSimpleValue("edu",education);
    }
    public String getMajor() {
        return getSimpleStringValue("major");
    }
    public void setMajor(String major) {
        setSimpleValue("major",major);
    }
    public String getHavetime() {
        return getSimpleStringValue("htime");
    }
    public void setHavetime(String havetime) {
        setSimpleValue("htime",havetime);
    }
    public int getDegree() {
        return getSimpleIntegerValue("degree");
    }
    public void setDegree(int degree) {
        setSimpleValue("degree",degree);
    }
    public String getDegreetime() {
        return getSimpleStringValue("dtime");
    }
    public void setDegreetime(String degreetime) {
        setSimpleValue("dtime",degreetime);
    }
    public String getJobtime() {
        return getSimpleStringValue("jtime");
    }
    public void setJobtime(String jobtime) {
        setSimpleValue("jtime",jobtime);
    }
    public String getSchooltime() {
        return getSimpleStringValue("stime");
    }
    public void setSchooltime(String schooltime) {
        setSimpleValue("stime",schooltime);
    }
    public String getTeachtime() {
        return getSimpleStringValue("teatime");
    }
    public void setTeachtime(String teachtime) {
        setSimpleValue("teatime",teachtime);
    }
    public int getTeachsubject() {
        return getSimpleIntegerValue("ts");
    }

    public void setTeachsubject(int teachsubject) {
        setSimpleValue("ts",teachsubject);
    }
    public int getlevel() {
        return getSimpleIntegerValue("level");
    }

    public void setlevel(int level) {
        setSimpleValue("level",level);
    }
    public String getCode() {
        return getSimpleStringValue("code");
    }
    public void setCode(String code) {
        setSimpleValue("code",code);
    }
    public String getIntroduce() {
        return getSimpleStringValue("intro");
    }
    public void setIntroduce(String introduce) {
        setSimpleValue("intro",introduce);
    }

   public String getBirthPlace() {
       return getSimpleStringValue("bplace");
   }
    public void setBirthPlace(String birthPlace) {
        setSimpleValue("bplace",birthPlace);
    }
    public ObjectId getUserId() {
        return getSimpleObjecIDValue("ui");
    }
    public void setUserId(ObjectId userId) {
        setSimpleValue("ui",userId);
    }

    public int getArea() {
        return getSimpleIntegerValue("area");
    }

    public void setArea(int area) {
        setSimpleValue("area",area);
    }

    public int getNation() {
        return getSimpleIntegerValue("nation");
    }

    public void setNation(int nation) {
        setSimpleValue("nation",nation);
    }
}
