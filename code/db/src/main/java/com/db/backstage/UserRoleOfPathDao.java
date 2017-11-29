package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.UserRoleOfPathEntry;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/29.
 */
public class UserRoleOfPathDao extends BaseDao{

    public void saveUserRoleOfPath(UserRoleOfPathEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_USER_ROLE_OF_PATH,entry.getBaseEntry());
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
}
