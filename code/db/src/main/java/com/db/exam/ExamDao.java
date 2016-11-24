package com.db.exam;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.*;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 考试管理数据库操作
 * Created by Caocui on 2015/7/22.
 */
public final class ExamDao extends BaseDao {
    /**
     * 获取考试科目信息
     *
     * @param examId
     * @param subjectId
     * @return
     */
    public ExamSubjectEntry loadExamSubject(final String examId, final String subjectId) {
        ExamEntry entry = load(examId);
        if (entry != null) {
            for (ExamSubjectEntry examSubjectEntry : entry.getExamSubject()) {
                if (examSubjectEntry.getSubjectId().toString().equals(subjectId)) {
                    return examSubjectEntry;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * pageCount
     *
     * @param gradeId
     * @return
     */
    public int loadExamCount(final String gradeId) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, new BasicDBObject().append("gId", new ObjectId(gradeId))
                .append("df", ExamEntry.FLAG_NOT_DELETE).append("isGra", 1));
    }

    /**
     * 根据条件获取考试信息列表
     *
     * @param gradeId 年级编码
     * @return 考试列表
     */
    public List<ExamEntry> findExamByGradeId(final String gradeId, int page) {
        List<DBObject> results = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_EXAM,
                new BasicDBObject().append("gId", new ObjectId(gradeId)).append("isGra", 1)
                        .append("df", ExamEntry.FLAG_NOT_DELETE),
                Constant.FIELDS,
                new BasicDBObject().append("ed", Constant.DESC).append(Constant.ID, Constant.ASC), (page - 1) * Constant.TEN, Constant.TEN);
        List<ExamEntry> resultList = new ArrayList<ExamEntry>(results.size());
        ExamEntry examEntry;
        for (DBObject dbObject : results) {
            examEntry = new ExamEntry((BasicDBObject) dbObject);
            resultList.add(examEntry);
        }
        return resultList;
    }

    /**
     * 加载详细信息
     *
     * @param id
     * @return
     */
    public ExamEntry load(final String id) {
        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM,
                new BasicDBObject().append(Constant.ID, new ObjectId(id)), Constant.FIELDS);
        return null != dbObject ? new ExamEntry((BasicDBObject) dbObject) : null;
    }

    /**
     * 删除考试信息
     *
     * @param id
     */
    public void delete(final String id) {
        update(MongoFacroty.getAppDB(),
                Constant.COLLECTION_EXAM,
                new BasicDBObject().append(Constant.ID, new ObjectId(id)),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("df", ExamEntry.FLAG_DELETED)));
    }

    /**
     * 更新考试信息
     *
     * @param dto
     */
    public void update(final ExamDTO dto) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(dto.getId()));
        ExamEntry examEntry = dto.getEntry();
        BasicDBObject entry = new BasicDBObject().append("name", dto.getName())
                .append("ed", DateTimeUtils.stringToDate(dto.getDate(),
                        DateTimeUtils.DATE_YYYY_MM_DD).getTime())
                .append("date", dto.getDate())
                .append("gId", new ObjectId(dto.getGradeId()))
                .append("gna", dto.getGradeName())
                .append("et", dto.getExamType())
                .append("type", ExamDTO.examTypeNames[dto.getExamType()])
                .append("cList", examEntry.getCList())
                .append("sList", examEntry.getSubjectList())
                .append("rm", dto.getRemark())
                .append("ty", dto.getType());
        DBObject values = new BasicDBObject(Constant.MONGO_SET, entry);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, values);
    }

    /**
     * 添加考试信息
     *
     * @param entry
     * @return
     */
    public ObjectId save(final ExamEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 添加考试科目
     *
     * @param examId
     * @param examSubjectDTO
     */
    public void saveExamSubject(final ObjectId examId, final ExamSubjectDTO examSubjectDTO) {
        ExamSubjectEntry entry = new ExamSubjectEntry(
                new ObjectId(examSubjectDTO.getSubjectId()),
                examSubjectDTO.getSubjectName(),
                examSubjectDTO.getFullMarks(),
                examSubjectDTO.getYouXiuScore(),
                examSubjectDTO.getFailScore(),
                examSubjectDTO.getDiFenScore(),
                examSubjectDTO.getTime(),
                examSubjectDTO.getExamDate(),
                examSubjectDTO.getWeekDay(),
                examSubjectDTO.getOpenStatus(),
                examSubjectDTO.getOpenBeginTime(),
                examSubjectDTO.getOpenEndTime());
        DBObject query = new BasicDBObject(Constant.ID, examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH,
                new BasicDBObject("es", entry.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, updateValue);
    }

    /**
     * 删除考试科目
     *
     * @param leaveData
     * @param examId
     */
    public void deleteExamSubject(final List<ObjectId> leaveData, final ObjectId examId) {
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL,
                new BasicDBObject("es", new BasicDBObject("sid", new BasicDBObject(Constant.MONGO_NOTIN, leaveData))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, new BasicDBObject(Constant.ID, examId), update);
    }

    /**
     * 更新考试科目信息
     *
     * @param examId
     * @param examSubjectDTO
     */
    public void updateExamSubject(final ObjectId examId, final ExamSubjectDTO examSubjectDTO) {
        DBObject query = new BasicDBObject(Constant.ID, examId)
                .append("es._id", new ObjectId(examSubjectDTO.getId()));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("es.$.sna", examSubjectDTO.getSubjectName()).
                        append("es.$.sid", new ObjectId(examSubjectDTO.getSubjectId())).
                        append("es.$.fm", examSubjectDTO.getFullMarks()).
                        append("es.$.yx", examSubjectDTO.getYouXiuScore()).
                        append("es.$.fl", examSubjectDTO.getFailScore()).
                        append("es.$.df", examSubjectDTO.getDiFenScore()).
                        append("es.$.tm", examSubjectDTO.getTime()).
                        append("es.$.ed", examSubjectDTO.getExamDate()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, updateValue);
    }

    /**
     * 更新考试科目成绩录入开关信息
     *
     * @param examSubjectDTO
     */
    public void updateExamSubjectOpenTime(final ExamSubjectDTO examSubjectDTO) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(examSubjectDTO.getExamId()));
        //如果没有指定编码，进行批量修改
        if (StringUtils.isEmpty(examSubjectDTO.getId())) {
            ExamEntry entry = this.load(examSubjectDTO.getExamId());
            for (ExamSubjectEntry entrySubject : entry.getExamSubject()) {
                query.append("es._id", entrySubject.getID());
                DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                        new BasicDBObject("es.$.os", examSubjectDTO.getOpenStatus()).
                                append("es.$.obt", examSubjectDTO.getOpenBeginTime()).
                                append("es.$.oet", examSubjectDTO.getOpenEndTime()));
                update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, updateValue);
            }
        } else {
            query.append("es._id", new ObjectId(examSubjectDTO.getId()));
            DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                    new BasicDBObject("es.$.os", examSubjectDTO.getOpenStatus()).
                            append("es.$.obt", examSubjectDTO.getOpenBeginTime()).
                            append("es.$.oet", examSubjectDTO.getOpenEndTime()));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, updateValue);
        }
    }

    /**
     * 锁定分配考场
     *
     * @param examId
     */
    public void lockArrange(final String examId, int isLock) {
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM,
                new BasicDBObject(Constant.ID, new ObjectId(examId)),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("lk", isLock)));
    }

    /**
     * 删除考试使用的考场信息
     */
    public void deleteExamRoom(final ObjectId examId) {
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM,
                new BasicDBObject(Constant.ID, examId),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ue", new BasicDBList())));
    }

    /**
     * 向考试考场中添加使用考场
     *
     * @param examId
     * @param arrangedNum
     * @param dto
     */
    public void addExamRoom(ObjectId examId, int arrangedNum, ExamRoomDTO dto) {
        DBObject query = new BasicDBObject(Constant.ID, examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH,
                new BasicDBObject("ue", new BasicDBObject("rid", dto.getId())
                        .append("rna",dto.getExamRoomName()).append("an", arrangedNum)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM, query, updateValue);
    }

    /**
     * 修改考试的考场信息是否已经编排完成
     * @param examId
     * @param status
     */
    public void updateExamroomArrange(ObjectId examId,int status){
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM,
                new BasicDBObject(Constant.ID, examId),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ac", status)));
    }
}
