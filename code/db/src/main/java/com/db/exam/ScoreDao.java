package com.db.exam;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.*;
import com.sys.constants.Constant;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * 成绩信息数据库操作
 * Created by Caocui on 2015/7/22.
 */
public class ScoreDao extends BaseDao {

    private static final String SCORE_TYPE_NORMAL = "normal";
    private static final String SCORE_TYPE_ABSENT = "absent";
    private static final String SCORE_TYPE_EXEMPTION = "exemption";


    /**
     * 修改考试成绩
     *
     * @param scoreDTO
     */
    public void updateScore(ScoreDTO scoreDTO) {
        Map<String, SubjectScoreDTO> itemList = scoreDTO.getExamScore();
        if (itemList != null) {
            DBObject query;
            DBObject updateValue;
            Iterator<Map.Entry<String, SubjectScoreDTO>> data = itemList.entrySet().iterator();
            Map.Entry<String, SubjectScoreDTO> entry;
            while (data.hasNext()) {
                //每次循环修改单科成绩
                entry = data.next();
                query = new BasicDBObject(Constant.ID, new ObjectId(scoreDTO.getId()))
                        .append("sList.subId", new ObjectId(entry.getKey()));
                updateValue = new BasicDBObject(Constant.MONGO_SET,
                        new BasicDBObject("sList.$.subS", Double.parseDouble(entry.getValue().getScore())));
                update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, updateValue);
            }

            //修改总分
            query = new BasicDBObject(Constant.ID, new ObjectId(scoreDTO.getId()));
            ScoreEntry scoreEntry = new ScoreEntry((BasicDBObject) this.findOne(MongoFacroty.getAppDB(),
                    Constant.COLLECTION_SCORE, query, Constant.FIELDS));
            updateValue = new BasicDBObject(Constant.MONGO_SET,
                    new BasicDBObject("suc", ScoreDTO.getSumScore(scoreEntry.getExamScore())));
            update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, updateValue);
        }
    }

    /**
     * 修改总分和及格分
     *
     * @param examId
     * @param examSubjectDTO
     */
    public void updateExamScoreFullMarks(ObjectId examId, ExamSubjectDTO examSubjectDTO) {
        BasicDBObject query = new BasicDBObject("exId", examId).append("sList.subId", new ObjectId(examSubjectDTO.getSubjectId()));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("sList.$.full", examSubjectDTO.getFullMarks()).append("sList.$.fail", examSubjectDTO.getFullMarks() * 0.6f));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, updateValue);
    }

    /**
     * 添加考试成绩
     *
     * @param scoreEntries
     */
    public void add(List<ScoreEntry> scoreEntries) {
        if (scoreEntries.isEmpty()) {
            return;
        }
        List<DBObject> objects = new ArrayList<DBObject>(scoreEntries.size());
        for (ScoreEntry scoreEntry : scoreEntries) {
            objects.add(scoreEntry.getBaseEntry());
        }
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, objects);
    }
    /**
	 * 批量添加考试成绩
	 * @param e
	 * @return
	 */
	public void addScoreEntryList(List<DBObject> list){
		save(MongoFacroty.getAppDB(),Constant.COLLECTION_SCORE, list);
	}
    /**
     * 导入学生成绩
     *
     * @param scoreDTO
     */
    public void updateByImport(List<ScoreDTO> scoreDTO) {
        for (ScoreDTO score : scoreDTO) {
            updateScore(score);
        }
    }

    /**
     * 查询缺免考的总数
     *
     * @param examId
     * @param classId
     * @param showType
     * @return
     */
    public int countQmExamStatus(final String examId, final String classId, final String showType, final String subjectId) {
        BasicDBObject basicDBObject = new BasicDBObject("exId", new ObjectId(examId));
        //是否有 班级 条件
        if (StringUtils.isNotEmpty(classId)) {
            basicDBObject.append("cId", new ObjectId(classId));
        }


        if (SCORE_TYPE_NORMAL.equals(showType)) {
            basicDBObject.append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("subId", new ObjectId(subjectId)).append("st", 0)));
        } else if (SCORE_TYPE_ABSENT.equals(showType)) {
            basicDBObject.append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("subId", new ObjectId(subjectId)).append("st", 1)));
        } else if (SCORE_TYPE_EXEMPTION.equals(showType)) {
            basicDBObject.append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("subId", new ObjectId(subjectId)).append("st", 2)));
        } else {
            basicDBObject.append("sList.subId", new ObjectId(subjectId));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, basicDBObject);
    }

    /**
     * 查询缺免考数据
     *
     * @param examId
     * @param classId
     * @param showType
     * @param page
     * @return
     */
    public List<ScoreEntry> loadQMExamStatus(final String examId, final String classId, final String showType, final String subjectId, int page) {
        BasicDBObject query = new BasicDBObject("exId", new ObjectId(examId));
        //是否有 班级 条件
        if (StringUtils.isNotEmpty(classId)) {
            query.append("cId", new ObjectId(classId));
        }

        //显示数据类型

        if (SCORE_TYPE_NORMAL.equals(showType)) {
            query.append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("subId", new ObjectId(subjectId)).append("st", Constant.ZERO)));
        } else if (SCORE_TYPE_ABSENT.equals(showType)) {
            query.append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("subId", new ObjectId(subjectId)).append("st", Constant.ONE)));
        } else if (SCORE_TYPE_EXEMPTION.equals(showType)) {
            query.append("sList", new BasicDBObject(Constant.MONGO_ELEMATCH, new BasicDBObject("subId", new ObjectId(subjectId)).append("st", Constant.TWO)));
        } else {
            query.append("sList.subId", new ObjectId(subjectId));
        }
        List<DBObject> results = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_SCORE,
                query,
                Constant.FIELDS,
                new BasicDBObject().append("cId", Constant.ASC),
                (page - 1) * Constant.TEN,
                Constant.TEN);
        List<ScoreEntry> resultList = new ArrayList<ScoreEntry>(results.size());
        ScoreEntry scoreEntry;
        for (DBObject dbObject : results) {
            scoreEntry = new ScoreEntry((BasicDBObject) dbObject);
            resultList.add(scoreEntry);
        }
        return resultList;
    }

    /**
     * 修改缺免考状态
     *
     * @param scoreId
     * @param subjectId
     * @param showType
     */
    public void updateQMStatus(String scoreId, String subjectId, int showType) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(scoreId))
                .append("sList.subId", new ObjectId(subjectId));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("sList.$.st", showType)
                        .append("sList.$.subNm", Constant.ZERO)
                        .append("sList.$.abs", showType == 1 ? 1 : 0)
                        .append("sList.$.exemp", showType == 2 ? 1 : 0));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, updateValue);

        //修改总分
        query = new BasicDBObject(Constant.ID, new ObjectId(scoreId));
        ScoreEntry scoreEntry = new ScoreEntry((BasicDBObject) this.findOne(MongoFacroty.getAppDB(),
                Constant.COLLECTION_SCORE, query, Constant.FIELDS));
        updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject("suc", ScoreDTO.getSumScore(scoreEntry.getExamScore())));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, updateValue);
    }

    /**
     * 根据考场编码获取考试信息
     *
     * @param examRoomId
     */
    public List<ScoreEntry> findScoreListByExamRoomId( final String examId, String roomId) {
        List<DBObject> results = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_SCORE,
                new BasicDBObject().append("exId", new ObjectId(examId)).append("rid", new ObjectId(roomId)),
                Constant.FIELDS, new BasicDBObject("rid", Constant.ASC).append("cId", Constant.ASC));
        List<ScoreEntry> resultList = new ArrayList<ScoreEntry>(results.size());
        ScoreEntry scoreEntry;
        for (DBObject dbObject : results) {
            scoreEntry = new ScoreEntry((BasicDBObject) dbObject);
            resultList.add(scoreEntry);
        }
        return resultList;
    }

    /**
     * 根据条件获取成绩信息
     *
     * @param examId   考试编码
     * @param classIds 班级编码
     * @param order    排序  class OR score
     * @return
     */
    public List<ScoreEntry> findScoreList(final String examId, final String[] classIds, final String order) {
        DBObject orderObject;

        //排序
        if (order == null || "class".equals(order)) {
            orderObject = new BasicDBObject().append("cna", Constant.ASC).append("suc", Constant.DESC);
        } else {
            orderObject = new BasicDBObject().append("suc", Constant.DESC).append("cna", Constant.ASC);
        }

        //班级查询条件
        List<ObjectId> clazzId = new ArrayList<ObjectId>(classIds.length);
        for (String id : classIds) {
            clazzId.add(new ObjectId(id));
        }
        List<DBObject> results = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_SCORE,
                new BasicDBObject().append("exId", new ObjectId(examId))
                        .append("cId", new BasicDBObject(Constant.MONGO_IN, clazzId)),
                Constant.FIELDS, orderObject);
        List<ScoreEntry> resultList = new ArrayList<ScoreEntry>(results.size());
        ScoreEntry scoreEntry;
        for (DBObject dbObject : results) {
            scoreEntry = new ScoreEntry((BasicDBObject) dbObject);
            resultList.add(scoreEntry);
        }
        return resultList;
    }

    /**
     * 本次考试所有考生的考试科目成绩中添加一科目成绩
     *
     * @param examId
     * @param dto
     */
    public void addScoreSubject(final ObjectId examId, final ExamSubjectDTO dto) {
        DBObject query = new BasicDBObject("exId", examId);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH,
                new BasicDBObject("sList", new SubjectScoreEntry(new ObjectId(dto.getSubjectId()), dto.getSubjectName(),
                        Constant.ZERO, Constant.ZERO, dto.getFullMarks(), dto.getFullMarks() * 0.6f, Constant.ZERO, Constant.ZERO).getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, updateValue);
    }

    /**
     * 删除考试科目成绩
     *
     * @param examId
     * @param subjectId
     */
    public void deleteExamSubject(final ObjectId examId, final List<ObjectId> subjectId) {
        BasicDBObject update = new BasicDBObject(Constant.MONGO_PULL,
                new BasicDBObject("sList", new BasicDBObject("subId", new BasicDBObject(Constant.MONGO_NOTIN, subjectId))));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, new BasicDBObject("exId", examId), update);
    }

    /**
     * 获取考试学生总人数
     *
     * @param examId
     * @return
     */
    public int countExamStudent(final ObjectId examId) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, new BasicDBObject("exId", examId));
    }

    /**
     * 获取已经排过考场的学生总记录
     *
     * @param examId
     * @return
     */
    public int countStudentNotArranged(final ObjectId examId) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, new BasicDBObject("exId", examId).append("rid", null));
    }

    /**
     * 获取考试的排考场情况
     *
     * @param examId
     * @param classId
     * @param roomId
     * @return
     */
    public List<ScoreDTO> loadArrangeInfo(final String examId, final String classId, final String roomId) {
        BasicDBObject query = new BasicDBObject("exId", new ObjectId(examId));
        BasicDBObject orderBy;
        if (StringUtils.isNotEmpty(classId)) {
            orderBy = new BasicDBObject("cId", Constant.ASC).append("rid", Constant.ASC).append("enm", Constant.ASC);
            if (!"ALL".equals(classId)) {
                query.append("cId", new ObjectId(classId));
            }
        } else if (StringUtils.isNotEmpty(roomId)) {
            orderBy = new BasicDBObject("ern", Constant.ASC).append("enm", Constant.ASC).append("cId", Constant.ASC);
            if (!"ALL".equals(roomId)) {
                query.append("rid", new ObjectId(roomId));
            }
        } else {
            return Collections.EMPTY_LIST;
        }

        List<DBObject> results = find(MongoFacroty.getAppDB(),
                Constant.COLLECTION_SCORE,
                query,
                new BasicDBObject(Constant.ID, Constant.EMPTY)
                        .append("stuNm", Constant.EMPTY)
                        .append("rid", Constant.EMPTY)
                        .append("stuId", Constant.EMPTY)
                        .append("cId", Constant.EMPTY)
                        .append("exId", Constant.EMPTY)
                        .append("cna", Constant.EMPTY)
                        .append("enm", Constant.EMPTY)
                        .append("rna", Constant.EMPTY)
                        .append("ern", Constant.EMPTY)
                        .append("suc", Constant.EMPTY), orderBy);
        List<ScoreDTO> resultList = new ArrayList<ScoreDTO>(results.size());
        for (DBObject dbObject : results) {
            resultList.add(new ScoreDTO(new ScoreEntry((BasicDBObject) dbObject)));
        }
        return resultList;
    }

    /**
     * 清空编排考场信息
     *
     * @param examId
     */
    public void clearExamRoom(final ObjectId examId) {
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE,
                new BasicDBObject("exId", examId),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("rid", null).append("rna", "").append("enm", "").append("ern", "")));
    }

    /**
     * 更新学生考场编排信息
     * @param examRoomDTO
     * @param studentId
     * @param examNumber
     */
    public void updateExamSeat(ObjectId examId,ExamRoomDTO examRoomDTO, ObjectId studentId, final String examNumber) {
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE,
                new BasicDBObject("stuId", studentId).append("exId", examId),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("rid", new ObjectId(examRoomDTO.getId()))
                        .append("rna", examRoomDTO.getExamRoomName())
                        .append("enm", examRoomDTO.getExamRoomNumber() + Constant.SEPARATE_LINE + examNumber)
                        .append("ern", examRoomDTO.getExamRoomNumber())));
    }
    
    /**
     * 根据校名词或班级名称排序查看
     * Created by Zoukai on 2015/11
     * @param schoolRanking "sr" 学校排名
     */
    public List<ScoreEntry> findByRanking(ObjectId exid,int skip, int size){
    	if(!"".equals(exid)){
    	DBObject query=new BasicDBObject("exId",exid);
    	DBObject orderBy =new BasicDBObject("sr", Constant.ASC); 
    	
    		List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, Constant.FIELDS,orderBy,skip, size);
    		List<ScoreEntry> list=new ArrayList<ScoreEntry>();
    		for (DBObject dbobjectlist : dbObjectList) {
    			ScoreEntry score=new ScoreEntry((BasicDBObject) dbobjectlist);
				list.add(score); 
			}
    		return list;
    	}
    	return null;
    }
    
    /**
     * 根据考试编码查询考试信息
     * @param examId "exId"
     */
    public List<ScoreEntry> findByExid(ObjectId exid,int skip, int size){
    		DBObject query=new BasicDBObject("exId",exid);
        	List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, Constant.FIELDS,new BasicDBObject(),skip, size);
        	List<ScoreEntry> list=new ArrayList<ScoreEntry>();
        	for (DBObject dbobjectlist : dbObjectList) {
    			ScoreEntry score=new ScoreEntry((BasicDBObject) dbobjectlist);
    			list.add(score); 
    		}
    		return list;
    	
    }
    
    /**
     * 根据考试编码查询考试信息
     * @param exId
     */
    public List<ScoreEntry> findByExid(ObjectId exid){
    		DBObject query=new BasicDBObject("exId",exid);
        	List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE, query, Constant.FIELDS,new BasicDBObject());
        	List<ScoreEntry> list=new ArrayList<ScoreEntry>();
        	for (DBObject dbobjectlist : dbObjectList) {
    			ScoreEntry score=new ScoreEntry((BasicDBObject) dbobjectlist);
    			list.add(score); 
    		}
    		return list;
    	
    }
    
    /**
     * 根据ID更新学校Ranking
     */
    public void updateSchoolRankById(ObjectId id , int rank){
    	 update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE,
                 new BasicDBObject(Constant.ID, id),
                 new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sr",rank)));
    	
    }

    /**
     * 更新学生姓名
     * @param stuId
     * @param stuName
     */
    public void updateStuName(ObjectId stuId, String stuName){
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_SCORE,
                new BasicDBObject("stuId", stuId),
                new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("stuNm",stuName)));
    }
}
