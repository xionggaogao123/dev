package com.db.questionbook;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/9/30.
 */
public class QuestionAdditionDao extends BaseDao {
     public ObjectId addEntry(QuestionAdditionEntry entry){
         save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTION_ADDITION, entry.getBaseEntry());
         return entry.getID();
     }
}
