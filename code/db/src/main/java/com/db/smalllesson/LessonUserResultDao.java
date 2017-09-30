package com.db.smalllesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smalllesson.LessonUserResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/27.
 */
public class LessonUserResultDao extends BaseDao {
    //用户的活跃列表（分页）
    public List<LessonUserResultEntry> getUserResultList(ObjectId lessonId,int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("lid",lessonId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_RESULT, query, Constant.FIELDS,new BasicDBObject("sco",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<LessonUserResultEntry> retList =new ArrayList<LessonUserResultEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new LessonUserResultEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }


    //查询相关用户
    public List<LessonUserResultEntry> getLetterUserResultList(List<ObjectId> userIds,ObjectId lessonId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("lid",lessonId);
        query.append("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_RESULT, query, Constant.FIELDS,new BasicDBObject("sco",Constant.DESC));
        List<LessonUserResultEntry> retList =new ArrayList<LessonUserResultEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new LessonUserResultEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 修改
     */
    public void updEntry(LessonUserResultEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_RESULT, query,updateValue);
    }


    public ObjectId addEntry(LessonUserResultEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_RESULT, e.getBaseEntry());
        return e.getID();
    }

}
