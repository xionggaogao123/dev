package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonQuickAnswerEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/11/26.
 */
public class InteractLessonQuickAnswerDao extends BaseDao {

    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addInteractLessonQuickAnswerEntry(InteractLessonQuickAnswerEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_QUICK_ANSWER,e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询
     * @param lessonId
     * @param type
     * @param times
     * @param fields
     * @param orderBy
     * @return
     */
    public List<InteractLessonQuickAnswerEntry> findQuickAnswerEntryList(ObjectId lessonId, int type, int times, DBObject fields, String orderBy) {
        List<InteractLessonQuickAnswerEntry> retList = new ArrayList<InteractLessonQuickAnswerEntry>();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("lid", lessonId);
        if(type!=0){
            dbo.append("ty", type);
        }
        if(times!=0){
            dbo.append("ts", times);
        }
        BasicDBObject sortDBO =new BasicDBObject();
        if(orderBy!=null&&!"".equals(orderBy)){
            sortDBO.append(orderBy, -1);
        }else{
            sortDBO.append(Constant.ID, 1);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_QUICK_ANSWER, dbo, fields, sortDBO);
        if (null != list && !list.isEmpty()) {
            InteractLessonQuickAnswerEntry e = null;
            for (DBObject dbo1 : list) {
                e = new InteractLessonQuickAnswerEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }

    public List<InteractLessonQuickAnswerEntry> findQuickAnswerEntryList(ObjectId lessonId, int type, int times) {
        return null;
    }
}
