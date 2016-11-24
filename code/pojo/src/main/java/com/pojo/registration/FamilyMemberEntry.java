package com.pojo.registration;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * @author cxy
 * 2015-9-12 14:52:02
 * 
 *  家庭成员Entry类
 * collectionName : familymenmber
 *  学生ID  : uid (userId)
 *  成员姓名 : mna (memberName)
 *  成员关系: mre (memberRelation)
 *  民族          : mra (memberRace)
 *  国籍地区: mnat (memberNationality)
 *  性别	   : msex (memberSex)
 *  出生日期: mbd (memberBirthday)
 *  学历 	: med (memberEducation)
 *  从业		: mwo (memberWork)
 *  政治面貌: mpo (memberPolitics)
 *  健康状态: mhe (memberHealth)
 *  现在住址: man (memberAddressNow)
 *  户籍地址: mar (memberAddressRegistration)
 *  联系电话: mph (memberPhone)
 *  电子邮箱: me (memberEmail)
 *  修改日期 : eda (editDate)
 * 	删除标志位：ir 0表示未删除，1表示已删除
 */
public class FamilyMemberEntry extends BaseDBObject{
	
	public FamilyMemberEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public FamilyMemberEntry(ObjectId userId,String memberName,String memberRelation,String memberRace,String memberNationality,int memberSex,
				long memberBirthday,String memberEducation,String memberWork,String memberPolitics,String memberHealth,String memberAddressNow,
				String memberAddressRegistration,String memberPhone,String memberEmail) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("uid", userId)
		.append("mna", memberName)
		.append("mre", memberRelation)
		.append("mra", memberRace)
		.append("mnat", memberNationality)
		.append("msex", memberSex)
		.append("mbd", memberBirthday)
		.append("med", memberEducation)
		.append("mwo", memberWork)
		.append("mpo", memberPolitics)
		.append("mhe", memberHealth)
		.append("man", memberAddressNow)
		.append("mar", memberAddressRegistration)
		.append("mph", memberPhone)
		.append("me", memberEmail)
		.append("eda", System.currentTimeMillis())
		.append("ir", Constant.ZERO);
		
		setBaseEntry(baseEntry);
		
	}
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("uid");
	}
	public void setSchoolId(ObjectId userId) {
		setSimpleValue("uid", userId);
	}
	
	
	public String getMemberName() {
		return getSimpleStringValue("mna");
	}
	public void setMemberName(String memberName) {
		setSimpleValue("mna", memberName);
	}
	
	public String getMemberRelation() {
		return getSimpleStringValue("mre");
	}
	public void setMemberRelation(String memberRelation) {
		setSimpleValue("mre", memberRelation);
	}
	
	public String getMemberRace() {
		return getSimpleStringValue("mra");
	}
	public void setMemberRace(String memberRace) {
		setSimpleValue("mra", memberRace);
	}
	
	public String getMemberNationality() {
		return getSimpleStringValue("mnat");
	}
	public void setMemberNationality(String memberNationality) {
		setSimpleValue("mnat", memberNationality);
	}
	
	public int getMemberSex() {
		return getSimpleIntegerValue("msex");
	}
	public void setMemberSex(String memberSex) {
		setSimpleValue("msex", memberSex);
	}
	
	public long getMemberBirthday() {
		return getSimpleLongValue("mbd"); 
	}
	public void setMemberBirthday(long memberBirthday) {
		setSimpleValue("mbd", memberBirthday);
	}
	
	public String getMemberEducation() {
		return getSimpleStringValue("med");
	}
	public void setMemberEducation(String memberEducation) {
		setSimpleValue("med", memberEducation);
	}
	
	public String getMemberWork() {
		return getSimpleStringValue("mwo");
	}
	public void setMemberWork(String memberWork) {
		setSimpleValue("mwo", memberWork);
	}
	
	public String getMemberPolitics() {
		return getSimpleStringValue("mpo");
	}
	public void setMemberPolitics(String memberPolitics) {
		setSimpleValue("mpo", memberPolitics);
	}
	
	public String getMemberHealth() {
		return getSimpleStringValue("mhe");
	}
	public void setMemberHealth(String memberHealth) {
		setSimpleValue("mhe", memberHealth);
	}
	
	public String getMemberAddressNow() {
		return getSimpleStringValue("man");
	}
	public void setMemberAddressNow(String memberAddressNow) {
		setSimpleValue("man", memberAddressNow);
	}
	
	public String getMemberAddressRegistration() {
		return getSimpleStringValue("mar");
	}
	public void setMemberAddressRegistration(String memberAddressRegistration) {
		setSimpleValue("mar", memberAddressRegistration);
	}
	
	public String getMemberPhone() {
		return getSimpleStringValue("mph");
	}
	public void setMemberPhone(String memberPhone) {
		setSimpleValue("mph", memberPhone);
	}
	
	public String getMemberEmail() {
		return getSimpleStringValue("me");
	}
	public void setMemberEmail(String memberEmail) {
		setSimpleValue("me", memberEmail);
	}
}
