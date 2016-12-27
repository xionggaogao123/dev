package com.db.communityValidate;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.fcommunity.ValidateInfoEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */
public class ValidateInfoDao extends BaseDao {

    public void saveOrUpdate(ValidateInfoEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,entry.getBaseEntry());
    }

    public void batchSaveInfo(List<ValidateInfoEntry> entries){
        List<DBObject> list= MongoUtils.fetchDBObjectList(entries);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,list);
    }

    public List<ValidateInfoEntry> getValidateInfos(ObjectId reviewId){
        List<ValidateInfoEntry> entries=new ArrayList<ValidateInfoEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("rw",reviewId)
                .append("ir",0);

        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_COMMUNITY_VALIDATE_INFO,query);
        if(null!=list&&!list.isEmpty()){
            for(DBObject dbObject:list){
                entries.add(new ValidateInfoEntry((BasicDBObject)dbObject));
            }

        }
        return entries;
    }
}
