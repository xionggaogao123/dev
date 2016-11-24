package com.fulaan.studentattendance.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.studentattendance.service.StudentAttendanceService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.ExportUtil;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.studentattendance.StudentAttendanceDTO;
import com.pojo.studentattendance.StudentAttendanceEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

/**
 * 高新一中学生考勤
 * @author xusy 2016-11-09
 */
@Controller
@RequestMapping(value = "/stuAttendance")
public class StudentAttendanceController extends BaseController{
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private StudentAttendanceService attendanceService;
	
	private Comparator<ObjectId> comparator = new Comparator<ObjectId>() {
		@Override
		public int compare(ObjectId arg0, ObjectId arg1) {
			return arg0.compareTo(arg1);
		}
	};
	
	@RequestMapping("/index")
	public String index() {
		return "studentattendance/studentAttendance";
	}
	
	/**
	 * 获取班主任所在班级
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/masterClazz")
	public RespObj getMasterClazzList() {
		
		RespObj result = new RespObj(Constant.SUCCESS_CODE);
		// 班主任id
		String masterId = getSessionValue().getId();
		List<ClassEntry> clazzList = classService.findClassByMasterId(new ObjectId(masterId));
		
		List<ClassInfoDTO> clazzDtoList = new ArrayList<ClassInfoDTO>();
		for(ClassEntry entry : clazzList) {
			ClassInfoDTO dto = new ClassInfoDTO(entry);
			dto.setClassType(0);
			clazzDtoList.add(dto);
		}
		result.setMessage(clazzDtoList);
		
		return result;
	}
	
	/**
	 * 导出考勤信息
	 * @param gradeId
	 * @param clazzId
	 * @param sDate
	 * @param eDate
	 * @param type
	 * @param stuName
	 * @param exportType
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "/export")
	public void exportAttendInfo(
			@RequestParam(required = false) String gradeId,
			@RequestParam(required = false) String clazzId,
			@RequestParam(required = false) String sDate,
			@RequestParam(required = false) String eDate,
			@RequestParam int type,
			@RequestParam(required = false) String stuName,
			@RequestParam int exportType,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		List<StudentAttendanceDTO> attendanceDtoList = new ArrayList<StudentAttendanceDTO>();
		if(exportType == 1) { // 校领导导出信息
			List<ObjectId> gradeIdList = new ArrayList<ObjectId>();
			List<ObjectId> clazzIdList = new ArrayList<ObjectId>();
			bulidObjectIdList(gradeId, clazzId, gradeIdList, clazzIdList);
			
			attendanceDtoList = 
					attendanceService.getStudentAttendanceInfo(gradeIdList, clazzIdList, sDate, eDate, stuName, type);
		} else if(exportType == 2) { // 班主任导出信息
			attendanceDtoList = 
					attendanceService.getStudentAttendanceInfo(clazzId, sDate, eDate, stuName, type);
		}
		
		response.setContentType("application/octet-stream;charset=UTF-8");
		response.addHeader("Pargam", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		ExportUtil util = new ExportUtil();
		attendanceService.exportAttendanceInfo(util, attendanceDtoList);
		response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
		util.getBook().write(response.getOutputStream());
	}
	
	/**
	 * 校领导查看学生考勤情况，只显示缺勤信息
	 * @param gradeId 年级id
	 * @param clazzId 班级id
	 * @param page 页码
	 * @param pageSize 每页条数
	 * @param sDate 开始日期
	 * @param eDate 结束日期
	 * @param type 考勤类型
	 * @param stuName 学生姓名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/showMasterAttendInfo")
	public RespObj listSchoolMasterAttendanceInfo(
			@RequestParam(required = false) String gradeId,
			@RequestParam(required = false) String clazzId,
			@RequestParam(defaultValue = "1")int page, 
			@RequestParam(defaultValue = "20")int pageSize,
			@RequestParam(required = false) String sDate,
			@RequestParam(required = false) String eDate,
			@RequestParam int type,
			@RequestParam(required = false) String stuName) {
		
		RespObj result= new RespObj(Constant.FAILD_CODE);
		
		List<ObjectId> gradeIdList = new ArrayList<ObjectId>();
		List<ObjectId> clazzIdList = new ArrayList<ObjectId>();
		List<StudentAttendanceDTO> attendanceInfoDtoList = new ArrayList<StudentAttendanceDTO>();
		
		bulidObjectIdList(gradeId, clazzId, gradeIdList, clazzIdList);
		
		try {
			attendanceInfoDtoList = 
					attendanceService.getStudentAttendanceInfo(gradeIdList, clazzIdList, sDate, eDate, stuName, type);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		
		List<StudentAttendanceDTO> resultDtoList = new ArrayList<StudentAttendanceDTO>();
		
		int listSize = attendanceInfoDtoList.size();
		int skip = page == 0 ? 0 : (page - 1) * pageSize;
		int lastIndex = skip + pageSize;
		if(skip > listSize) { // 越界
			return result;
		}
		
		lastIndex = lastIndex > listSize ? listSize : lastIndex; // 获取一条记录下标
		
		for(int i = skip; i < lastIndex; i++) {
			StudentAttendanceDTO dto = attendanceInfoDtoList.get(i);
			resultDtoList.add(dto);
		}
		
		int totalPage = (listSize + pageSize - 1) / pageSize;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("totalPage", totalPage);
		model.put("list", resultDtoList);
		
		result.setCode(Constant.SUCCESS_CODE);
		result.setMessage(model);
		
		return result;
	}
	
	private void bulidObjectIdList(String gradeId, String clazzId, 
			List<ObjectId> gradeIdList, List<ObjectId> clazzIdList) {
		if(StringUtils.isBlank(gradeId) 
				|| "-1".equals(gradeId)) { // 查询全部年级，即所有考勤信息
			List<GradeView> gradeViewList = schoolService.findGradeList(getSessionValue().getSchoolId());
			for(GradeView grade : gradeViewList) {
				String gId = grade.getId();
				gradeIdList.add(new ObjectId(gId));
			}
		} else if(StringUtils.isBlank(clazzId) 
				|| "-1".equals(clazzId)){
			gradeIdList.add(new ObjectId(gradeId));
			List<ClassInfoDTO> classDtoList = classService.findClassByGradeId(gradeId);
			for(ClassInfoDTO dto : classDtoList) {
				clazzIdList.add(new ObjectId(dto.getId()));
			}
		} else {
			gradeIdList.add(new ObjectId(gradeId));
			clazzIdList.add(new ObjectId(clazzId));
		}
	}
	
	/**
	 * 更新获取新建考勤记录
	 * @param attendanceId 考勤表记录id
	 * @param studentId 学生id
	 * @param clazzId 班级id
	 * @param status 状态
	 * @param remark 备注
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeAttendance", method = RequestMethod.POST)
	public RespObj changeAttendanceStatus(@RequestParam(required = false) String attendanceId,
			@RequestParam String studentId,
			@RequestParam String clazzId,
			@RequestParam String attendanceDate,
			@RequestParam int status,
			@RequestParam(required = false) String remark) {
		
		RespObj result = new RespObj(Constant.FAILD_CODE);
		
		ClassEntry clazzEntry = classService.getClassEntryById(new ObjectId(clazzId), Constant.FIELDS);
		ObjectId gradeId = clazzEntry.getGradeId();
		
		UserDetailInfoDTO userInfoDto = userService.getUserInfoById(studentId);
		String userName = userInfoDto.getUserName();
		
		StudentAttendanceEntry attendanceEntry = null;
		if(StringUtils.isBlank(attendanceId)) { // 新考勤记录
			attendanceEntry = new StudentAttendanceEntry();
			attendanceEntry.setGradeId(gradeId);
			attendanceEntry.setClazzId(new ObjectId(clazzId));
			attendanceEntry.setStudentId(new ObjectId(studentId));
			attendanceEntry.setStudentName(userName);
			attendanceEntry.setStuAttendanceStatus(status);
			attendanceEntry.setAttendanceDate(attendanceDate);
			attendanceEntry.setRemark(remark);
			attendanceService.saveOrUpdateAttendanceInfo(attendanceEntry);
		} else {
			attendanceEntry = attendanceService.getEntryById(new ObjectId(attendanceId));
			if(attendanceEntry == null) {
				return result;
			}
			
			
			if(StringUtils.isBlank(remark) && status == 0) { // 考勤正常并且无备注信息，删除记录
				attendanceService.removeById(new ObjectId(attendanceId));
				result.setCode(Constant.SUCCESS_CODE);
				return result;
			}
			
			attendanceEntry.setRemark(remark);
			attendanceEntry.setStuAttendanceStatus(status);
			attendanceEntry.setAttendanceUpdateTime(System.currentTimeMillis());
			attendanceService.saveOrUpdateAttendanceInfo(attendanceEntry);
			
		}
		
		result.setCode(Constant.SUCCESS_CODE);
		
		return result;
	}
	
	/**
	 * 获取班级学生考勤信息
	 * @param clazzId
	 * @param page 页数
	 * @param pageSize 每页条数
	 * @param sDate 开始日期
	 * @param eDate 结束日期
	 * @param type 状态 -1 全部状态
	 * @param stuName 学生姓名
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/masterQuer")
	public RespObj getStudentAttendanceWithMaster(String clazzId, 
			@RequestParam(defaultValue = "1")int page, 
			@RequestParam(defaultValue = "20")int pageSize,
			@RequestParam(required = false) String sDate,
			@RequestParam(required = false) String eDate,
			@RequestParam int type,
			@RequestParam(required = false) String stuName) {
		
		RespObj result = new RespObj(Constant.FAILD_CODE);
		
		List<StudentAttendanceDTO> stuAttendanceDtoList;
		try {
			stuAttendanceDtoList = 
					attendanceService.getStudentAttendanceInfo(clazzId, sDate, eDate, stuName, type);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		
		List<StudentAttendanceDTO> resultDtoList = new ArrayList<StudentAttendanceDTO>();
		
		int listSize = stuAttendanceDtoList.size();
		int skip = page == 0 ? 0 : (page - 1) * pageSize;
		int lastIndex = skip + pageSize;
		if(skip > listSize) { // 越界
			return result;
		}
		
		lastIndex = lastIndex > listSize ? listSize : lastIndex; // 获取一条记录下标
		
		for(int i = skip; i < lastIndex; i++) {
			StudentAttendanceDTO dto = stuAttendanceDtoList.get(i);
			resultDtoList.add(dto);
		}
		
		int totalPage = (listSize + pageSize - 1) / pageSize;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("totalPage", totalPage);
		model.put("list", resultDtoList);
		
		result.setCode(Constant.SUCCESS_CODE);
		result.setMessage(model);
		
		return result;
	}
	
	private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }
	
}
