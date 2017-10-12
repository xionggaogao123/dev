package com.fulaan.appmarket.dto;

import com.fulaan.pojo.Attachement;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.fcommunity.AttachmentEntry;
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
    private long size;
    private List<Attachement> imageList=new ArrayList<Attachement>();
    private String version;
    private String description;
    private String appName;
    private String url;

    public AppDetailDTO(){

    }

    public AppDetailEntry buildEntry(){
        ObjectId aId=null;
        if(StringUtils.isNotBlank(id)&&ObjectId.isValid(id)){
            aId=new ObjectId(id);
        }
        List<AttachmentEntry> images=new ArrayList<AttachmentEntry>();
        for(Attachement attachement:imageList){
            images.add(new AttachmentEntry(attachement.getUrl(), attachement.getFlnm(),
            System.currentTimeMillis(), null));
        }
        if(null!=aId){
            return new AppDetailEntry(aId,appPackageName,logo, type, size,
            images, version, description,appName,url);
        }else{
            return new AppDetailEntry(appPackageName,logo, type, size,
                    images, version, description,appName,url);
        }
    }

    public AppDetailDTO(AppDetailEntry detailEntry){
        this.id=detailEntry.getID().toString();
        this.appPackageName=detailEntry.getAppPackageName();
        this.logo=detailEntry.getLogo();
        this.type=detailEntry.getType();
        this.size=detailEntry.getSize();
        List<AttachmentEntry> images=detailEntry.getImageList();
        for(AttachmentEntry item:images){
            imageList.add(new Attachement(item));
        }
        this.version=detailEntry.getVersion();
        this.description=detailEntry.getDescription();
        this.url=detailEntry.getUrl();
        this.appName=detailEntry.getAppName();
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<Attachement> getImageList() {
        return imageList;
    }

    public void setImageList(List<Attachement> imageList) {
        this.imageList = imageList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
