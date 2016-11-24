package com.db.leave;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.leave.ReplaceCourse;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2016/3/4.
 */
public class ReplaceCourseDao extends BaseDao {
    //添加
    public void addReplaceCourse(ReplaceCourse rp) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, rp.getBaseEntry());
    }

    //查询全校的代课
    public List<ReplaceCourse> findReplaceList(ObjectId schoolId, int page, int pageSize, ObjectId teacherId, String term) {
        List<ReplaceCourse> list = new ArrayList<ReplaceCourse>();
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("te", term)
                .append("dl", 0);
        if (teacherId != null)
            query.append("tid", teacherId);
        int skip = (page - 1) * pageSize;
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query, new BasicDBObject(),
                new BasicDBObject(Constant.ID, -1), skip, pageSize);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject db : dbObjectList) {
                list.add(new ReplaceCourse((BasicDBObject) db));
            }
        }
        return list;
    }

    //统计全校代课
    public int countReplaceList(ObjectId schoolId, ObjectId teacherId, String term) {
        List<ReplaceCourse> list = new ArrayList<ReplaceCourse>();
        BasicDBObject query = new BasicDBObject()
                .append("sid", schoolId)
                .append("te", term)
                .append("dl", 0);
        if (teacherId != null)
            query.append("tid", teacherId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query);
    }

    //查询我的代课
    public List<ReplaceCourse> findMyReplaceList(ObjectId teacherId, String term, int page, int pageSize) {
        List<ReplaceCourse> list = new ArrayList<ReplaceCourse>();
        BasicDBObject query = new BasicDBObject()
                .append("tid", teacherId)
                .append("te", term).append("dl", 0);
        int skip = (page - 1) * pageSize;
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query, new BasicDBObject(),
                new BasicDBObject(Constant.ID, -1), skip, pageSize);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject db : dbObjectList) {
                list.add(new ReplaceCourse((BasicDBObject) db));
            }
        }
        return list;
    }

    //统计我的代课
    public int countMyReplace(ObjectId teacherId, String term) {
        BasicDBObject query = new BasicDBObject()
                .append("tid", teacherId)
                .append("te", term)
                .append("dl", 0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query);
    }

    //删除代课
    public void removeReplaceList(List<ObjectId> replaceIds) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, replaceIds));
        //remove(MongoFacroty.getAppDB(),Constant.COLLECTION_REPLACE_COURSE,query);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("dl", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query, updateValue);
    }

    //根据id列表查询
    public List<ReplaceCourse> findByIds(List<ObjectId> replaceIds) {
        List<ReplaceCourse> list = new ArrayList<ReplaceCourse>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, replaceIds))
                .append("dl", 0);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query, new BasicDBObject());
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject db : dbObjectList) {
                list.add(new ReplaceCourse((BasicDBObject) db));
            }
        }
        return list;
    }

    //删除无用的代课记录
    public void removeUnusedReplaceList(List<ObjectId> replaceIds, ObjectId leaveId) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_NOTIN, replaceIds))
                .append("li", leaveId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("dl", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query, updateValue);
        //remove(MongoFacroty.getAppDB(),Constant.COLLECTION_REPLACE_COURSE,query);
    }

    public void removeByTermAndSchool(String term, ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("sid", schoolId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("dl", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPLACE_COURSE, query, updateValue);
    }
}
