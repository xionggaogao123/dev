package com.fulaan.zouban.dto;

import com.pojo.zouban.SubjectConfEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wang_xinxin on 2015/9/23.
 */
public class SubjectConfDTO {
    private String subjectConfId;
    private String subjectId;
    private String subjectName; //学科名
    private String xuankeId;
    private int type;
    private String term;
    private String gradeId;
    private int advanceTime; //等级考课时
    private int simpleTime; //合格考课时
    private int advUserCount;
    private int simUserCount;

    private List<String> classList = new ArrayList<String>();
    private int classNumber;
    private int oddEvenType;

    //以下字段已废弃
    private int advmax;
    private int advmin;
    private int sipmax;
    private int sipmin;
    private int ifFengCeng;
    private String explain;
    private String fenceng;

    //显示字段
    private String classGroupName;

    public SubjectConfDTO() {
    }

    public SubjectConfDTO(SubjectConfEntry subconf, String subjectName) {
        this.advanceTime = subconf.getAdvanceTime();
        this.simpleTime = subconf.getSimpleTime();
        this.xuankeId = subconf.getXuanKeId().toString();
        this.subjectConfId = subconf.getID().toString();
        this.ifFengCeng = subconf.getIfFengCeng();
        fenceng = "";
        if (subconf.getIfFengCeng() == 1) {
            fenceng = "分层";
        }
        if (subconf.getAdvUsers() != null) {
            this.advUserCount = subconf.getAdvUsers().size();
        }
        if (subconf.getSimUsers() != null) {
            this.simUserCount = subconf.getSimUsers().size();
        }
        for (ObjectId item : subconf.getClassInfo()) {
            classList.add(item.toString());
        }
        this.subjectName = subjectName;
        this.subjectId = subconf.getSubjectId().toString();
        this.classNumber = subconf.getClassNumber();
        this.oddEvenType = subconf.getOddEvenType();
    }

    public SubjectConfEntry exportEntry() {
        SubjectConfEntry subjectConfEntry = new SubjectConfEntry();
        if (subjectConfId != null && !subjectConfId.equals("")) {
            subjectConfEntry.setID(new ObjectId(subjectConfId));
        }
        subjectConfEntry.setXuanKeId(new ObjectId(xuankeId));
        subjectConfEntry.setSubjectId(new ObjectId(subjectId));
        subjectConfEntry.setAdvanceTime(advanceTime);
        subjectConfEntry.setSimpleTime(simpleTime);
        subjectConfEntry.setType(type);
        subjectConfEntry.setIfFengCeng(0);
        subjectConfEntry.setClassNumber(classNumber);
        subjectConfEntry.setOddEvenType(oddEvenType);
        if (classList.size() > 0) {
            List<ObjectId> classes = new ArrayList<ObjectId>();
            for (String dto : classList) {
                classes.add(new ObjectId(dto));
            }
            subjectConfEntry.setClassInfo(classes);
        }
        return subjectConfEntry;
    }


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getAdvanceTime() {
        return advanceTime;
    }

    public void setAdvanceTime(int advanceTime) {
        this.advanceTime = advanceTime;
    }

    public int getSimpleTime() {
        return simpleTime;
    }

    public void setSimpleTime(int simpleTime) {
        this.simpleTime = simpleTime;
    }

    public String getXuankeId() {
        return xuankeId;
    }

    public void setXuankeId(String xuankeId) {
        this.xuankeId = xuankeId;
    }

    public int getIfFengCeng() {
        return ifFengCeng;
    }

    public void setIfFengCeng(int ifFengCeng) {
        this.ifFengCeng = ifFengCeng;
    }

    public String getSubjectConfId() {
        return subjectConfId;
    }

    public void setSubjectConfId(String subjectConfId) {
        this.subjectConfId = subjectConfId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public int getAdvUserCount() {
        return advUserCount;
    }

    public void setAdvUserCount(int advUserCount) {
        this.advUserCount = advUserCount;
    }

    public int getSimUserCount() {
        return simUserCount;
    }

    public void setSimUserCount(int simUserCount) {
        this.simUserCount = simUserCount;
    }

    public int getAdvmax() {
        return advmax;
    }

    public void setAdvmax(int advmax) {
        this.advmax = advmax;
    }

    public int getAdvmin() {
        return advmin;
    }

    public void setAdvmin(int advmin) {
        this.advmin = advmin;
    }

    public int getSipmax() {
        return sipmax;
    }

    public void setSipmax(int sipmax) {
        this.sipmax = sipmax;
    }

    public int getSipmin() {
        return sipmin;
    }

    public void setSipmin(int sipmin) {
        this.sipmin = sipmin;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getFenceng() {
        return fenceng;
    }

    public void setFenceng(String fenceng) {
        this.fenceng = fenceng;
    }

    public List<String> getClassList() {
        return classList;
    }

    public void setClassList(List<String> classList) {
        this.classList = classList;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getClassGroupName() {
        return classGroupName;
    }

    public void setClassGroupName(String classGroupName) {
        this.classGroupName = classGroupName;
    }

    public int getOddEvenType() {
        return oddEvenType;
    }

    public void setOddEvenType(int oddEvenType) {
        this.oddEvenType = oddEvenType;
    }
}
