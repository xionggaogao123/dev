package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.TeacherApproveEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2017/11/23.
 */
public class TeacherApproveDao extends BaseDao {
    //添加
    public String addEntry(TeacherApproveEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, entry.getBaseEntry());
        return entry.getID().toString();
    }

    public TeacherApproveEntry getEntry(ObjectId userId) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS);
        if (null != dbo) {
            return new TeacherApproveEntry((BasicDBObject) dbo);
        }
        return null;
    }

    //删除作业
    public void updateEntry4(ObjectId id, int type, String oldAvatar, String newAvatar) {
        BasicDBObject query = new BasicDBObject("uid", id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("typ", type).append("ota", oldAvatar).append("nta", newAvatar));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, updateValue);
    }

    //删除作业
    public void updateEntry(ObjectId id, int type) {
        BasicDBObject query = new BasicDBObject("uid", id);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("typ", type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, updateValue);
    }

    public List<TeacherApproveEntry> selectContentList(String seacherId, int type, int page, int pageSize) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if (type != 0) {
            query.append("typ", type);
        }
        if (seacherId != null && !seacherId.equals("")) {
            query.append("uid", new ObjectId(seacherId));
        }
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS, new BasicDBObject("ctm", Constant.DESC), (page - 1) * pageSize, pageSize);
        List<TeacherApproveEntry> retList = new ArrayList<TeacherApproveEntry>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                retList.add(new TeacherApproveEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<ObjectId> selectMap(List<ObjectId> userIds) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid", new BasicDBObject(Constant.MONGO_IN, userIds));
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS, new BasicDBObject("ctm", Constant.DESC));
        List<ObjectId> oids = new ArrayList<ObjectId>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                TeacherApproveEntry teacherApproveEntry = new TeacherApproveEntry((BasicDBObject) dbo);
                if (teacherApproveEntry.getType() == 2) {
                    oids.add(teacherApproveEntry.getUserId());
                }
            }
        }
        return oids;
    }

    public List<ObjectId> selectContentObjectList() {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS, new BasicDBObject("ctm", Constant.DESC));
        List<ObjectId> retList = new ArrayList<ObjectId>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                retList.add(new TeacherApproveEntry((BasicDBObject) dbo).getUserId());
            }
        }
        return retList;
    }

    /**
     * 符合搜索条件的对象个数
     *
     * @return
     */
    public int getNumber(String seacherId, int type) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if (type != 0) {
            query.append("typ", type);
        }
        if (seacherId != null && !seacherId.equals("")) {
            query.append("uid", new ObjectId(seacherId));
        }
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TEACHER_APPROVE,
                        query);
        return count;
    }

    public List<TeacherApproveEntry> getTeacherByType() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO)
                .append("typ", 2)
                .append("role", new BasicDBObject(Constant.MONGO_NOTIN, list));
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS);
        List<TeacherApproveEntry> retList = new ArrayList<TeacherApproveEntry>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                retList.add(new TeacherApproveEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<TeacherApproveEntry> getStaffByType() {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO)
                .append("typ", 2)
                .append("role", Constant.ONE);//员工跟老师区别
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS);
        List<TeacherApproveEntry> retList = new ArrayList<TeacherApproveEntry>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                retList.add(new TeacherApproveEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<TeacherApproveEntry> getUserListByRole(Map map) {
        BasicDBObject query = new BasicDBObject();
        if ("老师".equals(map.get("roleOption"))) {
            List<Integer> list = new ArrayList<Integer>();
            list.add(1);
            query.append("isr", Constant.ZERO)
                    .append("typ", 2)
                    .append("role", new BasicDBObject(Constant.MONGO_NOTIN, list)); //role 不为1的情况
        } else {
            query.append("isr", Constant.ZERO)
                    .append("typ", 2)
                    .append("role", Constant.ONE);//员工跟老师区别
        }
        int page = map.get("page") == null?1:Integer.parseInt(map.get("page").toString());
        int pageSize = map.get("pageSize") == null?10:Integer.parseInt(map.get("pageSize").toString());
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS,new BasicDBObject("ctm", Constant.DESC), (page - 1) * pageSize, pageSize);
        List<TeacherApproveEntry> retList = new ArrayList<TeacherApproveEntry>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                retList.add(new TeacherApproveEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public List<TeacherApproveEntry> getUserListByTeacherRole() {
        BasicDBObject query = new BasicDBObject();
            List<Integer> list = new ArrayList<Integer>();
            list.add(1);
            query.append("isr", Constant.ZERO)
                    .append("typ", 2)
                    .append("role", new BasicDBObject(Constant.MONGO_NOTIN, list)); //role 不为1的情况
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query);
        List<TeacherApproveEntry> retList = new ArrayList<TeacherApproveEntry>();
        if (null != dboList && !dboList.isEmpty()) {
            for (DBObject dbo : dboList) {
                retList.add(new TeacherApproveEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public TeacherApproveEntry getTeacherEntry(ObjectId userId) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", userId);

        query.append("typ", 2) .append("role", new BasicDBObject(Constant.MONGO_NE, 1)); //role 不为1的情况
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query, Constant.FIELDS);
        if (null != dbo) {
            return new TeacherApproveEntry((BasicDBObject) dbo);
        }
        return null;
    }

    public Map<ObjectId,TeacherApproveEntry> getTeacherEntryMap(List<ObjectId> userIds) {

        Map<ObjectId,TeacherApproveEntry> result = new HashMap<ObjectId, TeacherApproveEntry>();
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO).append("uid", new BasicDBObject(Constant.MONGO_IN, userIds));

        query.append("typ", 2) .append("role", new BasicDBObject(Constant.MONGO_NE, 1)); //role 不为1的情况
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_APPROVE, query);
        if (dbObjects.size() > 0) {
            for (DBObject dbObject : dbObjects){
                TeacherApproveEntry entry = new TeacherApproveEntry((BasicDBObject) dbObject);
                result.put(entry.getUserId(), entry);
            }
        }
        return result;
    }
}
