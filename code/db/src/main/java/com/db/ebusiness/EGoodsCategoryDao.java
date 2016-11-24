package com.db.ebusiness;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.EGoodsCategoryEntry;
import com.pojo.ebusiness.EGoodsEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/3/3.
 */
public class EGoodsCategoryDao extends BaseDao {

    /**
     * 增加商品分类
     * @param eBusinessGoodsCategory
     * @return
     */
    public ObjectId addEGoodsCategory(EGoodsCategoryEntry eBusinessGoodsCategory){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, eBusinessGoodsCategory.getBaseEntry());
        return eBusinessGoodsCategory.getID();
    }

    /**
     * 更新商品分类
     * */
    public ObjectId updateEGoodsCategory(EGoodsCategoryEntry entry){
        DBObject query = new BasicDBObject(Constant.ID,entry.getID());
        DBObject update = new BasicDBObject("nm",entry.getName());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query, updateValue);
        return entry.getID();
    }

    /**
     * 更新商品分类排序
     * */
    public void updateEGoodsCategorySort(ObjectId selfId,int selfSort,ObjectId anotherId,int anotherSort){
        DBObject query1 = new BasicDBObject(Constant.ID,selfId);
        DBObject update1 = new BasicDBObject("st",selfSort);
        DBObject updateValue1 = new BasicDBObject(Constant.MONGO_SET,update1);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query1, updateValue1);

        DBObject query2 = new BasicDBObject(Constant.ID,anotherId);
        DBObject update2 = new BasicDBObject("st",anotherSort);
        DBObject updateValue2 = new BasicDBObject(Constant.MONGO_SET,update2);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query2, updateValue2);
    }

    /**
     * 更新商品分类首页图片
     * */
    public void updateEGoodsCatetoryImg(ObjectId id,String type,String imgUrl){
        DBObject query = new BasicDBObject(Constant.ID,id);
        DBObject update = new BasicDBObject(type,imgUrl);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query, updateValue);
    }

     /**
     * 删除商品分类及其子分类
     * @param eGoodsCategoryId
     */
    public void deleteEGoodsCategory(ObjectId eGoodsCategoryId){
        DBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("pid", eGoodsCategoryId));
        values.add(new BasicDBObject(Constant.ID, eGoodsCategoryId));
        query.put(Constant.MONGO_OR, values);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query);
    }

    /**
     *得到商品分类
     * @param categoryId
     * @return
     */
    public EGoodsCategoryEntry getEGoodsCategory(ObjectId categoryId){
        DBObject query = new BasicDBObject(Constant.ID, categoryId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query, Constant.FIELDS);
        return new EGoodsCategoryEntry((BasicDBObject)dbObject);
    }

    /**
     * 根据父分类得到子分类
     * @param parentId
     * @return
     */
    public List<EGoodsCategoryEntry> getEGoodsCategoryListByParentId(ObjectId parentId){
        DBObject query = new BasicDBObject("pid", parentId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query, Constant.FIELDS);
        List<EGoodsCategoryEntry> retList = new ArrayList<EGoodsCategoryEntry>();
        if(dbObjectList!=null && dbObjectList.size()>0){
            for(DBObject dbo:dbObjectList)
            {
                EGoodsCategoryEntry eGoodsCategoryEntry=new EGoodsCategoryEntry((BasicDBObject)dbo);
                retList.add(eGoodsCategoryEntry);
            }
        }
        return retList;
    }

    /**
     * 根据等级得到分类
     * @param level
     * @return
     */
    public List<EGoodsCategoryEntry> getEGoodsCategoryListByLevel(int level,ObjectId parentId){
        BasicDBObject query = new BasicDBObject("lvl", level);
        DBObject sort = new BasicDBObject("st",1);
        if(null!=parentId){
            query.append("pid",parentId);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_GOODSCATEGORY, query, Constant.FIELDS,sort);
        List<EGoodsCategoryEntry> retList = new ArrayList<EGoodsCategoryEntry>();
        if(dbObjectList!=null && dbObjectList.size()>0){
            for(DBObject dbo:dbObjectList)
            {
                EGoodsCategoryEntry eGoodsCategoryEntry=new EGoodsCategoryEntry((BasicDBObject)dbo);
                retList.add(eGoodsCategoryEntry);
            }
        }
        return retList;
    }


}
