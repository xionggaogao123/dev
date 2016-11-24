package com.pojo.teacherevaluation;

import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/4/21.
 */
public class ProportionDTO {

    private String id;
    private String schoolId;
    private String year;
    private String evaluationId;
    private double leadGroupPro;
    private double leaderPro;
    private double groupPro;
    private double huPingPro;
    private int leaderMax;
    private int leaderMin;
    private int groupMax;
    private int groupMin;
    private int huPingMax;
    private int huPingMin;

    public ProportionDTO(){}

    public ProportionDTO(ProportionEntry entry){
        this.id = entry.getID().toString();
        this.schoolId = entry.getSchoolId().toString();
        this.year = entry.getYear();
        this.leadGroupPro = entry.getLeadGroupPro();
        this.leaderPro = entry.getLeaderPro();
        this.groupPro = entry.getGroupPro();
        this.huPingPro = entry.getHuPingPro();
        this.leaderMax = entry.getLeaderMax();
        this.leaderMin = entry.getLeaderMin();
        this.groupMax = entry.getGroupMax();
        this.groupMin = entry.getGroupMin();
        this.huPingMax = entry.getHuPingMax();
        this.huPingMin = entry.getHuPingMin();
    }

    public ProportionEntry exportEntry(){
        ProportionEntry entry = new ProportionEntry(new ObjectId(schoolId), year, new ObjectId(evaluationId), leadGroupPro, leaderPro, groupPro, huPingPro,
                leaderMax, leaderMin, groupMax, groupMin, huPingMax, huPingMin);
        if(id != null){
            entry.setID(new ObjectId(id));
        }
        return entry;
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

    public double getLeadGroupPro() {
        return leadGroupPro;
    }

    public void setLeadGroupPro(double leadGroupPro) {
        this.leadGroupPro = leadGroupPro;
    }

    public double getLeaderPro() {
        return leaderPro;
    }

    public void setLeaderPro(double leaderPro) {
        this.leaderPro = leaderPro;
    }

    public double getGroupPro() {
        return groupPro;
    }

    public void setGroupPro(double groupPro) {
        this.groupPro = groupPro;
    }

    public double getHuPingPro() {
        return huPingPro;
    }

    public void setHuPingPro(double huPingPro) {
        this.huPingPro = huPingPro;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getGroupMax() {
        return groupMax;
    }

    public void setGroupMax(int groupMax) {
        this.groupMax = groupMax;
    }

    public int getGroupMin() {
        return groupMin;
    }

    public void setGroupMin(int groupMin) {
        this.groupMin = groupMin;
    }

    public int getHuPingMax() {
        return huPingMax;
    }

    public void setHuPingMax(int huPingMax) {
        this.huPingMax = huPingMax;
    }

    public int getHuPingMin() {
        return huPingMin;
    }

    public void setHuPingMin(int huPingMin) {
        this.huPingMin = huPingMin;
    }

    public int getLeaderMax() {
        return leaderMax;
    }

    public void setLeaderMax(int leaderMax) {
        this.leaderMax = leaderMax;
    }

    public int getLeaderMin() {
        return leaderMin;
    }

    public void setLeaderMin(int leaderMin) {
        this.leaderMin = leaderMin;
    }
}
