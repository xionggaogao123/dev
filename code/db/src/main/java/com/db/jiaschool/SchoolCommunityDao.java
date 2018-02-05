package com.db.jiaschool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018/2/2.
 */
public class SchoolCommunityDao extends BaseDao {

    //添加标签
    public ObjectId addEntry(SchoolCommunityEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, entry.getBaseEntry());
        return entry.getID();
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
