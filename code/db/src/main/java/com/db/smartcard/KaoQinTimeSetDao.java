package com.db.smartcard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smartcard.KaoQinTimeSetEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/4/20.
 */
public class KaoQinTimeSetDao extends BaseDao {

    /**
     * 添加日志信息
     * @param e
     * @return
     */
    public ObjectId addKaoQinTimeSetEntry(KaoQinTimeSetEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_TIME_SET, e.getBaseEntry());
        return e.getID();
    }

    public void addKaoQinTimeSetEntrys(List<KaoQinTimeSetEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_TIME_SET, dbObjects);
    }



    /**
     * 根据type查询
     * @param schoolId
     * @return
     */
    public KaoQinTimeSetEntry getKaoQinTimeSetEntry(ObjectId schoolId)
    {
        DBObject query =new BasicDBObject("si",schoolId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_TIME_SET, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new KaoQinTimeSetEntry((BasicDBObject)dbo);
        }
        return null;
    }

    public List<KaoQinTimeSetEntry> getAllKaoQinTimeSetEntry() {
        DBObject query =new BasicDBObject();
        List<KaoQinTimeSetEntry> relist=new ArrayList<KaoQinTimeSetEntry>();
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_KAOQIN_TIME_SET,query, Constant.FIELDS);
        if(null!=list && !list.isEmpty())
        {
            KaoQinTimeSetEntry e;
            for(DBObject dbo:list)
            {
                e=new KaoQinTimeSetEntry((BasicDBObject)dbo);
                relist.add(e);
            }
        }
        return relist;
    }

    public void updKaoQinTimeSetEntry(KaoQinTimeSetEntry e) {
            BasicDBObject query = new BasicDBObject();
            query.append(Constant.ID, e.getID());
            BasicDBObject update=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
            update(MongoFacroty.getAppDB(),Constant.COLLECTION_KAOQIN_TIME_SET,query,update);
    }
}
