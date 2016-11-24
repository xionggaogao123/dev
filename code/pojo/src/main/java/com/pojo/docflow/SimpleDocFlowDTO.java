package com.pojo.docflow;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 本dto结构简单，用于列表显示
 * Created by qiangm on 2015/8/20.
 */
public class SimpleDocFlowDTO {
    private String id;
    private String title;
    private String userId;
    private String departmentId;
    private String departmentName;
    private String time;
    private List<ObjectId> publishList;
    private List<ObjectId> unreadList;

    private List<DocCheckDTO> checkDTOList;

    private Integer state;//
    private String stateDesc;//状态描述
    private Integer tag;//标记已读未读，0未读，1已读

    private String checkPerson;
    private String checkDepartment;
    private String checkTime;//审阅日期
    private String checkId;//最新审阅状态Id
    private Integer educationPub;//1教育局，0非教育局

    public SimpleDocFlowDTO(DocFlowEntry doc) {
        this.id = doc.getID().toString();
        this.title = doc.getTitle();
        this.userId = doc.getUserId().toString();
        this.departmentId = doc.getDepartmentId().toString();
        this.publishList = doc.getPublishList();
        this.unreadList = doc.getUnreadList();
        this.checkDTOList = new ArrayList<DocCheckDTO>();
        for (DocCheckEntry d : doc.getDocCheckList()) {
            DocCheckDTO dc = new DocCheckDTO(d);
            this.checkDTOList.add(dc);
        }
        this.title = doc.getTitle();
        this.checkId = doc.getCheckId().toString();
        this.state = doc.getState();
        this.educationPub=0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<ObjectId> getPublishList() {
        return publishList;
    }

    public void setPublishList(List<ObjectId> publishList) {
        this.publishList = publishList;
    }

    public List<ObjectId> getUnreadList() {
        return unreadList;
    }

    public void setUnreadList(List<ObjectId> unreadList) {
        this.unreadList = unreadList;
    }

    public List<DocCheckDTO> getCheckDTOList() {
        return checkDTOList;
    }

    public void setCheckDTOList(List<DocCheckDTO> checkDTOList) {
        this.checkDTOList = checkDTOList;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String state) {
        this.stateDesc = state;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getCheckDepartment() {
        return checkDepartment;
    }

    public void setCheckDepartment(String checkDepartment) {
        this.checkDepartment = checkDepartment;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getEducationPub() {
        return educationPub;
    }

    public void setEducationPub(Integer educationPub) {
        this.educationPub = educationPub;
    }
}
