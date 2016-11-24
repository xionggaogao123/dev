package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonExamEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/11/24.
 */
public class InteractLessonExamDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addInteractLessonExamEntry(InteractLessonExamEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_EXAM, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 增加
     *
     * @param list
     * @return
     */
    public void addInteractLessonExamEntryList(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_EXAM, list);
    }

    /**
     * 查询
     *
     * @param lessonId
     * @param type
     * @param times
     * @param fields
     * @param orderBy
     * @return
     */
    public List<InteractLessonExamEntry> findExamEntryList(ObjectId lessonId, int type, int times, DBObject fields, String orderBy) {
        List<InteractLessonExamEntry> retList = new ArrayList<InteractLessonExamEntry>();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("lid", lessonId);
        if (type != 0) {
            dbo.append("ty", type);
        }
        if (times != 0) {
            dbo.append("ts", times);
        }
        BasicDBObject sortDBO = new BasicDBObject();
        if (orderBy != null && !"".equals(orderBy)) {
            if ("cr".equals(orderBy)) {
                sortDBO.append(orderBy, 1);
            } else {
                sortDBO.append(orderBy, -1);
            }
        } else {
            sortDBO.append(Constant.ID, -1);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_EXAM, dbo, fields, sortDBO);
        if (null != list && !list.isEmpty()) {
            InteractLessonExamEntry e = null;
            for (DBObject dbo1 : list) {
                e = new InteractLessonExamEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }
}
