package com.fulaan_old.file.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.db.resources.CloudResourceDao;
import com.fulaan_old.utils.QiniuFileUtils;
import com.pojo.app.FileType;
import com.pojo.resources.ResourceEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.HttpFileConvertUtils;

/**
 * 文件上传
 * Created by Caocui on 2015/8/25.
 */
@Service
public class FileService {
    private static final List<String> VIDEO_TYPE = Arrays.asList(new String[]{".avi", ".mp4", ".mov", ".mpg", ".flv", ".wmv", ".mkv"});
    private static final List<String> IMAGE_TYPE = Arrays.asList(new String[]{".jpg", ".jpeg", ".gif", ".bmp", ".png"});
    private static final List<String> SWF_TYPE = Arrays.asList(new String[]{".doc", ".pdf"});
    private CloudResourceDao resourceDao = new CloudResourceDao();

    public List<String> uploadFile(HttpServletRequest request, ObjectId userId, String fileField) throws IOException, IllegalParamException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> multipartFiles = multipartRequest.getFiles(fileField);
        ResourceEntry resourceEntry;
        ObjectId objectId;
        List<String> resource = new ArrayList<String>(multipartFiles.size());
        for (MultipartFile multipartFile : multipartFiles) {
            String qiniuKey = UUID.randomUUID().toString().replace(Constant.SEPARATE_LINE, Constant.EMPTY);
            String realName = multipartFile.getOriginalFilename();
            String fileTypeWithoutPoint = realName.substring(realName.lastIndexOf('.') + 1).toLowerCase();
            String fileType = realName.substring(realName.lastIndexOf('.')).toLowerCase();
            FileType ft = FileType.getFileType(fileTypeWithoutPoint);
            int type;
            if (ft == null) {
                type = -1;
            } else {
                type = ft.getType();
            }
            int qiniutype = convertQiniuFileType(fileType);


            if (SWF_TYPE.contains(fileType)) {
                String tempDirPath = getWebRootAbsolutePath() + "upload" + File.separator + "swftemp" + File.separator;
                File tempDirFile = new File(getWebRootAbsolutePath() + "upload" + File.separator + "swftemp");
                if (!tempDirFile.exists()) {
                    tempDirFile.mkdirs();
                }
                InputStream inputStream = multipartFile.getInputStream();
                String srcFileName = UUID.randomUUID().toString();
                String srcFilePath = tempDirPath + srcFileName + fileType;
                File srcFile = new File(tempDirPath + srcFileName + fileType);
                if (!srcFile.exists()) {
                    srcFile.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(srcFile);
                int k = -1;
                while ((k = inputStream.read()) != -1) {
                    fileOutputStream.write(k);
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                String swfFileName = srcFileName + "_convert";
                String swfFilePath = tempDirPath + srcFileName + ".swf";

                //转换
                if (".doc".equals(fileType)) {
                    HttpFileConvertUtils.convertWord2Swf(srcFilePath, swfFilePath);
                } else if (".pdf".equals(fileType)) {
                    HttpFileConvertUtils.convertPdfToSwf(srcFile, swfFilePath);
                }
                //上传原文件
                resourceEntry = new ResourceEntry(type, realName, multipartFile.getSize(), null, qiniuKey, userId, null, null, null);
                ObjectId srcId = resourceDao.addResource(resourceEntry);
                resource.add(srcId.toString());
                QiniuFileUtils.uploadFile(qiniuKey, multipartFile.getInputStream(), qiniutype);
                //上传swf
                String swfKey = UUID.randomUUID().toString().replace(Constant.SEPARATE_LINE, Constant.EMPTY);
                File swfFile = new File(tempDirPath + srcFileName + ".swf");
                FileType swfFt = FileType.getFileType("swf");
                ResourceEntry swfRe = new ResourceEntry(swfFt == null ? -1 : swfFt.getType(), realName + "_swf", multipartFile.getSize(), null, swfKey, userId, null, null, null);
//                ObjectId swfId = resourceDao.addResource(resourceEntry);
                QiniuFileUtils.uploadFile(swfKey, new FileInputStream(swfFile), QiniuFileUtils.TYPE_FLASH);
                resourceDao.update(srcId, "iurl", QiniuFileUtils.getPath(QiniuFileUtils.TYPE_FLASH, swfKey));
            } else {
                resourceEntry = new ResourceEntry(type, realName, multipartFile.getSize(), null, qiniuKey, userId, null, null, null);
                objectId = resourceDao.addResource(resourceEntry);
                resource.add(objectId.toString());

                if (qiniutype == 4) {
                    QiniuFileUtils.uploadVideoFile(objectId, qiniuKey, multipartFile.getInputStream(), QiniuFileUtils.TYPE_VIDEO);
                } else {

                    QiniuFileUtils.uploadFile(qiniuKey, multipartFile.getInputStream(), qiniutype);
                    if (qiniutype == QiniuFileUtils.TYPE_IMAGE) {
                        resourceDao.update(objectId, "iurl", QiniuFileUtils.getPath(qiniutype, qiniuKey));
                    }
                }
            }


//            String tempDirPath = request.getRealPath("tempfile" + File.separator);
//            
//            ResourceEntry resourceEntry1=new ResourceEntry(type, realName, multipartFile.getSize(), null, qiniuKey, userId, null, null, null);
//            objectId = resourceDao.addResource(resourceEntry1);
//            String qiniuKey1 = UUID.randomUUID().toString().replace(Constant.SEPARATE_LINE, Constant.EMPTY);
//            String srcFileName = UUID.randomUUID().toString();
//            File srcFile = new File(tempDirPath + srcFileName + fileType);
//            InputStream is = multipartFile.getInputStream();
//            FileOutputStream fos = new FileOutputStream(srcFile);
//            byte[] b = new byte[1024];
//            while((is.read(b)) != -1){
//            	fos.write(b);
//            }
//            is.close();
//            fos.close();
//            if (type == 2 && type == 4) {
//                QiniuFileUtils.uploadFile(qiniuKey1, multipartFile.getInputStream(), QiniuFileUtils.TYPE_FLASH);
//                resourceDao.update(objectId, "ofs", QiniuFileUtils.getPath(qiniutype, qiniuKey));
//            }
        }
        return resource;
    }


    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws IllegalParamException
     */
    public void downFile(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalParamException {
        String key = request.getParameter("key");
        if (StringUtils.isEmpty(key)) {
            throw new IllegalParamException("未找到文件信息");
        }
        ResourceEntry resourceEntry = resourceDao.getResourceEntryById(new ObjectId(key));
        if (resourceEntry == null) {
            throw new IllegalParamException("未找到文件信息");
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, resourceEntry.getName()));
        String fileName = resourceEntry.getName();
        InputStream inputStream = QiniuFileUtils.downFile(
                convertQiniuFileType(fileName.substring(fileName.lastIndexOf('.'))), resourceEntry.getBucketkey());
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

    private int getFileType(String fileType) {
        if (VIDEO_TYPE.contains(fileType)) {
            return 1;
        } else if ("swf".equals(fileType) || "flv".equals(fileType)) {
            return 2;
        } else if ("mp3".equals(fileType)) {
            return 3;
        } else if ("doc".equals(fileType) || "docx".equals(fileType)) {
            return 4;
        } else if ("ppt".equals(fileType)) {
            return 5;
        } else if ("pdf".equals(fileType)) {
            return 6;
        } else if (IMAGE_TYPE.contains(fileType)) {
            return 7;
        } else {
            return -1;
        }
    }

    private int convertQiniuFileType(String fileType) {
        if (VIDEO_TYPE.contains(fileType)) {
            return QiniuFileUtils.TYPE_VIDEO;
        } else if (".swf".equals(fileType) || ".flv".equals(fileType)) {
            return QiniuFileUtils.TYPE_FLASH;
        } else if (".mp3".equals(fileType)) {
            return QiniuFileUtils.TYPE_SOUND;
        } else if (".ppt".equals(fileType) || ".pdf".equals(fileType)
                || ".doc".equals(fileType) || ".docx".equals(fileType)) {
            return QiniuFileUtils.TYPE_DOCUMENT;
        } else if (IMAGE_TYPE.contains(fileType)) {
            return QiniuFileUtils.TYPE_IMAGE;
        } else {
            return QiniuFileUtils.TYPE_DOCUMENT;
        }
    }

    /**
     * @return WebRoot目录的绝对路径
     */
    public static String getWebRootAbsolutePath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        java.net.URL url = classLoader.getResource("");
        String ROOT_CLASS_PATH = url.getPath() + File.separator;
        File rootFile = new File(ROOT_CLASS_PATH);
        String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + File.separator;
        File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);
        String SERVLET_CONTEXT_PATH = webInfoDir.getParent() + File.separator;
        return SERVLET_CONTEXT_PATH;
    }
}
