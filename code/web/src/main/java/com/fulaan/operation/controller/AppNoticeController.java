package com.fulaan.operation.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.operation.dto.AppNoticeDTO;
import com.fulaan.operation.dto.AppOperationDTO;
import com.fulaan.operation.dto.WebAppNoticeDTO;
import com.fulaan.operation.service.AppNoticeService;
import com.fulaan.pojo.User;
import com.fulaan.screenshot.Encoder;
import com.fulaan.screenshot.EncoderException;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scott on 2017/9/22.
 */
@Api(value = "通知相关接口")
@Controller
@RequestMapping("/web/appNotice")
public class AppNoticeController extends BaseController {

    @Autowired
    private AppNoticeService appNoticeService;

    @Autowired
    private VideoService videoService;

    @RequestMapping("/getReadNoticeUserList")
    @ResponseBody
    @ApiOperation(value = "查询已阅和未阅列表", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getReadNoticeUserList(@ObjectIdType ObjectId id){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,List<User>> userList=appNoticeService.getReadNoticeUserList(id);
            respObj.setMessage(userList);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @RequestMapping("/removeAppNoticeEntry")
    @ResponseBody
    @ApiOperation(value = "删除通知信息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "删除通知信息已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj removeAppNoticeEntry(@ObjectIdType ObjectId noticeId){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.removeAppNoticeEntry(noticeId,getUserId());
            respObj.setMessage("删除通知信息成功！");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @RequestMapping(value="/saveWebAppNoticeEntry", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @ApiOperation(value = "保存通知信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj saveWebAppNoticeEntry(@RequestBody WebAppNoticeDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.saveAppNoticeEntry(new AppNoticeDTO(dto),getUserId());
            respObj.setMessage("保存通知信息成功！");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            // if("推送失败".equals(e.getMessage())) {
            if(e.getMessage().contains("特殊")) {
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage(e.getMessage().replace("特殊",""));
            }else{
                respObj.setErrorMessage(e.getMessage());
            }
        }
        return respObj;
    }


    @RequestMapping(value="/saveAppNoticeEntry", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @ApiOperation(value = "保存通知信息", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj saveAppNoticeEntry(@RequestBody AppNoticeDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.saveAppNoticeEntry(dto,getUserId());
            respObj.setMessage("保存通知信息成功！");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            if("推送失败".equals(e.getMessage())) {
                respObj.setCode(Constant.SUCCESS_CODE);
                respObj.setMessage("推送失败");
            }else{
                respObj.setErrorMessage(e.getMessage());
            }
        }
        return respObj;
    }


    @RequestMapping("/getMySendAppNoticeDtos")
    @ResponseBody
    @ApiOperation(value = "获取我发送的通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMySendAppNoticeDtos(
            @RequestParam(required = false, defaultValue = "")String communityId,
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "")String title,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
            ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object>  retMap=appNoticeService.getMySendAppNoticeDtos(title, communityId,subjectId,getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @RequestMapping("/getMyGatherNotice")
    @ResponseBody
    @ApiOperation(value = "获取我汇总的所有通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMyGatherNotice(
            @RequestParam(required = false, defaultValue = "")String communityId,
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "")String title,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object>  retMap=appNoticeService.getMyAppNotices(title, communityId,subjectId,getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/getMyReceivedAppNoticeDtosForParent")
    @ResponseBody
    @ApiOperation(value = "获取我接收到的通知(家长)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMyReceivedAppNoticeDtosForParent(
            @RequestParam(required = false, defaultValue = "")String communityId,
            @RequestParam(required = false, defaultValue = "")String subjectId,
            @RequestParam(required = false, defaultValue = "")String title,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appNoticeService.getMyReceivedAppNoticeDtos(title, communityId,subjectId,getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    @RequestMapping("/pushRead")
    @ResponseBody
    @ApiOperation(value = "阅读该通知消息", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj pushRead(
           @ObjectIdType ObjectId id
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            appNoticeService.pushRead(id,getUserId());
            respObj.setMessage("阅读成功!");
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }

    @ApiOperation(value = "添加通知评论", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value="/addOperationEntry")
    @ResponseBody
    public String addOperationEntry(@RequestParam(required = false,defaultValue = "") String contactId,
                                    @RequestParam(required = false,defaultValue = "0") int role,
                                    @RequestParam(required = false,defaultValue = "") String backId,
                                    @RequestParam(required = false,defaultValue = "") String parentId,
                                    @RequestParam(required = false,defaultValue = "") String description){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            AppOperationDTO dto=new AppOperationDTO();
            dto.setContactId(contactId);
            dto.setRole(role);
            dto.setBackId(backId);
            dto.setParentId(parentId);
            dto.setDescription(description);
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(1);
            String result = appNoticeService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业评论失败!");

        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加通知评论
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加通知评论1", httpMethod = "POST", produces = "application/json")
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value="/addOperationEntry2")
    @ResponseBody
    public String addOperationEntry2(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为通知id，role为2学生评论区，role为1家长评论区") @RequestBody AppOperationDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(1);
            String result = appNoticeService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业评论失败!");

        }
        return JSON.toJSONString(respObj);
    }

    /**
     * 添加通知二级评论
     */
    @ApiOperation(value="添加二级回复",httpMethod = "POST",produces = "application/json")
    @ApiResponse(code=200,message = "success", response = String.class)
    @RequestMapping("/addSecondOperation")
    @ResponseBody
    public String addSecondOperation(@ApiParam(value = "parentId为上级评论id,backId为回复的对象id,contactId为通知id，role为1家长评论区，role为2学生评论区") @RequestBody AppOperationDTO dto){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try {
            respObj.setCode(Constant.SUCCESS_CODE);
            dto.setUserId(getUserId().toString());
            dto.setLevel(2);
            String result = appNoticeService.addOperationEntry(dto);
            respObj.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setErrorMessage("添加作业二级评论失败!");

        }
        return JSON.toJSONString(respObj);

    }


    @RequestMapping("/getMyReceivedAppNoticeDtosForStudent")
    @ResponseBody
    @ApiOperation(value = "获取我接收到的通知(学生)", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class),
            @ApiResponse(code = 500, message = "服务器不能完成请求")})
    public RespObj getMyReceivedAppNoticeDtosForStudent(
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            Map<String,Object> retMap=appNoticeService.getMyReceivedAppNoticeDtosForStudent(getUserId(),page,pageSize);
            respObj.setMessage(retMap);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }


    @RequestMapping("/searchAppNotice")
    @ResponseBody
    @ApiOperation(value = "搜索合适条件的通知", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "搜索合适条件的通知成功",response = RespObj.class),
            @ApiResponse(code = 500, message = "搜索合适条件的通知失败")})
    public RespObj searchAppNotice(
            @RequestParam(required = true, defaultValue = "")String keyWord,
            @RequestParam(required = false, defaultValue = "1")int page,
            @RequestParam(required = false, defaultValue = "10")int pageSize
    ){
        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        try{
            List<AppNoticeDTO> dtos=appNoticeService.searchAppNotice(keyWord,getUserId(),page,pageSize);
            respObj.setMessage(dtos);
            respObj.setCode(Constant.SUCCESS_CODE);
        }catch (Exception e){
            respObj.setErrorMessage(e.getMessage());
        }
        return respObj;
    }



    @ApiOperation(value = "上传视频", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = MultipartFile.class)})
    @RequestMapping("/video/uploadVideo")
    @ResponseBody
    @SessionNeedless
    public RespObj uploadVideo(@RequestParam("Filedata") MultipartFile Filedata) throws IllegalParamException, IllegalStateException, IOException, EncoderException {

        RespObj respObj=new RespObj(Constant.FAILD_CODE);
        Map map = new HashMap();
        String fileName = FilenameUtils.getName(Filedata.getOriginalFilename());

        String videoFilekey = new ObjectId().toString();

        File savedFile = File.createTempFile(videoFilekey, FilenameUtils.getExtension(fileName));
        OutputStream stream = new FileOutputStream(savedFile);
        stream.write(Filedata.getBytes());
        stream.flush();
        stream.close();

        String coverImage = new ObjectId().toString();
        Encoder encoder = new Encoder();
        File screenShotFile = File.createTempFile("coverImage", ".jpg");

        //是否生成了图片
        boolean isCreateImage = false;
        try {
            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            isCreateImage = true;
        } catch (Exception ex) {
            respObj.setMessage(ex.getMessage());
            return respObj;
        }

        //开始上传

        //上传图片
        String imgUrl = null;
        if (isCreateImage && screenShotFile.exists()) {
            QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);

            imgUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage);
        }

        VideoEntry ve = new VideoEntry(fileName, Filedata.getSize(), VideoSourceType.USER_VIDEO.getType(), videoFilekey);
        ve.setVideoSourceType(VideoSourceType.VOTE_VIDEO.getType());
        ve.setID(new ObjectId());
        QiniuFileUtils.uploadVideoFile(ve.getID(), videoFilekey, Filedata.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
        String url = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, videoFilekey);

        if (isCreateImage && screenShotFile.exists()) {
            ve.setImgUrl(imgUrl);
        }

        ObjectId videoId = videoService.addVideoEntry(ve);

        //删除临时文件
        try {
            savedFile.delete();
            screenShotFile.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
            respObj.setMessage(ex.getMessage());
            return respObj;
        }

        map.put("flg", true);
        map.put("vimage", QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage));
        map.put("vid", videoId.toString());
        map.put("vurl", QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, ve.getBucketkey()));
        respObj.setCode(Constant.SUCCESS_CODE);
        respObj.setMessage(map);
        return respObj;
    }

    @ApiOperation(value = "上传图片", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping(value = "/uploadImage", produces = "text/html; charset=utf-8")
    @ResponseBody
    public RespObj addBlogPic(MultipartRequest request) {
        RespObj  respObj=new RespObj(Constant.FAILD_CODE);
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String examName = getFileName(file);
                RespObj uploadFileRespObj = QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                if (uploadFileRespObj.getCode() != Constant.SUCCESS_CODE) {
                    throw new FileUploadException();
                }
                respObj.setMessage(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, examName));
            }
            respObj.setCode(Constant.SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("上传失败");
        }
        return respObj;
    }

    //得到文件名
    private String getFileName(MultipartFile file) {
        String orgname = file.getOriginalFilename();
        return new ObjectId().toString() + Constant.POINT + orgname.substring(orgname.lastIndexOf(".") + 1);
    }


    @ApiOperation(value = "一般性文档上传", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/doc/upload")
    @ResponseBody
    @SessionNeedless
    public RespObj uploadDocFile(HttpServletRequest request) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        Map<String,Object> result = new HashMap<String,Object>();
        if (multipartResolver.isMultipart(request)) { //
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multiRequest.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                try {
                    String dirPath = request.getServletContext().getRealPath("/upload");
                    File fileDir = new File(dirPath);
                    if (!fileDir.exists()) {
                        fileDir.mkdir();
                    }

                    File destFile = new File(fileDir, file.getOriginalFilename());
                    if (!destFile.exists()) {
                        destFile.createNewFile();
                    }
                    file.transferTo(destFile);

                    ObjectId id = new ObjectId();

                    String fileKey = id.toString() + Constant.POINT + FilenameUtils.getExtension(file.getOriginalFilename());


                    InputStream inputStream = new FileInputStream(destFile);
                    String extName = FilenameUtils.getExtension(file.getOriginalFilename());
                    String path = "";
                    if (extName.equalsIgnoreCase("amr")) {
                        String saveFileKey = new ObjectId().toString() + ".mp3";
                        com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
                        path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
                    } else {
                        QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);
                        path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                    }

                    result.put("fileName", file.getOriginalFilename());
                    result.put("path", path);
                    respObj.setCode(Constant.SUCCESS_CODE);
                    respObj.setMessage(result);
                } catch (Exception ex) {
                    respObj.setMessage(ex.getMessage());
                }
            }
        }
        return respObj;
    }

}
