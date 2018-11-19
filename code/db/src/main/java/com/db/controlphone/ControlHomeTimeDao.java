package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlHomeTimeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-11-16.
 */
public class ControlHomeTimeDao extends BaseDao {
    //添加
    public String addEntry(ControlHomeTimeEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_HOME_CONTROL_TIME, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    public ControlHomeTimeEntry getEntry(ObjectId parentId,ObjectId userId,int type){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO).append("pid",parentId).append("uid",userId).append("week",type);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_HOME_CONTROL_TIME, query,Constant.FIELDS);
        if(dbObject!=null){
            return new ControlHomeTimeEntry((BasicDBObject)dbObject);
        }
        return null;
    }

    public List<ControlHomeTimeEntry> getEntryList(ObjectId parentId,ObjectId userId){
        List<ControlHomeTimeEntry> controlHomeTimeEntries = new ArrayList<ControlHomeTimeEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO).append("pid",parentId).append("uid",userId).append("type",Constant.ONE);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_HOME_CONTROL_TIME,query,Constant.FIELDS);
        if(dbObjects!=null){
            for(DBObject dbObject:dbObjects){
                ControlHomeTimeEntry controlHomeTimeEntry = new ControlHomeTimeEntry(dbObject);
                controlHomeTimeEntries.add(controlHomeTimeEntry);
            }
        }
        return controlHomeTimeEntries;
    }
}
