package com.fulaan.resources.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.pojo.resources.ResourceDictionaryEntry;
import com.sys.utils.RespObj;

/**
 * 资源字典controller
 * 2015-8-24 19:50:28
 * @author cxy
 *
 */
@Controller
@RequestMapping("/resourceDictionary")
public class ResourceDictionaryController extends BaseController{
	@Autowired
	private ResourceDictionaryService resourceDictionaryService;
	
	/**
	 * 查看报修记录页面查询报修记录
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/queryRootResources")
	@ResponseBody
	public String queryRootResources(@RequestParam(value="rootType", defaultValue = "1")int rootType){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<ResourceDictionaryEntry> resultList = resourceDictionaryService.getResourceDictionaryEntrys(rootType);
		List<Map<String,Object>> formatList = formatResultList(resultList);
		resultMap.put("datas",formatList);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 根据传进的父ID查询所有子资源类型
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/querySubResources")
	@ResponseBody
	public String querySubResources(@RequestParam(value="parentId", defaultValue = "")String parentId,
									@RequestParam(value="type", defaultValue = "")int type){
		if(parentId == null || "".equals(parentId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<ResourceDictionaryEntry> resultList = resourceDictionaryService.getResourceDictionaryEntrys(new ObjectId(parentId),type);
		List<Map<String,Object>> formatList = formatResultList(resultList);
		resultMap.put("datas",formatList);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 根据传进的父ID查询所有子资源类型(资源管理后端用)
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/querySubResourcesForManager")  
	@ResponseBody
	public String querySubResourcesForManager(@RequestParam(value="parentId", defaultValue = "")String parentId,
											@RequestParam(value="type", defaultValue = "")int type){
		if(parentId == null || "".equals(parentId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		List<ResourceDictionaryEntry> resultList;
		if("ALL".equals(parentId)){
			resultList = resourceDictionaryService.getResourceDictionaryEntrys(type);
		}else{
			resultList = resourceDictionaryService.getResourceDictionaryEntrys(new ObjectId(parentId),type);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> formatList = formatResultList(resultList);
		resultMap.put("datas",formatList);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 根据父ID和type查询目录数据
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/queryCatalog")
	@ResponseBody
	public String queryCatalog(@RequestParam(value="parentId", defaultValue = "")String parentId,
									@RequestParam(value="type", defaultValue = "")int type){
		if(parentId == null || "".equals(parentId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<ResourceDictionaryEntry> resultList = resourceDictionaryService.getResourceDictionaryEntrys(new ObjectId(parentId),type);
		List<Map<String,Object>> formatList = formatResultList(resultList);
		for(Map<String,Object> m : formatList){
			putChildDatas(m,type + 1);
		}
		resultMap.put("datas",formatList);
		return JSON.toJSONString(resultMap);
	}
	
	private void putChildDatas(Map<String,Object> srcMap,int typeNum){
		List<ResourceDictionaryEntry> subList = resourceDictionaryService.getResourceDictionaryEntrys(new ObjectId(srcMap.get("id").toString()),typeNum);
		srcMap.put("subData",formatResultList(subList));
	}
	
	/**
	 * 将查询结果进行组装
	 * @param srcList
	 * @return
	 */
	private List<Map<String,Object>> formatResultList(List<ResourceDictionaryEntry> srcList){
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		for(ResourceDictionaryEntry e : srcList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", e.getID().toString());
			m.put("name", e.getName());
			list.add(m);
		}
		return list;
	}
	
	
	
}
