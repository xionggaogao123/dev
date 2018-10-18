package com.db.appmarket;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appmarket.AppDetailEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailDao extends BaseDao {

    public void saveAppDetailEntry(AppDetailEntry detailEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL,detailEntry.getBaseEntry());
    }

    public String saveEntry(AppDetailEntry detailEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL,detailEntry.getBaseEntry());
        return detailEntry.getID().toString() ;
    }
    public void updEntry(AppDetailEntry e) {
        BasicDBObject query=new BasicDBObject(Constant.ID,e.getID());
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL, query,updateValue);
    }

    public void updateEntry(ObjectId id){
        BasicDBObject query = new BasicDBObject(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("wob",Constant.TWO));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL, query,updateValue);
    }
    public void removeById(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL,query);
    }

    public void setOrder(ObjectId apkId,int order){
        BasicDBObject query=new BasicDBObject(Constant.ID,apkId);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ord",order));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL,query,updateValue);
    }

    public AppDetailEntry findEntryById(ObjectId id){
        BasicDBObject query=new BasicDBObject(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppDetailEntry(dbObject);
        }else{
            return null;
        }
    }

    public void updateApkByType(ObjectId id,int type){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ty",type));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL,query,updateValue);
    }


    public AppDetailEntry getEntryByApkPackageName(String packageName){
        BasicDBObject query=new BasicDBObject()
                .append("apn",packageName);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_APP_MARKET_DETAIL,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppDetailEntry(dbObject);
        }else{
            return null;
        }
    }

    public List<AppDetailEntry> getEntryByApkPackageNames(List<String> packageNames){

        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO).append("apn",new BasicDBObject(Constant.MONGO_IN,packageNames));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }

    public List<AppDetailEntry> getAppByCondition(String regular){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO).append("ty",Constant.TWO);
        if(StringUtils.isNotBlank(regular)){
            Pattern pattern = Pattern.compile("^.*" + regular + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("an",new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }

    public List<AppDetailEntry> getNoThreeAppList(){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(0);
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO).append("ty",new BasicDBObject(Constant.MONGO_IN,integers));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS,new BasicDBObject("ty",-1));
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                AppDetailEntry appDetailEntry = new AppDetailEntry(dbObject);
                //除去个人中心
                if(!"com.fulan.usercenter".equals(appDetailEntry.getAppPackageName()) && !"com.fulan.contact".equals(appDetailEntry.getAppPackageName())){
                    entries.add(appDetailEntry);
                }
            }
        }
        return entries;
    }

    public List<AppDetailEntry> searchFulanAppByCondition(String regular){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO).append("ty",Constant.ONE);
        if(StringUtils.isNotBlank(regular)){
            Pattern pattern = Pattern.compile("^.*" + regular + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("an",new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }

    public List<AppDetailEntry> searchSchoolAppByCondition(String regular){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO).append("ty",Constant.THREE);
        if(StringUtils.isNotBlank(regular)){
            Pattern pattern = Pattern.compile("^.*" + regular + ".*$", Pattern.CASE_INSENSITIVE);
            query.append("an",new BasicDBObject(Constant.MONGO_REGEX, pattern));
        }
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }

    public List<AppDetailEntry> searchSchoolAppByIds(List<ObjectId> oids){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO).append("ty",Constant.THREE);
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_IN,oids));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }


    public List<AppDetailEntry> getAllByCondition(){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
        query.append("ty",new BasicDBObject(Constant.MONGO_NE,2));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }
    public List<AppDetailEntry> getSimpleAppEntry(){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
        query.append("wob",1);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }
    public List<AppDetailEntry> getEntries(){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }

    public List<AppDetailEntry> getNewEntries(){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO).append("ty",new BasicDBObject(Constant.MONGO_IN,list));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS,new BasicDBObject("ty",1));
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }
    public List<AppDetailEntry> getEntriesByIds(List<ObjectId> appIds){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,appIds))
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }
    public List<AppDetailEntry> getEntriesByPackName(List<String> names){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject("apn",new BasicDBObject(Constant.MONGO_IN,names))
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }
    //查询第三方应用
    public List<AppDetailEntry> getThirdEntries(){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ty",2)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_APP_MARKET_DETAIL,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppDetailEntry(dbObject));
            }
        }
        return entries;
    }
}
