package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.SchoolSubjectGroupEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fl on 2016/7/16.
 */
public class SchoolSubjectGroupDao extends BaseDao {

    /**
     * 添加一个科目组合
     * @param schoolSubjectGroupEntry
     * @return
     */
    public ObjectId addEntry(SchoolSubjectGroupEntry schoolSubjectGroupEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_SCHOOLSUBJECTGROUP, schoolSubjectGroupEntry.getBaseEntry());
        return schoolSubjectGroupEntry.getID();
    }

    /**
     * 更新开放状态
     * @param id
     * @param isPublic
     */
    public void updatePublicState(ObjectId id, Boolean isPublic){
        DBObject query = new BasicDBObject("sgs.id", id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sgs.$.isp", isPublic));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_SCHOOLSUBJECTGROUP, query, updateValue);
    }

    /**
     * 查找
     * @param schoolId
     * @param year
     * @param gradeId
     * @return
     */
    public SchoolSubjectGroupEntry getEntry(ObjectId schoolId, String year, ObjectId gradeId){
        DBObject query = new BasicDBObject("sid", schoolId).append("year", year).append("gid", gradeId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_SCHOOLSUBJECTGROUP, query, Constant.FIELDS);
        if(dbObject != null){
            return new SchoolSubjectGroupEntry((BasicDBObject)dbObject);
        }
        return null;
    }

    //
}
