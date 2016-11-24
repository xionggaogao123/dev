package com.db.letter;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.letter.LetterRecordEntry;
import com.sys.constants.Constant;


/**
 * 信件记录操作类
 * index:ui_lui
 * {"ui":1,"lui":1}
 *
 * @author fourer
 */
public class LetterRecordDao extends BaseDao {


    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addLetterRecordEntry(LetterRecordEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除
     *
     * @param e
     * @return
     */
    public void removeLetterRecordEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query);

    }

    /**
     * 详情
     *
     * @param ui
     * @param letterFriendUi
     * @return
     */
    public LetterRecordEntry getLetterRecordEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new LetterRecordEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 检查是否存在
     *
     * @param ui
     * @param letterFriendUi
     * @return
     */
    public LetterRecordEntry getLetterRecordEntry(ObjectId ui, ObjectId letterFriendUi) {

        BasicDBList list = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject("ui", ui).append("lui", letterFriendUi);
        BasicDBObject query2 = new BasicDBObject("ui", letterFriendUi).append("lui", ui);
        list.add(query1);
        list.add(query2);

        BasicDBObject query = new BasicDBObject(Constant.MONGO_OR, list);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query, Constant.FIELDS);
        if (null != dbo) {
            return new LetterRecordEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 更新信件状态；
     * 用户两个人已经有信件记录，再次联系时调用；
     *
     * @param id
     * @param letterID
     * @param field
     */
    public void update(ObjectId id, ObjectId letterID, String field) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("us.id", letterID).append("lus.id", letterID).append(field + ".ur", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query, updateValue);
    }

    /**
     * 更新信件状态；
     * 用户两个人已经有信件记录，再次联系时调用；
     *
     * @param id
     * @param letterID
     * @param field
     */
    public void update(ObjectId id, ObjectId letterID) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("us.id", letterID).append("lus.id", letterID));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query, updateValue);
    }

    /**
     * 阅读一个信件时调用
     *
     * @param letterId
     * @param field
     */
    public void readLetter(ObjectId id, String field) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(field + ".ur", Constant.ZERO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query, updateValue);
    }


    /**
     * 删除信件
     *
     * @param id
     * @param removeLetterId
     * @param secondNewLetterId
     * @param field
     * @param isUpdateRecent
     */
    public void removeLetter(ObjectId id, ObjectId removeLetterId, ObjectId secondNewLetterId, String field, boolean isUpdateRecent) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject value = new BasicDBObject(field + ".ur", Constant.ZERO);
        if (isUpdateRecent) {
            value.append(field + ".id", secondNewLetterId);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query, updateValue);
    }

    /**
     * 查询
     *
     * @param ui
     * @param skip
     * @param limit
     * @return
     */
    public List<LetterRecordEntry> getList(ObjectId ui, int skip, int limit) {
        List<LetterRecordEntry> retlist = new ArrayList<LetterRecordEntry>();

        BasicDBList queryList = new BasicDBList();
        DBObject query1 = new BasicDBObject("ui", ui);
        DBObject query2 = new BasicDBObject().append("lui", ui);
        queryList.add(query1);
        queryList.add(query2);
        BasicDBObject query = new BasicDBObject(Constant.MONGO_OR, queryList);
        BasicDBObject sortQuery = new BasicDBObject("us.id", -1).append("uis.id", -1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query, Constant.FIELDS, sortQuery, skip, limit);
        if (null != list && list.size() > 0) {
            for (DBObject dbo : list) {
                retlist.add(new LetterRecordEntry((BasicDBObject) dbo));
            }
        }
        return retlist;
    }


    public int countList(ObjectId ui) {

        BasicDBList queryList = new BasicDBList();
        DBObject query1 = new BasicDBObject("ui", ui);
        DBObject query2 = new BasicDBObject().append("lui", ui);
        queryList.add(query1);
        queryList.add(query2);
        BasicDBObject query = new BasicDBObject(Constant.MONGO_OR, queryList);

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_RECORD_NAME, query);
    }
}
