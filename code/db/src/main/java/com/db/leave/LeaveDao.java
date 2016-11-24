package com.db.leave;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.leave.LeaveEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2016/3/1.
 */
public class LeaveDao extends BaseDao {
    //添加请假
    public void addTeacherLeave(LeaveEntry leaveEntry)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_LEAVE,leaveEntry.getBaseEntry());
    }
    //删除个人请假（未审核以及审核未通过的）
    public void removeMyLeave(ObjectId leaveId)
    {
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,leaveId)
                .append("dl",0);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("dl",1));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_TEACHER_LEAVE,query,updateValue);
    }

    //根据id查询
    public LeaveEntry findLeaveById(ObjectId id)
    {
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id)
                .append("dl",0);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_LEAVE, query, new BasicDBObject());
        if(dbObject==null)
            return null;
        LeaveEntry leaveEntry=new LeaveEntry((BasicDBObject)dbObject);
        return leaveEntry;
    }
    //根据申请老师id查询
    public List<LeaveEntry> findLeaveListByTeacherId(ObjectId teacherId,int page,int pageSize)
    {
        BasicDBObject query=new BasicDBObject()
                .append("tea",teacherId)
                .append("dl", 0);
        int skip=pageSize*(page-1);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_TEACHER_LEAVE,query,new BasicDBObject(),new BasicDBObject(Constant.ID,-1),skip,pageSize);
        List<LeaveEntry> list=new ArrayList<LeaveEntry>();
        if(dbObjectList!=null && !dbObjectList.isEmpty());
        for (DBObject dbObject:dbObjectList)
        {
            list.add(new LeaveEntry((BasicDBObject)dbObject));
        }
        return list;
    }
    //统计个人请假数量
    public int countMyLeave(ObjectId userId)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("tea",userId)
                .append("dl", 0);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_TEACHER_LEAVE,query);
    }
    //获取全校的请假申请
    public List<LeaveEntry> findAllLeaves(ObjectId schoolId,ObjectId teacherId,long dt1,long dt2,int type,int page,int pageSize,String term)
    {
        List<LeaveEntry> leaveEntries=new ArrayList<LeaveEntry>();
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("dt",new BasicDBObject(Constant.MONGO_GTE, dt1)));
        dblist.add(new BasicDBObject("dt",new BasicDBObject(Constant.MONGO_LTE,dt2)));
        BasicDBObject query = new BasicDBObject();
        if(dt1!=1234567890L) {
            query = new BasicDBObject()
                    .append("si", schoolId)
                    .append(Constant.MONGO_AND, dblist)
                    .append("dl", 0);
        }
        else
        {
            query = new BasicDBObject()
                    .append("si", schoolId)
                    .append("te", term)
                    .append("dl", 0);
        }
        if(teacherId!=null)
            query.append("tea",teacherId);
        if(type!=-1)
        {
            query.append("re",type);
        }
        int skip=(page-1)*pageSize;
        List<DBObject> leaveList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_TEACHER_LEAVE,query,new BasicDBObject(),new BasicDBObject(Constant.ID,-1),skip,pageSize);
        if(leaveList!=null && !leaveList.isEmpty())
        {
            for (DBObject dbObject:leaveList)
            {
                leaveEntries.add(new LeaveEntry((BasicDBObject)dbObject));
            }
        }
        return leaveEntries;
    }
    //统计全校的请假申请数量
    public int countAllLeave(ObjectId schoolId,ObjectId teacherId,long dt1,long dt2,int type,String term)
    {
        BasicDBList dblist =new BasicDBList();
        dblist.add(new BasicDBObject("dt",new BasicDBObject(Constant.MONGO_GTE, dt1)));
        dblist.add(new BasicDBObject("dt",new BasicDBObject(Constant.MONGO_LTE,dt2)));
        BasicDBObject query = new BasicDBObject();
        if(dt1!=1234567890L) {
            query = new BasicDBObject()
                    .append("si", schoolId)
                    .append(Constant.MONGO_AND, dblist)
                    .append("dl", 0);
        }
        else
        {
            query = new BasicDBObject()
                    .append("si", schoolId)
                    .append("te", term)
                    .append("dl", 0);
        }
        if(teacherId!=null)
            query.append("tea",teacherId);
        if(type!=-1)
        {
            query.append("re",type);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_LEAVE, query);
    }
    //管理员审核
    public void updateLeave(ObjectId leaveId,int replyType,ObjectId replyPersonId)
    {
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,leaveId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("re",replyType).append("rp",replyPersonId));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_TEACHER_LEAVE,query,updateValue);
    }

    public void removeByTermAndSchool(String term,ObjectId schoolId)
    {
        BasicDBObject query=new BasicDBObject()
                .append("te",term)
                .append("si",schoolId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("dl",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_TEACHER_LEAVE, query, updateValue);
    }
}
