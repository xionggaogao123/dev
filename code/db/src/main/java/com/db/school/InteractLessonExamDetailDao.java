package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonExamDetailEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2015/11/24.
 */
public class InteractLessonExamDetailDao extends BaseDao {

    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addInteractLessonExamDetailEntry(InteractLessonExamDetailEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_EXAM_DETAIL,e.getBaseEntry());
        return e.getID();
    }

    /**
     * 增加
     *
     * @param list
     * @return
     */
    public void addInteractLessonExamDetailEntryList(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_EXAM_DETAIL, list);
    }

    public List<InteractLessonExamDetailEntry> findExamDetailEntryList(ObjectId lessonId, int type, int times, int number, DBObject fields, String orderBy) {
        List<InteractLessonExamDetailEntry> retList = new ArrayList<InteractLessonExamDetailEntry>();
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("lid", lessonId);
        if(type!=0){
            dbo.append("ty", type);
        }
        if(times!=0){
            dbo.append("ts", times);
        }
        if(number!=0){
            dbo.append("nb", number);
        }
        BasicDBObject sortDBO =new BasicDBObject();
        if(orderBy!=null&&!"".equals(orderBy)){
            if("nb".equals(orderBy)){
                sortDBO.append(orderBy,1);
            }else{
                sortDBO.append(orderBy,-1);
            }
        }else{
            sortDBO.append(Constant.ID,-1);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_EXAM_DETAIL, dbo, fields, sortDBO);
        if (null != list && !list.isEmpty()) {
            InteractLessonExamDetailEntry e = null;
            for (DBObject dbo1 : list) {
                e = new InteractLessonExamDetailEntry((BasicDBObject) dbo1);
                retList.add(e);
            }
        }
        return retList;
    }

    public Map<ObjectId, List<InteractLessonExamDetailEntry>> getExamDetailEntryMap(ObjectId lessonId, int type, int times, DBObject fields, String orderBy) {
        Map<ObjectId, List<InteractLessonExamDetailEntry>> retMap =new HashMap<ObjectId, List<InteractLessonExamDetailEntry>>();
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
            if("nb".equals(orderBy)){
                sortDBO.append(orderBy,1);
            }else{
                sortDBO.append(orderBy,-1);
            }
        }else{
            sortDBO.append(Constant.ID,-1);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INTERACTLESSON_EXAM_DETAIL, dbo, fields, sortDBO);
        List<InteractLessonExamDetailEntry> retList=null;
        if(null!=list && !list.isEmpty())
        {
            InteractLessonExamDetailEntry e;
            for(DBObject dbo1:list)
            {
                e=new InteractLessonExamDetailEntry((BasicDBObject)dbo1);
                retList=retMap.get(e.getExamId());
                if(retList==null){
                    retList=new ArrayList<InteractLessonExamDetailEntry>();
                }
                retList.add(e);
                retMap.put(e.getExamId(), retList);
            }
        }
        return retMap;
    }
}
