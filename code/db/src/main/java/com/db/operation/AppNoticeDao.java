package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by scott on 2017/9/22.
 */
public class AppNoticeDao extends BaseDao{

    /**
     * 保存
     * @param entry
     */
    public ObjectId saveAppNoticeEntry(AppNoticeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,entry.getBaseEntry());
        return entry.getID();
    }

    public void removeAppNoticeEntry(ObjectId noticeId){
        BasicDBObject query=new BasicDBObject(Constant.ID,noticeId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ir",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,updateValue);
    }

    /**
     * 保存几条信息
     * @param entries
     */
    public void saveAppNoticeEntries(List<AppNoticeEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE, MongoUtils.fetchDBObjectList(entries));
    }


    /**
     * 查询我已发送的通知
     * @param userId
     * @return
     */
    public List<AppNoticeEntry> getMySendAppNoticeEntries(ObjectId userId,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    public int countMySendAppNoticeEntries(ObjectId userId){
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query);
    }


    public int countMyReceivedAppNoticeEntries(List<ObjectId> groupIds, ObjectId userId){
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getMyReceivedAppNoticeQueryCondition(groupIds, userId));
    }


    public BasicDBObject getMyReceivedAppNoticeQueryCondition(List<ObjectId> groupIds, ObjectId userId){
        List<Integer> watchPermissions=new ArrayList<Integer>();
        watchPermissions.add(Constant.ONE);
        watchPermissions.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions))
                .append("ir",Constant.ZERO);
        return query;
    }

    /**
     * 获取我接收到的通知
     * @param groupIds
     * @return
     */
    public List<AppNoticeEntry> getMyReceivedAppNoticeEntries(List<ObjectId> groupIds,int page,int pageSize,
            ObjectId userId){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getMyReceivedAppNoticeQueryCondition(groupIds, userId),
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }
    /**
     * 根据idlist查询通知信息
     * @return
     */
    public List<AppNoticeEntry> getAppNoticeEntriesByIds(List<ObjectId> ids){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids))
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 已阅读
     * @param userId
     * @param id
     */
    public void pushReadList(ObjectId userId,ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("rl",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,updateValue);
    }

    public void updateCommentCount(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("cc",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,updateValue);
    }


    /**
     * 查询entry
     * @param id
     */
    public AppNoticeEntry getAppNoticeEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppNoticeEntry(dbObject);
        }else{
            return null;
        }
    }
    public void updEntry(AppNoticeEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE, query,updateValue);
    }

    public int countMyReceivedAppNoticeEntriesForStudent(List<ObjectId> groupIds){
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getQueryMyReceivedAppNoticeEntriesForStudentCondition(groupIds));
    }

    public BasicDBObject getQueryMyReceivedAppNoticeEntriesForStudentCondition(List<ObjectId> groupIds){
        List<Integer> watchPermissions=new ArrayList<Integer>();
        watchPermissions.add(Constant.TWO);
        watchPermissions.add(Constant.THREE);
        BasicDBObject query=new BasicDBObject()
                .append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("wp",new BasicDBObject(Constant.MONGO_IN,watchPermissions))
                .append("ir",Constant.ZERO);
        return query;
    }


    public List<AppNoticeEntry> getMyReceivedAppNoticeEntriesForStudent(List<ObjectId> groupIds,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();

        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,
                getQueryMyReceivedAppNoticeEntriesForStudentCondition(groupIds),
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }



    public List<AppNoticeEntry> searchAppNotice(String keyWord,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
        BasicDBList list = new BasicDBList();
        if(StringUtils.isNotBlank(keyWord)){
            Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE);
            list.add(new BasicDBObject().append("gn", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            list.add(new BasicDBObject().append("un", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
            list.add(new BasicDBObject().append("su", new BasicDBObject(Constant.MONGO_REGEX, pattern)));
        }
        query.append(Constant.MONGO_OR, list);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }


}
