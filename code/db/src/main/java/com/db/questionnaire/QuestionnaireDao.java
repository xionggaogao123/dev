package com.db.questionnaire;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.questionnaire.OldQuestionnaireEntry;
import com.pojo.questionnaire.QuestionnaireEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qinbo on 15/3/13.
 */
public class QuestionnaireDao extends BaseDao{

    /**
     * 增加一个调查
     * @param e 调查entry
     * @return
     */
    public ObjectId add(QuestionnaireEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME, e.getBaseEntry());
        return e.getID();
    }

    /** 根据id查找某个调查问卷
     * @param surveyId 要超找的调查的id
     * @return
     */
    public QuestionnaireEntry findOne(ObjectId surveyId)
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,surveyId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,query,Constant.FIELDS);
        if(null!=dbo)
        {
            return new QuestionnaireEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /** 得到某学校相关班级的所有调查
     * @param schoolId
     * @param classIdList
     * @param skip
     * @param limit
     * @return
     * @throws Exception
     */
	public List<QuestionnaireEntry> getSurveyBySchoolId
            (ObjectId schoolId,List<ObjectId> classIdList,int skip,int limit, ObjectId userId, int role, int onlyMyself) throws Exception {

        DBObject query = new BasicDBObject("$match", buildQuestionQuery(schoolId, classIdList, userId, role, onlyMyself));

        DBObject sortQuery = new BasicDBObject("$sort",new BasicDBObject("pbt",-1));
        DBObject skipQuery = new BasicDBObject("$skip",skip);
        DBObject limitQuery = new BasicDBObject("$limit",limit);

//        List<DBObject> dboList = (List<DBObject>)aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,
//                query, sortQuery,skipQuery,limitQuery).getCommandResult().get("result");
        
      List<DBObject> dboList = (List<DBObject>)aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,
      query, sortQuery,skipQuery,limitQuery).results();

        List<QuestionnaireEntry> retList = new ArrayList<QuestionnaireEntry>();

        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new QuestionnaireEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**得到一个学校问卷的数量
     *
     */
    public int getQuestionnaireCountbySchoolId(ObjectId schoolId,List<ObjectId> classIdList, ObjectId userId, int role, int onlyMyself) {
        BasicDBObject matchQuery = buildQuestionQuery(schoolId, classIdList, userId, role, onlyMyself);

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,matchQuery);
    }

    private BasicDBObject buildQuestionQuery(ObjectId schoolId,List<ObjectId> classIdList, ObjectId userId, int role, int onlyMyself){
        BasicDBList matchMyClass = new BasicDBList();
        matchMyClass.add( new BasicDBObject("cls", new BasicDBObject("$in",classIdList)));
        matchMyClass.add( new BasicDBObject("cls",null));

        BasicDBList matchRole = new BasicDBList();//我发布的或我参与的
        matchRole.add(new BasicDBObject("pb", userId));
        if(1 != onlyMyself){
            DBObject roleQuery = new BasicDBObject("tear", 1);
            if(1 == role){//老师
                roleQuery = new BasicDBObject("tear", 1);
            } else if(2 == role){//学生
                roleQuery = new BasicDBObject("stur", 1);
            } else if(3 == role){//家长
                roleQuery = new BasicDBObject("parr", 1);
            } else if(4 == role){//校领导
                roleQuery = new BasicDBObject("hear", 1);
            }
            matchRole.add(roleQuery);
        }

        DBObject matchQuery= new BasicDBObject("sid",schoolId).append("$or", matchMyClass).append("$or", matchRole);
        BasicDBList myschoolOrPlatform = new BasicDBList();
        myschoolOrPlatform.add(matchQuery);
        myschoolOrPlatform.add(new BasicDBObject("ipf", 1).append("$or", matchRole));
        return new BasicDBObject("$or", myschoolOrPlatform);
    }

    /**
     * 更新调查的多个字段值
     * @param electId 投票的id
     * @param pairs 要更新的键值对
     */
    public void update(ObjectId electId,FieldValuePair... pairs )
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,electId);
        BasicDBObject valueDBO=new BasicDBObject();
        for(FieldValuePair pair:pairs)
        {
            valueDBO.append(pair.getField(), pair.getValue());
        }
        valueDBO.append(Constant.FIELD_SYN, Constant.SYN_YES_NEED);
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,valueDBO);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,query,updateValue);
    }

    /**移除某个调查
     * @param id 要移除投票的id
     */
    public void remove(ObjectId id)
    {
        DBObject query = new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME,query);
    }


    //导数据使用
    public List<OldQuestionnaireEntry> getOldElectEntryList(){
        List<DBObject> dboList = find(MongoFacroty.getAppDB(),"questionnaire_old",new BasicDBObject(), Constant.FIELDS);

        List<OldQuestionnaireEntry> retList =new ArrayList<OldQuestionnaireEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new OldQuestionnaireEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    
    
    
    
    /**
     * 数据统计用
     * @param name
     * @return
     */
    @Deprecated
    public List<QuestionnaireEntry> getByName(String name){
        List<DBObject> dboList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_QUESTIONNAIRE_NAME,new BasicDBObject("nm",MongoUtils.buildRegex(name)), Constant.FIELDS);
        List<QuestionnaireEntry> retList =new ArrayList<QuestionnaireEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new QuestionnaireEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    public void updateIsPublic(ObjectId qId, int isPublic){
        DBObject query = new BasicDBObject(Constant.ID, qId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ip", isPublic));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_QUESTIONNAIRE_NAME, query, updateValue);
    }

}
