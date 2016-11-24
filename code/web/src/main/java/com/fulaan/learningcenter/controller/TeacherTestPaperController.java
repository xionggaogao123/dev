package com.fulaan.learningcenter.controller;


import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.learningcenter.service.ExerciseItemService;
import com.fulaan.learningcenter.service.ExerciseService;
import com.fulaan.learningcenter.service.ItemPoolService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.utils.PdfUtils;
import com.mongodb.BasicDBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.exercise.ExerciseEntry;
import com.pojo.exercise.ExerciseItemEntry;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.*;					
import com.pojo.itempool.ItemPoolEntry.ObjectiveItem;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.IdNamePair;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.props.Resources;
import com.sys.utils.HttpFileConvertUtils;
import com.sys.utils.QiniuFileUtils;
import com.sys.utils.RespObj;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

/**
 * 老师组卷
 * @author fourer
 *
 */
@Controller
@RequestMapping("/testpaper")
public class TeacherTestPaperController extends BaseController {

    private static final Logger logger =org.apache.log4j.Logger.getLogger(TeacherTestPaperController.class);
	
    private static final String LOCAL_DOMAIN=Resources.getProperty("local.domain", "http://127.0.0.1:8080");
   
	private ItemPoolService itemPoolService =new ItemPoolService();
	private SchoolService schoolService =new SchoolService();
	private ExerciseService exerciseService =new ExerciseService();
	private ExerciseItemService exerciseItemService =new ExerciseItemService();
	@Autowired
	private ResourceDictionaryService resourceDictionaryService;
	@Autowired
	private EducationBureauService educationBureauService;
	@Autowired
	private ClassService classService;
	
	public static final Set<ObjectId> hideItemPoolIds =new HashSet<ObjectId>();
	public static final Set<ObjectId> hideResourceIds =new HashSet<ObjectId>();
	
	
	static
	{
		try
		{
			String itempoolPath=TeacherTestPaperController.class.getResource("/utils/itempool_hides.txt").getPath();
			String resourcePath=TeacherTestPaperController.class.getResource("/utils/resource_hides.txt").getPath();
		    
			File itempoolFile =new File(itempoolPath);
			File resourceFile =new File(resourcePath);
			
			List<String> itempoolList =FileUtils.readLines(itempoolFile, "utf-8") ;
			List<String> resourceList =FileUtils.readLines(resourceFile, "utf-8") ;
			
			hideItemPoolIds.addAll(MongoUtils.convertToObjectIdList(itempoolList));
			hideResourceIds.addAll(MongoUtils.convertToObjectIdList(resourceList));
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
	}
	
	
	/**
	 * 查找所有学段
	 * @return
	 */
	@RequestMapping("/res")
	@ResponseBody
	public List<IdValuePairDTO> getRes( Integer type)
	{
		List<IdValuePairDTO> retList =new ArrayList<IdValuePairDTO>();
		List<ResourceDictionaryEntry> list=itemPoolService.getResourceDictionaryEntrys(type);
		for(ResourceDictionaryEntry e:list)
		{
			retList.add(new IdValuePairDTO(e.getID(), e.getName()));
		}
		return retList;
	}
	
	
	/**
	 * 查找所有学段
	 * @return
	 */
	@RequestMapping("/itemType")
	@ResponseBody
	public List<IdValuePairDTO> getItemType( @ObjectIdType ObjectId subject)
	{
		List<IdValuePairDTO> retList =new ArrayList<IdValuePairDTO>();
		List<ResourceDictionaryEntry> list=itemPoolService.getItemTypeBySubject(subject);
		for(ResourceDictionaryEntry e:list)
		{
			retList.add(new IdValuePairDTO(e.getID(), e.getName()));
		}
		return retList;
	}
	

	/**
	 * 
	 * @param xd
	 * @param type
	 * @param st 1 题库 2资源
	 * @return
	 */
	@RequestMapping("/subject")
	@ResponseBody
	public List<IdValuePairDTO> getSubject(@ObjectIdType ObjectId xd , @RequestParam(required=false,defaultValue="-1")  int type,@RequestParam(required=false,defaultValue="0")Integer skip ,@RequestParam(required=false,defaultValue="50") Integer limit,@RequestParam(required=false,defaultValue="-1")  int st)
	{
		List<IdValuePairDTO> retList =new ArrayList<IdValuePairDTO>();
		List<ResourceDictionaryEntry> list=itemPoolService.getResourceDictionaryEntrys(xd,type,skip,limit);
		for(ResourceDictionaryEntry e:list)
		{
			if(st==Constant.ONE)
			{
				if(!hideItemPoolIds.contains(e.getID()))
				{
			      retList.add(new IdValuePairDTO(e.getID(), e.getName()));
				}
			}else if(st==Constant.TWO && type==3)
			{
				if(!hideResourceIds.contains(e.getID()))
				{
			      retList.add(new IdValuePairDTO(e.getID(), e.getName()));
				}
			}
			else
			{
				 retList.add(new IdValuePairDTO(e.getID(), e.getName()));
			}
		}
		return retList;
	}
	
	
	/**
	 * 加载老师组卷
	 * @param model
	 * @param
	 * @return
	 * @throws Exception 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/list")
	public String loadTeacher(Map<String, Object> model) throws Exception
	{
		 List<TestPaperDetailDTO> list =itemPoolService.getTestPaperDTOs(getUserId(), -1,-1,-1,new BasicDBObject(), 0, 100);
		 model.put("list", list);
		int userRole = getSessionValue().getUserRole();
		if(UserRole.isHeadmaster(userRole)){
			model.put("userRole", "headMaster");
		} else {
			model.put("userRole", "others");
		}
		 return "itempool/teacher_testpaper";
	}

	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/getTestPaperList")
	@ResponseBody
	public List<TestPaperDetailDTO> getTestPaperlist(@RequestParam(required = false, defaultValue = "0") int type) throws Exception
	{
		ObjectId userId = getUserId();
		if(1 == type){
			userId = new ObjectId(getSessionValue().getSchoolId());
		}
		List<TestPaperDetailDTO> list =itemPoolService.getTestPaperDTOs(userId, -1,-1,-1,new BasicDBObject(), 0, 100);
		return list;
	}
	
	/**
	 * 老师删除组卷
	 * @param id
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("delete")
	@ResponseBody
	public RespObj deletePaper( @ObjectIdType ObjectId id,Map<String, Object> model)
	{
		int role = getSessionValue().getUserRole();
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		TestPaperEntry e=itemPoolService.getTestPaperEntry(id);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!e.getUi().equals(getUserId()) && !UserRole.isHeadmaster(role))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		if(e.getState()==Constant.TWO)
		{
			//obj.setMessage("");
			//return obj;
			logger.error("该考卷已经推送到班级,不能删除");
		}
		itemPoolService.deleteTestPaper(id);
        /*if (DateTimeUtils.lessThanAWeek(e.getTime())) {
            ExpLogType deletequizScore = ExpLogType.DELETEQUIZ;
            if (experienceService.updateScore(e.getUi().toString(), deletequizScore, e.getID().toString())) {
                model.put("scoreMsg", deletequizScore.getDesc());
                model.put("score", deletequizScore.getExp());
            }
        }*/
		logger.info("test paper["+id+"] is deleted");
		return RespObj.SUCCESS;
	}
	
	

	/**
	 * 老师生成试卷
	 * @param name
	 * @param grade
	 * @param subject
	 * @param time
	 * @param ids
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/create")
	public String createPaper(String name,@ObjectIdType ObjectId grade, @ObjectIdType ObjectId subject,Integer time,String ids) throws Exception
	{
		if(StringUtils.isBlank(name))
		{
			throw new Exception("Illegal param for paper name!");
		}
		if(time<0 || time>180)
			throw new Exception("考试时间不超过180分钟");
		
		List<ObjectId> itemIds=MongoUtils.convert(ids);
		
		Map<ObjectId, ItemPoolEntry> itemMap=itemPoolService.getItemPoolEntryMap(itemIds, new BasicDBObject("oty",1) );
		
		List<ObjectId> chList =new ArrayList<ObjectId>();
		List<ObjectId> tfList =new ArrayList<ObjectId>();
		List<ObjectId> gapList =new ArrayList<ObjectId>();
		List<ObjectId> subList =new ArrayList<ObjectId>();
		
		for(Map.Entry<ObjectId, ItemPoolEntry> entry:itemMap.entrySet())
		{
			if(entry.getValue().getOrigType()==ExerciseItemType.SINGLECHOICE.getType() || entry.getValue().getOrigType()==ExerciseItemType.MULTICHOICE.getType())
			{
				chList.add(entry.getKey());
			}
			if(entry.getValue().getOrigType()==ExerciseItemType.TRUE_OR_FALSE.getType() )
			{
				tfList.add(entry.getKey());
			}
			
			if(entry.getValue().getOrigType()==ExerciseItemType.GAP.getType() )
			{
				gapList.add(entry.getKey());
			}
			if(entry.getValue().getOrigType()==ExerciseItemType.SUBJECTIVE.getType() )
			{
				subList.add(entry.getKey());
			}
		}
		
		//SubjectType ty=itemPoolService.getSubjectType(subject);
		TestPaperEntry e =new TestPaperEntry(getUserId(), name, grade, subject, time, Constant.ONE, null, chList, tfList, gapList, subList);
		
		ObjectId id=itemPoolService.addTestPaperEntry(e);
		logger.info("Teacher:"+getUserId()+" add testpaper;id="+id);
		
		//跳转页面
		return "redirect:/testpaper/view.do?pid="+id.toString() + "&type=0";
	}

	/**
	 * 老师自动组卷
	 * @param pto
	 * @return
	 * @throws Exception 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/create1")
	@ResponseBody
	public  RespObj createTestPaper(@ModelAttribute TestPaperPTO pto) throws Exception
	{
		RespObj obj= pto.validate();
		if(obj.getCode().equals(Constant.FAILD_CODE))
		{
			return obj;
		}
		
		List<ObjectId> choiseList =new ArrayList<ObjectId>();
		List<ObjectId> trueFalsesList =new ArrayList<ObjectId>();
		List<ObjectId> gapsList =new ArrayList<ObjectId>();
		List<ObjectId> subjectivesList =new ArrayList<ObjectId>();
		List<ObjectId> kwList=MongoUtils.convert(pto.getKws());
		List<ResourceDictionaryEntry> rdList = resourceDictionaryService.getItemTypeBySubject(kwList);
		if(null != rdList && rdList.size()>0){
			for(ResourceDictionaryEntry entry : rdList){
				kwList.add(entry.getID());
			}
		}

		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}

		Map<ExerciseItemType,List<ObjectId>> itemMap =itemPoolService.getItemMap(eduId, null, null, pto.getLevel(), null, kwList, null, null, Constant.HUNDRED * Constant.FIVETY);
		//选择题
		if(pto.getCh()>0) 
		{                           
			List<ObjectId> list=itemMap.get(ExerciseItemType.SINGLECHOICE);
			if(null!=list)
			{
				for(int i=0;i<pto.getCh();i++)
				{
					if(list.size()>0)
					{
					  int index=RandomUtils.nextInt(list.size());
					  choiseList.add(list.get(index));
					  list.remove(index);
					}
				}
			}
		}
		//判断
		if(pto.getTf()>0) 
		{                                     
			List<ObjectId> list=itemMap.get(ExerciseItemType.TRUE_OR_FALSE);
			if(null!=list)
			{
				for(int i=0;i<pto.getTf();i++)
				{
					if(list.size()>0)
					{
						int index=RandomUtils.nextInt(list.size());
						trueFalsesList.add(list.get(index));
						list.remove(index);
					}
				}
			}
		}
		//填空
		if(pto.getGap()>0) 
		{                                     
			List<ObjectId> list=	itemMap.get(ExerciseItemType.GAP);
			if(null!=list)
			{
				for(int i=0;i<pto.getGap();i++)
				{
					if(list.size()>0)
					{
						int index=RandomUtils.nextInt(list.size());
						gapsList.add(list.get(index));
						list.remove(index);
					}
				}
			}
		}
		//主观
		if(pto.getSub()>0) 
		{                          
			List<ObjectId> list=	itemMap.get(ExerciseItemType.SUBJECTIVE);
			if(null!=list)
			{
				for(int i=0;i<pto.getSub();i++)
				{
					if(list.size()>0)
					{
						int index=RandomUtils.nextInt(list.size());
						subjectivesList.add(list.get(index));
						list.remove(index);
					}
				}
			}
		}
		
		TestPaperEntry tpe =new TestPaperEntry(
				getUserId(),
				pto.getName(),
				new ObjectId(pto.getGrade()), 
				new ObjectId(pto.getSubject()), 
				pto.getTime(), Constant.ONE, null, choiseList, trueFalsesList, gapsList, subjectivesList);
		ObjectId id=itemPoolService.addTestPaperEntry(tpe);
		logger.info("teacher:"+getUserId()+" insert testPaper;Id=" +id.toString());
		obj =new RespObj(Constant.SUCCESS_CODE, id.toString());
		return obj;
	}
	
	/**
	 * 考卷视图
	 * @param
	 * @param pid
	 * @param model
	 * @param tag 仅仅为了生成考试PDF
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/view")
	public String paperView(@ObjectIdType ObjectId pid,Map<String, Object> model,@RequestParam(required=false,defaultValue="0") Integer tag)
			throws Exception {
		
		TestPaperEntry e = itemPoolService.getTestPaperEntry(pid);
		TestPaperDetailDTO dto =new TestPaperDetailDTO(e);
		String schoolName=Constant.EMPTY;
		SchoolEntry se=schoolService.getSchoolEntryByUserId(getUserId());
		if(null!=se)
		{
			schoolName=se.getName();
		}
		dto.setSchoolName(schoolName);
		

		Set<ObjectId> idSet = new HashSet<ObjectId>();
		for (ExerciseItemType type : ExerciseItemType.values()) {
			idSet.addAll(e.getQuestions(type));
		}

		Map<ObjectId, ItemPoolEntry> itemMap = itemPoolService.getItemPoolEntryMap(idSet, Constant.FIELDS);
		// 知识面
		List<ObjectId> scopeList = MongoUtils.getFieldObjectIDs(itemMap.values(), "sc");
			
		
		Map<ObjectId, ResourceDictionaryEntry> kpeMap = itemPoolService.getResourceDictionaryEntryMap(scopeList);
		
		ResourceDictionaryEntry kpe;
		ItemPoolEntry ipe;
		for(ExerciseItemType type:ExerciseItemType.values())
		{
			if(type.equals(ExerciseItemType.MULTICHOICE))
			{
				continue;
			}
			for(ObjectId id:e.getQuestions(type))
			{
				try
				{
					ipe=itemMap.get(id);
					kpe=kpeMap.get(ipe.getScList().get(0));
					dto.getList(type).add(new ItemDTO(ipe, kpe));
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
			}
		}
		model.put("dto", dto);
		
		if(tag==1)
		{
			return "itempool/testpaper_view1";
		}
		return "itempool/testpaper_view";
	}
	/**
	 * 考卷视图,用于生成PDF文件
	 * @param pid
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SessionNeedless
	@RequestMapping("/view1")
	public String paperView1(@ObjectIdType ObjectId pid,Map<String, Object> model,@RequestParam(required=false,defaultValue="0")  int tag)
			throws Exception {
		
		TestPaperEntry e = itemPoolService.getTestPaperEntry(pid);
		TestPaperDetailDTO dto =new TestPaperDetailDTO(e);

		Set<ObjectId> idSet = new HashSet<ObjectId>();
		for (ExerciseItemType type : ExerciseItemType.values()) {
			idSet.addAll(e.getQuestions(type));
		}

		
		
		
		Map<ObjectId, ItemPoolEntry> itemMap = itemPoolService.getItemPoolEntryMap(idSet, Constant.FIELDS);
		// 知识面
		List<ObjectId> scopeList = MongoUtils.getFieldObjectIDs(itemMap.values(), "sc");
		
		Map<ObjectId, ResourceDictionaryEntry> kpeMap =itemPoolService.getResourceDictionaryEntryMap(scopeList);
		
		ResourceDictionaryEntry kpe;
		ItemPoolEntry ipe;
		for(ExerciseItemType type:ExerciseItemType.values())
		{
			if(type.equals(ExerciseItemType.MULTICHOICE))
			{
				continue;
			}
			
			for(ObjectId id:e.getQuestions(type))
			{
				try
				{
					ipe=itemMap.get(id);
					kpe=kpeMap.get(ipe.getScList().get(0));
					dto.getList(type).add(new ItemDTO(ipe, kpe));
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
			}
		}
		model.put("dto", dto);
		if(tag==1) 
		{
			return "itempool/testpaper_view2";
		}
		return "itempool/testpaper_view1";
	}
	/**
	 * 考卷重设
	 * @param pid
	 * @return
	 * @throws IllegalParamException 
	 * @throws PermissionUnallowedException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/reset")
	public String resetPaper(HttpSession session,@ObjectIdType ObjectId pid,Map<String, Object> model)
			throws Exception {
		TestPaperEntry e = itemPoolService.getTestPaperEntry(pid);
		if (null==e) {
			throw new IllegalParamException();
		}
		if(!e.getUi().equals(getUserId()))
		{
			throw new PermissionUnallowedException();
		}
		Set<ObjectId> ids =new HashSet<ObjectId>();
		for(ExerciseItemType type:ExerciseItemType.values())
		{
			model.put(type.getField(), e.getQuestions(type));
			ids.addAll(e.getQuestions(type));
		}
		model.put("itemIds", MongoUtils.convertToStr(ids));
		model.put("paperId", e.getID().toString());
		model.put("grade", e.getGrage());
		model.put("subject", e.getSubject());
		return "itempool/testpaper_reset";
	}
	
	
	/**
	 * 加载考卷推送页面
	 * @param pid
	 * @param model
	 * @param type 0:个人组卷推送 1：校本组卷推送
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push/load")
	public String loadPushPaper(@ObjectIdType ObjectId pid,Map<String, Object> model, @RequestParam(required = false, defaultValue = "0") int type)
			throws Exception {
		TestPaperEntry e = itemPoolService.getTestPaperEntry(pid);
		
		if (null==e ) {
			throw new Exception();
		}

		List<IdValuePairDTO> alreadyList =new ArrayList<IdValuePairDTO>();
		List<IdValuePairDTO> list =new ArrayList<IdValuePairDTO>();
		List<ObjectId> pushedList =e.getClasses();

//		List<TeacherClassSubjectDTO> tcsList=teacherClassSubjectService.getTeacherClassSubjectDTOList(getUserId(),null);
//		Map<ObjectId,IdValuePairDTO> classMap =new HashMap<ObjectId, IdValuePairDTO>();
//		for(TeacherClassSubjectDTO dto:tcsList) {
//			if(null!=dto.getClassInfo().getId()) {
//			  classMap.put(dto.getClassInfo().getId(), dto.getClassInfo());
//			}
//		}
//
//		for(Map.Entry<ObjectId,IdValuePairDTO> entry:classMap.entrySet()) {
//			boolean isPushed=pushedList.contains(entry.getKey());
//			if(isPushed) {
//				alreadyList.add(entry.getValue());
//			} else {
//				list.add(entry.getValue());
//			}
//		}


		List<IdNamePair> idNamePairs = classService.getAllClassByTeacherId(getUserId(), getSchoolId());
		for(IdNamePair idNamePair : idNamePairs){
			boolean isPushed=pushedList.contains(idNamePair.getId());
			IdValuePairDTO idValuePairDTO = new IdValuePairDTO(idNamePair.getId(), idNamePair.getName());
			if(isPushed) {
				alreadyList.add(idValuePairDTO);
			} else {
				list.add(idValuePairDTO);
			}
		}
		model.put("alreadyList", alreadyList);
		model.put("list", list);
		
		model.put("paperId", e.getID().toString());
		model.put("name", e.getName());
		model.put("state", e.getState());
		model.put("type", type);
		
		return "itempool/testpaper_push";
	}
	/**
	 * 老师推送考卷
	 * @param pid
	 * @param cids
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/push")
	@ResponseBody
	public RespObj PushTestpaper( HttpServletRequest req, String pid,final String cids)
			throws Exception {
		Boolean flag = false;//是否推送到校本
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		final TestPaperEntry e = itemPoolService.getTestPaperEntry(new ObjectId(pid));
		if (null==e ) {
			obj.setMessage("参错错误");
			return obj;
		}
		List<ObjectId> objectCids =new ArrayList<ObjectId>();
		String[] cidArray = cids.split(",");
		for(String s : cidArray){
			if(s.equals("xbsj")){
				flag = true;
				itemPoolService.copyTestPaperForUser(new ObjectId(pid), new ObjectId(getSessionValue().getSchoolId()));
			} else {
				objectCids.add(new ObjectId(s));
			}
		}
		if(objectCids.isEmpty()) {
			if(flag){
				return RespObj.SUCCESS;
			}
			obj.setMessage("请选择要推送的班级");
			return obj;
		}
		
		itemPoolService.pushTestPaper(new ObjectId(pid), objectCids);
		/**
		 * 同步到考试
		 */
		// 1.生成试卷
		ObjectId fileObjectId=createPDF(req,e);
		ExerciseEntry exerciseEntry=new ExerciseEntry(Constant.ONE, 
				getUserId(), 
				objectCids,
				e.getName(),
				fileObjectId.toString(),
				fileObjectId.toString(),
				Constant.ZERO,
				Constant.HUNDRED, //默认分数，一会修改
				e.getTime(), 
				System.currentTimeMillis(), 
				null);
		

		ObjectId insertId=exerciseService.uploadTestPaper(exerciseEntry);
		
		
		List<ObjectId> itemIds =new ArrayList<ObjectId>();
		itemIds.addAll(e.getQuestions(ExerciseItemType.SINGLECHOICE));
		itemIds.addAll(e.getQuestions(ExerciseItemType.TRUE_OR_FALSE));
		itemIds.addAll(e.getQuestions(ExerciseItemType.GAP));
		itemIds.addAll(e.getQuestions(ExerciseItemType.SUBJECTIVE));
		
		Map<ObjectId, ItemPoolEntry> itemMap=itemPoolService.getItemPoolEntryMap(itemIds,Constant.FIELDS );
		
		
		List<ExerciseItemEntry> exerciseItemEntryList =new ArrayList<ExerciseItemEntry>();
		int order=Constant.ZERO;//作为答题题号
		ItemPoolEntry itemPoolEntry=null;
		double totalScore=0D;
		for(ExerciseItemType type :ExerciseItemType.values())
		{
			if(type.equals(ExerciseItemType.MULTICHOICE))
			{
				continue;
			}
			itemIds=e.getQuestions(type);
			if(!itemIds.isEmpty())
			{
				order++;
				//生成大题
				ExerciseItemEntry itemEntry =new ExerciseItemEntry(insertId, Constant.EMPTY, String.valueOf(order), Constant.NEGATIVE_ONE, null);
				
				//生成小题
				List<ExerciseItemEntry.Item> itemList =new ArrayList<ExerciseItemEntry.Item>();
				for(int i=0;i<itemIds.size();i++)
				{
					itemPoolEntry=itemMap.get(itemIds.get(i));
					totalScore+=itemPoolEntry.getScore();
					if(null!=itemPoolEntry)
					{
						try
						{
						  itemList.add(createItem(itemPoolEntry,i+1,type));
						}catch(Exception ex)
						{
							logger.error("", ex);
						}
					}
				}
				
				itemEntry.setItemList(itemList);
				
				exerciseItemEntryList.add(itemEntry);
			}
		}
		exerciseItemService.addEntrys(exerciseItemEntryList);
		
		FieldValuePair pair =new FieldValuePair("ts", totalScore);
		exerciseService.update(insertId, pair);
		return RespObj.SUCCESS;
	}

	/**
	 * 将试卷转化为PDF 或者word下载
	 * @param pid
	 * @param req
	 * @param response
	 * @param type 1 pdf 2 word
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/download/pdf")
	@ResponseBody
	public String downTestpaper( @ObjectIdType ObjectId pid,HttpServletRequest req,HttpServletResponse response, @RequestParam(required=false,defaultValue="1")  int type)
			throws Exception {
		try
		{
			final TestPaperEntry e = itemPoolService.getTestPaperEntry(pid);
			if (null==e ) {
				throw new IllegalParamException();
			}
			String path = req.getServletContext().getRealPath("/upload/exam");
			File targetFile=null;
			if(1==type)
			{
				String pdfFileStr =e.getID().toString()+".pdf";
				targetFile = new File(path, pdfFileStr);
				targetFile.createNewFile();
				FileOutputStream fileStream = new FileOutputStream(targetFile);
				PdfUtils pdfUtils =new PdfUtils();
				URL url =new URL(LOCAL_DOMAIN+"/testpaper/view1.do?pid="+e.getID().toString());
				pdfUtils.doConversion(url.toString(), fileStream);
			}
			else
			{
				String pdfFileStr =e.getID().toString()+".doc";
				FileOutputStream fos = null;
			    targetFile = new File(path, pdfFileStr);
				targetFile.createNewFile();
			    {
				    POIFSFileSystem poifs = new POIFSFileSystem();
				    DirectoryEntry directory = poifs.getRoot();
				    String pp=LOCAL_DOMAIN+"/testpaper/view1.do?tag=1&pid="+e.getID().toString();
				    InputStream steam =QiniuFileUtils.downFileByUrl(pp);
				    @SuppressWarnings("unused")
					DocumentEntry documentEntry = directory.createDocument("WordDocument", steam);
				    fos = new FileOutputStream(targetFile);
				    poifs.writeFilesystem(fos);
				    steam.close();
				    fos.close();
			     }
		     }
			response.setCharacterEncoding("utf-8");
	        response.setContentType("multipart/form-data");
	        response.setHeader( "Content-Disposition", "attachment;filename=" + new String( targetFile.getName().getBytes("utf-8"), "ISO8859-1" ) );;
	        
	        try {
	            InputStream inputStream = new FileInputStream(targetFile);
	            OutputStream os = response.getOutputStream();
	            byte[] b = new byte[2048];
	            int length;
	            while ((length = inputStream.read(b)) > 0) {
	                os.write(b, 0, length);
	            }
	            os.close();
	            inputStream.close();
	        }  catch (IOException ex) {
	        	
	        }
		}catch(Exception ex)
		{
			
		}
		return null;
	}
	

	private ExerciseItemEntry.Item createItem(ItemPoolEntry itemPoolEntry,int i,ExerciseItemType type)
	{
		if(type==ExerciseItemType.SINGLECHOICE)
		{
			ObjectiveItem objectiveItem=itemPoolEntry.getItem();
			return new ExerciseItemEntry.Item(String.valueOf(i), type.getType(), itemPoolEntry.getScore(), itemPoolEntry.getAnswer(), objectiveItem.getSelectCount(), null);
		}
		return new ExerciseItemEntry.Item(String.valueOf(i), type.getType(), itemPoolEntry.getScore(), itemPoolEntry.getAnswer(), -1, null);
	}
	
	/**
	 * 根据TestPaperEntry生成pdf和swf
	 * @param req
	 * @param e
	 * @return
	 * @throws Exception
	 */
	private ObjectId createPDF(HttpServletRequest req,TestPaperEntry e) throws Exception
	{
		ObjectId id =new ObjectId();
		logger.info("teacher push TestPaperEntry to exam; TestPaperEntry.id="+e.getID().toString()+",and file id="+id.toString());
		String path = req.getServletContext().getRealPath("/upload/exam");
		String pdfFileStr =id.toString()+".pdf";
		
		File targetFile = new File(path, pdfFileStr);
		targetFile.createNewFile();
		FileOutputStream fileStream = new FileOutputStream(targetFile);
		PdfUtils pdfUtils =new PdfUtils();
		//pdfUtils.doConversion("http://www.k6kt.com/testpaper/view1.do?pid=557e82c70cf2341c025df897", fileStream);
		URL url =new URL(LOCAL_DOMAIN+ "/testpaper/view1.do?pid="+e.getID().toString());
	
		pdfUtils.doConversion(url.toString(), fileStream);

		createSwfFile(id, path, targetFile);
		return id;
	}


	private void createSwfFile(final ObjectId id, final String path, final File targetFile)
			throws IOException {
		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
			    	 String swfFileStr=id.toString()+".swf";
				     File swfFile = new File(path, swfFileStr);
				     HttpFileConvertUtils.convertPdfToSwf(targetFile, swfFile.getAbsolutePath());
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
			}
		});
		t.start();
	}
	
	
	/**
	 * 老师删除题目
	 * @param pid 
	 * @param type
	 * @param tid
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/item/delete")
	@ResponseBody
	public RespObj deleteItem(@ObjectIdType  ObjectId pid,int type,@ObjectIdType  ObjectId tid)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		TestPaperEntry e=itemPoolService.getTestPaperEntry(pid);
		if(!e.getUi().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		
		ExerciseItemType eType=ExerciseItemType.getExerciseItemType(type);
		if(null==eType)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		itemPoolService.deleteTestPaperItemById(pid, eType, tid);
		return RespObj.SUCCESS;
	}
	/**
	 * 老师重设试卷，添加一个题目
	 * @param pid 
	 * @param type
	 * @param tid
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/item/add")
	@ResponseBody
	public RespObj addItem(@ObjectIdType  ObjectId pid,int type,@ObjectIdType  ObjectId tid)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		TestPaperEntry e=itemPoolService.getTestPaperEntry(pid);
		if(!e.getUi().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		ExerciseItemType eType=ExerciseItemType.getExerciseItemType(type);
		if(null==eType)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		itemPoolService.addItemToTestPaper(pid, eType, tid);
		return RespObj.SUCCESS;
	}
	
	/**
	 * 调整题目位置
	 * @param pid
	 * @param type
	 * @param tid
	 * @param pos 1 向上 0向下
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/item/pos/update")
	@ResponseBody
	public RespObj changeItemPos(@ObjectIdType  ObjectId pid,int type,@ObjectIdType ObjectId tid,int pos) throws Exception
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		TestPaperEntry e=itemPoolService.getTestPaperEntry(pid);
		if(!e.getUi().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		
		ExerciseItemType eType=ExerciseItemType.getExerciseItemType(type);
		if(null==eType)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		List<ObjectId> list=e.getQuestions(eType);
		
		int index=list.indexOf(tid);
		if(index<0)
			throw new Exception();
		
		
		if(pos==Constant.ONE && index!=0) //向上
		{
			list.remove(index);
			list.add(index-1, tid);
			
			itemPoolService.updateItems(pid , eType, list);
		}
		
		if(pos==Constant.ZERO && index!=list.size()-1) //向下
		{
			list.remove(index);
			list.add(index+1, tid);
			itemPoolService.updateItems(pid, eType, list);
		}
		return RespObj.SUCCESS; 
	}
}
