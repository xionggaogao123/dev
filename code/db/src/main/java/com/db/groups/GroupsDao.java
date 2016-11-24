package com.db.groups;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.groups.*;
import com.sys.constants.Constant;

import com.sys.exceptions.ResultTooManyException;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


/**
 * 群组交流
 * Created by wang_xinxin on 2015/3/26.
 */
public class GroupsDao extends BaseDao {

    /**
     * 保存群组
     * @param groups
     * @return
     */
    public ObjectId addGroups(GroupsEntry groups) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME,groups.getBaseEntry());
        return groups.getID();
    }

    
    /**
     * 取单个群组
     * @param roomid
     * @param userid
     * @return
     */
    public GroupsEntry selGroups(String roomid,String userid) {
        BasicDBObject query =new BasicDBObject("rd",roomid);
        if (userid!=null) {
            query.append("ui",userid);
        }
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPS_NAME,query,null);
        if(null!=dbo)
        {
            return new GroupsEntry((BasicDBObject)dbo);
        }
        return null;
    }
    
    
   
    /**
     * 根据roomID 集合查询
     * @param roomids
     * @param fields
     * @return
     */
    public List<GroupsEntry> selGroupsEntryList(List<String> roomids, DBObject fields) {
        List<GroupsEntry> retList = new ArrayList<GroupsEntry>();
        BasicDBObject query = new BasicDBObject("rd",new BasicDBObject(Constant.MONGO_IN,roomids));
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, fields);
        if(null!=list)
        {
	        for(DBObject dbo:list)
	        {
	            retList.add(new GroupsEntry((BasicDBObject)dbo));
	        }
        }
        return retList;
    }

    /**
     * 更新群
     * @param groupsEntry
     */
    public void updateGroups(String roomid,GroupsEntry groupsEntry) {
        BasicDBObject query = new BasicDBObject("rd",roomid);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("gn",groupsEntry.getGroupname()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, updateValue);

    }


    /**
     *添加聊天记录
     * @param gc
     */
    public ObjectId addGroupsChat (GroupsChatEntry gc)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPSCHAT_NAME,gc.getBaseEntry());
        return gc.getID();
//    	DBObject query =new BasicDBObject(Constant.ID,id);
//    	DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("chatli",gc.getBaseEntry()));
//    	update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, updateValue);
    }

    /**
     * 更新app群名称
     * @param group
     */
    public void updateGroupName(String roomid,GroupsEntry group) {
        BasicDBObject query = new BasicDBObject("rd",roomid);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("gn",group.getGroupname()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, updateValue);
    }

    /**
     * 删除群成员
     * @param roomid
     * @param groupsUser
     */
    public void deleteGroupUser(String roomid,GroupsUser groupsUser) {
        DBObject query =new BasicDBObject("rd",roomid);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("guli",groupsUser.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, updateValue);
    }

    /**
     * 添加群成员
     * @param groupsEntry
     */
    public void addGroupUser(ObjectId id,GroupsEntry groupsEntry) {
        DBObject query =new BasicDBObject(Constant.ID,id);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("guli",groupsEntry.getGroupsUserList()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, updateValue);
    }

    /**
     *添加群成员
     * @param roomid
     * @param groupsUser
     */
    public void addGroupUser(String roomid,GroupsUser groupsUser) {
        DBObject query =new BasicDBObject("rd",roomid);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("guli",groupsUser.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, updateValue);
    }


    /**
     * 删除群组
     * @param roomid
     * @param userid
     */
    public void deleteGroup(String roomid,String userid) {
        DBObject query =new BasicDBObject("rd",roomid).append("ui", userid);
        DBObject value =new BasicDBObject("st", GroupState.DELETED.getState());
        BasicDBObject updateValue =new  BasicDBObject(Constant.MONGO_SET,value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPS_NAME, query, updateValue);

    }

    /**
     * 插入群文件
     * @param groupsFileEntry
     */
    public ObjectId insertGroupFile(GroupsFileEntry groupsFileEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPFILE_NAME, groupsFileEntry.getBaseEntry());
        return groupsFileEntry.getID();
    }

    /**
     * 单条文件
     * @param fileid
     * @return
     */
    public GroupsFileEntry selGroupFile(ObjectId fileid) {
        BasicDBObject query =new BasicDBObject(Constant.ID,fileid);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPFILE_NAME,query,null);
        if(null!=dbo)
        {
            return new GroupsFileEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 更新群文件数量
     * @param fileid
     */
    public void updateGroupFile(ObjectId fileid) {
        DBObject query =new BasicDBObject(Constant.ID,fileid);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("num",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPFILE_NAME, query, updateValue);

    }

    /**
     * 查询文件列表
     * @param roomid
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupsFileEntry> selGroupFileList(String roomid, int page, int pageSize) {
        List<GroupsFileEntry> retList = new ArrayList<GroupsFileEntry>();
        BasicDBObject query = new BasicDBObject("rd",roomid);
        query.append("st",0);
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPFILE_NAME, query, null, new BasicDBObject("ut",-1), page, pageSize);
        for(DBObject dbo:list)
        {
            retList.add(new GroupsFileEntry((BasicDBObject)dbo));
        }
       
        return retList;
    }
    
    
   


    /**
     * 删除群文件
     * @param id
     */
    public void deleteGroupFile(ObjectId id) {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("st",1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPFILE_NAME, query, updateValue);
//        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPFILE_NAME,query);
    }

    /**
     * 查询聊天记录
     * @param roomid
     * @param page
     * @param pageSize
     * @return
     */
    public List<GroupsChatEntry> selChatLogList(String roomid,String chatid,long chatdate, DBObject fields, int page, int pageSize) throws ResultTooManyException {
        List<GroupsChatEntry> retList = new ArrayList<GroupsChatEntry>();
        BasicDBObject query =new BasicDBObject("rd",roomid);
        List<DBObject> list =new ArrayList<DBObject>();
        if (!StringUtils.isEmpty(chatid)) {
            query.append("ct",new BasicDBObject("$lt",chatdate));
            list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPSCHAT_NAME, query, fields, new BasicDBObject("ct",-1),30);
        } else {
            list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPSCHAT_NAME, query, fields, new BasicDBObject("ct",-1), page, pageSize);
        }

        for(DBObject dbo:list)
        {
            retList.add(new GroupsChatEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /**
     * 获取聊天数量
     * @param roomid
     * @param chatid
     * @param chatdate
     * @return
     */
    public int selChatLogListCount(String roomid, String chatid,long chatdate) {
        BasicDBObject query =new BasicDBObject("rd",roomid);
        if (!StringUtils.isEmpty(chatid)) {
            query.append("ct",new BasicDBObject("$lt",chatdate));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPSCHAT_NAME,query);
    }

    /**
     * roomid获取群信息
     * @param roomid
     * @return
     */
    public GroupsEntry selectGroupByRoomid(String roomid) {
        BasicDBObject query =new BasicDBObject("rd",roomid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPS_NAME, query,null);
        if(null!=dbo)
        {
            return new GroupsEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 获取单个用户
     * @param groupid
     * @param userId
     * @return
     */
    public GroupsEntry selSingleRefGroupUser(ObjectId groupid, String userId) {
        BasicDBObject query =new BasicDBObject(Constant.ID,groupid).append("guli.gui",userId);
        DBObject dbo =  findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPS_NAME,query,new BasicDBObject("guli.$",1));
        if(null!=dbo)
        {
            return new GroupsEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 收到消息更新状态
     * @param refGroupUser
     * @param roomid
     */
    public void updateStatus(GroupsUser refGroupUser,String roomid) {
        BasicDBObject query =new BasicDBObject("rd",roomid).append("guli.gui",refGroupUser.getUserid());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("guli.$.ir",refGroupUser.getIsread()).append("guli.$.ct",refGroupUser.getUpdatetime()));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPS_NAME,query,updateValue);

    }

    public List<GroupsChatEntry> selChatList(ObjectId groupid, int type,DBObject fields,long time) {
        List<GroupsChatEntry> retList = new ArrayList<GroupsChatEntry>();
        BasicDBObject query =new BasicDBObject("gid",groupid);
        int pagesize = 0;
        if (type==1) {
            pagesize = 3;
        } else if (type==2) {
            query.append("ct",new BasicDBObject("$gte",time));
            pagesize = 30;
        }
        List<DBObject> list =new ArrayList<DBObject>();
        list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPSCHAT_NAME, query, fields, new BasicDBObject("ct",-1),pagesize);
        for(DBObject dbo:list)
        {
            retList.add(new GroupsChatEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    public int selNotViewChatCount(ObjectId groupid, long updatetime,int type) {
        BasicDBObject query =new BasicDBObject("gid",groupid);
        if (type == 1) {
            query.append("ct",new BasicDBObject("$gte",updatetime));
        }
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPSCHAT_NAME,query);
    }

    public GroupsChatEntry selGroupChat(String chatid) {
        BasicDBObject query =new BasicDBObject(Constant.ID,chatid);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_GROUPSCHAT_NAME, query, null);
        if(null!=dbo)
        {
            return new GroupsChatEntry((BasicDBObject)dbo);
        }

        return null;
    }

    public int selGroupFileCount(String roomid) {
        BasicDBObject query =new BasicDBObject("rd",roomid);
        query.append("st",0);
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_GROUPFILE_NAME,query);
    }
}


