package com.fulaan.learningcenter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/class")
public class ClassController extends BaseController {

	private ClassService classService =new ClassService();
	private UserService userService =new UserService();
	
	
	/**
	 * 查找学生所在班级；需要权限检查
	 * @param studnetId
	 * @return
	 */
	@RequestMapping("/student/list")
	@ResponseBody
	public List<ClassInfoDTO> getStudentClasses(ObjectId studentId)
	{
		List<ClassInfoDTO> retList =new ArrayList<ClassInfoDTO>();
		List<ClassEntry> dboList= classService.getStudentClassList(studentId);
		for(ClassEntry e:dboList)
		{
			retList.add(new ClassInfoDTO(e));
		}
		return retList;
	}
	
	/**
	 * 得到学生班级
	 * @return
	 */
	@UserRoles({UserRole.STUDENT,UserRole.PARENT})
	@RequestMapping("/student/info")
	@ResponseBody
	public ClassInfoDTO getStudentClasse()
	{
		ObjectId stuId =getUserId();
		if(UserRole.isParent(getSessionValue().getUserRole()))
		{
			UserEntry e=userService.searchUserId(getUserId());
			stuId=e.getConnectIds().get(0);
		}
		ClassEntry e=classService.getClassEntryByStuId(stuId,Constant.FIELDS);
		return new ClassInfoDTO(e);
	}
	
	
	
	/**
	 * 查看一个人是不是班级成员，用于找回密码
	 * @param userName 找回密码人员名称
	 * @param checkName 要验证的人员名称
	 * @param type 0 同学 1老师
	 * @return 
	 */

	@SessionNeedless
	@RequestMapping("/checkName")
	@ResponseBody
	public RespObj checkUserNameIsClassMember(String userName,String studentName,String teacherName)
	{
		RespObj obj =new RespObj(Constant.FAILD_CODE,"回答错误");
		
		if(StringUtils.isBlank(studentName) || StringUtils.isBlank(userName) || StringUtils.isBlank(teacherName) || studentName.equalsIgnoreCase(teacherName) || userName.equalsIgnoreCase(studentName) || userName.equalsIgnoreCase(teacherName) )
		{
			//obj.setMessage("参数错误");
			return obj;
		}
		
		if(studentName.length()<2 || studentName.length()<2)
		{
			//obj.setMessage("参数错误");
			return obj;
		}
		UserEntry e=userService.searchUserByUserName(userName);
		if(null==e)
		{
			//obj.setMessage("没有找到用户");
			return obj;
		}
		if(!UserRole.isStudent(e.getRole()) && !UserRole.isParent(e.getRole()))
		{
			//obj.setMessage("权限错误");
			return obj;
		}
		
		Map<ObjectId, UserEntry> maps=classService.getClassMembersByStudentId(e.getID());
		
		boolean isStudent=false;
		boolean isTeacher=false;
		for(Map.Entry<ObjectId, UserEntry> entry:maps.entrySet())
		{
			if(!isStudent)
			{
				if(UserRole.isStudent(entry.getValue().getRole()))
				{
					if(entry.getValue().getUserName().replaceAll("\\d+","").equalsIgnoreCase(studentName.replaceAll("\\d+","")))
					{
						isStudent=true;
					}
				}
			}
			if(!isTeacher)
			{
				if(UserRole.isTeacher(entry.getValue().getRole()))
				{
					if(entry.getValue().getUserName().replaceAll("\\d+","").equalsIgnoreCase(teacherName.replaceAll("\\d+","")))
					{
						isTeacher=true;
					}
				}
			}
		}
		return (isStudent && isTeacher)?RespObj.SUCCESS: obj;
	}
	
}
