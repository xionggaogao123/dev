package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.NewVersionUserRoleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/8/23.
 */
public class NewVersionUserRoleDao extends BaseDao{

    public void saveEntry(NewVersionUserRoleEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_USER_ROLE,entry.getBaseEntry());
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
