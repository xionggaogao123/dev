package com.fulaan.commonupload.controller;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.fulaan.base.controller.BaseController;
import com.fulaan.screenshot.Encoder;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.video.service.VideoService;
import com.pojo.app.FileUploadDTO;
import com.pojo.video.VideoDTO;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoSourceType;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import com.sys.utils.RespObj;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;



import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 普通的文件，视频上传
 * Created by qinbo on 15/5/13.
 */

@Controller
@RequestMapping("/commonupload")
public class CommonUploadController extends BaseController {

	private static final Logger logger =Logger.getLogger(CommonUploadController.class);
	
	
    private VideoService videoService = new VideoService();

    @RequestMapping("/video")
    @ResponseBody
    public Map<String, Object> uploadVideo(HttpServletRequest req,
                                           @RequestParam("Filedata") MultipartFile file, @RequestParam("type") String type) throws Exception {


        String fileName = FilenameUtils.getName(file.getOriginalFilename());


        String videoFilekey = new ObjectId().toString() + Constant.POINT + FilenameUtils.getExtension(fileName);
        String bathPath = Resources.getProperty("upload.file");
        File dir = new File(bathPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File savedFile = new File(bathPath, videoFilekey);
        OutputStream stream = new FileOutputStream(savedFile);
        stream.write(file.getBytes());
        stream.flush();
        stream.close();

        String coverImage = new ObjectId().toString() + ".jpg";
        Encoder encoder = new Encoder();
        File screenShotFile = new File(bathPath, coverImage);
        //是否生成了图片
        boolean isCreateImage = false;
        try {
            encoder.getImage(savedFile, screenShotFile, 1, 480, 270);
            isCreateImage = true;
        } catch (Exception ex) {
            //logger.error("", ex);
        }

        //开始上传

        //上传图片
        String imgUrl = null;
        if (isCreateImage&&screenShotFile.exists()) {
            QiniuFileUtils.uploadFile(coverImage, new FileInputStream(screenShotFile), QiniuFileUtils.TYPE_IMAGE);

            imgUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, coverImage);
        }

        VideoEntry ve = new VideoEntry(fileName, file.getSize(), VideoSourceType.USER_VIDEO.getType(), videoFilekey);
        ve.setVideoSourceType(VideoSourceType.VOTE_VIDEO.getType());
        ve.setID(new ObjectId());
        QiniuFileUtils.uploadVideoFile(ve.getID(), videoFilekey, file.getInputStream(), QiniuFileUtils.TYPE_USER_VIDEO);


        if (isCreateImage&&screenShotFile.exists()) {
            ve.setImgUrl(imgUrl);
        }

        ObjectId videoId = videoService.addVideoEntry(ve);


        Map<String, Object> retMap = new HashMap<String, Object>();

        VideoDTO videoDTO = new VideoDTO(ve);
        videoDTO.setId(videoId.toString());
        videoDTO.setImageUrl(ve.getImgUrl());

        retMap.put("uploadType", "视频上传成功！");
        retMap.put("result", true);
        retMap.put("videoInfo", videoDTO);


        return retMap;

    }

    @RequestMapping("/base64image")
    @ResponseBody
    public Map<String, Object> uploadBase64Image(String base64ImgData,HttpServletRequest req)throws Exception {



        BASE64Decoder d = new BASE64Decoder();
        byte[] bs = d.decodeBuffer(base64ImgData);
        String filekey = new ObjectId()+".png";

        InputStream byteStream = new ByteArrayInputStream(bs);
        String parentPath = req.getServletContext().getRealPath("/upload")+"/homework";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        req.getServletContext().getRealPath("/upload");
        String urlPath = "/upload/homework/"+filekey;
        File attachFile =new File(parentFile, filekey);
        try {
            FileUtils.copyInputStreamToFile(byteStream, attachFile);
        }
        catch (Exception ioe){

        }

        Map<String, Object> retMap = new HashMap<String, Object>();

        retMap.put("name",filekey);
        retMap.put("path",urlPath);
        return retMap;
    }
    
    /**
     * 一般性文档上传
     * 
     * @param request
     * @param type 
     * @return
     */
    @RequestMapping("/doc/upload")
    @ResponseBody
    public RespObj uploadDocFile(MultipartRequest request,HttpServletRequest req,
                                 @RequestParam(required = false, defaultValue = "0") int useName){
        List<FileUploadDTO> fileInfos = new ArrayList<FileUploadDTO>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
        	try
        	{
        		String dirPath=req.getServletContext().getRealPath("/upload/commondoc");
        		File fileDir =new File(dirPath);
        		if(!fileDir.exists())
        		{
        			fileDir.mkdir();
        		}
        		
        		File destFile=new File(fileDir, file.getOriginalFilename());
        		file.transferTo(destFile);
        		
        		ObjectId id =new ObjectId();
        		
	            String fileKey = id.toString()+Constant.POINT+FilenameUtils.getExtension(file.getOriginalFilename());
                if(useName == 1){
                    String fileName = file.getOriginalFilename();
                    fileKey = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + id.toString() + "." + FilenameUtils.getExtension(fileName);
                }
	            
	            logger.info("User:["+getUserId()+"] try upload file:"+fileKey);
	            
	            
	            
	            
	            InputStream inputStream =new FileInputStream(destFile);
	            
	            
	            String extName =FilenameUtils.getExtension(file.getOriginalFilename());
	            String path="";
	            if(extName.equalsIgnoreCase("amr"))
	            {
	            	String saveFileKey=new ObjectId().toString()+".mp3";
	            	com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
	            	path =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
	            }
	            else
	            {
	            	QiniuFileUtils.uploadFile(fileKey,inputStream,QiniuFileUtils.TYPE_DOCUMENT);
		            path  =QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
	            }
	            
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
    
    
    /**
     * 文件下载
     * @param type
     * @param fileKey
     * @param response
     * @return
     */
    @RequestMapping("/doc/down")
    public String downFile(int type,String fileKey,HttpServletResponse response, @RequestParam(required = false, defaultValue = "") String fileName)
    {
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
        }catch (Exception e){
            
        }
        
        return null;
    }
    
    


}