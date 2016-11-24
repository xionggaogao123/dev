package com.pojo.teacherevaluation;

import org.bson.types.ObjectId;

/**
 * Created by fl on 2016/4/21.
 */
public class ProportionDTO {

    private String id;
    private String schoolId;
    private String year;
    private double leadGroupPro;
    private double leaderPro;
    private double groupPro;
    private double huPingPro;
    private double liangHuaPro;

    public ProportionDTO(){}

    public ProportionDTO(ProportionEntry entry){
        id = entry.getID().toString();
        schoolId = entry.getSchoolId().toString();
        year = entry.getYear();
        leadGroupPro = entry.getLeadGroupPro();
        leaderPro = entry.getLeaderPro();
        groupPro = entry.getGroupPro();
        huPingPro = entry.getHuPingPro();
        liangHuaPro = entry.getLiangHuaPro();
    }

    public ProportionEntry exportEntry(){
        ProportionEntry entry = new ProportionEntry(new ObjectId(schoolId), year, leadGroupPro, leaderPro, groupPro, huPingPro, liangHuaPro);
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

    public double getLiangHuaPro() {
        return liangHuaPro;
    }

    public void setLiangHuaPro(double liangHuaPro) {
        this.liangHuaPro = liangHuaPro;
    }
}
