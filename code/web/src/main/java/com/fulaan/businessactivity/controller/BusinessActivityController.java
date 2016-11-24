package com.fulaan.businessactivity.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.businessactivity.dto.FieryActivityDTO;
import com.fulaan.businessactivity.service.FieryActivityService;
import com.fulaan.educationbureau.dto.EducationBureauDTO;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.utils.CommonUtils;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.pojo.KeyValue;
import com.mongodb.BasicDBObject;
import com.pojo.app.FileUploadDTO;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.PageDTO;
import com.pojo.businessactivity.FieryActivityEntry;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.exceptions.UnLoginException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by fourer on 15-2-26.
 */
@Controller
@RequestMapping("/business")
public class BusinessActivityController  extends BaseController{

    private static final Logger logger =Logger.getLogger(BusinessActivityController.class);

    @Autowired
    private FieryActivityService fieryActivityService;

    @Autowired
    private EducationBureauService educationBureauService;
    
    private SchoolService schoolService =new SchoolService();

    @RequestMapping("/fiery")
    public String replyLetterPage(String replyId){
        return "businessactivity/fiery";
    }

    /**
     * 跳转火热活动列表页面
     * @param model
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     * @throws com.sys.exceptions.UnLoginException
     * @throws com.sys.exceptions.PermissionUnallowedException
     */
    @RequestMapping("/fieryactivitylist")
    public String fieryactivitylist(@RequestParam(required=false,defaultValue="1") Integer page,Map<String, Object> model) throws ResultTooManyException, UnLoginException
    {
        PageDTO<FieryActivityDTO> dto =getFieryActivitys((page - 1) * Constant.TWELVE, Constant.TWELVE);
        //PageDTO<FieryActivityDTO> dto =getFieryActivitys(0, 0);
        model.put("dto", dto);
        model.put("pages", (int)Math.ceil((Double.valueOf(dto.getCount()))/Constant.TWELVE));
        model.put("pageIndex", page);
        boolean is= UserRole.isStudentOrParent(getSessionValue().getUserRole());
        model.put("role", is?Constant.ZERO:Constant.ONE);
        return "businessactivity/fieryactivitylist";
    }

    /**
     * 火热活动列表
     * @param page
     * @return
     * @throws com.sys.exceptions.ResultTooManyException
     * @throws com.sys.exceptions.UnLoginException
     * @throws com.sys.exceptions.PermissionUnallowedException
     */
    @RequestMapping("/phonefieryactivitylist")
    @ResponseBody
    public Map<String, Object> phonefieryactivitylist(@RequestParam(required=false,defaultValue="1") Integer page) throws ResultTooManyException, UnLoginException
    {
        Map<String, Object> model=new HashMap<String, Object>();
        PageDTO<FieryActivityDTO> dto =getFieryActivitys((page - 1) * Constant.TWELVE, Constant.TWELVE);
        model.put("dto", dto);
        model.put("pages", (int)Math.ceil((Double.valueOf(dto.getCount()))/Constant.TWELVE));
        model.put("pageIndex", page);
        boolean is= UserRole.isStudentOrParent(getSessionValue().getUserRole());
        model.put("role", is?Constant.ZERO:Constant.ONE);
        return model;
    }

    /**
     * 火热活动列表
     * @param skip
     * @param limit
     * @return
     * @throws ResultTooManyException
     * @throws com.sys.exceptions.PermissionUnallowedException
     */
    @RequestMapping("/list")
    @ResponseBody
    public PageDTO<FieryActivityDTO> getFieryActivitys(int skip,int limit) throws ResultTooManyException
    {
        int role=getSessionValue().getUserRole();
        ObjectId userId=getUserId();
        String schoolId=getSessionValue().getSchoolId();
        ObjectId eduId = null;
        EducationBureauEntry eduEntry=educationBureauService.selEducationByUserId(userId,role,schoolId);
        if(eduEntry!=null){
            eduId=eduEntry.getID();
        }

        int count=fieryActivityService.getFieryActivityCount(eduId, role);
        List<FieryActivityDTO> list= fieryActivityService.getFieryActivitys(eduId, role,skip, limit);
        return new PageDTO<FieryActivityDTO>(count, list);
    }

    /**
     * 创建火热活动
     * @param model
     * @return
     */
    @RequestMapping("/create")
    public String createFieryActivity(Map<String, Object> model)
    {
        //获取过滤条件角色List
        List<KeyValue> roleList = CommonUtils.enumMapToList(UserRole.getFieryActivityRoleMap());
        model.put("roleList",roleList);
        return "businessactivity/fieryactivitycreate";
    }

    /**
     * 添加火热活动
     * @param title
     * @param content
     * @param docFile
     * @return
     * @throws com.sys.exceptions.IllegalParamException
     */
    @RequestMapping("/add")
    public String addFieryActivity(String title,
                             String content,
                             String beginTime,
                             String endTime,
                             int checkRole,
                             int takeEffect,
                             String picFile,
                             String picName,
                             String phonePicFile,
                             String phonePicName,
                             String docNames,
                             String docFile,
                             String eduIdsStr) throws IllegalParamException
    {
        content= HtmlUtils.delScriptTag(content);
        if(!ValidationUtils.isRequestNoticeName(title))
        {
            throw new IllegalParamException("title is error!!");
        }
        if(!ValidationUtils.isRequestNoticeContent(content))
        {
            throw new IllegalParamException("content is error!!");
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
        int activityType=0;
        int isFinish=0;

        List<ObjectId> eduIds = new ArrayList<ObjectId>();
        int isAll=1;
        if(null!=eduIdsStr&&!"".equals(eduIdsStr)){
            String[] arrEduIds=eduIdsStr.split(",");
            for(String str:arrEduIds){
                eduIds.add(new ObjectId(str));
            }
            isAll=0;
        }

        FieryActivityEntry e =new FieryActivityEntry(title, content, beginTime, endTime, checkRole, takeEffect, picFile, picName,phonePicFile,phonePicName,activityType, isFinish, new Date().getTime(),getUserId(), docList, isAll, eduIds);
        ObjectId id=fieryActivityService.addFieryActivity(e);

        return "redirect:/business/fieryactivitylist.do";
    }

    /**
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
     * 火热活动详情
     * @param id
     * @return
     * @throws com.sys.exceptions.PermissionUnallowedException
     * @throws IllegalParamException
     */
    @RequestMapping("/detail")
    public String fieryActivityDetail(@ObjectIdType ObjectId id,Map<String, Object> model) throws  IllegalParamException
    {
        FieryActivityEntry e =fieryActivityService.getFieryActivityEntry(id);

        FieryActivityDTO dto =new FieryActivityDTO(e);

        for(IdNameValuePairDTO idvpDto:dto.getDocFile())
        {
            idvpDto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, idvpDto.getValue().toString()));
        }
        dto.setPicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPicFile()));
        dto.setPhonePicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPhonePicFile()));
        model.put("dto", dto);
        String url="businessactivity/fieryactivitydetail";
        return url;
    }

    /**
     * 火热活动详情
     * @param id
     * @return
     * @throws com.sys.exceptions.PermissionUnallowedException
     * @throws IllegalParamException
     */
    @RequestMapping("/phonedetail")
    @ResponseBody
    public Map<String, Object> phonefieryActivityDetail(@ObjectIdType ObjectId id) throws  IllegalParamException
    {
        FieryActivityEntry e =fieryActivityService.getFieryActivityEntry(id);
        Map<String, Object> model=new HashMap<String, Object>();
        FieryActivityDTO dto =new FieryActivityDTO(e);

        for(IdNameValuePairDTO idvpDto:dto.getDocFile())
        {
            idvpDto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, idvpDto.getValue().toString()));
        }
        dto.setPicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPicFile()));
        dto.setPhonePicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPhonePicFile()));
        model.put("dto", dto);
        return model;
    }

    /**
     * 火热活动详情
     * @param id
     * @return
     * @throws com.sys.exceptions.PermissionUnallowedException
     * @throws IllegalParamException
     */
    @RequestMapping("/update")
    public String fieryActivityUpdate(@ObjectIdType ObjectId id,Map<String, Object> model) throws  IllegalParamException
    {
        FieryActivityEntry e =fieryActivityService.getFieryActivityEntry(id);
        List<EducationBureauDTO> eduDtos=new ArrayList<EducationBureauDTO>();
        String eduIdsStr="";
        if(e.getEduIds()!=null&&e.getEduIds().size()>0) {
            eduDtos=educationBureauService.findEduInfoByEduIds(e.getEduIds());
            for(EducationBureauDTO eduDto:eduDtos){
                if("".equals(eduIdsStr)){
                    eduIdsStr=eduDto.getId();
                }else{
                    eduIdsStr+=","+eduDto.getId();
                }
            }
        }
        model.put("eduDtos", eduDtos);
        model.put("eduIdsStr", eduIdsStr);
        FieryActivityDTO dto =new FieryActivityDTO(e);

        //for(IdNameValuePairDTO idvpDto:dto.getDocFile())
        //{
            //idvpDto.setValue(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, idvpDto.getValue().toString()));
        //}
        //dto.setPicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPicFile()));
        //dto.setPhonePicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPhonePicFile()));
        model.put("dto", dto);
        //获取过滤条件角色List
        List<KeyValue> roleList = CommonUtils.enumMapToList(UserRole.getFieryActivityRoleMap());
        model.put("roleList",roleList);
        String url="businessactivity/fieryactivityupdate";
        return url;
    }

    /**
     * 获取附件信息
     * @return
     * @throws com.sys.exceptions.IllegalParamException
     */
    @RequestMapping("/getFileInfo")
    @ResponseBody
    public Map<String,FieryActivityDTO> getFileInfo(@ObjectIdType ObjectId id) throws  IllegalParamException
    {
        Map<String,FieryActivityDTO> model = new HashMap<String,FieryActivityDTO>();
        FieryActivityEntry e =fieryActivityService.getFieryActivityEntry(id);

        FieryActivityDTO dto =new FieryActivityDTO(e);
        model.put("dto",dto);
        return model;
    }


    /**
     * 编辑火热活动
     * @param title
     * @param content
     * @param docFile
     * @return
     * @throws com.sys.exceptions.IllegalParamException
     */
    @RequestMapping("/edit")
    public String editFieryActivity(String id,
                                    String title,
                                    String content,
                                    String beginTime,
                                    String endTime,
                                    int checkRole,
                                    int takeEffect,
                                    String picFile,
                                    String picName,
                                    String phonePicFile,
                                    String phonePicName,
                                    String docNames,
                                    String docFile,
                                    String eduIdsStr) throws IllegalParamException
    {
        content= HtmlUtils.delScriptTag(content);
        if(!ValidationUtils.isRequestNoticeName(title))
        {
            throw new IllegalParamException("title is error!!");
        }
        if(!ValidationUtils.isRequestNoticeContent(content))
        {
            throw new IllegalParamException("content is error!!");
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
                        ObjectId dfId =getObjectIdByFilePath(str);
                        IdNameValuePair docPair =new IdNameValuePair(dfId);
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
        List<ObjectId> eduIds = new ArrayList<ObjectId>();
        int isAll=1;
        if(null!=eduIdsStr&&!"".equals(eduIdsStr)){
            String[] arrEduIds=eduIdsStr.split(",");
            for(String str:arrEduIds){
                eduIds.add(new ObjectId(str));
            }
            isAll=0;
        }
        FieryActivityEntry e =new FieryActivityEntry(title, content, beginTime, endTime, checkRole, takeEffect, picFile, picName, phonePicFile, phonePicName, docList, isAll, eduIds);
        fieryActivityService.editFieryActivity(id,e);
        return "redirect:/business/fieryactivitylist.do";
    }


    /**
     * 删除火热活动
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public RespObj deleteFieryActivity(@ObjectIdType ObjectId id)
    {

        RespObj obj=new RespObj(Constant.FAILD_CODE);
        try {
            fieryActivityService.deleteFieryActivity(id);
            return RespObj.SUCCESS;
        } catch (Exception e) {
            obj.setMessage("参数错误");
        }
        return obj;
    }

    /**
     * 文件下载
     * @param id
     * @param docId
     * @return
     * @throws IllegalParamException
     * @throws com.sys.exceptions.PermissionUnallowedException
     * @throws java.io.UnsupportedEncodingException
     */
    @RequestMapping("/doc/down")
    public String downDoc(@ObjectIdType ObjectId id,@ObjectIdType ObjectId docId,HttpServletResponse response) throws IllegalParamException, UnsupportedEncodingException
    {
        FieryActivityEntry e =fieryActivityService.getFieryActivityEntry(id);
        IdNameValuePair pair=null;

        for(IdNameValuePair p:e.getDocFile())
        {
            if(p.getId().equals(docId))
            {
                pair=p;
            }
        }
        if(pair==null)
            throw new IllegalParamException();
        String qiniuPath=QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, pair.getValue().toString());

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader( "Content-Disposition", "attachment;filename=" + new String( pair.getName().getBytes("utf-8"), "ISO8859-1" ) );

        try {
            InputStream inputStream = QiniuFileUtils.downFileByUrl(qiniuPath);
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
     * 上传火热活动广告图片
     * @param request
     * @return
     */
    @RequestMapping("/addFieryActivityPic")
    public @ResponseBody RespObj addFieryActivityPic(MultipartRequest request) {
        //String filePath = FileUtil.SCHOOL_SECURITY;
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {

                ObjectId id =new ObjectId();
                String fileKey = id.toString()+Constant.POINT+FilenameUtils.getExtension(file.getOriginalFilename());
                QiniuFileUtils.uploadFile(fileKey, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                String path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                FileUploadDTO dto =new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
                fileInfos.add(dto);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        RespObj obj =new RespObj(Constant.SUCCESS_CODE,fileInfos);
        return obj;
    }

    /**
     * 火热活动广告图片
     * @param skip
     * @param limit
     * @return
     * @throws ResultTooManyException
     * @throws com.sys.exceptions.PermissionUnallowedException
     */
    @RequestMapping("/getBusinessActivityImage")
    @ResponseBody
    public Map<String,List<FieryActivityDTO>> getBusinessActivityImage(int skip,int limit) throws ResultTooManyException
    {
        Map<String,List<FieryActivityDTO>> map=new HashMap<String, List<FieryActivityDTO>>();
        int role=getSessionValue().getUserRole();
        ObjectId userId=getUserId();
        String schoolId=getSessionValue().getSchoolId();
        ObjectId eduId = null;
        if(StringUtils.isNotBlank(schoolId))
        {
        	SchoolEntry se=schoolService.getSchoolEntry(new ObjectId(schoolId), new BasicDBObject("isf",1));
        	if(null!=se && se.getIsFieryactivity()==1)
        	{
        		 List<FieryActivityDTO> list=new ArrayList<FieryActivityDTO>();
        		 map.put("list",list);
        	     return map;
        	}
        }
        EducationBureauEntry eduEntry=educationBureauService.selEducationByUserId(userId,role,schoolId);
        if(eduEntry!=null){
            eduId=eduEntry.getID();
        }
        List<FieryActivityDTO> list= fieryActivityService.getBusinessActivityImage(eduId, role, skip, limit);
        for(FieryActivityDTO dto:list){
            dto.setPicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPicFile()));
            dto.setPhonePicFile(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, dto.getPhonePicFile()));
        }
        map.put("list",list);
        return map;
    }

    /**
     * 上传图片到七牛
     */
    @RequestMapping("/uploadImage")
    public String uploadImage(HttpServletRequest request, HttpServletResponse response)throws IllegalParamException, FileUploadException, IOException{
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file =multipartRequest.getFile("upfile");
        ReturnFile returnFile = new ReturnFile();
        String fileName = FilenameUtils.getName(file.getOriginalFilename());
        returnFile.setOriginal(fileName);
        returnFile.setTitle(fileName);
        returnFile.setSize(String.valueOf(file.getSize()));
        returnFile.setType(fileName.substring(fileName.lastIndexOf(".")));
        String examName = getFileName(file);
        RespObj upladTestPaper = QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
        if (upladTestPaper.getCode() != Constant.SUCCESS_CODE) {
            throw new FileUploadException();
        }
        returnFile.setState("SUCCESS");
        returnFile.setUrl(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, examName));
        String result = JSON.toJSONString(returnFile);//这边就是为了返回给UEditor做的格式转换
        response.getWriter().write(result);
        return null;
    }

    /**
     * 返回给ueditor的内容
     */
    public static class ReturnFile{
        private String original;
        private String size;
        private String state;
        private String title;
        private String type;
        private String url;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        String orgname = file.getOriginalFilename();

        return new ObjectId().toString()+Constant.POINT+ orgname.substring(orgname.lastIndexOf(".") + 1);
    }

}
