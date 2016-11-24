package com.fulaan.registration.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.educationbureau.EducationBureauDao;
import com.db.registration.RegistrationDao;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.registration.FamilyMemberEntry;
import com.pojo.registration.LearningResumeEntry;
/**
 * 学籍管理和成长档案Service
 * 2015-9-8 15:33:25
 * @author cxy
 *
 */
@Service
public class RegistrationService {
	
	private RegistrationDao registrationDao = new RegistrationDao();
	
	private EducationBureauDao educationBureauDao = new EducationBureauDao(); 
	
	/**
	 * 根据ID更新一条User基础信息
	 */
	public void updateBase(ObjectId id,String userName,String studentNumber,int sex,long birthday,String race,
								String addressNow,String addressR,String phone,String email,String health){
		registrationDao.updateBase(id, userName, studentNumber, sex, birthday, race, addressNow, addressR, phone, email, health);
		
	}
	
	/**
	 * 根据ID更新一条User学籍信息
	 */
	public void updateRegistration(ObjectId id,String newRStudentNumber,String newRSchool,String newRContent,long newRDate){
		registrationDao.updateRegistration(id, newRStudentNumber, newRSchool, newRContent, newRDate);
		
	}
	
	/**
	 * 根据一个教育局用户ID获取辖区内的学校
	 */
	public List<ObjectId> getSchoolIdsByEDUUserId(ObjectId userId){
		EducationBureauEntry e = educationBureauDao.selEducationByUserId(userId);
		List<ObjectId> schoolIds = new ArrayList<ObjectId>();
		if(!(e == null || e.getSchoolIds() == null)){ 
			schoolIds = e.getSchoolIds();
		}
		return schoolIds;
	}
	
	//===================================================================================
	/**
     * 添加家庭成员
     * @param e
     * @return
     */
    public ObjectId addFamilyMemberEntry(FamilyMemberEntry e)
    {
        return registrationDao.addFamilyMemberEntry(e);
    }
    /**
     * 根据ID查询一个家庭成员
     * @param id
     * @return
     */
    public FamilyMemberEntry queryFamilyMemberById(ObjectId id){
    	return registrationDao.queryFamilyMemberById(id);
    }
    /**
     * 添加学习简历
     * @param e
     * @return
     */
    public ObjectId addLearningResumeEntry(LearningResumeEntry e)
    {
      return registrationDao.addLearningResumeEntry(e);
    }
    /**
     * 根据ID查询一个学习简历
     * @param id
     * @return
     */
    public LearningResumeEntry queryLearningResumeById(ObjectId id){
    	return registrationDao.queryLearningResumeById(id);
    }
    
    /**
     * 通过userId查询家庭成员
     * @param schoolId
     */
    public List<FamilyMemberEntry> queryFamilyMembersByUserId(ObjectId userId) {
      return registrationDao.queryFamilyMembersByUserId(userId);
    }
    
    /**
     * 通过userId查询学习简历
     * @param schoolId
     */
    public List<LearningResumeEntry> queryLearningResume(ObjectId userId) {
    	return registrationDao.queryLearningResume(userId);
    }
    
    /**
	 * 根据ID更新一条学习简历
	 */
	public void updateLearningResume(ObjectId id,long startDate,long endDate,String entranceType,String studyType,String syudyUnit,String postScript){
		registrationDao.updateLearningResume(id, startDate, endDate, entranceType, studyType, syudyUnit, postScript);
		
	}
	
	/**
	 * 删除一条询家庭成员
	 * @param id
	 */
	public void deleteLearningResume(ObjectId id){
		registrationDao.deleteLearningResume(id);
	}
	
	/**
	 * 根据ID更新一条家庭成员
	 */
	public void updateFamilyMember(ObjectId id,String memberName,String memberRelation,String memberRace,String memberNationality,int memberSex,
			long memberBirthday,String memberEducation,String memberWork,String memberPolitics,String memberHealth,String memberAddressNow,
			String memberAddressRegistration,String memberPhone,String memberEmail){
		registrationDao.updateFamilyMember(id, memberName, memberRelation, memberRace, memberNationality, memberSex, memberBirthday, memberEducation, memberWork, memberPolitics, memberHealth, memberAddressNow, memberAddressRegistration, memberPhone, memberEmail);
		
	}
	
	/**
	 * 删除一条家庭成员
	 * @param id
	 */
	public void deleteFamilyMember(ObjectId id){
		registrationDao.deleteFamilyMember(id);
	}
	
	
}
