package com.db.smartcard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smartcard.KaoQinStateEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/6/20.
 */
public class KaoQinStateDao extends BaseDao {

    /**
     * 添加日志信息
     * @param e
     * @return
     */
    public ObjectId addKaoQinStateEntry(KaoQinStateEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_STATE, e.getBaseEntry());
        return e.getID();
    }

    public void addKaoQinStateEntrys(List<KaoQinStateEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_STATE, dbObjects);
    }



    /**
     * 根据type查询
     * @param userId
     * @return
     */
    public KaoQinStateEntry getKaoQinStateEntry(ObjectId userId, String type)
    {
        DBObject query =new BasicDBObject("uid",userId).append("ty", type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_STATE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new KaoQinStateEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 根据type查询
     * @param userIds
     * @return
     */
    public List<KaoQinStateEntry> getKaoQinStateEntry(List<ObjectId> userIds, String type)
    {
        List<KaoQinStateEntry> reList=new ArrayList<KaoQinStateEntry>();
        DBObject query =new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_IN,userIds)).append("ty", type);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_STATE, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            KaoQinStateEntry e;
            for(DBObject dbo:list)
            {
                e=new KaoQinStateEntry((BasicDBObject)dbo);
                reList.add(e);
            }
        }
        return reList;
    }


    public void updKaoQinStateEntry(KaoQinStateEntry e) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, e.getID());
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_KAOQIN_STATE,query,update);
    }


}