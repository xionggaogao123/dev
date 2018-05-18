package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.HourClassEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-04-26.
 */
public class HourClassDao extends BaseDao {

    /**
     * 批量保存
     *
     * @param list
     */
    public void addBatch(List<DBObject> list) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, list);
    }

    //添加课时
    public String addEntry(HourClassEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //删除
    public void delEntry(ObjectId parentId,ObjectId userId){
        BasicDBObject query = new BasicDBObject("pid",parentId).append("uid",userId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }

    //修改价格
    public void updatePriceEntry(ObjectId id,int price){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cnp",price));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,updateValue);
    }
    public HourClassEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id).append("isr",Constant.ZERO);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS,query,Constant.FIELDS);
        if(null!=dbObject){
            return new HourClassEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    //查询课时
    public List<HourClassEntry> getEntryList(ObjectId parentId){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append("pid", parentId).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord",Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }


    //查询订单课时
    public List<HourClassEntry> getEntryList(List<ObjectId> ids){
        List<HourClassEntry> entryList=new ArrayList<HourClassEntry>();
        BasicDBObject query=new BasicDBObject().append(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids)).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_HOUR_CLASS, query,
                Constant.FIELDS, new BasicDBObject("ord", Constant.ASC));
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new HourClassEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
}
