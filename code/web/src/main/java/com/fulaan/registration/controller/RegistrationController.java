package com.fulaan.registration.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.db.educationbureau.EducationBureauDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.registration.service.RegistrationService;
import com.fulaan.reward.service.RewardService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.registration.FamilyMemberEntry;
import com.pojo.registration.LearningResumeEntry;
import com.pojo.reward.RewardEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
/**
 * 学籍管理和成长档案Controller
 * 2015-9-8 15:32:35
 * @author cxy
 *
 */
@Controller
@RequestMapping("/registration") 
public class RegistrationController extends BaseController{ 
	@Autowired
	private ClassService classService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private RewardService rewardService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 页面跳转和初始化
	 */
	
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView showListPage(@RequestParam(required = false,defaultValue = "1")int a) {
		ModelAndView mv = new ModelAndView();
		String userId = getSessionValue().getId();
		UserEntry ue = userService.searchUserId(getUserId());
		ObjectId schoolId = ue.getSchoolID();
		int userRole = getSessionValue().getUserRole();
		boolean isEdu = UserRole.isEducation(userRole);
		List<Map<String,Object>> schoolList = new ArrayList<Map<String,Object>>();
		if(isEdu){
			for(ObjectId scid : registrationService.getSchoolIdsByEDUUserId(new ObjectId(userId))){
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
			if(UserRole.isStudent(userRole)){
				Grade gra = schoolService.getStudentGrade(new ObjectId(userId));
				ClassEntry ce = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
				Map<String,String> gradeMap = new HashMap<String,String>();
				gradeMap.put("id", gra.getGradeId().toString());
				gradeMap.put("name", gra.getName());
				Map<String,String> classMap = new HashMap<String,String>();
				classMap.put("id", ce.getID().toString());
				classMap.put("name", ce.getName());
				mv.addObject("gradeMap", gradeMap);
				mv.addObject("classMap", classMap);
				
			}
		}
		mv.addObject("schools", JSON.toJSONString(schoolList));
		mv.addObject("isStudent", UserRole.isStudent(userRole));
		if(a==10000){
			mv.setViewName("registration/registrationListc");
		} else {
			mv.setViewName("registration/registrationList");
		}
		return mv;
	}
	
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
	 * 查询列表数据
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/queryList")
	@ResponseBody
	public String deleteReward(@RequestParam(value = "classId",defaultValue = "") String classId,
							   @RequestParam(value = "keyword",defaultValue = "") String keyword,
							   @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
							   @RequestParam(value = "pageSize",defaultValue = "20") int pageSize){
		if(StringUtils.isBlank(classId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		List<UserDetailInfoDTO> srcList = classService.findStuByClassIdAndKeyword(classId,keyword);
		List pagedList = getListByPage(srcList,pageNo,pageSize);
		
		
		Map<String,Object> resultMap = new HashMap<String,Object>();

		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("cur",pageNo);
		pageMap.put("total",srcList.size());
		resultMap.put("pagejson", pageMap);
		resultMap.put("datas", pagedList);
		
		return JSON.toJSONString(resultMap); 
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
	 *  查询基础信息
	 */
	
	@RequestMapping("/queryBaseInfo")
	@ResponseBody
	public String queryBaseInfo(@RequestParam(value="userId", defaultValue = "")String userId) {
		UserEntry ue = userService.searchUserId(new ObjectId(userId));
		
		Map<String,Object> m = new HashMap<String,Object>();

		m.put("id",userId);
		m.put("userName",ue.getUserName());
		m.put("studentNumber",ue.getStudyNum());
		m.put("sex",ue.getSex());
		m.put("birthday",ue.getBirthDate() == 0L?"":sdf.format(new Date(ue.getBirthDate())));
		m.put("race",ue.getStudentRace()==null?"":ue.getStudentRace());
		m.put("health",ue.getStudentHealth()==null?"":ue.getStudentHealth());
		m.put("addressNow",ue.getAddress());
		m.put("addressR",ue.getResidenceAddress()==null?"":ue.getResidenceAddress());
		m.put("phone",ue.getPhoneNumber());
		m.put("email",ue.getEmail());

		return JSON.toJSONString(m);
	}
	
	
	/**
	 *  更新基础信息
	 */
	
	@RequestMapping("/updateBaseInfo")
	@ResponseBody
	public String updateBaseInfo(@RequestParam(value="id", defaultValue = "")String id,
								 @RequestParam(value="userName", defaultValue = "")String userName,
								 @RequestParam(value="studentNumber", defaultValue = "")String studentNumber,
								 @RequestParam(value="classId", defaultValue = "")String classId,
								 @RequestParam(value="preClassId", defaultValue = "")String preClassId,
								 @RequestParam(value="sex", defaultValue = "1")int sex,
								 @RequestParam(value="birthday", defaultValue = "")String birthday,
								 @RequestParam(value="race", defaultValue = "汉族")String race,
								 @RequestParam(value="addressNow", defaultValue = "")String addressNow,
								 @RequestParam(value="addressR", defaultValue = "")String addressR,
								 @RequestParam(value="phone", defaultValue = "")String phone,
								 @RequestParam(value="health", defaultValue = "健康")String health,
								 @RequestParam(value="email", defaultValue = "")String email) {
		
		classService.deleteStuFromClass(preClassId, id);
		classService.addStudentId(classId, id);
		try {
			registrationService.updateBase(new ObjectId(id), userName, studentNumber, sex, StringUtils.isBlank(birthday)?0L:sdf.parse(birthday).getTime(), race, addressNow, addressR, phone, email,health);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JSON.toJSONString(RespObj.FAILD);
		}
		
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 *  查询学籍变动信息
	 */
	
	@RequestMapping("/queryRegistration")
	@ResponseBody
	public String queryRegistration(@RequestParam(value="userId", defaultValue = "")String userId) {
		UserEntry ue = userService.searchUserId(new ObjectId(userId));
		
		Map<String,Object> m = new HashMap<String,Object>();

		m.put("id",userId);
		m.put("userName",ue.getUserName());
		m.put("studentNumber",ue.getStudyNum());
		m.put("changeDate",(ue.getChangeDate() == -1L)?"":sdf.format(new Date(ue.getChangeDate())));
		m.put("changePostscript",ue.getChagePostscript() == null?"":ue.getChagePostscript());
		m.put("newSchool",ue.getNewSchool() == null?"":ue.getNewSchool());
		

		return JSON.toJSONString(m);
	}
	
	/**
	 *  更新学籍变动信息
	 */
	
	@RequestMapping("/updateRegistration")
	@ResponseBody
	public String updateRegistration(@RequestParam(value="id", defaultValue = "")String id,
								 @RequestParam(value="newRClass", defaultValue = "")String newRClass,
								 @RequestParam(value="newRStudentNumber", defaultValue = "")String newRStudentNumber,
								 @RequestParam(value="newRSchool", defaultValue = "")String newRSchool,
								 @RequestParam(value="preClassId", defaultValue = "")String preClassId,
								 @RequestParam(value="newRContent", defaultValue = "")String newRContent,
								 @RequestParam(value="newRDate", defaultValue = "")String newRDate) {
		
		classService.deleteStuFromClass(preClassId, id);
		classService.addStudentId(newRClass, id);
		try {
			registrationService.updateRegistration(new ObjectId(id), newRStudentNumber, newRSchool, newRContent, "".equals(newRDate)?-1L:sdf.parse(newRDate).getTime());;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JSON.toJSONString(RespObj.FAILD);
		}
		
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	
	/**
	 *  学籍详细信息页面
	 */
	
	@RequestMapping("/rewardHistory")
	@ResponseBody
	public ModelAndView showDetail(@RequestParam(value="userId", defaultValue = "")String userId, HttpServletRequest request, @RequestParam(required = false,defaultValue = "1")int a) {
		ModelAndView mv = new ModelAndView();
		String userName = userService.searchUserId(new ObjectId(userId)).getUserName();
		//查询
		List<Map<String,Object>> rewardList = queryRewardList(userId);
		List<Map<String,Object>> memberList = queryMemberList(userId);
		List<Map<String,Object>> resumeList = queryResumeList(userId); 
		//权限
		int role = getSessionValue().getUserRole();
		boolean isHeadMaster = UserRole.isHeadmaster(role);
		//查询是否是班主任权限
		ObjectId classMasterId = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS).getMaster();
		boolean isMaster = getSessionValue().getId().equals(classMasterId==null?Constant.EMPTY:classMasterId.toString());
		//判断是不是学生本人
		boolean isMyself = getSessionValue().getId().equals(userId);
		
		
		
		mv.addObject("rewards", JSON.toJSONString(rewardList));
		mv.addObject("members", JSON.toJSONString(memberList));
		mv.addObject("resumes", JSON.toJSONString(resumeList));
		mv.addObject("familyRole", isHeadMaster || isMaster || isMyself);
		mv.addObject("resumeRole", isHeadMaster || isMaster || isMyself);
		mv.addObject("isMaster", isMaster);
		mv.addObject("userName", userName);
		mv.addObject("userId", userId);

//		mv.addObject("message", JSON.toJSONString(m));
		if(a==10000){
			mv.setViewName("registration/familyc");
		} else {
			mv.setViewName("registration/family");
		}

		return mv;
	}
	
	private List<Map<String,Object>> queryRewardList(String userId){
		List<RewardEntry> rewardList = rewardService.queryRewardsByUserId(userId);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<rewardList.size();i++){ 
			RewardEntry re = rewardList.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", re.getID().toString());
			map.put("name", re.getStudentName());
			map.put("type", re.getRewardType());
			map.put("level", re.getRewardGrade());
			map.put("date", sdf.format(new Date(re.getRewardTime())));
			map.put("contents", re.getRewardContent());
			resultList.add(map);
		}
		return resultList;
	}
	
	private List<Map<String,Object>> queryMemberList(String userId){
		List<FamilyMemberEntry> list = registrationService.queryFamilyMembersByUserId(new ObjectId(userId));
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){ 
			FamilyMemberEntry e = list.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", e.getID().toString());
			map.put("name", e.getMemberName());
			map.put("relation", e.getMemberRelation());
			map.put("race", e.getMemberRace());
			map.put("birthday", sdf.format(new Date(e.getMemberBirthday())));
			map.put("sex", e.getMemberSex() == 1?"男":"女");
			map.put("address", e.getMemberAddressNow());
			map.put("phone", e.getMemberPhone());
			resultList.add(map);
		}
		return resultList;
	} 
	
	private List<Map<String,Object>> queryResumeList(String userId){
		List<LearningResumeEntry> list = registrationService.queryLearningResume(new ObjectId(userId));
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){ 
			LearningResumeEntry e = list.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", e.getID().toString());
			map.put("startDate", sdf.format(new Date(e.getStartDate())));
			map.put("endDate", sdf.format(new Date(e.getEndDate())));
			map.put("entranceType", e.getEntranceType());
			map.put("studyType", e.getStudyType());
			map.put("studyUnit", e.getSyudyUnit());
			map.put("postScript", e.getPostScript());
			resultList.add(map);
		}
		return resultList;
	}  
	
	/**
	 * 新增一个家庭成员
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/addFamilyMember")
	@ResponseBody
	public String addFamilyMember(@RequestParam(value = "userId",defaultValue = "") String userId,
							   	  @RequestParam(value = "memberName",defaultValue = "") String memberName,
							   	  @RequestParam(value = "memberRelation",defaultValue = "") String memberRelation,
							   	  @RequestParam(value = "memberRace",defaultValue = "") String memberRace,
							   	  @RequestParam(value = "memberNationality",defaultValue = "") String memberNationality,
							   	  @RequestParam(value = "memberSex",defaultValue = "") int memberSex,
							   	  @RequestParam(value = "memberBirthday",defaultValue = "") String memberBirthday,
							   	  @RequestParam(value = "memberEducation",defaultValue = "") String memberEducation,
							   	  @RequestParam(value = "memberWork",defaultValue = "") String memberWork,
							   	  @RequestParam(value = "memberPolitics",defaultValue = "") String memberPolitics,
							   	  @RequestParam(value = "memberHealth",defaultValue = "") String memberHealth,
							   	  @RequestParam(value = "memberAddressNow",defaultValue = "") String memberAddressNow,
							   	  @RequestParam(value = "memberAddressRegistration",defaultValue = "") String memberAddressRegistration,
							   	  @RequestParam(value = "memberPhone",defaultValue = "") String memberPhone,
							   	  @RequestParam(value = "memberEmail",defaultValue = "") String memberEmail){
		
		UserEntry ue = userService.searchUserId(new ObjectId(userId));
		String userName = ue.getUserName();
		try {
			registrationService.addFamilyMemberEntry(new FamilyMemberEntry(new ObjectId(userId), memberName, memberRelation, 
					memberRace, memberNationality, memberSex, sdf.parse(memberBirthday).getTime(), memberEducation, memberWork, memberPolitics, memberHealth,
					memberAddressNow, memberAddressRegistration, memberPhone, memberEmail));
		} catch (ParseException e) {
			e.printStackTrace();
			return JSON.toJSONString(RespObj.FAILD); 
		}
		List<Map<String,Object>> memberList = queryMemberList(userId);
		return JSON.toJSONString(memberList); 
	}
	
	/**
	 *  查询基础信息
	 */
	
	@RequestMapping("/queryFamilyMemberDetail")
	@ResponseBody
	public String queryFamilyMemberDetail(@RequestParam(value="id", defaultValue = "")String id) {
		FamilyMemberEntry e = registrationService.queryFamilyMemberById(new ObjectId(id));
		
		Map<String,Object> m = new HashMap<String,Object>();

		m.put("id",id);
		m.put("memberName",e.getMemberName());
		m.put("memberRelation",e.getMemberRelation());
		m.put("memberRace",e.getMemberRace());
		m.put("memberNationality",e.getMemberNationality());
		m.put("memberSex",e.getMemberSex());
		m.put("memberBirthday",sdf.format(new Date(e.getMemberBirthday())));
		m.put("memberEducation",e.getMemberEducation());
		m.put("memberWork",e.getMemberWork());
		m.put("memberPolitics",e.getMemberPolitics());
		m.put("memberHealth",e.getMemberHealth());
		m.put("memberAddressNow",e.getMemberAddressNow());
		m.put("memberAddressRegistration",e.getMemberAddressRegistration());
		m.put("memberPhone",e.getMemberPhone());
		m.put("memberEmail",e.getMemberEmail());
		

		return JSON.toJSONString(m);
	}
	
	/**
	 * 编辑一个家庭成员
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/editFamilyMember")
	@ResponseBody
	public String editFamilyMember(@RequestParam(value = "id",defaultValue = "") String id,
								  @RequestParam(value = "userId",defaultValue = "") String userId,
							   	  @RequestParam(value = "memberName",defaultValue = "") String memberName,
							   	  @RequestParam(value = "memberRelation",defaultValue = "") String memberRelation,
							   	  @RequestParam(value = "memberRace",defaultValue = "") String memberRace,
							   	  @RequestParam(value = "memberNationality",defaultValue = "") String memberNationality,
							   	  @RequestParam(value = "memberSex",defaultValue = "") int memberSex,
							   	  @RequestParam(value = "memberBirthday",defaultValue = "") String memberBirthday,
							   	  @RequestParam(value = "memberEducation",defaultValue = "") String memberEducation,
							   	  @RequestParam(value = "memberWork",defaultValue = "") String memberWork,
							   	  @RequestParam(value = "memberPolitics",defaultValue = "") String memberPolitics,
							   	  @RequestParam(value = "memberHealth",defaultValue = "") String memberHealth,
							   	  @RequestParam(value = "memberAddressNow",defaultValue = "") String memberAddressNow,
							   	  @RequestParam(value = "memberAddressRegistration",defaultValue = "") String memberAddressRegistration,
							   	  @RequestParam(value = "memberPhone",defaultValue = "") String memberPhone,
							   	  @RequestParam(value = "memberEmail",defaultValue = "") String memberEmail){
		
		try {
			registrationService.updateFamilyMember(new ObjectId(id), memberName, memberRelation, 
					memberRace, memberNationality, memberSex, sdf.parse(memberBirthday).getTime(), memberEducation, memberWork, memberPolitics, memberHealth,
					memberAddressNow, memberAddressRegistration, memberPhone, memberEmail);
		} catch (ParseException e) {
			e.printStackTrace();
			return JSON.toJSONString(RespObj.FAILD); 
		}
		List<Map<String,Object>> memberList = queryMemberList(userId);
		return JSON.toJSONString(memberList); 
	}
	
	
	/**
	 *  删除一个家庭成员
	 */
	
	@RequestMapping("/delFamilyMember")
	@ResponseBody
	public String delFamilyMember(@RequestParam(value="id", defaultValue = "")String id) {
		String userId = registrationService.queryFamilyMemberById(new ObjectId(id)).getUserId().toString();
		registrationService.deleteFamilyMember(new ObjectId(id));
		
		List<Map<String,Object>> memberList = queryMemberList(userId);

		return JSON.toJSONString(memberList);
	}
	
	
	/**
	 * 新增一个学习简历
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/addStudyResume")
	@ResponseBody
	public String addStudyResume(@RequestParam(value = "userId",defaultValue = "") String userId,
							   	  @RequestParam(value = "resumeSD",defaultValue = "") String resumeSD,
							   	  @RequestParam(value = "resumeED",defaultValue = "") String resumeED,
							   	  @RequestParam(value = "entrance",defaultValue = "") String entrance,
							   	  @RequestParam(value = "studyType",defaultValue = "") String studyType,
							   	  @RequestParam(value = "studyCompany",defaultValue = "") String studyCompany,
							   	  @RequestParam(value = "resumePostscript",defaultValue = "") String resumePostscript){
		UserEntry ue = userService.searchUserId(new ObjectId(userId));
		String userName = ue.getUserName();
		try {
			registrationService.addLearningResumeEntry(new LearningResumeEntry(new ObjectId(userId), sdf.parse(resumeSD).getTime(), sdf.parse(resumeED).getTime(), 
														entrance, studyType, studyCompany, resumePostscript));
		} catch (ParseException e) {
			e.printStackTrace();
			return JSON.toJSONString(RespObj.FAILD); 
		}
		List<Map<String,Object>> resumeList = queryResumeList(userId);
		return JSON.toJSONString(resumeList); 
	}
	
	/**
	 *  查询一个学习简历详情
	 */
	
	@RequestMapping("/queryResumeDetail")
	@ResponseBody
	public String queryResumeDetail(@RequestParam(value="id", defaultValue = "")String id) {
		LearningResumeEntry e = registrationService.queryLearningResumeById(new ObjectId(id));
		

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", e.getID().toString());
		map.put("startDate", sdf.format(new Date(e.getStartDate())));
		map.put("endDate", sdf.format(new Date(e.getEndDate())));
		map.put("entranceType", e.getEntranceType());
		map.put("studyType", e.getStudyType());
		map.put("studyUnit", e.getSyudyUnit());
		map.put("postScript", e.getPostScript());
		

		return JSON.toJSONString(map);
	}
	
	/**
	 * 编辑一个学习简历
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/editLearningResume")
	@ResponseBody
	public String editLearningResume(@RequestParam(value = "id",defaultValue = "") String id,
									 @RequestParam(value = "userId",defaultValue = "") String userId,
									 @RequestParam(value = "resumeSD",defaultValue = "") String resumeSD,
									 @RequestParam(value = "resumeED",defaultValue = "") String resumeED,
									 @RequestParam(value = "entrance",defaultValue = "") String entrance,
									 @RequestParam(value = "studyType",defaultValue = "") String studyType,
									 @RequestParam(value = "studyCompany",defaultValue = "") String studyCompany,
									 @RequestParam(value = "resumePostscript",defaultValue = "") String resumePostscript){
		
		try {
			registrationService.updateLearningResume(new ObjectId(id), sdf.parse(resumeSD).getTime(), sdf.parse(resumeED).getTime(),
												entrance, studyType, studyCompany, resumePostscript);
		} catch (ParseException e) {
			e.printStackTrace();
			return JSON.toJSONString(RespObj.FAILD); 
		}
		List<Map<String,Object>> resumeList = queryResumeList(userId);
		return JSON.toJSONString(resumeList); 
	}
	
	/**
	 *  删除一个家庭成员
	 */
	
	@RequestMapping("/delLearningResume")
	@ResponseBody
	public String delLearningResume(@RequestParam(value="id", defaultValue = "")String id) {
		String userId = registrationService.queryLearningResumeById(new ObjectId(id)).getUserId().toString();
		registrationService.deleteLearningResume(new ObjectId(id));
		
		List<Map<String,Object>> resumeList = queryResumeList(userId);

		return JSON.toJSONString(resumeList);
	}
	
	/**
	 *  测试用增加教育局相关数据
	 */
	
	@RequestMapping("/addEDUTest")
	@ResponseBody
	public String addEDUTest() {
		EducationBureauEntry e = new EducationBureauEntry("测试教育局", "测试省", "测试市", "中国", System.currentTimeMillis(), System.currentTimeMillis());
		EducationBureauDao educationBureauDao = new EducationBureauDao(); 
		ObjectId id = educationBureauDao.addEducation(e);
		educationBureauDao.addRelationSchool(id, "", new ObjectId("5587d82f7f72332a4db249a5"), System.currentTimeMillis());
		educationBureauDao.addRelationSchool(id, "", new ObjectId("5587d82f7f72332a4db249a9"), System.currentTimeMillis());
		
		educationBureauDao.addEduUser(id, new ObjectId(getSessionValue().getId()), System.currentTimeMillis()); 

		return JSON.toJSONString("OK");
	}
	
	
	
}
