package com.db.yunying;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.elect.ElectEntry;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by wang_xinxin on 2016/1/27.
 */
public class YunyingDao extends BaseDao {

    /**得到一个学校问卷的数量
     *
     */
    public int getPlatformQuestionnaireCount()
    {

        DBObject matchQuery = new BasicDBObject("ipf", 1);

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,matchQuery);
    }

    /** 得到某学校相关班级的所有调查
     * @param skip
     * @param limit
     * @return
     * @throws Exception
     */

    public List<QuestionnaireEntry> getPlatformSurveies(int skip,int limit) throws Exception {
        DBObject matchQuery = new BasicDBObject("$match",new BasicDBObject("ipf", 1));
        DBObject sortQuery = new BasicDBObject("$sort",new BasicDBObject("pbt",-1));
        DBObject skipQuery = new BasicDBObject("$skip",skip);
        DBObject limitQuery = new BasicDBObject("$limit",limit);
//        List<DBObject> dboList = (List<DBObject>)aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,
//                    matchQuery, sortQuery,skipQuery,limitQuery).getCommandResult().get("result");
        List<DBObject> dboList = (List<DBObject>)aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,
              matchQuery, sortQuery,skipQuery,limitQuery).results();
        
        List<QuestionnaireEntry> retList = new ArrayList<QuestionnaireEntry>();

        if(null!=dboList && !dboList.isEmpty()) {
            for(DBObject dbo:dboList) {
                retList.add(new QuestionnaireEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /** 得出一个学校的所有投票,排序方式是未结束的都放在前面，结束的放在后面，未结束的都按照发布时间倒序排列
     * @param schoolId 学校的id号
     * @return 对应学校的所有投票选举信息
     */
    public List<ElectEntry> getElectEntryBySchoolId
    (ObjectId schoolId,List<ObjectId> classIdList,int skip,int limit) throws Exception
    {
        BasicDBList list = new BasicDBList();
        list.add( new BasicDBObject("cls", new BasicDBObject("$in",classIdList)));
        list.add(new BasicDBObject("cls", null));

        BasicDBList conlist = new BasicDBList();
        BasicDBObject query = new BasicDBObject();
        BasicDBObject query2 = new BasicDBObject("sid",schoolId)
                .append("$or", list);
        conlist.add(query2);
        conlist.add(new BasicDBObject("mtp",1));
        query.put(Constant.MONGO_OR,conlist);

        DBObject matchQuery = new BasicDBObject("$match",query);



        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);

        DBObject projectQuery = new BasicDBObject("$project",new BasicDBObject("_id",1)
                .append("nm",1)
                .append("desc", 1)
                .append("cls",1)
                .append("sid",1)
                .append("stt",1)
                .append("edt",1)
                .append("pbt",1)
                .append("mdt",1)
                .append("pb",1)
                .append("pare",1)
                .append("stue",1)
                .append("leae",1)
                .append("teae", 1)
                .append("parv",1)
                .append("stuv",1)
                .append("leav",1)
                .append("teav",1)
                .append("bcnt",1)
                .append("cands",1)
                .append("pub",1)
                .append("voting",new BasicDBObject(
                        "$gte", Arrays.asList("$edt", currentDate.getTimeInMillis() - 100000)
                ))
        );

        DBObject sortQuery = new BasicDBObject("$sort",new BasicDBObject("voting",-1)
                .append("pbt",-1)
        );

        DBObject skipQuery = new BasicDBObject("$skip",skip);
        DBObject limitQuery = new BasicDBObject("$limit",limit);


//        List<DBObject> dboList = (List<DBObject>)aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME,
//                matchQuery,projectQuery,sortQuery,skipQuery,limitQuery).getCommandResult().get("result");

        List<DBObject> dboList = (List<DBObject>)aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME,
                matchQuery,projectQuery,sortQuery,skipQuery,limitQuery).results();

        List<ElectEntry> retList =new ArrayList<ElectEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ElectEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**得到一个学校投票的数量
     *
     */
    public int getElectCountbySchoolId(ObjectId schoolId,List<ObjectId> classIdList)
    {

        BasicDBList list = new BasicDBList();
        list.add( new BasicDBObject("cls", new BasicDBObject("$in",classIdList)));
        list.add(new BasicDBObject("cls", null));

        BasicDBList conlist = new BasicDBList();
        BasicDBObject query2 = new BasicDBObject("sid",schoolId)
                .append("$or", list);
        conlist.add(query2);
        conlist.add(new BasicDBObject("mtp",1));
        DBObject matchQuery = new BasicDBObject(Constant.MONGO_OR,conlist);


        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME,matchQuery);
    }
}
