package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.TeacherSubjectBindEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/28.
 */
public class TeacherSubjectBindDao extends BaseDao{

    public void saveTeacherSubjectEntry(TeacherSubjectBindEntry subjectBindEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_SUBJECT_BIND,subjectBindEntry.getBaseEntry());
    }

    public void removeEntryByTeacherId(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_SUBJECT_BIND,query);
    }

    public TeacherSubjectBindEntry getEntry(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_SUBJECT_BIND,query,Constant.FIELDS);
        if(null!=dbObject){
            return new TeacherSubjectBindEntry(dbObject);
        }else{
            return null;
        }
    }
}
