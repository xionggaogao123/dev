package com.pojo.reminder;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**提醒类
 * sid:发提醒人ID
 * rid: List<ObjectId> 接收提醒的人ID
 * ty：类型  1：作业
 * ver: 版本号 目前为1
 * 其他信息===根据类型自定义
 * ===作业的其他信息
 * cid: 班级ID
 * sjid：科目ID
 * hwid：作业ID
 * ===
 * Created by fl on 2015/9/25.
 */
public class ReminderEntry  extends BaseDBObject {
    int ver = 1;

    public ReminderEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    /**
     * 用于作业类提醒
     * @param senderId
     * @param receiverList
     * @param classList
     * @param subjectList
     * @param hwId
     */
    public ReminderEntry(ObjectId senderId, List<ObjectId> receiverList, List<ObjectId> classList, List<ObjectId> subjectList, ObjectId hwId){
        BasicDBObject dbo =new BasicDBObject()
                .append("sid", senderId)
                .append("rlist", MongoUtils.convert(receiverList))
                .append("ty", 1)
                .append("cid", MongoUtils.convert(classList))
                .append("sjid", MongoUtils.convert(subjectList))
                .append("hwid", hwId)
                .append("ver", ver);
        setBaseEntry(dbo);
    }

    public void setSenderId(ObjectId senderId){
        setSimpleValue("sid", senderId);
    }

    public ObjectId getSenderId(){
        return  getSimpleObjecIDValue("sid");
    }

    public void setHomeworkId(ObjectId senderId){
        setSimpleValue("hwid", senderId);
    }

    public ObjectId getHomeworkId(){
        return  getSimpleObjecIDValue("hwid");
    }

    public List<ObjectId> getReceiverList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("rlist");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setReceiverList(List<ObjectId> receiverList) {
        setSimpleValue("rList", MongoUtils.convert(receiverList));
    }

    public List<ObjectId> getClassList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("cid");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setClassList(List<ObjectId> classList) {
        setSimpleValue("cid", MongoUtils.convert(classList));
    }

    public List<ObjectId> getSubjectList() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("sjid");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setSubjectList(List<ObjectId> subjectList) {
        setSimpleValue("sjid", MongoUtils.convert(subjectList));
    }

}
