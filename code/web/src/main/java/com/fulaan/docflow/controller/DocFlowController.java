package com.fulaan.docflow.controller;

import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.docflow.dto.SimpleSchoolDTO;
import com.fulaan.docflow.service.DocFlowService;
import com.fulaan.educationbureau.dto.EducationBureauDTO;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.app.FileUploadDTO;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.UserGroupInfo;
import com.pojo.docflow.*;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.user.ExpLogType;
import com.pojo.user.SimpleUserInfo;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by qiangm on 2015/8/17.
 */
@Controller
@RequestMapping("/docflow")
public class DocFlowController extends BaseController {
    private static final Logger logger = Logger.getLogger(DocFlowController.class);
    private DocFlowService docFlowService = new DocFlowService();
    private UserService userService = new UserService();
    private EducationBureauService educationBureauService = new EducationBureauService();
    @Resource
    private ExperienceService experienceService;

    /**
     * 跳转页面方法
     *
     * @return 目标页面地址
     */
    @RequestMapping("/documentCreate")
    @UserRoles(noValue = {UserRole.STUDENT,UserRole.PARENT})
    public String documentCreate(Model model,@RequestParam(required = false,defaultValue = "1")int a) {
        model.addAttribute("userName", getSessionValue().getUserName());
        model.addAttribute("currentTerm", getCurrentTerm());
        if(a==10000) {
            if(UserRole.isEducation(getSessionValue().getUserRole())) {
                EducationBureauDTO educationBureauDTO = educationBureauService.getEducationByEduUserId(getUserId());
                model.addAttribute("eduId", educationBureauDTO.getId());
                model.addAttribute("eduName", educationBureauDTO.getEducationName());
                return "/docflow/documentCreatec";
            }
            else
            {
                return "/docflow/documentCreateTc";
            }
        }
        else if(UserRole.isEducation(getSessionValue().getUserRole()))
        {
            EducationBureauDTO educationBureauDTO = educationBureauService.getEducationByEduUserId(getUserId());
            model.addAttribute("eduId",educationBureauDTO.getId());
            model.addAttribute("eduName",educationBureauDTO.getEducationName());
            return "/docflow/documentCreateEdu";
        }
        return "/docflow/documentCreate";
    }

    @RequestMapping("/documentList")
    @UserRoles(noValue = {UserRole.STUDENT,UserRole.PARENT})
    public String documentList(@RequestParam int type, Model model,@RequestParam(required=false,defaultValue="1")int a) {
        model.addAttribute("type", type);
        model.addAttribute("uid", getUserId().toString());
        if(a==10000) {
            if(UserRole.isEducation(getSessionValue().getUserRole())) {
                return "/docflow/documentListc";
            }
            else
            {
                return "/docflow/documentListTc";
            }
        }
        else if(UserRole.isEducation(getSessionValue().getUserRole()))
        {
            return "/docflow/documentListEdu";
        }
        else {
            return "/docflow/documentList";
        }
    }

    @RequestMapping("/documentDetail")
    @UserRoles(noValue = {UserRole.STUDENT,UserRole.PARENT})
    public String documentDetail(int type, String docId, String p, String d, Model model,@RequestParam(required=false,defaultValue="1")int a) {
        if (type == 0) {//查看公文，修改未阅读人
            docFlowService.updateUnread(getUserId(), new ObjectId(docId));
        }
        model.addAttribute("type", type);
        model.addAttribute("docId", docId);
        if (!p.equals("")) {
            try {
                p = java.net.URLDecoder.decode(p, "UTF-8");
                d = java.net.URLDecoder.decode(d, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            model.addAttribute("person", p);
            model.addAttribute("department", d);
        }
        if(a==10000) {
            if (UserRole.isEducation(getSessionValue().getUserRole()))
            {
                return "/docflow/documentDetailc";
            }
            else
            {
                return "/docflow/documentDetailTc";
            }
        }
        else if(UserRole.isEducation(getSessionValue().getUserRole()))
        {
            return "/docflow/documentDetailEdu";
        }
        else {
            return "/docflow/documentDetail";
        }
    }

    @RequestMapping("/documentCheck")
    @UserRoles(noValue = {UserRole.STUDENT,UserRole.PARENT})
    public String documentCheck(String docId, Model model,@RequestParam(required=false,defaultValue="1")int a) {
        model.addAttribute("docId", docId);
        model.addAttribute("userName", getSessionValue().getUserName());
        if(a==10000) {
            if(UserRole.isEducation(getSessionValue().getUserRole())) {
                return "/docflow/documentCheckc";
            }
            return "/docflow/documentCheckTc";
        }
        else if(UserRole.isEducation(getSessionValue().getUserRole()))
        {
            return "/docflow/documentCheckEdu";
        }
        return "/docflow/documentCheck";
    }

    @RequestMapping("/documentModify")
    @UserRoles(noValue = {UserRole.PARENT,UserRole.PARENT})
    public String documentModify(int type, String docId, Model model,@RequestParam(required=false,defaultValue="1")int a) {
        model.addAttribute("docId", docId);
        model.addAttribute("type", type);
        if(a==10000) {
            if(UserRole.isEducation(getSessionValue().getUserRole())) {
                return "/docflow/documentModifyc";
            }
            return "/docflow/documentModifyTc";
        }
        else if(UserRole.isEducation(getSessionValue().getUserRole()))
        {
            return "/docflow/documentModifyEdu";
        }
        return "/docflow/documentModify";
    }

    @RequestMapping("/myDocumentModify")
    @UserRoles(noValue = {UserRole.STUDENT,UserRole.PARENT})
    public String myDocumentModify(String docId, Model model,@RequestParam(required=false,defaultValue="1")int a) {
        model.addAttribute("docId", docId);
        model.addAttribute("userName", getSessionValue().getUserName());
        if(a==10000) {
            if(UserRole.isEducation(getSessionValue().getUserRole())) {
                return "/docflow/myDocumentModifyc";
            }
            return "/docflow/myDocumentModifyTc";
        }
        else if(UserRole.isEducation(getSessionValue().getUserRole()))
        {
            return "/docflow/myDocumentModifyEdu";
        }
        return "/docflow/myDocumentModify";
    }


    /**
     * 获取公文列表---已经修改
     *
     * @param term     学期
     * @param isHandle 处理/未处理 0未处理 1已处理
     * @param keyWords 关键词
     * @param page     当前页数
     * @param pageSize 每页显示数目
     * @param type     0公文 1审阅 2我的公文
     * @return map结构
     */
    @RequestMapping("/doclist")
    @ResponseBody
    public Map<String, Object> docList(@RequestParam String term, @RequestParam Boolean isHandle, @RequestParam String keyWords,
                                       @RequestParam int page, @RequestParam int pageSize, @RequestParam int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectId userId = getUserId();
        term = term.equals("") ? getCurrentTerm() : term;
        int count = docFlowService.getDocFlowCount(userId, term, isHandle, keyWords, type);
        List<SimpleDocFlowDTO> docFlowDTOs = docFlowService.getDocFlowList(userId, term, isHandle, keyWords, page, pageSize, type);
        //填充数据：用户名、部门名，是否未读标记
        if (docFlowDTOs != null && !docFlowDTOs.isEmpty()) {
            List<ObjectId> departmentIds = new ArrayList<ObjectId>();
            List<ObjectId> userIds = new ArrayList<ObjectId>();
            for (SimpleDocFlowDTO docFlowDTO : docFlowDTOs) {
                departmentIds.add(new ObjectId(docFlowDTO.getDepartmentId()));
                List<DocCheckDTO> docCheckDTOs = docFlowDTO.getCheckDTOList();
                if (docCheckDTOs != null && !docCheckDTOs.isEmpty()) {
                    boolean finish1 = false;
                    boolean finish2 = false;
                    for (DocCheckDTO docCheckDTO : docCheckDTOs) {
                        if (type == 0 || type == 1) {
                            if (docCheckDTO.getId().equals(docFlowDTO.getCheckId())) {
                                departmentIds.add(new ObjectId(docCheckDTO.getDepartmentId()));
                                userIds.add(new ObjectId(docCheckDTO.getUserId()));
                                break;
                            }
                        } else {
                            if (docCheckDTO.getOpinion() == -1) {
                                departmentIds.add(new ObjectId(docCheckDTO.getDepartmentId()));
                                userIds.add(new ObjectId(docCheckDTO.getUserId()));
                                finish1 = true;
                            } else if (docCheckDTO.getId().equals(docFlowDTO.getCheckId())) {
                                departmentIds.add(new ObjectId(docCheckDTO.getDepartmentId()));
                                userIds.add(new ObjectId(docCheckDTO.getUserId()));
                                finish2 = true;
                            }
                            if (finish1 && finish2)
                                break;
                        }
                    }
                }
            }
            Map<String, String> departmentMap = docFlowService.getDepartmentsByDepIds(departmentIds);
            Map<String, String> userMap = docFlowService.getUserNameByIds(userIds);
            if (type == 0)//公文
            {
                for (SimpleDocFlowDTO docFlowDTO : docFlowDTOs) {
                    List<DocCheckDTO> docCheckDTOs = docFlowDTO.getCheckDTOList();
                    String department=departmentMap.get(docFlowDTO.getDepartmentId());
                    if(department==null)
                    {
                        EducationBureauEntry educationBureauEntry=educationBureauService.selEducationById(docFlowDTO.getDepartmentId());
                        if(educationBureauEntry!=null)
                        {
                            department=educationBureauEntry.getEducationName();
                            docFlowDTO.setEducationPub(1);
                        }
                    }
                    docFlowDTO.setCheckDepartment(department);//设置发文部门名
                    docFlowDTO.setCheckTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new ObjectId(docFlowDTO.getCheckId()).getTime())));//设置发布日期
                    if (docFlowDTO.getUnreadList().contains(userId)) {
                        docFlowDTO.setTag(0);//标记未读
                    }
                }
            } else if (type == 1)//审阅---撰写时间、最新状态、上一位审阅时间
            {
                for (SimpleDocFlowDTO docFlowDTO : docFlowDTOs) {
                    docFlowDTO.setTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new ObjectId(docFlowDTO.getId()).getTime())));//设置撰写日期
                    docFlowDTO.setCheckTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(new ObjectId(docFlowDTO.getCheckId()).getTime())));//设置上一位审阅日期
                    //设置当前状态
                    List<DocCheckDTO> docCheckDTOs = docFlowDTO.getCheckDTOList();
                    if (docCheckDTOs != null && !docCheckDTOs.isEmpty()) {
                        for (DocCheckDTO docCheckDTO : docCheckDTOs) {//找到最新状态对应的审核历史
                            if (docCheckDTO.getId().equals(docFlowDTO.getCheckId())) {
                                docFlowDTO.setStateDesc(generalState(docCheckDTO.getOpinion(), userMap.get(docCheckDTO.getUserId())));//设置最新状态
                                break;
                            }
                        }
                    }
                }
            } else if (type == 2)//我的公文---撰写日期、审阅人、审阅部门、状态
            {
                for (SimpleDocFlowDTO docFlowDTO : docFlowDTOs) {
                    docFlowDTO.setTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new ObjectId(docFlowDTO.getId()).getTime())));//设置撰写日期
                    List<DocCheckDTO> docCheckDTOs = docFlowDTO.getCheckDTOList();
                    if (docCheckDTOs != null && !docCheckDTOs.isEmpty()) {
                        boolean finish1 = false;
                        boolean finish2 = false;
                        docFlowDTO.setCheckDepartment("--");//设置审阅部门名
                        docFlowDTO.setCheckPerson("--");//设置审阅人姓名
                        for (DocCheckDTO docCheckDTO : docCheckDTOs) {
                            if (docCheckDTO.getId().equals(docFlowDTO.getCheckId())) {
                                docFlowDTO.setStateDesc(generalState(docCheckDTO.getOpinion(), userMap.get(docCheckDTO.getUserId())));//设置最新状态
                                finish1 = true;
                            } else if (docCheckDTO.getOpinion() == -1) {
                                docFlowDTO.setCheckDepartment(departmentMap.get(docCheckDTO.getDepartmentId()));//设置审阅部门名
                                docFlowDTO.setCheckPerson(userMap.get(docCheckDTO.getUserId()));//设置审阅人姓名
                                finish1 = true;
                            }
                            if (finish1 && finish2)
                                break;
                        }
                    }
                }
            }
        }
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("rowCount", count);
        map.put("rows", docFlowDTOs);
        return map;
    }

    /**
     * 公文列表----教育局专用
     * @param term
     * @param schoolId
     * @param keyWords
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    @RequestMapping("/doclistEdu")
    @UserRoles(value = {UserRole.EDUCATION})
    @ResponseBody
    public Map<String, Object> docListEdu(@RequestParam String term, @RequestParam String schoolId, @RequestParam String keyWords,
                                       @RequestParam int page, @RequestParam int pageSize, @RequestParam int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectId userId = getUserId();
        term = term.equals("") ? getCurrentTerm() : term;
        int count = docFlowService.getDocFlowCountEdu(userId, term, schoolId, keyWords, type);
        List<SimpleDocFlowDTO> docFlowDTOs = docFlowService.getDocFlowListEdu(userId, term, schoolId, keyWords, page, pageSize, type);
        //填充数据：用户名、部门名，是否未读标记
        if (docFlowDTOs != null && !docFlowDTOs.isEmpty()) {
            List<ObjectId> departmentIds = new ArrayList<ObjectId>();
            //List<ObjectId> userIds = new ArrayList<ObjectId>();
            for (SimpleDocFlowDTO docFlowDTO : docFlowDTOs) {
                departmentIds.add(new ObjectId(docFlowDTO.getDepartmentId()));
                List<DocCheckDTO> docCheckDTOs = docFlowDTO.getCheckDTOList();
                if (docCheckDTOs != null && !docCheckDTOs.isEmpty()) {
                    boolean finish1 = false;
                    boolean finish2 = false;
                    for (DocCheckDTO docCheckDTO : docCheckDTOs) {
                        if (type == 0 || type == 1) {
                            if (docCheckDTO.getId().equals(docFlowDTO.getCheckId())) {
                                departmentIds.add(new ObjectId(docCheckDTO.getDepartmentId()));
                                //userIds.add(new ObjectId(docCheckDTO.getUserId()));
                                break;
                            }
                        } else {
                            if (docCheckDTO.getOpinion() == -1) {
                                departmentIds.add(new ObjectId(docCheckDTO.getDepartmentId()));
                                //userIds.add(new ObjectId(docCheckDTO.getUserId()));
                                finish1 = true;
                            } else if (docCheckDTO.getId().equals(docFlowDTO.getCheckId())) {
                                departmentIds.add(new ObjectId(docCheckDTO.getDepartmentId()));
                                //userIds.add(new ObjectId(docCheckDTO.getUserId()));
                                finish2 = true;
                            }
                            if (finish1 && finish2)
                                break;
                        }
                    }
                }
            }
            Map<String, String> departmentMap = docFlowService.getDepartmentsByDepIds(departmentIds);
            //Map<String, String> userMap = docFlowService.getUserNameByIds(userIds);
            if (type == 0)//公文
            {
                for (SimpleDocFlowDTO docFlowDTO : docFlowDTOs) {
                    docFlowDTO.setCheckDepartment(educationBureauService.getEducationByEduUserId(getUserId()).getEducationName());//设置发文部门名
                    docFlowDTO.setCheckTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new ObjectId(docFlowDTO.getCheckId()).getTime())));//设置发布日期
                    /*if (docFlowDTO.getUnreadList().contains(userId)) {
                        docFlowDTO.setTag(0);//标记未读
                    }*/
                }
            } else if (type == 1)//审阅---撰写时间、最新状态、上一位审阅时间
            {
                for (SimpleDocFlowDTO docFlowDTO : docFlowDTOs) {
                    if(departmentMap.get(docFlowDTO.getDepartmentId())==null)
                    {
                        List<ObjectId> educationIds=new ArrayList<ObjectId>();
                        educationIds.add(new ObjectId(docFlowDTO.getDepartmentId()));
                        docFlowDTO.setCheckDepartment(educationBureauService.findEduInfoByEduIds(educationIds).get(0).getEducationName());
                    }
                    else {
                        docFlowDTO.setCheckDepartment(departmentMap.get(docFlowDTO.getDepartmentId()));//设置发文部门名
                    }
                    docFlowDTO.setCheckTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(new ObjectId(docFlowDTO.getCheckId()).getTime())));//设置发布日期
                }
            }
        }
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("rowCount", count);
        map.put("rows", docFlowDTOs);
        return map;
    }

    /**
     * 根据审阅意见生成公文最新状态
     *
     * @param type
     * @param name
     * @return
     */
    public String generalState(int type, String name) {
        String typeStr = CheckStateEnum.getCheckState(type);
        if (type == 0 || type == 4) {//同意并转发、转寄
            return name + typeStr;
        }
        if (type == 1 || type == 3) {//被驳回、被废弃
            return "被" + name + typeStr;
        }
        if (type == 6 || type == 7)
            return "由" + name + typeStr;
        return "已" + typeStr;//发布、撤销
    }


    /**
     * 修改
     *
     * @param docId
     * @param departmentId
     * @param title
     * @param publishIds
     * @param content
     * @param modifyReason
     * @return
     */
    @RequestMapping("/modifyDoc")
    @ResponseBody
    public Map<String, String> modifyDoc(@RequestParam String docId, @RequestParam String departmentId,
                                         @RequestParam String title,@RequestParam String publishIds,
                                         @RequestParam String content,@RequestParam String docIds,
                                         @RequestParam String userIds,@RequestParam String docNames,
                                         @RequestParam String docValues,@RequestParam String checkManId,
                                         @RequestParam String checkDepartmentId,@RequestParam String modifyReason,
                                         @RequestParam int type) {
        Map<String, String> map = new HashMap<String, String>();
        ObjectId userId = getUserId();
        DocFlowDTO docFlowDTO = new DocFlowDTO();
        docFlowDTO.setId(docId);
        docFlowDTO.setUserId(userId.toString());
        docFlowDTO.setTitle(title);
        docFlowDTO.setContent(content);
        String[] publishList = StringUtils.split(publishIds, Constant.COMMA);
        List<String> publishIdList = new ArrayList<String>();
        for (String str : publishList) {
            publishIdList.add(str);
        }
        docFlowDTO.setPublishList(publishIdList);
        List<String> unReadList = new ArrayList<String>();
        unReadList.addAll(publishIdList);
        unReadList = setUnreadList(userId, unReadList);//未读加入本校校长以及管理员
        docFlowDTO.setUnreadList(unReadList);
        List<DocCheckDTO> docCheckDTOs = new ArrayList<DocCheckDTO>();
        String currentStateId = new ObjectId().toString();
        ObjectId dep = findDepartmentId(docId);
        if (dep != null) {
            departmentId = dep.toString();//用于审阅中的部门id
        }
        if (type == 0)//公文修改，修改完直接发布
        {
            departmentId=new ObjectId().toString();
            DocCheckDTO docCheckDTO = new DocCheckDTO(new ObjectId().toString(), userId.toString(), departmentId, 7, "校长/管理员编辑");
            docCheckDTOs.add(docCheckDTO);
            currentStateId = new ObjectId().toString();
            docCheckDTO = new DocCheckDTO(currentStateId, userId.toString(), departmentId, 2, "校长/管理员直接发布");
            docCheckDTOs.add(docCheckDTO);
        } else if (type == 1)//审阅时修改，只修改
        {
            DocCheckDTO docCheckDTO = new DocCheckDTO(currentStateId, userId.toString(), departmentId, 7, modifyReason);
            docCheckDTOs.add(docCheckDTO);

        } else if (type == 2)//我的公文被驳回时修改，修改并添加下一位审阅人
        {
            currentStateId = new ObjectId().toString();
            DocCheckDTO docCheckDTO = new DocCheckDTO(currentStateId, userId.toString(), departmentId, 7, modifyReason);
            docCheckDTOs.add(docCheckDTO);
            docCheckDTO = new DocCheckDTO(new ObjectId().toString(), checkManId, checkDepartmentId, -1, "");
            docCheckDTOs.add(docCheckDTO);
        }
        List<IdUserFilePairDTO> docList = generalFileList(docNames, docValues, docIds, userIds);
        docFlowDTO.setDocList(docList);
        docFlowService.updateDocFlow(docFlowDTO, docCheckDTOs, new ObjectId(currentStateId), type);
        map.put("code", Constant.SUCCESS_CODE);
        return map;
    }

    /**
     * 获取当前待审阅的check id
     *
     * @param docId
     * @return
     */
    public ObjectId findDepartmentId(String docId) {
        DocFlowDTO docFlowDTO = docFlowService.getDocDetail(new ObjectId(docId));
        for (DocCheckDTO docCheckDTO : docFlowDTO.getCheckDTOList()) {
            if (docCheckDTO.getOpinion() == -1) {
                return new ObjectId(docCheckDTO.getDepartmentId());
            }
        }
        return null;
    }
    @RequestMapping("/adddocEdu")
    @ResponseBody
    public Map<String,String> addDocEdu(@RequestParam String departmentId, @RequestParam String term, @RequestParam String title,
                                        @RequestParam String publishIds,@RequestParam String content, @RequestParam String docNames,
                                        @RequestParam String docAddress)
    {
        Map<String, String> map = new HashMap<String, String>();
        DocFlowDTO docFlowDTO = new DocFlowDTO();
        ObjectId userId = getUserId();
        docFlowDTO.setUserId(userId.toString());
        docFlowDTO.setDepartmentId(departmentId);
        docFlowDTO.setTerm(term);
        docFlowDTO.setTitle(title);
        String[] publishList = StringUtils.split(publishIds, Constant.COMMA);
        List<String> publishIdList = new ArrayList<String>();
        for (String str : publishList) {
            publishIdList.add(str);
        }
        docFlowDTO.setPublishList(publishIdList);
        List<String> unReadList = new ArrayList<String>();
        unReadList.addAll(publishIdList);
        //unReadList = setUnreadList(userId, unReadList);//未读加入本校校长以及管理员
        docFlowDTO.setUnreadList(unReadList);
        docFlowDTO.setIfCheck(false);
        docFlowDTO.setContent(content);

        List<IdUserFilePairDTO> docList = generalFileList(docNames, docAddress, "", "");
        docFlowDTO.setDocList(docList);
        List<DocCheckDTO> checkList = new ArrayList<DocCheckDTO>();
        DocCheckDTO docCheckDTO = new DocCheckDTO(new ObjectId().toString(), userId.toString(), departmentId, 6, "");
        checkList.add(docCheckDTO);//先添加撰写记录

        docCheckDTO = new DocCheckDTO(new ObjectId().toString(), userId.toString(), departmentId, 2, "");
        docCheckDTO.setId(new ObjectId().toString());
        checkList.add(docCheckDTO);
        docFlowDTO.setCheckDTOList(checkList);
        docFlowDTO.setState(2);
        docFlowDTO.setCheckId(docCheckDTO.getId());
        //docFlowDTO.setSchoolId(getSessionValue().getSchoolId());
        docFlowService.addDocFlow(docFlowDTO);
        map.put("code", Constant.SUCCESS_CODE);
        return map;
    }
    /**
     * 添加公文
     *
     * @param departmentId
     * @param term
     * @param title
     * @param ifCheck
     * @param publishIds
     * @param checkManId
     * @param checkDepId
     * @param content
     * @param docNames
     * @param docAddress
     * @return
     */
    @RequestMapping("/adddoc")
    @ResponseBody
    public Map<String, String> addDoc(@RequestParam String departmentId, @RequestParam String term, @RequestParam String title,
                                      @RequestParam boolean ifCheck, @RequestParam String publishIds, @RequestParam String checkManId,
                                      @RequestParam String checkDepId, @RequestParam String content, @RequestParam String docNames,
                                      @RequestParam String docAddress) {
        Map<String, String> map = new HashMap<String, String>();
        DocFlowDTO docFlowDTO = new DocFlowDTO();
        ObjectId userId = getUserId();
        docFlowDTO.setUserId(userId.toString());
        docFlowDTO.setDepartmentId(departmentId);
        docFlowDTO.setTerm(term);
        docFlowDTO.setTitle(title);
        String[] publishList = StringUtils.split(publishIds, Constant.COMMA);
        List<String> publishIdList = new ArrayList<String>();
        for (String str : publishList) {
            publishIdList.add(str);
        }
        docFlowDTO.setPublishList(publishIdList);
        List<String> unReadList = new ArrayList<String>();
        unReadList.addAll(publishIdList);
        unReadList = setUnreadList(userId, unReadList);//未读加入本校校长以及管理员
        docFlowDTO.setUnreadList(unReadList);
        docFlowDTO.setIfCheck(ifCheck);
        docFlowDTO.setContent(content);

        List<IdUserFilePairDTO> docList = generalFileList(docNames, docAddress, "", "");
        docFlowDTO.setDocList(docList);
        List<DocCheckDTO> checkList = new ArrayList<DocCheckDTO>();
        DocCheckDTO docCheckDTO = new DocCheckDTO(new ObjectId().toString(), userId.toString(), departmentId, 6, "");
        checkList.add(docCheckDTO);//先添加撰写记录
        if (ifCheck) {//需要审阅
            docFlowDTO.setCheckId(docCheckDTO.getId());
            docCheckDTO = new DocCheckDTO(new ObjectId().toString(), checkManId, checkDepId, -1, "");
            checkList.add(docCheckDTO);
            docFlowDTO.setCheckDTOList(checkList);
            docFlowDTO.setState(-1);
        } else {//不需要审阅,直接发布
            docCheckDTO = new DocCheckDTO(new ObjectId().toString(), userId.toString(), departmentId, 2, "");
            docCheckDTO.setId(new ObjectId().toString());
            checkList.add(docCheckDTO);
            docFlowDTO.setCheckDTOList(checkList);
            docFlowDTO.setState(2);
            docFlowDTO.setCheckId(docCheckDTO.getId());
        }
        docFlowDTO.setSchoolId(getSessionValue().getSchoolId());
        ObjectId id=docFlowService.addDocFlow(docFlowDTO);
        map.put("code", Constant.SUCCESS_CODE);

        ExpLogType expLogType = ExpLogType.DOC_FLOW;
        if (experienceService.updateScore(userId.toString(), expLogType, id.toString())) {
            map.put("scoreMsg", expLogType.getDesc());
            map.put("score", expLogType.getExp()+"");
        }
        return map;
    }

    public List<String> setUnreadList(ObjectId userId, List<String> unReadList) {
        //未读加入本校校长以及管理员
        List<UserEntry> userEntryList = docFlowService.getSchoolLeader(userId);
        if (userEntryList != null && !userEntryList.isEmpty()) {
            for (UserEntry userEntry : userEntryList) {
                int role = userEntry.getRole();
                if (UserRole.isHeadmaster(role) || UserRole.isManager(role)) {
                    if (!unReadList.contains(userEntry.getID().toString()))
                        unReadList.add(userEntry.getID().toString());
                }
            }
        }
        return unReadList;
    }

    /**
     * 获取公文详情
     *
     * @param docId 公文Id
     * @return 公文实体类
     */
    @RequestMapping("/getdocdetail")
    @ResponseBody
    public DocFlowDTO getDocDetail(@RequestParam String docId) {
        DocFlowDTO docFlowDTO = docFlowService.getDocDetail(new ObjectId(docId));
        if (docFlowDTO != null)//填充部门名、用户姓名
        {
            List<ObjectId> departmentIds = new ArrayList<ObjectId>();
            List<ObjectId> userIds = new ArrayList<ObjectId>();
            //收集部门以及用户id
            departmentIds.add(new ObjectId(docFlowDTO.getDepartmentId()));
            userIds.add(new ObjectId(docFlowDTO.getUserId()));
            for(String uid:docFlowDTO.getPublishList())
            {
                userIds.add(new ObjectId(uid));
            }
            List<DocCheckDTO> docCheckDTOs = docFlowDTO.getCheckDTOList();
            if (docCheckDTOs != null && !docCheckDTOs.isEmpty()) {
                for (DocCheckDTO docCheckDTO : docCheckDTOs) {
                    departmentIds.add(new ObjectId(docCheckDTO.getDepartmentId()));
                    userIds.add(new ObjectId(docCheckDTO.getUserId()));
                }
            }
            List<IdUserFilePairDTO> fileList = docFlowDTO.getDocList();
            if (fileList != null && !fileList.isEmpty()) {
                for (IdUserFilePairDTO file : fileList) {
                    userIds.add(new ObjectId(file.getUserId()));
                }
            }
            Map<String, String> departmentMap = docFlowService.getDepartmentsByDepIds(departmentIds);
            Map<String, String> userMap = docFlowService.getUserNameByIds(userIds);
            //设置部门以及用户名称
            docFlowDTO.setDepartmentName(departmentMap.get(docFlowDTO.getDepartmentId()));//设置部门名
            docFlowDTO.setTime(new SimpleDateFormat("yyyy年MM月dd日").format(new Date(new ObjectId(docFlowDTO.getId()).getTime())));//设置日期
            docFlowDTO.setUserName(userMap.get(docFlowDTO.getUserId()));
            if (docCheckDTOs != null && !docCheckDTOs.isEmpty()) {//审核部分
                for (int i = 0; i < docCheckDTOs.size(); i++) {
                    DocCheckDTO docCheckDTO = docCheckDTOs.get(i);
                    if (docCheckDTO.getOpinion() == -1) {
                        docCheckDTOs.remove(docCheckDTO);
                        i--;
                    } else {
                        String departmentName=departmentMap.get(docCheckDTO.getDepartmentId());
                        docCheckDTO.setDepartmentName(departmentName != null && !departmentName.equals("")?departmentName:"---");//设置部门名
                        docCheckDTO.setUserName(userMap.get(docCheckDTO.getUserId()));//设置姓名
                        docCheckDTO.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(new ObjectId(docCheckDTO.getId()).getTime())));//审阅时间
                    }
                }
            }
            List<String> publishNames=new ArrayList<String>();
            for(String uid:docFlowDTO.getPublishList())
            {
                publishNames.add(userMap.get(uid));
            }
            if (fileList != null && !fileList.isEmpty()) {//设置附件上传者姓名
                for (IdUserFilePairDTO file : fileList) {
                    file.setUserName(userMap.get(file.getUserId()));
                }
            }
            docFlowDTO.setPublishNames(publishNames);
            Collections.sort(docCheckDTOs, new Comparator<DocCheckDTO>() {
                public int compare(DocCheckDTO arg0, DocCheckDTO arg1) {
                    return arg0.getId().compareTo(arg1.getId());
                }
            });
            if(UserRole.isEducation(getSessionValue().getUserRole()))
            {
                docFlowDTO.setDepartmentName(educationBureauService.getEducationByEduUserId(getUserId()).getEducationName());
            }
            if(docFlowDTO.getDepartmentName()==null) {
                Map<String, Object> map = new HashMap<String, Object>();
                EducationBureauEntry educationBureauEntry=educationBureauService.selEducationById(docFlowDTO.getDepartmentId());
                if(educationBureauEntry!=null)
                    docFlowDTO.setDepartmentName(educationBureauEntry.getEducationName());
            }
        }
        return docFlowDTO;
    }

    /**
     * 提交公文审核
     *
     * @param docId               公文id
     * @param handleType          处理意见
     * @param receiveId           下一位接受者，无则传递""
     * @param receiveDepartmentId 下一位接受者部门ID，无则传递""
     * @param remark              备注说明
     * @param docNames
     * @param docAddress
     * @return true/false
     */
    @RequestMapping("/checkdoc")
    @ResponseBody
    public boolean checkDoc(@RequestParam String docId, @RequestParam int handleType,
                            @RequestParam String receiveId, @RequestParam String receiveDepartmentId,
                            @RequestParam String remark, @RequestParam String docNames,
                            @RequestParam String docAddress) {
        ObjectId doc = new ObjectId(docId);
        ObjectId receive = receiveId.equals("") ? null : new ObjectId(receiveId);
        ObjectId receiveDepartment = receiveDepartmentId.equals("") ? null : new ObjectId(receiveDepartmentId);

        List<IdUserFilePairDTO> docList = generalFileList(docNames, docAddress, "", "");
        docFlowService.checkDoc(doc, handleType, receive, receiveDepartment, remark, docList);
        return true;
    }

    /**
     * 生成IdUserFilePairDTO列表
     *
     * @param docNames
     * @param docValues
     * @return
     */
    public List<IdUserFilePairDTO> generalFileList(String docNames, String docValues, String docIds, String userIds) {
        List<IdUserFilePairDTO> docList = new ArrayList<IdUserFilePairDTO>();
        ObjectId userId = getUserId();
        if (StringUtils.isNotBlank(docNames)) {
            String[] fileIdArr = new String[docNames.length()];
            for (int i = 0; i < fileIdArr.length; i++) {
                fileIdArr[i] = "-1";
            }
            String[] fileUserIdArr = new String[docNames.length()];
            if (StringUtils.isNotBlank(docIds)) {
                fileIdArr = StringUtils.split(docIds, Constant.COMMA);
                fileUserIdArr = StringUtils.split(userIds, Constant.COMMA);
            }
            String[] filenameArr = StringUtils.split(docNames, Constant.COMMA);
            String[] fileValueArr = StringUtils.split(docValues, Constant.COMMA);
            for (int i = 0; i < filenameArr.length; i++) {
                try {
                    String str = filenameArr[i];
                    String fileId = fileIdArr[i];
                    if (fileId.equals("-1")) {
                        IdUserFilePairDTO docPair = new IdUserFilePairDTO(new ObjectId().toString());
                        docPair.setUserId(userId.toString());
                        docPair.setValue(fileValueArr[i]);
                        docPair.setName(str);
                        docList.add(docPair);
                    } else {
                        IdUserFilePairDTO docPair = new IdUserFilePairDTO(fileId);
                        docPair.setUserId(fileUserIdArr[i]);
                        docPair.setValue(fileValueArr[i]);
                        docPair.setName(str);
                        docList.add(docPair);
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
        }
        return docList;
    }

    /**
     * 撤销公文
     *
     * @param docId 公文Id
     * @return true/false
     */
    @RequestMapping("revdoc")
    @ResponseBody
    public boolean revDoc(@RequestParam String docId) {
        String userId=getUserId().toString();
        String currentStateId = new ObjectId().toString();
        DocCheckDTO docCheckDTO = new DocCheckDTO(currentStateId, userId, new ObjectId().toString(), CheckStateEnum.REVOCATE.getIndex(), "校长/管理员撤销");
        return docFlowService.revDoc(new ObjectId(docId),docCheckDTO);
    }

    /**
     * 删除公文
     *
     * @param docId 公文Id
     * @return true/false
     */
    @RequestMapping("deldoc")
    @ResponseBody
    public boolean delDoc(@RequestParam String docId) {
        return docFlowService.delDoc(new ObjectId(docId));
    }

    /**
     * 获取提醒的数据
     * 获取数据包括：未读公文数量、未审阅数量、被驳回到原作者的数量
     *
     * @return
     */
    @RequestMapping("/getpromote")
    @ResponseBody
    public Map<String, Integer> getPromote() {
        ObjectId userId = getUserId();
        String term = getCurrentTerm();
        Map<String, Integer> map = new HashMap<String, Integer>();
        int unReadCount = docFlowService.getUnreadCount(term, userId);
        int unCheckCount = docFlowService.getUnCheckCount(term, userId);
        int rejectCount = docFlowService.getRejectCount(term, userId);
        int all = unReadCount + unCheckCount + rejectCount;
        map.put("unread", unReadCount);
        map.put("uncheck", unCheckCount);
        map.put("promote", rejectCount);
        map.put("all", all);
        return map;
    }

    /**
     * 根据用户id返回其部门列表
     *
     * @return
     */
    @RequestMapping("/getdepartmentlist")
    @ResponseBody
    public List<Map<String, String>> GetDepartmentList() {
        ObjectId userId = getUserId();
        List<Map<String, String>> list = docFlowService.GetDepartmentList(userId);
        return list;
    }

    /**
     * 获取当前学期列表
     *
     * @return
     */
    @RequestMapping("/getcurrentterm")
    @ResponseBody
    public String getCurrentTerm() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        if (month <= 8 && month >= 2) {
            return (year - 1) + "-" + year + "学年第二学期";
        } else if (month > 8) {
            return year + "-" + (year + 1) + "学年第一学期";
        } else {
            return (year - 1) + "-" + year + "学年第一学期";
        }
    }

    /**
     * 获取学期列表，从2014年开始，最多显示十年
     *
     * @return
     */
    @RequestMapping("/getallterm")
    @ResponseBody
    public List<String> getAllTerm() {
        List<String> allTerm = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int currentYear;
        int startYear = 2014;
        int currentTerm;
        if (month <= 8 && month >= 2) {
            currentYear = year - 1;
            currentTerm = 2;
        } else if (month > 8) {
            currentYear = year;
            currentTerm = 1;
        } else {
            currentYear = year - 1;
            currentTerm = 1;
        }
        startYear = currentYear - startYear > 10 ? currentYear - 10 : startYear;
        for (int i = startYear; i <= currentYear; i++) {
            if (i != currentYear)//前些年
            {
                allTerm.add(i + "-" + (i + 1) + "学年第一学期");
                allTerm.add(i + "-" + (i + 1) + "学年第二学期");
            } else//当前学年
            {
                allTerm.add(i + "-" + (i + 1) + "学年第一学期");
                if (currentTerm == 2)
                    allTerm.add(i + "-" + (i + 1) + "学年第二学期");
            }
        }
        Collections.reverse(allTerm);
        return allTerm;
    }

    /**
     * 获取部门及学科人员
     *
     * @return
     */
    @RequestMapping("/getdepartmentandsubjectusers")
    @ResponseBody
    public Map<String, List<UserGroupInfo<IdNameValuePairDTO>>> getDepartmentAndSubjectUsers() {
        Map<String, List<UserGroupInfo<IdNameValuePairDTO>>> map = new HashMap<String, List<UserGroupInfo<IdNameValuePairDTO>>>();
        //部门的
        List<UserGroupInfo<IdNameValuePairDTO>> retList = new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = null;

        retMap = userService.getDepartmemtMembersMap(new ObjectId(getSessionValue().getSchoolId()));
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entryMaps : retMap.entrySet()) {
            retList.add(new UserGroupInfo<IdNameValuePairDTO>(entryMaps.getKey(), new ArrayList<IdNameValuePairDTO>(entryMaps.getValue())));
        }
        map.put("department", retList);
        //学科
        retList = new ArrayList<UserGroupInfo<IdNameValuePairDTO>>();
        retMap = userService.getTeacherMap(new ObjectId(getSessionValue().getSchoolId()), 1);
        for (Map.Entry<IdNameValuePairDTO, Set<IdNameValuePairDTO>> entryMaps : retMap.entrySet()) {
            retList.add(new UserGroupInfo<IdNameValuePairDTO>(entryMaps.getKey(), new ArrayList<IdNameValuePairDTO>(entryMaps.getValue())));
        }
        map.put("subject", retList);
        return map;
    }
    //获取教育局下所有学校
    @RequestMapping("/getSchoolsByEdu")
    @ResponseBody
    public List<SimpleSchoolDTO> getSchoolsByEdu()
    {
        return docFlowService.findAllSchoolList(getUserId().toString());
    }
    //获取教育局下所有学校校长
    @RequestMapping("/getSchoolMastersByEdu")
    @ResponseBody
    public List<SimpleUserInfo> getSchoolMastersByEdu()
    {
        return docFlowService.findAllHeadMasterList(getUserId().toString());
    }
    //附件上传
    @RequestMapping("/doc/upload")
    @ResponseBody
    public RespObj uploadDocFile(MultipartRequest request,HttpServletRequest req){
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            try
            {
                ObjectId id =new ObjectId();

                String fileKey = id.toString()+Constant.POINT+ FilenameUtils.getExtension(file.getOriginalFilename());

                logger.info("User:["+getUserId()+"] try upload file:"+fileKey);

                InputStream inputStream =file.getInputStream();

                QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);

                String path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                FileUploadDTO dto =new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
                fileInfos.add(dto);
            }catch(Exception ex)
            {
                logger.error("", ex);
            }
        }
        RespObj obj =new RespObj(Constant.SUCCESS_CODE,fileInfos);
        return obj;
    }
}
