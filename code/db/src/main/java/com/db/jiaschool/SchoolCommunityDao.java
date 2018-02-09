package com.db.jiaschool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/2/2.
 */
public class SchoolCommunityDao extends BaseDao {

    //添加标签
    public ObjectId addEntry(SchoolCommunityEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, entry.getBaseEntry());
        return entry.getID();
    }

    public List<SchoolCommunityEntry> getReviewList(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("sid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_HOME_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<SchoolCommunityEntry> entryList = new ArrayList<SchoolCommunityEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolCommunityEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //查询
    public SchoolCommunityEntry getEntryById(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject("cid",communityId);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, query, Constant.FIELDS);
        if (obj != null) {
            return new SchoolCommunityEntry((BasicDBObject) obj);
        }
        return null;
    }

    //删除
    public void delEntry(ObjectId communityId){
        BasicDBObject query = new BasicDBObject("cid",communityId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, query,updateValue);
    }
}