package com.fulaan.level.controller;

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
import com.fulaan.base.controller.BaseController;
import com.fulaan.level.service.LevelService;
import com.pojo.level.LevelEntry;
import com.sys.utils.RespObj;

/**
 * 等级设置controller,处理等级设置请求
 * 
 * @author cxy
 *
 */
@Controller
@RequestMapping("/level")
public class LevelController extends BaseController{
	@Autowired
	private LevelService levelService;
	/**
	 * 查询本校所有等级信息
	 * @return
	 */
	@RequestMapping("/queryLevel")
	@ResponseBody
	public String queryLevel(){
		String schoolId = getSessionValue().getSchoolId();
		List<LevelEntry> list = levelService.queryLevelsBySchoolId(new ObjectId(schoolId));
		List<Map<String,Object>> formatList = new ArrayList<Map<String,Object>>();
		for(LevelEntry l : list){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("id", l.getID().toString());
			m.put("levelName", l.getLevelName());
			m.put("scoreRange", l.getScoreRange() + "");
			formatList.add(m);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("levels", formatList);
		return JSON.toJSONString(map);
//		return "";
	}
	/**
	 * 新增一条等级信息
	 * @return
	 */
	@RequestMapping("/addLevel")
	@ResponseBody
	public String addLevel(){
		String schoolId = getSessionValue().getSchoolId();
		ObjectId id = levelService.addLevelEntry(new LevelEntry(new ObjectId(schoolId)));
		Map<String,String> m = new HashMap<String,String>();
		m.put("levelId", id.toString());
		return JSON.toJSONString(m);
	}
	/**
	 * 更新等级信息
	 * @return
	 */
	@RequestMapping("/updateLevel")
	@ResponseBody
	public String updateLevel(@RequestParam(value="levelName", defaultValue = "")String levelName,
							  @RequestParam(value="scoreRange", defaultValue = "0")String scoreRange,
							  @RequestParam(value="levelId", defaultValue = "") String levelId){
		if("".equals(levelId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		levelService.updateLevel(new ObjectId(levelId), levelName, Integer.parseInt(scoreRange));
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 删除一条等级
	 * @return
	 */
	@RequestMapping("/deleteLevel")
	@ResponseBody
	public String deleteLevel(@RequestParam(value="levelId", defaultValue = "") String levelId){
		if("".equals(levelId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		levelService.deleteLevel(new ObjectId(levelId));
		return JSON.toJSONString(RespObj.SUCCESS);
	}
}
