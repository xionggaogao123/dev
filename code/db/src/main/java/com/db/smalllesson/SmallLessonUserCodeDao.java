package com.db.smalllesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smalllesson.SmallLessonUserCodeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/28.
 */
public class SmallLessonUserCodeDao extends BaseDao{

    public void saveSmallLessonUserCodeEntry(SmallLessonUserCodeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON_USER_CODE,entry.getBaseEntry());
    }


    public SmallLessonUserCodeEntry getEntryByUserId(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON_USER_CODE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new SmallLessonUserCodeEntry(dbObject);
        }else{
            return null;
        }
    }

    public SmallLessonUserCodeEntry getEntryByCode(String code){
        BasicDBObject query=new BasicDBObject()
                .append("co",code);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_SMALL_LESSON_USER_CODE,query,Constant.FIELDS);
        if(null!=dbObject){
          return new SmallLessonUserCodeEntry(dbObject);
        }else{
            return null;
        }
    }
}
