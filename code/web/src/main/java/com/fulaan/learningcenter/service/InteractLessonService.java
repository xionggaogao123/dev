package com.fulaan.learningcenter.service;

import com.db.school.InteractLessonDao;
import com.fulaan.cache.CacheHandler;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.learningcenter.dto.InteractLessonDTO;
import com.fulaan.learningcenter.dto.SubjectDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.mongodb.BasicDBObject;
import com.pojo.school.*;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.DeleteState;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.*;

/**
 * 互动课堂service
 * @author fourer
 *
 */
@Service
public class InteractLessonService {

	private static final Logger logger =Logger.getLogger(InteractLessonService.class);

	@Resource
	private TeacherClassSubjectService teacherClassSubjectService;

	@Resource
	private ClassService classService;

	@Resource
	private SchoolService schoolService;

	private InteractLessonDao interactLessonDao =new InteractLessonDao();

	private VideoService videoService = new VideoService();

	@Resource
	private UserService userService;

	@Resource
	private ExperienceService experienceService;
	/**
	 * 新建互动课堂
	 * @param e
	 * @return
	 */
	public ObjectId addInteractLessonEntry(InteractLessonEntry e)
	{
		return interactLessonDao.addInteractLessonEntry(e);
	}
	
	
	/**
	 * 
	 * @param userId 
	 * @param schoolId
	 * @param classId
	 * @param push
	 * @return
	 */
	public List<InteractLessonEntry> getInteractLessonEntryList(ObjectId userId,ObjectId schoolId,ObjectId classId,int push)
	{
		return interactLessonDao.getInteractLessonEntryList(userId, schoolId, classId,push);
	}
	
	/**
	 * 根据视频Id查询
	 * @param id
	 * @return
	 */
	public InteractLessonEntry getInteractLessonEntryByVideoId(ObjectId id)
	{
		return interactLessonDao.getInteractLessonEntryByVideoId(id);
	}

	/**
	 * 根据Id查询
	 * @param id
	 * @return
	 */
	public InteractLessonEntry getInteractLessonEntryById(ObjectId id)
	{
		return interactLessonDao.getInteractLessonEntryById(id);
	}
	
	/**
	 * 根据_id删除
	 * @param id
	 */
	public void removeInteractLessonEntry(ObjectId id)
	{
		interactLessonDao.removeInteractLessonEntry(id);
	}
	
	/**
	 * 推送课程
	 * @param id
	 */
	public void pushInteractLesson(ObjectId id)
	{
		interactLessonDao.pushInteractLesson(id);
	}

	/**
	 * 新建互动课堂
	 * @param subjectId
	 *
	 * @return
	 */
	public String addInteractLessonEntry(ObjectId subjectId,ObjectId schoolId)
	{
		try {
			String currDate= DateTimeUtils.getCurrDate();
			String key=   CacheHandler.getKeyString(CacheHandler.CACHE_TEACHER_INTERACT_LESSON, currDate + subjectId.toString());
			String value = CacheHandler.getStringValue(key);
			if (value!=null&&!"".equals(value)){
				CacheHandler.cache(key, value, Constant.SESSION_TEN_MINUTE);
				return value;
			}
			TeacherClassSubjectEntry tcSubject=teacherClassSubjectService.getTeacherClassSubjectEntry(subjectId);
			InteractLessonEntry e=new InteractLessonEntry(tcSubject.getTeacherId(), schoolId, tcSubject.getClassInfo().getId(), tcSubject.getID(), null,"");
			ObjectId lessonId=addInteractLessonEntry(e);
			Map<String, String> result=new HashMap<String, String>();
			result.put("lessonId",lessonId.toString());
			ExpLogType interactLesson = ExpLogType.INTERACT_LESSON;
			experienceService.updateScore(tcSubject.getTeacherId().toString(), interactLesson, lessonId.toString());
			String jsonstr = JSONObject.valueToString(result);
			CacheHandler.cache(key, jsonstr, Constant.SESSION_TEN_MINUTE);
			return jsonstr;
		}catch (Exception ex) {
			logger.error("", ex);
		}
		return null;
	}

	/**
	 * 修改互动课堂
	 * @param e
	 * @return
	 */
	public void updInteractLessonEntry(InteractLessonEntry e) {
		interactLessonDao.updInteractLessonEntry(e);
	}

	/**
	 * 获取首页班级
	 * @param userRole
	 * @param userid
	 * @param schoolid
	 * @param model
	 */
	public void getInteractLessonClassInfo(int userRole,ObjectId userid,String schoolid,Model model) throws IllegalParamException {
//		Map<ObjectId,Subject> subjectMap = schoolService.getSubjectEntryMap(schoolid);
		SchoolDTO schoolDTO = schoolService.findSchoolById(schoolid);
		List<SubjectDTO> subjectlist = new ArrayList<SubjectDTO>();
		Map<String,SubjectDTO> stMap=new HashMap<String,SubjectDTO>();
		List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
		ClassInfoDTO classInfoDTO = new ClassInfoDTO();
		SubjectDTO subjectDTO = null;
		if(UserRole.isStudent(userRole))
		{
			ClassEntry e=classService.getClassEntryByStuId(userid,Constant.FIELDS);

			List<TeacherClassSubjectEntry> list=teacherClassSubjectService.findEntryByClassId(e.getID());
			for(TeacherClassSubjectEntry sub:list){
				if(sub.getSubjectInfo()!=null){
					subjectDTO = new SubjectDTO();
					subjectDTO.setSubjectId(sub.getSubjectInfo().getId().toString());
					subjectDTO.setSubjectName(sub.getSubjectInfo().getValue().toString());
					stMap.put(subjectDTO.getSubjectId(),subjectDTO);
				}
			}
			for(Map.Entry<String,SubjectDTO> dto : stMap.entrySet()){
				subjectlist.add(dto.getValue());
			}
			classInfoDTO.setClassName(e.getName());
			classInfoDTO.setId(e.getID().toString());
			model.addAttribute("classinfo",classInfoDTO);
			model.addAttribute("subject", subjectlist);
		}
		if(UserRole.isTeacher(userRole))
		{
			List<InteractLessonEntry> list=getInteractLessonEntryList(userid, null, null, Constant.NEGATIVE_ONE);
//			List<ClassInfoDTO> classInfoDTOList1 = classService.getSimpleClassInfoDTOs(userid, new ObjectId(schoolid));
//			Map<ObjectId, ClassEntry> classMap= classService.getClassEntryMap(MongoUtils.getFieldObjectIDs(list, "cid"), new BasicDBObject("nm", 1));
			Set<ObjectId> tcsids = new HashSet<ObjectId>();
			if (list!=null && list.size()!=0) {
				for (InteractLessonEntry interactLessonEntry : list) {
					if (!tcsids.contains(interactLessonEntry.getSubjectId())) {
						tcsids.add(interactLessonEntry.getSubjectId());
					}
				}
			}
			List<TeacherClassSubjectEntry> teacherClassSubjectEntryMap = teacherClassSubjectService.getTeacherClassSubjectEntry(tcsids);
			if (teacherClassSubjectEntryMap!=null && teacherClassSubjectEntryMap.size()!=0) {
				for (TeacherClassSubjectEntry tcsub : teacherClassSubjectEntryMap) {
					classInfoDTO = new ClassInfoDTO();
					classInfoDTO.setClassName(tcsub.getClassInfo().getValue()+"("+tcsub.getSubjectInfo().getValue()+")");
					classInfoDTO.setId(tcsub.getID().toString());
					classInfoDTOList.add(classInfoDTO);
				}
			}

			model.addAttribute("classinfo",classInfoDTOList);
		}
	}

	/**
	 * 获取课
	 * @param csid
	 * @param classid
	 * @param subjectid
	 * @param role
	 */
	public List<InteractLessonDTO> getInteractLessonList(String csid, String classid, String subjectid,int role,int page, int pageSize) {
		List<InteractLessonEntry> interactLessonEntries = new ArrayList<InteractLessonEntry>();
		if (UserRole.isStudent(role)) {
			List<ObjectId> tcsids = new ArrayList<ObjectId>();
			List<ObjectId> cids = new ArrayList<ObjectId>();
			if(classid!=null&&!"".equals(classid)){
				cids.add(new ObjectId(classid));
				if(subjectid!=null&&!"".equals(subjectid)) {
					List<TeacherClassSubjectEntry> tcsList = teacherClassSubjectService.findTeacherClassSubjectByParam(cids, new ObjectId(subjectid));
					for(TeacherClassSubjectEntry tcs :tcsList){
						tcsids.add(tcs.getID());
					}
				}
				interactLessonEntries = interactLessonDao.getInteractLessonListByParam(cids, tcsids, page < 1 ? 0 : ((page - 1) * pageSize), pageSize);
			}
		} else if (UserRole.isTeacher(role)) {
			if(csid!=null&&!"".equals(csid)) {
				interactLessonEntries = interactLessonDao.getInteractLessonList(new ObjectId(csid), Constant.NEGATIVE_ONE, page < 1 ? 0 : ((page - 1) * pageSize), pageSize);
			}
		}
		List<InteractLessonDTO> interactLessonDTOs = new ArrayList<InteractLessonDTO>();
		InteractLessonDTO interactLessonDTO = null;

		List<ObjectId> vids = new ArrayList<ObjectId>();
		if (interactLessonEntries!=null && interactLessonEntries.size()!=0) {
			for (InteractLessonEntry interactLessonEntry : interactLessonEntries) {
				vids.add(interactLessonEntry.getVideoId());
			}
			Map<ObjectId, VideoEntry> videoEntryMap = videoService.getVideoEntryMap(vids,null);
			for (InteractLessonEntry interactLessonEntry : interactLessonEntries) {
				interactLessonDTO = new InteractLessonDTO();
				interactLessonDTO.setName(interactLessonEntry.getLessonName());
				interactLessonDTO.setLock(interactLessonEntry.getLock());
				interactLessonDTO.setDate(DateTimeUtils.convert(interactLessonEntry.getCreateTime(), DateTimeUtils.DATE_YYYY_MM_DD));
				interactLessonDTO.setIlid(interactLessonEntry.getID().toString());
				interactLessonDTO.setImgurl("/images/youj.png");
				if (videoEntryMap.get(interactLessonEntry.getVideoId())!=null) {
					VideoEntry videoEntry=videoEntryMap.get(interactLessonEntry.getVideoId());
					if(videoEntry.getImgUrl()!=null&&!"".equals(videoEntry.getImgUrl())){
						interactLessonDTO.setImgurl(videoEntry.getImgUrl());
					}
				}
				interactLessonDTOs.add(interactLessonDTO);
			}
		}
	return interactLessonDTOs;
	}

	/**
	 * 对互动课堂编辑
	 * @param lessonId 互动课堂Id
	 * @param type:1:修改名称，2：开锁解锁，3：删除
	 * @param lessonName：互动课堂名称
	 * @return
	 */
	public void editInteractLessonEntry(ObjectId lessonId, int type, String lessonName) {
		InteractLessonEntry entry=getInteractLessonEntryById(lessonId);
		if(type==1){
			entry.setLessonName(lessonName);
		}
		if(type==2){
			if(entry.getLock()==Constant.ZERO){
				entry.setLock(Constant.ONE);
			}else{
				entry.setLock(Constant.ZERO);
			}
		}
		if(type==3){
			entry.setDeleteState(DeleteState.DELETED.getState());
		}
		interactLessonDao.updateInteractLessonEntry(entry);
	}

	/**
	 * 获取互动课堂
	 * @param lessonId
	 */
	public InteractLessonDTO getInteractLessonDTOById(ObjectId lessonId) {
		InteractLessonDTO interactLessonDTO = new InteractLessonDTO();
		InteractLessonEntry lessonEntry=getInteractLessonEntryById(lessonId);
		interactLessonDTO.setName(lessonEntry.getLessonName());
		interactLessonDTO.setDate(DateTimeUtils.convert(lessonEntry.getCreateTime(), DateTimeUtils.DATE_YYYY_MM_DD));
		interactLessonDTO.setIlid(lessonEntry.getID().toString());
		interactLessonDTO.setClassId(lessonEntry.getClassId().toString());
		List<ObjectId> videoList = new ArrayList<ObjectId>();
		videoList.add(lessonEntry.getVideoId());
		VideoDTO VideoDTO =null;
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

					VideoDTO =new VideoDTO(ve); //todo video location
					if(ve.getVideoSourceType() == VideoSourceType.USER_VIDEO.getType()){
						VideoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, entry.getValue().getBucketkey()));
					}else if(ve.getVideoSourceType() == VideoSourceType.VIDEO_CLOUD_CLASS.getType())
					{
						VideoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, entry.getValue().getBucketkey()));
					}else if(ve.getVideoSourceType() == VideoSourceType.SWF_CLOUD_CLASS.getType())
					{
						VideoDTO.setValue1(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, entry.getValue().getBucketkey()));
					}
					interactLessonDTO.addDTOToVidesList(VideoDTO);
				}
			}
		}

		return interactLessonDTO;
	}

	/**
	 *
	 * @param csid
	 * @param classid
	 * @param subjectid
	 * @param userRole
	 * @return
	 */
	public int getInteractLessonCount(String csid, String classid, String subjectid, int userRole) {
		int count = 0;
		if (UserRole.isStudent(userRole)) {
			List<ObjectId> tcsids = new ArrayList<ObjectId>();
			List<ObjectId> cids = new ArrayList<ObjectId>();
			if(classid!=null&&!"".equals(classid)){
				cids.add(new ObjectId(classid));
				if(subjectid!=null&&!"".equals(subjectid)) {
					List<TeacherClassSubjectEntry> tcsList = teacherClassSubjectService.findTeacherClassSubjectByParam(cids, new ObjectId(subjectid));
					for(TeacherClassSubjectEntry tcs :tcsList){
						tcsids.add(tcs.getID());
					}
				}
				count = interactLessonDao.getInteractLessonCountByParam(cids, tcsids);
			}
		} else if (UserRole.isTeacher(userRole)) {
			if(csid!=null&&!"".equals(csid)) {
				count = interactLessonDao.getInteractLessonCount(new ObjectId(csid), Constant.NEGATIVE_ONE);
			}
		}
		return count;
	}

	public List<Map<String,String>> getInteractLessonUseTeaList(ObjectId schoolId, String gradeId, String teaName) {
		List<Map<String,String>> relist=new ArrayList<Map<String, String>>();

		List<ObjectId> teaIds=new ArrayList<ObjectId>();
		if(!"".equals(gradeId)&&!"".equals(teaName)) {
			List<ClassInfoDTO> clsLis=classService.findClassByGradeId(gradeId);
			List<ObjectId> userIds=new ArrayList<ObjectId>();
			//从班级信息下获取学生id和教师id
			if(clsLis!=null&&clsLis.size()>0){
				for(ClassInfoDTO classInfo: clsLis){
					if(classInfo.getTeacherIds()!=null){
						userIds.addAll(classInfo.getTeacherIds());
					}
				}
			}
			List<UserEntry> userList=userService.getTeacherEntryBySchoolId(schoolId,teaName,new BasicDBObject("nm",1));
			for(UserEntry entry:userList){
				if(userIds.contains(entry.getID())){
					teaIds.add(entry.getID());
				}
			}
		}else if(!"".equals(gradeId)){
			List<ClassInfoDTO> clsLis=classService.findClassByGradeId(gradeId);
			if(clsLis!=null&&clsLis.size()>0){
				for(ClassInfoDTO classInfo: clsLis){
					if(classInfo.getTeacherIds()!=null){
						teaIds.addAll(classInfo.getTeacherIds());
					}
				}
			}
		}else if(!"".equals(teaName)){
			List<UserEntry> userList=userService.getTeacherEntryBySchoolId(schoolId,teaName,new BasicDBObject("nm",1));
			for(UserEntry entry:userList){
				teaIds.add(entry.getID());
			}
		}else{
			List<UserEntry> userEntries=userService.getTeacherEntryBySchoolId(schoolId,new BasicDBObject("nm",1));
			for(UserEntry entry:userEntries){
				teaIds.add(entry.getID());
			}
		}

		Map<ObjectId, UserEntry> map=userService.getUserEntryMap(teaIds,new BasicDBObject("nm",1));

		Map<ObjectId,List<InteractLessonEntry>> iLEMap=interactLessonDao.getInteractLessonMapByTeacherIds(teaIds,new BasicDBObject("ui",1).append("ct",1));

		for (Map.Entry<ObjectId, UserEntry> entry : map.entrySet()) {
			Map<String,String> subMap=new HashMap<String, String>();
			subMap.put("teaName",entry.getValue().getUserName());
			subMap.put("teaId",entry.getKey().toString());
			subMap.put("lastTime","");
			List<InteractLessonEntry> list = iLEMap.get(entry.getKey());
			if(list!=null&&list.size()>0){
				long time=list.get(0).getCreateTime();
				subMap.put("lastTime",DateTimeUtils.getLongToStrTimeTwo(time));
			}
			relist.add(subMap);
		}
		sortList(relist);
		return relist;
	}


	/**
	 * 对list进行排序
	 * @param list
	 */
	private void sortList(List<Map<String,String>> list){
		Collections.sort(list, new Comparator<Map<String,String>>() {
			public int compare(Map<String,String> obj1 , Map<String,String> obj2) {
				int flag=obj2.get("lastTime").compareTo(obj1.get("lastTime"));
				if(flag==0){
					return obj2.get("teaName").compareTo(obj1.get("teaName"));
				}else{
					return flag;
				}
			}
		});
	}
}
