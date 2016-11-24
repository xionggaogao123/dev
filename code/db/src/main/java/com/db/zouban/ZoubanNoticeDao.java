package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZoubanNoticeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/11/9.
 */
public class ZoubanNoticeDao extends BaseDao {

    /**
     * 新建调课通知
     *
     * @param zoubanNoticeEntry
     */
    public void addNotice(ZoubanNoticeEntry zoubanNoticeEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, zoubanNoticeEntry.getBaseEntry());
    }


    /**
     * 更新调课详情
     *
     * @param id
     * @param state
     * @param detailList
     */
    public void updateNotice(ObjectId id, int state, List<ZoubanNoticeEntry.NoticeDetail> detailList) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject update = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("st", state))
                .append(Constant.MONGO_PUSHALL, new BasicDBObject("con", MongoUtils.convert(MongoUtils.fetchDBObjectList(detailList))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, update);
    }


    /**
     * 获取调课通知列表
     *
     * @param term
     * @param gradeId
     * @param page
     * @param pageSize
     * @return
     */
    public List<ZoubanNoticeEntry> findNoticeList(String term, ObjectId gradeId, int page, int pageSize) {
        List<ZoubanNoticeEntry> zoubanNoticeEntryList = new ArrayList<ZoubanNoticeEntry>();
        BasicDBObject query = new BasicDBObject("te", term)
                .append("gid", gradeId)
                .append("del",0);
        int skip = pageSize * (page - 1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, Constant.FIELDS, new BasicDBObject(Constant.ID, -1), skip, pageSize);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zoubanNoticeEntryList.add(new ZoubanNoticeEntry((BasicDBObject) dbObject));
            }
        }
        return zoubanNoticeEntryList;
    }

    /**
     * 统计共有多少条调课通知
     *
     * @param term
     * @param gradeId
     * @return
     */
    public int countNotice(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject()
                .append("te",term)
                .append("gid",gradeId)
                .append("del",0);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public ZoubanNoticeEntry findNoticeById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id).append("del", Constant.ZERO);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, Constant.FIELDS);
        return dbObject == null ? null : new ZoubanNoticeEntry((BasicDBObject) dbObject);
    }


    /**
     * 判断是否调过课
     *
     * @param term
     * @param schoolId
     * @param weeks
     * @return
     */
    public boolean hasNotice(String term, ObjectId schoolId, int type, List<Integer> weeks) {
        BasicDBObject query = new BasicDBObject("te", term)
                .append("sid", schoolId)
                .append("ty", type)
                .append("week", new BasicDBObject(Constant.MONGO_IN, weeks));
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, Constant.FIELDS);
        if (dbObjectList == null || dbObjectList.isEmpty()) {
            return false;
        }
        return true;
    }



    /**
     * 删除通知
     *
     * @param term
     * @param gradeId
     */
    public void deleteAllNotice(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject("te", term).append("gid", gradeId);
        BasicDBObject updateValue = new BasicDBObject()
                .append(Constant.MONGO_SET, new BasicDBObject("del", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_NOTICE, query, updateValue);
    }

}
