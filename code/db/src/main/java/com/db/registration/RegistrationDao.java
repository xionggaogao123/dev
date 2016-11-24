package com.db.registration;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.educationbureau.EducationBureauDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.registration.FamilyMemberEntry;
import com.pojo.registration.LearningResumeEntry;
import com.sys.constants.Constant;

/**
 * 学籍管理Dao
 * 2015-9-8 15:31:00
 *
 * @author cxy
 */
public class RegistrationDao extends BaseDao {


    /**
     * 根据ID更新一条User基础信息
     */
    public void updateBase(ObjectId id, String userName, String studentNumber, int sex, long birthday, String race,
                           String addressNow, String addressR, String phone, String email, String health) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("nm", userName)
                        .append("sn", studentNumber)
                        .append("sex", sex)
                        .append("bir", birthday)
                        .append("sra", race)
                        .append("add", addressNow)
                        .append("rad", addressR)
                        .append("pn", phone)
                        .append("she", health)
                        .append("e", email));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);

    }

    /**
     * 根据ID更新一条User学籍信息
     */
    public void updateRegistration(ObjectId id, String newRStudentNumber, String newRSchool, String newRContent, long newRDate) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("sn", newRStudentNumber)
                        .append("nsc", newRSchool)
                        .append("cps", newRContent)
                        .append("cdt", newRDate));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_NAME, query, updateValue);

    }

    /**
     * 添加家庭成员
     *
     * @param e
     * @return
     */
    public ObjectId addFamilyMemberEntry(FamilyMemberEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FAMILY_MEMBER, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据ID查询一个家庭成员
     *
     * @param id
     * @return
     */
    public FamilyMemberEntry queryFamilyMemberById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FAMILY_MEMBER, query, Constant.FIELDS);
        if (null != dbo) {
            return new FamilyMemberEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 添加学习简历
     *
     * @param e
     * @return
     */
    public ObjectId addLearningResumeEntry(LearningResumeEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_LEANING_RESUME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据ID查询一个学习简历
     *
     * @param id
     * @return
     */
    public LearningResumeEntry queryLearningResumeById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LEANING_RESUME, query, Constant.FIELDS);
        if (null != dbo) {
            return new LearningResumeEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 通过userId查询家庭成员
     *
     * @param schoolId
     */
    public List<FamilyMemberEntry> queryFamilyMembersByUserId(ObjectId userId) {
        List<FamilyMemberEntry> retList = new ArrayList<FamilyMemberEntry>();
        DBObject query = new BasicDBObject("uid", userId).append("ir", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("eda", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FAMILY_MEMBER, query, Constant.FIELDS, orderBy);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new FamilyMemberEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 通过userId查询学习简历
     *
     * @param schoolId
     */
    public List<LearningResumeEntry> queryLearningResume(ObjectId userId) {
        List<LearningResumeEntry> retList = new ArrayList<LearningResumeEntry>();
        DBObject query = new BasicDBObject("uid", userId).append("ir", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("sd", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LEANING_RESUME, query, Constant.FIELDS, orderBy);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new LearningResumeEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 根据ID更新一条学习简历
     */
    public void updateLearningResume(ObjectId id, long startDate, long endDate, String entranceType, String studyType, String syudyUnit, String postScript) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("sd", startDate)
                        .append("ed", endDate)
                        .append("et", entranceType)
                        .append("st", studyType)
                        .append("su", syudyUnit)
                        .append("ps", postScript));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LEANING_RESUME, query, updateValue);

    }

    /**
     * 删除一条询家庭成员
     *
     * @param id
     */
    public void deleteLearningResume(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LEANING_RESUME, query, updateValue);
    }

    /**
     * 根据ID更新一条家庭成员
     */
    public void updateFamilyMember(ObjectId id, String memberName, String memberRelation, String memberRace, String memberNationality, int memberSex,
                                   long memberBirthday, String memberEducation, String memberWork, String memberPolitics, String memberHealth, String memberAddressNow,
                                   String memberAddressRegistration, String memberPhone, String memberEmail) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
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
                        .append("eda", System.currentTimeMillis()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FAMILY_MEMBER, query, updateValue);

    }

    /**
     * 删除一条家庭成员
     *
     * @param id
     */
    public void deleteFamilyMember(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FAMILY_MEMBER, query, updateValue);
    }
}
