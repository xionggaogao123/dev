package com.db.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ExamRoomDTO;
import com.pojo.exam.ExamRoomEntry;
import com.sys.constants.Constant;

/**
 * 考场资源Dao
 *
 * @author cxy
 *         2015-7-29 16:38:42
 */
public class ExamRoomDao extends BaseDao {
    /**
     * 添加一条考场资源信息
     *
     * @param e
     * @return
     */
    public ObjectId addExamRoomEntry(ExamRoomEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ROOM, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据Id查询一个特定的考场资源信息
     *
     * @param id
     * @return
     */
    public ExamRoomEntry getExamRoomEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ROOM, query, Constant.FIELDS);
        if (null != dbo) {
            return new ExamRoomEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 删除一条考场资源
     *
     * @param id
     */
    public void deleteExamRoom(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ROOM, query, updateValue);
    }

    /**
     * 根据ID更新一条考场资源信息
     */
    public void updateExamRoom(ObjectId id, String examRoomNumber, String examRoomName, int examRoomSitNumber, String examRoomPostscript) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("ernu", examRoomNumber)
                        .append("erna", examRoomName)
                        .append("ersn", examRoomSitNumber)
                        .append("erps", examRoomPostscript));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ROOM, query, updateValue);

    }

    /**
     * 查询本校所有的考场资源记录
     *
     * @return
     */
    public List<ExamRoomEntry> queryExamRoomsBySchoolId(ObjectId schoolId) {
        BasicDBObject query = new BasicDBObject();
        query.append("ir", Constant.ZERO)
                .append("scid", schoolId);
        DBObject orderBy = new BasicDBObject("ernu", Constant.ASC);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ROOM, query, Constant.FIELDS, orderBy);
        List<ExamRoomEntry> resultList = new ArrayList<ExamRoomEntry>();
        for (DBObject dbObject : dbObjects) {
            ExamRoomEntry entry = new ExamRoomEntry((BasicDBObject) dbObject);
            resultList.add(entry);
        }
        return resultList;
    }

    /**
     * 查询本场考试所用的激活的所有的考场记录信息
     *
     * @return
     */
    public List<ExamRoomDTO> queryExamRoomsByExamId(ObjectId examId) {
        ExamEntry ee = new ExamDao().load(examId.toString());
        Map<String, Map<String, Object>> rooms = ee.getRoomUsed();
        List<ExamRoomDTO> resultList = new ArrayList<ExamRoomDTO>();
        Iterator<Entry<String, Map<String, Object>>> it = rooms.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Map<String, Object>> room = it.next();
            Map<String, Object> roomMap = room.getValue();
            ExamRoomDTO e = new ExamRoomDTO();
            e.setExamRoomName(roomMap.get("name").toString());
            e.setId(roomMap.get("id").toString());
            resultList.add(e);
        }


        return resultList;
    }
}

