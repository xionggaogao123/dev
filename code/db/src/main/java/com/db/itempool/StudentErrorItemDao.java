package com.db.itempool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.itempool.StudentErrorItemEntry;
import com.pojo.itempool.StudentErrorItemEntry.Item;
import com.sys.constants.Constant;

/**
 * 学生错题集
 *
 * @author fourer
 */
public class StudentErrorItemDao extends BaseDao {

    /**
     * 增加一个学生错题集
     *
     * @param e
     * @return
     */
    public ObjectId addStudentErrorItem(StudentErrorItemEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查看一个学生是有已经有错题集
     *
     * @param userId
     * @return
     */
    public boolean isExists(ObjectId userId) {
        DBObject query = new BasicDBObject("ui", userId);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query);
        return count >= Constant.ONE;
    }

    /**
     * 查找某个用户的某个科目情况
     *
     * @param userId
     * @param subject
     * @return
     */
    public IdNameValuePair getSubject(ObjectId userId, int subject) {
        DBObject query = new BasicDBObject("ui", userId).append("subs.id", subject);

        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, new BasicDBObject("subs.$", 1));
        if (null != dbo) {
            StudentErrorItemEntry e = new StudentErrorItemEntry((BasicDBObject) dbo);
            List<IdNameValuePair> list = e.getSubjects();
            if (!list.isEmpty()) {
                return list.get(Constant.ZERO);
            }
        }
        return null;
    }

    /**
     * 查找某个用户的某个错题情况
     *
     * @param userId
     * @param itemId
     * @return
     */
    public StudentErrorItemEntry.Item getErrorItem(ObjectId userId, ObjectId itemId) {
        DBObject query = new BasicDBObject("ui", userId).append("items.ori", itemId);

        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, new BasicDBObject("items.$", 1));
        if (null != dbo) {
            StudentErrorItemEntry e = new StudentErrorItemEntry((BasicDBObject) dbo);
            List<Item> list = e.getItems();
            if (!list.isEmpty()) {
                return list.get(Constant.ZERO);
            }
        }
        return null;
    }

    /**
     * 增加一个科目信息
     *
     * @param userId
     * @param pair
     */
    public void addSubject(ObjectId userId, IdNameValuePair pair) {
        DBObject query = new BasicDBObject("ui", userId);
        DBObject updateValue = new BasicDBObject().append(Constant.MONGO_PUSH, new BasicDBObject("subs", pair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, updateValue);
    }


    /**
     * 更新某个科目的数目
     *
     * @param userId
     * @param subject
     * @param count
     */
    public void updateSubjectCount(ObjectId userId, int subject, int count) {
        DBObject query = new BasicDBObject("ui", userId).append("subs.id", subject);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("subs.$.v", count));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, updateValue);
    }


    /**
     * 增加一个错题集；
     * 必须是之前没有该错题集；
     *
     * @param userId
     * @param subject 科目ID 该科目之前已经含有
     * @param item
     */
    public void addItem(ObjectId userId, int subject, StudentErrorItemEntry.Item item) {
        DBObject query = new BasicDBObject("ui", userId);
        DBObject incDBObject = new BasicDBObject("con", 1);
        if (subject > -1) {
            query.put("subs.id", subject);
            incDBObject.put("subs.$.v", 1);
        }
        DBObject updateValue = new BasicDBObject(Constant.MONGO_INC, incDBObject).append(Constant.MONGO_PUSH, new BasicDBObject("items", item.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, updateValue);
    }


    /**
     * 更新一个错题情况；
     * 必须是该题目已经存在；
     *
     * @param userId
     * @param item
     */
    public void updateItem(ObjectId userId, ObjectId item) {
        DBObject query = new BasicDBObject("ui", userId).append("items.ori", item);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("items.$.maxt", System.currentTimeMillis()))
                .append(Constant.MONGO_INC, new BasicDBObject("items.$.con", 1))
                .append(Constant.MONGO_PUSH, new BasicDBObject("items.$.ts", System.currentTimeMillis()));

        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, updateValue);
    }


    /**
     * 科目信息
     *
     * @param userId
     * @return
     */
    public List<IdNameValuePair> getSubjects(ObjectId userId) {
        DBObject query = new BasicDBObject("ui", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, new BasicDBObject("subs", 1));
        if (null != dbo) {
            StudentErrorItemEntry e = new StudentErrorItemEntry((BasicDBObject) dbo);
            return e.getSubjects();
        }
        return new ArrayList<IdNameValuePair>();
    }

    /**
     * 查找错题的知识面
     *
     * @param userId
     * @return
     */
    public Set<ObjectId> getErrorItemScopes(ObjectId userId, int subject) {
        Set<ObjectId> hashset = new HashSet<ObjectId>();
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("ui", userId));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("items", 1));
        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$items");
        DBObject matchDBO2 = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("items.sub", subject));

        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, matchDBO, projectDBO, unbindDBO, matchDBO2);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject dbo;
            BasicDBObject itemDBO;
            Item item;
            while (iter.hasNext()) {
                dbo = (BasicDBObject) iter.next();

                itemDBO = (BasicDBObject) dbo.get("items");
                item = new Item(itemDBO);
                hashset.add(item.getScope());
            }
        } catch (Exception e) {
        }
        return hashset;
    }

    /**
     * 删除学生错题集
     *
     * @param userId
     * @param item
     */
    public void removeItem(ObjectId userId, StudentErrorItemEntry.Item item) {
        DBObject query = new BasicDBObject("ui", userId).append("subs.id", item.getSubject());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("items", item.getBaseEntry()))
                .append(Constant.MONGO_INC, new BasicDBObject("subs.$.v", -1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, updateValue)
        ;
    }

    /**
     * 根据科目查询用户已经做过的题目ID
     *
     * @param userId
     * @param subject
     * @return
     */
    public List<ObjectId> getItemIds(ObjectId userId, int subject) {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        DBObject query = new BasicDBObject("ui", userId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, new BasicDBObject("items.ori", 1).append("items.sub", 1));
        if (null != dbo) {
            StudentErrorItemEntry e = new StudentErrorItemEntry((BasicDBObject) dbo);
            for (Item item : e.getItems()) {
                if (item.getSubject() == subject) {
                    retList.add(item.getOriId());
                }
            }
        }
        return retList;
    }


    /**
     * @param userId
     * @param orderBy 1:乱序 2最新错题 3错次最多
     * @param subject 科目
     * @param skip
     * @param limit
     * @return
     */
    public List<StudentErrorItemEntry.Item> getItems(ObjectId userId, int orderBy, int subject, int skip, int limit) {
        DBObject matchDBO = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("ui", userId));
        DBObject projectDBO = new BasicDBObject(Constant.MONGO_PROJECT, new BasicDBObject("items", 1));

        DBObject unbindDBO = new BasicDBObject(Constant.MONGO_UNWIND, "$items");

        DBObject matchDBO1 = new BasicDBObject(Constant.MONGO_MATCH, new BasicDBObject("items.sub", subject));


        DBObject order = new BasicDBObject();
        if (Constant.ONE == orderBy) {
            order.put("items.scope", -1);
        }
        if (Constant.TWO == orderBy) {
            order.put("items.maxt", -1);
        }
        if (Constant.THREE == orderBy) {
            order.put("items.con", -1);
        }

        DBObject sortDBO = new BasicDBObject(Constant.MONGO_SORT, order);
        DBObject skipDBO = new BasicDBObject(Constant.MONGO_SKIP, skip);
        DBObject limitDBO = new BasicDBObject(Constant.MONGO_LIMIT, limit);


        List<StudentErrorItemEntry.Item> returnList = new ArrayList<StudentErrorItemEntry.Item>();
        AggregationOutput output;
        try {
            output = aggregate(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, matchDBO, projectDBO, unbindDBO, matchDBO1, sortDBO, skipDBO, limitDBO);
            Iterator<DBObject> iter = output.results().iterator();
            BasicDBObject studenterrorInfo;
            BasicDBObject itemInfo;
            while (iter.hasNext()) {

                studenterrorInfo = (BasicDBObject) iter.next();
                itemInfo = (BasicDBObject) studenterrorInfo.get("items");
                returnList.add(new StudentErrorItemEntry.Item(itemInfo));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return returnList;
    }


    /**
     * 查找题目ids
     *
     * @param studentIds 学生ids
     * @param knowledges 知识点
     * @return
     */
    public List<ObjectId> getOriIds(List<ObjectId> studentIds, List<ObjectId> knowledges) {
        List<ObjectId> retList = new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject("ui", new BasicDBObject(Constant.MONGO_IN, studentIds));
        if (knowledges.size() > 0) {
            query.append("items.sc", new BasicDBObject(Constant.MONGO_IN, knowledges));
        }
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, new BasicDBObject("items.ori", 1).append("items.sc", 1));
        if (null != dboList) {
            for (DBObject dbo : dboList) {
                StudentErrorItemEntry e = new StudentErrorItemEntry((BasicDBObject) dbo);
                for (Item item : e.getItems()) {
                    if (knowledges.size() == 0 || knowledges.contains(item.getScope())) {
                        retList.add(item.getOriId());
                    }
                }
            }
        }
        return retList;
    }

    /**
     * 在特定学生中查找错某道题的学生
     *
     * @param studentIds
     * @param oriId
     * @return
     */
    public List<StudentErrorItemEntry> getErrorItemEntrys(List<ObjectId> studentIds, ObjectId oriId) {
        List<StudentErrorItemEntry> retList = new ArrayList<StudentErrorItemEntry>();
        BasicDBObject query = new BasicDBObject("ui", new BasicDBObject(Constant.MONGO_IN, studentIds)).append("items.ori", oriId);
        List<DBObject> dboList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STUDENT_ERROR_ITEM, query, new BasicDBObject("items", 1).append("ui", 1));
        if (null != dboList) {
            for (DBObject dbo : dboList) {
                StudentErrorItemEntry e = new StudentErrorItemEntry((BasicDBObject) dbo);
                retList.add(e);
            }
        }
        return retList;
    }


}
