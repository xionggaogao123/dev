package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.SystemMessageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/12/4.
 */
public class SystemMessageDao extends BaseDao {
    //添加
    public String addEntry(SystemMessageEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SYSTEM_MESSAGE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    public List<SystemMessageEntry> selectContentList(List<ObjectId> ids) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SYSTEM_MESSAGE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC));
        List<SystemMessageEntry> retList =new ArrayList<SystemMessageEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new SystemMessageEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

}
