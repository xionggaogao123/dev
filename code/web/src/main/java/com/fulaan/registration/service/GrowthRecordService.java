package com.fulaan.registration.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.factory.MongoFacroty;
import com.db.registration.GrowthRecordDao;
import com.db.registration.QualityDao;
import com.db.registration.SubQualityDao;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ScoreEntry;
import com.pojo.registration.GrowthRecordEntry;
import com.pojo.registration.QualityEntry;
import com.pojo.registration.QualityObject;
import com.pojo.registration.SubQualityEntry;
import com.pojo.registration.SubQualityObject;
import com.sys.constants.Constant;

/**
 * 成长档案Service
 * @author cxy
 * 2015-11-25 15:25:18
 */
@Service
public class GrowthRecordService {
	
	private GrowthRecordDao growthRecordDao = new GrowthRecordDao();
	
	private QualityDao qualityDao = new QualityDao();
	
	private SubQualityDao subQualityDao = new SubQualityDao();
	
	
	/**
     * 添加一条成长档案
     * @param e
     * @return
     */
    public ObjectId addGrowthRecordEntry(GrowthRecordEntry e){
        return growthRecordDao.addGrowthRecordEntry(e);
    }
    
    /**
     * 删除一条成长档案
     * @param id
     */
    public void deleteGrowthRecordEntry(ObjectId id){
    	growthRecordDao.deleteGrowthRecordEntry(id);
    }
    
    /**
	 * 根据学期和UserId查询一个特定的成长档案
	 */
	public GrowthRecordEntry getGrowthRecordEntryByTermAndUserId(String termType, ObjectId userId){
		return growthRecordDao.getGrowthRecordEntryByTermAndUserId(termType, userId);
	}
	
	 /**
	  * 根据ID更新一条成长档案
	  */
	public void updateGoodPerformanceById(ObjectId id,String goodPerformance){
		growthRecordDao.updateGoodPerformanceById(id, goodPerformance);
	}
	
	/**
	  * 根据ID更新一条成长档案
	  */
	public void updateMasterCommentById(ObjectId id,String masterComment){
		growthRecordDao.updateMasterCommentById(id, masterComment);
	}
	
	/**
	  * 根据ID更新一条成长档案
	  */
	public void updateQualityEducationById(ObjectId id,List<QualityObject> qualityEducationList){
		growthRecordDao.updateQualityEducationById(id, qualityEducationList);
	}
	/**
	 * 去重查询有哪些学期有考试
	 * @param schoolId
	 * @return
	 */
	public List<String> distinctExamTerm(ObjectId schoolId){
		List<String> resultList = new ArrayList<String>();
		BasicDBList dbList = growthRecordDao.distinctExamTerm(schoolId);
		for(Object o : dbList){
			resultList.add(o.toString());
		}
		return resultList;
	}
	
	/**
	 * 去重查询有哪些学期有考试(为某个特定学生进行查询，用于详情页)
	 * @param schoolId
	 * @return
	 */
	public List<String> distinctExamTermForUniqueStudent(ObjectId schoolId, ObjectId studentId){
		List<String> resultList = new ArrayList<String>();
		BasicDBList dbList = growthRecordDao.distinctExamTermForUniqueStudent(schoolId, studentId);
		List<ObjectId> examIds = new ArrayList<ObjectId>();
		for(Object o : dbList){
			examIds.add((ObjectId)o);
		}
		BasicDBList termDbList = growthRecordDao.distinctExamTermFromExamIdCollection(examIds);
		for(Object o : termDbList){
			resultList.add(o.toString());
		}
		return resultList;
	}
	
	
	
	/**
     * 通过schoolId和学期查询该学期所有考试
     * @param schoolId
     */
    public List<ExamEntry> queryExamBySchoolIdAndTerm(ObjectId schoolId, ObjectId gradeId, String term) {
       return growthRecordDao.queryExamBySchoolIdAndTerm(schoolId, gradeId, term);
    }
    /**
     * 获取该学校的素质教育未填写状态List(供构造GrowthRecord使用)
     * @param schoolId
     * @return
     */
    public List<QualityObject> getEmptyQualityListBySchoolId(ObjectId schoolId){
    	List<QualityObject> resultList = new ArrayList<QualityObject>();
    	for(QualityEntry qe : qualityDao.queryQuality(schoolId)){
    		List<SubQualityObject> sqList = new ArrayList<SubQualityObject>();
    		for(SubQualityEntry sqe : subQualityDao.querySubQualityList(qe.getID())){
    			SubQualityObject sqo = new SubQualityObject(sqe.getName(), sqe.getRequirement());
    			sqList.add(sqo);
    		}
    		QualityObject qo = new QualityObject(qe.getName(), sqList);
    		resultList.add(qo);
    	}
    	return resultList;
    }
    
    /**
	 * 查询所给学期有哪些该同学参加的考试(为某个特定学生进行查询，用于详情页)
	 * @param schoolId
	 * @return
	 */
	public List<ExamEntry> findExamListByTermForUniqueStudent(ObjectId schoolId, ObjectId studentId, String term){
		BasicDBList dbList = growthRecordDao.distinctExamTermForUniqueStudent(schoolId, studentId);
		List<ObjectId> examIds = new ArrayList<ObjectId>();
		for(Object o : dbList){
			examIds.add((ObjectId)o);
		}
		return growthRecordDao.findExamListByTermForUniqueStudent(examIds, term);
	}
	
	/**
	 * 去重查询有某个学生哪些学期有素质教育
	 * @param schoolId
	 * @return
	 */
	public List<String> distinctGrowthTermForUniqueStudent(ObjectId studentId){
		List<String> resultList = new ArrayList<String>();
		BasicDBList dbList = growthRecordDao.distinctGrowthTermForUniqueStudent(studentId);
		for(Object o : dbList){
			resultList.add(o.toString());
		}
		return resultList;
	}
	
	 /**
     * 获取区域联考考试信息
     *
     * @param examId
     * @param stydentId
     * @return
     */
    public ScoreEntry getScoreEntryForUniqueStudent(ObjectId examId, ObjectId studentId) {
		return growthRecordDao.getScoreEntryForUniqueStudent(examId, studentId);
	}
    
    /**
     * 删除一条成长档案
     * @param id
     */
    public void removeGrowthRecordBySchoolId(ObjectId schoolId, String termType){
    	growthRecordDao.removeGrowthRecordBySchoolId(schoolId, termType);
    }
}
