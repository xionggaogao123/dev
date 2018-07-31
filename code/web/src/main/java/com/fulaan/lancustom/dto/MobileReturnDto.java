package com.fulaan.lancustom.dto;

import org.bson.types.ObjectId;

import com.pojo.lancustom.MobileReturnEntry;

/**
 * 
 * <简述>退换货dto
 * <详细描述>
 * @author   Brant
 * @version  $Id$
 * @since
 * @see
 */
public class MobileReturnDto {
    
    private String id;

    //手机型号
    private String mobileVersion;
    //售后类型     1.退货 2.维修3.换货
    private int serviceVersion;
    //手机问题
    private String problem;
    //支付宝账号
    private String aliPayNum;
    //小孩姓名
    private String name;
    //小孩学校
    private String schoole;
    //小孩班级
    private String classRoom;
    //联系电话
    private String contactNum;
    //地址
    private String address;
    
    //物流公司
    private String excompanyNo;
    //运单编号
    private String expressNo;
    
  //状态 0未受理 1已受理
    private Integer state;
    //
    private String stateStr;
    
    public MobileReturnEntry buildAddEntry(ObjectId uid) {
        return new MobileReturnEntry(uid, mobileVersion, serviceVersion, problem, aliPayNum, name, schoole, classRoom, contactNum, address, "","");
    }
    public MobileReturnDto(MobileReturnEntry m) {
        if (m != null) {
            this.id = m.getID() == null ? new ObjectId().toString():m.getID().toString();
            this.mobileVersion = m.getMobileVersion();
            this.serviceVersion = m.getServiceVersion();
            this.problem = m.getProblem();
            this.aliPayNum = m.getAliPayNum();
            this.name = m.getName();
            this.schoole = m.getSchoole();
            this.classRoom = m.getClassRoom();
            this.contactNum = m.getContactNum();
            this.address = m.getAddress();
            this.excompanyNo = m.getExcompanyNo();
            this.expressNo = m.getExpressNo();
            this.state = m.getSta();
            if (m.getSta() == 0) {
                this.stateStr = "未受理";
            } else if (m.getSta() == 1) {
                this.stateStr = "已受理";
            } else {
                this.stateStr = "已发货";
            }
        } else {
            new CommonQuestionDto();
        }
    }
    
    public String getMobileVersion() {
        return mobileVersion;
    }
    public void setMobileVersion(String mobileVersion) {
        this.mobileVersion = mobileVersion;
    }
    public int getServiceVersion() {
        return serviceVersion;
    }
    public void setServiceVersion(int serviceVersion) {
        this.serviceVersion = serviceVersion;
    }
    public String getProblem() {
        return problem;
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }
    public String getAliPayNum() {
        return aliPayNum;
    }
    public void setAliPayNum(String aliPayNum) {
        this.aliPayNum = aliPayNum;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSchoole() {
        return schoole;
    }
    public void setSchoole(String schoole) {
        this.schoole = schoole;
    }
    public String getClassRoom() {
        return classRoom;
    }
    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
    public String getContactNum() {
        return contactNum;
    }
    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getStateStr() {
        return stateStr;
    }
    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    
}
