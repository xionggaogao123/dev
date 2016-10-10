package com.fulaan.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.common.ProjectContent;
import com.fulaan.dto.ProjectDto;
import com.fulaan.dto.StaffDto;
import com.fulaan.entity.Directory;
import com.fulaan.entity.Project;
import com.fulaan.entity.Staff;
import com.fulaan.service.DirectoryService;
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
	
	/**
	 * 显示项目信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	public String listProjects(HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session) {

		Staff staff = (Staff) session.getAttribute(ProjectContent.LOGIN_USER_IN_SESSION);
		Long totalPro = projectService.getTotalNumByStaffId(staff.getId());
		
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
	@ResponseBody
	@RequestMapping("/show")
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
		List projectList = projectService.getProjectPageListByStaffId(page, size, staff.getId());
		if(projectList == null || projectList.size() <= 0) { // 未获取到数据
			resultMap.put("code", 0);
			resultMap.put("results", null);
		}
		
		List<ProjectDto> results = new ArrayList<ProjectDto>();
		for(int i = 0; i < projectList.size(); i++) {
			Object[] obj = (Object[]) projectList.get(i);
			Staff projectOwner = staffService.get(Staff.class, (int) obj[4]);
			
			ProjectDto pDto = new ProjectDto();
			pDto.setId(new Integer((int) obj[0]));
			pDto.setOwnerName(projectOwner.getName());
			pDto.setProjectName((String) obj[2]);
			pDto.setCreatedDate((Date) obj[8]);
			
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
	public String showDetail(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable int id) {
		Project project = projectService.get(id);
		
		List<Staff> members = project.getMembers();
		Map<String, List<StaffDto>> staffDtoMap = bulidDtoMap(members);
		
		int rootDirId = new Integer(project.getDocsPath()).intValue();
		Directory rootDir = directoryService.get(Directory.class, rootDirId);
		
		request.setAttribute("project", project);
		request.setAttribute("sdtoMap", staffDtoMap);
		request.setAttribute("rootDir", rootDir);
		
		return PROJECT_DETAIL_PAGE;
	}
	
	/**
	 * 新建项目
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/new_project")
	public String newPrj(HttpServletRequest request,
			HttpServletResponse response) {
		
		List<Staff> allStaff = staffService.findAllStaff();
		
		Map<String, List<StaffDto>> staffDtoMap = bulidDtoMap(allStaff);
		
		request.setAttribute("staffs", allStaff);
		request.setAttribute("sdtoMap", staffDtoMap);
		
		return PROJECT_ADD_PAGE;
	}
	
	private Map<String, List<StaffDto>> bulidDtoMap(List<Staff> allStaff) {
		
		Map<String, List<StaffDto>> staffDtoMap = new HashMap<String, List<StaffDto>>();
		
		for(Staff sf : allStaff) {
			StaffDto sdto = new StaffDto();
			String department = sf.getSubDepartment().getDepartment().getDepartmentName();
			sdto.setId(sf.getId());
			sdto.setName(sf.getName());
			sdto.setDepartment(department);
			
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
		
		// 为项目生成文档根目录
		Directory rootDir = new Directory();
		rootDir.setName(project.getProjectName() + "_" + project.getId());
		directoryService.save(rootDir);
		
		project.setDocsPath(rootDir.getId() + ""); // 关联根目录
		
		projectService.save(project);
		
		return PROJECT_ADD_PAGE;
	}
	
	
}
