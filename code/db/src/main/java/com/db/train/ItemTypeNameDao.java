package com.db.train;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.train.ItemTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/5.
 */
public class ItemTypeNameDao extends BaseDao {

    /**
     * 保存
     * @param entry
     */
    public void saveOrUpdateEntry(ItemTypeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_ITEMTYPENAME,entry.getBaseEntry());
    }

    public ItemTypeEntry find(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_ITEMTYPENAME,query);
        if(null!=dbObject){
            return new ItemTypeEntry((BasicDBObject)dbObject);
        }else{
            return null;
        }
    }

    /**
     * 获取某一层的分类数据
     * @return
     */
    public List<ItemTypeEntry> getLevelEntries(int level,ObjectId parentId){
        BasicDBObject query=new BasicDBObject().append("lel",level);
        if(null!=parentId) {
                query.append("pid", parentId);
        }
        List<ItemTypeEntry> entries=new ArrayList<ItemTypeEntry>();
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_ITEMTYPENAME,query);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
            for(DBObject dbo:dbObjects) {
                entries.add(new ItemTypeEntry((BasicDBObject)dbo));
            }
        }
        return entries;
    }
}
