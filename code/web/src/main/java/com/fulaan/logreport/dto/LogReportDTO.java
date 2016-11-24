package com.fulaan.logreport.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2015/4/13.
 */
public class LogReportDTO {

    //学校名称
    private String schoolName;

    //总登录人数
    private int totalPeopleCount;

    //总登录数
    private int totalCount;

    private int otherTotalCount;

    //统计
    private List<CountDTO> countList=new ArrayList<CountDTO>();

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<CountDTO> getCountList() {
        return countList;
    }

    public void setCountList(List<CountDTO> countList) {
        this.countList = countList;
    }

    public int getTotalPeopleCount() {
        return totalPeopleCount;
    }

    public void setTotalPeopleCount(int totalPeopleCount) {
        this.totalPeopleCount = totalPeopleCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getOtherTotalCount() {
        return otherTotalCount;
    }

    public void setOtherTotalCount(int otherTotalCount) {
        this.otherTotalCount = otherTotalCount;
    }
}
