package com.fulaan.video.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.learningcenter.service.CloudResourceService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.video.service.VideoService;
import com.fulaan.video.service.VideoViewRecordService;
import com.pojo.app.IdValuePair;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserRole;
import com.pojo.video.VideoEntry;
import com.pojo.video.VideoViewRecordEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qinbo on 15/5/20.
 */
@Controller
@RequestMapping("/video")
public class VideoController extends BaseController {


    private VideoViewRecordService videoViewRecordService = new VideoViewRecordService();
    private VideoService videoService = new VideoService();
    private ClassService classService = new ClassService();
   
    private CloudResourceService cloudResourceService=new CloudResourceService();

    private static final Logger logger =Logger.getLogger(VideoController.class);

    @RequestMapping("/persistentNotify")
    @SessionNeedless
    public @ResponseBody void persistentNotify(@RequestBody Map requestBody) {


        logger.info("video persistent enter");
        String persistentId = (String)requestBody.get("id");
        String bucketKey = null;
        try {
            String inputKey = (String) requestBody.get("inputKey");
            String[] tempArr = inputKey.split("/");
            bucketKey = tempArr[tempArr.length - 1];
        } catch (Exception e) {}

        int code = Integer.parseInt(requestBody.get("code").toString());

        VideoEntry videoEntry = videoService.getVideoEntryByPersistentId(persistentId);
//        UploadState uploadState;
        if (code == 0) {
            logger.info("video persistent code = 0");

            videoService.updateVideoUpdateStatus(videoEntry.getID(),1);
        } else {
            logger.info("video persistent code = 1");

            videoService.updateVideoUpdateStatus(videoEntry.getID(),2);

        }
    }

    @RequestMapping("/viewrecord/add")
    @ResponseBody
    public RespObj viewRecord(@ObjectIdType ObjectId videoId,
                              int viewType) throws IllegalParamException {
        VideoEntry ve = videoService.getVideoEntryById(videoId);
        String name;
        if(ve == null){
            name = cloudResourceService.getResourceEntryById(videoId).getName();
        } else {
            name = ve.getName();
        }
//        if(null!=ve)
//        {
	        IdValuePair videoInfo = new IdValuePair(videoId,name);
	        IdValuePair studentInfo = new IdValuePair(getUserId(),getSessionValue().getUserName());
	
	        IdValuePair classInfo = null;
	        if(UserRole.isStudent(getSessionValue().getUserRole()))
	        {
	            ClassEntry classEntry = classService.getClassEntryByStuId(getUserId(), Constant.FIELDS);
	            classInfo = new IdValuePair(classEntry.getID(),classEntry.getName());
	
	        }
	        VideoViewRecordEntry vviewe = new VideoViewRecordEntry(videoInfo,studentInfo,classInfo,
	                viewType,null,1,System.currentTimeMillis()
	                );
	        videoViewRecordService.addVideoViewRecordEntry(vviewe);
//        }

        return RespObj.SUCCESS;
    }

    /**
     * 上传视频
     *
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    public void uploadVideo(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            resultMap.put("data", videoService.uploadVideo(request,"file"));
            resultMap.put("code", "200");
        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("data", "上传文件失败");
        } catch (IllegalParamException e) {
            e.printStackTrace();
            resultMap.put("code", "500");
            resultMap.put("data", "上传文件失败");
        } finally {
            PrintWriter pw;
            try {
                pw = response.getWriter();
                pw.write(JSON.toJSONString(resultMap));
                pw.flush();
                pw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // return JSON.toJSONString(respObj);
    }

}
