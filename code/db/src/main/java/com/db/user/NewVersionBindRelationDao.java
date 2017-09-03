package com.db.user;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.user.NewVersionBindRelationEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/8/28.
 */
public class NewVersionBindRelationDao extends BaseDao{

    public void saveNewVersionBindEntry(NewVersionBindRelationEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,entry.getBaseEntry());
    }

    public List<NewVersionBindRelationEntry> getEntriesByMainUserId(
            ObjectId mainUserId
    ){
        List<NewVersionBindRelationEntry> entries
                =new ArrayList<NewVersionBindRelationEntry>();
        BasicDBObject query = new BasicDBObject()
                .append("muid",mainUserId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_BIND_RELATION,
                query,Constant.FIELDS);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new NewVersionBindRelationEntry(dbObject));
            }
        }
        return entries;
    }
}
