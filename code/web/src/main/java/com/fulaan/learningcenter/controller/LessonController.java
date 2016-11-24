package com.fulaan.learningcenter.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.learningcenter.service.*;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.reminder.service.ReminderService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.dto.StudentVideoViewDTO;
import com.fulaan.video.dto.VideoViewRecordDTO;
import com.fulaan.video.service.VideoService;
import com.fulaan.video.service.VideoViewRecordService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.*;
import com.pojo.emarket.Comment;
import com.pojo.emarket.CommentDTO;
import com.pojo.exercise.ExerciseAnswerEntry;
import com.pojo.exercise.ExerciseEntry;
import com.pojo.exercise.ExerciseItemEntry;
import com.pojo.lesson.*;
import com.pojo.reminder.ReminderEntry;
import com.pojo.resources.ResourceEntry;
import com.pojo.school.*;
import com.pojo.user.*;
import com.pojo.utils.MongoUtils;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.pojo.video.VideoViewRecordEntry;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.props.Resources;
import com.sys.utils.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 课程操作
 * @author fourer
 */

@Controller
@RequestMapping("/lesson")
public class LessonController extends BaseController {
	private static final Logger logger =Logger.getLogger(LessonController.class);
	
	
	private UserService userService =new UserService();
	private DirService dirService =new DirService();
	private VideoService videoService =new VideoService();
	private LessonService lessonService =new LessonService();
	private TeacherClassSubjectService teacherClassLessonService =new TeacherClassSubjectService();
	private ExerciseService exerciseService =new ExerciseService();
    private LeagueService leagueService =new LeagueService();
	private VideoViewRecordService videoViewRecordService =new VideoViewRecordService();
	private ExerciseItemService exerciseItemService =new ExerciseItemService();
    private ExperienceService experienceService=new ExperienceService();
    private ClassService classService = new ClassService();
    private CloudResourceService cloudResourceService =new CloudResourceService();
    private SchoolService schoolService =new SchoolService();
	@Autowired
	private ReminderService reminderService;
	@Autowired
	private InterestClassService interestClassService;

    
	@RequestMapping("teacher")
	public String teacherSpace() {
		return "learningcenter/teacherCourse";
	}

	@RequestMapping("class")
	public String classCourse() {
		SessionValue sessionValue = getSessionValue();
		if (UserRole.isStudentOrParent(sessionValue.getUserRole())) {
			return "learningcenter/studentCourse";
		} else {
			return "learningcenter/classCourse";
		}
	}
    @RequestMapping("stat")
    public String statistics(@ObjectIdType ObjectId lessonId, @RequestParam(defaultValue="",required=false) String classId,Map<String,Object> model){

		model.put("lessonId",lessonId.toString());
		model.put("classId",classId);

        LessonEntry lessonEntry = lessonService.getLessonEntry(lessonId);
        if(lessonEntry.getExercise()!=null){
            model.put("exerciseId",lessonEntry.getExercise().toString());
        }
		if(!classId.equals("")) {
			return "homework/statPage";
		}
        return "learningcenter/statPage";

    }

	@RequestMapping("school")
	public String schoolResource() {
		return "learningcenter/schoolResource";
	}

	@RequestMapping("league")
	public String schoolLeague() {
		return "learningcenter/schoolLeague";
	}

	@RequestMapping("/view")
	public String viewLesson(@ObjectIdType ObjectId lessonId, Map<String, Object> model,HttpServletRequest req) throws Exception {

		LessonEntry e=lessonService.getLessonEntry(lessonId);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		int isParent=0; //是不是家长用户，0 不是 ，1是家长
		if(UserRole.isParent(getSessionValue().getUserRole()))
		{
			isParent=Constant.ONE;
		}
		LessonDetailDTO lessonDetail = buildLessonDTO(e);
		BeanInfo info = Introspector.getBeanInfo(lessonDetail.getClass());
		for (PropertyDescriptor pd: info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if (reader != null) {
				model.put(pd.getName(), reader.invoke(lessonDetail));
//				System.out.println(pd.getName() + "---" + reader.invoke(lessonDetail));
			}
		}
        //判断是否去答题还是查看批改 todo
        if(lessonDetail.getExercise()!=null) {
        	
        	if(UserRole.isStudentOrParent(getSessionValue().getUserRole())) //学生或家长
        	{
        		ObjectId uid =getUserId();
        		if(UserRole.isParent(getSessionValue().getUserRole())) //家长
        		{
        			UserEntry ue=userService.searchUserId(getUserId());
        			uid=ue.getConnectIds().get(0);
        		}

				ExerciseEntry exerciseEntry = exerciseService.getExerciseEntry(e.getExercise());
				if(exerciseEntry.getTimeType()==2 && System.currentTimeMillis() < exerciseEntry.getDate() + 24 * 60 * 60 * 1000){
					model.put("exerciseStat", 1);//0-无，1-答题，2-查看结果
				} else {
					List<ExerciseAnswerEntry> list=exerciseItemService.getListByDocIdAndItemId(e.getExercise(),null,uid,new BasicDBObject(Constant.ID,1));
					if(list.size()>Constant.ZERO) {
						model.put("exerciseStat", 2);//0-无，1-答题，2-查看结果
					}
					else {
						model.put("exerciseStat", 1);//0-无，1-答题，2-查看结果
					}
				}

        	}
           // model.put("exerciseStat", 1);//0-无，1-答题，2-查看结果
            model.put("exerciseId", lessonDetail.getExercise().getId().toString());
        }
        
        model.put("isParent", isParent);
        
        if(e.getVideoIds().size()>0) //视频
        {model.put("type","view");
		  return "learningcenter/viewLesson";
        }
        List<LessonWare> wareList =e.getLessonWareList();
        if(wareList.size()>0)
        {
        	LessonWare lw=wareList.get(0);
        	ResourceEntry re=	cloudResourceService.getResourceEntryById(lw.getId());
        	if(null!=re)
        	{
	        	FileType ty =FileType.getFileType(lw.getFileType());
	        	String cloudPath=req.getServletContext().getRealPath("/upload/cloudres");
	        	if(ty.equals(FileType.TXT))//文本
	        	{
	        		File lwFile =new File(cloudPath,lw.getId().toString()+".txt");
	        		 String code =FileUtil.getTextFileEncoding(lwFile);
	        		String content=FileUtils.readFileToString(lwFile,code);
	        		content= content.replaceAll("\r\n", "<br>");
	        		model.put("text_content", content);
					model.put("type","txt");
	        		return "learningcenter/viewLesson_text";
	        	}
	        	if(ty.equals(FileType.DOC) || ty.equals(FileType.DOCX) || ty.equals(FileType.PDF) || ty.equals(FileType.PPTX) ||ty.equals(FileType.PPT))//swf
	        	{
	        		
	        		model.put("swf_path",CloudResourceService.CLOUD_RESOURCE_QINIU_URL+lw.getId().toString()+".swf" );
	        		//model.put("swf_path", "http://7xiclj.com1.z0.glb.clouddn.com/cloud_res.swf");
					model.put("type","swf");
	        		return "learningcenter/viewLesson_swf";
	        	}
        	}
        }
		model.put("type","view");
        return "learningcenter/viewLesson";
	}

	/**
	 * 编辑课程
	 * @param lessonId 
	 * @param model
	 * @return
	 * @throws PermissionUnallowedException 
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/edit")
	public String editLesson(@ObjectIdType ObjectId lessonId, Map<String, Object> model) throws PermissionUnallowedException, IllegalParamException {
		LessonEntry e =lessonService.getLessonEntry(lessonId);
		boolean isPermission =isHavePermission(e);
		if(!isPermission)
		{
			throw new PermissionUnallowedException();
		}
		if (e.getDirId()!=null) {
			DirEntry dirEntry=dirService.getDirEntry(e.getDirId(), null);
			//所在文件夹的type
			model.put("dirType", DirType.getDirType(dirEntry.getType()).toString());
		}
		//该课程被推送次数
		model.put("pushCount", e.getPushCount());

        if(e.getExercise()!=null){
            model.put("exerciseId",e.getExercise().toString());
            String exename = exerciseService.getExerciseEntry(e.getExercise()).getName();
            model.put("exerciseName",exename);
        }
		return "learningcenter/editLesson";
	}
	/**
	 * 删除课程，不删除资源
	 * @param lessonId
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/remove")
	@ResponseBody
	public RespObj removeLesson(@ObjectIdType ObjectId lessonId) throws IllegalParamException
	{
		LessonEntry e=lessonService.getLessonEntry(lessonId);
		if(e==null)
		{
			throw new IllegalParamException();
		}
		boolean isPermission=isHavePermission(e);
		if(!isPermission)
		{
			logger.error("Permission lost!lessonId="+lessonId);
			return RespObj.FAILD;
		}
		
		logger.info("delete lesson; id= "+lessonId);
		List<ObjectId> list =new ArrayList<ObjectId>();
		list.add(lessonId);
		lessonService.deleteByIds(list);

        /*
        DirEntry dirEntry = dirService.getDirEntry(e.getDirId(), null);
        if (dirEntry.getType() != DirType.MICRO_LESSON.getType()) {
            if (!e.getIsFromCloud() && e.getSrcId() > 0 && DateUtil.lessThanAWeek(e.getCreateTime())) {
                DirInfo dir = DirCenter.getDirByLesson(lesson.getSrcId());
                if (dir.getType() == DirType.TEACHER) {
                    FzTeacherOutputInfo teacher = TeacherCenter.getFzTeacherInfo(dir.getOwner());
                    if (teacher != null) {
                        ExpLogType deletepush = ExpLogType.DELETEPUSH;
                        if (experienceService.updateScore(getUserId().toString(), deletepush, lessonId.toString())) {
                            //result.put("score", deletepush.getExp());
                            //result.put("scoreMsg", deletepush.getDesc());
                        }
                    }
                }
            }
        }
        */
		return RespObj.SUCCESS;
	}

	
	/**载入课程列表，Web使用
	 * 
	 * @param type 1备课空间 2 班级课程 3校本资源 4联盟资源
	 * @return
	 */
	@RequestMapping("/load/list")
	public String getLessonDTOList(DirType type, Map<String, Object> model)
	{
		List<LessonDTO> retList =new ArrayList<LessonDTO>();
		
		List<ObjectId> ownerList =new ArrayList<ObjectId>();

		switch (type) {
			case BACK_UP:
				ownerList.add(getUserId());
				model.put("writable", true);
				break;
			case CLASS_LESSON: {
				List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOList(getUserId(), null);
				for (TeacherClassSubjectDTO dto : list) {
					ownerList.add(new ObjectId(dto.getId()));
				}

				if (UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
					model.put("writable", false);
				} else {
					model.put("writable", true);

				}

				break;
			}
			
			case SCHOOL_RESOURCE:
				model.put("writable", false);
				if (UserRole.isHeadmaster(getSessionValue().getUserRole())) {
					model.put("writable", true);
				} 
				ObjectId schoolId =new ObjectId(getSessionValue().getSchoolId());
				DBObject fileds =new BasicDBObject("sis",1);
				SchoolEntry s=schoolService.getSchoolEntry(schoolId,fileds);
				if(null!=s)
				{
				  ownerList.add(s.getID());
				  ownerList.addAll(s.getSchoolIds());
				}
				break;
			case UNION_RESOURCE:
				ObjectId mySchoolId =new ObjectId(getSessionValue().getSchoolId());
				List<LeagueEnrty> list=leagueService.getLeagueEnrtys(mySchoolId, new BasicDBObject(Constant.ID, 1));
				ownerList.addAll(MongoUtils.getFieldObjectIDs(list, Constant.ID));
				break;
			case MICRO_LESSON:
				//model.put("writable", role != 0 && voteService.getVoteMasters().contains(userService.getTeacherIdByUserId(userId)));
				break;
		default:
			break;
		}

		
		List<DirEntry> dirList =dirService.getDirEntryList(ownerList,new BasicDBObject(Constant.ID,1),type.getType());
		List<ObjectId> dirids=MongoUtils.getFieldObjectIDs(dirList, Constant.ID);
		List<LessonEntry> lessonList = lessonService.getLessonEntryList(dirids, Constant.FIELDS);
		Map<ObjectId, LessonDTO> map = attachExeitemCount(lessonList);
		retList.addAll(map.values());
		/**
		 * 先按照时间先后顺序排序
		 */
		Collections.sort(retList, new Comparator<LessonDTO>() {
			@Override
			public int compare(LessonDTO arg0, LessonDTO arg1) {
				try
				{
					ObjectId o1=new ObjectId(arg0.getId()); 
					ObjectId o2=new ObjectId(arg1.getId()); 
					return o2.compareTo(o1);
				}catch(Exception ex)
				{
					
				}
				return Constant.ZERO;
			}
		});
		
		int count =lessonService.count(dirids);
	
		model.put("total", count);
		model.put("lessons", retList);
		model.put("dirType", type.toString());

		if (type == DirType.BACK_UP) {
			return "learningcenter/lessonsUnderDir";
		} else {
			return "learningcenter/lessonList";
		}
	}

    /** 载入课程列表，客户端使用
     * @param type
     * @return
     */
    @RequestMapping("/load/list/json")
    @ResponseBody
    public Map<String,Object> getLessonDTOList(DirType type)
    {
        Map<String, Object> model = new HashMap<String, Object>();
        List<ObjectId> ownerList =new ArrayList<ObjectId>();
        List<LessonDTO> retList =new ArrayList<LessonDTO>();

        switch (type) {
            case BACK_UP:
                ownerList.add(getUserId());
                break;
            case CLASS_LESSON: {
                List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOList(getUserId(), null);
                for (TeacherClassSubjectDTO dto : list) {
                    ownerList.add(new ObjectId(dto.getId()));
                }

                break;
            }
            case SCHOOL_RESOURCE:
                ownerList.add(new ObjectId(getSessionValue().getSchoolId()));
                break;
            case UNION_RESOURCE:
                ObjectId mySchoolId =new ObjectId(getSessionValue().getSchoolId());
                List<LeagueEnrty> list=leagueService.getLeagueEnrtys(mySchoolId, new BasicDBObject(Constant.ID, 1));
                ownerList.addAll(MongoUtils.getFieldObjectIDs(list, Constant.ID));
                break;
            case MICRO_LESSON:
                //model.put("writable", role != 0 && voteService.getVoteMasters().contains(userService.getTeacherIdByUserId(userId)));
                break;
		default:
			break;
        }
        List<DirEntry> dirList =dirService.getDirEntryList(ownerList,new BasicDBObject(Constant.ID,1),type.getType());
        List<ObjectId> dirids=MongoUtils.getFieldObjectIDs(dirList, Constant.ID);
        List<LessonEntry> lessonList = lessonService.getLessonEntryList(dirids, Constant.FIELDS);
        for(LessonEntry e:lessonList)
        {
            retList.add(new LessonDTO(e));
        }
        int count =lessonService.count(dirids);

        model.put("total", count);
        model.put("lessons", retList);
        model.put("dirType", type.toString());
        return model;

    }

    /**学生载入列表，只有班级课程被接受
     * @param type
     * @param model
     * @return
     */
    @RequestMapping("/student/load/list")
    public String getStudentLessonDTOList(DirType type, Map<String, Object> model) {
        if(type!=DirType.CLASS_LESSON){
            return null;
        }
        if(type.getType() != DirType.CLASS_LESSON.getType()){
            return null;
        }
        //找到对应班级的文件夹
       // ObjectId ui=getUserId();
        //先得到学生所在的班级
       // ClassService classService = new ClassService();
       // ClassEntry classEntry = classService.getClassEntryByStuId(ui, Constant.FIELDS);


        List<ClassInfoDTO> classInfoDTOList = userService.getClassDTOList(getUserId(), getSessionValue().getUserRole());
        //再得到该班级关联的所有teacherclasssubjects
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        for(ClassInfoDTO classInfoDTO:classInfoDTOList){
            classIdList.add(new ObjectId(classInfoDTO.getId()));

        }


        List<TeacherClassSubjectDTO> teacherClassLessonInfoList =
                teacherClassLessonService.findTeacherClassSubjectByClassIds(classIdList);


        List<ObjectId> ownerList = new ArrayList<ObjectId>();
        for (TeacherClassSubjectDTO dto : teacherClassLessonInfoList) {
            ownerList.add(new ObjectId(dto.getId()));
        }


        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, Constant.FIELDS,type.getType());
        List<ObjectId> dirids=MongoUtils.getFieldObjectIDs(dirList, Constant.ID);
        List<LessonEntry> lessonList = lessonService.getLessonEntryList(dirids, Constant.FIELDS);

        List<LessonDTO> retList =new ArrayList<LessonDTO>();
        for(LessonEntry e:lessonList)
        {
            retList.add(new LessonDTO(e));
        }
        int count =lessonService.count(dirids);

        //return new PageDTO<LessonDTO>(count,retList);
        model.put("total", count);
        model.put("lessons", retList);
        model.put("dirType", type.toString());

        return "learningcenter/lessonList";
    }

    /** 学生载入列表，（班级课程），移动端使用
     * @param type
     * @return
     */
    @RequestMapping("/student/load/list/json")
    @ResponseBody
    public Map<String,Object> getStudentLessonDTOList(DirType type)
    {
        Map<String, Object> model = new HashMap<String, Object>();
        //找到对应班级的文件夹
        ObjectId ui=getUserId();
        //先得到学生所在的班级
        ClassService classService = new ClassService();
        ClassEntry classEntry = classService.getClassEntryByStuId(ui, Constant.FIELDS);

        //再得到该班级关联的所有teacherclasssubjects
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        classIdList.add(classEntry.getID());
        List<TeacherClassSubjectDTO> teacherClassLessonInfoList =
                teacherClassLessonService.findTeacherClassSubjectByClassIds(classIdList);


        List<ObjectId> ownerList = new ArrayList<ObjectId>();
        for (TeacherClassSubjectDTO dto : teacherClassLessonInfoList) {
            ownerList.add(new ObjectId(dto.getId()));
        }


        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, Constant.FIELDS,type.getType());
        List<ObjectId> dirids=MongoUtils.getFieldObjectIDs(dirList, Constant.ID);
        List<LessonEntry> lessonList = lessonService.getLessonEntryList(dirids, Constant.FIELDS);

        List<LessonDTO> retList =new ArrayList<LessonDTO>();
        for(LessonEntry e:lessonList)
        {
            retList.add(new LessonDTO(e));
        }
        int count =lessonService.count(dirids);
      
        model.put("total", count);
        model.put("lessons", retList);
        model.put("dirType", type.toString());
        return model;

    }
    
    

    /**
     * 得到给定目录的课程
     * @param ids
     * @return
     * @throws IllegalParamException 
     */
    @RequestMapping("/dir/backups")
    @ResponseBody
    public RespObj getBackUpLessons(String ids) throws IllegalParamException
    {
    	List<IdNameValuePairDTO> list =new ArrayList<IdNameValuePairDTO>();
    	List<ObjectId> objectIds =MongoUtils.convert(ids);
    	List<LessonEntry> lessonList = lessonService.getLessonEntryList(objectIds, Constant.FIELDS);
    	
    	for(LessonEntry e:lessonList)
    	{
    		list.add(new IdNameValuePairDTO(e));
    		
    	
    	}
    	
    	RespObj obj =new RespObj(Constant.SUCCESS_CODE, list);
    	return obj;
    }
    
    
    
    /**
     * 根据目录查询课程，Web使用，返回页面
     * @param dirId
     * @return
     * @throws IllegalParamException
     */
    @RequestMapping("/dir/list/page")
    public String getLessonPage(@ObjectIdType ObjectId dirId, Map<String, Object> model) throws IllegalParamException {


        DirEntry dirEntry = dirService.getDirEntry(dirId, null);
        if (dirEntry == null) //班级课程顶级菜单s
        {
            throw new IllegalParamException("Can not find dir;id="+dirId.toString());
        }
        Set<ObjectId> ids = dirService.getSelfAndChildDirs(dirEntry.getOwerId(), dirId);
        List<LessonEntry> lessonList = lessonService.getLessonEntryList(ids, Constant.FIELDS);
        /**
         * key 为小练习ID
         */
        Map<ObjectId, LessonDTO> map = attachExeitemCount(lessonList);
        List<LessonDTO>  lessonDTOList=  new ArrayList<LessonDTO>(map.values());
		/**
		 * 先按照时间先后顺序排序
		 */
		Collections.sort(lessonDTOList, new Comparator<LessonDTO>() {
			@Override
			public int compare(LessonDTO arg0, LessonDTO arg1) {
				try
				{
					ObjectId o1=new ObjectId(arg0.getId()); 
					ObjectId o2=new ObjectId(arg1.getId()); 
					return o2.compareTo(o1);
				}catch(Exception ex)
				{
					
				}
				return Constant.ZERO;
			}
		});
		
		model.put("writable", false);
        switch (DirType.getDirType(dirEntry.getType())) {
            case BACK_UP:
                model.put("writable", true);
                break;
            case CLASS_LESSON: {

                if (!UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
                    model.put("writable", true);
                } 
                break;
            }
            case SCHOOL_RESOURCE:
            	if(UserRole.isHeadmaster(getSessionValue().getUserRole()))
            	{
            		model.put("writable", true);
            	}
                break;
            case UNION_RESOURCE:
                break;
            case MICRO_LESSON:
                //model.put("writable", role != 0 && voteService.getVoteMasters().contains(userService.getTeacherIdByUserId(userId)));
                break;
		default:
			break;
        }
		
        model.put("total", map.size());
        model.put("lessons", lessonDTOList);
        model.put("dirType", DirType.getDirType(dirEntry.getType()).toString());
        if(dirEntry.getType() == DirType.BACK_UP.getType()){
            return "learningcenter/lessonsUnderDir";
        } else {
            return "learningcenter/lessonList";
        }
    }

	

	
	/**
	 * 根据目录查询课程，返回数据
	 * @param dirId
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/dir/list")
	@ResponseBody
	public List<LessonDTO> getLessonDTOList(@ObjectIdType ObjectId dirId) 
	{

        DirEntry dirEntry = dirService.getDirEntry(dirId, null);
		List<LessonDTO> retList =new ArrayList<LessonDTO>();
		Set<ObjectId> ids=dirService.getSelfAndChildDirs(dirEntry.getOwerId(), dirId);
		List<LessonEntry> lessonList=lessonService.getLessonEntryList(ids, Constant.FIELDS);
		for(LessonEntry e:lessonList)
		{
			retList.add(new LessonDTO(e));
		}
		return retList;
	}

	
	/**
	 * 查找某个目录下得课程，返回页面
	 * @param dirId 
	 * @param model
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/lessonsUnderDir")
	public String getLessonsUnderDir(@ObjectIdType ObjectId dirId, Map<String, Object> model) throws IllegalParamException  {
		
		DirEntry dir =dirService.getDirEntry(dirId, null);
		if(null==dir)
		{
			throw new IllegalParamException();
		}
		
		List<LessonDTO> lessons = getLessonDTOList(dirId);
		model.put("lessons", lessons);
		List<ObjectId> dirIds = new ArrayList<ObjectId>();
		dirIds.add(dirId);
		model.put("total", lessonService.count(dirIds));
		
		boolean writable=dirService.isPermission(dir, getUserId(), new ObjectId(getSessionValue().getSchoolId()));
		model.put("writable", writable);
		model.put("parentDir", new DirDTO(dir));
		model.put("dirType", DirType.getDirType(dir.getType()).toString());
		return "learningcenter/lessonList";
	}


	/**
	 * 新建课程
	 * @param dirId
	 * @param name
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/create")
	@ResponseBody
	public RespObj createLesson(@ObjectIdType ObjectId dirId,String name) throws IllegalParamException
	{
		if(!dirId.toString().equals("000000000000000000000000")) {
			DirEntry dir=dirService.getDirEntry(dirId, null);
			if(null==dir)
			{
				throw new IllegalParamException();
			}
		}


//		if(!dirService.isHavePermission(dirId, getUserId(), new ObjectId(getSessionValue().getSchoolId())))
//		{
//			logger.info("permission is lost");
//			return RespObj.FAILD;
//		}


		if(!ValidationUtils.isRequestLessonName(name))
		{
			return RespObj.FAILD;
		}
		LessonEntry e;
		if(dirId.toString().equals("000000000000000000000000")) {
			e = new LessonEntry(name, name, LessonType.CLASS_LESSON, getUserId(), dirId, null, Constant.ZERO);
		} else {
			e = new LessonEntry(name, name, LessonType.BACKUP_LESSON, getUserId(), dirId, null, Constant.ZERO);
		}
		ObjectId id=lessonService.addLessonEntry(e);
		RespObj obj =new RespObj(Constant.SUCCESS_CODE, id.toString());
		return obj;
	}

	/**
	 * 用于老作业转成新作业时新建
	 * @param dirId
	 * @param name
	 * @param userId
	 * @return
	 * @throws IllegalParamException
	 */
	public RespObj createLessonForHW(ObjectId dirId,String name, ObjectId userId, List<LessonWare> lessonWareList) throws IllegalParamException {
		if(!ValidationUtils.isRequestLessonName(name))
		{
			return RespObj.FAILD;
		}
		LessonEntry e = new LessonEntry(name, name, LessonType.CLASS_LESSON, userId, dirId, null, Constant.ZERO);
		e.setLessonWareList(lessonWareList);
		ObjectId id=lessonService.addLessonEntry(e);
		RespObj obj =new RespObj(Constant.SUCCESS_CODE, id.toString());
		return obj;
	}

	/**
	 * 课程移动
	 * @param lessonId
	 * @param dirId
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/move")
	@ResponseBody
	public RespObj lessonMove(@ObjectIdType ObjectId lessonId, @ObjectIdType ObjectId dirId) throws IllegalParamException
	{
		LessonEntry le =lessonService.getLessonEntry(lessonId);
		DirEntry de =dirService.getDirEntry(dirId, null);
		if(null==le && null==de)
		{
			throw new IllegalParamException();
		}
		
		//RespObj obj =new RespObj(Constant.FAILD_CODE);
//		boolean lessonPermission=isHavePermission(le);
//		if(!lessonPermission)
//		{
//			obj.setMessage("lesson permission is lost!");
//			return obj;
//		}
//		boolean dirPermission=dirService.isHavePermission(dirId, getUserId(), new ObjectId(getSessionValue().getSchoolId()));
//		if(!dirPermission)
//		{
//			obj.setMessage("dir permission is lost!");
//			return obj;
//		}
		List<ObjectId> lessonList =new ArrayList<ObjectId>();
		lessonList.add(lessonId);
		lessonService.update(lessonList, new FieldValuePair("di",dirId),new FieldValuePair("lut",System.currentTimeMillis()));
		return RespObj.SUCCESS;
	}
	/**
	 * 得到老师班级课程
	 * @return
	 */
	@RequestMapping("/tcl/list")
	@ResponseBody
	public List<TeacherClassSubjectDTO> getTeacherClassLessons()
	{
		 List<TeacherClassSubjectDTO> list =teacherClassLessonService.getTeacherClassSubjectDTOList(getUserId(),null);
		 return list;
	}
	/**
	 * 推送一个课程
	 * @param lessonId
	 * @param dirIds
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/push")
	@ResponseBody
	public RespObj pushLesson(@ObjectIdType ObjectId lessonId, String dirIds) throws IllegalParamException
	{
		//todo 权限检查
		LessonEntry e=lessonService.getLessonEntry(lessonId);
		if(null==e)
			throw new IllegalParamException();
		
		if(StringUtils.isBlank(dirIds))
			throw new IllegalParamException();
		
		Set<ObjectId> myDirIds=dirService.getDirIds(getUserId(), new ObjectId(getSessionValue().getSchoolId()));
		
		List<ObjectId> dirIdList =MongoUtils.convert(dirIds);
		
		for(ObjectId id:dirIdList)
		{
			if(!myDirIds.contains(id))
			{
				throw new IllegalParamException();
			}
		}
		
		
		List<DirEntry> dirs=dirService.getDirEntryListByIds(dirIdList, Constant.FIELDS);
		if(dirs.size()!=dirIdList.size())
			throw new IllegalParamException();
		
		List<LessonEntry> lessonList =new ArrayList<LessonEntry>();
		for(DirEntry dir:dirs)
		{
			    LessonEntry copyLesson =new LessonEntry (
			    		                                 e.getName(), //name
			    		                                 e.getContent(), //content
			    		                                 LessonType.getLessonType(dir.getType()),
			    		                                 getUserId(),
			    		                                 dir.getID(),
			    		                                 e.getImgUrl(),
			    		                                 e.getVideoIds(),
			    		                                 System.currentTimeMillis(),
			    		                                 e.getVideoCount(),
			    		                                 e.getLessonWareCount(),
			    		                                 e.getExerciseCount(),
			    		                                 e.getLessonWareList(),
			    		                                 e.getExercise(),
			    		                                 e.getSourceId(),
			    		                                 e.getIsFromCloud()
			    		                                 );
				lessonList.add(copyLesson);
		}
		lessonService.addLessonEntrys(lessonList);
		lessonService.increasePushCount(lessonId);
       // int score = 0;
        /*for (ObjectId destDirId : dirIdList) {
            int newLessonId = LessonCenter.pushLesson(lesson, destDirId, false);
            pushResult++;
            if (newLessonId > 0) {
                DirInfo destDir = DirCenter.getDir(destDirId);
                if (destDir.getType() == DirType.VOTE) {
//                    dirService.addVoteLesson(newLessonId);
                } else {
                    DirInfo oldDir = DirCenter.getDirByLesson(lessonId);
                    if (oldDir.getType() == DirType.TEACHER) {
                        ExpLogType push = ExpLogType.PUSH;
                        if (experienceService.updateScore(getUserId().toString(), push, lessonId.toString())) {
                            //result.put("score", deletepush.getExp());
                            //result.put("scoreMsg", deletepush.getDesc());
                        }
                    }
                }
            }
        }*/
        ExpLogType push = ExpLogType.PUSH;
        if (experienceService.updateScore(getUserId().toString(), push, lessonId.toString())) {
            //result.put("score", push.getExp());
            //result.put("scoreMsg", push.getDesc());
        }
	    return RespObj.SUCCESS;
	}
	
	/**
	 * 得到课程详情，用于编辑课程
	 * @param lessonId
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/detail")
	@ResponseBody
	public LessonDetailDTO lessonDetail(@ObjectIdType ObjectId lessonId) throws IllegalParamException
	{
		LessonEntry e=lessonService.getLessonEntry(lessonId);
		if(null==e)
		{
			throw new IllegalParamException();
		}
		return buildLessonDTO(e);
	}
	
	/**
	 * 上传视频
	 * @param lessonId
	 * @param
	 * @return
	 * @throws IllegalParamException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws EncoderException 
	 */
	@RequestMapping("/video/upload")
	@ResponseBody
    @SessionNeedless
	public RespObj uploadVideo(@ObjectIdType ObjectId lessonId, MultipartFile Filedata) throws IllegalParamException, IllegalStateException, IOException, EncoderException
	{
		//LessonEntry eff=lessonService.getLessonEntry(lessonId);
//		boolean isPermission=isHavePermission(e);
//		if(!isPermission)
//		{
//			logger.error("Permission lost!lessonId="+lessonId);
//			return RespObj.FAILD;
//		}
		
		String fileName=FilenameUtils.getName(Filedata.getOriginalFilename());
		if(!ValidationUtils.isRequestVideoName(fileName))
		{
			RespObj obj =new RespObj(Constant.FAILD_CODE, "视频名字非法");
			return obj;
		}
		
		//视频filekey
		String videoFilekey =new ObjectId().toString()+Constant.POINT+FilenameUtils.getExtension(fileName);
		String bathPath=Resources.getProperty("upload.file");
		File dir =new File(bathPath);
		if(!dir.exists())
		{
			dir.mkdir();
		}
	   
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
        	 logger.error("", ex);
         }
        if(videoLength==-1){
            videoLength = 60000;//获取不到时间就设为1分钟
        }

 		logger.debug("begin upload video iamge");

        //String imageFilePath = null;
 		//上传图片
 		if(isCreateImage && screenShotFile.exists())
 		{
            RespObj obj=QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);
	 		if(!obj.getCode().equals(Constant.SUCCESS_CODE))
	 		{
	 			QiniuFileUtils.deleteFile(QiniuFileUtils.TYPE_VIDEO, videoFilekey);
	 			obj =new RespObj(Constant.FAILD_CODE, "视频图片上传失败");
	 			return obj;
	 		}
 		}
		VideoEntry ve =new VideoEntry(fileName, videoLength, VideoSourceType.USER_VIDEO.getType(),videoFilekey);
        ve.setID(new ObjectId());
        logger.debug("begin upload video");

        QiniuFileUtils.uploadVideoFile(ve.getID(),videoFilekey, Filedata.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
        if(isCreateImage&&screenShotFile.exists())
		{
		   ve.setImgUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,coverImage));
		}
		ObjectId videoId=videoService.addVideoEntry(ve);
		lessonService.addVideo(lessonId, videoId);
		//删除临时文件
		try
		{
		 savedFile.delete();
		 screenShotFile.delete();
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		return RespObj.SUCCESS;
	}
	/**
	 * 删除课程视频
	 * @param lessonId
	 * @param videoId
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/video/remove")
	@ResponseBody
	public RespObj removeVideo(@ObjectIdType ObjectId lessonId,@ObjectIdType ObjectId videoId) throws IllegalParamException
	{
		LessonEntry e=lessonService.getLessonEntry(lessonId);
		boolean isPermission=isHavePermission(e);
		if(!isPermission)
		{
			logger.error("Permission lost!lessonId="+lessonId);
			return RespObj.FAILD;
		}
		
		VideoEntry videoEntry =videoService.getVideoEntryById(videoId);
		if(null==videoEntry)
		{
			throw new IllegalParamException();
		}
		lessonService.removeVideo(lessonId, videoId);
		return RespObj.SUCCESS;
	}
	
	
	/**
	 * 添加课程文档
	 * @param lid
	 * @param
	 * @param doc
	 * @return
	 * @throws IllegalParamException
	 * @throws IOException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/ware/add")
	@ResponseBody
	public RespObj addLessonDoc(@ObjectIdType ObjectId lid,int ftype, MultipartFile doc) throws IllegalParamException, IOException
	{
		LessonEntry e=lessonService.getLessonEntry(lid);
		boolean isPermission=isHavePermission(e);
		if(!isPermission)
		{
			logger.error("Permission lost!lessonId="+lid);
			return RespObj.FAILD;
		}
		String name=FilenameUtils.getBaseName(doc.getOriginalFilename());
		//上传文件
		RespObj obj=null;
		String docName=null;
		if(null!=doc)
		{
			 docName =new ObjectId().toString()+Constant.POINT+FilenameUtils.getExtension(doc.getOriginalFilename());
			 obj=QiniuFileUtils.uploadFile(docName, doc.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
			if(!obj.getCode().equals(Constant.SUCCESS_CODE))
			{
				return obj;
			}
		}
		LessonWare ware =new LessonWare(Constant.EMPTY, name, docName);
		ware.setSort(ftype);
		ware.setPush(0);
		lessonService.addLessonWare(lid, ware);
		return RespObj.SUCCESS;
	}
	/**
	 * 删除学习附件
	 * @param lessonId
	 * @param wareId
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/ware/remove")
	@ResponseBody
	public RespObj removeWare(@ObjectIdType ObjectId lessonId,@ObjectIdType ObjectId wareId) throws IllegalParamException
	{
		LessonEntry e=lessonService.getLessonEntry(lessonId);
		boolean isPermission=isHavePermission(e);
		if(!isPermission)
		{
			logger.error("Permission lost!lessonId="+lessonId);
			return RespObj.FAILD;
		}
		LessonWare ware=null;
		
		List<LessonWare> list =e.getLessonWareList();
		for(LessonWare w:list)
		{
			if(w.getId().equals(wareId))
			{
				ware=w;
				break;
			}
		}
		if(null==ware)
		{
			throw new IllegalParamException();
		}
		
		lessonService.removeWare(lessonId, ware);
		return RespObj.SUCCESS;
	}

	/**
	 * 推送学习附件
	 * @param lessonId
	 * @param wareId
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/ware/update")
	@ResponseBody
	public RespObj updateWare(@ObjectIdType ObjectId lessonId,String wareId,int sort) throws IllegalParamException
	{
		LessonEntry e=lessonService.getLessonEntry(lessonId);
//		boolean isPermission=isHavePermission(e);
//		if(!isPermission)
//		{
//			logger.error("Permission lost!lessonId="+lessonId);
//			return RespObj.FAILD;
//		}
		if (!StringUtils.isEmpty(wareId)) {
			lessonService.updateWare(lessonId, new ObjectId(wareId));
		} else {

			List<LessonWare> list =e.getLessonWareList();
			if (list!=null && list.size()!=0) {
				List<ObjectId> wids = new ArrayList<ObjectId>();
				for(LessonWare w:list)
				{
					if (w.getSort()==sort && w.getPush()==0) {
						wids.add(w.getId());
					}
				}
				if (wids!=null && wids.size()!=0) {
					for (ObjectId id : wids) {
						lessonService.updateWare(lessonId, id);
					}
				} else {
					return RespObj.FAILD;
				}
			}
		}

		return RespObj.SUCCESS;
	}

    /**上传练习
     * @param lessonId
     * @param examDoc
     * @param parseDoc
     * @param type
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/exe/upload")
    @ResponseBody
    public Map<String,Object> uploadExercise(@ObjectIdType ObjectId lessonId, String name,
                                  @RequestParam(required=false) MultipartFile examDoc,@RequestParam(required=false) MultipartFile parseDoc,@DefaultValue("2") int type,HttpServletRequest req)
                                    throws Exception
    {
        Set<ObjectId> submitCalssIdSet=new HashSet<ObjectId>();
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
        ObjectId parseDocId=new ObjectId();
        if(null!=parseDoc)
        {
            try
            {
                exerciseService.convertPdfAndSwfFile(req,parseDoc, parseDocId);
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
                parseDocId.toString());
        ObjectId id=exerciseService.uploadTestPaper(e);
        logger.info(getSessionValue()+" upload test paper:"+id.toString());

        lessonService.updateExercise(lessonId,id);
        Map<String,Object> retMap = new HashMap<String, Object>();
        retMap.put("exerciseId",id.toString());

        return retMap;
    }

	/**
	 * 删除学习练习
	 * @param lessonId
	 * @param exeId
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/exe/remove")
	@ResponseBody
	public RespObj removeExe(@ObjectIdType ObjectId lessonId,@ObjectIdType ObjectId exeId) throws IllegalParamException
	{
		LessonEntry e=lessonService.getLessonEntry(lessonId);
		boolean isPermission=isHavePermission(e);
		if(!isPermission)
		{
			logger.error("Permission lost!lessonId="+lessonId);
			return RespObj.FAILD;
		}
		if(!e.getExercise().equals(exeId))
		{
			logger.error("param exeId is error");
			return RespObj.FAILD;
		}
		lessonService.deleteExe(lessonId);
		exerciseService.delete(exeId, getUserId());
		return RespObj.SUCCESS;
	}
	/**
	 * 编辑课程保存
	 * @param lessonId
	 * @param name
	 * @param exercieId
	 * @param isSyn 1同步推送 0不推送
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/update")
	@ResponseBody
	public RespObj update(@ObjectIdType ObjectId lessonId,String name,@ObjectIdType(isRequire=false) ObjectId exercieId,int isSyn, String type) throws IllegalParamException
	{
		LessonEntry e=lessonService.getLessonEntry(lessonId);

        if(e.getVideoIds()!=null && e.getVideoIds().size()>0) {
            //替换一下e的封面为第一个视频的封面
            VideoEntry videoEntry = videoService.getVideoEntryById(e.getVideoIds().get(0));
            if(null!=videoEntry)
            {
				if(type==null||"".equals(type)) {
					ExpLogType expLogType = ExpLogType.UPLOAD_LESSON;
					experienceService.updateScore(getUserId().toString(), expLogType, videoEntry.getID().toString());
				}
	            if(videoEntry.getImgUrl()!=null &&!videoEntry.getImgUrl().equals(e.getImgUrl())){
	                lessonService.updateCoverImage(lessonId,videoEntry.getImgUrl());
	            }
            }
        }
		boolean isPermission=isHavePermission(e);
		if(!isPermission)
		{
			logger.error("Permission lost!lessonId="+lessonId);
			return RespObj.FAILD;
		}
		
		if(!ValidationUtils.isRequestLessonName(name))
		{
			logger.error(name+" is not right!");
			return RespObj.FAILD;
		}
		
		lessonService.updateName(lessonId, name);
		if(null!=exercieId)
		{
			lessonService.updateExercise(lessonId, exercieId);
		}
		
		if(Constant.ONE==isSyn)
		{
			DBObject fields =new BasicDBObject(Constant.ID,1);
			Set<ObjectId> set=lessonService.getLessonEntryIdSet(lessonId, getUserId(), fields);
			
			//nm
			FieldValuePair namePair =new FieldValuePair("nm", name);
			
			//vis
			FieldValuePair videoListPair =new FieldValuePair("vis", e.getVideoIds());
			
			//ware
			List<LessonWare> wareList=e.getLessonWareList();
			FieldValuePair warePair =new FieldValuePair("dcl", MongoUtils.fetchDBObjectList(wareList));
			
			
			//exl
			ObjectId exeId=e.getExercise();
			if(null!=exercieId)
			{
				exeId=exercieId;
			}
			FieldValuePair exercisePair =new FieldValuePair("exl", exeId);
			
			lessonService.update(set, namePair,videoListPair,warePair,exercisePair);
		}
		
		return RespObj.SUCCESS;
	}
	/**
	 * 添加一个评论
	 * @param comment
	 * @param lessonId
	 * @return
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/comment")
	@ResponseBody
	public RespObj addComment(String comment,@ObjectIdType ObjectId lessonId) throws IllegalParamException
	{
		RespObj obj= new RespObj(Constant.FAILD_CODE);
		if(!ValidationUtils.isRequestLessonComment(comment))
		{
			obj.setMessage("评论不合法");
			return obj;
		}
		LessonEntry oe=lessonService.getLessonEntry(lessonId);
		if(null==oe)
		{
			throw new IllegalParamException();
		}
		Comment commentEntry =new Comment(getUserId(), comment);
		lessonService.addComment(lessonId, commentEntry);

		return RespObj.SUCCESS;
	}

	/**
	 * 删除用户一条评论
	 * @param lessonId
	 * @param userId
	 * @param time
	 * @return
	 */
	@RequestMapping("/deleteComment")
	@ResponseBody
	public RespObj deleteComment(@ObjectIdType ObjectId lessonId, @ObjectIdType ObjectId userId, long time){
		lessonService.deleteComment(lessonId, userId, time);
		return RespObj.SUCCESS;
	}
	
	/**
	 * 看过某个视频的学生
	 * @param lessonId
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException 
	 * @throws IllegalParamException 
	 */
	@RequestMapping("/video/stu/list")
	@ResponseBody
	public List<UserInfoDTO> getVideoViewRecordUserInfo(@ObjectIdType ObjectId lessonId,int skip,int limit) throws ResultTooManyException, IllegalParamException
	{
		if(limit>Constant.TWENTY)
			throw  new ResultTooManyException();
		LessonEntry oe=lessonService.getLessonEntry(lessonId);
		if(null==oe)
		{
			throw new IllegalParamException();
		}
		return videoViewRecordService.getVideoViewRecordUserInfo(lessonId, skip, limit);
	}
	/**
	 * 得到相关课程和课件列表
	 * @param lessonId
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/relate/ware")
	@ResponseBody
	public Map<String,Object> getRelatedLesson(@ObjectIdType ObjectId lessonId) throws IllegalParamException
	{
		Map<String,Object> retMap =new HashMap<String, Object>();
		List<LessonDTO> lessonDTOList =new ArrayList<LessonDTO>();
		List<IdNameValuePairDTO> lessLessonWareDTOList =new ArrayList<IdNameValuePairDTO>();
		LessonEntry oe=lessonService.getLessonEntry(lessonId);
		if(null==oe)
		{
			throw new IllegalParamException();
		}
		List<LessonWare> LessonWareList=oe.getLessonWareList();
		for(LessonWare ware:LessonWareList)
		{
			try
			{
				IdNameValuePairDTO dto =new IdNameValuePairDTO();
				dto.setName(ware.getName());
                if(ware.getPath().contains("upload")){//兼容老的数据
                    dto.setValue(ware.getPath());
                }else {
                    dto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, ware.getPath()));
                }
				lessLessonWareDTOList.add(dto);
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		
		
		List<ObjectId> dirs =new ArrayList<ObjectId>();
		dirs.add(oe.getDirId());
		List<LessonEntry> list=lessonService.getLessonEntryList(dirs, new BasicDBObject("nm",1).append("im", 1));
		for(LessonEntry e:list)
		{
			if(!e.getID().equals(lessonId))
			{
				lessonDTOList.add(new LessonDTO(e) );
				if(lessonDTOList.size()>=Constant.FOUR)
				{
					break;
				}
			}
		}
		
		retMap.put("relateDTO", lessonDTOList);
		retMap.put("wareDTO", lessLessonWareDTOList);
		return retMap;
	}
	/**
	 * 得到评论列表
	 * @param lessonId
	 * @param skip
	 * @param limit
	 * @return
	 * @throws IllegalParamException
	 * @throws ResultTooManyException 
	 */
	@RequestMapping("/comment/list")
	@ResponseBody
	public Map<String,Object> getComments(@ObjectIdType ObjectId lessonId,int skip,int limit) throws IllegalParamException, ResultTooManyException
	{
		LessonEntry oe=lessonService.getLessonEntry(lessonId);
		if(null==oe)
		{
			throw new IllegalParamException();
		}
		if(limit>=Constant.FIVETY)
		{
			throw new ResultTooManyException();
		}
        Map<String,Object> retmap = new HashMap<String, Object>();
        List<CommentDTO> commentDTOList = lessonService.getCommentList(lessonId, skip, limit);
        int totalCount = lessonService.getCommentList(lessonId,0,10000).size();
        retmap.put("rows",commentDTOList);
        retmap.put("total",totalCount);
		return retmap;
	
	}

    /** 得到班级课程视频观看记录
     * @param lessonId
     * @param timeType
     * @param starttime
     * @param endtime
     * @return
     */
    @RequestMapping("/videoview/list")
    @ResponseBody
    public Map<String,Object> getVideoViewInfo(@ObjectIdType ObjectId lessonId,@ObjectIdType ObjectId classId,int timeType,String starttime, String endtime){

    	
    
        Map<String,Object> retMap = new HashMap<String, Object>();
        //videoViewRecordService.
        //根据lesson 查找dir，查找班级，查找班级内所有学生， 根据lesson 中的videolist找到video ids , 再返回每个视频学生的观看情况
        LessonEntry lessonEntry = lessonService.getLessonEntry(lessonId);

       
        //得到班级
        ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
		String className = "";
		List<ObjectId> studentIds = new ArrayList<ObjectId>();
		if(classEntry != null) {
			studentIds = classEntry.getStudents();
			className = classEntry.getName();
		} else {//兴趣班
			InterestClassEntry interestClassEntry = classService.findInterestClassEntry(classId);
			if(interestClassEntry != null){
				className = interestClassEntry.getClassName();
				List<UserDetailInfoDTO> studentList = classService.findStuByInterestClassId(classId.toString());
				for(UserDetailInfoDTO stu : studentList) {
					studentIds.add(new ObjectId(stu.getId()));
				}
			} else {//选修课
				ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(classId);
				studentIds.addAll(zouBanCourseEntry.getStudentList());
			}
		}
		UserService userService = new UserService();
		Map<ObjectId, UserEntry> userNameMap = userService.getUserEntryMap(studentIds, new BasicDBObject("nm", Constant.ONE));

        List<ObjectId> videoIds = lessonEntry.getVideoIds();
		Map<ObjectId, String> videoNameMap = new HashMap<ObjectId, String>();

        Map<ObjectId,VideoEntry> videoMap = videoService.getVideoEntryMap(videoIds, new BasicDBObject("nm", Constant.ONE));
		if(videoMap != null && videoMap.size()>0){
			for(Map.Entry<ObjectId, VideoEntry> entry:videoMap.entrySet()) {
				VideoEntry ve = entry.getValue();
				videoNameMap.put(ve.getID(),ve.getName());
			}
		}

		Map<ObjectId, ResourceEntry> resMaps=cloudResourceService.getResourceEntryMap(videoIds,Constant.FIELDS);


		if(!resMaps.isEmpty())
		{
			for(Map.Entry<ObjectId, ResourceEntry> entry:resMaps.entrySet())
			{
				ResourceEntry ve = entry.getValue();
				videoNameMap.put(ve.getID(),ve.getName());
			}
		}

        Date startDate = new Date(0);
        Date endDate = new Date();
        if(timeType==2){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            startDate = calendar.getTime();
        }
        if(timeType ==3){
            if(starttime!=null&& !starttime.isEmpty()){
                startDate = DateTimeUtils.stringToDate(starttime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
            }
            if(endtime!=null&& !endtime.isEmpty()){
                endDate = DateTimeUtils.stringToDate(endtime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
            }
        }

        List<VideoViewRecordEntry> videoViewRecordEntryList = videoViewRecordService.getVideoViewRecordByVideoId(videoIds,studentIds,
                startDate.getTime(),endDate.getTime()
        );

        List<StudentVideoViewDTO> resultData = new ArrayList<StudentVideoViewDTO>();
        //对于每一个视频，统计一下学生观看的情况
        for(ObjectId videoId:videoIds){
            List<VideoViewRecordDTO> videoViewRecordDTOList = new ArrayList<VideoViewRecordDTO>();
            int viewNumber = 0;
            int endViewNumber = 0;
            int notViewNumber = studentIds.size();

            for(ObjectId studentId:studentIds){
				UserEntry userEntry = userNameMap.get(studentId);
				if(userEntry == null){
					continue;
				}
				VideoViewRecordDTO userVideoViewDTO = getUserVideoView(videoViewRecordEntryList, videoId, studentId);
				if (userVideoViewDTO == null) {
					userVideoViewDTO = new VideoViewRecordDTO();
					userVideoViewDTO.setViewCount(0);
					userVideoViewDTO.setViewState(-1);
					userVideoViewDTO.setClassInfo(new IdValuePairDTO(classId, className));
					userVideoViewDTO.setUserInfo(new IdValuePairDTO(studentId, userEntry.getUserName()));
				} else {
					if (userVideoViewDTO.getViewState() == 1) {
						endViewNumber++;
					}
					viewNumber++;
					notViewNumber--;
				}
				videoViewRecordDTOList.add(userVideoViewDTO);
            }

			Comparator<VideoViewRecordDTO> comparator = new Comparator<VideoViewRecordDTO>() {
				@Override
				public int compare(VideoViewRecordDTO recordDTO1, VideoViewRecordDTO recordDTO2) {
					int viewState1 = recordDTO1.getViewState();
					int viewState2 = recordDTO2.getViewState();
					if(viewState2 > viewState1){
						return 1;
					} else if(viewState2 < viewState1){
						return -1;
					} else {
						int viewCount1 = recordDTO1.getViewCount();
						int viewCount2 = recordDTO2.getViewCount();
						if(viewCount2 > viewCount1){
							return 1;
						} else if(viewCount2 < viewCount1){
							return -1;
						} else {
							long viewTime1 = recordDTO1.getViewTime();
							long viewTime2 = recordDTO2.getViewTime();
							if(viewTime1 > viewTime2){
								return 1;
							} else if(viewTime1 < viewTime2){
								return -1;
							} else {
								return 0;
							}
						}
					}
				}
			};

			Collections.sort(videoViewRecordDTOList, comparator);


            //HashMap<String,Object> videoData = new HashMap<String, Object>();
            StudentVideoViewDTO studentVideoViewDTO = new StudentVideoViewDTO();
            studentVideoViewDTO.setStudentrecord(videoViewRecordDTOList);
            studentVideoViewDTO.setViewNumber(viewNumber);
            studentVideoViewDTO.setEndViewNumber(endViewNumber);
            studentVideoViewDTO.setNotViewNumber(notViewNumber);
            studentVideoViewDTO.setVideoName(videoNameMap.get(videoId));
            //videoData.put("")
            resultData.add(studentVideoViewDTO);
        }



        retMap.put("rows",resultData);
        return retMap;
    }

    private VideoViewRecordDTO getUserVideoView(List<VideoViewRecordEntry> videoViewRecordEntryList,ObjectId videoId,
                                                ObjectId userId){
        VideoViewRecordDTO videoViewRecordDTO = null;
        for(VideoViewRecordEntry videoViewRecordEntry:videoViewRecordEntryList){
            if(videoViewRecordEntry.getUserInfo().getId().equals(userId) &&
                    videoViewRecordEntry.getVideoInfo().getId().equals(videoId)){
                if(videoViewRecordDTO == null){
                    videoViewRecordDTO = new VideoViewRecordDTO(videoViewRecordEntry);
                }
                else{

                    //如果时间是比这个小
                    if(videoViewRecordEntry.getLastViewTime()>videoViewRecordDTO.getViewTime()){
                        videoViewRecordDTO.setViewTime(videoViewRecordEntry.getLastViewTime());
                    }
                    //看完，未看完的设置
                    if(videoViewRecordEntry.getState() == 1){
                        videoViewRecordDTO.setViewState(1);
                    }
					videoViewRecordDTO.setViewCount(videoViewRecordDTO.getViewCount()+1);

                }
            }
        }
        return videoViewRecordDTO;
    }



    //长轮询检测上传状态
    private final Map<String, List<DeferredResult<String>>> uploadStatus = new ConcurrentHashMap<String, List<DeferredResult<String>>>();

    @RequestMapping("/getUploadStatus")
    @SessionNeedless
    public @ResponseBody DeferredResult<String> getUploadStatus(final String lessonId) {
        List<DeferredResult<String>> lessonDRList = uploadStatus.get(lessonId);
        if (lessonDRList == null) {
            lessonDRList = new ArrayList<DeferredResult<String>>();
            uploadStatus.put(lessonId, lessonDRList);
        }

        final DeferredResult<String> dr = new DeferredResult<String>(60000);

        final List<DeferredResult<String>> finalLessonDRList = lessonDRList;
        dr.onCompletion(new Runnable() {
            @Override
            public void run() {
                finalLessonDRList.remove(dr);
                if (finalLessonDRList.isEmpty()) {
                    uploadStatus.remove(lessonId);
                }
            }
        });

        lessonDRList.add(dr);
        return dr;
    }

    @RequestMapping("/finishedUpload")
    @SessionNeedless
    public @ResponseBody boolean finishedUpload(String lessonId) {
        List<DeferredResult<String>> lessonDRList = uploadStatus.get(lessonId);
        if (lessonDRList != null) {
            for (DeferredResult<String> dr : lessonDRList) {
                dr.setResult("Success");
            }
            return true;
        } else {
            return false;
        }
    }
    
    
    private LessonDetailDTO buildLessonDTO(LessonEntry e) {
		LessonDetailDTO dto =new LessonDetailDTO(e);
		List<ObjectId> videoList =e.getVideoIds();
		SimpleDTO simpleDTO=null;
		VideoDTO videoDTO =null;
		//视频
		if(!videoList.isEmpty())
		{
			//从视频表中
			Map<ObjectId, VideoEntry> maps=videoService.getVideoEntryMap(videoList, Constant.FIELDS);
			if(!maps.isEmpty())
			{
				for(Map.Entry<ObjectId, VideoEntry> entry:maps.entrySet())
				{
                    VideoEntry ve = entry.getValue();

					videoDTO =new VideoDTO(ve); //todo video location
                    if(ve.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
						videoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, entry.getValue().getBucketkey()));
                    }else if(ve.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType())
                    {
						videoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, entry.getValue().getBucketkey()));
                    }else if(ve.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType())
                    {
						videoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, entry.getValue().getBucketkey()));
                    }
					int role = getSessionValue().getUserRole();
					if(UserRole.isStudent(role)) {
						String state = getStuVideoState(ve.getID());
						videoDTO.setValue2(state);
					} else {
						videoDTO.setValue2(videoDTO.getType());
					}
					dto.addDTOToVidesList(videoDTO);
				}
			}
			
			
			Map<ObjectId, ResourceEntry> resMaps=cloudResourceService.getResourceEntryMap(videoList,Constant.FIELDS);
			
			if(!resMaps.isEmpty())
			{
				for(Map.Entry<ObjectId, ResourceEntry> entry:resMaps.entrySet())
				{
					ResourceEntry ve = entry.getValue();

					videoDTO =new VideoDTO(ve); //todo video location
                    if(ve.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
						videoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, entry.getValue().getBucketkey()));
                    }else if(ve.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType())
                    {
						videoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, entry.getValue().getBucketkey()));
                    }else if(ve.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType())
                    {
						videoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, entry.getValue().getBucketkey()));
                    }
					int role = getSessionValue().getUserRole();
					if(UserRole.isStudent(role)) {
						String state = getStuVideoState(ve.getID());
						videoDTO.setValue2(state);
					} else {
						videoDTO.setValue2(videoDTO.getType());
					}
					dto.addDTOToVidesList(videoDTO);
				}
			}
		}
		//课件
		List<LessonWare> lessonWareList =e.getLessonWareList();
		
		if(!lessonWareList.isEmpty())
		{
			
			Map<ObjectId, ResourceEntry> wareMaps=cloudResourceService.getResourceEntryMap(MongoUtils.getFieldObjectIDs(lessonWareList, "id"),new BasicDBObject(Constant.ID,1));
			
			for(LessonWare ware:lessonWareList)
			{
				try
				{
					simpleDTO =new SimpleDTO(ware);
					
					
					if(!wareMaps.containsKey(ware.getId()))
					{

		                if(ware.getPath().contains("upload")){//兼容老的数据
		                    simpleDTO.setValue(ware.getPath());
							simpleDTO.setValue1(ware.getPath());
		                }else {
		                   simpleDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, ware.getPath()));



							String path = ware.getPath();
							String suffix = path.substring(path.lastIndexOf("."));
		                	 simpleDTO.setValue("/commonupload/doc/down.do?type="+QiniuFileUtils.TYPE_DOCUMENT+"&fileKey="+ware.getPath()+"&fileName="+URLEncoder.encode(ware.getName().replace(" ", "") + suffix,"utf-8"));
		                }
					}
					else
					{
						simpleDTO.setValue("/cloudres/down.do?id="+ware.getId());
						simpleDTO.setValue1("/cloudres/down.do?id=" + ware.getId());
					}
	                
	                
	                String image="/img/coursecommon.png";
					int type1= 0;
	                if(simpleDTO.getValue().toString().indexOf(".doc")>0 || simpleDTO.getValue().toString().indexOf(".docx")>0) 
	                {
	                	image="/img/coursedoc.png";
						type1 = 1;
	                }
	            	if(simpleDTO.getValue().toString().indexOf(".pdf")>0 )
	            	{
	            		image="/img/coursepdf.png";
	            	}
	            	if(simpleDTO.getValue().toString().indexOf(".xls")>0 || simpleDTO.getValue().toString().indexOf(".xlsx")>0)
	            	{
	            		image="/img/coursexls.png";
						type1 = 1;
	            	}
	            	if(simpleDTO.getValue().toString().indexOf(".ppt")>0 || simpleDTO.getValue().toString().indexOf(".pptx")>0)
	            	{
	            		image="/img/courseppt.png";
						type1 = 1;
	            	}
	            	if(simpleDTO.getValue().toString().indexOf(".swf")>0)
	            	{
	            		image="/img/courseswf.png";
	            	}
					simpleDTO.setType1(type1);
	            	simpleDTO.setValue3(image);
					simpleDTO.setValue4(ware.getSort());
					simpleDTO.setValue5(ware.getPush());
					simpleDTO.setValue2(ware.getFileType());
					dto.addDTOToCoursewareList(simpleDTO);
				}catch(Exception ex)
				{
					logger.error("", ex);
				}

			}
		}
		//小练习
		ObjectId exeId=e.getExercise();
		ExerciseEntry cde= exerciseService.getExerciseEntry(exeId);
		if(null!=cde)
		{
			dto.setExercise(new SimpleDTO(cde));
		}
		return dto;
	}

	private String getStuVideoState(final ObjectId videoId){
		String state = "未看过";
		List<ObjectId> videos = new ArrayList<ObjectId>(){{add(videoId);}};
		List<ObjectId> userIds = new ArrayList<ObjectId>(){{add(getUserId());}};
		List<VideoViewRecordEntry> recordEntries = videoViewRecordService.getVideoViewRecordByVideoId(videos, userIds, 0, new Date().getTime());
		if(recordEntries.size() > 0){
			state = "已点击";
			for(VideoViewRecordEntry recordEntry : recordEntries){
				if(recordEntry.getState() == 1){
					state = "已看完";
					break;
				}
			}
		}
		return state;
	}
    
    /**
     * 为 LessonEntry list 添加练习数目
     * @param lessonList
     * @return
     */
    private Map<ObjectId, LessonDTO> attachExeitemCount(
			List<LessonEntry> lessonList) {
		Map<ObjectId, LessonDTO> map =new HashMap<ObjectId, LessonDTO>();
        LessonDTO dto;
        for (LessonEntry e : lessonList) {
        	dto=new LessonDTO(e);
        	if(null==e.getExercise())
        	{
        		map.put(new ObjectId(), dto);
        	}
        	else
        	{
        	  map.put(e.getExercise(), dto);
        	}
        }
        Map<ObjectId, Integer> countMap= exerciseItemService.statItemCount(map.keySet());
        if(countMap.size()>0)
        {
        	for(Map.Entry<ObjectId, Integer> entry:countMap.entrySet())
        	{
        		dto=map.get(entry.getKey());
        		if(null!=dto)
        		{
        			dto.setExerciseCount(entry.getValue());
        		}
        	}
        }
		return map;
	}

	
	/**
	 * 检查当前登录者是不是有权限修改此课程
	 * @param e
	 * @throws IllegalParamException 
	 */
	private boolean  isHavePermission(LessonEntry e) throws IllegalParamException {
		//todo
		return true;
		
		/***
		if(null==e)
			throw new IllegalParamException();
		boolean isPermission=false;
		ObjectId myId=getUserId();
		
		if(myId.equals(e.getUserId()))
		{
			isPermission=true;
		}
		
		//是不是校本资源
		if(!isPermission) 
		{
			DirEntry dirEntry =dirService.getDirEntry(e.getDirId(), null);
			if(null!=dirEntry && dirEntry.getOwerId().toString().equals(getSessionValue().get(SessionValue.SCHOOL_ID)))
			{
				isPermission=true;
			}
		}
	
	    //是不是联盟资源
		if(!isPermission) 
		{
			List<LeagueEnrty> LeagueEnrtyList=leagueService.getLeagueEnrtys(new ObjectId(getSessionValue().get(SessionValue.SCHOOL_ID)), new BasicDBObject(Constant.ID,1));
			List<ObjectId> leagusIds =MongoUtils.getFieldObjectIDs(LeagueEnrtyList, Constant.ID);
			
			
			List<DirEntry> dirs=dirService.getDirEntryList(leagusIds,new BasicDBObject(Constant.ID,1));
			List<ObjectId> dirIds =MongoUtils.getFieldObjectIDs(dirs, Constant.ID);
			
			isPermission=lessonService.isExists(e.getID(), dirIds);
		}
		return isPermission;
		**/
	
	}

	//================================================整合=============================================
	/**
	 * 新建课程
	 * @param name
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/createclasslesson")
	@ResponseBody
	public RespObj createClassLesson(String name) throws IllegalParamException {
		if(!ValidationUtils.isRequestLessonName(name))
		{
			return RespObj.FAILD;
		}
		LessonEntry e =new LessonEntry(name, name,LessonType.BACKUP_LESSON, getUserId(),null,null,Constant.ZERO);
		ObjectId id=lessonService.addLessonEntry(e);
		RespObj obj =new RespObj(Constant.SUCCESS_CODE, id.toString());
		return obj;
	}

	/**
	 * 得到课程相关
	 * @param lessonId
	 * @return
	 */
	@RequestMapping("/getviewlesson")
	@ResponseBody
	public Map<String, Object> getViewLesson(@ObjectIdType ObjectId lessonId,HttpServletRequest req) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			viewLesson(lessonId, model,req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}


    @RequestMapping("/pushToHomework")
    @ResponseBody
    public Map<String, Object> pushToHomework(@ObjectIdType ObjectId lessonId,String hwTitle,
                                              String hwContent , String classIdList,
                                              String subjectId,
                                              String hwType,String voiceUrl,int pg,int isFromCloud) {
        Map<String, Object> model = new HashMap<String, Object>();
        //创建一个班级课程，并创建一份作业关联它



        String[] classes = classIdList.split(";");
        Map<ObjectId, IdValuePair> classPairMap = new HashMap<ObjectId, IdValuePair>();
		Map<ObjectId, IdValuePair> classLessonMap = new HashMap<ObjectId, IdValuePair>();
        String[] claArr=new String[classes.length];
		int[] index = new int[classes.length];
        for(int i=0;i<classes.length;i++){
            claArr[i] = classes[i].split(",")[1];
			index[i] = Integer.parseInt(classes[i].split(",")[2]);
        }
		List<ObjectId> stuList = new ArrayList<ObjectId>();
		List<ObjectId> classList  =new ArrayList<ObjectId>();
		for(int i=0; i<claArr.length; i++) {
			String cla = claArr[i];
            if(null==cla || !ObjectId.isValid(cla))
            {
                model.put("message", "班级参数错误");
                return model;
            }
            IdValuePair clapair =new IdValuePair(new ObjectId(cla), Constant.EMPTY);
			IdValuePair clapair1 =new IdValuePair(new ObjectId(cla), index[i]);
            classPairMap.put(clapair.getId(), clapair);
			classLessonMap.put(clapair1.getId(), clapair1);
			classList.add(new ObjectId(cla));

			ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(new ObjectId(cla));
			if(zouBanCourseEntry != null){//选修课
				stuList.addAll(zouBanCourseEntry.getStudentList());
			} else {
				stuList.addAll(classService.findStuByClassId(new ObjectId(cla)));
			}
        }
        List<TeacherClassSubjectDTO> list=teacherClassLessonService.getTeacherClassSubjectDTOList(getUserId(), classPairMap.keySet());

        if(list.size()!=classPairMap.size())
        {
            //return obj;
        }
        ObjectId classId;
        for(TeacherClassSubjectDTO dto:list)
        {
            classId=dto.getClassInfo().getId();
            classPairMap.get(classId).setValue(dto.getClassInfo().getValue());
        }

        IdNameValuePair voiceivp = null;
        if(voiceUrl!=null && voiceUrl != ""){
            voiceivp= new IdNameValuePair(new ObjectId(),"voice",voiceUrl);
        }

        HomeWorkEntry e =new HomeWorkEntry(getUserId(), new ArrayList<IdValuePair>(classPairMap.values()), hwTitle, HtmlUtils.delScriptTag(hwContent),
                voiceivp, null, new ArrayList<IdValuePair>(classLessonMap.values()));
        e.setVersion(1);
        e.setType(Integer.valueOf(hwType));
        ExamResultService examResultService = new ExamResultService();
        e.setTerm(examResultService.getCurrentTerm());
		List<ObjectId> subjectList = new ArrayList<ObjectId>();
		subjectList.add(new ObjectId(subjectId));
        e.setSubjectIdList(subjectList);
        e.setCorrect(pg);
		e.setIsFromCloud(isFromCloud);


        LessonEntry lessonEntry=lessonService.getLessonEntry(lessonId);
		//复制课后练习
		ObjectId exeId = lessonEntry.getExercise();
		ExerciseEntry exerciseEntry = exerciseService.getExerciseEntry(exeId);
		ObjectId newExeId = new ObjectId();
		if(exerciseEntry != null) {
			exerciseEntry.setID(newExeId);
			exerciseService.uploadTestPaper(exerciseEntry);
			List<ExerciseItemEntry> exerciseItemEntries = exerciseItemService.getExerciseItemEntrys(exeId, Constant.FIELDS);
			if (exerciseItemEntries != null && exerciseItemEntries.size() > 0) {
				for (ExerciseItemEntry entry : exerciseItemEntries) {
					entry.setDocumentId(newExeId);
					entry.setID(new ObjectId());
				}
			}
			exerciseItemService.addEntrys(exerciseItemEntries);
		} else {
			newExeId = null;
		}

		List<ObjectId> videoIds = lessonEntry.getVideoIds();
		List<ObjectId> vids = new ArrayList<ObjectId>();
		if(videoIds.size() > 0){
			for(ObjectId videoId : videoIds){
				VideoEntry videoEntry = videoService.getVideoEntryById(videoId);
				
				if(null!=videoEntry)
				{
				ObjectId id = new ObjectId();
				vids.add(id);
				videoEntry.setID(id);
				videoService.addVideoEntry(videoEntry);
				}
			}
		}

        LessonEntry copyLesson =new LessonEntry (
                lessonEntry.getName(), //name
                lessonEntry.getContent(), //content
                LessonType.CLASS_LESSON,
                getUserId(),
                new ObjectId("000000000000000000000000"),
                lessonEntry.getImgUrl(),
				vids,
				System.currentTimeMillis(),
                lessonEntry.getVideoCount(),
                lessonEntry.getLessonWareCount(),
                lessonEntry.getExerciseCount(),
                lessonEntry.getLessonWareList(),
				newExeId,
                lessonEntry.getSourceId(),
                lessonEntry.getIsFromCloud()
        );

        lessonService.addLessonEntry(copyLesson);

        e.setLessonId(copyLesson.getID());

		//设置多媒体个数
		e.setFileNum(lessonEntry.getDocumentCount());
		e.setVoiceNum(e.getVoiceFile().size());
		e.setVideoNum(lessonEntry.getVideoIds().size());
		List<ObjectId> exercises = new ArrayList<ObjectId>();
		ObjectId exerciseId = lessonEntry.getExercise();
		exercises.add(exerciseId);
		Map<ObjectId, Integer> map = exerciseItemService.statItemCount(exercises);
		Integer exerciseNum = exerciseId==null? new Integer(0) : map.get(exerciseId);
		if(exerciseNum == null){
			e.setExerciseNum(0);
		} else {
			e.setExerciseNum(exerciseNum);
		}


        HomeWorkService homeWorkService = new HomeWorkService();
		ObjectId id = homeWorkService.addHomeWork(e);
		System.out.println(id);

		//增加提醒
		ReminderEntry reminderEntry = new ReminderEntry(getUserId(),stuList,classList,subjectList,id);
		reminderService.addReminder(reminderEntry);

		ExpLogType notifyScore = ExpLogType.PUSH;
		if(experienceService.updateScore(getUserId().toString(), notifyScore, id.toString())){
			model.put("desc", notifyScore.getDesc());
			model.put("score", notifyScore.getExp());
		}
        model.put("result", "ok");
        return model;
    }
}
