package com.fulaan.schoolsecurity.contraller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.schoolsecurity.dto.SchoolSecurityDTO;
import com.fulaan.schoolsecurity.service.SchoolSecurityService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.schoolsecurity.SchoolSecurityEntry;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.*;

/**
 * Created by guojing on 2015/6/18.
 */
@Controller
@RequestMapping("/schoolSecurity")
public class SchoolSecurityController extends BaseController {

    private static final Logger logger = Logger.getLogger(SchoolSecurityController.class);

    @Autowired
    private SchoolSecurityService schoolSecurityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExperienceService experienceService;
    /**
     * 校园安全
     * @param model
     * @return
     */
    @RequestMapping("/schoolSecurityPage")
    public String schoolSecurityPage(Map<String, Object> model) {
        return "schoolsecurity/schoolsecurity";
    }

    /**
     * 添加校园安全
     * @param publishContent
     * @param fileNameAry
     * @param headers
     * @return
     */
    @RequestMapping("publishSchoolSecurity")
    public @ResponseBody Map<String ,Object> publishSchoolSecurity(String publishContent, String fileNameAry,@RequestHeader HttpHeaders headers) {
        Map<String,Object> model = new HashMap<String,Object>();
        String userId = getSessionValue().getId();
        String schoolId = getSessionValue().getSchoolId();

        String key = CacheHandler.getKeyString(CacheHandler.CACHE_PUBLISH_SCHOOL_SECURITY, userId);
        long curTime = new Date().getTime()/ 1000;
        if(UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            String oldTimeStr = CacheHandler.getStringValue(key);
            if (oldTimeStr != null) {
                long oldTime = Long.parseLong(oldTimeStr);
                int minute = (int)(curTime - oldTime);
                int intervalTime = Constant.SESSION_FIVE_MINUTE;
                if (minute <= intervalTime) {
                    model.put("surplusTime",intervalTime-minute);
                    model.put("resultCode","2");
                    return model;
                }
            }
        }
        SchoolSecurityDTO schoolSecurityDTO=new SchoolSecurityDTO();
        schoolSecurityDTO.setPublishContent(publishContent);
        schoolSecurityDTO.setFileNameAry(fileNameAry.split(","));

        //判断发布内容和上传图片是否为空
        if (StringUtils.isEmpty(schoolSecurityDTO.getPublishContent())
                && schoolSecurityDTO.getFileNameAry()!=null && schoolSecurityDTO.getFileNameAry().length!=0) {
            model.put("resultCode","1");
            return model;
        }
        schoolSecurityDTO.setUserId(userId);
        //获取客户端信息
        String client = headers.getFirst("User-Agent");
        SchoolSecurityEntry schoolSecurityEntry = schoolSecurityDTO.buildSchoolSecurityEntry(client, new ObjectId(schoolId));
        //添加校园安全信息
        ObjectId schoolSecurityId=schoolSecurityService.addSchoolSecurity(schoolSecurityEntry);
        //记录经验值
        ExpLogType schoolSecurity = ExpLogType.SCHOOL_SECURITY;
        if (experienceService.updateScore(getUserId().toString(), schoolSecurity, schoolSecurityId.toString())) {
            model.put("scoreMsg", schoolSecurity.getDesc());
            model.put("score", schoolSecurity.getExp());
        }
        model.put("resultCode","0");
        if(UserRole.isStudentOrParent(getSessionValue().getUserRole())) {
            CacheHandler.cache(key, curTime + "", Constant.SESSION_FIVE_MINUTE);
        }
        return model;
    }

    /**
     * 删除校园安全
     *@param id
     * @return
     */
    @RequestMapping("deleteSchoolSecurity")
    public @ResponseBody Map<String,Object> deleteSchoolSecurityInfo(@RequestParam("id") String id) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        ObjectId userId = getUserId();
        if(null==id || !ObjectId.isValid(id))
        {
            logger.error("the param id is error!the user:"+getSessionValue());
        }
        //删除校园安全信息（逻辑删除）
        schoolSecurityService.deleteSchoolSecurityInfo(id, userId);
        //记录经验值
        /*ExpLogType schoolSecurity = ExpLogType.DELETE_SCHOOL_SECURITY;
        if (experienceService.updateScore(getUserId().toString(), schoolSecurity, id.toString())) {
            model.put("scoreMsg", schoolSecurity.getDesc());
            model.put("score", schoolSecurity.getExp());
        }*/
        model.put("result", true);
        return model;
    }

    /**
     * 批量删除校园安全
     *@param delIds
     * @return
     */
    @UserRoles(value = {UserRole.HEADMASTER})
    @RequestMapping("batchDeleteSchoolSecurity")
    public @ResponseBody Map<String,Object> batchDeleteSchoolSecurity(@RequestParam("delIds")String delIds) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        ObjectId userId = getUserId();
        String[] arrIds=delIds.split(",");
        List<String> ids= Arrays.asList(arrIds);
        //删除校园安全信息（逻辑删除）
        schoolSecurityService.batchDeleteSchoolSecurity(ids, userId);
        //记录经验值
        /*ExpLogType schoolSecurity = ExpLogType.DELETE_SCHOOL_SECURITY;
        if (experienceService.updateScore(getUserId().toString(), schoolSecurity, id.toString())) {
            model.put("scoreMsg", schoolSecurity.getDesc());
            model.put("score", schoolSecurity.getExp());
        }*/
        model.put("result", true);
        return model;
    }

    /**
     * 处理校园安全
     *@param id
     * @return
     */
    @RequestMapping("handleSchoolSecurity")
    public @ResponseBody Map<String,Object> handleSchoolSecurity(@RequestParam("id") String id) throws Exception{
        Map<String,Object>  model = new HashMap<String,Object>();
        ObjectId userId = getUserId();
        if(null==id || !ObjectId.isValid(id))
        {
            logger.error("the param id is error!the user:"+getSessionValue());
        }
        //将未处理的校园安全信息变更为已处理
        schoolSecurityService.handleSchoolSecurity(id, userId);
        model.put("result", true);
        return model;
    }

    /**
     * 上传校园安全图片
     * @param request
     * @return
     */
    @RequestMapping(value = "/addSchoolSecurityPic", produces = "text/html; charset=utf-8")
    public @ResponseBody Map addSchoolSecurityPic(MultipartRequest request) {
        Map result = new HashMap<String,Object>();
        String filePath = FileUtil.SCHOOL_SECURITY;
        List<String> fileUrls = new ArrayList<String>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String examName =getFileName(file);
                RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
                {
                    throw new FileUploadException();
                }
                fileUrls.add(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,examName));
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

    /**
     * 校园安全列表
     * @param handleState
     * @param page
     * @param pageSize
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     */
    @RequestMapping("selSchoolSecurityInfo")
    public @ResponseBody Map<String,Object> selSchoolSecurityInfo(@RequestParam("handleState") int handleState,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) throws ResultTooManyException {
        Map<String,Object> model = new HashMap<String,Object>();
        String schoolId = getSessionValue().getSchoolId();
        if (pageSize == 0) {
            pageSize = 10;
        }
        //根据条件获取校园安全信息数量
        int count = schoolSecurityService.selSchoolSecurityCount(handleState, schoolId);
        //根据条件获取校园安全信息

        List<SchoolSecurityEntry> schoolSecurityList = schoolSecurityService.selSchoolSecurityInfo(handleState, getUserId(), schoolId, page, pageSize);

        List<SchoolSecurityDTO> dTOList = new ArrayList<SchoolSecurityDTO>();
        for (SchoolSecurityEntry schoolSecurity : schoolSecurityList) {
            SchoolSecurityDTO schoolSecurityDTO = new SchoolSecurityDTO(schoolSecurity);
            //获取发布校园安全信息的用户信息
            UserEntry user = userService.searchUserId(schoolSecurity.getUserId());
            if (user != null) {
                schoolSecurityDTO.setUserImage(AvatarUtils.getAvatar(user.getAvatar(), 3));
                schoolSecurityDTO.setUserName(user.getUserName());
                schoolSecurityDTO.setRole(user.getRole());
                schoolSecurityDTO.setRoleDescription(UserRole.getRoleDescription(user.getRole()));
            }
            if (UserRole.isNotStudentAndParent(getSessionValue().getUserRole())) {
                schoolSecurityDTO.setSchoolManager(1);
            }else{
                schoolSecurityDTO.setSchoolManager(0);
            }
            schoolSecurityDTO.setCreateTime((DateTimeUtils.convert(schoolSecurity.getPublishTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A)));
            dTOList.add(schoolSecurityDTO);
        }

        model.put("rows",dTOList);
        model.put("total", count);
        model.put("page", page);
        model.put("pageSize", pageSize);
        return model;
    }

    /**
     * 查询单条校园安全
     * @param id
     * @return
     */
    @RequestMapping("selOneSchoolSecurityInfo")
    public @ResponseBody Map<String,Object> selOneSchoolSecurityInfo(@RequestParam("id") String id) {
        Map<String,Object> model = new HashMap<String,Object>();
        //根据校园安全信息id获取校园安全信息
        SchoolSecurityEntry schoolSecurityEntry = schoolSecurityService.selOneSchoolSecurityInfo(id);
        SchoolSecurityDTO schoolSecurityDTO = new SchoolSecurityDTO(schoolSecurityEntry);
        //获取发布校园安全信息的用户信息
        UserEntry user = userService.searchUserId(schoolSecurityEntry.getUserId());
        if (user!=null) {
            schoolSecurityDTO.setUserImage(AvatarUtils.getAvatar(user.getAvatar(), 3));
            schoolSecurityDTO.setUserName(user.getUserName());
            schoolSecurityDTO.setRole(user.getRole());
            schoolSecurityDTO.setRoleDescription(UserRole.getRoleDescription(user.getRole()));
        }
        model.put("data",schoolSecurityDTO);
        return model;
    }
}
