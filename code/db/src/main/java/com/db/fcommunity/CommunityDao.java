package com.db.fcommunity;

import com.db.base.BaseDao;
import com.db.controlphone.ControlVersionDao;
import com.db.factory.MongoFacroty;
import com.mongodb.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MineCommunityEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by jerry on 2016/10/24.
 * 社区Entry Dao层
 */
public class CommunityDao extends BaseDao {

    /**
     * 保存社区
     *
     * @param entry
     * @return 返回保存是否成功
     */
    public void save(CommunityEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, entry.getBaseEntry());
    }

    /**
     * 返回社区Entry
     *
     * @param id
     * @return
     */
    public CommunityEntry findByObjectId(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    public CommunityEntry findCommunityByObjectId(ObjectId groupId) {
        BasicDBObject query = new BasicDBObject("grid", groupId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    public List<CommunityEntry> findByObjectIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }
    
    public List<CommunityEntry> findByObjectIds(String title, List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        if (StringUtils.isNotBlank(title)) {
            if(StringUtils.isNotBlank(title)){
                Pattern pattern = Pattern.compile("^.*" + title + ".*$", Pattern.CASE_INSENSITIVE);
                query.append("cmmn",new BasicDBObject(Constant.MONGO_REGEX, pattern));
            }
        }
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }

    public Map<ObjectId,CommunityEntry> findMapByObjectIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        Map<ObjectId,CommunityEntry> communitys = new HashMap<ObjectId, CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            CommunityEntry communityEntry = new CommunityEntry(dbo);
            communitys.put(communityEntry.getID(), communityEntry);
        }
        return communitys;
    }

    public String findStringByObjectIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        StringBuffer sb = new StringBuffer();
        for (DBObject dbo : dbObjects) {
            sb.append(new CommunityEntry(dbo).getCommunityName());
            sb.append("、");
        }
        sb.substring(0,sb.length()-1);
        return sb.toString();
    }

    public List<CommunityEntry> findByNotObjectIds(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids)).append("r",0);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }

    /**
     * 返回Map<userId,List<communityId>>
     * @param userIds
     * @param allMineCommunitys
     * @return
     */
    public Map<ObjectId,List<ObjectId>> findCommunityIdsGroupByUserId(List<ObjectId> userIds, Map<ObjectId,List<ObjectId>> allMineCommunityIds) {
        //封装in 的条件
        List<ObjectId> mineCommunityIds = new ArrayList<ObjectId>();
        for (ObjectId userId:userIds){
            mineCommunityIds.addAll((ArrayList)allMineCommunityIds.get(userId));
        }

        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,mineCommunityIds)).append("r",0);
        //排除某些不展示的社群
        List<String> notShowCommunityNames = new ArrayList<String>();
        notShowCommunityNames.add("复兰社区");
        notShowCommunityNames.add("复兰大学");
        query.append("cmmn",  new BasicDBObject(Constant.MONGO_NOTIN, notShowCommunityNames));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        //开始封装返回数据
        Map<ObjectId,List<ObjectId>> result = new HashMap<ObjectId, List<ObjectId>>();
        for (ObjectId userId:userIds){
            List<ObjectId> communityIds = new ArrayList<ObjectId>();
            for (ObjectId communityId:(ArrayList<ObjectId>)allMineCommunityIds.get(userId)){
                for (CommunityEntry entry : communitys){
                    if (communityId.equals(entry.getID())){//communityId 在COLLECTION_FORUM_COMMUNITY表里r是0的
                        communityIds.add(communityId);
                    }
                }
            }
            result.put(userId,communityIds);
        }
        return result;
    }

    public List<CommunityEntry> findPageByObjectIds(List<ObjectId> ids,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }

    public List<CommunityEntry> findPageByObjectIdsName(List<ObjectId> ids,String communityName,String gradeVal, int page,int pageSize) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        if(!"".equals(communityName)){
            query.append("cmmn",communityName);
        }
        if(!"".equals(gradeVal)){
            query.append("gradeVal",gradeVal);
        }
        query.append("r",Constant.ZERO);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS,
                Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }

    public int getNumber(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_FORUM_COMMUNITY,
                        query);
        return count;
    }

    /**
     * 返回社区Entry
     *
     * @param searchId 搜索id
     * @return
     */
    public CommunityEntry findBySearchId(String searchId) {
        BasicDBObject query = new BasicDBObject("cmid", searchId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    /**
     * 返回社区Entry
     *
     * @param emChatId 环信id
     * @return
     */
    public CommunityEntry findByEmChatId(String emChatId) {
        BasicDBObject query = new BasicDBObject("emid", emChatId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }

    /**
     * 名称精确查找
     *
     * @param communityName
     * @return
     */
    public CommunityEntry findByName(String communityName) {
        BasicDBObject query = new BasicDBObject("cmmn", communityName);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }
    
    /**
     * 查找社群数量
     *
     * @param communityName
     * @return
     */
    public Integer countCommunities() {
        BasicDBObject query = new BasicDBObject("r", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
    }

    /**
     * 社团名称:正则查找
     *
     * @param regular
     * @return
     */
    public List<CommunityEntry> findByRegularName(String regular) {
        BasicDBObject query = new BasicDBObject("cmmn", MongoUtils.buildRegex(regular));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }


    public List<CommunityEntry> getCommunities(int page,int pageSize){
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_FORUM_COMMUNITY,
                new BasicDBObject(),Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        List<CommunityEntry> communitys = new ArrayList<CommunityEntry>();
        for (DBObject dbo : dbObjects) {
            communitys.add(new CommunityEntry(dbo));
        }
        return communitys;
    }

    /**
     * 获取 - 公开群
     *
     * @return
     */
    public List<CommunityEntry> getOpenCommunitys(int page, int pageSize, String lastId) {
        List<CommunityEntry> retList = new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject().append("op", Constant.ONE);
        DB myMongo = MongoFacroty.getAppDB();
        DBCollection communityCollection = myMongo.getCollection(Constant.COLLECTION_FORUM_COMMUNITY);
        DBCursor limit;
        if (page == 1) {
            limit = communityCollection.find(query)
                    .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
        } else {
            if (StringUtils.isNotBlank(lastId)) {
                limit = communityCollection
                        .find(new BasicDBObject(Constant.ID, new BasicDBObject(
                                Constant.MONGO_GT, new ObjectId(lastId))))
                        .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
            } else {
                limit = communityCollection.find(query).skip((page - 1) * pageSize)
                        .sort(Constant.MONGO_SORTBY_ASC).limit(pageSize);
            }
        }

        while (limit.hasNext()) {
            retList.add(new CommunityEntry(limit.next()));
        }
        return retList;
    }

    /**
     * 更改 - 社区名称
     *
     * @param communityId
     * @param name
     */
    public void updateCommunityName(ObjectId communityId, String name) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cmmn", name));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    /**
     * 更改 -社区公开状态
     *
     * @param communityId
     * @param open
     */
    public void updateCommunityOpenStatus(ObjectId communityId, int open) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("op", open));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }


    /**
     * 更改 - 社区描述
     *
     * @param communityId
     * @param desc
     */
    public void updateCommunityDesc(ObjectId communityId, String desc) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cmde", desc));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    /**
     * 更改logo
     *
     * @param communityId
     * @param logo
     */
    public void updateCommunityLogo(ObjectId communityId, String logo) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cmlg", logo));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    /**
     * 获取 - 社区的群组 id
     *
     * @param communityId
     * @return
     */
    public ObjectId getGroupId(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject field = new BasicDBObject().append("grid", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, field);
        return dbo == null ? null : (ObjectId) dbo.get("grid");
    }
    
    /**
     * 
     *〈简述〉社区名称
     *〈详细描述〉
     * @author Administrator
     * @param communityId
     * @return
     */
    public String getCommunityName(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        //BasicDBObject field = new BasicDBObject().append("cmmn", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        return dbo == null ? null : (String) dbo.get("cmmn");
    }
    
    public CommunityEntry getCommunity(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        //BasicDBObject field = new BasicDBObject().append("cmmn", 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        return new CommunityEntry(dbo);
    }


    public void transferOwner(ObjectId communityId,ObjectId ownerId){
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("cmow", ownerId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,updateValue);
    }

    public ObjectId getCommunityIdByGroupId(ObjectId groupId){
        BasicDBObject query = new BasicDBObject().append("grid", groupId);
        BasicDBObject field = new BasicDBObject().append(Constant.ID, 1);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, field);
        return dbo == null ? null : (ObjectId) dbo.get(Constant.ID);
    }


    /**
     * 通过群组Ids列表查询社区列表
     * @param groupIds
     * @return
     */
    public List<CommunityEntry> getCommunityEntriesByGroupIds(List<ObjectId> groupIds){
        List<CommunityEntry> entries=new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject().append("grid", new BasicDBObject(Constant.MONGO_IN,groupIds));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommunityEntry(dbObject));
            }
        }
        return entries;
    }
    
    /**
     * 通过群组Ids列表查询社区列表
     * @param groupIds
     * @return
     */
    public List<CommunityEntry> getCommunityEntriesByGroupIds(String title, List<ObjectId> groupIds){
        List<CommunityEntry> entries=new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject().append("grid", new BasicDBObject(Constant.MONGO_IN,groupIds));
   
        if(StringUtils.isNotBlank(title)){
            Pattern pattern = Pattern.compile("^.*" + title + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("cmmn",new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
      
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommunityEntry(dbObject));
            }
        }
        return entries;
    }

    /**
     * 通过群组Ids列表查询社区列表
     * @param groupIds
     * @return
     */
    public Map<ObjectId,String> getMapCommunityEntriesByGroupIds(List<ObjectId> groupIds){
        Map<ObjectId,String> map = new HashMap<ObjectId, String>();
        BasicDBObject query = new BasicDBObject().append("grid", new BasicDBObject(Constant.MONGO_IN,groupIds));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                map.put(new CommunityEntry(dbObject).getGroupId(),new CommunityEntry(dbObject).getCommunityName());
            }
        }
        return map;
    }


    public Boolean isCommunityNameUnique(String communityName) {
        return findByName(communityName) == null;
    }

    public boolean judgeCommunityName(String communityName, ObjectId id) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, id)
                .append("cmmn", communityName);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query) == 1;
    }

    /**
     * 通过id查找社区消息
     *
     * @param ids
     * @return
     */
    public Map<ObjectId, CommunityEntry> findMapInfo(List<ObjectId> ids) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                retMap.put(e.getID(), e);
            }
        }
        return retMap;
    }

    public List<ObjectId> getGroupIdsByCommunityIds(List<ObjectId> ids){
        List<ObjectId> groupIds=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for(DBObject dbObject:list){
                CommunityEntry entry=new CommunityEntry(dbObject);
                groupIds.add(entry.getGroupId());
            }
        }
        return groupIds;
    }
    
    public List<CommunityEntry> getCommunitysByCommunityIds(List<ObjectId> ids){
        List<CommunityEntry> list1= new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            for(DBObject dbObject:list){
                CommunityEntry entry=new CommunityEntry(dbObject);
                list1.add(entry);
            }
        }
        return list1;
    }



    public Map<ObjectId,ObjectId> getGroupIds(List<ObjectId> ids){
        Map<ObjectId,ObjectId> groupIds=new HashMap<ObjectId, ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                groupIds.put(e.getID(),e.getGroupId());
            }
        }
        return groupIds;
    }

    public List<ObjectId> getListGroupIds(List<ObjectId> ids){
        List<ObjectId> groupIds=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                groupIds.add(e.getGroupId());
            }
        }
        return groupIds;
    }

    public List<ObjectId> selectCommunityByGroupIds(List<ObjectId> ids){
        List<ObjectId> groupIds=new ArrayList<ObjectId>();
        BasicDBObject query = new BasicDBObject()
                .append("grid", new BasicDBObject(Constant.MONGO_IN, ids)).append("r", 0);
        Map<ObjectId, CommunityEntry> retMap = new HashMap<ObjectId, CommunityEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (null != list && !list.isEmpty()) {
            CommunityEntry e;
            for (DBObject dbo : list) {
                e = new CommunityEntry(dbo);
                groupIds.add(e.getID());
            }
        }
        return groupIds;
    }

    /**
     * 根据社区id
     *
     */
    public ObjectId getGroupIdByCommunityId(ObjectId cid) {
        BasicDBObject query = new BasicDBObject();
        query.append("r",Constant.ZERO);
        query.append(Constant.ID,cid);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, Constant.FIELDS);
        if (obj != null) {
            return new CommunityEntry(obj).getGroupId();
        }
        return null;
    }

    /**
     * 获取userId 创建的社群
     * @param map
     * @return
     */
    public Map<String,Object> getUserCreatedCommunity(Map map,List<ObjectId> createdGroupIds) {
        int page = map.get("page") == null?1:Integer.parseInt(map.get("page").toString());
        int pageSize = map.get("pageSize") == null?10:Integer.parseInt(map.get("pageSize").toString());

        Map<String,Object> result = new HashMap<String, Object>();
        List<CommunityEntry> entries=new ArrayList<CommunityEntry>();
        BasicDBObject query = new BasicDBObject()/*.append("cmow", new ObjectId(map.get("userId").toString()))*/;
        query.append("r",Constant.ZERO);
        query.append("grid",new BasicDBObject(Constant.MONGO_IN,createdGroupIds));
        //排除某些不展示的社群
        List<String> notShowCommunityNames = new ArrayList<String>();
        notShowCommunityNames.add("复兰社区");
        notShowCommunityNames.add("复兰大学");
        query.append("cmmn",  new BasicDBObject(Constant.MONGO_NOTIN, notShowCommunityNames));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS
                ,new BasicDBObject("_id", Constant.DESC), (page - 1) * pageSize, pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommunityEntry(dbObject));
            }
        }
        result.put("communityEntryList",entries);
        //获取总 count
        List<DBObject> countList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS);
        result.put("count",countList.size());
        return result;
    }

    /**
     * 获取userId 加入的社群
     * @param map
     * @return
     */
    public Map<String,Object> getUserJoinCommunityByIdList(List<ObjectId> notCreatedGroupIds, Map map) {
        int page = map.get("page") == null?1:Integer.parseInt(map.get("page").toString());
        int pageSize = map.get("pageSize") == null?10:Integer.parseInt(map.get("pageSize").toString());

        Map<String,Object> result = new HashMap<String, Object>();
        List<CommunityEntry> entries=new ArrayList<CommunityEntry>();
//        //不取自己创建的社群 操作有退出
//        List<ObjectId> userIdList = new ArrayList<ObjectId>();
//        userIdList.add(new ObjectId(map.get("userId").toString()));
//        BasicDBObject query = new BasicDBObject().append("cmow",  new BasicDBObject(Constant.MONGO_NOTIN, userIdList));
        BasicDBObject query = new BasicDBObject();
//        query.append("cmow",  new BasicDBObject(Constant.MONGO_NOTIN, userIdList));
        query.append("r",Constant.ZERO);
        query.append("grid",new BasicDBObject(Constant.MONGO_IN, notCreatedGroupIds));

        //排除某些不展示的社群
        List<String> notShowCommunityNames = new ArrayList<String>();
        notShowCommunityNames.add("复兰社区");
        notShowCommunityNames.add("复兰大学");
        query.append("cmmn",  new BasicDBObject(Constant.MONGO_NOTIN, notShowCommunityNames));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS
                ,new BasicDBObject("_id", Constant.DESC), (page - 1) * pageSize, pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new CommunityEntry(dbObject));
            }
        }
        result.put("communityEntryList",entries);
        //获取总 count
        List<DBObject> countList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query,Constant.FIELDS);
        result.put("count",countList.size());
        return result;
    }

    public void delCommunityById(ObjectId communityId) {
        BasicDBObject query = new BasicDBObject().append(Constant.ID, communityId);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("r", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    public int getNotDelNumber(List<ObjectId> communityIdList) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,communityIdList));
        query.append("r",Constant.ZERO);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_FORUM_COMMUNITY,
                        query);
        return count;
    }

    /**
     * 返回社区Entry
     *
     * @param searchId communityName
     * @return
     */
    public CommunityEntry findBySearchIdOrName(String searchParam) {
        BasicDBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        BasicDBObject query1 = new BasicDBObject().append("cmid", searchParam);
        BasicDBObject query2 = new BasicDBObject().append("cmmn", searchParam);
        values.add(query1);
        values.add(query2);
        query.put(Constant.MONGO_OR, values);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query);
        return dbObject == null ? null : new CommunityEntry(dbObject);
    }


    public void updateCommunityGrade(String communityId,String gradeVal, int createGradeYear, int createGradeMonth) {
        ObjectId id = new ObjectId(communityId);
        BasicDBObject query = new BasicDBObject().append(Constant.ID, id);

        BasicDBObject updateVal = new BasicDBObject();
        updateVal.append("gradeVal", gradeVal);
        updateVal.append("createGradeYear", createGradeYear);
        updateVal.append("createGradeMonth", createGradeMonth);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateVal);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }

    /**
     *  学校年级社群编辑
     * @return
     */
    public void updateEditCommunityGrade(String communityId, String communityName, String gradeVal, int createGradeYear, int createGradeMonth) {
        ObjectId id = new ObjectId(communityId);
        BasicDBObject query = new BasicDBObject().append(Constant.ID, id);

        BasicDBObject updateVal = new BasicDBObject();
        updateVal.append("gradeVal", gradeVal);
        updateVal.append("cmmn", communityName);
        updateVal.append("createGradeYear", createGradeYear);
        updateVal.append("createGradeMonth", createGradeMonth);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, updateVal);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY, query, update);
    }
}
