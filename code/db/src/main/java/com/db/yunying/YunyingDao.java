package com.db.yunying;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/1/27.
 */
public class YunyingDao extends BaseDao {

    /**
     * 得到一个学校问卷的数量
     */
    public int getPlatformQuestionnaireCount() {

        DBObject matchQuery = new BasicDBObject("ipf", 1);

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME, matchQuery);
    }

    /**
     * 得到某学校相关班级的所有调查
     *
     * @param skip
     * @param limit
     * @return
     * @throws Exception
     */

    public List<QuestionnaireEntry> getPlatformSurveies(int skip, int limit) throws Exception {
        return null;
    }

}
