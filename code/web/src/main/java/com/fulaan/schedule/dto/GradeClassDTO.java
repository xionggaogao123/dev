package com.fulaan.schedule.dto;

import java.util.List;

/**
 * Created by qiangm on 2016/3/1.
 */
public class GradeClassDTO {
    private String gradeId;
    private String gradeName;
    private List<ClassDTO> classDTOs;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public List<ClassDTO> getClassDTOs() {
        return classDTOs;
    }

    public void setClassDTOs(List<ClassDTO> classDTOs) {
        this.classDTOs = classDTOs;
    }

    public class ClassDTO{
        private String classId;
        private String className;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
}
