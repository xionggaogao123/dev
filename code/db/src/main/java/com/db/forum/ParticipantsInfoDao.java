package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.ParticipantsInfoEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/1/11.
 */
public class ParticipantsInfoDao extends BaseDao {

    public ObjectId saveOrUpdate(ParticipantsInfoEntry participantsInfoEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_PARTICIPATE,participantsInfoEntry.getBaseEntry());
        return participantsInfoEntry.getID();
    }


    public ParticipantsInfoEntry getEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_PARTICIPATE,query);
        if(null!=dbObject){
            return new ParticipantsInfoEntry(dbObject);
        }else {
            return null;
        }
    }

    /**
     * 查询创建者创建的参赛者列表
     * @param userId
     * @return
     */
    public List<ParticipantsInfoEntry> getEntries(ObjectId userId){
        List<ParticipantsInfoEntry> entries=new ArrayList<ParticipantsInfoEntry>();
        BasicDBObject query=new BasicDBObject().append("ctr",userId).append("ir",0);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_PARTICIPATE,query);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbObject:dbObjects){
                entries.add(new ParticipantsInfoEntry(dbObject));
            }
        }
        return entries;
    }

    public void removeInfo(ObjectId id){
        BasicDBObject query=new BasicDBObject().append(Constant.ID,id);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_PARTICIPATE,query,update);
    }
}
