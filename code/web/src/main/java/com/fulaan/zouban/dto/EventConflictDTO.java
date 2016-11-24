package com.fulaan.zouban.dto;


/**
 * Created by wangkaidong on 2016/10/10.
 */
public class EventConflictDTO {
    private String eventName;
    private String teacherName;
    private String courseName;
    private String timeStr;
    private String className;


    final String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    final String[] sections = {"第一节", "第二节", "第三节", "第四节", "第五节", "第六节", "第七节", "第八节", "第九节", "第十节", "第十一节", "第十二节", "第十三节"};




    public EventConflictDTO() {}

    public EventConflictDTO(String eventName, String teacherName, String courseName, int x, int y, String className) {
        this.eventName = eventName;
        this.teacherName = teacherName;
        this.courseName = courseName;
        this.timeStr = days[x - 1] + sections[y - 1];
        this.className = className;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
