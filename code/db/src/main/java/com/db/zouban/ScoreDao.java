package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ScoreEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;


/**
 * Created by wangkaidong on 2016/7/19.
 *
 * 学生等级考成绩Dao（分层）
 */
public class ScoreDao extends BaseDao {

    /**
     * 新增/更新学生等级考成绩
     *
     * @param scoreEntry
     * @return
     */
    public ObjectId addOrUpdateScore(ScoreEntry scoreEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_SCORE, scoreEntry.getBaseEntry());
        return scoreEntry.getID();
    }

    /**
     * 查询学生等级考成绩
     *
     * @param term
     * @param gradeId
     * @param subjectId
     * @return
     */
    public ScoreEntry findScore(String term, ObjectId gradeId, ObjectId subjectId) {
        BasicDBObject query = new BasicDBObject("term", term)
                .append("gid", gradeId)
                .append("subid", subjectId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_SCORE, query, Constant.FIELDS);

        return dbObject == null ? null : new ScoreEntry((BasicDBObject)dbObject);
    }

    /**
     * 清空学生等级考成绩
     *
     * @param term
     * @param gradeId
     */
    public void clearStudentScore(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("term", term).append("gid", gradeId);
        /*BasicDBObject update = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("sl", MongoUtils.convert(MongoUtils.fetchDBObjectList(new ArrayList<IdValuePair>()))));*/
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_SCORE, query);
    }

}
