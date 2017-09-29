package com.db.smalllesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smalllesson.SmallLessonCodeEntry;
import com.sys.constants.Constant;

/**
 * Created by scott on 2017/9/29.
 */
public class SmallLessonCodeDao extends BaseDao{


    public void saveSmallLessonCodeEntry(SmallLessonCodeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON_CODE,entry.getBaseEntry());
    }

    public void removeSmallLessonCodeEntry(String code){
        BasicDBObject query=new BasicDBObject()
                .append("co",code);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON_CODE,query);
    }

    public SmallLessonCodeEntry getCodeEntry(String code){
        BasicDBObject query=new BasicDBObject()
                .append("co",code);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON_CODE,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new SmallLessonCodeEntry(dbObject);
        }else{
            return null;
        }
    }
}
