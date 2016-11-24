package com.fulaan.learningcenter.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.learningcenter.service.ItemPoolService;
import com.fulaan.resources.service.ResourceDictionaryService;
import com.fulaan.user.service.UserService;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.IdValuePairSortDTO;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.exercise.ExerciseItemType;
import com.pojo.itempool.*;
import com.pojo.resources.ResourceDictionaryEntry;
import com.pojo.school.SubjectType;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.*;

/**
 * 题库controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/itempool")
public class ItemPoolController extends BaseController {

	private static final Logger logger =org.apache.log4j.Logger.getLogger(ItemPoolController.class);

	private ItemPoolService itemPoolService =new ItemPoolService() ;
    private UserService userService =new UserService();
	@Autowired
	private ResourceDictionaryService resourceDictionaryService;
	@Autowired
	private ExperienceService experienceService;
	@Autowired
	private EducationBureauService educationBureauService;

	/**
	 * 题库前端列表界面跳转
	 * @return
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/frontList")
	public String frontList(@RequestParam(value = "flag", defaultValue = "tcv") String flag){
		return "itempoolguanli/itempoolMge";
	}

	/**
	 * 查询已入库问题信息
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/queryItemList")
	@ResponseBody
	public String queryItemList(
			@RequestParam(value = "searchName", defaultValue = "") String searchName,
			@RequestParam(value = "typeId", defaultValue = "") String typeId,
			@RequestParam(value = "propertyType", defaultValue = "tcv") String propertyType,
			@RequestParam(value = "level", defaultValue = "0")int level,
			@RequestParam(value = "isSaved", defaultValue = "1") int isSaved,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "questionTopic", defaultValue = "0")int questionTopic) {
		try {
			ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
			int role=getSessionValue().getUserRole();
			EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
			if (ebe == null) {
				RespObj respObj = new RespObj(Constant.FAILD_CODE);
				respObj.message = "该用户未加入任何教育局，无法查询资源!";
				return JSON.toJSONString(respObj);
			}
			List<ItemDTO> srcList=itemPoolService.getItemPoolEntryListByParam(ebe.getID(), searchName,isSaved,typeId,propertyType,pageNo,pageSize,questionTopic,level);
			int total=itemPoolService.getItemPoolEntryCountByParam(ebe.getID(), searchName,isSaved,typeId,propertyType,questionTopic,level);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> pageMap = new HashMap<String, Object>();
			pageMap.put("cur", pageNo);
			pageMap.put("total", total);
			resultMap.put("pagejson", pageMap);
			resultMap.put("datas", srcList);
			return JSON.toJSONString(resultMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查找题目的详细信息，包括全部的资源字典表路径
	 * @param id
	 * @return
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/queryOneItem")
	@ResponseBody
	public Map<String, Object> queryOneItem(@ObjectIdType ObjectId id){
		Map<String, Object> model = new HashMap<String, Object>();
		ItemDTO itemDTO = itemPoolService.getItemDTODetail(id);
		model.put("itemDTO",itemDTO);
		return model;
	}

	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/updateStatus1")
	@ResponseBody
	public RespObj updateStatus1(@RequestParam(value = "id", defaultValue = "") String id) throws IllegalParamException{
		itemPoolService.updateStatus(new ObjectId(id), 1);
		return RespObj.SUCCESS;

	}

	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/updateStatus2")
	@ResponseBody
	public RespObj updateStatus2(@RequestParam(value = "id", defaultValue = "") String id) throws IllegalParamException{
		itemPoolService.updateStatus(new ObjectId(id), 2);
		return RespObj.SUCCESS;

	}

	/**
	 * 题库新增页面跳转
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/addItem")
	public String addItem() {
		return "itempoolguanli/addItem";
	}

	@RequestMapping("/saveItem")
	@ResponseBody
	public RespObj saveItem(@ObjectIdType ObjectId stage,
							@ObjectIdType ObjectId subject,
							int itemType,
							int level,
							double score,
							String book,
							String knowledge,
							String question,
							String answer,
							String parse,
							@RequestParam(value = "zc", defaultValue = "0")int zc,
							@RequestParam(value = "sc", defaultValue = "0")int sc){
		RespObj respObj = RespObj.FAILD;
		try{
			ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
			int role=getSessionValue().getUserRole();
			EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
			if (ebe == null) {
				respObj.message = "该用户未加入任何教育局，无法添加资源!";
				return respObj;
			}
			int isSaved = 1;
			question = question.replaceAll("</?p>", "");
			answer = answer.replaceAll("</?p>", "");
			parse = parse.replaceAll("</?p>", "");
			ObjectId retId = itemPoolService.save(ebe.getID(), getUserId(), isSaved, stage, subject, itemType, level, score, book, knowledge, question, answer, parse, getUserId(), zc, sc);
			respObj = RespObj.SUCCESS;
			respObj.setMessage(retId.toString());
		} catch (Exception e){
			e.printStackTrace();
		}

		return respObj;
	}

	/**
	 * 题库新增页面跳转
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/editItem")
	public String editItem(Map<String, Object> model,
						   @RequestParam(value = "id", defaultValue = "") String id) {
		itemPoolService.getItemDTODetail(new ObjectId(id),model);
		return "itempoolguanli/editItem";
	}

	/**
	 * 更新一个item
	 */
	@SessionNeedless
	@RequestMapping("/updateItem")
	@ResponseBody
	public String updateCourseware(
			@RequestParam(value = "id", defaultValue = "") String id,
			@ObjectIdType ObjectId stage,
			@ObjectIdType ObjectId subject,
			int itemType,
			int level,
			double score,
			String book,
			String knowledge,
			String question,
			String answer,
			String parse,
			@RequestParam(value = "zc", defaultValue = "0")int zc,
			@RequestParam(value = "sc", defaultValue = "0")int sc) {

		RespObj respObj = RespObj.FAILD;
		try{
			ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
			int role=getSessionValue().getUserRole();
			EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
			if (ebe == null) {
				respObj.message = "该用户未加入任何教育局，无法编辑资源!";
				return JSON.toJSONString(respObj);
			}
			int isSaved = 1;
			question = question.replaceAll("</?p>", "");
			answer = answer.replaceAll("</?p>", "");
			parse = parse.replaceAll("</?p>", "");
			itemPoolService.updateItem(new ObjectId(id), stage, subject, itemType, level, score, book, knowledge, question, answer, parse, zc, sc);
			respObj = RespObj.SUCCESS;
			respObj.setMessage(id);
		} catch (Exception e){
			e.printStackTrace();
		}
		return JSON.toJSONString(RespObj.SUCCESS);

	}
	/**
	 * 学生加载我的练习
	 * @param model
	 * @return
	 */
	@UserRoles({UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/student/exercise")
	public String loadStudent(Map<String, Object> model)
	{
		List<ObjectId> userIds =new ArrayList<ObjectId>();
		ObjectId ui=getUserId();
		userIds.add(ui);
		
		UserEntry ue=userService.searchUserId(getUserId());
		userIds.addAll(ue.getConnectIds());
		
		List<StudeneExerciseDTO> list =itemPoolService.getStudeneExerciseDTOs(userIds, -1, Constant.ZERO, Constant.HUNDRED);
		model.put("list", list);
		return "itempool/student_exercise";
	}
	
	
	/**
	 * 加载题库
	 * @return
	 */
	@RequestMapping("/load")
	public String loadItemPool()
	{
		 return "itempool/itempool";
	}

	
	/**
	 * 加载错题本
	 * @return
	 */
	@RequestMapping("/errorbook")
	public String errorbook()
	{
		 return "itempool/errorbook";
	}

	/**
	 * 教师题库首页
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/teacher")
	public String teacherItempool(Map<String, Object> model){
		int userRole = getSessionValue().getUserRole();
		if(UserRole.isHeadmaster(userRole)){
			model.put("userRole", "headMaster");
		} else {
			model.put("userRole", "others");
		}
		return "/itempool/teacherIP";
	}

	/**
	 * 批量上传题目后编辑
	 * @return
	 */
	@RequestMapping("/editMulItem")
	public String editMulItem(String ids, Map<String, Object> model){
		model.put("ids", ids);
		return "/itempool/editMulItem";
	}
	
	
	/**
	 * 学生练习详情
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping("student/exercise/detail")
	@ResponseBody
	public StudeneExerciseDTO getStudeneExerciseDTO(String id)
	{
		StudentExerciseEntry e =itemPoolService.getStudentExerciseEntry(new ObjectId(id));
		SubjectType type=SubjectType.getSubjectType(e.getSubjectType());
		return new StudeneExerciseDTO(e,type.getName());
	}
	
	/**
	 * 得到学生练习列表
	 * @param state
	 * @param skip
	 * @param limit
	 * @return
	 */
	@RequestMapping("student/exercise/list")
	@ResponseBody
	public List<StudeneExerciseDTO> getStudeneExerciseDTOs(int state,int skip,int limit)
	{
		List<StudeneExerciseDTO> list =itemPoolService.getStudeneExerciseDTOs(getUserId(), state, skip, limit);
		return list;
	}

	/**
	 * 删除学生练习
	 * @param id 试卷ID
	 * @return
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping("student/delete")
	@ResponseBody
	public RespObj deleteExercise(@ObjectIdType ObjectId id)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		StudentExerciseEntry e=itemPoolService.getStudentExerciseEntry(id);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!e.getUserId().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		itemPoolService.deleteStudentExercise(id);
		return RespObj.SUCCESS;
	}


	/**
	 * 根据科目查询知识点；知识点的第一级和第二级
	 * @param subject
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("kw/list")
	@ResponseBody
	public List<KnowledgePointDTO> getKnowledgePointDTOs(@ObjectIdType(isRequire=false)  ObjectId subject, @RequestParam(required=false,defaultValue="0")  Integer type) throws Exception
	{
		/**
		 * 只构建2层知识点
		 */
		if(type==Constant.ONE)
		{
			
			Map<ObjectId,KnowledgePointDTO> map =new HashMap<ObjectId, KnowledgePointDTO>();
			List<ResourceDictionaryEntry> firstLevelRDEList=itemPoolService.getResourceDictionaryEntrys(subject,Constant.SEVEN);
			for(ResourceDictionaryEntry e:firstLevelRDEList)
			{
				map.put(e.getID(), new KnowledgePointDTO(e));
			}
			Map<ObjectId,ResourceDictionaryEntry> secondLevelRDEMap =itemPoolService.getResourceDictionaryEntryMapByParents(map.keySet(),-1);
			
			ObjectId id=null;
			for(Map.Entry<ObjectId,ResourceDictionaryEntry> entry:secondLevelRDEMap.entrySet())
			{
				id=entry.getValue().getParentId();
				if(map.containsKey(id))
				{
					map.get(id).addDTO(new IdValuePairDTO(entry.getKey(), entry.getValue().getName()));
				}
			}
			return new ArrayList<KnowledgePointDTO>(map.values());
		}
		
		return buildResTreeByParentId(subject, Constant.SEVEN);
	}
	
	
	
	/**
	 * 得到教材章节
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/bs/list")
	@ResponseBody
	public List<KnowledgePointDTO> getBooKSelectionDTOs( @ObjectIdType ObjectId eType) throws Exception
	{
        return buildResTreeByParentId(eType, Constant.FOUR);
	}
	
	
	/**
	 * 知识点详情
	 * @param id
	 * @return
	 */
	@RequestMapping("kw/detail")
	@ResponseBody
	public KnowledgePointDTO knowledgePointDTODetail(@ObjectIdType ObjectId id)
	{
		//KnowledgePointDTO dto = itemPoolService.getKnowledgePointDTO(id);
		//return dto;
		return null;
	}
	
	/**
	 * 根据难易程度和知识点取得题目多少
	 * @param kws
	 * @param level
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/count")
	@ResponseBody
	public List<IdNameValuePairDTO> count(String kws,int level) throws Exception
	{
	
		Set<ObjectId> ids =new HashSet<ObjectId>();
		if(StringUtils.isNotBlank(kws))
		{
			String[] arr=StringUtils.split(kws, Constant.COMMA);
			for(String id:arr)
			{
				if(null!=id && ObjectId.isValid(id))
					ids.add(new ObjectId(id));
			}
		}
		
		List<ResourceDictionaryEntry> list=itemPoolService.getResourceDictionaryEntrysByParents(ids, Constant.NINE);
	
		return itemPoolService.getcount(MongoUtils.getFieldObjectIDs(list, Constant.ID), level);
	}
	
	/**
	 * 得到学生每个科目错题统计情况
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/subject/count")
	@ResponseBody
	public List<IdNameValuePairDTO> subjectErrorCount() throws Exception
	{
		return itemPoolService.getStudentSubjectErrorItemcount(getUserId());
	}
	
	
	/**
	 * 学生错题面集合
	 * @param sub 科目
	 * @return
	 */
	@RequestMapping("/erritem/scope")
	@ResponseBody
	public List<IdValuePairDTO> getErrorItemScope(int sub)
	{
		 return itemPoolService.getErrorItemScope(getUserId(), sub);
	}
	
	
	/**
	 * 获取ItemDTO
	 * @param order 排序 1:乱序 2最新错题 3错次最多
	 * @param itemId 当前题目ID
	 * @param direction 0 上一题 1 下一题 ；默认-1
	 * @param subject 科目
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/erritem/detail")
	@ResponseBody
	public ItemDTO getItemDTO(int order,String itemId,@RequestParam(defaultValue="-1") int direction,int subject) throws Exception
	{
		StudentErrorItemEntry.Item item=null;
		Integer count=0;
		if(null==itemId)
		{
			//取总数
			IdNameValuePair idvp=itemPoolService.getSubject(getUserId(), subject);
			if(null!=idvp)
			{
				count=(Integer)idvp.getValue();
			}
			item=itemPoolService.getItem(getUserId(),order,subject,Constant.ZERO,Constant.TEN);
		}
		else
		{
			item=itemPoolService.getItem(getUserId(), new ObjectId(itemId), order, direction);
			if(null==item)
			{
			
				String errorDatakey=	MessageFormat.format(CacheHandler.CACHE_ERRORITEM_BEGIN, getUserId().toString(),  String.valueOf(order));
				String errorDataBegin=CacheHandler.getStringValue(errorDatakey);
			
				if(direction!=Constant.ONE && direction!=-1)
				{
					throw new Exception();
				}
				int beginData=Integer.parseInt(errorDataBegin);
				if(direction==Constant.ONE)
				{
					beginData=beginData+Constant.TEN;
				}
				if(direction==-1)
				{
					beginData=beginData-Constant.TEN;
					beginData=beginData<Constant.ZERO?Constant.ZERO:beginData;
				}
				item=itemPoolService.getItem(getUserId(),order,subject,beginData,Constant.TEN);
			}
		}
		
		//表示已经没有数据可供查询
		if(null==item)
		{
			item=itemPoolService.getErrorItem(getUserId(),new ObjectId(itemId));
		}
		
		ItemDTO dto= itemPoolService.getItemDTO(item);
		if(null==itemId)
		{
			dto.setTotalCount(count);
		}
		return dto;
	}

	/**
	 * 学生创建练习
	 * @param name
	 * @param level
	 * @param subject
	 * @param kws
	 * @param ch
	 * @param tf
	 * @param gap
	 * @param sub
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles({UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/student/exer/create")
	@ResponseBody
	public  RespObj createExercise( @RequestParam(defaultValue="") String name,Integer level,  @ObjectIdType  ObjectId subject,String kws,Integer ch,Integer tf,Integer gap, Integer sub) throws Exception
	{
		RespObj obj= new RespObj(Constant.FAILD_CODE);
		
		if(StringUtils.isBlank(name) ||name.length()>10 )
		{
			obj.setMessage("练习名字不为空并且不超过10个字");
			return obj;
		}
		
		
		
		SubjectType subjectType=itemPoolService.getSubjectType(subject);
		
		
		
		if(null==subjectType)
		{
			obj.setMessage("科目错误");
			return obj;
		}
		
		int total=ch+tf+gap+sub;
		if( total>Constant.HUNDRED )
		{
			obj.setMessage("总题目数应该小于100");
			return obj;
		}
		
		List<ObjectId> kwObjectIdList =MongoUtils.convert(kws);
		List<ResourceDictionaryEntry> rdList = resourceDictionaryService.getItemTypeBySubject(kwObjectIdList);
		if(null != rdList && rdList.size()>0){
			for(ResourceDictionaryEntry entry : rdList){
				kwObjectIdList.add(entry.getID());
			}
		}
		List<IdNameValuePair> choiseList =new ArrayList<IdNameValuePair>(ch);
		List<IdNameValuePair> trueFalsesList =new ArrayList<IdNameValuePair>(tf);
		List<IdNameValuePair> gapsList =new ArrayList<IdNameValuePair>(gap);
		List<IdNameValuePair> subjectivesList =new ArrayList<IdNameValuePair>(sub);



		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}
		
		
		Map<ExerciseItemType,List<ObjectId>> itemMap =itemPoolService.getItemMap(eduId,null,null, level, null,kwObjectIdList,null,null, Constant.HUNDRED*Constant.FIVETY);
		//选择题
		if(ch>0) 
		{                           
			List<ObjectId> list=itemMap.get(ExerciseItemType.SINGLECHOICE);
			
			if(null!=list)
			{
				for(int i=0;i<ch;i++)
				{
					if(list.size()>0)
					{
					  int index=RandomUtils.nextInt(list.size());
					  choiseList.add( new IdNameValuePair(list.get(index)));
					  list.remove(index);
					}
				}
			}
		}
		//判断
		if(tf>0) 
		{                                     
			List<ObjectId> list=itemMap.get(ExerciseItemType.TRUE_OR_FALSE);
			if(null!=list)
			{
				for(int i=0;i<tf;i++)
				{
					if(list.size()>0)
					{
						int index=RandomUtils.nextInt(list.size());
						trueFalsesList.add(new IdNameValuePair(list.get(index)));
						list.remove(index);
					}
				}
			}
		}
		//填空
		if(gap>0) 
		{                                     
			List<ObjectId> list=	itemMap.get(ExerciseItemType.GAP);
			if(null!=list)
			{
				for(int i=0;i<gap;i++)
				{
					if(list.size()>0)
					{
						int index=RandomUtils.nextInt(list.size());
						gapsList.add(new IdNameValuePair(list.get(index)));
						list.remove(index);
					}
				}
			}
		}
		//主观
		if(sub>0) 
		{                          
			List<ObjectId> list=itemMap.get(ExerciseItemType.SUBJECTIVE);
			if(null!=list)
			{
				for(int i=0;i<sub;i++)
				{
					if(list.size()>0)
					{
						int index=RandomUtils.nextInt(list.size());
						subjectivesList.add(new IdNameValuePair(list.get(index)));
						list.remove(index);
					}
				}
			}
		}
		StudentExerciseEntry e=new StudentExerciseEntry(
				getUserId(), 
				name, 
				subjectType.getId(),
				choiseList,
				trueFalsesList,
                gapsList,
                subjectivesList,
                kwObjectIdList,
                -1, 
                level);
		ObjectId insertId=itemPoolService.addStudentExerciseEntry(e);
		logger.info("student["+getUserId()+"] create Exercise;the insert id="+insertId.toString());
		return RespObj.SUCCESS;
	}
	
	/**
	 * 学生开始答题
	 * @param exeid
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping("/student/exer/begin")
	@ResponseBody
	public ItemDTO beginExercise(@ObjectIdType ObjectId exeid) throws Exception
	{
		StudentExerciseEntry e=itemPoolService.getStudentExerciseEntry(exeid);
//		if(null==e || !e.getUserId().equals(getUserId()))
//		{
//			UserEntry ce=userService.searchUserId(getUserId());
//			if(!e.getUserId().equals(ce.getConnectIds()))
//			{
//			  throw new Exception();
//			}
//		}
		List<ObjectId> itemList =getItemIdList(e);
		return getItemDTO(itemList.get(Constant.ZERO), e);
	}

	/**
	 * 得到练习题
	 * @param exeid
	 * @param itemId 肯定不为null
	 * @param direction 0 上一条 1下一条
	 * @param answer 我的答案
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/student/exercise/item")
	@ResponseBody
	public ItemDTO getItemDTO( @ObjectIdType ObjectId  exeid, @ObjectIdType ObjectId   itemId,int direction,@RequestParam(defaultValue="") String answer) throws Exception
	{
		StudentExerciseEntry e=itemPoolService.getStudentExerciseEntry(exeid);
		if(null==e )
		{
			throw new Exception();
		}
		try
		{
			if(StringUtils.isNotBlank(answer))
			{
		      submitOneItem(e,  itemId , answer,getUserId());
			}
		}catch(Exception ex)
		{
			throw new Exception();
		}
		ObjectId calItemId=getItemId(e, itemId, direction);
		if(direction==Constant.ONE && calItemId.equals(itemId))
		{
			return null;
		}
		return getItemDTO(calItemId, e);
	}


	
	/**
	 * 学生提交练习
	 * @param exeid
	 * @param itemId
	 * @param answer
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/student/exer/submit")
	@ResponseBody
	public RespObj updateExerciseAnswer( @ObjectIdType ObjectId exeid,@ObjectIdType ObjectId itemId,@RequestParam(defaultValue="") String answer) throws Exception
	{
		StudentExerciseEntry e=itemPoolService.getStudentExerciseEntry(exeid);
		if(null==e )
		{
			throw new Exception();
		}
		try
		{
		    submitOneItem(e, itemId, answer,getUserId());
			ExpLogType expLogType = ExpLogType.FINISHED_PRACTICE;
			experienceService.updateNoRepeat(getUserId().toString(), expLogType,exeid.toString());

		}catch(Exception ex)
		{
			throw new Exception();
		}
		return RespObj.SUCCESS;
	}
	
	
	/**
	 * 学生提交之后查看答案
	 * @param exeid
	 * @param itemId
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/student/exercise/view/answer")
	@ResponseBody
	public ItemDTO getItemDTO( @ObjectIdType ObjectId exeid,@ObjectIdType(isRequire=false) ObjectId itemId) throws Exception
	{
		StudentExerciseEntry e=itemPoolService.getStudentExerciseEntry(exeid);
		if(null==e)
		{
			throw new Exception();
		}
	
		List<ObjectId>  finishedList= getItemIdList(e);
		
		ObjectId id=null;
		
		if(null==itemId)
		{
			id=finishedList.get(Constant.ZERO);
		}
		else
		{
			int index=finishedList.indexOf(itemId);
			if(index>=0 && index!=finishedList.size()-1)
			{
				id= finishedList.get(index+1);
			}
			else
			{
				id=itemId;
			}
		}
		
		return getItemDTO(id, e);
	}

	/**
	 * 老师组卷挑选题目
	 * @param subject
	 * @param level
	 * @param itemType
	 * @param cltys
	 * @param bs
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/teacher/items")
	@ResponseBody
	public List<ItemDTO> getItemDTOs(@ObjectIdType  ObjectId subject,Integer level,@RequestParam(required=false,defaultValue="-1") Integer itemType,
									 String cltys,String bs,Integer page) throws Exception
	{
//		getItems(int itemType, int level, int type, String select, Boolean isLast, Boolean isBook, String regular, int page, int pageSize);
		List<ObjectId> cltysList =MongoUtils.convert(cltys);
		List<ObjectId> bsList =MongoUtils.convert(bs);
		if(page<Constant.ZERO)
			page=Constant.ZERO;
		List<ItemDTO> retList =new ArrayList<ItemDTO>(Constant.TWENTY);
		List<Integer> itemTypeList =new ArrayList<Integer>();
		if(itemType>-1)
		{
			itemTypeList.add(itemType);
		}


		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}

		List<ItemPoolEntry> list=itemPoolService.getItemPoolEntryList(eduId, null,subject,level,null,cltysList,bsList,itemTypeList,null, new BasicDBObject("_id",-1), Constant.FIELDS,page*Constant.TWENTY,Constant.TWENTY);
		List<ObjectId> scopeList=new ArrayList<ObjectId>();
		if(null != list){
			for(ItemPoolEntry entry : list){
				scopeList.addAll(entry.getScList());
			}
		}
		Map<ObjectId,ResourceDictionaryEntry> map=itemPoolService.getResourceDictionaryEntryMap(scopeList);
		
		ResourceDictionaryEntry kpe;
		for(ItemPoolEntry e:list) {
			try {
			 kpe=map.get(e.getScList().get(0));
			 retList.add(new ItemDTO(e, kpe));
			}catch(Exception ex) {
				logger.error("", ex);
			}
		}
		return retList;
	}
	

	/**
	 * 选择题目
	 * @param types 题目类型 1，2，3，
	 * @param cltys
	 * @param bs
	 * @param
	 * @param isDoned 0包含已经做过的, 1不包含
	 * @param subject 科目
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/items/ids")
	@ResponseBody
	public RespObj getItemDTOIds(String types, @RequestParam(required=false,defaultValue="") String cltys,@RequestParam(required=false,defaultValue="") String bs,Integer isDoned,@ObjectIdType       ObjectId subject) throws Exception
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		List<ObjectId> cltysList =MongoUtils.convert(cltys);
		List<ObjectId> bsList= MongoUtils.convert(bs);
		
		if(cltysList.isEmpty() && bsList.isEmpty())
		{
			obj.setMessage("知识点或者章节为空");
			return obj;
		}
		if(!cltysList.isEmpty()){
			List<ResourceDictionaryEntry> rdList = resourceDictionaryService.getItemTypeBySubject(cltysList);
			if(null != rdList && rdList.size()>0){
				for(ResourceDictionaryEntry entry : rdList){
					cltysList.add(entry.getID());
				}
			}
		}
				
		if(StringUtils.isBlank(types))
		{
			obj.setMessage("请选择题目类型");
			return obj;
		}
		
		String[] itemTypeArr=StringUtils.split(types,Constant.COMMA);
		List<Integer> itemTypeList =new ArrayList<Integer>();
		for(String itemType:itemTypeArr)
		{
			try
			{
				if(StringUtils.isNotBlank(itemType))
				{
			      itemTypeList.add(Integer.valueOf(itemType));
				}
			}catch(Exception ex)
			{
			}
		}
		
		List<ObjectId> donedList =null;
		if(Constant.ONE==isDoned)
		{
			SubjectType type=itemPoolService.getSubjectType(subject);
			donedList=itemPoolService.getDonedItemIds(getUserId(), type.getType());
		}

		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}

		List<ObjectId> list=itemPoolService.getItemPoolIdsList(eduId, cltysList, bsList,itemTypeList,donedList,null);
		obj =new RespObj(Constant.SUCCESS_CODE, MongoUtils.convertToStringList(list));
		return obj;
	}
	
	/**
	 * 得到题目详情，用户新题库
	 * @param id
	 * @return
	 */
	@RequestMapping("/items/detail")
	@ResponseBody
	public ItemDTO ItemDTODetail( @ObjectIdType ObjectId id)
	{
		StudentExerciseEntry e=null;
		return getItemDTO(id,e);
	}
	
	/**
	 * 学生删除错题 
	 * @param
	 * @param item
	 * @return
	 */
	@RequestMapping("/erritem/delete")
	@ResponseBody
	public RespObj deleteErrorItem(@ObjectIdType ObjectId item)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		StudentErrorItemEntry.Item objectItem=itemPoolService.getErrorItem(getUserId(), item);
		if(null==objectItem)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		logger.info("student delete item;stuent="+getUserId()+";the item="+item);
		itemPoolService.deleteErrorItem(getUserId(),objectItem);
		return RespObj.SUCCESS;
	}
	
	
	
	/**
	 * 加入错题库
	 * @param itemId
	 * @param answer
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public RespObj addErrorItemPool(@ObjectIdType  ObjectId itemId,String answer)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		ItemPoolEntry ipe=itemPoolService.getItemPoolEntry(itemId);
		if(null==ipe)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		try
		{
		 itemPoolService.handleStudentErrorItem(getUserId(),ipe,answer);
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		return RespObj.SUCCESS;
	}
	
	

	//学生完成一个习题时调用
	private void submitOneItem(StudentExerciseEntry e, ObjectId itemId, String answer,ObjectId ui
			) throws Exception {
		ItemPoolEntry ipe=itemPoolService.getItemPoolEntry(itemId);
		if(null==ipe)
		{
			throw new Exception();
		}
		ExerciseItemType type=getItemType(e,itemId);
		if(null==type)
		{
			throw new Exception();
		}
		boolean isNowRight=ItemDTO.isAnswerRight(ipe,answer);
		itemPoolService.updateItemAnswer(e.getID(), itemId, type, answer, isNowRight?Constant.ONE:Constant.ZERO);
		//处理错题库
		try
		{
			if(!isNowRight)
			{
		     itemPoolService.handleStudentErrorItem(ui,ipe,answer);
			}
		}catch(Exception ex)
		{
			//logger.error("", ex);
		}
		
		if(e.getTotalCount()==e.getFinishCount()+1) //更新为完成状态
		{
			itemPoolService.updateExerciseStateToFinish(e.getID());
		}
	}

	
	/**
	 * 得到练习DTO
	 * @param itemId
	 * @param e
	 * @return
	 */

	private ItemDTO getItemDTO(ObjectId itemId, StudentExerciseEntry e) {
		ItemPoolEntry ipe=itemPoolService.getItemPoolEntry(itemId);
		ResourceDictionaryEntry kwPoint=null;
		if(ipe.getScList().size()>0)
		{
		    kwPoint=itemPoolService.getResourceDictionaryEntry(ipe.getScList().get(0));
		}
		return new ItemDTO(ipe,kwPoint,e);
	}
	
	/**
	 * 得到上一条或下一条练习ID
	 * @param e
	 * @param id
	 * @param direction 0 上一条 1下一条
	 * @return
	 * @throws IllegalParamException
	 */
	private ObjectId  getItemId(StudentExerciseEntry e,ObjectId id,int direction) throws Exception
	{
		List<ObjectId> list =getItemIdList(e);
		if(direction==Constant.ZERO)//上一条
		{
			int index=list.indexOf(id);
			if(index<0)
				throw new  Exception ();
			if(index==0)
			{
				return id;
			}
			return list.get(index-1);
		}
		if(direction==Constant.ONE)//下一条
		{
			int index=list.indexOf(id);
			if(index>=0 && index!=list.size()-1)
			{
				return list.get(index+1);
			}
		}
		return id;
	}
	
	/**
	 * 题目列表
	 * @param e
	 * @return
	 */
	public List<ObjectId> getItemIdList(StudentExerciseEntry e)
	{
		List<ObjectId> list =new ArrayList<ObjectId>();
		for(ExerciseItemType thisType:ExerciseItemType.values())
		{
			if(thisType==ExerciseItemType.MULTICHOICE)
				continue;
			if(null!=e.getList(thisType))
			{
				for(IdNameValuePair p:e.getList(thisType))
				{
					
					list.add(p.getId());
				}
			}
		}
		return list;
	}
	
	/**
	 * 得到练习题类型
	 * @param e
	 * @return
	 * @throws IllegalParamException
	 */
	private  ExerciseItemType getItemType(StudentExerciseEntry e,ObjectId itemId) throws Exception
	{
		for(ExerciseItemType thisType:ExerciseItemType.values())
		{
			if(thisType==ExerciseItemType.MULTICHOICE)
				continue;
			
			if(null!=e.getList(thisType))
			{
				for(IdNameValuePair p:e.getList(thisType))
				{
					
					if(null!=p.getId() && p.getId().equals(itemId) )
					{
						return thisType;
					}
				}
			}
		}

		return null;
	}
	
	
	/**
	 * 根据父节点构建资源树
	 * @param parentId
	 * @return
	 */
	private List<KnowledgePointDTO> buildResTreeByParentId(ObjectId parentId,int type) {
		List<ResourceDictionaryEntry> firstLevelRDEList=itemPoolService.getResourceDictionaryEntrys(parentId,type);
		Map<ObjectId,ResourceDictionaryEntry> firstLevelRDEMap =new HashMap<ObjectId, ResourceDictionaryEntry>();
		for(ResourceDictionaryEntry e:firstLevelRDEList)
		{
			firstLevelRDEMap.put(e.getID(), e);
		}
		Map<ObjectId,ResourceDictionaryEntry> secondLevelRDEMap =itemPoolService.getResourceDictionaryEntryMapByParents(firstLevelRDEMap.keySet(),-1);
		Map<ObjectId,ResourceDictionaryEntry> thirdLevelRDEMap =itemPoolService.getResourceDictionaryEntryMapByParents(secondLevelRDEMap.keySet(),-1);
		return build(firstLevelRDEMap,secondLevelRDEMap,thirdLevelRDEMap);
	}
	/**
	 * 创建资源树
	 * @param firstLevel
	 * @param secondLevel
	 * @param thirdLevel
	 * @return
	 */
	private List<KnowledgePointDTO> build(Map<ObjectId,ResourceDictionaryEntry> firstLevel,Map<ObjectId,ResourceDictionaryEntry> secondLevel,Map<ObjectId,ResourceDictionaryEntry> thirdLevel)
	{
		//先循环2级
		Map<ObjectId,List<IdValuePairSortDTO>> dtoMapList =new HashMap<ObjectId, List<IdValuePairSortDTO>>();
		IdValuePairSortDTO sortDTO;
		for(ResourceDictionaryEntry e:secondLevel.values())
		{
			if(!dtoMapList.containsKey(e.getID()))
			{
				List<IdValuePairSortDTO> list =new ArrayList<IdValuePairSortDTO>();
				sortDTO=new IdValuePairSortDTO(e.getID(), e.getName());
				sortDTO.setSort(e.getSort());
				list.add(sortDTO);
				dtoMapList.put(e.getID(), list);
			}
		}
		
	
		for(ResourceDictionaryEntry e:thirdLevel.values())
		{
			sortDTO=new IdValuePairSortDTO(e.getID(), e.getName());
			sortDTO.setSort(e.getSort());
			List<IdValuePairSortDTO> list=dtoMapList.get(e.getParentId());
			list.add(sortDTO);
		}
		
		Map<ObjectId,KnowledgePointDTO> KnowledgePointDTOMap =new HashMap<ObjectId, KnowledgePointDTO>();
		
		ResourceDictionaryEntry firstEntry;
		ResourceDictionaryEntry secondEntry;
		KnowledgePointDTO knowledgePointDTO;
		List<IdValuePairSortDTO> list;
		for(Map.Entry<ObjectId,List<IdValuePairSortDTO>> entry:dtoMapList.entrySet())
		{
			secondEntry=secondLevel.get(entry.getKey());
			firstEntry=firstLevel.get(secondEntry.getParentId());
			if(null!=secondEntry && null!=firstEntry)
			{
				if(!KnowledgePointDTOMap.containsKey(firstEntry.getID()))
				{
					knowledgePointDTO=new KnowledgePointDTO(firstEntry);
					KnowledgePointDTOMap.put(firstEntry.getID(), knowledgePointDTO);
				}
				list =entry.getValue();
				if(list.size()>Constant.ONE)
				{
					List<IdValuePairSortDTO> sublist=list.subList(Constant.ONE, list.size());
					Collections.sort(sublist, new Comparator<IdValuePairSortDTO>() {
						@Override
						public int compare(IdValuePairSortDTO arg0, IdValuePairSortDTO arg1) {
							try
							{
								long a= arg0.getSort()-arg1.getSort();
								if(a>0)
								{
									return Constant.ONE;
								}
								if(a<0)
								{
									return Constant.NEGATIVE_ONE;
								}
								return Constant.ZERO;
							}catch(Exception ex)
							{
							}
							return Constant.ZERO;
						}
					});
					
					 KnowledgePointDTOMap.get(firstEntry.getID()).addSecondNode(list.get(0), sublist);
				}
			}
		}
		List<KnowledgePointDTO> retList= new ArrayList<KnowledgePointDTO>(KnowledgePointDTOMap.values());
		
		
		for(KnowledgePointDTO dto:retList)
		{
			dto.sort();
		}
		
		Collections.sort(retList, new Comparator<KnowledgePointDTO>() {
			@Override
			public int compare(KnowledgePointDTO arg0, KnowledgePointDTO arg1) {
				try
				{
					long a= arg0.getSort()-arg1.getSort();
					if(a>0)
					{
						return Constant.ONE;
					}
					if(a<0)
					{
						return Constant.NEGATIVE_ONE;
					}
					return Constant.ZERO;
				}catch(Exception ex)
				{
				}
				return Constant.ZERO;
			}
		});
		
		
		return retList;
	}

	@RequestMapping("/uploadItem")
	public String uploadItems(){
		return "/itempool/uploadItems";
	}

	@RequestMapping("/wordToHtml")
	@ResponseBody
	public Map<String, Object> wordToHtml(MultipartFile file, HttpServletRequest req){
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("code", 500);
		try {
			List<ItemPoolEntry> itemPoolEntries = new WordToHtml().upload(file, req);
			String ids = itemPoolService.save(itemPoolEntries);
			model.put("ids", ids);
			model.put("code", 200);
		} catch (Exception e){
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * @author shanchao
	 * 根据资源字典表节点查找题目
	 * @param itemType  题型（选择、判断、填空、主观）
	 * @param level  难易程度
	 * @param type   类型（1云题库，2教师题库，3校本题库，4学生错题本）
	 * @param select  资源字典表节点
	 * @param isBook  是否是教材筛选（true：教材筛选，false：知识点筛选）
	 * @param regular 用于匹配题干的正则表达式
	 * @param page 页数
	 * @param pageSize 每页个数
	 * @return
	 */
	@RequestMapping("/getItems")
	@ResponseBody
	public Map<String, Object> getItems(int itemType, int level, int type, String select, Boolean isBook, String regular, int page, int pageSize, HttpServletResponse response){
		if(type == 1) {
			response.setHeader("Cache-Control", "max-age=36400");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		List<ObjectId> selectIds = new ArrayList<ObjectId>();

		String cacheKey =CacheHandler.getKeyString(CacheHandler.CACHE_HWSECTION_KEY, select);
//		CacheHandler.deleteKey(CacheHandler.CACHE_HWSECTION_KEY, select);
		String value=CacheHandler.getStringValue(cacheKey);
		if(StringUtils.isBlank(value)) {//没有缓存
			Map<String, Object> map = new HashMap<String, Object>();
			List<String> ids = new ArrayList<String>();
			if(!select.equals("")){
				selectIds.add(new ObjectId(select));
				ids.add(select);
				List<ObjectId> selectList = new ArrayList<ObjectId>();
				selectList.add(new ObjectId(select));
				List<ResourceDictionaryEntry> rdList = resourceDictionaryService.getItemTypeBySubject(selectList);
				if(null != rdList && rdList.size()>0){
					for(ResourceDictionaryEntry entry : rdList){
						selectIds.add(entry.getID());
						ids.add(entry.getID().toString());
					}
				}
			}
			map.put("ids", ids);
			JSONObject data = new JSONObject(map);
			CacheHandler.cache(cacheKey, data.toString(), Constant.SECONDS_IN_DAY);
		} else {//有缓存
			try {
				JSONObject jsonObject = new JSONObject(value);
				JSONArray jsonArray = jsonObject.getJSONArray("ids");
				for(int i=0; i<jsonArray.length(); i++){
					selectIds.add(new ObjectId(jsonArray.getString(i)));
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
		}

		if(4 == type){
			model = itemPoolService.getStudentErrorItems(itemType, level,new ObjectId(getSessionValue().getId()), selectIds, page, pageSize);
		} else {
			ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
			int role=getSessionValue().getUserRole();
			EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
			ObjectId eduId=null;
			if (ebe != null) {
				eduId=ebe.getID();
			}
			String user = "";
			if(2 == type){
				user = getSessionValue().getId();
			} else if(3 == type){
				user = getSessionValue().getSchoolId();
			}
			model = itemPoolService.getItems(eduId,itemType, level, user, selectIds, isBook, regular, page, pageSize);
		}

		return model;
	}

	/**
	 * 查找题目的详细信息，包括全部的资源字典表路径
	 * @param id
	 * @return
	 */
	@RequestMapping("/getItemDetail")
	@ResponseBody
	public Map<String, Object> getItemDetail(@ObjectIdType ObjectId id){
		Map<String, Object> model = itemPoolService.getItemDetail(id);
		return model;
	}

	/**
	 * 推送
	 * @param id 被推送题目的id
	 * @param type 类型  1,3推送到教师题库，2推送到校本题库
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push")
	@ResponseBody
	public RespObj push(@ObjectIdType ObjectId id, int type){
		RespObj respObj = RespObj.FAILD;
		try{
			ObjectId ownerId = getUserId();
			Boolean isCloud = false;
			if(2 == type){
				ownerId = new ObjectId(getSessionValue().getSchoolId());
			} else if(1 == type){
				isCloud = true;
			}
			itemPoolService.push(id, ownerId, isCloud);
			respObj = RespObj.SUCCESS;
		} catch (Exception e){
			e.printStackTrace();
		}

		return respObj;
	}

	/**
	 * 删除
	 * @param id 题目的id
	 * @param type 类型  1云题库,2教师题库，3校本题库
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/remove")
	@ResponseBody
	public RespObj remove(@ObjectIdType ObjectId id, int type){
		RespObj respObj = RespObj.FAILD;
		try{
			ObjectId ownerId = getUserId();
			Boolean isCloud = false;
			if(3 == type){
				ownerId = new ObjectId(getSessionValue().getSchoolId());
			} else if(1 == type){
				isCloud = true;
			}
			if(UserRole.isEducation(getSessionValue().getUserRole())){
				ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
				int role=getSessionValue().getUserRole();
				EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
				if (ebe == null) {
					respObj.message = "该用户未加入任何教育局，无法删除资源!";
					return respObj;
				}
				itemPoolService.eduRemove(id, ebe.getID(), isCloud);
			}else {
				itemPoolService.remove(id, ownerId, isCloud);
			}
			respObj = RespObj.SUCCESS;
		} catch (Exception e){
			e.printStackTrace();
		}

		return respObj;
	}

	@RequestMapping("/save")
	@ResponseBody
	public RespObj save(String id,@ObjectIdType ObjectId stage, @ObjectIdType ObjectId subject, int itemType, int level, double score,
						String book, String knowledge, String question, String answer, String parse, int zc, int sc){
		RespObj respObj = RespObj.FAILD;
		try{
			question = question.replaceAll("</?p>", "");
			answer = answer.replaceAll("</?p>", "");
			parse = parse.replaceAll("</?p>", "");
			ObjectId retId = itemPoolService.save(id, stage, subject, itemType, level, score, book, knowledge, question, answer, parse, getUserId(), zc, sc);
			respObj = RespObj.SUCCESS;
			respObj.setMessage(retId.toString());
		} catch (Exception e){
			e.printStackTrace();
		}

		return respObj;
	}

	@RequestMapping("/getItemErrorCount")
	@ResponseBody
	public List<Map<String, Object>> getItemErrorCount(@ObjectIdType ObjectId itemId){
		return itemPoolService.getErrorItemEntrys(getUserId(), itemId);
	}

	@RequestMapping("/getXueduanAndSubject")
	@ResponseBody
	public Map<String, Object> getXueduanAndSubject(){
		return itemPoolService.getXueduanAndSubject(getUserId());
	}

}
