package com.pojo.teacherevaluation;


import com.pojo.app.NameValuePair;
import com.pojo.app.NameValuePairDTO;
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
    private long evaluationTimeBegin;
    private long evaluationTimeEnd;
    private int mode;
    private List<GradeSettingDTO> gradeSettingDTOs = new ArrayList<GradeSettingDTO>();
    private List<NameValuePairDTO> modeGrades = new ArrayList<NameValuePairDTO>();

    public SettingDTO(){}

    public SettingDTO(SettingEntry settingEntry){
        this.id = settingEntry.getID().toString();
        this.schoolId = settingEntry.getSchoolId().toString();
        this.year = settingEntry.getYear();
        this.rule = settingEntry.getRule();
        this.evaluationTimeBegin = settingEntry.getEvaluationTimeBegin();
        this.evaluationTimeEnd = settingEntry.getEvaluationTimeEnd();
        this.mode = settingEntry.getMode();
        List<SettingEntry.GradeSetting> gradeSettings = settingEntry.getGradeSettings();
        if(gradeSettings.size() > 0){
            for(SettingEntry.GradeSetting gradeSetting : gradeSettings) {
                this.gradeSettingDTOs.add(new GradeSettingDTO(gradeSetting));
            }
        }
        List<NameValuePair> nameValuePairs = settingEntry.getModeGrades();
        if(nameValuePairs.size() > 0){
            for(NameValuePair nameValuePair : nameValuePairs){
                this.modeGrades.add(new NameValuePairDTO(nameValuePair));
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public List<NameValuePairDTO> getModeGrades() {
        return modeGrades;
    }

    public void setModeGrades(List<NameValuePairDTO> modeGrades) {
        this.modeGrades = modeGrades;
    }

    /**
     *
     */
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
