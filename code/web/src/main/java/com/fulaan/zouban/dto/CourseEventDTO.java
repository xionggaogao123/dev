package com.fulaan.zouban.dto;

import com.fulaan.examresult.controller.IdNameDTO;
import com.pojo.app.IdValuePair;
import com.pojo.zouban.CourseEvent;
import com.pojo.zouban.TeacherEvent;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiangm on 2015/9/15.
 */
public class CourseEventDTO  implements Serializable {
    private String id;
    private int xIndex;//星期
    private int yIndex;//第几节课
    private List<String> forbidEvent;//不可排课事务，例：午饭/不排课
    private List<IdNameDTO> groupStudy;//集体调研 id:学科id，name:学科名
    private List<TeacherEventDTO> personEvent;//老师个人事务


    public CourseEventDTO() {
    }

    public CourseEventDTO(CourseEvent courseEvent) {
        this.id = courseEvent.getId() == null ? null :courseEvent.getId().toString();
        this.xIndex = courseEvent.getXIndex();
        this.yIndex = courseEvent.getYIndex();
        this.forbidEvent = courseEvent.getForbidEvent();
        List<ObjectId> subjectIds = courseEvent.getGroupStudy();
        List<IdNameDTO> subjectStrs = new ArrayList<IdNameDTO>();
        if (subjectIds != null && !subjectIds.isEmpty()) {
            for (ObjectId oid : subjectIds) {
                subjectStrs.add(new IdNameDTO(oid.toString(),""));
            }
        }
        this.groupStudy = subjectStrs;
        List<TeacherEvent> teacherEvents = courseEvent.getPersonEvent();
        List<TeacherEventDTO> teacherEventDTOs = new ArrayList<TeacherEventDTO>();
        if (teacherEvents != null && !teacherEvents.isEmpty()) {
            for (TeacherEvent teacherEvent : teacherEvents) {
                teacherEventDTOs.add(new TeacherEventDTO(teacherEvent));
            }
        }
        this.setPersonEvent(teacherEventDTOs);
    }

    public CourseEvent export() {
        CourseEvent courseEvent = new CourseEvent();
        if(this.getId().equals(""))
        {
            setId(new ObjectId().toString());
        }
        courseEvent.setID(new ObjectId(this.getId()));
        courseEvent.setXIndex(this.getxIndex());
        courseEvent.setYIndex(this.getyIndex());
        courseEvent.setForbidEvent(this.getForbidEvent());
        List<IdNameDTO> subjectStrs = this.getGroupStudy();
        List<ObjectId> subjectIds = new ArrayList<ObjectId>();
        if (subjectStrs != null && !subjectStrs.isEmpty()) {
            for (IdNameDTO str : subjectStrs) {
                subjectIds.add(new ObjectId(str.getId().toString()));
            }
        }
        courseEvent.setGroupStudy(subjectIds);

        List<TeacherEventDTO> teacherEventDTOs = this.getPersonEvent();
        List<TeacherEvent> teacherEvents = new ArrayList<TeacherEvent>();
        if (teacherEventDTOs != null && !teacherEventDTOs.isEmpty()) {
            for (TeacherEventDTO teacherEventDTO : teacherEventDTOs) {
                teacherEvents.add(teacherEventDTO.export());
            }
        }
        courseEvent.setPersonEvent(teacherEvents);
        return courseEvent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getxIndex() {
        return xIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    public List<String> getForbidEvent() {
        return forbidEvent;
    }

    public void setForbidEvent(List<String> forbidEvent) {
        this.forbidEvent = forbidEvent;
    }

    public List<IdNameDTO> getGroupStudy() {
        return groupStudy;
    }

    public void setGroupStudy(List<IdNameDTO> groupStudy) {
        this.groupStudy = groupStudy;
    }

    public List<TeacherEventDTO> getPersonEvent() {
        return personEvent;
    }

    public void setPersonEvent(List<TeacherEventDTO> personEvent) {
        this.personEvent = personEvent;
    }


    @Override
    public String toString() {
        return forbidEvent.toString();
    }
}
