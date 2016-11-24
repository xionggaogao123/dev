package com.db.reminder;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.reminder.ReminderEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2015/9/25.
 */
public class ReminderDao extends BaseDao {

    /**
     * 新增一个提醒
     * @param reminderEntry
     * @return
     */
    public ObjectId addAReminder(ReminderEntry reminderEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, reminderEntry.getBaseEntry());
        return reminderEntry.getID();
    }

    /**
     * 查找提醒
     * @param
     * @return
     */
    public List<ReminderEntry> findReminderEntryList(ObjectId receiverId, ObjectId classId, ObjectId subjectId, ObjectId homeworkId){
        DBObject query = new BasicDBObject("rlist", receiverId);
        if(classId != null){
            query.put("cid", classId);
        }
        if(subjectId != subjectId){
            query.put("sjid", subjectId);
        }
        if(homeworkId != null){
            query.put("hwid", homeworkId);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty()) {
            List<ReminderEntry> reminderEntries = new ArrayList<ReminderEntry>();
            for(DBObject dbo:list)
            {
                ReminderEntry e = new ReminderEntry((BasicDBObject)dbo);
                reminderEntries.add(e);
            }
            return reminderEntries;
        }
        return null;
    }

    public ReminderEntry findReminderEntry(ObjectId senderId, ObjectId homeworkId){
        DBObject query = new BasicDBObject("hwid", homeworkId).append("sid", senderId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query, Constant.FIELDS);
        if(dbo != null){
            return new ReminderEntry((BasicDBObject)dbo);
        }
        return null;
    }


    /**
     * 找到未读的作业提醒
     * @param receiverId 必须
     * @param classId 不为null时有效
     * @param subjectId 不为null时有效
     * @param homeworkId 不为null时有效
     * @return
     */
    public int findHWReminderCount(ObjectId receiverId, ObjectId classId, ObjectId subjectId, ObjectId homeworkId){
        DBObject query = new BasicDBObject("rlist", receiverId);
        if(classId != null){
            query.put("cid", classId);
        }
        if(subjectId != null){
            query.put("sjid", subjectId);
        }
        if(homeworkId != null){
            query.put("hwid", homeworkId);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query);
    }

    /**
     * 删除已读作业提醒
     * @param classId
     * @param subjectId
     * @param homeworkId
     */
    public void removeReminder(ObjectId classId, ObjectId subjectId, ObjectId homeworkId){
        DBObject query = new BasicDBObject();
        if(classId != null){
            query.put("cid", classId);
        }
        if(subjectId != subjectId){
            query.put("sjid", subjectId);
        }
        if(homeworkId != null){
            query.put("hwid", homeworkId);
        }
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query);
    }

    /**
     * 删除已读作业提醒
     * @param reminderId
     */
    public void removeReminder(ObjectId reminderId){
        DBObject query = new BasicDBObject(Constant.ID, reminderId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query);
    }

    /**
     * 更新收件人列表
     * @param reminderId
     * @param receiverList
     */
    public void updateReceiverList(ObjectId reminderId, List<ObjectId> receiverList){
        DBObject query = new BasicDBObject(Constant.ID, reminderId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("rlist",receiverList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query, updateValue);
    }

    /**
     * 更新提醒
     * @param reminderEntry
     */
    public void update(ObjectId reminderId, ReminderEntry reminderEntry){
        DBObject query = new BasicDBObject(Constant.ID, reminderId);
        DBObject updateValue  = new BasicDBObject(Constant.MONGO_SET,reminderEntry.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query, updateValue);
    }

    //========================脚本使用========================
    public List<ReminderEntry> findAllReminder(){
        DBObject query = new BasicDBObject("ver", 1);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_REMIDNER_NAME, query, Constant.FIELDS);
        if(null!=list && !list.isEmpty()) {
            List<ReminderEntry> reminderEntries = new ArrayList<ReminderEntry>();
            for(DBObject dbo:list)
            {
                ReminderEntry e = new ReminderEntry((BasicDBObject)dbo);
                reminderEntries.add(e);
            }
            return reminderEntries;
        }
        return null;
    }
}
