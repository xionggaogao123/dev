package com.db.jiaschool;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2018/2/2.
 */
public class SchoolCommunityDao extends BaseDao {

    //添加标签
    public ObjectId addEntry(SchoolCommunityEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 由学校list查询社群list
     * @param objectIds
     * @return
     */
    public List<SchoolCommunityEntry> getReviewList(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("sid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_COMMUNITY_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<SchoolCommunityEntry> entryList = new ArrayList<SchoolCommunityEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolCommunityEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }
    //
    public List<ObjectId> getCommunityIdsList(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("sid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_COMMUNITY_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<ObjectId> entryList = new ArrayList<ObjectId>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolCommunityEntry((BasicDBObject) obj).getCommunityId());
            }
        }
        return entryList;
    }

    /**
     * 由社群list查询学校list
     * @param objectIds
     * @return
     */
    public List<SchoolCommunityEntry> getReviewList2(List<ObjectId> objectIds) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        query.append("cid",new BasicDBObject(Constant.MONGO_IN,objectIds));
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_COMMUNITY_SCHOOL,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC);
        List<SchoolCommunityEntry> entryList = new ArrayList<SchoolCommunityEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolCommunityEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    //查询
    public SchoolCommunityEntry getEntryById(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject("cid",communityId);
        query.append("isr",Constant.ZERO);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, query, Constant.FIELDS);
        if (obj != null) {
            return new SchoolCommunityEntry((BasicDBObject) obj);
        }
        return null;
    }

    //删除
    public void delEntry(ObjectId communityId){
        BasicDBObject query = new BasicDBObject("cid",communityId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, query,updateValue);
    }

    //删除学校绑定关系
    public void delEntryBySchoolId(ObjectId schoolId){
        BasicDBObject query = new BasicDBObject("sid",schoolId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("isr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_COMMUNITY_SCHOOL, query,updateValue);
    }

    public Map<ObjectId,List<ObjectId>> getAllIdGroupBySchoolId(List<ObjectId> schoolIdList) {

        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if (schoolIdList.size()>0){
            query.append("sid",new BasicDBObject(Constant.MONGO_IN,schoolIdList));
        }

        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_COMMUNITY_SCHOOL,
                        query, Constant.FIELDS);
        List<SchoolCommunityEntry> entryList = new ArrayList<SchoolCommunityEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new SchoolCommunityEntry((BasicDBObject) obj));
            }
        }
//        List<ObjectId> schoolIdList = new ArrayList<ObjectId>();
//        //以学校分组-获取学校Id
//        for (SchoolCommunityEntry entry : entryList){
//            if (!schoolIdList.contains(entry.getSchoolId())){
//                schoolIdList.add(entry.getSchoolId());
//            }
//        }
        //以学校分组-获取学校Id对应的communityIdList
        Map<ObjectId,List<ObjectId>> map = new HashMap<ObjectId, List<ObjectId>>();
        for (ObjectId schoolId : schoolIdList){
            List<ObjectId> communityIdList = new ArrayList<ObjectId>();
            for (SchoolCommunityEntry entry : entryList){
                if (schoolId.equals(entry.getSchoolId())){
                    communityIdList.add(entry.getCommunityId());
                }
            }
            map.put(schoolId,communityIdList);
        }
        return map;
    }
}
