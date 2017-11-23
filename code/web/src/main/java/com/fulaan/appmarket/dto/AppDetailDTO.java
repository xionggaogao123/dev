package com.fulaan.appmarket.dto;

import com.fulaan.pojo.Attachement;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.fcommunity.AttachmentEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/10/10.
 */
public class AppDetailDTO {

    private String id;
    private String appPackageName;
    private String logo;
    private int type;
    private int isControl;
    private int whiteOrBlack;
    private int order;
    private String size;
    private long appSize;
    private int versionCode;
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private String versionName;
    private String description;
    private String appName;
    private String url;
    private int isUpdated;
    private String updateTime;
    private int isCheck;//1 家长推荐      2 老师推荐         3 不推荐

    private String fileKey;

    public AppDetailDTO(){

    }

    public AppDetailEntry buildEntry(ObjectId userId){
        ObjectId aId=null;
        if(StringUtils.isNotBlank(id)&&ObjectId.isValid(id)){
            aId=new ObjectId(id);
        }
        List<AttachmentEntry> images=new ArrayList<AttachmentEntry>();
        for(Attachement attachement:imageList){
            images.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
            System.currentTimeMillis(), userId));
        }
        if(null!=aId){
            return new AppDetailEntry(aId,appPackageName,logo, type,appSize,versionCode,isControl,whiteOrBlack, size,
            images, versionName, description,appName,url,fileKey);
        }else{
            return new AppDetailEntry(appPackageName,logo, type,appSize, versionCode,isControl,whiteOrBlack,size,
                    images, versionName, description,appName,url,fileKey);
        }
    }

    public AppDetailDTO(AppDetailEntry detailEntry){
        this.id=detailEntry.getID().toString();
        this.appPackageName=detailEntry.getAppPackageName();
        this.logo=detailEntry.getLogo();
        this.type=detailEntry.getType();
        this.isControl = detailEntry.getIsControl();
        this.whiteOrBlack = detailEntry.getWhiteOrBlack();
        this.size=detailEntry.getSize();
        List<AttachmentEntry> images=detailEntry.getImageList();
        for(AttachmentEntry item:images){
            imageList.add(new Attachement(item));
        }
        this.order = detailEntry.getOrder();
        this.versionName=detailEntry.getVersionName();
        this.description=detailEntry.getDescription();
        this.url=detailEntry.getUrl();
        this.appName=detailEntry.getAppName();
        this.appSize=detailEntry.getAppSize();
        this.versionCode=detailEntry.getVersionCode();
        this.isUpdated=detailEntry.getIsUpdated();
        this.fileKey=detailEntry.getFileKey();
        this.updateTime= DateTimeUtils.convert(detailEntry.getUpdateTime(),DateTimeUtils.DATE_YYYY_MM_DD);
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public int getIsControl() {
        return isControl;
    }

    public void setIsControl(int isControl) {
        this.isControl = isControl;
    }

    public int getWhiteOrBlack() {
        return whiteOrBlack;
    }

    public void setWhiteOrBlack(int whiteOrBlack) {
        this.whiteOrBlack = whiteOrBlack;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(int isUpdated) {
        this.isUpdated = isUpdated;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
}
