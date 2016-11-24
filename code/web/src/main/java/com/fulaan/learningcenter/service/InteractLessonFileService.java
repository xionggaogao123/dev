package com.fulaan.learningcenter.service;

import com.db.school.InteractLessonFileDao;
import com.fulaan.learningcenter.dto.InteractLessonFileDTO;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.pojo.KeyValue;
import com.fulaan.video.service.VideoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.school.InteractLessonFileEntry;
import com.pojo.user.UserEntry;
import com.pojo.video.VideoEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.NumberUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by guojing on 2015/11/23.
 */
@Service
public class InteractLessonFileService {

    private static final Logger logger =Logger.getLogger(InteractLessonFileService.class);

    private InteractLessonFileDao interactLessonFileDao =new InteractLessonFileDao();

    @Resource
    private UserService userService;

    private VideoService videoService = new VideoService();

    /**
     * 新建互动课堂文件上传
     * @param e
     * @return
     */
    public ObjectId addInteractLessonFileEntry(InteractLessonFileEntry e)
    {
        return interactLessonFileDao.addInteractLessonFileEntry(e);
    }

    /**
     * 获取互动课堂文件上传次数
     * @param lessonId
     * @param type
     * @return
     */
    public List<KeyValue> getFileUploadTimesList(ObjectId lessonId, int type) {
        List<KeyValue> resultList=new ArrayList<KeyValue>();
        DBObject fields =new BasicDBObject("ts", Constant.ONE);
        List<InteractLessonFileEntry> list=interactLessonFileDao.findInteractLessonFileEntry(lessonId, type, 0, fields,"ts");
        if(list!=null&&list.size()>0){
            InteractLessonFileEntry entry=list.get(0);
            int maxVal=entry.getTimes();
            for(int i=1;i<=maxVal;i++){
                KeyValue keyValue = new KeyValue();
                keyValue.setKey(i);
                String value="第"+NumberUtils.formatInteger(i)+"次上传";
                keyValue.setValue(value);
                resultList.add(keyValue);
            }
        }
        return resultList;
    }


    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalParamException
     */
    public void downFile(ObjectId videoId, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException {
        VideoEntry videoEntry=videoService.getVideoEntryById(videoId);
        if (videoEntry == null) {
            throw new IllegalParamException("未找到文件信息");
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, videoEntry.getName()));

        InputStream inputStream = QiniuFileUtils.downFile(QiniuFileUtils.TYPE_USER_VIDEO, videoEntry.getBucketkey());
        if (inputStream == null) {
            throw new IllegalParamException("未找到文件信息");
        }
        BufferedOutputStream bufferedOutputStream = null;
        byte[] bytes = new byte[1024];
        int len;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }
        } finally {
            if (bufferedOutputStream != null) {
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            inputStream.close();
        }
    }

    /**
     * 获取导出文件的名称
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null && agent.toLowerCase().indexOf("firefox") >= 0) {
            fileName = new String(fileName.getBytes(Constant.UTF_8), Constant.ISO);
        } else {
            fileName = java.net.URLEncoder.encode(fileName, Constant.UTF_8);
        }
        return fileName;
    }

    /**
     * 获取互动课堂文件上传列表
     * @param lessonId
     * @param type
     * @return
     */
    public List<InteractLessonFileDTO> getLessonUploadFileList(ObjectId lessonId, int type, int times) {
        List<InteractLessonFileDTO> resultList=new ArrayList<InteractLessonFileDTO>();
        List<InteractLessonFileEntry> list=interactLessonFileDao.findInteractLessonFileEntry(lessonId, type, times, Constant.FIELDS, "");
        List<ObjectId> vids = new ArrayList<ObjectId>();
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for(InteractLessonFileEntry entry : list){
            userIds.add(entry.getUserId());
            if("Y".equals(entry.getIsVideo())){
                vids.add(entry.getVideoId());
            }
        }
        Map<ObjectId, UserEntry> userMap=userService.getUserEntryMap(userIds, new BasicDBObject("nm",1).append("ir",1));
        Map<ObjectId, VideoEntry> videoEntryMap = videoService.getVideoEntryMap(vids,null);
        for(InteractLessonFileEntry entry : list){
            UserEntry userEntry=userMap.get(entry.getUserId());
            if(userEntry==null){
                continue;
            }
            InteractLessonFileDTO dto=new InteractLessonFileDTO();
            dto.setId(entry.getID().toString());
            dto.setLessonId(entry.getLessonId().toString());
            dto.setUserId(userEntry.getID().toString());
            dto.setUserName(userEntry.getUserName());

            if((type==3||type==4)&&userEntry!=null){
                dto.setFileName(userEntry.getUserName());
            }else{
                dto.setFileName(entry.getFileName());
            }

            dto.setIsVideo(entry.getIsVideo());

            if("Y".equals(entry.getIsVideo())) {
                if (videoEntryMap.get(entry.getVideoId()) != null) {
                    dto.setVideoId(entry.getVideoId().toString());
                    VideoEntry videoEntry=videoEntryMap.get(entry.getVideoId());
                    //String path=QiniuFileUtils.getPath(QiniuFileUtils.TYPE_USER_VIDEO, videoEntry.getBucketkey());
                    dto.setImgUrl(videoEntry.getImgUrl());
                }
            }else{
                dto.setImgUrl(entry.getFilePath());
            }
            dto.setDate(DateTimeUtils.convert(entry.getCreateTime(), DateTimeUtils.DATE_YYYY_MM_DD));
            resultList.add(dto);
        }
        return resultList;
    }

    public Map<String, InteractLessonFileEntry> getExamFileEntryMap(ObjectId lessonId, int type) {
        Map<String, InteractLessonFileEntry>  map=interactLessonFileDao.getExamFileEntryMap(lessonId, type);
        return map;
    }

    public void downFile1(String url) {
        QiniuFileUtils.downFileByUrl(url);
    }
}
