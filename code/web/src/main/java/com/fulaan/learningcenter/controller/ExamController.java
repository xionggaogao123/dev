package com.fulaan.learningcenter.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.learningcenter.service.ExerciseItemService;
import com.fulaan.learningcenter.service.ExerciseService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.CommonMultipartFile;
import com.fulaan.utils.PageUtils;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.zouban.dto.ZouBanCourseDTO;
import com.google.common.base.Splitter;
import com.mongodb.BasicDBObject;
import com.pojo.app.*;
import com.pojo.exam.StudentExamResDTO;
import com.pojo.examresult.ExamResultEntry;
import com.pojo.exercise.*;
import com.pojo.exercise.ExerciseItemStateDTO.UserItemAnswerDTO;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.lesson.LessonEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.TeacherClassSubjectDTO;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.IdNamePair;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import java.io.*;
import java.util.*;

/**
 * 考试和课后小练习 controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/exam")
public class ExamController extends BaseController {
	private static final Logger logger =Logger.getLogger(ExamController.class);

	private UserService userService =new UserService();
	private ClassService classService =new ClassService();
	private ExerciseService exerciseService =new ExerciseService();
	private ExerciseItemService exerciseItemService =new ExerciseItemService();
	private TeacherClassSubjectService teacherClassSubjectService=new TeacherClassSubjectService();
	private LessonService lessonService =new LessonService();
	private DirService dirService =new DirService();
	@Autowired
	private ExamResultService examResultService;
	@Autowired
	private ExperienceService experienceService;
	@Autowired
	private InterestClassService interestClassService;



	/**
	 * 老师首页
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String manageExams(String newId,Map<String, Object> model) {
		/*List<TeacherClassSubjectDTO> list = teacherClassSubjectService.getTeacherClassSubjectDTOList(getUserId(), null);
		Map<ObjectId,IdValuePairDTO> map =new HashMap<ObjectId, IdValuePairDTO>();
		for(TeacherClassSubjectDTO teacherClass: list) {
			IdValuePairDTO classInfo = teacherClass.getClassInfo();
			if (classInfo.getId() != null) {
				if(!map.containsKey(classInfo.getId()))
				{
					map.put(classInfo.getId(), classInfo);
				}
			}
		}
		//株洲选修课
		*//*List<ZouBanCourseDTO> zouBanCourseEntryList = interestClassService.findZoubanCourseByTeacherId(getUserId());
		for(ZouBanCourseDTO zouBanCourseDTO : zouBanCourseEntryList){
			IdValuePairDTO classInfo = new IdValuePairDTO(new ObjectId(zouBanCourseDTO.getZbCourseId()),zouBanCourseDTO.getClassName());
			if(!map.containsKey(classInfo.getId())){
				map.put(classInfo.getId(),classInfo);
			}
		}*//*


		model.put("classList", map.values());*/

		List<IdNamePair> idNamePairs = classService.getAllClassByTeacherId(getUserId(), getSchoolId());
		List<IdValuePairDTO> classList = new ArrayList<IdValuePairDTO>();
		if(idNamePairs.size() > 0){
			for(IdNamePair idNamePair : idNamePairs){
				classList.add(new IdValuePairDTO(idNamePair.getId(), idNamePair.getName()));
			}
		}
		model.put("classList", classList);
		if(StringUtils.isNotBlank(newId))
		{
			model.put("newId", newId);
		}
        return "learningcenter/exam";
	}
	
	
	
	/**
	 * 老师查看学生考试
	 * @param did
	 * @return
	 * @throws IllegalParamException 
	 * @throws ResultTooManyException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("studentlist")
	public String examStudentList(@ObjectIdType ObjectId did,Map<String, Object> model) throws ResultTooManyException, IllegalParamException
	{
		
		ExerciseEntry e=exerciseService.getExerciseEntry(did);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		model.put("name", e.getName());
		List<StudentExamResDTO> list= exerciseService.getStudentExamResDTOs( e );
		model.put("id", did.toString());
	    model.put("list", list);
		return "learningcenter/studentlist";
	}
	
	
	/**
	 * 批改学生试卷
	 * @param did
	 * @param model
	 * @return
	 * @throws ResultTooManyException
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/correct/student")
	public String correntStudentExam(@ObjectIdType ObjectId did,@ObjectIdType ObjectId ui,Map<String, Object> model,HttpServletRequest request) throws ResultTooManyException, IllegalParamException
	{
		
		ExerciseEntry e= exerciseService.getExerciseEntry(did);
		if(null==e || !e.getTeacherId().equals(getUserId()))
		{
			throw new IllegalParamException();
		}
	
		bindPaths(request, model, e);
		List<StudentExerciseAnswerDTO> list= exerciseItemService.getStudentExerciseAnswerDTOs(did,ui);
		
		//todo 七牛功能放到sys中可以移除此代码
		for(StudentExerciseAnswerDTO dto:list)
		{
			for(SimpleDTO simpleDTP:dto.getIamge())
			{
				if(null!=simpleDTP.getValue())
				{
					try
					{
						down(request,simpleDTP.getValue().toString(),QiniuFileUtils.TYPE_IMAGE);
						simpleDTP.setValue("/upload/exam/"+simpleDTP.getValue().toString());
					}catch(Exception ex)
					{
						logger.error("", ex);
					}
				}
			}
		}
		
		model.put("did", did.toString());
	    model.put("list", list);
		return "learningcenter/correct_student_exam";
	}
	/**
	 * 设置好的试卷
	 * @param did
	 * @param uid
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/g/add")
	@ResponseBody
	public RespObj addGoodPaper(@ObjectIdType ObjectId did,@ObjectIdType ObjectId uid)
	{
		exerciseService.addGoodUser(did, uid);
		return RespObj.SUCCESS;
	}
	
	/**
	 * 去除好的试卷
	 * @param did
	 * @param uid
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/g/remove")
	@ResponseBody
	public RespObj removeGoodPaper(@ObjectIdType ObjectId did,@ObjectIdType ObjectId uid)
	{
		exerciseService.removeGoodUser(did, uid);
		return RespObj.SUCCESS;
	}

	/**
	 * 需要老师权限
	 * 上传考试试卷或者课后小练习
	 * @param name
	 * @param classes
	 * @param examDoc
	 * @param parseDoc
	 * @param type 1为考试 2课后小练习
	 * @return
	 * @throws IllegalParamException
	 * @throws IOException
	 * @throws FileUploadException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping(value = "/testpaper/upload")
	@ResponseBody
	public String uploadTestpaper(String name,String classes,@RequestParam(required=false) MultipartFile examDoc,@RequestParam(required=false) MultipartFile parseDoc,@DefaultValue("1") int type,HttpServletRequest req) throws IllegalParamException, IOException, FileUploadException
	{
		if(!ValidationUtils.isRequestTestPaper(name))
		{
			throw new IllegalParamException();
		}
		Set<ObjectId> submitCalssIdSet=new HashSet<ObjectId>();
		if(Constant.ONE==type)
		{
			if(StringUtils.isBlank(classes))
			{
				throw new IllegalParamException();
			}
			Iterable<String> result = Splitter.on(Constant.COMMA)
					.omitEmptyStrings()
					.split(classes);
			for(String classId:result)
			{
				if(null==classId || !ObjectId.isValid(classId))
				{
					throw new IllegalParamException();
				}
				submitCalssIdSet.add(new ObjectId(classId));
			}
		}
		/**
		 * 上传考试试卷
		 */
		ObjectId docId=new ObjectId();
		try
		{
			exerciseService.convertPdfAndSwfFile(req,examDoc, docId);
		}catch(Exception ex)
		{
			logger.error("", ex);
			throw new FileUploadException("Can not upload files!!the teacher= "+getUserId());
		}
		/**
		 * 上传解析试卷
		 */
		String parseDocIdStr=null;
		if(null!=parseDoc)
		{
			try
			{
				ObjectId parseDocId=new ObjectId();
				exerciseService.convertPdfAndSwfFile(req,parseDoc, parseDocId);
				parseDocIdStr=parseDocId.toString();
			}catch(Exception ex)
			{
				QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT,docId.toString()+".doc");
				QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT,docId.toString()+".docx");
				QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT,docId.toString()+".pdf");
				QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT,docId.toString()+".swf");
				logger.error("", ex);
				throw new FileUploadException("Can not upload parse files!!the teacher= "+getUserId());
			}
		}

		ExerciseEntry e =new ExerciseEntry(
				type,
				getUserId(),
				new ArrayList<ObjectId>(submitCalssIdSet),
				name,
				docId.toString(),
				parseDocIdStr);
		ObjectId id=exerciseService.uploadTestPaper(e);
		logger.info(getSessionValue()+" upload test paper:"+id.toString());

		//把考试信息导入examresult表
		if(Constant.ONE == type) {
			ExamResultEntry examResultEntry = new ExamResultEntry();
			examResultEntry.setName(name);
			examResultEntry.setType("其他");
			List<ObjectId> classList = new ArrayList<ObjectId>();
			Map<ObjectId, ClassEntry> cMap=	classService.getClassEntryMap(submitCalssIdSet, Constant.FIELDS);
			if(null==cMap || cMap.size()==0)
			{
				return id.toString();
			}
			List<ClassEntry> ClassEntryList=new ArrayList<ClassEntry>(cMap.values());
			for(ClassEntry classEntry : ClassEntryList){
				if(classEntry != null){
					classList.add(classEntry.getID());
				}
			}
			ClassEntry classEntry = ClassEntryList.get(0);
			ObjectId gradeId = classEntry.getGradeId();
			examResultEntry.setGradeId(gradeId);
			examResultEntry.setSchoolId(new ObjectId(getSessionValue().getSchoolId()));
			examResultEntry.setClassList(classList);
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String schoolYear;
			if (month < 9 && month > 2) {
				schoolYear = (year - 1) + "-" + year + "学年第二学期";
			} else if(month >= 9){
				schoolYear = year + "-" + (year + 1) + "学年第一学期";
			} else {
				schoolYear = (year - 1) + "-" + year + "学年第一学期";
			}
			examResultEntry.setSchoolYear(schoolYear);
			examResultEntry.setDate(year + "-" + month + "-" + day);
			examResultEntry.setIsGradeExam(2);
			Set<ObjectId> subjectSet = new HashSet<ObjectId>();
			List<TeacherClassSubjectDTO> teacherClassSubjectDTOList = teacherClassSubjectService.getTeacherClassSubjectDTOList(getUserId(), classList);
			for(TeacherClassSubjectDTO tcs : teacherClassSubjectDTOList) {
				subjectSet.add(tcs.getSubjectInfo().getId());
			}
			List<ObjectId> subjectList = new ArrayList<ObjectId>(subjectSet);
			examResultEntry.setSubjectList(subjectList);
			examResultEntry.setExerciseId(id);
			examResultService.addExam(examResultEntry, subjectList, 100, 60);

			ExpLogType expLogType = ExpLogType.UPLOADQUIZ;
			experienceService.updateNoRepeat(getUserId().toString(), expLogType,id.toString());
		}

		return id.toString();
	}

	/**
	 * 需要老师权限；
	 * 查询考试list
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("teacher/list")
	@ResponseBody
	public PageDTO<ExerciseDTO> getTeacherDTOList(int skip,int limit)
	{
		return exerciseService.getPageDTO(Constant.ONE, getUserId(),skip,limit);
	}

	/**
	 * 需要老师权限
	 * 作业中添加一个班级
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/class/add")
	@ResponseBody
	public RespObj addClass(@ObjectIdType ObjectId id,@ObjectIdType ObjectId classId)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		ExerciseEntry e= exerciseService.getExerciseEntry(id);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!e.getTeacherId().equals(getUserId()))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		Set<ObjectId> alreadyClassSet =new HashSet<ObjectId>(e.getClassIds());
		if(alreadyClassSet.contains(classId))
		{
			obj.setMessage("已经推送过");
			return obj;
		}

		ClassEntry ce=classService.getClassEntryById(classId, new BasicDBObject("teas",1));
		if(null==ce)
		{
			obj.setMessage("参数错误");
			return obj;
		}

		Set<ObjectId> teachers =new HashSet<ObjectId>(ce.getTeachers());
		if(!teachers.contains(getUserId()))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		exerciseService.addClassId(id, classId);
		examResultService.addClassPerformance(id, classId);
		return RespObj.SUCCESS;
	}

	/**
	 * 需要权限检查
	 * @param id
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/delete")
	@ResponseBody
	public RespObj delete(@ObjectIdType ObjectId id)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		ExerciseEntry e= exerciseService.getExerciseEntry(id);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!e.getTeacherId().equals(getUserId()))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(e.getSubmitStudents().size()>0)
		{
			//obj.setMessage("该考试已经有学生提交答案，不能删除");
			//return obj;
			logger.error("student already do,but teacher delete test paper!");
			logger.error(e);
		}
		//删除对应examresult中的文档
		ExamResultEntry examResultEntry = examResultService.getExamResultEntryByEid(e.getID());
		if(examResultEntry != null)
			examResultService.managerDeleteExam(examResultEntry.getID());

		//删除练习文档
		exerciseService.delete(id, getUserId());
		//删除练习题目
		exerciseItemService.deleteExerciseItems(id);

		try {
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getQuestionPath()+".doc");
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getQuestionPath()+".docx");
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getQuestionPath()+".pdf");
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getQuestionPath()+".swf");
		} catch (IllegalParamException e1) {
			logger.error("delete question doc error;"+e);
			logger.error("", e1);
		}

		try {
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getAnswerPath()+".doc");
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getAnswerPath()+".docx");
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getAnswerPath()+".pdf");
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_DOCUMENT, e.getAnswerPath()+".swf");
		} catch (IllegalParamException e1) {

			logger.error("delete answer doc error;"+e);
			logger.error("", e1);
		}
		return RespObj.SUCCESS;
	}

	/**
	 * 替换文件
	 * @param wordexerciseId
	 * @param type 1为文件 2为解析文件
	 * @return
	 * @throws IllegalParamException
	 * @throws IOException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/file/replace")
	@ResponseBody
	public RespObj replaceFile(@ObjectIdType ObjectId wordexerciseId,Integer type,MultipartHttpServletRequest request) throws IllegalParamException, IOException
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		ExerciseEntry e= exerciseService.getExerciseEntry(wordexerciseId);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!e.getTeacherId().equals(getUserId()))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(Constant.ONE!=type && Constant.TWO!=type)
		{
			obj.setMessage("参数错误");
			return obj;
		}

		MultipartFile file=request.getFile("file");
		if(null==file || file.getSize()==0L)
		{
			obj.setMessage("文件错误");
			return obj;
		}
		if(Constant.ONE==type) //更换试卷
		{
			try
			{
				exerciseService.convertPdfAndSwfFile(request,file,new ObjectId(e.getQuestionPath()));
			}catch(Exception ex)
			{
				obj.setMessage("文件上传失败");
				return obj;
			}
		}
		if(Constant.TWO==type) //更换解析
		{
			ObjectId newid=new ObjectId();
			String path=e.getAnswerPath();
			if(null!=path && ObjectId.isValid(path))
			{
				newid=new ObjectId(path);
			}
			try
			{
				exerciseService.convertPdfAndSwfFile(request,file,newid);
			}catch(Exception ex)
			{
				obj.setMessage("文件上传失败");
				return obj;
			}

			FieldValuePair fvp =new FieldValuePair("ap", newid.toString());
			exerciseService.update(e.getID(), fvp);
		}
		return RespObj.SUCCESS;
	}

	/**
	 * 更改状态
	 * @param id
	 * @param state
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/state/update")
	@ResponseBody
	public RespObj updateState(@ObjectIdType ObjectId id,int state)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		ExerciseEntry e= exerciseService.getExerciseEntry(id);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!e.getTeacherId().equals(getUserId()))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(Constant.ONE!=state && Constant.ZERO!=state)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		FieldValuePair fvp =new FieldValuePair("st", state);
		exerciseService.update(id, fvp);
		return RespObj.SUCCESS;
	}

	/**
	 * 考试视图，用于学生答题,查看考试结果和老师题目配置 ，以及课后小练习配置
	 * @param request
	 * @param id
	 * @param type 0老师 配置  1 学生答题  2 学生查看答案 
	 * @param lesson 有此参数说明是课后小练习编辑，需要跳转至/lesson/edit.do?lessonId=546175c37f722d5cac86f94c页面 ；或者是学生作答课后小练习，跳转至 lesson/view.do?lessonId=552cd97c7f722d5cac8775ac
	 * @param model
	 * @return
	 * @throws IllegalParamException
	 * @throws PermissionUnallowedException
	 */
	@RequestMapping("/view")
	public String examView(HttpServletRequest request,@ObjectIdType ObjectId id,@RequestParam(defaultValue="0",required=false) Integer type,
						   @RequestParam(defaultValue="",required=false) String lesson, @RequestParam(defaultValue="",required=false) String homework,
						   @ObjectIdType(isRequire=false) ObjectId stuid   ,        Map<String, Object> model,
						   @RequestParam(required = false, defaultValue = "1") int tty) throws IllegalParamException, PermissionUnallowedException
	{
		try {
			buildExamDetail(request, id, type, lesson, homework, stuid, model);
			model.put("tty", tty);
		} catch (Exception e){
			e.printStackTrace();
		}
		String res="learningcenter/load_conf_exam";
		if(type==Constant.ONE) res="learningcenter/exam_view";
		if(type==Constant.TWO) res="learningcenter/stu_exam_result";
		return res;
	}


	/**
	 * 考试视图，应用于移动端
	 * @param request
	 * @param id
	 * @return
	 * @throws IllegalParamException
	 * @throws PermissionUnallowedException
	 */
	@RequestMapping("/mobile/view")
	@ResponseBody
	public Map<String, Object> mobileExamView(HttpServletRequest request,@ObjectIdType ObjectId id) throws IllegalParamException, PermissionUnallowedException
	{
		Map<String, Object> model =new HashMap<String, Object>();
		buildExamDetail(request, id, 0, "","",null, model);
		return model;
	}





	private void buildExamDetail(HttpServletRequest request, ObjectId id,
								 Integer type, String lesson, String homework,ObjectId stuid, Map<String, Object> model)
			throws IllegalParamException {
		//如果是PC，学生客户端隐藏左右箭头
		String client = request.getHeader("User-Agent");
		Platform pf = null;
		if (client.contains("iOS")) {
			pf = Platform.IOS;
		} else if (client.contains("Android")){
			pf = Platform.Android;
		} else {
			pf = Platform.PC;
		}
		
		model.put("isShow", !pf.equals(Platform.PC));
		
		ExerciseEntry e=exerciseService.getExerciseEntry(id);
		if(null==e)
		{
			throw new IllegalParamException();
		}


		ObjectId studentId=getUserId();
		if(UserRole.isParent(getSessionValue().getUserRole()))
		{
			UserEntry ue=userService.searchUserId(getUserId());
			studentId=ue.getConnectIds().get(0);
		}



		model.put("id", e.getID().toString());
		model.put("time", e.getTotalTime() > 0 ? e.getTotalTime() : 30);
		model.put("ui", getUserId().toString());
		model.put("lesson", lesson);
		model.put("homework", homework);
		model.put("dld", e.getDownLoad());
		model.put("date", DateTimeUtils.convert(e.getDate(), DateTimeUtils.DATE_YYYY_MM_DD));
		model.put("missdeadline", e.getTimeType() == 2 ? System.currentTimeMillis() > e.getDate() + 24 * 60 * 60 * 1000 : false);

		bindPaths(request, model, e);

		if (type == Constant.ZERO || type == Constant.ONE) //用于学生答题和老师题目配置
		{
			List<ExerciseItemDTO> list =new ArrayList<ExerciseItemDTO>();
			List<ExerciseItemEntry> elist=	exerciseItemService.getExerciseItemEntrys(e.getID(),Constant.FIELDS);
			double totalScore=0D;
			ExerciseItemDTO dto;
			for(ExerciseItemEntry eit:elist)
			{
				dto=new ExerciseItemDTO(eit);
				totalScore+=dto.getTotalScore();
				list.add(dto);
			}
			model.put("totalScore", totalScore);

			Collections.sort(list, new Comparator<ExerciseItemDTO>() {
				@Override
				public int compare(ExerciseItemDTO o1, ExerciseItemDTO o2) {
					return Integer.valueOf(o1.getTitleId())-Integer.valueOf(o2.getTitleId());
				}
			});
			if(list.isEmpty())
			{
				list.add(ExerciseItemDTO.create());
			}
			model.put("itemList", list);
		}
		if(type==Constant.TWO) //查看考试结果
		{
			studentId=(null==stuid?studentId:stuid);
			List<ExerciseAnswerDTO> list=exerciseItemService.getStudentDocumentAnswerDTOs(studentId, e.getID(), false);
			request.setAttribute("itemList", list);
		}
		if(type==Constant.ONE && e.getTimeType() == 2) //查看保存结果
		{
			studentId=(null==stuid?studentId:stuid);
			List<ExerciseAnswerDTO> list=exerciseItemService.getStudentDocumentAnswerDTOs(studentId, e.getID(), true);
			model.put("temps", list);
			Map<String, ExerciseAnswerDTO> map = new HashMap<String, ExerciseAnswerDTO>();
			for(ExerciseAnswerDTO dto : list){
				map.put(dto.getId(), dto);
			}
			List<ExerciseItemDTO> exerciseItemDTOs = (List<ExerciseItemDTO>)model.get("itemList");
			for(ExerciseItemDTO exerciseItemDTO : exerciseItemDTOs){
				List<ExerciseItemDTO.ItemDTO> itemDTOs =  exerciseItemDTO.getList();
				for(ExerciseItemDTO.ItemDTO itemDTO : itemDTOs){
					ExerciseAnswerDTO answerDTO = map.get(itemDTO.getId());
					itemDTO.setUserAnswer(answerDTO.getUserAnswer());
					itemDTO.setImageList(answerDTO.getImageList());
				}
			}
		}
	}


	/**
	 * 查看答案解析
	 * @param id
	 * @param model
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/parse/view")
	public String viewPaseAnswer(HttpServletRequest request, @ObjectIdType ObjectId id,Map<String, Object> model) throws PermissionUnallowedException, IllegalParamException
	{
		ExerciseEntry e=exerciseService.getExerciseEntry(id);
		if(null==e )
		{
			throw new IllegalParamException();
		}
		if(!e.getTeacherId().equals(getUserId()))
		{
			//todo
			//什么情况下其他人可以查看解析
			if(e.getState()!=Constant.THREE)
			{
				throw new PermissionUnallowedException();
			}
		}

		model.put("id", e.getID().toString());
		model.put("time", e.getTotalTime());

		try
		{
			down(request,e.getAnswerPath()+".swf",QiniuFileUtils.TYPE_IMAGE);
			model.put("ansPath", "/upload/exam/"+e.getAnswerPath()+".swf");
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		return "learningcenter/parse_view";
	}


	/**
	 * 检查试卷是否可以再次配置
	 * @param id
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping(value="/canconf",method = RequestMethod.POST)
	@ResponseBody
	public RespObj isCanConf(@ObjectIdType ObjectId id)
	{
		RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
		ExerciseEntry e= exerciseService.getExerciseEntry(id);
		if(null!=e && e.getSubmitStudents().size()==Constant.ZERO)
		{
			respObj.setMessage(e.getTimeType());
			return respObj;
		}
		return RespObj.FAILD;
	}


	/**
	 * 老师配置题目
	 * @param
	 * @param dtos 其中要求titleId为1-4的形式
	 *   @param  timeType 1:限时 比如30分钟    2：限定截止日期
	 *   @param  date  截止日期
	 *   @param  downLoad  是否支持下载
	 * @return
	 * @throws Exception
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping(value="/conf",method = RequestMethod.POST)
	@ResponseBody
	public RespObj configDocTitles( @RequestBody ExerciseItemDTO[] dtos, @RequestParam(required = false, defaultValue = "1") int timeType,
									@RequestParam(required = false, defaultValue = "")String date, int downLoad)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		if(null==dtos || dtos.length==0)
		{
			obj.setMessage("请配置题目");
			return obj;
		}
		ExerciseEntry e= exerciseService.getExerciseEntry(new ObjectId(dtos[0].getDocId()));
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!e.getTeacherId().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		List<ExerciseAnswerEntry> list=null;
		try {
			list = exerciseItemService.getListByDocIdAndItemId(e.getID(), null,null, Constant.FIELDS);
		} catch (ResultTooManyException e1) {
			logger.error("", e1);
		}
		if(null!=list && !list.isEmpty())
		{
			obj.setMessage("该考试已经有学生提交答案，不能重新配置");
			return obj;
		}

		logger.info("exam ["+e.getID().toString()+" ] will be resetted!!");

		exerciseItemService.deleteExerciseItems(e.getID());

		List<ExerciseItemEntry> itemList =new ArrayList<ExerciseItemEntry>(dtos.length);

		double totalScore=0D;
		int totalTime =0;
		for(ExerciseItemDTO dto:dtos)
		{
			try {
				if(StringUtils.isNotBlank(dto.getTitleId()))
				{
					totalScore+=dto.getTotalScore();
					itemList.add(dto.convert());
				}
				else
				{
					totalTime=dto.getTime();
				}
			} catch (Exception ex) {
				logger.error("", ex);
				obj.setMessage("题目配置出错了");
				return obj;
			}
		}
		exerciseItemService.addEntrys(itemList);
		Date endDate;
		if(date.equals("")){
			endDate = new Date();
		} else {
			endDate = DateTimeUtils.stringToDate(date, DateTimeUtils.DATE_YYYY_MM_DD);
		}
		exerciseService.update(e.getID(), new FieldValuePair("st", Constant.ONE),new FieldValuePair("ts", totalScore),new FieldValuePair("tt", totalTime),
				new FieldValuePair("tty", timeType), new FieldValuePair("dt", endDate.getTime()), new FieldValuePair("dld", downLoad));
		//更新满分及格分
		ExamResultEntry examResultEntry = examResultService.getExamResultEntryByEid(e.getID());
		if(examResultEntry != null)
			examResultService.editFullFailScore(examResultEntry.getID(), examResultEntry.getSubjectList().get(0), (int)totalScore, (int)(totalScore*0.6));
		return RespObj.SUCCESS;

	}

	/**
	 * 得到答案统计list
	 * @param id
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws IllegalParamException
	 * @throws ResultTooManyException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/answer/stat/list")
	public String getList(@ObjectIdType ObjectId id,Map<String, Object> model,HttpServletRequest request,
						  @RequestParam(defaultValue="0",required=false) int type,
						  @RequestParam(defaultValue = "class",required = false) String classId) throws PermissionUnallowedException, IllegalParamException, ResultTooManyException
	{

		ExerciseEntry e= exerciseService.getExerciseEntry(id);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		//不带课校长有权限查看试卷，modify bu miaoqiang
		if(!UserRole.isHeadmaster(getSessionValue().getUserRole())) {
			if (!e.getTeacherId().equals(getUserId()) && e.getType() == Constant.ONE) //考试需要检查权限
			{
				throw new PermissionUnallowedException();
			}
		}
		model.put("name", e.getName());
		model.put("docId", id.toString());
		Set<ObjectId> studentIds = new HashSet<ObjectId>();
		if(classId.equals("class")){
			studentIds = null;
		} else {
			ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
			if (classEntry == null) {
				List<UserDetailInfoDTO> studentList = classService.findStuByInterestClassId(classId);
				for (UserDetailInfoDTO userDetailInfoDTO : studentList) {
					studentIds.add(new ObjectId(userDetailInfoDTO.getId()));
				}
			} else {
				studentIds = new HashSet<ObjectId>(classEntry.getStudents());
			}
		}

		List<ExerciseItemStateDTO> list= exerciseItemService.getDocumentTitleAndAnswerDTOList(id,studentIds) ;
		request.setAttribute("statList", list);
		if(type == 1){//表示作业
			return "homework/exam_answer_stat";
		}
		return "learningcenter/exam_answer_stat";
	}

	/**
	 * 得到答案统计list  //移动端使用
	 * @param id
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws IllegalParamException
	 * @throws ResultTooManyException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/answer/stat/list/mobile")
	@ResponseBody
	public Map<String, Object> getList(@ObjectIdType ObjectId id,@ObjectIdType ObjectId classId) throws PermissionUnallowedException, IllegalParamException, ResultTooManyException
	{
		Map<String, Object> model = new HashMap<String, Object>();
		ExerciseEntry e= exerciseService.getExerciseEntry(id);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		if(!e.getTeacherId().equals(getUserId()) && e.getType()==Constant.ONE ) //考试需要检查权限
		{
			throw new PermissionUnallowedException();
		}
		model.put("name", e.getName());
		List<ExerciseItemStateDTO> list= exerciseItemService.getDocumentTitleAndAnswerDTOList(id, null) ;
		model.put("statList", list);
		ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
		List<ObjectId> stu = classEntry.getStudents();
		model.put("totalStu", stu.size());
		return model;
	}



	/**
	 * 课后小练习统计
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/answer/stat/list/data")
	@ResponseBody
	public Map<String,Object> getAnswerStatListData(@ObjectIdType ObjectId id,@ObjectIdType ObjectId lessonid,@ObjectIdType ObjectId classId) throws PermissionUnallowedException, IllegalParamException, ResultTooManyException
	{
		Map<String,Object> retMap = new HashMap<String, Object>();
		ExerciseEntry e= exerciseService.getExerciseEntry(id);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		//不带课校长有权限查看试卷，modify bu miaoqiang
		if(!UserRole.isHeadmaster(getSessionValue().getUserRole())) {
			if (!e.getTeacherId().equals(getUserId()) && e.getType() == Constant.ONE) {
				throw new PermissionUnallowedException();
			}
		}
//        Set<ObjectId> studentIds =getStudentIdsByLessonId(lessonid);
		Set<ObjectId> studentIds = new HashSet<ObjectId>();
		ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
		if(classEntry == null){
			List<UserDetailInfoDTO> studentList = classService.findStuByInterestClassId(classId.toString());
			for(UserDetailInfoDTO userDetailInfoDTO : studentList){
				studentIds.add(new ObjectId(userDetailInfoDTO.getId()));
			}
		} else {
			studentIds = new HashSet<ObjectId>(classEntry.getStudents());
		}

        List<ExerciseItemStateDTO> list= exerciseItemService.getDocumentTitleAndAnswerDTOList(id,studentIds) ;
        retMap.put("name", e.getName());
        retMap.put("statList",list);
		retMap.put("totalCount", studentIds.size());
        return retMap;
    }
    
    
    /**
     * 根据班级课程查找该班学生ID
     * @param lessonId
     * @return
     */
    private Set<ObjectId> getStudentIdsByLessonId(ObjectId lessonId)
    {
        LessonEntry lessonEntry = lessonService.getLessonEntry(lessonId);
        if(null!=lessonEntry)
        {
	        DirEntry dirEntry = dirService.getDirEntry(lessonEntry.getDirId(),null);
	        //现在只处理班级课程情况
	        if(dirEntry.getType() == DirType.CLASS_LESSON.getType())
	        {
		        TeacherClassSubjectEntry teacherClassSubjectEntry = teacherClassSubjectService.getTeacherClassSubjectEntry(dirEntry.getOwerId());
		        ObjectId classId = teacherClassSubjectEntry.getClassInfo().getId();
		        //得到班级
		        ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
		        return  new HashSet<ObjectId>(classEntry.getStudents());
	        }
        }
        return null;
    }

	/**
	 *
	 * @param docId 文档的ID；试卷ID
	 * @param titleId 题目ID
	 * @param model
	 * @param lesson 若带有此参数，则表示是小练习，跳转至/lesson/stat.do?lessonId=550277de7f722d5cac875c27页面
	 * @param classId 有此参数表示作业配置的练习
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/item/answer")
	public String itemAmswer(HttpServletRequest request,@ObjectIdType ObjectId docId,@RequestParam(defaultValue="",required=false) String lesson,
							 @ObjectIdType ObjectId titleId,@ObjectIdType(isRequire=false) String classId,Map<String, Object> model) throws PermissionUnallowedException, IllegalParamException
	{
		ExerciseEntry e= exerciseService.getExerciseEntry(docId);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		//不带课校长有权限查看试卷，modify bu miaoqiang
		if(!UserRole.isHeadmaster(getSessionValue().getUserRole())) {
			if (!e.getTeacherId().equals(getUserId()) && e.getType() == Constant.ONE) {
				throw new PermissionUnallowedException();
			}
		}
		bindPaths(request, model, e);
		model.put("titleId", titleId.toString());
		model.put("docId", docId.toString());
		model.put("lesson", lesson);
		model.put("classId", classId);

		return "learningcenter/item_answer_stat";
	}
	/**
	 * 得到某题目的具体答题情况
	 * @param docId 文档的ID；试卷ID
	 * @param titleId 题目ID
	 * @param type -1;上一题； 0本题目；1下一题；
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws IllegalParamException
	 * @throws ResultTooManyException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/item/answer/stat")
	@ResponseBody
	public ExerciseItemStateDTO itemState(HttpServletRequest req, @ObjectIdType ObjectId docId,@ObjectIdType ObjectId titleId,
										  @RequestParam(required=false,defaultValue="0") int type,
										  @ObjectIdType(isRequire=false) ObjectId lessonId,
										  @ObjectIdType(isRequire=false) ObjectId classId) throws PermissionUnallowedException, IllegalParamException, ResultTooManyException
	{
		ExerciseEntry e= exerciseService.getExerciseEntry(docId);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		//不带课校长有权限查看试卷，modify bu miaoqiang
		if(!UserRole.isHeadmaster(getSessionValue().getUserRole())) {
			if (!e.getTeacherId().equals(getUserId()) && e.getType() == Constant.ONE) {
				throw new PermissionUnallowedException();
			}
		}
		if(Constant.ZERO!=type)
		{
			List<ExerciseMixItem> list= exerciseItemService.getDocumentTitleEntrys(docId);
			int index=0;
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i).getItemId().equals(titleId))
				{
					index=i;
					break;
				}
			}
			if(type==Constant.TWO)
			{
				index=index-1;
				index=index<Constant.ZERO?Constant.ZERO:index;
				titleId=list.get(index).getItemId();
			}

			if(type==Constant.ONE)
			{
				index=index+1;
				index=index>list.size()-1?list.size()-1:index;
				titleId=list.get(index).getItemId();
			}
		}

		Set<ObjectId> studentIds = new LinkedHashSet<ObjectId>();
		if(null != classId) {
			ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
			if(classEntry != null) {
				studentIds = new HashSet<ObjectId>(classEntry.getStudents());
			}else{
				ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(classId);
				studentIds.addAll(zouBanCourseEntry.getStudentList());
			}
		}else if(null!=lessonId) {
			studentIds=getStudentIdsByLessonId(lessonId);
		} else {
			List<ObjectId> classIds = e.getClassIds();
			Map<ObjectId, ClassEntry> classEntryMap = classService.getClassEntryMap(classIds, Constant.FIELDS);
			for(ClassEntry classEntry : classEntryMap.values()){
				studentIds.addAll(classEntry.getStudents());
			}
		}

		ExerciseItemStateDTO stat= exerciseItemService.getDocumentTitleAndAnswerDTO(docId,titleId,studentIds) ;
		
		
		
		//todo 七牛功能放到sys中可以移除此代码
		for(UserItemAnswerDTO dto:stat.getUserAnswerList())
		{
			for(IdValuePairDTO idValuePairDTO:dto.getImageList())
			{
				if(null!=idValuePairDTO.getValue())
				{
					try
					{
						down(req,idValuePairDTO.getValue().toString(),QiniuFileUtils.TYPE_IMAGE);
						idValuePairDTO.setValue("/upload/exam/"+idValuePairDTO.getValue().toString());
					}catch(Exception ex)
					{
						logger.error("", ex);
					}
				}
			}
		}
		return stat;
	}

	/**
	 * 56692a3863e7f4fcd28963ee-2,56692a1b63e7f4fcd28963e8-2,56692a6263e7f4fcd28963f9-2
	 * 老师评分
	 * @param sc : id-2,id-4
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/item/update")
	@ResponseBody
	public RespObj updateScore(String sc)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);

		if(StringUtils.isBlank(sc))
		{
			obj.setMessage("请给学生评分");
			return obj;
		}
		List<IdValuePair> list =new ArrayList<IdValuePair>();

		String[] scArr=sc.split(Constant.COMMA);
		for(String scStr:scArr)
		{
			String[] arr=scStr.split(Constant.SEPARATE_LINE);
			if(null == arr[0]  || !ObjectId.isValid(arr[0]))
			{
				obj.setMessage("参数错误");
				return obj;
			}
			list.add(new IdValuePair(new ObjectId(arr[0]), Double.valueOf(arr[1])));
		}
		List<ObjectId> ids=MongoUtils.getFieldObjectIDs(list, "id");
		List<ExerciseAnswerEntry> elist=exerciseItemService.getList(ids, new BasicDBObject("di",1).append("ti", 1));
		//ExerciseAnswerEntry._id --> ExerciseAnswerEntry.ti
		Map<ObjectId,ObjectId> map =new HashMap<ObjectId, ObjectId>();

		for(ExerciseAnswerEntry eae:elist)
		{
			map.put(eae.getID(), eae.getTitleId());
		}

		List<ObjectId> eids=MongoUtils.getFieldObjectIDs(elist, "di");

		Set<ObjectId> idSet=new HashSet<ObjectId>(eids);
		if(idSet.size()!=Constant.ONE)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		ExerciseEntry ee=exerciseService.getExerciseEntry(eids.get(0));
		//放开编辑
		//if(null==ee || !ee.getTeacherId().equals(getUserId()))
		if(null==ee)
		{
			obj.setMessage("参数错误");
			return obj;
		}

		Map<ObjectId,ExerciseMixItem> mixItemMap=exerciseItemService.getExerciseMixItemList(ee.getID());
		ExerciseMixItem item;
		ObjectId itemId;
		//先检查
		for(IdValuePair ip:list)
		{
			try
			{
				itemId=map.get(ip.getId());
				item=mixItemMap.get(itemId);
				if((Double)ip.getValue() > item.getScore())
				{
					obj.setMessage("分数超过最高分数了");
					return obj;
				}
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		for(IdValuePair ip:list)
		{
			try
			{
				exerciseItemService.updateStudentAnswerScore(ip);
				ExerciseAnswerEntry e = exerciseItemService.getExerciseAnswerEntry(ip.getId());
				Double score = exerciseItemService.getStuScore(e.getDocumentId(), e.getUserId());
				examResultService.updateStuScore(e.getDocumentId(), e.getUserId(), score);
				//避免服务器瞬间过大负载
				Thread.sleep(5);
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		//更新学生总分

		return RespObj.SUCCESS;
	}
	/**
	 * 学生考试首页
	 * 需要学生权限；
	 * 查询考试list
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles({UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("student/index")
	public String getExamDTOList(@RequestParam(defaultValue="1",required=false) Integer page,Map<String, Object> model) throws IllegalParamException
	{
		ObjectId userId=getUserId();
		int isParent=0; //是不是家长用户，0 不是 ，1是家长
		if(UserRole.isParent(getSessionValue().getUserRole()))
		{
			isParent=Constant.ONE;
			UserEntry ue=userService.searchUserId(userId);
			userId=ue.getConnectIds().get(0);
		}
		ClassEntry ce=classService.getClassEntryByStuId(userId, new BasicDBObject(Constant.ID,1));
		if(null==ce)
			throw new IllegalParamException("Can not find class info for student["+userId.toString()+"]");
		int count =exerciseService.countExercise(ce.getID());
		PageUtils utils =new PageUtils(Constant.TEN, count,page);
		List<ExerciseDTO> dtos= exerciseService.getStudentClassDocs(Constant.NEGATIVE_ONE, userId,ce.getID(), (page-1)*Constant.TEN, Constant.TEN);

		model.put("isParent", isParent);
		model.put("page", utils);
		model.put("dtos", dtos);

		return "learningcenter/exam_stu_index";
	}
	/**
	 * 学生提交答题图片
	 * @param file
	 * @param titleId
	 * @return
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping("item/image/upload")
	@ResponseBody
	public RespObj studentUploadItemAnswerImage(@RequestParam("file") MultipartFile file,@ObjectIdType ObjectId titleId, @ObjectIdType ObjectId docId)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);

		if(null==file || file.getSize()==0)
		{
			obj.setMessage("请上传图片");
			return obj;
		}
		ExerciseItemEntry item=exerciseItemService.getExerciseItemEntryByTitleId(titleId, docId, new BasicDBObject("di",1) );
		if(null==item)
		{
			obj.setMessage("参数错误");
			return obj;
		}
//		ObjectId docId =item.getDocumentId();
		ExerciseEntry ee=exerciseService.getExerciseEntry(docId);
		if(null==ee)
		{
			obj.setMessage("参数错误");
			return obj;
		}


		if(ee.getType()==Constant.ONE)
		{
			ClassEntry ce=classService.getClassEntryByStuId(getUserId(), Constant.FIELDS);
			if(null==ce)
			{
				obj.setMessage("参数错误");
				return obj;
			}
			if(!ee.getClassIds().contains(ce.getID()))
			{
				obj.setMessage("没有权限");
				return obj;
			}
		}
		
		logger.info("begin upload item answer image!the user="+getUserId()+"; the title="+item.getID());
		String fileName=file.getOriginalFilename();
		String newFileName=new ObjectId()+ Constant.POINT+  FilenameUtils.getExtension(fileName);
		IdValuePair pair=new IdValuePair(new ObjectId(), newFileName);
		synchronized (this) 
		{
			List<ExerciseAnswerEntry> list=null;
			try {
				list = exerciseItemService.getListByDocIdAndItemId(docId, titleId,getUserId(), Constant.FIELDS);
			} catch (ResultTooManyException e2) {
				logger.error("", e2);
				obj.setMessage("参数错误");
				return obj;
			}
			
			logger.info("**********************************************");
			logger.info("size="+list.size());
			logger.info("**********************************************");
			
	
			if(null!=list && list.size()>Constant.ONE)
			{
				logger.error("[ERROR]item id:"+titleId+",user "+getUserId()+" data error!!!!");
				obj.setMessage("数据错误");
				return obj;
			}
			
			ExerciseAnswerEntry eae=null;
			if(null!=list && list.size()>0) //已经答过题
			{
				eae=list.get(0);
				exerciseItemService.addAnswerImage(eae.getID(), pair);
				if(ee.getTimeType()==2){
					exerciseItemService.addTempAnswerImage(getUserId(), titleId, pair);
				}
			}
			else
			{
				List<IdValuePair> idList =new ArrayList<IdValuePair>();
				idList.add(pair);
				eae=new ExerciseAnswerEntry(docId, titleId, getUserId(), Constant.EMPTY, Constant.NEGATIVE_ONE, Constant.ZERO, idList);
				exerciseItemService.addAnswerEntry(eae, ee.getTimeType()==2);
			}
		
		}
		
		
		try {
			obj=QiniuFileUtils.uploadFile(newFileName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
		} catch (Exception e) {
			logger.error("", e);
			obj.setMessage("文件上传失败");
			return obj;
		}

		IdValuePairDTO dto =new IdValuePairDTO(pair.getId(),QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, pair.getValue().toString()));
		obj =new RespObj(Constant.SUCCESS_CODE, dto);
		return obj;
	}
	/**
	 * 学生涂鸦
	 * @param request
	 * @param titleId
	 * @return
	 * @throws IOException
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping("item/scrawl/upload")
	@ResponseBody
	public RespObj studentScrawl(HttpServletRequest request,String titleId) throws IOException
	{
		String[] params = titleId.split(",");
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		File f = new File(Resources.getProperty("upload.file"),
				new ObjectId().toString() + ".jpg");
		try {
			ServletInputStream inputStream = request.getInputStream();
			OutputStream fileOutputStream = new FileOutputStream(f);
			byte[] bytes = new byte[1024];
			int k = inputStream.readLine(bytes, 0, bytes.length);
			while (k != -1) {
				fileOutputStream.write(bytes, 0, k);
				k = inputStream.readLine(bytes, 0, bytes.length);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("", ex);
		}
		if(!f.exists() || f.length()==0)
		{
			obj.setMessage("文件上传失败");
			return obj;
		}
		return studentUploadItemAnswerImage(new CommonMultipartFile(f),new ObjectId(params[0]), new ObjectId(params[1]));
	}

	/**
	 * 老师提交答案图片，用于涂鸦学生答案
	 * @param
	 * @param
	 * @return
	 */
	@UserRoles({UserRole.TEACHER})
	@RequestMapping("item/image/upload1")
	@ResponseBody
	public RespObj teacherUploadItemAnswerImage1(  HttpServletRequest request ,  @ObjectIdType ObjectId answerId, @ObjectIdType ObjectId picId )
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);

		File f = new File(Resources.getProperty("upload.file"),
				new ObjectId().toString() + ".jpg");
		try {
			ServletInputStream inputStream = request.getInputStream();
			OutputStream fileOutputStream = new FileOutputStream(f);
			byte[] bytes = new byte[1024];
			int k = inputStream.readLine(bytes, 0, bytes.length);
			while (k != -1) {
				fileOutputStream.write(bytes, 0, k);
				k = inputStream.readLine(bytes, 0, bytes.length);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception ex) {
			logger.error("", ex);
			obj.setMessage("参数错误");
			return obj;
		}

		ExerciseAnswerEntry e =exerciseItemService.getExerciseAnswerEntry(answerId);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		ExerciseEntry ee=exerciseService.getExerciseEntry(e.getDocumentId());
		if(null==ee)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!ee.getTeacherId().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		List<IdValuePair> images=e.getImages();
		IdValuePair iamgePair=null;
		for(IdValuePair pair:images)
		{
			if(pair.getId().equals(picId))
			{
				iamgePair=pair;
			}
		}
		if(null==iamgePair)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		String newfileKey=new ObjectId().toString() + ".jpg";
		logger.info("begin replace item answer image!the user="+getUserId()+"; the answerId="+answerId.toString()+",and the image="+newfileKey);

		try {
			exerciseItemService.removeAnswerImage(answerId, iamgePair);
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_IMAGE, iamgePair.getValue().toString());

			IdValuePair pair =new IdValuePair(new ObjectId(), newfileKey );
			obj=QiniuFileUtils.uploadFile(newfileKey, new FileInputStream(f), QiniuFileUtils.TYPE_IMAGE);

			exerciseItemService.addAnswerImage(answerId, pair);
		} catch (Exception e1) {
			logger.error("", e1);
			obj.setMessage("文件上传失败");
			return obj;
		}
		return RespObj.SUCCESS;
	}

	/**
	 * 老师提交答案图片，用于涂鸦学生答案//用于移动端
	 * @param file
	 * @param
	 * @return
	 */
	@UserRoles({UserRole.TEACHER})
	@RequestMapping("item/image/upload2")
	@ResponseBody
	public RespObj teacherUploadItemAnswerImage2( MultipartFile file,@ObjectIdType(isRequire=false) ObjectId answerId, @ObjectIdType(isRequire=false) ObjectId picId )
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		if(null==file || file.getSize()==0)
		{
			obj.setMessage("请上传图片");
			return obj;
		}

		ExerciseAnswerEntry e =exerciseItemService.getExerciseAnswerEntry(answerId);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		ExerciseEntry ee=exerciseService.getExerciseEntry(e.getDocumentId());
		if(null==ee)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(!ee.getTeacherId().equals(getUserId()))
		{
			obj.setMessage("没有权限");
			return obj;
		}
		List<IdValuePair> images=e.getImages();
		IdValuePair iamgePair=null;
		for(IdValuePair pair:images)
		{
			if(pair.getId().equals(picId))
			{
				iamgePair=pair;
			}
		}
		if(null==iamgePair)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		String newfileKey=new ObjectId().toString() + ".jpg";
		logger.info("begin replace item answer image!the user="+getUserId()+"; the answerId="+answerId.toString()+",and the image="+newfileKey);

		try {
			exerciseItemService.removeAnswerImage(answerId, iamgePair);
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_IMAGE, iamgePair.getValue().toString());

			IdValuePair pair =new IdValuePair(new ObjectId(), newfileKey );
			obj=QiniuFileUtils.uploadFile(newfileKey, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);

			exerciseItemService.addAnswerImage(answerId, pair);
		} catch (Exception e1) {
			logger.error("", e1);
			obj.setMessage("文件上传失败");
			return obj;
		}
		return RespObj.SUCCESS;
	}

	/**
	 * 学生提交答题图片
	 * @param
	 * @param titleId
	 * @return
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping("item/image/remove")
	@ResponseBody
	public RespObj removeItemAnswerImage(@ObjectIdType ObjectId titleId,@ObjectIdType ObjectId imageId)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE);

		List<ExerciseAnswerEntry> list=null;
		try {
			list = exerciseItemService.getListByDocIdAndItemId(null, titleId,getUserId(), Constant.FIELDS);
		} catch (ResultTooManyException e2) {
			logger.error("", e2);
			obj.setMessage("参数错误");
			return obj;
		}

		if(null==list || list.size()!=Constant.ONE)
		{
			obj.setMessage("参数错误");
			return obj;
		}

		ExerciseAnswerEntry e=list.get(0);
		IdValuePair pair=null;
		for(IdValuePair p:e.getImages())
		{
			if(p.getId().equals(imageId))
			{
				pair=p;
				break;
			}
		}
		if(null==pair)
		{
			obj.setMessage("没有找到该图片");
			return obj;
		}

		logger.info("remove image!the file="+pair.getValue().toString());

		try {
			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_IMAGE, pair.getValue().toString());
		} catch (IllegalParamException e1) {
			logger.error("", e1);
			obj.setMessage("删除图片失败");
			return obj;
		}
		exerciseItemService.removeAnswerImage(e.getID(), pair);
		exerciseItemService.removeTempAnswerImage(getUserId(), titleId, pair);
		return RespObj.SUCCESS;
	}

	/**
	 * 学生提交答题卡
	 * @param dto
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws ResultTooManyException
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping(value="/student/answer/submit",method = RequestMethod.POST)
	@ResponseBody
	public RespObj studentSubmitAnswer(@RequestBody ExerciseSubmitAnswerDTO dto, HttpServletResponse response) throws PermissionUnallowedException, ResultTooManyException
	{
		response.setHeader("Cache-Control", "no-cache");
		studentTempAnswer(dto, response);//保存
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		if(null== dto.getDocId() ||  !ObjectId.isValid(dto.getDocId()))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		ObjectId docId =new ObjectId(dto.getDocId());
		ExerciseEntry e= exerciseService.getExerciseEntry(docId);
		if(null==e)
		{
			obj.setMessage("参数错误");
			return obj;
		}

		ClassEntry  ce=classService.getClassEntryByStuId(getUserId(),Constant.FIELDS);
		if(null==ce)
		{
			logger.error("Can not find class for student["+getUserId()+"]");
			obj.setMessage("参数错误");
			return obj;
		}
		if(e.getType()==Constant.ONE)
		{
			if(!e.getClassIds().contains(ce.getID()))
			{
				obj.setMessage("没有权限");
				return obj;
			}
		}
		//查询是否已答题
		List<ExerciseAnswerEntry> list= exerciseItemService.getListByDocIdAndItemId(docId,null,getUserId(),Constant.FIELDS);
//		for(ExerciseAnswerEntry eae:list)
//		{
//			if(StringUtils.isNotBlank(eae.getUserAnswer()))
//			{
//				obj.setMessage("该考试已经作答过");
//				return obj;
//			}
//		}
		
		List<ObjectId> alreadyTitleIDs =MongoUtils.getFieldObjectIDs(list, "ti");
		
		
	
		exerciseItemService.studentSubmitAnswer(getUserId(), docId, dto,alreadyTitleIDs, false);
		exerciseService.studentSubmit(docId, getUserId());
		//得到学生总成绩，更新到performance
		Double score = exerciseItemService.getStuScore(docId, getUserId());
		examResultService.updateStuScore(docId, getUserId(), score);
		ExpLogType expLogType =null;
		if(e.getType()==1){
			expLogType = ExpLogType.QUIZ;
		}
		if(e.getType()==2){
			expLogType = ExpLogType.FINISHED_ADVANCED_PRACTICE;
		}
		experienceService.updateNoRepeat(getUserId().toString(), expLogType, docId.toString());
		return RespObj.SUCCESS;
	}

	/**
	 * 学生保存答题卡//未提交
	 * @param dto
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws ResultTooManyException
	 */
	@UserRoles({UserRole.STUDENT})
	@RequestMapping(value="/student/answer/temp",method = RequestMethod.POST)
	@ResponseBody
	public RespObj studentTempAnswer(@RequestBody ExerciseSubmitAnswerDTO dto,  HttpServletResponse response) throws PermissionUnallowedException, ResultTooManyException
	{
		response.setHeader("Cache-Control", "no-cache");
		RespObj obj =new RespObj(Constant.FAILD_CODE);
		if(!ObjectId.isValid(dto.getDocId())) {
			obj.setMessage("参数错误");
			return obj;
		}
		ObjectId docId =new ObjectId(dto.getDocId());
		ExerciseEntry e= exerciseService.getExerciseEntry(docId);
		if(null==e) {
			obj.setMessage("参数错误");
			return obj;
		}

		ClassEntry  ce=classService.getClassEntryByStuId(getUserId(),Constant.FIELDS);
		if(null==ce) {
			logger.error("Can not find class for student["+getUserId()+"]");
			obj.setMessage("参数错误");
			return obj;
		}
		if(e.getType()==Constant.ONE) {
			if(!e.getClassIds().contains(ce.getID())) {
				obj.setMessage("没有权限");
				return obj;
			}
		}


//查询是否已答题
		List<ExerciseAnswerEntry> list= exerciseItemService.getListByDocIdAndItemId(docId,null,getUserId(),Constant.FIELDS);

		List<ObjectId> alreadyTitleIDs =MongoUtils.getFieldObjectIDs(list, "ti");

		exerciseItemService.studentSubmitAnswer(getUserId(), docId, dto, alreadyTitleIDs, true);
		return RespObj.SUCCESS;
	}

	/**
	 * 下载七牛文件到本地，
	 * @param request
	 * @param model
	 * @param e
	 */
	private void bindPaths(HttpServletRequest request,
						   Map<String, Object> model, ExerciseEntry e) {
		try
		{
			down(request,e.getQuestionPath()+".pdf",QiniuFileUtils.TYPE_DOCUMENT);
			model.put("pdfPath", "/upload/exam/"+e.getQuestionPath()+".pdf");
		}catch(Exception ex)
		{
			logger.error("", ex);
		}

		try
		{
		
			RespObj res=down(request,e.getQuestionPath()+".swf",QiniuFileUtils.TYPE_DOCUMENT);
			if(res.equals(RespObj.SUCCESS))
			{
				model.put("swfExists",1);
			    model.put("swfPath", "/upload/exam/"+e.getQuestionPath()+".swf");
			}
			else
			{
				model.put("swfExists",0);
			}
		}catch(Exception ex)
		{
			logger.error("", ex);
		}




		try
		{
			down(request,e.getAnswerPath()+".pdf",QiniuFileUtils.TYPE_DOCUMENT);
			model.put("ansPdfPath", "/upload/exam/"+e.getAnswerPath()+".pdf");
		}catch(Exception ex)
		{
			logger.error("", ex);
		}


		try
		{
			down(request,e.getAnswerPath()+".swf",QiniuFileUtils.TYPE_DOCUMENT);
			model.put("ansPath", "/upload/exam/"+e.getAnswerPath()+".swf");
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
	}



	public RespObj down(HttpServletRequest req,String fileKey,int type) throws IOException
	{

		String path=req.getServletContext().getRealPath("/upload/exam");
		File downFile=new File(path, fileKey);
		if(!downFile.exists())
		{
			InputStream stream =QiniuFileUtils.downFile(type, fileKey);
			
			OutputStream fileStream =new FileOutputStream(downFile);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = stream.read(buff, 0, buff.length))) {
				fileStream.write(buff, 0, bytesRead);
			}
			stream.close();
			fileStream.close();
		}
		
		if(downFile.exists() && downFile.length()>Constant.HUNDRED )
		{
			return RespObj.SUCCESS;
		}
		return RespObj.FAILD;
		
	}

	/**
	 * 不带班校长查看班级考试
	 * add by miaoqiang
	 * @param classId
	 * @param skip
	 * @param limit
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("classExam/list")
	@ResponseBody
	public PageDTO<ExerciseDTO> getExamListByClassId(String classId,int skip,int limit)
	{
		return exerciseService.getExamPageDTO(Constant.ONE, new ObjectId(classId),skip,limit);
	}
}
