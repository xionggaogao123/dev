package com.db.smartcard;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.smartcard.TransInfoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by guojing on 2015/4/20.
 */
public class TransInfoDao extends BaseDao {

    /**
     * 添加日志信息
     * @param e
     * @return
     */
    public ObjectId addTransInfoEntry(TransInfoEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRANS_INFO, e.getBaseEntry());
        return e.getID();
    }

    public void addTransInfoEntrys(List<TransInfoEntry> list)
    {
        List<DBObject> dbObjects = MongoUtils.fetchDBObjectList(list);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRANS_INFO, dbObjects);
    }



    /**
     * 根据type查询
     * @param cardNo
     * @return
     */
    public TransInfoEntry getTransInfoEntry(String cardNo)
    {
        DBObject query =new BasicDBObject("cno",cardNo);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TRANS_INFO, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new TransInfoEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /*public void updTransInfoEntry(TransInfoEntry e) {
            BasicDBObject query = new BasicDBObject();
            query.append(Constant.ID, e.getID());
            BasicDBObject updateValue= new BasicDBObject("ems",e.getEmails()).append("ty",e.getType()).append("udf",e.getUserDef());
            BasicDBObject update=new BasicDBObject(Constant.MONGO_SET,updateValue);
            update(MongoFacroty.getAppDB(),Constant.COLLECTION_TRANS_INFO,query,update);
    }*/
}
