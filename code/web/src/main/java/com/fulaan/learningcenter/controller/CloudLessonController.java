package com.fulaan.learningcenter.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.learningcenter.service.CloudLessonService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.resources.service.CloudResourceService;
import com.pojo.app.PageDTO;
import com.pojo.app.SimpleDTO;
import com.pojo.cloudlesson.CloudLessonDTO;
import com.pojo.cloudlesson.CloudLessonEntry;
import com.pojo.cloudlesson.CloudLessonTypeDTO;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.lesson.LessonWare;
import com.pojo.school.GradeType;
import com.pojo.school.SchoolType;
import com.pojo.school.SubjectType;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 云课程
 * @author fourer
 */

@Controller
@RequestMapping("/cloud")
public class CloudLessonController extends BaseController {

	private static final Logger logger =Logger.getLogger(CloudLessonController.class);
	
	
	private CloudLessonService cloudLessonService =new CloudLessonService();
	private DirService dirService =new DirService();
	private LessonService lessonService =new LessonService();
	@Autowired
	private EducationBureauService educationBureauService;

	@Autowired
	private CloudResourceService cloudResourceService;

    @RequestMapping("/cloudLesson")
    public String cloudLesson(Map<String, Object> model) {

        List<SimpleDTO> schoolTypeList = getSchoolTypes();
        model.put("styStionList", schoolTypeList);
        return "learningcenter/cloudLesson";
    }

	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/cloudLessonManage")
	public String cloudLessonManage(Map<String, Object> model) {
		List<SimpleDTO> schoolTypeList = getSchoolTypes();
		model.put("styStionList", schoolTypeList);
		return "weikeziyuanguanli/weikeziyuanguanli";
	}
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/cloudLessonGit")
	public String cloudLessonGit(Map<String, Object> model) {
		List<SimpleDTO> schoolTypeList = getSchoolTypes();
		model.put("styStionList", schoolTypeList);
		return "weikeziyuanguanli/weikeziyuangit";
	}

	/**
	 * 删除一个微课资源
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/delCloudLesson")
	@ResponseBody
	public String delCourseware(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "videoId", defaultValue = "") String videoId) {
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法删除资源!";
			return JSON.toJSONString(respObj);
		}
		cloudLessonService.deleteCloudLesson(ebe.getID(), new ObjectId(id), new ObjectId(videoId));
		return JSON.toJSONString(RespObj.SUCCESS);
	}


	/**
	 * 新增至资源库页面跳转
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/addToGit")
	public String addWeiKeiToGit(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "videoId", defaultValue = "") String videoId,Map<String, Object> model) {
		cloudLessonService.getMicroLessonDetail(new ObjectId(id),new ObjectId(videoId),model);
		List<SimpleDTO> schoolTypeList = getSchoolTypes();
		model.put("styStionList", schoolTypeList);
		model.put("isSaved","0");
		return "weikeziyuanguanli/addWeiKeToGit";
	}

	/**
	 * 新增至资源库页面跳转
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/addWeiKei")
	public String addWeiKei(Map<String, Object> model) {
		List<SimpleDTO> schoolTypeList = getSchoolTypes();
		model.put("styStionList", schoolTypeList);
		return "weikeziyuanguanli/addWeiKe";
	}

	/**
	 * 前台新增课件
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/addMicroLesson")
	@ResponseBody
	public String addMicroLesson(
			//@RequestParam(value = "termType", defaultValue = "") String termType,
			@RequestParam(value = "subject", defaultValue = "") Integer subject,
			@RequestParam(value = "subjectText", defaultValue = "") String subjectText,
			@RequestParam(value = "grade", defaultValue = "") Integer grade,
			@RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "coverId", defaultValue = "") String coverId,
			@RequestParam(value = "videoId", defaultValue = "") String videoId,
			@RequestParam(value = "videoName", defaultValue = "") String videoName
	) {
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法添加资源!";
			return JSON.toJSONString(respObj);
		}
		String coverUrl = cloudResourceService.getResourceEntryById(new ObjectId(coverId)).getImgUrl();
		cloudResourceService.removeById(new ObjectId(coverId));

		List<ObjectId> videoIds = new ArrayList<ObjectId>();
		videoIds.add(new ObjectId(videoId));

		List<Integer> gradeTypes =new ArrayList<Integer>();
		gradeTypes.add(grade);
		List<ObjectId> classTypes =new ArrayList<ObjectId>();
		if(!"".equals(type)){
			classTypes.add(new ObjectId(type));
		}

		CloudLessonEntry cloudLessonEntry = new CloudLessonEntry(
				subjectText+":"+videoName,
				getUserId(),
				videoName,
				0,//order
				coverUrl,
				videoIds,
				subject,
				gradeTypes,
				classTypes,
				0,
				1,
				1,
				ebe.getID(),
				new ArrayList<ObjectId>(),
				"资源板块前端"
		);

		cloudLessonService.addCloudLessonEntry(cloudLessonEntry);
		return JSON.toJSONString(RespObj.SUCCESS);
	}

	/**
	 * 新增至资源库页面跳转
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/editCloudLesson")
	public String editCloudLesson(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "videoId", defaultValue = "") String videoId,Map<String, Object> model) {
		cloudLessonService.getMicroLessonDetail(new ObjectId(id),new ObjectId(videoId),model);
		List<SimpleDTO> schoolTypeList = getSchoolTypes();
		model.put("styStionList", schoolTypeList);
		model.put("isSaved","1");
		return "weikeziyuanguanli/addWeiKeToGit";
	}

	/**
	 * 查询微课信息
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/getCloudLesson")
	@ResponseBody
	public Map<String, Object> getCloudLesson(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "videoId", defaultValue = "") String videoId) {
		Map<String, Object> model = new HashMap<String, Object>();
		cloudLessonService.getMicroLessonDetail(new ObjectId(id),new ObjectId(videoId),model);
		return model;
	}

	/**
	 * 前台新增课件
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/updMicroLesson")
	@ResponseBody
	public String updMicroLesson(
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "termType", defaultValue = "") Integer termType,
			@RequestParam(value = "subject", defaultValue = "") Integer subject,
			@RequestParam(value = "subjectText", defaultValue = "") String subjectText,
			@RequestParam(value = "grade", defaultValue = "") Integer grade,
			@RequestParam(value = "type", defaultValue = "") String type,
			@RequestParam(value = "imageUrl", defaultValue = "") String imageUrl,
			@RequestParam(value = "coverId", defaultValue = "") String coverId,
			@RequestParam(value = "oldVideoId", defaultValue = "") String oldVideoId,
			@RequestParam(value = "videoId", defaultValue = "") String videoId,
			@RequestParam(value = "videoName", defaultValue = "") String videoName
	) {
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法添加资源!";
			return JSON.toJSONString(respObj);
		}

		CloudLessonEntry cloudLessonEntry= cloudLessonService.getCloudLessonEntry(new ObjectId(id));
        if("".equals(imageUrl)){
			String coverUrl = cloudResourceService.getResourceEntryById(new ObjectId(coverId)).getImgUrl();
			cloudResourceService.removeById(new ObjectId(coverId));
			cloudLessonEntry.setImageUrl(coverUrl);
		}
		if("".equals(oldVideoId)){
			List<ObjectId> videoIds = new ArrayList<ObjectId>();
			videoIds.add(new ObjectId(videoId));
			cloudLessonEntry.setVideoIds(videoIds);
			cloudLessonEntry.setContent(videoName);
			cloudLessonEntry.setName(subjectText+":"+videoName);
		}
		if(subject!=cloudLessonEntry.getSubject()){
			cloudLessonEntry.setSubject(subject);
			String[] names=cloudLessonEntry.getName().split(":");
			cloudLessonEntry.setName(subjectText+":"+names[1]);
		}
		List<Integer> gradeTypes =new ArrayList<Integer>();
		if(null!=grade) {
			gradeTypes.add(grade);
		} else {
			gradeTypes.addAll(GradeType.getGradeTypeIds(termType, Constant.NEGATIVE_ONE));
		}

		cloudLessonEntry.setCloudClassGradeTypes(gradeTypes);
		List<ObjectId> classTypes =new ArrayList<ObjectId>();
		if(!"".equals(type)){
			classTypes.add(new ObjectId(type));
		}
		cloudLessonEntry.setCloudClassTypes(classTypes);

		cloudLessonEntry.setIsSaved(1);

		cloudLessonService.updCloudLessonEntry(cloudLessonEntry);
		return JSON.toJSONString(RespObj.SUCCESS);
	}

	/**
	 * 未入库微课课件列表
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/queryCloudList")
	@ResponseBody
	public String getCloudLessonVideoView(
			@RequestParam(defaultValue="-1") int schoolType,
			@RequestParam(defaultValue="-1")Integer grade,
			@RequestParam(defaultValue="1") int isSaved,
			@RequestParam(defaultValue="-1") int subject,
			@ObjectIdType(isRequire=false) ObjectId classTypeId,
			String searchName,
			@RequestParam(defaultValue="1") int pageNo,
			@RequestParam(defaultValue="12") int pageSize
	) throws ResultTooManyException, IllegalParamException{
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		if (ebe == null) {
			RespObj respObj = new RespObj(Constant.FAILD_CODE);
			respObj.message = "该用户未加入任何教育局，无法添加资源!";
			return JSON.toJSONString(respObj);
		}
		List<Integer> grades = new ArrayList<Integer>();
		if(schoolType!=-99) {
			if (grade != -1) {
				grades.add(grade);
			} else {
				grades.addAll(GradeType.getGradeTypeIds(schoolType, Constant.NEGATIVE_ONE));
			}
		}
		int count=	cloudLessonService.getCloudLessonCount(ebe.getID(), isSaved, grades, subject, classTypeId,searchName);
		List<CloudLessonDTO> list = cloudLessonService.getCloudLessonDTO(ebe.getID(), isSaved, grades, subject, classTypeId, searchName, (pageNo - 1) * pageSize, pageSize);
		PageDTO<CloudLessonDTO> retList = new PageDTO<CloudLessonDTO>(count, list);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (UserRole.isTeacher(role) || UserRole.isHeadmaster(role)) {
			resultMap.put("couldPush", true);
		}
		resultMap.put("page", pageNo);
		resultMap.put("pageSize", pageSize);
		resultMap.put("datas", retList);

		return JSON.toJSONString(resultMap);
	}

    
	/**
	 * 学校种类
	 * @return
	 */
	@RequestMapping("/schooltypes")
	@ResponseBody
	public List<SimpleDTO> getSchoolTypes()
	{
		List<SimpleDTO> list =new ArrayList<SimpleDTO>();
		list.add(SchoolType.PRIMARY.toSimpleDTO());
		list.add(SchoolType.JUNIOR.toSimpleDTO());
		list.add(SchoolType.SENIOR.toSimpleDTO());
		return list;
	}
	
	
	/**
	 * 根据学校类型得到学科科目
	 * @return
	 */
	@RequestMapping("/subjects")
	@ResponseBody
	public List<SimpleDTO> getSubjects(int schoolType)
	{
		List<SimpleDTO> retList =new ArrayList<SimpleDTO>();
		List<SubjectType> subjectTypeList =SubjectType.getSubjectTypes(schoolType);
		for(SubjectType subjectType:subjectTypeList)
		{
			if(!subjectType.equals(SubjectType.GOOD_LESSON))
			{
			  retList.add(subjectType.toSimpleDTO());
			}
		}
		return retList;
	}
	
	
	/**
	 * 根据学校类型得到年级
	 * @param schoolType
	 * @return
	 */
	@RequestMapping("/grades")
	@ResponseBody
	public List<SimpleDTO> getCloundLessonGrades(int schoolType,@RequestParam(required = false,defaultValue="-1") int subject)
	{
		List<SimpleDTO> retList =new ArrayList<SimpleDTO>();
		List<GradeType> cloudLessonGradeTypeList =GradeType.getGradeTypes(schoolType, subject);
		for(GradeType cloudClassGradeType:cloudLessonGradeTypeList)
		{
			retList.add(cloudClassGradeType.toSimpleDTO());
		}
		return retList;
	}
	
	
	/**
	 * 根据学校得到学校信息，科目信息，年级信息以及类型信息
	 * @param schoolType
	 * @return
	 */
	@RequestMapping("/infos1")
	@ResponseBody
	public Map<String, Object> getInfos1(@RequestParam(required = false,defaultValue="2") int schoolType,@RequestParam(required = false,defaultValue="-1") int subject)
	{
		Map<String, Object> retMap =new HashMap<String, Object>();
		List<SimpleDTO> schoolList =new ArrayList<SimpleDTO>();
		if(Constant.NEGATIVE_ONE==schoolType)
		{
			schoolList.addAll(getSchoolTypes());
		}
		if(SchoolType.PRIMARY.getType()==schoolType)
		{
			schoolList.add(SchoolType.PRIMARY.toSimpleDTO());
		}
		if(SchoolType.JUNIOR.getType()==schoolType)
		{
			schoolList.add(SchoolType.JUNIOR.toSimpleDTO());
		}
		if(SchoolType.SENIOR.getType()==schoolType)
		{
			schoolList.add(SchoolType.SENIOR.toSimpleDTO());
		}
		retMap.put("school", schoolList);
		
		int querySchoolType =-1==schoolType?SchoolType.PRIMARY.getType():schoolType;
		
		retMap.put("subject", getSubjects(querySchoolType));
		retMap.put("grade", getCloundLessonGrades(querySchoolType,subject));
		retMap.put("type", getCloudLessonTypeDTOs(schoolType,Constant.NEGATIVE_ONE,subject));
	
		return retMap;
	}
	
	
	
	
	/**
	 * 为了适配移动端
	 * 根据学校得到学校信息，科目信息，年级信息以及类型信息
	 * @param schoolType
	 * @return
	 */
	@RequestMapping("/infos")
	@ResponseBody
	public Map<String, Object> getInfos(@RequestParam(required = false,defaultValue="-1") int schoolType,@RequestParam(required = false,defaultValue="-1") int subject)
	{
		Map<String, Object> retMap =new HashMap<String, Object>();
		List<SimpleDTO> schoolList =new ArrayList<SimpleDTO>();
		if(Constant.NEGATIVE_ONE==schoolType)
		{
			schoolList.addAll(getSchoolTypes());
		}
		if(SchoolType.PRIMARY.getType()==schoolType)
		{
			schoolList.add(SchoolType.PRIMARY.toSimpleDTO());
		}
		if(SchoolType.JUNIOR.getType()==schoolType)
		{
			schoolList.add(SchoolType.JUNIOR.toSimpleDTO());
		}
		if(SchoolType.SENIOR.getType()==schoolType)
		{
			schoolList.add(SchoolType.SENIOR.toSimpleDTO());
		}
		retMap.put("school", schoolList);
		
		int querySchoolType =-1==schoolType?SchoolType.PRIMARY.getType():schoolType;
		
		retMap.put("subject", getSubjects(querySchoolType));
		retMap.put("grade", getCloundLessonGrades(querySchoolType,subject));
		retMap.put("type", getCloudLessonTypeDTOs(schoolType,Constant.NEGATIVE_ONE,subject));
	
		return retMap;
	}
	
	
	/**
	 * 根据学校类型，年级类型和科目查询云课程类别
	 * @param school
	 * @param grade
	 * @param subject
	 * @return
	 */
	@RequestMapping("/classtypes")
	@ResponseBody
	public List<CloudLessonTypeDTO> getCloudLessonTypeDTOs(@RequestParam(required = false,defaultValue="-1") int school,@RequestParam(required = false,defaultValue="-1") int grade,@RequestParam(required = false,defaultValue="-1") int subject)
	{
		List<CloudLessonTypeDTO> retList= cloudLessonService.getCloudLessonTypeDTOs(school, grade, subject);

		return retList;
	}
	
	
	
	/**
	 * 云课程查询视频列表数目
	 * @param schoolType 学校类型；参见SchoolType
	 * @param grade 年级
	 * @param subject 科目
	 * @param cloudLesson 学课程类别
	 * @return
	 */
	@RequestMapping("/videos/count")
	@ResponseBody
	public int getCloudLessonVideoCount(@RequestParam(defaultValue="-1") int schoolType,@RequestParam(defaultValue="-1") int grade,@RequestParam(defaultValue="-1") int  subject,@ObjectIdType ObjectId cloudLesson,@RequestParam(defaultValue="") String searchName) 
	{
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}

		List<Integer> grades =new ArrayList<Integer>();

        if(grade!=-1) {
            grades.add(grade);
        }else {
           // grades.addAll(GradeType.getGradeTypeIds(schoolType, Constant.NEGATIVE_ONE));
        }
		return cloudLessonService.getCloudLessonCount(grades, subject, cloudLesson,searchName, eduId);
	}

	/**
	 * 云课程查询视频列表
	 * @param schoolType 学校类型；参见SchoolType
	 * @param grade 年级
	 * @param subject 科目
	 * @param classTypeId
	 * @param searchName
	 * @param page 从1开始
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 * @throws IllegalParamException
	 */
	@RequestMapping("/videos")
	@ResponseBody
	public PageDTO<CloudLessonDTO> getCloudLessonVideoList(@RequestParam(defaultValue="-1") int schoolType,@RequestParam(defaultValue="-1")Integer grade,@RequestParam(defaultValue="-1") int subject,@ObjectIdType(isRequire=false) ObjectId classTypeId,String searchName,@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="20") int limit) throws ResultTooManyException, IllegalParamException
	{
		ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
		int role=getSessionValue().getUserRole();
		EducationBureauEntry ebe = educationBureauService.selEducationByUserId(getUserId(),role,schoolId.toString());
		ObjectId eduId=null;
		if (ebe != null) {
			eduId=ebe.getID();
		}
	    int count=	getCloudLessonVideoCount(schoolType, grade, subject, classTypeId, searchName);
		List<CloudLessonDTO> retList =new ArrayList<CloudLessonDTO>();
		
		
		if(count>0) 
		{
			List<Integer> grades =new ArrayList<Integer>();
            if(grade!=-1) {
                grades.add(grade);
            }else {
               // grades.addAll(GradeType.getGradeTypeIds(schoolType, Constant.NEGATIVE_ONE));
            }
			retList=cloudLessonService.getCloudLessonDTO(grades, subject, classTypeId,searchName, eduId, (page-1)*limit, limit);
			logger.debug(retList);
		}
		
		
		
		return new PageDTO<CloudLessonDTO>(count, retList);
	}

	@RequestMapping("/courseList")
	public String getCloudLessonVideoView(@RequestParam(defaultValue="-1") int schoolType,
										  @RequestParam(defaultValue="-1")Integer grade,
										  @RequestParam(defaultValue="-1") int subject,
										  @ObjectIdType(isRequire=false) ObjectId classTypeId,
										  String searchName,
										  @RequestParam(defaultValue="1") int page,
										  @RequestParam(defaultValue="20") int limit,
										  Map<String, Object> model) throws ResultTooManyException, IllegalParamException
	{
		PageDTO<CloudLessonDTO> retList = getCloudLessonVideoList(schoolType, grade, subject, classTypeId, searchName, page, limit);
		model.put("returnData", retList);
		model.put("page", page);
		model.put("limit", limit);
		int role = getSessionValue().getUserRole();
		if (UserRole.isTeacher(role) || UserRole.isHeadmaster(role)) {
			model.put("couldPush", true);
		}
		return "learningcenter/cloudCourses";
	}

    @RequestMapping("/courseList/json")
    @ResponseBody
    public Map<String,Object> getCloudLessonVideoData(@RequestParam(defaultValue="-1") int schoolType,
                                          @RequestParam(defaultValue="-1")Integer grade,
                                          @RequestParam(defaultValue="-1") int subject,
                                          @ObjectIdType(isRequire=false) ObjectId classTypeId,
                                          String searchName,
                                          @RequestParam(defaultValue="1") int page,
                                          @RequestParam(defaultValue="20") int limit
                                          ) throws ResultTooManyException, IllegalParamException
    {
        Map<String, Object>  retData = new HashMap<String,Object>();
        PageDTO<CloudLessonDTO> retList = getCloudLessonVideoList(schoolType, grade, subject, classTypeId, searchName, page, limit);
        retData.put("returnData", retList);
        retData.put("page", page);
        retData.put("limit", limit);
        int role = getSessionValue().getUserRole();
        if (UserRole.isTeacher(role) || UserRole.isHeadmaster(role)) {
            retData.put("couldPush", true);
        }
        return retData;
    }



	/**
	 * 推送一个课程到多个目录;应该添加权限检查
	 * @param dirId
	 * @param lessonId
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push")
	@ResponseBody
	public RespObj push(String dirIds, @ObjectIdType ObjectId lessonId)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
//		if(StringUtils.isBlank(dirIds))
//		{
//			obj.setMessage("请选择目录");
//			return obj;
//		}
		ObjectId userId=getUserId();
		
        Set<ObjectId> myDirIds=dirService.getDirIds(userId, new ObjectId(getSessionValue().getSchoolId()));
		
		List<ObjectId> dirIdList=new ArrayList<ObjectId>();
		try {
			dirIdList = MongoUtils.convert(dirIds);
		} catch (IllegalParamException e1) {
			logger.error("", e1);
			obj.setMessage("参数错误");
			return obj;
		}
		
		for(ObjectId id:dirIdList)
		{
			if(!myDirIds.contains(id))
			{
				obj.setMessage("参数错误");
				return obj;
			}
		}
		
		List<DirEntry> dirs=dirService.getDirEntryListByIds(dirIdList, Constant.FIELDS);
		if(dirs.size()!=dirIdList.size())
		{
			obj.setMessage("参数错误");
			return obj;
		}
		
		
		CloudLessonEntry cloudLessonEntry =cloudLessonService.getCloudLessonEntry(lessonId);
		if(null==cloudLessonEntry)
		{
			logger.error("can not find CloudLessonEntry;"+lessonId);
			obj.setMessage("参数错误");
			return obj;
		}

		
	    List<ObjectId> videoIds =new ArrayList<ObjectId>();
	    videoIds.addAll(cloudLessonEntry.getVideoIds());
	    
	    
	    List<LessonEntry> LessonEntryList =new ArrayList<LessonEntry>(dirs.size());
	    for(DirEntry dir:dirs)
	    {
		LessonEntry e =new LessonEntry(cloudLessonEntry.getName(), //name
				cloudLessonEntry.getContent(), //content
				LessonType.BACKUP_LESSON, //type
				userId,//ui
				dir.getID(),  //dirId
				cloudLessonEntry.getImageUrl(), //imgUrl
				videoIds,//videoIds
				System.currentTimeMillis(), //lastUpdateTime
                videoIds.size(),
				Constant.ZERO,
				Constant.ZERO,
				new ArrayList<LessonWare>(),
				null,
				lessonId,
				Constant.ONE
				);
		LessonEntryList.add(e);
	    }
		
	  lessonService.addLessonEntrys(LessonEntryList);
	  logger.info("push cloud lesson;dirId="+dirIds+";lessonId="+lessonId);
	  return RespObj.SUCCESS;
	}
	
	
	/**
	 * 将云课程推送到多个备课空间
	 * @param lessonId
	 * @param ids
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push/buckups")
	@ResponseBody
	public RespObj pushToMultiBackUp(@ObjectIdType ObjectId lessonId, String ids) throws IllegalParamException
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		
		if(StringUtils.isBlank(ids))
		{
			obj.setMessage("请选择要推动的备课空间");
			return obj;
		}
		
		CloudLessonEntry cloudLessonEntry =cloudLessonService.getCloudLessonEntry(lessonId);
		if(null==cloudLessonEntry)
		{
			logger.error("can not find CloudLessonEntry;"+lessonId);
			obj.setMessage("参数错误");
			return obj;
		}
		
		ids=ids.replaceAll("a_", "");
		
		List<ObjectId> objectids =MongoUtils.convert(ids);
		
		for(ObjectId objId:objectids)
		{
			try
			{
			  lessonService.addVideo(objId, cloudLessonEntry.getVideoIds().get(0));
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		
		return RespObj.SUCCESS;
	}
}
