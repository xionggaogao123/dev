package com.fulaan.preparation.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.db.comment.CommentDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.comment.service.ComplexCommentService;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.preparation.service.PreparationService;
import com.fulaan.resources.service.CloudResourceService;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.comment.CommentEntry;
import com.pojo.comment.ComplexCommentEntry;
import com.pojo.comment.StarLevel;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.preparation.PreparationEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.RespObj;

/**
 * 集体备课controller
 * 
 * @author cxy 
 * 
 */  
@Controller
@RequestMapping("/preparation")
public class PreparationController extends BaseController{
	@Autowired
	PreparationService preparationService;
	@Autowired
	SchoolService schoolService;
	@Autowired
	ResourceDictionaryService resourceDictionaryService;
	@Autowired
	CloudResourceService resourceService;
	@Autowired
	UserService userService;
	@Autowired
	EducationBureauService educationBureauService;
	@Autowired
	ComplexCommentService complexCommentService;
	
	private SimpleDateFormat publishTimeSDF = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 列表页面跳转
	 */ 
	@SessionNeedless
	@RequestMapping("/listPage")
	@ResponseBody
	public ModelAndView showList() {
		ModelAndView mv = new ModelAndView();
//		mv.addObject("message", JSON.toJSONString(messageMap));
		mv.setViewName("preparation/preparationList");
		
		
		return mv;
	}
	
	/**
	 * 新增页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/addPage")
	@ResponseBody
	public ModelAndView showAdd() {
		ModelAndView mv = new ModelAndView();
//		mv.addObject("message", JSON.toJSONString(messageMap));
		mv.setViewName("preparation/preparationAdd");
		return mv;
	}
	
	/**
	 * 详情页面跳转
	 */
	@SessionNeedless
	@RequestMapping("/detailPage")
	@ResponseBody
	public ModelAndView showDetail(@RequestParam(value="id", defaultValue = "")String id) {
		ModelAndView mv = new ModelAndView();
		String userId = getSessionValue().getId();
		PreparationEntry e = preparationService.getPreparationEntryById(new ObjectId(id));
		UserEntry ue = userService.searchUserId(new ObjectId(userId));
		boolean isMine = e.getPublishUserId().toString().equals(getSessionValue().getId());
		List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
		for(ObjectId fileId : e.getCoursewareList()){
			ResourceEntry re = resourceService.getResourceEntryById(fileId);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("fileName", re.getName());
			m.put("id", re.getID().toString());
			m.put("type", re.getType());
			fileList.add(m);
		}
		List<Map<String,Object>> fileListBack = new ArrayList<Map<String,Object>>();
		for(ObjectId fileId : e.getCoursewareBackList()){
			ResourceEntry re = resourceService.getResourceEntryById(fileId);
			UserEntry fileUploadUserEntry = userService.searchUserId(re.getUserId());
			SchoolEntry sce = schoolService.getSchoolEntry(fileUploadUserEntry.getSchoolID(),Constant.FIELDS);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("fileName", re.getName());
			m.put("id", re.getID().toString());
			m.put("userName",fileUploadUserEntry.getUserName());
			m.put("schoolName",sce.getName());
			m.put("type", re.getType());
			fileListBack.add(m);
		}
		List<Map<String,Object>> commentList = uploadComplexCommentsByTargetId(new ObjectId(id));
		
		
		Map<String, Object> m = new HashMap<String, Object>();
		mv.addObject("headImage",AvatarUtils.getAvatar(userService.searchUserId(e.getPublishUserId()).getAvatar(),2));
		mv.addObject("schoolName", e.getSchoolName());
		mv.addObject("publishUserName", e.getPublishUserName());
		mv.addObject("publishTime", publishTimeSDF.format(new Date(e.getPublishTime())));
		mv.addObject("content", e.getPreparationContent());
		mv.addObject("fileList", JSON.toJSONString(fileList));
		mv.addObject("fileListBack", JSON.toJSONString(fileListBack));
		mv.addObject("commentList", JSON.toJSONString(commentList));
		mv.addObject("commentNum", commentList.size());
		mv.addObject("id", id);
		mv.addObject("isMine", isMine);
//		mv.addObject("message", JSON.toJSONString(m));
		mv.setViewName("preparation/preparationDetail");
		return mv;
	}
	
	/**
	 * 测试新增
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addTest")
	@ResponseBody
	public String addLevel(){ 
		String schoolId = getSessionValue().getSchoolId();
		ObjectId id = new CommentDao().addCommentEntry(new CommentEntry(new ObjectId(), "232", new ObjectId(), "232", 1L, new ObjectId(), "ads", "123213"));
		Map<String,String> m = new HashMap<String,String>();
		m.put("levelId", "12323");
		return JSON.toJSONString(m);
	}
	
	/**
	 * 新增一个集体备课信息
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addPreparation")
	@ResponseBody
	public String addPreparation(@RequestParam(value="preparationName", defaultValue = "")String preparationName,
								 @RequestParam(value="educationTermType", defaultValue = "")String educationTermType,
								 @RequestParam(value="educationSubject", defaultValue = "")String educationSubject,
								 @RequestParam(value="textbookVersion", defaultValue = "")String textbookVersion,
								 @RequestParam(value="educationGrade", defaultValue = "")String educationGrade,
								 @RequestParam(value="chapter", defaultValue = "")String chapter,
								 @RequestParam(value="part", defaultValue = "")String part,
								 @RequestParam(value="preparationCover", defaultValue = "")String preparationCover,
								 @RequestParam(value="startTime", defaultValue = "")String startTime,
								 @RequestParam(value="endTime", defaultValue = "")String endTime,
								 @RequestParam(value="content", defaultValue = "")String content,
								 @RequestParam(value="fileListStr", defaultValue = "")String fileListStr){
		ObjectId userId = new ObjectId(getSessionValue().getId());
		UserEntry ue = userService.searchUserId(userId);
		String userName = ue.getUserName();
		
		int role=getSessionValue().getUserRole();
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId educationBureauId = ebe.getID();
		
		
		String schoolName = schoolService.findSchoolNameByUserId(userId.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//上传的附件列表
		List<ObjectId> fileObjectIdList = new ArrayList<ObjectId>();
		//TODO 这里将所有上传的附件ObjectId放入List中
		if(!("".equals(fileListStr))){
			for(String fileId : fileListStr.split(",")){
				fileObjectIdList.add(new ObjectId(fileId));
			}
		}
		
		String coverUrl = resourceService.getResourceEntryById(new ObjectId(preparationCover)).getImgUrl();
		try {
			preparationService.addPreparationEntry(new PreparationEntry(userId,userName,schoolId,schoolName,preparationName, educationTermType, educationSubject, textbookVersion, 
					educationGrade, chapter, part, new Date().getTime(), coverUrl, sdf.parse(startTime).getTime(), 
					sdf.parse(endTime).getTime(), content, fileObjectIdList , new ArrayList<ObjectId>(),educationBureauId));
		} catch (ParseException e) {
			e.printStackTrace();
			return JSON.toJSONString(RespObj.FAILD);
		}finally{
			resourceService.removeById(new ObjectId(preparationCover));
		}
		
		return JSON.toJSONString(RespObj.SUCCESS);
	}
	
	/**
	 * 根据条件查询列表信息
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/queryList")
	@ResponseBody
	public String queryList(@RequestParam(value="queryId", defaultValue = "")String queryId,
						    @RequestParam(value="columName", defaultValue = "")String columName,
						    @RequestParam(value = "pageNo" , defaultValue="1") int pageNo,
							@RequestParam(value = "pageSize" , defaultValue="10") int pageSize){
		int role=getSessionValue().getUserRole();
		String schoolId=getSessionValue().getSchoolId();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(), role, schoolId);
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法查询!";
			return JSON.toJSONString(respObj);
		}
		List<PreparationEntry> srcList = new ArrayList<PreparationEntry>(); 
		if("".equals(queryId) && "".equals(columName)){
			srcList = preparationService.getAllPreparationEntries(ebe.getID());
		}else{
			srcList = preparationService.getPreparationEntriesByResourceDictionaryId(queryId, columName,ebe.getID());
		}
		List<Map<String,Object>> fomartList = fomartPreparationList(srcList);
		List pagedList = getListByPage(fomartList,pageNo,pageSize);
		
		Map<String,Object> pageMap = new HashMap<String,Object>();
		pageMap.put("total",fomartList.size());
		pageMap.put("pageNo",pageNo); 
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("datas",pagedList);
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
	 * 组装前台要用的list
	 * @param srcList
	 * @return
	 */
	private List<Map<String,Object>> fomartPreparationList(List<PreparationEntry> srcList){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(PreparationEntry e : srcList){
			list.add(uploadPreparationEntryInMap(e));
		}
		return list;
	}
	
	/**
	 * 将一个PreparationEntry组装成一个前台能用的Map
	 * @param e
	 * @return
	 */
	private Map<String,Object> uploadPreparationEntryInMap(PreparationEntry e){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id",e.getID().toString());
		m.put("name",e.getPreparationName());
		m.put("userName",e.getPublishUserName());
		m.put("schoolName",e.getSchoolName());
		m.put("subject",resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(e.getEducationSubject())).getName());
		m.put("version",resourceDictionaryService.getResourceDictionaryEntry(new ObjectId(e.getTextbookVersion())).getName());
		m.put("cover",e.getPreparationCover());
		m.put("publishTime",publishTimeSDF.format(new Date(e.getPublishTime())));
		return m;
	}
	
	
	
	/**
	 * 给一个备课增加备选课件
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addFileBack")
	@ResponseBody
	public String addFileBack(@RequestParam(value="fileId", defaultValue = "")String fileId,
						    @RequestParam(value="targetId", defaultValue = "")String targetId){
		if("".equals(fileId) || "".equals(targetId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		preparationService.addFileBackForPreparation(new ObjectId(targetId), new ObjectId(fileId));
		PreparationEntry e = preparationService.getPreparationEntryById(new ObjectId(targetId));
		List<Map<String,Object>> fileListBack = new ArrayList<Map<String,Object>>();
		for(ObjectId id : e.getCoursewareBackList()){
			ResourceEntry re = resourceService.getResourceEntryById(id);
			UserEntry fileUploadUserEntry = userService.searchUserId(re.getUserId());
			SchoolEntry sce = schoolService.getSchoolEntry(fileUploadUserEntry.getSchoolID(),Constant.FIELDS);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("fileName", re.getName());
			m.put("id", re.getID().toString());
			m.put("userName",fileUploadUserEntry.getUserName());
			m.put("schoolName",sce.getName());
			m.put("type", re.getType());
			fileListBack.add(m);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("fileListBack",fileListBack);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 选用一个备选课件
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addFileFromBack")
	@ResponseBody
	public String addFileFromBack(@RequestParam(value="fileId", defaultValue = "")String fileId,
						    @RequestParam(value="targetId", defaultValue = "")String targetId){
		if("".equals(fileId) || "".equals(targetId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		preparationService.addFileFromBack(new ObjectId(targetId), new ObjectId(fileId));
		
		PreparationEntry e = preparationService.getPreparationEntryById(new ObjectId(targetId));
		
		List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
		for(ObjectId id : e.getCoursewareList()){
			ResourceEntry re = resourceService.getResourceEntryById(id);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("fileName", re.getName());
			m.put("id", re.getID().toString());
			m.put("type", re.getType());
			fileList.add(m);
		}
		
		List<Map<String,Object>> fileListBack = new ArrayList<Map<String,Object>>();
		for(ObjectId id : e.getCoursewareBackList()){
			ResourceEntry re = resourceService.getResourceEntryById(id);
			UserEntry fileUploadUserEntry = userService.searchUserId(re.getUserId());
			SchoolEntry sce = schoolService.getSchoolEntry(fileUploadUserEntry.getSchoolID(),Constant.FIELDS);
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("fileName", re.getName());
			m.put("id", re.getID().toString());
			m.put("userName",fileUploadUserEntry.getUserName());
			m.put("schoolName",sce.getName());
			m.put("type", re.getType());
			fileListBack.add(m);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("fileList",fileList);
		resultMap.put("fileListBack",fileListBack);
		return JSON.toJSONString(resultMap);
	}
	
	
	/**
	 * 新增复杂评论
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/addComplexComment")
	@ResponseBody
	public String addComplexComment(@RequestParam(value="targetId", defaultValue = "")String targetId,
						    		@RequestParam(value="starNums", defaultValue = "")String starNums,
						    		@RequestParam(value = "starNames" , defaultValue="")String starNames,
						    		@RequestParam(value = "advantage" , defaultValue="")String advantage,
						    		@RequestParam(value = "weakPoint" , defaultValue="")String weakPoint,
						    		@RequestParam(value = "parentId" , defaultValue="0")String parentId){
		
		if("".equals(targetId)){
			return JSON.toJSONString(RespObj.FAILD);
		}
		List<StarLevel> starLevelList = new ArrayList<StarLevel>();
		if(!("".equals(starNames))){
			String[] starNameArr = starNames.split(",");
			String[] starNumArr = starNums.split(",");
			for(int i=0;(i<starNameArr.length && i<starNumArr.length);i++){
				StarLevel sl = new StarLevel(starNameArr[i], starNumArr[i]);
				starLevelList.add(sl);
			}
		}
		
		UserEntry ue = userService.searchUserId(new ObjectId(getSessionValue().getId()));
		PreparationEntry e = preparationService.getPreparationEntryById(new ObjectId(targetId));
		if("0".equals(parentId)){
			complexCommentService.addComplexCommentEntry(
					new ComplexCommentEntry(ue.getID(), ue.getUserName(), e.getPublishUserId(), 
							e.getPublishUserName(), new Date().getTime(),new ObjectId(targetId), advantage, weakPoint, starLevelList, parentId));
		}else{
			ComplexCommentEntry parentCce = complexCommentService.getComplexCommentEntry(new ObjectId(parentId));
			complexCommentService.addComplexCommentEntry(
					new ComplexCommentEntry(ue.getID(), ue.getUserName(), parentCce.getCommentUserId(), 
							parentCce.getCommentUserName(), new Date().getTime(),new ObjectId(targetId), advantage, weakPoint, starLevelList, parentId));
		}
		
		
		List<Map<String,Object>> resultList = uploadComplexCommentsByTargetId(new ObjectId(targetId));
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("datas",resultList);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 组装
	 * @param targetId
	 * @return
	 */
	private List<Map<String,Object>> uploadComplexCommentsByTargetId(ObjectId targetId){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<ComplexCommentEntry> rootCommentList = complexCommentService.queryComplexCommentsByTargetId(targetId);
		for(ComplexCommentEntry rce : rootCommentList){
			Map<String,Object> rootCommentMap = new HashMap<String,Object>();
			List<Map<String,Object>> subCommentList = new ArrayList<Map<String,Object>>();
			List<ComplexCommentEntry> subCommentEntryList = complexCommentService.queryComplexCommentsByParentId(rce.getID().toString());
			for(ComplexCommentEntry sce : subCommentEntryList){
				Map<String,Object> subCommentMap = new HashMap<String,Object>();
				subCommentMap.put("id", sce.getID().toString());
				subCommentMap.put("advantage", sce.getAdvantage());
				subCommentMap.put("weakPoint", sce.getWeakPoint());
				subCommentMap.put("commentUserId", sce.getCommentUserId()==null?"":sce.getCommentUserId().toString());
				subCommentMap.put("commentUserName", sce.getCommentUserName());
				subCommentMap.put("replyUserId", sce.getReplyUserId()==null?"":sce.getReplyUserId().toString());
				subCommentMap.put("replyUserName", sce.getReplyUserName());
				subCommentMap.put("parentId", sce.getParentId());
				subCommentMap.put("starLevelList", sce.getStarLevelList());
				subCommentMap.put("timeStamp", publishTimeSDF.format(new Date(sce.getTimestamp())));
				subCommentList.add(subCommentMap);
			}
			rootCommentMap.put("id", rce.getID().toString());
			rootCommentMap.put("advantage", rce.getAdvantage());
			rootCommentMap.put("weakPoint", rce.getWeakPoint());
			rootCommentMap.put("commentUserId", rce.getCommentUserId()==null?"":rce.getCommentUserId().toString());
			rootCommentMap.put("commentUserName", rce.getCommentUserName());
			rootCommentMap.put("replyUserId", rce.getReplyUserId()==null?"":rce.getReplyUserId().toString());
			rootCommentMap.put("replyUserName", rce.getReplyUserName());
			rootCommentMap.put("parentId", rce.getParentId());
			rootCommentMap.put("starLevelList", rce.getStarLevelList());
			rootCommentMap.put("timeStamp", publishTimeSDF.format(new Date(rce.getTimestamp())));
			rootCommentMap.put("headImage", AvatarUtils.getAvatar(userService.searchUserId(rce.getCommentUserId()).getAvatar(),1));
			
			rootCommentMap.put("subCommentList", subCommentList);
			list.add(rootCommentMap);
		}
		
		
		return list;
	}
}
