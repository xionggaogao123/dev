package com.fulaan.registration.controller;

import java.util.ArrayList;
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
import com.fulaan.registration.service.QualityService;
import com.fulaan.registration.service.SubQualityService;
import com.pojo.registration.QualityEntry;
import com.pojo.registration.SubQualityEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.utils.RespObj;
/**
 * 素质教育Controller
 * @author cxy
 * 2015-11-25 15:53:33
 */
@Controller
@RequestMapping("/quality") 
public class QualityController extends BaseController{
	
	@Autowired
	private QualityService qualityService;
	
	@Autowired
	private SubQualityService subQualityService;
	
	/**
	 * 项目管理列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView showListPage() {
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		ModelAndView mv = new ModelAndView();
		List<QualityEntry> srcList = qualityService.queryQuality(schoolId);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(QualityEntry qe : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", qe.getID().toString());
			m.put("name", qe.getName());
			resultList.add(m);
		}
		mv.addObject("datas", resultList);
		mv.setViewName("registration/projectManage");
		return mv;
	}
	
	/**
	 * 子项目项目管理列表
	 */
	@RequestMapping("/subList")
	@ResponseBody
	public ModelAndView showSubListPage(@RequestParam(value = "parentId",defaultValue = "") String parentId) {
		ObjectId pid = new ObjectId(parentId);
		ModelAndView mv = new ModelAndView();
		List<SubQualityEntry> srcList = subQualityService.querySubQualityList(pid);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(SubQualityEntry qe : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", qe.getID().toString());
			m.put("name", qe.getName());
			m.put("requirement", qe.getRequirement());
			resultList.add(m);
		}
		mv.addObject("datas", resultList);
		mv.addObject("pid", parentId);
		mv.setViewName("registration/addProject");
		return mv;
	}
	
	/**
	 * 新增素质教育项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/addProject")
	@ResponseBody
	public RespObj addProject(@RequestParam(value = "name",defaultValue = "") String name){
		if(StringUtils.isBlank(name)){
			return RespObj.FAILD;
		}
		QualityEntry qe = new QualityEntry(name,new ObjectId(getSessionValue().getSchoolId()));
		qualityService.addQualityEntry(qe);
		return RespObj.SUCCESS; 
	}
	
	/**
	 * 新增素质教育子项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/addSubProject")
	@ResponseBody
	public RespObj addSubProject(@RequestParam(value = "name",defaultValue = "") String name,
								 @RequestParam(value = "requirement",defaultValue = "") String requirement,
								 @RequestParam(value = "parentId",defaultValue = "") String parentId){
		if(StringUtils.isBlank(name) || StringUtils.isBlank(requirement) || StringUtils.isBlank(parentId)){
			return RespObj.FAILD;
		}
		SubQualityEntry sqe = new SubQualityEntry(name,requirement,new ObjectId(parentId));
		subQualityService.addSubQualityEntry(sqe);
		return RespObj.SUCCESS; 
	}
	
	/**
	 * 更新素质教育项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/editProject")
	@ResponseBody
	public RespObj editProject(@RequestParam(value = "name",defaultValue = "") String name,
							   @RequestParam(value = "id",defaultValue = "") String id){
		if(StringUtils.isBlank(name) || StringUtils.isBlank(id)){
			return RespObj.FAILD;
		}
		qualityService.updateQualityEntry(new ObjectId(id), name);
		return RespObj.SUCCESS; 
	}
	
	/**
	 * 更新素质教育项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/editSubProject")
	@ResponseBody
	public RespObj editProject(@RequestParam(value = "name",defaultValue = "") String name,
							   @RequestParam(value = "requirement",defaultValue = "") String requirement,
							   @RequestParam(value = "id",defaultValue = "") String id){
		if(StringUtils.isBlank(name) || StringUtils.isBlank(requirement) || StringUtils.isBlank(id)){
			return RespObj.FAILD;
		}
		subQualityService.updateSubQualityEntry(new ObjectId(id), name, requirement);
		return RespObj.SUCCESS; 
	}
	
	/**
	 * 删除一条素质教育项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/delProject")
	@ResponseBody
	public RespObj delProject(@RequestParam(value = "id",defaultValue = "") String id){
		if(StringUtils.isBlank(id)){
			return RespObj.FAILD;
		}
		qualityService.deleteQualityEntry(new ObjectId(id));
		return RespObj.SUCCESS; 
	}
	
	/**
	 * 删除一条素质教育子项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/delSubProject")
	@ResponseBody
	public RespObj delSubProject(@RequestParam(value = "id",defaultValue = "") String id){
		if(StringUtils.isBlank(id)){
			return RespObj.FAILD;
		}
		subQualityService.deleteSubQualityEntry(new ObjectId(id));
		return RespObj.SUCCESS; 
	}
}
