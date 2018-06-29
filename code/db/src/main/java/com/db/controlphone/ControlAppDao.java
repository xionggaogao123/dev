package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlAppEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/6.
 */
public class ControlAppDao extends BaseDao {

    //添加
    public String addEntry(ControlAppEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //单查询
    public ControlAppEntry getEntry(ObjectId userId,ObjectId communityId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("cid", communityId) .append("uid", userId);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlAppEntry((BasicDBObject)dbo);
        }
        return null;
    }
    //修改
    public void updEntry(ControlAppEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP, query,updateValue);
    }
    //修改推送应用列表
    public void updateEntry(String name,String phone,ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET,
                        new BasicDBObject("nam", name).append("pho",phone));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_PHONE, query, updateValue);
    }
    //查找社区推荐应用列表
    public List<ControlAppEntry> getEntryListByCommunityId(List<ObjectId> communityIds) {
        BasicDBObject query = new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,communityIds))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_APP,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlAppEntry> entryList = new ArrayList<ControlAppEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlAppEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //删除
    public void delEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_APP, query,updateValue);
    }

    //查找社区推荐应用列表
    public List<ControlAppEntry> getEntryListByUserId(ObjectId userId,List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject()
                .append("uid",userId)
                .append("cid",new BasicDBObject(Constant.MONGO_IN,ids))
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_APP,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ControlAppEntry> entryList = new ArrayList<ControlAppEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ControlAppEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
