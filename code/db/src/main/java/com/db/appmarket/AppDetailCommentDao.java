package com.db.appmarket;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appmarket.AppDetailCommentEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailCommentDao extends BaseDao{

    public void saveAppDetailComment(AppDetailCommentEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL_COMMENT,entry.getBaseEntry());
    }

    public int countAppEntries(ObjectId appDetailId){
        BasicDBObject query=new BasicDBObject()
                .append("apd",appDetailId)
                .append("ir", Constant.ZERO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL_COMMENT,query);
    }
    public List<AppDetailCommentEntry> getAppEntries(ObjectId appDetailId,
                                                     int page,
                                                     int pageSize){
        List<AppDetailCommentEntry> entries=new ArrayList<AppDetailCommentEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("apd",appDetailId)
                .append("ir", Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL_COMMENT,
                query,Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailCommentEntry(dbObject));
            }
        }
        return entries;
    }
}
