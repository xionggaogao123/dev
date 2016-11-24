package com.fulaan.educationbureau.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.educationbureau.dto.EducationBureauDTO;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.pojo.app.RegionDTO;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.school.SchoolDTO;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.*;

/**
 * Created by guojing on 2015/4/8.
 */
@Controller
@RequestMapping("/education")
public class EducationBureauController extends BaseController {
    private static final Logger logger =Logger.getLogger(EducationBureauController.class);

    @Autowired
    private EducationBureauService educationBureauService;


    @RequestMapping("/eduManageList")
    public String eduManageList(Map<String, Object> model) {
        int userRole=getSessionValue().getUserRole();
        String url="";
        if(UserRole.isSysManager(userRole)) {
        List<RegionDTO> regions=educationBureauService.getRegionEntryList(2, null);
            model.put("regions",regions);
            url="educationbureau/eduManageList";
        }
        return url;
    }

    /**
     * 查询区域
     * @param parentId
     */
    @RequestMapping("/getRegionList")
    public @ResponseBody Map getRegionList(@ObjectIdType ObjectId parentId) {
        Map map = new HashMap();
        List<RegionDTO> regions=educationBureauService.getRegionEntryList(0, parentId);
        map.put("regions",regions);
        return map;
    }

    /**
     * 查询全部教育局
     * @param model
     */
    @RequestMapping("/selAllEducation")
    public @ResponseBody  Map selAllEducation(Map<String, Object> model) {
        List<EducationBureauDTO> list= educationBureauService.selAllEducation();
        model.put("list",list);
        return model;
    }

    /**
     * 查询教育局
     * @param eduName
     * @param eduIdsStr
     */
    @RequestMapping("/getEducationList")
    public @ResponseBody  List<EducationBureauDTO> getEducationListByParams(String eduName, String eduIdsStr) {
        List<ObjectId> eduIds = new ArrayList<ObjectId>();
        if(null!=eduIdsStr&&!"".equals(eduIdsStr)){
            String[] arrEduIds=eduIdsStr.split(",");
            for(String str:arrEduIds){
                eduIds.add(new ObjectId(str));
            }
        }
        List<EducationBureauDTO> list= educationBureauService.getEducationListByParams(eduName,eduIds);
        return list;
    }

    /**
     * 查询教育局
     * @param eduName
     * @param province
     * @param city
     * @param county
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/selEducationList")
    public @ResponseBody  Map selEducationList(String eduName, @ObjectIdType(isRequire=false) ObjectId province, @ObjectIdType(isRequire=false) ObjectId city,
                               @ObjectIdType(isRequire=false) ObjectId county, int page, int size) {
        Map<String,Object> model = educationBureauService.selEducationList(eduName,province,city,county,page,size);
        return model;
    }

    /**
     * 判断教育局名称是否存在
     * @param id
     * @param educationName
     */
    @RequestMapping("/educationNameIsExist")
    public @ResponseBody Map educationNameIsExist(String id,@RequestParam("educationName") String educationName) {
        Map map = new HashMap();
        boolean isExist= educationBureauService.educationNameIsExist(id,educationName);
        map.put("isExist",isExist);
        return map;
    }


    /**
     * 跳转到添加教育局
     * @param model
     */
    @RequestMapping("/gotoAddEducation")
    public String gotoAddEducation(Map<String, Object> model) {
        int userRole=getSessionValue().getUserRole();
        String url="";
        if(UserRole.isSysManager(userRole)) {
            List<RegionDTO> regions=educationBureauService.getRegionEntryList(2, null);
            model.put("regions",regions);
            url="educationbureau/addEducation";
        }
        return url;
    }

    /**
     * 跳转到修改教育局
     * @param model
     */
    @RequestMapping("/gotoEditEducation")
    public String gotoEditEducation(Map<String, Object> model,String id) {
        int userRole=getSessionValue().getUserRole();
        String url="";
        if(UserRole.isSysManager(userRole)) {
            educationBureauService.getEducationInfoById(model,id);
            url="educationbureau/editEducation";
        }
        return url;
    }


    /**
     * 查询学校
     * @param province
     * @param city
     * @param county
     * @return
     */
    @RequestMapping("/getSchoolList")
    public @ResponseBody List<SchoolDTO> getSchoolList(@ObjectIdType(isRequire=false) ObjectId province,
                                @ObjectIdType(isRequire=false) ObjectId city,
                                @ObjectIdType(isRequire=false) ObjectId county,
                                String schoolName,String schoolIdsStr) {
        List<ObjectId> schoolIds = new ArrayList<ObjectId>();
        if(null!=schoolIdsStr&&!"".equals(schoolIdsStr)){
            String[] arrSchoolIds=schoolIdsStr.split(",");
            for(String str:arrSchoolIds){
                schoolIds.add(new ObjectId(str));
            }
        }
        List<SchoolDTO> list = educationBureauService.getSchoolList(province,city,county,schoolName,schoolIds);
        return list;
    }

    /**
     * 查询教育局用户
     * @return
     */
    @RequestMapping("/getEduUserList")
    public @ResponseBody List<UserInfoDTO> getEduUserList(Map<String, Object> model,String userName,String userIdsStr) {
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        if(null!=userIdsStr&&!"".equals(userIdsStr)){
            String[] arrUserIds=userIdsStr.split(",");
            for(String str:arrUserIds){
                userIds.add(new ObjectId(str));
            }
        }
        List<UserInfoDTO> list = educationBureauService.getAllEduUserList(userName,userIds);
        return list;
    }

    /**
     * 添加教育局
     * @param educationName
     * @param province
     * @param city
     * @param county
     */
    @RequestMapping("/addEducation")
    public @ResponseBody  Map addEducation(@RequestParam("educationName") String educationName,String province, String city, String county, String educationLogo, String userIdsStr, String schoolIdsStr) {
        Map map = new HashMap();
        try {
            List<String> userIds = new ArrayList<String>();
            List<String> schoolIds = new ArrayList<String>();
            if(!"".equals(userIdsStr)){
                String[] arrUserIds=userIdsStr.split(",");
                userIds.addAll(Arrays.asList(arrUserIds));
            }
            if(!"".equals(schoolIdsStr)){
                String[] arrSchoolIds=schoolIdsStr.split(",");
                schoolIds.addAll(Arrays.asList(arrSchoolIds));
            }
            EducationBureauDTO educationBureauDTO =new EducationBureauDTO();
            educationBureauDTO.setEducationName(educationName);
            educationBureauDTO.setProvince(province);
            educationBureauDTO.setCity(city);
            educationBureauDTO.setCounty(county);
            educationBureauDTO.setEducationLogo(educationLogo);
            educationBureauDTO.setUserIds(userIds);
            educationBureauDTO.setSchoolIds(schoolIds);
            ObjectId id= educationBureauService.addEducation(educationBureauDTO);
            if(id==null){
                map.put("resultCode", 3);
            }else{
                map.put("resultCode", 0);
            }
        } catch(Exception e) {
            map.put("resultCode", 2);
        }
        return map;
    }

    /**
     * 删除教育局
     */
    @RequestMapping("/delEducation")
    public @ResponseBody  Map delEducation(@ObjectIdType ObjectId id) {
        Map map = new HashMap();
        try {
            int userRole=getSessionValue().getUserRole();
            if(UserRole.isSysManager(userRole)) {
                educationBureauService.delEducation(id);
            }
        } catch(Exception e) {
            map.put("resultCode", 2);
        }
        return map;
    }

    /**
     * 教育局开通或关闭云平台
     */
    @RequestMapping("/educationCloud")
    public @ResponseBody  Map educationCloud(@ObjectIdType ObjectId id) {
        Map map = new HashMap();
        try {
            int userRole=getSessionValue().getUserRole();
            if(UserRole.isSysManager(userRole)) {
                educationBureauService.educationCloud(id);
            }
        } catch(Exception e) {
            map.put("resultCode", 2);
        }
        return map;
    }


    /**
     * 修改教育局
     * @param id
     * @param educationName
     * @param province
     * @param city
     * @param county
     */
    @RequestMapping("/editEducation")
    public @ResponseBody  Map editEducation(
            @RequestParam("id") String id,String educationName,
            String province,String city,String county,
            String educationLogo, String userIdsStr, String schoolIdsStr
    ) {
        Map map = new HashMap();
        try {
            List<ObjectId> schoolIds = new ArrayList<ObjectId>();
            if(!"".equals(schoolIdsStr)){
                String[] arrSchoolIds=schoolIdsStr.split(",");
                for(String str:arrSchoolIds){
                    schoolIds.add(new ObjectId(str));
                }
            }
            List<ObjectId> userIds = new ArrayList<ObjectId>();
            if(!"".equals(userIdsStr)){
                String[] arrUserIds=userIdsStr.split(",");
                for(String str:arrUserIds){
                    userIds.add(new ObjectId(str));
                }
            }
            String schoolCreateDate="";
            if(schoolIds.size()>0) {
                DateTimeUtils time=new DateTimeUtils();
                Collections.sort(schoolIds);
                schoolCreateDate = time.getLongToStrTime(schoolIds.get(0).getTime());
            }
            EducationBureauEntry entry=educationBureauService.selEducationById(id);
            entry.setEducationName(educationName);
            entry.setProvince(province);
            entry.setCity(city);
            entry.setCounty(county);
            entry.setEducationLogo(educationLogo);
            entry.setSchoolCreateDate(schoolCreateDate);
            entry.setUserIds(userIds);
            entry.setSchoolIds(schoolIds);
            educationBureauService.updateEducationInfo(entry);
            map.put("resultCode", 0);
        } catch(Exception e) {
            map.put("resultCode", 2);
        }
        return map;
    }


    /**
     * 删除旧教育局logo图片
     */
    @RequestMapping("/delOldEduLogo")
    public @ResponseBody  void delOldEduLogo(@RequestParam("oldEduLogo")String oldEduLogo,HttpServletRequest servletRequest) {
        if(null!=oldEduLogo&&!"".equals(oldEduLogo)){
            String filePath =servletRequest.getServletContext().getRealPath("/upload/img_cloud");
            filePath+=oldEduLogo.substring(0,oldEduLogo.lastIndexOf("/"));
            FileUtil util=new FileUtil();
            util.deleteFolder(filePath);
        }
    }

    /**
     * 上传教育局logo图片
     * @param request
     * @return
     */
    @RequestMapping(value = "/addEduLogoPic", produces = "text/html; charset=utf-8")
    public @ResponseBody Map addEduLogoPic(MultipartRequest request, HttpServletRequest servletRequest) {
        Map result = new HashMap<String,Object>();
        String path=servletRequest.getServletContext().getRealPath("/upload/img_cloud");
        List<String> fileUrls = new ArrayList<String>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String randomPath = "/" + FileUtil.randomPath();
                path+=randomPath;
                File dir = FileUtil.mkDir(path);
                String logoName =file.getOriginalFilename();
                File f = new File(dir, logoName);
                file.transferTo(f);
                fileUrls.add("/upload/img_cloud/"+randomPath + "/" + logoName);



                /*RespObj upladTestPaper= QiniuFileUtils.uploadFile(logoName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
                {
                    throw new FileUploadException();
                }
                fileUrls.add(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,logoName));*/
            }
            result.put("result", true);
            result.put("path", fileUrls);
        }  catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        return new ObjectId().toString()+Constant.POINT+ file.getOriginalFilename();
    }

    //================================================= 教育局管理 ======================================

    /**
     * 教育局管理年级
     * @return
     */
    @RequestMapping("/eduManageGrade")
    public String eduManageGrade(){
        return "/myschool/edu-manage-grade";
    }

    /**
     * 教育局年级列表
     * @return
     */
    @RequestMapping("/getGradeList")
    @ResponseBody
    public Map<String, Object> getGradeList(){
        return educationBureauService.getGradeList(getUserId());
    }

    /**
     * 添加年级
     * @param gradeName
     * @param gradeType
     * @return
     */
    @RequestMapping("/addGrade")
    @ResponseBody
    public Map<String, Object> addGrade(@ObjectIdType ObjectId eduId, String gradeName, int gradeType){
        Map<String, Object> model = new HashMap<String, Object>();
        educationBureauService.addGrade(eduId, gradeName, gradeType);
        model.put("code", "200");
        return model;
    }

    /**
     * 编辑年级
     * @param gradeName
     * @param gradeType
     * @return
     */
    @RequestMapping("/editGrade")
    @ResponseBody
    public Map<String, Object> editGrade(@ObjectIdType ObjectId eduId, @ObjectIdType ObjectId gradeId,String gradeName, int gradeType){
        Map<String, Object> model = new HashMap<String, Object>();
        educationBureauService.editGrade(eduId, gradeId, gradeName, gradeType);
        model.put("code", "200");
        return model;
    }

    /**
     * 删除年级
     * @param eduId
     * @param gradeId
     * @return
     */
    @RequestMapping("/delGrade")
    @ResponseBody
    public Map<String, Object> delGrade(@ObjectIdType ObjectId eduId, @ObjectIdType ObjectId gradeId){
        Map<String, Object> model = new HashMap<String, Object>();
        educationBureauService.delGrade(eduId, gradeId);
        model.put("code", "200");
        return model;
    }

    /**
     * 教育局管理学科
     * @return
     */
    @UserRoles(noValue = {UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/eduManageSubject")
    public String eduManageSubject(){
        return "/myschool/edu-manage-subject";
    }

    @RequestMapping("/getSubjectList")
    @ResponseBody
    public Map<String, Object> getSubjectList(){
        return educationBureauService.getSubjectList(getUserId());
    }

    /**
     * 添加学科
     * @param subjectName
     * @param gradeList
     * @return
     */
    @RequestMapping("/addSubject")
    @ResponseBody
    public Map<String, Object> addSubject(@ObjectIdType ObjectId eduId, String subjectName, int subjectType, String gradeList){
        Map<String, Object> model = new HashMap<String, Object>();
        educationBureauService.addSubject(eduId, subjectName, subjectType,  gradeList);
        model.put("code", "200");
        return model;
    }

    /**
     * 编辑学科
     * @param subjectName
     * @param gradeList
     * @return
     */
    @RequestMapping("/editSubject")
    @ResponseBody
    public Map<String, Object> editSubject(@ObjectIdType ObjectId eduId, @ObjectIdType ObjectId subjectId, String subjectName, int subjectType, String gradeList){
        Map<String, Object> model = new HashMap<String, Object>();
        educationBureauService.editSubject(eduId, subjectId, subjectName, subjectType, gradeList);
        model.put("code", "200");
        return model;
    }

    /**
     * 删除学科
     * @param eduId
     * @param subjectId
     * @return
     */
    @RequestMapping("/delSubject")
    @ResponseBody
    public Map<String, Object> delSubject(@ObjectIdType ObjectId eduId,@ObjectIdType ObjectId subjectId){
        Map<String, Object> model = new HashMap<String, Object>();
        educationBureauService.delSubject(eduId, subjectId);
        model.put("code", "200");
        return model;
    }
    
    /**
     * 根据用户ID，获取该用户所属教育局的学校dtos
     * @return
     */
    @RequestMapping("/user/schools")
    @ResponseBody
    public RespObj getSchoolDTOsByUserId()
    {
    	RespObj obj =new RespObj(Constant.SUCCESS_CODE);
    	List<SchoolDTO> dtos=educationBureauService.getEducationSchoolByUserId(getUserId(),getSessionValue().getUserRole(),null);
    	obj.setMessage(dtos);
    	return obj;
    }
}
