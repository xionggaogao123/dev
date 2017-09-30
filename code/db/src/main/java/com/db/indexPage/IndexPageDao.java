package com.db.indexPage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.indexPage.IndexPageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/9/29.
 */
public class IndexPageDao extends BaseDao {

    //添加临时记录
    public ObjectId addEntry(IndexPageEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE, entry.getBaseEntry());
        return entry.getID();
    }

    //查询首页显示列表
    public List<IndexPageEntry> getPageList(List<ObjectId> olist,int page,int pageSize){
        List<IndexPageEntry> entryList=new ArrayList<IndexPageEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("cid",new BasicDBObject(Constant.MONGO_IN,olist))
                .append("ir",Constant.ZERO);
        List<DBObject> dbList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_INDEX_PAGE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new IndexPageEntry((BasicDBObject) obj));
            }
        }
        return entryList;



    }

}