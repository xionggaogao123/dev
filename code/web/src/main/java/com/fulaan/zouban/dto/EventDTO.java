package com.fulaan.zouban.dto;

import com.fulaan.examresult.controller.IdNameDTO;
import com.pojo.zouban.IdNamePair;
import com.pojo.zouban.PointEntry;
import com.pojo.zouban.TimetableConfEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/25.
 */
public class EventDTO {
    private String id;
    private String name;
    private List<PointJson> pointList = new ArrayList<PointJson>();
    private List<IdNameDTO> teacherList = new ArrayList<IdNameDTO>();

    public EventDTO(){}

    public EventDTO(TimetableConfEntry.Event event) {
        this.id = event.getID().toString();
        this.name = event.getName();
        for (PointEntry pointEntry : event.getPointList()) {
            this.pointList.add(new PointJson(pointEntry.getX(), pointEntry.getY()));
        }
        for (IdNamePair inp : event.getTeacherList()) {
            this.teacherList.add(new IdNameDTO(inp.getId().toString(), inp.getName()));
        }
    }

    public TimetableConfEntry.Event exportEntry() {
        TimetableConfEntry.Event event = new TimetableConfEntry.Event();
        if (!StringUtils.isEmpty(id)) {
            event.setID(new ObjectId(id));
        } else {
            event.setID(new ObjectId());
        }
        event.setName(name);

        List<PointEntry> pointEntryList = new ArrayList<PointEntry>();
        for (PointJson pointJson : pointList) {
            pointEntryList.add(new PointEntry(pointJson.getX(), pointJson.getY()));
        }
        event.setPointList(pointEntryList);

        List<IdNamePair> idNamePairList = new ArrayList<IdNamePair>();
        for (IdNameDTO idNameDTO : teacherList) {
            idNamePairList.add(new IdNamePair(new ObjectId(idNameDTO.getId()), idNameDTO.getName()));
        }
        event.setTeacherList(idNamePairList);

        return event;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PointJson> getPointList() {
        return pointList;
    }

    public void setPointList(List<PointJson> pointList) {
        this.pointList = pointList;
    }

    public List<IdNameDTO> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<IdNameDTO> teacherList) {
        this.teacherList = teacherList;
    }
}
