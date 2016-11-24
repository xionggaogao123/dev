package com.fulaan.learningcenter.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.learningcenter.dto.*;
import com.fulaan.learningcenter.service.*;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.pojo.KeyValue;
import com.fulaan.video.service.VideoService;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.SimpleDTO;
import com.pojo.school.ClassEntry;
import com.pojo.school.InteractLessonEntry;
import com.pojo.school.InteractLessonFileEntry;
import com.pojo.school.InteractLessonFileType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 互动课堂
 * @author fourer
 *
 */
@Controller
@RequestMapping("/interactLesson")
public class InteractLessonController extends BaseController {

	private static final Logger logger =Logger.getLogger(InteractLessonController.class);

	private VideoService videoService=new VideoService();
	@Resource
	private UserService userService;
	@Resource
	private InteractLessonService interactLessonService;
	@Resource
	private ClassService classService;
	@Resource
	private ActivinessService activinessService;

	@Resource
	private InteractLessonExamService interactLessonExamService;

	@Resource
	private InteractLessonScoreClassifyService interactLessonScoreClassifyService;

	@Resource
	private InteractLessonQuickAnswerService interactLessonQuickAnswerService;

	@Resource
	private InteractLessonFileService interactLessonFileService;

	@Autowired
	private SchoolService schoolService;

	//记录文件上传次数
	private static Map<String,Integer> fileTimesMap = new HashMap<String,Integer>();

	@RequestMapping("lanclass")
	public String lanClass(@RequestParam(required=false,defaultValue="0")Integer type,Model model) throws IllegalParamException {
		int role=getSessionValue().getUserRole();
		if(!UserRole.isHeadmaster(role)||type==1) {
			interactLessonService.getInteractLessonClassInfo(role, getUserId(), getSessionValue().getSchoolId(), model);
			return "lanclass/lanclass";
		}else{
			List<GradeView> gradeViewList = schoolService.findGradeList(getSessionValue().getSchoolId());
			model.addAttribute("gradelist",gradeViewList);
			return "lanclass/headmastereclass";
		}
	}

	@RequestMapping("teaLanClass")
	public String teaLanClass(String userId,Model model) throws IllegalParamException {
		int role=getSessionValue().getUserRole();
		if(UserRole.isHeadmaster(role)){
			interactLessonService.getInteractLessonClassInfo(UserRole.TEACHER.getRole(), new ObjectId(userId), getSessionValue().getSchoolId(), model);
			return "lanclass/tealanclass";
		}else{
			return "";
		}
	}

	/**
	 * 查询互动课堂 使用过的老师列表
	 * @return
	 */
	@RequestMapping("/getInteractLessonUseTeaList")
	@ResponseBody
	public Map<String,Object> getInteractLessonUseTeaList(String gradeId,String teaName,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize)
	{
		Map<String,Object> model = new HashMap<String,Object>();
		ObjectId schoolId=getSchoolId();
		List<Map<String,String>> list=interactLessonService.getInteractLessonUseTeaList(schoolId,gradeId,teaName);
		List<Map<String,String>> reList=listImitatePage(list,page,pageSize);
		model.put("rows",reList);
		model.put("total", list.size());
		model.put("page", page);
		model.put("pageSize", pageSize);
		return model;
	}

	/**
	 * 模拟对list分页查询
	 * @param list
	 * @param page
	 * @param pageSize
	 * @return
	 */
	private List<Map<String,String>> listImitatePage(List<Map<String,String>> list,int page,int pageSize) {
		int totalCount =list.size();
		int pageCount=0;
		int m=totalCount%pageSize;
		if(m>0){
			pageCount=totalCount/pageSize+1;
		} else {
			pageCount=totalCount/pageSize;
		}
		List<Map<String,String>> subList=new ArrayList<Map<String,String>>();
		if(list!=null&&list.size()>0) {
			if (m == 0) {
				subList = list.subList((page - 1) * pageSize, pageSize * (page));
			} else {
				if (page == pageCount) {
					subList = list.subList((page - 1) * pageSize, totalCount);
				} else {
					subList = list.subList((page - 1) * pageSize, pageSize * (page));
				}
			}
		}
		return subList;
	}

	@RequestMapping("lessonclass")
	public String lessonclass(@ObjectIdType ObjectId lessonId, Map<String, Object> model) throws Exception{
		InteractLessonDTO lessonDTO=interactLessonService.getInteractLessonDTOById(lessonId);
		BeanInfo info = Introspector.getBeanInfo(lessonDTO.getClass());
		for (PropertyDescriptor pd: info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader != null) {
				model.put(pd.getName(), reader.invoke(lessonDTO));
			}
		}
		List<KeyValue> fileTimesList=interactLessonFileService.getFileUploadTimesList(lessonId,InteractLessonFileType.FILL.getType());
		List<KeyValue> answerTimesList=interactLessonQuickAnswerService.getQuickAnswerTimesList(lessonId);
		List<KeyValue> examTimesList=interactLessonExamService.getExamTimesList(lessonId);
		model.put("fileTimesList",fileTimesList);
		model.put("answerTimesList",answerTimesList);
		model.put("examTimesList",examTimesList);
		return "lanclass/lessonclass";
	}

	@RequestMapping("load")
	public String loadTeacher()
	{
		if(UserRole.isStudentOrParent(getSessionValue().getUserRole()))
		{
			return "learningcenter/interactLesson_student";
		}
		return "learningcenter/interactLesson_teacher";
	}
	
	@RequestMapping("/empty")
	public String empty()
	{
		return "learningcenter/empty_interactLesson";
	}
	
	/**
	 * 上传视频
	 * @param userId
	 * @param classId
	 * @param lessonId
	 * @param Filedata
	 * @return
	 * @throws IllegalParamException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws EncoderException 
	 * @throws PermissionUnallowedException 
	 */
	@RequestMapping("/video/upload")
	@ResponseBody
	@SessionNeedless
	public RespObj uploadVideo(@ObjectIdType(isRequire=false) ObjectId userId,@ObjectIdType(isRequire=false) ObjectId classId,@ObjectIdType(isRequire=false) ObjectId lessonId,MultipartFile Filedata) throws IllegalParamException, IllegalStateException, IOException, EncoderException, PermissionUnallowedException
	{
 		UserEntry ue= userService.searchUserId(userId);
		if(null==ue)
		{
			throw new IllegalParamException();
		}
		logger.info("111");
		if(!UserRole.isTeacher(ue.getRole()))
		{
			throw new PermissionUnallowedException();
		}
		logger.info("begin upload video!user:"+ue.getID().toString()+","+ue.getUserName()+";school:"+ue.getSchoolID());
		logger.info("222");
		ObjectId videoId=null;
		String result=uploadVideo(Filedata);
		if("videoNameError".equals(result)){
			RespObj obj =new RespObj(Constant.FAILD_CODE, "视频名字非法");
			return obj;
		}else if("videoUploadFail".equals(result)){
			RespObj obj =new RespObj(Constant.FAILD_CODE, "视频图片上传失败");
			return obj;
		}else{
			videoId=new ObjectId(result);
		}
		logger.info("333");
		InteractLessonEntry e=null;
		VideoEntry videoEntry = videoService.getVideoEntryById(videoId);
		if(classId!=null){
			e=new InteractLessonEntry(userId, ue.getSchoolID(), classId,null, videoId,videoEntry.getName());
			interactLessonService.addInteractLessonEntry(e);
		}
		if(lessonId!=null){
			e=interactLessonService.getInteractLessonEntryById(lessonId);
			if(e!=null) {
				e.setVideoId(videoId);
				e.setLessonName(videoEntry.getName());
				interactLessonService.updInteractLessonEntry(e);
			}
		}
		logger.info("444");
		return RespObj.SUCCESS;
	}

	/**
	 * 上传视频
	 * @param Filedata
	 * @return
	 * @throws IllegalParamException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws EncoderException
	 * @throws PermissionUnallowedException
	 */
	private String uploadVideo(MultipartFile Filedata) throws IllegalParamException, IllegalStateException, IOException, EncoderException, PermissionUnallowedException{
		String fileName=FilenameUtils.getName(Filedata.getOriginalFilename());
		logger.info("        video:");
		if(!ValidationUtils.isRequestVideoName(fileName))
		{
			logger.info("        video:111111");
			return "videoNameError";
		}
		//视频filekey
		String videoFilekey =new ObjectId().toString()+Constant.POINT+FilenameUtils.getExtension(fileName);
		String bathPath=Resources.getProperty("upload.file");
		File dir =new File(bathPath);
		if(!dir.exists())
		{
			logger.info("        video:2222");
			dir.mkdir();
		}
		logger.info("        video:33333");
		File savedFile = new File(bathPath, videoFilekey);

		OutputStream stream =new FileOutputStream(savedFile);
		stream.write(Filedata.getBytes());
		stream.flush();
		stream.close();

		String coverImage = new ObjectId().toString() + ".jpg";
		Encoder encoder = new Encoder();
		File screenShotFile = new File(bathPath, coverImage);
		long videoLength = 60000;//缺省一分钟
		//是否生成了图片
		boolean isCreateImage=false;
		try
		{
			encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
			videoLength = encoder.getInfo(savedFile).getDuration();
			isCreateImage=true;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			logger.error("", ex);
		}
		if(videoLength==-1){
			videoLength = 60000;//获取不到时间就设为1分钟
		}

		logger.debug("begin upload video iamge");
		logger.info("        video:444444");
		//String imageFilePath = null;
		//上传图片
		if(isCreateImage && screenShotFile.exists())
		{
			RespObj obj=QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);
			if(!obj.getCode().equals(Constant.SUCCESS_CODE))
			{
				QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_VIDEO, videoFilekey);
				return "videoUploadFail";
			}
		}
		logger.info("        video:5555");
		VideoEntry ve =new VideoEntry(fileName, videoLength, VideoSourceType.USER_VIDEO.getType(),videoFilekey);
		ve.setID(new ObjectId());
		logger.debug("begin upload video");
		logger.info("        video:66666");
		QiniuFileUtils.uploadVideoFile(ve.getID(),videoFilekey, Filedata.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
		if(isCreateImage && screenShotFile.exists())
		{
			ve.setImgUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,coverImage));
		}
		logger.info("        video:77777");
		ObjectId videoId=videoService.addVideoEntry(ve);

		//删除临时文件
		try
		{
			savedFile.delete();
			screenShotFile.delete();
		}catch(Exception ex)
		{
			logger.error("", ex);
		}

		return videoId.toString();
	}

	/**
	 * 得到互动课堂视频列表
	 * @param classId
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<SimpleDTO> getInteractLessons(@ObjectIdType(isRequire=false) ObjectId classId)
	{
	  List<SimpleDTO> retList=new ArrayList<SimpleDTO>();
	  List<InteractLessonEntry> list=null;
	  if(!UserRole.isStudentOrParent(getSessionValue().getUserRole())) //老师
	  {
	    list=interactLessonService.getInteractLessonEntryList(getUserId(), null, classId,Constant.NEGATIVE_ONE);
	  }
	  else
	  {
		  ObjectId studentId=getUserId();
		  if(UserRole.isParent(getSessionValue().getUserRole()))
		  {
			  UserEntry ue=  userService.searchUserId(getUserId());
			  studentId=ue.getConnectIds().get(0);
		  }
		  ClassEntry ce=  classService.getClassEntryByStuId(studentId, new BasicDBObject("_id",1));
		  if(null==ce)
		  {
			  logger.error("Can not find ClassEntry for student; the studentId="+studentId.toString());
			  return retList;
		  }
		  list=interactLessonService.getInteractLessonEntryList(null, null, classId,Constant.ONE);
	  }
	  //video_id->lesson
	  
	  if(null!=list && list.size()>0)
	  {
		  Map<ObjectId,InteractLessonEntry> map =new HashMap<ObjectId, InteractLessonEntry>();
		  
		  for(InteractLessonEntry e:list)
		  {
			  map.put(e.getVideoId(), e);
		  }
		  Map<ObjectId, VideoEntry> videoMap=videoService.getVideoEntryMap(MongoUtils.getFieldObjectIDs(list, "vi"), Constant.FIELDS);
		  for(Map.Entry<ObjectId, VideoEntry> entry:videoMap.entrySet())
		  {
			  SimpleDTO dto =new SimpleDTO();
			  dto.setId(entry.getValue().getID().toString());
			  dto.setValue(entry.getValue().getName());
			  dto.setValue1(entry.getValue().getImgUrl());
			  dto.setValue2(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO,entry.getValue().getBucketkey()));
			  dto.setValue3(map.get(entry.getKey()).getPush());
			  dto.setType(DateTimeUtils.convert(entry.getKey().getTime(),DateTimeUtils.DATE_YYYY_MM_DD));
			  dto.setSize(entry.getValue().getLength());
			  retList.add(dto);
		  }
		  
		  
		  Collections.sort(retList, new Comparator<SimpleDTO>() {
				@Override
				public int compare(SimpleDTO o1, SimpleDTO o2) {
					try
					{
						ObjectId obj1=new ObjectId(o1.getId().toString());
						ObjectId obj2=new ObjectId(o2.getId().toString());
						return obj2.compareTo(obj1);
					}catch(Exception ex)
					{
						
					}
					return 0;
				}
			});
	  }
	
	  return retList;
	}
	
	/**
	 * 得到班级
	 * @return
	 */
	@RequestMapping("/classes")
	@ResponseBody
	public List<IdValuePairDTO> getClassList()
	{ 
		List<IdValuePairDTO> retList =new ArrayList<IdValuePairDTO>();
		if(UserRole.isStudent(getSessionValue().getUserRole()))
		{
			ClassEntry e=classService.getClassEntryByStuId(getUserId(),Constant.FIELDS);
			retList.add(new IdValuePairDTO(e.getID(), e.getName()));
		}
		if(UserRole.isTeacher(getSessionValue().getUserRole()))
		{
		 List<InteractLessonEntry> list=interactLessonService.getInteractLessonEntryList(getUserId(), null, null,Constant.NEGATIVE_ONE);
		 Map<ObjectId, ClassEntry> classMap= classService.getClassEntryMap(MongoUtils.getFieldObjectIDs(list, "cid"), new BasicDBObject("nm",1));
		
		 for(Map.Entry<ObjectId, ClassEntry> entry:classMap.entrySet())
		 {
			 retList.add(new IdValuePairDTO(entry.getKey(), entry.getValue().getName()));
		 }
		}
		return retList;
	}
	
	
	/**
	 * 删除互动课堂
	 * @param id
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/remove")
	@ResponseBody
	public RespObj removeInteractLesson(@ObjectIdType ObjectId id)
	{
		InteractLessonEntry e=interactLessonService.getInteractLessonEntryByVideoId(id);
		if(null==e)
		{
			return new RespObj(Constant.FAILD_CODE, "参数错误");
		}
		if(!e.getUserId().equals(getUserId()))
		{
			return new RespObj(Constant.FAILD_CODE, "没有权限");
		}
		interactLessonService.removeInteractLessonEntry(e.getID());
		return RespObj.SUCCESS;
	}
	
	
	/**
	 * 推送互动课堂
	 * @param id
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push")
	@ResponseBody
	public RespObj pushInteractLesson(@ObjectIdType ObjectId id)
	{
		InteractLessonEntry e=interactLessonService.getInteractLessonEntryByVideoId(id);
		if(null==e)
		{
			return new RespObj(Constant.FAILD_CODE, "参数错误");
		}
		if(!e.getUserId().equals(getUserId()))
		{
			return new RespObj(Constant.FAILD_CODE, "没有权限");
		}
		interactLessonService.pushInteractLesson(e.getID());
		return RespObj.SUCCESS;
	}

	/**
	 * 新建互动课堂
	 * @param subjectId
	 * @return
	 */
	@RequestMapping("/addInteractLesson")
	@ResponseBody
	public String addInteractLesson(@ObjectIdType ObjectId subjectId)
	{
		String str = interactLessonService.addInteractLessonEntry(subjectId, new ObjectId(getSessionValue().getSchoolId()));
		return str;
	}

	/**
	 * 互动课堂下课
	 * @param subjectId
	 * @return
	 */
	@RequestMapping("/closeInteractLesson")
	@ResponseBody
	public void closeInteractLesson(@ObjectIdType ObjectId subjectId)
	{
		String currDate= DateTimeUtils.getCurrDate();
		String key= CacheHandler.getKeyString(CacheHandler.CACHE_TEACHER_INTERACT_LESSON, currDate + subjectId.toString());
		//String value = CacheHandler.getStringValue(key);
		CacheHandler.cache(key, "", Constant.SESSION_TWO_SECONDS);
		//CacheHandler.deleteKey(CacheHandler.CACHE_TEACHER_INTERACT_LESSON,currDate + subjectId.toString());
	}

	/**
	 * 对互动课堂编辑
	 * @param lessonId 互动课堂Id
	 * @param type:1:修改名称，2：开锁解锁，3：删除
	 * @param lessonName：互动课堂名称
	 * @return
	 */
	@RequestMapping("/editInteractLesson")
	@ResponseBody
	public RespObj editInteractLesson(@ObjectIdType ObjectId lessonId, int type, String lessonName)
	{
		int role= getSessionValue().getUserRole();
		if(UserRole.isNotStudentAndParent(role)) {
			interactLessonService.editInteractLessonEntry(lessonId, type, lessonName);
			return RespObj.SUCCESS;
		}else{
			return RespObj.FAILD;
		}
	}


	/**
	 * 查询学生活跃度
	 * @param lessonId
	 * @return
	 */
	@RequestMapping("/lessonActiviness")
	@ResponseBody
	public Map<String, Object> getLessonActivinessList(@ObjectIdType ObjectId lessonId,@ObjectIdType ObjectId classId)
	{
		Map<String, Object> map=activinessService.getLessonActivinessList(lessonId,classId);
		return map;
	}

	/**
	 * 新建学生活跃度
	 * @param req
	 * @return
	 */
	@RequestMapping("/activiness")
	@ResponseBody
	public RespObj activiness(HttpServletRequest req)
	{
		try {
			InputStream is = req.getInputStream();
			String contentStr= IOUtils.toString(is, "utf-8");
			String teacherId= getSessionValue().getId();
			activinessService.addActivinessEntry(contentStr,teacherId);
			return RespObj.SUCCESS;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 考试成绩分类
	 * @param req
	 * @return
	 */
	@RequestMapping("/scoreClassification")
	@ResponseBody
	public RespObj scoreClassify(HttpServletRequest req)
	{
		try {
			InputStream is = req.getInputStream();
			String contentStr= IOUtils.toString(is, "utf-8");
			interactLessonScoreClassifyService.addInteractLessonScoreClassifyEntry(contentStr);
			return RespObj.SUCCESS;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询学生考试-整卷模式
	 * @param lessonId
	 * @param classId
	 * @param times
	 * @return
	 */
	@RequestMapping("/getExamList")
	@ResponseBody
	public Map<String, Object> getInteractLessonExamList(@ObjectIdType ObjectId lessonId,@ObjectIdType ObjectId classId, @RequestParam(required=false,defaultValue="0") Integer times)
	{
		Map<String,Object> map=new HashMap<String, Object>();
		InteractLessonScoreClassifyDTO scoreClassifyDTO=interactLessonScoreClassifyService.getInteractLessonScoreClassifyDTO(lessonId, times);
		map.put("scoreClassify",scoreClassifyDTO);
		int type=2;//查询学生提交的考试信息
		interactLessonExamService.getInteractLessonExamList(lessonId, classId, type, times, map);

		return map;
	}

	/**
	 * 查询学生考试-单题模式-题目编号列表
	 * @param lessonId
	 * @param times
	 * @return
	 */
	@RequestMapping("/getExamQuestionNumberList")
	@ResponseBody
	public List<KeyValue> getExamQuestionNumberList(@ObjectIdType ObjectId lessonId,@RequestParam(required=false,defaultValue="0") Integer times)
	{
		int type=1;//查询老师提交的考试信息
		List<KeyValue> questionNumberList=interactLessonExamService.getExamQuestionNumberList(lessonId, type, times);
		return questionNumberList;
	}

	/**
	 * 查询考试试卷
	 * @param lessonId
	 * @param times
	 * @return
	 */
	@RequestMapping("/getInteractLessonExamText")
	@ResponseBody
	public List<InteractLessonExamDetailDTO> getInteractLessonExamText(@ObjectIdType ObjectId lessonId,@RequestParam(required=false,defaultValue="0") Integer times)
	{
		int type=1;//查询老师提交的考试信息
		List<InteractLessonExamDetailDTO> list=interactLessonExamService.getInteractLessonExamText(lessonId, type, times);
		return list;
	}

	/**
	 * 查询学生考试-单题模式
	 * @param lessonId
	 * @param classId
	 * @param times
	 * @param number
	 * @return
	 */
	@RequestMapping("/getSingleQuestionList")
	@ResponseBody
	public Map<String, Object> getInteractLessonSingleQuestionList(@ObjectIdType ObjectId lessonId,@ObjectIdType ObjectId classId,@RequestParam(required=false,defaultValue="0") Integer times,@RequestParam(required=false,defaultValue="0") Integer number)
	{
		int type=0;//查询学生提交的考试信息
		Map<String,Object> map=interactLessonExamService.getInteractLessonSingleQuestionList(lessonId, classId, type, times, number);
		return map;
	}

	/**
	 * 考试内容
	 * @param type ：1表示试题问题，2：学生答案
	 * @param req
	 * @return
	 */
	@RequestMapping("/examText")
	@ResponseBody
	public RespObj examText(int type, HttpServletRequest req)
	{
		try {
			InputStream is = req.getInputStream();
			String contentStr= IOUtils.toString(is, "utf-8");
			interactLessonExamService.addInteractLessonExamEntry(type, contentStr);
			return RespObj.SUCCESS;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询快速答题内容
	 * @param lessonId
	 * @param classId
	 * @param times
	 */
	@RequestMapping("/getStuQuickAnswerTextList")
	@ResponseBody
	public Map<String,Object> getStuQuickAnswerTextList(@ObjectIdType ObjectId lessonId, @ObjectIdType ObjectId classId, @RequestParam(required=false,defaultValue="0") Integer times)
	{
		Map<String,Object> map=interactLessonQuickAnswerService.getStuQuickAnswerTextList(lessonId, classId, times);
		return map;
	}

	/**
	 * 快速答题内容
	 * @param type ：1表示快速答题问题，2：学生答案
	 * @param req
	 * @return
	 */
	@RequestMapping("/quickAnswerText")
	@ResponseBody
	public RespObj quickAnswerText(int type, HttpServletRequest req)
	{
		try {
			InputStream is = req.getInputStream();
			String contentStr= IOUtils.toString(is, "utf-8");
			interactLessonQuickAnswerService.addInteractLessonQuickAnswerEntry(type, contentStr);
			return RespObj.SUCCESS;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询互动课堂上传文件
	 * @param lessonId
	 * @param type
	 * @param times
	 */
	@RequestMapping("/lessonUploadFile")
	@ResponseBody
	public List<InteractLessonFileDTO> getLessonUploadFileList(@ObjectIdType ObjectId lessonId, int type, @RequestParam(required=false,defaultValue="0") Integer times)
	{
		List<InteractLessonFileDTO> list=interactLessonFileService.getLessonUploadFileList(lessonId, type, times);
		return list;
	}

	/**
	 * 文件下载
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@SessionNeedless
	@RequestMapping("/downloadVideo")
	public void downloadVideo(@ObjectIdType ObjectId videoId, HttpServletRequest request, HttpServletResponse response) {
		try {
			interactLessonFileService.downFile(videoId, request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalParamException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 互动课堂上传文件
	 * @param type ：1:老师上传课件，2：老师上传考试试题，3：学生上传课件，4：学生上传考试答案
	 * @param req
	 * @return
	 */
	@RequestMapping("/file/upload")
	@ResponseBody
	public RespObj fileUpload(int type, MultipartFile Filedata, HttpServletRequest req)
	{
		try {
			String lessonId=req.getParameter("lessonId");
			String userId=req.getParameter("userId");
			String isVideo=req.getParameter("isVideo");
			String fileName=FilenameUtils.getName(Filedata.getOriginalFilename());
			int times=0;
			String number=req.getParameter("number");
			boolean isNum = number.matches("[0-9]+");
			if(isNum) {
				times =Integer.parseInt(number);
			}

			String key=lessonId+type+userId;
			if(fileTimesMap.get(key)==null){
				fileTimesMap.put(key,times);
			}else{
				times=fileTimesMap.get(key)+1;
				fileTimesMap.put(key,times);
			}

			int role=0;
			if(type==1||type==2){
				role=2;
			}
			if(type==3||type==4){
				role=1;
			}
			String path="";
			ObjectId videoId=null;
			if("Y".equals(isVideo)) {
				String vid = uploadVideo(Filedata);
				videoId=new ObjectId(vid);
			}else{
				ObjectId id =new ObjectId();
				String fileKey = id.toString()+Constant.POINT+FilenameUtils.getExtension(Filedata.getOriginalFilename());
				QiniuFileUtils.uploadFile(fileKey, Filedata.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
				path =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
			}
			InteractLessonFileEntry entry=new InteractLessonFileEntry(
					new ObjectId(lessonId),
					new ObjectId(userId),
					role,
					type,
					times,
					fileName,
					isVideo,
					videoId,
					path);
			interactLessonFileService.addInteractLessonFileEntry(entry);
			return RespObj.SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 互动课堂列表
	 * @param csid
	 * @param classid
	 * @param subjectid
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getInteractLessonList")
	public @ResponseBody Map getInteractLessonList(String csid,String classid,String subjectid,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
		Map map = new HashMap();
		try {
			int count = interactLessonService.getInteractLessonCount(csid, classid, subjectid, getSessionValue().getUserRole());
			List<InteractLessonDTO> interactLessonDTOs = interactLessonService.getInteractLessonList(csid, classid, subjectid, getSessionValue().getUserRole(), page, pageSize);
			map.put("rows", interactLessonDTOs);
			map.put("total", count);
			map.put("page", page);
			map.put("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
}
