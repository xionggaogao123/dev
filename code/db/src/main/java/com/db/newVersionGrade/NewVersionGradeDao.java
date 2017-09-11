package com.db.newVersionGrade;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.newVersionGrade.NewVersionGradeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/5.
 */
public class NewVersionGradeDao extends BaseDao{

    public void saveNewVersionGradeEntry(NewVersionGradeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_GRADE,entry.getBaseEntry());
    }

    public Map<ObjectId,NewVersionGradeEntry> getNewVersionGradeMap(
            List<ObjectId> userIds,String year
    ){
        Map<ObjectId,NewVersionGradeEntry> newVersionGradeEntryMap = new HashMap<ObjectId, NewVersionGradeEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("uid", new BasicDBObject(Constant.MONGO_IN,userIds))
                .append("isr", Constant.ZERO)
                .append("ye", year);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_GRADE,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionGradeEntry entry=new NewVersionGradeEntry(dbObject);
                newVersionGradeEntryMap.put(entry.getUserId(),entry);
            }
        }
        return newVersionGradeEntryMap;
    }

    public void updateNewVersionGrade(ObjectId userId,
                                      String year,int gradeType){
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("isr", Constant.ZERO)
                .append("ye", year);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject( "gt",gradeType));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_GRADE,query,updateValue);
    }

    public NewVersionGradeEntry getEntryByCondition(ObjectId userId,
                                                    String year){
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("isr", Constant.ZERO)
                .append("ye", year);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_GRADE,
                query, Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionGradeEntry((BasicDBObject) dbObject);
        }else {
            return null;
        }
    }

    public int getMaxGradeType(ObjectId userId){
        BasicDBObject query = new BasicDBObject()
                .append("uid", userId)
                .append("isr", Constant.ZERO);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_NEW_VERSION_GRADE,
                        query, Constant.FIELDS,
                        new BasicDBObject("gt", Constant.DESC));
        List<NewVersionGradeEntry> entryList = new ArrayList<NewVersionGradeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new NewVersionGradeEntry((BasicDBObject) obj));
            }
        }
        if(entryList.size()>0){
            return entryList.get(0).getGradeType();
        }else{
            return 0;
        }
    }
}
