package com.fulaan.registration.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.exam.service.ExamService;
import com.fulaan.exam.service.ScoreService;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.level.service.LevelService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.registration.service.GrowthRecordService;
import com.fulaan.registration.service.RegistrationService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.exam.ExamEntry;
import com.pojo.exam.ExamSubjectEntry;
import com.pojo.exam.ScoreDTO;
import com.pojo.exam.ScoreEntry;
import com.pojo.exam.SubjectScoreEntry;
import com.pojo.level.LevelEntry;
import com.pojo.registration.GrowthRecordEntry;
import com.pojo.registration.LevelObject;
import com.pojo.registration.QualityObject;
import com.pojo.registration.SubQualityObject;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * 成长档案和素质教育Controller
 * @author cxy
 * 2015-11-30 10:35:45
 */
@Controller
@RequestMapping("/growth") 
public class GrowRecordController extends BaseController{
	
	@Autowired
	private GrowthRecordService growthRecordService;
	
	@Autowired
	private ScoreService scoreService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private ExamService examService;
	
	@Autowired
	private ExamResultService examResultService;
	
	@Autowired
	private LevelService levelService;
	
	/**
	 * 成长档案列表
	 */
	
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView showListPage(@RequestParam(required = false,defaultValue = "1")int a) {
		String userId = getSessionValue().getId();
		UserEntry ue = userService.searchUserId(getUserId());
		int userRole = getSessionValue().getUserRole();
		boolean isEdu = UserRole.isEducation(userRole);
		List<Map<String,Object>> schoolList = new ArrayList<Map<String,Object>>();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		if(isEdu){
			boolean isFirst = true;
			for(ObjectId scid : registrationService.getSchoolIdsByEDUUserId(new ObjectId(userId))){
				if(isFirst){
					schoolId = scid;
					isFirst = false;
				}
				SchoolEntry se = schoolService.getSchoolEntry(scid,Constant.FIELDS);
				if(se == null){
					continue;
				}
				Map<String,Object> schoolMap = uploadSchoolMap(se);
				if(schoolMap != null){
					schoolList.add(uploadSchoolMap(se));
				}
			}
		}else{
			schoolList.add(uploadSchoolMap(schoolService.getSchoolEntry(schoolId,Constant.FIELDS)));
		}
		List<String> teamList = growthRecordService.distinctExamTerm(schoolId);
		if(teamList.size() == 0){
			teamList.add(examResultService.getTerm(System.currentTimeMillis()));
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("schools", JSON.toJSONString(schoolList));
		mv.addObject("terms", JSON.toJSONString(teamList));
		if(a == 10000) {
			mv.setViewName("registration/growRecordc");
		} else {
			mv.setViewName("registration/growRecord");
		}
		return mv;
	}
	/**
	 * 拼装学校-年级-班级系列Map
	 * @param schoolEntry
	 * @return
	 */
	private Map<String,Object> uploadSchoolMap(SchoolEntry schoolEntry){
		Map<String,Object> schoolMap = new HashMap<String,Object>();
		// 获取所有年级
		List<GradeView> grades = schoolService.findGradeList(schoolEntry.getID().toString());
		List<Map<String,Object>> gradeList = new ArrayList<Map<String,Object>>();
		for(GradeView g : grades){
			Map<String,Object> gradeMap = new HashMap<String,Object>();
			gradeMap.put("id",g.getId());
			gradeMap.put("name",g.getName());
			// 获取所有班级
			List<Map<String,Object>> classList = new ArrayList<Map<String,Object>>();
			List<ClassInfoDTO> classDTOList = classService.findClassByGradeId(g.getId());
			if(classDTOList != null){
				for(ClassInfoDTO cid : classDTOList){
					Map<String,Object> classMap = new HashMap<String,Object>();
					classMap.put("id", cid.getId());
					classMap.put("name", cid.getClassName());
					classList.add(classMap);
				}
			}
			gradeMap.put("classes",classList);
			gradeList.add(gradeMap);
		}
		schoolMap.put("id",schoolEntry.getID().toString());
		schoolMap.put("name",schoolEntry.getName());
		schoolMap.put("grades", gradeList);
		return schoolMap;
	}
	
	/**
	 * 获取考试列表
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/getExamList")
	@ResponseBody
	public RespObj getExamList(@RequestParam(value = "schoolId",defaultValue = "") String schoolId,
							   @RequestParam(value = "term",defaultValue = "") String term,
							   @RequestParam(value = "gradeId",defaultValue = "") String gradeId){
		if(StringUtils.isBlank(term) || StringUtils.isBlank(schoolId) || StringUtils.isBlank(gradeId)){
			return RespObj.FAILD;
		}
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<Map<String,String>> examList = new ArrayList<Map<String,String>>();
		for(ExamEntry ee : growthRecordService.queryExamBySchoolIdAndTerm(new ObjectId(schoolId), new ObjectId(gradeId), term)){
			Map<String,String> m = new HashMap<String,String>();
			m.put("id", ee.getID().toString());
			m.put("name",ee.getName());
			examList.add(m);
		}
		respObj.setMessage(examList);
		return respObj; 
	}
	
	
	/**
     * 获取考试成绩详情
     *
     * @param examId
     * @param classId
     * @return
     */
    
    @RequestMapping("/queryScoreList")
    @ResponseBody
    public RespObj queryScoreList(@RequestParam(value = "examId",defaultValue = "") String examId,
    							  @RequestParam(value = "classId",defaultValue = "") String classId,
    							  @RequestParam(value = "pageNo",defaultValue = "") int pageNo,
    							  @RequestParam(value = "pageSize",defaultValue = "") int pageSize) {
    	if(StringUtils.isBlank(examId) || StringUtils.isBlank(classId)){
			return RespObj.FAILD;
		}
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        String[] subject = new String[0];
        String[] classArray = new String[1];
        classArray[0] = classId;
        List<ScoreDTO> scoreDTOs = scoreService.loadDetail(examId, classArray, subject, null);
        List pagedList = getListByPage(scoreDTOs, pageNo, pageSize);
        Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("cur",pageNo);
		pageMap.put("total",scoreDTOs.size());
		
        
        List<Map<String,String>> headList = new ArrayList<Map<String,String>>();
        ExamEntry exemEntry = examService.findExamEntry(examId);
        for(ExamSubjectEntry ese : exemEntry.getExamSubject()){
        	Map<String,String> m = new HashMap<String,String>();
        	m.put("id", ese.getSubjectId().toString());
        	m.put("name", ese.getSubjectName());
        	headList.add(m);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("list",pagedList); 
        resultMap.put("head",headList); 
        resultMap.put("pagejson", pageMap);
        respObj.setMessage(resultMap);
        return respObj;
    }
    /**
	 * 根据分页信息进行数据筛选
	 * @param src
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<? extends Object> getListByPage(List<? extends Object> src,int pageNo,int pageSize){
		List<Object> list = new ArrayList<Object>();
		int startIndex = (pageNo - 1) * pageSize;
		int endIndex = (pageNo * pageSize) - 1;
		if(src.size() < pageNo * pageSize){
			endIndex = src.size() - 1;
		}
		for(int i=startIndex;i<endIndex + 1;i++){
			list.add(src.get(i));
		}
		return list;
	}
	
	
	/**
	 * 成长档案成绩单详情
	 * @throws Exception 
	 */
	
	@RequestMapping("/scoreDetail")
	@ResponseBody
	public ModelAndView scoreDetail(@RequestParam(value = "id",defaultValue = "") String id) throws Exception {
		if(StringUtils.isBlank(id)){
			throw new Exception("参数错误！");
		}
		UserEntry ue = userService.searchUserId(new ObjectId(id));
		String currnetTerm = examResultService.getTerm(System.currentTimeMillis());
		//查询所有有该学生参加考试的学期。
		List<String> reportTermList = growthRecordService.distinctExamTermForUniqueStudent(ue.getSchoolID(), ue.getID());
		if(reportTermList.size() == 0){
			reportTermList.add(currnetTerm);
		}
		//查询当前学期该学生的素质教育单是否已经创建，若没有则创建
		GrowthRecordEntry gre = growthRecordService.getGrowthRecordEntryByTermAndUserId(currnetTerm, ue.getID());
		if(gre == null){
			List<QualityObject> qualityEducationList = growthRecordService.getEmptyQualityListBySchoolId(ue.getSchoolID());
			List<LevelObject> levelList = new ArrayList<LevelObject>();
			for(LevelEntry le : levelService.queryLevelsBySchoolId(ue.getSchoolID())){
				LevelObject lo = new LevelObject(le.getLevelName(), le.getScoreRange());
				levelList.add(lo);
			}
			gre = new GrowthRecordEntry(ue.getID(), currnetTerm, ue.getSchoolID(), levelList, qualityEducationList);
			growthRecordService.addGrowthRecordEntry(gre);
		}
		//查询所有有素质教育初始化的学期
		List<String> growthTermList = growthRecordService.distinctGrowthTermForUniqueStudent(ue.getID());
		Collections.sort(growthTermList);
		//查询是否是班主任权限
		ObjectId classMasterId = classService.getClassEntryByStuId(new ObjectId(id), Constant.FIELDS).getMaster();
		boolean isClassMaster = getSessionValue().getId().equals(classMasterId==null?Constant.EMPTY:classMasterId.toString());
		//判断是不是学生本人
		boolean isMyself = getSessionValue().getId().equals(id);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("studentId", id);
		mv.addObject("studentName", ue.getUserName());
		mv.addObject("growthId", gre.getID().toString()); 
		mv.addObject("isMaster", isClassMaster); 
		mv.addObject("isMyself", isMyself);
		mv.addObject("reportTermList", reportTermList);
		mv.addObject("growthTermList", growthTermList);
		mv.addObject("goodPerformance", gre.getGoodPerformance());
		mv.addObject("masterComment", gre.getMasterComment());
		mv.setViewName("registration/growthDetail_score");
		return mv;
	}
	
	/**
	 * 成长档案成绩单详情
	 * @throws Exception 
	 */
	
	@RequestMapping("/qualityDetail")
	@ResponseBody
	public ModelAndView detail(@RequestParam(value = "id",defaultValue = "") String id) throws Exception {
		if(StringUtils.isBlank(id)){
			throw new Exception("参数错误！");
		}
		UserEntry ue = userService.searchUserId(new ObjectId(id));
		String currnetTerm = examResultService.getTerm(System.currentTimeMillis());
		//查询所有有该学生参加考试的学期。
		List<String> reportTermList = growthRecordService.distinctExamTermForUniqueStudent(ue.getSchoolID(), ue.getID());
		if(reportTermList.size() == 0){
			reportTermList.add(currnetTerm);
		}
		//查询当前学期该学生的素质教育单是否已经创建，若没有则创建
		GrowthRecordEntry gre = growthRecordService.getGrowthRecordEntryByTermAndUserId(currnetTerm, ue.getID());
		if(gre == null){
			List<QualityObject> qualityEducationList = growthRecordService.getEmptyQualityListBySchoolId(ue.getSchoolID());
			List<LevelObject> levelList = new ArrayList<LevelObject>();
			for(LevelEntry le : levelService.queryLevelsBySchoolId(ue.getSchoolID())){
				LevelObject lo = new LevelObject(le.getLevelName(), le.getScoreRange());
				levelList.add(lo);
			}
			gre = new GrowthRecordEntry(ue.getID(), currnetTerm, ue.getSchoolID(), levelList, qualityEducationList);
			growthRecordService.addGrowthRecordEntry(gre);
		}
		//查询所有有素质教育初始化的学期
		List<String> growthTermList = growthRecordService.distinctGrowthTermForUniqueStudent(ue.getID());
		Collections.sort(growthTermList);
		//查询是否是班主任权限
		ObjectId classMasterId = classService.getClassEntryByStuId(new ObjectId(id), Constant.FIELDS).getMaster();
		boolean isClassMaster = getSessionValue().getId().equals(classMasterId==null?Constant.EMPTY:classMasterId.toString());
		//判断是不是学生本人
		boolean isMyself = getSessionValue().getId().equals(id);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("studentId", id);
		mv.addObject("studentName", ue.getUserName());
		mv.addObject("growthId", gre.getID().toString()); 
		mv.addObject("isMaster", isClassMaster); 
		mv.addObject("isMyself", isMyself);
		mv.addObject("reportTermList", reportTermList);
		mv.addObject("growthTermList", growthTermList);
		mv.addObject("goodPerformance", gre.getGoodPerformance());
		mv.addObject("masterComment", gre.getMasterComment());
		mv.setViewName("registration/growthDetail_quality");
		return mv;
	}
	
	/**
	 * 获取考试列表
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/getExamListForDetail")
	@ResponseBody
	public RespObj getExamListForDetail(@RequestParam(value = "term",defaultValue = "") String term,
							   @RequestParam(value = "studentId",defaultValue = "") String studentId){
		if(StringUtils.isBlank(term) || StringUtils.isBlank(studentId)){
			return RespObj.FAILD;
		}
		UserEntry ue = userService.searchUserId(new ObjectId(studentId));
		GrowthRecordEntry gre = growthRecordService.getGrowthRecordEntryByTermAndUserId(term, new ObjectId(studentId));
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<Map<String,String>> examList = new ArrayList<Map<String,String>>();
		for(ExamEntry ee : growthRecordService.findExamListByTermForUniqueStudent(ue.getSchoolID(), ue.getID(), term)){
			Map<String,String> m = new HashMap<String,String>();
			m.put("id", ee.getID().toString());
			m.put("name",ee.getName());
			examList.add(m);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("examList", examList);
		map.put("growthId", gre==null?Constant.EMPTY:gre.getID().toString());
		respObj.setMessage(map);
		return respObj; 
	}
	
	/**
	 * 获取考试各科目成绩列表
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/getExamScoreList")
	@ResponseBody
	public RespObj getExamScoreList(@RequestParam(value = "examId",defaultValue = "") String examId,
							   @RequestParam(value = "studentId",defaultValue = "") String studentId){
		if(StringUtils.isBlank(examId) || StringUtils.isBlank(studentId)){
			return RespObj.FAILD;
		}
		ScoreEntry se = growthRecordService.getScoreEntryForUniqueStudent(new ObjectId(examId), new ObjectId(studentId));
		if(se == null){
			return RespObj.FAILD;
		}
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<Map<String,Object>> scoreList = new ArrayList<Map<String,Object>>();
		for(SubjectScoreEntry sse : se.getExamScore()){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("name", sse.getSubjectName());
			m.put("score",sse.getScore());
			scoreList.add(m);
		}
		respObj.setMessage(scoreList);
		return respObj; 
	}
	
	/**
	 * 获取素质教育成绩单
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/getReportQualityList")
	@ResponseBody
	public RespObj getReportQualityList(@RequestParam(value = "term",defaultValue = "") String term,
							   @RequestParam(value = "studentId",defaultValue = "") String studentId){
		if(StringUtils.isBlank(term) || StringUtils.isBlank(studentId)){
			return RespObj.FAILD;
		}
		GrowthRecordEntry gre = growthRecordService.getGrowthRecordEntryByTermAndUserId(term, new ObjectId(studentId));
		if(gre == null){
			return RespObj.FAILD;
		}
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<Map<String,Object>> scoreList = new ArrayList<Map<String,Object>>();
		for(QualityObject qo : gre.getQualityEducationList()){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("name", qo.getName());
			m.put("score",qo.getLevel());
			scoreList.add(m);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("scoreList", scoreList);
		resultMap.put("goodPerformance", gre.getGoodPerformance());
		resultMap.put("mastComment", gre.getMasterComment());
		respObj.setMessage(resultMap);
		return respObj; 
	}
	
	/**
	 * 更新优秀表现和班主任评语
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/updateGpAndMc")
	@ResponseBody
	public RespObj updateGpAndMc(@RequestParam(value = "growthId",defaultValue = "") String growthId,
							     @RequestParam(value = "goodPerformance",defaultValue = "") String goodPerformance,
								 @RequestParam(value = "masterComment",defaultValue = "") String masterComment){
		if(StringUtils.isBlank(growthId)){
			return RespObj.FAILD;
		}
		growthRecordService.updateGoodPerformanceById(new ObjectId(growthId), goodPerformance);
		growthRecordService.updateMasterCommentById(new ObjectId(growthId), masterComment);
		return RespObj.SUCCESS; 
	}
	
	/**
	 * 获取素质教育报告单所有数据
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/getQualityDatas")
	@ResponseBody
	public RespObj getQualityDatas(@RequestParam(value = "term",defaultValue = "") String term,
							   @RequestParam(value = "studentId",defaultValue = "") String studentId){
		if(StringUtils.isBlank(term) || StringUtils.isBlank(studentId)){
			return RespObj.FAILD;
		}
		GrowthRecordEntry gre = growthRecordService.getGrowthRecordEntryByTermAndUserId(term, new ObjectId(studentId));
		if(gre == null){
			return RespObj.SUCCESS;
		}
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<String> levelList = new ArrayList<String>();
		for(LevelObject lo : gre.getLevels()){
			levelList.add(lo.getName());
		}
		List<Map<String,Object>> qualityList = new ArrayList<Map<String,Object>>();
		for(QualityObject qo : gre.getQualityEducationList()){
			List<Map<String,Object>> subList = new ArrayList<Map<String,Object>>();
			for(SubQualityObject sqo : qo.getSubQualityList()){
				Map<String,Object> subM = new HashMap<String,Object>();
				subM.put("name", sqo.getName());
				subM.put("requirement",sqo.getRequirement());
				subM.put("slv",sqo.getSelfLevel());
				subM.put("tlv",sqo.getTeacherLevel());
				subList.add(subM);
			}
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("name", qo.getName());
			m.put("level",qo.getLevel());
			m.put("subList",subList);
			qualityList.add(m);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("qualityList", qualityList);
		resultMap.put("levelList", levelList);
		respObj.setMessage(resultMap);
		return respObj; 
	}
	
	
	/**
	 * 更新素质教育成绩单中的级别改变
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/updateQualityLevel")
	@ResponseBody
	public RespObj updateQualityLevel(@RequestParam(value = "term",defaultValue = "") String term,
									  @RequestParam(value = "studentId",defaultValue = "") String studentId,
									  @RequestParam(value = "coordinate",defaultValue = "") String coordinate,
									  @RequestParam(value = "level",defaultValue = "") String level){
		if(StringUtils.isBlank(term) || StringUtils.isBlank(studentId) || StringUtils.isBlank(coordinate) || StringUtils.isBlank(level)){
			return RespObj.FAILD;
		}
		String[] coordinateArray = coordinate.split("-");
		int parentNum = Integer.parseInt(coordinateArray[0]);
		int subNum = Integer.parseInt(coordinateArray[1]);
		int typeNum = Integer.parseInt(coordinateArray[2]);
		GrowthRecordEntry gre = growthRecordService.getGrowthRecordEntryByTermAndUserId(term, new ObjectId(studentId));
		
		if(gre == null){
			return RespObj.SUCCESS;
		}
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		List<QualityObject> quaList = gre.getQualityEducationList();
		if(typeNum == 0){
			quaList.get(parentNum).setLevel(level);
		}else if(typeNum == 1){
			quaList.get(parentNum).getSubQualityList().get(subNum).setSelfLevel(level);
		}else if(typeNum == 2){
			quaList.get(parentNum).getSubQualityList().get(subNum).setTeacherLevel(level);
		}
		growthRecordService.updateQualityEducationById(gre.getID(), quaList);
		return respObj; 
	}
	
	/**
	 * 更新优秀表现和班主任评语
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/resetAll")
	@ResponseBody
	public RespObj resetAll(){
		growthRecordService.removeGrowthRecordBySchoolId(new ObjectId(getSessionValue().getSchoolId()), 
									examResultService.getTerm(System.currentTimeMillis()));
		return RespObj.SUCCESS; 
	}
	
	
	/**
	 * 检测学生是否有班级
	 */
	
	@RequestMapping("/checkStudent")
	@ResponseBody
	public RespObj check(@RequestParam(value = "id",defaultValue = "") String id) {
		if(StringUtils.isBlank(id)){
			return RespObj.FAILD;
		}
		ClassEntry ce = classService.getClassEntryByStuId(new ObjectId(id), Constant.FIELDS);
		if(ce == null){
			return RespObj.FAILD;
		}else{
			return RespObj.SUCCESS;
		}
	}
	
	/**
     * 获取考试成绩详情
     *
     * @param examId
     * @param classId
     * @return
     */
    
    @RequestMapping("/queryStudentList")
    @ResponseBody
    public RespObj queryStudentList(@RequestParam(value = "classId",defaultValue = "") String classId,
    							    @RequestParam(value = "pageNo",defaultValue = "") int pageNo,
    							    @RequestParam(value = "pageSize",defaultValue = "") int pageSize) {
    	if(StringUtils.isBlank(classId)){
			return RespObj.FAILD;
		}
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
       
        List<UserDetailInfoDTO> stuList = classService.findStuByClassId(classId);
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        for(UserDetailInfoDTO dto : stuList){
        	Map<String,Object> m = new HashMap<String,Object>();
        	m.put("studentName", dto.getUserName());
        	m.put("studentId", dto.getId());
        	resultList.add(m);
        }
        List pagedList = getListByPage(resultList, pageNo, pageSize);
        Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("cur",pageNo);
		pageMap.put("total",resultList.size());
		
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("list",pagedList); 
        resultMap.put("pagejson", pageMap);
        respObj.setMessage(resultMap);
        return respObj;
    }
	
	
	/**
	 * 测试distinct
	 */
	
	@RequestMapping("/distinct")
	@ResponseBody
	public RespObj distinct() {
		RespObj resultObj = RespObj.SUCCESS;
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		resultObj.setMessage(growthRecordService.distinctExamTerm(schoolId));
		return resultObj;
	}
}
