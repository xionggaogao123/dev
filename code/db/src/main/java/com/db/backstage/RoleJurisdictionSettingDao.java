package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.RoleJurisdictionSettingEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/23 16:07
 * @Description:
 */
public class RoleJurisdictionSettingDao extends BaseDao {

    /**
     * 新增
     * @param settingEntry
     * @return
     */
    public String addEntry(RoleJurisdictionSettingEntry settingEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ROLE_JURISDICTION_SETTING, settingEntry.getBaseEntry());
        return settingEntry.getID().toString();
    }


    /**
     * 更新 逻辑删除isr 为1的时候
     * @param map
     * @return
     */
    public String updateSetting(Map map) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(map.get("id").toString()));
        BasicDBObject updateParam = new BasicDBObject();
        if (map.get("roleName") != null) {
            updateParam.append("roleName",map.get("roleName").toString());
        }
        if (map.get("jurisdictionLevelId") != null) {
            updateParam.append("jurisdictionLevelId",new ObjectId(map.get("jurisdictionLevelId").toString()));
        }
        if (map.get("roleProperty") != null) {
            updateParam.append("roleProperty",map.get("roleProperty").toString());
        }
        if (map.get("isr") != null) {
            updateParam.append("isr",map.get("isr"));
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ROLE_JURISDICTION_SETTING,query,updateValue);
        return map.get("id").toString();
    }

    public List<RoleJurisdictionSettingEntry> getRoleJurisdictionList(Map map) {
        List<RoleJurisdictionSettingEntry> entries=new ArrayList<RoleJurisdictionSettingEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ROLE_JURISDICTION_SETTING,new BasicDBObject().append("isr",Constant.ZERO),
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new RoleJurisdictionSettingEntry(dbObject));
            }
        }
        return  entries;
    }

    public RoleJurisdictionSettingEntry getEntryById(ObjectId roleId) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO)
                .append("_id",roleId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ROLE_JURISDICTION_SETTING,query,
                Constant.FIELDS);
        if (null != dbObject){
            return new RoleJurisdictionSettingEntry(dbObject);
        }else {
            return null;
        }
    }


    public Map<ObjectId,List<String>> getSysRoleNameListGroupByUserId(List<ObjectId> userIds, Map<ObjectId,List<ObjectId>> userLogResultEntryRoleIdsMap) {
        //封装in 的条件
        List<ObjectId> roleIds = new ArrayList<ObjectId>();
        for (ObjectId userId:userIds){
            roleIds.addAll((ArrayList)userLogResultEntryRoleIdsMap.get(userId));
        }
        //查询
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO)
                .append("_id",new BasicDBObject(Constant.MONGO_IN,roleIds));
        List<RoleJurisdictionSettingEntry> settingEntries = new ArrayList<RoleJurisdictionSettingEntry>();
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ROLE_JURISDICTION_SETTING, query, Constant.FIELDS);
        for (DBObject dbo : dbObjects) {
            settingEntries.add(new RoleJurisdictionSettingEntry(dbo));
        }
        //封装返回的数据
        Map<ObjectId,List<String>> result = new HashMap<ObjectId, List<String>>();
        if (settingEntries.size()>0){
            for (ObjectId userId : userIds){
                List<String> roleNameList = new ArrayList<String>();
                for (ObjectId roleId : (ArrayList<ObjectId>)userLogResultEntryRoleIdsMap.get(userId)){
                    for (RoleJurisdictionSettingEntry entry : settingEntries){
                        if (roleId != null && roleId.equals(entry.getID())) {
                            roleNameList.add(entry.getRoleName());
                        }
                    }
                }
                result.put(userId, roleNameList);
            }
        }

        return result;
    }
    public List<RoleJurisdictionSettingEntry> getEntriesByRoleProperty(String roleProperty) {
        List<RoleJurisdictionSettingEntry> entries=new ArrayList<RoleJurisdictionSettingEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_ROLE_JURISDICTION_SETTING,
                new BasicDBObject().append("isr",Constant.ZERO).append("roleProperty",roleProperty),
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new RoleJurisdictionSettingEntry(dbObject));
            }
        }
        return  entries;
    }
}
