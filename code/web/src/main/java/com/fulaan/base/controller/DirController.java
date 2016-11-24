package com.fulaan.base.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fulaan.learningcenter.service.LessonService;

import com.fulaan.user.service.UserService;

import com.pojo.lesson.LessonEntry;

import com.pojo.school.ClassInfoDTO;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.service.DirService;
import com.fulaan.learningcenter.service.LeagueService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.mongodb.BasicDBObject;
import com.pojo.lesson.DirDTO;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.school.LeagueEnrty;
import com.pojo.school.TeacherClassSubjectDTO;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;

/**
 * 
 * 目录controller
 * @author fourer
 *
 */
@Controller
@RequestMapping("/dir")
public class DirController extends BaseController {

	private static final Logger logger =Logger.getLogger(DirController.class);
	
	private DirService dirService =new DirService();
	private TeacherClassSubjectService teacherClassLessonService =new TeacherClassSubjectService();
    private LeagueService leagueService =new LeagueService();
	private LessonService lessonService = new LessonService();
   
	
    /**
     * 
     * @param userId
     * @param type 2 备课空间课程 3班级课程 4校本资源课程 5联盟资源课程
     * @return
     */
	@RequestMapping("/find")
	@ResponseBody
	public List<DirDTO> getDirDTOs(@ObjectIdType(isRequire=false) ObjectId userId, DirType type)
	{
		List<DirDTO> retList = new ArrayList<DirDTO>();
        List<ObjectId> ownerList = new ArrayList<ObjectId>();
        ObjectId ui=null==userId?getUserId():userId;

        ObjectId schoolId =new ObjectId(getSessionValue().getSchoolId());

        List<TeacherClassSubjectDTO> tlist = null;

		switch (type) {
			case BACK_UP:
				ownerList.add(ui);
				break;
			case CLASS_LESSON: {
				tlist = teacherClassLessonService.getTeacherClassSubjectDTOList(ui, null);
				for (TeacherClassSubjectDTO dto : tlist) {
					ownerList.add(new ObjectId(dto.getId()));
				}
				break;
			}
			case SCHOOL_RESOURCE:
				ownerList.add(schoolId);
				break;
			case UNION_RESOURCE:
					List<LeagueEnrty> list = leagueService.getLeagueEnrtys(schoolId, new BasicDBObject(Constant.ID,1));
					ownerList.addAll(MongoUtils.getFieldObjectIDs(list, Constant.ID));
				break;
		default:
			ownerList.add(schoolId);
			break;
		}
		
		List<DirEntry> dirList =dirService.getDirEntryList(ownerList, Constant.FIELDS,type.getType());

//        //如果是班级课程,老师的情况,
//        if(type.getType()== DirType.CLASS_LESSON.getType()){
//            for(TeacherClassSubjectDTO teacherClassSubjectDTO:tlist){
//                DirDTO dirDTO = new DirDTO();
//                dirDTO.setName(teacherClassSubjectDTO.getClassInfo().getValue()+""+teacherClassSubjectDTO.getSubjectInfo().getValue());
//                dirDTO.setId(teacherClassSubjectDTO.getId());
//                retList.add(dirDTO);
//            }
//        }


		for(DirEntry dire:dirList)
	    {
            DirDTO dirDTO = new DirDTO(dire);
//            if(dirDTO.getParentId()==null&&type.getType()== DirType.CLASS_LESSON.getType()){ //如果是班级课程
//                dirDTO.setParentId(dire.getOwerId().toString());
//            }
	    	retList.add(dirDTO);
	    }
		return retList;
	}

    @RequestMapping("/student/find")
    @ResponseBody
    public List<DirDTO> getStudentDirDTOs(DirType type) {

        if(type.getType() != DirType.CLASS_LESSON.getType()){
            return null;
        }
        List<DirDTO> retList = new ArrayList<DirDTO>();
        //找到对应班级的文件夹
        ObjectId ui=getUserId();
        //先得到学生所在的班级
//        ClassService classService = new ClassService();
//        ClassEntry classEntry = classService.getClassEntryByStuId(ui, Constant.FIELDS);
        UserService userService = new UserService();

        List<ClassInfoDTO> classInfoDTOList =  userService.getClassDTOList(ui,getSessionValue().getUserRole());
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        for(ClassInfoDTO classInfoDTO:classInfoDTOList){
            classIdList.add(new ObjectId(classInfoDTO.getId()));
        }
        //再得到该班级关联的所有teacherclasssubjects
        List<TeacherClassSubjectDTO> teacherClassLessonInfoList =
                teacherClassLessonService.findTeacherClassSubjectByClassIds(classIdList);


        List<ObjectId> ownerList = new ArrayList<ObjectId>();
        for (TeacherClassSubjectDTO dto : teacherClassLessonInfoList) {
            ownerList.add(new ObjectId(dto.getId()));
        }


        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, Constant.FIELDS,type.getType());


        for(DirEntry dire:dirList)
        {
            DirDTO dirDTO = new DirDTO(dire);

            retList.add(dirDTO);
        }
        return retList;

    }

	
	
	/**
	 * 查找某人的目录
	 * @param ui
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/rename")
	@ResponseBody
	public RespObj dirRename(@ObjectIdType ObjectId dirId,String name) throws IllegalParamException
	{
//		if(!dirService.isHavePermission(dirId, getUserId(), new ObjectId(getSessionValue().getSchoolId())))
//		{
//			logger.info("permission is lost");
//			RespObj obj =new RespObj(Constant.FAILD_CODE, "没有权限");
//			return obj;
//		}
		dirService.update(dirId, "dn", name);;
		return RespObj.SUCCESS;
	}
	
	

	/**
	 * 更改顺序
	 * @param dirId
	 * @param order
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/reorder")
	@ResponseBody
	public RespObj updateOrder(@ObjectIdType ObjectId dirId,int  order) throws IllegalParamException
	{
		
		if(!dirService.isHavePermission(dirId, getUserId(), new ObjectId(getSessionValue().getSchoolId())))
		{
			logger.info("permission is lost");
			RespObj obj =new RespObj(Constant.FAILD_CODE, "没有权限");
			return obj;
		}
		dirService.update(dirId, "so", order);
		return RespObj.SUCCESS;
	}
	
	/**
	 * 文件夹移动
	 * @param dirIds 要移动的dir id,顺序按照dirIds先后
	 * @param parent 父级dir id
	 * @return
	 * @throws IllegalParamException
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/move")
	@ResponseBody
	 public RespObj moveDir(@RequestParam(value = "nodeIds[]") String[] dirIds,  String parent) throws IllegalParamException {
		    if(null==dirIds || dirIds.length==Constant.ZERO )
		    {
		    	return RespObj.FAILD;
		    }
		    
//		    if(!dirService.isHavePermission(parent, getUserId(), new ObjectId(getSessionValue().getSchoolId())))
//			{
//				logger.info("permission is lost");
//				RespObj obj =new RespObj(Constant.FAILD_CODE, "没有权限");
//				return obj;
//			}
		    
		    List<ObjectId> objIds =new ArrayList<ObjectId>();
		    for(String s:dirIds)
		    {
		    	if(!ObjectId.isValid(s))
		    		throw new IllegalParamException();
		    	objIds.add(new ObjectId(s));
		    }

		ObjectId parentId = null;
		if(!parent.equals("0")){
			parentId = new ObjectId(parent);
		}
		    
		    for(int i=0;i<objIds.size();i++)
		    {
		    	dirService.move(objIds.get(i), parentId, i+1);
		    }
		    
	        return RespObj.SUCCESS;
	    }
	
	

	/**
	 * 需要权限检查
	 * 新建文件夹
	 * @param dirId
	 * @param name
	 * @param type 1备课空间 2 班级课程 3校本资源
	 * @return
	 * @throws IllegalParamException 
	 */
	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/add")
	@ResponseBody
	public RespObj createDir( @ObjectIdType(isRequire=false) ObjectId parentdir,String name,@RequestParam(required = false)Integer type) throws IllegalParamException
	{
		if( (parentdir == null && type == null) || type != null && type!=Constant.ONE && type!=Constant.TWO && type!=Constant.THREE)
		{
			return RespObj.FAILD;
		}
		ObjectId ownerId=getUserId();
		DirEntry parent=null;
		if(null!=parentdir)
		{
//			if(!dirService.isHavePermission(parentdir, getUserId(), new ObjectId(getSessionValue().getSchoolId())))
//			{
//				RespObj obj =new RespObj(Constant.FAILD_CODE, "没有权限");
//				return obj;
//			}
		 	parent = dirService.getDirEntry(parentdir, null);
			type = parent.getType();
		}
		
		
		if(Constant.TWO==type) //班级课程
		{
			if(null==parent)
				throw new IllegalParamException();
			ownerId=parent.getOwerId();
		}
		if(Constant.THREE==type) //校本资源
		{
			ownerId=new ObjectId(getSessionValue().getSchoolId());
		}
		
		DirEntry e =new DirEntry(ownerId, name, parentdir, Constant.ZERO,DirType.getDirType(type));
		ObjectId id=dirService.addDirEntry(e);
		RespObj obj =new RespObj(Constant.SUCCESS_CODE,id.toString());
		return obj;
	}
    /**
     * 删除目录
     * @param dirId
     * @return
     * @throws IllegalParamException
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/remove")
    @ResponseBody
    public RespObj removedir(@ObjectIdType ObjectId dirId) throws IllegalParamException
    {
        
          DirEntry e =dirService.getDirEntry(dirId, null);//删除时不指定owner

//        boolean isHavePermission=dirService.isPermission(e, getUserId(), new ObjectId(getSessionValue().getSchoolId()));

//        if(!isHavePermission)
//        {
//            return RespObj.FAILD;
//        }

        Set<ObjectId> dIds=dirService.removeDir(e);
        //处理课程
        List<LessonEntry> list =lessonService.getLessonEntryList(dIds, new BasicDBObject("vis",1));
        Set<ObjectId> deleteVidesids =new HashSet<ObjectId>();
        for(LessonEntry lessonEntry:list)
        {
            deleteVidesids.addAll(lessonEntry.getVideoIds());
        }
        logger.info("delete lesson; dirs= "+dIds);
        lessonService.deleteByDirs(dIds);
       
        return RespObj.SUCCESS;
    }

	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/party/add")
	@ResponseBody
	public RespObj partyCreateDir(@ObjectIdType(isRequire=false) ObjectId parentdir, String name, Integer type) throws IllegalParamException {
		DirEntry parent=null;
		if(null!=parentdir) {
			parent = dirService.getDirEntry(parentdir, null);
			type = parent.getType();
		}
		ObjectId ownerId=new ObjectId(getSessionValue().getSchoolId());

		DirEntry e =new DirEntry(ownerId, name, parentdir, Constant.ZERO, DirType.getDirType(type));
		ObjectId id=dirService.addDirEntry(e);
		RespObj obj =new RespObj(Constant.SUCCESS_CODE,id.toString());
		return obj;
	}

	@UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/party/remove")
	@ResponseBody
	public RespObj partyRemovedir(@ObjectIdType ObjectId dirId) throws IllegalParamException
	{

		DirEntry e =dirService.getDirEntry(dirId, null);//删除时不指定owner

		Set<ObjectId> dIds=dirService.removeDir(e);//本目录及子目录
		//todo 处理资源
//		List<LessonEntry> list =lessonService.getLessonEntryList(dIds, new BasicDBObject("vis",1));
//		Set<ObjectId> deleteVidesids =new HashSet<ObjectId>();
//		for(LessonEntry lessonEntry:list) {
//			deleteVidesids.addAll(lessonEntry.getVideoIds());
//		}
//		logger.info("delete lesson; dirs= "+dIds);
//		lessonService.deleteByDirs(dIds);

		return RespObj.SUCCESS;
	}
	
}
