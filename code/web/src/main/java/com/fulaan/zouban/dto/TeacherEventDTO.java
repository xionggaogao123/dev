package com.fulaan.zouban.dto;

import com.pojo.zouban.TeacherEvent;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * Created by qiangm on 2015/9/15.
 */
public class TeacherEventDTO  implements Serializable {
    private String id;
    private String subjectId; //学科id
    private String subjectName; //学科名
    private String teacherId; //老师id
    private String teacherName; //老师名
    private String event; //事务名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public TeacherEventDTO() {
    }

    public TeacherEventDTO(TeacherEvent teacherEvent) {
        this.id = teacherEvent.getId().toString();
        this.subjectId = teacherEvent.getSubjectId().toString();
        this.teacherId = teacherEvent.getTeacherId().toString();
        this.event = teacherEvent.getEvent();
    }

    public TeacherEvent export() {
        TeacherEvent teacherEvent = new TeacherEvent();
        if(this.getId().equals(""))
            setId(new ObjectId().toString());
        teacherEvent.setId(new ObjectId(this.getId()));
        teacherEvent.setSubjectId(new ObjectId(this.getSubjectId()));
        teacherEvent.setTeacherId(new ObjectId(this.getTeacherId()));
        teacherEvent.setEvent(this.getEvent());
        return teacherEvent;
    }
    @Override
    public String toString() {
        return "ActivityView]";
    }
}
