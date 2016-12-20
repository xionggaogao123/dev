package com.db.train;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.train.ItemTypeCopyEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/12/19.
 */
public class ItemTypeNameCopyDao extends BaseDao {

    public ObjectId saveOrUpdate(ItemTypeCopyEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_ITEMTYPENAME_COPY,entry.getBaseEntry());
        return entry.getID();
    }

    public ItemTypeCopyEntry getEntryByName(String name){
        BasicDBObject query=new BasicDBObject("nm",name);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_ITEMTYPENAME_COPY,query);
        if(null!=dbo){
            return new ItemTypeCopyEntry((BasicDBObject)dbo);
        }else{
            return null;
        }
    }

    public ItemTypeCopyEntry find(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_ITEMTYPENAME_COPY,query);
        if(null!=dbo){
            return new ItemTypeCopyEntry((BasicDBObject)dbo);
        }else{
            return null;
        }
    }


}
