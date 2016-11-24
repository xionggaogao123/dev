package com.db.examscoreanalysis;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.examscoreanalysis.ZongFenEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fl on 2016/8/23.
 */
public class ZongFenDao extends BaseDao {

    /**
     * 保存（新增或更新）
     * @param zongFenEntry
     * @return
     */
    public ObjectId save(ZongFenEntry zongFenEntry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZONGFEN, zongFenEntry.getBaseEntry());
        return zongFenEntry.getID();
    }

    public void save(List<ZongFenEntry> zongFenEntries){
        List<DBObject> list= MongoUtils.fetchDBObjectList(zongFenEntries);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZONGFEN, list);
    }

    /**
     * 按考试取出总分
     * @param examId
     * @return
     */
    public List<ZongFenEntry> getZongFenByExamId(ObjectId examId, int skip, int limit){
        List<ZongFenEntry> zongFenEntries = new ArrayList<ZongFenEntry>();
        DBObject query = new BasicDBObject("exid", examId);
        DBObject orderBy = new BasicDBObject("gr", Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZONGFEN, query, Constant.FIELDS, orderBy, skip, limit);
        if(dbObjects != null && dbObjects.size() > 0){
            for(DBObject dbObject : dbObjects)
                zongFenEntries.add(new ZongFenEntry((BasicDBObject)dbObject));
        }
        return zongFenEntries;
    }

    /**
     * 按行政班取出总分
     * @param classId
     * @return
     */
    public List<ZongFenEntry> getZongFenByClassId(ObjectId classId){
        List<ZongFenEntry> zongFenEntries = new ArrayList<ZongFenEntry>();
        DBObject query = new BasicDBObject("acid", classId);
        DBObject orderBy = new BasicDBObject("gr", Constant.DESC);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZONGFEN, query, Constant.FIELDS, orderBy);
        if(dbObjects != null && dbObjects.size() > 0){
            zongFenEntries.add(new ZongFenEntry((BasicDBObject)dbObjects));
        }
        return zongFenEntries;
    }

    public void remove(ObjectId examId){
        DBObject query = new BasicDBObject("exid", examId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZONGFEN, query);
    }

    public int countByExamId(ObjectId examId){
        DBObject query = new BasicDBObject("exid", examId);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZONGFEN, query);
    }



}
