package com.db.train;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.DBObject;
import com.pojo.train.InstituteCopyEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

import java.util.List;

/**
 * Created by admin on 2016/12/19.
 */
public class InstituteCopyDao extends BaseDao {

    public void saveOrUpdate(InstituteCopyEntry entry){
      save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_INSTITUTE_COPY,entry.getBaseEntry());
    }

    public void batchInsertData(List<InstituteCopyEntry> entries){
        List<DBObject> list = MongoUtils.fetchDBObjectList(entries);
        if(null!=list&&!list.isEmpty()){
            save(MongoFacroty.getAppDB(), Constant.COLLECTION_TRAIN_INSTITUTE_COPY,list);
        }
    }
}
