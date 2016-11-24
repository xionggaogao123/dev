package com.db.smartcard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smartcard.AccountInfoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2015/4/20.
 */
public class AccountInfoDao extends BaseDao {

    /**
     * 添加日志信息
     * @param e
     * @return
     */
    public ObjectId addAccountInfoEntry(AccountInfoEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_INFO, e.getBaseEntry());
        return e.getID();
    }

    public void addAccountInfoEntrys(List<AccountInfoEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_INFO, dbObjects);
    }



    /**
     * 根据type查询
     * @param cardNo
     * @return
     */
    public AccountInfoEntry getAccountInfoEntry(String cardNo)
    {
        DBObject query =new BasicDBObject("cno",cardNo);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new AccountInfoEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public Map<Integer, ObjectId> getAllAccountIdMap() {
        Map<Integer, ObjectId> retMap =new HashMap<Integer, ObjectId>();
        BasicDBObject query =new BasicDBObject();
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_INFO,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            AccountInfoEntry e;
            for(DBObject dbo:list)
            {
                e=new AccountInfoEntry((BasicDBObject)dbo);
                retMap.put(e.getAccounts(), e.getID());
            }
        }
        return retMap;
    }

    public AccountInfoEntry getAccountInfoEntryByUserId(ObjectId userId) {
        DBObject query =new BasicDBObject("uid",userId).append("af",Constant.ONE);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new AccountInfoEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public List<AccountInfoEntry> getAccountInfoEntryByUserIds(List<ObjectId> stuIds) {
        List<AccountInfoEntry> reList=new ArrayList<AccountInfoEntry>();
        DBObject query =new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_IN,stuIds)).append("af",Constant.ONE);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_INFO, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            AccountInfoEntry e;
            for(DBObject dbo:list)
            {
                e=new AccountInfoEntry((BasicDBObject)dbo);
                reList.add(e);
            }
        }
        return reList;
    }

    public List<AccountInfoEntry> getAccountInfoEntryBySchoolId(ObjectId schoolId) {
        List<AccountInfoEntry> reList=new ArrayList<AccountInfoEntry>();
        DBObject query =new BasicDBObject("si", schoolId).append("af",Constant.ONE);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ACCOUNT_INFO, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            AccountInfoEntry e;
            for(DBObject dbo:list)
            {
                e=new AccountInfoEntry((BasicDBObject)dbo);
                reList.add(e);
            }
        }
        return reList;
    }

    /*public void updAccountInfoEntry(AccountInfoEntry e) {
            BasicDBObject query = new BasicDBObject();
            query.append(Constant.ID, e.getID());
            BasicDBObject updateValue= new BasicDBObject("ems",e.getEmails()).append("ty",e.getType()).append("udf",e.getUserDef());
            BasicDBObject update=new BasicDBObject(Constant.MONGO_SET,updateValue);
            update(MongoFacroty.getAppDB(),Constant.COLLECTION_ACCOUNT_INFO,query,update);
    }*/
}
