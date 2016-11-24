package com.db.smartcard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smartcard.DoorInfoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/4/20.
 */
public class DoorInfoDao extends BaseDao {

    /**
     * 添加日志信息
     * @param e
     * @return
     */
    public ObjectId addDoorInfoEntry(DoorInfoEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DOOR_INFO, e.getBaseEntry());
        return e.getID();
    }

    public void addDoorInfoEntrys(List<DoorInfoEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DOOR_INFO, dbObjects);
    }



    /**
     * 根据type查询
     * @param cardNo
     * @return
     */
    public DoorInfoEntry getDoorInfoEntry(String cardNo)
    {
        DBObject query =new BasicDBObject("cno",cardNo);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DOOR_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new DoorInfoEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public int searchDoorDataListCount(String name, String selState, String doorNum, long dateStart, long dateEnd) {
        BasicDBObject query =new BasicDBObject();
        if(!"".equals(name)){
            query.append("nm",MongoUtils.buildRegex(name));
        }
        if(!"".equals(selState)){
            query.append("iof", selState);
        }
        if(!"".equals(doorNum)){
            query.append("drn", doorNum);
        }
        BasicDBList dblist =new BasicDBList();
        if(dateStart>0){
            dblist.add(new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_GTE, dateStart)));
        }
        if(dateEnd>0) {
            dblist.add(new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_LTE, dateEnd)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DOOR_INFO, query);
    }

    public List<DoorInfoEntry> searchDoorDataList(String name, String selState, String doorNum, long dateStart, long dateEnd, int skip, int limit) {
        List<DoorInfoEntry> retList =new ArrayList<DoorInfoEntry>();
        BasicDBObject query =new BasicDBObject();
        if(!"".equals(name)){
            query.append("nm",MongoUtils.buildRegex(name));
        }
        if(!"".equals(selState)){
            query.append("iof", selState);
        }
        if(!"".equals(doorNum)){
            query.append("drn", doorNum);
        }
        BasicDBList dblist =new BasicDBList();
        if(dateStart>0){
            dblist.add(new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_GTE, dateStart)));
        }
        if(dateEnd>0) {
            dblist.add(new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_LTE, dateEnd)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        BasicDBObject sort =new BasicDBObject("cd",Constant.DESC);

        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_DOOR_INFO, query, Constant.FIELDS, sort, skip, limit);

        for(DBObject dbo:list)
        {
            retList.add(new DoorInfoEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /*public void updDoorInfoEntry(DoorInfoEntry e) {
            BasicDBObject query = new BasicDBObject();
            query.append(Constant.ID, e.getID());
            BasicDBObject updateValue= new BasicDBObject("ems",e.getEmails()).append("ty",e.getType()).append("udf",e.getUserDef());
            BasicDBObject update=new BasicDBObject(Constant.MONGO_SET,updateValue);
            update(MongoFacroty.getAppDB(),Constant.COLLECTION_DOOR_INFO,query,update);
    }*/
}
