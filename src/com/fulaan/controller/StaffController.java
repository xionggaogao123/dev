package com.fulaan.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.common.CommonResult;
import com.fulaan.common.ProjectContent;
import com.fulaan.dao.base.BaseDao;
import com.fulaan.dto.StaffDto;
import com.fulaan.entity.Department;
import com.fulaan.entity.Project;
import com.fulaan.entity.Staff;
import com.fulaan.entity.SubDepartment;
import com.fulaan.service.ProjectService;
import com.fulaan.service.StaffService;
import com.fulaan.util.MD5Util;

@Controller
@RequestMapping("/staff")
public class StaffController {
	
	// 所有职员页面
	private final static String ALL_STAFF_PAGE = "member"; 
	
	@Resource
	BaseDao baseDao;
	
	@Resource
	StaffService staffService;
	
	@Resource
	ProjectService projectService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response) {
		Long total = staffService.getTotalNum(Staff.class);
		if(total == null || total.intValue() == 0) {
			request.setAttribute("totalPage", 0);
		} else {
			int pageSize = ProjectContent.MAX_RESULT_PER_PAGE;
			int page = 0;
			int temp = total.intValue() / pageSize;
			page = total.intValue() % pageSize == 0 ? temp : temp + 1;
			request.setAttribute("totalPage", page);
		}
		
		List<Department> departmentList = baseDao.findAll(Department.class);
		
		request.setAttribute("departmentList", departmentList);
		
		return ALL_STAFF_PAGE;
	}
	
	/**
	 * 显示所有职员信息
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("null")
	@ResponseBody
	@RequestMapping(value = "/show", method = RequestMethod.POST)
	public Map showStaffByPage(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(defaultValue = "0") Integer pageNum) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		Long total = staffService.getTotalNum(Staff.class);
		if(total == null || total.intValue() == 0) { // 未查询到结果
			resultMap.put("code", 0);
			resultMap.put("page", 0);
			resultMap.put("results", null);
		} else { // 返回结果
			int pageSize = ProjectContent.MAX_RESULT_PER_PAGE;
			List<Staff> resutls = staffService.getStaffsByPageNum(pageNum, pageSize);
			int page = 0;
			int temp = total.intValue() / pageSize;
			page = total.intValue() % pageSize == 0 ? temp : temp + 1;
			
			resultMap.put("code", 1);
			resultMap.put("page", page);
			
			List<StaffDto> tempResults = new ArrayList<>();
			for(Staff s : resutls) { // 将结果保存到Dto中
				StaffDto sDto = new StaffDto();
				sDto.setId(s.getId());
				sDto.setJobNumber(s.getJobNumber());
				sDto.setName(s.getName());
				sDto.setGender(s.getGender());
				sDto.setDepartment(s.getSubDepartment().getDepartment().getDepartmentName());
				sDto.setJobTitle(s.getJobTitle());
				
				tempResults.add(sDto);
			}
			
			resultMap.put("results", tempResults);
		}
		
		return resultMap;
	}
	
	/**
	 * 删除职员信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/remove", method = {RequestMethod.POST})
	public CommonResult deleteStaff(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam int id) {
		
		Staff staff = staffService.get(Staff.class, id);
		
		CommonResult result = null;
		
		if(staff == null) { // 不存在该职员
			result = new CommonResult(1, "error", "不存在该职员");
			return result;
		}
		
		// 查找该员工是否有负责的项目
		List<Project> ownerProjects = projectService.getOwnerProjectByStaffId(id);
		if(ownerProjects != null && ownerProjects.size() > 0) { // 存在负责的项目
			result = new CommonResult(1, "error", "该职员为项目负责人");
			return result;
		}
		
//		List<Project> memberInProject = staff.getProjects();
//		if(memberInProject != null && memberInProject.size() > 0) { // 存在该职员参与的项目
//			for(Project p : memberInProject) {
//				if(p.getProjectOwner().getId() == id) { // 该职员为某项目负责人
//					result = new CommonResult(1, "error", "该职员为项目负责人");
//					return result;
//				}
//			}
//		}
		
		staffService.removeStaff(staff); // 删除
		result = new CommonResult(0, "success", "成功");
		
		return result;
	}
	
	/**
	 * 保存职员信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public CommonResult save(HttpServletRequest request,
			HttpServletResponse response,
			Staff staff) {
		
		CommonResult result = null;
		
		List<Staff> staffList = staffService.findStaffByJobNum(staff.getJobNumber());
		if(staffList != null && staffList.size() > 0) { // 存在重复的职工号
			result = new CommonResult(1, "error", "存在重复的职工号");
			return result;
		}

		staffList = staffService.findStaffByLoginName(staff.getLoginName());
		if(staffList != null && staffList.size() > 0) { // 存在重复登录名
			result = new CommonResult(1, "error", "存在重复登录名");
			return result;
		}
		
		staff.setPassword(MD5Util.MD5Encode(staff.getPassword())); // 保存为密文
		staff.setAddTime(new Date());
		staffService.save(staff);
		result = new CommonResult(0, "success", "添加成功");
		
		return result;
	}
	
	/**
	 * 更新职员信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = {RequestMethod.POST})
	public CommonResult updateStaff(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam int id,
			@RequestParam String jobNumber,
			@RequestParam String name,
			@RequestParam String gender,
			@RequestParam int subDepartment,
			@RequestParam String jobTitle) {
		
		CommonResult result = null;
		
		Staff staff = staffService.get(Staff.class, id);
		if(staff == null) { // 不存在该职员
			result = new CommonResult(1, "error", "不存在该职员");
			return result;
		}
		
		List<Staff> staffList = staffService.findStaffByJobNum(jobNumber);
		if(staffList != null && staffList.size() > 0) {
			Staff sf = staffList.get(0);
			if(sf.getId().intValue() != staff.getId().intValue())  { // 存在重复的职工号
				result = new CommonResult(1, "error", "存在重复的职工号");
				return result;
			}
		}
		
		SubDepartment subDepa = new SubDepartment();
		subDepa.setId(subDepartment);
		
		staff.setUpdateTime(new Date()); // 更新时间
		staff.setJobNumber(jobNumber);
		staff.setName(name);
		staff.setGender(gender);
		staff.setSubDepartment(subDepa);
		staff.setJobTitle(jobTitle);
		staffService.update(staff);
		
		result = new CommonResult(0, "success", "更新成功");
		
		return result;
	}
	
}
