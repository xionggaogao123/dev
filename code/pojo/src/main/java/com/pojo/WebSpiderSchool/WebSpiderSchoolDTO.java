package com.pojo.WebSpiderSchool;

/**
 * _id
 * sn:schoolName
 * st:schoolType
 * pr:province
 * ci:city
 * co:country
 * icon:schoolIcon
 * tn:tyeacherNum;
 * cn:classNum
 * Created by qiangm on 2016/3/16.
 */
public class WebSpiderSchoolDTO{
    private static final long serialVersionUID = -1085751543276800705L;

    private String schoolId;

    private String schoolName;
    private int test;//0未开通 1自行开通 2试点校

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}
