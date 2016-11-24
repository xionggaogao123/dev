package com.pojo.teacherevaluation;

import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/9/12.
 */
public class EvaluationTeacherDTO {

    private String id;

    private String schoolId;

    private String teacherId;

    private String teacherName;

    private String statement;

    private String evidence;

    private int eviState;//实证资料状态 1：已录入  0：未录入

    public EvaluationTeacherDTO() {
    }

    public EvaluationTeacherDTO(EvaluationTeacherEntry entry) {
        this.id = entry.getID() == null ? "" : entry.getID().toString();
        this.schoolId = entry.getSchoolId() == null ? "" : entry.getSchoolId().toString();
        this.teacherId = entry.getTeacherId().toString();
        this.statement = entry.getStatement();
        this.evidence = entry.getEvidence();
        this.eviState = entry.getEvidence().equals("") ? 0 : 1;
    }

    public EvaluationTeacherDTO(String id, String schoolId, String teacherId, String evidence, String statement) {
        this.evidence = evidence;
        this.id = id;
        this.schoolId = schoolId;
        this.statement = statement;
        this.teacherId = teacherId;
    }

    public EvaluationTeacherEntry exportEntry(){
        EvaluationTeacherEntry entry = new EvaluationTeacherEntry(new ObjectId(schoolId), new ObjectId(teacherId), statement, evidence);
        if(!"".equals(id)){
            entry.setID(new ObjectId(id));
        }
        return entry;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
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

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getEviState() {
        return eviState;
    }

    public void setEviState(int eviState) {
        this.eviState = eviState;
    }
}
