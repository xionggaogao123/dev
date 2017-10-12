package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.wrongquestion.QuestionTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/10/12.
 */
public class QuestionTypeDao extends BaseDao {
    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addQuestionTypeEntry(QuestionTypeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_TYPE, e.getBaseEntry());
        return e.getID();
    }
}
