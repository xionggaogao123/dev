package com.fulaan.upload.upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.BaseController;
import com.fulaan.screenshot.Encoder;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.app.FileUploadDTO;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 普通的文件，视频上传
 * Created by qinbo on 15/5/13.
 */
@Api(value="普通的文件，视频上传",hidden = true)
@Controller
@RequestMapping("/web/commonupload")
public class WebCommonUploadController extends BaseController {

    private static final Logger logger = Logger.getLogger(WebCommonUploadController.class);
    
    public final static List<String> VideoList = new ArrayList<String>(Arrays.asList("avi","wmv","mpeg","mp4","mov","mkv","flv","f4v","m4v","rmvb","rm","3gp","dat","ts","mts","vob"));


    private VideoService videoService = new VideoService();

    @ApiOperation(value = "uploadVideo", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/video")
    @ResponseBody
    @SessionNeedless
    public Map<String, Object> uploadVideo(HttpServletRequest request) throws Exception {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        List<String> list = new ArrayList<String>(fileMap.keySet());
        MultipartFile file = fileMap.get(list.get(0));

        String fileName = FilenameUtils.getName(file.getOriginalFilename());

        String videoFilekey = new ObjectId().toString();

        File savedFile = File.createTempFile(videoFilekey, FilenameUtils.getExtension(fileName));
        OutputStream stream = new FileOutputStream(savedFile);
        stream.write(file.getBytes());
        stream.flush();
        stream.close();

        String coverImage = new ObjectId().toString();
        Encoder encoder = new Encoder();
        File screenShotFile = File.createTempFile(coverImage, ".jpg");
        //是否生成了图片
        boolean isCreateImage = false;
        try {
            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            isCreateImage = true;
        } catch (Exception ex) {
        }

        //开始上传

        //上传图片
        String imgUrl = null;
        if (isCreateImage && screenShotFile.exists()) {
            QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);

            imgUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage);
        }

        VideoEntry ve = new VideoEntry(fileName, file.getSize(), VideoSourceType.USER_VIDEO.getType(), videoFilekey);
        ve.setVideoSourceType(VideoSourceType.VOTE_VIDEO.getType());
        ve.setID(new ObjectId());
        QiniuFileUtils.uploadVideoFile(ve.getID(), videoFilekey, file.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);
        String url = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, videoFilekey);

        if (isCreateImage && screenShotFile.exists()) {
            ve.setImgUrl(imgUrl);
        }

        ObjectId videoId = videoService.addVideoEntry(ve);


        Map<String, Object> retMap = new HashMap<String, Object>();

        VideoDTO videoDTO = new VideoDTO(ve);
        videoDTO.setId(videoId.toString());
        videoDTO.setImageUrl(ve.getImgUrl());
        videoDTO.setUrl(url);

        retMap.put("uploadType", "视频上传成功！");
        retMap.put("result", true);
        retMap.put("videoInfo", videoDTO);


        return retMap;

    }

    @ApiOperation(value = "uploadBase64Image", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = Map.class)})
    @RequestMapping("/base64image")
    @ResponseBody
    public Map<String, Object> uploadBase64Image(String base64ImgData, HttpServletRequest req) throws Exception {


        BASE64Decoder d = new BASE64Decoder();
        byte[] bs = d.decodeBuffer(base64ImgData);
        String filekey = new ObjectId() + ".png";

        InputStream byteStream = new ByteArrayInputStream(bs);
        String parentPath = req.getServletContext().getRealPath("/upload") + "/homework";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        req.getServletContext().getRealPath("/upload");
        String urlPath = "/upload/homework/" + filekey;
        File attachFile = new File(parentFile, filekey);
        try {
            FileUtils.copyInputStreamToFile(byteStream, attachFile);
        } catch (Exception ioe) {

        }

        Map<String, Object> retMap = new HashMap<String, Object>();

        retMap.put("name", filekey);
        retMap.put("path", urlPath);
        return retMap;
    }

    /**
     * 一般性文档上传
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "一般性文档上传", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/doc/upload")
    @ResponseBody
    @SessionNeedless
    public RespObj uploadDocFile(HttpServletRequest request) {
        logger.info("=========upload");
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
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

                    logger.info("User:[" + getUserId() + "] try upload file:" + fileKey);


                    InputStream inputStream = new FileInputStream(destFile);
                    String extName = FilenameUtils.getExtension(file.getOriginalFilename());
                    String path = "";
                    if (extName.equalsIgnoreCase("amr")) {
                        String saveFileKey = new ObjectId().toString() + ".mp3";
                        com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
                        path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
                    } /*else if(VideoList.contains(extName.toLowerCase())) {
                        QiniuFileUtils.uploadVideoFileCkzl(fileKey, inputStream, QiniuFileUtils.TYPE_VIDEO);
                        path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_VIDEO, fileKey);
                    } */else {
                        QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);
                        path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                    } 

                    FileUploadDTO dto = new FileUploadDTO(id.toString(), fileKey, file.getOriginalFilename(), path);
                    fileInfos.add(dto);
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
        }
        RespObj obj = new RespObj(Constant.SUCCESS_CODE, fileInfos);
        return obj;
    }


    /**
     * 文件下载
     *
     * @param type
     * @param fileKey
     * @param response
     * @return
     */
    @ApiOperation(value = "文件下载", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/doc/down")
    public String downFile(int type, String fileKey, HttpServletResponse response, @RequestParam(required = false, defaultValue = "") String fileName) {
        try {
            if (fileName.equals("")) {
                fileName = fileKey;
            }

            String qiniuPath = QiniuFileUtils.getPath(type, fileKey);
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));

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
            } catch (IOException ex) {
                logger.error("", ex);
            }
        } catch (Exception e) {

        }

        return null;
    }


}