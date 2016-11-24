package com.fulaan.zouban.dto;

import com.pojo.utils.MongoUtils;
import com.pojo.zouban.SchoolSubjectGroupEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fl on 2016/7/18.
 */
public class SchoolSubjectGroupDTO {

    private String id;
    private String schoolId;
    private String year;
    private String gradeId;
    private List<SubjectGroupDTO> subjectGroupDTOs = new ArrayList<SubjectGroupDTO>();

    public SchoolSubjectGroupDTO(){}

    public SchoolSubjectGroupDTO(SchoolSubjectGroupEntry entry){
        id = entry.getID().toString();
        schoolId = entry.getSchoolId().toString();
        year = entry.getYear();
        gradeId = entry.getGradeId().toString();
        List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = entry.getSubjectGroups();
        for(SchoolSubjectGroupEntry.SubjectGroup subjectGroup : subjectGroups){
            subjectGroupDTOs.add(new SubjectGroupDTO(subjectGroup));
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

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public List<SubjectGroupDTO> getSubjectGroupDTOs() {
        return subjectGroupDTOs;
    }

    public void setSubjectGroupDTOs(List<SubjectGroupDTO> subjectGroupDTOs) {
        this.subjectGroupDTOs = subjectGroupDTOs;
    }

    public static class SubjectGroupDTO{
        private String id;
        private Boolean isPublic;
        private String groupName;
        private List<String> advSubjects = new ArrayList<String>();
        private List<String> simSubjects = new ArrayList<String>();
        private List<Integer> chooseState = new ArrayList<Integer>();//方便页面展示

        public SubjectGroupDTO(){}

        public SubjectGroupDTO(SchoolSubjectGroupEntry.SubjectGroup subjectGroup){
            id = subjectGroup.getId().toString();
            isPublic = subjectGroup.getIsPublic();
            advSubjects = MongoUtils.convertToStringList(subjectGroup.getAdvSubjects());
            simSubjects = MongoUtils.convertToStringList(subjectGroup.getSimSubjects());
            groupName = subjectGroup.getName();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getIsPublic() {
            return isPublic;
        }

        public void setIsPublic(Boolean isPublic) {
            this.isPublic = isPublic;
        }

        public List<String> getAdvSubjects() {
            return advSubjects;
        }

        public void setAdvSubjects(List<String> advSubjects) {
            this.advSubjects = advSubjects;
        }

        public List<String> getSimSubjects() {
            return simSubjects;
        }

        public void setSimSubjects(List<String> simSubjects) {
            this.simSubjects = simSubjects;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public List<Integer> getChooseState() {
            return chooseState;
        }

        public void setChooseState(List<Integer> chooseState) {
            this.chooseState = chooseState;
        }

        //
    }



    //
}
