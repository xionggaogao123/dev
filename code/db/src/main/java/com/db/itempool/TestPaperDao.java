package com.db.itempool;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.TestPaperEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 试卷操作类
 *
 * @author fourer
 */
public class TestPaperDao extends BaseDao {
    /**
     * 增加
     *
     * @param e
     * @return
     */
    public ObjectId addTestPaperEntry(TestPaperEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 推送到多个班级
     *
     * @param id
     * @param classes
     */
    public void addClass(ObjectId id, List<ObjectId> classes) {
        DBObject dbo = new BasicDBObject(Constant.ID, id);
        BasicDBObject operDBO = new BasicDBObject(Constant.MONGO_EACH, classes);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject("cls", operDBO))
                .append(Constant.MONGO_SET, new BasicDBObject("st", Constant.TWO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, dbo, updateValue);
    }

    /**
     * 更新
     *
     * @param id
     */
    public void update(ObjectId id, DBObject update) {
        DBObject dbo = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject().append(Constant.MONGO_SET, update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, dbo, updateValue);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public TestPaperEntry getTestPaperEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, query, Constant.FIELDS);
        if (null != dbo) {
            return new TestPaperEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 查询
     *
     * @param userId  必须参数
     * @param grade
     * @param subject
     * @param state
     * @param fields
     * @param skip
     * @param limit
     * @return
     */
    public List<TestPaperEntry> getTestPaperEntrys(ObjectId userId, int grade, int subject, int state, DBObject fields, int skip, int limit) {
        DBObject query = new BasicDBObject("ui", userId);
        if (grade > Constant.NEGATIVE_ONE) {
            query.put("gr", grade);
        }
        if (subject > Constant.NEGATIVE_ONE) {
            query.put("sty", subject);
        }
        if (state > Constant.NEGATIVE_ONE) {
            query.put("st", state);
        }
        List<TestPaperEntry> retList = new ArrayList<TestPaperEntry>(limit);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new TestPaperEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }


    /**
     * 删除
     *
     * @param id
     */
    public void delete(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, query);
    }


    /**
     * 删除试卷的某个题目
     *
     * @param paperId
     * @param type
     * @param id
     */
    public void deleteItem(ObjectId paperId, ExerciseItemType type, ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, paperId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject(type.getField(), id));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, query, updateValue);
    }


    /**
     * 修改试卷，添加一个题目
     *
     * @param paperId
     * @param type
     * @param id
     */
    public void addItem(ObjectId paperId, ExerciseItemType type, ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, paperId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_ADDTOSET, new BasicDBObject(type.getField(), id));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, query, updateValue);
    }


    /**
     * 更新题目，用于题目位置排序
     *
     * @param paperId
     * @param type
     * @param ids
     */
    public void updateItems(ObjectId paperId, ExerciseItemType type, List<ObjectId> ids) {
        DBObject query = new BasicDBObject(Constant.ID, paperId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(type.getField(), MongoUtils.convert(ids)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_PAPER, query, updateValue);
    }

}
