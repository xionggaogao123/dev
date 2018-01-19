package com.db.business;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.business.ModuleNumberEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018/1/17.
 */
public class ModuleNumberDao extends BaseDao {
    public String addEntry(ObjectId userId,int type){
        ModuleNumberEntry entry =  this.getEntry(userId,type);
        if(null != entry){
            entry.setNumber(entry.getNumber()+1);
        }else{
            entry = new ModuleNumberEntry(userId,type, ApplyTypeEn.getDes(type),1);
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MODULE_NUMBER, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //单查询
    public ModuleNumberEntry getEntry(ObjectId userId,int type) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO) .append("uid", userId) .append("mty", type);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MODULE_NUMBER, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new ModuleNumberEntry((BasicDBObject)dbo);
        }
        return null;
    }
    public List<ModuleNumberEntry> getPageList(ObjectId userId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("uid",userId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_MODULE_NUMBER, query, Constant.FIELDS,new BasicDBObject("num",Constant.DESC));
        List<ModuleNumberEntry> retList =new ArrayList<ModuleNumberEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ModuleNumberEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
}
