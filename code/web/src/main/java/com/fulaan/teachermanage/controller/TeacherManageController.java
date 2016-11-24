package com.fulaan.teachermanage.controller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.teachermanage.dto.CourseProjectDTO;
import com.fulaan.teachermanage.dto.ResumeDTO;
import com.fulaan.teachermanage.dto.TeacherInfoDTO;
import com.fulaan.teachermanage.service.TeacherManageService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.school.SchoolDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/2/29.
 */
@Controller
@RequestMapping("/manage")
public class TeacherManageController extends BaseController {


    @Autowired
    private TeacherManageService teacherManageService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private UserService userService;

    @RequestMapping("/teachermanage")
    public String teachermanage(Model model) {
        List<GradeView> gradeViewList = schoolService.findGradeList(getSessionValue().getSchoolId());
        model.addAttribute("gradelist",gradeViewList);
        return "teachermanage/furtheredu";
    }

    /**
     * 添加教师课程项目
     * @param courseProjectDTO
     * @return
     */
    @RequestMapping("/addProject")
    public @ResponseBody
    Map<String ,Object> addProject(CourseProjectDTO courseProjectDTO) {
        Map<String,Object> model = new HashMap<String,Object>();
        if (StringUtils.isEmpty(courseProjectDTO.getContent())||StringUtils.isEmpty(courseProjectDTO.getCourse())) {
            model.put("flag",true);
            return model;
        }
        teacherManageService.addProjectEntry(courseProjectDTO.getCourseProject(new ObjectId(getSessionValue().getSchoolId())));
        model.put("flag", false);
        return model;
    }

    /**
     * 查询教师课程项目
     * @return
     */
    @RequestMapping("/getProjectList")
    public @ResponseBody Map<String,Object> getProjectList() {
        Map<String,Object> model = new HashMap<String,Object>();
        List<CourseProjectDTO> courseProjectDTOList = teacherManageService.getCourseProjectListBySchoolId(new ObjectId(getSessionValue().getSchoolId()));
        model.put("rows",courseProjectDTOList);
        return model;
    }

    /**
     * 删除教师课程项目
     * @param id
     * @return
     */
    @RequestMapping("/delProject")
    public @ResponseBody Map<String,Object> delProject(String id) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            teacherManageService.delProjectEntry(new ObjectId(id));
            model.put("flag",false);
        } catch (Exception e) {
            model.put("flag",true);
        }
        return model;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/getTeacherList")
    public @ResponseBody Map<String,Object> getTeacherList(String grade,int customize,String keyword,int type,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        Map<String,Object> model = new HashMap<String,Object>();
        List<TeacherInfoDTO> teacherInfoDTOList = teacherManageService.getTeacherList(grade, customize, keyword, type, getSessionValue().getSchoolId());
        List<TeacherInfoDTO> resultList=listImitatePage(teacherInfoDTOList,page,pageSize);
        model.put("rows",resultList);
        model.put("total", teacherInfoDTOList.size());
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
    private List<TeacherInfoDTO> listImitatePage(List<TeacherInfoDTO> list,int page,int pageSize) {
        int totalCount =list.size();
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        List<TeacherInfoDTO> subList=new ArrayList<TeacherInfoDTO>();
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

    /**
     * 初始化老师密码
     * @param userId
     * @return
     */
    @RequestMapping("/initpwd")
    @ResponseBody
    public Map<String,Object> initPwdOfTeacher(String userId,String userName,int rds) {
            Map<String,Object> model = new HashMap<String,Object>();
            teacherManageService.initPwdOfTeacher(userId,userName, getSessionValue().getSchoolId(),rds,model);
        return model;
    }

    /**
     * 修改权限
     * @param userid
     * @param name
     * @param jobnum
     * @param role
     * @return
     */
    @RequestMapping("/updateTeacherRole")
    @ResponseBody
    public Map<String,Object> updateTeacherRole(String userid,String name,String jobnum,String role,@RequestParam(required=false,defaultValue="0") Integer ismanage) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            //schoolService.updateTeacherCheck(userid, name, jobnum, role,model);
            schoolService.updateTeacherRole(userid, name, jobnum, role,ismanage);
            model.put("flg",true);
        }catch (Exception e) {
            model.put("flg",false);
        }
        return model;
    }

    /**
     * 添加课程文档
     * @return
     * @throws com.sys.exceptions.IllegalParamException
     * @throws java.io.IOException
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/addPhoto")
    @ResponseBody
    public RespObj addLessonDoc(String uid,  MultipartFile pic) throws IllegalParamException, IOException
    {
        String name= FilenameUtils.getBaseName(pic.getOriginalFilename());
        //上传文件
        RespObj obj=null;
        String docName=null;
        if(null!=pic)
        {
            docName =new ObjectId().toString()+ Constant.POINT+FilenameUtils.getExtension(pic.getOriginalFilename());
            obj= QiniuFileUtils.uploadFile(docName, pic.getInputStream(), QiniuFileUtils.TYPE_DOCUMENT);
            if(!obj.getCode().equals(Constant.SUCCESS_CODE))
            {
                return obj;
            }
        }
        return RespObj.SUCCESS;
    }

    /**
     * 添加个人信息
     * @param resumeDTO
     * @return
     */
    @RequestMapping("/addOrUpTerInfo")
    @ResponseBody
    public Map<String,Object> addOrUpdateTeacherInfo(ResumeDTO resumeDTO) {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            teacherManageService.addOrUpdateTeacherInfo(resumeDTO,getSessionValue().getSchoolId(),model);
            model.put("flg",true);
        }catch (Exception e) {
            model.put("flg",false);
        }
       return model;
    }

    @RequestMapping("/delTeacherInfo")
    @ResponseBody
    public RespObj delTeacherInfo(String teacherId) {
        teacherManageService.delTeacherInfo(new ObjectId(teacherId));
        return RespObj.SUCCESS;
    }

    /**
     * 教室成长档案
     * @param projectId
     * @param type
     * @return
     */
    @RequestMapping("projectDetailList")
    @ResponseBody
    public Map<String,Object> getProjectDetailList(String projectId,int type) {
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("rows", teacherManageService.getProjectDetailList(projectId, getSessionValue().getSchoolId(), type));
        return model;
    }

    /**
     * 获取老师简历
     * @param userid
     * @return
     */
    @RequestMapping("getTeacherDetail")
    public String getTeacherDetail(String userid, Map<String,Object> model){
        teacherManageService.getTeacherDetail(new ObjectId(userid), getSessionValue().getSchoolId(), model);
        return "teachermanage/furtheredutea";
    }
    /**
     * 获取老师简历
     * @param userid
     * @return
     */
    @RequestMapping("getTeacherDetailEdit")
    @ResponseBody
    public Map<String,Object> getTeacherDetailEdit(String userid){
        Map<String,Object> model = new HashMap<String,Object>();
        teacherManageService.getTeacherDetail(new ObjectId(userid), getSessionValue().getSchoolId(), model);
        return model;
    }

    /**
     * 新建老师时检查用户名
     * @param userName
     * @return
     */
    @RequestMapping("/checkUserName")
    @ResponseBody
    public Map<String, Object> checkUserName(@RequestParam String userName, int etype, String userId) {
        //List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByUserName(userName);
        userName = userName.toLowerCase();
        SchoolDTO schoolDTO = schoolService.findSchoolById(getSessionValue().getSchoolId());
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if(etype==1&&userId!=null&&!"".equals(userId)){
            List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByUserName(userName);
            if (userDetailInfoDTOList.size() == 1) {
                if (userId.equals(userDetailInfoDTOList.get(0).getId())){
                    returnMap.put("userName",userName);
                    returnMap.put("resultCode",0);
                }else{
                    Map<String,Object> result=checkAndGetName(userName);
                    returnMap.put("userName", result.get("userName"));
                    returnMap.put("resultCode", result.get("resultCode"));
                }
            }else{
                Map<String,Object> result=checkAndGetName(userName);
                returnMap.put("userName", result.get("userName"));
                returnMap.put("resultCode", result.get("resultCode"));
            }
        }else{
            Map<String,Object> result=checkAndGetName(userName);
            returnMap.put("userName", result.get("userName"));
            returnMap.put("resultCode", result.get("resultCode"));
        }
        returnMap.put("pwd", schoolDTO.getInitPwd());
        return returnMap;
    }

    /**
     * 检查核对教师用户名是否已经存在
     * @param userName
     * @return
     */
    public Map<String,Object> checkAndGetName(String userName)
    {
        Map<String,Object> map=new HashMap<String, Object>();
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByUserName(userName);
        if (userDetailInfoDTOList != null && !userDetailInfoDTOList.isEmpty()) {
            UserDetailInfoDTO userDetailInfoDTO = userDetailInfoDTOList.get(userDetailInfoDTOList.size() - 1);
            String name = userDetailInfoDTO.getUserName();
            boolean nameHasDigit = false;
            String nameCount="";
            String nameBase=name;
            for(int i=name.length()-1;i>0;i--)
            {
                char ch=name.charAt(i);
                if(Character.isDigit(ch))
                {
                    nameCount=ch+nameCount;
                    nameHasDigit = true;
                    nameBase=name.substring(0,i);
                }
                else
                {
                    break;
                }
            }
            if(nameHasDigit) {
                name = nameBase + checkIfValid(Integer.toString(Integer.parseInt(nameCount) + 1));
            }
            else{
                name = name + "1";
            }
            //return checkAndGetName(name);
            map.put("userName",checkAndGetName(name).get("userName"));
            map.put("resultCode",1);
            return map;
        }
        else
        {
            map.put("userName",userName);
            map.put("resultCode",0);
            return map;
        }
    }

    /**
     * 检查名字是否是合适的，即不可以是4，13，14，38，250等数字
     * @param name
     * @return
     */
    public String checkIfValid(String name)
    {
        if(name.equals("4")||name.equals("13")||name.equals("14")||name.equals("38")||name.equals("250"))
            return checkIfValid(Integer.toString(Integer.parseInt(name)+1));
        return name;
    }

}
