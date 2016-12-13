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
import java.util.regex.Pattern;

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
        BasicDBObject sort=new BasicDBObject("so",-1);
        List<RegionEntry> regionEntries=new ArrayList<RegionEntry>();
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_REGIONS,query,Constant.FIELDS,sort);
        if(null!=dbObjects&&!dbObjects.isEmpty()){
           for(DBObject dbObject:dbObjects){
               regionEntries.add(new RegionEntry((BasicDBObject)dbObject));
           }
        }
        return regionEntries;
    }

    public RegionEntry getRegionEntry(String name){
        BasicDBObject query =getQueryCondition("nm",name);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_REGIONS,query);
        if(null!=dbObject){
            return new RegionEntry((BasicDBObject) dbObject);
        }else{
            return null;
        }
    }

    public void  setSort(ObjectId id,int sort){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("so",sort));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_TRAIN_REGIONS,query,updateValue);
    }


    private BasicDBObject getQueryCondition(String field, String name) {
        BasicDBObject query = new BasicDBObject();
        Pattern pattern = Pattern.compile("^.*" + name + ".*$", Pattern.CASE_INSENSITIVE);
        query.append(field, new BasicDBObject(Constant.MONGO_REGEX, pattern));
        return query;
    }

}
