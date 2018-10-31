package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.NewVersionUserRoleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/8/23.
 */
public class NewVersionUserRoleDao extends BaseDao{

    public void saveEntry(NewVersionUserRoleEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,entry.getBaseEntry());
    }


    public Map<ObjectId,Integer>  getUserRoleMap(List<ObjectId> userIds){
        Map<ObjectId,Integer> userRoleMap = new HashMap<ObjectId, Integer>();
        BasicDBObject query=new BasicDBObject("uid",new BasicDBObject(Constant.MONGO_IN,userIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionUserRoleEntry entry=new NewVersionUserRoleEntry(dbObject);
                userRoleMap.put(entry.getUserId(),entry.getNewRole());
            }
        }
        return userRoleMap;
    }
    
    public Integer  getUserRole(ObjectId userId){
        
        BasicDBObject query=new BasicDBObject("uid",userId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,query,Constant.FIELDS);
     
        NewVersionUserRoleEntry entry=new NewVersionUserRoleEntry(dbObject);
        
       
        return entry.getNewRole();
    }



    public void updateNewRole(ObjectId userId){
        BasicDBObject query=new BasicDBObject("uid",userId);
        BasicDBObject updateValue= new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("nr",Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,query,updateValue);
    }

    public NewVersionUserRoleEntry getEntry(ObjectId userId){
        BasicDBObject query=new BasicDBObject("uid",userId);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,query,
                Constant.FIELDS);
        if(null!=dbObject){
            return new NewVersionUserRoleEntry(dbObject);
        }else {
            return null;
        }
    }


    public List<NewVersionUserRoleEntry> getUserByRoleList(List<Integer> roleListInt) {
        List<NewVersionUserRoleEntry> userRoleEntries = new ArrayList<NewVersionUserRoleEntry>();
        BasicDBObject query=new BasicDBObject("nr",new BasicDBObject(Constant.MONGO_IN,roleListInt))
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionUserRoleEntry entry=new NewVersionUserRoleEntry(dbObject);
                userRoleEntries.add(entry);
            }
        }
        return userRoleEntries;
    }

    public List<NewVersionUserRoleEntry> getUserByRolePageList(List<Integer> roleListInt, Map map, List<ObjectId> teacherUserIds) {
        int page = map.get("page") == null?1:Integer.parseInt(map.get("page").toString());
        int pageSize = map.get("pageSize") == null?10:Integer.parseInt(map.get("pageSize").toString());
        List<NewVersionUserRoleEntry> userRoleEntries = new ArrayList<NewVersionUserRoleEntry>();
        BasicDBObject query=new BasicDBObject("nr",new BasicDBObject(Constant.MONGO_IN,roleListInt))
                .append("ir",Constant.ZERO);
        query.append("uid", new BasicDBObject(Constant.MONGO_NOTIN, teacherUserIds));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,query,Constant.FIELDS
                ,new BasicDBObject("_id", Constant.DESC), (page - 1) * pageSize, pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                NewVersionUserRoleEntry entry=new NewVersionUserRoleEntry(dbObject);
                userRoleEntries.add(entry);
            }
        }
        return userRoleEntries;
    }
}
