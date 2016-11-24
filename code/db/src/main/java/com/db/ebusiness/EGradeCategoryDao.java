package com.db.ebusiness;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EGradeCategoryEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/4/8.
 */
public class EGradeCategoryDao extends BaseDao{
    /**
     * 新增
     * */
    public ObjectId add(EGradeCategoryEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GRADECATEGORY,entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 获取年级分类
     * */
    public List<EGradeCategoryEntry> getGradeCategoryList(){
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_GRADECATEGORY,Constant.QUERY,Constant.FIELDS);
        List<EGradeCategoryEntry> entryList = new ArrayList<EGradeCategoryEntry>();

        if(dbObjectList != null && dbObjectList.size() > 0){
            for(DBObject dbo : dbObjectList){
                entryList.add(new EGradeCategoryEntry((BasicDBObject)dbo));
            }
        }
        return entryList;
    }

    /**
     * 删除年级分类
     * */
    public void delete(ObjectId id) {
        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_GRADECATEGORY,new BasicDBObject(Constant.ID,id));
    }

}
