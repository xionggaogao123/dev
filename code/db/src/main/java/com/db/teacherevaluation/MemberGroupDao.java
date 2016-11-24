package com.db.teacherevaluation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teacherevaluation.MemberGroupEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/19.
 */
public class MemberGroupDao extends BaseDao {

    private static final String COLLECTION_NAME = Constant.COLLECTION_TE_MEMBERGROUP;


    public ObjectId saveMemberGroup(MemberGroupEntry entry){
        save(MongoFacroty.getAppDB(), COLLECTION_NAME, entry.getBaseEntry());
        return entry.getID();
    }

    public MemberGroupEntry getMemberGroup(ObjectId id){
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        if(dbObject != null){
            return new MemberGroupEntry((BasicDBObject)dbObject);
        }
        return null;
    }

    public List<MemberGroupEntry> getMemberGroupByIds(List<ObjectId> evaluationIds, DBObject fields){
        List<MemberGroupEntry> memberGroupEntries = new ArrayList<MemberGroupEntry>();
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, evaluationIds)).append("ir", 0);
        DBObject orderBy = new BasicDBObject(Constant.ID, Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields, orderBy);
        if(dbObjects != null && dbObjects.size() > 0){
            for(DBObject dbObject : dbObjects){
                memberGroupEntries.add(new MemberGroupEntry((BasicDBObject)dbObject));
            }
        }
        return memberGroupEntries;
    }

    public List<MemberGroupEntry> getMemberGroupBySchoolIdAndYear(ObjectId schoolId, String year, DBObject fields){
        List<MemberGroupEntry> memberGroupEntries = new ArrayList<MemberGroupEntry>();
        DBObject query = new BasicDBObject("si", schoolId).append("y", year).append("ir", 0);
        DBObject orderBy = new BasicDBObject(Constant.ID, Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, fields, orderBy);
        if(dbObjects != null && dbObjects.size() > 0){
            for(DBObject dbObject : dbObjects){
                memberGroupEntries.add(new MemberGroupEntry((BasicDBObject)dbObject));
            }
        }
        return memberGroupEntries;
    }

    public void removeMemberGroup(ObjectId id){
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", 1));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    public void updateTeacherGroup(ObjectId evaluationId, ObjectId groupId, String name, int num, int lnum){
        DBObject query = new BasicDBObject(Constant.ID, evaluationId).append("tgs.id", groupId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("tgs.$.nm", name).append("tgs.$.num", num).append("tgs.$.lnum", lnum));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    public void deleteTeacherGroup(ObjectId evaluationId, MemberGroupEntry.TeacherGroup teacherGroup){
        DBObject query = new BasicDBObject(Constant.ID, evaluationId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("tgs", teacherGroup.getBaseEntry()));
        update(MongoFacroty.getAppDB(), COLLECTION_NAME, query, updateValue);
    }

    @Deprecated
    public List<MemberGroupEntry> getAll(){
        List<MemberGroupEntry> memberGroupEntries = new ArrayList<MemberGroupEntry>();
        DBObject query = new BasicDBObject();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), COLLECTION_NAME, query, Constant.FIELDS);
        if(dbObjects != null && dbObjects.size()>0){
            for(DBObject dbObject : dbObjects){
                memberGroupEntries.add(new MemberGroupEntry((BasicDBObject)dbObject));
            }
        }
        return memberGroupEntries;
    }

}
