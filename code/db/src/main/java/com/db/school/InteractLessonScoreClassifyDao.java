package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonScoreClassifyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2015/11/24.
 */
public class InteractLessonScoreClassifyDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addInteractLessonScoreClassifyEntry(InteractLessonScoreClassifyEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_SCORE_CLASSIFY,e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询
     * @param lessonId
     * @param times
     * @return
     */
    public InteractLessonScoreClassifyEntry findInteractLessonScoreClassifyEntry(ObjectId lessonId, int times) {
        BasicDBObject query = new BasicDBObject("lid", lessonId);
        query.append("ts",times);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_INTERACTLESSON_SCORE_CLASSIFY, query, Constant.FIELDS);
        if (null != dbo) {
            InteractLessonScoreClassifyEntry e = new InteractLessonScoreClassifyEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }



}
