package com.fulaan.reward.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.reward.service.RewardService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.ExportUtil;
import com.pojo.app.IdNameValuePairDTO;

import com.pojo.reward.RewardEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * 奖惩controller,处理奖惩功能请求
 * 
 * @author cxy
 *
 */
@Controller
@RequestMapping("/reward")
public class RewardController extends BaseController {
	@Autowired
	private ClassService classService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RewardService rewardService;
	
	@Autowired
	private DepartmentService departmentService;
	
	private static int x;

	
	private static final Logger logger = Logger
			.getLogger(RewardController.class);

	/**
	 * 页面跳转和初始化
	 */
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView retrieveRewardInfo() {
		// String schoolId = getSessionValue().getSchoolId();
		// 这里本应动态获取schoolId，如上一行代码，但没有登录功能，构造一个。
		String schoolId = getSessionValue().getSchoolId();
		// 获取所有部门并重新组装
		List<DepartmentEntry> depsSrc = departmentService.getDepartmentEntrys(new ObjectId(
				schoolId));
		List<Map<String, String>> deps = new ArrayList<Map<String, String>>();
		for (int i = 0; i < depsSrc.size(); i++) {
			DepartmentEntry de = depsSrc.get(i);
			Map<String, String> m = new HashMap<String, String>();
			m.put("id", de.getID().toString());
			m.put("name", de.getName());
			deps.add(m);
		}
		// 获取所有年级
		List<GradeView> grades = schoolService.findGradeList(schoolId);
		// 获取所有班级
		List<ClassInfoDTO> classes = classService.findClassInfoBySchoolId(schoolId);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("grades", grades);
		m.put("classes", classes);
		m.put("departments", deps);
		String jsonString = JSON.toJSONString(m);
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", jsonString);
		mv.setViewName("jiangcheng/jiangcheng");
		return mv;
	}

	/**
	 * 根据条件获取奖惩ADD页面的学生
	 */
	@RequestMapping("/queryStudents")
	@ResponseBody
	public String queryStudents(
			@RequestParam(value = "gradeId", defaultValue = "ALL") String gradeId,
			@RequestParam(value = "classId", defaultValue = "ALL") String classId) {
		// String schoolId = getSessionValue().getSchoolId();
		// 这里本应动态获取schoolId，如上一行代码，但没有登录功能，构造一个。
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		String schoolId = getSessionValue().getSchoolId();
		boolean isAllGrades = false;
		boolean isAllClasses = false;
		if ("ALL".equals(gradeId)) {
			isAllGrades = true;
		}
		if ("ALL".equals(classId)) {
			isAllClasses = true;
		}
		Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> studentMap = userService
								.getStudentOrParentMap(new ObjectId(schoolId), 1);
		Set<Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>>> outSet = studentMap
				.entrySet();
		Iterator<Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>>> outIt = outSet
				.iterator();
		while (outIt.hasNext()) {
			Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> outEntry = outIt
					.next();
			ClassEntry ce = null;
			if (!isAllGrades) {
				ce = classService.getClassEntryById(new ObjectId(outEntry.getKey()
						.getIdStr()), Constant.FIELDS);
			}
			if (isAllGrades || (ce != null && ce.getGradeId() != null && gradeId.equals(ce.getGradeId() + ""))) { 
				if (isAllClasses
						|| classId.equals(outEntry.getKey().getIdStr())) {
					Set<IdNameValuePairDTO> inSet = outEntry.getValue();
					Iterator<IdNameValuePairDTO> inIt = inSet.iterator();
					while (inIt.hasNext()) {
						IdNameValuePairDTO ivp = inIt.next();
						Map<String, String> subMap = new HashMap<String, String>();
						subMap.put("id", ivp.getIdStr());
						subMap.put("name", ivp.getValue().toString());
						resultList.add(subMap);
					}
				}
			}
		}
		return JSON.toJSONString(resultList);
	}

	/**
	 * 新增奖惩信息
	 */
	@RequestMapping("/saveReward")
	@ResponseBody
	public String saveReward(
			@RequestParam(value = "rewardType", defaultValue = "") String rewardType,
			@RequestParam(value = "rewardGrade", defaultValue = "") String rewardGrade,
			@RequestParam(value = "rewardDate", defaultValue = "") String rewardDate,
			@RequestParam(value = "students", defaultValue = "") String students,
			@RequestParam(value = "rewardContent", defaultValue = "") String rewardContent,
			@RequestParam(value = "departments", defaultValue = "") String departments,
			@RequestParam(value = "classes", defaultValue = "") String classes) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String schoolId = getSessionValue().getSchoolId();
		long rd = 0;
		try {
			if ("".equals(rewardDate)) {

				rd = sdf.parse(sdf.format(new Date())).getTime();

			} else {
				rd = sdf.parse(rewardDate).getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "{\"success\":\"false\"}";
		}
		
		String rt = "表彰奖励";
		if ("PUNISHMENT".equals(rewardType)) {
			rt = "违纪违规";
		}

		List<ObjectId> depList = uploadObjectIdFromString(departments);
		List<ObjectId> classesList = uploadObjectIdFromString(classes);
		
		if("".equals(students)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		String[] studentIds = students.split(",");
		for (int i = 0; i < studentIds.length; i++) {
			System.out.println(studentIds[i]);
			UserEntry ue = userService.searchUserId(new ObjectId(studentIds[i]));
			ClassEntry ce = classService.getClassEntryByStuId(
					new ObjectId(studentIds[i]), Constant.FIELDS);
			RewardEntry re = new RewardEntry("2015第二学期", ce.getGradeId(),
					ce.getID(), rt, rewardGrade, studentIds[i],
					ue.getUserName(), rewardContent, depList, classesList,
					new ObjectId(schoolId), rd,schoolService.getStudentGrade(new ObjectId(studentIds[i])).getName(),ce.getName());
			rewardService.addRewardEntry(re);
		}
//		return "{\"success\":\"true\"}";
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 组装新增用的List<ObjectId>
	 * @param src
	 * @return
	 */
	private List<ObjectId> uploadObjectIdFromString(String src) {
		List<ObjectId> list = new ArrayList<ObjectId>();
		String[] ids = src.split(",");
		for (int i = 0; i < ids.length; i++) {
			if("".equals(ids[i])){
				break;
			}
			list.add(new ObjectId(ids[i]));
		}
		return list;
	}

	/**
	 * 查询奖惩列表
	 * @param gradeId
	 * @param classId
	 * @param rewardType
	 * @param rewardGrade
	 * @param studentName
	 * @return
	 */

	@RequestMapping("/queryRewards")
	@ResponseBody
	public String queryRewards(
			@RequestParam(value = "gradeId", defaultValue = "ALL") String gradeId,
			@RequestParam(value = "classId", defaultValue = "ALL") String classId,
			@RequestParam(value = "rewardType", defaultValue = "ALL") String rewardType,
			@RequestParam(value = "rewardGrade", defaultValue = "ALL") String rewardGrade,
			@RequestParam(value = "studentName", defaultValue = "ALL") String studentName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String schoolId = getSessionValue().getSchoolId();
		int role = getSessionValue().getUserRole();
		boolean isAdmin = UserRole.isManager(role) || UserRole.isHeadmaster(role) || UserRole.isK6ktHelper(role);
		String rt = "ALL";
		if ("PUNISHMENT".equals(rewardType)) {
			rt = "违纪违规";
		}
		if("REWARD".equals(rewardType)){
			rt = "表彰奖励";
		}
		List<RewardEntry> srcList = rewardService.queryRewardsByfields(
				"ALL".equals(gradeId)?null:new ObjectId(gradeId), "ALL".equals(classId)?null:new ObjectId(classId), rt, rewardGrade,
				studentName,new ObjectId(schoolId));
		List<RewardEntry> listAfterRole = isAdmin?srcList:getListByRole(srcList);
		List<RewardEntry> listAfterPage = getListByPage(listAfterRole,pageNo,pageSize);
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		for(int i=0;i<listAfterPage.size();i++){ 
			RewardEntry re = listAfterPage.get(i);
			Map<String,String> map = new HashMap<String,String>();
			map.put("id", re.getID().toString());
			map.put("name", re.getStudentName());
			map.put("nianji", re.getGradeName() );
			map.put("banji", re.getClassName());
			map.put("type", re.getRewardType());
			map.put("dengji", re.getRewardGrade());
			map.put("date", sdf.format(new Date(re.getRewardTime())));
			map.put("contents", re.getRewardContent());
			List<ObjectId> prsList= re.getPublicityRangeStudents();
			StringBuffer prsSb = new StringBuffer("");
			for(int j=0;j<prsList.size();j++){
				prsSb.append(prsList.get(j).toString());
				if(j != prsList.size() - 1){
					prsSb.append(",");
				}
			}
			List<ObjectId> prtList= re.getPublicityRangeTeachers();
			StringBuffer prtSb = new StringBuffer("");
			for(int j=0;j<prtList.size();j++){
				prtSb.append(prtList.get(j).toString());
				if(j != prtList.size() - 1){
					prtSb.append(",");
				}
			}
			map.put("departments", prtSb.toString());
			map.put("classes", prsSb.toString());
			resultList.add(map);
		}
		//最终组装数据
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("cur",pageNo);
		pageMap.put("total",listAfterRole.size());
		resultMap.put("rewardData",resultList);
		resultMap.put("pagejson", pageMap);
		return JSON.toJSONString(resultMap); 
	}
	/**
	 * 根据角色筛结果集 
	 * @param srcList
	 * @return
	 */
	private List<RewardEntry> getListByRole(List<RewardEntry> srcList){
		List<RewardEntry> list = new ArrayList<RewardEntry>();
		List<String> depList = new ArrayList<String>();
		List<String> claList = new ArrayList<String>();
		ObjectId userId = getUserId();
		int role = getSessionValue().getUserRole();
		if(UserRole.isEducation(role)){
			return srcList;
		}
		//学生或家长
		if(UserRole.isStudentOrParent(role)){
			if(UserRole.isStudent(role)){
				claList.add(classService.getClassEntryByStuId(userId,Constant.FIELDS).getID().toString());
			}else{
				claList.add(classService.getClassEntryByStuId(new ObjectId(userService.findStuInfoByParentId(userId.toString()).getId()),Constant.FIELDS).getID().toString());
			}
		}
		//教师，班主任，学科组长，年级组长
		if(UserRole.isTeacher(role) || UserRole.isLeaderOfSubject(role) || UserRole.isLeaderClass(role) || UserRole.isLeaderOfGrade(role)){
			//首先找到所在部门（教师、学科组长到此结束）
			List<DepartmentEntry> userDepList = departmentService.getDepartmentsByUserId(userId);
			for(DepartmentEntry d : userDepList){
				depList.add(d.getID().toString());
			}
			//添加所教班级
			List<ClassInfoDTO> classesOfTeacher = classService.findClassInfoByTeacherId(userId);
			for(ClassInfoDTO c : classesOfTeacher){
				claList.add(c.getId());
			}
			//如果是班主任，则添加所带班级，注意去重
			if(UserRole.isLeaderClass(role)){
				ClassEntry ce = classService.findClassByClassLeaderUserId(userId);
				if(ce != null || !claList.contains(ce.getID().toString())){
					claList.add(ce.getID().toString());
				}
			}
			//如果是如果是年级组长，添加该年级所有班级，注意去重
			if(UserRole.isLeaderOfGrade(role)){
				SchoolEntry se = schoolService.getSchoolEntryByUserIdWithoutException(userId);
				if(se != null){
					List<Grade> grades = se.getGradeList();
					for(Grade g : grades){
						if(userId.toString().equals(g.getLeader().toString())){
							List<ClassInfoDTO> classedOfGrade = classService.getGradeClassesInfo(g.getID().toString());
							if(classedOfGrade != null){
								for(ClassInfoDTO c : classedOfGrade){
									if(!claList.contains(c.getId())){
										claList.add(c.getId());
									}
								}
							}
						}
					}
				}
			}
			//开始根据部门和班级筛选
			
			for(RewardEntry e : srcList){
				//首先根据部门筛选
				List<ObjectId> publicityDepIds = e.getPublicityRangeTeachers();
				boolean isAdded = false;
				for(String depId : depList){
					if(publicityDepIds.contains(new ObjectId(depId))){
						list.add(e);
						isAdded = true;
						break;
					} 
				}
				//再根据班级筛选
				if(!isAdded){
					List<ObjectId> publicityClaIds = e.getPublicityRangeStudents();
					for(String claId : claList){
						if(publicityClaIds.contains(new ObjectId(claId)) && !list.contains(e)){
							list.add(e);
							break;
						}
					}  
				}
			}
		}
//		classService.addTeacher(new ObjectId("5587d8347f72332a4db24c15"), userId); 
		return list; 
	}
	
	private List<RewardEntry> getListByPage(List<RewardEntry> src,int pageNo,int pageSize){
		List<RewardEntry> list = new ArrayList<RewardEntry>();
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
	 * 更新一条数据
	 * @param id
	 * @param rewardType
	 * @param rewardGrade
	 * @param rewardDate
	 * @param rewardContent
	 * @param departments
	 * @param classes
	 * @return
	 */

	@RequestMapping("/updateReward")
	@ResponseBody
	public String updateReward(
			@RequestParam(value = "id") String id,
			@RequestParam(value = "rewardType") String rewardType,
			@RequestParam(value = "rewardGrade") String rewardGrade,
			@RequestParam(value = "rewardDate") String rewardDate,
			@RequestParam(value = "rewardContent") String rewardContent,
			@RequestParam(value = "departments") String departments,
			@RequestParam(value = "classes") String classes){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String rt = "表彰奖励";
		if ("PUNISHMENT".equals(rewardType)) {
			rt = "违纪违规";
		}
		
		long rd = 0L;
		try {
			rd = sdf.parse(rewardDate).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return "{\"success\":\"false\"}";
		}
		String[] deps = departments.split(",");
		String[] cls = classes.split(",");
		List<ObjectId> depList = new ArrayList<ObjectId>(); 
		List<ObjectId> clList = new ArrayList<ObjectId>();
		if(!("".equals(departments))){
			for(String dep: deps){
				depList.add(new ObjectId(dep.split("-")[1]));
			}
		}
		if(!("".equals(classes))){
			for(String cl: cls){
				clList.add(new ObjectId(cl.split("-")[1]));
			}
		} 
		
		rewardService.updateReward(new ObjectId(id), rt, rewardGrade, rd, rewardContent, depList, clList);
		
		return "{\"success\":\"true\"}";
	}
	/**
	 * 删除一条
	 * @param id
	 * @return
	 */

	@RequestMapping("/deleteReward")
	@ResponseBody
	public String deleteReward(@RequestParam(value = "id") String id){
		rewardService.deleteReward(new ObjectId(id));
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 导出奖惩列表
	 * @param gradeId
	 * @param classId
	 * @param rewardType
	 * @param rewardGrade
	 * @param studentName
	 * @return
	 */

	@RequestMapping("/exportRewards")
	@ResponseBody
	public String exportRewards(
			@RequestParam(value = "gradeId", defaultValue = "ALL") String gradeId,
			@RequestParam(value = "classId", defaultValue = "ALL") String classId,
			@RequestParam(value = "rewardType", defaultValue = "ALL") String rewardType,
			@RequestParam(value = "rewardGrade", defaultValue = "ALL") String rewardGrade,
			@RequestParam(value = "studentName", defaultValue = "ALL") String studentName,
			HttpServletRequest request, HttpServletResponse response)  throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String schoolId = getSessionValue().getSchoolId();
		int role = getSessionValue().getUserRole();
		boolean isAdmin = UserRole.isManager(role) || UserRole.isHeadmaster(role) || UserRole.isK6ktHelper(role);
		String rt = "ALL";
		if ("PUNISHMENT".equals(rewardType)) {
			rt = "违纪违规";
		}
		if("REWARD".equals(rewardType)){
			rt = "表彰奖励";
		}
		List<RewardEntry> srcList = rewardService.queryRewardsByfields(
				"ALL".equals(gradeId)?null:new ObjectId(gradeId), "ALL".equals(classId)?null:new ObjectId(classId), rt, rewardGrade,
				studentName,new ObjectId(schoolId));
		List<RewardEntry> listAfterRole = isAdmin?srcList:getListByRole(srcList);
		
		response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        int index = 1;
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            List<String> datas = new ArrayList<String>();
            datas.add("序号");
            datas.add("姓名");
            datas.add("年级");
            datas.add("班级");
            datas.add("类型");
            datas.add("等级");
            datas.add("内容");
            datas.add("奖惩日期");
            util.addTitle(datas.toArray());
          //获取导出数据
            for (RewardEntry e : listAfterRole) {
            	datas.clear();
                datas.add(index++ + "");
                datas.add(e.getStudentName());
                datas.add(e.getGradeName());
                datas.add(e.getClassName());
                datas.add(e.getRewardType());
                datas.add(e.getRewardGrade());
                datas.add(e.getRewardContent());
                datas.add(sdf.format(new Date(e.getRewardTime())));

                util.appendRow(datas.toArray());
            }
            util.setFileName(String.format("%s_%s", sdf.format(new Date()), "_奖惩列表.xlsx"));
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
		return null;
	}
	/**
     * 获取导出文件的名称
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }
    

	@RequestMapping("/createStudents")
	@ResponseBody
	public String createStudents(){
//    	ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
//    	List<ClassEntry> classes = classService.findClassInfoBySchoolId(schoolId);
//    	int stuNo = 1;
//    	for(ClassEntry ce : classes){
//    		for(int i=0;i<60;i++){
//    			ObjectId uid = userService.addUser(new UserEntry("人造人" + stuNo + "号", "123456", 0, "人造人" + stuNo + "号", 1, "", "", "", "", "", "", "", "", "","", 0, System.currentTimeMillis(), "", schoolId, System.currentTimeMillis(), System.currentTimeMillis(), 888, "", "", "", "", 0, null, "", new ArrayList<IdValuePair>()));
//    			classService.addStudent(ce.getID(), uid);
//    			stuNo++;
//    		}
//    	}
    	return null;
	}
}
