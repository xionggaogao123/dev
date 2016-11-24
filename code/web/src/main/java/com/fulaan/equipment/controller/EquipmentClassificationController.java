package com.fulaan.equipment.controller;

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
import com.fulaan.equipment.service.EquipmentClassificationService;
import com.fulaan.equipment.service.EquipmentService;
import com.pojo.equipment.EquipmentClassificationEntry;
import com.sys.utils.RespObj;

/**
 * 校园设备分类Controller
 * @author cxy
 *
 */
@Controller
@RequestMapping("/equipmentClassification")
public class EquipmentClassificationController extends BaseController{
	@Autowired
	private EquipmentClassificationService equipmentClassificationService;
	
	@Autowired
	private EquipmentService equipmentService;
	
	/**
	 * 新增一个设备分类
	 * @return
	 */

	@RequestMapping("/addEquipmentClassification")
	@ResponseBody
	public String addEquipmentClassification(@RequestParam(value = "equipmentClassificationParentId" , defaultValue="") String equipmentClassificationParentId){
		if("".equals(equipmentClassificationParentId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EquipmentClassificationEntry e = new EquipmentClassificationEntry(schoolId, "新建分类", "", new ObjectId().toString(),new ObjectId(equipmentClassificationParentId).toString());
		equipmentClassificationService.addEquipmentClassificationEntry(e); 
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 查询本学校所有设备分类
	 * @return
	 */

	@RequestMapping("/queryEquipmentClassification")
	@ResponseBody
	public String queryEquipmentClassification(){
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		if(equipmentClassificationService.queryEquipmentClassificationBySchoolIdAndEquipmentClassificationId(schoolId).size() == 0){
			EquipmentClassificationEntry e = new EquipmentClassificationEntry(schoolId, "根节点", "", new ObjectId().toString() ,"0");
			equipmentClassificationService.addEquipmentClassificationEntry(e);
		}
		List<EquipmentClassificationEntry> queryList = equipmentClassificationService.queryEquipmentClassificationBySchoolIdAndEquipmentClassificationId(schoolId);
		List<Map<String,String>> fomartList = new ArrayList<Map<String,String>>();
		for(EquipmentClassificationEntry pce : queryList){
			Map<String,String> m = new HashMap<String,String>();
			m.put("id", pce.getEquipmentClassificationId());
			m.put("pId", pce.getEquipmentClassificationParentId());
			m.put("name", pce.getEquipmentClassificationName());
			m.put("postscript", pce.getEquipmentClassificationPostscript());
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
	 * 更新一个设备分类
	 * @return
	 */

	@RequestMapping("/updateEquipmentClassification")
	@ResponseBody
	public String updateEquipmentClassification(@RequestParam(value = "equipmentClassificationId" , defaultValue="") String equipmentClassificationId,
											@RequestParam(value = "equipmentClassificationName" , defaultValue="") String equipmentClassificationName,
											@RequestParam(value = "equipmentClassificationPostscript" , defaultValue="") String equipmentClassificationPostscript){
		if("".equals(equipmentClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		equipmentClassificationService.updateEquipmentClassificationBaseInfo(equipmentClassificationId, equipmentClassificationName, equipmentClassificationPostscript); 
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	/**
	 * 删除一个设备分类
	 * @return
	 */

	@RequestMapping("/deleteEquipmentClassification")
	@ResponseBody
	public String deleteEquipmentClassification(@RequestParam(value = "equipmentClassificationId" , defaultValue="") String equipmentClassificationId){
		if("".equals(equipmentClassificationId)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		deleteEquipmentClassificationAndAllChildren(equipmentClassificationId);
		
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	/**
	 * 递归删除本身和子节点
	 */
	private void deleteEquipmentClassificationAndAllChildren(String equipmentClassificationId){
		List<EquipmentClassificationEntry> list = equipmentClassificationService.queryEquipmentClassificationByParentId(equipmentClassificationId);
		equipmentClassificationService.deleteEquipmentClassification(equipmentClassificationId);
		equipmentService.deleteEquipmentsByEquipmentClassificationId(new ObjectId(equipmentClassificationId));
		for(EquipmentClassificationEntry pce : list){
			deleteEquipmentClassificationAndAllChildren(pce.getEquipmentClassificationId());
		}
	}
	/**
	 * 校产分类树根据拖拽结果重新进行排序
	 * @return
	 */

	@RequestMapping("/rebuildAllEquipmentClassification")
	@ResponseBody
	public String rebuildAllEquipmentClassification(@RequestParam(value = "treeJsonStr" , defaultValue="") String treeJsonStr){
		if("".equals(treeJsonStr)){
			return JSON.toJSONString(RespObj.FAILD); 
		}
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		Object jsonObject = JSON.parse(treeJsonStr);
		JSONObject root = (JSONObject)((JSONArray) jsonObject).get(0);
//		((JSONObject)((JSONArray) jsonObject).get(0)).get("children")
		equipmentClassificationService.deleteAllEquipmentClassifications(schoolId);
		saveTree(root);
		return JSON.toJSONString(RespObj.SUCCESS);  
	}
	
	private void saveTree(JSONObject obj){
		EquipmentClassificationEntry pce = new EquipmentClassificationEntry(new ObjectId(getSessionValue().getSchoolId()), obj.getString("name") + "", obj.get("postscript") + "",
																			obj.get("id") + "", obj.get("pId")==null?"0":obj.get("pId") + "");
		equipmentClassificationService.addEquipmentClassificationEntry(pce);
		JSONArray children = (JSONArray)obj.get("children");
		if(children != null){
			for(int i=0;i<children.size();i++){
				saveTree((JSONObject)children.get(i));
			}
		}
	} 
}
