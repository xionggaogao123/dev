package com.db.elect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.pojo.elect.OldElectEntry;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.elect.Candidate;
import com.pojo.elect.ElectEntry;
import com.sys.constants.Constant;

/**
 * Created by qinbo on 15/3/2.
 *  * index:sid_cls
 *       {"sid":1,"cls":1}
 * 投票选举
 * @author qinbo
 */
public class ElectDao extends BaseDao{
    /**
     * 增加一个投票
     * @param e 投票entry
     * @return
     */
    public ObjectId add(ElectEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME, e.getBaseEntry());
        return e.getID();
    }

    /** 根据id查找某个投票
     * @param electId 要超找的投票的id
     * @return
     */
    public ElectEntry findOne(ObjectId electId)
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,electId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME,query,Constant.FIELDS);
        if(null!=dbo)
        {
            return new ElectEntry((BasicDBObject)dbo);
        }
        return null;
    }
    /**
     * 更新投票的多个字段值
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
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME,query,updateValue);
    }


    /**移除某个投票
     * @param id 要移除投票的id
     */
    public void remove(ObjectId id)
    {
        DBObject query = new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME,query);
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
                        "$gte", Arrays.asList("$edt",currentDate.getTimeInMillis()-100000)
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


    /**添加一个候选人到选举,如果cands为null，则先创建一个dblist
     * @param id elect id
     * @param candidate 候选人
     */
    public void addCandidate(ObjectId id,Candidate candidate){
        DBObject query=new BasicDBObject(Constant.ID,id);
        DBObject updateView = new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("cands",candidate.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ELECT_NAME, query, updateView);
    }


    //导数据使用
    public List<OldElectEntry> getOldElectEntryList(){
        List<DBObject> dboList = find(MongoFacroty.getAppDB(),"elects_old",new BasicDBObject(), Constant.FIELDS);

        List<OldElectEntry> retList =new ArrayList<OldElectEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new OldElectEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }




}
