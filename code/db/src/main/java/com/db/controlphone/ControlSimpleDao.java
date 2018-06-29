package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlSimpleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-06-27.
 */
public class ControlSimpleDao extends BaseDao {
    //
    public ControlSimpleEntry getEntryForParent(int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO).append("typ",type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SIMPLE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlSimpleEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //添加
    public String addEntry(ControlSimpleEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SIMPLE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //修改
    public void updateEntry(ObjectId id,long time,String ip){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("tim",time).append("ip",ip));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_SIMPLE, query,updateValue);
    }

}
