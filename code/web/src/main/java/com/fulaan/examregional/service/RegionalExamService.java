package com.fulaan.examregional.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.educationbureau.EducationBureauDao;
import com.db.exam.ExamDao;
import com.db.exam.ScoreDao;
import com.db.examregional.RegionalExamDao;
import com.db.factory.MongoFacroty;
import com.db.school.ClassDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ScoreEntry;
import com.pojo.exam.SubjectScoreEntry;
import com.pojo.examregional.EducationSubject;
import com.pojo.examregional.ExamSummaryEntry;
import com.pojo.examregional.RegionalExamEntry;
import com.pojo.examregional.RegionalSchItem;
import com.pojo.examregional.RegionalSubjectItem;
import com.pojo.examregional.SubjectDetails;
import com.pojo.school.ClassEntry;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.school.Subject;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

@Service
public class RegionalExamService {
	private RegionalExamDao regionalExamDao = new RegionalExamDao();
	private ExamDao examDao = new ExamDao();
	private ClassDao classDao = new ClassDao();
	private UserDao userDao = new UserDao();
	private ScoreDao scoreDao = new ScoreDao();
	private SchoolDao schoolDao = new SchoolDao();
	private EducationBureauDao educationDao = new EducationBureauDao();
	
	
	
	/**
     * 获取区域联考Entry
     *
     * @return
     */
    public RegionalExamEntry loadRegionalExamEntry(ObjectId examId) {
        return regionalExamDao.getRegionalExamEntryInfo(examId);
    } 
	
	/**
     * 获取考试成绩详情
     *
     * @return
     */
    public List<ScoreEntry> loadExam(String examId) {
        return regionalExamDao.load(examId);
    } 
    /**
     * 获取考试成绩详情(分页)
     *
     * @return
     */
    
    public List<ScoreEntry> loadExamByPage(String examId ,int skip, int size) {
        return regionalExamDao.loadByPage(examId, skip, size);
    } 
    /**
     * 获得区域联考学生人数
     * @param examId
     * @return
     */
    public int countScoreEntryService(ObjectId examId){
    	return regionalExamDao.countScoreEntry(examId);
    }
    /**
     * 获取考试成绩详情
     *
     * @return
     */
    public ScoreEntry loadPerformanceById(String perfodmanceId) {
        return regionalExamDao.loadPerformanceById(perfodmanceId);
    } 
	
    /**
	 * 根据ID更新一条performance，用于区域联考excel成绩导入
	 */
	public void updatePerformanceById(String performanceId,double sumScore,List<SubjectScoreEntry> subjectScoreList){
		regionalExamDao.updatePerformanceById(performanceId, sumScore, subjectScoreList);
	}
    
	/**
     * 查询考试成绩信息
	 */
    public ExamEntry findExamEntryById(ObjectId id){
		return regionalExamDao.getExamEntryInfo(id);
	}
    /**
     * 
     * @param objectId
     * @return
     */
    public RegionalExamEntry findRegionalExamById(ObjectId id) {
		return regionalExamDao.getRegionalExamEntryInfo(id);
	}
    /**
  	 * 根据ID更新是否提交到教育局
  	 */
  	public void updateJointexamById(String jiontExamId, List<RegionalSchItem> schoolList){
  		regionalExamDao.updatejiontExamById(jiontExamId, schoolList);
  	}
	/**
	 * 根据用户id获取当前所属教育局的id(l)
	 * @param userId
	 * @return
	 */
	public ObjectId getEducationId(ObjectId userId){
		ObjectId educationId = educationDao.selEducationByUserId(userId).getID();
		return educationId;
	}
	
	 /**
     * 根据教育局id获取年级下拉列表(l)
     * @param edcationId
     * @return
     */
    public List<String> getGradeNameListByLuJiang(ObjectId edcationId){
    	List<String> list = new ArrayList<String>();
    	List<Grade> gradeList =  educationDao.selEducationById(edcationId).getGradeList();
    	Iterator<Grade> it = gradeList.iterator();
    	while(it.hasNext()){
    		Grade gname = it.next();
    		String name = gname.getName();
    		list.add(name);
    	}
    	return list;
    }
	
    /**
     * 根据学校id查询年级编码(l)
     * @param schoolId
     * @return
     */
 	public List<ObjectId> getGradeIdList(ObjectId schoolId){
 		List<ObjectId> resultList = new ArrayList<ObjectId>();
 		List<Grade> gradeList = schoolDao.getSchoolEntry(schoolId,Constant.FIELDS).getGradeList();
 		Iterator<Grade> it = gradeList.iterator();
 		while(it.hasNext()){
    		Grade gname = it.next();
    		ObjectId id = gname.getID();
    		resultList.add(id);
    	}
    	return resultList;
 	}

	/**
	 * 教育局
	 *获得区域联考的本年的学期信息
	 */
	public List<Map<String,Object>> tremFind(ObjectId eductionId){
		Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String tt=formatter.format(date);
		int e = Integer.parseInt(tt);
		int ee=e+1;
		List<Map<String,Object>> lists=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		Set<String> set=new HashSet<String>();
		List<String> list=regionalExamDao.findTrem(eductionId);
		for(String str:list){
			set.add(str);
 		}
		if(set.contains(e+"-"+ee+"学年第一学期")||set.contains(e+"-"+ee+"学年第二学期")){
			for(String s:set){
				map.put("trem", s);
			}
			lists.add(map);
		}
		return lists;
		
	}
	
	/**
	 * 学校
	 *获得学校区域联考的本年的学期信息
	 */
	public List<Map<String,Object>> tremSchFind(String schid){
		Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String tt=formatter.format(date);
		int e = Integer.parseInt(tt);
		int ee=e+1;
		
		List<Map<String,Object>> lists=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		Set<String> set=new HashSet<String>();
		List<String> list=regionalExamDao.findSchoolTrem(schid);
		for(String str:list){
			set.add(str);
 		}
		if(set.contains(e+"-"+ee+"学年第一学期")||set.contains(e+"-"+ee+"学年第二学期")){
			for(String s:set){
				map.put("schY", s);
			}
			lists.add(map);
		}
 		return lists;
		
	}
	/**
	 * 教育局
	 * 根据学期查询区域连考信息
	 */
	public List<Map<String,Object>> findByTrem(String term,ObjectId id){
		return regionalExamDao.findByTerm(term, id);
		
	}
	 
	 /**
     * 根据每个联考项目的id查询这次联考的所有信息
     */
   public List<Map<String,Object>> allById(String id) {
	   List<Map<String,Object>> lists=new ArrayList<Map<String,Object>>();
	   Map<String,Object> map=new HashMap<String,Object>();
    	RegionalExamEntry ree =regionalExamDao.findByJointId(id);
    	map.put("nm", ree.getName());
    	map.put("sch", ree.getSchool());
    	lists.add(map);
        return lists;
    }

	/**
	 * 根据区域联考id 删除联考信息
	 */
   public String remove(String id){
	   regionalExamDao.delete(id);
	   return "200"; 
   }
   /**
    * 根据userId
    * @param userId
    * @return
    */
   public ObjectId getEduId(String userId){
	   ObjectId eduId = regionalExamDao.selEducationByUserId(new ObjectId(userId));
	   return eduId;
   }
   /**
	 * 学校
	 * 根据学期查询区域连考信息
	 */
	public List<Map<String,Object>> findBySchoolTrem(String schoolYear,ObjectId schId){
		return regionalExamDao.findByschTerm(schoolYear, schId);
		
	}
	
	/**
     * 修改成绩
     *
     * @param examDTO
     * @return
     */
    public ObjectId createExamForSchool(ExamEntry examEntry , List<RegionalSubjectItem> subjectList) {
        ObjectId returnId = examDao.save(examEntry);
        List<SubjectScoreEntry> scoreList = new ArrayList<SubjectScoreEntry>(examEntry.getExamSubject().size());
        for (RegionalSubjectItem rse : subjectList) {
            scoreList.add(new SubjectScoreEntry(rse.getSubjectId(), rse.getName(),
                    (float)Constant.ZERO, Constant.ZERO, (float)rse.getFull(), (float)rse.getPass(), Constant.ZERO, Constant.ZERO));
        }
        //新增考试，预初始化各班级考试成绩信息，分考场时修改学生考试的考场信息
        //获取年级的学生信息
        List<ClassEntry> classes = regionalExamDao.getClassEntryListByObjectIds(examEntry.getCList());
          UserEntry entryUser;
          List<DBObject> list =new ArrayList<DBObject>();
          for (ClassEntry clazz : classes) {
              for (ObjectId obj : clazz.getStudents()) {
                  entryUser = userDao.getUserEntry(obj, new BasicDBObject("nm", ""));
                  list.add(new ScoreEntry(examEntry.getID(),
                              entryUser.getID(), entryUser.getUserName(),
                              clazz.getID(), clazz.getName(), null, "",
                              scoreList, "", "",examEntry.getRegionalExamId(),0,0).getBaseEntry());
              }
          }
        scoreDao.addScoreEntryList(list);
        return returnId;
    }
    /**
     * 根据学校ID和年级类型获取该学校所有年级的ID List
     * @param schoolId
     * @param type
     * @return
     */
    public List<ObjectId> getClassIdBySchoolIAndType(ObjectId schoolId,int type){
    	List<ObjectId> schoolIdList = new ArrayList<ObjectId>();
    	schoolIdList.add(schoolId);
    	List<Object> objList = getClassId(schoolIdList,type);
    	List<ObjectId> resultList = new ArrayList<ObjectId>();
    	if(objList.isEmpty()){
    		return resultList;
    	}else{
    		List<ClassEntry> ceList =  (List<ClassEntry>)(((HashMap)objList.get(0)).get("classList"));
    		for(ClassEntry ce : ceList){
    			resultList.add(ce.getID());
    		}
    		return resultList;
    	}
    }
    /**
	 * 添加区域联考信息(l)
	 */
	public ObjectId save(RegionalExamEntry entry) {
		return regionalExamDao.save(entry);
	}
	
	public int countStudentNumByClassIds(List<ObjectId> classIdList){
		int sum = 0;
		for(ObjectId cid : classIdList){
			ClassEntry ce = classDao.getClassEntryById(cid,Constant.FIELDS);
			if(ce != null && ce.getStudents() != null){
				sum += ce.getStudents().size();
			}
		}
		return sum;
	}
	
	/**
	 * 添加区域联考汇总信息(cxy)
	 */
	public ObjectId saveExamSummary(ExamSummaryEntry entry) {
		return regionalExamDao.saveExamSummary(entry);
	}
	
	/**
	 * 根据联考ID和学校ID获取一条区域联考汇总信息(cxy)
	 */
	public ExamSummaryEntry getExamSummaryEntryByExamIdAndSchoolId(ObjectId examId,ObjectId schoolId) {
		return regionalExamDao.getExamSummaryEntryByExamIdAndSchoolId(examId, schoolId);
	}
	
	/**
	 * 根据教育局科目ID列表获取学校对应的科目列表
	 * @param srcList
	 * @return
	 */
	public List<ObjectId> getSchoolSubjectIdListByRegionalSubjectIdList(List<ObjectId> srcList,ObjectId eduId,ObjectId schoolId){
		List<ObjectId> schoolSubjectIdList = new ArrayList<ObjectId>();
		EducationBureauEntry eduEntry = educationDao.selEducationById(eduId);
		List<EducationSubject> eduSubList = eduEntry.getSubjects();
		List<EducationSubject> containList = new ArrayList<EducationSubject>();
		for(EducationSubject eduSub : eduSubList){
			if(srcList.contains(eduSub.getSubjectId())){
				containList.add(eduSub);
			}
		}
		SchoolEntry se = schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
		for(Subject subject : se.getSubjects()){
			for(EducationSubject eduSub : containList){
				if(eduSub.getName() != null && eduSub.getName().equals(subject.getName())){
					schoolSubjectIdList.add(subject.getSubjectId());
					break;
				}
			}
		}
		return schoolSubjectIdList;
	}
	
	/**
	 * 根据学校的id集合查询所有学校的信息(l)
	 */
	public List<SchoolEntry> getSchoolList(List<ObjectId> schoolIds) {
		List<SchoolEntry> list = new ArrayList<SchoolEntry>();
		Map<ObjectId, SchoolEntry> map = schoolDao.getSchoolMap(schoolIds,Constant.FIELDS);
		Set<?> entries = map.entrySet();
		if (entries != null) {
			Iterator<?> it = entries.iterator();
			while (it.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) it.next();
				Object value = entry.getValue();
				list.add((SchoolEntry) value);
			}
		}

		return list;
	}
	
	/**
	 * 根据学校的id集合信息，和年级类型，筛选学校(用于组装页面学校列表checkbox)
	 * 
	 * @param schoolIds
	 * @param type
	 * @return
	 */
	public List<Object> getSchoolNameId(List<ObjectId> schoolIds, int type) {
		List<Object> retList = new ArrayList<Object>();
		List<SchoolEntry> list = getSchoolList(schoolIds);
		for (SchoolEntry se : list) {
			List<Grade> gradeList = se.getGradeList();
			for (Grade grade : gradeList) {
				if (type == grade.getGradeType()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("schName", se.getName());
					map.put("schId", se.getID().toString());
					retList.add(map);
					break;
				}
			}
		}
		return retList;
	}

	/**
	 * 根据年级id查询班级集合
	 * 
	 * @param studentId
	 * @param fields
	 * @return
	 */
	public List<ClassEntry> getClassEntryListByGradetId(ObjectId gradeId) {
		return classDao.findClassEntryByGradeId(gradeId);
	}

	/**
	 * 根据学校的id集合信息，和年级类型，查找班级列表List<ClassEntry>(l)
	 * 
	 * @param schoolIds
	 * @param type
	 * @return
	 */
	public List<Object> getClassId(List<ObjectId> schoolIds, int type) {
		List<Object> retList = new ArrayList<Object>();
		List<SchoolEntry> list = getSchoolList(schoolIds);
		for (SchoolEntry se : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Grade> gradeList = se.getGradeList();
			List<ClassEntry> classList = new ArrayList<ClassEntry>();
			for (Grade grade : gradeList) {
				if (type == grade.getGradeType()) {
					ObjectId gradeId = grade.getGradeId();
					List<ClassEntry> queryByGradeId = getClassEntryListByGradetId(gradeId);
					if(queryByGradeId != null){
						classList.addAll(queryByGradeId);
					}
				}
			}
			String schName = se.getName();
			ObjectId schId = se.getID();
			map.put("schId", schId.toString());
			map.put("schName", schName);
			map.put("classList", classList);
			retList.add(map);
		}
		return retList;
	}

	/**
	 * 根据学校id和年级id 查询科目列表id和名字(l)
	 * 
	 * @param gradeId
	 * @return
	 */
	public List<Object> getSubjectList(List<ObjectId> schoolIds, int type) {
		List<Object> retList = new ArrayList<Object>();
		List<SchoolEntry> list = getSchoolList(schoolIds);
		for (SchoolEntry se : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Grade> gradeList = se.getGradeList();
			for (Grade grade : gradeList) {
				if (type == grade.getGradeType()) {
					List<Subject> subjectList = se.getSubjects();
					String name = se.getName();
					ObjectId schId = se.getID();
					map.put("schId", schId);
					map.put("schName", name);
					map.put("sub", subjectList);
					retList.add(map);
				}
			}
		}
		return retList;

	}
	
	
	/**
	 * 根据教育局id获取年级下拉列表(l)
	 * 
	 * @param edcationId
	 * @return
	 */
	public List<Object> getGradeNameList(ObjectId edcationId) {
		List<Object> list = new ArrayList<Object>();
		EducationBureauEntry entry = educationDao.selEducationById(edcationId);
		List<Grade> grade = entry.getGradeList();
		for (Grade g : grade) {
			Map<String,Object> map = new HashMap<String,Object>();
			String gradeName = g.getName();
			int type = g.getGradeType();
			map.put("type", type);
			map.put("gradeName", gradeName);
			map.put("id",g.getGradeId().toString());
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据教育局id和年级类型查询年级id(l)
	 * 
	 * @param edcationId
	 * @param gradeType
	 * @return
	 */
	public ObjectId getGradeId(ObjectId edcationId, int gradeType) {
		EducationBureauEntry entry = educationDao.selEducationById(edcationId);
		List<Grade> grade = entry.getGradeList();
		for (Grade name : grade) {
			int type = name.getGradeType();
			if (gradeType == type) {
				ObjectId id = name.getGradeId();
				return id;
			}
		}
		return null;
	}

	/**
	 * 根据年级类型从教育局表中 查询考试科目信息组装页面的科目checkbox(l)(包含科目名称和id)
	 * 
	 * @param educationId
	 * @param gradeType
	 * @return
	 */
	public List<Object> getSubjectsList(ObjectId edcationId, int gradeType) {
		List<Object> retList = new ArrayList<Object>();
		ObjectId gradeId = getGradeId(edcationId, gradeType);
		EducationBureauEntry entry = educationDao.selEducationById(edcationId);
		List<EducationSubject> subject = entry.getSubjects();

		for (EducationSubject name : subject) {
			List<ObjectId> list = name.getGradeIds();
			for (ObjectId id : list) {
				if (gradeId.equals(id)) {
					Map<String, Object> map = new HashMap<String, Object>();
					String subName = name.getName();
					ObjectId subId = name.getSubjectId();
					map.put("subId", subId.toString());
					map.put("subName", subName);
					retList.add(map);
				}
			}
		}
		return retList;
	}

	/**
	 * 根据教育局id和年级类型获取学校id列表信息(l)
	 * 
	 * @param educationId
	 * @param gradeType
	 * @return
	 */
	public List<ObjectId> getSchoolIdList(ObjectId educationId) {
		EducationBureauEntry entry = educationDao.selEducationById(educationId);
		List<ObjectId> list = entry.getSchoolIds();
		return list;
	}
	
	/**
	 * 更新统计信息相关数据(根据ID)
	 */
	public void updateExamSummaryById(ObjectId id, double csAll,List<SubjectDetails> sdList){
		regionalExamDao.updateExamSummaryById(id, csAll, sdList);
	}
}
