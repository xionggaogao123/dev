package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlStudentResultEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-05-21.
 */
public class ControlStudentResultDao extends BaseDao {
    //添加
    public String addEntry(ControlStudentResultEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_STUDENT_RESULT, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public ControlStudentResultEntry getEntry(ObjectId userId,ObjectId parentId,long dateTime) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId).append("pid",parentId).append("dtm",dateTime);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_STUDENT_RESULT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ControlStudentResultEntry((BasicDBObject)dbo);
        }
        return null;
    }
    //修改最新指令时间戳
    public void updateEntry(ObjectId id,long newAppUser,long newAppTime,long time){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("tim",time).append("nau",newAppUser).append("nat",newAppTime));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_STUDENT_RESULT, query,updateValue);
    }
}
