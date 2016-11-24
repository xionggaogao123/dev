package com.fulaan.guard.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.controller.BaseController;
import com.fulaan.department.service.DepartmentService;
import com.fulaan.guard.service.GuardService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.pojo.guard.StudentEnterEntry;
import com.pojo.guard.StudentOutEntry;
import com.pojo.guard.VisitorEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * @author chengwei@ycode.cn
 * @version 2015年12月8日 上午10:30:37 类说明 门卫管理controller
 */
@Controller
@RequestMapping("/guard")
public class GuardController extends BaseController {

	@Autowired
	private GuardService guardService;

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private ClassService cs;

	/**
	 * 门卫管理页面初始状态(cw)
	 * 
	 * @return
	 */

	@RequestMapping("/enterList")
	@ResponseBody
	public ModelAndView enterStudentInfos(){
		ModelAndView mv= new ModelAndView();
		int role = getSessionValue().getUserRole();
		String uid = getSessionValue().getId();
		if(UserRole.isDoorKeeper(role) || UserRole.isManager(role)){
			mv.setViewName("guard/guard");
			mv.addObject("power",Constant.ZERO);
		}else if(UserRole.isTeacher(role) || UserRole.isHeadmaster(role) || UserRole.isLeaderClass(role) || UserRole.isLeaderOfGrade(role) || UserRole.isLeaderOfSubject(role)|| guardService.isGuardAdmin(new ObjectId(uid),Constant.DEPARTMENT_GUARD)){
			mv.setViewName("guard/guard");
			mv.addObject("power",Constant.ONE);
		}else{
			mv.setViewName("guard/error");
		}
		return mv;
	}



	/**
	 * 入校记录列表
	 * 
	 * @param grade
	 * @param classroom
	 * @return
	 */

	@RequestMapping("/filterEnterList")
	@ResponseBody
	public String filterEnterStudentInfos(
			@RequestParam(value = "grade", defaultValue = "") String grade,
			@RequestParam(value = "classroom", defaultValue = "") String classroom,
			@RequestParam(value = "skip", defaultValue = "1") int skip,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		// 根据条件获取进校记录
		List<StudentEnterEntry> enterList = guardService.queryEnterStudents(
				grade, classroom,skip,size);
		// 格式化数据
		List<Map<String, Object>> formatList = formatEnterList(enterList);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		int role = getSessionValue().getUserRole();
		if(UserRole.isDoorKeeper(role) || UserRole.isManager(role)){
			resultMap.put("power", Constant.ZERO);
		}else{
			resultMap.put("power", Constant.ONE);
		}
		int enterTotal = guardService.countEnter(grade,classroom);
		resultMap.put("total", enterTotal);
		resultMap.put("datas", formatList);
		return JSON.toJSONString(resultMap);
	}
	

	/**
	 * 插入一条入校记录(cw)
	 * 
	 * @param enterTime
	 * @param grade
	 * @param classroom
	 * @param studentName
	 * @param entryReasons
	 * @return
	 */

	@RequestMapping("/addEnter")
	@ResponseBody
	public String addStudentEnterEntry(
			@RequestParam(value = "entryTime", defaultValue = "") String enterTime,
			@RequestParam(value = "grade", defaultValue = "") String grade,
			@RequestParam(value = "classroom", defaultValue = "") String classroom,
			@RequestParam(value = "studentName", defaultValue = "") String studentName,
			@RequestParam(value = "entryReasons", defaultValue = "") String entryReasons) {
		int role = getSessionValue().getUserRole();
		if (UserRole.isDoorKeeper(role) || UserRole.isManager(role)) {
			StudentEnterEntry studentEnterEntry = new StudentEnterEntry(
					new ObjectId(), studentName, grade, classroom, enterTime,
					entryReasons);
			ObjectId resultId = guardService
					.addStudentEnterEntry(studentEnterEntry);
			if (resultId == null) {
				return JSON.toJSONString(RespObj.FAILD);
			}
			return JSON.toJSONString(RespObj.SUCCESS);
		}

		return JSON.toJSONString(RespObj.FAILD);
	}

	/**
	 * 删除一条入校记录(cw)
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping("/deleteEnter")
	@ResponseBody
	public String deleteStudentEnterEntry(
			@RequestParam(value = "id", defaultValue = "") String id) {
		int role = getSessionValue().getUserRole();

		if (UserRole.isDoorKeeper(role) || UserRole.isManager(role)) {
			if (StringUtils.isBlank(id)) {
				return JSON.toJSONString(RespObj.FAILD);
			}
			guardService.deleteStudentEnterEntry(new ObjectId(id));
			return JSON.toJSONString(RespObj.SUCCESS);
		}
		return JSON.toJSONString(RespObj.FAILD);
	}

	/**
	 * 出校记录列表(cw)
	 * 
	 * @return
	 */

	@RequestMapping("/outList")
	@ResponseBody

	public Map<String, Object> outStudentInfos(
			@RequestParam(value="skip", defaultValue="1")int skip,
			@RequestParam(value="size", defaultValue="10")int size,
			@RequestParam(value="grade", defaultValue="All")String grade,
			@RequestParam(value="classroom", defaultValue="All")String classroom){
		//获取所有的出校记录
		List<StudentOutEntry> outList = guardService.queryOutStudents(grade, classroom,skip,size);
		// 格式化数据
		List<Map<String, Object>> formatList = formatOutList(outList);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String schoolId = getSessionValue().getSchoolId();
		SchoolEntry schoolEntry = schoolService.getSchoolEntry(new ObjectId(
				schoolId),Constant.FIELDS);
		List<Grade> l1 = schoolEntry.getGradeList();
		List<String> gradeList = new ArrayList<String>();

		List<String> gradeIds = new ArrayList<String>();
		for(int i=0;i<l1.size();i++){
			gradeList.add(l1.get(i).getName());//年级信息
			gradeIds.add(l1.get(i).getGradeId().toString());//年级ID
		}
		
		int role = getSessionValue().getUserRole();
		if(UserRole.isDoorKeeper(role) || UserRole.isManager(role)){
			resultMap.put("power", Constant.ZERO);
		}else{
			resultMap.put("power", Constant.ONE);
		}
		int outTotal = guardService.countOut(grade,classroom);
		resultMap.put("total", outTotal);
		resultMap.put("datas", formatList);
		resultMap.put("gradeList", gradeList);
		resultMap.put("gradeIds", gradeIds);
		
		return resultMap;
	}

	/**
	 * 增加一条出校记录(cw)
	 * 
	 * @param outTime
	 * @param grade
	 * @param classroom
	 * @param studentName
	 * @param approveTeacher
	 * @param outReasons
	 * @return
	 */

	@RequestMapping("/addOut")
	@ResponseBody
	public String addStudentOutEntry(
			@RequestParam(value = "outTime", defaultValue = "") String outTime,
			@RequestParam(value = "grade", defaultValue = "") String grade,
			@RequestParam(value = "classroom", defaultValue = "") String classroom,
			@RequestParam(value = "studentName", defaultValue = "") String studentName,
			@RequestParam(value = "approveTeacher", defaultValue = "") String approveTeacher,
			@RequestParam(value = "outReasons", defaultValue = "") String outReasons) {
		if(StringUtils.isBlank(outTime) || "请选择".equals(grade) || "ALL".equals(classroom) || "ALL".equals(studentName) || StringUtils.isBlank(approveTeacher) || StringUtils.isBlank(outReasons)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		int role = getSessionValue().getUserRole();

		if (UserRole.isDoorKeeper(role) || UserRole.isManager(role)) {
			StudentOutEntry studentOutEntry = new StudentOutEntry();
			studentOutEntry.setOutTime(outTime);
			studentOutEntry.setGrade(grade);
			studentOutEntry.setClassroom(classroom);
			studentOutEntry.setStudentName(studentName);
			studentOutEntry.setApproveTeacher(approveTeacher);
			studentOutEntry.setOutReasons(outReasons);
			studentOutEntry.setIsRemove(0);
			ObjectId resultId = guardService.addStudentOutEntry(studentOutEntry);
			if (StringUtils.isBlank(resultId.toString())) {
				return JSON.toJSONString(RespObj.FAILD);
			}
			return resultId.toString();
		}
		return JSON.toJSONString(RespObj.FAILD);
	}

	/**
	 * 删除一条出校记录(cw)
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping("/deleteOut")
	@ResponseBody
	public String deleteStudentOutEntry(
			@RequestParam(value = "id", defaultValue = "") String id) {
		int role = getSessionValue().getUserRole();

		if (UserRole.isDoorKeeper(role) || UserRole.isManager(role)) {
			if (StringUtils.isBlank(id)) {
				return JSON.toJSONString(RespObj.FAILD);
			}
			guardService.deleteStudentOutEntry(new ObjectId(id));
			return JSON.toJSONString(RespObj.SUCCESS);
		}
		return JSON.toJSONString(RespObj.FAILD);
	}

	/**
	 * 访客记录列表(cw)
	 * @return
	 */
	@RequestMapping("/visitList")
	@ResponseBody
	public String queryVisitors(
			@RequestParam(value="skip", defaultValue="1")int skip,
			@RequestParam(value="size", defaultValue="10")int size){
		String schoolId = getSessionValue().getSchoolId();
		//获取访客数据
		List<VisitorEntry> visitList = guardService.queryVisitors(skip,size);
		//获取部门数据
		List<DepartmentEntry> l3 = departmentService.getDepartmentEntrys(new ObjectId(schoolId));
		List<String> departmentList = new ArrayList<String>();
		for(int i=0;i<l3.size();i++){
			departmentList.add(l3.get(i).getName());
		}
		//格式化数据
		List<Map<String,Object>> formatList = formatVisitList(visitList);
		//装配
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int role = getSessionValue().getUserRole();
		
		if(UserRole.isDoorKeeper(role) || UserRole.isManager(role)){
			resultMap.put("power", Constant.ZERO);//门卫和管理员有操作权限
		}else{
			resultMap.put("power", Constant.ONE);
		}
		int visitTotal = guardService.countVisit();
		resultMap.put("total", visitTotal);
		resultMap.put("datas", formatList);
		resultMap.put("depts", departmentList);
		
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 增加一条来访记录(cw)
	 * @param visitTime
	 * @param visitorName
	 * @param gender
	 * @param company
	 * @param papers
	 * @param telephone
	 * @param numOfPeople
	 * @param reasons
	 * @param department
	 * @param object
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/addVisit")
	@ResponseBody
	public String addVisitorEntry(
			@RequestParam(value="visitTime", defaultValue="")String visitTime,
			@RequestParam(value="visitorName", defaultValue="")String visitorName,
			@RequestParam(value="gender", defaultValue="1")Integer gender,
			@RequestParam(value="company", defaultValue="")String company,
			@RequestParam(value="papers", defaultValue="")String papers,
			@RequestParam(value="telephone", defaultValue="")String telephone,
			@RequestParam(value="numOfPeople", defaultValue="")String numOfPeople,
			@RequestParam(value="reasons", defaultValue="")String reasons,
			@RequestParam(value="department", defaultValue="")String department,
			@RequestParam(value="object", defaultValue="")String object) {
		int role = getSessionValue().getUserRole();
		if(UserRole.isDoorKeeper(role) || UserRole.isManager(role)){
			VisitorEntry ve = new VisitorEntry(visitorName, gender, company, papers, telephone, visitTime, numOfPeople, 
					department, object, reasons);
			ObjectId resultId = guardService.addVisitorEntry(ve);
			if(resultId == null){
				return JSON.toJSONString(RespObj.FAILD);
			}
			return JSON.toJSONString(RespObj.SUCCESS);
		}
		return JSON.toJSONString(RespObj.FAILD);
	}
	
	/**
	 * 删除一条访客记录(cw)
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteVisit")
	@ResponseBody
	public String deleteVisitorEntry(
			@RequestParam(value="id", defaultValue="")String id,
			@RequestParam(value="pageNo", defaultValue="1")String pageNo){
		int role = getSessionValue().getUserRole();
		
		if(UserRole.isDoorKeeper(role) || UserRole.isManager(role)){
			if(StringUtils.isBlank(id)){
				return JSON.toJSONString(RespObj.FAILD);
			}
			guardService.deleteVisitorEntry(new ObjectId(id));
			return JSON.toJSONString(RespObj.SUCCESS);
		}
		return JSON.toJSONString(RespObj.FAILD);
	}
	
	/**
	 * 查看一条访客记录
	 * @param id
	 * @return
	 */
	@RequestMapping("/visitorDetail")
	@ResponseBody
	public VisitorEntry findOneInfo(@RequestParam(value="id", defaultValue="")String id){
			return guardService.getVisitor(new ObjectId(id));
	}

	/**
	 * 格式化查询到入校结果集，供前台使用
	 * 
	 * @param srcList
	 * @return
	 */
	private List<Map<String, Object>> formatEnterList(
			List<StudentEnterEntry> srcList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
		for (StudentEnterEntry e : srcList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", e.getID().toString());
			m.put("sn", e.getStudentName());
			m.put("gd", e.getGrade());
			m.put("cr", e.getClassroom());
			m.put("et", e.getEntryTime());
			m.put("ers", e.getEntryReasons());
			formatList.add(m);
		}
		return formatList;
	}

	/**
	 * 格式化查询到出校结果集，供前台使用
	 * 
	 * @param srcList
	 * @return
	 */
	private List<Map<String, Object>> formatOutList(
			List<StudentOutEntry> srcList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
		for (StudentOutEntry e : srcList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", e.getID().toString());
			m.put("sn", e.getStudentName());
			m.put("gd", e.getGrade());
			m.put("cs",e.getClassroom());
			m.put("ot", e.getOutTime());
			m.put("ors", e.getOutReasons());
			m.put("apt", e.getApproveTeacher());
			formatList.add(m);
		}
		return formatList;
	}

	/**
	 * 格式化查询到访客结果集，供前台使用
	 * 
	 * @param srcList
	 * @return
	 */
	private List<Map<String, Object>> formatVisitList(List<VisitorEntry> srcList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
		for (VisitorEntry e : srcList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", e.getID().toString());
			m.put("vnam", e.getVisitorName());
			m.put("gd", e.getGender());
			m.put("cp", e.getCompany());
			m.put("pp", e.getPapers());
			m.put("te", e.getTelephone());
			m.put("nop", e.getNumOfPeople());
			m.put("vt", e.getVisitTime());
			m.put("dpt", e.getDepartment());
			m.put("obj", e.getObject());
			m.put("rs", e.getReasons());
			formatList.add(m);
		}
		return formatList;
	}


	
	/**
	 * 查班级信息
	 * need  0为列表页面筛选  1为新增页面下拉
	 */
	@RequestMapping("/findClass")
	@ResponseBody
	public Map<String,Object> classInfo(String gradeId,int need){
		Map<String,Object> map = new HashMap<String, Object>();
		
		int count = 0; 
		String regEx = "[\\u4e00-\\u9fa5]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(gradeId.trim()); 
		while (m.find()) { 
		  for (int i = 0; i <= m.groupCount(); i++) { 
		    count = count + 1; 
		   } 
		  }
		if(count>0 || "ALL".equals(gradeId.trim())){
			return map;
		}
		
		List<ClassEntry> l2 = guardService.findByGraId(new ObjectId(gradeId));
		List<String> classList = new ArrayList<String>();
		List<String> classIds = new ArrayList<String>();
		for(int i=0;i<l2.size();i++){
			classList.add(l2.get(i).getName());//班级信息
			classIds.add(l2.get(i).getID().toString());
		}
		map.put("classList", classList);
		map.put("classIds", classIds);
		return map;
	}
	
	/**
	 * 查年级ID和年级名称
	 * @return
	 * need 0为列表页面筛选  1为新增页面下拉
	 */
	@RequestMapping("/findByGrade")
	@ResponseBody
	public Map<String,Object> c(int need){
		String schoolId = getSessionValue().getSchoolId();
		Map<String,Object> map = new HashMap<String, Object>();
		SchoolEntry schoolEntry = schoolService.getSchoolEntry(new ObjectId(
				schoolId),Constant.FIELDS);
		List<Grade> l1 = schoolEntry.getGradeList();
		List<String> gradeList = new ArrayList<String>();

		List<String> gradeIds = new ArrayList<String>();
		for(int i=0;i<l1.size();i++){
			gradeList.add(l1.get(i).getName());//年级信息
			gradeIds.add(l1.get(i).getGradeId().toString());//年级ID
		}
		map.put("gradeList", gradeList);
		map.put("gradeIds", gradeIds);
		return map;
	}
	
	/**
	 * 部门信息的查询
	 */
	@RequestMapping("/findDepart")
	@ResponseBody
	public List<String> findDepart(){
		String schoolId = getSessionValue().getSchoolId();
		List<DepartmentEntry> deList = departmentService.getDepartmentEntrys(new ObjectId(schoolId));
		List<String> deparName = new ArrayList<String>();
		for(int i=0;i<deList.size();i++){
			deparName.add(deList.get(i).getName());
		}
		return deparName;
	}
	
	/**
	 * 根据班级ID查询学生信息
	 */
	@RequestMapping("/findStuInfo")
	@ResponseBody
	public Map<String,Object> findStuInfo(String classId){
		Map<String,Object> map = new HashMap<String, Object>();
		
		int count = 0; 
		String regEx = "[\\u4e00-\\u9fa5]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(classId.trim()); 
		while (m.find()) { 
		  for (int i = 0; i <= m.groupCount(); i++) { 
		    count = count + 1; 
		   } 
		  }
		if(count>0 || "ALL".equals(classId.trim())){
			return map;
		}
		
		List<String> stuName = new ArrayList<String>();
			ClassEntry ce = cs.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
			Collection<ObjectId> stuId = ce.getStudents();
			List<UserEntry> userEntry = guardService.findStuName(stuId, Constant.FIELDS);
			for(int i=0;i<userEntry.size();i++){
				stuName.add(userEntry.get(i).getUserName());//学生姓名
			}
			map.put("stuId", stuId);
			map.put("stuName", stuName);
	
		return map;
	}
}
