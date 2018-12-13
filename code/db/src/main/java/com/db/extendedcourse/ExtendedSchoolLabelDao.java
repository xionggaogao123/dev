package com.db.extendedcourse;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.extendedcourse.ExtendedSchoolLabelEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-12-06.
 */
public class ExtendedSchoolLabelDao extends BaseDao {

    public void saveEntry(ExtendedSchoolLabelEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_SCHOOL_LABEL,entry.getBaseEntry());
    }


    public List<ExtendedSchoolLabelEntry> getListBySchoolId(ObjectId schoolId){
        List<ExtendedSchoolLabelEntry> entryList=new ArrayList<ExtendedSchoolLabelEntry>();
        BasicDBObject query=new BasicDBObject("sid",schoolId).append("isr", Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_SCHOOL_LABEL, query,
                Constant.FIELDS, Constant.MONGO_SORTBY_DESC);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtendedSchoolLabelEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //删除
    public void delEntryById(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_SCHOOL_LABEL, query,updateValue);
    }


    public ExtendedSchoolLabelEntry getEntryBySchoolIdAndName(ObjectId schoolId,String name){
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append("sid",schoolId);
        query.append("nm",name);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTENDED_SCHOOL_LABEL,query,Constant.FIELDS);
        if(dbObject==null){
            return null;
        }else{
            return new ExtendedSchoolLabelEntry((BasicDBObject) dbObject);
        }
    }


}
