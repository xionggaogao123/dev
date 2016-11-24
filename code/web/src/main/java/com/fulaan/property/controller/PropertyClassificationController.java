package com.fulaan.property.controller;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fulaan.base.controller.BaseController;
import com.fulaan.property.service.PropertyClassificationService;
import com.fulaan.property.service.PropertyService;
import com.pojo.property.PropertyClassificationEntry;
import com.sys.utils.RespObj;

/**
 * 校园资产分类Controller
 * @author cxy
 *
 */
@Controller
@RequestMapping("/propertyClassification")
public class PropertyClassificationController extends BaseController{
	@Autowired
	private PropertyClassificationService propertyClassificationService;
	
	@Autowired
	private PropertyService propertyService;
	
	/**
	 * 新增一个校产分类
	 * @return
	 */
	@RequestMapping("/addPropertyClassification")
	@ResponseBody
	public String addPropertyClassification(@RequestParam(value = "propertyClassificationParentId" , defaultValue="") String propertyClassificationParentId){
		if("".equals(propertyClassificationParentId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		PropertyClassificationEntry e = new PropertyClassificationEntry(schoolId, "新建分类", "", new ObjectId().toString(),new ObjectId(propertyClassificationParentId).toString());
		propertyClassificationService.addPropertyClassificationEntry(e); 
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 查询本学校所有校产分类
	 * @return
	 */
	@RequestMapping("/queryPropertyClassification")
	@ResponseBody
	public String queryPropertyClassification(){
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		if(propertyClassificationService.queryPropertyClassificationBySchoolIdAndPropertyClassificationId(schoolId).size() == 0){
			PropertyClassificationEntry e = new PropertyClassificationEntry(schoolId, "根节点", "", new ObjectId().toString() ,"0");
			propertyClassificationService.addPropertyClassificationEntry(e);
		}
		List<PropertyClassificationEntry> queryList = propertyClassificationService.queryPropertyClassificationBySchoolIdAndPropertyClassificationId(schoolId); 
		List<Map<String,String>> fomartList = new ArrayList<Map<String,String>>();
		for(PropertyClassificationEntry pce : queryList){
			Map<String,String> m = new HashMap<String,String>();
			m.put("id", pce.getPropertyClassificationId());
			m.put("pId", pce.getPropertyClassificationParentId());
			m.put("name", pce.getPropertyClassificationName());
			m.put("postscript", pce.getPropertyClassificationPostscript());
			m.put("open", "true");
			fomartList.add(m);
		}
		addIndexForList(fomartList);
		return JSON.toJSONString(fomartList);  
	}
	/**
	 * 增加序号
	 * @param srcList
	 * @return
	 */
	private void addIndexForList(List<Map<String,String>> srcList){
		for(int i=0;i<srcList.size();i++){
			srcList.get(i).put("ranking", (i+1) + "");
		}
	}
	
	/**
	 * 更新一个校产分类
	 * @return
	 */
	@RequestMapping("/updatePropertyClassification")
	@ResponseBody
	public String updatePropertyClassification(@RequestParam(value = "propertyClassificationId" , defaultValue="") String propertyClassificationId,
											@RequestParam(value = "propertyClassificationName" , defaultValue="") String propertyClassificationName,
											@RequestParam(value = "propertyClassificationPostscript" , defaultValue="") String propertyClassificationPostscript){
		if("".equals(propertyClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		propertyClassificationService.updatePropertyClassificationBaseInfo(propertyClassificationId, propertyClassificationName, propertyClassificationPostscript); 
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 删除一个校产分类
	 * @return
	 */
	@RequestMapping("/deletePropertyClassification")
	@ResponseBody
	public String deletePropertyClassification(@RequestParam(value = "propertyClassificationId" , defaultValue="") String propertyClassificationId){
		if("".equals(propertyClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		deletePropertyClassificationAndAllChildren(propertyClassificationId);
		
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	/**
	 * 递归删除本身和子节点
	 */
	private void deletePropertyClassificationAndAllChildren(String propertyClassificationId){
		List<PropertyClassificationEntry> list = propertyClassificationService.queryPropertyClassificationByParentId(propertyClassificationId);
		propertyClassificationService.deletePropertyClassification(propertyClassificationId);
		propertyService.deletePropertiesByPropertyClassificationId(new ObjectId(propertyClassificationId));
		for(PropertyClassificationEntry pce : list){
			deletePropertyClassificationAndAllChildren(pce.getPropertyClassificationId());
		}
	}
	
	/**
	 * 校产分类树根据拖拽结果重新进行排序
	 * @return
	 */
	@RequestMapping("/rebuildAllPropertyClassification")
	@ResponseBody
	public String rebuildAllPropertyClassification(@RequestParam(value = "treeJsonStr" , defaultValue="") String treeJsonStr){
		if("".equals(treeJsonStr)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		Object jsonObject = JSON.parse(treeJsonStr);
		JSONObject root = (JSONObject)((JSONArray) jsonObject).get(0);
//		((JSONObject)((JSONArray) jsonObject).get(0)).get("children")
		propertyClassificationService.deleteAllPropertyClassifications(schoolId);
		saveTree(root);
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	private void saveTree(JSONObject obj){
		PropertyClassificationEntry pce = new PropertyClassificationEntry(new ObjectId(getSessionValue().getSchoolId()), obj.getString("name") + "", obj.get("postscript") + "",
																			obj.get("id") + "", obj.get("pId")==null?"0":obj.get("pId") + "");
		propertyClassificationService.addPropertyClassificationEntry(pce);
		JSONArray children = (JSONArray)obj.get("children");
		if(children != null){
			for(int i=0;i<children.size();i++){
				saveTree((JSONObject)children.get(i));
			}
		}
	} 
}
