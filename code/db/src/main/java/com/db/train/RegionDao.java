package com.db.train;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.train.RegionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/5.
 */
public class RegionDao extends BaseDao {

    public void saveOrUpdate(RegionEntry entry){
       save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_REGIONS,entry.getBaseEntry());
    }

    /**
     * 获取位置信息
     * @param parentId
     * @return
     */
    public List<RegionEntry> getRegionEntries(int level,ObjectId parentId){
        BasicDBObject query=new BasicDBObject().append("lel",level);
        if(null!=parentId) {
            query.append("pid", parentId);
        }
        List<RegionEntry> regionEntries=new ArrayList<RegionEntry>();
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_REGIONS,query);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
           for(DBObject dbObject:dbObjects){
               regionEntries.add(new RegionEntry((BasicDBObject)dbObject));
           }
        }
        return regionEntries;
    }

}
