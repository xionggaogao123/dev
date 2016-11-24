package com.db.pet;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.pet.PetTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/3/27.
 */
public class PetTypeDao extends BaseDao {

    /**
     * 添加宠物信息
     * @param e
     * @return
     */
    public ObjectId addPetTypeEntry(PetTypeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PET_TYPE, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 获取全部宠物信息
     * @return
     */
    public List<PetTypeEntry> selAllPetType() {
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_PET_TYPE,new BasicDBObject(), Constant.FIELDS);
        List<PetTypeEntry> list=new ArrayList<PetTypeEntry>();
        for(DBObject dbObject:dbObjects){
            list.add(new PetTypeEntry((BasicDBObject)dbObject));
        }
        return list;
    }

    /**
     * 获取指定宠物信息
     * @param petEnName
     * @return
     */
    public PetTypeEntry getPetByEnName(String petEnName) {
        DBObject query =new BasicDBObject("enm",petEnName);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PET_TYPE, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new PetTypeEntry((BasicDBObject)dbo);
        }
        return null;
    }

    /**
     * 获取非活动宠物信息
     * @return
     */
    public List<PetTypeEntry> getNotActivityPet() {
        BasicDBObject query =new BasicDBObject("act",0);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_PET_TYPE, query, Constant.FIELDS);
        List<PetTypeEntry> list=new ArrayList<PetTypeEntry>();
        for(DBObject dbObject:dbObjects){
            list.add(new PetTypeEntry((BasicDBObject)dbObject));
        }
        return list;
    }

    /**
     * 获取展示宠物信息
     * @return
     */
    public List<PetTypeEntry> getShowPet() {
        BasicDBObject query =new BasicDBObject("shw",1);
        List<DBObject> dbObjects=find(MongoFacroty.getAppDB(), Constant.COLLECTION_PET_TYPE, query, Constant.FIELDS, new BasicDBObject("_id",Constant.ASC));
        List<PetTypeEntry> list=new ArrayList<PetTypeEntry>();
        for(DBObject dbObject:dbObjects){
            list.add(new PetTypeEntry((BasicDBObject)dbObject));
        }
        return list;
    }

}
