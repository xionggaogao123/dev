package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.NewVersionUserRoleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

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



}
