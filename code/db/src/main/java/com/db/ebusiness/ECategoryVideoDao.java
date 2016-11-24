package com.db.ebusiness;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.ebusiness.ECategoryVideoDTO;
import com.pojo.ebusiness.ECategoryVideoEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/3/31.
 */
public class ECategoryVideoDao extends BaseDao{
    /**
     * 新增商品分类简介视频
     * */
    public ObjectId addCategoryVideo(ECategoryVideoEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EBUSINESS_CATEGORYVIDEO,entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 更新
     * */
    public ObjectId updateCategoryVideo(ECategoryVideoEntry entry){
        BasicDBObject query = new BasicDBObject(Constant.ID,entry.getID());
        BasicDBObject update = new BasicDBObject()
                .append("nm",entry.getName())
                .append("vu",entry.getVideoUrl())
                .append("viu",entry.getVideoImageUrl())
                .append("img",entry.getImageUrl())
                .append("tt", entry.getTitle())
                .append("txt",entry.getText())
                .append("ct",entry.getCategory());
        BasicDBObject updateVaule = new BasicDBObject(Constant.MONGO_SET,update);
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_CATEGORYVIDEO,query,updateVaule);
        return entry.getID();
    }

    /**
     * 删除
     * */
    public void deleteCategroyVideo(ObjectId id){
        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_CATEGORYVIDEO,new BasicDBObject(Constant.ID,id));
    }

    /**
     * 分页查询
     * @param categoryId 商品分类，可选
     * */
    public List<ECategoryVideoEntry> getCategoryVideoList(ObjectId categoryId,int skip,int limit){
        BasicDBObject query;
        if(categoryId == null){
            query = Constant.QUERY;
        }else {
            query = new BasicDBObject("ct",categoryId);
        }
        List<DBObject> result = find(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_CATEGORYVIDEO,query,Constant.FIELDS,Constant.MONGO_SORTBY_ASC,skip,limit);
        List<ECategoryVideoEntry> rtList = new ArrayList<ECategoryVideoEntry>();
        for(DBObject dbo : result){
            rtList.add(new ECategoryVideoEntry((BasicDBObject)dbo));
        }
        return rtList;
    }

    /**
     * 查询（不分页）
     * */
    public List<ECategoryVideoEntry> getCategoryVideoList(ObjectId categoryId){
        BasicDBObject query;
        if(categoryId == null){
            query = Constant.QUERY;
        }else {
            query = new BasicDBObject("ct",categoryId);
        }
        List<DBObject> result = find(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_CATEGORYVIDEO,query,Constant.FIELDS,Constant.MONGO_SORTBY_ASC);
        List<ECategoryVideoEntry> rtList = new ArrayList<ECategoryVideoEntry>();
        for(DBObject dbo : result){
            rtList.add(new ECategoryVideoEntry((BasicDBObject)dbo));
        }
        return rtList;
    }

    /**
     * 查询总数
     * */
    public int getCount(ObjectId categoryId){
        BasicDBObject query;
        if(categoryId == null){
            query = Constant.QUERY;
        }else {
            query = new BasicDBObject("ct",categoryId);
        }
        return count(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_CATEGORYVIDEO,query);
    }

    /**
     * 根据id查询
     * */
    public ECategoryVideoEntry getCategoryVideoById(ObjectId videoId){
        BasicDBObject query = new BasicDBObject(Constant.ID,videoId);
        DBObject result = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_EBUSINESS_CATEGORYVIDEO,query,Constant.FIELDS);
        return new ECategoryVideoEntry((BasicDBObject)result);
    }
}
