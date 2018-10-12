package com.fulaan.backstage.dto;

import com.pojo.backstage.InOutStorageEntry;
import com.pojo.backstage.StorageManageEntry;

import java.util.List;

/**
 * Created by taotao.chan on 2018年9月20日13:45:36
 */
public class InOutStorageRecordDto {
    private String id;
    private String imeiNo;
    private String phoneModel;
    private String color;
    private String manufacturer;
    private String inStorageTime;
    private String storageStatus;
    private String comment;
    private String inStorageYear;
    private String inStorageMonth;
    private String projectName;
    private String projectDockPeople;
    private String schoolName;
    private String accessClass;
    private String accessObj;
    private String contactInfo;
    private String address;
    private String deliveryTime;
    private String deliveryMethod;
    private String excompanyNo;
    private String expressNo;
    private String parentName;
    private String parentMobile;
    private String parentId;
    private String studentName;
    private String studentMobile;
    private String studentId;
    private String recycleComment;
    private String repairRange;
    private String repairCost;
    private String storageRecordStatus;
    private String outStorageTime;
    private String outStorageYear;
    private String outStorageMonth;
    private List<String> needRepairComment;
    private String isPay;
    private String payFrom;
    private String afterRepair;
    private String repairType;
    private String isr;


    public InOutStorageRecordDto(InOutStorageEntry entry){
        this.id = entry.getID().toString();
        this.imeiNo = entry.getImeiNo();
        this.phoneModel = entry.getPhoneModel();
        this.color = entry.getColor();
        this.manufacturer = entry.getManufacturer();
        this.inStorageTime = entry.getInStorageTime();
        this.storageStatus = entry.getStorageStatus();
        this.comment = entry.getComment();
        this.inStorageYear = entry.getInStorageYear();
        this.inStorageMonth = entry.getInStorageMonth();
        this.projectName = entry.getProjectName();
        this.projectDockPeople = entry.getProjectDockPeople();
        this.schoolName = entry.getSchoolName();
        this.accessClass = entry.getAccessClass();
        this.accessObj = entry.getAccessObj();
        this.contactInfo = entry.getContactInfo();
        this.address = entry.getAddress();
        this.deliveryTime = entry.getDeliveryTime();
        this.deliveryMethod = entry.getDeliveryMethod();
        this.excompanyNo = entry.getExcompanyNo();
        this.expressNo = entry.getExpressNo();
        this.parentName = entry.getParentName();
        this.parentMobile = entry.getParentMobile();
        this.parentId = entry.getParentId();
        this.studentName = entry.getStudentName();
        this.studentMobile = entry.getStudentMobile();
        this.studentId = entry.getStudentId();
        this.recycleComment = entry.getRecycleComment();
        this.repairRange = entry.getRepairRange();
        this.repairCost = entry.getRepairCost();
        this.storageRecordStatus = entry.getStorageRecordStatus();
        this.outStorageTime = entry.getOutStorageTime();
        this.outStorageYear = entry.getOutStorageYear();
        this.outStorageMonth = entry.getOutStorageMonth();
        this.needRepairComment = entry.getNeedRepairComment();
        this.isPay = entry.getIsPay();
        this.payFrom = entry.getPayFrom();
        this.afterRepair = entry.getAfterRepair();
        this.repairType = entry.getRepairType();
        this.isr = entry.getIsr();
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDockPeople() {
        return projectDockPeople;
    }

    public void setProjectDockPeople(String projectDockPeople) {
        this.projectDockPeople = projectDockPeople;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAccessClass() {
        return accessClass;
    }

    public void setAccessClass(String accessClass) {
        this.accessClass = accessClass;
    }

    public String getAccessObj() {
        return accessObj;
    }

    public void setAccessObj(String accessObj) {
        this.accessObj = accessObj;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getExcompanyNo() {
        return excompanyNo;
    }

    public void setExcompanyNo(String excompanyNo) {
        this.excompanyNo = excompanyNo;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentMobile() {
        return parentMobile;
    }

    public void setParentMobile(String parentMobile) {
        this.parentMobile = parentMobile;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(String studentMobile) {
        this.studentMobile = studentMobile;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getRecycleComment() {
        return recycleComment;
    }

    public void setRecycleComment(String recycleComment) {
        this.recycleComment = recycleComment;
    }

    public String getRepairRange() {
        return repairRange;
    }

    public void setRepairRange(String repairRange) {
        this.repairRange = repairRange;
    }

    public String getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(String repairCost) {
        this.repairCost = repairCost;
    }

    public String getStorageRecordStatus() {
        return storageRecordStatus;
    }

    public void setStorageRecordStatus(String storageRecordStatus) {
        this.storageRecordStatus = storageRecordStatus;
    }

    public String getOutStorageTime() {
        return outStorageTime;
    }

    public void setOutStorageTime(String outStorageTime) {
        this.outStorageTime = outStorageTime;
    }

    public String getOutStorageYear() {
        return outStorageYear;
    }

    public void setOutStorageYear(String outStorageYear) {
        this.outStorageYear = outStorageYear;
    }

    public String getOutStorageMonth() {
        return outStorageMonth;
    }

    public void setOutStorageMonth(String outStorageMonth) {
        this.outStorageMonth = outStorageMonth;
    }

    public List<String> getNeedRepairComment() {
        return needRepairComment;
    }

    public void setNeedRepairComment(List<String> needRepairComment) {
        this.needRepairComment = needRepairComment;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(String payFrom) {
        this.payFrom = payFrom;
    }

    public String getAfterRepair() {
        return afterRepair;
    }

    public void setAfterRepair(String afterRepair) {
        this.afterRepair = afterRepair;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getIsr() {
        return isr;
    }

    public void setIsr(String isr) {
        this.isr = isr;
    }
}
