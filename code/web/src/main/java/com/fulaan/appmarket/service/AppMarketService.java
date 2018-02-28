package com.fulaan.appmarket.service;

import com.db.appmarket.AppDetailCommentDao;
import com.db.appmarket.AppDetailDao;
import com.db.appmarket.AppDetailStarStatisticDao;
import com.db.backstage.JxmAppVersionDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolAppDao;
import com.fulaan.apkForParse.apkResolverParse.entity.ApkInfo;
import com.fulaan.apkForParse.apkResolverParse.utils.ApkUtil;
import com.fulaan.apkForParse.apkResolverParse.utils.IconUtil;
import com.fulaan.appmarket.dto.AppDetailCommentDTO;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.appmarket.dto.AppDetailStarStatisticDTO;
import com.fulaan.backstage.dto.JxmAppVersionDTO;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.GetApkSize;
import com.fulaan.utils.QiniuFileUtils;
import com.pojo.appmarket.AppDetailCommentEntry;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.appmarket.AppDetailStarStatisticEntry;
import com.pojo.backstage.JxmAppVersionEntry;
import com.pojo.backstage.LogMessageType;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.jiaschool.SchoolAppEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.props.Resources;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by scott on 2017/10/10.
 */
@Service
public class AppMarketService {

    private AppDetailDao appDetailDao = new AppDetailDao();

    private SchoolAppDao schoolAppDao = new SchoolAppDao();

    private AppDetailCommentDao appDetailCommentDao = new AppDetailCommentDao();

    private AppDetailStarStatisticDao appDetailStarStatisticDao = new AppDetailStarStatisticDao();

    private JxmAppVersionDao jxmAppVersionDao = new JxmAppVersionDao();

    private BackStageService backStageService = new BackStageService();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();
    @Autowired
    private UserService userService;


    public void saveAppDetail(AppDetailDTO appDetailDTO, ObjectId userId) {
        appDetailDao.saveAppDetailEntry(appDetailDTO.buildEntry(userId));
    }


    public AppDetailDTO getApp(ObjectId appId) {
        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        if (null != appDetailEntry) {
            return new AppDetailDTO(appDetailEntry);
        } else {
            return null;
        }
    }

    public List<AppDetailDTO> getAppByCondition(String regular) {
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> appDetailEntries = appDetailDao.getAppByCondition(regular);
        for (AppDetailEntry entry : appDetailEntries) {
            detailDTOs.add(new AppDetailDTO(entry));
        }
        return detailDTOs;
    }

    public List<AppDetailDTO> searchFulanAppByCondition(String regular) {
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> appDetailEntries = appDetailDao.searchFulanAppByCondition(regular);
        for (AppDetailEntry entry : appDetailEntries) {
            detailDTOs.add(new AppDetailDTO(entry));
        }
        return detailDTOs;
    }

    public List<AppDetailDTO> searchSchoolAppByCondition(String regular,String schoolId) {
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> appDetailEntries = new ArrayList<AppDetailEntry>();
        String schoolName = "";
        if(schoolId != null && !schoolId.equals("")){
            SchoolAppEntry schoolAppEntry = schoolAppDao.getEntryById(new ObjectId(schoolId));
            if(schoolAppEntry!=null && schoolAppEntry.getAppIdList()!= null){
                appDetailEntries = appDetailDao.searchSchoolAppByIds(schoolAppEntry.getAppIdList());
                HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(new ObjectId(schoolId));
                if(homeSchoolEntry!=null){
                    schoolName=homeSchoolEntry.getName();
                }
            }
        }else{
            appDetailEntries = appDetailDao.searchSchoolAppByCondition(regular);
        }

        for (AppDetailEntry entry : appDetailEntries) {
            entry.setUrl(schoolName);
            detailDTOs.add(new AppDetailDTO(entry));
        }
        return detailDTOs;
    }
    public List<AppDetailDTO> getAllAppDetails() {
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> appDetailEntries = appDetailDao.getEntries();
        for (AppDetailEntry entry : appDetailEntries) {
            detailDTOs.add(new AppDetailDTO(entry));
        }
        return detailDTOs;
    }

    public Map<String, Object> getCommentList(ObjectId appDetailId, int page, int pageSize) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        List<AppDetailCommentDTO> commentDTOs = new ArrayList<AppDetailCommentDTO>();
        List<AppDetailCommentEntry> commentEntries = appDetailCommentDao.getAppEntries(appDetailId, page, pageSize);
        Set<ObjectId> userIds = new HashSet<ObjectId>();
        for (AppDetailCommentEntry commentEntry : commentEntries) {
            userIds.add(commentEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(userIds, Constant.FIELDS);
        for (AppDetailCommentEntry commentEntry : commentEntries) {
            AppDetailCommentDTO commentDTO = new AppDetailCommentDTO(commentEntry);
            UserEntry userEntry = userEntryMap.get(commentEntry.getUserId());
            if (null != userEntry) {
                commentDTO.setUserName(userEntry.getUserName());
            }
            commentDTOs.add(commentDTO);
        }
        int count = appDetailCommentDao.countAppEntries(appDetailId);
        List<AppDetailStarStatisticDTO> initDatas = generateInitData();
        Map<Integer, Integer> map = appDetailStarStatisticDao.getMapEntry(appDetailId);
        for (AppDetailStarStatisticDTO dto : initDatas) {
            int star = dto.getStar();
            if (null != map.get(star)) {
                dto.setCount(map.get(star));
            }
        }
        int total = 0;
        int totalStar = 0;
        double avgStar = 0D;
        for (AppDetailStarStatisticDTO dto : initDatas) {
            total += dto.getCount();
            totalStar += dto.getStar() * dto.getCount();
        }
        if (total != 0) {
            avgStar = divide((double) totalStar, (double) total, 1);
            for (AppDetailStarStatisticDTO dto : initDatas) {
                double p = divide(mul((double) dto.getCount(), 100D), (double) total, 1);
                dto.setPercent(p);
            }
        }
        Collections.sort(initDatas, new Comparator<AppDetailStarStatisticDTO>() {
            @Override
            public int compare(AppDetailStarStatisticDTO o1, AppDetailStarStatisticDTO o2) {
                return o2.getStar() - o1.getStar();
            }
        });
        retMap.put("avgStar", avgStar);
        retMap.put("stars", initDatas);
        retMap.put("list", commentDTOs);
        retMap.put("count", count);
        retMap.put("page", page);
        retMap.put("pageSize", pageSize);
        return retMap;
    }

    public static Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static Double divide(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public List<AppDetailStarStatisticDTO> generateInitData() {
        List<AppDetailStarStatisticDTO>
                dtos = new ArrayList<AppDetailStarStatisticDTO>();
        dtos.add(new AppDetailStarStatisticDTO(Constant.ONE, 0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.TWO, 0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.THREE, 0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.FOUR, 0));
        dtos.add(new AppDetailStarStatisticDTO(Constant.FIVE, 0));
        return dtos;
    }

    public void saveAppDetailComment(AppDetailCommentDTO commentDTO, ObjectId userId) {
        commentDTO.setUserId(userId.toString());
        appDetailCommentDao.saveAppDetailComment(commentDTO.buildEntry());
        AppDetailStarStatisticEntry
                entry = appDetailStarStatisticDao.getEntryByStarAndDetailId(
                new ObjectId(commentDTO.getAppDetailId()), commentDTO.getStar()
        );
        if (null != entry) {
            appDetailStarStatisticDao.updateAppDetailStar(new ObjectId(commentDTO.getAppDetailId()), commentDTO.getStar(),
                    Constant.ONE);
        } else {
            AppDetailStarStatisticEntry starStatisticEntry = new AppDetailStarStatisticEntry(
                    new ObjectId(commentDTO.getAppDetailId()), commentDTO.getStar(),
                    Constant.ONE
            );
            appDetailStarStatisticDao.saveAppDetailStarStatisticEntry(starStatisticEntry);
        }
    }

    public void deleteApk(ObjectId apkId,ObjectId userId)throws Exception{
        AppDetailEntry appDetailEntry=appDetailDao.findEntryById(apkId);
        if(null!=appDetailEntry){
            appDetailDao.removeById(apkId);
            backStageService.addLogMessage(apkId.toString(), "删除应用："+appDetailEntry.getAppName(), LogMessageType.table.getDes(), userId.toString());
        }
        if(appDetailEntry.getType()==1){
            JxmAppVersionEntry entry1 = jxmAppVersionDao.getEntry(appDetailEntry.getAppPackageName());
            if(entry1 !=null){
                jxmAppVersionDao.removeById(entry1.getID());
            }
        }

    }

    public void setOrder(ObjectId apkId,int order){
        appDetailDao.setOrder(apkId,order);
    }

    public void updateApkByType(String id, int type){
        if(StringUtils.isNotEmpty(id)) {
            ObjectId apkId = new ObjectId(id);
            appDetailDao.updateApkByType(apkId,type);
        }
    }

    public void importApkFile(MultipartFile file, InputStream inputStream, String fileName,ObjectId userId) throws Exception {
        ObjectId id = new ObjectId();
        final String anOSName = System.getProperty("os.name");
        String bathPath = Resources.getProperty("upload.file");
        if (anOSName.toLowerCase().startsWith("windows")) {
            bathPath= Resources.getProperty("uploads.file");
        }
        File dir = new File(bathPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(bathPath, fileName);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        file.transferTo(destFile);
        String fileKey = id.toString() + Constant.POINT + FilenameUtils.getExtension(fileName);
        String extName = FilenameUtils.getExtension(fileName);
        String path = "";
        if (extName.equalsIgnoreCase("amr")) {
            String saveFileKey = new ObjectId().toString() + ".mp3";
            com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
            path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
        } else {
            QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);
            path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
        }
        Map<String, Object> apkSize = GetApkSize.getFilePath(destFile);
        long size = 0L;
        if (null != apkSize.get("size")) {
            size = Long.valueOf(String.valueOf(apkSize.get("size")));
            System.out.println(size);
        }
        String sizeStr = "";
        if (null != apkSize.get("apkSize")) {
            sizeStr = String.valueOf(apkSize.get("apkSize"));
            System.out.println(sizeStr);
        }
        ApkUtil apkUtil = new ApkUtil();
        ApkInfo apkInfo = apkUtil.getApkInfo(destFile.getPath());
        String imageFileName = new ObjectId().toString() + Constant.POINT + "png";
        String imageFilePath = bathPath +"/"+ imageFileName;
        if(anOSName.toLowerCase().startsWith("windows")) {
            imageFilePath = bathPath + "\\" + imageFileName;
        }
        File imageFile = new File(imageFilePath);
        IconUtil.extractFileFromApk(destFile.getPath(), apkInfo.getApplicationIcon(), imageFilePath);
        QiniuFileUtils.uploadFile(imageFileName, new FileInputStream(new File(imageFilePath)), QiniuFileUtils.TYPE_IMAGE);
        String imageFileUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, imageFileName);

        String packageName = apkInfo.getPackageName();
        AppDetailEntry entry = appDetailDao.getEntryByApkPackageName(packageName);
        if (null != entry) {

            AppDetailEntry
                    updateEntry = new AppDetailEntry(entry.getID(),
                    packageName,
                    imageFileUrl,
                    Constant.TWO,
                    size,
                    Integer.valueOf(apkInfo.getVersionCode()),
                    entry.getIsControl(),
                    entry.getWhiteOrBlack(),
                    sizeStr,
                    new ArrayList<AttachmentEntry>(),
                    apkInfo.getVersionName(),
                    Constant.EMPTY,
                    apkInfo.getApplicationLable(),
                    path,
                    fileKey);
            appDetailDao.saveAppDetailEntry(updateEntry);
        } else {
            AppDetailEntry
                    newEntry = new AppDetailEntry(
                    packageName,
                    imageFileUrl,
                    Constant.TWO,
                    size,
                    Integer.valueOf(apkInfo.getVersionCode()),
                    Constant.ONE,
                    Constant.ONE,
                    sizeStr,
                    new ArrayList<AttachmentEntry>(),
                    apkInfo.getVersionName(),
                    Constant.EMPTY,
                    apkInfo.getApplicationLable(),
                    path,
                    fileKey);
            appDetailDao.saveAppDetailEntry(newEntry);
        }
        backStageService.addLogMessage(userId.toString(), "添加了新的第三方应用："+packageName, LogMessageType.table.getDes(), userId.toString());
        destFile.delete();
        imageFile.delete();
    }


    public void importApkFile2(MultipartFile file, InputStream inputStream, String fileName,ObjectId userId) throws Exception {
        ObjectId id = new ObjectId();
        final String anOSName = System.getProperty("os.name");
        String bathPath = Resources.getProperty("upload.file");
        if (anOSName.toLowerCase().startsWith("windows")) {
            bathPath= Resources.getProperty("uploads.file");
        }
        File dir = new File(bathPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(bathPath, fileName);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        file.transferTo(destFile);
        String fileKey = id.toString() + Constant.POINT + FilenameUtils.getExtension(fileName);
        String extName = FilenameUtils.getExtension(fileName);
        String path = "";
        if (extName.equalsIgnoreCase("amr")) {
            String saveFileKey = new ObjectId().toString() + ".mp3";
            com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
            path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
        } else {
            QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);
            path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
        }
        Map<String, Object> apkSize = GetApkSize.getFilePath(destFile);
        long size = 0L;
        if (null != apkSize.get("size")) {
            size = Long.valueOf(String.valueOf(apkSize.get("size")));
            System.out.println(size);
        }
        String sizeStr = "";
        if (null != apkSize.get("apkSize")) {
            sizeStr = String.valueOf(apkSize.get("apkSize"));
            System.out.println(sizeStr);
        }
        ApkUtil apkUtil = new ApkUtil();
        ApkInfo apkInfo = apkUtil.getApkInfo(destFile.getPath());
        String imageFileName = new ObjectId().toString() + Constant.POINT + "png";
        String imageFilePath = bathPath +"/"+ imageFileName;
        if(anOSName.toLowerCase().startsWith("windows")) {
            imageFilePath = bathPath + "\\" + imageFileName;
        }
        File imageFile = new File(imageFilePath);
        IconUtil.extractFileFromApk(destFile.getPath(), apkInfo.getApplicationIcon(), imageFilePath);
        QiniuFileUtils.uploadFile(imageFileName, new FileInputStream(new File(imageFilePath)), QiniuFileUtils.TYPE_IMAGE);
        String imageFileUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, imageFileName);

        String packageName = apkInfo.getPackageName();
        AppDetailEntry entry = appDetailDao.getEntryByApkPackageName(packageName);
        if (null != entry) {

            AppDetailEntry
                    updateEntry = new AppDetailEntry(entry.getID(),
                    packageName,
                    imageFileUrl,
                    Constant.ONE,
                    size,
                    Integer.valueOf(apkInfo.getVersionCode()),
                    entry.getIsControl(),
                    entry.getWhiteOrBlack(),
                    sizeStr,
                    new ArrayList<AttachmentEntry>(),
                    apkInfo.getVersionName(),
                    Constant.EMPTY,
                    apkInfo.getApplicationLable(),
                    path,
                    fileKey);
            appDetailDao.saveAppDetailEntry(updateEntry);
        } else {
            AppDetailEntry
                    newEntry = new AppDetailEntry(
                    packageName,
                    imageFileUrl,
                    Constant.ONE,
                    size,
                    Integer.valueOf(apkInfo.getVersionCode()),
                    Constant.ONE,
                    Constant.TWO,
                    sizeStr,
                    new ArrayList<AttachmentEntry>(),
                    apkInfo.getVersionName(),
                    Constant.EMPTY,
                    apkInfo.getApplicationLable(),
                    path,
                    fileKey);
            appDetailDao.saveAppDetailEntry(newEntry);
        }
        JxmAppVersionEntry entry1 = jxmAppVersionDao.getEntry(packageName);
        if(entry1!= null){
            entry1.setFileUrl(path);
            entry1.setVersion(apkInfo.getVersionName());
            jxmAppVersionDao.updEntry(entry1);
        } else{
            JxmAppVersionDTO dto = new JxmAppVersionDTO();
            dto.setFileUrl(path);
            dto.setName(packageName);
            dto.setVersion(apkInfo.getVersionName());
            jxmAppVersionDao.addEntry(dto.buildAddEntry());
        }
        backStageService.addLogMessage(userId.toString(), "添加了新的复兰应用："+packageName, LogMessageType.table.getDes(), userId.toString());
        destFile.delete();
        imageFile.delete();
    }

    //学校（校本资源推送）
    public String importApkFile3(MultipartFile file, InputStream inputStream, String fileName,ObjectId userId) throws Exception {
        ObjectId id = new ObjectId();
        final String anOSName = System.getProperty("os.name");
        String bathPath = Resources.getProperty("upload.file");
        if (anOSName.toLowerCase().startsWith("windows")) {
            bathPath= Resources.getProperty("uploads.file");
        }
        File dir = new File(bathPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(bathPath, fileName);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        file.transferTo(destFile);
        String fileKey = id.toString() + Constant.POINT + FilenameUtils.getExtension(fileName);
        String extName = FilenameUtils.getExtension(fileName);
        String path = "";
        if (extName.equalsIgnoreCase("amr")) {
            String saveFileKey = new ObjectId().toString() + ".mp3";
            com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
            path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
        } else {
            QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);
            path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
        }
        Map<String, Object> apkSize = GetApkSize.getFilePath(destFile);
        long size = 0L;
        if (null != apkSize.get("size")) {
            size = Long.valueOf(String.valueOf(apkSize.get("size")));
            System.out.println(size);
        }
        String sizeStr = "";
        if (null != apkSize.get("apkSize")) {
            sizeStr = String.valueOf(apkSize.get("apkSize"));
            System.out.println(sizeStr);
        }
        ApkUtil apkUtil = new ApkUtil();
        ApkInfo apkInfo = apkUtil.getApkInfo(destFile.getPath());
        String imageFileName = new ObjectId().toString() + Constant.POINT + "png";
        String imageFilePath = bathPath +"/"+ imageFileName;
        if(anOSName.toLowerCase().startsWith("windows")) {
            imageFilePath = bathPath + "\\" + imageFileName;
        }
        File imageFile = new File(imageFilePath);
        IconUtil.extractFileFromApk(destFile.getPath(), apkInfo.getApplicationIcon(), imageFilePath);
        QiniuFileUtils.uploadFile(imageFileName, new FileInputStream(new File(imageFilePath)), QiniuFileUtils.TYPE_IMAGE);
        String imageFileUrl = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, imageFileName);

        String packageName = apkInfo.getPackageName();
        AppDetailEntry entry = appDetailDao.getEntryByApkPackageName(packageName);
        String osid = "";
        if (null != entry) {
            if(entry.getType()==3){
                AppDetailEntry
                        updateEntry = new AppDetailEntry(entry.getID(),
                        packageName,
                        imageFileUrl,
                        Constant.THREE,
                        size,
                        Integer.valueOf(apkInfo.getVersionCode()),
                        entry.getIsControl(),
                        entry.getWhiteOrBlack(),
                        sizeStr,
                        new ArrayList<AttachmentEntry>(),
                        apkInfo.getVersionName(),
                        Constant.EMPTY,
                        apkInfo.getApplicationLable(),
                        path,
                        fileKey);
                appDetailDao.saveAppDetailEntry(updateEntry);
                osid = entry.getID().toString();

            }
        } else {
            AppDetailEntry
                    newEntry = new AppDetailEntry(
                    packageName,
                    imageFileUrl,
                    Constant.THREE,
                    size,
                    Integer.valueOf(apkInfo.getVersionCode()),
                    Constant.ONE,
                    Constant.TWO,
                    sizeStr,
                    new ArrayList<AttachmentEntry>(),
                    apkInfo.getVersionName(),
                    Constant.EMPTY,
                    apkInfo.getApplicationLable(),
                    path,
                    fileKey);
            appDetailDao.saveAppDetailEntry(newEntry);
            osid = newEntry.getID().toString();
        }
        backStageService.addLogMessage(userId.toString(), "添加了新的校本资源："+packageName, LogMessageType.table.getDes(), userId.toString());
        destFile.delete();
        imageFile.delete();
        return osid;
    }

    public void addDescription(ObjectId id,String content,ObjectId userId){
        AppDetailEntry entry = appDetailDao.findEntryById(id);
        if(entry != null){
            entry.setDescription(content);
            appDetailDao.saveAppDetailEntry(entry);
            backStageService.addLogMessage(entry.getID().toString(), "修改了描述："+entry.getAppName(), LogMessageType.table.getDes(), userId.toString());
        }
    }

    public void addAppName(ObjectId id,String name,ObjectId userId){
        AppDetailEntry entry = appDetailDao.findEntryById(id);
        if(entry != null){
            entry.setAppName(name);
            appDetailDao.saveAppDetailEntry(entry);
            backStageService.addLogMessage(entry.getID().toString(), "修改了应用名："+entry.getAppName(), LogMessageType.table.getDes(), userId.toString());
        }
    }
}
