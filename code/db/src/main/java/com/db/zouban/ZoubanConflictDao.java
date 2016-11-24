package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZouBanCourseEntry;
import com.pojo.zouban.ZoubanConflict;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2015/10/15.
 */
public class ZoubanConflictDao extends BaseDao {
    /**
     * 保存一条冲突记录
     *
     * @param zoubanConflict
     */
    public void addZoubanConflict(ZoubanConflict zoubanConflict) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, zoubanConflict.getBaseEntry());
    }

    /**
     * 保存多条冲突记录
     *
     * @param zoubanConflictList
     */
    public void addZoubanConflictList(List<ZoubanConflict> zoubanConflictList) {
        for (ZoubanConflict z : zoubanConflictList) {
            addZoubanConflict(z);
        }
        /*try {
            save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT,
                    MongoUtils.convert(MongoUtils.fetchDBObjectList(zoubanConflictList)));
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }*/
    }

    /**
     * 获取某一课程的详细冲突
     *
     * @param term
     * @param courseId
     * @return
     */
    public ZoubanConflict findOneCourseConflict(String term, ObjectId courseId) {
        BasicDBObject query = new BasicDBObject();
        query.append("te", term);
        query.append("cid", courseId);
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query, Constant.FIELDS);
        if (dbObject != null)
            return new ZoubanConflict((BasicDBObject) dbObject);
        return new ZoubanConflict();
    }

    /**
     * 获取全年级的课程id-冲突数map,并按冲突数量排序
     *
     * @param term
     * @param gradeId
     * @return
     */
    public Map<String, Integer> findConflictListByGrade(String term, ObjectId gradeId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId);
        BasicDBObject sort = new BasicDBObject("cc", -1);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query, Constant.FIELDS, sort);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                ZoubanConflict conflict = new ZoubanConflict((BasicDBObject) dbObject);
                map.put(conflict.getCourseId().toString(), conflict.getConflictCount());
            }
        }

        return map;
    }

    /**
     * 获取某一段内的全部冲突
     *
     * @param term
     * @param groupId
     * @return
     */
    public List<ZoubanConflict> findConflictListByGroup(String term, ObjectId groupId) {
        List<ZoubanConflict> zoubanConflicts = new ArrayList<ZoubanConflict>();
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", groupId);
        List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query, Constant.FIELDS);
        if (dbObjectList != null && !dbObjectList.isEmpty()) {
            for (DBObject dbObject : dbObjectList) {
                zoubanConflicts.add(new ZoubanConflict((BasicDBObject) dbObject));
            }
        }
        return zoubanConflicts;
    }

    /**
     * 删除数据
     *
     * @param term
     * @param gradeId
     */
    public void removeConflict(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query);
    }

    public int countConflict(String term, ObjectId gradeId) {
        BasicDBObject query = new BasicDBObject()
                .append("te", term)
                .append("gid", gradeId);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_CONFLICT, query);
        return count;
    }
}
