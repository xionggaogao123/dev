package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by James on 2017/11/7.
 */
public class ControlTimeDao extends BaseDao {
    //添加
    public String addEntry(ControlTimeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_TIME, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public ControlTimeEntry getEntry(ObjectId userId,ObjectId parentId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId).append("pid",parentId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }
     //单查询
    public ControlTimeEntry getEntryByUserId(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_TIME, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlTimeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    //修改
    public void updEntry(ControlTimeEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_TIME, query,updateValue);
    }

    //修改最新指令时间戳
    public void delEntry(ObjectId userId,long time){
        BasicDBObject query = new BasicDBObject("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("btm",time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_TIME, query,updateValue);
    }
    //批量修改最新指令时间戳
    public void delAllEntry(List<ObjectId> userIds,long time){
        BasicDBObject query = new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("btm",time));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_TIME, query,updateValue);
    }
    //修改管控状态
    public void updateTypeEntry(ObjectId id,int type){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cty",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_TIME, query,updateValue);
    }
}
