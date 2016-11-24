package com.fulaan.schoolsecurity.dto;

import com.fulaan.utils.KeyWordFilterUtil;
import com.pojo.app.Platform;
import com.pojo.schoolsecurity.SchoolSecurityEntry;
import com.pojo.schoolsecurity.SchoolSecurityImage;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by guojing on 2015/6/18.
 */
public class SchoolSecurityDTO {

    /**
     * id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userImage;

    /**
     * 操作平台
     */
    private String platformDesc;

    /**
     * 学校id
     */
    private String schoolId;

    /**
     * 用户角色
     */
    private int role;

    /**
     * 发布内容
     */
    private String publishContent;

    /**
     * 上传图片
     */
    private String[] fileNameAry;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 角色描述
     */
    private String roleDescription;

    /**
     * 学校管理者
     */
    private int schoolManager;

    /**
     * 是否处理
     */
    private int isHandle;

    /**
     * 是否删除
     */
    private int isDelete;

    public SchoolSecurityDTO() {

    }

    public SchoolSecurityDTO(SchoolSecurityEntry schoolSecurityEntry) {
        this.id = schoolSecurityEntry.getID().toString();
        this.userId = schoolSecurityEntry.getUserId().toString();
        this.publishContent = KeyWordFilterUtil.getReplaceStrTxtKeyWords(schoolSecurityEntry.getContent(), "*", 2);
        Map<Integer, String> pfMap=Platform.getPlatformMap();
        this.platformDesc =pfMap.get(schoolSecurityEntry.getPlatformType());
        this.schoolId=schoolSecurityEntry.getSchoolID().toString();
        int num = 0;
        if (schoolSecurityEntry.getImageList()!=null && schoolSecurityEntry.getImageList().size()!=0) {
            String[] fileList = new String[schoolSecurityEntry.getImageList().size()];
            for (SchoolSecurityImage image : schoolSecurityEntry.getImageList()) {
                fileList[num] = image.getPath().toString();
                num++;
            }
            this.fileNameAry = fileList;
        }else{
            this.fileNameAry =new String[0];

        }
        this.isHandle  = schoolSecurityEntry.getHandleState();
        this.isDelete = schoolSecurityEntry.getDeleteState();
        this.publishTime = new Date(schoolSecurityEntry.getPublishTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPlatformDesc() {
        return platformDesc;
    }

    public void setPlatformDesc(String platformDesc) {
        this.platformDesc = platformDesc;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPublishContent() {
        return publishContent;
    }

    public void setPublishContent(String publishContent) {
        this.publishContent = publishContent;
    }

    public String[] getFileNameAry() {
        return fileNameAry;
    }

    public void setFileNameAry(String[] fileNameAry) {
        this.fileNameAry = fileNameAry;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public int getSchoolManager() {
        return schoolManager;
    }

    public void setSchoolManager(int schoolManager) {
        this.schoolManager = schoolManager;
    }

    public int getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(int isHandle) {
        this.isHandle = isHandle;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public SchoolSecurityEntry buildSchoolSecurityEntry(String client,ObjectId schoolId) {
        Platform pf = null;
        if (client.contains("iOS")) {
            pf = Platform.IOS;
        } else if (client.contains("Android")){
            pf = Platform.Android;
        } else {
            pf = Platform.PC;
        }

        List<SchoolSecurityImage> _filenames = new ArrayList<SchoolSecurityImage>();
        if(this.getFileNameAry()!=null ){
            for(int i=0;i<this.getFileNameAry().length;i++){
                _filenames.add(new SchoolSecurityImage(this.getFileNameAry()[i]));
            }
        }

        SchoolSecurityEntry schoolSecurityEntry = new SchoolSecurityEntry(
                new ObjectId(this.userId),
                getPublishContent(),
                pf,
                schoolId,
                _filenames
        );
        return schoolSecurityEntry;

    }
}
