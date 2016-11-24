package com.fulaan.competition.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.controller.BaseController;
import com.fulaan.competition.service.CompetitionItemDetailService;
import com.fulaan.competition.service.CompetitionScoreService;
import com.fulaan.competition.service.CompetitionService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.utils.ExportUtil;
import com.fulaan.utils.pojo.KeyValue;
import com.mongodb.DBObject;
import com.pojo.competition.*;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 综合评比controller
 * @author cxy
 *
 */
@Controller
@RequestMapping("/competition")
public class CompetitionController extends BaseController{
	@Autowired
	private CompetitionService competitionService;
	
	@Autowired
	private CompetitionScoreService competitionScoreService;
	
	@Autowired
	private ClassService classService;
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private CompetitionItemDetailService competitionItemDetailService;
	
	/**
	 * 评比页面跳转
	 */
	@RequestMapping("/list")
	@ResponseBody
	public ModelAndView retrieveRepairInfo() {
		String schoolId = getSessionValue().getSchoolId();
		// 获取所有年级
		List<GradeView> grades = schoolService.findGradeList(schoolId);
		// 获取所有班级
		List<ClassInfoDTO> classes = classService.findClassInfoBySchoolId(schoolId);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("grades", grades);
		m.put("classes", classes);
		String jsonString = JSON.toJSONString(m);
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", jsonString);
		
		mv.setViewName("competition/competition");
		return mv;
	}

	/**
	 * 评比获取年级信息
	 */
	@RequestMapping("/getGradeInfos")
	@ResponseBody
	public String getGradeInfos(@RequestParam String gradeIdsStr) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<GradeView> grades =new ArrayList<GradeView>();
		String schoolId = getSessionValue().getSchoolId();
		if(gradeIdsStr!=null&&!"".equals(gradeIdsStr)){
			List<String> gradeIds= Arrays.asList(gradeIdsStr.split(","));
			// 获取所有年级
			List<GradeView> retList = schoolService.findGradeList(schoolId);
			for(GradeView grade : retList){
				if(gradeIds.contains(grade.getId())){
					grades.add(grade);
				}
			}
		}
		resultMap.put("grades", grades);
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 新增一条评比
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/addCompetition")
	@ResponseBody
	public String addCompetition(@RequestParam(value = "competitionRange" , defaultValue="") String rangeGrades,
								 @RequestParam(value = "termType" , defaultValue="2015年第二学期") String termType,
								 @RequestParam(value = "competitionName" , defaultValue="") String competitionName,
								 @RequestParam(value = "competitionPostscript" , defaultValue="") String competitionPostscript,
								 @RequestParam(value = "redFlagNum" , defaultValue="0") Integer redFlagNum){
		String schoolId = getSessionValue().getSchoolId();
		List<ObjectId> gradeIds = new ArrayList<ObjectId>();
		if(!("".equals(rangeGrades))){
			String[] gradeIdStrs = rangeGrades.split(",");
			for(String s : gradeIdStrs){
				gradeIds.add(new ObjectId(s));
			}
		}
		List<CompetitionItem> competitionItems = new ArrayList<CompetitionItem>();
		competitionItems.add(new CompetitionItem("评比项目", "", 100));
		List<CompetitionBatch> competitionBatches = new ArrayList<CompetitionBatch>();
		competitionBatches.add(new CompetitionBatch("评比批次1"));
		CompetitionEntry entry = new CompetitionEntry(new	ObjectId(schoolId), termType, competitionName, competitionPostscript, gradeIds, competitionItems, competitionBatches, redFlagNum);
		competitionService.addCompetitionEntry(entry);
		
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 根据当前登录用户的学校Id和选择的学期进行查询
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/queryCompetitions")
	@ResponseBody
	public String queryCompetitions(@RequestParam(value = "termType" , defaultValue="2015年第二学期") String termType){
		String schoolId = getSessionValue().getSchoolId();
		List<CompetitionEntry> coms = competitionService.getCompetitionsBySchoolIdAndTermType(new ObjectId(schoolId), termType);
		List<Map<String,Object>> formatList = uploadCompetitionResultList(schoolId,coms);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("competitions",formatList );
		return JSON.toJSONString(resultMap); 
	}
	
	/**
	 * 根据查询的List组装成前台需要的List
	 * @param coms
	 * @return
	 */
	public List<Map<String,Object>> uploadCompetitionResultList(String schoolId, List<CompetitionEntry> coms){
		List<GradeView> grades = schoolService.findGradeList(schoolId);
		Map<String,GradeView> gradeMap=new HashMap<String, GradeView>();
		for(GradeView gradeView : grades){
			gradeMap.put(gradeView.getId().toString(),gradeView);
		}

		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for(CompetitionEntry ce : coms){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("competitionId",ce.getID().toString()); 
			m.put("competitionName",ce.getCompetitionName());
			m.put("competitionPostscript",ce.getCompetitionPostscript());
			m.put("redFlagNum",ce.getRedFlagNum());
			StringBuffer sb = new StringBuffer("");
			StringBuffer gradeNameSb = new StringBuffer("");
			for(ObjectId id : ce.getCompetitionRange()){
				sb.append(id.toString());
				sb.append(",");
				GradeView gradeView = gradeMap.get(id.toString());
				if(gradeView != null){
					gradeNameSb.append(gradeView.getName());
					gradeNameSb.append(",");
				}
			}

			m.put("competitionRange",sb.toString().equals("")?"":sb.toString().substring(0,sb.toString().length() - 1));
			m.put("competitionRangeName",gradeNameSb.toString().equals("")?"":gradeNameSb.toString().substring(0,gradeNameSb.toString().length() - 1));
			List<Map<String,String>> competitionItems = new ArrayList<Map<String,String>>();
			for(CompetitionItem ci : ce.getCompetitionItems()){
				Map<String,String> mi = new HashMap<String,String>();
				mi.put("itemName", ci.getItemName());
				mi.put("itemPostscript", ci.getItemPostscript());
				mi.put("id", ci.getItemId().toString());
				mi.put("itemFullScore", ci.getItemFullScore() + "");
				competitionItems.add(mi);
			}
			List<Map<String,String>> competitionBatches = new ArrayList<Map<String,String>>();
			for(CompetitionBatch cb : ce.getCompetitionBatches()) {
				Map<String,String> mb = new HashMap<String,String>();
				mb.put("id", cb.getBatchId().toString());
				mb.put("batchName", cb.getBatchName());
				competitionBatches.add(mb);
			}
			m.put("competitionItems",competitionItems);
			m.put("competitionBatches",competitionBatches);
			resultList.add(m);
		}
		return resultList;
	}
	
	/**
	 * 删除一条评比
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/deleteCompetition")
	@ResponseBody
	public String deleteCompetition(@RequestParam(value = "competitionId" , defaultValue="") String competitionId){
		if("".equals(competitionId)){
			return JSON.toJSONString(RespObj.FAILD);  
		}
		competitionService.deleteCompetition(new ObjectId(competitionId));
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 修改一条评比
	 * @param id
	 * @return
	 */
	
	@RequestMapping("/editCompetition")
	@ResponseBody
	public String editCompetition(@RequestParam(value = "competitionRange" , defaultValue="") String rangeGrades,
								 @RequestParam(value = "competitionId" , defaultValue="") String competitionId,
								 @RequestParam(value = "competitionName" , defaultValue="") String competitionName,
								 @RequestParam(value = "competitionPostscript" , defaultValue="") String competitionPostscript,
								  @RequestParam(value = "redFlagNum" , defaultValue="0") Integer redFlagNum){
		if("".equals(competitionId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		List<ObjectId> gradeIds = new ArrayList<ObjectId>();
		if(!("".equals(rangeGrades))){
			String[] gradeIdStrs = rangeGrades.split(",");
			for(String s : gradeIdStrs){
				gradeIds.add(new ObjectId(s));
			}
		}
		//查询之前的参与范围等相关信息
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		ObjectId comId = new ObjectId(competitionId);
		CompetitionEntry ce = competitionService.getCompetitionEntry(comId);
		List<CompetitionBatch> batches = ce.getCompetitionBatches();
		List<CompetitionItem> items = ce.getCompetitionItems();
		//执行更新
		competitionService.updateCompetition(new ObjectId(competitionId), competitionName, competitionPostscript, gradeIds, redFlagNum);
		//找出前后相比新增的和减少的年级ID
		List<ObjectId> preRange = ce.getCompetitionRange();
		List<ObjectId> addRange = new ArrayList<ObjectId>();
		List<ObjectId> delRange = new ArrayList<ObjectId>();
		for(ObjectId pId : preRange){
			if(!(gradeIds.contains(pId))){
				delRange.add(pId);
			}
		}
		for(ObjectId aId : gradeIds){
			if(!(preRange.contains(aId))){
				addRange.add(aId);
			}
		}
		//对每个批次分别进行操作
		for(CompetitionBatch batch : batches){
			//根据是否初始化决定是否执行相应操作
			if(competitionScoreService.queryCompetitionScoresByCompetitionIdAndBatchId(comId, batch.getBatchId()).size() == 0){
				continue;
			}
			//删除该批次下的去除年级分数信息
			for(ObjectId delId : delRange){
				competitionScoreService.deleteAllCompetitionScoresOfCompetitionBatchAndGradeId(batch.getBatchId(), delId);
			}
			//新增该批次下的新增年级分数信息
			for(ObjectId addId : addRange){
				List<ClassInfoDTO> classDTOs = classService.findClassByGradeId(addId.toString());
				if(classDTOs == null){
					continue;
				}
				for(ClassInfoDTO classDTO : classDTOs){
					for(CompetitionItem item : items){
						CompetitionScoreEntry cse = new CompetitionScoreEntry(comId, addId, new ObjectId(classDTO.getId()), batch.getBatchId(), item.getItemId(), item.getItemFullScore());
						competitionScoreService.addCompetitionScoreEntry(cse);
					}
				}
			}
		}
		
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 修改一条评比项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/editCompetitionItem")
	@ResponseBody
	public String editCompetitionItem(@RequestParam(value = "itemId" , defaultValue="") String itemId,
								 @RequestParam(value = "competitionId" , defaultValue="") String competitionId,
								 @RequestParam(value = "itemName" , defaultValue="") String itemName,
								 @RequestParam(value = "itemPostscript" , defaultValue="") String itemPostscript,
								 @RequestParam(value = "itemFullScore" , defaultValue="") String itemFullScore){
		if("".equals(itemId) || "".equals(competitionId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		competitionService.updateCompetitionItemForOne(new ObjectId(competitionId), new ObjectId(itemId), itemName, itemPostscript, Integer.parseInt(itemFullScore));
		competitionScoreService.updateCompetitionScoreByIds(new ObjectId(competitionId), new ObjectId(itemId),Integer.parseInt(itemFullScore));
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 添加一条评比项目
	 * @param id 
	 * @return
	 */
	@RequestMapping("/addCompetitionItem")  
	@ResponseBody
	public String addCompetitionItem(@RequestParam(value = "competitionId" , defaultValue="") String competitionId){
		if( "".equals(competitionId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		//添加项目
		CompetitionItem item = new CompetitionItem("评比项目", "", 100);
		ObjectId itemId = item.getItemId();
		competitionService.addCompetitionItemforCompetition(new ObjectId(competitionId), item);
		//判断之前是否已经初始化过，若没有则不做任何操作，若已经做过相关初始化工作则新增该项目的所有分数信息
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		ObjectId comId = new ObjectId(competitionId);
		CompetitionEntry ce = competitionService.getCompetitionEntry(comId);
		List<CompetitionBatch> batches = ce.getCompetitionBatches();
		List<ObjectId> gradeIds = ce.getCompetitionRange();
		for(CompetitionBatch batch : batches){
			if(competitionScoreService.queryCompetitionScoresByCompetitionIdAndBatchId(comId, batch.getBatchId()).size() == 0){
				continue;
			}
			for(ObjectId gradeId : gradeIds){
				List<ClassInfoDTO> classDTOs = classService.findClassByGradeId(gradeId.toString());
				if(classDTOs == null){
					continue;
				}
				for(ClassInfoDTO classDTO : classDTOs){
						CompetitionScoreEntry cse = new CompetitionScoreEntry(comId, gradeId, new ObjectId(classDTO.getId()), batch.getBatchId(), itemId, item.getItemFullScore());
						competitionScoreService.addCompetitionScoreEntry(cse);
				}
			}
		}
		
		return JSON.toJSONString(RespObj.SUCCESS); 
	}

	/**
	 * 获取评比项目明细
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/getCompetitionItemDetails")
	@ResponseBody
	public String getCompetitionItemDetails(@RequestParam String itemId){
		List<Map<String, String>> list = competitionItemDetailService.getCompetitionItemDetailsByItemId(new ObjectId(itemId));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("itemDetails", list);
		return JSON.toJSONString(resultMap);
	}


	/**
	 * 添加或修改一组评比项目明细
	 * @param itemId
	 * @param ids
	 * @param itemDetailNames
	 * @return
	 */
	@RequestMapping("/addOrEditCompetitionItemDetails")
	@ResponseBody
	public String addOrEditCompetitionItemDetails(
			@RequestParam(value = "competitionId" , defaultValue="") String competitionId,
			@RequestParam(value = "itemId" , defaultValue="") String itemId,
		    @RequestParam(value = "ids" , required = false) List<String> ids,
		    @RequestParam(value = "itemDetailNames" ,  required = false)List<String> itemDetailNames,
			String detailIdsStr){
		if("".equals(itemId) || "".equals(competitionId)){
			return JSON.toJSONString(RespObj.FAILD);
		}

		List<ObjectId> delDetailIds = new ArrayList<ObjectId>();
		if(!"".equals(detailIdsStr)){
			String[] arrUserIds=detailIdsStr.split(",");
			for(String str:arrUserIds){
				delDetailIds.add(new ObjectId(str));
			}
		}

        List<DBObject> addList=new ArrayList<DBObject>();
		List<CompetitionItemDetailEntry> updList=new ArrayList<CompetitionItemDetailEntry>();
		List<CompetitionItemDetail> addDetailList=new ArrayList<CompetitionItemDetail>();
		Map<String,CompetitionItemDetail> updDetailMap=new HashMap<String, CompetitionItemDetail>();
		Map<String,CompetitionItemDetailEntry> itemDetailEntryMap=competitionItemDetailService.getCompetitionItemDetailMapByItemId(new ObjectId(itemId));
		if(ids!=null) {
			for (int i = 0; i < ids.size(); i++) {
				String itemDetailName = itemDetailNames.get(i);
				if (itemDetailName != null && !"".equals(itemDetailName.trim())) {
					CompetitionItemDetailEntry itemDetailEntry = new CompetitionItemDetailEntry(new ObjectId(competitionId),new ObjectId(itemId), itemDetailName);
					CompetitionItemDetail itemDetail = new CompetitionItemDetail(itemDetailName, "");
					String id = ids.get(i);
					if (id != null && !"".equals(id) && !"new".equals(id)) {
						CompetitionItemDetailEntry entry = itemDetailEntryMap.get(id);
						if (!itemDetailName.equals(entry.getItemDetailName())) {
							itemDetailEntry.setID(new ObjectId(id));
							updList.add(itemDetailEntry);
							itemDetail.setItemDetailId(itemDetailEntry.getID());
							updDetailMap.put(id, itemDetail);
						}

					} else {
						itemDetailEntry.setID(new ObjectId());
						addList.add(itemDetailEntry.getBaseEntry());
						itemDetail.setItemDetailId(itemDetailEntry.getID());
						addDetailList.add(itemDetail);
					}
				}
			}
		}
		competitionItemDetailService.addOrEditCompetitionItemDetails(addList,updList,delDetailIds);

		competitionScoreService.editCompetitionItemDetails(new ObjectId(itemId), addDetailList, updDetailMap, delDetailIds);

		return JSON.toJSONString(RespObj.SUCCESS);
	}

	/**
	 * 删除一条评比项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/delCompetitionItemDetail")
	@ResponseBody
	public String delCompetitionItemDetail(@RequestParam String id){
		try {
			competitionItemDetailService.delCompetitionItemDetailById(new ObjectId(id));
			return JSON.toJSONString(RespObj.SUCCESS);
		} catch(Exception e) {
			return JSON.toJSONString(RespObj.FAILD);
		}
	}

	
	/**
	 * 删除一条评比项目
	 * @param id 
	 * @return
	 */
	@RequestMapping("/delCompetitionItem")  
	@ResponseBody
	public String delCompetitionItem(@RequestParam(value = "competitionId" , defaultValue="") String competitionId,
									 @RequestParam(value = "itemId" , defaultValue="") String itemId){
		if( "".equals(itemId) || "".equals(competitionId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ObjectId id = new ObjectId(competitionId);
		CompetitionItem item = null;
		CompetitionEntry ce = competitionService.getCompetitionEntry(id);
		for(CompetitionItem ci : ce.getCompetitionItems()){
			if(ci.getItemId().toString().equals(itemId)){
				item = ci;
				break;
			}
		}
		if(item == null){
			return JSON.toJSONString(RespObj.FAILD);
		}else{
			competitionService.deleteCompetitionItemForCompetition(id, item);
			//成功删除后，删除所有和该项目的明细信息
			competitionItemDetailService.delCompetitionItemDetailByItemId(item.getItemId());
			//成功删除后，删除所有和该项目的分数信息
			competitionScoreService.deleteAllCompetitionScoresOfCompetitionItem(item.getItemId());
			return JSON.toJSONString(RespObj.SUCCESS); 
		}
	}
	
	/**
	 * 查询一条评比
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryOneCompetition")
	@ResponseBody
	public String queryOneCompetition(@RequestParam(value = "competitionId" , defaultValue="") String competitionId){
		if("".equals(competitionId)){
			return JSON.toJSONString(RespObj.FAILD);  
		}
		List<CompetitionEntry> srcList = new ArrayList<CompetitionEntry>();
		srcList.add(competitionService.getCompetitionEntry(new ObjectId(competitionId)));
		String schoolId = getSessionValue().getSchoolId();
		List<Map<String,Object>> resultList = uploadCompetitionResultList(schoolId,srcList);
		return JSON.toJSONString(resultList.get(0)); 
	}
	
	/**
	 * 根据传进的批次数目，重新生成相应评比的批次数
	 */
	@RequestMapping("/createNewBatches")
	@ResponseBody
	public String createNewBatches(@RequestParam(value = "competitionId" , defaultValue="") String competitionId,
								   @RequestParam(value = "batchNum" , defaultValue="") int batchNum){
		if("".equals(competitionId) || batchNum < 1){
			return JSON.toJSONString(RespObj.FAILD);  
		}
		//清除此次评比的所有分数信息
		competitionScoreService.deleteAllCompetitionScoresOfCompetition(new ObjectId(competitionId));
		List<CompetitionBatch> batches = new ArrayList<CompetitionBatch>();
		for(int i=0;i<batchNum;i++){
			CompetitionBatch cb = new CompetitionBatch("评比批次" + (i+1));
			batches.add(cb);
		}
		competitionService.updateCompetitionBatches(new ObjectId(competitionId), batches); 
		return JSON.toJSONString(RespObj.SUCCESS);
	}

	/**
	 * 更新该评比的批次信息
	 */
	@RequestMapping("/updateBatches")
	@ResponseBody
	public String updateBatches(@RequestParam(value = "competitionId" , defaultValue="") String competitionId,
								   @RequestParam(value = "batchesStr" , defaultValue="") String batchesStr){
		if("".equals(competitionId) || "".equals(batchesStr)){
			return JSON.toJSONString(RespObj.FAILD);  
		}
		List<CompetitionBatch> batchesList = new ArrayList<CompetitionBatch>();
		String[] batches = batchesStr.split(";");
		for(String b : batches){
			CompetitionBatch cb = new CompetitionBatch(b.split(",")[1]);
			ObjectId batchId=new ObjectId(b.split(",")[0]);
			cb.setBatchId(batchId);
			batchesList.add(cb);
		}
		competitionService.updateCompetitionBatches(new ObjectId(competitionId), batchesList);
		return JSON.toJSONString(RespObj.SUCCESS); 
	}
	
	/**
	 * 根据当前登录用户的学校Id进行查询
	 * @param id
	 * @return
	 */
	@RequestMapping("/getCompetitionsForSelection")
	@ResponseBody
	public String getCompetitionsForInputTab(@RequestParam(value = "termType" , defaultValue="2015年第二学期") String termType){
		String schoolId = getSessionValue().getSchoolId();
		List<CompetitionEntry> coms = competitionService.getCompetitionsBySchoolIdAndTermType(new ObjectId(schoolId), termType);
		List<Map<String,Object>> formatList = uploadCompetitionResultList(schoolId,coms);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("competitions", formatList);
		return JSON.toJSONString(resultMap); 
	}
	
	/**
	 * 根据传进的参数，生成（初始化）成绩并将所有查询结果返回给前台
	 * @param id
	 * @return
	 */
	@RequestMapping("/createAndQueryScoreList")
	@ResponseBody
	public String createAndQueryScoreList(@RequestParam(value = "competitionId" , defaultValue="") String competitionId,
										  @RequestParam(value = "competitionBatch" , defaultValue="") String competitionBatch,
										  @RequestParam(value = "gradeId" , defaultValue="ALL") String gradeId,
										  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
										  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
		if("".equals(competitionId) || "".equals(competitionBatch)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ObjectId comId = new ObjectId(competitionId);
		ObjectId batchId = new ObjectId(competitionBatch);
		String schoolId = getSessionValue().getSchoolId();
		List<GradeView> grades = schoolService.findGradeList(schoolId);
		CompetitionEntry ce = competitionService.getCompetitionEntry(new ObjectId(competitionId));
		//首先判断是否初始化，若否则进行初始化相关工作
		List<CompetitionScoreEntry> srcList = competitionScoreService.queryCompetitionScoresByCompetitionIdAndBatchId(new ObjectId(competitionId),new ObjectId(competitionBatch));
		if(srcList.size() == 0){
			createScoreEntries(new ObjectId(competitionId),new ObjectId(competitionBatch));
			srcList = competitionScoreService.queryCompetitionScoresByCompetitionIdAndBatchId(new ObjectId(competitionId),new ObjectId(competitionBatch));
		}
		//组装表头相关信息
		List<CompetitionItem> itemsSrc = ce.getCompetitionItems();
		List<Map<String,String>> items = new ArrayList<Map<String,String>>(); 
		for(CompetitionItem i : itemsSrc){
			Map<String,String> m = new HashMap<String,String>();
			m.put("itemId", i.getItemId().toString());
			m.put("itemName", i.getItemName());
			m.put("itemFullScore", i.getItemFullScore() + "");
			items.add(m);
		}
		//查询相关分数信息，并组装。
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		//List<ObjectId> gradeIds = ce.getCompetitionRange();
		List<ObjectId> gradeIds;
		if("ALL".equals(gradeId)){
			gradeIds = ce.getCompetitionRange();
		}else{
			gradeIds = new ArrayList<ObjectId>();
			gradeIds.add(new ObjectId(gradeId));
		}
		for(ObjectId gId : gradeIds){
			String gradeName = "";
			for(GradeView g : grades){
				if(g.getId().equals(gId.toString())){
					gradeName = g.getName();
				}
			}
			List<ClassInfoDTO> classDTOs = classService.findClassByGradeId(gId.toString());
			if(classDTOs == null){
				continue;
			}
			for(ClassInfoDTO classDTO : classDTOs){
				List<CompetitionScoreEntry> listOfClass = competitionScoreService
						.queryCompetitionScoresByCompetitionIdAndBatchIdAndGradeIdAndClassId(comId, batchId, gId, new ObjectId(classDTO.getId()));
				Map<String,Object> m = new HashMap<String,Object>();
				m.put("gradeName", gradeName);
				m.put("className", classDTO.getClassName());
				List scoreList = new ArrayList();
				List<Integer> itemDetailCountList = new ArrayList<Integer>();
				double allScore = 0L;
				for(CompetitionItem i : itemsSrc){
					for(CompetitionScoreEntry cse : listOfClass){
						if(i.getItemId().toString().equals(cse.getItemId().toString())){
							Map<String,Object> scoreMap = new HashMap<String,Object>();
							scoreMap.put("scoreId",cse.getID().toString());
							scoreMap.put("score",cse.getCompetitionScore() + "");
//							scoreList.add(cse.getCompetitionScore() + "");
							scoreList.add(scoreMap);
							allScore += cse.getCompetitionScore();

							if(cse.getCompetitionItemDetails()!=null&&cse.getCompetitionItemDetails().size()>0){
								itemDetailCountList.add(cse.getCompetitionItemDetails().size());
							}else{
								itemDetailCountList.add(0);
							}
						}
					}
				}
				m.put("allScore", allScore + "");
				m.put("itemScores", scoreList);
				m.put("itemDetailCounts", itemDetailCountList);
				resultList.add(m); 
			}
		}
		List pagedList = getListByPage(resultList,pageNo,pageSize);
		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("total",resultList.size());
		pageMap.put("pageNo",pageNo);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("competitionScroes",pagedList);
		resultMap.put("competitionItems", items);
		resultMap.put("pagejson",pageMap);
		return JSON.toJSONString(resultMap); 
	}
	/**
	 * 根据分页信息进行数据筛选
	 * @param src
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	private List<? extends Object> getListByPage(List<? extends Object> src,int pageNo,int pageSize){
		List<Object> list = new ArrayList<Object>();
		int startIndex = (pageNo - 1) * pageSize;
		int endIndex = (pageNo * pageSize) - 1;
		if(src.size() < pageNo * pageSize){
			endIndex = src.size() - 1;
		}
		for(int i=startIndex;i<endIndex + 1;i++){
			list.add(src.get(i));
		}
		return list;
	}
	/**
	 * 根据评比Id和批次id进行分数初始化
	 * @param competitionId
	 * @param batchhId
	 */
	private void createScoreEntries(ObjectId competitionId,ObjectId batchId){
		CompetitionEntry ce = competitionService.getCompetitionEntry(competitionId);
		List<CompetitionItem> items = ce.getCompetitionItems();
		List<ObjectId> gradeIds = ce.getCompetitionRange();
		Map<String,List<CompetitionItemDetailEntry>> map=competitionItemDetailService.getCompetitionItemDetailsMapByComId(competitionId);
		for(ObjectId gradeId : gradeIds){
			List<ClassInfoDTO> classDTOs = classService.findClassByGradeId(gradeId.toString());
			if(classDTOs == null){
				continue;
			}
			for(ClassInfoDTO classDTO : classDTOs){
				for(CompetitionItem item : items){
					CompetitionScoreEntry cse = new CompetitionScoreEntry(competitionId, gradeId, new ObjectId(classDTO.getId()), batchId, item.getItemId(), item.getItemFullScore());
					List<CompetitionItemDetail> addDetailList=new ArrayList<CompetitionItemDetail>();
					List<CompetitionItemDetailEntry> sublist=map.get(item.getItemId().toString());
					if(sublist!=null){
						for(CompetitionItemDetailEntry entry:sublist){
							CompetitionItemDetail itemDetail=new CompetitionItemDetail(entry.getItemDetailName(), "");
							itemDetail.setItemDetailId(entry.getID());
							addDetailList.add(itemDetail);
						}
					}
					cse.setCompetitionItemDetails(addDetailList);
					competitionScoreService.addCompetitionScoreEntry(cse);
				}
			}
		}
	}
	
	/**
	 * 更新该评比的批次信息
	 */
	@RequestMapping("/updateCompetitionScore")
	@ResponseBody
	public String updateCompetitionScore(@RequestParam(value = "competitionScoreId" , defaultValue="") String competitionScoreId,
								   @RequestParam(value = "score") double score){
		if("".equals(competitionScoreId)){
			return JSON.toJSONString(RespObj.FAILD);  
		}
		competitionScoreService.updateCompetitionScoreById( new ObjectId(competitionScoreId), score);
		return JSON.toJSONString(RespObj.SUCCESS); 
	}


	/**
	 * 查询该评比的批次信息
	 */
	@RequestMapping("/getComScoreItemDetailsInfo")
	@ResponseBody
	public String getComScoreItemDetailsInfo(
			@RequestParam(value = "competitionScoreId" , defaultValue="") String competitionScoreId)
	{
		if("".equals(competitionScoreId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		CompetitionScoreEntry scoreEntry=competitionScoreService.getCompetitionScoreEntry(new ObjectId(competitionScoreId));
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(scoreEntry!=null&&scoreEntry.getCompetitionItemDetails()!=null) {
			for (CompetitionItemDetail detail : scoreEntry.getCompetitionItemDetails()) {
				Map<String, String> mb = new HashMap<String, String>();
				mb.put("id", detail.getItemDetailId().toString());
				mb.put("itemDetailName", detail.getItemDetailName());
				mb.put("itemDetail", detail.getItemDetail());
				list.add(mb);
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("itemDetails", list);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 更新该评比的批次项目评比明细信息
	 */
	@RequestMapping("/updateCompetitionScoreItemDetail")
	@ResponseBody
	public String updateCompetitionScoreItemDetail(
			@RequestParam(value = "competitionScoreId" , defaultValue="") String competitionScoreId,
			@RequestParam(value = "ids" , required = false) List<String> ids,
			@RequestParam(value = "itemDetails" ,  required = false)List<String> itemDetails
			){
		if("".equals(competitionScoreId)){
			return JSON.toJSONString(RespObj.FAILD);
		}

		Map<String,String> updDetailMap=new HashMap<String, String>();
		if(ids!=null) {
			for (int i = 0; i < ids.size(); i++) {
				String itemDetail = itemDetails.get(i);
				if (itemDetail != null && !"".equals(itemDetail.trim())) {
					String id = ids.get(i);
					if (id != null && !"".equals(id)) {
						updDetailMap.put(id, itemDetail);
					}
				}
			}
		}
		competitionScoreService.updateCompetitionScoreItemDetailById(new ObjectId(competitionScoreId), updDetailMap);
		return JSON.toJSONString(RespObj.SUCCESS);
	}

	/**
	 * 根据传进的参数，生成（初始化）成绩并将所有查询结果返回给前台
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryReportCompetitionList")
	@ResponseBody
	public String queryReportCompetitionList(@RequestParam(value = "competitionId" , defaultValue="") String competitionId,
										  @RequestParam(value = "gradeId" , defaultValue="ALL") String gradeId,
											 @RequestParam(value = "batchIds", defaultValue = "") String batchIds,
										  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
										  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
		if("".equals(competitionId) ){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ObjectId comId = new ObjectId(competitionId);
		List<ObjectId> batchIdList=new ArrayList<ObjectId>();
		if(!"".equals(batchIds)){
			List<String> batchIdStrList=Arrays.asList(batchIds.split(","));
			for(String batchId:batchIdStrList){
				batchIdList.add(new ObjectId(batchId));
			}
			Collections.sort(batchIdList);
		}
		CompetitionEntry ce = competitionService.getCompetitionEntryByParam(new ObjectId(competitionId),batchIdList);
		int redFlagNum=ce.getRedFlagNum();
		List<ObjectId> queryGradeIds;
		if("ALL".equals(gradeId)){
			queryGradeIds = ce.getCompetitionRange();
		}else{
			queryGradeIds = new ArrayList<ObjectId>();
			queryGradeIds.add(new ObjectId(gradeId));
		}
		String schoolId = getSessionValue().getSchoolId();
		List<GradeView> grades = schoolService.findGradeList(schoolId);
		
		List<Map<String,Object>> queryList = queryAndUploadDetailList(ce,queryGradeIds,grades);
		List<Map<String,Object>> resultList = sortWithScore(redFlagNum, queryList);
		List<Map<String,String>> batchesList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> itemsList = new ArrayList<Map<String,String>>();
		
		List<CompetitionBatch> batchesSrc = ce.getCompetitionBatches();
		List<CompetitionItem> itemsSrc = ce.getCompetitionItems();
		for(CompetitionBatch batch : batchesSrc){
			Map<String,String> m = new HashMap<String,String>();
			m.put("batchId", batch.getBatchId().toString());
			m.put("batchName", batch.getBatchName());
			batchesList.add(m);
		}
		for(CompetitionItem item : itemsSrc){
			Map<String,String> m = new HashMap<String,String>();
			m.put("itemId", item.getItemId().toString());
			m.put("itemName", item.getItemName() );
			m.put("itemFullScore", item.getItemFullScore() + "");
			itemsList.add(m);
		}
		List pagedList = getListByPage(resultList,pageNo,pageSize);
		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("total",resultList.size());
		pageMap.put("pageNo",pageNo);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("datas",pagedList);
		resultMap.put("batches",batchesList);
		resultMap.put("items",itemsList);
		resultMap.put("pagejson",pageMap);
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 根据总分进行排序
	 * @param srcList
	 * @return
	 */
	private List<Map<String,Object>> sortWithScore(int redFlagNum, List<Map<String,Object>> srcList){
		Comparator<Map<String,Object>> comparator = new Comparator<Map<String,Object>>() {
            @Override
            public int compare(Map<String,Object> m1, Map<String,Object> m2) {
                double s1 = Double.parseDouble(m1.get("averageScore").toString());
                double s2 = Double.parseDouble(m2.get("averageScore").toString());
            	if(s1 > s2){
            		return -1;
            	}
            	if(s1 < s2){
            		return 1;
            	}
            	return 0;
            }
        };
        Collections.sort(srcList, comparator);
		for(int i=0;i<srcList.size();i++){
			srcList.get(i).put("ranking", (i+1) + "");
			Map<String,Object> obj=srcList.get(i);
			if(redFlagNum>0) {
				if (i < redFlagNum) {
					obj.put("redFlag", "1");
				} else {
					Map<String,Object> obj2=srcList.get(redFlagNum-1);
					String as1 = obj.get("averageScore").toString();
					String as2 = obj2.get("averageScore").toString();
					if(as1.equals(as2)){
						obj.put("redFlag", "1");
					}else {
						obj.put("redFlag", "0");
					}
				}
			}else{
				obj.put("redFlag", "0");
			}
		}
		return srcList;
	}
	/**
	 * 根据前台需要组装数据
	 * @param ce
	 * @param gradeIds
	 * @return
	 */
	private List<Map<String,Object>> queryAndUploadDetailList(CompetitionEntry ce,List<ObjectId> gradeIds,List<GradeView> grades){
		Map<String,GradeView> gradeMap=new HashMap<String, GradeView>();
		for(GradeView gradeView : grades){
			gradeMap.put(gradeView.getId().toString(),gradeView);
		}
		DecimalFormat df =new DecimalFormat("###0.###");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(ObjectId gradeId : gradeIds){
			GradeView gradeView = gradeMap.get(gradeId.toString());
			if(gradeView == null){
				continue;
			}
			List<ClassInfoDTO> classDTOs = classService.findClassByGradeId(gradeId.toString());
			if(classDTOs == null){
				continue;
			}
			for(ClassInfoDTO classDTO : classDTOs){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("gradeId",gradeId.toString());
				map.put("gradeName",gradeView.getName());
				map.put("className",classDTO.getClassName());
				double mathAverage = 0L;
				double allScoreForAverage = 0L;
				List<Map<String,Object>> batchesInfoList = new ArrayList<Map<String,Object>>();
				List<CompetitionBatch> batches = ce.getCompetitionBatches();
				for(CompetitionBatch batch : batches){
					if(competitionScoreService.queryCompetitionScoresByCompetitionIdAndBatchId(ce.getID(), batch.getBatchId()).size() == 0){
						createScoreEntries(ce.getID(), batch.getBatchId());
					}
					Map<String,Object> batchMap = new HashMap<String,Object>();
					double batchAllScore = 0L;
					List<String> itemsInfoList = new ArrayList<String>();
					List<List<CompetitionItemDetail>> itemDetailList = new ArrayList<List<CompetitionItemDetail>>();
					List<CompetitionItem> items = ce.getCompetitionItems();
					for(CompetitionItem item : items){
						CompetitionScoreEntry cse = competitionScoreService.getCompetitionScoreEntryByIds(ce.getID(), batch.getBatchId(), new ObjectId(classDTO.getId()), item.getItemId());
						if(cse!=null) {
							batchAllScore += cse.getCompetitionScore();
							itemsInfoList.add(cse.getCompetitionScore() + "");
							if (cse.getCompetitionItemDetails() != null) {
								itemDetailList.add(cse.getCompetitionItemDetails());
							} else {
								itemDetailList.add(new ArrayList<CompetitionItemDetail>());
							}
						}else{
							itemsInfoList.add("0");
							itemDetailList.add(new ArrayList<CompetitionItemDetail>());
						}
					}
					allScoreForAverage += batchAllScore;
					batchMap.put("items", itemsInfoList);
					batchMap.put("itemDetails", itemDetailList);
					batchMap.put("allScore",batchAllScore + "" );
					batchesInfoList.add(batchMap);
				}
				if(batches.size() > 0){
					mathAverage = allScoreForAverage/((double)batches.size());
				}else{
					mathAverage = 0L;
				}
				
				map.put("batches",batchesInfoList);
				map.put("allTotalScore",df.format(allScoreForAverage));
				map.put("averageScore",df.format(mathAverage));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 导出汇总

	 */
	@RequestMapping("/exportReport")
	@ResponseBody
	public String exportReport(@RequestParam(value = "competitionId", defaultValue = "") String competitionId,
								@RequestParam(value = "gradeId", defaultValue = "ALL") String gradeId,
								HttpServletRequest request, HttpServletResponse response)  throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//查询
		if("".equals(competitionId) ){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ObjectId comId = new ObjectId(competitionId);
		CompetitionEntry ce = competitionService.getCompetitionEntry(new ObjectId(competitionId));
		int redFlagNum=ce.getRedFlagNum();
		List<ObjectId> queryGradeIds;
		if("ALL".equals(gradeId)){
			queryGradeIds = ce.getCompetitionRange();
		}else{
			queryGradeIds = new ArrayList<ObjectId>();
			queryGradeIds.add(new ObjectId(gradeId));
		}
		List<GradeView> grades = schoolService.findGradeList(getSessionValue().getSchoolId());
		List<CompetitionBatch> batches = ce.getCompetitionBatches();
		List<CompetitionItem> items = ce.getCompetitionItems();
		List<Map<String,Object>> queryList = queryAndUploadDetailList(ce,queryGradeIds,grades);
		List<Map<String,Object>> resultList = sortWithScore(redFlagNum, queryList);
		
		response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try {
            util = new ExportUtil();
            List<String> datas = new ArrayList<String>();
            datas.add("排名");
            datas.add("年级");
            datas.add("班级");
            datas.add("总分");
			datas.add("平均分");
            for(CompetitionBatch cb : batches){
            	datas.add(cb.getBatchName() + " 分数");
            }
            
            util.addTitle(datas.toArray());
          //获取导出数据
            for (Map<String,Object> m : resultList) {
            	datas.clear();
                datas.add(m.get("ranking").toString());
                for(GradeView g : grades){
                	if(g.getId().equals(m.get("gradeId").toString())){
                		datas.add(g.getName());
                	}
                }
                datas.add(m.get("className").toString());
				datas.add(m.get("allTotalScore").toString());
                datas.add(m.get("averageScore").toString());
                for(Map<String,Object> subMap : (List<Map<String,Object>>)m.get("batches")){
                	datas.add(subMap.get("allScore").toString());
                }
                util.appendRow(datas.toArray());
            }
            util.setFileName(String.format("%s_%s", sdf.format(new Date()), "_综合评比汇总表.xlsx"));
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
		return null;
	}
	
	/**
	 * 导出详细

	 */
	@RequestMapping("/exportDetail")
	@ResponseBody
	public String exportDetail(@RequestParam(value = "competitionId", defaultValue = "") String competitionId,
								@RequestParam(value = "gradeId", defaultValue = "ALL") String gradeId,
								HttpServletRequest request, HttpServletResponse response)  throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//查询
		if("".equals(competitionId) ){
			return JSON.toJSONString(RespObj.FAILD);
		}
		ObjectId comId = new ObjectId(competitionId);
		CompetitionEntry ce = competitionService.getCompetitionEntry(new ObjectId(competitionId));
		int redFlagNum=ce.getRedFlagNum();
		List<ObjectId> queryGradeIds;
		if("ALL".equals(gradeId)){
			queryGradeIds = ce.getCompetitionRange();
		}else{
			queryGradeIds = new ArrayList<ObjectId>();
			queryGradeIds.add(new ObjectId(gradeId));
		}
		List<GradeView> grades = schoolService.findGradeList(getSessionValue().getSchoolId());
		List<CompetitionBatch> batches = ce.getCompetitionBatches();
		List<CompetitionItem> items = ce.getCompetitionItems();
		List<Map<String,Object>> queryList = queryAndUploadDetailList(ce,queryGradeIds,grades);
		List<Map<String,Object>> resultList = sortWithScore(redFlagNum, queryList);
		List<Map<String,Object>> colspanList = new ArrayList<Map<String,Object>>();
		int fromIndex = 5;
		for(CompetitionBatch cb : batches){
			Map<String,Object> col = new HashMap<String,Object>();
			col.put("s", fromIndex + "");
			col.put("e", (fromIndex + items.size() + 1) + "");
			col.put("v", cb.getBatchName());
			colspanList.add(col);
			fromIndex = (fromIndex + items.size() + 1);
		}
		
		response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExportUtil util = null;
        try { 
            util = new ExportUtil(); 
            util.appendColspanRow(colspanList);
            List<String> datas = new ArrayList<String>();
            datas.add("排名");
            datas.add("年级");
            datas.add("班级");
            datas.add("总分");
			datas.add("平均分");
            for(CompetitionBatch cb : batches){
            	for(int i=0;i<items.size() + 1;i++){
            		if(i < items.size()){
            			datas.add(items.get(i).getItemName() + "(" + items.get(i).getItemFullScore() + ")");
            		}else{
            			datas.add("得分");
            		}
            	}
            }
            
            util.addTitle(datas.toArray());
          //获取导出数据
            for (Map<String,Object> m : resultList) {
            	datas.clear();
                datas.add(m.get("ranking").toString());
                for(GradeView g : grades){
                	if(g.getId().equals(m.get("gradeId").toString())){
                		datas.add(g.getName());
                	}
                }
                datas.add(m.get("className").toString());
				datas.add(m.get("allTotalScore").toString());
                datas.add(m.get("averageScore").toString());
                for(Map<String,Object> bMap : (List<Map<String,Object>>)m.get("batches")){
                	for(String scoreStr : (List<String>)bMap.get("items")){
                		datas.add(scoreStr);
                	}
                	datas.add(bMap.get("allScore").toString());
                }
                util.appendRow(datas.toArray());
            }
            util.setFileName(String.format("%s_%s", sdf.format(new Date()), "_综合评比明细表.xlsx"));
            response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, util.getFileName()));
            util.getBook().write(response.getOutputStream());
        } finally {
            if (util != null) {
                util.destroy();
            }
        }
		return null;
	}
	
	
	/**
     * 获取导出文件的名称
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }

	/**
	 *  获取综合评比学期信息
	 */
	@RequestMapping("/termTypes")
	@ResponseBody
	public List<KeyValue> getTermTypes(){
		List<KeyValue> list=new ArrayList<KeyValue>();
		DateTimeUtils time=new DateTimeUtils();
		//取得当前月
		int currMonth=time.getMonth();
		//取得当前年
		int currYear=time.getYear();
		int numb=4;
		for(int year=2016;year<=currYear;year++) {
			String schoolYear;
			if(year==currYear)
			{
				if (currMonth >= 2) {
					KeyValue value=new KeyValue();
					schoolYear = (year - 1) + "-" + year + "学年 第一学期";
					value.setKey(numb++);
					value.setValue(schoolYear);
					list.add(value);
					KeyValue value1=new KeyValue();
					schoolYear = (year - 1) + "-" + year + "学年 第二学期";
					value1.setKey(numb++);
					value1.setValue(schoolYear);
					list.add(value1);
				    if (currMonth >= 9) {
						KeyValue value2=new KeyValue();
						schoolYear = year + "-" + (year + 1) + "学年 第一学期";
						value2.setKey(numb++);
						value2.setValue(schoolYear);
						list.add(value2);
					}
				}
			}
			/*else if(year==2015)
			{
				KeyValue value=new KeyValue();
				schoolYear = (year - 1) + "-" + year + "学年&nbsp;&nbsp;&nbsp;第二学期";
				value.setKey(numb++);
				value.setValue(schoolYear);
				list.add(value);
			}*/
			else
			{
				KeyValue value=new KeyValue();
				schoolYear = (year - 1) + "-" + year + "学年&nbsp;第一学期";
				value.setKey(numb++);
				value.setValue(schoolYear);
				list.add(value);
				KeyValue value1=new KeyValue();
				schoolYear = (year - 1) + "-" + year + "学年&nbsp;第二学期";
				value1.setKey(numb++);
				value1.setValue(schoolYear);
				list.add(value1);
			}
		}
		sortSemesters(list);
		return list;
	}

	/**
	 * 对班级list进行排序
	 * @param list
	 */
	private void sortSemesters(List<KeyValue> list){
		Collections.sort(list, new Comparator<KeyValue>() {
			public int compare(KeyValue obj1 , KeyValue obj2) {
				int flag=obj2.getKey()-obj1.getKey();
				return flag;
			}
		});
	}

}
