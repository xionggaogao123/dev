package com.fulaan.zouban.dto;

import com.pojo.zouban.TimetableConfEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/25.
 */
public class TimetableConfDTO {
    private String id;
    private String term;
    private String gradeId;
    private List<Integer> days = new ArrayList<Integer>();
    private int classCount;
    private List<String> classTime = new ArrayList<String>();
    private List<EventDTO> eventList = new ArrayList<EventDTO>();
    private List<ClassEventDTO> classEventList = new ArrayList<ClassEventDTO>();
    private List<PointDTO> pointDTOs = new ArrayList<PointDTO>();
    private int lock;


    public TimetableConfDTO(){}

    public TimetableConfDTO(TimetableConfEntry entry) {
        this.id = entry.getID().toString();
        this.term = entry.getTerm();
        this.gradeId = entry.getGradeId().toString();
        this.classCount = entry.getClassCount();
        this.days.addAll(entry.getDays());
        this.classTime.addAll(entry.getClassTime());

        for (TimetableConfEntry.Event event : entry.getEvent()) {
            this.eventList.add(new EventDTO(event));
        }
        for (TimetableConfEntry.ClassEvent event : entry.getClassEvent()) {
            this.classEventList.add(new ClassEventDTO(event));
        }
        this.lock = entry.getLock();
    }

    public TimetableConfEntry exportEntry() {
        TimetableConfEntry entry = new TimetableConfEntry();
        if(StringUtils.isNotBlank(id)){
            entry.setID(new ObjectId(id));
        }
        entry.setTerm(term);
        entry.setGradeId(new ObjectId(gradeId));
        entry.setDays(days);
        entry.setClassCount(classCount);
        entry.setClassTime(classTime);
        entry.setLock(lock);

        List<TimetableConfEntry.Event> events = new ArrayList<TimetableConfEntry.Event>();
        for (EventDTO eventDTO : eventList) {
            events.add(eventDTO.exportEntry());
        }
        entry.setEvent(events);

        List<TimetableConfEntry.ClassEvent> classEvents = new ArrayList<TimetableConfEntry.ClassEvent>();
        for (ClassEventDTO classEventDTO : classEventList) {
            classEvents.add(classEventDTO.exportEntry());
        }
        entry.setClassEvent(classEvents);

        return entry;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public List<String> getClassTime() {
        return classTime;
    }

    public void setClassTime(List<String> classTime) {
        this.classTime = classTime;
    }

    public List<EventDTO> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventDTO> eventList) {
        this.eventList = eventList;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public List<ClassEventDTO> getClassEventList() {
        return classEventList;
    }

    public void setClassEventList(List<ClassEventDTO> classEventList) {
        this.classEventList = classEventList;
    }

    public List<PointDTO> getPointDTOs() {
        return pointDTOs;
    }

    public void setPointDTOs(List<PointDTO> pointDTOs) {
        this.pointDTOs = pointDTOs;
    }
}
