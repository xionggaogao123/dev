package com.pojo.docflow;

import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/8/17.
 */
public class DocCheckDTO {
    private String id;
    private String userId;
    private String userName;
    private String departmentName;
    private String departmentId;
    private int opinion;
    private String remark;
    private String time;
    private String opinionDesc;

    public DocCheckDTO(String id, String userId, String departmentId, int opinion, String remark) {
        super();
        this.id = id;
        this.userId = userId;
        this.departmentId = departmentId;
        this.opinion = opinion;
        this.remark = remark;
    }

    public DocCheckDTO(DocCheckEntry doc) {
        super();
        this.id = doc.getId().toString();
        this.userId = doc.getUserId().toString();
        this.departmentId = doc.getDepartmentId().toString();
        this.opinion = doc.getOPinion();
        this.opinionDesc = CheckStateEnum.getCheckState(opinion);
        this.remark = doc.getRemark();
    }

    public DocCheckEntry export() {
        DocCheckEntry docCheckEntry = new DocCheckEntry();
        docCheckEntry.setId(new ObjectId(this.getId()));
        docCheckEntry.setUserId(new ObjectId(this.getUserId()));
        docCheckEntry.setDepartmentId(new ObjectId(this.getDepartmentId()));
        docCheckEntry.setOpinion(this.getOpinion());
        docCheckEntry.setRemark(this.getRemark());
        return docCheckEntry;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getOpinion() {
        return opinion;
    }

    public void setOpinion(int opinion) {
        this.opinion = opinion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpinionDesc() {
        return opinionDesc;
    }

    public void setOpinionDesc(int opinion) {
        this.opinionDesc = CheckStateEnum.getCheckState(opinion);
    }
}
