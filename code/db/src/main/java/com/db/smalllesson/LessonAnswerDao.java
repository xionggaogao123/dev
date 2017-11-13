package com.db.smalllesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smalllesson.LessonAnswerEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/27.
 */
public class LessonAnswerDao extends BaseDao {

    //答题列表（分页）
    public List<LessonAnswerEntry> getUserResultPageList(ObjectId lessonId,int number,int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("lid",lessonId);
        query.append("num",number);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_ANSWER, query, Constant.FIELDS,new BasicDBObject("tim",Constant.ASC),(page - 1) * pageSize, pageSize);
        List<LessonAnswerEntry> retList =new ArrayList<LessonAnswerEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new LessonAnswerEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    public int getNumber(ObjectId lessonId,int number) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("num",number);
        query.append("lid",lessonId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_LESSON_ANSWER,
                        query);
        return count;
    }

    public List<Integer> getIntResultList(ObjectId lessonId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("lid",lessonId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_ANSWER, query, Constant.FIELDS,new BasicDBObject("ist",Constant.DESC));
        List<Integer> retList =new ArrayList<Integer>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new LessonAnswerEntry((BasicDBObject)dbo).getNumber());
            }
        }
        return retList;
    }
    //答题列表
    public List<LessonAnswerEntry> getUserResultList(ObjectId lessonId,int number) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("lid",lessonId);
        query.append("num",number);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_ANSWER, query, Constant.FIELDS,new BasicDBObject("ist",Constant.DESC));
        List<LessonAnswerEntry> retList =new ArrayList<LessonAnswerEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new LessonAnswerEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 批量增加薪资
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_LESSON_ANSWER, list);
    }



}
