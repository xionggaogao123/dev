package com.fulaan.teachermanage.dto;

import com.pojo.teachermanage.CourseProjectEntry;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/2/29.
 */
public class CourseProjectDTO {

    private String id;

    private String course;

    private String content;

    private String schoolId;

    private int score;

    private int delflg;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDelflg() {
        return delflg;
    }

    public void setDelflg(int delflg) {
        this.delflg = delflg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CourseProjectEntry getCourseProject(ObjectId schoolId) {
        return new CourseProjectEntry(this.course,this.content,schoolId,this.score,0);
    }

    public CourseProjectDTO() {

    }

    public CourseProjectDTO(CourseProjectEntry courseProjectEntry) {
        this.course = courseProjectEntry.getCourse();
        this.content = courseProjectEntry.getContent();
        this.schoolId = courseProjectEntry.getSchoolId().toString();
        this.score = courseProjectEntry.getScore();
        this.id = courseProjectEntry.getID().toString();
    }
}
