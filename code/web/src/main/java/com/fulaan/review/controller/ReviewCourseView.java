package com.fulaan.review.controller;

import com.pojo.review.ReviewEntry;
import com.sys.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Caocui on 2015/8/27.
 */
public class ReviewCourseView {
    private String id;
    private String userId;
    private String userName;
    private String schoolId;
    private String schoolName;
    private String name;
    private String educationTermType;
    private String educationSubject;
    private String textbookVersion;
    private String educationGrade;
    private String chapter;
    private String part;
    private String cover;
    private String video;
    private String fileList;
    private ObjectId educationId;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducationTermType() {
        return educationTermType;
    }

    public void setEducationTermType(String educationTermType) {
        this.educationTermType = educationTermType;
    }

    public String getEducationSubject() {
        return educationSubject;
    }

    public void setEducationSubject(String educationSubject) {
        this.educationSubject = educationSubject;
    }

    public String getTextbookVersion() {
        return textbookVersion;
    }

    public void setTextbookVersion(String textbookVersion) {
        this.textbookVersion = textbookVersion;
    }

    public String getEducationGrade() {
        return educationGrade;
    }

    public void setEducationGrade(String educationGrade) {
        this.educationGrade = educationGrade;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
    public void setEducationId(ObjectId educationId) {
    	this.educationId = educationId;
    }

    public String getFileList() {
        return fileList;
    }

    public void setFileList(String fileList) {
        this.fileList = fileList;
    }

    public ReviewEntry getEntry() {
        List<ObjectId> list = new ArrayList<ObjectId>();
        if (!StringUtils.isEmpty(this.getFileList())) {
            for (String str : this.getFileList().split(",")) {
                list.add(new ObjectId(str));
            }
        }
        return new ReviewEntry(this.getName(),
                this.getEducationTermType(),
                this.getEducationSubject(),
                this.getTextbookVersion(),
                this.getEducationGrade(),
                this.getChapter(),
                this.getPart(),
                System.currentTimeMillis(),
                this.getCover(),
                new ObjectId(this.getVideo()),
                list,
                new ObjectId(this.getUserId()),
                this.getUserName(),
                new ObjectId(this.getSchoolId()),
                this.getSchoolName(),
                this.educationId);
    }
}
