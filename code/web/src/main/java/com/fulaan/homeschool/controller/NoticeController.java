package com.fulaan.homeschool.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.groupdiscussion.service.GroupDiscussionService;
import com.fulaan.homeschool.service.NoticeService;
import com.fulaan.letter.service.LetterService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JPushUtils;
import com.fulaan.utils.KeyWordFilterUtil;
import com.fulaan.utils.QiniuFileUtils;
import com.mongodb.BasicDBObject;
import com.pojo.app.*;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.groups.GroupsEntry;
import com.pojo.groups.GroupsUser;
import com.pojo.letter.LetterEntry;
import com.pojo.notice.NoticeDTO;
import com.pojo.notice.NoticeEntry;
import com.pojo.notice.NoticeEntry.NoticeAllInfo;
import com.pojo.school.*;
import com.pojo.user.AvatarType;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.exceptions.UnLoginException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.HtmlUtils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;

/**
 * 通知contrller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

	private static final Logger logger =Logger.getLogger(NoticeController.class);
	
	private NoticeService noticeService =new NoticeService();
	private UserService userService =new UserService();
	private final JPushUtils pushUtils=new JPushUtils();
    private ExperienceService experienceService=new ExperienceService();
    private LetterService letterService =new LetterService();
    private ClassService classService =new ClassService();
    private SchoolService schoolService =new SchoolService();
    private InterestClassService interestClassService =new InterestClassService();
    private TeacherClassSubjectService tcsService =new TeacherClassSubjectService();
    @Autowired
    private EaseMobService easeMobService;
    @Autowired
    private GroupDiscussionService groupDiscussionService;
    @Autowired
    private EducationBureauService educationBureauService;
	/**
	 * 
	 * @param model
	 * @return
	 * @throws ResultTooManyException
	 * @throws UnLoginException 
	 * @throws IllegalParamException 
	 * @throws PermissionUnallowedException
	 */
	@RequestMapping("/index")
	public String load(@RequestParam(required=false,defaultValue="1") Integer page,Map<String, Object> model) throws ResultTooManyException, UnLoginException, IllegalParamException 
	{
        int type=0;
		PageDTO<NoticeDTO> dto =getNotices((page-1)*Constant.TWELVE, Constant.TWELVE, type);
		model.put("dto", dto);
		model.put("pages", Math.ceil((Double.valueOf(dto.getCount()))/Constant.TWELVE));
		model.put("pageIndex", page);
		boolean is=UserRole.isStudentOrParent(getSessionValue().getUserRole());
		model.put("role", is?Constant.ZERO:Constant.ONE);
		return "notice/notice_list";
	}
	
	/**
	 * 未读通知数量
	 * @return
	 */
	@RequestMapping("/unread/count")
	@ResponseBody
	public RespObj getUnReadCount()
	{
		ObjectId childId=null;
		if(UserRole.isParent(getSessionValue().getUserRole()))
		{
			UserEntry ue=userService.searchUserId(getUserId());
			childId=ue.getConnectIds().get(0);
		}
		int count=	noticeService.getNoReadCount(getSessionValue().getUserRole(), getUserId(), new ObjectId(getSessionValue().getSchoolId()),childId);
		RespObj obj =new RespObj(Constant.SUCCESS_CODE, count);
		return obj;
	}
	
	
	/**
	 * 创建通知
	 * @param model
	 * @return
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/create")
	public String createNotice(Map<String, Object> model)
	{
		return "notice/notice_create";
	}
	/**
	 * 增加一个通知
	 * 需要权限检查
	 * 根据产品需求：参数有所改变，users表示班级ID
	 * @param title 
	 * @param all是否是全学校  -1不是；1全部老师 2全部学生 ；
	 * @param users 用户ID，用“,”相连
	 * @param content
	 * @param voiceFile
	 * @param docFile
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/add")
	public String addNotice( final String title,
			@RequestParam(defaultValue="") final String all,
			@RequestParam(defaultValue="-1") int isSyn,
			@RequestParam(defaultValue="") String roomids,
			String users,
			String content,
			Long beginTime,
			Long endTime,
			String voiceFile,
			String docNames,
			String docFile) throws IllegalParamException
	{
		content=HtmlUtils.delScriptTag(content);
		if(!ValidationUtils.isRequestNoticeName(title))
		{
			throw new IllegalParamException("title is error!!");
		}
		if(!ValidationUtils.isRequestNoticeContent(content))
		{
			throw new IllegalParamException("content is error!!");
		}
		
		if(StringUtils.isBlank(all) && StringUtils.isBlank(users) && StringUtils.isBlank(roomids))
		{
			throw new IllegalParamException("user is error!!");
		}
		if(isSyn==Constant.ONE)
		{
			if(endTime<=beginTime)
			{
				throw new IllegalParamException("time is error!!");
			}
		}
		
		List<IdNameValuePair> voiceList =new ArrayList<IdNameValuePair>();
		if(StringUtils.isNotBlank(voiceFile))
		{
			String[] voiceFileArr=voiceFile.split(",");
			for(int i=0;i<voiceFileArr.length;i++)
			{
				ObjectId id =getObjectIdByFilePath(voiceFileArr[i]);
				IdNameValuePair voicePair =new IdNameValuePair(id);
				voicePair.setValue(FilenameUtils.getName(voiceFileArr[i]));
				voiceList.add(voicePair);
			}
		}
		
		List<IdNameValuePair> docList =new ArrayList<IdNameValuePair>();
		if(StringUtils.isNotBlank(docFile))
		{
			String[] docArr=StringUtils.split(docFile,Constant.COMMA);
			String[] docNamesArr=StringUtils.split(docNames,"//|");
			
			if(docArr.length==docNamesArr.length)
			{
				for(int i=0;i<docArr.length;i++)
				{
					try
					{
						String str=docArr[i];
						ObjectId id =getObjectIdByFilePath(str);
						IdNameValuePair docPair =new IdNameValuePair(id);
						docPair.setValue(str);
						docPair.setName(docNamesArr[i]);
						docList.add(docPair);
					}catch(Exception ex)
					{
						logger.error("", ex);
					}
				}
			}
		}
		
		final Map<ObjectId, IdValuePair> userMap = new HashMap<ObjectId, IdValuePair>();
		if(StringUtils.isNotBlank(users)   ||  StringUtils.isNotBlank(roomids))
		{
			Set<ObjectId> idSet =new HashSet<ObjectId>();
			if(StringUtils.isNotBlank(users))
			{
					Set<ObjectId> classIds =new HashSet<ObjectId>(MongoUtils.convert(users));
					Map<ObjectId, ClassEntry> classMaps=classService.getClassEntryMap(classIds, Constant.FIELDS) ;
					
					classIds.removeAll(classMaps.keySet());
					
					List<InterestClassEntry> intList=null;
					
					
				    if(classIds.size()>0)
				    {
				    	intList= interestClassService.findInterestClassEntrysByIds(classIds,new BasicDBObject("tid",1).append("stl", 1));
				    }
					
				
					for(Map.Entry<ObjectId, ClassEntry> entry:classMaps.entrySet())
					{
						idSet.add(entry.getValue().getMaster());
						idSet.addAll(entry.getValue().getStudents());
						idSet.addAll(entry.getValue().getTeachers());
					}
					
					if(null!=intList && intList.size()>0)
					{
						for(InterestClassEntry intc:intList)
						{
							idSet.add(intc.getTeacherId());
							idSet.addAll(MongoUtils.getFieldObjectIDs(intc.getInterestClassStudents(), "sid"));
						}
					}
					
					idSet.addAll(MongoUtils.convert(users));
			}
			
			
			Map<ObjectId, UserEntry> userNameMap=userService.getUserEntryMap(idSet, new BasicDBObject("nm",1));
			
			UserEntry ue=null;
			for(ObjectId uid:idSet)
			{
				ue=userNameMap.get(uid);
				if(null!=ue)
				{
					IdValuePair clapair =new IdValuePair(uid, ue.getUserName());
					userMap.put(clapair.getId(), clapair);
				}
			}
			
			//添加群组支持
			if(StringUtils.isNotBlank(roomids))
			{
				List<String> charids =new ArrayList<String>();
				String[] roomArr=StringUtils.split(roomids, Constant.COMMA);
		    	List<GroupsEntry> list=groupDiscussionService.selGroupsEntryList(Arrays.asList(roomArr), new BasicDBObject("guli",1));
		    	if(null!=list && list.size()>0)
		    	{
		    		for(GroupsEntry ge:list)
		    		{
		    			for(GroupsUser gu:ge.getGroupsUserList())
		    			{
		    					charids.add(gu.getUserid());
		    			}
		    		}
		    	}
		    	
		    	List<UserEntry> uList=	userService.getUserEntrysByChatids(charids, new BasicDBObject("nm",1));
		    	
		    	for(UserEntry uee:uList)
		    	{
		    		if(!userMap.containsKey(uee.getID()))
		    		{
		    			IdValuePair clapair =new IdValuePair(uee.getID(), uee.getUserName());
		    			userMap.put(uee.getID(), clapair);
		    		}
		    	}
			}
		}
		
		
		ObjectId schoolId=new ObjectId(getSessionValue().getSchoolId());
		
		NoticeAllInfo info =null;
		if(StringUtils.isNotBlank(all))
		{
			 info =new NoticeAllInfo(schoolId, 
					all.indexOf("1")>=Constant.ZERO?Constant.ONE:Constant.ZERO,
					all.indexOf("2")>=Constant.ZERO?Constant.ONE:Constant.ZERO,
					all.indexOf("3")>=Constant.ZERO?Constant.ONE:Constant.ZERO
			);
		}
		
		List<IdValuePair> toUsers=new ArrayList<IdValuePair>(userMap.values());

		//添加校领导
		
		int count=0;
//		List<UserEntry> ueList=userService.getSchoolLeader(schoolId);
//		for(UserEntry ue:ueList)
//		{
//			if(ue.getIsRemove()==Constant.ZERO)
//			{
//			 IdValuePair idv =new IdValuePair(ue.getID(), ue.getUserName());
//			 toUsers.add(idv);
//			 count=count+1;
//			}
//		}
		
		
		
		if(null!=info)
		{
			Map<ObjectId,UserEntry> userMaps=userService.getUserEntryMapBySchoolid(schoolId, new BasicDBObject("nm",1).append("r", 1));
			
			for(Map.Entry<ObjectId,UserEntry> entry:userMaps.entrySet())
			{
				
				if(info.getIsAllTeacherAndStudent()==1)
				{
					if( !userMap.containsKey(entry.getKey()))
					{
						toUsers.add(new IdValuePair(entry.getValue().getID(), entry.getValue().getUserName()));
					}
				}
				else
				{
				
				  if(info.getIsAllStudent()==Constant.ONE)
				  {
					if(UserRole.isStudent(entry.getValue().getRole()) && !userMap.containsKey(entry.getKey()))
					{
						toUsers.add(new IdValuePair(entry.getValue().getID(), entry.getValue().getUserName()));
					}
				  }
				  
				  if(info.getIsAllTeacher()==Constant.ONE)
				  {
					if(UserRole.isTeacher(entry.getValue().getRole()) && !userMap.containsKey(entry.getKey()))
					{
						toUsers.add(new IdValuePair(entry.getValue().getID(), entry.getValue().getUserName()));
					}
				  }
				}
			}
		}
		
		
		NoticeEntry e =new NoticeEntry(getUserId(),info,toUsers , title, content, voiceList, docList,beginTime,endTime,0,isSyn,count);
		ObjectId id=noticeService.addNotice(e);

		try
		{
            ExpLogType notifyScore = ExpLogType.NOTIFY;
            experienceService.updateScore(getUserId().toString(), notifyScore, id.toString()) ;
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		logger.info(getUserId()+" created notice ;"+e);
		try
		{
		    pushUtils.pushNotice(e, getSessionValue().getUserName(), schoolId.toString());
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		return "redirect:/notice/index.do";
	}
	

	/**
	 * 教育局添加通知
	 * @param title
	 * @param schoolIds 学校的ID
	 * @param content
	 * @param beginTime
	 * @param endTime
	 * @param voiceFile
	 * @param docNames
	 * @param docFile
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles({UserRole.EDUCATION})
	@RequestMapping("/edu/add")
	public String addEduNotice( final String title,
			@RequestParam(defaultValue="") final String schoolIds,
			@RequestParam(defaultValue="") String isall,
			String content,
			Long beginTime,
			Long endTime,
			String voiceFile,
			String docNames,
			String docFile) throws IllegalParamException
	{
		content=HtmlUtils.delScriptTag(content);
		if(!ValidationUtils.isRequestNoticeName(title))
		{
			throw new IllegalParamException("title is error!!");
		}
		if(!ValidationUtils.isRequestNoticeContent(content))
		{
			throw new IllegalParamException("content is error!!");
		}
		
		
		List<IdNameValuePair> voiceList =new ArrayList<IdNameValuePair>();
		if(StringUtils.isNotBlank(voiceFile))
		{
			ObjectId id =getObjectIdByFilePath(voiceFile);
			IdNameValuePair voicePair =new IdNameValuePair(id);
			voicePair.setValue(FilenameUtils.getName(voiceFile));
			voiceList.add(voicePair);
		}
		
		List<IdNameValuePair> docList =new ArrayList<IdNameValuePair>();
		if(StringUtils.isNotBlank(docFile))
		{
			String[] docArr=StringUtils.split(docFile,Constant.COMMA);
			String[] docNamesArr=StringUtils.split(docNames,"//|");
			
			if(docArr.length==docNamesArr.length)
			{
				for(int i=0;i<docArr.length;i++)
				{
					try
					{
						String str=docArr[i];
						ObjectId id =getObjectIdByFilePath(str);
						IdNameValuePair docPair =new IdNameValuePair(id);
						docPair.setValue(str);
						docPair.setName(docNamesArr[i]);
						docList.add(docPair);
					}catch(Exception ex)
					{
						logger.error("", ex);
					}
				}
			}
		}
		
		final Map<ObjectId, IdValuePair> userMap = new HashMap<ObjectId, IdValuePair>();
		
		
		List<ObjectId> totalSchoolList=	MongoUtils.convert(schoolIds);
		
		if(String.valueOf(Constant.ONE).equals(isall))
		{
			EducationBureauEntry ebe=	educationBureauService.selEducationByEduUserId(getUserId());
			if(null!=ebe)
			{
				totalSchoolList.addAll(ebe.getSchoolIds());
			}
		}
		
		if(totalSchoolList.size()==0)
		{
			throw new IllegalParamException("school size =0");
		}
		
	
		try
		{
				List<UserEntry> list=	userService.getTeacherEntryBySchoolIds(totalSchoolList, null);
				
				for(UserEntry uee:list)
		    	{
		    		if(!userMap.containsKey(uee.getID()))
		    		{
		    			IdValuePair clapair =new IdValuePair(uee.getID(), uee.getUserName());
		    			userMap.put(uee.getID(), clapair);
		    		}
		    	}
		
		}catch(Exception ex)
		{
				logger.error("", ex);
		}
		
		
		if(userMap.isEmpty())
		{
			throw new IllegalParamException("user size =0");
		}
		
		
		ObjectId schoolId=new ObjectId(getSessionValue().getSchoolId());
		List<IdValuePair> toUsers=new ArrayList<IdValuePair>(userMap.values());
		NoticeEntry e =new NoticeEntry(getUserId(),null,toUsers , title, content, voiceList, docList,beginTime,endTime,0,0,0);
		ObjectId id=noticeService.addNotice(e);

		try
		{
            ExpLogType notifyScore = ExpLogType.NOTIFY;
            experienceService.updateScore(getUserId().toString(), notifyScore, id.toString()) ;
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		logger.info(getUserId()+" created notice ;"+e);
		try
		{
		    pushUtils.pushNotice(e, getSessionValue().getUserName(), schoolId.toString());
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		return "redirect:/notice/index.do";
	}
	
	
	
	/**
	 * 苏州团队调用接口
	 * @param userId
	 * @param toUsers
	 * @param content
	 * @return
	 * @throws Exception 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/simple/add")
	public RespObj addNotice(@ObjectIdType  ObjectId userId,String tus,String title,String content) throws Exception
	{
		logger.info("userId:"+userId);
		logger.info("tus:"+tus);
		logger.info("title:"+title);
		logger.info("content:"+content);
		
		UserEntry ue=userService.searchUserId(userId);
		if(null==ue)
		{
			throw new Exception("can not find user;"+userId.toString());
		}
		List<IdValuePair> toUsers=new ArrayList<IdValuePair>();
		List<ObjectId> uIds =MongoUtils.convert(tus);
		
		Map<ObjectId,UserEntry> uMap =userService.getUserEntryMap(uIds, new BasicDBObject("nm",1));
		for(Map.Entry<ObjectId,UserEntry> entry:uMap.entrySet())
		{
			 IdValuePair idv =new IdValuePair(entry.getValue().getID(), entry.getValue().getUserName());
			 toUsers.add(idv);
		}
		
		NoticeEntry e =new NoticeEntry(userId,null,toUsers , title, content, null, null,null,null,0,-1,toUsers.size());
		ObjectId id=noticeService.addNotice(e);

		try
		{
            ExpLogType notifyScore = ExpLogType.NOTIFY;
            experienceService.updateScore(getUserId().toString(), notifyScore, id.toString()) ;
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		logger.info(getUserId()+" created notice ;"+e);
		try
		{
		    pushUtils.pushNotice(e, getSessionValue().getUserName(), ue.getSchoolID().toString());
		}catch(Exception ex)
		{
			logger.error("", ex);
		}
		
		return RespObj.SUCCESS;
	}
	
	
	
	
	
	
	
	/**
	 * 删除通知
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public RespObj deleteNotice(@ObjectIdType ObjectId id)
	{
		
		RespObj obj=new RespObj(Constant.FAILD_CODE);
		try {
			NoticeEntry e =noticeService.getNoticeEntry(id);
			if(e==null)
			{
				obj.setMessage("参数错误");
				return obj;
			}
			int role=getSessionValue().getUserRole();
			if(!e.getTeacherId().equals(getUserId())&&!UserRole.isHeadmaster(role)&&!UserRole.isManager(role))
			{
				obj.setMessage("没有权限");
				return obj;
			}
			noticeService.deleteNotice(id);
			return RespObj.SUCCESS;
		} catch (Exception e) {
			obj.setMessage("参数错误");
		}
		return obj;
	}
	/**
	 * 通知置顶
	 * @param id
	 * @return
	 */
	@RequestMapping("/top")
	@ResponseBody
	public RespObj toTop(@ObjectIdType ObjectId id)
	{
		RespObj obj=new RespObj(Constant.FAILD_CODE);
		try {
			NoticeEntry e =noticeService.getNoticeEntry(id);
			if(e==null)
			{
				obj.setMessage("参数错误");
				return obj;
			}
			if(!e.getTeacherId().equals(getUserId()))
			{
				obj.setMessage("没有权限");
				return obj;
			}
			noticeService.updateIsTop(id,Constant.ONE-e.getIsTop());
			return RespObj.SUCCESS;
		} catch (Exception e) {
			obj.setMessage("参数错误");
		}
		return obj;
	
	}

	/**
	 * 
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 * @throws IllegalParamException 
	 * @throws PermissionUnallowedException
	 */
	@RequestMapping("/list")
	@ResponseBody
    public PageDTO<NoticeDTO> getNotices(int skip,int limit,@RequestParam(required=false,defaultValue="0") Integer type) throws ResultTooManyException, IllegalParamException
    {
		ObjectId childId=null;
		if(UserRole.isParent(getSessionValue().getUserRole()))
		{
			UserEntry ue=userService.searchUserId(getUserId());
			childId=ue.getConnectIds().get(0);
		}
		int count=noticeService.count(getSessionValue().getUserRole(), getUserId(), new ObjectId(getSessionValue().getSchoolId()),childId, type);
		List<NoticeDTO> list= noticeService.getNoticeDTOs(getSessionValue().getUserRole(), getUserId(), new ObjectId(getSessionValue().getSchoolId()),childId, type, skip, limit);
	    return new PageDTO<NoticeDTO>(count, list);
    }

	/**
	 * 通知详情
	 * @param id
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws IllegalParamException
	 */
	@RequestMapping("/detail")
    public String noticeDetail(@ObjectIdType ObjectId id,Map<String, Object> model) throws  IllegalParamException
    {
		
		NoticeEntry e =isHavePermission(id);
		List<IdValuePair> readIdValueList=e.getReadUsers();
		Set<ObjectId> alreadyUserIdSet =new HashSet<ObjectId>(MongoUtils.getFieldObjectIDs(readIdValueList, "id"));
		
		if(!alreadyUserIdSet.contains(getUserId()) && !e.getTeacherId().equals(getUserId()))
		{
			noticeService.readNotice(id, getUserId());
			if(UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
				ExpLogType searchNotice = ExpLogType.SEARCH_SCHOOL_NOTICE;
				experienceService.updateScore(getUserId().toString(), searchNotice, id.toString());
			}
		}
		
		NoticeDTO dto =new NoticeDTO(e);
		dto.setTitle(KeyWordFilterUtil.getReplaceStrTxtKeyWords(e.getName(), "*", 2));
		dto.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(e.getContent(), "*", 2));
		if(e.getTeacherId().equals(getUserId()))
		{
			dto.setIsSelfOper(Constant.ONE);
		}
		
		UserEntry ue=userService.searchUserId(e.getTeacherId());
		if(null!=ue)
		{
			dto.setTeacherName(ue.getUserName());
		}
		
		for(IdNameValuePairDTO idvpDto:dto.getVoiceFile())
		{
			idvpDto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, idvpDto.getValue().toString()));
		}
		for(IdNameValuePairDTO idvpDto:dto.getDocFile())
		{
			idvpDto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, idvpDto.getValue().toString()));
		}
		model.put("dto", dto);
        String url="notice/notice_detail";
        if(e.getType()==1){
            url="documentflow/docflow_detail";
        }
        
		return url;
    }
	/**
	 * 通知详情
	 * @param id
	 * @return
	 * @throws PermissionUnallowedException
	 * @throws IllegalParamException
	 */
	@RequestMapping("/mobile/detail")
	@ResponseBody
    public NoticeDTO noticeDetail(@ObjectIdType ObjectId id) throws  IllegalParamException
    {
		NoticeEntry e =isHavePermission(id);
		
		NoticeDTO dto =new NoticeDTO(e);
		UserEntry ue=userService.searchUserId(e.getTeacherId());
		
		List<IdValuePair> readIdValueList=e.getReadUsers();
		Set<ObjectId> alreadyUserIdSet =new HashSet<ObjectId>(MongoUtils.getFieldObjectIDs(readIdValueList, "id"));
		
		if(!alreadyUserIdSet.contains(getUserId()))
		{
			noticeService.readNotice(id, getUserId());
			if(UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
				ExpLogType searchNotice = ExpLogType.SEARCH_SCHOOL_NOTICE;
				experienceService.updateScore(getUserId().toString(), searchNotice, id.toString());
			}
		}
		
		if(null!=ue)
		{
			dto.setTeacherName(ue.getNickName());
		}
		
		for(IdNameValuePairDTO idvpDto:dto.getVoiceFile())
		{
			idvpDto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, idvpDto.getValue().toString()));
		}
		for(IdNameValuePairDTO idvpDto:dto.getDocFile())
		{
			idvpDto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, idvpDto.getValue().toString()));
		}
		return dto;
    }

	/**
	 * 查询相应用户信息
	 * @param type 4部门
	 * @param ist 是不是老师 1是  0不是 
	 * @return
	 * @throws ResultTooManyException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/users")
	@ResponseBody
	public List<UserGroupInfo<IdNameValuePairDTO>> getUserGroupInfos(Integer type,Integer ist) throws ResultTooManyException
	{
		List<UserGroupInfo<IdNameValuePairDTO>> retList =new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();
		//todo
		Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> retMap=null;
		if(Constant.ONE==ist)//老师
		{
			retMap= userService.getTeacherMap(new ObjectId(getSessionValue().getSchoolId()), type);
		}
		if(Constant.ZERO==ist)
		{
		   retMap= userService.getStudentOrParentMap(new ObjectId(getSessionValue().getSchoolId()), type);
		}
		if(Constant.FOUR==type)//部门
		{
		   retMap=  userService.getDepartmemtMembersMap(new ObjectId(getSessionValue().getSchoolId()));
		}
		
		for(Map.Entry<IdNameValuePairDTO,Set<IdNameValuePairDTO>> entryes:retMap.entrySet())
		{
			retList.add(new UserGroupInfo<IdNameValuePairDTO>(entryes.getKey(),new ArrayList<IdNameValuePairDTO>(entryes.getValue())));
		}
		return retList;
	}
	
	
	/**
	 * 按照学科分组查询用户
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/subject/users")
	@ResponseBody
	public List<UserGroupInfo<IdNameValuePairDTO>> subjectUsers() throws  IllegalParamException
	{
		ObjectId schoolId =new ObjectId(getSessionValue().getSchoolId());
		SchoolEntry se =schoolService.getSchoolEntry(schoolId, new BasicDBObject("subs",1));
		Map<ObjectId,UserGroupInfo<IdNameValuePairDTO>> userGroupMap=new HashMap<ObjectId, UserGroupInfo<IdNameValuePairDTO>>();
		
		for(Subject sub:se.getSubjects())
		{
			IdNameValuePairDTO dto =new IdNameValuePairDTO();
			dto.setId(sub.getSubjectId());
			dto.setValue(sub.getName());
			
			UserGroupInfo<IdNameValuePairDTO> info =new UserGroupInfo<IdNameValuePairDTO>(dto, new ArrayList<IdNameValuePairDTO>());
			userGroupMap.put(sub.getSubjectId(), info);
		}
		
		
		List<TeacherClassSubjectEntry> tcsList=tcsService.findTeacherClassSubjectBySubjectIds(userGroupMap.keySet(),new BasicDBObject("ti",1).append("sui", 1));
		//teacher->subject
		Map<ObjectId,ObjectId> teacherSubjectMap =new HashMap<ObjectId, ObjectId>();
		
		for(TeacherClassSubjectEntry tcs:tcsList)
		{
			teacherSubjectMap.put(tcs.getTeacherId(), tcs.getSubjectInfo().getId());
		}
		
		
		
		Map<ObjectId, UserEntry> userMap=userService.getUserEntryMap(teacherSubjectMap.keySet(), new BasicDBObject("nm",1));
		
		
		for(Map.Entry<ObjectId, UserEntry> userEntry:userMap.entrySet())
		{
			IdNameValuePairDTO dto =new IdNameValuePairDTO();
			dto.setId(userEntry.getKey());
			dto.setValue(userEntry.getValue().getUserName());
			
			
			ObjectId subjectId=teacherSubjectMap.get(userEntry.getKey());
			if(null!=subjectId)
			{
				UserGroupInfo<IdNameValuePairDTO> info =userGroupMap.get(subjectId);
				
				if(null!=info)
				{
					info.getList().add(dto);
				}
			}
		}
			
		return new ArrayList<UserGroupInfo<IdNameValuePairDTO>>(userGroupMap.values());
	}

	/**
	 * 查询学校班级
	 * @return
	 * @throws ResultTooManyException
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/classes")
	@ResponseBody
	public List<UserGroupInfo<IdNameValuePairDTO>> getUserGroupInfos() throws  IllegalParamException
	{
		List<UserGroupInfo<IdNameValuePairDTO>> retList =new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();

		ObjectId schoolId =new ObjectId(getSessionValue().getSchoolId());
		SchoolEntry se=schoolService.getSchoolEntry(schoolId,Constant.FIELDS);

		if(null==se)
		{
			throw new IllegalParamException("Cannot find school for user "+getUserId().toString());
		}
		Map<ObjectId,Grade> gradeMap =new HashMap<ObjectId, Grade>();

		for(Grade g:se.getGradeList())
		{
			gradeMap.put(g.getGradeId(), g);
		}
		
		Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> gradeClassMap =new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
		List<ClassEntry> cls=new ArrayList<ClassEntry>();
		List<InterestClassEntry> incLis=new ArrayList<InterestClassEntry>();
		int userRole=getSessionValue().getUserRole();
		if(!UserRole.isHeadmaster(userRole)&&!UserRole.isManager(userRole)){
			ObjectId userId=getUserId();
			cls = classService.findClassInfoByTeacherId(userId, new BasicDBObject("gid", 1).append("nm", 1));
			incLis=interestClassService.getInterestClassEntryByTeacherId(userId);
		}else {
			cls = classService.findClassInfoBySchoolId(schoolId, new BasicDBObject("gid", 1).append("nm", 1));
			//兴趣班
			incLis=interestClassService.getInterestClassEntryBySchoolId(schoolId);
		}

		Grade g=null;
		IdNameValuePairDTO gradeDTO=null;
		
		for(ClassEntry ce:cls)
		{
			try
			{
				g=gradeMap.get(ce.getGradeId());
				gradeDTO=new IdNameValuePairDTO();
				gradeDTO.setId(g.getGradeId());
				gradeDTO.setValue(g.getName());;
				
				if(!gradeClassMap.containsKey(gradeDTO))
				{
					gradeClassMap.put(gradeDTO, new HashSet<IdNameValuePairDTO>());
				}
				gradeClassMap.get(gradeDTO).add(new IdNameValuePairDTO(ce));
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		for(Map.Entry<IdNameValuePairDTO,Set<IdNameValuePairDTO>> entryes:gradeClassMap.entrySet())
		{
			retList.add(new UserGroupInfo<IdNameValuePairDTO>(entryes.getKey(),new ArrayList<IdNameValuePairDTO>(entryes.getValue())));
		}
		

		if(null!=incLis && !incLis.isEmpty())
		{
			List<IdNameValuePairDTO> list =new ArrayList<IdNameValuePairDTO>();
			for(InterestClassEntry inc:incLis)
			{
				list.add(new IdNameValuePairDTO(inc.getID(), null, inc.getClassName()));
			}
			retList.add(new UserGroupInfo<IdNameValuePairDTO>(new IdNameValuePairDTO(new ObjectId(),null,"兴趣班"),list));
		}
		
		return retList;
	}
	
	
	
	/**
	 * 查询学校班级成员
	 * 1老师 2学生 3家长
	 * @param type
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/classes/users")
	@ResponseBody
	public List<UserGroupInfo<IdNameValuePairDTO>> getClassUsers(@RequestParam(defaultValue="1") Integer type) throws  IllegalParamException
	{
		List<UserGroupInfo<IdNameValuePairDTO>> retList =new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();
		ObjectId schoolId =new ObjectId(getSessionValue().getSchoolId());
		
		Set<ObjectId> totalUserIdSet =new HashSet<ObjectId>();
		
		Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> classMap =new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
		
		BasicDBObject fields =new BasicDBObject("nm",1);
		if(type==1)
		{
			fields.append("teas", 1);
		}
		else
		{
			fields.append("stus", 1);
		}

		List<ClassEntry> cls=new ArrayList<ClassEntry>();
		List<InterestClassEntry> incLis=new ArrayList<InterestClassEntry>();
		int userRole=getSessionValue().getUserRole();
		if(!UserRole.isHeadmaster(userRole)&&!UserRole.isManager(userRole)){
			ObjectId userId=getUserId();
			cls = classService.findClassInfoByTeacherId(userId, fields);
			incLis=interestClassService.getInterestClassEntryByTeacherId(userId);
		}else {
			cls = classService.findClassInfoBySchoolId(schoolId, fields);
			//兴趣班
			incLis=interestClassService.getInterestClassEntryBySchoolId(schoolId);
		}

		if(null!=incLis && incLis.size()>0)
		{
			for(InterestClassEntry inc:incLis)
			{
				try
				{
					  ClassEntry ce=new ClassEntry(schoolId, null, inc.getClassName(), null, inc.getStudentCount());
					  ce.setID(inc.getID());
					  
					  if(type==1)
					  {
					  ce.setTeachers(Arrays.asList(inc.getTeacherId()));
					  }
					  else
					  {
					   ce.setStudents(MongoUtils.getFieldObjectIDs(inc.getInterestClassStudents(), "sid"));
					  }
					  cls.add(ce);
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
			}
		}
		
		for(ClassEntry ce:cls)
		{
			
			if(type==1)
			{
				totalUserIdSet.addAll(ce.getTeachers());
			}
			else
			{
				totalUserIdSet.addAll(ce.getStudents());
			}
			
		}
		
		
		Map<ObjectId,UserEntry> userMap =userService.getUserEntryMap(totalUserIdSet, new BasicDBObject("nm",1).append("cid", 1));
		
		Map<ObjectId,List<ObjectId>> studentParentMap =new HashMap<ObjectId, List<ObjectId>>();
		
		Set<ObjectId> parentSetIds =new HashSet<ObjectId>();
		if(type==3)
		{
			for(Map.Entry<ObjectId,UserEntry> entry:userMap.entrySet())
			{
				if(null!=entry.getValue().getConnectIds())
				{
					if(entry.getValue().getConnectIds().size()>0)
					{
					  studentParentMap.put(entry.getValue().getID(), entry.getValue().getConnectIds());
					  parentSetIds.addAll(entry.getValue().getConnectIds());
					}
				}
			}
		}
		
		Map<ObjectId,UserEntry> parentMap =userService.getUserEntryMap(parentSetIds, new BasicDBObject("nm",1).append("cid", 1));
		
		
		
		UserEntry parentEntry=null;
		
		IdNameValuePairDTO classDTO=null;
		
		for(ClassEntry ce:cls)
		{
			try
			{
				
				classDTO=new IdNameValuePairDTO();
				classDTO.setId(ce.getID().toString());
				classDTO.setValue(ce.getName());;
				
				if(!classMap.containsKey(classDTO))
				{
				   Set<IdNameValuePairDTO> userSet=	new HashSet<IdNameValuePairDTO>();
				   
				   for(ObjectId id:ce.getStudents())
				   {
					   UserEntry e=userMap.get(id);
					   if(null!=e)
					   {
						   if(type==2)
						   {
						     userSet.add(new IdNameValuePairDTO(e));
						   }
						   if(type==3)
						   {
							   if(studentParentMap.containsKey(e.getID()))
							   {
								   for(ObjectId parentId:studentParentMap.get(e.getID()))
								   {
									   parentEntry=parentMap.get(parentId);
									   if(null!=parentEntry)
									   {
										   userSet.add(new IdNameValuePairDTO(parentEntry.getID().toString(), "", parentEntry.getUserName()));
									   }
								   }
							   }
						   }
					   }
				   }
				   
				   
				   if(type==1)
				   {
					   for(ObjectId id:ce.getTeachers())
					   {
						   UserEntry e=userMap.get(id);
						   if(null!=e)
						   {
							  
							   userSet.add(new IdNameValuePairDTO(e));
						   }
					   }
				   }
				    classMap.put(classDTO, userSet);
				}
				//classMap.get(classDTO).add(new IdNameValuePairDTO(ce));
			}catch(Exception ex)
			{
				logger.error("", ex);
			}
		}
		for(Map.Entry<IdNameValuePairDTO,Set<IdNameValuePairDTO>> entryes:classMap.entrySet())
		{
			retList.add(new UserGroupInfo<IdNameValuePairDTO>(entryes.getKey(),new ArrayList<IdNameValuePairDTO>(entryes.getValue())));
		}
		
		return retList;
	}
	
	
	/**
	 * 文件下载
	 * @param id
	 * @param docId
	 * @return
	 * @throws IllegalParamException 
	 * @throws PermissionUnallowedException 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	@RequestMapping("/doc/down")
	public String downDoc(@ObjectIdType ObjectId id,@ObjectIdType ObjectId docId,HttpServletRequest req,HttpServletResponse response) throws IllegalParamException, UnsupportedEncodingException, FileNotFoundException
	{
		NoticeEntry e=isHavePermission(id);
		IdNameValuePair pair=null;
		for(IdNameValuePair p:e.getVoiceFile())
		{
			if(p.getId().equals(docId))
			{
				pair=p;
			}
		}
		for(IdNameValuePair p:e.getDocFile())
		{
			if(p.getId().equals(docId))
			{
				pair=p;
			}
		}
		if(pair==null)
			throw new IllegalParamException();
		
		
		response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
		response.setHeader( "Content-Disposition", "attachment;filename=" + new String( pair.getName().getBytes("utf-8"), "ISO8859-1" ) );

		InputStream inputStream=null;
		if(pair.getValue().toString().indexOf("upload")>=0)
		{
			inputStream=new FileInputStream(new File(req.getServletContext().getRealPath(pair.getValue().toString())));
		}
		else
		{
			String qiniuPath=QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, pair.getValue().toString());
			inputStream = QiniuFileUtils.downFileByUrl(qiniuPath);
		}
        try {
            
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.close();
            inputStream.close();
        }  catch (IOException ex) {
           logger.error("", ex);
        }
		return null;
	}
	
	
	/**
	 * 用户通知的查看情况
	 * @param id 通知ID
	 * @param tag 0没有查看 1 已经查看
	 * @param skip 
	 * @param limit
	 * @return
	 * @throws IllegalParamException
	 */
	@RequestMapping("/receive/infos")
	@ResponseBody
	public PageDTO<SimpleDTO> getNoticeReceiveInfos(
			@ObjectIdType ObjectId id,
			@RequestParam(required=false,defaultValue="0") Integer tag,
			@RequestParam(required=false,defaultValue="0") Integer skip, 
			@RequestParam(required=false,defaultValue="1") Integer limit,
			@RequestParam(required=false,defaultValue="0") Integer isSend
			)
			throws IllegalParamException {
		if(tag!=Constant.ZERO && tag!=Constant.ONE)
		{
			throw new IllegalParamException();
		}
		List<SimpleDTO> retList = new ArrayList<SimpleDTO>();
		NoticeEntry e = noticeService.getNoticeEntry(id);
		if (e == null) {
			throw new IllegalParamException();
		}
		int total=0;

		// 计算出的应该查询的用户ID
		List<ObjectId> calTotalList = new ArrayList<ObjectId>();
		Map<ObjectId, IdValuePair> alreadyMap = new HashMap<ObjectId, IdValuePair>();
		List<IdValuePair> alreadyList = e.getReadUsers();
		
		
		if (tag == Constant.ONE) // 已经查看的用户
		{
			calTotalList = MongoUtils.getFieldObjectIDs(alreadyList, "id");
			for (IdValuePair idp : alreadyList) {
				alreadyMap.put(idp.getId(), idp);
			}
			total=alreadyList.size();
		} else {
			// 所有接受者ID
			Set<ObjectId> totalIdset = new HashSet<ObjectId>();
			List<IdValuePair> userList = e.getUsers();
			totalIdset.addAll(MongoUtils.getFieldObjectIDs(userList, "id"));

			NoticeAllInfo allSchool = e.getAllSchool();
			if (null != allSchool) {
				Map<ObjectId, UserEntry> userMap = userService
						.getUserEntryMapBySchoolid(new ObjectId(
								getSessionValue().getSchoolId()),
								new BasicDBObject("r", 1));
				int role = 0;
				if (allSchool.getIsAllTeacherAndStudent() == Constant.ONE) {
					role = 3;
				} else {
					if (allSchool.getIsAllStudent() == Constant.ONE) {
						role = UserRole.STUDENT.getRole();
					}
					if (allSchool.getIsAllTeacher() == Constant.ONE) {
						role = UserRole.TEACHER.getRole();
					}
				}

				if (role != 0) {
					for (Map.Entry<ObjectId, UserEntry> entry : userMap
							.entrySet()) {
						if (role != 3) {
							if ((entry.getValue().getRole() & role) == role) {
								totalIdset.add(entry.getKey());
							}
						} else {
							if (UserRole.isTeacher(entry.getValue().getRole())
									|| UserRole.isStudent(entry.getValue()
											.getRole())) {
								totalIdset.add(entry.getKey());
							}
						}
					}
				}
			}
			totalIdset.removeAll(MongoUtils
					.getFieldObjectIDs(alreadyList, "id"));
			calTotalList = new ArrayList<ObjectId>(totalIdset);
			total=calTotalList.size();
			
			if(Constant.ONE==isSend)
			{
				//发送信件
				try
				{
				 LetterEntry le=new LetterEntry(e.getTeacherId(), "您有一条重要的通知，请尽快查看!", calTotalList);
				 letterService.sendLetter(le);
				 
				 Map<ObjectId,UserEntry> users =userService.getUserEntryMap(totalIdset, new BasicDBObject("chatid",1));
				 List<String> toChatIds =new ArrayList<String>();
				 for(Map.Entry<ObjectId,UserEntry> entry:users.entrySet())
				 {
					 toChatIds.add(entry.getValue().getChatId());
				 }
				 easeMobService.sendMessage(getSessionValue().getChatid().toString(), toChatIds, le.getContent(),getSessionValue().getUserName(),getSessionValue().getMaxAvatar());
				}catch(Exception ex)
				{
					logger.error("", ex);
				}
				return new PageDTO<SimpleDTO>();
			}
		}
		if (calTotalList.size() > skip)
		{
			int endIndex=skip + limit;
			if(endIndex>=calTotalList.size())
				endIndex=calTotalList.size();
			calTotalList = calTotalList.subList(skip, endIndex);
	
			Map<ObjectId, UserEntry> receiveMap = userService.getUserEntryMap(
					calTotalList, new BasicDBObject("nm", 1).append("avt", 1));
	
			SimpleDTO dto = null;
			UserEntry ue = null;
			IdValuePair ipv = null;
			for (Map.Entry<ObjectId, UserEntry> entry : receiveMap.entrySet()) {
				try {
					dto = new SimpleDTO();
					ue = entry.getValue();
					ipv = alreadyMap.get(entry.getKey());
					dto.setId(ue.getID().toString());
					dto.setValue(AvatarUtils.getAvatar(ue.getAvatar(),
							AvatarType.MIDDLE_AVATAR.getType()));
					dto.setValue1(ue.getUserName());
					if (null != ipv) {
						dto.setValue2(ipv.getValue());
					}
					retList.add(dto);
				} catch (Exception ex) {
					logger.error("", ex);
				}
			}
		}
		return new PageDTO<SimpleDTO>(total, retList);
	}
	
	/**
	 * 是否有权限查看;么有的话报错,有的话返回该通知
	 * @param hwid 作业ID
	 * @return
	 * @throws PermissionUnallowedException 
	 * @throws IllegalParamException 
	 */
	private NoticeEntry isHavePermission(ObjectId id) throws  IllegalParamException
	{
		NoticeEntry e =noticeService.getNoticeEntry(id);
		
		if(e==null)
		{
			throw new IllegalParamException();
		}
		if(e.getTeacherId().equals(getUserId()))
		{
			return e;
		}
		List<IdValuePair> users =e.getUsers();
		
		List<ObjectId> userids =MongoUtils.getFieldObjectIDs(users, Constant.ID);
		
		Set<ObjectId> idSet =new HashSet<ObjectId>(userids);
		if(idSet.contains(getUserId()))
		{
			return e;
		}
		
		NoticeAllInfo allSchool =e.getAllSchool();
		
		//老师
		boolean isTeachaer=UserRole.isTeacher(getSessionValue().getUserRole());
		if(isTeachaer)
		{
			if(null!=allSchool && allSchool.getSchoolId().toString().equals(getSessionValue().getSchoolId()) )
			{
				
				if(allSchool.getIsAllTeacher()==Constant.ONE || allSchool.getIsAllTeacherAndStudent()==Constant.ONE)
				{
					return e;
				}
			}
		}
		//学生
		boolean isStudent=UserRole.isStudent(getSessionValue().getUserRole());
		
		if(isStudent)
		{
			if(null!=allSchool && allSchool.getSchoolId().toString().equals(getSessionValue().getSchoolId()))
			{
				if(allSchool.getIsAllStudent()==Constant.ONE || allSchool.getIsAllTeacherAndStudent()==Constant.ONE)
				{
					return e;
				}
			}
		}
		//todo
		//throw new PermissionUnallowedException();
		return e;
	}
	
	/**
	 * 55710b1a63e79c8c2e423613.txt
	 * @param path
	 * @return
	 * @throws IllegalParamException
	 */
	private ObjectId getObjectIdByFilePath(String path) throws IllegalParamException
	{
		String name =FilenameUtils.getName(path);
		int index=name.indexOf(Constant.POINT);
		String objectIdStr=name.substring(0, index);
		if(!ObjectId.isValid(objectIdStr))
			throw new IllegalParamException("objectid error:"+objectIdStr);
		return new ObjectId(objectIdStr);
	}

    /**
     * 公文流转
     * @param model
     * @return
     * @throws ResultTooManyException
     * @throws UnLoginException
     * @throws IllegalParamException 
     * @throws PermissionUnallowedException
     */
    @RequestMapping("/docflow")
    public String docFlow(@RequestParam(required=false,defaultValue="1") Integer page,Map<String, Object> model) throws ResultTooManyException, UnLoginException, IllegalParamException
    {
        int type=1;
        PageDTO<NoticeDTO> dto =getNotices((page - 1) * Constant.TWELVE, Constant.TWELVE, type);
        model.put("dto", dto);
        model.put("pages", Math.ceil((Double.valueOf(dto.getCount()))/Constant.TWELVE));
        model.put("pageIndex", page);
        //boolean is=UserRole.isStudentOrParent(getSessionValue().getUserRole());
        //model.put("role", is?Constant.ZERO:Constant.ONE);
        return "documentflow/docflow_list";
    }

    /**
     * 创建公文流转
     * @param model
     * @return
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/createDocflow")
    public String createDocFlow(Map<String, Object> model)
    {
        return "documentflow/docflow_create";
    }
    //""
    /**
     * 需要权限检查
     * @param title
     * @param all 是否是全学校  -1不是；1全部老师
     * @param users 用户ID，用“,”相连
     * @param content
     * @param voiceFile
     * @param docFile
     * @return
     * @throws IllegalParamException
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/newdocflow")
    public String newDocFlow( final String title,
                             @RequestParam(defaultValue="") final String all,
                             @RequestParam(defaultValue="-1") int isSyn,
                             String users,
                             String content,
                             Long beginTime,
                             Long endTime,
                             String voiceFile,
                             String docNames,
                             String docFile) throws IllegalParamException
    {
        content=HtmlUtils.delScriptTag(content);
        if(!ValidationUtils.isRequestNoticeName(title))
        {
            throw new IllegalParamException("title is error!!");
        }
        if(!ValidationUtils.isRequestNoticeContent(content))
        {
            throw new IllegalParamException("content is error!!");
        }

        if(StringUtils.isBlank(all) && StringUtils.isBlank(users))
        {
            throw new IllegalParamException("user is error!!");
        }
        if(isSyn==Constant.ONE)
        {
            if(endTime<=beginTime)
            {
                throw new IllegalParamException("time is error!!");
            }
        }

        List<IdNameValuePair> voiceList =new ArrayList<IdNameValuePair>();
        if(StringUtils.isNotBlank(voiceFile))
        {
            ObjectId id =getObjectIdByFilePath(voiceFile);
            IdNameValuePair voicePair =new IdNameValuePair(id);
            voicePair.setValue(FilenameUtils.getName(voiceFile));
            voiceList.add(voicePair);
        }

        List<IdNameValuePair> docList =new ArrayList<IdNameValuePair>();
        if(StringUtils.isNotBlank(docFile))
        {
            String[] docArr=StringUtils.split(docFile,Constant.COMMA);
            String[] docNamesArr=StringUtils.split(docNames,"//|");

            if(docArr.length==docNamesArr.length)
            {
                for(int i=0;i<docArr.length;i++)
                {
                    try
                    {
                        String str=docArr[i];
                        ObjectId id =getObjectIdByFilePath(str);
                        IdNameValuePair docPair =new IdNameValuePair(id);
                        docPair.setValue(str);
                        docPair.setName(docNamesArr[i]);
                        docList.add(docPair);
                    }catch(Exception ex)
                    {
                        logger.error("", ex);
                    }
                }
            }
        }

        final Map<ObjectId, IdValuePair> userMap = new HashMap<ObjectId, IdValuePair>();
        //用于通知推送
        final List<String> tags =new ArrayList<String>();


        if(StringUtils.isNotBlank(users))
        {
            String[] userArr=StringUtils.split(users, Constant.COMMA);
            for(String user:userArr)
            {
                if(null!=user && ObjectId.isValid(user))
                {
                    IdValuePair clapair =new IdValuePair(new ObjectId(user), Constant.EMPTY);
                    userMap.put(clapair.getId(), clapair);
                    tags.add(user);
                }
            }
        }

        ObjectId schoolId=new ObjectId(getSessionValue().getSchoolId());

        NoticeAllInfo info =null;
        if(StringUtils.isNotBlank(all))
        {
            info =new NoticeAllInfo(schoolId,
                    all.indexOf("1")>=Constant.ZERO?Constant.ONE:Constant.ZERO,
                    all.indexOf("2")>=Constant.ZERO?Constant.ONE:Constant.ZERO,
                    all.indexOf("3")>=Constant.ZERO?Constant.ONE:Constant.ZERO
            );
            tags.add(schoolId.toString());
            if(all.indexOf("1")>=Constant.ZERO)
            {
                tags.add(String.valueOf(UserRole.TEACHER.getRole()));
            }
            if(all.indexOf("2")>=Constant.ZERO)
            {
                tags.add(String.valueOf(UserRole.STUDENT.getRole()));
            }
            if(all.indexOf("3")>=Constant.ZERO)
            {
                tags.add(String.valueOf(UserRole.TEACHER.getRole()));
                tags.add(String.valueOf(UserRole.STUDENT.getRole()));
            }

        }
        NoticeEntry e =new NoticeEntry(getUserId(),info,new ArrayList<IdValuePair>(userMap.values()) , title, content, voiceList, docList,beginTime,endTime,1,isSyn);
        ObjectId id=noticeService.addNotice(e);

        try
        {
            ExpLogType docFlow =  ExpLogType.DOC_FLOW;
            experienceService.updateScore(getUserId().toString(), docFlow, id.toString()) ;
        }catch(Exception ex)
        {
            logger.error("", ex);
        }

        logger.info(getUserId()+" created docflow ;"+e);
        logger.info(getUserId()+" new docflow id ="+id.toString());
        return "redirect:/notice/docflow.do";
    }
}
