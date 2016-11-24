package com.pojo.teacherevaluation;


import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/21.
 */
public class SettingDTO{
    
    private String id;
    private String schoolId;
    private String year;
    private String rule;
    private long personalTimeBegin;
    private long personalTimeEnd;
    private long groupTimeBegin;
    private long groupTimeEnd;
    private long evaluationTimeBegin;
    private long evaluationTimeEnd;
    private List<GradeSettingDTO> gradeSettingDTOs = new ArrayList<GradeSettingDTO>();

    public SettingDTO(){}

    public SettingDTO(SettingEntry settingEntry){
        id = settingEntry.getID().toString();
        schoolId = settingEntry.getSchoolId().toString();
        year = settingEntry.getYear();
        rule = settingEntry.getRule();
        personalTimeBegin = settingEntry.getPersonalTimeBegin();
        personalTimeEnd = settingEntry.getPersonalTimeEnd();
        groupTimeBegin = settingEntry.getGroupTimeBegin();
        groupTimeEnd = settingEntry.getGroupTimeEnd();
        evaluationTimeBegin = settingEntry.getEvaluationTimeBegin();
        evaluationTimeEnd = settingEntry.getEvaluationTimeEnd();
        List<SettingEntry.GradeSetting> gradeSettings = settingEntry.getGradeSettings();
        if(gradeSettings.size() > 0){
            for(SettingEntry.GradeSetting gradeSetting : gradeSettings) {
                gradeSettingDTOs.add(new GradeSettingDTO(gradeSetting));
            }
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public long getPersonalTimeBegin() {
        return personalTimeBegin;
    }

    public void setPersonalTimeBegin(long personalTimeBegin) {
        this.personalTimeBegin = personalTimeBegin;
    }

    public long getPersonalTimeEnd() {
        return personalTimeEnd;
    }

    public void setPersonalTimeEnd(long personalTimeEnd) {
        this.personalTimeEnd = personalTimeEnd;
    }

    public long getGroupTimeBegin() {
        return groupTimeBegin;
    }

    public void setGroupTimeBegin(long groupTimeBegin) {
        this.groupTimeBegin = groupTimeBegin;
    }

    public long getGroupTimeEnd() {
        return groupTimeEnd;
    }

    public void setGroupTimeEnd(long groupTimeEnd) {
        this.groupTimeEnd = groupTimeEnd;
    }

    public long getEvaluationTimeBegin() {
        return evaluationTimeBegin;
    }

    public void setEvaluationTimeBegin(long evaluationTimeBegin) {
        this.evaluationTimeBegin = evaluationTimeBegin;
    }

    public long getEvaluationTimeEnd() {
        return evaluationTimeEnd;
    }

    public void setEvaluationTimeEnd(long evaluationTimeEnd) {
        this.evaluationTimeEnd = evaluationTimeEnd;
    }

    public List<GradeSettingDTO> getGradeSettingDTOs() {
        return gradeSettingDTOs;
    }

    public void setGradeSettingDTOs(List<GradeSettingDTO> gradeSettingDTOs) {
        this.gradeSettingDTOs = gradeSettingDTOs;
    }

    public static class GradeSettingDTO{
        private String id;
        private String name;
        private double begin;
        private double end;

        public GradeSettingDTO(){}

        public GradeSettingDTO(SettingEntry.GradeSetting gradeSetting){
            id = gradeSetting.getId().toString();
            name = gradeSetting.getName();
            begin = gradeSetting.getBegin();
            end = gradeSetting.getEnd();
        }

        public SettingEntry.GradeSetting exportEntry(){
            SettingEntry.GradeSetting gradeSetting = new SettingEntry.GradeSetting();
            gradeSetting.setId(id==null ? new ObjectId() : new ObjectId(id));
            gradeSetting.setName(name);
            gradeSetting.setBegin(begin);
            gradeSetting.setEnd(end);
            return gradeSetting;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getBegin() {
            return begin;
        }

        public void setBegin(double begin) {
            this.begin = begin;
        }

        public double getEnd() {
            return end;
        }

        public void setEnd(double end) {
            this.end = end;
        }
    }


}
