package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FSectionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/31.
 */
public class FSectionDao extends BaseDao {

    /**
     * 增加板块
     * @param
     *
     * @return
     */
    public ObjectId addFSectionEntry(FSectionEntry fSectionEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, fSectionEntry.getBaseEntry());
        return fSectionEntry.getID();
    }

    /**
     * 更新板块
     * */
    public ObjectId updateFSectionEntry(FSectionEntry entry){
        DBObject query = new BasicDBObject(Constant.ID,entry.getID());
        DBObject update = new BasicDBObject("nm",entry.getName());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query, updateValue);
        return entry.getID();
    }

    /**
     * 更新板块排序
     * */
    public void updateFSectionEntrySort(ObjectId selfId,int selfSort,ObjectId anotherId,int anotherSort){
        DBObject query1 = new BasicDBObject(Constant.ID,selfId);
        DBObject update1 = new BasicDBObject("st",selfSort);
        DBObject updateValue1 = new BasicDBObject(Constant.MONGO_SET,update1);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query1, updateValue1);

        DBObject query2 = new BasicDBObject(Constant.ID,anotherId);
        DBObject update2 = new BasicDBObject("st",anotherSort);
        DBObject updateValue2 = new BasicDBObject(Constant.MONGO_SET,update2);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query2, updateValue2);
    }

    /**
     * 更新板块首页图片
     * */
    public void updateFSectionEntryImg(ObjectId id,String type,String imgUrl){
        DBObject query = new BasicDBObject(Constant.ID,id);
        DBObject update = new BasicDBObject(type,imgUrl);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,update);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query, updateValue);
    }

    /**
     * 删除商品分类及其子分类
     * @param fSectionEntryId
     */
    public void deleteFSectionEntry(ObjectId fSectionEntryId){
        DBObject query = new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("pid", fSectionEntryId));
        values.add(new BasicDBObject(Constant.ID, fSectionEntryId));
        query.put(Constant.MONGO_OR, values);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query);
    }

    /**
     *得到板块
     * @param fSectionEntryId
     * @return
     */
    public FSectionEntry getFSection(ObjectId fSectionEntryId){
        DBObject query = new BasicDBObject(Constant.ID, fSectionEntryId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query, Constant.FIELDS);
        return new FSectionEntry((BasicDBObject)dbObject);
    }

    /**
     * 根据板块得到子版块
     * @param parentId
     * @return
     */
    public List<FSectionEntry> getFSectionListByParentId(ObjectId parentId){
        DBObject query = new BasicDBObject("pid", parentId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query, Constant.FIELDS);
        List<FSectionEntry> retList = new ArrayList<FSectionEntry>();
        if(dbObjectList!=null && dbObjectList.size()>0){
            for(DBObject dbo:dbObjectList)
            {
                FSectionEntry fSectionEntry=new FSectionEntry((BasicDBObject)dbo);
                retList.add(fSectionEntry);
            }
        }
        return retList;
    }

    /**
     * 根据等级得到板块
     * @param level
     * @return
     */
    public List<FSectionEntry> getFSectionListByLevel(int level,ObjectId id,String name){
        BasicDBObject query = new BasicDBObject("lvl", level);
        DBObject sort = new BasicDBObject("st",1);
        if(null!=id){
            query.append("_id",id);
        }
        if(!"".equals(name)){
            query.append("nm",name);
        }
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FORUM_SECTION, query, Constant.FIELDS,sort);
        List<FSectionEntry> retList = new ArrayList<FSectionEntry>();
        if(dbObjectList!=null && dbObjectList.size()>0){
            for(DBObject dbo:dbObjectList)
            {
                FSectionEntry fSectionEntry=new FSectionEntry((BasicDBObject)dbo);
                retList.add(fSectionEntry);
            }
        }
        return retList;
    }

}
