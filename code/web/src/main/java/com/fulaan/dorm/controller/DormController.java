package com.fulaan.dorm.controller;

import java.util.ArrayList;
import java.util.Collection;
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
import com.fulaan.base.controller.BaseController;
import com.fulaan.dorm.service.DormService;
import com.fulaan.myclass.service.ClassService;
import com.pojo.dorm.DormAreaEntry;
import com.pojo.dorm.DormBuildingEntry;
import com.pojo.dorm.DormEntry;
import com.pojo.dorm.DormFloorEntry;
import com.pojo.dorm.DormStudentEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.Grade;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/dorm")
public class DormController extends BaseController{
	@Autowired
    private DormService dormService;
	@Autowired
	private ClassService classService;
	
	/**
	 * 宿舍页面跳转
	 * @throws PermissionUnallowedException 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView retrieveDormInfo() throws Exception{
		Map<String, Object> messageMap = new HashMap<String, Object>();
		String schoolId = getSessionValue().getSchoolId();
		String uid = getSessionValue().getId();
		int role=getSessionValue().getUserRole();
		boolean isAdministrator = UserRole.isDormManager(role) || UserRole.isK6ktHelper(role) || UserRole.isManager(role) || UserRole.isLeaderClass(role)||UserRole.isHeadmaster(role)||UserRole.isLeaderOfGrade(role)|| UserRole.isTeacher(role)||UserRole.isLeaderOfSubject(role)|| dormService.isDormAdmin(new ObjectId(uid), Constant.DEPARTMENT_GUARD);
		ModelAndView mv = new ModelAndView();
		List<Grade> gradeList =dormService.findGradeList(schoolId);
		List<Map<String, String>> gra = new ArrayList<Map<String, String>>();
		for(Grade grade : gradeList){
			Map<String, String> gradeMap = new HashMap<String,String>();
			gradeMap.put("name", grade.getName());
			gradeMap.put("gradeId", grade.getGradeId().toString());
			gra.add(gradeMap);
		}
		messageMap.put("grades", gra);
		if(isAdministrator){
			mv.addObject("message", messageMap);
			mv.setViewName("dorm/dormManage");
			return mv;
		}else{
//			mv.addObject("wrongMessage","");
//			mv.setViewName("dorm/errorPage");
//			return mv;
			throw new PermissionUnallowedException("您没有权限查看相关网页信息！");
		}
	}

	
	/**
	 * 新建一个宿舍
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/addDorm")
	@ResponseBody
	public RespObj createDorm(@RequestParam(value="dormAreaId", defaultValue = "")String dormAreaId,
			                 @RequestParam(value="dormBuildingId", defaultValue = "")String dormBuildingId,
			                 @RequestParam(value="dormFloorId", defaultValue = "")String dormFloorId,
			                 @RequestParam(value="dormName", defaultValue = "")String dormName,
			                 @RequestParam(value="bedNumber", defaultValue = "")String bedNumber,
			                 @RequestParam(value="dormPhone", defaultValue = "")String dormPhone,
			                 @RequestParam(value="equipment", defaultValue = "")String equipment){
		RespObj respObj= new RespObj(Constant.FAILD_CODE);
		int role=getSessionValue().getUserRole();
		boolean isAdmin = UserRole.isDormManager(role) || UserRole.isK6ktHelper(role) || UserRole.isManager(role);;
		String schoolId=getSessionValue().getSchoolId();
		String regex = "[0-9]{1,2}";
		if(!bedNumber.matches(regex)){
		   respObj.setMessage("您输入的床位数量过大或格式不对！");
		   return respObj;
		}
		if(StringUtils.isBlank(schoolId)){
		   respObj.setMessage("您没有所属学校，不能新建宿舍！");
		   return respObj;
		}
		if(isAdmin){
		   dormService.addDormEntry(new DormEntry(new ObjectId(schoolId),new ObjectId(dormAreaId),
				   new ObjectId(dormBuildingId),new ObjectId(dormFloorId), dormName,Integer.parseInt(bedNumber),dormPhone,equipment));
		   return RespObj.SUCCESS;
		}
		return RespObj.FAILD;
	}
	
	/**
	 * 宿舍列表
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/dormList")
	@ResponseBody
	public String queryDormList(@RequestParam(value = "queryId" , defaultValue="") String queryId,
			                    @RequestParam(value = "columName" , defaultValue="") String columName,
			                    @RequestParam(value = "pageNo" , defaultValue="1") int pageNo,
			                    @RequestParam(value = "pageSize" , defaultValue="10") int pageSize){
		String schoolId=getSessionValue().getSchoolId();
		if(schoolId==null || "".equals(schoolId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		//查询宿舍列表
		List<DormEntry>  srcList = new ArrayList<DormEntry>();
		int total =0;
		int startIndex = (pageNo-1)*pageSize;
		if("".equals(queryId) && "".equals(columName)){
			srcList=dormService.findDorms(new ObjectId(schoolId),startIndex,pageSize);
			total = dormService.countDorm(new ObjectId(schoolId));
		}else{
			srcList=dormService.findDormsByQuery(columName,new ObjectId(queryId), new ObjectId(schoolId),startIndex,pageSize);
			total = dormService.countDormByQuery(new ObjectId(schoolId),columName,new ObjectId(queryId));
		}
		List<Map<String,Object>> fomartList = fomartDormList(srcList);
		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("total",total);
		pageMap.put("pageNo",pageNo); 
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("datas",fomartList);
		resultMap.put("pagejson",pageMap);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 组装前台要用的list
	 * @param srcList
	 * @return
	 */
	private List<Map<String,Object>> fomartDormList(List<DormEntry> srcList){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(DormEntry dormEntry:srcList){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dormEntry.getID().toString());
			map.put("dormName", dormEntry.getDormName());
			map.put("dormPhone", dormEntry.getDormPhone());
			map.put("bedNumber", dormEntry.getBedNumber());
			map.put("equipment", dormEntry.getEquipment());
			List<DormStudentEntry> dormStudentList=dormService.findDormStudentEntry(dormEntry.getID());
			List<String> clasList=new ArrayList<String>();
			Map<String, Object> gcMap = new HashMap<String, Object>();
			String gc = Constant.EMPTY;
			for(DormStudentEntry dormStudentEntry:dormStudentList){
				gcMap.put("sex", dormStudentEntry.getSex());//性别
				String grade = dormStudentEntry.getGrade();//年级
				String clas = dormStudentEntry.getDlass();//班级
				if(!clasList.contains(grade+"/"+clas)){
					clasList.add(grade+"/"+clas);
					if(StringUtils.isEmpty(gc)){
						gc = gc + grade + "/" + clas;
					}else{
						gc = gc + "、" + grade + "/" + clas;
					}
				}
			}
			gcMap.put("gradeAndClass", gc);
			map.put("gAndc", gcMap);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * @Description: 新增宿舍区
	 * @param dormAreaName
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/newDormArea")
	@ResponseBody
	public String addDormArea(@RequestParam(value="dormAreaName", defaultValue = "")String dormAreaName){
		String schoolId = getSessionValue().getSchoolId();
		DormAreaEntry dormAreaEntry = new DormAreaEntry(new ObjectId(schoolId), dormAreaName);
		ObjectId resultId = dormService.addDormArea(dormAreaEntry);
		if(resultId == null){
			return JSON.toJSONString(RespObj.FAILD);
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("areaId", resultId.toString());
		return JSON.toJSONString(map);
	}
	
	/**
	 * @Description: 新增宿舍楼
	 * @param dormAreaName
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/newDormBuild")
	@ResponseBody
	public String addDormBuild(@RequestParam(value="dormBuildName", defaultValue = "")String dormBuildName,
								@RequestParam(value="dormAreaId", defaultValue = "")String dormAreaId){
		DormBuildingEntry dormBuildingEntry = new DormBuildingEntry(new ObjectId(dormAreaId), dormBuildName);
		ObjectId resultId = dormService.addDormBuilding(dormBuildingEntry);
		if(resultId == null){
			return JSON.toJSONString(RespObj.FAILD);
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("builId", resultId.toString());
		return JSON.toJSONString(map);
	}
	/**
	 * @Description:新增宿舍楼层  
	 * @param dormFloorName
	 * @param dormBuildId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/newDormFloor")
	@ResponseBody
	public String addDormFloor(@RequestParam(value="dormFloorName", defaultValue = "")String dormFloorName,
			@RequestParam(value="dormBuildId", defaultValue = "")String dormBuildId){
		DormFloorEntry dormFloorEntry = new DormFloorEntry(new ObjectId(dormBuildId), dormFloorName);
		ObjectId resultId = dormService.addDormFloor(dormFloorEntry);
		if(resultId == null){
			return JSON.toJSONString(RespObj.FAILD);
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("floorId", resultId.toString());
		return JSON.toJSONString(map);
	}
	
	/**
	 * @Description:根据id更新宿舍区  
	 * @param dormAreaName
	 * @param dormAreaId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/updateDormArea")
	@ResponseBody
	public String updateDormArea(@RequestParam(value="dormAreaName", defaultValue = "")String dormAreaName,
								@RequestParam(value="dormAreaId", defaultValue = "")String dormAreaId){
		dormService.updateDormArea(new ObjectId(dormAreaId), dormAreaName);
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 宿舍添加一个学生
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/addDormStudent")
	@ResponseBody
	public RespObj addDormStudents(@RequestParam(value="dormId", defaultValue = "")String dormId,
			                 @RequestParam(value="dormName", defaultValue = "")String dormName,
			                 @RequestParam(value="gradeId", defaultValue = "")String gradeId,
                             @RequestParam(value="classId", defaultValue = "")String classId,
			                 @RequestParam(value="grade", defaultValue = "")String grade,
			                 @RequestParam(value="dclass", defaultValue = "")String dclass,
			                 @RequestParam(value="studentId", defaultValue = "")String studentId,
			                 @RequestParam(value="studentName", defaultValue = "")String studentName,
			                 @RequestParam(value="sex", defaultValue = "")String sex,
			                 @RequestParam(value="studentNumber", defaultValue = "")String studentNumber,
			                 @RequestParam(value="bed", defaultValue = "")String bed,
			                 @RequestParam(value="receiveGoods", defaultValue = "")String receiveGoods){
		RespObj respObj= new RespObj(Constant.FAILD_CODE);
		String schoolId=getSessionValue().getSchoolId();
	    if(StringUtils.isBlank(schoolId)){
	    	respObj.setMessage("您没有所属学校，不能添加学生！");
	    	return respObj;
	    }
	    if(StringUtils.isBlank(dormId)&&StringUtils.isBlank(studentId)&&StringUtils.isBlank(sex)){
	    	respObj.setMessage("添加信息有误，添加失败！");
	    	return respObj;
	    }
	    List<DormStudentEntry> dormStudentList=dormService.findDormStudentEntry(new ObjectId(dormId));
	    for(DormStudentEntry dormStudentEntry:dormStudentList){
	    	if(studentId.equals(dormStudentEntry.getStudentId().toString())){
	    		respObj.setMessage("此学生已在当前宿舍中，添加失败！");
		    	return respObj;
	    	}
	    	if(!sex.equals(dormStudentEntry.getSex())){
	    		respObj.setMessage("此宿舍不能住"+sex+"生，添加失败！");
		    	return respObj;
	    	}
	    }
	    DormStudentEntry study=dormService.findStudy(new ObjectId(studentId));
	    if(null!=study){
	    	respObj.setMessage("此学生已有宿舍，添加失败！");
	    	return respObj;
	    }
		dormService.addDormStudent(new DormStudentEntry(new ObjectId(schoolId),new ObjectId(dormId),dormName,
				new ObjectId(gradeId),new ObjectId(classId),studentNumber,Integer.parseInt(bed),new ObjectId(studentId),
				studentName,sex,grade,dclass,receiveGoods,System.currentTimeMillis()));
	    return RespObj.SUCCESS;
	}
	
	/**
	 * 宿舍查看
	 * @author huanxiaolei@ycode.cn
	 */
	@RequestMapping("/dormStudentDetail")
	@ResponseBody
	public String findDormStudent(@RequestParam(value="dormId", defaultValue = "")String dormId){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<DormStudentEntry> dormStudentList=dormService.findDormStudentEntry(new ObjectId(dormId));
		for(DormStudentEntry dormStudent:dormStudentList){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", dormStudent.getID().toString());
			map.put("bed", dormStudent.getBed());
			map.put("studentNumber", dormStudent.getStudentNum());
			map.put("studentName", dormStudent.getStudentName());
			map.put("sex", dormStudent.getSex());
			map.put("grade", dormStudent.getGrade());
			map.put("clas", dormStudent.getDlass());
			map.put("receiveGoods", dormStudent.getReceiveGoods());
			resultList.add(map);
		}
		return JSON.toJSONString(resultList);	
	}
	
	/**
	 * 将学生从宿舍删除
	 * @author huanxiaolei@ycode.cn
	 * @param studentNum
	 * @return
	 */
	@RequestMapping("/delStudent")
	@ResponseBody
	public RespObj deleteDormStudent(@RequestParam(value="id", defaultValue = "")String id){
		if("".equals(id)){
			return RespObj.FAILD;
		}
		dormService.delDormStudent(new ObjectId(id));
		return RespObj.SUCCESS;	
	}
	
	/**
	 * 获得宿舍床位和所住学生性别
	 * @author huanxiaolei@ycode.cn
	 * @param dormId
	 * @return
	 */
	@RequestMapping("/findDormBed")
	@ResponseBody
	public String findDormBeds(@RequestParam(value="dormId", defaultValue = "")String dormId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Integer> bedList = new ArrayList<Integer>();
		DormEntry dormEntry=dormService.findDormEntry(new ObjectId(dormId));
		int totalBed = dormEntry.getBedNumber();
		for(int i=1;i<totalBed+1;i++){
			bedList.add(i);
		}
		List<DormStudentEntry> dormStudentList=dormService.findDormStudentEntry(new ObjectId(dormId));
		for(DormStudentEntry dormStudentEntry:dormStudentList){
			List<String> sexList = new ArrayList<String>();
			String sex=dormStudentEntry.getSex()==null?"":dormStudentEntry.getSex();
			Integer bedNum = dormStudentEntry.getBed();
			if(bedList.contains(bedNum)){
			   bedList.remove(bedNum);
			}
			sexList.add(sex);
			resultMap.put("sex",sexList);
		}
		resultMap.put("bed",bedList);
		return JSON.toJSONString(resultMap);	
	}
	
	/**
	 * @Description:根据id更新宿舍楼    
	 * @param dormBuildId
	 * @param dormBuildName
	 * @param dormAreaId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/updateDormBuild")
	@ResponseBody
	public String updateDormBuild(@RequestParam(value="dormBuildId", defaultValue = "")String dormBuildId,
								@RequestParam(value="dormBuildName", defaultValue = "")String dormBuildName,
								@RequestParam(value="dormAreaId", defaultValue = "")String dormAreaId){
		dormService.updateDormBuildingEntry(new ObjectId(dormBuildId), dormBuildName, new ObjectId(dormAreaId));
		//查询该楼下所有宿舍信息
		List<DormEntry> dormList = dormService.findByBuildId(new ObjectId(dormBuildId));
		for(DormEntry dormEntry : dormList){
			dormService.updateDormAreaId(dormEntry.getID(), new ObjectId(dormAreaId));
		}
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * @Description: 根据id更新宿舍层    
	 * @param dormFloorId
	 * @param dormFloorName
	 * @param dormBuildId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/updateDormFloor")
	@ResponseBody
	public String updateDormFloor(@RequestParam(value="dormFloorId", defaultValue = "")String dormFloorId,
			@RequestParam(value="dormFloorName", defaultValue = "")String dormFloorName,
			@RequestParam(value="dormBuildId", defaultValue = "")String dormBuildId){
		dormService.updateDormFloor(new ObjectId(dormFloorId), dormFloorName, new ObjectId(dormBuildId));
		//获取该层所有宿舍
		List<DormEntry> dormList = dormService.findByDormFloorId(new ObjectId(dormFloorId));
		//根据id更细宿舍的宿舍楼的id
		for(DormEntry dormEntry : dormList){
			dormService.updateDormBuildId(dormEntry.getID(), new ObjectId(dormBuildId));
		}
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 根据id删除宿舍区
	 * @return
	 */
	@RequestMapping("/deleteDormArea")
	@ResponseBody
	public String deleteDormArea(@RequestParam(value="id", defaultValue = "")String dormAreaId){
		if("".equals(dormAreaId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		dormService.deleteDormAreaById(new ObjectId(dormAreaId));
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 根据id删除宿舍楼
	 * @return
	 */
	@RequestMapping("/deleteDormBuild")
	@ResponseBody
	public String deleteDormBuild(@RequestParam(value="id", defaultValue = "")String dormBuildId){
		if("".equals(dormBuildId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		dormService.deleteDormBuildingById(new ObjectId(dormBuildId));
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	/**
	 * 根据id删除宿舍楼层
	 * @return
	 */
	@RequestMapping("/deleteDormFloor")
	@ResponseBody
	public String deleteDormFloor(@RequestParam(value="id", defaultValue = "")String dormFloorId){
		if("".equals(dormFloorId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		dormService.deleteDormFloorById(new ObjectId(dormFloorId));
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * @Description: 根据学校id查询宿舍区列表(包含所有的楼，层)
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/findDormAreaFullList")
	@ResponseBody
	public String findDormAreaFullList(){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String schoolId = getSessionValue().getSchoolId();
		List<DormAreaEntry> srcList = dormService.findDormAreaEntry(new ObjectId(schoolId));
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		for(DormAreaEntry e : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", e.getID().toString());
			m.put("name", e.getDormAreaName());
			list.add(m);
		}
		resultMap.put("datas", list);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * @Description: 根据学校id查询宿舍区列表(包含所有的楼，层)
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/findDormAreaList")
	@ResponseBody
	public String findDormAreaList(){
		String schoolId = getSessionValue().getSchoolId();
		int role = getSessionValue().getUserRole();
		boolean isAdmin = UserRole.isDormManager(role) || UserRole.isK6ktHelper(role) || UserRole.isManager(role);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> dormAreaMap = new HashMap<String,Object>();
		//所有宿舍区列表
		List<DormAreaEntry> dormArealist = dormService.findDormAreaEntry(new ObjectId(schoolId));
		for(DormAreaEntry dormArea : dormArealist){
			//存储宿舍区组装的数据
			Map<String,Object> areaMap = new HashMap<String,Object>();
			areaMap.put("id", dormArea.getID().toString());//宿舍区id
			areaMap.put("dormAreaName", dormArea.getDormAreaName());//宿舍区名字
			areaMap.put("type", dormArea.getType());//宿舍区类型
			//所有宿舍楼的列表
			List<DormBuildingEntry> dormBuildList = dormService.findByDormAreaId(dormArea.getID());
			//存储宿舍楼
			List<Map<String,Object>> buildList = new ArrayList<Map<String,Object>>();
			for(DormBuildingEntry dormBuild : dormBuildList){
				//存储宿舍楼组装的数据
				Map<String,Object> buildMap = new HashMap<String,Object>();
				buildMap.put("id", dormBuild.getID().toString());//宿舍楼id
				buildMap.put("areaId", dormBuild.getDormAreaId());//所属宿舍区的id
				buildMap.put("dormBuildName", dormBuild.getDormBuildingName());//宿舍楼名字
				buildMap.put("type", dormBuild.getType());//宿舍楼类型
				//宿舍层列表
				List<DormFloorEntry> dormFloorList = dormService.findByDormBuildId(dormBuild.getID());
				//存储宿舍层
				List<Map<String,Object>> floorList = new ArrayList<Map<String,Object>>();
				for(DormFloorEntry dormFloor : dormFloorList){
					//存储宿舍层组装的数据
					Map<String,Object> floorMap = new HashMap<String,Object>();
					floorMap.put("id", dormFloor.getID().toString());//宿舍层id
					floorMap.put("buildId", dormFloor.getDormBuildingId().toString());//所属宿舍楼id
					floorMap.put("dormFloorName", dormFloor.getdormFloorName());//宿舍层名字
					floorMap.put("type", dormFloor.getType());//宿舍层的类型
					floorList.add(floorMap);
				}
				buildMap.put("subFloor", floorList);
				buildList.add(buildMap);
			}
			areaMap.put("subBuild",buildList);
			list.add(areaMap);
			dormAreaMap.put("dormData", list);
			dormAreaMap.put("isAdmin", isAdmin);
		}
		return JSON.toJSONString(dormAreaMap);
	}
	
	/**
	 * @Description: 根据宿舍区id查询宿舍楼列表
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/findDormBuildList")
	@ResponseBody
	public String findDormBuildList(@RequestParam(value="dormAreaId", defaultValue="")String dormAreaId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<DormBuildingEntry> srcList = dormService.findByDormAreaId(new ObjectId(dormAreaId));
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		for(DormBuildingEntry e : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", e.getID().toString());
			m.put("name", e.getDormBuildingName());
			list.add(m);
		}
		resultMap.put("datas", list);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * @Description: 根据宿舍楼id查询宿舍层列表
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/findDormFloorList")
	@ResponseBody
	public String findDormFloorList(@RequestParam(value="dormBuildId", defaultValue="")String dormBuildId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<DormFloorEntry> srcList = dormService.findByDormBuildId(new ObjectId(dormBuildId));
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		for(DormFloorEntry e : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", e.getID().toString());
			m.put("name", e.getdormFloorName());
			list.add(m);
		}
		resultMap.put("datas", list);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 根据年级id获取班级
	 * @author zhanghao
	 */
	@RequestMapping("/findClassList")
	@ResponseBody
	public String findClassList(@RequestParam(value="gradeId",defaultValue="ALL")String gradeId){
		List<ClassEntry> classes=dormService.findClassListByGradeId(new ObjectId(gradeId));
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		for(ClassEntry classEntry:classes){
			Map<String,Object> classMap=new HashMap<String,Object>();
			classMap.put("id", classEntry.getID().toString());
			classMap.put("name", classEntry.getName());
			resultList.add(classMap);
		}
		return JSON.toJSONString(resultList);
	}
	
	/**
	 * 根据搜索条件查看入住学生列表（默认全部学生）
	 * @author zhanghao
	 */
	@RequestMapping("/findAllStudents")
	@ResponseBody
	public String findStudentInDorm(@RequestParam(value="gradeId",defaultValue="ALL")String gradeId,
						            @RequestParam(value="dlassId",defaultValue="ALL")String dlassId,
									@RequestParam(value="sex",defaultValue="ALL")String sex,
									@RequestParam(value="studentName",defaultValue="")String studentName,
									@RequestParam(value="studentNum",defaultValue="")String studentNum,
									@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
									@RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
		String schoolId=getSessionValue().getSchoolId();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int total = 0;
		int startIndex = (pageNo-1)*pageSize;
		List<DormStudentEntry> students=dormService.findDormStudent(new ObjectId(schoolId),"ALL".equals(gradeId)?null:new ObjectId(gradeId),
				"ALL".equals(dlassId)?null:new ObjectId(dlassId), sex, studentName, studentNum,startIndex,pageSize);
		total = dormService.findDormStudentCount(new ObjectId(schoolId),"ALL".equals(gradeId)?null:new ObjectId(gradeId),
				"ALL".equals(dlassId)?null:new ObjectId(dlassId), sex, studentName, studentNum);
		List<Map<String , Object>> formatList=new ArrayList<Map<String , Object>>();
		for(DormStudentEntry  student: students ){
		    Map<String,Object> map = new HashMap<String,Object>();
    		map.put("dormName", student.getDormName());
        	map.put("studentName", student.getStudentName());
        	map.put("studentNum", student.getStudentNum());
        	map.put("bed", student.getBed());
        	map.put("sex", student.getSex());
        	map.put("grade", student.getGrade());
        	map.put("dlass", student.getDlass());
        	map.put("receiveGoods", student.getReceiveGoods());
        	map.put("subGoods",student.getReceiveGoods());
        	formatList.add(map);
    	}

		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("total", total);
		pageMap.put("pageNo", pageNo);
		resultMap.put("pageJson", pageMap);
		resultMap.put("list", formatList);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 根据班级ID查询学生信息
	 * @author:huanxiaolei@ycode.cn
	 */
	@RequestMapping("/findStuInfo")
	@ResponseBody
	public String findStuInfo(String classId){
		List<Map<String,Object>> stuName = new ArrayList<Map<String,Object>>();
		ClassEntry ce = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
		List<DormStudentEntry> dormStudents = dormService.findStudentByClassId(new ObjectId(classId));
		Collection<ObjectId> stuId = ce.getStudents();
		for(DormStudentEntry dormStudent:dormStudents){
			if(stuId.contains(dormStudent.getStudentId())){
				stuId.remove(dormStudent.getStudentId());
			}
		}
		List<UserEntry> userEntryList = dormService.findStuName(stuId);
		for(UserEntry userEntry:userEntryList){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", userEntry.getID().toString());
			map.put("studentName", userEntry.getUserName());//学生姓名
			map.put("studyNum", userEntry.getStudyNum());//学生学号
			map.put("sex", userEntry.getSex()==0?"女":"男");//性别
			stuName.add(map);
		}
		return JSON.toJSONString(stuName);
	}
	/**
	 * 根据宿舍层id查询是否有宿舍
	 * @Description:  
	 * @param dormFloorId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	@RequestMapping("/findDormCount")
	@ResponseBody
	public String findDormCount(@RequestParam(value="id", defaultValue = "")String dormFloorId){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<DormEntry> dormList = dormService.findByDormFloorId(new ObjectId(dormFloorId));
		resultMap.put("datas", dormList);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 删除宿舍
	 * @author:huanxiaolei@ycode.cn
	 */
	@RequestMapping("/delDorm")
	@ResponseBody
	public RespObj delDorm(@RequestParam(value="dormId",defaultValue="")String dormId){
		if("".equals(dormId)){
			return RespObj.FAILD;
		}
		dormService.deleteDrom(new ObjectId(dormId));
		return RespObj.SUCCESS;
	}
}
