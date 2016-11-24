package com.db.meeting;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.lesson.LessonWare;
import com.pojo.meeting.MeetLogEntry;
import com.pojo.meeting.MeetingEntry;
import com.pojo.meeting.MessageEntry;
import com.pojo.meeting.VoteEntry;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/7/29.
 */
public class MeetingDao extends BaseDao {
    /**
     * 
     * @param entry
     * @return
     */
    public ObjectId addMeetingInfo(MeetingEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     *
     * @param id
     * @return
     */
    public MeetingEntry selMeetingDetail(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        query.append("ir", DeleteState.NORMAL.getState());
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_MEETING_INFO,query,null);
        if(null!=dbo)
        {
            return new MeetingEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param schoolId
     * @param userId
     * @return
     */
    public List<MeetingEntry> selModelList(ObjectId schoolId, ObjectId userId,DBObject fields) {
        List<MeetingEntry> retList =new ArrayList<MeetingEntry>();
        BasicDBObject query =new BasicDBObject("si",schoolId).append("ui",userId).append("modeltp",1);
        query.append("ir", DeleteState.NORMAL.getState());
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, fields,new BasicDBObject("_id",-1));
        if(null!=list)
        {
            for(DBObject dbo:list)
            {
                retList.add(new MeetingEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     *
     * @param userId
     * @param fields
     * @return
     */
    public List<MeetingEntry> selMyMeetingList(long stime,long etime,int type,String keyword,ObjectId userId,int index, DBObject fields) {
        List<MeetingEntry> retList =new ArrayList<MeetingEntry>();
        BasicDBObject query =new BasicDBObject("modeltp",0).append("ir", DeleteState.NORMAL.getState());
        if (index==0) {
            query.append("apty", new BasicDBObject(Constant.MONGO_NE, 2));
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("ui", userId));
            values.add(new BasicDBObject("users", userId));
            query.put(Constant.MONGO_OR, values);
            if (type!=0) {
                if (type==1) {
                    query.append("st", type);
                } else if (type==2) {
                    BasicDBList value3 = new BasicDBList();
                    value3.add(new BasicDBObject("st", new BasicDBObject(Constant.MONGO_NE,1)));
                    value3.add(new BasicDBObject("ot", new BasicDBObject(Constant.MONGO_GT, System.currentTimeMillis())));
                    query.put(Constant.MONGO_AND, value3);
                } else if (type==3) {
                    BasicDBList condList = new BasicDBList();
                    BasicDBObject queryChild2 =new BasicDBObject();
                    queryChild2.append("st", new BasicDBObject(Constant.MONGO_NE, 1));
                    queryChild2.append("ot", new BasicDBObject(Constant.MONGO_LTE, System.currentTimeMillis()));
                    queryChild2.append("et", new BasicDBObject(Constant.MONGO_GTE, System.currentTimeMillis()));
                    condList.add(queryChild2);
                    condList.add(new BasicDBObject("st",type));
                    query.put(Constant.MONGO_OR, condList);
                } else if (type==4) {
                    BasicDBList condList = new BasicDBList();
                    BasicDBObject queryChild3 =new BasicDBObject();
                    queryChild3.append("st", new BasicDBObject(Constant.MONGO_NE,1));
                    queryChild3.append("et", new BasicDBObject(Constant.MONGO_LT, System.currentTimeMillis()));
                    condList.add(queryChild3);
                    condList.add(new BasicDBObject("st",type));
                    query.put(Constant.MONGO_OR, condList);
                }
            }
        } else {
            query.append("apui",userId.toString());
            if (type!=3) {
                query.append("apty", type);
            }

        }

        if(!StringUtils.isEmpty(keyword)) {
            BasicDBList value2 = new BasicDBList();
            value2.add(new BasicDBObject("nm", MongoUtils.buildRegex(keyword)));
            value2.add(new BasicDBObject("issue", MongoUtils.buildRegex(keyword)));
            query.put(Constant.MONGO_OR, value2);
        }
        BasicDBList dblist =new BasicDBList();
        if(stime>0){
            dblist.add(new BasicDBObject("ot", new BasicDBObject(Constant.MONGO_GTE, stime)));
        }
        if(etime>0) {
            dblist.add(new BasicDBObject("ot", new BasicDBObject(Constant.MONGO_LTE, etime)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, fields,new BasicDBObject("_id",-1));
        if(null!=list)
        {
            for(DBObject dbo:list)
            {
                retList.add(new MeetingEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     *
     * @param id
     */
    public void delMeetingInfo(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject value =new BasicDBObject("ir", DeleteState.DELETED.getState());
        BasicDBObject updateValue =new  BasicDBObject(Constant.MONGO_SET,value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param id
     * @param modelName
     */
    public void updateMeetModel(ObjectId id, String modelName) {
        DBObject query =new BasicDBObject(Constant.ID,id).append("ir", DeleteState.NORMAL.getState());
        DBObject value =new BasicDBObject("mnm", modelName);
        BasicDBObject updateValue =new  BasicDBObject(Constant.MONGO_SET,value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param id
     * @param userId
     */
    public void checkInMeeting(ObjectId id, ObjectId userId) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("cinus",new IdValuePair(userId,System.currentTimeMillis()).getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param voteEntry
     */
    public ObjectId addVoteIfo(VoteEntry voteEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_VOTE_INFO, voteEntry.getBaseEntry());
        return voteEntry.getID();

    }

    /**
     *
     * @param id
     */
    public void removeVoteInfo(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_VOTE_INFO,query);
    }

    /**
     *
     * @param id
     * @return
     */
    public VoteEntry selVoteInfo(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_MEETING_VOTE_INFO,query,null);
        if(null!=dbo)
        {
            return new VoteEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public List<VoteEntry> selVoteList(ObjectId id) {
        List<VoteEntry> retList =new ArrayList<VoteEntry>();
        BasicDBObject query =new BasicDBObject("mid",id);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_VOTE_INFO, query, null,new BasicDBObject("_id",-1));
        if(null!=list)
        {
            for(DBObject dbo:list)
            {
                retList.add(new VoteEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     *
     * @param voteId
     * @param chooseId
     * @param userId
     */
    public void updateVoteInfo(ObjectId voteId, ObjectId chooseId, ObjectId userId) {
        DBObject query =new BasicDBObject(Constant.ID,voteId).append("csl.id",chooseId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("csl.$.uis",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_VOTE_INFO, query, updateValue);
    }

    /**
     * 审核
     * @param id
     * @param userId
     * @param remark
     * @param flag
     */
    public void submitShenHe(ObjectId id, ObjectId userId, String appUserId, String remark, int flag) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject update=new BasicDBObject("apty",flag==0?1:2).append("st",2);
        if (!StringUtils.isEmpty(appUserId)) {
            update.append("apui",appUserId);
        }
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("apuis",new IdValuePair(userId,remark).getBaseEntry())).append(Constant.MONGO_SET,update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param entry
     * @param id
     */
    public void updateMeetingInfo(MeetingEntry entry, String id) {
        DBObject query =new BasicDBObject(Constant.ID,new ObjectId(id));
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("nm",entry.getName()).append("ty",entry.getType())
        .append("ot",entry.getOpenTime()).append("et",entry.getEndTime()).append("cause",entry.getCause()).append("pres",entry.getProcess())
        .append("order",entry.getOrder()).append("issue",entry.getIssue()).append("apui",entry.getApprovalUserId()).append("st",entry.getStatus()).append("users", entry.getUserIds()).append("dcl",entry.getLessonWareDBOList()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param id
     * @param lessonWare
     */
    public void updateMeetFile(ObjectId id, LessonWare lessonWare) {
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("dcl",lessonWare.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param id
     * @param userId
     */
    public void checkOutMeeting(ObjectId id, ObjectId userId) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("conus",new IdValuePair(userId,System.currentTimeMillis()).getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param entry
     * @return
     */
    public ObjectId endMeetingInfo(MeetLogEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_LOG_INFO, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     *
     * @param id
     */
    public void updateMeetingStatus(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st",4));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     *
     * @param stime
     * @param etime
     * @param keyword
     * @param userId
     * @param fields
     * @return
     */
    public List<MeetLogEntry> selMeetLogList(long stime, long etime, String keyword, ObjectId userId, DBObject fields) {
        List<MeetLogEntry> retList =new ArrayList<MeetLogEntry>();
        BasicDBObject query =new BasicDBObject("ir", DeleteState.NORMAL.getState()).append("ui",userId);
        BasicDBList dblist =new BasicDBList();
        if(stime>0){
            dblist.add(new BasicDBObject("ot", new BasicDBObject(Constant.MONGO_GTE, stime)));
        }
        if(etime>0) {
            dblist.add(new BasicDBObject("ot", new BasicDBObject(Constant.MONGO_LTE, etime)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        if(!StringUtils.isEmpty(keyword)) {
            BasicDBList value2 = new BasicDBList();
            value2.add(new BasicDBObject("nm", MongoUtils.buildRegex(keyword)));
            value2.add(new BasicDBObject("issue", MongoUtils.buildRegex(keyword)));
            query.put(Constant.MONGO_OR, value2);
        }
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_LOG_INFO, query, fields,new BasicDBObject("_id",-1));
        if(null!=list)
        {
            for(DBObject dbo:list)
            {
                retList.add(new MeetLogEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     *
     * @param id
     */
    public void delMeetLog(ObjectId id) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_LOG_INFO, query, updateValue);
    }

    /**
     *
     * @param entry
     */
    public ObjectId addMessage(MessageEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_MESSAGE_INFO, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     *
     * @param meetId
     * @return
     */
    public List<MessageEntry> selChatLogList(ObjectId meetId) {
        List<MessageEntry> retList =new ArrayList<MessageEntry>();
        BasicDBObject query =new BasicDBObject("mid",meetId);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_MESSAGE_INFO, query, null,Constant.MONGO_SORTBY_ASC);
        if(null!=list)
        {
            for(DBObject dbo:list)
            {
                retList.add(new MessageEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    /**
     *添加议题
     * @param id
     * @param content
     */
    public void addIssueList(ObjectId id, String content) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("issues",new IdValuePair(new ObjectId(),content).getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, updateValue);
    }

    /**
     * 删除议题
     * @param meetId
     * @param issueId
     */
    public void delIssueInfo(ObjectId meetId,ObjectId issueId) {
        BasicDBObject query=new BasicDBObject(Constant.ID,meetId);
        BasicDBObject update=new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("issues",new BasicDBObject("id",issueId)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query, update);
    }

    /**
     * 检查模板名字是否重复
     * @param modelName
     * @param id
     * @return
     */
    public int checkModelNameCount(String modelName, ObjectId id) {
        BasicDBObject query=new BasicDBObject("mnm",modelName).append("ir", DeleteState.NORMAL.getState());
        if (id!=null) {
            query.append(Constant.ID,id);
        }
        return  count(MongoFacroty.getAppDB(), Constant.COLLECTION_MEETING_INFO, query);
    }
}
