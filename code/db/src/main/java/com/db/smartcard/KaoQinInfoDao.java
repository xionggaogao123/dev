package com.db.smartcard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smartcard.KaoQinInfoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/4/20.
 */
public class KaoQinInfoDao extends BaseDao {

    /**
     * 添加日志信息
     * @param e
     * @return
     */
    public ObjectId addKaoQinInfoEntry(KaoQinInfoEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_INFO, e.getBaseEntry());
        return e.getID();
    }

    public void addKaoQinInfoEntrys(List<KaoQinInfoEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_INFO, dbObjects);
    }



    /**
     * 根据type查询
     * @param cardNo
     * @return
     */
    public KaoQinInfoEntry getKaoQinInfoEntry(String cardNo)
    {
        DBObject query =new BasicDBObject("cno",cardNo);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new KaoQinInfoEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public List<KaoQinInfoEntry> getAllKaoQinInfoEntry() {
        DBObject query =new BasicDBObject();
        List<KaoQinInfoEntry> relist=new ArrayList<KaoQinInfoEntry>();
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_INFO,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            KaoQinInfoEntry e;
            for(DBObject dbo:list)
            {
                e=new KaoQinInfoEntry((BasicDBObject)dbo);
                relist.add(e);
            }
        }
        return relist;
    }

    public void updKaoQinInfoEntry(KaoQinInfoEntry e) {
            BasicDBObject query = new BasicDBObject();
            query.append(Constant.ID, e.getID());
            BasicDBObject update=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
            update(MongoFacroty.getAppDB(),Constant.COLLECTION_KAOQIN_INFO,query,update);
    }

    public List<KaoQinInfoEntry> getKaoQinInfoEntryList(Integer accountNo, long startDate, long endDate) {
        BasicDBObject query =new BasicDBObject("ac",accountNo);

        BasicDBList dblist = new BasicDBList();
        if(startDate>0){
            dblist.add( new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_GTE,startDate)));
        }
        if(endDate>0) {
            dblist.add( new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_LTE,endDate)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        BasicDBObject sort =new BasicDBObject("cd",Constant.ASC);
        List<KaoQinInfoEntry> relist=new ArrayList<KaoQinInfoEntry>();
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_INFO,query, Constant.FIELDS, sort);
        if(null!=list && !list.isEmpty())
        {
            KaoQinInfoEntry e;
            for(DBObject dbo:list)
            {
                e=new KaoQinInfoEntry((BasicDBObject)dbo);
                relist.add(e);
            }
        }
        return relist;
    }

    public List<KaoQinInfoEntry> getKaoQinInfoEntryList(List<Integer> accountNos, long startDate, long endDate) {
        BasicDBObject query =new BasicDBObject("ac",new BasicDBObject(Constant.MONGO_IN, accountNos));

        BasicDBList dblist = new BasicDBList();
        if(startDate>0){
            dblist.add( new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_GTE,startDate)));
        }
        if(endDate>0) {
            dblist.add( new BasicDBObject("cd", new BasicDBObject(Constant.MONGO_LTE,endDate)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        BasicDBObject sort =new BasicDBObject("cd",Constant.ASC);
        List<KaoQinInfoEntry> relist=new ArrayList<KaoQinInfoEntry>();
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_INFO,query, Constant.FIELDS, sort);
        if(null!=list && !list.isEmpty())
        {
            KaoQinInfoEntry e;
            for(DBObject dbo:list)
            {
                e=new KaoQinInfoEntry((BasicDBObject)dbo);
                relist.add(e);
            }
        }
        return relist;
    }
}
