package com.fulaan.backstage.dto;

import com.pojo.backstage.SchoolControlTimeEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/27 16:30
 * @Description:
 */
public class SchoolControlTimeDTO {
    private String id;
    private int type;
    private int week;
    private String dateFrom;
    private String dateTo;
    private String schoolTimeFrom;
    private String schoolTimeTo;
    private String bedTimeFrom;
    private String bedTimeTo;
    private String schoolId;
    private String holidayName;

    //用来展示的时间段
    private String inSchoolTime;
    private List<String> outSchoolTime;
    private String bedTime;
    private String holidayDate;

    public SchoolControlTimeDTO(SchoolControlTimeEntry entry){
        this.id = entry.getID().toString();
        this.type = entry.getType();
        this.week = entry.getWeek();
        this.dateFrom = entry.getDateFrom();
        this.dateTo = entry.getDateTo();
        this.schoolTimeFrom = entry.getSchoolTimeFrom();
        this.schoolTimeTo = entry.getSchoolTimeTo();
        this.bedTimeFrom = entry.getBedTimeFrom();
        this.bedTimeTo = entry.getBedTimeTo();
        this.schoolId = entry.getSchoolId();
        this.holidayName = entry.getHolidayName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getSchoolTimeFrom() {
        return schoolTimeFrom;
    }

    public void setSchoolTimeFrom(String schoolTimeFrom) {
        this.schoolTimeFrom = schoolTimeFrom;
    }

    public String getSchoolTimeTo() {
        return schoolTimeTo;
    }

    public void setSchoolTimeTo(String schoolTimeTo) {
        this.schoolTimeTo = schoolTimeTo;
    }

    public String getBedTimeFrom() {
        return bedTimeFrom;
    }

    public void setBedTimeFrom(String bedTimeFrom) {
        this.bedTimeFrom = bedTimeFrom;
    }

    public String getBedTimeTo() {
        return bedTimeTo;
    }

    public void setBedTimeTo(String bedTimeTo) {
        this.bedTimeTo = bedTimeTo;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getInSchoolTime() {
        String inSchoolTime = "";
        if (!"".equals(this.schoolTimeTo) && !"".equals(this.schoolTimeFrom)){
            inSchoolTime = this.schoolTimeFrom+"-"+this.schoolTimeTo;
        }
        return inSchoolTime;
    }

//    public void setInSchoolTime(String inSchoolTime) {
//        this.inSchoolTime = inSchoolTime;
//    }

    public List<String> getOutSchoolTime() {
        List<String> outSchoolTime = new ArrayList<String>();
        if (!"".equals(this.schoolTimeTo) && !"".equals(this.schoolTimeFrom)){
            //上学期间
            if ( this.bedTimeTo != "" && !this.bedTimeTo.equals(this.schoolTimeFrom)){
                String outSchoolTimeTwo = this.bedTimeTo+"-"+this.schoolTimeFrom;
                outSchoolTime.add(outSchoolTimeTwo);
            }
            if (this.schoolTimeTo != "" && !this.schoolTimeTo.equals(this.bedTimeFrom)){
                String outSchoolTimeOne = this.schoolTimeTo+"-"+this.bedTimeFrom;
                outSchoolTime.add(outSchoolTimeOne);
            }
        }else {
            //不上学期间
            String outSchoolTimeThree = this.bedTimeTo+"-"+this.bedTimeFrom;
            outSchoolTime.add(outSchoolTimeThree);
        }

        return outSchoolTime;
    }

//    public void setOutSchoolTime(String outSchoolTime) {
//        this.outSchoolTime = outSchoolTime;
//    }

    public String getBedTime() {
        String bedTime = "";
        bedTime = this.bedTimeFrom+"- 24:00"+" "+"24:00 -"+this.bedTimeTo;
//        if (!"".equals(this.bedTimeTo) && "0".equals(this.bedTimeTo.charAt(0) + "")) {
//            bedTime = this.bedTimeFrom+"-次日"+this.bedTimeTo;
//        }else {
//            bedTime = this.bedTimeFrom+"-"+this.bedTimeTo;
//        }
        return bedTime;
    }

//    public void setBedTime(String bedTime) {
//        this.bedTime = bedTime;
//    }

    public String getHolidayDate() {
        String holidayDate;
        if (this.dateFrom.equals(this.dateTo)){
            holidayDate = this.dateFrom;
        }else if ("".equals(this.dateFrom)){
            holidayDate = this.dateFrom;
        }else {
            holidayDate = this.dateFrom+"至"+this.dateTo;
        }
        return holidayDate;
    }
}
