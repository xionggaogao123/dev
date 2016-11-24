package com.db.teacherevaluation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teacherevaluation.MemberGroupEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/4/19.
 */
public class MemberGroupDao extends BaseDao {

    private static final String COLLECTION_NAME = Constant.COLLECTION_TE_MEMBERGROUP;


    public ObjectId addMemberGroup(MemberGroupEntry entry) {
        save(MongoFacroty.getAppDB(), COLLECTION_NAME, entry.getBaseEntry());
        return entry.getID();
    }

    public MemberGroupEntry getMemberGroup(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        if (dbObject != null) {
            return new MemberGroupEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    public MemberGroupEntry getMemberGroupBySchoolIdAndYear(ObjectId schoolId, String year) {
        DBObject query = new BasicDBObject("si", schoolId).append("y", year);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        if (dbObject != null) {
            return new MemberGroupEntry((BasicDBObject) dbObject);
        }
        return null;
    }

    public void removeMemberGroupBySchoolIdAndYear(ObjectId schoolId, String year) {
        DBObject query = new BasicDBObject("si", schoolId).append("y", year);
        remove(MongoFacroty.getAppDB(), COLLECTION_NAME, query);
    }

    public void updateTeacherGroup(ObjectId schoolId, String year, ObjectId groupId, String name, int num, int lnum) {
        DBObject query = new BasicDBObject("si", schoolId).append("y", year).append("tgs.id", groupId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tgs.$.nm", name).append("tgs.$.num", num).append("tgs.$.lnum", lnum));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    public void deleteTeacherGroup(ObjectId schoolId, String year, MemberGroupEntry.TeacherGroup teacherGroup) {
        DBObject query = new BasicDBObject("si", schoolId).append("y", year);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("tgs", teacherGroup.getBaseEntry()));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }
}
