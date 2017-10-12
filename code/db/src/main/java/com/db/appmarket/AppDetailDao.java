package com.db.appmarket;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appmarket.AppDetailEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;

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

    public List<AppDetailEntry> getAppByCondition(String regular){
        List<AppDetailEntry> entries=new ArrayList<AppDetailEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("ir",Constant.ZERO);
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
}
