package com.db.extendedcourse;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.extendedcourse.ExtendedUserApplyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-12-10.
 */
public class ExtendedUserApplyDao extends BaseDao {
    public void saveEntry(ExtendedUserApplyEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY,entry.getBaseEntry());
    }

    //查询已成功入选用户
    public List<ObjectId> getIdsByCourseId(ObjectId courseId,ObjectId communityId){
        List<ObjectId> entryList=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("cid",courseId);
        query.append("cmid",communityId);
        query.append("sta",Constant.TWO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtendedUserApplyEntry((BasicDBObject) obj).getUserId());
            }
        }
        return entryList;
    }

    //查询用户记录
    public ExtendedUserApplyEntry getEntryById(ObjectId id,ObjectId userId){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("uid",userId);
        query.append("cid",id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_USER_APPLY,query,Constant.FIELDS);
        if(dbObject==null){
            return null;
        }else{
            return new ExtendedUserApplyEntry((BasicDBObject) dbObject);
        }
    }
}

