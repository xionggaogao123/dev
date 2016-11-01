package com.fulaan.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.FlushMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.auth.annotation.AuthFunctionType;
import com.fulaan.auth.annotation.Authority;
import com.fulaan.auth.annotation.ModuleType;
import com.fulaan.auth.annotation.SuperAdmin;
import com.fulaan.common.CommonResult;
import com.fulaan.common.LabelValueVO;
import com.fulaan.common.ProjectContent;
import com.fulaan.common.ProjectStatus;
import com.fulaan.dto.ProjectDto;
import com.fulaan.dto.ProjectLogDto;
import com.fulaan.dto.StaffDto;
import com.fulaan.entity.AuthFunction;
import com.fulaan.entity.Directory;
import com.fulaan.entity.Project;
import com.fulaan.entity.ProjectLog;
import com.fulaan.entity.Staff;
import com.fulaan.service.DirectoryService;
import com.fulaan.service.ProjectLogService;
import com.fulaan.service.ProjectService;
import com.fulaan.service.StaffService;

@Controller
@RequestMapping("/project")
public class ProjectController {

	/**
	 * 显示项目页面
	 */
	private static final String LIST_PROJECTS_PAGE = "projectList";
	
	/**
	 * 项目信息详情页
	 */
	private static final String PROJECT_DETAIL_PAGE = "projectDetial";
	
	/**
	 * 添加新项目页
	 */
	private static final String PROJECT_ADD_PAGE = "newProject";
	
	@Resource
	ProjectService projectService;
	
	@Resource
	StaffService staffService;
	
	@Resource
	DirectoryService directoryService;
	
	@Resource
	ProjectLogService projectLogService;
	
	/**
	 * 显示项目信息
	 * @param request
	 * @param response
	 * @return
	 */
	@SuperAdmin
	@RequestMapping("/list")
	@Authority(module = ModuleType.PROJECT, function = AuthFunctionType.READ)
	public String listProjects(HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session) {

		Long totalPro = null;
		
		Object isSuperAdmin = session.getAttribute(ProjectContent.SUPERADMIN_FLAG);
		if(isSuperAdmin != null && (boolean)isSuperAdmin == true) { // superAdmin特殊处理
			totalPro = projectService.getTotalNumByStaffId(-1); // 查询所有
		} else {
			Staff staff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
			totalPro = projectService.getTotalNumByStaffId(staff.getId());
		}
		
		int temp = totalPro.intValue() / ProjectContent.MAX_RESULT_PER_PAGE;
		int totalPage = totalPro.intValue() % ProjectContent.MAX_RESULT_PER_PAGE == 0 ? temp : temp + 1;
		
		request.setAttribute("totalPage", totalPage);
		
		return LIST_PROJECTS_PAGE;
	}
	
	/**
	 * ajax请求分页数据
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@SuperAdmin
	@ResponseBody
	@RequestMapping("/show")
	@Authority(module = ModuleType.PROJECT, function = AuthFunctionType.READ)
	public Map toPageProjectItems(HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			@RequestParam(defaultValue = "0") int page) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Staff staff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		if(staff == null) {
			resultMap.put("code", 0); // 出错了，session中未获取到登录用户的信息
			resultMap.put("results", null);
			
			return resultMap;
		}
		
		int size = ProjectContent.MAX_RESULT_PER_PAGE;
		List projectList = new ArrayList();
		
		Object isSuperAdmin = session.getAttribute(ProjectContent.SUPERADMIN_FLAG);
		if(isSuperAdmin != null && (boolean)isSuperAdmin == true) { // superAdmin特殊处理
			projectList = projectService.getProjectPageListByStaffId(page, size, -1); // 查询所有
		} else {
			projectList = projectService.getProjectPageListByStaffId(page, size, staff.getId());
		}
		
		if(projectList == null || projectList.size() <= 0) { // 未获取到数据
			resultMap.put("code", 0);
			resultMap.put("results", null);
		}
		
		List<ProjectDto> results = new ArrayList<ProjectDto>();
		for(int i = 0; i < projectList.size(); i++) {
			Object[] obj = (Object[]) projectList.get(i);
			Staff projectOwner = staffService.get(Staff.class, (int) obj[4]);
			Staff projectCreater = staffService.get(Staff.class, (int) obj[10]);
			
			ProjectDto pDto = new ProjectDto();
			pDto.setId(new Integer((int) obj[0]));
			pDto.setOwnerName(projectOwner.getName());
			pDto.setProjectName((String) obj[2]);
			pDto.setCreatedDate((Date) obj[8]);
			pDto.setCreaterName(projectCreater != null ? projectCreater.getName() : null);
			
			results.add(pDto);
		}
		
		// 获取成功
		resultMap.put("code", 1);
		resultMap.put("results", results);
		
		return resultMap;
	}
	
	/**
	 * 显示项目详情
	 * @return
	 */
	@RequestMapping("/{id}/detail")
	@Authority(module = ModuleType.PROJECT, function = AuthFunctionType.READ)
	public String showDetail(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable int id) {
		Project project = projectService.get(id);
		
		List<Staff> allStaff = staffService.findAllStaff();
		Map<String, List<StaffDto>> allStaffDtoMap = bulidDtoMap(allStaff);
		
		List<Staff> members = project.getMembers();
		Map<String, List<StaffDto>> staffDtoMap = bulidDtoMap(members);
		
		int rootDirId = new Integer(project.getDocsPath()).intValue();
		Directory rootDir = directoryService.get(Directory.class, rootDirId);
		
		request.setAttribute("project", project);
		request.setAttribute("sdtoMap", staffDtoMap);
		request.setAttribute("allStaffMap", allStaffDtoMap);
		request.setAttribute("rootDir", rootDir);
		request.setAttribute("statusVO", ProjectStatus.getStatusVO());
		request.setAttribute("status", ProjectStatus.getStatusByCode(project.getProjectStatus()));
		
		return PROJECT_DETAIL_PAGE;
	}
	
	/**
	 * 新建项目
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/new_project")
	@Authority(module = ModuleType.PROJECT, function = AuthFunctionType.INSERT)
	public String newPrj(HttpServletRequest request,
			HttpServletResponse response) {
		
		List<Staff> allStaff = staffService.getActiveStaff();
		Collections.sort(allStaff, new Comparator<Staff>() {

			@Override
			public int compare(Staff o1, Staff o2) {
				String departmentName1 = o1.getDepartment().getDepartmentName();
				String departmentName2 = o2.getDepartment().getDepartmentName();
				return departmentName1.compareTo(departmentName2);
			}
		});
		
		Map<String, List<StaffDto>> staffDtoMap = bulidDtoMap(allStaff);
		//Map<String, List<StaffDto>> staffDtoBySubDepartMap = bulidSubDepartMap(allStaff);
		
		Iterator<Staff> iterator = allStaff.iterator();
		while(iterator.hasNext()) { // 去除不能作为项目负责人的员工
			Staff s = iterator.next();
			if(s.getIsPrjOwner() == null 
					|| "0".equals(s.getIsPrjOwner())) {
				iterator.remove();
			}
		}
		
		request.setAttribute("staffs", allStaff);
		request.setAttribute("sdtoMap", staffDtoMap);
		//request.setAttribute("sdtoSubDepartMap", staffDtoBySubDepartMap);
		
		return PROJECT_ADD_PAGE;
	}
	
	private Map<String, List<StaffDto>> bulidSubDepartMap(List<Staff> allStaff) {
		
		Map<String, List<StaffDto>> staffDtoMap = new TreeMap<String, List<StaffDto>>();
		
		for(Staff sf : allStaff) {
			StaffDto sdto = new StaffDto();
			if(sf.getDepartment() != null) {
				String department = sf.getDepartment().getDepartmentName();
				sdto.setDepartment(department);
				sdto.setDepartmentId(sf.getDepartment().getId());
			}
			if(sf.getSubDepartment() != null) {
				String subDepartment = sf.getSubDepartment().getSubDepartmentName();
				sdto.setSubDepartment(subDepartment);
				sdto.setSubDepartmentId(sf.getSubDepartment().getId());
			}
			sdto.setId(sf.getId());
			sdto.setName(sf.getName());
			sdto.setJobNumber(sf.getJobNumber());
			sdto.setJobTitle(sf.getJobTitle());
			
			String key = sdto.getDepartment() + sdto.getSubDepartment();
			if(staffDtoMap.containsKey(key)) {
				staffDtoMap.get(key).add(sdto);
			} else {
				List<StaffDto> dtoList = new ArrayList<StaffDto>();
				dtoList.add(sdto);
				staffDtoMap.put(key, dtoList);
			}
		}
		
		return staffDtoMap;
	}
	
	private Map<String, List<StaffDto>> bulidDtoMap(List<Staff> allStaff) {
		
		Map<String, List<StaffDto>> staffDtoMap = new HashMap<String, List<StaffDto>>();
		
		for(Staff sf : allStaff) {
			if(sf.getIsDeleted() != null 
					&& sf.getIsDeleted() == 1) { // 该员工已被删除
				continue;
			}
			StaffDto sdto = new StaffDto();
			if(sf.getDepartment() != null) {
				String department = sf.getDepartment().getDepartmentName();
				sdto.setDepartment(department);
				sdto.setDepartmentId(sf.getDepartment().getId());
			}
			if(sf.getSubDepartment() != null) {
				String subDepartment = sf.getSubDepartment().getSubDepartmentName();
				sdto.setSubDepartment(subDepartment);
				sdto.setSubDepartmentId(sf.getSubDepartment().getId());
			}
			sdto.setId(sf.getId());
			sdto.setName(sf.getName());
			sdto.setJobNumber(sf.getJobNumber());
			sdto.setJobTitle(sf.getJobTitle());
			
			if(staffDtoMap.containsKey(sdto.getDepartment())) {
				staffDtoMap.get(sdto.getDepartment()).add(sdto);
			} else {
				List<StaffDto> dtoList = new ArrayList<StaffDto>();
				dtoList.add(sdto);
				staffDtoMap.put(sdto.getDepartment(), dtoList);
			}
		}
		
		return staffDtoMap;
	}
	
	/**
	 * 保存新建项目
	 * @return
	 */
	@RequestMapping("/save")
	@Authority(module = ModuleType.PROJECT, function = AuthFunctionType.INSERT)
	public String save(HttpServletRequest request,
			HttpServletResponse response,
			Project project,
			@RequestParam int[] staffs) {
		
		project.setCreatedTime(new Date());
		List<Staff> members = new ArrayList<Staff>();
		for(int sId : staffs) {
			Staff staff = new Staff();
			staff.setId(sId);
			members.add(staff);
		}
		project.setMembers(members);
		
		Staff staff = (Staff) request.getSession().getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		project.setProjectCreater(staff); // 项目创建人
		
		// 为项目生成文档根目录
		projectService.save(project);
		
		Directory rootDir = new Directory();
		rootDir.setName(project.getProjectName() + "_" + project.getId());
		directoryService.save(rootDir);
		
		project.setDocsPath(rootDir.getId() + ""); // 关联根目录
		projectService.update(project);
		
		return "redirect:/project/list";
	}
	
	/**
	 * 更新项目组成员
	 * @param prjId 项目id
	 * @param ids 项目成员id数组
	 */
	@ResponseBody
	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	@Authority(module = ModuleType.PROJECT, function = AuthFunctionType.UPDATE)
	private Map<String, Object> updateProjectGroup(@RequestParam int prjId,
			@RequestParam(value = "ids[]") int[] ids) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		CommonResult commonInfo = null;
		
		Project project = projectService.get(prjId);
		if(project == null) {
			commonInfo = new CommonResult(1, "error", "项目不存在");
			result.put("info", commonInfo);
			return result;
		}

		List<Staff> prjMemberList = new ArrayList<>();
		for(int i = 0; i < ids.length; i++) {
			Staff staff = staffService.get(Staff.class, ids[i]);
			if(staff == null) {
				commonInfo = new CommonResult(1, "error", "id:" + ids[i] + "员工不存在");
				result.put("info", commonInfo);
				return result;
			}
			prjMemberList.add(staff);
		}
		
		project.setMembers(prjMemberList);
		projectService.update(project);
		
		Map<String, List<StaffDto>> staffDtoMap = bulidDtoMap(prjMemberList);
		commonInfo = new CommonResult(0, "success", "更新成功");
		
		result.put("info", commonInfo);
		result.put("data", staffDtoMap.values());
		
		return result;
	}
	
	/**
	 * 添加项目日志
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add_log", method = RequestMethod.POST)
	public CommonResult addProjectLog(HttpSession session,
			@RequestParam int prjId,
			@RequestParam String logInfo) {
		
		CommonResult result = null;
		
		Staff loginStaff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		
		Project project = projectService.get(prjId);
		if(project == null) {
			result = new CommonResult(1, "error", "不存在该项目");
			return result;
		}
		
		List<Staff> memberList = project.getMembers();
		Staff prjOwner = project.getProjectOwner();
		Staff prjCreater = project.getProjectCreater();
		if(prjOwner == null || prjCreater == null) {
			result = new CommonResult(1, "error", "未找到相关负责人");
			return result;
		}
		
		boolean isMem = false; // 是否为属于该项目
		int loginId = loginStaff.getId();
		if(loginId == prjOwner.getId()) { // 是否为该项目负责人
			isMem = true;
		}
		if(loginId == prjCreater.getId()) { // 是否为该项目创建者
			isMem = true;
		}
		for(Staff m : memberList) { // 是否为该项目成员
			if(loginId == m.getId()) {
				isMem = true;
			}
		}
		if(!isMem) { // 非该项目成员
			if(loginStaff.getIsPrjOwner() == null 
					|| "0".equals(loginStaff.getIsPrjOwner())) {
				result = new CommonResult(1, "error", "非项目成员不能添加日志");
				return result;
			}
		}
		
		ProjectLog log = new ProjectLog();
		log.setLogInfo(logInfo);
		log.setProject(project);
		log.setCreatedUser(loginStaff);
		log.setCreatedTime(Calendar.getInstance().getTime());
		projectLogService.save(log);
		
		result = new CommonResult(0, "success", "添加成功");
		
		return result;
		
	}
	
	/**
	 * 返回该项目下所有日志生产的时间列表
	 * @param prjId 项目id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/log_dates", method = RequestMethod.POST)
	public List<String> getLogTime(@RequestParam int prjId) {
		
		List<Date> dateList = projectLogService.getProLogDateList(prjId);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		List<String> logDateList = new ArrayList<String>();
		for(Date d : dateList) {
			String date = format.format(d);
			if(!logDateList.contains(date)) {
				logDateList.add(date);
			}
		}
		
		return logDateList;
	}
	
	/**
	 * 根据日期进行日志查询
	 * @param prjId
	 * @param date
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logInDate")
	public Map<String, Object> getLogByDate(@RequestParam int prjId,
			@RequestParam String date,
			HttpSession session) {
		
		CommonResult info = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Staff loginStaff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		
		Project project = projectService.get(prjId);
		
		if(project == null) {
			info = new CommonResult(1, "error", "不存在该项目");
			resultMap.put("info", info);
			return resultMap;
		}
		
		List<Staff> memberList = project.getMembers();
		Staff prjOwner = project.getProjectOwner();
		Staff prjCreater = project.getProjectCreater();
		if(prjOwner == null || prjCreater == null) {
			info = new CommonResult(1, "error", "未找到相关负责人");
			resultMap.put("info", info);
			return resultMap;
		}
		
		boolean isMem = false; // 是否为属于该项目
		int loginId = loginStaff.getId();
		if(loginId == prjOwner.getId()) { // 是否为该项目负责人
			isMem = true;
		}
		if(loginId == prjCreater.getId()) { // 是否为该项目创建者
			isMem = true;
		}
		for(Staff m : memberList) { // 是否为该项目成员
			if(loginId == m.getId()) {
				isMem = true;
			}
		}
		if(!isMem) { // 非该项目成员
			if(loginStaff.getIsPrjOwner() == null 
					|| "0".equals(loginStaff.getIsPrjOwner())) {
				info = new CommonResult(1, "error", "非项目成员不能查看日志");
				resultMap.put("info", info);
				return resultMap;
			}
		}
		
		List<ProjectLogDto> logDtoList = new ArrayList<ProjectLogDto>();
		List<ProjectLog> prjLogList = projectLogService.getLogByDate(prjId, date);
		for(ProjectLog p : prjLogList) {
			ProjectLogDto dto = new ProjectLogDto(p);
			logDtoList.add(dto);
		}
		
		info = new CommonResult(0, "success", "查询成功");
		resultMap.put("info", info);
		resultMap.put("data", logDtoList);
		
		return resultMap;
	}
	
	/**
	 * 获取所有项目状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/allStatus", method = RequestMethod.POST)
	public List<LabelValueVO> getProjectStatus() {
		return ProjectStatus.getStatusVO();
	}
	
	/**
	 * 更改项目状态
	 * @param prjId 项目id
	 * @param status 状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/chgStatus", method = RequestMethod.POST)
	@Authority(module = ModuleType.PROJECT, function = AuthFunctionType.UPDATE)
	public CommonResult changeProjectStatus(@RequestParam int prjId,
			@RequestParam int status) {
		
		CommonResult result = null;
		
		Project project = projectService.get(prjId);
		if(project == null) {
			result = new CommonResult(1, "error", "不存在该项目");
			return result;
		}
		
		if(!ProjectStatus.isCorrectStatus(status)) {
			result = new CommonResult(1, "error", "非法项目状态");
			return result;
		}
		
		project.setProjectStatus(status);
		projectService.update(project);
		
		result = new CommonResult(0, "success", "更新成功");
		
		return result;
	}
	
	/**
	 * 查看项目日志
	 * @param session
	 * @param prjId 项目id
	 * @return
	 */
	@ResponseBody
	//@RequestMapping(value = "/logs", method = RequestMethod.POST)
	public Map<String, Object> getPrjLogs(HttpSession session,
			@RequestParam int prjId,
			@RequestParam(defaultValue = "0") int index) {
		
		CommonResult info = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Staff loginStaff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		
		Project project = projectService.get(prjId);
		
		if(project == null) {
			info = new CommonResult(1, "error", "不存在该项目");
			resultMap.put("info", info);
			return resultMap;
		}
		
		List<Staff> memberList = project.getMembers();
		Staff prjOwner = project.getProjectOwner();
		Staff prjCreater = project.getProjectCreater();
		if(prjOwner == null || prjCreater == null) {
			info = new CommonResult(1, "error", "未找到相关负责人");
			resultMap.put("info", info);
			return resultMap;
		}
		
		boolean isMem = false; // 是否为属于该项目
		int loginId = loginStaff.getId();
		if(loginId == prjOwner.getId()) { // 是否为该项目负责人
			isMem = true;
		}
		if(loginId == prjCreater.getId()) { // 是否为该项目创建者
			isMem = true;
		}
		for(Staff m : memberList) { // 是否为该项目成员
			if(loginId == m.getId()) {
				isMem = true;
			}
		}
		if(!isMem) { // 非该项目成员
			if(loginStaff.getIsPrjOwner() == null 
					|| "0".equals(loginStaff.getIsPrjOwner())) {
				info = new CommonResult(1, "error", "非项目成员不能查看日志");
				resultMap.put("info", info);
				return resultMap;
			}
		}
		
		// 日志查询语句
		String logQuerySql = "select count(*) from ProjectLog where project.id = ? order by createdTime";
		long logCount = projectLogService.count(logQuerySql, prjId);
		
		String pageQuerySql = "from ProjectLog where project.id = ? order by createdTime DESC".replace("?", prjId + "");
		List logList = projectLogService.getPageItem(pageQuerySql, index = index > 0 ? index : 0, 1); // 每次查询一条
		
		ProjectLogDto logDto = null;
		if(logList != null && logList.size() > 0) {
			logDto = new ProjectLogDto((ProjectLog) logList.get(0));
		}
		
		info = new CommonResult(0, "success", "查询成功");
		resultMap.put("data", logDto);
		resultMap.put("info", info);
		resultMap.put("count", logCount);
		resultMap.put("index", index);
		
		return resultMap;
	}
	
}
