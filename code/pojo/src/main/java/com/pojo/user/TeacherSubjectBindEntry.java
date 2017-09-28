package com.pojo.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/28.
 */
public class TeacherSubjectBindEntry extends BaseDBObject{

    public TeacherSubjectBindEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public TeacherSubjectBindEntry(ObjectId userId,List<ObjectId> subjectIds){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("uid",userId)
                .append("sis", MongoUtils.convert(subjectIds))
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setSubjectIds(List<ObjectId> subjectIds){
        setSimpleValue("sis",MongoUtils.convert(subjectIds));
    }

    public List<ObjectId> getSubjectIds(){
        List<ObjectId> subjectIds=new ArrayList<ObjectId>();
        BasicDBList list=getDbList("sis");
        for(Object o:list){
            subjectIds.add((ObjectId)o);
        }
        return subjectIds;
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
}
