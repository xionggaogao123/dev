package com.db.appmarket;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appmarket.AppDetailStarStatisticEntry;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailStarStatisticDao extends BaseDao{

    public void saveAppDetailStarStatisticEntry(AppDetailStarStatisticEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL_STAR_STATISTIC,entry.getBaseEntry());
    }

    public void updateAppDetailStar(ObjectId appDetailId,
                                    int star,
                                    int count){
        BasicDBObject query=new BasicDBObject()
                .append("apd",appDetailId)
                .append("sr",star);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_INC,new BasicDBObject("cn",count));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL_STAR_STATISTIC,query,updateValue);
    }

    public AppDetailStarStatisticEntry getEntryByStarAndDetailId(ObjectId appDetailId,
                                                                 int star){
        BasicDBObject query=new BasicDBObject()
                .append("apd",appDetailId)
                .append("sr",star);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL_STAR_STATISTIC,
                query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppDetailStarStatisticEntry(dbObject);
        }else{
            return null;
        }
    }

    public Map<Integer,Integer> getMapEntry(ObjectId appDetailId){
        Map<Integer,Integer> retMap=new HashMap<Integer, Integer>();
        BasicDBObject query=new BasicDBObject()
                .append("apd",appDetailId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL_STAR_STATISTIC,
                    query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                AppDetailStarStatisticEntry entry=new AppDetailStarStatisticEntry(dbObject);
                retMap.put(entry.getStar(),entry.getCount());
            }
        }
        return retMap;
    }

}
