package com.db.moralculture;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.moralculture.MoralCultureScoreEntry;
import com.pojo.moralculture.MoralCultureScoreInfo;
import com.pojo.utils.DeleteState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/7/3.
 */
public class MoralCultureScoreDao extends BaseDao {

    /**
     * 添加德育分数
     *
     * @param e
     * @return
     */
    public ObjectId addMoralCultureScoreEntry(MoralCultureScoreEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 添加德育成绩
     *
     * @param userId
     * @param gradeId
     * @param classId
     * @param semesterId
     * @return
     */
    public void addMoralCultureScoreEntry2(String userId, String gradeId, String classId, String semesterId, MoralCultureScoreInfo mcsi) {
        DBObject query = new BasicDBObject("ui", new ObjectId(userId)).append("gid", new ObjectId(gradeId)).append("cid", new ObjectId(classId)).append("sei", semesterId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("mcsis", mcsi.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, query, updateValue);
    }

    /**
     * 德育单一课程修改
     *
     * @param userId
     * @param gradeId
     * @param classId
     * @param semesterId
     * @param mcsi
     * @return
     */
    public void updMoralCultureScoreEntry(String userId, String gradeId, String classId, String semesterId, MoralCultureScoreInfo mcsi) {
        DBObject query = new BasicDBObject("ui", new ObjectId(userId)).append("gid", new ObjectId(gradeId)).append("cid", new ObjectId(classId)).append("sei", semesterId).append("mcsis.pi", mcsi.getProjectId());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("mcsis.$.ps", mcsi.getProjectScore()).
                        append("mcsis.$.upt", mcsi.getUpdateTime()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, query, updateValue);
    }

    /**
     * 批修改德育成绩
     *
     * @param userId
     * @param gradeId
     * @param classId
     * @param semesterId
     * @return
     */
    public void updMoralCultureScoreEntry(ObjectId userId, ObjectId gradeId, ObjectId classId, String semesterId, List<DBObject> list) {
        DBObject query = new BasicDBObject("ui", userId).append("gid", gradeId).append("cid", classId).append("sei", semesterId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("mcsis", list));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, query, updateValue);
    }

    /**
     * 查询德育成绩
     *
     * @param userId
     * @param gradeId
     * @param classId
     * @param semesterId
     * @return
     */
    public MoralCultureScoreEntry selMoralCultureScoreEntry(String userId, String gradeId, String classId, String semesterId) {
        BasicDBObject query = new BasicDBObject("ui", new ObjectId(userId)).append("gid", new ObjectId(gradeId)).append("cid", new ObjectId(classId)).append("sei", semesterId);
        query.append("st", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, query, Constant.FIELDS);
        if (null != dbo) {
            return new MoralCultureScoreEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 查询某一用户德育成绩
     *
     * @param userId
     * @param gradeId
     * @param classId
     * @param semesterId
     * @param projectId
     */
    public MoralCultureScoreEntry selMoralCultureScoreEntry(String userId, String gradeId, String classId, String semesterId, ObjectId projectId) {
        DBObject query = new BasicDBObject("ui", new ObjectId(userId)).append("gid", new ObjectId(gradeId)).append("cid", new ObjectId(classId)).append("sei", semesterId).append("mcsis.pi", projectId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, query, new BasicDBObject("mcsis.$", 1).append("ui", 1).append("sei", 1));
        if (null != dbo) {
            return new MoralCultureScoreEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 查询某一班德育成绩
     *
     * @param gradeId
     * @param gradeId
     * @param classId
     * @param semesterId
     */
    public List<MoralCultureScoreEntry> selClassMoralCultureScore(String gradeId, String classId, String semesterId) {
        List<MoralCultureScoreEntry> reList = new ArrayList<MoralCultureScoreEntry>();
        BasicDBObject query = new BasicDBObject("gid", new ObjectId(gradeId)).append("cid", new ObjectId(classId)).append("sei", semesterId);
        query.append("st", DeleteState.NORMAL.getState());
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, query, Constant.FIELDS);
        for (DBObject dbo : list) {
            reList.add(new MoralCultureScoreEntry((BasicDBObject) dbo));
        }
        return reList;
    }

    /**
     * 查询某一学校德育成绩
     *
     * @param schoolId
     */
    public List<MoralCultureScoreEntry> selAllSchoolMoralCultureScore(String schoolId) {
        List<MoralCultureScoreEntry> reList = new ArrayList<MoralCultureScoreEntry>();
        BasicDBObject query = new BasicDBObject("si", new ObjectId(schoolId));
        query.append("st", DeleteState.NORMAL.getState());
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MORAL_CULTURE_SCORE, query, Constant.FIELDS);
        for (DBObject dbo : list) {
            reList.add(new MoralCultureScoreEntry((BasicDBObject) dbo));
        }
        return reList;
    }
}
