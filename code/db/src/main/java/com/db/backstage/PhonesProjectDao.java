package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.PhonesProjectEntry;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/20 09:58
 * @Description:
 */
public class PhonesProjectDao extends BaseDao {

    public void saveProjectEntry(PhonesProjectEntry phonesProjectEntry) {
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_PHONES_PROJECT,phonesProjectEntry.getBaseEntry());
    }

    public List<PhonesProjectEntry> getAllProjects() {
        List<PhonesProjectEntry> phonesProjectEntryList = new ArrayList<PhonesProjectEntry>();
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_PHONES_PROJECT,new BasicDBObject());
        if (dbObjectList.size() == 0){
            return null;
        }else {
            for (DBObject dbObject : dbObjectList){
                phonesProjectEntryList.add(new PhonesProjectEntry(dbObject));
            }
        }
        return phonesProjectEntryList;
    }
}
