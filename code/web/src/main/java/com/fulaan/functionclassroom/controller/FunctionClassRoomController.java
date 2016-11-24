 package com.fulaan.functionclassroom.controller;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fulaan.functionclassroom.ClassRoomDTO;
import com.pojo.user.UserDetailInfoDTO;
import org.apache.commons.lang.StringUtils;
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
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.functionclassroom.service.FunctionClassRoomService;
import com.fulaan.user.service.UserService;
import com.pojo.funcitonclassroom.ClassRoomAppointmentEntry;
import com.pojo.funcitonclassroom.ClassRoomEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.utils.RespObj;

/**
 * 功能教室
 * @author huanxiaolei@ycode.cn
 * @2015年12月21日
 */
@Controller
@RequestMapping("/functionclassroom")
public class FunctionClassRoomController extends BaseController{
	@Autowired
	private FunctionClassRoomService functionClassRoomService;
	@Autowired
	private UserService userService;
	@Autowired
	private ExamResultService examResultService;
	@Autowired
	private DepartmentService departmentService;

	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private SimpleDateFormat sdfSearch = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 录入页面的跳转
	 * @author chengong@ycode.cn
	 * @throws PermissionUnallowedException  
	 */
	@RequestMapping("/classList")
	@ResponseBody
	public ModelAndView classroom() throws PermissionUnallowedException {
		ModelAndView mv = new ModelAndView();
		int role=getSessionValue().getUserRole();
		String uid = getSessionValue().getId();
		boolean isAdministrator = UserRole.isManager(role) || UserRole.isTeacher(role) || UserRole.isHeadmaster(role) || UserRole.isLeaderClass(role) || UserRole.isLeaderOfGrade(role) || UserRole.isLeaderOfSubject(role) || UserRole.isK6ktHelper(role) || UserRole.isFunctionRoomManager(role)||functionClassRoomService.isDeAdmin(new ObjectId(uid), Constant.DEPARTMENT_FUNCTION_CLASS);
		if(isAdministrator){
			int count = functionClassRoomService.findMyManageRoom(getUserId());
			mv.addObject("count",count);
			mv.setViewName("funclassroom/classrooms");
		}else{
			throw new PermissionUnallowedException("您没有权限查看相关网页信息！"); 
		}
		return mv;
	}
			
	
	/** 
	* 功能教室页面
	 * @author huanxiaolei@ycode.cn
	 * @return
	 */
	@RequestMapping("/funClassRoomPage")
	@ResponseBody
	public ModelAndView showList() throws PermissionUnallowedException {
		ModelAndView mv = new ModelAndView();
		int role=getSessionValue().getUserRole();
		boolean isAdmin = UserRole.isManager(role);
		if(isAdmin){
			mv.setViewName("funclassroom/classManage");
		}else{
			throw new PermissionUnallowedException("您没有权限查看相关网页信息！");
		}
//		mv.setViewName("funclassroom/classManage");
		return mv;
	}
	
	/**
	 * 添加功能教室
	 * @author huanxiaolei@ycode.cn
	 * @return
	 */
	@RequestMapping("/addFunctionClassRoom")
	@ResponseBody
	public RespObj addFunctionClassRoom(ClassRoomDTO classRoomDTO){
		RespObj respObj = new RespObj(Constant.FAILD_CODE);
		String schoolId = getSessionValue().getSchoolId();
		int role=getSessionValue().getUserRole();
		boolean isAdmin = UserRole.isManager(role);
		if(StringUtils.isBlank(schoolId)){
			respObj.setMessage("您没有所属学校！");
			return respObj;
		}
		List<ClassRoomEntry> numberList = functionClassRoomService.getNum(new ObjectId(schoolId));
		for(ClassRoomEntry classRoomEntry:numberList){
			if(classRoomEntry.getNumber()==classRoomDTO.getNumber()){
				respObj.setMessage("序号重复，请重新填写！");
				return respObj;
			}
			if(classRoomDTO.getClassRoomName().equals(classRoomEntry.getClassRoomName())){
				respObj.setMessage("此功能教室已存在 ，请重新填写！");
				return respObj;
			}
		}
		if(isAdmin){
			List<ObjectId> userIds = new ArrayList<ObjectId>();
			if (classRoomDTO.getUsers()!=null && classRoomDTO.getUsers().length!=0) {
				for (String ui : classRoomDTO.getUsers()) {
					if (!StringUtils.isEmpty(ui)) {
						userIds.add(new ObjectId(ui));
					}
				}
			}
		    functionClassRoomService.addFunctionClassRoomEntry(new ClassRoomEntry(new ObjectId(schoolId),
					classRoomDTO.getNumber(),classRoomDTO.getClassRoomName(),userIds));
		    return RespObj.SUCCESS;
		}
		return respObj;
	}
	
	/**
	 * 根据学校id查询所有该学校的功能教室
	 * @return
	 * @author chengwei@ycode.cn
	 */
	@RequestMapping("/classroomList")
	@ResponseBody
	public Map<String, Object> queryClassRooms(@RequestParam(value="skip", defaultValue="0")int skip,
												@RequestParam(value="limit", defaultValue="10")int limit){
		String schId = getSessionValue().getSchoolId();
		// 根据条件获取功能教室结果集
		List<ClassRoomEntry> roomList = functionClassRoomService.queryRoomsBySchId(new ObjectId(schId), skip, limit);
		//格式化数据
		List<Map<String, Object>> resultList = formatRoomList(roomList);
		//查询学校功能教室总数
		int roomTotal = functionClassRoomService.countRooms(new ObjectId(schId));
		//组装数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", resultList);
		resultMap.put("total", roomTotal);
		return resultMap;
	}
	
	/**
	 * 根据教室id查本学期的预约记录
	 * @return
	 * @author chengwei@ycode.cn
	 */
	@RequestMapping("/appointmentList")
	@ResponseBody
	public Map<String, Object> queryAppointments(@RequestParam(value="cid", defaultValue="")String classId,
												 @RequestParam(value="skip", defaultValue="0")int skip,
												 @RequestParam(value="limit", defaultValue="10")int limit){
		String trem=examResultService.getTerm(System.currentTimeMillis());
		// 根据条件获取功能教室结果集
		List<ClassRoomAppointmentEntry> appointList = functionClassRoomService.queryApptByClsId(new ObjectId(classId), trem,skip, limit);
		//格式化数据
		List<Map<String, Object>> formatList = formatAppointList(appointList);
		//查询学校功能教室总数
		int appointTotal = functionClassRoomService.countAppointments(new ObjectId(classId), trem);
		//组装数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("datas", formatList);
		resultMap.put("total", appointTotal);
		return resultMap;
	}
	
	/**
	 * 添加一条预约记录
	 * @param schoolId
	 * @param classRoomId
	 * @param userId
	 * @param classroomName
	 * @param startTime
	 * @param endTime
	 * @param user
	 * @param reasons
	 * @return
	 * @author chengwei@ycode.cn
	 */
	@RequestMapping("/saveAppoint")
	@ResponseBody
	public Map<String, Object> saveAppointment(@RequestParam(value="sid", defaultValue="")String schoolId,
									@RequestParam(value="cid", defaultValue="")String classRoomId,
									@RequestParam(value="uid", defaultValue="")String userId,
									@RequestParam(value="className", defaultValue="")String classroomName,
									@RequestParam(value="startTime", defaultValue="")String startTime,
									@RequestParam(value="endTime", defaultValue="")String endTime,
									@RequestParam(value="userName", defaultValue="")String user,
									@RequestParam(value="reasons", defaultValue="")String reasons){
		ClassRoomAppointmentEntry e = new ClassRoomAppointmentEntry();
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(startTime, pos);
		String tire= examResultService.getTerm(strtodate.getTime());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Long startTm = sdf.parse(startTime).getTime();
			Long endTm = sdf.parse(endTime).getTime();
			e.setSchoolId(new ObjectId(schoolId));
			e.setClassRoomId(new ObjectId(classRoomId));
			e.setUserId(new ObjectId(userId));
			e.setClassRoomName(classroomName);
			e.setStartTime(startTm); 
			e.setEndTime(endTm);
			e.setUser(user);
			e.setReasons(reasons);
			e.setIsRemove(Constant.ZERO);
			e.setTerm(tire);
		} catch (ParseException e1) {
			resultMap.put("flag", 0); //添加失败
			return resultMap;
		}
		List<ClassRoomAppointmentEntry> list = functionClassRoomService.saveAppointment(e);
		if(list==null){
			resultMap.put("flag", 1); //添加成功
			return resultMap;
		}else{
			//格式化数据
			List<Map<String, Object>> formatList = formatAppointList(list);
			//组装数据
			resultMap.put("datas", formatList);
			resultMap.put("flag", 2); //有重复数据
			return resultMap;
		}
	}
	
	/**
	 * 删除一条预约记录
	 * @param id
	 * @author chengwei@ycode.cn
	 */
	@RequestMapping("/deleteAppoint")
	@ResponseBody
	public Map deleteAppointment(@RequestParam(value="id", defaultValue="")String id){
		Map map = new HashMap();
		try {
			functionClassRoomService.deleteAppointment(new ObjectId(id));
			map.put("flag",true);
		} catch (Exception e) {
			map.put("flag",false);
		}
		return map;
	}
	
	/**
	 * 根据id更新一条预约记录
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param reasons
	 * @author caotiecheng@ycode.cn
	 */
	/*@RequestMapping("/updateAppoint")
	@ResponseBody
	public Map<String, Object> updateAppintment(@RequestParam(value="id", defaultValue="")String id, 
								 @RequestParam(value="startTime", defaultValue="")String startTime, 
								 @RequestParam(value="endTime", defaultValue="")String endTime, 
								 @RequestParam(value="reasons", defaultValue="")String reasons){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Long startTm = sdf.parse(startTime).getTime();
			Long endTm = sdf.parse(endTime).getTime();
			functionClassRoomService.updateAppint(new ObjectId(id), startTm, endTm, reasons);
			resultMap.put("flag", 1); //添加成功
		} catch (ParseException e) {
			resultMap.put("flag", 0); //添加失敗
		}
		return resultMap;
	}*/
	@RequestMapping("/updateAppoint")
	@ResponseBody
	public Map<String, Object> updateAppintment(@RequestParam(value="id", defaultValue="")String id, 
								 @RequestParam(value="startTime", defaultValue="")String startTime, 
								 @RequestParam(value="endTime", defaultValue="")String endTime, 
								 @RequestParam(value="reasons", defaultValue="")String reasons){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ClassRoomAppointmentEntry> list;
		try {
			Long startTm = sdf.parse(startTime).getTime();
			Long endTm = sdf.parse(endTime).getTime();
			list = functionClassRoomService.updateAppintment(new ObjectId(id), startTm, endTm, reasons);
		} catch (ParseException e) {
			resultMap.put("flag", 0); //添加失敗
			return resultMap;
		}
		if(list==null){
			resultMap.put("flag", 1); //添加成功
			return resultMap;
		}else{
			//格式化数据
			List<Map<String, Object>> formatList = formatAppointList(list);
			//组装数据
			resultMap.put("datas", formatList);
			resultMap.put("flag", 2);
			return resultMap;
		}
	}
	/**
	 * 根据id更新一条预约记录
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param reasons
	 * @author chengwei@ycode.cn
	 */
	@RequestMapping("/updateAppoint2")
	@ResponseBody
	public Map<String, Object> updateAppintment2(@RequestParam(value="id", defaultValue="")String id, 
								 @RequestParam(value="startTime", defaultValue="")String startTime, 
								 @RequestParam(value="endTime", defaultValue="")String endTime, 
								 @RequestParam(value="reasons", defaultValue="")String reasons){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ClassRoomAppointmentEntry> list;
		try {
			Long startTm = sdf.parse(startTime).getTime();
			Long endTm = sdf.parse(endTime).getTime();
			list = functionClassRoomService.updateAppintment(new ObjectId(id), startTm, endTm, reasons);
		} catch (ParseException e) {
			resultMap.put("flag", 0); //添加失敗
			return resultMap;
		}
		if(list==null){
			resultMap.put("flag", 1); //添加成功
			return resultMap;
		}else{
			//格式化数据
			List<Map<String, Object>> formatList = formatAppointList(list);
			//组装数据
			resultMap.put("datas", formatList);
			resultMap.put("flag", 2);
			return resultMap;
		}
	}
	
	
	/**
	 * 格式化查询到功能教室结果集，供前台使用
	 * @param srcList
	 * @return
	 * @author chengwei@ycode.cn
	 */
	private List<Map<String, Object>> formatRoomList(List<ClassRoomEntry> srcList) {
		List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
		for (ClassRoomEntry e : srcList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", e.getID().toString());
			m.put("schoolId", e.getSchoolId().toString());
			m.put("number", e.getNumber());
			m.put("className", e.getClassRoomName());
			List<UserDetailInfoDTO> userList = userService.findUserInfoByIds(e.getUserIds());
			m.put("userList", userList);
			formatList.add(m);
		}
		return formatList;
	}
	
	/**
	 * 格式化查询到预约结果集，供前台使用
	 * @param srcList
	 * @return
	 * @author chengwei@ycode.cn
	 */
	private List<Map<String, Object>> formatAppointList(List<ClassRoomAppointmentEntry> srcList) {
		List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
		for (ClassRoomAppointmentEntry e : srcList) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", e.getID().toString());
			m.put("schoolId", e.getSchoolId().toString());
			m.put("classrroomId", e.getClassRoomId());
			m.put("className", e.getClassRoomName());
			m.put("startTime", sdfOut.format(e.getStartTime()));
			m.put("endTime", sdfOut.format(e.getEndTime()));
			m.put("user", e.getUser());
			m.put("reasons", e.getReasons());
			formatList.add(m);
		}
		return formatList;
	}
	
	
	/**
	 * 查看管理者所管理的教室
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	@RequestMapping("/mymanagerooms")  
	@ResponseBody
	public  String findRoomByManageId(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
		String schoolId=getSessionValue().getSchoolId();
		String administratorId=getSessionValue().getId();
		int total=0;
		int startIndex = (pageNo-1)*pageSize;
		List<ClassRoomEntry> classRoomList=functionClassRoomService.findManageRoom(new ObjectId(schoolId), new ObjectId(administratorId),startIndex,pageSize);
		total=functionClassRoomService.countManageRooms(new ObjectId(schoolId), new ObjectId(administratorId));
		List<Map<String,Object>> roomList =new ArrayList<Map<String, Object>>();
		Map<String,Object> result=new HashMap<String,Object>();
		for(ClassRoomEntry cl:classRoomList){
			Map room = new HashMap();
//			room.put("userId", cl.getAdministratorId().toString());
			room.put("roomId", cl.getID().toString());
			room.put("num", cl.getNumber());
			room.put("roomName",cl.getClassRoomName());
			List<UserDetailInfoDTO> userList = userService.findUserInfoByIds(cl.getUserIds());
			room.put("userList", userList);
//			room.put("administratorName", cl.getAdministratorName());
			roomList.add(room);
		}
		result.put("classRoom", roomList);
		result.put("pageNo", pageNo);
		result.put("total", total);
		return JSON.toJSONString(result);
	}
	/**
	 * 查询功能教室管理员
	 * @return
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/loadmanages")  
	@ResponseBody
	public List<Map<String,String>> funClassRoomManage(){
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		String schoolId = getSessionValue().getSchoolId();
		if(StringUtils.isBlank(schoolId)){
			return null;
		}
//		List<UserEntry> userEntryList = functionClassRoomService.findAllUsersBySchoolId(new ObjectId(schoolId));
//		List<UserEntry> userEntryList = functionClassRoomService.findFunClassRoomManages(new ObjectId(schoolId));
//		List<UserEntry> managerList = functionClassRoomService.findManagers(new ObjectId(schoolId));
//		for(UserEntry user : managerList){
//			userEntryList.add(user);
//		}
		List<UserDetailInfoDTO> userEntryList = userService.findTeacherInfoBySchoolId(schoolId);
	    for(UserDetailInfoDTO userEntry:userEntryList){
//	    	int r = userEntry.getRole();
//	    	if(!(UserRole.isManager(r) || UserRole.isFunctionRoomManager(r))){
//	    		continue;
//	    	}
	    	Map<String,String> map = new HashMap<String,String>();
	    	map.put("id", userEntry.getId());
	    	map.put("userName", userEntry.getUserName());
	    	resultList.add(map);
	    }
	    return resultList;
	}
	/**
	 * 查询功能教室列表
	 * @return
	 * @author huanxiaolei@ycode.cn
	 * @return
	 */
	@RequestMapping("/funClassRoomList")  
	@ResponseBody
	public Map<String, Object> funClassRoomList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
		int startIndex = (pageNo-1) * pageSize;
		List<Map> resultList = new ArrayList<Map>();
		String schoolId = getSessionValue().getSchoolId();
		List<ClassRoomEntry> classRoomEntryList = functionClassRoomService.queryRoomsBySchId(new ObjectId(schoolId),startIndex,pageSize);
	    int total = functionClassRoomService.countRooms(new ObjectId(schoolId));  
		for(ClassRoomEntry classRoomEntry:classRoomEntryList){
	    	Map resultMap = formart(classRoomEntry);
	    	resultList.add(resultMap);
	    }
	    Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cur", pageNo);
		resultMap.put("datas", resultList);
		resultMap.put("total", total);
	    return resultMap;
	}
	
	
	
	/**
	 * 查询我的预约记录
	 * @param skip
	 * @param size
	 * @param searchTime
	 * @return
	 * @author caotiecheng@ycode.cn
	 */
	
	@RequestMapping("/queryMyAppointments")  
	@ResponseBody
	public Map<String, Object> queryMyAppointBySearchTime(@RequestParam(value = "skip", defaultValue = "1") int skip,
							   							   @RequestParam(value = "size", defaultValue = "10") int size,
							   							@RequestParam(value = "searchTime", defaultValue = "") String searchTime){
		String userId = getSessionValue().getId();
		List<ClassRoomAppointmentEntry> myAppointmentList = new ArrayList<ClassRoomAppointmentEntry>();
		int total = 0;
			if(StringUtils.isBlank(searchTime)){
				myAppointmentList = functionClassRoomService.myClassRoomAppointment(new ObjectId(userId),skip,size);
				total = functionClassRoomService.countSum(new ObjectId(userId));
			}else{
				Date d3;
				try {
					d3 = sdfSearch.parse(searchTime);
					Long searchTmStart = d3.getTime();
					Long searchTmEnd = d3.getTime()+86400000;
					myAppointmentList = functionClassRoomService.myAppointBySearchTime(new ObjectId(userId),searchTmStart,searchTmEnd,skip,size);
					total = functionClassRoomService.countSum1(new ObjectId(userId), searchTmStart, searchTmEnd);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		
		List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
		if(myAppointmentList==null || myAppointmentList.size()==0){
			}else{
				for (int i = 0; i < myAppointmentList.size(); i++) {
					ClassRoomAppointmentEntry cl = myAppointmentList.get(i);
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("id", cl.getID().toString());
					m.put("classRoomName", cl.getClassRoomName());
					m.put("startTime", sdfOut.format(cl.getStartTime()));
					m.put("endTime", sdfOut.format(cl.getEndTime()));
					m.put("reasons", cl.getReasons());
					m.put("user", cl.getUser());
					myList.add(m);
			}
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("myList", myList);
		data.put("total", total);
		return data;
	}
	
	
	/**
	 * 删除一条功能教室
	 * @param classRoomId
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/delFunClassRoom")
	@ResponseBody
	public RespObj deleteFunClassRoom(@RequestParam(value="classRoomId", defaultValue="")String classRoomId){
		if(StringUtils.isBlank(classRoomId)){
			return RespObj.FAILD;
		}
		functionClassRoomService.delClassRoomEntry(new ObjectId(classRoomId));
		return RespObj.SUCCESS;
	}
	/**
	 * 功能教室详情
	 * @param classRoomId
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/detialFunClassRoom")
	@ResponseBody
	public String updateFunClassRoom(@RequestParam(value="classRoomId", defaultValue="")String classRoomId){
		if(StringUtils.isBlank(classRoomId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ClassRoomEntry classRoomEntry = functionClassRoomService.findClassRoom(new ObjectId(classRoomId));
		Map resultMap = formart(classRoomEntry);
		return JSON.toJSONString(resultMap);
	}
	//功能教室前台数据组装公共部分
	public Map formart(ClassRoomEntry classRoomEntry){
		Map resultMap = new HashMap();
		resultMap.put("id", classRoomEntry.getID().toString());
		resultMap.put("number", String.valueOf(classRoomEntry.getNumber()));
		resultMap.put("classRoomName", classRoomEntry.getClassRoomName());
		List<UserDetailInfoDTO> userList = userService.findUserInfoByIds(classRoomEntry.getUserIds());
		resultMap.put("userList", userList);
		return resultMap;
	}
	/**
	 * 编辑功能教室
	 * @author huanxiaolei@ycode.cn
	 * @return
	 */
	@RequestMapping("/updateFunctionClassRoom")
	@ResponseBody
	public RespObj updateFunctionClassRoom(ClassRoomDTO classRoomDTO){
		RespObj respObj = new RespObj(Constant.FAILD_CODE);
		String schoolId = getSessionValue().getSchoolId();
		if(StringUtils.isBlank(classRoomDTO.getClassRoomId())){
			respObj.setMessage("编辑信息有误！");
			return respObj;
		}
		ClassRoomEntry classRoomEntry = functionClassRoomService.findClassRoom(new ObjectId(classRoomDTO.getClassRoomId()));
		List<ClassRoomEntry> numberList = functionClassRoomService.getNum(new ObjectId(schoolId));
		if(classRoomEntry.getNumber()!=classRoomDTO.getNumber()){
			for(ClassRoomEntry classRoom:numberList){
				if(classRoom.getNumber()==classRoomDTO.getNumber()){
					respObj.setMessage("序号重复，请重新填写！");
					return respObj;
				}
			}
		}
		if(!classRoomDTO.getClassRoomName().equals(classRoomEntry.getClassRoomName())){
			for(ClassRoomEntry classRoom:numberList){
				if(classRoomDTO.getClassRoomName().equals(classRoom.getClassRoomName())){
					respObj.setMessage("功能教室名重复，请重新编辑！");
					return respObj;
				}
			}
		}
		List<ObjectId> userIds = new ArrayList<ObjectId>();
		if (classRoomDTO.getUsers()!=null && classRoomDTO.getUsers().length!=0) {
			for (String ui : classRoomDTO.getUsers()) {
				if (!StringUtils.isEmpty(ui)) {
					userIds.add(new ObjectId(ui));
				}
			}
		}
		functionClassRoomService.updateClassRoom(new ObjectId(classRoomDTO.getClassRoomId()), classRoomDTO.getNumber(), classRoomDTO.getClassRoomName(),userIds);
		return RespObj.SUCCESS;
	}	
	/**
	 * 查看一个教室ID预约详情
	 * @param roomId
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	@RequestMapping("/roomdetils")  
	@ResponseBody
	public String roomDetils(@RequestParam(value="roomId", defaultValue = "")String roomId,
			@RequestParam(value = "pageNo", defaultValue = "1")int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
		String trem=examResultService.getTerm(new Date().getTime());
		if("".equals(roomId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		int total=0;
		int startIndex = (pageNo-1)*pageSize;
		List<ClassRoomAppointmentEntry> rmDts = functionClassRoomService.findRoomDetil(new ObjectId(roomId),trem,startIndex,pageSize);
		total=functionClassRoomService.countAppointments(new ObjectId(roomId), trem);
		List<Map<String,Object>> reList =new ArrayList<Map<String, Object>>();
		Map<String,Object> result=new HashMap<String,Object>();
		for(ClassRoomAppointmentEntry cr: rmDts){
			Map<String,Object> d=new HashMap<String,Object>();
			d.put("id", cr.getID().toString());
			d.put("startTime", sdfOut.format(cr.getStartTime()));
			d.put("endTime", sdfOut.format(cr.getEndTime()));
			d.put("user", cr.getUser());
			d.put("resons", cr.getReasons());
			reList.add(d);
		}
		result.put("totals", total);
		result.put("pageNo", pageNo);
		result.put("roomDetils", reList);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 添加一条预约信息
	 * @param classRoomId
	 * @param startTime
	 * @param endTime
	 * @param reasons
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	@RequestMapping("/addrev")  
	@ResponseBody
	public String addReservation(@RequestParam(value="classRoomId", defaultValue = "")String classRoomId,
			@RequestParam(value="startTime", defaultValue = "")String startTime,
			@RequestParam(value="endTime", defaultValue = "")String endTime,
			@RequestParam(value="reasons", defaultValue = "")String reasons){
		String schoolId=getSessionValue().getSchoolId();
		String trem=examResultService.getTerm(new Date().getTime());
		String userId=getSessionValue().getId();
		String userName=getSessionValue().getUserName();
		String classRoomName=functionClassRoomService.findClassRoom(new ObjectId(classRoomId)).getClassRoomName();
		ClassRoomAppointmentEntry cra = new ClassRoomAppointmentEntry();
		cra.setSchoolId(new ObjectId(schoolId));
		cra.setClassRoomId(new ObjectId(classRoomId));
		cra.setUserId(new ObjectId(userId));
		cra.setUser(userName);
		cra.setClassRoomId(new ObjectId(classRoomId));
		cra.setClassRoomName(classRoomName);
		cra.setTerm(trem);
		try {
			cra.setStartTime(sdf.parse(startTime).getTime());
			cra.setEndTime(sdf.parse(endTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cra.setReasons(reasons);
		cra.setIsRemove(Constant.ZERO);
		ObjectId resultId=functionClassRoomService.addaReservation(cra);
		if(resultId == null){
			return JSON.toJSONString(RespObj.FAILD);
		}
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 跟新一条预约信息
	 * @param startTime
	 * @param endTime
	 * @param reason
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	@RequestMapping("/updaterev")  
	@ResponseBody
	public String updateReservation(@RequestParam(value="detilId", defaultValue = "")String detilId,
			@RequestParam(value="startTime", defaultValue = "")String startTime,
			@RequestParam(value="endTime", defaultValue = "")String endTime,
			@RequestParam(value="reason", defaultValue = "")String reason){
		if("".equals(detilId) || "".equals(startTime) || "".equals(endTime) || "".equals(reason)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		Long st = null;
		Long et = null;
		try {
			st = sdfOut.parse(startTime).getTime();
		    et = sdfOut.parse(endTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		functionClassRoomService.updateReservation(new ObjectId(detilId), st, et, reason);
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 根据ID删除一条预约记录
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	@RequestMapping("/deleterev")  
	@ResponseBody
	public String deleteReservation(@RequestParam(value="rsid", defaultValue = "")String rsid){
		if("".equals(rsid)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		functionClassRoomService.deleteAReservation(new ObjectId(rsid));
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 删除重复预约
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	@RequestMapping("/deleteDoublerev")  
	@ResponseBody
	public String deleteDoubleReservation(@RequestParam(value="str", defaultValue = "")String str){
		String [] arr= str.split(",");
		for(int i=0;i<arr.length;i++){
			functionClassRoomService.deleteAReservation(new ObjectId(arr[i]));
		}
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	//获取向预约列表添加的必要信息
	@RequestMapping("/putAppInfo")
	@ResponseBody
	public Map<String, Object> putAppointInfo(){
		String sid = getSessionValue().getSchoolId();
		String id = getSessionValue().getId();
		UserEntry userEntry = userService.searchUserId(new ObjectId(id));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("sid", sid);
		resultMap.put("id", id);
		resultMap.put("userName", userEntry.getUserName());
		return resultMap;
	}
	
	/**
	 * 获得学期
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/getSemester")  
	@ResponseBody
	public List<String> getSemester(@RequestParam(value="classRoomId", defaultValue="")String classRoomId){
		List<String> termList = functionClassRoomService.getTerms(new ObjectId(classRoomId));
		String trem = examResultService.getTerm(System.currentTimeMillis());
		if(!termList.contains(trem)){
			termList.add(trem);
		}
		Collections.sort(termList);
		Collections.reverse(termList);
		return termList;
	}
	/**
	 * 功能教室统计（查看功能教室预约详情）
	 * @return
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/queryappointmentList")
	@ResponseBody
	public Map<String, Object> countAppointments(@RequestParam(value="term", defaultValue="")String term,
			                                     @RequestParam(value="classRoomId", defaultValue="")String classRoomId,
												 @RequestParam(value="pageNo", defaultValue="1")int pageNo,
												 @RequestParam(value="pageSize", defaultValue="5")int pageSize){
		int startIndex = (pageNo-1) * pageSize;
		List<ClassRoomAppointmentEntry> appointList = functionClassRoomService.queryApptByClsId(new ObjectId(classRoomId),term,startIndex, pageSize);
		List<Map<String, Object>> formatList = formatAppointList(appointList);
		int total = functionClassRoomService.countAppointments(new ObjectId(classRoomId),term);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cur", pageNo);
		resultMap.put("datas", formatList);
		resultMap.put("total", total);
		return resultMap;
	}
	
    /**
	 * 根据id查询一条预约记录
	 * @param id
	 * @return
	 * @author chengwei@ycode.cn
	 */
	@RequestMapping("/getAppoint")
	@ResponseBody
	public Map<String, Object> getAppointment(@RequestParam(value="id", defaultValue="")String id){
		ClassRoomAppointmentEntry e = functionClassRoomService.getAppointment(new ObjectId(id));
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("userName", e.getUser());
		resultMap.put("startTime", sdf.format(new Date(e.getStartTime())));
		resultMap.put("endTime", sdf.format(new Date(e.getEndTime())));
		resultMap.put("reasons", e.getReasons());
		return resultMap;
	}
	
	/**
	 * 根据教室ID,使用时间,查询有冲突的预约
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	@RequestMapping("/finddoubledetils")
	@ResponseBody
	public String getSameTimeRoomDetils(@RequestParam(value="classRoomId", defaultValue = "")String classRoomId,
			@RequestParam(value="startTime", defaultValue = "")String startTime,
			@RequestParam(value="endTime", defaultValue = "")String endTime){
		
		Long st=null;
		Long et = null;
		try {
			st = sdf.parse(startTime).getTime();
			et=sdf.parse(endTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<ClassRoomAppointmentEntry> rmDtils = functionClassRoomService.findReservationByTime(new ObjectId(classRoomId),st ,et);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		 if(rmDtils==null){
			 resultMap .put("flag", 0);
			 return  JSON.toJSONString(resultMap);
		 }
		 List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		 resultMap.put("numberOfPerson", rmDtils.size());
		for(ClassRoomAppointmentEntry cr:rmDtils){
			Map<String,Object> detils = new HashMap<String,Object>();
			detils.put("reciver", cr.getUserId().toString());
			detils.put("dId", cr.getID().toString());
			detils.put("startTime", sdfOut.format(cr.getStartTime()));
			detils.put("endTime", sdfOut.format(cr.getEndTime()));
			detils.put("user", cr.getUser());
			detils.put("reason", cr.getReasons());
			resultList.add(detils);
		}
		resultMap.put("flag", 1);
		resultMap.put("roomDtls", resultList);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 功能教室详情
	 * @param classRoomId
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/findClassRoomName")
	@ResponseBody
	public String findClassRoom(@RequestParam(value="classRoomId", defaultValue="")String classRoomId){
		if(StringUtils.isBlank(classRoomId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ClassRoomEntry classRoomEntry = functionClassRoomService.findClassRoom(new ObjectId(classRoomId));
		
		return JSON.toJSONString(classRoomEntry.getClassRoomName());
	}
}
