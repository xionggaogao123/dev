package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonFileEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2015/11/23.
 */
public class InteractLessonFileDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addInteractLessonFileEntry(InteractLessonFileEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_FILE,e.getBaseEntry());
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
    public List<InteractLessonFileEntry> findInteractLessonFileEntry(ObjectId lessonId, int type, int times, DBObject fields, String orderBy) {
        List<InteractLessonFileEntry> retList = new ArrayList<InteractLessonFileEntry>();
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
            sortDBO.append(orderBy,-1);
        }else{
            sortDBO.append(Constant.ID,-1);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_FILE, dbo, fields, sortDBO);
        if (null != list && !list.isEmpty()) {
            InteractLessonFileEntry e = null;
            for (DBObject dbo1 : list) {
                e = new InteractLessonFileEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }
    /**
     * 查询
     * @param lessonId
     * @param type
     * @return
     */
    public Map<String, InteractLessonFileEntry> getExamFileEntryMap(ObjectId lessonId, int type) {
        Map<String, InteractLessonFileEntry> retMap =new HashMap<String, InteractLessonFileEntry>();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("lid", lessonId);
        if(type!=0){
            dbo.append("ty", type);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_FILE, dbo, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            InteractLessonFileEntry e;
            for(DBObject dbo1:list)
            {
                e=new InteractLessonFileEntry((BasicDBObject)dbo1);
                retMap.put(e.getFileName(), e);
            }
        }
        return retMap;
    }
}
