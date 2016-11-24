package com.fulaan.quality.dto;

import com.pojo.Quality.TeachProjectEntry;
import org.bson.types.ObjectId;

/**
 * Created by wang_xinxin on 2016/11/11.
 */
public class TeacherProjectDTO {

    private String id;

    private String projectName;

    private int count;

    private String quality;

    private String score;

    private int type;

    private String hrefUrl;

    public TeacherProjectDTO() {

    }

    public TeacherProjectDTO(TeachProjectEntry entry,ObjectId teacherId,int planCnt,int lessonCnt,String term) {
//        this.id = entry.getID().toString();
        this.projectName = entry.getProjectName();
        this.type = 2;
        this.count = entry.getCount();
        if (entry.getProjectName().equals("教学计划")) {
            this.type = 1;
            this.hrefUrl = "/teach/planUser.do?userId="+teacherId.toString() + "&term=" + term;
            this.count = planCnt;
        } else if (entry.getProjectName().equals("备课")) {
            this.type = 1;
            this.hrefUrl = "/teacher/course/"+teacherId.toString();
            this.count = lessonCnt;
        }

        this.quality = entry.getQuality();
        this.score = entry.getScore();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }
}
