package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.UserRoleJurisdictionEntry;
import com.pojo.backstage.UserRoleOfPathEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by taotao.chan on 2018年8月22日17:11:52
 */
public class UserRoleJurisdictionDao extends BaseDao{

    public String saveUserRoleOfPath(UserRoleOfPathEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_OF_PATH,entry.getBaseEntry());
        return entry.getID().toString();
    }
    public List<UserRoleOfPathEntry> getRoleEntries(){
        List<UserRoleOfPathEntry> entries=new ArrayList<UserRoleOfPathEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_OF_PATH,new BasicDBObject(),
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new UserRoleOfPathEntry(dbObject));
            }
        }
        return  entries;
    }

    public UserRoleOfPathEntry getEntryByRole(int role){
        BasicDBObject query=new BasicDBObject()
                .append("rl",role);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_OF_PATH,query,
                Constant.FIELDS);
        if(null!=dbObject){
            return new UserRoleOfPathEntry(dbObject);
        }else{
            return null;
        }
    }

    public String addEntry(UserRoleJurisdictionEntry jurisdictionEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_JURISDICTION, jurisdictionEntry.getBaseEntry());
        return jurisdictionEntry.getID().toString();
    }

    public List<UserRoleJurisdictionEntry> getJurisdiction(Map map) {
        List<UserRoleJurisdictionEntry> entries=new ArrayList<UserRoleJurisdictionEntry>();
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_JURISDICTION,new BasicDBObject().append("isr",Constant.ZERO),
                Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new UserRoleJurisdictionEntry(dbObject));
            }
        }
        return  entries;
    }

    public UserRoleJurisdictionEntry getEntryById(ObjectId jurisdictionLevelId) {
        BasicDBObject query = new BasicDBObject()
                .append(Constant.ID, jurisdictionLevelId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_JURISDICTION,
                query, Constant.FIELDS);
        if (null != dbObject) {
            return new UserRoleJurisdictionEntry(dbObject);
        } else {
            return null;
        }
    }

    public String updateRoleJurisdiction(Map map) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(map.get("id").toString()));
        BasicDBObject updateParam = new BasicDBObject();
        if (map.get("community") != null) {
            List<String> comList =new ArrayList<String>();
            String[] comStr = map.get("community").toString().split(",");
            for(int i =0;i<comStr.length;i++){
                if(!comList.contains(comStr[i])){
                    comList.add(comStr[i]);
                }
            }
            updateParam.append("roleJurisdiction",comList);
        }
        if (map.get("path") != null) {
            List<String> pathList =new ArrayList<String>();
            String[] str = map.get("path").toString().split(",");
            for(int i =0;i<str.length;i++){
                if(!pathList.contains(str[i])){
                    pathList.add(str[i]);
                }
            }
            updateParam.append("rolePath",pathList);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_JURISDICTION,query,updateValue);
        return map.get("id").toString();
    }
}
