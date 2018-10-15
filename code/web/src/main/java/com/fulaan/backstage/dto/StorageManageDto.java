package com.fulaan.backstage.dto;

import com.pojo.backstage.StorageManageEntry;

import java.util.List;

/**
 * Created by taotao.chan on 2018年9月18日11:06:56
 */
public class StorageManageDto {
    private String id;
    private String imeiNo;
    private String phoneModel;
    private String color;
    private String manufacturer;
    private String inStorageTime;
    private String storageStatus;
    private String comment;
    private String useStatus;
    private String inStorageYear;
    private String inStorageMonth;
    private String commentType;
    private List<String> needRepairComment;
    private String creationTime;

    public StorageManageDto(StorageManageEntry entry){
        this.id = entry.getID().toString();
        this.imeiNo = entry.getImeiNo();
        this.phoneModel = entry.getPhoneModel();
        this.color = entry.getColor();
        this.manufacturer = entry.getManufacturer();
        this.inStorageTime = entry.getInStorageTime();
        this.storageStatus = entry.getStorageStatus();
        this.comment = entry.getComment();
        this.useStatus = entry.getUseStatus();
        this.inStorageYear = entry.getInStorageYear();
        this.inStorageMonth = entry.getInStorageMonth();
        this.commentType = entry.getCommentType();
        this.needRepairComment = entry.getNeedRepairComment();
        this.creationTime = entry.getCreationTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getInStorageTime() {
        return inStorageTime;
    }

    public void setInStorageTime(String inStorageTime) {
        this.inStorageTime = inStorageTime;
    }

    public String getStorageStatus() {
        return storageStatus;
    }

    public void setStorageStatus(String storageStatus) {
        this.storageStatus = storageStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getInStorageYear() {
        return inStorageYear;
    }

    public void setInStorageYear(String inStorageYear) {
        this.inStorageYear = inStorageYear;
    }

    public String getInStorageMonth() {
        return inStorageMonth;
    }

    public void setInStorageMonth(String inStorageMonth) {
        this.inStorageMonth = inStorageMonth;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public List<String> getNeedRepairComment() {
        return needRepairComment;
    }

    public void setNeedRepairComment(List<String> needRepairComment) {
        this.needRepairComment = needRepairComment;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
